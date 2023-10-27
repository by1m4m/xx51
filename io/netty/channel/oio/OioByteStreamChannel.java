/*     */ package io.netty.channel.oio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.Channel.Unsafe;
/*     */ import io.netty.channel.FileRegion;
/*     */ import io.netty.channel.RecvByteBufAllocator.Handle;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.NotYetConnectedException;
/*     */ import java.nio.channels.WritableByteChannel;
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
/*     */ public abstract class OioByteStreamChannel
/*     */   extends AbstractOioByteChannel
/*     */ {
/*  37 */   private static final InputStream CLOSED_IN = new InputStream()
/*     */   {
/*     */     public int read() {
/*  40 */       return -1;
/*     */     }
/*     */   };
/*     */   
/*  44 */   private static final OutputStream CLOSED_OUT = new OutputStream()
/*     */   {
/*     */     public void write(int b) throws IOException {
/*  47 */       throw new ClosedChannelException();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */   private InputStream is;
/*     */   
/*     */ 
/*     */   private OutputStream os;
/*     */   
/*     */   private WritableByteChannel outChannel;
/*     */   
/*     */ 
/*     */   protected OioByteStreamChannel(Channel parent)
/*     */   {
/*  62 */     super(parent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void activate(InputStream is, OutputStream os)
/*     */   {
/*  69 */     if (this.is != null) {
/*  70 */       throw new IllegalStateException("input was set already");
/*     */     }
/*  72 */     if (this.os != null) {
/*  73 */       throw new IllegalStateException("output was set already");
/*     */     }
/*  75 */     if (is == null) {
/*  76 */       throw new NullPointerException("is");
/*     */     }
/*  78 */     if (os == null) {
/*  79 */       throw new NullPointerException("os");
/*     */     }
/*  81 */     this.is = is;
/*  82 */     this.os = os;
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/*  87 */     InputStream is = this.is;
/*  88 */     if ((is == null) || (is == CLOSED_IN)) {
/*  89 */       return false;
/*     */     }
/*     */     
/*  92 */     OutputStream os = this.os;
/*  93 */     return (os != null) && (os != CLOSED_OUT);
/*     */   }
/*     */   
/*     */   protected int available()
/*     */   {
/*     */     try {
/*  99 */       return this.is.available();
/*     */     } catch (IOException ignored) {}
/* 101 */     return 0;
/*     */   }
/*     */   
/*     */   protected int doReadBytes(ByteBuf buf)
/*     */     throws Exception
/*     */   {
/* 107 */     RecvByteBufAllocator.Handle allocHandle = unsafe().recvBufAllocHandle();
/* 108 */     allocHandle.attemptedBytesRead(Math.max(1, Math.min(available(), buf.maxWritableBytes())));
/* 109 */     return buf.writeBytes(this.is, allocHandle.attemptedBytesRead());
/*     */   }
/*     */   
/*     */   protected void doWriteBytes(ByteBuf buf) throws Exception
/*     */   {
/* 114 */     OutputStream os = this.os;
/* 115 */     if (os == null) {
/* 116 */       throw new NotYetConnectedException();
/*     */     }
/* 118 */     buf.readBytes(os, buf.readableBytes());
/*     */   }
/*     */   
/*     */   protected void doWriteFileRegion(FileRegion region) throws Exception
/*     */   {
/* 123 */     OutputStream os = this.os;
/* 124 */     if (os == null) {
/* 125 */       throw new NotYetConnectedException();
/*     */     }
/* 127 */     if (this.outChannel == null) {
/* 128 */       this.outChannel = Channels.newChannel(os);
/*     */     }
/*     */     
/* 131 */     long written = 0L;
/*     */     for (;;) {
/* 133 */       long localWritten = region.transferTo(this.outChannel, written);
/* 134 */       if (localWritten == -1L) {
/* 135 */         checkEOF(region);
/* 136 */         return;
/*     */       }
/* 138 */       written += localWritten;
/*     */       
/* 140 */       if (written >= region.count()) {
/* 141 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void checkEOF(FileRegion region) throws IOException {
/* 147 */     if (region.transferred() < region.count())
/*     */     {
/* 149 */       throw new EOFException("Expected to be able to write " + region.count() + " bytes, but only wrote " + region.transferred());
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void doClose()
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 2	io/netty/channel/oio/OioByteStreamChannel:is	Ljava/io/InputStream;
/*     */     //   4: astore_1
/*     */     //   5: aload_0
/*     */     //   6: getfield 6	io/netty/channel/oio/OioByteStreamChannel:os	Ljava/io/OutputStream;
/*     */     //   9: astore_2
/*     */     //   10: aload_0
/*     */     //   11: getstatic 12	io/netty/channel/oio/OioByteStreamChannel:CLOSED_IN	Ljava/io/InputStream;
/*     */     //   14: putfield 2	io/netty/channel/oio/OioByteStreamChannel:is	Ljava/io/InputStream;
/*     */     //   17: aload_0
/*     */     //   18: getstatic 13	io/netty/channel/oio/OioByteStreamChannel:CLOSED_OUT	Ljava/io/OutputStream;
/*     */     //   21: putfield 6	io/netty/channel/oio/OioByteStreamChannel:os	Ljava/io/OutputStream;
/*     */     //   24: aload_1
/*     */     //   25: ifnull +7 -> 32
/*     */     //   28: aload_1
/*     */     //   29: invokevirtual 46	java/io/InputStream:close	()V
/*     */     //   32: aload_2
/*     */     //   33: ifnull +21 -> 54
/*     */     //   36: aload_2
/*     */     //   37: invokevirtual 47	java/io/OutputStream:close	()V
/*     */     //   40: goto +14 -> 54
/*     */     //   43: astore_3
/*     */     //   44: aload_2
/*     */     //   45: ifnull +7 -> 52
/*     */     //   48: aload_2
/*     */     //   49: invokevirtual 47	java/io/OutputStream:close	()V
/*     */     //   52: aload_3
/*     */     //   53: athrow
/*     */     //   54: return
/*     */     // Line number table:
/*     */     //   Java source line #155	-> byte code offset #0
/*     */     //   Java source line #156	-> byte code offset #5
/*     */     //   Java source line #157	-> byte code offset #10
/*     */     //   Java source line #158	-> byte code offset #17
/*     */     //   Java source line #161	-> byte code offset #24
/*     */     //   Java source line #162	-> byte code offset #28
/*     */     //   Java source line #165	-> byte code offset #32
/*     */     //   Java source line #166	-> byte code offset #36
/*     */     //   Java source line #165	-> byte code offset #43
/*     */     //   Java source line #166	-> byte code offset #48
/*     */     //   Java source line #168	-> byte code offset #52
/*     */     //   Java source line #169	-> byte code offset #54
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	55	0	this	OioByteStreamChannel
/*     */     //   4	25	1	is	InputStream
/*     */     //   9	40	2	os	OutputStream
/*     */     //   43	10	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   24	32	43	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\oio\OioByteStreamChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */