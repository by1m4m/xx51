/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.eclipse.jetty.io.AbstractConnection;
/*     */ import org.eclipse.jetty.io.ClientConnectionFactory;
/*     */ import org.eclipse.jetty.io.EndPoint;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.Promise;
/*     */ import org.eclipse.jetty.util.log.Log;
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
/*     */ 
/*     */ 
/*     */ public class Socks4Proxy
/*     */   extends ProxyConfiguration.Proxy
/*     */ {
/*     */   public Socks4Proxy(String host, int port)
/*     */   {
/*  43 */     this(new Origin.Address(host, port), false);
/*     */   }
/*     */   
/*     */   public Socks4Proxy(Origin.Address address, boolean secure)
/*     */   {
/*  48 */     super(address, secure);
/*     */   }
/*     */   
/*     */ 
/*     */   public ClientConnectionFactory newClientConnectionFactory(ClientConnectionFactory connectionFactory)
/*     */   {
/*  54 */     return new Socks4ProxyClientConnectionFactory(connectionFactory);
/*     */   }
/*     */   
/*     */   public static class Socks4ProxyClientConnectionFactory implements ClientConnectionFactory
/*     */   {
/*     */     private final ClientConnectionFactory connectionFactory;
/*     */     
/*     */     public Socks4ProxyClientConnectionFactory(ClientConnectionFactory connectionFactory)
/*     */     {
/*  63 */       this.connectionFactory = connectionFactory;
/*     */     }
/*     */     
/*     */     public org.eclipse.jetty.io.Connection newConnection(EndPoint endPoint, Map<String, Object> context)
/*     */       throws IOException
/*     */     {
/*  69 */       HttpDestination destination = (HttpDestination)context.get("http.destination");
/*  70 */       Executor executor = destination.getHttpClient().getExecutor();
/*  71 */       Socks4Proxy.Socks4ProxyConnection connection = new Socks4Proxy.Socks4ProxyConnection(endPoint, executor, this.connectionFactory, context);
/*  72 */       return customize(connection, context);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class Socks4ProxyConnection extends AbstractConnection implements Callback
/*     */   {
/*  78 */     private static final Pattern IPv4_PATTERN = Pattern.compile("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})");
/*  79 */     private static final Logger LOG = Log.getLogger(Socks4ProxyConnection.class);
/*     */     
/*  81 */     private final Socks4Parser parser = new Socks4Parser(null);
/*     */     private final ClientConnectionFactory connectionFactory;
/*     */     private final Map<String, Object> context;
/*     */     
/*     */     public Socks4ProxyConnection(EndPoint endPoint, Executor executor, ClientConnectionFactory connectionFactory, Map<String, Object> context)
/*     */     {
/*  87 */       super(executor);
/*  88 */       this.connectionFactory = connectionFactory;
/*  89 */       this.context = context;
/*     */     }
/*     */     
/*     */ 
/*     */     public void onOpen()
/*     */     {
/*  95 */       super.onOpen();
/*  96 */       writeSocks4Connect();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void writeSocks4Connect()
/*     */     {
/* 105 */       HttpDestination destination = (HttpDestination)this.context.get("http.destination");
/* 106 */       String host = destination.getHost();
/* 107 */       short port = (short)destination.getPort();
/* 108 */       Matcher matcher = IPv4_PATTERN.matcher(host);
/* 109 */       if (matcher.matches())
/*     */       {
/*     */ 
/* 112 */         ByteBuffer buffer = ByteBuffer.allocate(9);
/* 113 */         buffer.put((byte)4).put((byte)1).putShort(port);
/* 114 */         for (int i = 1; i <= 4; i++)
/* 115 */           buffer.put((byte)Integer.parseInt(matcher.group(i)));
/* 116 */         buffer.put((byte)0);
/* 117 */         buffer.flip();
/* 118 */         getEndPoint().write(this, new ByteBuffer[] { buffer });
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 123 */         byte[] hostBytes = host.getBytes(StandardCharsets.UTF_8);
/* 124 */         ByteBuffer buffer = ByteBuffer.allocate(9 + hostBytes.length + 1);
/* 125 */         buffer.put((byte)4).put((byte)1).putShort(port);
/* 126 */         buffer.put((byte)0).put((byte)0).put((byte)0).put((byte)1).put((byte)0);
/* 127 */         buffer.put(hostBytes).put((byte)0);
/* 128 */         buffer.flip();
/* 129 */         getEndPoint().write(this, new ByteBuffer[] { buffer });
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 136 */       if (LOG.isDebugEnabled())
/* 137 */         LOG.debug("Written SOCKS4 connect request", new Object[0]);
/* 138 */       fillInterested();
/*     */     }
/*     */     
/*     */ 
/*     */     public void failed(Throwable x)
/*     */     {
/* 144 */       close();
/*     */       
/* 146 */       Promise<org.eclipse.jetty.client.api.Connection> promise = (Promise)this.context.get("http.connection.promise");
/* 147 */       promise.failed(x);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void onFillable()
/*     */     {
/*     */       try
/*     */       {
/*     */         for (;;)
/*     */         {
/* 159 */           ByteBuffer buffer = BufferUtil.allocate(this.parser.expected());
/* 160 */           int filled = getEndPoint().fill(buffer);
/* 161 */           if (LOG.isDebugEnabled()) {
/* 162 */             LOG.debug("Read SOCKS4 connect response, {} bytes", filled);
/*     */           }
/* 164 */           if (filled < 0) {
/* 165 */             throw new IOException("SOCKS4 tunnel failed, connection closed");
/*     */           }
/* 167 */           if (filled == 0)
/*     */           {
/* 169 */             fillInterested();
/* 170 */             return;
/*     */           }
/*     */           
/* 173 */           if (this.parser.parse(buffer)) {
/* 174 */             return;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 181 */         return;
/*     */       }
/*     */       catch (Throwable x)
/*     */       {
/* 179 */         failed(x);
/*     */       }
/*     */     }
/*     */     
/*     */     private void onSocks4Response(int responseCode) throws IOException
/*     */     {
/* 185 */       if (responseCode == 90) {
/* 186 */         tunnel();
/*     */       } else {
/* 188 */         throw new IOException("SOCKS4 tunnel failed with code " + responseCode);
/*     */       }
/*     */     }
/*     */     
/*     */     private void tunnel()
/*     */     {
/*     */       try {
/* 195 */         HttpDestination destination = (HttpDestination)this.context.get("http.destination");
/* 196 */         HttpClient client = destination.getHttpClient();
/* 197 */         ClientConnectionFactory connectionFactory = this.connectionFactory;
/* 198 */         if (destination.isSecure())
/* 199 */           connectionFactory = client.newSslClientConnectionFactory(connectionFactory);
/* 200 */         org.eclipse.jetty.io.Connection newConnection = connectionFactory.newConnection(getEndPoint(), this.context);
/* 201 */         getEndPoint().upgrade(newConnection);
/* 202 */         if (LOG.isDebugEnabled()) {
/* 203 */           LOG.debug("SOCKS4 tunnel established: {} over {}", new Object[] { this, newConnection });
/*     */         }
/*     */       }
/*     */       catch (Throwable x) {
/* 207 */         failed(x);
/*     */       }
/*     */     }
/*     */     
/*     */     private class Socks4Parser {
/*     */       private static final int EXPECTED_LENGTH = 8;
/*     */       private int cursor;
/*     */       private int response;
/*     */       
/*     */       private Socks4Parser() {}
/*     */       
/*     */       private boolean parse(ByteBuffer buffer) throws IOException {
/* 219 */         while (buffer.hasRemaining())
/*     */         {
/* 221 */           byte current = buffer.get();
/* 222 */           if (this.cursor == 1)
/* 223 */             this.response = (current & 0xFF);
/* 224 */           this.cursor += 1;
/* 225 */           if (this.cursor == 8)
/*     */           {
/* 227 */             Socks4Proxy.Socks4ProxyConnection.this.onSocks4Response(this.response);
/* 228 */             return true;
/*     */           }
/*     */         }
/* 231 */         return false;
/*     */       }
/*     */       
/*     */       private int expected()
/*     */       {
/* 236 */         return 8 - this.cursor;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\Socks4Proxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */