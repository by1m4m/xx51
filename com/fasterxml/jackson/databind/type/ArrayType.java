/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import java.lang.reflect.Array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ArrayType
/*     */   extends TypeBase
/*     */ {
/*     */   private static final long serialVersionUID = 9040058063449087477L;
/*     */   protected final JavaType _componentType;
/*     */   protected final Object _emptyArray;
/*     */   
/*     */   private ArrayType(JavaType componentType, Object emptyInstance, Object valueHandler, Object typeHandler, boolean asStatic)
/*     */   {
/*  32 */     super(emptyInstance.getClass(), componentType.hashCode(), valueHandler, typeHandler, asStatic);
/*     */     
/*  34 */     this._componentType = componentType;
/*  35 */     this._emptyArray = emptyInstance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ArrayType construct(JavaType componentType, Object valueHandler, Object typeHandler)
/*     */   {
/*  47 */     Object emptyInstance = Array.newInstance(componentType.getRawClass(), 0);
/*  48 */     return new ArrayType(componentType, emptyInstance, null, null, false);
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayType withTypeHandler(Object h)
/*     */   {
/*  54 */     if (h == this._typeHandler) {
/*  55 */       return this;
/*     */     }
/*  57 */     return new ArrayType(this._componentType, this._emptyArray, this._valueHandler, h, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayType withContentTypeHandler(Object h)
/*     */   {
/*  63 */     if (h == this._componentType.getTypeHandler()) {
/*  64 */       return this;
/*     */     }
/*  66 */     return new ArrayType(this._componentType.withTypeHandler(h), this._emptyArray, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayType withValueHandler(Object h)
/*     */   {
/*  72 */     if (h == this._valueHandler) {
/*  73 */       return this;
/*     */     }
/*  75 */     return new ArrayType(this._componentType, this._emptyArray, h, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */   public ArrayType withContentValueHandler(Object h)
/*     */   {
/*  80 */     if (h == this._componentType.getValueHandler()) {
/*  81 */       return this;
/*     */     }
/*  83 */     return new ArrayType(this._componentType.withValueHandler(h), this._emptyArray, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public ArrayType withStaticTyping()
/*     */   {
/*  89 */     if (this._asStatic) {
/*  90 */       return this;
/*     */     }
/*  92 */     return new ArrayType(this._componentType.withStaticTyping(), this._emptyArray, this._valueHandler, this._typeHandler, true);
/*     */   }
/*     */   
/*     */ 
/*     */   protected String buildCanonicalName()
/*     */   {
/*  98 */     return this._class.getName();
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
/*     */   protected JavaType _narrow(Class<?> subclass)
/*     */   {
/* 117 */     if (!subclass.isArray()) {
/* 118 */       throw new IllegalArgumentException("Incompatible narrowing operation: trying to narrow " + toString() + " to class " + subclass.getName());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 123 */     Class<?> newCompClass = subclass.getComponentType();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 131 */     JavaType newCompType = TypeFactory.defaultInstance().constructType(newCompClass);
/* 132 */     return construct(newCompType, this._valueHandler, this._typeHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType narrowContentsBy(Class<?> contentClass)
/*     */   {
/* 143 */     if (contentClass == this._componentType.getRawClass()) {
/* 144 */       return this;
/*     */     }
/* 146 */     return construct(this._componentType.narrowBy(contentClass), this._valueHandler, this._typeHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType widenContentsBy(Class<?> contentClass)
/*     */   {
/* 154 */     if (contentClass == this._componentType.getRawClass()) {
/* 155 */       return this;
/*     */     }
/* 157 */     return construct(this._componentType.widenBy(contentClass), this._valueHandler, this._typeHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isArrayType()
/*     */   {
/* 168 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAbstract()
/*     */   {
/* 176 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isConcrete()
/*     */   {
/* 184 */     return true;
/*     */   }
/*     */   
/*     */   public boolean hasGenericTypes()
/*     */   {
/* 189 */     return this._componentType.hasGenericTypes();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String containedTypeName(int index)
/*     */   {
/* 200 */     if (index == 0) return "E";
/* 201 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getParameterSource()
/*     */   {
/* 210 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isContainerType()
/*     */   {
/* 220 */     return true;
/*     */   }
/*     */   
/* 223 */   public JavaType getContentType() { return this._componentType; }
/*     */   
/*     */   public int containedTypeCount() {
/* 226 */     return 1;
/*     */   }
/*     */   
/* 229 */   public JavaType containedType(int index) { return index == 0 ? this._componentType : null; }
/*     */   
/*     */ 
/*     */   public StringBuilder getGenericSignature(StringBuilder sb)
/*     */   {
/* 234 */     sb.append('[');
/* 235 */     return this._componentType.getGenericSignature(sb);
/*     */   }
/*     */   
/*     */   public StringBuilder getErasedSignature(StringBuilder sb)
/*     */   {
/* 240 */     sb.append('[');
/* 241 */     return this._componentType.getErasedSignature(sb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 253 */     return "[array type, component type: " + this._componentType + "]";
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 259 */     if (o == this) return true;
/* 260 */     if (o == null) return false;
/* 261 */     if (o.getClass() != getClass()) { return false;
/*     */     }
/* 263 */     ArrayType other = (ArrayType)o;
/* 264 */     return this._componentType.equals(other._componentType);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\ArrayType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */