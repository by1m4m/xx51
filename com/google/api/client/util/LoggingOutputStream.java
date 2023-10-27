/*    */ package com.google.api.client.util;
/*    */ 
/*    */ import java.io.FilterOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ 
/*    */ public class LoggingOutputStream
/*    */   extends FilterOutputStream
/*    */ {
/*    */   private final LoggingByteArrayOutputStream logStream;
/*    */   
/*    */   public LoggingOutputStream(OutputStream outputStream, Logger logger, Level loggingLevel, int contentLoggingLimit)
/*    */   {
/* 45 */     super(outputStream);
/* 46 */     this.logStream = new LoggingByteArrayOutputStream(logger, loggingLevel, contentLoggingLimit);
/*    */   }
/*    */   
/*    */   public void write(int b) throws IOException
/*    */   {
/* 51 */     this.out.write(b);
/* 52 */     this.logStream.write(b);
/*    */   }
/*    */   
/*    */   public void write(byte[] b, int off, int len) throws IOException
/*    */   {
/* 57 */     this.out.write(b, off, len);
/* 58 */     this.logStream.write(b, off, len);
/*    */   }
/*    */   
/*    */   public void close() throws IOException
/*    */   {
/* 63 */     this.logStream.close();
/* 64 */     super.close();
/*    */   }
/*    */   
/*    */   public final LoggingByteArrayOutputStream getLogStream()
/*    */   {
/* 69 */     return this.logStream;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\LoggingOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */