/*    */ package com.google.api.client.testing.json;
/*    */ 
/*    */ import com.google.api.client.json.JsonFactory;
/*    */ import com.google.api.client.json.JsonGenerator;
/*    */ import com.google.api.client.util.Beta;
/*    */ import java.io.IOException;
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
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
/*    */ @Beta
/*    */ public class MockJsonGenerator
/*    */   extends JsonGenerator
/*    */ {
/*    */   private final JsonFactory factory;
/*    */   
/*    */   MockJsonGenerator(JsonFactory factory)
/*    */   {
/* 42 */     this.factory = factory;
/*    */   }
/*    */   
/*    */   public JsonFactory getFactory()
/*    */   {
/* 47 */     return this.factory;
/*    */   }
/*    */   
/*    */   public void flush()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void close()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeStartArray()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeEndArray()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeStartObject()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeEndObject()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeFieldName(String name)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeNull()
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeString(String value)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeBoolean(boolean state)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeNumber(int v)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeNumber(long v)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeNumber(BigInteger v)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeNumber(float v)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeNumber(double v)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeNumber(BigDecimal v)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */   public void writeNumber(String encodedValue)
/*    */     throws IOException
/*    */   {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\json\MockJsonGenerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */