/*    */ package org.apache.log4j.lf5;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import org.apache.log4j.lf5.viewer.LogBrokerMonitor;
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
/*    */ public class AppenderFinalizer
/*    */ {
/* 41 */   protected LogBrokerMonitor _defaultMonitor = null;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AppenderFinalizer(LogBrokerMonitor defaultMonitor)
/*    */   {
/* 52 */     this._defaultMonitor = defaultMonitor;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void finalize()
/*    */     throws Throwable
/*    */   {
/* 66 */     System.out.println("Disposing of the default LogBrokerMonitor instance");
/* 67 */     this._defaultMonitor.dispose();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\lf5\AppenderFinalizer.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */