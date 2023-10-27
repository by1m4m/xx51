/*     */ package com.google.api.client.testing.http.javanet;
/*     */ 
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class MockHttpURLConnection
/*     */   extends HttpURLConnection
/*     */ {
/*     */   private boolean doOutputCalled;
/*  53 */   private OutputStream outputStream = new ByteArrayOutputStream(0);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*  62 */   public static final byte[] INPUT_BUF = new byte[1];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*  71 */   public static final byte[] ERROR_BUF = new byte[5];
/*     */   
/*     */ 
/*  74 */   private InputStream inputStream = null;
/*     */   
/*     */ 
/*  77 */   private InputStream errorStream = null;
/*     */   
/*  79 */   private Map<String, List<String>> headers = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */   public MockHttpURLConnection(URL u)
/*     */   {
/*  85 */     super(u);
/*     */   }
/*     */   
/*     */ 
/*     */   public void disconnect() {}
/*     */   
/*     */ 
/*     */   public boolean usingProxy()
/*     */   {
/*  94 */     return false;
/*     */   }
/*     */   
/*     */   public void connect()
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */   public int getResponseCode() throws IOException
/*     */   {
/* 103 */     return this.responseCode;
/*     */   }
/*     */   
/*     */   public void setDoOutput(boolean dooutput)
/*     */   {
/* 108 */     this.doOutputCalled = true;
/*     */   }
/*     */   
/*     */   public OutputStream getOutputStream() throws IOException
/*     */   {
/* 113 */     if (this.outputStream != null) {
/* 114 */       return this.outputStream;
/*     */     }
/* 116 */     return super.getOutputStream();
/*     */   }
/*     */   
/*     */   public final boolean doOutputCalled()
/*     */   {
/* 121 */     return this.doOutputCalled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockHttpURLConnection setOutputStream(OutputStream outputStream)
/*     */   {
/* 133 */     this.outputStream = outputStream;
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public MockHttpURLConnection setResponseCode(int responseCode)
/*     */   {
/* 139 */     Preconditions.checkArgument(responseCode >= -1);
/* 140 */     this.responseCode = responseCode;
/* 141 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockHttpURLConnection addHeader(String name, String value)
/*     */   {
/* 150 */     Preconditions.checkNotNull(name);
/* 151 */     Preconditions.checkNotNull(value);
/* 152 */     if (this.headers.containsKey(name)) {
/* 153 */       ((List)this.headers.get(name)).add(value);
/*     */     } else {
/* 155 */       List<String> values = new ArrayList();
/* 156 */       values.add(value);
/* 157 */       this.headers.put(name, values);
/*     */     }
/* 159 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockHttpURLConnection setInputStream(InputStream is)
/*     */   {
/* 170 */     Preconditions.checkNotNull(is);
/* 171 */     if (this.inputStream == null) {
/* 172 */       this.inputStream = is;
/*     */     }
/* 174 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockHttpURLConnection setErrorStream(InputStream is)
/*     */   {
/* 185 */     Preconditions.checkNotNull(is);
/* 186 */     if (this.errorStream == null) {
/* 187 */       this.errorStream = is;
/*     */     }
/* 189 */     return this;
/*     */   }
/*     */   
/*     */   public InputStream getInputStream() throws IOException
/*     */   {
/* 194 */     if (this.responseCode < 400) {
/* 195 */       return this.inputStream;
/*     */     }
/* 197 */     throw new IOException();
/*     */   }
/*     */   
/*     */   public InputStream getErrorStream()
/*     */   {
/* 202 */     return this.errorStream;
/*     */   }
/*     */   
/*     */   public Map<String, List<String>> getHeaderFields()
/*     */   {
/* 207 */     return this.headers;
/*     */   }
/*     */   
/*     */   public String getHeaderField(String name)
/*     */   {
/* 212 */     List<String> values = (List)this.headers.get(name);
/* 213 */     return values == null ? null : (String)values.get(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\http\javanet\MockHttpURLConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */