/*    */ package org.apache.http.conn;
/*    */ 
/*    */ import java.io.InterruptedIOException;
/*    */ import org.apache.http.annotation.Immutable;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ public class ConnectTimeoutException
/*    */   extends InterruptedIOException
/*    */ {
/*    */   private static final long serialVersionUID = -4816682903149535989L;
/*    */   
/*    */   public ConnectTimeoutException() {}
/*    */   
/*    */   public ConnectTimeoutException(String message)
/*    */   {
/* 59 */     super(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\conn\ConnectTimeoutException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */