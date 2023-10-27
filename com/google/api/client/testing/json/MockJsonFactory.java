/*    */ package com.google.api.client.testing.json;
/*    */ 
/*    */ import com.google.api.client.json.JsonFactory;
/*    */ import com.google.api.client.json.JsonGenerator;
/*    */ import com.google.api.client.json.JsonParser;
/*    */ import com.google.api.client.util.Beta;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.io.Reader;
/*    */ import java.io.Writer;
/*    */ import java.nio.charset.Charset;
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
/*    */ public class MockJsonFactory
/*    */   extends JsonFactory
/*    */ {
/*    */   public JsonParser createJsonParser(InputStream in)
/*    */     throws IOException
/*    */   {
/* 45 */     return new MockJsonParser(this);
/*    */   }
/*    */   
/*    */   public JsonParser createJsonParser(InputStream in, Charset charset) throws IOException
/*    */   {
/* 50 */     return new MockJsonParser(this);
/*    */   }
/*    */   
/*    */   public JsonParser createJsonParser(String value) throws IOException
/*    */   {
/* 55 */     return new MockJsonParser(this);
/*    */   }
/*    */   
/*    */   public JsonParser createJsonParser(Reader reader) throws IOException
/*    */   {
/* 60 */     return new MockJsonParser(this);
/*    */   }
/*    */   
/*    */   public JsonGenerator createJsonGenerator(OutputStream out, Charset enc) throws IOException
/*    */   {
/* 65 */     return new MockJsonGenerator(this);
/*    */   }
/*    */   
/*    */   public JsonGenerator createJsonGenerator(Writer writer) throws IOException
/*    */   {
/* 70 */     return new MockJsonGenerator(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\json\MockJsonFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */