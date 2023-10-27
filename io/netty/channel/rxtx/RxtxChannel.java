/*     */ package io.netty.channel.rxtx;
/*     */ 
/*     */ import gnu.io.CommPort;
/*     */ import gnu.io.CommPortIdentifier;
/*     */ import gnu.io.SerialPort;
/*     */ import io.netty.channel.AbstractChannel.AbstractUnsafe;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.oio.OioByteStreamChannel;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ @Deprecated
/*     */ public class RxtxChannel
/*     */   extends OioByteStreamChannel
/*     */ {
/*  45 */   private static final RxtxDeviceAddress LOCAL_ADDRESS = new RxtxDeviceAddress("localhost");
/*     */   
/*     */   private final RxtxChannelConfig config;
/*     */   
/*  49 */   private boolean open = true;
/*     */   private RxtxDeviceAddress deviceAddress;
/*     */   private SerialPort serialPort;
/*     */   
/*     */   public RxtxChannel() {
/*  54 */     super(null);
/*     */     
/*  56 */     this.config = new DefaultRxtxChannelConfig(this);
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig config()
/*     */   {
/*  61 */     return this.config;
/*     */   }
/*     */   
/*     */   public boolean isOpen()
/*     */   {
/*  66 */     return this.open;
/*     */   }
/*     */   
/*     */   protected AbstractChannel.AbstractUnsafe newUnsafe()
/*     */   {
/*  71 */     return new RxtxUnsafe(null);
/*     */   }
/*     */   
/*     */   protected void doConnect(SocketAddress remoteAddress, SocketAddress localAddress) throws Exception
/*     */   {
/*  76 */     RxtxDeviceAddress remote = (RxtxDeviceAddress)remoteAddress;
/*  77 */     CommPortIdentifier cpi = CommPortIdentifier.getPortIdentifier(remote.value());
/*  78 */     CommPort commPort = cpi.open(getClass().getName(), 1000);
/*  79 */     commPort.enableReceiveTimeout(((Integer)config().getOption(RxtxChannelOption.READ_TIMEOUT)).intValue());
/*  80 */     this.deviceAddress = remote;
/*     */     
/*  82 */     this.serialPort = ((SerialPort)commPort);
/*     */   }
/*     */   
/*     */   protected void doInit() throws Exception {
/*  86 */     this.serialPort.setSerialPortParams(
/*  87 */       ((Integer)config().getOption(RxtxChannelOption.BAUD_RATE)).intValue(), 
/*  88 */       ((RxtxChannelConfig.Databits)config().getOption(RxtxChannelOption.DATA_BITS)).value(), 
/*  89 */       ((RxtxChannelConfig.Stopbits)config().getOption(RxtxChannelOption.STOP_BITS)).value(), 
/*  90 */       ((RxtxChannelConfig.Paritybit)config().getOption(RxtxChannelOption.PARITY_BIT)).value());
/*     */     
/*  92 */     this.serialPort.setDTR(((Boolean)config().getOption(RxtxChannelOption.DTR)).booleanValue());
/*  93 */     this.serialPort.setRTS(((Boolean)config().getOption(RxtxChannelOption.RTS)).booleanValue());
/*     */     
/*  95 */     activate(this.serialPort.getInputStream(), this.serialPort.getOutputStream());
/*     */   }
/*     */   
/*     */   public RxtxDeviceAddress localAddress()
/*     */   {
/* 100 */     return (RxtxDeviceAddress)super.localAddress();
/*     */   }
/*     */   
/*     */   public RxtxDeviceAddress remoteAddress()
/*     */   {
/* 105 */     return (RxtxDeviceAddress)super.remoteAddress();
/*     */   }
/*     */   
/*     */   protected RxtxDeviceAddress localAddress0()
/*     */   {
/* 110 */     return LOCAL_ADDRESS;
/*     */   }
/*     */   
/*     */   protected RxtxDeviceAddress remoteAddress0()
/*     */   {
/* 115 */     return this.deviceAddress;
/*     */   }
/*     */   
/*     */   protected void doBind(SocketAddress localAddress) throws Exception
/*     */   {
/* 120 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   protected void doDisconnect() throws Exception
/*     */   {
/* 125 */     doClose();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected void doClose()
/*     */     throws Exception
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: iconst_0
/*     */     //   2: putfield 2	io/netty/channel/rxtx/RxtxChannel:open	Z
/*     */     //   5: aload_0
/*     */     //   6: invokespecial 49	io/netty/channel/oio/OioByteStreamChannel:doClose	()V
/*     */     //   9: aload_0
/*     */     //   10: getfield 22	io/netty/channel/rxtx/RxtxChannel:serialPort	Lgnu/io/SerialPort;
/*     */     //   13: ifnull +54 -> 67
/*     */     //   16: aload_0
/*     */     //   17: getfield 22	io/netty/channel/rxtx/RxtxChannel:serialPort	Lgnu/io/SerialPort;
/*     */     //   20: invokevirtual 50	gnu/io/SerialPort:removeEventListener	()V
/*     */     //   23: aload_0
/*     */     //   24: getfield 22	io/netty/channel/rxtx/RxtxChannel:serialPort	Lgnu/io/SerialPort;
/*     */     //   27: invokevirtual 51	gnu/io/SerialPort:close	()V
/*     */     //   30: aload_0
/*     */     //   31: aconst_null
/*     */     //   32: putfield 22	io/netty/channel/rxtx/RxtxChannel:serialPort	Lgnu/io/SerialPort;
/*     */     //   35: goto +32 -> 67
/*     */     //   38: astore_1
/*     */     //   39: aload_0
/*     */     //   40: getfield 22	io/netty/channel/rxtx/RxtxChannel:serialPort	Lgnu/io/SerialPort;
/*     */     //   43: ifnull +22 -> 65
/*     */     //   46: aload_0
/*     */     //   47: getfield 22	io/netty/channel/rxtx/RxtxChannel:serialPort	Lgnu/io/SerialPort;
/*     */     //   50: invokevirtual 50	gnu/io/SerialPort:removeEventListener	()V
/*     */     //   53: aload_0
/*     */     //   54: getfield 22	io/netty/channel/rxtx/RxtxChannel:serialPort	Lgnu/io/SerialPort;
/*     */     //   57: invokevirtual 51	gnu/io/SerialPort:close	()V
/*     */     //   60: aload_0
/*     */     //   61: aconst_null
/*     */     //   62: putfield 22	io/netty/channel/rxtx/RxtxChannel:serialPort	Lgnu/io/SerialPort;
/*     */     //   65: aload_1
/*     */     //   66: athrow
/*     */     //   67: return
/*     */     // Line number table:
/*     */     //   Java source line #130	-> byte code offset #0
/*     */     //   Java source line #132	-> byte code offset #5
/*     */     //   Java source line #134	-> byte code offset #9
/*     */     //   Java source line #135	-> byte code offset #16
/*     */     //   Java source line #136	-> byte code offset #23
/*     */     //   Java source line #137	-> byte code offset #30
/*     */     //   Java source line #134	-> byte code offset #38
/*     */     //   Java source line #135	-> byte code offset #46
/*     */     //   Java source line #136	-> byte code offset #53
/*     */     //   Java source line #137	-> byte code offset #60
/*     */     //   Java source line #139	-> byte code offset #65
/*     */     //   Java source line #140	-> byte code offset #67
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	68	0	this	RxtxChannel
/*     */     //   38	28	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   5	9	38	finally
/*     */   }
/*     */   
/*     */   protected boolean isInputShutdown()
/*     */   {
/* 144 */     return !this.open;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 149 */   protected ChannelFuture shutdownInput() { return newFailedFuture(new UnsupportedOperationException("shutdownInput")); }
/*     */   
/*     */   private final class RxtxUnsafe extends AbstractChannel.AbstractUnsafe {
/* 152 */     private RxtxUnsafe() { super(); }
/*     */     
/*     */ 
/*     */     public void connect(SocketAddress remoteAddress, SocketAddress localAddress, final ChannelPromise promise)
/*     */     {
/* 157 */       if ((!promise.setUncancellable()) || (!ensureOpen(promise))) {
/* 158 */         return;
/*     */       }
/*     */       try
/*     */       {
/* 162 */         final boolean wasActive = RxtxChannel.this.isActive();
/* 163 */         RxtxChannel.this.doConnect(remoteAddress, localAddress);
/*     */         
/* 165 */         int waitTime = ((Integer)RxtxChannel.this.config().getOption(RxtxChannelOption.WAIT_TIME)).intValue();
/* 166 */         if (waitTime > 0) {
/* 167 */           RxtxChannel.this.eventLoop().schedule(new Runnable()
/*     */           {
/*     */             public void run() {
/*     */               try {
/* 171 */                 RxtxChannel.this.doInit();
/* 172 */                 RxtxChannel.RxtxUnsafe.this.safeSetSuccess(promise);
/* 173 */                 if ((!wasActive) && (RxtxChannel.this.isActive())) {
/* 174 */                   RxtxChannel.this.pipeline().fireChannelActive();
/*     */                 }
/*     */               } catch (Throwable t) {
/* 177 */                 RxtxChannel.RxtxUnsafe.this.safeSetFailure(promise, t);
/* 178 */                 RxtxChannel.RxtxUnsafe.this.closeIfClosed(); } } }, waitTime, TimeUnit.MILLISECONDS);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 183 */           RxtxChannel.this.doInit();
/* 184 */           safeSetSuccess(promise);
/* 185 */           if ((!wasActive) && (RxtxChannel.this.isActive())) {
/* 186 */             RxtxChannel.this.pipeline().fireChannelActive();
/*     */           }
/*     */         }
/*     */       } catch (Throwable t) {
/* 190 */         safeSetFailure(promise, t);
/* 191 */         closeIfClosed();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\rxtx\RxtxChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */