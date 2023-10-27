/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ import java.nio.channels.SelectableChannel;
/*     */ import java.nio.channels.SelectionKey;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.util.Map;
/*     */ import org.eclipse.jetty.io.ClientConnectionFactory;
/*     */ import org.eclipse.jetty.io.EndPoint;
/*     */ import org.eclipse.jetty.io.ManagedSelector;
/*     */ import org.eclipse.jetty.io.SelectorManager;
/*     */ import org.eclipse.jetty.io.SocketChannelEndPoint;
/*     */ import org.eclipse.jetty.util.Promise;
/*     */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*     */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ManagedObject
/*     */ public abstract class AbstractConnectorHttpClientTransport
/*     */   extends AbstractHttpClientTransport
/*     */ {
/*     */   private final int selectors;
/*     */   private SelectorManager selectorManager;
/*     */   
/*     */   protected AbstractConnectorHttpClientTransport(int selectors)
/*     */   {
/*  48 */     this.selectors = selectors;
/*     */   }
/*     */   
/*     */   @ManagedAttribute(value="The number of selectors", readonly=true)
/*     */   public int getSelectors()
/*     */   {
/*  54 */     return this.selectors;
/*     */   }
/*     */   
/*     */   protected void doStart()
/*     */     throws Exception
/*     */   {
/*  60 */     HttpClient httpClient = getHttpClient();
/*  61 */     this.selectorManager = newSelectorManager(httpClient);
/*  62 */     this.selectorManager.setConnectTimeout(httpClient.getConnectTimeout());
/*  63 */     addBean(this.selectorManager);
/*  64 */     super.doStart();
/*     */   }
/*     */   
/*     */   protected void doStop()
/*     */     throws Exception
/*     */   {
/*  70 */     super.doStop();
/*  71 */     removeBean(this.selectorManager);
/*     */   }
/*     */   
/*     */ 
/*     */   public void connect(InetSocketAddress address, Map<String, Object> context)
/*     */   {
/*  77 */     SocketChannel channel = null;
/*     */     try
/*     */     {
/*  80 */       channel = SocketChannel.open();
/*  81 */       HttpDestination destination = (HttpDestination)context.get("http.destination");
/*  82 */       HttpClient client = destination.getHttpClient();
/*  83 */       SocketAddress bindAddress = client.getBindAddress();
/*  84 */       if (bindAddress != null)
/*  85 */         channel.bind(bindAddress);
/*  86 */       configure(client, channel);
/*     */       
/*  88 */       context.put("ssl.peer.host", destination.getHost());
/*  89 */       context.put("ssl.peer.port", Integer.valueOf(destination.getPort()));
/*     */       
/*  91 */       boolean connected = true;
/*  92 */       if (client.isConnectBlocking())
/*     */       {
/*  94 */         channel.socket().connect(address, (int)client.getConnectTimeout());
/*  95 */         channel.configureBlocking(false);
/*     */       }
/*     */       else
/*     */       {
/*  99 */         channel.configureBlocking(false);
/* 100 */         connected = channel.connect(address);
/*     */       }
/* 102 */       if (connected) {
/* 103 */         this.selectorManager.accept(channel, context);
/*     */       } else {
/* 105 */         this.selectorManager.connect(channel, context);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/*     */ 
/* 113 */       if (x.getClass() == SocketException.class) {
/* 114 */         x = new SocketException("Could not connect to " + address).initCause(x);
/*     */       }
/*     */       try
/*     */       {
/* 118 */         if (channel != null) {
/* 119 */           channel.close();
/*     */         }
/*     */       }
/*     */       catch (IOException xx) {
/* 123 */         LOG.ignore(xx);
/*     */       }
/*     */       finally
/*     */       {
/* 127 */         connectFailed(context, x);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void connectFailed(Map<String, Object> context, Throwable x)
/*     */   {
/* 134 */     if (LOG.isDebugEnabled()) {
/* 135 */       LOG.debug("Could not connect to {}", new Object[] { context.get("http.destination") });
/*     */     }
/* 137 */     Promise<org.eclipse.jetty.client.api.Connection> promise = (Promise)context.get("http.connection.promise");
/* 138 */     promise.failed(x);
/*     */   }
/*     */   
/*     */   protected void configure(HttpClient client, SocketChannel channel) throws IOException
/*     */   {
/* 143 */     channel.socket().setTcpNoDelay(client.isTCPNoDelay());
/*     */   }
/*     */   
/*     */   protected SelectorManager newSelectorManager(HttpClient client)
/*     */   {
/* 148 */     return new ClientSelectorManager(client, getSelectors());
/*     */   }
/*     */   
/*     */   protected class ClientSelectorManager extends SelectorManager
/*     */   {
/*     */     private final HttpClient client;
/*     */     
/*     */     protected ClientSelectorManager(HttpClient client, int selectors)
/*     */     {
/* 157 */       super(client.getScheduler(), selectors);
/* 158 */       this.client = client;
/*     */     }
/*     */     
/*     */ 
/*     */     protected EndPoint newEndPoint(SelectableChannel channel, ManagedSelector selector, SelectionKey key)
/*     */     {
/* 164 */       SocketChannelEndPoint endp = new SocketChannelEndPoint(channel, selector, key, getScheduler());
/* 165 */       endp.setIdleTimeout(this.client.getIdleTimeout());
/* 166 */       return endp;
/*     */     }
/*     */     
/*     */ 
/*     */     public org.eclipse.jetty.io.Connection newConnection(SelectableChannel channel, EndPoint endPoint, Object attachment)
/*     */       throws IOException
/*     */     {
/* 173 */       Map<String, Object> context = (Map)attachment;
/* 174 */       HttpDestination destination = (HttpDestination)context.get("http.destination");
/* 175 */       return destination.getClientConnectionFactory().newConnection(endPoint, context);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected void connectionFailed(SelectableChannel channel, Throwable x, Object attachment)
/*     */     {
/* 182 */       Map<String, Object> context = (Map)attachment;
/* 183 */       AbstractConnectorHttpClientTransport.this.connectFailed(context, x);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\AbstractConnectorHttpClientTransport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */