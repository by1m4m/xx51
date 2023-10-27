/*    */ package org.apache.http.impl.conn.tsccm;
/*    */ 
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.lang.ref.WeakReference;
/*    */ import org.apache.http.annotation.Immutable;
/*    */ import org.apache.http.conn.routing.HttpRoute;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class BasicPoolEntryRef
/*    */   extends WeakReference<BasicPoolEntry>
/*    */ {
/*    */   private final HttpRoute route;
/*    */   
/*    */   public BasicPoolEntryRef(BasicPoolEntry entry, ReferenceQueue<Object> queue)
/*    */   {
/* 61 */     super(entry, queue);
/* 62 */     if (entry == null) {
/* 63 */       throw new IllegalArgumentException("Pool entry must not be null.");
/*    */     }
/*    */     
/* 66 */     this.route = entry.getPlannedRoute();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final HttpRoute getRoute()
/*    */   {
/* 77 */     return this.route;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\conn\tsccm\BasicPoolEntryRef.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */