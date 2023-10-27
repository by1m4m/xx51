/*     */ package com.google.api.client.testing.json;
/*     */ 
/*     */ import com.google.api.client.json.JsonFactory;
/*     */ import com.google.api.client.json.JsonParser;
/*     */ import com.google.api.client.json.JsonToken;
/*     */ import com.google.api.client.util.Beta;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
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
/*     */ public class MockJsonParser
/*     */   extends JsonParser
/*     */ {
/*     */   private boolean isClosed;
/*     */   private final JsonFactory factory;
/*     */   
/*     */   MockJsonParser(JsonFactory factory)
/*     */   {
/*  45 */     this.factory = factory;
/*     */   }
/*     */   
/*     */   public JsonFactory getFactory()
/*     */   {
/*  50 */     return this.factory;
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/*  55 */     this.isClosed = true;
/*     */   }
/*     */   
/*     */   public JsonToken nextToken() throws IOException
/*     */   {
/*  60 */     return null;
/*     */   }
/*     */   
/*     */   public JsonToken getCurrentToken()
/*     */   {
/*  65 */     return null;
/*     */   }
/*     */   
/*     */   public String getCurrentName() throws IOException
/*     */   {
/*  70 */     return null;
/*     */   }
/*     */   
/*     */   public JsonParser skipChildren() throws IOException
/*     */   {
/*  75 */     return null;
/*     */   }
/*     */   
/*     */   public String getText() throws IOException
/*     */   {
/*  80 */     return null;
/*     */   }
/*     */   
/*     */   public byte getByteValue() throws IOException
/*     */   {
/*  85 */     return 0;
/*     */   }
/*     */   
/*     */   public short getShortValue() throws IOException
/*     */   {
/*  90 */     return 0;
/*     */   }
/*     */   
/*     */   public int getIntValue() throws IOException
/*     */   {
/*  95 */     return 0;
/*     */   }
/*     */   
/*     */   public float getFloatValue() throws IOException
/*     */   {
/* 100 */     return 0.0F;
/*     */   }
/*     */   
/*     */   public long getLongValue() throws IOException
/*     */   {
/* 105 */     return 0L;
/*     */   }
/*     */   
/*     */   public double getDoubleValue() throws IOException
/*     */   {
/* 110 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public BigInteger getBigIntegerValue() throws IOException
/*     */   {
/* 115 */     return null;
/*     */   }
/*     */   
/*     */   public BigDecimal getDecimalValue() throws IOException
/*     */   {
/* 120 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 129 */     return this.isClosed;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\json\MockJsonParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */