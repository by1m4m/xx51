/*     */ package org.apache.http.impl.conn;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ConnectException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.annotation.ThreadSafe;
/*     */ import org.apache.http.conn.ClientConnectionOperator;
/*     */ import org.apache.http.conn.HttpHostConnectException;
/*     */ import org.apache.http.conn.OperatedClientConnection;
/*     */ import org.apache.http.conn.scheme.LayeredSocketFactory;
/*     */ import org.apache.http.conn.scheme.Scheme;
/*     */ import org.apache.http.conn.scheme.SchemeRegistry;
/*     */ import org.apache.http.conn.scheme.SocketFactory;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.protocol.HttpContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ThreadSafe
/*     */ public class DefaultClientConnectionOperator
/*     */   implements ClientConnectionOperator
/*     */ {
/*     */   protected final SchemeRegistry schemeRegistry;
/*     */   
/*     */   public DefaultClientConnectionOperator(SchemeRegistry schemes)
/*     */   {
/*  79 */     if (schemes == null) {
/*  80 */       throw new IllegalArgumentException("Scheme registry must not be null.");
/*     */     }
/*     */     
/*  83 */     this.schemeRegistry = schemes;
/*     */   }
/*     */   
/*     */   public OperatedClientConnection createConnection() {
/*  87 */     return new DefaultClientConnection();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void openConnection(OperatedClientConnection conn, HttpHost target, InetAddress local, HttpContext context, HttpParams params)
/*     */     throws IOException
/*     */   {
/*  97 */     if (conn == null) {
/*  98 */       throw new IllegalArgumentException("Connection must not be null.");
/*     */     }
/*     */     
/* 101 */     if (target == null) {
/* 102 */       throw new IllegalArgumentException("Target host must not be null.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 107 */     if (params == null) {
/* 108 */       throw new IllegalArgumentException("Parameters must not be null.");
/*     */     }
/*     */     
/* 111 */     if (conn.isOpen()) {
/* 112 */       throw new IllegalArgumentException("Connection must not be open.");
/*     */     }
/*     */     
/*     */ 
/* 116 */     Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
/* 117 */     SocketFactory sf = schm.getSocketFactory();
/*     */     
/* 119 */     Socket sock = sf.createSocket();
/* 120 */     conn.opening(sock, target);
/*     */     try
/*     */     {
/* 123 */       sock = sf.connectSocket(sock, target.getHostName(), schm.resolvePort(target.getPort()), local, 0, params);
/*     */     }
/*     */     catch (ConnectException ex)
/*     */     {
/* 127 */       throw new HttpHostConnectException(target, ex);
/*     */     }
/* 129 */     prepareSocket(sock, context, params);
/* 130 */     conn.openCompleted(sf.isSecure(sock), params);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateSecureConnection(OperatedClientConnection conn, HttpHost target, HttpContext context, HttpParams params)
/*     */     throws IOException
/*     */   {
/* 140 */     if (conn == null) {
/* 141 */       throw new IllegalArgumentException("Connection must not be null.");
/*     */     }
/*     */     
/* 144 */     if (target == null) {
/* 145 */       throw new IllegalArgumentException("Target host must not be null.");
/*     */     }
/*     */     
/* 148 */     if (params == null) {
/* 149 */       throw new IllegalArgumentException("Parameters must not be null.");
/*     */     }
/*     */     
/* 152 */     if (!conn.isOpen()) {
/* 153 */       throw new IllegalArgumentException("Connection must be open.");
/*     */     }
/*     */     
/*     */ 
/* 157 */     Scheme schm = this.schemeRegistry.getScheme(target.getSchemeName());
/* 158 */     if (!(schm.getSocketFactory() instanceof LayeredSocketFactory)) {
/* 159 */       throw new IllegalArgumentException("Target scheme (" + schm.getName() + ") must have layered socket factory.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 164 */     LayeredSocketFactory lsf = (LayeredSocketFactory)schm.getSocketFactory();
/*     */     Socket sock;
/*     */     try {
/* 167 */       sock = lsf.createSocket(conn.getSocket(), target.getHostName(), target.getPort(), true);
/*     */     }
/*     */     catch (ConnectException ex) {
/* 170 */       throw new HttpHostConnectException(target, ex);
/*     */     }
/* 172 */     prepareSocket(sock, context, params);
/* 173 */     conn.update(sock, target, lsf.isSecure(sock), params);
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
/*     */   protected void prepareSocket(Socket sock, HttpContext context, HttpParams params)
/*     */     throws IOException
/*     */   {
/* 189 */     sock.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(params));
/* 190 */     sock.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
/*     */     
/* 192 */     int linger = HttpConnectionParams.getLinger(params);
/* 193 */     if (linger >= 0) {
/* 194 */       sock.setSoLinger(linger > 0, linger);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\conn\DefaultClientConnectionOperator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */