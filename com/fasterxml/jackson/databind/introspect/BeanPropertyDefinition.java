/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.util.EmptyIterator;
/*     */ import com.fasterxml.jackson.databind.util.Named;
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
/*     */ public abstract class BeanPropertyDefinition
/*     */   implements Named
/*     */ {
/*     */   @Deprecated
/*     */   public BeanPropertyDefinition withName(String newName)
/*     */   {
/*  29 */     return withSimpleName(newName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract BeanPropertyDefinition withName(PropertyName paramPropertyName);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract BeanPropertyDefinition withSimpleName(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String getName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract PropertyName getFullName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String getInternalName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract PropertyName getWrapperName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract PropertyMetadata getMetadata();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isExplicitlyIncluded();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isExplicitlyNamed()
/*     */   {
/* 110 */     return isExplicitlyIncluded();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */   public boolean couldDeserialize() { return getMutator() != null; }
/* 120 */   public boolean couldSerialize() { return getAccessor() != null; }
/*     */   
/*     */ 
/*     */   public abstract boolean hasGetter();
/*     */   
/*     */ 
/*     */   public abstract boolean hasSetter();
/*     */   
/*     */ 
/*     */   public abstract boolean hasField();
/*     */   
/*     */ 
/*     */   public abstract boolean hasConstructorParameter();
/*     */   
/*     */ 
/*     */   public abstract AnnotatedMethod getGetter();
/*     */   
/*     */   public abstract AnnotatedMethod getSetter();
/*     */   
/*     */   public abstract AnnotatedField getField();
/*     */   
/*     */   public abstract AnnotatedParameter getConstructorParameter();
/*     */   
/*     */   public Iterator<AnnotatedParameter> getConstructorParameters()
/*     */   {
/* 145 */     return EmptyIterator.instance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract AnnotatedMember getAccessor();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract AnnotatedMember getMutator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract AnnotatedMember getNonConstructorMutator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract AnnotatedMember getPrimaryMember();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?>[] findViews()
/*     */   {
/* 190 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public AnnotationIntrospector.ReferenceProperty findReferenceType()
/*     */   {
/* 196 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isTypeId()
/*     */   {
/* 203 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ObjectIdInfo findObjectIdInfo()
/*     */   {
/* 210 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRequired()
/*     */   {
/* 219 */     PropertyMetadata md = getMetadata();
/* 220 */     return (md != null) && (md.isRequired());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonInclude.Include findInclusion()
/*     */   {
/* 230 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\introspect\BeanPropertyDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */