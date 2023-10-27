/*    */ package com.google.api.client.http.apache;
/*    */ 
/*    */ import com.google.api.client.util.Preconditions;
/*    */ import java.net.URI;
/*    */ import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
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
/*    */ final class HttpExtensionMethod
/*    */   extends HttpEntityEnclosingRequestBase
/*    */ {
/*    */   private final String methodName;
/*    */   
/*    */   public HttpExtensionMethod(String methodName, String uri)
/*    */   {
/* 36 */     this.methodName = ((String)Preconditions.checkNotNull(methodName));
/* 37 */     setURI(URI.create(uri));
/*    */   }
/*    */   
/*    */   public String getMethod()
/*    */   {
/* 42 */     return this.methodName;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\apache\HttpExtensionMethod.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */