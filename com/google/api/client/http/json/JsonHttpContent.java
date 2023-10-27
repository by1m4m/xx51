/*     */ package com.google.api.client.http.json;
/*     */ 
/*     */ import com.google.api.client.http.AbstractHttpContent;
/*     */ import com.google.api.client.http.HttpMediaType;
/*     */ import com.google.api.client.json.JsonFactory;
/*     */ import com.google.api.client.json.JsonGenerator;
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
/*     */ public class JsonHttpContent
/*     */   extends AbstractHttpContent
/*     */ {
/*     */   private final Object data;
/*     */   private final JsonFactory jsonFactory;
/*     */   private String wrapperKey;
/*     */   
/*     */   public JsonHttpContent(JsonFactory jsonFactory, Object data)
/*     */   {
/*  67 */     super("application/json; charset=UTF-8");
/*  68 */     this.jsonFactory = ((JsonFactory)Preconditions.checkNotNull(jsonFactory));
/*  69 */     this.data = Preconditions.checkNotNull(data);
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/*  73 */     JsonGenerator generator = this.jsonFactory.createJsonGenerator(out, getCharset());
/*  74 */     if (this.wrapperKey != null) {
/*  75 */       generator.writeStartObject();
/*  76 */       generator.writeFieldName(this.wrapperKey);
/*     */     }
/*  78 */     generator.serialize(this.data);
/*  79 */     if (this.wrapperKey != null) {
/*  80 */       generator.writeEndObject();
/*     */     }
/*  82 */     generator.flush();
/*     */   }
/*     */   
/*     */   public JsonHttpContent setMediaType(HttpMediaType mediaType)
/*     */   {
/*  87 */     super.setMediaType(mediaType);
/*  88 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Object getData()
/*     */   {
/*  97 */     return this.data;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final JsonFactory getJsonFactory()
/*     */   {
/* 106 */     return this.jsonFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getWrapperKey()
/*     */   {
/* 115 */     return this.wrapperKey;
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
/*     */   public JsonHttpContent setWrapperKey(String wrapperKey)
/*     */   {
/* 129 */     this.wrapperKey = wrapperKey;
/* 130 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\json\JsonHttpContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */