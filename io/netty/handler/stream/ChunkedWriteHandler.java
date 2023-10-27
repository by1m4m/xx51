/*     */ package io.netty.handler.stream;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelDuplexHandler;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelProgressivePromise;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.EventExecutor;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
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
/*     */ public class ChunkedWriteHandler
/*     */   extends ChannelDuplexHandler
/*     */ {
/*  71 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ChunkedWriteHandler.class);
/*     */   
/*  73 */   private final Queue<PendingWrite> queue = new ArrayDeque();
/*     */   
/*     */   private volatile ChannelHandlerContext ctx;
/*     */   
/*     */   private PendingWrite currentWrite;
/*     */   
/*     */ 
/*     */   public ChunkedWriteHandler() {}
/*     */   
/*     */   @Deprecated
/*     */   public ChunkedWriteHandler(int maxPendingWrites)
/*     */   {
/*  85 */     if (maxPendingWrites <= 0) {
/*  86 */       throw new IllegalArgumentException("maxPendingWrites: " + maxPendingWrites + " (expected: > 0)");
/*     */     }
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/*  93 */     this.ctx = ctx;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resumeTransfer()
/*     */   {
/* 100 */     final ChannelHandlerContext ctx = this.ctx;
/* 101 */     if (ctx == null) {
/* 102 */       return;
/*     */     }
/* 104 */     if (ctx.executor().inEventLoop()) {
/* 105 */       resumeTransfer0(ctx);
/*     */     }
/*     */     else {
/* 108 */       ctx.executor().execute(new Runnable()
/*     */       {
/*     */         public void run()
/*     */         {
/* 112 */           ChunkedWriteHandler.this.resumeTransfer0(ctx);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private void resumeTransfer0(ChannelHandlerContext ctx) {
/*     */     try {
/* 120 */       doFlush(ctx);
/*     */     } catch (Exception e) {
/* 122 */       if (logger.isWarnEnabled()) {
/* 123 */         logger.warn("Unexpected exception while sending chunks.", e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 130 */     this.queue.add(new PendingWrite(msg, promise));
/*     */   }
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 135 */     doFlush(ctx);
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 140 */     doFlush(ctx);
/* 141 */     ctx.fireChannelInactive();
/*     */   }
/*     */   
/*     */   public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 146 */     if (ctx.channel().isWritable())
/*     */     {
/* 148 */       doFlush(ctx);
/*     */     }
/* 150 */     ctx.fireChannelWritabilityChanged();
/*     */   }
/*     */   
/*     */   private void discard(Throwable cause) {
/*     */     for (;;) {
/* 155 */       PendingWrite currentWrite = this.currentWrite;
/*     */       
/* 157 */       if (this.currentWrite == null) {
/* 158 */         currentWrite = (PendingWrite)this.queue.poll();
/*     */       } else {
/* 160 */         this.currentWrite = null;
/*     */       }
/*     */       
/* 163 */       if (currentWrite == null) {
/*     */         break;
/*     */       }
/* 166 */       Object message = currentWrite.msg;
/* 167 */       if ((message instanceof ChunkedInput)) {
/* 168 */         ChunkedInput<?> in = (ChunkedInput)message;
/*     */         try {
/* 170 */           if (!in.isEndOfInput()) {
/* 171 */             if (cause == null) {
/* 172 */               cause = new ClosedChannelException();
/*     */             }
/* 174 */             currentWrite.fail(cause);
/*     */           } else {
/* 176 */             currentWrite.success(in.length());
/*     */           }
/* 178 */           closeInput(in);
/*     */         } catch (Exception e) {
/* 180 */           currentWrite.fail(e);
/* 181 */           if (logger.isWarnEnabled()) {
/* 182 */             logger.warn(ChunkedInput.class.getSimpleName() + ".isEndOfInput() failed", e);
/*     */           }
/* 184 */           closeInput(in);
/*     */         }
/*     */       } else {
/* 187 */         if (cause == null) {
/* 188 */           cause = new ClosedChannelException();
/*     */         }
/* 190 */         currentWrite.fail(cause);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void doFlush(ChannelHandlerContext ctx) {
/* 196 */     final Channel channel = ctx.channel();
/* 197 */     if (!channel.isActive()) {
/* 198 */       discard(null);
/* 199 */       return;
/*     */     }
/*     */     
/* 202 */     boolean requiresFlush = true;
/* 203 */     ByteBufAllocator allocator = ctx.alloc();
/* 204 */     while (channel.isWritable()) {
/* 205 */       if (this.currentWrite == null) {
/* 206 */         this.currentWrite = ((PendingWrite)this.queue.poll());
/*     */       }
/*     */       
/* 209 */       if (this.currentWrite == null) {
/*     */         break;
/*     */       }
/* 212 */       final PendingWrite currentWrite = this.currentWrite;
/* 213 */       final Object pendingMessage = currentWrite.msg;
/*     */       
/* 215 */       if ((pendingMessage instanceof ChunkedInput)) {
/* 216 */         final ChunkedInput<?> chunks = (ChunkedInput)pendingMessage;
/*     */         
/*     */ 
/* 219 */         Object message = null;
/*     */         try {
/* 221 */           message = chunks.readChunk(allocator);
/* 222 */           boolean endOfInput = chunks.isEndOfInput();
/*     */           boolean suspend;
/* 224 */           if (message == null)
/*     */           {
/* 226 */             suspend = !endOfInput;
/*     */           } else
/* 228 */             suspend = false;
/*     */         } catch (Throwable t) {
/*     */           boolean suspend;
/* 231 */           this.currentWrite = null;
/*     */           
/* 233 */           if (message != null) {
/* 234 */             ReferenceCountUtil.release(message);
/*     */           }
/*     */           
/* 237 */           currentWrite.fail(t);
/* 238 */           closeInput(chunks);
/* 239 */           break; }
/*     */         boolean suspend;
/*     */         boolean endOfInput;
/* 242 */         if (suspend) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 249 */         if (message == null)
/*     */         {
/*     */ 
/* 252 */           message = Unpooled.EMPTY_BUFFER;
/*     */         }
/*     */         
/* 255 */         ChannelFuture f = ctx.write(message);
/* 256 */         if (endOfInput) {
/* 257 */           this.currentWrite = null;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 264 */           f.addListener(new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 267 */               currentWrite.progress(chunks.progress(), chunks.length());
/* 268 */               currentWrite.success(chunks.length());
/* 269 */               ChunkedWriteHandler.closeInput(chunks);
/*     */             }
/*     */           });
/* 272 */         } else if (channel.isWritable()) {
/* 273 */           f.addListener(new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 276 */               if (!future.isSuccess()) {
/* 277 */                 ChunkedWriteHandler.closeInput((ChunkedInput)pendingMessage);
/* 278 */                 currentWrite.fail(future.cause());
/*     */               } else {
/* 280 */                 currentWrite.progress(chunks.progress(), chunks.length());
/*     */               }
/*     */             }
/*     */           });
/*     */         } else {
/* 285 */           f.addListener(new ChannelFutureListener()
/*     */           {
/*     */             public void operationComplete(ChannelFuture future) throws Exception {
/* 288 */               if (!future.isSuccess()) {
/* 289 */                 ChunkedWriteHandler.closeInput((ChunkedInput)pendingMessage);
/* 290 */                 currentWrite.fail(future.cause());
/*     */               } else {
/* 292 */                 currentWrite.progress(chunks.progress(), chunks.length());
/* 293 */                 if (channel.isWritable()) {
/* 294 */                   ChunkedWriteHandler.this.resumeTransfer();
/*     */                 }
/*     */               }
/*     */             }
/*     */           });
/*     */         }
/*     */         
/* 301 */         ctx.flush();
/* 302 */         requiresFlush = false;
/*     */       } else {
/* 304 */         this.currentWrite = null;
/* 305 */         ctx.write(pendingMessage, currentWrite.promise);
/* 306 */         requiresFlush = true;
/*     */       }
/*     */       
/* 309 */       if (!channel.isActive()) {
/* 310 */         discard(new ClosedChannelException());
/* 311 */         break;
/*     */       }
/*     */     }
/*     */     
/* 315 */     if (requiresFlush) {
/* 316 */       ctx.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void closeInput(ChunkedInput<?> chunks) {
/*     */     try {
/* 322 */       chunks.close();
/*     */     } catch (Throwable t) {
/* 324 */       if (logger.isWarnEnabled()) {
/* 325 */         logger.warn("Failed to close a chunked input.", t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class PendingWrite {
/*     */     final Object msg;
/*     */     final ChannelPromise promise;
/*     */     
/*     */     PendingWrite(Object msg, ChannelPromise promise) {
/* 335 */       this.msg = msg;
/* 336 */       this.promise = promise;
/*     */     }
/*     */     
/*     */     void fail(Throwable cause) {
/* 340 */       ReferenceCountUtil.release(this.msg);
/* 341 */       this.promise.tryFailure(cause);
/*     */     }
/*     */     
/*     */     void success(long total) {
/* 345 */       if (this.promise.isDone())
/*     */       {
/* 347 */         return;
/*     */       }
/* 349 */       progress(total, total);
/* 350 */       this.promise.trySuccess();
/*     */     }
/*     */     
/*     */     void progress(long progress, long total) {
/* 354 */       if ((this.promise instanceof ChannelProgressivePromise)) {
/* 355 */         ((ChannelProgressivePromise)this.promise).tryProgress(progress, total);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\stream\ChunkedWriteHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */