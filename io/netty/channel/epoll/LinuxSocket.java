/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.channel.DefaultFileRegion;
/*     */ import io.netty.channel.unix.Errors;
/*     */ import io.netty.channel.unix.Errors.NativeIoException;
/*     */ import io.netty.channel.unix.NativeInetAddress;
/*     */ import io.netty.channel.unix.PeerCredentials;
/*     */ import io.netty.channel.unix.Socket;
/*     */ import io.netty.util.internal.ThrowableUtil;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.nio.channels.ClosedChannelException;
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
/*     */ final class LinuxSocket
/*     */   extends Socket
/*     */ {
/*     */   private static final long MAX_UINT32_T = 4294967295L;
/*  39 */   private static final Errors.NativeIoException SENDFILE_CONNECTION_RESET_EXCEPTION = Errors.newConnectionResetException("syscall:sendfile(...)", Errors.ERRNO_EPIPE_NEGATIVE);
/*  40 */   private static final ClosedChannelException SENDFILE_CLOSED_CHANNEL_EXCEPTION = (ClosedChannelException)ThrowableUtil.unknownStackTrace(new ClosedChannelException(), Native.class, "sendfile(...)");
/*     */   
/*     */   public LinuxSocket(int fd)
/*     */   {
/*  44 */     super(fd);
/*     */   }
/*     */   
/*     */   void setTcpDeferAccept(int deferAccept) throws IOException {
/*  48 */     setTcpDeferAccept(intValue(), deferAccept);
/*     */   }
/*     */   
/*     */   void setTcpQuickAck(boolean quickAck) throws IOException {
/*  52 */     setTcpQuickAck(intValue(), quickAck ? 1 : 0);
/*     */   }
/*     */   
/*     */   void setTcpCork(boolean tcpCork) throws IOException {
/*  56 */     setTcpCork(intValue(), tcpCork ? 1 : 0);
/*     */   }
/*     */   
/*     */   void setSoBusyPoll(int loopMicros) throws IOException {
/*  60 */     setSoBusyPoll(intValue(), loopMicros);
/*     */   }
/*     */   
/*     */   void setTcpNotSentLowAt(long tcpNotSentLowAt) throws IOException {
/*  64 */     if ((tcpNotSentLowAt < 0L) || (tcpNotSentLowAt > 4294967295L)) {
/*  65 */       throw new IllegalArgumentException("tcpNotSentLowAt must be a uint32_t");
/*     */     }
/*  67 */     setTcpNotSentLowAt(intValue(), (int)tcpNotSentLowAt);
/*     */   }
/*     */   
/*     */   void setTcpFastOpen(int tcpFastopenBacklog) throws IOException {
/*  71 */     setTcpFastOpen(intValue(), tcpFastopenBacklog);
/*     */   }
/*     */   
/*     */   void setTcpFastOpenConnect(boolean tcpFastOpenConnect) throws IOException {
/*  75 */     setTcpFastOpenConnect(intValue(), tcpFastOpenConnect ? 1 : 0);
/*     */   }
/*     */   
/*     */   boolean isTcpFastOpenConnect() throws IOException {
/*  79 */     return isTcpFastOpenConnect(intValue()) != 0;
/*     */   }
/*     */   
/*     */   void setTcpKeepIdle(int seconds) throws IOException {
/*  83 */     setTcpKeepIdle(intValue(), seconds);
/*     */   }
/*     */   
/*     */   void setTcpKeepIntvl(int seconds) throws IOException {
/*  87 */     setTcpKeepIntvl(intValue(), seconds);
/*     */   }
/*     */   
/*     */   void setTcpKeepCnt(int probes) throws IOException {
/*  91 */     setTcpKeepCnt(intValue(), probes);
/*     */   }
/*     */   
/*     */   void setTcpUserTimeout(int milliseconds) throws IOException {
/*  95 */     setTcpUserTimeout(intValue(), milliseconds);
/*     */   }
/*     */   
/*     */   void setIpFreeBind(boolean enabled) throws IOException {
/*  99 */     setIpFreeBind(intValue(), enabled ? 1 : 0);
/*     */   }
/*     */   
/*     */   void setIpTransparent(boolean enabled) throws IOException {
/* 103 */     setIpTransparent(intValue(), enabled ? 1 : 0);
/*     */   }
/*     */   
/*     */   void setIpRecvOrigDestAddr(boolean enabled) throws IOException {
/* 107 */     setIpRecvOrigDestAddr(intValue(), enabled ? 1 : 0);
/*     */   }
/*     */   
/*     */   void getTcpInfo(EpollTcpInfo info) throws IOException {
/* 111 */     getTcpInfo(intValue(), info.info);
/*     */   }
/*     */   
/*     */   void setTcpMd5Sig(InetAddress address, byte[] key) throws IOException {
/* 115 */     NativeInetAddress a = NativeInetAddress.newInstance(address);
/* 116 */     setTcpMd5Sig(intValue(), a.address(), a.scopeId(), key);
/*     */   }
/*     */   
/*     */   boolean isTcpCork() throws IOException {
/* 120 */     return isTcpCork(intValue()) != 0;
/*     */   }
/*     */   
/*     */   int getSoBusyPoll() throws IOException {
/* 124 */     return getSoBusyPoll(intValue());
/*     */   }
/*     */   
/*     */   int getTcpDeferAccept() throws IOException {
/* 128 */     return getTcpDeferAccept(intValue());
/*     */   }
/*     */   
/*     */   boolean isTcpQuickAck() throws IOException {
/* 132 */     return isTcpQuickAck(intValue()) != 0;
/*     */   }
/*     */   
/*     */   long getTcpNotSentLowAt() throws IOException {
/* 136 */     return getTcpNotSentLowAt(intValue()) & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   int getTcpKeepIdle() throws IOException {
/* 140 */     return getTcpKeepIdle(intValue());
/*     */   }
/*     */   
/*     */   int getTcpKeepIntvl() throws IOException {
/* 144 */     return getTcpKeepIntvl(intValue());
/*     */   }
/*     */   
/*     */   int getTcpKeepCnt() throws IOException {
/* 148 */     return getTcpKeepCnt(intValue());
/*     */   }
/*     */   
/*     */   int getTcpUserTimeout() throws IOException {
/* 152 */     return getTcpUserTimeout(intValue());
/*     */   }
/*     */   
/*     */   boolean isIpFreeBind() throws IOException {
/* 156 */     return isIpFreeBind(intValue()) != 0;
/*     */   }
/*     */   
/*     */   boolean isIpTransparent() throws IOException {
/* 160 */     return isIpTransparent(intValue()) != 0;
/*     */   }
/*     */   
/*     */   boolean isIpRecvOrigDestAddr() throws IOException {
/* 164 */     return isIpRecvOrigDestAddr(intValue()) != 0;
/*     */   }
/*     */   
/*     */   PeerCredentials getPeerCredentials() throws IOException {
/* 168 */     return getPeerCredentials(intValue());
/*     */   }
/*     */   
/*     */   long sendFile(DefaultFileRegion src, long baseOffset, long offset, long length)
/*     */     throws IOException
/*     */   {
/* 174 */     src.open();
/*     */     
/* 176 */     long res = sendFile(intValue(), src, baseOffset, offset, length);
/* 177 */     if (res >= 0L) {
/* 178 */       return res;
/*     */     }
/* 180 */     return Errors.ioResult("sendfile", (int)res, SENDFILE_CONNECTION_RESET_EXCEPTION, SENDFILE_CLOSED_CHANNEL_EXCEPTION);
/*     */   }
/*     */   
/*     */   public static LinuxSocket newSocketStream() {
/* 184 */     return new LinuxSocket(newSocketStream0());
/*     */   }
/*     */   
/*     */   public static LinuxSocket newSocketDgram() {
/* 188 */     return new LinuxSocket(newSocketDgram0());
/*     */   }
/*     */   
/*     */   public static LinuxSocket newSocketDomain() {
/* 192 */     return new LinuxSocket(newSocketDomain0());
/*     */   }
/*     */   
/*     */   private static native long sendFile(int paramInt, DefaultFileRegion paramDefaultFileRegion, long paramLong1, long paramLong2, long paramLong3)
/*     */     throws IOException;
/*     */   
/*     */   private static native int getTcpDeferAccept(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int isTcpQuickAck(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int isTcpCork(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int getSoBusyPoll(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int getTcpNotSentLowAt(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int getTcpKeepIdle(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int getTcpKeepIntvl(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int getTcpKeepCnt(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int getTcpUserTimeout(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int isIpFreeBind(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int isIpTransparent(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int isIpRecvOrigDestAddr(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native void getTcpInfo(int paramInt, long[] paramArrayOfLong)
/*     */     throws IOException;
/*     */   
/*     */   private static native PeerCredentials getPeerCredentials(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native int isTcpFastOpenConnect(int paramInt)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setTcpDeferAccept(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setTcpQuickAck(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setTcpCork(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setSoBusyPoll(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setTcpNotSentLowAt(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setTcpFastOpen(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setTcpFastOpenConnect(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setTcpKeepIdle(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setTcpKeepIntvl(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setTcpKeepCnt(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setTcpUserTimeout(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setIpFreeBind(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setIpTransparent(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setIpRecvOrigDestAddr(int paramInt1, int paramInt2)
/*     */     throws IOException;
/*     */   
/*     */   private static native void setTcpMd5Sig(int paramInt1, byte[] paramArrayOfByte1, int paramInt2, byte[] paramArrayOfByte2)
/*     */     throws IOException;
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\epoll\LinuxSocket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */