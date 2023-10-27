/*     */ package com.google.api.client.json.rpc2;
/*     */ 
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.GenericData;
/*     */ import com.google.api.client.util.Key;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class JsonRpcRequest
/*     */   extends GenericData
/*     */ {
/*     */   @Key
/*  36 */   private final String jsonrpc = "2.0";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Key
/*     */   private Object id;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Key
/*     */   private String method;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Key
/*     */   private Object params;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getVersion()
/*     */   {
/*  63 */     return "2.0";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getId()
/*     */   {
/*  73 */     return this.id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setId(Object id)
/*     */   {
/*  83 */     this.id = id;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMethod()
/*     */   {
/*  92 */     return this.method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMethod(String method)
/*     */   {
/* 101 */     this.method = method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getParameters()
/*     */   {
/* 111 */     return this.params;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParameters(Object parameters)
/*     */   {
/* 121 */     this.params = parameters;
/*     */   }
/*     */   
/*     */   public JsonRpcRequest set(String fieldName, Object value)
/*     */   {
/* 126 */     return (JsonRpcRequest)super.set(fieldName, value);
/*     */   }
/*     */   
/*     */   public JsonRpcRequest clone()
/*     */   {
/* 131 */     return (JsonRpcRequest)super.clone();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\json\rpc2\JsonRpcRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */