/*     */ package com.google.api.client.json;
/*     */ 
/*     */ import com.google.api.client.util.Charsets;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
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
/*     */ public abstract class JsonFactory
/*     */ {
/*     */   public abstract JsonParser createJsonParser(InputStream paramInputStream)
/*     */     throws IOException;
/*     */   
/*     */   public abstract JsonParser createJsonParser(InputStream paramInputStream, Charset paramCharset)
/*     */     throws IOException;
/*     */   
/*     */   public abstract JsonParser createJsonParser(String paramString)
/*     */     throws IOException;
/*     */   
/*     */   public abstract JsonParser createJsonParser(Reader paramReader)
/*     */     throws IOException;
/*     */   
/*     */   public abstract JsonGenerator createJsonGenerator(OutputStream paramOutputStream, Charset paramCharset)
/*     */     throws IOException;
/*     */   
/*     */   public abstract JsonGenerator createJsonGenerator(Writer paramWriter)
/*     */     throws IOException;
/*     */   
/*     */   public final JsonObjectParser createJsonObjectParser()
/*     */   {
/* 101 */     return new JsonObjectParser(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String toString(Object item)
/*     */     throws IOException
/*     */   {
/* 112 */     return toString(item, false);
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
/*     */   public final String toPrettyString(Object item)
/*     */     throws IOException
/*     */   {
/* 130 */     return toString(item, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final byte[] toByteArray(Object item)
/*     */     throws IOException
/*     */   {
/* 142 */     return toByteStream(item, false).toByteArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String toString(Object item, boolean pretty)
/*     */     throws IOException
/*     */   {
/* 154 */     return toByteStream(item, pretty).toString("UTF-8");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ByteArrayOutputStream toByteStream(Object item, boolean pretty)
/*     */     throws IOException
/*     */   {
/* 166 */     ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
/* 167 */     JsonGenerator generator = createJsonGenerator(byteStream, Charsets.UTF_8);
/* 168 */     if (pretty) {
/* 169 */       generator.enablePrettyPrint();
/*     */     }
/* 171 */     generator.serialize(item);
/* 172 */     generator.flush();
/* 173 */     return byteStream;
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
/*     */   public final <T> T fromString(String value, Class<T> destinationClass)
/*     */     throws IOException
/*     */   {
/* 187 */     return (T)createJsonParser(value).parse(destinationClass);
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
/*     */   public final <T> T fromInputStream(InputStream inputStream, Class<T> destinationClass)
/*     */     throws IOException
/*     */   {
/* 206 */     return (T)createJsonParser(inputStream).parseAndClose(destinationClass);
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
/*     */   public final <T> T fromInputStream(InputStream inputStream, Charset charset, Class<T> destinationClass)
/*     */     throws IOException
/*     */   {
/* 222 */     return (T)createJsonParser(inputStream, charset).parseAndClose(destinationClass);
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
/*     */   public final <T> T fromReader(Reader reader, Class<T> destinationClass)
/*     */     throws IOException
/*     */   {
/* 236 */     return (T)createJsonParser(reader).parseAndClose(destinationClass);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\json\JsonFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */