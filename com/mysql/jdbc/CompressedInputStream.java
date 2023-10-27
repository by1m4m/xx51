/*     */ package com.mysql.jdbc;
/*     */ 
/*     */ import com.mysql.jdbc.log.Log;
/*     */ import com.mysql.jdbc.log.NullLogger;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.sql.SQLException;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Inflater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class CompressedInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private byte[] buffer;
/*     */   private InputStream in;
/*     */   private Inflater inflater;
/*     */   private ConnectionPropertiesImpl.BooleanConnectionProperty traceProtocol;
/*     */   private Log log;
/*  65 */   private byte[] packetHeaderBuffer = new byte[7];
/*     */   
/*     */ 
/*  68 */   private int pos = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CompressedInputStream(Connection conn, InputStream streamFromServer)
/*     */   {
/*  79 */     this.traceProtocol = ((ConnectionPropertiesImpl)conn).traceProtocol;
/*     */     try {
/*  81 */       this.log = conn.getLog();
/*     */     } catch (SQLException e) {
/*  83 */       this.log = new NullLogger(null);
/*     */     }
/*     */     
/*  86 */     this.in = streamFromServer;
/*  87 */     this.inflater = new Inflater();
/*     */   }
/*     */   
/*     */ 
/*     */   public int available()
/*     */     throws IOException
/*     */   {
/*  94 */     if (this.buffer == null) {
/*  95 */       return this.in.available();
/*     */     }
/*     */     
/*  98 */     return this.buffer.length - this.pos + this.in.available();
/*     */   }
/*     */   
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 105 */     this.in.close();
/* 106 */     this.buffer = null;
/* 107 */     this.inflater.end();
/* 108 */     this.inflater = null;
/* 109 */     this.traceProtocol = null;
/* 110 */     this.log = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void getNextPacketFromServer()
/*     */     throws IOException
/*     */   {
/* 121 */     byte[] uncompressedData = null;
/*     */     
/* 123 */     int lengthRead = readFully(this.packetHeaderBuffer, 0, 7);
/*     */     
/* 125 */     if (lengthRead < 7) {
/* 126 */       throw new IOException("Unexpected end of input stream");
/*     */     }
/*     */     
/* 129 */     int compressedPacketLength = (this.packetHeaderBuffer[0] & 0xFF) + ((this.packetHeaderBuffer[1] & 0xFF) << 8) + ((this.packetHeaderBuffer[2] & 0xFF) << 16);
/*     */     
/*     */ 
/*     */ 
/* 133 */     int uncompressedLength = (this.packetHeaderBuffer[4] & 0xFF) + ((this.packetHeaderBuffer[5] & 0xFF) << 8) + ((this.packetHeaderBuffer[6] & 0xFF) << 16);
/*     */     
/*     */ 
/*     */ 
/* 137 */     boolean doTrace = this.traceProtocol.getValueAsBoolean();
/*     */     
/* 139 */     if (doTrace) {
/* 140 */       this.log.logTrace("Reading compressed packet of length " + compressedPacketLength + " uncompressed to " + uncompressedLength);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 146 */     if (uncompressedLength > 0) {
/* 147 */       uncompressedData = new byte[uncompressedLength];
/*     */       
/* 149 */       byte[] compressedBuffer = new byte[compressedPacketLength];
/*     */       
/* 151 */       readFully(compressedBuffer, 0, compressedPacketLength);
/*     */       try
/*     */       {
/* 154 */         this.inflater.reset();
/*     */       } catch (NullPointerException npe) {
/* 156 */         this.inflater = new Inflater();
/*     */       }
/*     */       
/* 159 */       this.inflater.setInput(compressedBuffer);
/*     */       try
/*     */       {
/* 162 */         this.inflater.inflate(uncompressedData);
/*     */       } catch (DataFormatException dfe) {
/* 164 */         throw new IOException("Error while uncompressing packet from server.");
/*     */       }
/*     */       
/*     */ 
/* 168 */       this.inflater.end();
/*     */     } else {
/* 170 */       if (doTrace) {
/* 171 */         this.log.logTrace("Packet didn't meet compression threshold, not uncompressing...");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 179 */       uncompressedData = new byte[compressedPacketLength];
/* 180 */       readFully(uncompressedData, 0, compressedPacketLength);
/*     */     }
/*     */     
/* 183 */     if (doTrace) {
/* 184 */       this.log.logTrace("Uncompressed packet: \n" + StringUtils.dumpAsHex(uncompressedData, compressedPacketLength));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 190 */     if ((this.buffer != null) && (this.pos < this.buffer.length)) {
/* 191 */       if (doTrace) {
/* 192 */         this.log.logTrace("Combining remaining packet with new: ");
/*     */       }
/*     */       
/*     */ 
/* 196 */       int remaining = this.buffer.length - this.pos;
/* 197 */       byte[] newBuffer = new byte[remaining + uncompressedData.length];
/*     */       
/* 199 */       int newIndex = 0;
/*     */       
/* 201 */       for (int i = this.pos; i < this.buffer.length; i++) {
/* 202 */         newBuffer[(newIndex++)] = this.buffer[i];
/*     */       }
/* 204 */       System.arraycopy(uncompressedData, 0, newBuffer, newIndex, uncompressedData.length);
/*     */       
/*     */ 
/* 207 */       uncompressedData = newBuffer;
/*     */     }
/*     */     
/* 210 */     this.pos = 0;
/* 211 */     this.buffer = uncompressedData;
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
/*     */   private void getNextPacketIfRequired(int numBytes)
/*     */     throws IOException
/*     */   {
/* 227 */     if ((this.buffer == null) || (this.pos + numBytes > this.buffer.length))
/*     */     {
/* 229 */       getNextPacketFromServer();
/*     */     }
/*     */   }
/*     */   
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 238 */       getNextPacketIfRequired(1);
/*     */     } catch (IOException ioEx) {
/* 240 */       return -1;
/*     */     }
/*     */     
/* 243 */     return this.buffer[(this.pos++)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */   public int read(byte[] b)
/*     */     throws IOException
/*     */   {
/* 250 */     return read(b, 0, b.length);
/*     */   }
/*     */   
/*     */ 
/*     */   public int read(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 257 */     if (b == null)
/* 258 */       throw new NullPointerException();
/* 259 */     if ((off < 0) || (off > b.length) || (len < 0) || (off + len > b.length) || (off + len < 0))
/*     */     {
/* 261 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/* 264 */     if (len <= 0) {
/* 265 */       return 0;
/*     */     }
/*     */     try
/*     */     {
/* 269 */       getNextPacketIfRequired(len);
/*     */     } catch (IOException ioEx) {
/* 271 */       return -1;
/*     */     }
/*     */     
/* 274 */     System.arraycopy(this.buffer, this.pos, b, off, len);
/* 275 */     this.pos += len;
/*     */     
/* 277 */     return len;
/*     */   }
/*     */   
/*     */   private final int readFully(byte[] b, int off, int len) throws IOException {
/* 281 */     if (len < 0) {
/* 282 */       throw new IndexOutOfBoundsException();
/*     */     }
/*     */     
/* 285 */     int n = 0;
/*     */     
/* 287 */     while (n < len) {
/* 288 */       int count = this.in.read(b, off + n, len - n);
/*     */       
/* 290 */       if (count < 0) {
/* 291 */         throw new EOFException();
/*     */       }
/*     */       
/* 294 */       n += count;
/*     */     }
/*     */     
/* 297 */     return n;
/*     */   }
/*     */   
/*     */ 
/*     */   public long skip(long n)
/*     */     throws IOException
/*     */   {
/* 304 */     long count = 0L;
/*     */     
/* 306 */     for (long i = 0L; i < n; i += 1L) {
/* 307 */       int bytesRead = read();
/*     */       
/* 309 */       if (bytesRead == -1) {
/*     */         break;
/*     */       }
/*     */       
/* 313 */       count += 1L;
/*     */     }
/*     */     
/* 316 */     return count;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\CompressedInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */