/*     */ package org.apache.http.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
/*     */ import org.apache.http.params.HttpConnectionParams;
/*     */ import org.apache.http.params.HttpParams;
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
/*     */ public class DefaultHttpClientConnection
/*     */   extends SocketHttpClientConnection
/*     */ {
/*     */   public void bind(Socket socket, HttpParams params)
/*     */     throws IOException
/*     */   {
/*  79 */     if (socket == null) {
/*  80 */       throw new IllegalArgumentException("Socket may not be null");
/*     */     }
/*  82 */     if (params == null) {
/*  83 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/*     */     }
/*  85 */     assertNotOpen();
/*  86 */     socket.setTcpNoDelay(HttpConnectionParams.getTcpNoDelay(params));
/*  87 */     socket.setSoTimeout(HttpConnectionParams.getSoTimeout(params));
/*     */     
/*  89 */     int linger = HttpConnectionParams.getLinger(params);
/*  90 */     if (linger >= 0) {
/*  91 */       socket.setSoLinger(linger > 0, linger);
/*     */     }
/*  93 */     super.bind(socket, params);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  97 */     StringBuffer buffer = new StringBuffer();
/*  98 */     buffer.append("[");
/*  99 */     if (isOpen()) {
/* 100 */       buffer.append(getRemotePort());
/*     */     } else {
/* 102 */       buffer.append("closed");
/*     */     }
/* 104 */     buffer.append("]");
/* 105 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\DefaultHttpClientConnection.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */