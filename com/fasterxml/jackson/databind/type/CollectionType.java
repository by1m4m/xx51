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
/*     */ public final class CollectionType
/*     */   extends CollectionLikeType
/*     */ {
/*     */   private static final long serialVersionUID = -7834910259750909424L;
/*     */   
/*     */   private CollectionType(Class<?> collT, JavaType elemT, Object valueHandler, Object typeHandler, boolean asStatic)
/*     */   {
/*  22 */     super(collT, elemT, valueHandler, typeHandler, asStatic);
/*     */   }
/*     */   
/*     */   protected JavaType _narrow(Class<?> subclass)
/*     */   {
/*  27 */     return new CollectionType(subclass, this._elementType, null, null, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JavaType narrowContentsBy(Class<?> contentClass)
/*     */   {
/*  34 */     if (contentClass == this._elementType.getRawClass()) {
/*  35 */       return this;
/*     */     }
/*  37 */     return new CollectionType(this._class, this._elementType.narrowBy(contentClass), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType widenContentsBy(Class<?> contentClass)
/*     */   {
/*  45 */     if (contentClass == this._elementType.getRawClass()) {
/*  46 */       return this;
/*     */     }
/*  48 */     return new CollectionType(this._class, this._elementType.widenBy(contentClass), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static CollectionType construct(Class<?> rawType, JavaType elemT)
/*     */   {
/*  55 */     return new CollectionType(rawType, elemT, null, null, false);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionType withTypeHandler(Object h)
/*     */   {
/*  61 */     return new CollectionType(this._class, this._elementType, this._valueHandler, h, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CollectionType withContentTypeHandler(Object h)
/*     */   {
/*  68 */     return new CollectionType(this._class, this._elementType.withTypeHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionType withValueHandler(Object h)
/*     */   {
/*  74 */     return new CollectionType(this._class, this._elementType, h, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */   public CollectionType withContentValueHandler(Object h)
/*     */   {
/*  79 */     return new CollectionType(this._class, this._elementType.withValueHandler(h), this._valueHandler, this._typeHandler, this._asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionType withStaticTyping()
/*     */   {
/*  85 */     if (this._asStatic) {
/*  86 */       return this;
/*     */     }
/*  88 */     return new CollectionType(this._class, this._elementType.withStaticTyping(), this._valueHandler, this._typeHandler, true);
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
/* 100 */     return Collection.class;
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
/* 112 */     return "[collection type; class " + this._class.getName() + ", contains " + this._elementType + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\CollectionType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */