/*    */ package com.google.api.client.http;
/*    */ 
/*    */ import com.google.api.client.util.Preconditions;
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
/*    */ public final class BasicAuthentication
/*    */   implements HttpRequestInitializer, HttpExecuteInterceptor
/*    */ {
/*    */   private final String username;
/*    */   private final String password;
/*    */   
/*    */   public BasicAuthentication(String username, String password)
/*    */   {
/* 42 */     this.username = ((String)Preconditions.checkNotNull(username));
/* 43 */     this.password = ((String)Preconditions.checkNotNull(password));
/*    */   }
/*    */   
/*    */   public void initialize(HttpRequest request) throws IOException {
/* 47 */     request.setInterceptor(this);
/*    */   }
/*    */   
/*    */   public void intercept(HttpRequest request) throws IOException {
/* 51 */     request.getHeaders().setBasicAuthentication(this.username, this.password);
/*    */   }
/*    */   
/*    */   public String getUsername()
/*    */   {
/* 56 */     return this.username;
/*    */   }
/*    */   
/*    */   public String getPassword()
/*    */   {
/* 61 */     return this.password;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\BasicAuthentication.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */