/*    */ package org.apache.http.client.methods;
/*    */ 
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpEntityEnclosingRequest;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ import org.apache.http.client.utils.CloneUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public abstract class HttpEntityEnclosingRequestBase
/*    */   extends HttpRequestBase
/*    */   implements HttpEntityEnclosingRequest
/*    */ {
/*    */   private HttpEntity entity;
/*    */   
/*    */   public HttpEntity getEntity()
/*    */   {
/* 55 */     return this.entity;
/*    */   }
/*    */   
/*    */   public void setEntity(HttpEntity entity) {
/* 59 */     this.entity = entity;
/*    */   }
/*    */   
/*    */   public boolean expectContinue() {
/* 63 */     Header expect = getFirstHeader("Expect");
/* 64 */     return (expect != null) && ("100-Continue".equalsIgnoreCase(expect.getValue()));
/*    */   }
/*    */   
/*    */   public Object clone() throws CloneNotSupportedException
/*    */   {
/* 69 */     HttpEntityEnclosingRequestBase clone = (HttpEntityEnclosingRequestBase)super.clone();
/*    */     
/* 71 */     if (this.entity != null) {
/* 72 */       clone.entity = ((HttpEntity)CloneUtils.clone(this.entity));
/*    */     }
/* 74 */     return clone;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\client\methods\HttpEntityEnclosingRequestBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */