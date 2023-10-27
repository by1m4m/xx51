/*    */ package org.apache.http.params;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class HttpAbstractParamBean
/*    */ {
/*    */   protected final HttpParams params;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public HttpAbstractParamBean(HttpParams params)
/*    */   {
/* 42 */     if (params == null)
/* 43 */       throw new IllegalArgumentException("HTTP parameters may not be null");
/* 44 */     this.params = params;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\params\HttpAbstractParamBean.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */