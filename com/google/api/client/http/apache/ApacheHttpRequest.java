/*    */ package com.google.api.client.http.apache;
/*    */ 
/*    */ import com.google.api.client.http.LowLevelHttpRequest;
/*    */ import com.google.api.client.http.LowLevelHttpResponse;
/*    */ import com.google.api.client.util.Preconditions;
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpEntityEnclosingRequest;
/*    */ import org.apache.http.RequestLine;
/*    */ import org.apache.http.client.HttpClient;
/*    */ import org.apache.http.client.methods.HttpRequestBase;
/*    */ import org.apache.http.conn.params.ConnManagerParams;
/*    */ import org.apache.http.params.HttpConnectionParams;
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
/*    */ final class ApacheHttpRequest
/*    */   extends LowLevelHttpRequest
/*    */ {
/*    */   private final HttpClient httpClient;
/*    */   private final HttpRequestBase request;
/*    */   
/*    */   ApacheHttpRequest(HttpClient httpClient, HttpRequestBase request)
/*    */   {
/* 39 */     this.httpClient = httpClient;
/* 40 */     this.request = request;
/*    */   }
/*    */   
/*    */   public void addHeader(String name, String value)
/*    */   {
/* 45 */     this.request.addHeader(name, value);
/*    */   }
/*    */   
/*    */   public void setTimeout(int connectTimeout, int readTimeout) throws IOException
/*    */   {
/* 50 */     HttpParams params = this.request.getParams();
/* 51 */     ConnManagerParams.setTimeout(params, connectTimeout);
/* 52 */     HttpConnectionParams.setConnectionTimeout(params, connectTimeout);
/* 53 */     HttpConnectionParams.setSoTimeout(params, readTimeout);
/*    */   }
/*    */   
/*    */   public LowLevelHttpResponse execute() throws IOException
/*    */   {
/* 58 */     if (getStreamingContent() != null) {
/* 59 */       Preconditions.checkArgument(this.request instanceof HttpEntityEnclosingRequest, "Apache HTTP client does not support %s requests with content.", new Object[] { this.request.getRequestLine().getMethod() });
/*    */       
/*    */ 
/* 62 */       ContentEntity entity = new ContentEntity(getContentLength(), getStreamingContent());
/* 63 */       entity.setContentEncoding(getContentEncoding());
/* 64 */       entity.setContentType(getContentType());
/* 65 */       ((HttpEntityEnclosingRequest)this.request).setEntity(entity);
/*    */     }
/* 67 */     return new ApacheHttpResponse(this.request, this.httpClient.execute(this.request));
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\apache\ApacheHttpRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */