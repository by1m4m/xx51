/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.WriteBufferWaterMark;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.util.Map;
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
/*     */ public final class EpollSocketChannelConfig
/*     */   extends EpollChannelConfig
/*     */   implements SocketChannelConfig
/*     */ {
/*     */   private final EpollSocketChannel channel;
/*     */   private volatile boolean allowHalfClosure;
/*     */   
/*     */   EpollSocketChannelConfig(EpollSocketChannel channel)
/*     */   {
/*  48 */     super(channel);
/*     */     
/*  50 */     this.channel = channel;
/*  51 */     if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
/*  52 */       setTcpNoDelay(true);
/*     */     }
/*  54 */     calculateMaxBytesPerGatheringWrite();
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  59 */     return getOptions(
/*  60 */       super.getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE, EpollChannelOption.TCP_CORK, EpollChannelOption.TCP_NOTSENT_LOWAT, EpollChannelOption.TCP_KEEPCNT, EpollChannelOption.TCP_KEEPIDLE, EpollChannelOption.TCP_KEEPINTVL, EpollChannelOption.TCP_MD5SIG, EpollChannelOption.TCP_QUICKACK, EpollChannelOption.IP_TRANSPARENT, EpollChannelOption.TCP_FASTOPEN_CONNECT, EpollChannelOption.SO_BUSY_POLL });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  71 */     if (option == ChannelOption.SO_RCVBUF) {
/*  72 */       return Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  74 */     if (option == ChannelOption.SO_SNDBUF) {
/*  75 */       return Integer.valueOf(getSendBufferSize());
/*     */     }
/*  77 */     if (option == ChannelOption.TCP_NODELAY) {
/*  78 */       return Boolean.valueOf(isTcpNoDelay());
/*     */     }
/*  80 */     if (option == ChannelOption.SO_KEEPALIVE) {
/*  81 */       return Boolean.valueOf(isKeepAlive());
/*     */     }
/*  83 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  84 */       return Boolean.valueOf(isReuseAddress());
/*     */     }
/*  86 */     if (option == ChannelOption.SO_LINGER) {
/*  87 */       return Integer.valueOf(getSoLinger());
/*     */     }
/*  89 */     if (option == ChannelOption.IP_TOS) {
/*  90 */       return Integer.valueOf(getTrafficClass());
/*     */     }
/*  92 */     if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/*  93 */       return Boolean.valueOf(isAllowHalfClosure());
/*     */     }
/*  95 */     if (option == EpollChannelOption.TCP_CORK) {
/*  96 */       return Boolean.valueOf(isTcpCork());
/*     */     }
/*  98 */     if (option == EpollChannelOption.TCP_NOTSENT_LOWAT) {
/*  99 */       return Long.valueOf(getTcpNotSentLowAt());
/*     */     }
/* 101 */     if (option == EpollChannelOption.TCP_KEEPIDLE) {
/* 102 */       return Integer.valueOf(getTcpKeepIdle());
/*     */     }
/* 104 */     if (option == EpollChannelOption.TCP_KEEPINTVL) {
/* 105 */       return Integer.valueOf(getTcpKeepIntvl());
/*     */     }
/* 107 */     if (option == EpollChannelOption.TCP_KEEPCNT) {
/* 108 */       return Integer.valueOf(getTcpKeepCnt());
/*     */     }
/* 110 */     if (option == EpollChannelOption.TCP_USER_TIMEOUT) {
/* 111 */       return Integer.valueOf(getTcpUserTimeout());
/*     */     }
/* 113 */     if (option == EpollChannelOption.TCP_QUICKACK) {
/* 114 */       return Boolean.valueOf(isTcpQuickAck());
/*     */     }
/* 116 */     if (option == EpollChannelOption.IP_TRANSPARENT) {
/* 117 */       return Boolean.valueOf(isIpTransparent());
/*     */     }
/* 119 */     if (option == EpollChannelOption.TCP_FASTOPEN_CONNECT) {
/* 120 */       return Boolean.valueOf(isTcpFastOpenConnect());
/*     */     }
/* 122 */     if (option == EpollChannelOption.SO_BUSY_POLL) {
/* 123 */       return Integer.valueOf(getSoBusyPoll());
/*     */     }
/* 125 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/* 130 */     validate(option, value);
/*     */     
/* 132 */     if (option == ChannelOption.SO_RCVBUF) {
/* 133 */       setReceiveBufferSize(((Integer)value).intValue());
/* 134 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 135 */       setSendBufferSize(((Integer)value).intValue());
/* 136 */     } else if (option == ChannelOption.TCP_NODELAY) {
/* 137 */       setTcpNoDelay(((Boolean)value).booleanValue());
/* 138 */     } else if (option == ChannelOption.SO_KEEPALIVE) {
/* 139 */       setKeepAlive(((Boolean)value).booleanValue());
/* 140 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 141 */       setReuseAddress(((Boolean)value).booleanValue());
/* 142 */     } else if (option == ChannelOption.SO_LINGER) {
/* 143 */       setSoLinger(((Integer)value).intValue());
/* 144 */     } else if (option == ChannelOption.IP_TOS) {
/* 145 */       setTrafficClass(((Integer)value).intValue());
/* 146 */     } else if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/* 147 */       setAllowHalfClosure(((Boolean)value).booleanValue());
/* 148 */     } else if (option == EpollChannelOption.TCP_CORK) {
/* 149 */       setTcpCork(((Boolean)value).booleanValue());
/* 150 */     } else if (option == EpollChannelOption.TCP_NOTSENT_LOWAT) {
/* 151 */       setTcpNotSentLowAt(((Long)value).longValue());
/* 152 */     } else if (option == EpollChannelOption.TCP_KEEPIDLE) {
/* 153 */       setTcpKeepIdle(((Integer)value).intValue());
/* 154 */     } else if (option == EpollChannelOption.TCP_KEEPCNT) {
/* 155 */       setTcpKeepCnt(((Integer)value).intValue());
/* 156 */     } else if (option == EpollChannelOption.TCP_KEEPINTVL) {
/* 157 */       setTcpKeepIntvl(((Integer)value).intValue());
/* 158 */     } else if (option == EpollChannelOption.TCP_USER_TIMEOUT) {
/* 159 */       setTcpUserTimeout(((Integer)value).intValue());
/* 160 */     } else if (option == EpollChannelOption.IP_TRANSPARENT) {
/* 161 */       setIpTransparent(((Boolean)value).booleanValue());
/* 162 */     } else if (option == EpollChannelOption.TCP_MD5SIG)
/*     */     {
/* 164 */       Map<InetAddress, byte[]> m = (Map)value;
/* 165 */       setTcpMd5Sig(m);
/* 166 */     } else if (option == EpollChannelOption.TCP_QUICKACK) {
/* 167 */       setTcpQuickAck(((Boolean)value).booleanValue());
/* 168 */     } else if (option == EpollChannelOption.TCP_FASTOPEN_CONNECT) {
/* 169 */       setTcpFastOpenConnect(((Boolean)value).booleanValue());
/* 170 */     } else if (option == EpollChannelOption.SO_BUSY_POLL) {
/* 171 */       setSoBusyPoll(((Integer)value).intValue());
/*     */     } else {
/* 173 */       return super.setOption(option, value);
/*     */     }
/*     */     
/* 176 */     return true;
/*     */   }
/*     */   
/*     */   public int getReceiveBufferSize()
/*     */   {
/*     */     try {
/* 182 */       return this.channel.socket.getReceiveBufferSize();
/*     */     } catch (IOException e) {
/* 184 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getSendBufferSize()
/*     */   {
/*     */     try {
/* 191 */       return this.channel.socket.getSendBufferSize();
/*     */     } catch (IOException e) {
/* 193 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getSoLinger()
/*     */   {
/*     */     try {
/* 200 */       return this.channel.socket.getSoLinger();
/*     */     } catch (IOException e) {
/* 202 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getTrafficClass()
/*     */   {
/*     */     try {
/* 209 */       return this.channel.socket.getTrafficClass();
/*     */     } catch (IOException e) {
/* 211 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isKeepAlive()
/*     */   {
/*     */     try {
/* 218 */       return this.channel.socket.isKeepAlive();
/*     */     } catch (IOException e) {
/* 220 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isReuseAddress()
/*     */   {
/*     */     try {
/* 227 */       return this.channel.socket.isReuseAddress();
/*     */     } catch (IOException e) {
/* 229 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isTcpNoDelay()
/*     */   {
/*     */     try {
/* 236 */       return this.channel.socket.isTcpNoDelay();
/*     */     } catch (IOException e) {
/* 238 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isTcpCork()
/*     */   {
/*     */     try
/*     */     {
/* 247 */       return this.channel.socket.isTcpCork();
/*     */     } catch (IOException e) {
/* 249 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getSoBusyPoll()
/*     */   {
/*     */     try
/*     */     {
/* 258 */       return this.channel.socket.getSoBusyPoll();
/*     */     } catch (IOException e) {
/* 260 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getTcpNotSentLowAt()
/*     */   {
/*     */     try
/*     */     {
/* 270 */       return this.channel.socket.getTcpNotSentLowAt();
/*     */     } catch (IOException e) {
/* 272 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getTcpKeepIdle()
/*     */   {
/*     */     try
/*     */     {
/* 281 */       return this.channel.socket.getTcpKeepIdle();
/*     */     } catch (IOException e) {
/* 283 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getTcpKeepIntvl()
/*     */   {
/*     */     try
/*     */     {
/* 292 */       return this.channel.socket.getTcpKeepIntvl();
/*     */     } catch (IOException e) {
/* 294 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getTcpKeepCnt()
/*     */   {
/*     */     try
/*     */     {
/* 303 */       return this.channel.socket.getTcpKeepCnt();
/*     */     } catch (IOException e) {
/* 305 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getTcpUserTimeout()
/*     */   {
/*     */     try
/*     */     {
/* 314 */       return this.channel.socket.getTcpUserTimeout();
/*     */     } catch (IOException e) {
/* 316 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setKeepAlive(boolean keepAlive)
/*     */   {
/*     */     try {
/* 323 */       this.channel.socket.setKeepAlive(keepAlive);
/* 324 */       return this;
/*     */     } catch (IOException e) {
/* 326 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public EpollSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth)
/*     */   {
/* 333 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setReceiveBufferSize(int receiveBufferSize)
/*     */   {
/*     */     try {
/* 339 */       this.channel.socket.setReceiveBufferSize(receiveBufferSize);
/* 340 */       return this;
/*     */     } catch (IOException e) {
/* 342 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setReuseAddress(boolean reuseAddress)
/*     */   {
/*     */     try {
/* 349 */       this.channel.socket.setReuseAddress(reuseAddress);
/* 350 */       return this;
/*     */     } catch (IOException e) {
/* 352 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setSendBufferSize(int sendBufferSize)
/*     */   {
/*     */     try {
/* 359 */       this.channel.socket.setSendBufferSize(sendBufferSize);
/* 360 */       calculateMaxBytesPerGatheringWrite();
/* 361 */       return this;
/*     */     } catch (IOException e) {
/* 363 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setSoLinger(int soLinger)
/*     */   {
/*     */     try {
/* 370 */       this.channel.socket.setSoLinger(soLinger);
/* 371 */       return this;
/*     */     } catch (IOException e) {
/* 373 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setTcpNoDelay(boolean tcpNoDelay)
/*     */   {
/*     */     try {
/* 380 */       this.channel.socket.setTcpNoDelay(tcpNoDelay);
/* 381 */       return this;
/*     */     } catch (IOException e) {
/* 383 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpCork(boolean tcpCork)
/*     */   {
/*     */     try
/*     */     {
/* 392 */       this.channel.socket.setTcpCork(tcpCork);
/* 393 */       return this;
/*     */     } catch (IOException e) {
/* 395 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public EpollSocketChannelConfig setSoBusyPoll(int loopMicros)
/*     */   {
/*     */     try
/*     */     {
/* 404 */       this.channel.socket.setSoBusyPoll(loopMicros);
/* 405 */       return this;
/*     */     } catch (IOException e) {
/* 407 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpNotSentLowAt(long tcpNotSentLowAt)
/*     */   {
/*     */     try
/*     */     {
/* 417 */       this.channel.socket.setTcpNotSentLowAt(tcpNotSentLowAt);
/* 418 */       return this;
/*     */     } catch (IOException e) {
/* 420 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setTrafficClass(int trafficClass)
/*     */   {
/*     */     try {
/* 427 */       this.channel.socket.setTrafficClass(trafficClass);
/* 428 */       return this;
/*     */     } catch (IOException e) {
/* 430 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpKeepIdle(int seconds)
/*     */   {
/*     */     try
/*     */     {
/* 439 */       this.channel.socket.setTcpKeepIdle(seconds);
/* 440 */       return this;
/*     */     } catch (IOException e) {
/* 442 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpKeepIntvl(int seconds)
/*     */   {
/*     */     try
/*     */     {
/* 451 */       this.channel.socket.setTcpKeepIntvl(seconds);
/* 452 */       return this;
/*     */     } catch (IOException e) {
/* 454 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public EpollSocketChannelConfig setTcpKeepCntl(int probes)
/*     */   {
/* 463 */     return setTcpKeepCnt(probes);
/*     */   }
/*     */   
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpKeepCnt(int probes)
/*     */   {
/*     */     try
/*     */     {
/* 471 */       this.channel.socket.setTcpKeepCnt(probes);
/* 472 */       return this;
/*     */     } catch (IOException e) {
/* 474 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpUserTimeout(int milliseconds)
/*     */   {
/*     */     try
/*     */     {
/* 483 */       this.channel.socket.setTcpUserTimeout(milliseconds);
/* 484 */       return this;
/*     */     } catch (IOException e) {
/* 486 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isIpTransparent()
/*     */   {
/*     */     try
/*     */     {
/* 496 */       return this.channel.socket.isIpTransparent();
/*     */     } catch (IOException e) {
/* 498 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EpollSocketChannelConfig setIpTransparent(boolean transparent)
/*     */   {
/*     */     try
/*     */     {
/* 508 */       this.channel.socket.setIpTransparent(transparent);
/* 509 */       return this;
/*     */     } catch (IOException e) {
/* 511 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpMd5Sig(Map<InetAddress, byte[]> keys)
/*     */   {
/*     */     try
/*     */     {
/* 522 */       this.channel.setTcpMd5Sig(keys);
/* 523 */       return this;
/*     */     } catch (IOException e) {
/* 525 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpQuickAck(boolean quickAck)
/*     */   {
/*     */     try
/*     */     {
/* 535 */       this.channel.socket.setTcpQuickAck(quickAck);
/* 536 */       return this;
/*     */     } catch (IOException e) {
/* 538 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isTcpQuickAck()
/*     */   {
/*     */     try
/*     */     {
/* 548 */       return this.channel.socket.isTcpQuickAck();
/*     */     } catch (IOException e) {
/* 550 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpFastOpenConnect(boolean fastOpenConnect)
/*     */   {
/*     */     try
/*     */     {
/* 562 */       this.channel.socket.setTcpFastOpenConnect(fastOpenConnect);
/* 563 */       return this;
/*     */     } catch (IOException e) {
/* 565 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isTcpFastOpenConnect()
/*     */   {
/*     */     try
/*     */     {
/* 574 */       return this.channel.socket.isTcpFastOpenConnect();
/*     */     } catch (IOException e) {
/* 576 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAllowHalfClosure()
/*     */   {
/* 582 */     return this.allowHalfClosure;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure)
/*     */   {
/* 587 */     this.allowHalfClosure = allowHalfClosure;
/* 588 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 593 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 594 */     return this;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public EpollSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 600 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 601 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 606 */     super.setWriteSpinCount(writeSpinCount);
/* 607 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 612 */     super.setAllocator(allocator);
/* 613 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 618 */     super.setRecvByteBufAllocator(allocator);
/* 619 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 624 */     super.setAutoRead(autoRead);
/* 625 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setAutoClose(boolean autoClose)
/*     */   {
/* 630 */     super.setAutoClose(autoClose);
/* 631 */     return this;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public EpollSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 637 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 638 */     return this;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public EpollSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 644 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 645 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark)
/*     */   {
/* 650 */     super.setWriteBufferWaterMark(writeBufferWaterMark);
/* 651 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 656 */     super.setMessageSizeEstimator(estimator);
/* 657 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setEpollMode(EpollMode mode)
/*     */   {
/* 662 */     super.setEpollMode(mode);
/* 663 */     return this;
/*     */   }
/*     */   
/*     */   private void calculateMaxBytesPerGatheringWrite()
/*     */   {
/* 668 */     int newSendBufferSize = getSendBufferSize() << 1;
/* 669 */     if (newSendBufferSize > 0) {
/* 670 */       setMaxBytesPerGatheringWrite(getSendBufferSize() << 1);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\epoll\EpollSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */