/*     */ package com.fasterxml.jackson.core;
/*     */ 
/*     */ import com.fasterxml.jackson.core.type.ResolvedType;
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ObjectCodec
/*     */   extends TreeCodec
/*     */   implements Versioned
/*     */ {
/*     */   public Version version()
/*     */   {
/*  31 */     return Version.unknownVersion();
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
/*     */   public abstract <T> T readValue(JsonParser paramJsonParser, Class<T> paramClass)
/*     */     throws IOException, JsonProcessingException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract <T> T readValue(JsonParser paramJsonParser, TypeReference<?> paramTypeReference)
/*     */     throws IOException, JsonProcessingException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract <T> T readValue(JsonParser paramJsonParser, ResolvedType paramResolvedType)
/*     */     throws IOException, JsonProcessingException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract <T> Iterator<T> readValues(JsonParser paramJsonParser, Class<T> paramClass)
/*     */     throws IOException, JsonProcessingException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract <T> Iterator<T> readValues(JsonParser paramJsonParser, TypeReference<?> paramTypeReference)
/*     */     throws IOException, JsonProcessingException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract <T> Iterator<T> readValues(JsonParser paramJsonParser, ResolvedType paramResolvedType)
/*     */     throws IOException, JsonProcessingException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void writeValue(JsonGenerator paramJsonGenerator, Object paramObject)
/*     */     throws IOException, JsonProcessingException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract <T extends TreeNode> T readTree(JsonParser paramJsonParser)
/*     */     throws IOException, JsonProcessingException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void writeTree(JsonGenerator paramJsonGenerator, TreeNode paramTreeNode)
/*     */     throws IOException, JsonProcessingException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract TreeNode createObjectNode();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract TreeNode createArrayNode();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JsonParser treeAsTokens(TreeNode paramTreeNode);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract <T> T treeToValue(TreeNode paramTreeNode, Class<T> paramClass)
/*     */     throws JsonProcessingException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public JsonFactory getJsonFactory()
/*     */   {
/* 175 */     return getFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonFactory getFactory()
/*     */   {
/* 183 */     return getJsonFactory();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\ObjectCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */