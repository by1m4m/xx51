/*    */ package org.apache.http.impl.conn.tsccm;
/*    */ 
/*    */ import org.apache.http.conn.ClientConnectionManager;
/*    */ import org.apache.http.impl.conn.AbstractPoolEntry;
/*    */ import org.apache.http.impl.conn.AbstractPooledConnAdapter;
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
/*    */ public class BasicPooledConnAdapter
/*    */   extends AbstractPooledConnAdapter
/*    */ {
/*    */   protected BasicPooledConnAdapter(ThreadSafeClientConnManager tsccm, AbstractPoolEntry entry)
/*    */   {
/* 53 */     super(tsccm, entry);
/* 54 */     markReusable();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected ClientConnectionManager getManager()
/*    */   {
/* 61 */     return super.getManager();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected AbstractPoolEntry getPoolEntry()
/*    */   {
/* 71 */     return this.poolEntry;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void detach()
/*    */   {
/* 79 */     super.detach();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\conn\tsccm\BasicPooledConnAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */