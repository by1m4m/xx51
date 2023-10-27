/*    */ package com.fasterxml.jackson.databind.ser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*    */ import com.fasterxml.jackson.core.JsonGenerationException;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.SerializableString;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
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
/*    */ public final class WritableObjectId
/*    */ {
/*    */   public final ObjectIdGenerator<?> generator;
/*    */   public Object id;
/* 23 */   protected boolean idWritten = false;
/*    */   
/*    */   public WritableObjectId(ObjectIdGenerator<?> generator) {
/* 26 */     this.generator = generator;
/*    */   }
/*    */   
/*    */   public boolean writeAsId(JsonGenerator jgen, SerializerProvider provider, ObjectIdWriter w)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 32 */     if ((this.id != null) && ((this.idWritten) || (w.alwaysAsId)))
/*    */     {
/* 34 */       if (jgen.canWriteObjectId()) {
/* 35 */         jgen.writeObjectRef(String.valueOf(this.id));
/*    */       } else {
/* 37 */         w.serializer.serialize(this.id, jgen, provider);
/*    */       }
/* 39 */       return true;
/*    */     }
/* 41 */     return false;
/*    */   }
/*    */   
/*    */   public Object generateId(Object forPojo) {
/* 45 */     return this.id = this.generator.generateId(forPojo);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void writeAsField(JsonGenerator jgen, SerializerProvider provider, ObjectIdWriter w)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 55 */     this.idWritten = true;
/*    */     
/*    */ 
/* 58 */     if (jgen.canWriteObjectId())
/*    */     {
/* 60 */       jgen.writeObjectId(String.valueOf(this.id));
/* 61 */       return;
/*    */     }
/*    */     
/* 64 */     SerializableString name = w.propertyName;
/* 65 */     if (name != null) {
/* 66 */       jgen.writeFieldName(name);
/* 67 */       w.serializer.serialize(this.id, jgen, provider);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\WritableObjectId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */