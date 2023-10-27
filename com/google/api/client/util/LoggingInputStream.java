/*    */ package com.google.api.client.util;
/*    */ 
/*    */ import java.io.FilterInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
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
/*    */ public class LoggingInputStream
/*    */   extends FilterInputStream
/*    */ {
/*    */   private final LoggingByteArrayOutputStream logStream;
/*    */   
/*    */   public LoggingInputStream(InputStream inputStream, Logger logger, Level loggingLevel, int contentLoggingLimit)
/*    */   {
/* 44 */     super(inputStream);
/* 45 */     this.logStream = new LoggingByteArrayOutputStream(logger, loggingLevel, contentLoggingLimit);
/*    */   }
/*    */   
/*    */   public int read() throws IOException
/*    */   {
/* 50 */     int read = super.read();
/* 51 */     this.logStream.write(read);
/* 52 */     return read;
/*    */   }
/*    */   
/*    */   public int read(byte[] b, int off, int len) throws IOException
/*    */   {
/* 57 */     int read = super.read(b, off, len);
/* 58 */     if (read > 0) {
/* 59 */       this.logStream.write(b, off, read);
/*    */     }
/* 61 */     return read;
/*    */   }
/*    */   
/*    */   public void close() throws IOException
/*    */   {
/* 66 */     this.logStream.close();
/* 67 */     super.close();
/*    */   }
/*    */   
/*    */   public final LoggingByteArrayOutputStream getLogStream()
/*    */   {
/* 72 */     return this.logStream;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\LoggingInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */