/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CollectionLikeType
/*     */   extends TypeBase
/*     */ {
/*     */   private static final long serialVersionUID = 4611641304150899138L;
/*     */   protected final JavaType _elementType;
/*     */   
/*     */   protected CollectionLikeType(Class<?> collT, JavaType elemT, Object valueHandler, Object typeHandler, boolean asStatic)
/*     */   {
/*  32 */     super(collT, elemT.hashCode(), valueHandler, typeHandler, asStatic);
/*  33 */     this._elementType = elemT;
/*     */   }
/*     */   
/*     */   protected JavaType _narrow(Class<?> subclass)
/*     */   {
/*  38 */     return new CollectionLikeType(subclass, this._elementType, this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType narrowContentsBy(Class<?> contentClass)
/*     */   {
/*  46 */     if (contentClass == this._elementType.getRawClass()) {
/*  47 */       return this;
/*     */     }
/*  49 */     return new CollectionLikeType(this._class, this._elementType.narrowBy(contentClass), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType widenContentsBy(Class<?> contentClass)
/*     */   {
/*  57 */     if (contentClass == this._elementType.getRawClass()) {
/*  58 */       return this;
/*     */     }
/*  60 */     return new CollectionLikeType(this._class, this._elementType.widenBy(contentClass), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static CollectionLikeType construct(Class<?> rawType, JavaType elemT)
/*     */   {
/*  67 */     return new CollectionLikeType(rawType, elemT, null, null, false);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionLikeType withTypeHandler(Object h)
/*     */   {
/*  73 */     return new CollectionLikeType(this._class, this._elementType, this._valueHandler, h, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionLikeType withContentTypeHandler(Object h)
/*     */   {
/*  79 */     return new CollectionLikeType(this._class, this._elementType.withTypeHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionLikeType withValueHandler(Object h)
/*     */   {
/*  85 */     return new CollectionLikeType(this._class, this._elementType, h, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */   public CollectionLikeType withContentValueHandler(Object h)
/*     */   {
/*  90 */     return new CollectionLikeType(this._class, this._elementType.withValueHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionLikeType withStaticTyping()
/*     */   {
/*  96 */     if (this._asStatic) {
/*  97 */       return this;
/*     */     }
/*  99 */     return new CollectionLikeType(this._class, this._elementType.withStaticTyping(), this._valueHandler, this._typeHandler, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isContainerType()
/*     */   {
/* 110 */     return true;
/*     */   }
/*     */   
/* 113 */   public boolean isCollectionLikeType() { return true; }
/*     */   
/*     */   public JavaType getContentType() {
/* 116 */     return this._elementType;
/*     */   }
/*     */   
/* 119 */   public int containedTypeCount() { return 1; }
/*     */   
/*     */   public JavaType containedType(int index)
/*     */   {
/* 123 */     return index == 0 ? this._elementType : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String containedTypeName(int index)
/*     */   {
/* 132 */     if (index == 0) return "E";
/* 133 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Class<?> getParameterSource()
/*     */   {
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   public StringBuilder getErasedSignature(StringBuilder sb)
/*     */   {
/* 145 */     return _classSignature(this._class, sb, true);
/*     */   }
/*     */   
/*     */   public StringBuilder getGenericSignature(StringBuilder sb)
/*     */   {
/* 150 */     _classSignature(this._class, sb, false);
/* 151 */     sb.append('<');
/* 152 */     this._elementType.getGenericSignature(sb);
/* 153 */     sb.append(">;");
/* 154 */     return sb;
/*     */   }
/*     */   
/*     */   protected String buildCanonicalName()
/*     */   {
/* 159 */     StringBuilder sb = new StringBuilder();
/* 160 */     sb.append(this._class.getName());
/* 161 */     if (this._elementType != null) {
/* 162 */       sb.append('<');
/* 163 */       sb.append(this._elementType.toCanonical());
/* 164 */       sb.append('>');
/*     */     }
/* 166 */     return sb.toString();
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
/*     */   public boolean isTrueCollectionType()
/*     */   {
/* 182 */     return Collection.class.isAssignableFrom(this._class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 194 */     if (o == this) return true;
/* 195 */     if (o == null) return false;
/* 196 */     if (o.getClass() != getClass()) { return false;
/*     */     }
/* 198 */     CollectionLikeType other = (CollectionLikeType)o;
/* 199 */     return (this._class == other._class) && (this._elementType.equals(other._elementType));
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 205 */     return "[collection-like type; class " + this._class.getName() + ", contains " + this._elementType + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\CollectionLikeType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */