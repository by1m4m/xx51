/*     */ package org.apache.commons.net.tftp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.commons.net.io.FromNetASCIIOutputStream;
/*     */ import org.apache.commons.net.io.ToNetASCIIInputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TFTPClient
/*     */   extends TFTP
/*     */ {
/*     */   public static final int DEFAULT_MAX_TIMEOUTS = 5;
/*     */   private int __maxTimeouts;
/*  69 */   private long totalBytesReceived = 0L;
/*     */   
/*     */ 
/*  72 */   private long totalBytesSent = 0L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TFTPClient()
/*     */   {
/*  81 */     this.__maxTimeouts = 5;
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
/*     */   public void setMaxTimeouts(int numTimeouts)
/*     */   {
/*  96 */     if (numTimeouts < 1) {
/*  97 */       this.__maxTimeouts = 1;
/*     */     } else {
/*  99 */       this.__maxTimeouts = numTimeouts;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxTimeouts()
/*     */   {
/* 111 */     return this.__maxTimeouts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getTotalBytesReceived()
/*     */   {
/* 119 */     return this.totalBytesReceived;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getTotalBytesSent()
/*     */   {
/* 126 */     return this.totalBytesSent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int receiveFile(String filename, int mode, OutputStream output, InetAddress host, int port)
/*     */     throws IOException
/*     */   {
/* 149 */     int bytesRead = 0;
/* 150 */     int lastBlock = 0;
/* 151 */     int block = 1;
/* 152 */     int hostPort = 0;
/* 153 */     int dataLength = 0;
/*     */     
/* 155 */     this.totalBytesReceived = 0L;
/*     */     
/* 157 */     if (mode == 0) {
/* 158 */       output = new FromNetASCIIOutputStream(output);
/*     */     }
/*     */     
/* 161 */     TFTPPacket sent = new TFTPReadRequestPacket(host, port, filename, mode);
/* 162 */     TFTPAckPacket ack = new TFTPAckPacket(host, port, 0);
/*     */     
/* 164 */     beginBufferedOps();
/*     */     
/* 166 */     boolean justStarted = true;
/*     */     try {
/*     */       do {
/* 169 */         bufferedSend(sent);
/* 170 */         boolean wantReply = true;
/* 171 */         int timeouts = 0;
/*     */         do {
/*     */           try {
/* 174 */             TFTPPacket received = bufferedReceive();
/*     */             
/*     */ 
/* 177 */             int recdPort = received.getPort();
/* 178 */             InetAddress recdAddress = received.getAddress();
/* 179 */             if (justStarted) {
/* 180 */               justStarted = false;
/* 181 */               if (recdPort == port) {
/* 182 */                 TFTPErrorPacket error = new TFTPErrorPacket(recdAddress, recdPort, 5, "INCORRECT SOURCE PORT");
/*     */                 
/*     */ 
/* 185 */                 bufferedSend(error);
/* 186 */                 throw new IOException("Incorrect source port (" + recdPort + ") in request reply.");
/*     */               }
/* 188 */               hostPort = recdPort;
/* 189 */               ack.setPort(hostPort);
/* 190 */               if (!host.equals(recdAddress))
/*     */               {
/* 192 */                 host = recdAddress;
/* 193 */                 ack.setAddress(host);
/* 194 */                 sent.setAddress(host);
/*     */               }
/*     */             }
/*     */             
/*     */ 
/* 199 */             if ((host.equals(recdAddress)) && (recdPort == hostPort)) { TFTPErrorPacket error;
/* 200 */               switch (received.getType())
/*     */               {
/*     */               case 5: 
/* 203 */                 error = (TFTPErrorPacket)received;
/* 204 */                 throw new IOException("Error code " + error.getError() + " received: " + error.getMessage());
/*     */               
/*     */               case 3: 
/* 207 */                 TFTPDataPacket data = (TFTPDataPacket)received;
/* 208 */                 dataLength = data.getDataLength();
/* 209 */                 lastBlock = data.getBlockNumber();
/*     */                 
/* 211 */                 if (lastBlock == block) {
/*     */                   try {
/* 213 */                     output.write(data.getData(), data.getDataOffset(), dataLength);
/*     */                   } catch (IOException e) {
/* 215 */                     error = new TFTPErrorPacket(host, hostPort, 3, "File write failed.");
/*     */                     
/*     */ 
/* 218 */                     bufferedSend(error);
/* 219 */                     throw e;
/*     */                   }
/* 221 */                   block++;
/* 222 */                   if (block > 65535)
/*     */                   {
/* 224 */                     block = 0;
/*     */                   }
/* 226 */                   wantReply = false;
/*     */                 } else {
/* 228 */                   discardPackets();
/* 229 */                   if (lastBlock == (block == 0 ? 65535 : block - 1)) {
/* 230 */                     wantReply = false;
/*     */                   }
/*     */                 }
/*     */                 
/*     */                 break;
/*     */               default: 
/* 236 */                 throw new IOException("Received unexpected packet type (" + received.getType() + ")");
/*     */               }
/*     */             } else {
/* 239 */               TFTPErrorPacket error = new TFTPErrorPacket(recdAddress, recdPort, 5, "Unexpected host or port.");
/*     */               
/*     */ 
/* 242 */               bufferedSend(error);
/*     */             }
/*     */           } catch (SocketException e) {
/* 245 */             timeouts++; if (timeouts >= this.__maxTimeouts) {
/* 246 */               throw new IOException("Connection timed out.");
/*     */             }
/*     */           } catch (InterruptedIOException e) {
/* 249 */             timeouts++; if (timeouts >= this.__maxTimeouts) {
/* 250 */               throw new IOException("Connection timed out.");
/*     */             }
/*     */           } catch (TFTPPacketException e) {
/* 253 */             throw new IOException("Bad packet: " + e.getMessage());
/*     */           }
/* 255 */         } while (wantReply);
/*     */         
/* 257 */         ack.setBlockNumber(lastBlock);
/* 258 */         sent = ack;
/* 259 */         bytesRead += dataLength;
/* 260 */         this.totalBytesReceived += dataLength;
/* 261 */       } while (dataLength == 512);
/* 262 */       bufferedSend(sent);
/*     */     } finally {
/* 264 */       endBufferedOps();
/*     */     }
/* 266 */     return bytesRead;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int receiveFile(String filename, int mode, OutputStream output, String hostname, int port)
/*     */     throws UnknownHostException, IOException
/*     */   {
/* 292 */     return receiveFile(filename, mode, output, InetAddress.getByName(hostname), port);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int receiveFile(String filename, int mode, OutputStream output, InetAddress host)
/*     */     throws IOException
/*     */   {
/* 312 */     return receiveFile(filename, mode, output, host, 69);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public int receiveFile(String filename, int mode, OutputStream output, String hostname)
/*     */     throws UnknownHostException, IOException
/*     */   {
/* 331 */     return receiveFile(filename, mode, output, InetAddress.getByName(hostname), 69);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendFile(String filename, int mode, InputStream input, InetAddress host, int port)
/*     */     throws IOException
/*     */   {
/* 356 */     int block = 0;
/* 357 */     int hostPort = 0;
/* 358 */     boolean justStarted = true;
/* 359 */     boolean lastAckWait = false;
/*     */     
/* 361 */     this.totalBytesSent = 0L;
/*     */     
/* 363 */     if (mode == 0) {
/* 364 */       input = new ToNetASCIIInputStream(input);
/*     */     }
/*     */     
/* 367 */     TFTPPacket sent = new TFTPWriteRequestPacket(host, port, filename, mode);
/* 368 */     TFTPDataPacket data = new TFTPDataPacket(host, port, 0, this._sendBuffer, 4, 0);
/*     */     
/* 370 */     beginBufferedOps();
/*     */     
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/* 376 */         bufferedSend(sent);
/* 377 */         boolean wantReply = true;
/* 378 */         int timeouts = 0;
/*     */         do {
/*     */           try {
/* 381 */             TFTPPacket received = bufferedReceive();
/* 382 */             InetAddress recdAddress = received.getAddress();
/* 383 */             int recdPort = received.getPort();
/*     */             
/*     */ 
/* 386 */             if (justStarted) {
/* 387 */               justStarted = false;
/* 388 */               if (recdPort == port) {
/* 389 */                 TFTPErrorPacket error = new TFTPErrorPacket(recdAddress, recdPort, 5, "INCORRECT SOURCE PORT");
/*     */                 
/*     */ 
/* 392 */                 bufferedSend(error);
/* 393 */                 throw new IOException("Incorrect source port (" + recdPort + ") in request reply.");
/*     */               }
/* 395 */               hostPort = recdPort;
/* 396 */               data.setPort(hostPort);
/* 397 */               if (!host.equals(recdAddress)) {
/* 398 */                 host = recdAddress;
/* 399 */                 data.setAddress(host);
/* 400 */                 sent.setAddress(host);
/*     */               }
/*     */             }
/*     */             
/*     */ 
/* 405 */             if ((host.equals(recdAddress)) && (recdPort == hostPort))
/*     */             {
/* 407 */               switch (received.getType()) {
/*     */               case 5: 
/* 409 */                 TFTPErrorPacket error = (TFTPErrorPacket)received;
/* 410 */                 throw new IOException("Error code " + error.getError() + " received: " + error.getMessage());
/*     */               
/*     */ 
/*     */               case 4: 
/* 414 */                 int lastBlock = ((TFTPAckPacket)received).getBlockNumber();
/*     */                 
/* 416 */                 if (lastBlock == block) {
/* 417 */                   block++;
/* 418 */                   if (block > 65535)
/*     */                   {
/* 420 */                     block = 0;
/*     */                   }
/* 422 */                   wantReply = false;
/*     */                 } else {
/* 424 */                   discardPackets();
/*     */                 }
/* 426 */                 break;
/*     */               default: 
/* 428 */                 throw new IOException("Received unexpected packet type.");
/*     */               }
/*     */             } else {
/* 431 */               TFTPErrorPacket error = new TFTPErrorPacket(recdAddress, recdPort, 5, "Unexpected host or port.");
/*     */               
/*     */ 
/*     */ 
/* 435 */               bufferedSend(error);
/*     */             }
/*     */           } catch (SocketException e) {
/* 438 */             timeouts++; if (timeouts >= this.__maxTimeouts) {
/* 439 */               throw new IOException("Connection timed out.");
/*     */             }
/*     */           } catch (InterruptedIOException e) {
/* 442 */             timeouts++; if (timeouts >= this.__maxTimeouts) {
/* 443 */               throw new IOException("Connection timed out.");
/*     */             }
/*     */           } catch (TFTPPacketException e) {
/* 446 */             throw new IOException("Bad packet: " + e.getMessage());
/*     */           }
/*     */           
/* 449 */         } while (wantReply);
/*     */         
/* 451 */         if (lastAckWait) {
/*     */           break;
/*     */         }
/*     */         
/* 455 */         int dataLength = 512;
/* 456 */         int offset = 4;
/* 457 */         int totalThisPacket = 0;
/* 458 */         int bytesRead = 0;
/*     */         
/* 460 */         while ((dataLength > 0) && ((bytesRead = input.read(this._sendBuffer, offset, dataLength)) > 0)) {
/* 461 */           offset += bytesRead;
/* 462 */           dataLength -= bytesRead;
/* 463 */           totalThisPacket += bytesRead;
/*     */         }
/* 465 */         if (totalThisPacket < 512)
/*     */         {
/* 467 */           lastAckWait = true;
/*     */         }
/* 469 */         data.setBlockNumber(block);
/* 470 */         data.setData(this._sendBuffer, 4, totalThisPacket);
/* 471 */         sent = data;
/* 472 */         this.totalBytesSent += totalThisPacket;
/*     */       }
/*     */     } finally {
/* 475 */       endBufferedOps();
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendFile(String filename, int mode, InputStream input, String hostname, int port)
/*     */     throws UnknownHostException, IOException
/*     */   {
/* 502 */     sendFile(filename, mode, input, InetAddress.getByName(hostname), port);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendFile(String filename, int mode, InputStream input, InetAddress host)
/*     */     throws IOException
/*     */   {
/* 522 */     sendFile(filename, mode, input, host, 69);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendFile(String filename, int mode, InputStream input, String hostname)
/*     */     throws UnknownHostException, IOException
/*     */   {
/* 541 */     sendFile(filename, mode, input, InetAddress.getByName(hostname), 69);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\tftp\TFTPClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */