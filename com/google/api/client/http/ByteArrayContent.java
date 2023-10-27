/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.StringUtils;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ByteArrayContent
/*     */   extends AbstractInputStreamContent
/*     */ {
/*     */   private final byte[] byteArray;
/*     */   private final int offset;
/*     */   private final int length;
/*     */   
/*     */   public ByteArrayContent(String type, byte[] array)
/*     */   {
/*  65 */     this(type, array, 0, array.length);
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
/*     */   public ByteArrayContent(String type, byte[] array, int offset, int length)
/*     */   {
/*  79 */     super(type);
/*  80 */     this.byteArray = ((byte[])Preconditions.checkNotNull(array));
/*  81 */     Preconditions.checkArgument((offset >= 0) && (length >= 0) && (offset + length <= array.length), "offset %s, length %s, array length %s", new Object[] { Integer.valueOf(offset), Integer.valueOf(length), Integer.valueOf(array.length) });
/*     */     
/*  83 */     this.offset = offset;
/*  84 */     this.length = length;
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
/*     */   public static ByteArrayContent fromString(String type, String contentString)
/*     */   {
/* 107 */     return new ByteArrayContent(type, StringUtils.getBytesUtf8(contentString));
/*     */   }
/*     */   
/*     */   public long getLength() {
/* 111 */     return this.length;
/*     */   }
/*     */   
/*     */   public boolean retrySupported() {
/* 115 */     return true;
/*     */   }
/*     */   
/*     */   public InputStream getInputStream()
/*     */   {
/* 120 */     return new ByteArrayInputStream(this.byteArray, this.offset, this.length);
/*     */   }
/*     */   
/*     */   public ByteArrayContent setType(String type)
/*     */   {
/* 125 */     return (ByteArrayContent)super.setType(type);
/*     */   }
/*     */   
/*     */   public ByteArrayContent setCloseInputStream(boolean closeInputStream)
/*     */   {
/* 130 */     return (ByteArrayContent)super.setCloseInputStream(closeInputStream);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\ByteArrayContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */