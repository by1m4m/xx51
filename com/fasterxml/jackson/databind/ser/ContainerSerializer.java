/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.StdSerializer;
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
/*     */ public abstract class ContainerSerializer<T>
/*     */   extends StdSerializer<T>
/*     */ {
/*     */   protected ContainerSerializer(Class<T> t)
/*     */   {
/*  26 */     super(t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ContainerSerializer(JavaType fullType)
/*     */   {
/*  33 */     super(fullType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ContainerSerializer(Class<?> t, boolean dummy)
/*     */   {
/*  43 */     super(t, dummy);
/*     */   }
/*     */   
/*     */   protected ContainerSerializer(ContainerSerializer<?> src) {
/*  47 */     super(src._handledType, false);
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
/*     */   public ContainerSerializer<?> withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  61 */     if (vts == null) return this;
/*  62 */     return _withValueTypeSerializer(vts);
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
/*     */   public abstract JavaType getContentType();
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
/*     */   public abstract JsonSerializer<?> getContentSerializer();
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
/*     */   @Deprecated
/*     */   public boolean isEmpty(T value)
/*     */   {
/* 100 */     return isEmpty(null, value);
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
/*     */   public abstract boolean hasSingleElement(T paramT);
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
/*     */   protected abstract ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer paramTypeSerializer);
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
/*     */   protected boolean hasContentTypeAnnotation(SerializerProvider provider, BeanProperty property)
/*     */   {
/* 141 */     if (property != null) {
/* 142 */       AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 143 */       AnnotatedMember m = property.getMember();
/* 144 */       if ((m != null) && (intr != null) && 
/* 145 */         (intr.findSerializationContentType(m, property.getType()) != null)) {
/* 146 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 150 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\ContainerSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */