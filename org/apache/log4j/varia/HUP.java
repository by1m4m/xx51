/*     */ package org.apache.log4j.varia;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import org.apache.log4j.helpers.LogLog;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class HUP
/*     */   extends Thread
/*     */ {
/*     */   int port;
/*     */   ExternallyRolledFileAppender er;
/*     */   
/*     */   HUP(ExternallyRolledFileAppender er, int port)
/*     */   {
/* 115 */     this.er = er;
/* 116 */     this.port = port;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/* 121 */     while (!isInterrupted()) {
/*     */       try {
/* 123 */         ServerSocket serverSocket = new ServerSocket(this.port);
/*     */         for (;;) {
/* 125 */           Socket socket = serverSocket.accept();
/* 126 */           LogLog.debug("Connected to client at " + socket.getInetAddress());
/* 127 */           new Thread(new HUPNode(socket, this.er), "ExternallyRolledFileAppender-HUP").start();
/*     */         }
/*     */       } catch (InterruptedIOException e) {
/* 130 */         Thread.currentThread().interrupt();
/* 131 */         e.printStackTrace();
/*     */       } catch (IOException e) {
/* 133 */         e.printStackTrace();
/*     */       } catch (RuntimeException e) {
/* 135 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\varia\HUP.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */