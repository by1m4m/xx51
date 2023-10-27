/*    */ package org.apache.http.auth.params;
/*    */ 
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
/*    */ public class AuthParamBean
/*    */   extends HttpAbstractParamBean
/*    */ {
/*    */   public AuthParamBean(HttpParams params)
/*    */   {
/* 43 */     super(params);
/*    */   }
/*    */   
/*    */   public void setCredentialCharset(String charset) {
/* 47 */     AuthParams.setCredentialCharset(this.params, charset);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\auth\params\AuthParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */