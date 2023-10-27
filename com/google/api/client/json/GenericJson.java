/*    */ package com.google.api.client.json;
/*    */ 
/*    */ import com.google.api.client.util.GenericData;
/*    */ import com.google.api.client.util.Throwables;
/*    */ import java.io.IOException;
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
/*    */ public class GenericJson
/*    */   extends GenericData
/*    */   implements Cloneable
/*    */ {
/*    */   private JsonFactory jsonFactory;
/*    */   
/*    */   public final JsonFactory getFactory()
/*    */   {
/* 51 */     return this.jsonFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final void setFactory(JsonFactory factory)
/*    */   {
/* 60 */     this.jsonFactory = factory;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 65 */     if (this.jsonFactory != null) {
/*    */       try {
/* 67 */         return this.jsonFactory.toString(this);
/*    */       } catch (IOException e) {
/* 69 */         throw Throwables.propagate(e);
/*    */       }
/*    */     }
/* 72 */     return super.toString();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toPrettyString()
/*    */     throws IOException
/*    */   {
/* 82 */     if (this.jsonFactory != null) {
/* 83 */       return this.jsonFactory.toPrettyString(this);
/*    */     }
/* 85 */     return super.toString();
/*    */   }
/*    */   
/*    */   public GenericJson clone()
/*    */   {
/* 90 */     return (GenericJson)super.clone();
/*    */   }
/*    */   
/*    */   public GenericJson set(String fieldName, Object value)
/*    */   {
/* 95 */     return (GenericJson)super.set(fieldName, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\json\GenericJson.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */