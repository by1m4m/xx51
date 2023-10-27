/*    */ package org.apache.http.conn.params;
/*    */ 
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.params.HttpAbstractParamBean;
/*    */ import org.apache.http.params.HttpParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class ConnManagerParamBean
/*    */   extends HttpAbstractParamBean
/*    */ {
/*    */   public ConnManagerParamBean(HttpParams params)
/*    */   {
/* 46 */     super(params);
/*    */   }
/*    */   
/*    */   public void setTimeout(long timeout) {
/* 50 */     this.params.setLongParameter("http.conn-manager.timeout", timeout);
/*    */   }
/*    */   
/*    */   public void setMaxTotalConnections(int maxConnections)
/*    */   {
/* 55 */     this.params.setIntParameter("http.conn-manager.max-total", maxConnections);
/*    */   }
/*    */   
/*    */   public void setConnectionsPerRoute(ConnPerRouteBean connPerRoute)
/*    */   {
/* 60 */     this.params.setParameter("http.conn-manager.max-per-route", connPerRoute);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\conn\params\ConnManagerParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */