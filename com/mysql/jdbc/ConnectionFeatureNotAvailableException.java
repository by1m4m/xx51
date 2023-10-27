/*    */ package com.mysql.jdbc;
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
/*    */ public class ConnectionFeatureNotAvailableException
/*    */   extends CommunicationsException
/*    */ {
/*    */   static final long serialVersionUID = -5065030488729238287L;
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
/*    */   public ConnectionFeatureNotAvailableException(MySQLConnection conn, long lastPacketSentTimeMs, Exception underlyingException)
/*    */   {
/* 50 */     super(conn, lastPacketSentTimeMs, 0L, underlyingException);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 59 */     return "Feature not available in this distribution of Connector/J";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getSQLState()
/*    */   {
/* 68 */     return "01S00";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\ConnectionFeatureNotAvailableException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */