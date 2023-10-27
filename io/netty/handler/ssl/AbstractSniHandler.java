/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelOutboundHandler;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
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
/*     */ public abstract class AbstractSniHandler<T>
/*     */   extends ByteToMessageDecoder
/*     */   implements ChannelOutboundHandler
/*     */ {
/*     */   private static final int MAX_SSL_RECORDS = 4;
/*  49 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractSniHandler.class);
/*     */   private boolean handshakeFailed;
/*     */   private boolean suppressRead;
/*     */   private boolean readPending;
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  57 */     if ((!this.suppressRead) && (!this.handshakeFailed)) {
/*  58 */       int writerIndex = in.writerIndex();
/*     */       try
/*     */       {
/*  61 */         for (int i = 0; i < 4; i++) {
/*  62 */           int readerIndex = in.readerIndex();
/*  63 */           int readableBytes = writerIndex - readerIndex;
/*  64 */           if (readableBytes < 5)
/*     */           {
/*  66 */             return;
/*     */           }
/*     */           
/*  69 */           int command = in.getUnsignedByte(readerIndex);
/*     */           
/*     */ 
/*  72 */           switch (command) {
/*     */           case 20: 
/*     */           case 21: 
/*  75 */             int len = SslUtils.getEncryptedPacketLength(in, readerIndex);
/*     */             
/*     */ 
/*  78 */             if (len == -2) {
/*  79 */               this.handshakeFailed = true;
/*     */               
/*  81 */               NotSslRecordException e = new NotSslRecordException("not an SSL/TLS record: " + ByteBufUtil.hexDump(in));
/*  82 */               in.skipBytes(in.readableBytes());
/*  83 */               ctx.fireUserEventTriggered(new SniCompletionEvent(e));
/*  84 */               SslUtils.handleHandshakeFailure(ctx, e, true);
/*  85 */               throw e;
/*     */             }
/*  87 */             if ((len == -1) || (writerIndex - readerIndex - 5 < len))
/*     */             {
/*     */ 
/*  90 */               return;
/*     */             }
/*     */             
/*  93 */             in.skipBytes(len);
/*  94 */             break;
/*     */           case 22: 
/*  96 */             int majorVersion = in.getUnsignedByte(readerIndex + 1);
/*     */             
/*     */ 
/*  99 */             if (majorVersion == 3) {
/* 100 */               int packetLength = in.getUnsignedShort(readerIndex + 3) + 5;
/*     */               
/*     */ 
/* 103 */               if (readableBytes < packetLength)
/*     */               {
/* 105 */                 return;
/*     */               }
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
/* 128 */               int endOffset = readerIndex + packetLength;
/* 129 */               int offset = readerIndex + 43;
/*     */               
/* 131 */               if (endOffset - offset < 6) {
/*     */                 break;
/*     */               }
/*     */               
/* 135 */               int sessionIdLength = in.getUnsignedByte(offset);
/* 136 */               offset += sessionIdLength + 1;
/*     */               
/* 138 */               int cipherSuitesLength = in.getUnsignedShort(offset);
/* 139 */               offset += cipherSuitesLength + 2;
/*     */               
/* 141 */               int compressionMethodLength = in.getUnsignedByte(offset);
/* 142 */               offset += compressionMethodLength + 1;
/*     */               
/* 144 */               int extensionsLength = in.getUnsignedShort(offset);
/* 145 */               offset += 2;
/* 146 */               int extensionsLimit = offset + extensionsLength;
/*     */               
/* 148 */               if (extensionsLimit > endOffset) {
/*     */                 break;
/*     */               }
/*     */               
/*     */ 
/*     */ 
/* 154 */               while (extensionsLimit - offset >= 4)
/*     */               {
/*     */ 
/*     */ 
/* 158 */                 int extensionType = in.getUnsignedShort(offset);
/* 159 */                 offset += 2;
/*     */                 
/* 161 */                 int extensionLength = in.getUnsignedShort(offset);
/* 162 */                 offset += 2;
/*     */                 
/* 164 */                 if (extensionsLimit - offset < extensionLength) {
/*     */                   break;
/*     */                 }
/*     */                 
/*     */ 
/*     */ 
/* 170 */                 if (extensionType == 0) {
/* 171 */                   offset += 2;
/* 172 */                   if (extensionsLimit - offset < 3) {
/*     */                     break;
/*     */                   }
/*     */                   
/* 176 */                   int serverNameType = in.getUnsignedByte(offset);
/* 177 */                   offset++;
/*     */                   
/* 179 */                   if (serverNameType != 0) break;
/* 180 */                   int serverNameLength = in.getUnsignedShort(offset);
/* 181 */                   offset += 2;
/*     */                   
/* 183 */                   if (extensionsLimit - offset < serverNameLength) {
/*     */                     break;
/*     */                   }
/*     */                   
/* 187 */                   String hostname = in.toString(offset, serverNameLength, CharsetUtil.US_ASCII);
/*     */                   
/*     */                   try
/*     */                   {
/* 191 */                     select(ctx, hostname.toLowerCase(Locale.US));
/*     */                   } catch (Throwable t) {
/* 193 */                     PlatformDependent.throwException(t);
/*     */                   }
/* 195 */                   return;
/*     */                 }
/*     */                 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 202 */                 offset += extensionLength;
/*     */               }
/*     */             }
/*     */             break;
/*     */           }
/*     */           
/* 208 */           break;
/*     */         }
/*     */       }
/*     */       catch (NotSslRecordException e)
/*     */       {
/* 213 */         throw e;
/*     */       }
/*     */       catch (Exception e) {
/* 216 */         if (logger.isDebugEnabled()) {
/* 217 */           logger.debug("Unexpected client hello packet: " + ByteBufUtil.hexDump(in), e);
/*     */         }
/*     */       }
/*     */       
/* 221 */       select(ctx, null);
/*     */     }
/*     */   }
/*     */   
/*     */   private void select(final ChannelHandlerContext ctx, final String hostname) throws Exception {
/* 226 */     Future<T> future = lookup(ctx, hostname);
/* 227 */     if (future.isDone()) {
/* 228 */       fireSniCompletionEvent(ctx, hostname, future);
/* 229 */       onLookupComplete(ctx, hostname, future);
/*     */     } else {
/* 231 */       this.suppressRead = true;
/* 232 */       future.addListener(new FutureListener()
/*     */       {
/*     */         /* Error */
/*     */         public void operationComplete(Future<T> future)
/*     */           throws Exception
/*     */         {
/*     */           // Byte code:
/*     */           //   0: aload_0
/*     */           //   1: getfield 1	io/netty/handler/ssl/AbstractSniHandler$1:this$0	Lio/netty/handler/ssl/AbstractSniHandler;
/*     */           //   4: iconst_0
/*     */           //   5: invokestatic 5	io/netty/handler/ssl/AbstractSniHandler:access$002	(Lio/netty/handler/ssl/AbstractSniHandler;Z)Z
/*     */           //   8: pop
/*     */           //   9: aload_0
/*     */           //   10: getfield 1	io/netty/handler/ssl/AbstractSniHandler$1:this$0	Lio/netty/handler/ssl/AbstractSniHandler;
/*     */           //   13: aload_0
/*     */           //   14: getfield 2	io/netty/handler/ssl/AbstractSniHandler$1:val$ctx	Lio/netty/channel/ChannelHandlerContext;
/*     */           //   17: aload_0
/*     */           //   18: getfield 3	io/netty/handler/ssl/AbstractSniHandler$1:val$hostname	Ljava/lang/String;
/*     */           //   21: aload_1
/*     */           //   22: invokestatic 6	io/netty/handler/ssl/AbstractSniHandler:access$100	(Lio/netty/handler/ssl/AbstractSniHandler;Lio/netty/channel/ChannelHandlerContext;Ljava/lang/String;Lio/netty/util/concurrent/Future;)V
/*     */           //   25: aload_0
/*     */           //   26: getfield 1	io/netty/handler/ssl/AbstractSniHandler$1:this$0	Lio/netty/handler/ssl/AbstractSniHandler;
/*     */           //   29: aload_0
/*     */           //   30: getfield 2	io/netty/handler/ssl/AbstractSniHandler$1:val$ctx	Lio/netty/channel/ChannelHandlerContext;
/*     */           //   33: aload_0
/*     */           //   34: getfield 3	io/netty/handler/ssl/AbstractSniHandler$1:val$hostname	Ljava/lang/String;
/*     */           //   37: aload_1
/*     */           //   38: invokevirtual 7	io/netty/handler/ssl/AbstractSniHandler:onLookupComplete	(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/String;Lio/netty/util/concurrent/Future;)V
/*     */           //   41: goto +52 -> 93
/*     */           //   44: astore_2
/*     */           //   45: aload_0
/*     */           //   46: getfield 2	io/netty/handler/ssl/AbstractSniHandler$1:val$ctx	Lio/netty/channel/ChannelHandlerContext;
/*     */           //   49: aload_2
/*     */           //   50: invokeinterface 9 2 0
/*     */           //   55: pop
/*     */           //   56: goto +37 -> 93
/*     */           //   59: astore_2
/*     */           //   60: aload_0
/*     */           //   61: getfield 2	io/netty/handler/ssl/AbstractSniHandler$1:val$ctx	Lio/netty/channel/ChannelHandlerContext;
/*     */           //   64: new 8	io/netty/handler/codec/DecoderException
/*     */           //   67: dup
/*     */           //   68: aload_2
/*     */           //   69: invokespecial 11	io/netty/handler/codec/DecoderException:<init>	(Ljava/lang/Throwable;)V
/*     */           //   72: invokeinterface 9 2 0
/*     */           //   77: pop
/*     */           //   78: goto +15 -> 93
/*     */           //   81: astore_2
/*     */           //   82: aload_0
/*     */           //   83: getfield 2	io/netty/handler/ssl/AbstractSniHandler$1:val$ctx	Lio/netty/channel/ChannelHandlerContext;
/*     */           //   86: aload_2
/*     */           //   87: invokeinterface 9 2 0
/*     */           //   92: pop
/*     */           //   93: aload_0
/*     */           //   94: getfield 1	io/netty/handler/ssl/AbstractSniHandler$1:this$0	Lio/netty/handler/ssl/AbstractSniHandler;
/*     */           //   97: invokestatic 13	io/netty/handler/ssl/AbstractSniHandler:access$200	(Lio/netty/handler/ssl/AbstractSniHandler;)Z
/*     */           //   100: ifeq +57 -> 157
/*     */           //   103: aload_0
/*     */           //   104: getfield 1	io/netty/handler/ssl/AbstractSniHandler$1:this$0	Lio/netty/handler/ssl/AbstractSniHandler;
/*     */           //   107: iconst_0
/*     */           //   108: invokestatic 14	io/netty/handler/ssl/AbstractSniHandler:access$202	(Lio/netty/handler/ssl/AbstractSniHandler;Z)Z
/*     */           //   111: pop
/*     */           //   112: aload_0
/*     */           //   113: getfield 2	io/netty/handler/ssl/AbstractSniHandler$1:val$ctx	Lio/netty/channel/ChannelHandlerContext;
/*     */           //   116: invokeinterface 15 1 0
/*     */           //   121: pop
/*     */           //   122: goto +35 -> 157
/*     */           //   125: astore_3
/*     */           //   126: aload_0
/*     */           //   127: getfield 1	io/netty/handler/ssl/AbstractSniHandler$1:this$0	Lio/netty/handler/ssl/AbstractSniHandler;
/*     */           //   130: invokestatic 13	io/netty/handler/ssl/AbstractSniHandler:access$200	(Lio/netty/handler/ssl/AbstractSniHandler;)Z
/*     */           //   133: ifeq +22 -> 155
/*     */           //   136: aload_0
/*     */           //   137: getfield 1	io/netty/handler/ssl/AbstractSniHandler$1:this$0	Lio/netty/handler/ssl/AbstractSniHandler;
/*     */           //   140: iconst_0
/*     */           //   141: invokestatic 14	io/netty/handler/ssl/AbstractSniHandler:access$202	(Lio/netty/handler/ssl/AbstractSniHandler;Z)Z
/*     */           //   144: pop
/*     */           //   145: aload_0
/*     */           //   146: getfield 2	io/netty/handler/ssl/AbstractSniHandler$1:val$ctx	Lio/netty/channel/ChannelHandlerContext;
/*     */           //   149: invokeinterface 15 1 0
/*     */           //   154: pop
/*     */           //   155: aload_3
/*     */           //   156: athrow
/*     */           //   157: return
/*     */           // Line number table:
/*     */           //   Java source line #236	-> byte code offset #0
/*     */           //   Java source line #238	-> byte code offset #9
/*     */           //   Java source line #239	-> byte code offset #25
/*     */           //   Java source line #246	-> byte code offset #41
/*     */           //   Java source line #240	-> byte code offset #44
/*     */           //   Java source line #241	-> byte code offset #45
/*     */           //   Java source line #246	-> byte code offset #56
/*     */           //   Java source line #242	-> byte code offset #59
/*     */           //   Java source line #243	-> byte code offset #60
/*     */           //   Java source line #246	-> byte code offset #78
/*     */           //   Java source line #244	-> byte code offset #81
/*     */           //   Java source line #245	-> byte code offset #82
/*     */           //   Java source line #248	-> byte code offset #93
/*     */           //   Java source line #249	-> byte code offset #103
/*     */           //   Java source line #250	-> byte code offset #112
/*     */           //   Java source line #248	-> byte code offset #125
/*     */           //   Java source line #249	-> byte code offset #136
/*     */           //   Java source line #250	-> byte code offset #145
/*     */           //   Java source line #252	-> byte code offset #155
/*     */           //   Java source line #253	-> byte code offset #157
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	signature
/*     */           //   0	158	0	this	1
/*     */           //   0	158	1	future	Future<T>
/*     */           //   44	6	2	err	io.netty.handler.codec.DecoderException
/*     */           //   59	10	2	cause	Exception
/*     */           //   81	6	2	cause	Throwable
/*     */           //   125	31	3	localObject	Object
/*     */           // Exception table:
/*     */           //   from	to	target	type
/*     */           //   9	41	44	io/netty/handler/codec/DecoderException
/*     */           //   9	41	59	java/lang/Exception
/*     */           //   9	41	81	java/lang/Throwable
/*     */           //   0	93	125	finally
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private void fireSniCompletionEvent(ChannelHandlerContext ctx, String hostname, Future<T> future)
/*     */   {
/* 259 */     Throwable cause = future.cause();
/* 260 */     if (cause == null) {
/* 261 */       ctx.fireUserEventTriggered(new SniCompletionEvent(hostname));
/*     */     } else {
/* 263 */       ctx.fireUserEventTriggered(new SniCompletionEvent(hostname, cause));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Future<T> lookup(ChannelHandlerContext paramChannelHandlerContext, String paramString)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void onLookupComplete(ChannelHandlerContext paramChannelHandlerContext, String paramString, Future<T> paramFuture)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void read(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 285 */     if (this.suppressRead) {
/* 286 */       this.readPending = true;
/*     */     } else {
/* 288 */       ctx.read();
/*     */     }
/*     */   }
/*     */   
/*     */   public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception
/*     */   {
/* 294 */     ctx.bind(localAddress, promise);
/*     */   }
/*     */   
/*     */   public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 300 */     ctx.connect(remoteAddress, localAddress, promise);
/*     */   }
/*     */   
/*     */   public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 305 */     ctx.disconnect(promise);
/*     */   }
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 310 */     ctx.close(promise);
/*     */   }
/*     */   
/*     */   public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 315 */     ctx.deregister(promise);
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 320 */     ctx.write(msg, promise);
/*     */   }
/*     */   
/*     */   public void flush(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 325 */     ctx.flush();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\AbstractSniHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */