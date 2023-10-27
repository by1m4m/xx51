/*    */ package org.apache.http.impl.conn.tsccm;
/*    */ 
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import org.apache.http.conn.ClientConnectionOperator;
/*    */ import org.apache.http.conn.OperatedClientConnection;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ import org.apache.http.impl.conn.AbstractPoolEntry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BasicPoolEntry
/*    */   extends AbstractPoolEntry
/*    */ {
/*    */   @Deprecated
/*    */   public BasicPoolEntry(ClientConnectionOperator op, HttpRoute route, ReferenceQueue<Object> queue)
/*    */   {
/* 50 */     super(op, route);
/* 51 */     if (route == null) {
/* 52 */       throw new IllegalArgumentException("HTTP route may not be null");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BasicPoolEntry(ClientConnectionOperator op, HttpRoute route)
/*    */   {
/* 64 */     super(op, route);
/* 65 */     if (route == null) {
/* 66 */       throw new IllegalArgumentException("HTTP route may not be null");
/*    */     }
/*    */   }
/*    */   
/*    */   protected final OperatedClientConnection getConnection() {
/* 71 */     return this.connection;
/*    */   }
/*    */   
/*    */   protected final HttpRoute getPlannedRoute() {
/* 75 */     return this.route;
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   protected final BasicPoolEntryRef getWeakRef() {
/* 80 */     return null;
/*    */   }
/*    */   
/*    */   protected void shutdownEntry()
/*    */   {
/* 85 */     super.shutdownEntry();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\conn\tsccm\BasicPoolEntry.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */