/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.SerializationFeature;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class ArraySerializerBase<T>
/*    */   extends ContainerSerializer<T>
/*    */ {
/*    */   protected final BeanProperty _property;
/*    */   
/*    */   protected ArraySerializerBase(Class<T> cls)
/*    */   {
/* 24 */     super(cls);
/* 25 */     this._property = null;
/*    */   }
/*    */   
/*    */   protected ArraySerializerBase(Class<T> cls, BeanProperty property)
/*    */   {
/* 30 */     super(cls);
/* 31 */     this._property = property;
/*    */   }
/*    */   
/*    */   protected ArraySerializerBase(ArraySerializerBase<?> src)
/*    */   {
/* 36 */     super(src._handledType, false);
/* 37 */     this._property = src._property;
/*    */   }
/*    */   
/*    */   protected ArraySerializerBase(ArraySerializerBase<?> src, BeanProperty property)
/*    */   {
/* 42 */     super(src._handledType, false);
/* 43 */     this._property = property;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void serialize(T value, JsonGenerator gen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 52 */     if ((provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) && (hasSingleElement(value)))
/*    */     {
/* 54 */       serializeContents(value, gen, provider);
/* 55 */       return;
/*    */     }
/* 57 */     gen.writeStartArray();
/*    */     
/* 59 */     gen.setCurrentValue(value);
/* 60 */     serializeContents(value, gen, provider);
/* 61 */     gen.writeEndArray();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final void serializeWithType(T value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*    */     throws IOException
/*    */   {
/* 70 */     typeSer.writeTypePrefixForArray(value, gen);
/*    */     
/* 72 */     gen.setCurrentValue(value);
/* 73 */     serializeContents(value, gen, provider);
/* 74 */     typeSer.writeTypeSuffixForArray(value, gen);
/*    */   }
/*    */   
/*    */   protected abstract void serializeContents(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*    */     throws IOException;
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\ArraySerializerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */