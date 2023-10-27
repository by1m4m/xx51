/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MapType
/*     */   extends MapLikeType
/*     */ {
/*     */   private static final long serialVersionUID = -811146779148281500L;
/*     */   
/*     */   private MapType(Class<?> mapType, JavaType keyT, JavaType valueT, Object valueHandler, Object typeHandler, boolean asStatic)
/*     */   {
/*  20 */     super(mapType, keyT, valueT, valueHandler, typeHandler, asStatic);
/*     */   }
/*     */   
/*     */   public static MapType construct(Class<?> rawType, JavaType keyT, JavaType valueT)
/*     */   {
/*  25 */     return new MapType(rawType, keyT, valueT, null, null, false);
/*     */   }
/*     */   
/*     */   protected JavaType _narrow(Class<?> subclass)
/*     */   {
/*  30 */     return new MapType(subclass, this._keyType, this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType narrowContentsBy(Class<?> contentClass)
/*     */   {
/*  38 */     if (contentClass == this._valueType.getRawClass()) {
/*  39 */       return this;
/*     */     }
/*  41 */     return new MapType(this._class, this._keyType, this._valueType.narrowBy(contentClass), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JavaType widenContentsBy(Class<?> contentClass)
/*     */   {
/*  48 */     if (contentClass == this._valueType.getRawClass()) {
/*  49 */       return this;
/*     */     }
/*  51 */     return new MapType(this._class, this._keyType, this._valueType.widenBy(contentClass), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType narrowKey(Class<?> keySubclass)
/*     */   {
/*  59 */     if (keySubclass == this._keyType.getRawClass()) {
/*  60 */       return this;
/*     */     }
/*  62 */     return new MapType(this._class, this._keyType.narrowBy(keySubclass), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType widenKey(Class<?> keySubclass)
/*     */   {
/*  70 */     if (keySubclass == this._keyType.getRawClass()) {
/*  71 */       return this;
/*     */     }
/*  73 */     return new MapType(this._class, this._keyType.widenBy(keySubclass), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public MapType withTypeHandler(Object h)
/*     */   {
/*  79 */     return new MapType(this._class, this._keyType, this._valueType, this._valueHandler, h, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public MapType withContentTypeHandler(Object h)
/*     */   {
/*  85 */     return new MapType(this._class, this._keyType, this._valueType.withTypeHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public MapType withValueHandler(Object h)
/*     */   {
/*  91 */     return new MapType(this._class, this._keyType, this._valueType, h, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */   public MapType withContentValueHandler(Object h)
/*     */   {
/*  96 */     return new MapType(this._class, this._keyType, this._valueType.withValueHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public MapType withStaticTyping()
/*     */   {
/* 102 */     if (this._asStatic) {
/* 103 */       return this;
/*     */     }
/* 105 */     return new MapType(this._class, this._keyType.withStaticTyping(), this._valueType.withStaticTyping(), this._valueHandler, this._typeHandler, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getParameterSource()
/*     */   {
/* 117 */     return Map.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapType withKeyTypeHandler(Object h)
/*     */   {
/* 129 */     return new MapType(this._class, this._keyType.withTypeHandler(h), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public MapType withKeyValueHandler(Object h)
/*     */   {
/* 135 */     return new MapType(this._class, this._keyType.withValueHandler(h), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
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
/*     */   public String toString()
/*     */   {
/* 148 */     return "[map type; class " + this._class.getName() + ", " + this._keyType + " -> " + this._valueType + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\MapType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */