/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.core.type.ResolvedType;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
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
/*     */ public abstract class JavaType
/*     */   extends ResolvedType
/*     */   implements Serializable, Type
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Class<?> _class;
/*     */   protected final int _hash;
/*     */   protected final Object _valueHandler;
/*     */   protected final Object _typeHandler;
/*     */   protected final boolean _asStatic;
/*     */   
/*     */   protected JavaType(Class<?> raw, int additionalHash, Object valueHandler, Object typeHandler, boolean asStatic)
/*     */   {
/*  77 */     this._class = raw;
/*  78 */     this._hash = (raw.getName().hashCode() + additionalHash);
/*  79 */     this._valueHandler = valueHandler;
/*  80 */     this._typeHandler = typeHandler;
/*  81 */     this._asStatic = asStatic;
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
/*     */   public abstract JavaType withTypeHandler(Object paramObject);
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
/*     */   public abstract JavaType withContentTypeHandler(Object paramObject);
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
/*     */   public abstract JavaType withValueHandler(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JavaType withContentValueHandler(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JavaType withStaticTyping();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType narrowBy(Class<?> subclass)
/*     */   {
/* 147 */     if (subclass == this._class) { return this;
/*     */     }
/* 149 */     _assertSubclass(subclass, this._class);
/* 150 */     JavaType result = _narrow(subclass);
/*     */     
/*     */ 
/* 153 */     if (this._valueHandler != result.getValueHandler()) {
/* 154 */       result = result.withValueHandler(this._valueHandler);
/*     */     }
/* 156 */     if (this._typeHandler != result.getTypeHandler()) {
/* 157 */       result = result.withTypeHandler(this._typeHandler);
/*     */     }
/* 159 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType forcedNarrowBy(Class<?> subclass)
/*     */   {
/* 169 */     if (subclass == this._class) {
/* 170 */       return this;
/*     */     }
/* 172 */     JavaType result = _narrow(subclass);
/*     */     
/* 174 */     if (this._valueHandler != result.getValueHandler()) {
/* 175 */       result = result.withValueHandler(this._valueHandler);
/*     */     }
/* 177 */     if (this._typeHandler != result.getTypeHandler()) {
/* 178 */       result = result.withTypeHandler(this._typeHandler);
/*     */     }
/* 180 */     return result;
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
/*     */   public JavaType widenBy(Class<?> superclass)
/*     */   {
/* 194 */     if (superclass == this._class) { return this;
/*     */     }
/* 196 */     _assertSubclass(this._class, superclass);
/* 197 */     return _widen(superclass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract JavaType _narrow(Class<?> paramClass);
/*     */   
/*     */ 
/*     */   protected JavaType _widen(Class<?> superclass)
/*     */   {
/* 207 */     return _narrow(superclass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract JavaType narrowContentsBy(Class<?> paramClass);
/*     */   
/*     */ 
/*     */   public abstract JavaType widenContentsBy(Class<?> paramClass);
/*     */   
/*     */ 
/*     */   public final Class<?> getRawClass()
/*     */   {
/* 220 */     return this._class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean hasRawClass(Class<?> clz)
/*     */   {
/* 228 */     return this._class == clz;
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/* 232 */     return Modifier.isAbstract(this._class.getModifiers());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isConcrete()
/*     */   {
/* 242 */     int mod = this._class.getModifiers();
/* 243 */     if ((mod & 0x600) == 0) {
/* 244 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 249 */     return this._class.isPrimitive();
/*     */   }
/*     */   
/*     */   public boolean isThrowable() {
/* 253 */     return Throwable.class.isAssignableFrom(this._class);
/*     */   }
/*     */   
/* 256 */   public boolean isArrayType() { return false; }
/*     */   
/*     */   public final boolean isEnumType() {
/* 259 */     return this._class.isEnum();
/*     */   }
/*     */   
/* 262 */   public final boolean isInterface() { return this._class.isInterface(); }
/*     */   
/*     */   public final boolean isPrimitive() {
/* 265 */     return this._class.isPrimitive();
/*     */   }
/*     */   
/* 268 */   public final boolean isFinal() { return Modifier.isFinal(this._class.getModifiers()); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isContainerType();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCollectionLikeType()
/*     */   {
/* 283 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isMapLikeType()
/*     */   {
/* 291 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isJavaLangObject()
/*     */   {
/* 302 */     return this._class == Object.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean useStaticType()
/*     */   {
/* 312 */     return this._asStatic;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasGenericTypes()
/*     */   {
/* 321 */     return containedTypeCount() > 0;
/*     */   }
/*     */   
/* 324 */   public JavaType getKeyType() { return null; }
/*     */   
/*     */   public JavaType getContentType() {
/* 327 */     return null;
/*     */   }
/*     */   
/* 330 */   public int containedTypeCount() { return 0; }
/*     */   
/*     */   public JavaType containedType(int index) {
/* 333 */     return null;
/*     */   }
/*     */   
/* 336 */   public String containedTypeName(int index) { return null; }
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
/*     */   public abstract Class<?> getParameterSource();
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
/*     */   public JavaType containedTypeOrUnknown(int index)
/*     */   {
/* 364 */     JavaType t = containedType(index);
/* 365 */     return t == null ? TypeFactory.unknownType() : t;
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
/*     */   public <T> T getValueHandler()
/*     */   {
/* 378 */     return (T)this._valueHandler;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T getTypeHandler()
/*     */   {
/* 384 */     return (T)this._typeHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getGenericSignature()
/*     */   {
/* 403 */     StringBuilder sb = new StringBuilder(40);
/* 404 */     getGenericSignature(sb);
/* 405 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract StringBuilder getGenericSignature(StringBuilder paramStringBuilder);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getErasedSignature()
/*     */   {
/* 424 */     StringBuilder sb = new StringBuilder(40);
/* 425 */     getErasedSignature(sb);
/* 426 */     return sb.toString();
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
/*     */   public abstract StringBuilder getErasedSignature(StringBuilder paramStringBuilder);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _assertSubclass(Class<?> subclass, Class<?> superClass)
/*     */   {
/* 449 */     if (!this._class.isAssignableFrom(subclass)) {
/* 450 */       throw new IllegalArgumentException("Class " + subclass.getName() + " is not assignable to " + this._class.getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String toString();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract boolean equals(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */   public final int hashCode()
/*     */   {
/* 467 */     return this._hash;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\JavaType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */