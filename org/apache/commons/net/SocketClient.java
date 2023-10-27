/*     */ package org.apache.commons.net;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.nio.charset.Charset;
/*     */ import javax.net.ServerSocketFactory;
/*     */ import javax.net.SocketFactory;
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
/*     */ public abstract class SocketClient
/*     */ {
/*     */   public static final String NETASCII_EOL = "\r\n";
/*  64 */   private static final SocketFactory __DEFAULT_SOCKET_FACTORY = ;
/*     */   
/*     */ 
/*     */ 
/*  68 */   private static final ServerSocketFactory __DEFAULT_SERVER_SOCKET_FACTORY = ServerSocketFactory.getDefault();
/*     */   
/*     */ 
/*     */ 
/*     */   private ProtocolCommandSupport __commandSupport;
/*     */   
/*     */ 
/*     */ 
/*     */   protected int _timeout_;
/*     */   
/*     */ 
/*     */   protected Socket _socket_;
/*     */   
/*     */ 
/*     */   protected String _hostname_;
/*     */   
/*     */ 
/*     */   protected int _defaultPort_;
/*     */   
/*     */ 
/*     */   protected InputStream _input_;
/*     */   
/*     */ 
/*     */   protected OutputStream _output_;
/*     */   
/*     */ 
/*     */   protected SocketFactory _socketFactory_;
/*     */   
/*     */ 
/*     */   protected ServerSocketFactory _serverSocketFactory_;
/*     */   
/*     */ 
/*     */   private static final int DEFAULT_CONNECT_TIMEOUT = 0;
/*     */   
/*     */ 
/* 103 */   protected int connectTimeout = 0;
/*     */   
/*     */ 
/* 106 */   private int receiveBufferSize = -1;
/*     */   
/*     */ 
/* 109 */   private int sendBufferSize = -1;
/*     */   
/*     */ 
/*     */ 
/*     */   private Proxy connProxy;
/*     */   
/*     */ 
/*     */ 
/* 117 */   private Charset charset = Charset.defaultCharset();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SocketClient()
/*     */   {
/* 128 */     this._socket_ = null;
/* 129 */     this._hostname_ = null;
/* 130 */     this._input_ = null;
/* 131 */     this._output_ = null;
/* 132 */     this._timeout_ = 0;
/* 133 */     this._defaultPort_ = 0;
/* 134 */     this._socketFactory_ = __DEFAULT_SOCKET_FACTORY;
/* 135 */     this._serverSocketFactory_ = __DEFAULT_SERVER_SOCKET_FACTORY;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _connectAction_()
/*     */     throws IOException
/*     */   {
/* 158 */     this._socket_.setSoTimeout(this._timeout_);
/* 159 */     this._input_ = this._socket_.getInputStream();
/* 160 */     this._output_ = this._socket_.getOutputStream();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void connect(InetAddress host, int port)
/*     */     throws SocketException, IOException
/*     */   {
/* 180 */     this._hostname_ = null;
/* 181 */     _connect(host, port, null, -1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void connect(String hostname, int port)
/*     */     throws SocketException, IOException
/*     */   {
/* 201 */     this._hostname_ = hostname;
/* 202 */     _connect(InetAddress.getByName(hostname), port, null, -1);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void connect(InetAddress host, int port, InetAddress localAddr, int localPort)
/*     */     throws SocketException, IOException
/*     */   {
/* 225 */     this._hostname_ = null;
/* 226 */     _connect(host, port, localAddr, localPort);
/*     */   }
/*     */   
/*     */ 
/*     */   private void _connect(InetAddress host, int port, InetAddress localAddr, int localPort)
/*     */     throws SocketException, IOException
/*     */   {
/* 233 */     this._socket_ = this._socketFactory_.createSocket();
/* 234 */     if (this.receiveBufferSize != -1) {
/* 235 */       this._socket_.setReceiveBufferSize(this.receiveBufferSize);
/*     */     }
/* 237 */     if (this.sendBufferSize != -1) {
/* 238 */       this._socket_.setSendBufferSize(this.sendBufferSize);
/*     */     }
/* 240 */     if (localAddr != null) {
/* 241 */       this._socket_.bind(new InetSocketAddress(localAddr, localPort));
/*     */     }
/* 243 */     this._socket_.connect(new InetSocketAddress(host, port), this.connectTimeout);
/* 244 */     _connectAction_();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void connect(String hostname, int port, InetAddress localAddr, int localPort)
/*     */     throws SocketException, IOException
/*     */   {
/* 267 */     this._hostname_ = hostname;
/* 268 */     _connect(InetAddress.getByName(hostname), port, localAddr, localPort);
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
/*     */ 
/*     */   public void connect(InetAddress host)
/*     */     throws SocketException, IOException
/*     */   {
/* 286 */     this._hostname_ = null;
/* 287 */     connect(host, this._defaultPort_);
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
/*     */ 
/*     */ 
/*     */   public void connect(String hostname)
/*     */     throws SocketException, IOException
/*     */   {
/* 306 */     connect(hostname, this._defaultPort_);
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
/*     */   public void disconnect()
/*     */     throws IOException
/*     */   {
/* 322 */     closeQuietly(this._socket_);
/* 323 */     closeQuietly(this._input_);
/* 324 */     closeQuietly(this._output_);
/* 325 */     this._socket_ = null;
/* 326 */     this._hostname_ = null;
/* 327 */     this._input_ = null;
/* 328 */     this._output_ = null;
/*     */   }
/*     */   
/*     */   private void closeQuietly(Socket socket) {
/* 332 */     if (socket != null) {
/*     */       try {
/* 334 */         socket.close();
/*     */       }
/*     */       catch (IOException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */   private void closeQuietly(Closeable close)
/*     */   {
/* 342 */     if (close != null) {
/*     */       try {
/* 344 */         close.close();
/*     */       }
/*     */       catch (IOException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isConnected()
/*     */   {
/* 359 */     if (this._socket_ == null) {
/* 360 */       return false;
/*     */     }
/*     */     
/* 363 */     return this._socket_.isConnected();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAvailable()
/*     */   {
/* 375 */     if (isConnected())
/*     */     {
/*     */       try {
/* 378 */         if (this._socket_.getInetAddress() == null) {
/* 379 */           return false;
/*     */         }
/* 381 */         if (this._socket_.getPort() == 0) {
/* 382 */           return false;
/*     */         }
/* 384 */         if (this._socket_.getRemoteSocketAddress() == null) {
/* 385 */           return false;
/*     */         }
/* 387 */         if (this._socket_.isClosed()) {
/* 388 */           return false;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 393 */         if (this._socket_.isInputShutdown()) {
/* 394 */           return false;
/*     */         }
/* 396 */         if (this._socket_.isOutputShutdown()) {
/* 397 */           return false;
/*     */         }
/*     */         
/* 400 */         this._socket_.getInputStream();
/* 401 */         this._socket_.getOutputStream();
/*     */       }
/*     */       catch (IOException ioex)
/*     */       {
/* 405 */         return false;
/*     */       }
/* 407 */       return true;
/*     */     }
/* 409 */     return false;
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
/*     */   public void setDefaultPort(int port)
/*     */   {
/* 423 */     this._defaultPort_ = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getDefaultPort()
/*     */   {
/* 434 */     return this._defaultPort_;
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
/*     */ 
/*     */   public void setDefaultTimeout(int timeout)
/*     */   {
/* 451 */     this._timeout_ = timeout;
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
/*     */   public int getDefaultTimeout()
/*     */   {
/* 464 */     return this._timeout_;
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
/*     */ 
/*     */   public void setSoTimeout(int timeout)
/*     */     throws SocketException
/*     */   {
/* 482 */     this._socket_.setSoTimeout(timeout);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSendBufferSize(int size)
/*     */     throws SocketException
/*     */   {
/* 494 */     this.sendBufferSize = size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getSendBufferSize()
/*     */   {
/* 503 */     return this.sendBufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReceiveBufferSize(int size)
/*     */     throws SocketException
/*     */   {
/* 514 */     this.receiveBufferSize = size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getReceiveBufferSize()
/*     */   {
/* 523 */     return this.receiveBufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSoTimeout()
/*     */     throws SocketException
/*     */   {
/* 535 */     return this._socket_.getSoTimeout();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTcpNoDelay(boolean on)
/*     */     throws SocketException
/*     */   {
/* 548 */     this._socket_.setTcpNoDelay(on);
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
/*     */   public boolean getTcpNoDelay()
/*     */     throws SocketException
/*     */   {
/* 563 */     return this._socket_.getTcpNoDelay();
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
/*     */   public void setKeepAlive(boolean keepAlive)
/*     */     throws SocketException
/*     */   {
/* 579 */     this._socket_.setKeepAlive(keepAlive);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getKeepAlive()
/*     */     throws SocketException
/*     */   {
/* 591 */     return this._socket_.getKeepAlive();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSoLinger(boolean on, int val)
/*     */     throws SocketException
/*     */   {
/* 604 */     this._socket_.setSoLinger(on, val);
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
/*     */   public int getSoLinger()
/*     */     throws SocketException
/*     */   {
/* 618 */     return this._socket_.getSoLinger();
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
/*     */   public int getLocalPort()
/*     */   {
/* 633 */     return this._socket_.getLocalPort();
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
/*     */   public InetAddress getLocalAddress()
/*     */   {
/* 646 */     return this._socket_.getLocalAddress();
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
/*     */   public int getRemotePort()
/*     */   {
/* 660 */     return this._socket_.getPort();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InetAddress getRemoteAddress()
/*     */   {
/* 671 */     return this._socket_.getInetAddress();
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
/*     */ 
/*     */ 
/*     */   public boolean verifyRemote(Socket socket)
/*     */   {
/* 689 */     InetAddress host1 = socket.getInetAddress();
/* 690 */     InetAddress host2 = getRemoteAddress();
/*     */     
/* 692 */     return host1.equals(host2);
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
/*     */   public void setSocketFactory(SocketFactory factory)
/*     */   {
/* 707 */     if (factory == null) {
/* 708 */       this._socketFactory_ = __DEFAULT_SOCKET_FACTORY;
/*     */     } else {
/* 710 */       this._socketFactory_ = factory;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 715 */     this.connProxy = null;
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
/*     */   public void setServerSocketFactory(ServerSocketFactory factory)
/*     */   {
/* 728 */     if (factory == null) {
/* 729 */       this._serverSocketFactory_ = __DEFAULT_SERVER_SOCKET_FACTORY;
/*     */     } else {
/* 731 */       this._serverSocketFactory_ = factory;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setConnectTimeout(int connectTimeout)
/*     */   {
/* 742 */     this.connectTimeout = connectTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getConnectTimeout()
/*     */   {
/* 751 */     return this.connectTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServerSocketFactory getServerSocketFactory()
/*     */   {
/* 760 */     return this._serverSocketFactory_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addProtocolCommandListener(ProtocolCommandListener listener)
/*     */   {
/* 771 */     getCommandSupport().addProtocolCommandListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeProtocolCommandListener(ProtocolCommandListener listener)
/*     */   {
/* 781 */     getCommandSupport().removeProtocolCommandListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fireReplyReceived(int replyCode, String reply)
/*     */   {
/* 792 */     if (getCommandSupport().getListenerCount() > 0) {
/* 793 */       getCommandSupport().fireReplyReceived(replyCode, reply);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void fireCommandSent(String command, String message)
/*     */   {
/* 805 */     if (getCommandSupport().getListenerCount() > 0) {
/* 806 */       getCommandSupport().fireCommandSent(command, message);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void createCommandSupport()
/*     */   {
/* 814 */     this.__commandSupport = new ProtocolCommandSupport(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ProtocolCommandSupport getCommandSupport()
/*     */   {
/* 825 */     return this.__commandSupport;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProxy(Proxy proxy)
/*     */   {
/* 837 */     setSocketFactory(new DefaultSocketFactory(proxy));
/* 838 */     this.connProxy = proxy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Proxy getProxy()
/*     */   {
/* 846 */     return this.connProxy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String getCharsetName()
/*     */   {
/* 858 */     return this.charset.name();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Charset getCharset()
/*     */   {
/* 868 */     return this.charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharset(Charset charset)
/*     */   {
/* 878 */     this.charset = charset;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\SocketClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */