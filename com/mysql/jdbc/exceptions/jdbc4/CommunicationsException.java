/*    */ package com.mysql.jdbc.exceptions.jdbc4;
/*    */ 
/*    */ import com.mysql.jdbc.MySQLConnection;
/*    */ import com.mysql.jdbc.SQLError;
/*    */ import com.mysql.jdbc.StreamingNotifiable;
/*    */ import java.sql.SQLRecoverableException;
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
/*    */ public class CommunicationsException
/*    */   extends SQLRecoverableException
/*    */   implements StreamingNotifiable
/*    */ {
/*    */   private String exceptionMessage;
/* 49 */   private boolean streamingResultSetInPlay = false;
/*    */   
/*    */ 
/*    */ 
/*    */   public CommunicationsException(MySQLConnection conn, long lastPacketSentTimeMs, long lastPacketReceivedTimeMs, Exception underlyingException)
/*    */   {
/* 55 */     this.exceptionMessage = SQLError.createLinkFailureMessageBasedOnHeuristics(conn, lastPacketSentTimeMs, lastPacketReceivedTimeMs, underlyingException, this.streamingResultSetInPlay);
/*    */     
/*    */ 
/* 58 */     if (underlyingException != null) {
/* 59 */       initCause(underlyingException);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 69 */     return this.exceptionMessage;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getSQLState()
/*    */   {
/* 78 */     return "08S01";
/*    */   }
/*    */   
/*    */   public void setWasStreamingResults() {
/* 82 */     this.streamingResultSetInPlay = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\exceptions\jdbc4\CommunicationsException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */