/*     */ package com.fasterxml.jackson.core;
/*     */ 
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
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
/*     */ public class JsonPointer
/*     */ {
/*  27 */   protected static final JsonPointer EMPTY = new JsonPointer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonPointer _nextSegment;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected volatile JsonPointer _head;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _asString;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _matchingPropertyName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int _matchingElementIndex;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonPointer()
/*     */   {
/*  71 */     this._nextSegment = null;
/*  72 */     this._matchingPropertyName = "";
/*  73 */     this._matchingElementIndex = -1;
/*  74 */     this._asString = "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonPointer(String fullString, String segment, JsonPointer next)
/*     */   {
/*  81 */     this._asString = fullString;
/*  82 */     this._nextSegment = next;
/*     */     
/*  84 */     this._matchingPropertyName = segment;
/*     */     
/*  86 */     this._matchingElementIndex = _parseIndex(segment);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonPointer(String fullString, String segment, int matchIndex, JsonPointer next)
/*     */   {
/*  93 */     this._asString = fullString;
/*  94 */     this._nextSegment = next;
/*  95 */     this._matchingPropertyName = segment;
/*  96 */     this._matchingElementIndex = matchIndex;
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
/*     */ 
/*     */   public static JsonPointer compile(String input)
/*     */     throws IllegalArgumentException
/*     */   {
/* 117 */     if ((input == null) || (input.length() == 0)) {
/* 118 */       return EMPTY;
/*     */     }
/*     */     
/* 121 */     if (input.charAt(0) != '/') {
/* 122 */       throw new IllegalArgumentException("Invalid input: JSON Pointer expression must start with '/': \"" + input + "\"");
/*     */     }
/* 124 */     return _parseTail(input);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static JsonPointer valueOf(String input)
/*     */   {
/* 131 */     return compile(input);
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
/* 161 */   public boolean matches() { return this._nextSegment == null; }
/* 162 */   public String getMatchingProperty() { return this._matchingPropertyName; }
/* 163 */   public int getMatchingIndex() { return this._matchingElementIndex; }
/* 164 */   public boolean mayMatchProperty() { return this._matchingPropertyName != null; }
/* 165 */   public boolean mayMatchElement() { return this._matchingElementIndex >= 0; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonPointer last()
/*     */   {
/* 174 */     JsonPointer current = this;
/* 175 */     if (current == EMPTY) {
/* 176 */       return null;
/*     */     }
/*     */     JsonPointer next;
/* 179 */     while ((next = current._nextSegment) != EMPTY) {
/* 180 */       current = next;
/*     */     }
/* 182 */     return current;
/*     */   }
/*     */   
/*     */   public JsonPointer append(JsonPointer tail) {
/* 186 */     if (this == EMPTY) {
/* 187 */       return tail;
/*     */     }
/* 189 */     if (tail == EMPTY) {
/* 190 */       return this;
/*     */     }
/* 192 */     String currentJsonPointer = this._asString;
/* 193 */     if (currentJsonPointer.endsWith("/"))
/*     */     {
/* 195 */       currentJsonPointer = currentJsonPointer.substring(0, currentJsonPointer.length() - 1);
/*     */     }
/* 197 */     return compile(currentJsonPointer + tail._asString);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean matchesProperty(String name)
/*     */   {
/* 207 */     return (this._nextSegment != null) && (this._matchingPropertyName.equals(name));
/*     */   }
/*     */   
/*     */   public JsonPointer matchProperty(String name) {
/* 211 */     if ((this._nextSegment == null) || (!this._matchingPropertyName.equals(name))) {
/* 212 */       return null;
/*     */     }
/* 214 */     return this._nextSegment;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean matchesElement(int index)
/*     */   {
/* 224 */     return (index == this._matchingElementIndex) && (index >= 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonPointer tail()
/*     */   {
/* 233 */     return this._nextSegment;
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
/*     */   public JsonPointer head()
/*     */   {
/* 247 */     JsonPointer h = this._head;
/* 248 */     if (h == null) {
/* 249 */       if (this != EMPTY) {
/* 250 */         h = _constructHead();
/*     */       }
/* 252 */       this._head = h;
/*     */     }
/* 254 */     return h;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 263 */   public String toString() { return this._asString; }
/* 264 */   public int hashCode() { return this._asString.hashCode(); }
/*     */   
/*     */   public boolean equals(Object o) {
/* 267 */     if (o == this) return true;
/* 268 */     if (o == null) return false;
/* 269 */     if (!(o instanceof JsonPointer)) return false;
/* 270 */     return this._asString.equals(((JsonPointer)o)._asString);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int _parseIndex(String str)
/*     */   {
/* 280 */     int len = str.length();
/*     */     
/*     */ 
/* 283 */     if ((len == 0) || (len > 10)) {
/* 284 */       return -1;
/*     */     }
/*     */     
/* 287 */     char c = str.charAt(0);
/* 288 */     if (c <= '0') {
/* 289 */       return (len == 1) && (c == '0') ? 0 : -1;
/*     */     }
/* 291 */     if (c > '9') {
/* 292 */       return -1;
/*     */     }
/* 294 */     for (int i = 1; i < len; i++) {
/* 295 */       c = str.charAt(i);
/* 296 */       if ((c > '9') || (c < '0')) {
/* 297 */         return -1;
/*     */       }
/*     */     }
/* 300 */     if (len == 10) {
/* 301 */       long l = NumberInput.parseLong(str);
/* 302 */       if (l > 2147483647L) {
/* 303 */         return -1;
/*     */       }
/*     */     }
/* 306 */     return NumberInput.parseInt(str);
/*     */   }
/*     */   
/*     */   protected static JsonPointer _parseTail(String input) {
/* 310 */     int end = input.length();
/*     */     
/*     */ 
/* 313 */     for (int i = 1; i < end;) {
/* 314 */       char c = input.charAt(i);
/* 315 */       if (c == '/') {
/* 316 */         return new JsonPointer(input, input.substring(1, i), _parseTail(input.substring(i)));
/*     */       }
/*     */       
/* 319 */       i++;
/*     */       
/* 321 */       if ((c == '~') && (i < end)) {
/* 322 */         return _parseQuotedTail(input, i);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 327 */     return new JsonPointer(input, input.substring(1), EMPTY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static JsonPointer _parseQuotedTail(String input, int i)
/*     */   {
/* 338 */     int end = input.length();
/* 339 */     StringBuilder sb = new StringBuilder(Math.max(16, end));
/* 340 */     if (i > 2) {
/* 341 */       sb.append(input, 1, i - 1);
/*     */     }
/* 343 */     _appendEscape(sb, input.charAt(i++));
/* 344 */     while (i < end) {
/* 345 */       char c = input.charAt(i);
/* 346 */       if (c == '/') {
/* 347 */         return new JsonPointer(input, sb.toString(), _parseTail(input.substring(i)));
/*     */       }
/*     */       
/* 350 */       i++;
/* 351 */       if ((c == '~') && (i < end)) {
/* 352 */         _appendEscape(sb, input.charAt(i++));
/*     */       }
/*     */       else {
/* 355 */         sb.append(c);
/*     */       }
/*     */     }
/* 358 */     return new JsonPointer(input, sb.toString(), EMPTY);
/*     */   }
/*     */   
/*     */ 
/*     */   protected JsonPointer _constructHead()
/*     */   {
/* 364 */     JsonPointer last = last();
/* 365 */     if (last == this) {
/* 366 */       return EMPTY;
/*     */     }
/*     */     
/* 369 */     int suffixLength = last._asString.length();
/* 370 */     JsonPointer next = this._nextSegment;
/* 371 */     return new JsonPointer(this._asString.substring(0, this._asString.length() - suffixLength), this._matchingPropertyName, this._matchingElementIndex, next._constructHead(suffixLength, last));
/*     */   }
/*     */   
/*     */ 
/*     */   protected JsonPointer _constructHead(int suffixLength, JsonPointer last)
/*     */   {
/* 377 */     if (this == last) {
/* 378 */       return EMPTY;
/*     */     }
/* 380 */     JsonPointer next = this._nextSegment;
/* 381 */     String str = this._asString;
/* 382 */     return new JsonPointer(str.substring(0, str.length() - suffixLength), this._matchingPropertyName, this._matchingElementIndex, next._constructHead(suffixLength, last));
/*     */   }
/*     */   
/*     */   private static void _appendEscape(StringBuilder sb, char c)
/*     */   {
/* 387 */     if (c == '0') {
/* 388 */       c = '~';
/* 389 */     } else if (c == '1') {
/* 390 */       c = '/';
/*     */     } else {
/* 392 */       sb.append('~');
/*     */     }
/* 394 */     sb.append(c);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\JsonPointer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */