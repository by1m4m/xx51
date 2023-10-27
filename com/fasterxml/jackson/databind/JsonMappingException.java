/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ public class JsonMappingException
/*     */   extends JsonProcessingException
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   static final int MAX_REFS_TO_LIST = 1000;
/*     */   protected LinkedList<Reference> _path;
/*     */   
/*     */   public static class Reference
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     protected Object _from;
/*     */     protected String _fieldName;
/*  65 */     protected int _index = -1;
/*     */     
/*     */ 
/*     */ 
/*     */     protected Reference() {}
/*     */     
/*     */ 
/*  72 */     public Reference(Object from) { this._from = from; }
/*     */     
/*     */     public Reference(Object from, String fieldName) {
/*  75 */       this._from = from;
/*  76 */       if (fieldName == null) {
/*  77 */         throw new NullPointerException("Can not pass null fieldName");
/*     */       }
/*  79 */       this._fieldName = fieldName;
/*     */     }
/*     */     
/*     */     public Reference(Object from, int index) {
/*  83 */       this._from = from;
/*  84 */       this._index = index;
/*     */     }
/*     */     
/*  87 */     public void setFrom(Object o) { this._from = o; }
/*  88 */     public void setFieldName(String n) { this._fieldName = n; }
/*  89 */     public void setIndex(int ix) { this._index = ix; }
/*     */     
/*  91 */     public Object getFrom() { return this._from; }
/*  92 */     public String getFieldName() { return this._fieldName; }
/*  93 */     public int getIndex() { return this._index; }
/*     */     
/*     */     public String toString() {
/*  96 */       StringBuilder sb = new StringBuilder();
/*  97 */       Class<?> cls = (this._from instanceof Class) ? (Class)this._from : this._from.getClass();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */       Package pkg = cls.getPackage();
/* 104 */       if (pkg != null) {
/* 105 */         sb.append(pkg.getName());
/* 106 */         sb.append('.');
/*     */       }
/* 108 */       sb.append(cls.getSimpleName());
/* 109 */       sb.append('[');
/* 110 */       if (this._fieldName != null) {
/* 111 */         sb.append('"');
/* 112 */         sb.append(this._fieldName);
/* 113 */         sb.append('"');
/* 114 */       } else if (this._index >= 0) {
/* 115 */         sb.append(this._index);
/*     */       } else {
/* 117 */         sb.append('?');
/*     */       }
/* 119 */       sb.append(']');
/* 120 */       return sb.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */   public JsonMappingException(String msg) { super(msg); }
/* 143 */   public JsonMappingException(String msg, Throwable rootCause) { super(msg, rootCause); }
/* 144 */   public JsonMappingException(String msg, JsonLocation loc) { super(msg, loc); }
/* 145 */   public JsonMappingException(String msg, JsonLocation loc, Throwable rootCause) { super(msg, loc, rootCause); }
/*     */   
/*     */   public static JsonMappingException from(JsonParser jp, String msg) {
/* 148 */     return new JsonMappingException(msg, jp == null ? null : jp.getTokenLocation());
/*     */   }
/*     */   
/*     */   public static JsonMappingException from(JsonParser jp, String msg, Throwable problem) {
/* 152 */     return new JsonMappingException(msg, jp == null ? null : jp.getTokenLocation(), problem);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonMappingException fromUnexpectedIOE(IOException src)
/*     */   {
/* 163 */     return new JsonMappingException("Unexpected IOException (of type " + src.getClass().getName() + "): " + src.getMessage(), (JsonLocation)null, src);
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
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Object refFrom, String refFieldName)
/*     */   {
/* 177 */     return wrapWithPath(src, new Reference(refFrom, refFieldName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Object refFrom, int index)
/*     */   {
/* 189 */     return wrapWithPath(src, new Reference(refFrom, index));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static JsonMappingException wrapWithPath(Throwable src, Reference ref)
/*     */   {
/*     */     JsonMappingException jme;
/*     */     
/*     */     JsonMappingException jme;
/*     */     
/* 200 */     if ((src instanceof JsonMappingException)) {
/* 201 */       jme = (JsonMappingException)src;
/*     */     } else {
/* 203 */       String msg = src.getMessage();
/*     */       
/*     */ 
/*     */ 
/* 207 */       if ((msg == null) || (msg.length() == 0)) {
/* 208 */         msg = "(was " + src.getClass().getName() + ")";
/*     */       }
/* 210 */       jme = new JsonMappingException(msg, null, src);
/*     */     }
/* 212 */     jme.prependPath(ref);
/* 213 */     return jme;
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
/*     */   public List<Reference> getPath()
/*     */   {
/* 228 */     if (this._path == null) {
/* 229 */       return Collections.emptyList();
/*     */     }
/* 231 */     return Collections.unmodifiableList(this._path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPathReference()
/*     */   {
/* 240 */     return getPathReference(new StringBuilder()).toString();
/*     */   }
/*     */   
/*     */   public StringBuilder getPathReference(StringBuilder sb)
/*     */   {
/* 245 */     _appendPathDesc(sb);
/* 246 */     return sb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void prependPath(Object referrer, String fieldName)
/*     */   {
/* 255 */     Reference ref = new Reference(referrer, fieldName);
/* 256 */     prependPath(ref);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void prependPath(Object referrer, int index)
/*     */   {
/* 264 */     Reference ref = new Reference(referrer, index);
/* 265 */     prependPath(ref);
/*     */   }
/*     */   
/*     */   public void prependPath(Reference r)
/*     */   {
/* 270 */     if (this._path == null) {
/* 271 */       this._path = new LinkedList();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 277 */     if (this._path.size() < 1000) {
/* 278 */       this._path.addFirst(r);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalizedMessage()
/*     */   {
/* 290 */     return _buildMessage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 299 */     return _buildMessage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String _buildMessage()
/*     */   {
/* 307 */     String msg = super.getMessage();
/* 308 */     if (this._path == null) {
/* 309 */       return msg;
/*     */     }
/* 311 */     StringBuilder sb = msg == null ? new StringBuilder() : new StringBuilder(msg);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 317 */     sb.append(" (through reference chain: ");
/* 318 */     sb = getPathReference(sb);
/* 319 */     sb.append(')');
/* 320 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 326 */     return getClass().getName() + ": " + getMessage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _appendPathDesc(StringBuilder sb)
/*     */   {
/* 337 */     if (this._path == null) {
/* 338 */       return;
/*     */     }
/* 340 */     Iterator<Reference> it = this._path.iterator();
/* 341 */     while (it.hasNext()) {
/* 342 */       sb.append(((Reference)it.next()).toString());
/* 343 */       if (it.hasNext()) {
/* 344 */         sb.append("->");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\JsonMappingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */