/*    */ package com.google.api.client.testing.http;
/*    */ 
/*    */ import com.google.api.client.http.HttpRequest;
/*    */ import com.google.api.client.http.HttpResponse;
/*    */ import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
/*    */ import com.google.api.client.util.Beta;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ public class MockHttpUnsuccessfulResponseHandler
/*    */   implements HttpUnsuccessfulResponseHandler
/*    */ {
/*    */   private boolean isCalled;
/*    */   private boolean successfullyHandleResponse;
/*    */   
/*    */   public MockHttpUnsuccessfulResponseHandler(boolean successfullyHandleResponse)
/*    */   {
/* 47 */     this.successfullyHandleResponse = successfullyHandleResponse;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isCalled()
/*    */   {
/* 54 */     return this.isCalled;
/*    */   }
/*    */   
/*    */   public boolean handleResponse(HttpRequest request, HttpResponse response, boolean supportsRetry) throws IOException
/*    */   {
/* 59 */     this.isCalled = true;
/* 60 */     return this.successfullyHandleResponse;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\http\MockHttpUnsuccessfulResponseHandler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */