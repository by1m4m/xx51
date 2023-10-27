/*     */ package com.google.api.client.json;
/*     */ 
/*     */ import com.google.api.client.util.ObjectParser;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.Sets;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public class JsonObjectParser
/*     */   implements ObjectParser
/*     */ {
/*     */   private final JsonFactory jsonFactory;
/*     */   private final Set<String> wrapperKeys;
/*     */   
/*     */   public JsonObjectParser(JsonFactory jsonFactory)
/*     */   {
/*  65 */     this(new Builder(jsonFactory));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonObjectParser(Builder builder)
/*     */   {
/*  74 */     this.jsonFactory = builder.jsonFactory;
/*  75 */     this.wrapperKeys = new HashSet(builder.wrapperKeys);
/*     */   }
/*     */   
/*     */   public <T> T parseAndClose(InputStream in, Charset charset, Class<T> dataClass)
/*     */     throws IOException
/*     */   {
/*  81 */     return (T)parseAndClose(in, charset, dataClass);
/*     */   }
/*     */   
/*     */   public Object parseAndClose(InputStream in, Charset charset, Type dataType) throws IOException {
/*  85 */     JsonParser parser = this.jsonFactory.createJsonParser(in, charset);
/*  86 */     initializeParser(parser);
/*  87 */     return parser.parse(dataType, true);
/*     */   }
/*     */   
/*     */   public <T> T parseAndClose(Reader reader, Class<T> dataClass) throws IOException
/*     */   {
/*  92 */     return (T)parseAndClose(reader, dataClass);
/*     */   }
/*     */   
/*     */   public Object parseAndClose(Reader reader, Type dataType) throws IOException {
/*  96 */     JsonParser parser = this.jsonFactory.createJsonParser(reader);
/*  97 */     initializeParser(parser);
/*  98 */     return parser.parse(dataType, true);
/*     */   }
/*     */   
/*     */   public final JsonFactory getJsonFactory()
/*     */   {
/* 103 */     return this.jsonFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getWrapperKeys()
/*     */   {
/* 112 */     return Collections.unmodifiableSet(this.wrapperKeys);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initializeParser(JsonParser parser)
/*     */     throws IOException
/*     */   {
/* 121 */     if (this.wrapperKeys.isEmpty()) {
/* 122 */       return;
/*     */     }
/* 124 */     boolean failed = true;
/*     */     try {
/* 126 */       String match = parser.skipToKey(this.wrapperKeys);
/* 127 */       Preconditions.checkArgument((match != null) && (parser.getCurrentToken() != JsonToken.END_OBJECT), "wrapper key(s) not found: %s", new Object[] { this.wrapperKeys });
/*     */       
/* 129 */       failed = false;
/*     */     } finally {
/* 131 */       if (failed) {
/* 132 */         parser.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Builder
/*     */   {
/*     */     final JsonFactory jsonFactory;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 152 */     Collection<String> wrapperKeys = Sets.newHashSet();
/*     */     
/*     */ 
/*     */ 
/*     */     public Builder(JsonFactory jsonFactory)
/*     */     {
/* 158 */       this.jsonFactory = ((JsonFactory)Preconditions.checkNotNull(jsonFactory));
/*     */     }
/*     */     
/*     */     public JsonObjectParser build()
/*     */     {
/* 163 */       return new JsonObjectParser(this);
/*     */     }
/*     */     
/*     */     public final JsonFactory getJsonFactory()
/*     */     {
/* 168 */       return this.jsonFactory;
/*     */     }
/*     */     
/*     */     public final Collection<String> getWrapperKeys()
/*     */     {
/* 173 */       return this.wrapperKeys;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder setWrapperKeys(Collection<String> wrapperKeys)
/*     */     {
/* 185 */       this.wrapperKeys = wrapperKeys;
/* 186 */       return this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\json\JsonObjectParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */