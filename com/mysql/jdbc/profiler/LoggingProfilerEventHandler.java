/*    */ package com.mysql.jdbc.profiler;
/*    */ 
/*    */ import com.mysql.jdbc.Connection;
/*    */ import com.mysql.jdbc.log.Log;
/*    */ import java.sql.SQLException;
/*    */ import java.util.Properties;
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
/*    */ public class LoggingProfilerEventHandler
/*    */   implements ProfilerEventHandler
/*    */ {
/*    */   private Log log;
/*    */   
/*    */   public void consumeEvent(ProfilerEvent evt)
/*    */   {
/* 43 */     if (evt.eventType == 0) {
/* 44 */       this.log.logWarn(evt);
/*    */     } else {
/* 46 */       this.log.logInfo(evt);
/*    */     }
/*    */   }
/*    */   
/*    */   public void destroy() {
/* 51 */     this.log = null;
/*    */   }
/*    */   
/*    */   public void init(Connection conn, Properties props) throws SQLException {
/* 55 */     this.log = conn.getLog();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\profiler\LoggingProfilerEventHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */