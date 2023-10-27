/*     */ package io.netty.handler.codec.memcache.binary;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.handler.codec.memcache.AbstractMemcacheObjectDecoder;
/*     */ import io.netty.handler.codec.memcache.DefaultLastMemcacheContent;
/*     */ import io.netty.handler.codec.memcache.DefaultMemcacheContent;
/*     */ import io.netty.handler.codec.memcache.LastMemcacheContent;
/*     */ import io.netty.handler.codec.memcache.MemcacheContent;
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
/*     */ public abstract class AbstractBinaryMemcacheDecoder<M extends BinaryMemcacheMessage>
/*     */   extends AbstractMemcacheObjectDecoder
/*     */ {
/*     */   public static final int DEFAULT_MAX_CHUNK_SIZE = 8192;
/*     */   private final int chunkSize;
/*     */   private M currentMessage;
/*     */   private int alreadyReadChunkSize;
/*  47 */   private State state = State.READ_HEADER;
/*     */   
/*     */ 
/*     */ 
/*     */   protected AbstractBinaryMemcacheDecoder()
/*     */   {
/*  53 */     this(8192);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractBinaryMemcacheDecoder(int chunkSize)
/*     */   {
/*  62 */     if (chunkSize < 0) {
/*  63 */       throw new IllegalArgumentException("chunkSize must be a positive integer: " + chunkSize);
/*     */     }
/*     */     
/*  66 */     this.chunkSize = chunkSize;
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*     */   {
/*  71 */     switch (this.state) {
/*     */     case READ_HEADER:  try {
/*  73 */         if (in.readableBytes() < 24) {
/*  74 */           return;
/*     */         }
/*  76 */         resetDecoder();
/*     */         
/*  78 */         this.currentMessage = decodeHeader(in);
/*  79 */         this.state = State.READ_EXTRAS;
/*     */       } catch (Exception e) {
/*  81 */         resetDecoder();
/*  82 */         out.add(invalidMessage(e));
/*  83 */         return;
/*     */       }
/*     */     case READ_EXTRAS:  try {
/*  86 */         byte extrasLength = this.currentMessage.extrasLength();
/*  87 */         if (extrasLength > 0) {
/*  88 */           if (in.readableBytes() < extrasLength) {
/*  89 */             return;
/*     */           }
/*     */           
/*  92 */           this.currentMessage.setExtras(in.readRetainedSlice(extrasLength));
/*     */         }
/*     */         
/*  95 */         this.state = State.READ_KEY;
/*     */       } catch (Exception e) {
/*  97 */         resetDecoder();
/*  98 */         out.add(invalidMessage(e));
/*  99 */         return;
/*     */       }
/*     */     case READ_KEY:  try {
/* 102 */         short keyLength = this.currentMessage.keyLength();
/* 103 */         if (keyLength > 0) {
/* 104 */           if (in.readableBytes() < keyLength) {
/* 105 */             return;
/*     */           }
/*     */           
/* 108 */           this.currentMessage.setKey(in.readRetainedSlice(keyLength));
/*     */         }
/* 110 */         out.add(this.currentMessage.retain());
/* 111 */         this.state = State.READ_CONTENT;
/*     */       } catch (Exception e) {
/* 113 */         resetDecoder();
/* 114 */         out.add(invalidMessage(e));
/* 115 */         return;
/*     */       }
/*     */     case READ_CONTENT: 
/*     */       try
/*     */       {
/* 120 */         int valueLength = this.currentMessage.totalBodyLength() - this.currentMessage.keyLength() - this.currentMessage.extrasLength();
/* 121 */         int toRead = in.readableBytes();
/* 122 */         if (valueLength > 0) {
/* 123 */           if (toRead == 0) {
/* 124 */             return;
/*     */           }
/*     */           
/* 127 */           if (toRead > this.chunkSize) {
/* 128 */             toRead = this.chunkSize;
/*     */           }
/*     */           
/* 131 */           int remainingLength = valueLength - this.alreadyReadChunkSize;
/* 132 */           if (toRead > remainingLength) {
/* 133 */             toRead = remainingLength;
/*     */           }
/*     */           
/* 136 */           ByteBuf chunkBuffer = in.readRetainedSlice(toRead);
/*     */           MemcacheContent chunk;
/*     */           MemcacheContent chunk;
/* 139 */           if (this.alreadyReadChunkSize += toRead >= valueLength) {
/* 140 */             chunk = new DefaultLastMemcacheContent(chunkBuffer);
/*     */           } else {
/* 142 */             chunk = new DefaultMemcacheContent(chunkBuffer);
/*     */           }
/*     */           
/* 145 */           out.add(chunk);
/* 146 */           if (this.alreadyReadChunkSize < valueLength) {
/* 147 */             return;
/*     */           }
/*     */         } else {
/* 150 */           out.add(LastMemcacheContent.EMPTY_LAST_CONTENT);
/*     */         }
/*     */         
/* 153 */         resetDecoder();
/* 154 */         this.state = State.READ_HEADER;
/* 155 */         return;
/*     */       } catch (Exception e) {
/* 157 */         resetDecoder();
/* 158 */         out.add(invalidChunk(e));
/* 159 */         return;
/*     */       }
/*     */     case BAD_MESSAGE: 
/* 162 */       in.skipBytes(actualReadableBytes());
/* 163 */       return;
/*     */     }
/* 165 */     throw new Error("Unknown state reached: " + this.state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private M invalidMessage(Exception cause)
/*     */   {
/* 176 */     this.state = State.BAD_MESSAGE;
/* 177 */     M message = buildInvalidMessage();
/* 178 */     message.setDecoderResult(DecoderResult.failure(cause));
/* 179 */     return message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private MemcacheContent invalidChunk(Exception cause)
/*     */   {
/* 189 */     this.state = State.BAD_MESSAGE;
/* 190 */     MemcacheContent chunk = new DefaultLastMemcacheContent(Unpooled.EMPTY_BUFFER);
/* 191 */     chunk.setDecoderResult(DecoderResult.failure(cause));
/* 192 */     return chunk;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void channelInactive(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 203 */     super.channelInactive(ctx);
/*     */     
/* 205 */     resetDecoder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void resetDecoder()
/*     */   {
/* 212 */     if (this.currentMessage != null) {
/* 213 */       this.currentMessage.release();
/* 214 */       this.currentMessage = null;
/*     */     }
/* 216 */     this.alreadyReadChunkSize = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract M decodeHeader(ByteBuf paramByteBuf);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract M buildInvalidMessage();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static enum State
/*     */   {
/* 245 */     READ_HEADER, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 250 */     READ_EXTRAS, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 255 */     READ_KEY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 260 */     READ_CONTENT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 265 */     BAD_MESSAGE;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\memcache\binary\AbstractBinaryMemcacheDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */