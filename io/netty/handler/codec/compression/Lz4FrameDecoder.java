/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import java.util.List;
/*     */ import java.util.zip.Checksum;
/*     */ import net.jpountz.lz4.LZ4Exception;
/*     */ import net.jpountz.lz4.LZ4Factory;
/*     */ import net.jpountz.lz4.LZ4FastDecompressor;
/*     */ import net.jpountz.xxhash.StreamingXXHash32;
/*     */ import net.jpountz.xxhash.XXHashFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Lz4FrameDecoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static enum State
/*     */   {
/*  52 */     INIT_BLOCK, 
/*  53 */     DECOMPRESS_DATA, 
/*  54 */     FINISHED, 
/*  55 */     CORRUPTED;
/*     */     
/*     */     private State() {} }
/*  58 */   private State currentState = State.INIT_BLOCK;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private LZ4FastDecompressor decompressor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ByteBufChecksum checksum;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int blockType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int compressedLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int decompressedLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int currentChecksum;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Lz4FrameDecoder()
/*     */   {
/* 100 */     this(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Lz4FrameDecoder(boolean validateChecksums)
/*     */   {
/* 111 */     this(LZ4Factory.fastestInstance(), validateChecksums);
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
/*     */ 
/*     */   public Lz4FrameDecoder(LZ4Factory factory, boolean validateChecksums)
/*     */   {
/* 127 */     this(factory, validateChecksums ? 
/* 128 */       XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum() : null);
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
/*     */   public Lz4FrameDecoder(LZ4Factory factory, Checksum checksum)
/*     */   {
/* 142 */     if (factory == null) {
/* 143 */       throw new NullPointerException("factory");
/*     */     }
/* 145 */     this.decompressor = factory.fastDecompressor();
/* 146 */     this.checksum = (checksum == null ? null : ByteBufChecksum.wrapChecksum(checksum));
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*     */     try {
/* 152 */       switch (this.currentState) {
/*     */       case INIT_BLOCK: 
/* 154 */         if (in.readableBytes() >= 21)
/*     */         {
/*     */ 
/* 157 */           long magic = in.readLong();
/* 158 */           if (magic != 5501767354678207339L) {
/* 159 */             throw new DecompressionException("unexpected block identifier");
/*     */           }
/*     */           
/* 162 */           int token = in.readByte();
/* 163 */           int compressionLevel = (token & 0xF) + 10;
/* 164 */           int blockType = token & 0xF0;
/*     */           
/* 166 */           int compressedLength = Integer.reverseBytes(in.readInt());
/* 167 */           if ((compressedLength < 0) || (compressedLength > 33554432)) {
/* 168 */             throw new DecompressionException(String.format("invalid compressedLength: %d (expected: 0-%d)", new Object[] {
/*     */             
/* 170 */               Integer.valueOf(compressedLength), Integer.valueOf(33554432) }));
/*     */           }
/*     */           
/* 173 */           int decompressedLength = Integer.reverseBytes(in.readInt());
/* 174 */           int maxDecompressedLength = 1 << compressionLevel;
/* 175 */           if ((decompressedLength < 0) || (decompressedLength > maxDecompressedLength)) {
/* 176 */             throw new DecompressionException(String.format("invalid decompressedLength: %d (expected: 0-%d)", new Object[] {
/*     */             
/* 178 */               Integer.valueOf(decompressedLength), Integer.valueOf(maxDecompressedLength) }));
/*     */           }
/* 180 */           if (((decompressedLength == 0) && (compressedLength != 0)) || ((decompressedLength != 0) && (compressedLength == 0)) || ((blockType == 16) && (decompressedLength != compressedLength)))
/*     */           {
/*     */ 
/* 183 */             throw new DecompressionException(String.format("stream corrupted: compressedLength(%d) and decompressedLength(%d) mismatch", new Object[] {
/*     */             
/* 185 */               Integer.valueOf(compressedLength), Integer.valueOf(decompressedLength) }));
/*     */           }
/*     */           
/* 188 */           int currentChecksum = Integer.reverseBytes(in.readInt());
/* 189 */           if ((decompressedLength == 0) && (compressedLength == 0)) {
/* 190 */             if (currentChecksum != 0) {
/* 191 */               throw new DecompressionException("stream corrupted: checksum error");
/*     */             }
/* 193 */             this.currentState = State.FINISHED;
/* 194 */             this.decompressor = null;
/* 195 */             this.checksum = null;
/*     */           }
/*     */           else
/*     */           {
/* 199 */             this.blockType = blockType;
/* 200 */             this.compressedLength = compressedLength;
/* 201 */             this.decompressedLength = decompressedLength;
/* 202 */             this.currentChecksum = currentChecksum;
/*     */             
/* 204 */             this.currentState = State.DECOMPRESS_DATA;
/*     */           }
/*     */         }
/*     */         break; case DECOMPRESS_DATA:  int blockType = this.blockType;
/* 208 */         int compressedLength = this.compressedLength;
/* 209 */         int decompressedLength = this.decompressedLength;
/* 210 */         int currentChecksum = this.currentChecksum;
/*     */         
/* 212 */         if (in.readableBytes() >= compressedLength)
/*     */         {
/*     */ 
/*     */ 
/* 216 */           ByteBufChecksum checksum = this.checksum;
/* 217 */           ByteBuf uncompressed = null;
/*     */           try
/*     */           {
/* 220 */             switch (blockType)
/*     */             {
/*     */ 
/*     */             case 16: 
/* 224 */               uncompressed = in.retainedSlice(in.readerIndex(), decompressedLength);
/* 225 */               break;
/*     */             case 32: 
/* 227 */               uncompressed = ctx.alloc().buffer(decompressedLength, decompressedLength);
/*     */               
/* 229 */               this.decompressor.decompress(CompressionUtil.safeNioBuffer(in), uncompressed
/* 230 */                 .internalNioBuffer(uncompressed.writerIndex(), decompressedLength));
/*     */               
/* 232 */               uncompressed.writerIndex(uncompressed.writerIndex() + decompressedLength);
/* 233 */               break;
/*     */             default: 
/* 235 */               throw new DecompressionException(String.format("unexpected blockType: %d (expected: %d or %d)", new Object[] {
/*     */               
/* 237 */                 Integer.valueOf(blockType), Integer.valueOf(16), Integer.valueOf(32) }));
/*     */             }
/*     */             
/* 240 */             in.skipBytes(compressedLength);
/*     */             
/* 242 */             if (checksum != null) {
/* 243 */               CompressionUtil.checkChecksum(checksum, uncompressed, currentChecksum);
/*     */             }
/* 245 */             out.add(uncompressed);
/* 246 */             uncompressed = null;
/* 247 */             this.currentState = State.INIT_BLOCK;
/*     */           } catch (LZ4Exception e) {
/* 249 */             throw new DecompressionException(e);
/*     */           } finally {
/* 251 */             if (uncompressed != null)
/* 252 */               uncompressed.release();
/*     */           }
/*     */         }
/* 255 */         break;
/*     */       case FINISHED: 
/*     */       case CORRUPTED: 
/* 258 */         in.skipBytes(in.readableBytes());
/* 259 */         break;
/*     */       default: 
/* 261 */         throw new IllegalStateException();
/*     */       }
/*     */     } catch (Exception e) {
/* 264 */       this.currentState = State.CORRUPTED;
/* 265 */       throw e;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 274 */     return this.currentState == State.FINISHED;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\compression\Lz4FrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */