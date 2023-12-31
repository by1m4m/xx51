/*    */ package org.apache.http.params;
/*    */ 
/*    */ import org.apache.http.HttpVersion;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpProtocolParamBean
/*    */   extends HttpAbstractParamBean
/*    */ {
/*    */   public HttpProtocolParamBean(HttpParams params)
/*    */   {
/* 46 */     super(params);
/*    */   }
/*    */   
/*    */   public void setHttpElementCharset(String httpElementCharset) {
/* 50 */     HttpProtocolParams.setHttpElementCharset(this.params, httpElementCharset);
/*    */   }
/*    */   
/*    */   public void setContentCharset(String contentCharset) {
/* 54 */     HttpProtocolParams.setContentCharset(this.params, contentCharset);
/*    */   }
/*    */   
/*    */   public void setVersion(HttpVersion version) {
/* 58 */     HttpProtocolParams.setVersion(this.params, version);
/*    */   }
/*    */   
/*    */   public void setUserAgent(String userAgent) {
/* 62 */     HttpProtocolParams.setUserAgent(this.params, userAgent);
/*    */   }
/*    */   
/*    */   public void setUseExpectContinue(boolean useExpectContinue) {
/* 66 */     HttpProtocolParams.setUseExpectContinue(this.params, useExpectContinue);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\params\HttpProtocolParamBean.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */