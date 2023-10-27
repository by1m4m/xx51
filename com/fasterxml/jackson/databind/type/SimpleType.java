/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SimpleType
/*     */   extends TypeBase
/*     */ {
/*     */   private static final long serialVersionUID = -800374828948534376L;
/*     */   protected final Class<?> _typeParametersFor;
/*     */   protected final JavaType[] _typeParameters;
/*     */   protected final String[] _typeNames;
/*     */   
/*     */   protected SimpleType(Class<?> cls)
/*     */   {
/*  44 */     this(cls, null, null, null, null, false, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected SimpleType(Class<?> cls, String[] typeNames, JavaType[] typeParams, Object valueHandler, Object typeHandler, boolean asStatic)
/*     */   {
/*  54 */     this(cls, typeNames, typeParams, valueHandler, typeHandler, asStatic, null);
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
/*     */   protected SimpleType(Class<?> cls, String[] typeNames, JavaType[] typeParams, Object valueHandler, Object typeHandler, boolean asStatic, Class<?> parametersFrom)
/*     */   {
/*  68 */     super(cls, 0, valueHandler, typeHandler, asStatic);
/*  69 */     if ((typeNames == null) || (typeNames.length == 0)) {
/*  70 */       this._typeNames = null;
/*  71 */       this._typeParameters = null;
/*     */     } else {
/*  73 */       this._typeNames = typeNames;
/*  74 */       this._typeParameters = typeParams;
/*     */     }
/*  76 */     this._typeParametersFor = parametersFrom;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SimpleType constructUnsafe(Class<?> raw)
/*     */   {
/*  86 */     return new SimpleType(raw, null, null, null, null, false, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JavaType _narrow(Class<?> subclass)
/*     */   {
/*  93 */     return new SimpleType(subclass, this._typeNames, this._typeParameters, this._valueHandler, this._typeHandler, this._asStatic, this._typeParametersFor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType narrowContentsBy(Class<?> subclass)
/*     */   {
/* 101 */     throw new IllegalArgumentException("Internal error: SimpleType.narrowContentsBy() should never be called");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JavaType widenContentsBy(Class<?> subclass)
/*     */   {
/* 108 */     throw new IllegalArgumentException("Internal error: SimpleType.widenContentsBy() should never be called");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SimpleType construct(Class<?> cls)
/*     */   {
/* 116 */     if (Map.class.isAssignableFrom(cls)) {
/* 117 */       throw new IllegalArgumentException("Can not construct SimpleType for a Map (class: " + cls.getName() + ")");
/*     */     }
/* 119 */     if (Collection.class.isAssignableFrom(cls)) {
/* 120 */       throw new IllegalArgumentException("Can not construct SimpleType for a Collection (class: " + cls.getName() + ")");
/*     */     }
/*     */     
/* 123 */     if (cls.isArray()) {
/* 124 */       throw new IllegalArgumentException("Can not construct SimpleType for an array (class: " + cls.getName() + ")");
/*     */     }
/* 126 */     return new SimpleType(cls);
/*     */   }
/*     */   
/*     */ 
/*     */   public SimpleType withTypeHandler(Object h)
/*     */   {
/* 132 */     return new SimpleType(this._class, this._typeNames, this._typeParameters, this._valueHandler, h, this._asStatic, this._typeParametersFor);
/*     */   }
/*     */   
/*     */ 
/*     */   public JavaType withContentTypeHandler(Object h)
/*     */   {
/* 138 */     throw new IllegalArgumentException("Simple types have no content types; can not call withContenTypeHandler()");
/*     */   }
/*     */   
/*     */   public SimpleType withValueHandler(Object h)
/*     */   {
/* 143 */     if (h == this._valueHandler) {
/* 144 */       return this;
/*     */     }
/* 146 */     return new SimpleType(this._class, this._typeNames, this._typeParameters, h, this._typeHandler, this._asStatic, this._typeParametersFor);
/*     */   }
/*     */   
/*     */ 
/*     */   public SimpleType withContentValueHandler(Object h)
/*     */   {
/* 152 */     throw new IllegalArgumentException("Simple types have no content types; can not call withContenValueHandler()");
/*     */   }
/*     */   
/*     */   public SimpleType withStaticTyping()
/*     */   {
/* 157 */     return this._asStatic ? this : new SimpleType(this._class, this._typeNames, this._typeParameters, this._valueHandler, this._typeHandler, this._asStatic, this._typeParametersFor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String buildCanonicalName()
/*     */   {
/* 164 */     StringBuilder sb = new StringBuilder();
/* 165 */     sb.append(this._class.getName());
/* 166 */     if ((this._typeParameters != null) && (this._typeParameters.length > 0)) {
/* 167 */       sb.append('<');
/* 168 */       boolean first = true;
/* 169 */       for (JavaType t : this._typeParameters) {
/* 170 */         if (first) {
/* 171 */           first = false;
/*     */         } else {
/* 173 */           sb.append(',');
/*     */         }
/* 175 */         sb.append(t.toCanonical());
/*     */       }
/* 177 */       sb.append('>');
/*     */     }
/* 179 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isContainerType()
/*     */   {
/* 189 */     return false;
/*     */   }
/*     */   
/*     */   public int containedTypeCount() {
/* 193 */     return this._typeParameters == null ? 0 : this._typeParameters.length;
/*     */   }
/*     */   
/*     */ 
/*     */   public JavaType containedType(int index)
/*     */   {
/* 199 */     if ((index < 0) || (this._typeParameters == null) || (index >= this._typeParameters.length)) {
/* 200 */       return null;
/*     */     }
/* 202 */     return this._typeParameters[index];
/*     */   }
/*     */   
/*     */ 
/*     */   public String containedTypeName(int index)
/*     */   {
/* 208 */     if ((index < 0) || (this._typeNames == null) || (index >= this._typeNames.length)) {
/* 209 */       return null;
/*     */     }
/* 211 */     return this._typeNames[index];
/*     */   }
/*     */   
/*     */   public Class<?> getParameterSource()
/*     */   {
/* 216 */     return this._typeParametersFor;
/*     */   }
/*     */   
/*     */   public StringBuilder getErasedSignature(StringBuilder sb)
/*     */   {
/* 221 */     return _classSignature(this._class, sb, true);
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuilder getGenericSignature(StringBuilder sb)
/*     */   {
/* 227 */     _classSignature(this._class, sb, false);
/* 228 */     if (this._typeParameters != null) {
/* 229 */       sb.append('<');
/* 230 */       for (JavaType param : this._typeParameters) {
/* 231 */         sb = param.getGenericSignature(sb);
/*     */       }
/* 233 */       sb.append('>');
/*     */     }
/* 235 */     sb.append(';');
/* 236 */     return sb;
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
/* 248 */     StringBuilder sb = new StringBuilder(40);
/* 249 */     sb.append("[simple type, class ").append(buildCanonicalName()).append(']');
/* 250 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 256 */     if (o == this) return true;
/* 257 */     if (o == null) return false;
/* 258 */     if (o.getClass() != getClass()) { return false;
/*     */     }
/* 260 */     SimpleType other = (SimpleType)o;
/*     */     
/*     */ 
/* 263 */     if (other._class != this._class) { return false;
/*     */     }
/*     */     
/* 266 */     JavaType[] p1 = this._typeParameters;
/* 267 */     JavaType[] p2 = other._typeParameters;
/* 268 */     if (p1 == null) {
/* 269 */       return (p2 == null) || (p2.length == 0);
/*     */     }
/* 271 */     if (p2 == null) { return false;
/*     */     }
/* 273 */     if (p1.length != p2.length) return false;
/* 274 */     int i = 0; for (int len = p1.length; i < len; i++) {
/* 275 */       if (!p1[i].equals(p2[i])) {
/* 276 */         return false;
/*     */       }
/*     */     }
/* 279 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\SimpleType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */