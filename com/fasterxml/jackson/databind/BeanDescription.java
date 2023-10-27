/*    */ package com.fasterxml.jackson.databind;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*    */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*    */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*    */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*    */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*    */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*    */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*    */ import com.fasterxml.jackson.databind.util.Annotations;
/*    */ import com.fasterxml.jackson.databind.util.Converter;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
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
/*    */ public abstract class BeanDescription
/*    */ {
/*    */   protected final JavaType _type;
/*    */   
/*    */   protected BeanDescription(JavaType type)
/*    */   {
/* 43 */     this._type = type;
/*    */   }
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
/* 56 */   public JavaType getType() { return this._type; }
/*    */   
/* 58 */   public Class<?> getBeanClass() { return this._type.getRawClass(); }
/*    */   
/*    */   public abstract AnnotatedClass getClassInfo();
/*    */   
/*    */   public abstract ObjectIdInfo getObjectIdInfo();
/*    */   
/*    */   public abstract boolean hasKnownClassAnnotations();
/*    */   
/*    */   public abstract TypeBindings bindingsForBeanType();
/*    */   
/*    */   public abstract JavaType resolveType(Type paramType);
/*    */   
/*    */   public abstract Annotations getClassAnnotations();
/*    */   
/*    */   public abstract List<BeanPropertyDefinition> findProperties();
/*    */   
/*    */   public abstract Map<String, AnnotatedMember> findBackReferenceProperties();
/*    */   
/*    */   public abstract Set<String> getIgnoredPropertyNames();
/*    */   
/*    */   public abstract List<AnnotatedConstructor> getConstructors();
/*    */   
/*    */   public abstract List<AnnotatedMethod> getFactoryMethods();
/*    */   
/*    */   public abstract AnnotatedConstructor findDefaultConstructor();
/*    */   
/*    */   public abstract Constructor<?> findSingleArgConstructor(Class<?>... paramVarArgs);
/*    */   
/*    */   public abstract Method findFactoryMethod(Class<?>... paramVarArgs);
/*    */   
/*    */   public abstract AnnotatedMember findAnyGetter();
/*    */   
/*    */   public abstract AnnotatedMethod findAnySetter();
/*    */   
/*    */   public abstract AnnotatedMethod findJsonValueMethod();
/*    */   
/*    */   public abstract AnnotatedMethod findMethod(String paramString, Class<?>[] paramArrayOfClass);
/*    */   
/*    */   public abstract JsonInclude.Include findSerializationInclusion(JsonInclude.Include paramInclude);
/*    */   
/*    */   public abstract JsonInclude.Include findSerializationInclusionForContent(JsonInclude.Include paramInclude);
/*    */   
/*    */   public abstract JsonFormat.Value findExpectedFormat(JsonFormat.Value paramValue);
/*    */   
/*    */   public abstract Converter<Object, Object> findSerializationConverter();
/*    */   
/*    */   public abstract Converter<Object, Object> findDeserializationConverter();
/*    */   
/*    */   public abstract Map<Object, AnnotatedMember> findInjectables();
/*    */   
/*    */   public abstract Class<?> findPOJOBuilder();
/*    */   
/*    */   public abstract JsonPOJOBuilder.Value findPOJOBuilderConfig();
/*    */   
/*    */   public abstract Object instantiateBean(boolean paramBoolean);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\BeanDescription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */