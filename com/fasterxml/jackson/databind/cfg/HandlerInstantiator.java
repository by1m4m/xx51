/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
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
/*     */ public abstract class HandlerInstantiator
/*     */ {
/*     */   public abstract JsonDeserializer<?> deserializerInstance(DeserializationConfig paramDeserializationConfig, Annotated paramAnnotated, Class<?> paramClass);
/*     */   
/*     */   public abstract KeyDeserializer keyDeserializerInstance(DeserializationConfig paramDeserializationConfig, Annotated paramAnnotated, Class<?> paramClass);
/*     */   
/*     */   public abstract JsonSerializer<?> serializerInstance(SerializationConfig paramSerializationConfig, Annotated paramAnnotated, Class<?> paramClass);
/*     */   
/*     */   public abstract TypeResolverBuilder<?> typeResolverBuilderInstance(MapperConfig<?> paramMapperConfig, Annotated paramAnnotated, Class<?> paramClass);
/*     */   
/*     */   public abstract TypeIdResolver typeIdResolverInstance(MapperConfig<?> paramMapperConfig, Annotated paramAnnotated, Class<?> paramClass);
/*     */   
/*     */   public ValueInstantiator valueInstantiatorInstance(MapperConfig<?> config, Annotated annotated, Class<?> resolverClass)
/*     */   {
/* 114 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectIdGenerator<?> objectIdGeneratorInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass)
/*     */   {
/* 125 */     return null;
/*     */   }
/*     */   
/*     */   public ObjectIdResolver resolverIdGeneratorInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass) {
/* 129 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyNamingStrategy namingStrategyInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass)
/*     */   {
/* 140 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Converter<?, ?> converterInstance(MapperConfig<?> config, Annotated annotated, Class<?> implClass)
/*     */   {
/* 150 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public VirtualBeanPropertyWriter virtualPropertyWriterInstance(MapperConfig<?> config, Class<?> implClass)
/*     */   {
/* 161 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\cfg\HandlerInstantiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */