/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import com.ning.compress.BufferRecycler;
/*     */ import com.ning.compress.lzf.ChunkDecoder;
/*     */ import com.ning.compress.lzf.util.ChunkDecoderFactory;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LzfDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static enum State
/*     */   {
/*  44 */     INIT_BLOCK, 
/*  45 */     INIT_ORIGINAL_LENGTH, 
/*  46 */     DECOMPRESS_DATA, 
/*  47 */     CORRUPTED;
/*     */     
/*     */     private State() {} }
/*  50 */   private State currentState = State.INIT_BLOCK;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final short MAGIC_NUMBER = 23126;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ChunkDecoder decoder;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private BufferRecycler recycler;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int chunkLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int originalLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isCompressed;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LzfDecoder()
/*     */   {
/*  90 */     this(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LzfDecoder(boolean safeInstance)
/*     */   {
/* 105 */     this.decoder = (safeInstance ? ChunkDecoderFactory.safeInstance() : ChunkDecoderFactory.optimalInstance());
/*     */     
/* 107 */     this.recycler = BufferRecycler.instance();
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*     */     try {
/* 113 */       switch (this.currentState) {
/*     */       case INIT_BLOCK: 
/* 115 */         if (in.readableBytes() >= 5)
/*     */         {
/*     */ 
/* 118 */           int magic = in.readUnsignedShort();
/* 119 */           if (magic != 23126) {
/* 120 */             throw new DecompressionException("unexpected block identifier");
/*     */           }
/*     */           
/* 123 */           int type = in.readByte();
/* 124 */           switch (type) {
/*     */           case 0: 
/* 126 */             this.isCompressed = false;
/* 127 */             this.currentState = State.DECOMPRESS_DATA;
/* 128 */             break;
/*     */           case 1: 
/* 130 */             this.isCompressed = true;
/* 131 */             this.currentState = State.INIT_ORIGINAL_LENGTH;
/* 132 */             break;
/*     */           default: 
/* 134 */             throw new DecompressionException(String.format("unknown type of chunk: %d (expected: %d or %d)", new Object[] {
/*     */             
/* 136 */               Integer.valueOf(type), Integer.valueOf(0), Integer.valueOf(1) }));
/*     */           }
/* 138 */           this.chunkLength = in.readUnsignedShort();
/*     */           
/* 140 */           if (type != 1)
/*     */             break;
/*     */         }
/*     */         break;
/*     */       case INIT_ORIGINAL_LENGTH: 
/* 145 */         if (in.readableBytes() >= 2)
/*     */         {
/*     */ 
/* 148 */           this.originalLength = in.readUnsignedShort();
/*     */           
/* 150 */           this.currentState = State.DECOMPRESS_DATA;
/*     */         }
/*     */         break;
/* 153 */       case DECOMPRESS_DATA:  int chunkLength = this.chunkLength;
/* 154 */         if (in.readableBytes() >= chunkLength)
/*     */         {
/*     */ 
/* 157 */           int originalLength = this.originalLength;
/*     */           
/* 159 */           if (this.isCompressed) {
/* 160 */             int idx = in.readerIndex();
/*     */             int inPos;
/*     */             byte[] inputArray;
/*     */             int inPos;
/* 164 */             if (in.hasArray()) {
/* 165 */               byte[] inputArray = in.array();
/* 166 */               inPos = in.arrayOffset() + idx;
/*     */             } else {
/* 168 */               inputArray = this.recycler.allocInputBuffer(chunkLength);
/* 169 */               in.getBytes(idx, inputArray, 0, chunkLength);
/* 170 */               inPos = 0;
/*     */             }
/*     */             
/* 173 */             ByteBuf uncompressed = ctx.alloc().heapBuffer(originalLength, originalLength);
/* 174 */             byte[] outputArray = uncompressed.array();
/* 175 */             int outPos = uncompressed.arrayOffset() + uncompressed.writerIndex();
/*     */             
/* 177 */             boolean success = false;
/*     */             try {
/* 179 */               this.decoder.decodeChunk(inputArray, inPos, outputArray, outPos, outPos + originalLength);
/* 180 */               uncompressed.writerIndex(uncompressed.writerIndex() + originalLength);
/* 181 */               out.add(uncompressed);
/* 182 */               in.skipBytes(chunkLength);
/* 183 */               success = true;
/*     */             } finally {
/* 185 */               if (!success) {
/* 186 */                 uncompressed.release();
/*     */               }
/*     */             }
/*     */             
/* 190 */             if (!in.hasArray()) {
/* 191 */               this.recycler.releaseInputBuffer(inputArray);
/*     */             }
/* 193 */           } else if (chunkLength > 0) {
/* 194 */             out.add(in.readRetainedSlice(chunkLength));
/*     */           }
/*     */           
/* 197 */           this.currentState = State.INIT_BLOCK; }
/* 198 */         break;
/*     */       case CORRUPTED: 
/* 200 */         in.skipBytes(in.readableBytes());
/* 201 */         break;
/*     */       default: 
/* 203 */         throw new IllegalStateException();
/*     */       }
/*     */     } catch (Exception e) {
/* 206 */       this.currentState = State.CORRUPTED;
/* 207 */       this.decoder = null;
/* 208 */       this.recycler = null;
/* 209 */       throw e;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\compression\LzfDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */