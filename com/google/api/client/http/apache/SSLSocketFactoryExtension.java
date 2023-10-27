/*    */ package com.google.api.client.http.apache;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.Socket;
/*    */ import java.net.UnknownHostException;
/*    */ import java.security.KeyManagementException;
/*    */ import java.security.KeyStore;
/*    */ import java.security.KeyStoreException;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.UnrecoverableKeyException;
/*    */ import javax.net.ssl.SSLContext;
/*    */ import javax.net.ssl.SSLSocket;
/*    */ import org.apache.http.conn.ssl.X509HostnameVerifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class SSLSocketFactoryExtension
/*    */   extends org.apache.http.conn.ssl.SSLSocketFactory
/*    */ {
/*    */   private final javax.net.ssl.SSLSocketFactory socketFactory;
/*    */   
/*    */   SSLSocketFactoryExtension(SSLContext sslContext)
/*    */     throws KeyManagementException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException
/*    */   {
/* 47 */     super((KeyStore)null);
/* 48 */     this.socketFactory = sslContext.getSocketFactory();
/*    */   }
/*    */   
/*    */   public Socket createSocket() throws IOException
/*    */   {
/* 53 */     return this.socketFactory.createSocket();
/*    */   }
/*    */   
/*    */   public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
/*    */     throws IOException, UnknownHostException
/*    */   {
/* 59 */     SSLSocket sslSocket = (SSLSocket)this.socketFactory.createSocket(socket, host, port, autoClose);
/* 60 */     getHostnameVerifier().verify(host, sslSocket);
/* 61 */     return sslSocket;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\apache\SSLSocketFactoryExtension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */