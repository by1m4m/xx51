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
/*    */ public final class TFTPWriteRequestPacket
/*    */   extends TFTPRequestPacket
/*    */ {
/*    */   public TFTPWriteRequestPacket(InetAddress destination, int port, String filename, int mode)
/*    */   {
/* 61 */     super(destination, port, 2, filename, mode);
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
/*    */   TFTPWriteRequestPacket(DatagramPacket datagram)
/*    */     throws TFTPPacketException
/*    */   {
/* 76 */     super(2, datagram);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 85 */     return super.toString() + " WRQ " + getFilename() + " " + TFTP.getModeName(getMode());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\tftp\TFTPWriteRequestPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */