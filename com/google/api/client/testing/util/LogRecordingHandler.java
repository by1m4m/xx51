/*    */ package com.google.api.client.testing.util;
/*    */ 
/*    */ import com.google.api.client.util.Beta;
/*    */ import com.google.api.client.util.Lists;
/*    */ import java.util.List;
/*    */ import java.util.logging.Handler;
/*    */ import java.util.logging.LogRecord;
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
/*    */ @Beta
/*    */ public class LogRecordingHandler
/*    */   extends Handler
/*    */ {
/* 35 */   private final List<LogRecord> records = Lists.newArrayList();
/*    */   
/*    */   public void publish(LogRecord record)
/*    */   {
/* 39 */     this.records.add(record);
/*    */   }
/*    */   
/*    */ 
/*    */   public void flush() {}
/*    */   
/*    */ 
/*    */   public void close()
/*    */     throws SecurityException
/*    */   {}
/*    */   
/*    */   public List<String> messages()
/*    */   {
/* 52 */     List<String> result = Lists.newArrayList();
/* 53 */     for (LogRecord record : this.records) {
/* 54 */       result.add(record.getMessage());
/*    */     }
/* 56 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\util\LogRecordingHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */