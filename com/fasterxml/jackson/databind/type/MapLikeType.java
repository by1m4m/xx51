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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MapLikeType
/*     */   extends TypeBase
/*     */ {
/*     */   private static final long serialVersionUID = 416067702302823522L;
/*     */   protected final JavaType _keyType;
/*     */   protected final JavaType _valueType;
/*     */   
/*     */   protected MapLikeType(Class<?> mapType, JavaType keyT, JavaType valueT, Object valueHandler, Object typeHandler, boolean asStatic)
/*     */   {
/*  38 */     super(mapType, keyT.hashCode() ^ valueT.hashCode(), valueHandler, typeHandler, asStatic);
/*  39 */     this._keyType = keyT;
/*  40 */     this._valueType = valueT;
/*     */   }
/*     */   
/*     */ 
/*     */   public static MapLikeType construct(Class<?> rawType, JavaType keyT, JavaType valueT)
/*     */   {
/*  46 */     return new MapLikeType(rawType, keyT, valueT, null, null, false);
/*     */   }
/*     */   
/*     */   protected JavaType _narrow(Class<?> subclass)
/*     */   {
/*  51 */     return new MapLikeType(subclass, this._keyType, this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JavaType narrowContentsBy(Class<?> contentClass)
/*     */   {
/*  58 */     if (contentClass == this._valueType.getRawClass()) {
/*  59 */       return this;
/*     */     }
/*  61 */     return new MapLikeType(this._class, this._keyType, this._valueType.narrowBy(contentClass), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JavaType widenContentsBy(Class<?> contentClass)
/*     */   {
/*  68 */     if (contentClass == this._valueType.getRawClass()) {
/*  69 */       return this;
/*     */     }
/*  71 */     return new MapLikeType(this._class, this._keyType, this._valueType.widenBy(contentClass), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JavaType narrowKey(Class<?> keySubclass)
/*     */   {
/*  78 */     if (keySubclass == this._keyType.getRawClass()) {
/*  79 */       return this;
/*     */     }
/*  81 */     return new MapLikeType(this._class, this._keyType.narrowBy(keySubclass), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JavaType widenKey(Class<?> keySubclass)
/*     */   {
/*  88 */     if (keySubclass == this._keyType.getRawClass()) {
/*  89 */       return this;
/*     */     }
/*  91 */     return new MapLikeType(this._class, this._keyType.widenBy(keySubclass), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MapLikeType withTypeHandler(Object h)
/*     */   {
/*  98 */     return new MapLikeType(this._class, this._keyType, this._valueType, this._valueHandler, h, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public MapLikeType withContentTypeHandler(Object h)
/*     */   {
/* 104 */     return new MapLikeType(this._class, this._keyType, this._valueType.withTypeHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public MapLikeType withValueHandler(Object h)
/*     */   {
/* 110 */     return new MapLikeType(this._class, this._keyType, this._valueType, h, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */   public MapLikeType withContentValueHandler(Object h)
/*     */   {
/* 115 */     return new MapLikeType(this._class, this._keyType, this._valueType.withValueHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public MapLikeType withStaticTyping()
/*     */   {
/* 121 */     if (this._asStatic) {
/* 122 */       return this;
/*     */     }
/* 124 */     return new MapLikeType(this._class, this._keyType, this._valueType.withStaticTyping(), this._valueHandler, this._typeHandler, true);
/*     */   }
/*     */   
/*     */ 
/*     */   protected String buildCanonicalName()
/*     */   {
/* 130 */     StringBuilder sb = new StringBuilder();
/* 131 */     sb.append(this._class.getName());
/* 132 */     if (this._keyType != null) {
/* 133 */       sb.append('<');
/* 134 */       sb.append(this._keyType.toCanonical());
/* 135 */       sb.append(',');
/* 136 */       sb.append(this._valueType.toCanonical());
/* 137 */       sb.append('>');
/*     */     }
/* 139 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isContainerType()
/*     */   {
/* 149 */     return true;
/*     */   }
/*     */   
/* 152 */   public boolean isMapLikeType() { return true; }
/*     */   
/*     */   public JavaType getKeyType() {
/* 155 */     return this._keyType;
/*     */   }
/*     */   
/* 158 */   public JavaType getContentType() { return this._valueType; }
/*     */   
/*     */   public int containedTypeCount() {
/* 161 */     return 2;
/*     */   }
/*     */   
/*     */   public JavaType containedType(int index) {
/* 165 */     if (index == 0) return this._keyType;
/* 166 */     if (index == 1) return this._valueType;
/* 167 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String containedTypeName(int index)
/*     */   {
/* 177 */     if (index == 0) return "K";
/* 178 */     if (index == 1) return "V";
/* 179 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Class<?> getParameterSource()
/*     */   {
/* 186 */     return null;
/*     */   }
/*     */   
/*     */   public StringBuilder getErasedSignature(StringBuilder sb)
/*     */   {
/* 191 */     return _classSignature(this._class, sb, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuilder getGenericSignature(StringBuilder sb)
/*     */   {
/* 197 */     _classSignature(this._class, sb, false);
/* 198 */     sb.append('<');
/* 199 */     this._keyType.getGenericSignature(sb);
/* 200 */     this._valueType.getGenericSignature(sb);
/* 201 */     sb.append(">;");
/* 202 */     return sb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapLikeType withKeyTypeHandler(Object h)
/*     */   {
/* 213 */     return new MapLikeType(this._class, this._keyType.withTypeHandler(h), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */   public MapLikeType withKeyValueHandler(Object h)
/*     */   {
/* 218 */     return new MapLikeType(this._class, this._keyType.withValueHandler(h), this._valueType, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isTrueMapType()
/*     */   {
/* 229 */     return Map.class.isAssignableFrom(this._class);
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
/* 241 */     return "[map-like type; class " + this._class.getName() + ", " + this._keyType + " -> " + this._valueType + "]";
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 247 */     if (o == this) return true;
/* 248 */     if (o == null) return false;
/* 249 */     if (o.getClass() != getClass()) { return false;
/*     */     }
/* 251 */     MapLikeType other = (MapLikeType)o;
/* 252 */     return (this._class == other._class) && (this._keyType.equals(other._keyType)) && (this._valueType.equals(other._valueType));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\MapLikeType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */