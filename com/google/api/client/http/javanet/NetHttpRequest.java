/*    */ package com.google.api.client.http.javanet;
/*    */ 
/*    */ import com.google.api.client.http.LowLevelHttpRequest;
/*    */ import com.google.api.client.http.LowLevelHttpResponse;
/*    */ import com.google.api.client.util.Preconditions;
/*    */ import com.google.api.client.util.StreamingContent;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.net.HttpURLConnection;
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
/*    */ final class NetHttpRequest
/*    */   extends LowLevelHttpRequest
/*    */ {
/*    */   private final HttpURLConnection connection;
/*    */   
/*    */   NetHttpRequest(HttpURLConnection connection)
/*    */   {
/* 36 */     this.connection = connection;
/* 37 */     connection.setInstanceFollowRedirects(false);
/*    */   }
/*    */   
/*    */   public void addHeader(String name, String value)
/*    */   {
/* 42 */     this.connection.addRequestProperty(name, value);
/*    */   }
/*    */   
/*    */   public void setTimeout(int connectTimeout, int readTimeout)
/*    */   {
/* 47 */     this.connection.setReadTimeout(readTimeout);
/* 48 */     this.connection.setConnectTimeout(connectTimeout);
/*    */   }
/*    */   
/*    */   public LowLevelHttpResponse execute() throws IOException
/*    */   {
/* 53 */     HttpURLConnection connection = this.connection;
/*    */     
/* 55 */     if (getStreamingContent() != null) {
/* 56 */       String contentType = getContentType();
/* 57 */       if (contentType != null) {
/* 58 */         addHeader("Content-Type", contentType);
/*    */       }
/* 60 */       String contentEncoding = getContentEncoding();
/* 61 */       if (contentEncoding != null) {
/* 62 */         addHeader("Content-Encoding", contentEncoding);
/*    */       }
/* 64 */       long contentLength = getContentLength();
/* 65 */       if (contentLength >= 0L) {
/* 66 */         addHeader("Content-Length", Long.toString(contentLength));
/*    */       }
/* 68 */       String requestMethod = connection.getRequestMethod();
/* 69 */       if (("POST".equals(requestMethod)) || ("PUT".equals(requestMethod))) {
/* 70 */         connection.setDoOutput(true);
/*    */         
/* 72 */         if ((contentLength >= 0L) && (contentLength <= 2147483647L)) {
/* 73 */           connection.setFixedLengthStreamingMode((int)contentLength);
/*    */         } else {
/* 75 */           connection.setChunkedStreamingMode(0);
/*    */         }
/* 77 */         OutputStream out = connection.getOutputStream();
/*    */         try {
/* 79 */           getStreamingContent().writeTo(out);
/*    */         } finally {
/* 81 */           out.close();
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 86 */         Preconditions.checkArgument(contentLength == 0L, "%s with non-zero content length is not supported", new Object[] { requestMethod });
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 91 */     boolean successfulConnection = false;
/*    */     try {
/* 93 */       connection.connect();
/* 94 */       NetHttpResponse response = new NetHttpResponse(connection);
/* 95 */       successfulConnection = true;
/* 96 */       return response;
/*    */     } finally {
/* 98 */       if (!successfulConnection) {
/* 99 */         connection.disconnect();
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\javanet\NetHttpRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */