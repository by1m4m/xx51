/*    */ package com.google.api.client.util;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LoggingStreamingContent
/*    */   implements StreamingContent
/*    */ {
/*    */   private final StreamingContent content;
/*    */   private final int contentLoggingLimit;
/*    */   private final Level loggingLevel;
/*    */   private final Logger logger;
/*    */   
/*    */   public LoggingStreamingContent(StreamingContent content, Logger logger, Level loggingLevel, int contentLoggingLimit)
/*    */   {
/* 55 */     this.content = content;
/* 56 */     this.logger = logger;
/* 57 */     this.loggingLevel = loggingLevel;
/* 58 */     this.contentLoggingLimit = contentLoggingLimit;
/*    */   }
/*    */   
/*    */   public void writeTo(OutputStream out) throws IOException {
/* 62 */     LoggingOutputStream loggableOutputStream = new LoggingOutputStream(out, this.logger, this.loggingLevel, this.contentLoggingLimit);
/*    */     try
/*    */     {
/* 65 */       this.content.writeTo(loggableOutputStream);
/*    */     }
/*    */     finally {
/* 68 */       loggableOutputStream.getLogStream().close();
/*    */     }
/* 70 */     out.flush();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\LoggingStreamingContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */