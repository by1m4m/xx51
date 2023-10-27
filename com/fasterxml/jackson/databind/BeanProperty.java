/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.Named;
/*     */ import java.lang.annotation.Annotation;
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
/*     */ public abstract interface BeanProperty
/*     */   extends Named
/*     */ {
/*     */   public abstract String getName();
/*     */   
/*     */   public abstract PropertyName getFullName();
/*     */   
/*     */   public abstract JavaType getType();
/*     */   
/*     */   public abstract PropertyName getWrapperName();
/*     */   
/*     */   public abstract PropertyMetadata getMetadata();
/*     */   
/*     */   public abstract boolean isRequired();
/*     */   
/*     */   public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
/*     */   
/*     */   public abstract <A extends Annotation> A getContextAnnotation(Class<A> paramClass);
/*     */   
/*     */   public abstract AnnotatedMember getMember();
/*     */   
/*     */   public abstract void depositSchemaProperty(JsonObjectFormatVisitor paramJsonObjectFormatVisitor)
/*     */     throws JsonMappingException;
/*     */   
/*     */   public static class Std
/*     */     implements BeanProperty
/*     */   {
/*     */     protected final PropertyName _name;
/*     */     protected final JavaType _type;
/*     */     protected final PropertyName _wrapperName;
/*     */     protected final PropertyMetadata _metadata;
/*     */     protected final AnnotatedMember _member;
/*     */     protected final Annotations _contextAnnotations;
/*     */     
/*     */     public Std(PropertyName name, JavaType type, PropertyName wrapperName, Annotations contextAnnotations, AnnotatedMember member, PropertyMetadata metadata)
/*     */     {
/* 149 */       this._name = name;
/* 150 */       this._type = type;
/* 151 */       this._wrapperName = wrapperName;
/* 152 */       this._metadata = metadata;
/* 153 */       this._member = member;
/* 154 */       this._contextAnnotations = contextAnnotations;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     @Deprecated
/*     */     public Std(String name, JavaType type, PropertyName wrapperName, Annotations contextAnnotations, AnnotatedMember member, boolean isRequired)
/*     */     {
/* 162 */       this(new PropertyName(name), type, wrapperName, contextAnnotations, member, isRequired ? PropertyMetadata.STD_REQUIRED : PropertyMetadata.STD_OPTIONAL);
/*     */     }
/*     */     
/*     */ 
/*     */     public Std withType(JavaType type)
/*     */     {
/* 168 */       return new Std(this._name, type, this._wrapperName, this._contextAnnotations, this._member, this._metadata);
/*     */     }
/*     */     
/*     */     public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */     {
/* 173 */       return this._member == null ? null : this._member.getAnnotation(acls);
/*     */     }
/*     */     
/*     */     public <A extends Annotation> A getContextAnnotation(Class<A> acls)
/*     */     {
/* 178 */       return this._contextAnnotations == null ? null : this._contextAnnotations.get(acls);
/*     */     }
/*     */     
/* 181 */     public String getName() { return this._name.getSimpleName(); }
/* 182 */     public PropertyName getFullName() { return this._name; }
/* 183 */     public JavaType getType() { return this._type; }
/* 184 */     public PropertyName getWrapperName() { return this._wrapperName; }
/* 185 */     public boolean isRequired() { return this._metadata.isRequired(); }
/* 186 */     public PropertyMetadata getMetadata() { return this._metadata; }
/* 187 */     public AnnotatedMember getMember() { return this._member; }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isVirtual()
/*     */     {
/* 196 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor)
/*     */     {
/* 206 */       throw new UnsupportedOperationException("Instances of " + getClass().getName() + " should not get visited");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\BeanProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */