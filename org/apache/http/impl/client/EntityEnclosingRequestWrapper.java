/*    */ package org.apache.http.impl.client;
/*    */ 
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpEntityEnclosingRequest;
/*    */ import org.apache.http.ProtocolException;
/*    */ import org.apache.http.annotation.NotThreadSafe;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
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
/*    */ public class EntityEnclosingRequestWrapper
/*    */   extends RequestWrapper
/*    */   implements HttpEntityEnclosingRequest
/*    */ {
/*    */   private HttpEntity entity;
/*    */   
/*    */   public EntityEnclosingRequestWrapper(HttpEntityEnclosingRequest request)
/*    */     throws ProtocolException
/*    */   {
/* 57 */     super(request);
/* 58 */     this.entity = request.getEntity();
/*    */   }
/*    */   
/*    */   public HttpEntity getEntity() {
/* 62 */     return this.entity;
/*    */   }
/*    */   
/*    */   public void setEntity(HttpEntity entity) {
/* 66 */     this.entity = entity;
/*    */   }
/*    */   
/*    */   public boolean expectContinue() {
/* 70 */     Header expect = getFirstHeader("Expect");
/* 71 */     return (expect != null) && ("100-Continue".equalsIgnoreCase(expect.getValue()));
/*    */   }
/*    */   
/*    */   public boolean isRepeatable()
/*    */   {
/* 76 */     return (this.entity == null) || (this.entity.isRepeatable());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\client\EntityEnclosingRequestWrapper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */