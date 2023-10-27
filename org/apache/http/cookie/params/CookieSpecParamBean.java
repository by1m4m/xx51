/*    */ package org.apache.http.cookie.params;
/*    */ 
/*    */ import java.util.Collection;
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
/*    */ 
/*    */ @NotThreadSafe
/*    */ public class CookieSpecParamBean
/*    */   extends HttpAbstractParamBean
/*    */ {
/*    */   public CookieSpecParamBean(HttpParams params)
/*    */   {
/* 48 */     super(params);
/*    */   }
/*    */   
/*    */   public void setDatePatterns(Collection<String> patterns) {
/* 52 */     this.params.setParameter("http.protocol.cookie-datepatterns", patterns);
/*    */   }
/*    */   
/*    */   public void setSingleHeader(boolean singleHeader) {
/* 56 */     this.params.setBooleanParameter("http.protocol.single-cookie-header", singleHeader);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\cookie\params\CookieSpecParamBean.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */