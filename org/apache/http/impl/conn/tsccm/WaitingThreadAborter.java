/*    */ package org.apache.http.impl.conn.tsccm;
/*    */ 
/*    */ import org.apache.http.annotation.NotThreadSafe;
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
/*    */ @NotThreadSafe
/*    */ public class WaitingThreadAborter
/*    */ {
/*    */   private WaitingThread waitingThread;
/*    */   private boolean aborted;
/*    */   
/*    */   public void abort()
/*    */   {
/* 49 */     this.aborted = true;
/*    */     
/* 51 */     if (this.waitingThread != null) {
/* 52 */       this.waitingThread.interrupt();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setWaitingThread(WaitingThread waitingThread)
/*    */   {
/* 63 */     this.waitingThread = waitingThread;
/* 64 */     if (this.aborted) {
/* 65 */       waitingThread.interrupt();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\conn\tsccm\WaitingThreadAborter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */