/*     */ package com.fasterxml.jackson.core.json;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
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
/*     */ public class JsonWriteContext
/*     */   extends JsonStreamContext
/*     */ {
/*     */   public static final int STATUS_OK_AS_IS = 0;
/*     */   public static final int STATUS_OK_AFTER_COMMA = 1;
/*     */   public static final int STATUS_OK_AFTER_COLON = 2;
/*     */   public static final int STATUS_OK_AFTER_SPACE = 3;
/*     */   public static final int STATUS_EXPECT_VALUE = 4;
/*     */   public static final int STATUS_EXPECT_NAME = 5;
/*     */   protected final JsonWriteContext _parent;
/*     */   protected DupDetector _dups;
/*  38 */   protected JsonWriteContext _child = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String _currentName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _currentValue;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _gotName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonWriteContext(int type, JsonWriteContext parent, DupDetector dups)
/*     */   {
/*  71 */     this._type = type;
/*  72 */     this._parent = parent;
/*  73 */     this._dups = dups;
/*  74 */     this._index = -1;
/*     */   }
/*     */   
/*     */   protected JsonWriteContext reset(int type) {
/*  78 */     this._type = type;
/*  79 */     this._index = -1;
/*  80 */     this._currentName = null;
/*  81 */     this._gotName = false;
/*  82 */     this._currentValue = null;
/*  83 */     if (this._dups != null) this._dups.reset();
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public JsonWriteContext withDupDetector(DupDetector dups) {
/*  88 */     this._dups = dups;
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public Object getCurrentValue()
/*     */   {
/*  94 */     return this._currentValue;
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v)
/*     */   {
/*  99 */     this._currentValue = v;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static JsonWriteContext createRootContext()
/*     */   {
/* 112 */     return createRootContext(null);
/*     */   }
/*     */   
/* 115 */   public static JsonWriteContext createRootContext(DupDetector dd) { return new JsonWriteContext(0, null, dd); }
/*     */   
/*     */   public JsonWriteContext createChildArrayContext()
/*     */   {
/* 119 */     JsonWriteContext ctxt = this._child;
/* 120 */     if (ctxt == null) {
/* 121 */       this._child = (ctxt = new JsonWriteContext(1, this, this._dups == null ? null : this._dups.child()));
/* 122 */       return ctxt;
/*     */     }
/* 124 */     return ctxt.reset(1);
/*     */   }
/*     */   
/*     */   public JsonWriteContext createChildObjectContext() {
/* 128 */     JsonWriteContext ctxt = this._child;
/* 129 */     if (ctxt == null) {
/* 130 */       this._child = (ctxt = new JsonWriteContext(2, this, this._dups == null ? null : this._dups.child()));
/* 131 */       return ctxt;
/*     */     }
/* 133 */     return ctxt.reset(2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 138 */   public final JsonWriteContext getParent() { return this._parent; }
/* 139 */   public final String getCurrentName() { return this._currentName; }
/*     */   
/*     */   public DupDetector getDupDetector() {
/* 142 */     return this._dups;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int writeFieldName(String name)
/*     */     throws JsonProcessingException
/*     */   {
/* 153 */     if (this._gotName) {
/* 154 */       return 4;
/*     */     }
/* 156 */     this._gotName = true;
/* 157 */     this._currentName = name;
/* 158 */     if (this._dups != null) _checkDup(this._dups, name);
/* 159 */     return this._index < 0 ? 0 : 1;
/*     */   }
/*     */   
/*     */   private final void _checkDup(DupDetector dd, String name) throws JsonProcessingException {
/* 163 */     if (dd.isDup(name)) throw new JsonGenerationException("Duplicate field '" + name + "'");
/*     */   }
/*     */   
/*     */   public int writeValue()
/*     */   {
/* 168 */     if (this._type == 2) {
/* 169 */       this._gotName = false;
/* 170 */       this._index += 1;
/* 171 */       return 2;
/*     */     }
/*     */     
/*     */ 
/* 175 */     if (this._type == 1) {
/* 176 */       int ix = this._index;
/* 177 */       this._index += 1;
/* 178 */       return ix < 0 ? 0 : 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 183 */     this._index += 1;
/* 184 */     return this._index == 0 ? 0 : 3;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void appendDesc(StringBuilder sb)
/*     */   {
/* 190 */     if (this._type == 2) {
/* 191 */       sb.append('{');
/* 192 */       if (this._currentName != null) {
/* 193 */         sb.append('"');
/*     */         
/* 195 */         sb.append(this._currentName);
/* 196 */         sb.append('"');
/*     */       } else {
/* 198 */         sb.append('?');
/*     */       }
/* 200 */       sb.append('}');
/* 201 */     } else if (this._type == 1) {
/* 202 */       sb.append('[');
/* 203 */       sb.append(getCurrentIndex());
/* 204 */       sb.append(']');
/*     */     }
/*     */     else {
/* 207 */       sb.append("/");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 218 */     StringBuilder sb = new StringBuilder(64);
/* 219 */     appendDesc(sb);
/* 220 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\json\JsonWriteContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */