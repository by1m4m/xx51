/*     */ package com.google.api.client.http.javanet;
/*     */ 
/*     */ import com.google.api.client.http.LowLevelHttpResponse;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ final class NetHttpResponse
/*     */   extends LowLevelHttpResponse
/*     */ {
/*     */   private final HttpURLConnection connection;
/*     */   private final int responseCode;
/*     */   private final String responseMessage;
/*  32 */   private final ArrayList<String> headerNames = new ArrayList();
/*  33 */   private final ArrayList<String> headerValues = new ArrayList();
/*     */   
/*     */   NetHttpResponse(HttpURLConnection connection) throws IOException {
/*  36 */     this.connection = connection;
/*  37 */     int responseCode = connection.getResponseCode();
/*  38 */     this.responseCode = (responseCode == -1 ? 0 : responseCode);
/*  39 */     this.responseMessage = connection.getResponseMessage();
/*  40 */     List<String> headerNames = this.headerNames;
/*  41 */     List<String> headerValues = this.headerValues;
/*  42 */     for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
/*  43 */       key = (String)entry.getKey();
/*  44 */       if (key != null) {
/*  45 */         for (String value : (List)entry.getValue()) {
/*  46 */           if (value != null) {
/*  47 */             headerNames.add(key);
/*  48 */             headerValues.add(value);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     String key;
/*     */   }
/*     */   
/*     */   public int getStatusCode() {
/*  57 */     return this.responseCode;
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
/*     */   public InputStream getContent()
/*     */     throws IOException
/*     */   {
/*  88 */     InputStream in = null;
/*     */     try {
/*  90 */       in = this.connection.getInputStream();
/*     */     } catch (IOException ioe) {
/*  92 */       in = this.connection.getErrorStream();
/*     */     }
/*  94 */     return in == null ? null : new SizeValidatingInputStream(in);
/*     */   }
/*     */   
/*     */   public String getContentEncoding()
/*     */   {
/*  99 */     return this.connection.getContentEncoding();
/*     */   }
/*     */   
/*     */   public long getContentLength()
/*     */   {
/* 104 */     String string = this.connection.getHeaderField("Content-Length");
/* 105 */     return string == null ? -1L : Long.parseLong(string);
/*     */   }
/*     */   
/*     */   public String getContentType()
/*     */   {
/* 110 */     return this.connection.getHeaderField("Content-Type");
/*     */   }
/*     */   
/*     */   public String getReasonPhrase()
/*     */   {
/* 115 */     return this.responseMessage;
/*     */   }
/*     */   
/*     */   public String getStatusLine()
/*     */   {
/* 120 */     String result = this.connection.getHeaderField(0);
/* 121 */     return (result != null) && (result.startsWith("HTTP/1.")) ? result : null;
/*     */   }
/*     */   
/*     */   public int getHeaderCount()
/*     */   {
/* 126 */     return this.headerNames.size();
/*     */   }
/*     */   
/*     */   public String getHeaderName(int index)
/*     */   {
/* 131 */     return (String)this.headerNames.get(index);
/*     */   }
/*     */   
/*     */   public String getHeaderValue(int index)
/*     */   {
/* 136 */     return (String)this.headerValues.get(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void disconnect()
/*     */   {
/* 146 */     this.connection.disconnect();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final class SizeValidatingInputStream
/*     */     extends FilterInputStream
/*     */   {
/* 156 */     private long bytesRead = 0L;
/*     */     
/*     */     public SizeValidatingInputStream(InputStream in) {
/* 159 */       super();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int read(byte[] b, int off, int len)
/*     */       throws IOException
/*     */     {
/* 169 */       int n = this.in.read(b, off, len);
/* 170 */       if (n == -1) {
/* 171 */         throwIfFalseEOF();
/*     */       } else {
/* 173 */         this.bytesRead += n;
/*     */       }
/* 175 */       return n;
/*     */     }
/*     */     
/*     */     public int read() throws IOException
/*     */     {
/* 180 */       int n = this.in.read();
/* 181 */       if (n == -1) {
/* 182 */         throwIfFalseEOF();
/*     */       } else {
/* 184 */         this.bytesRead += 1L;
/*     */       }
/* 186 */       return n;
/*     */     }
/*     */     
/*     */     private void throwIfFalseEOF() throws IOException
/*     */     {
/* 191 */       long contentLength = NetHttpResponse.this.getContentLength();
/* 192 */       if (contentLength == -1L)
/*     */       {
/* 194 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 201 */       if ((this.bytesRead != 0L) && (this.bytesRead < contentLength)) {
/* 202 */         long l1 = this.bytesRead;long l2 = contentLength;throw new IOException(102 + "Connection closed prematurely: bytesRead = " + l1 + ", Content-Length = " + l2);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\javanet\NetHttpResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */