/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.nio.channels.DatagramChannel;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Enumeration;
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
/*     */ public final class SocketUtils
/*     */ {
/*     */   public static void connect(Socket socket, final SocketAddress remoteAddress, final int timeout)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  51 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws IOException {
/*  54 */           this.val$socket.connect(remoteAddress, timeout);
/*  55 */           return null;
/*     */         }
/*     */       });
/*     */     } catch (PrivilegedActionException e) {
/*  59 */       throw ((IOException)e.getCause());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void bind(Socket socket, final SocketAddress bindpoint) throws IOException {
/*     */     try {
/*  65 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws IOException {
/*  68 */           this.val$socket.bind(bindpoint);
/*  69 */           return null;
/*     */         }
/*     */       });
/*     */     } catch (PrivilegedActionException e) {
/*  73 */       throw ((IOException)e.getCause());
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean connect(SocketChannel socketChannel, final SocketAddress remoteAddress) throws IOException
/*     */   {
/*     */     try {
/*  80 */       ((Boolean)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Boolean run() throws IOException {
/*  83 */           return Boolean.valueOf(this.val$socketChannel.connect(remoteAddress));
/*     */         }
/*     */       })).booleanValue();
/*     */     } catch (PrivilegedActionException e) {
/*  87 */       throw ((IOException)e.getCause());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void bind(SocketChannel socketChannel, final SocketAddress address) throws IOException {
/*     */     try {
/*  93 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws IOException {
/*  96 */           this.val$socketChannel.bind(address);
/*  97 */           return null;
/*     */         }
/*     */       });
/*     */     } catch (PrivilegedActionException e) {
/* 101 */       throw ((IOException)e.getCause());
/*     */     }
/*     */   }
/*     */   
/*     */   public static SocketChannel accept(ServerSocketChannel serverSocketChannel) throws IOException {
/*     */     try {
/* 107 */       (SocketChannel)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public SocketChannel run() throws IOException {
/* 110 */           return this.val$serverSocketChannel.accept();
/*     */         }
/*     */       });
/*     */     } catch (PrivilegedActionException e) {
/* 114 */       throw ((IOException)e.getCause());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void bind(DatagramChannel networkChannel, final SocketAddress address) throws IOException {
/*     */     try {
/* 120 */       AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Void run() throws IOException {
/* 123 */           this.val$networkChannel.bind(address);
/* 124 */           return null;
/*     */         }
/*     */       });
/*     */     } catch (PrivilegedActionException e) {
/* 128 */       throw ((IOException)e.getCause());
/*     */     }
/*     */   }
/*     */   
/*     */   public static SocketAddress localSocketAddress(ServerSocket socket) {
/* 133 */     (SocketAddress)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public SocketAddress run() {
/* 136 */         return this.val$socket.getLocalSocketAddress();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static InetAddress addressByName(String hostname) throws UnknownHostException {
/*     */     try {
/* 143 */       (InetAddress)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public InetAddress run() throws UnknownHostException {
/* 146 */           return InetAddress.getByName(this.val$hostname);
/*     */         }
/*     */       });
/*     */     } catch (PrivilegedActionException e) {
/* 150 */       throw ((UnknownHostException)e.getCause());
/*     */     }
/*     */   }
/*     */   
/*     */   public static InetAddress[] allAddressesByName(String hostname) throws UnknownHostException {
/*     */     try {
/* 156 */       (InetAddress[])AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public InetAddress[] run() throws UnknownHostException {
/* 159 */           return InetAddress.getAllByName(this.val$hostname);
/*     */         }
/*     */       });
/*     */     } catch (PrivilegedActionException e) {
/* 163 */       throw ((UnknownHostException)e.getCause());
/*     */     }
/*     */   }
/*     */   
/*     */   public static InetSocketAddress socketAddress(String hostname, final int port) {
/* 168 */     (InetSocketAddress)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public InetSocketAddress run() {
/* 171 */         return new InetSocketAddress(this.val$hostname, port);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static Enumeration<InetAddress> addressesFromNetworkInterface(NetworkInterface intf) {
/* 177 */     (Enumeration)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Enumeration<InetAddress> run() {
/* 180 */         return this.val$intf.getInetAddresses();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static InetAddress loopbackAddress() {
/* 186 */     (InetAddress)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public InetAddress run() {
/* 189 */         if (PlatformDependent.javaVersion() >= 7) {
/* 190 */           return InetAddress.getLoopbackAddress();
/*     */         }
/*     */         try {
/* 193 */           return InetAddress.getByName(null);
/*     */         } catch (UnknownHostException e) {
/* 195 */           throw new IllegalStateException(e);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static byte[] hardwareAddressFromNetworkInterface(NetworkInterface intf) throws SocketException {
/*     */     try {
/* 203 */       (byte[])AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public byte[] run() throws SocketException {
/* 206 */           return this.val$intf.getHardwareAddress();
/*     */         }
/*     */       });
/*     */     } catch (PrivilegedActionException e) {
/* 210 */       throw ((SocketException)e.getCause());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\SocketUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */