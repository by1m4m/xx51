/*    */ package org.apache.http.impl.entity;
/*    */ 
/*    */ import org.apache.http.Header;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.HttpVersion;
/*    */ import org.apache.http.ProtocolException;
/*    */ import org.apache.http.ProtocolVersion;
/*    */ import org.apache.http.entity.ContentLengthStrategy;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StrictContentLengthStrategy
/*    */   implements ContentLengthStrategy
/*    */ {
/*    */   public long determineLength(HttpMessage message)
/*    */     throws HttpException
/*    */   {
/* 62 */     if (message == null) {
/* 63 */       throw new IllegalArgumentException("HTTP message may not be null");
/*    */     }
/*    */     
/*    */ 
/*    */ 
/* 68 */     Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
/* 69 */     Header contentLengthHeader = message.getFirstHeader("Content-Length");
/* 70 */     if (transferEncodingHeader != null) {
/* 71 */       String s = transferEncodingHeader.getValue();
/* 72 */       if ("chunked".equalsIgnoreCase(s)) {
/* 73 */         if (message.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
/* 74 */           throw new ProtocolException("Chunked transfer encoding not allowed for " + message.getProtocolVersion());
/*    */         }
/*    */         
/*    */ 
/* 78 */         return -2L; }
/* 79 */       if ("identity".equalsIgnoreCase(s)) {
/* 80 */         return -1L;
/*    */       }
/* 82 */       throw new ProtocolException("Unsupported transfer encoding: " + s);
/*    */     }
/*    */     
/* 85 */     if (contentLengthHeader != null) {
/* 86 */       String s = contentLengthHeader.getValue();
/*    */       try {
/* 88 */         return Long.parseLong(s);
/*    */       }
/*    */       catch (NumberFormatException e) {
/* 91 */         throw new ProtocolException("Invalid content length: " + s);
/*    */       }
/*    */     }
/* 94 */     return -1L;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\entity\StrictContentLengthStrategy.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */