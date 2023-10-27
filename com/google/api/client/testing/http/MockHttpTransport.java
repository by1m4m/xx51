/*     */ package com.google.api.client.testing.http;
/*     */ 
/*     */ import com.google.api.client.http.HttpTransport;
/*     */ import com.google.api.client.http.LowLevelHttpRequest;
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ public class MockHttpTransport
/*     */   extends HttpTransport
/*     */ {
/*     */   private Set<String> supportedMethods;
/*     */   private MockLowLevelHttpRequest lowLevelHttpRequest;
/*     */   private MockLowLevelHttpResponse lowLevelHttpResponse;
/*     */   
/*     */   public MockHttpTransport() {}
/*     */   
/*     */   protected MockHttpTransport(Builder builder)
/*     */   {
/*  68 */     this.supportedMethods = builder.supportedMethods;
/*  69 */     this.lowLevelHttpRequest = builder.lowLevelHttpRequest;
/*  70 */     this.lowLevelHttpResponse = builder.lowLevelHttpResponse;
/*     */   }
/*     */   
/*     */   public boolean supportsMethod(String method) throws IOException
/*     */   {
/*  75 */     return (this.supportedMethods == null) || (this.supportedMethods.contains(method));
/*     */   }
/*     */   
/*     */   public LowLevelHttpRequest buildRequest(String method, String url) throws IOException
/*     */   {
/*  80 */     Preconditions.checkArgument(supportsMethod(method), "HTTP method %s not supported", new Object[] { method });
/*  81 */     if (this.lowLevelHttpRequest != null) {
/*  82 */       return this.lowLevelHttpRequest;
/*     */     }
/*  84 */     MockLowLevelHttpRequest request = new MockLowLevelHttpRequest(url);
/*  85 */     if (this.lowLevelHttpResponse != null) {
/*  86 */       request.setResponse(this.lowLevelHttpResponse);
/*     */     }
/*  88 */     return request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Set<String> getSupportedMethods()
/*     */   {
/*  96 */     return this.supportedMethods == null ? null : Collections.unmodifiableSet(this.supportedMethods);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final MockLowLevelHttpRequest getLowLevelHttpRequest()
/*     */   {
/* 106 */     return this.lowLevelHttpRequest;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static Builder builder()
/*     */   {
/* 120 */     return new Builder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static class Builder
/*     */   {
/*     */     Set<String> supportedMethods;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     MockLowLevelHttpRequest lowLevelHttpRequest;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     MockLowLevelHttpResponse lowLevelHttpResponse;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public MockHttpTransport build()
/*     */     {
/* 164 */       return new MockHttpTransport(this);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public final Set<String> getSupportedMethods()
/*     */     {
/* 171 */       return this.supportedMethods;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public final Builder setSupportedMethods(Set<String> supportedMethods)
/*     */     {
/* 178 */       this.supportedMethods = supportedMethods;
/* 179 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final Builder setLowLevelHttpRequest(MockLowLevelHttpRequest lowLevelHttpRequest)
/*     */     {
/* 193 */       Preconditions.checkState(this.lowLevelHttpResponse == null, "Cannnot set a low level HTTP request when a low level HTTP response has been set.");
/*     */       
/* 195 */       this.lowLevelHttpRequest = lowLevelHttpRequest;
/* 196 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final MockLowLevelHttpRequest getLowLevelHttpRequest()
/*     */     {
/* 206 */       return this.lowLevelHttpRequest;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final Builder setLowLevelHttpResponse(MockLowLevelHttpResponse lowLevelHttpResponse)
/*     */     {
/* 222 */       Preconditions.checkState(this.lowLevelHttpRequest == null, "Cannot set a low level HTTP response when a low level HTTP request has been set.");
/*     */       
/* 224 */       this.lowLevelHttpResponse = lowLevelHttpResponse;
/* 225 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     MockLowLevelHttpResponse getLowLevelHttpResponse()
/*     */     {
/* 236 */       return this.lowLevelHttpResponse;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\http\MockHttpTransport.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */