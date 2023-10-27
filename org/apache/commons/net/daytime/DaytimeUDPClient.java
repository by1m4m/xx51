/*    */ package org.apache.commons.net.daytime;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.DatagramSocket;
/*    */ import java.net.InetAddress;
/*    */ import org.apache.commons.net.DatagramSocketClient;
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
/*    */ public final class DaytimeUDPClient
/*    */   extends DatagramSocketClient
/*    */ {
/*    */   public static final int DEFAULT_PORT = 13;
/* 46 */   private final byte[] __dummyData = new byte[1];
/*    */   
/* 48 */   private final byte[] __timeData = new byte['Ā'];
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
/*    */   public String getTime(InetAddress host, int port)
/*    */     throws IOException
/*    */   {
/* 63 */     DatagramPacket sendPacket = new DatagramPacket(this.__dummyData, this.__dummyData.length, host, port);
/*    */     
/* 65 */     DatagramPacket receivePacket = new DatagramPacket(this.__timeData, this.__timeData.length);
/*    */     
/* 67 */     this._socket_.send(sendPacket);
/* 68 */     this._socket_.receive(receivePacket);
/*    */     
/* 70 */     return new String(receivePacket.getData(), 0, receivePacket.getLength(), getCharsetName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getTime(InetAddress host)
/*    */     throws IOException
/*    */   {
/* 80 */     return getTime(host, 13);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\daytime\DaytimeUDPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */