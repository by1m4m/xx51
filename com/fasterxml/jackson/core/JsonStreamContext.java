/*     */ package com.fasterxml.jackson.core;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JsonStreamContext
/*     */ {
/*     */   protected static final int TYPE_ROOT = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final int TYPE_ARRAY = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final int TYPE_OBJECT = 2;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _type;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _index;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JsonStreamContext getParent();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean inArray()
/*     */   {
/*  61 */     return this._type == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final boolean inRoot()
/*     */   {
/*  68 */     return this._type == 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public final boolean inObject()
/*     */   {
/*  74 */     return this._type == 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getTypeDesc()
/*     */   {
/*  82 */     switch (this._type) {
/*  83 */     case 0:  return "ROOT";
/*  84 */     case 1:  return "ARRAY";
/*  85 */     case 2:  return "OBJECT";
/*     */     }
/*  87 */     return "?";
/*     */   }
/*     */   
/*     */ 
/*     */   public final int getEntryCount()
/*     */   {
/*  93 */     return this._index + 1;
/*     */   }
/*     */   
/*     */   public final int getCurrentIndex()
/*     */   {
/*  98 */     return this._index < 0 ? 0 : this._index;
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
/*     */   public abstract String getCurrentName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getCurrentValue()
/*     */   {
/* 120 */     return null;
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v) {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\JsonStreamContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */