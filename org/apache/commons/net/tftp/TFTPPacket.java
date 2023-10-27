/*     */ package org.apache.commons.net.tftp;
/*     */ 
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.InetAddress;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TFTPPacket
/*     */ {
/*     */   static final int MIN_PACKET_SIZE = 4;
/*     */   public static final int READ_REQUEST = 1;
/*     */   public static final int WRITE_REQUEST = 2;
/*     */   public static final int DATA = 3;
/*     */   public static final int ACKNOWLEDGEMENT = 4;
/*     */   public static final int ERROR = 5;
/*     */   public static final int SEGMENT_SIZE = 512;
/*     */   int _type;
/*     */   int _port;
/*     */   InetAddress _address;
/*     */   
/*     */   public static final TFTPPacket newTFTPPacket(DatagramPacket datagram)
/*     */     throws TFTPPacketException
/*     */   {
/* 128 */     TFTPPacket packet = null;
/*     */     
/* 130 */     if (datagram.getLength() < 4) {
/* 131 */       throw new TFTPPacketException("Bad packet. Datagram data length is too short.");
/*     */     }
/*     */     
/*     */ 
/* 135 */     byte[] data = datagram.getData();
/*     */     
/* 137 */     switch (data[1])
/*     */     {
/*     */     case 1: 
/* 140 */       packet = new TFTPReadRequestPacket(datagram);
/* 141 */       break;
/*     */     case 2: 
/* 143 */       packet = new TFTPWriteRequestPacket(datagram);
/* 144 */       break;
/*     */     case 3: 
/* 146 */       packet = new TFTPDataPacket(datagram);
/* 147 */       break;
/*     */     case 4: 
/* 149 */       packet = new TFTPAckPacket(datagram);
/* 150 */       break;
/*     */     case 5: 
/* 152 */       packet = new TFTPErrorPacket(datagram);
/* 153 */       break;
/*     */     default: 
/* 155 */       throw new TFTPPacketException("Bad packet.  Invalid TFTP operator code.");
/*     */     }
/*     */     
/*     */     
/* 159 */     return packet;
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
/*     */   TFTPPacket(int type, InetAddress address, int port)
/*     */   {
/* 172 */     this._type = type;
/* 173 */     this._address = address;
/* 174 */     this._port = port;
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
/*     */   abstract DatagramPacket _newDatagram(DatagramPacket paramDatagramPacket, byte[] paramArrayOfByte);
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
/*     */   public abstract DatagramPacket newDatagram();
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
/*     */   public final int getType()
/*     */   {
/* 211 */     return this._type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final InetAddress getAddress()
/*     */   {
/* 222 */     return this._address;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getPort()
/*     */   {
/* 233 */     return this._port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setPort(int port)
/*     */   {
/* 242 */     this._port = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setAddress(InetAddress address)
/*     */   {
/* 250 */     this._address = address;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 259 */     return this._address + " " + this._port + " " + this._type;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\tftp\TFTPPacket.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */