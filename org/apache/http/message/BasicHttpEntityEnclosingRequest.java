/*    */ package org.apache.http.message;
/*    */ 
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpEntity;
/*    */ import org.apache.http.HttpEntityEnclosingRequest;
/*    */ import org.apache.http.ProtocolVersion;
/*    */ import org.apache.http.RequestLine;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BasicHttpEntityEnclosingRequest
/*    */   extends BasicHttpRequest
/*    */   implements HttpEntityEnclosingRequest
/*    */ {
/*    */   private HttpEntity entity;
/*    */   
/*    */   public BasicHttpEntityEnclosingRequest(String method, String uri)
/*    */   {
/* 54 */     super(method, uri);
/*    */   }
/*    */   
/*    */   public BasicHttpEntityEnclosingRequest(String method, String uri, ProtocolVersion ver)
/*    */   {
/* 59 */     super(method, uri, ver);
/*    */   }
/*    */   
/*    */   public BasicHttpEntityEnclosingRequest(RequestLine requestline) {
/* 63 */     super(requestline);
/*    */   }
/*    */   
/*    */   public HttpEntity getEntity() {
/* 67 */     return this.entity;
/*    */   }
/*    */   
/*    */   public void setEntity(HttpEntity entity) {
/* 71 */     this.entity = entity;
/*    */   }
/*    */   
/*    */   public boolean expectContinue() {
/* 75 */     Header expect = getFirstHeader("Expect");
/* 76 */     return (expect != null) && ("100-Continue".equalsIgnoreCase(expect.getValue()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\message\BasicHttpEntityEnclosingRequest.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */