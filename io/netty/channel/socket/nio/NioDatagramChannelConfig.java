/*     */ package io.netty.channel.socket.nio;
/*     */ 
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.socket.DatagramChannelConfig;
/*     */ import io.netty.channel.socket.DefaultDatagramChannelConfig;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SocketUtils;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import java.util.Enumeration;
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
/*     */ class NioDatagramChannelConfig
/*     */   extends DefaultDatagramChannelConfig
/*     */ {
/*     */   private static final Object IP_MULTICAST_TTL;
/*     */   private static final Object IP_MULTICAST_IF;
/*     */   private static final Object IP_MULTICAST_LOOP;
/*     */   private static final Method GET_OPTION;
/*     */   private static final Method SET_OPTION;
/*     */   private final DatagramChannel javaChannel;
/*     */   
/*     */   static
/*     */   {
/*  45 */     ClassLoader classLoader = PlatformDependent.getClassLoader(DatagramChannel.class);
/*  46 */     Class<?> socketOptionType = null;
/*     */     try {
/*  48 */       socketOptionType = Class.forName("java.net.SocketOption", true, classLoader);
/*     */     }
/*     */     catch (Exception localException1) {}
/*     */     
/*  52 */     Class<?> stdSocketOptionType = null;
/*     */     try {
/*  54 */       stdSocketOptionType = Class.forName("java.net.StandardSocketOptions", true, classLoader);
/*     */     }
/*     */     catch (Exception localException2) {}
/*     */     
/*     */ 
/*  59 */     Object ipMulticastTtl = null;
/*  60 */     Object ipMulticastIf = null;
/*  61 */     Object ipMulticastLoop = null;
/*  62 */     Method getOption = null;
/*  63 */     Method setOption = null;
/*  64 */     if (socketOptionType != null) {
/*     */       try {
/*  66 */         ipMulticastTtl = stdSocketOptionType.getDeclaredField("IP_MULTICAST_TTL").get(null);
/*     */       } catch (Exception e) {
/*  68 */         throw new Error("cannot locate the IP_MULTICAST_TTL field", e);
/*     */       }
/*     */       try
/*     */       {
/*  72 */         ipMulticastIf = stdSocketOptionType.getDeclaredField("IP_MULTICAST_IF").get(null);
/*     */       } catch (Exception e) {
/*  74 */         throw new Error("cannot locate the IP_MULTICAST_IF field", e);
/*     */       }
/*     */       try
/*     */       {
/*  78 */         ipMulticastLoop = stdSocketOptionType.getDeclaredField("IP_MULTICAST_LOOP").get(null);
/*     */       } catch (Exception e) {
/*  80 */         throw new Error("cannot locate the IP_MULTICAST_LOOP field", e);
/*     */       }
/*     */       
/*  83 */       Class<?> networkChannelClass = null;
/*     */       try {
/*  85 */         networkChannelClass = Class.forName("java.nio.channels.NetworkChannel", true, classLoader);
/*     */       }
/*     */       catch (Throwable localThrowable) {}
/*     */       
/*     */ 
/*  90 */       if (networkChannelClass == null) {
/*  91 */         getOption = null;
/*  92 */         setOption = null;
/*     */       } else {
/*     */         try {
/*  95 */           getOption = networkChannelClass.getDeclaredMethod("getOption", new Class[] { socketOptionType });
/*     */         } catch (Exception e) {
/*  97 */           throw new Error("cannot locate the getOption() method", e);
/*     */         }
/*     */         try
/*     */         {
/* 101 */           setOption = networkChannelClass.getDeclaredMethod("setOption", new Class[] { socketOptionType, Object.class });
/*     */         } catch (Exception e) {
/* 103 */           throw new Error("cannot locate the setOption() method", e);
/*     */         }
/*     */       }
/*     */     }
/* 107 */     IP_MULTICAST_TTL = ipMulticastTtl;
/* 108 */     IP_MULTICAST_IF = ipMulticastIf;
/* 109 */     IP_MULTICAST_LOOP = ipMulticastLoop;
/* 110 */     GET_OPTION = getOption;
/* 111 */     SET_OPTION = setOption;
/*     */   }
/*     */   
/*     */ 
/*     */   NioDatagramChannelConfig(NioDatagramChannel channel, DatagramChannel javaChannel)
/*     */   {
/* 117 */     super(channel, javaChannel.socket());
/* 118 */     this.javaChannel = javaChannel;
/*     */   }
/*     */   
/*     */   public int getTimeToLive()
/*     */   {
/* 123 */     return ((Integer)getOption0(IP_MULTICAST_TTL)).intValue();
/*     */   }
/*     */   
/*     */   public DatagramChannelConfig setTimeToLive(int ttl)
/*     */   {
/* 128 */     setOption0(IP_MULTICAST_TTL, Integer.valueOf(ttl));
/* 129 */     return this;
/*     */   }
/*     */   
/*     */   public InetAddress getInterface()
/*     */   {
/* 134 */     NetworkInterface inf = getNetworkInterface();
/* 135 */     if (inf != null) {
/* 136 */       Enumeration<InetAddress> addresses = SocketUtils.addressesFromNetworkInterface(inf);
/* 137 */       if (addresses.hasMoreElements()) {
/* 138 */         return (InetAddress)addresses.nextElement();
/*     */       }
/*     */     }
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   public DatagramChannelConfig setInterface(InetAddress interfaceAddress)
/*     */   {
/*     */     try {
/* 147 */       setNetworkInterface(NetworkInterface.getByInetAddress(interfaceAddress));
/*     */     } catch (SocketException e) {
/* 149 */       throw new ChannelException(e);
/*     */     }
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   public NetworkInterface getNetworkInterface()
/*     */   {
/* 156 */     return (NetworkInterface)getOption0(IP_MULTICAST_IF);
/*     */   }
/*     */   
/*     */   public DatagramChannelConfig setNetworkInterface(NetworkInterface networkInterface)
/*     */   {
/* 161 */     setOption0(IP_MULTICAST_IF, networkInterface);
/* 162 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isLoopbackModeDisabled()
/*     */   {
/* 167 */     return ((Boolean)getOption0(IP_MULTICAST_LOOP)).booleanValue();
/*     */   }
/*     */   
/*     */   public DatagramChannelConfig setLoopbackModeDisabled(boolean loopbackModeDisabled)
/*     */   {
/* 172 */     setOption0(IP_MULTICAST_LOOP, Boolean.valueOf(loopbackModeDisabled));
/* 173 */     return this;
/*     */   }
/*     */   
/*     */   public DatagramChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 178 */     super.setAutoRead(autoRead);
/* 179 */     return this;
/*     */   }
/*     */   
/*     */   protected void autoReadCleared()
/*     */   {
/* 184 */     ((NioDatagramChannel)this.channel).clearReadPending0();
/*     */   }
/*     */   
/*     */   private Object getOption0(Object option) {
/* 188 */     if (GET_OPTION == null) {
/* 189 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     try {
/* 192 */       return GET_OPTION.invoke(this.javaChannel, new Object[] { option });
/*     */     } catch (Exception e) {
/* 194 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setOption0(Object option, Object value)
/*     */   {
/* 200 */     if (SET_OPTION == null) {
/* 201 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     try {
/* 204 */       SET_OPTION.invoke(this.javaChannel, new Object[] { option, value });
/*     */     } catch (Exception e) {
/* 206 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/* 213 */     if ((PlatformDependent.javaVersion() >= 7) && ((option instanceof NioChannelOption))) {
/* 214 */       return NioChannelOption.setOption(this.javaChannel, (NioChannelOption)option, value);
/*     */     }
/* 216 */     return super.setOption(option, value);
/*     */   }
/*     */   
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/* 221 */     if ((PlatformDependent.javaVersion() >= 7) && ((option instanceof NioChannelOption))) {
/* 222 */       return (T)NioChannelOption.getOption(this.javaChannel, (NioChannelOption)option);
/*     */     }
/* 224 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/* 230 */     if (PlatformDependent.javaVersion() >= 7) {
/* 231 */       return getOptions(super.getOptions(), NioChannelOption.getOptions(this.javaChannel));
/*     */     }
/* 233 */     return super.getOptions();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\socket\nio\NioDatagramChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */