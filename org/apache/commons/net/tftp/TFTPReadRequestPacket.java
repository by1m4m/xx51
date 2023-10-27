/*    */ package org.apache.commons.net.tftp;
/*    */ 
/*    */ import java.net.DatagramPacket;
/*    */ import java.net.InetAddress;
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
/*    */ public final class TFTPReadRequestPacket
/*    */   extends TFTPRequestPacket
/*    */ {
/*    */   public TFTPReadRequestPacket(InetAddress destination, int port, String filename, int mode)
/*    */   {
/* 61 */     super(destination, port, 1, filename, mode);
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
/*    */   TFTPReadRequestPacket(DatagramPacket datagram)
/*    */     throws TFTPPacketException
/*    */   {
/* 76 */     super(1, datagram);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 85 */     return super.toString() + " RRQ " + getFilename() + " " + TFTP.getModeName(getMode());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\tftp\TFTPReadRequestPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */