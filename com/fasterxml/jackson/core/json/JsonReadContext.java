/*     */ package com.fasterxml.jackson.core.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParseException;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.io.CharTypes;
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
/*     */ public final class JsonReadContext
/*     */   extends JsonStreamContext
/*     */ {
/*     */   protected final JsonReadContext _parent;
/*     */   protected DupDetector _dups;
/*  33 */   protected JsonReadContext _child = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String _currentName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _currentValue;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _lineNr;
/*     */   
/*     */ 
/*     */ 
/*     */   protected int _columnNr;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonReadContext(JsonReadContext parent, DupDetector dups, int type, int lineNr, int colNr)
/*     */   {
/*  59 */     this._parent = parent;
/*  60 */     this._dups = dups;
/*  61 */     this._type = type;
/*  62 */     this._lineNr = lineNr;
/*  63 */     this._columnNr = colNr;
/*  64 */     this._index = -1;
/*     */   }
/*     */   
/*     */   protected void reset(int type, int lineNr, int colNr) {
/*  68 */     this._type = type;
/*  69 */     this._index = -1;
/*  70 */     this._lineNr = lineNr;
/*  71 */     this._columnNr = colNr;
/*  72 */     this._currentName = null;
/*  73 */     this._currentValue = null;
/*  74 */     if (this._dups != null) {
/*  75 */       this._dups.reset();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonReadContext withDupDetector(DupDetector dups)
/*     */   {
/*  86 */     this._dups = dups;
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public Object getCurrentValue()
/*     */   {
/*  92 */     return this._currentValue;
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v)
/*     */   {
/*  97 */     this._currentValue = v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static JsonReadContext createRootContext(int lineNr, int colNr)
/*     */   {
/* 108 */     return createRootContext(lineNr, colNr, null);
/*     */   }
/*     */   
/*     */   public static JsonReadContext createRootContext(int lineNr, int colNr, DupDetector dups) {
/* 112 */     return new JsonReadContext(null, dups, 0, lineNr, colNr);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static JsonReadContext createRootContext() {
/* 117 */     return createRootContext(null);
/*     */   }
/*     */   
/*     */   public static JsonReadContext createRootContext(DupDetector dups) {
/* 121 */     return new JsonReadContext(null, dups, 0, 1, 0);
/*     */   }
/*     */   
/*     */   public JsonReadContext createChildArrayContext(int lineNr, int colNr) {
/* 125 */     JsonReadContext ctxt = this._child;
/* 126 */     if (ctxt == null) {
/* 127 */       this._child = (ctxt = new JsonReadContext(this, this._dups == null ? null : this._dups.child(), 1, lineNr, colNr));
/*     */     }
/*     */     else {
/* 130 */       ctxt.reset(1, lineNr, colNr);
/*     */     }
/* 132 */     return ctxt;
/*     */   }
/*     */   
/*     */   public JsonReadContext createChildObjectContext(int lineNr, int colNr) {
/* 136 */     JsonReadContext ctxt = this._child;
/* 137 */     if (ctxt == null) {
/* 138 */       this._child = (ctxt = new JsonReadContext(this, this._dups == null ? null : this._dups.child(), 2, lineNr, colNr));
/*     */       
/* 140 */       return ctxt;
/*     */     }
/* 142 */     ctxt.reset(2, lineNr, colNr);
/* 143 */     return ctxt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 152 */   public String getCurrentName() { return this._currentName; }
/* 153 */   public JsonReadContext getParent() { return this._parent; }
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
/*     */   public JsonLocation getStartLocation(Object srcRef)
/*     */   {
/* 167 */     long totalChars = -1L;
/* 168 */     return new JsonLocation(srcRef, totalChars, this._lineNr, this._columnNr);
/*     */   }
/*     */   
/*     */   public DupDetector getDupDetector() {
/* 172 */     return this._dups;
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
/*     */   public boolean expectComma()
/*     */   {
/* 186 */     int ix = ++this._index;
/* 187 */     return (this._type != 0) && (ix > 0);
/*     */   }
/*     */   
/*     */   public void setCurrentName(String name) throws JsonProcessingException {
/* 191 */     this._currentName = name;
/* 192 */     if (this._dups != null) _checkDup(this._dups, name);
/*     */   }
/*     */   
/*     */   private void _checkDup(DupDetector dd, String name) throws JsonProcessingException {
/* 196 */     if (dd.isDup(name)) {
/* 197 */       throw new JsonParseException("Duplicate field '" + name + "'", dd.findLocation());
/*     */     }
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
/*     */   public String toString()
/*     */   {
/* 213 */     StringBuilder sb = new StringBuilder(64);
/* 214 */     switch (this._type) {
/*     */     case 0: 
/* 216 */       sb.append("/");
/* 217 */       break;
/*     */     case 1: 
/* 219 */       sb.append('[');
/* 220 */       sb.append(getCurrentIndex());
/* 221 */       sb.append(']');
/* 222 */       break;
/*     */     case 2: 
/* 224 */       sb.append('{');
/* 225 */       if (this._currentName != null) {
/* 226 */         sb.append('"');
/* 227 */         CharTypes.appendQuoted(sb, this._currentName);
/* 228 */         sb.append('"');
/*     */       } else {
/* 230 */         sb.append('?');
/*     */       }
/* 232 */       sb.append('}');
/*     */     }
/*     */     
/* 235 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\json\JsonReadContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */