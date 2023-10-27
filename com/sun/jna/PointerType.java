/*     */ package com.sun.jna;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PointerType
/*     */   implements NativeMapped
/*     */ {
/*     */   private Pointer pointer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PointerType()
/*     */   {
/*  25 */     this.pointer = Pointer.NULL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected PointerType(Pointer p)
/*     */   {
/*  32 */     this.pointer = p;
/*     */   }
/*     */   
/*     */ 
/*     */   public Class nativeType()
/*     */   {
/*  38 */     return Pointer.class;
/*     */   }
/*     */   
/*     */   public Object toNative()
/*     */   {
/*  43 */     return getPointer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Pointer getPointer()
/*     */   {
/*  50 */     return this.pointer;
/*     */   }
/*     */   
/*     */   public void setPointer(Pointer p) {
/*  54 */     this.pointer = p;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object fromNative(Object nativeValue, FromNativeContext context)
/*     */   {
/*  65 */     if (nativeValue == null) {
/*  66 */       return null;
/*     */     }
/*     */     try {
/*  69 */       PointerType pt = (PointerType)getClass().newInstance();
/*  70 */       pt.pointer = ((Pointer)nativeValue);
/*  71 */       return pt;
/*     */     }
/*     */     catch (InstantiationException e) {
/*  74 */       throw new IllegalArgumentException("Can't instantiate " + getClass());
/*     */     }
/*     */     catch (IllegalAccessException e) {
/*  77 */       throw new IllegalArgumentException("Not allowed to instantiate " + getClass());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  85 */     return this.pointer != null ? this.pointer.hashCode() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  92 */     if (o == this) return true;
/*  93 */     if ((o instanceof PointerType)) {
/*  94 */       Pointer p = ((PointerType)o).getPointer();
/*  95 */       if (this.pointer == null)
/*  96 */         return p == null;
/*  97 */       return this.pointer.equals(p);
/*     */     }
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 103 */     return this.pointer.toString() + " (" + super.toString() + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\PointerType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */