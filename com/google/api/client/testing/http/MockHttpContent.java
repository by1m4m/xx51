/*     */ package com.google.api.client.testing.http;
/*     */ 
/*     */ import com.google.api.client.http.HttpContent;
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public class MockHttpContent
/*     */   implements HttpContent
/*     */ {
/*  39 */   private long length = -1L;
/*     */   
/*     */ 
/*     */   private String type;
/*     */   
/*     */ 
/*  45 */   private byte[] content = new byte[0];
/*     */   
/*     */   public long getLength() throws IOException {
/*  48 */     return this.length;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  52 */     return this.type;
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/*  56 */     out.write(this.content);
/*  57 */     out.flush();
/*     */   }
/*     */   
/*     */   public boolean retrySupported() {
/*  61 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final byte[] getContent()
/*     */   {
/*  70 */     return this.content;
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
/*     */   public MockHttpContent setContent(byte[] content)
/*     */   {
/*  83 */     this.content = ((byte[])Preconditions.checkNotNull(content));
/*  84 */     return this;
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
/*     */   public MockHttpContent setLength(long length)
/*     */   {
/*  97 */     Preconditions.checkArgument(length >= -1L);
/*  98 */     this.length = length;
/*  99 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MockHttpContent setType(String type)
/*     */   {
/* 108 */     this.type = type;
/* 109 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\http\MockHttpContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */