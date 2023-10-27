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
/*    */ public class ConnConnectionParamBean
/*    */   extends HttpAbstractParamBean
/*    */ {
/*    */   public ConnConnectionParamBean(HttpParams params)
/*    */   {
/* 46 */     super(params);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setMaxStatusLineGarbage(int maxStatusLineGarbage)
/*    */   {
/* 53 */     this.params.setIntParameter("http.connection.max-status-line-garbage", maxStatusLineGarbage);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\conn\params\ConnConnectionParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */