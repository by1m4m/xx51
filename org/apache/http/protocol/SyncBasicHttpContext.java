/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SyncBasicHttpContext
/*    */   extends BasicHttpContext
/*    */ {
/*    */   public SyncBasicHttpContext(HttpContext parentContext)
/*    */   {
/* 45 */     super(parentContext);
/*    */   }
/*    */   
/*    */   public synchronized Object getAttribute(String id) {
/* 49 */     return super.getAttribute(id);
/*    */   }
/*    */   
/*    */   public synchronized void setAttribute(String id, Object obj) {
/* 53 */     super.setAttribute(id, obj);
/*    */   }
/*    */   
/*    */   public synchronized Object removeAttribute(String id) {
/* 57 */     return super.removeAttribute(id);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\protocol\SyncBasicHttpContext.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */