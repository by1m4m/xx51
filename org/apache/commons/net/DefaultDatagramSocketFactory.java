/*    */ package org.apache.commons.net;
/*    */ 
/*    */ import java.net.DatagramSocket;
/*    */ import java.net.InetAddress;
/*    */ import java.net.SocketException;
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
/*    */ public class DefaultDatagramSocketFactory
/*    */   implements DatagramSocketFactory
/*    */ {
/*    */   public DatagramSocket createDatagramSocket()
/*    */     throws SocketException
/*    */   {
/* 48 */     return new DatagramSocket();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DatagramSocket createDatagramSocket(int port)
/*    */     throws SocketException
/*    */   {
/* 61 */     return new DatagramSocket(port);
/*    */   }
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
/*    */   public DatagramSocket createDatagramSocket(int port, InetAddress laddr)
/*    */     throws SocketException
/*    */   {
/* 77 */     return new DatagramSocket(port, laddr);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\DefaultDatagramSocketFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */