/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializable;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import java.io.IOException;
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
/*     */ public abstract class TypeBase
/*     */   extends JavaType
/*     */   implements JsonSerializable
/*     */ {
/*     */   private static final long serialVersionUID = -3581199092426900829L;
/*     */   volatile transient String _canonicalName;
/*     */   
/*     */   @Deprecated
/*     */   protected TypeBase(Class<?> raw, int hash, Object valueHandler, Object typeHandler)
/*     */   {
/*  30 */     this(raw, hash, valueHandler, typeHandler, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TypeBase(Class<?> raw, int hash, Object valueHandler, Object typeHandler, boolean asStatic)
/*     */   {
/*  39 */     super(raw, hash, valueHandler, typeHandler, asStatic);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toCanonical()
/*     */   {
/*  45 */     String str = this._canonicalName;
/*  46 */     if (str == null) {
/*  47 */       str = buildCanonicalName();
/*     */     }
/*  49 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract String buildCanonicalName();
/*     */   
/*     */ 
/*     */   public abstract StringBuilder getGenericSignature(StringBuilder paramStringBuilder);
/*     */   
/*     */   public abstract StringBuilder getErasedSignature(StringBuilder paramStringBuilder);
/*     */   
/*     */   public <T> T getValueHandler()
/*     */   {
/*  62 */     return (T)this._valueHandler;
/*     */   }
/*     */   
/*     */   public <T> T getTypeHandler() {
/*  66 */     return (T)this._typeHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serializeWithType(JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  79 */     typeSer.writeTypePrefixForScalar(this, jgen);
/*  80 */     serialize(jgen, provider);
/*  81 */     typeSer.writeTypeSuffixForScalar(this, jgen);
/*     */   }
/*     */   
/*     */ 
/*     */   public void serialize(JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  88 */     jgen.writeString(toCanonical());
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
/*     */   protected static StringBuilder _classSignature(Class<?> cls, StringBuilder sb, boolean trailingSemicolon)
/*     */   {
/* 104 */     if (cls.isPrimitive()) {
/* 105 */       if (cls == Boolean.TYPE) {
/* 106 */         sb.append('Z');
/* 107 */       } else if (cls == Byte.TYPE) {
/* 108 */         sb.append('B');
/*     */       }
/* 110 */       else if (cls == Short.TYPE) {
/* 111 */         sb.append('S');
/*     */       }
/* 113 */       else if (cls == Character.TYPE) {
/* 114 */         sb.append('C');
/*     */       }
/* 116 */       else if (cls == Integer.TYPE) {
/* 117 */         sb.append('I');
/*     */       }
/* 119 */       else if (cls == Long.TYPE) {
/* 120 */         sb.append('J');
/*     */       }
/* 122 */       else if (cls == Float.TYPE) {
/* 123 */         sb.append('F');
/*     */       }
/* 125 */       else if (cls == Double.TYPE) {
/* 126 */         sb.append('D');
/*     */       }
/* 128 */       else if (cls == Void.TYPE) {
/* 129 */         sb.append('V');
/*     */       } else {
/* 131 */         throw new IllegalStateException("Unrecognized primitive type: " + cls.getName());
/*     */       }
/*     */     } else {
/* 134 */       sb.append('L');
/* 135 */       String name = cls.getName();
/* 136 */       int i = 0; for (int len = name.length(); i < len; i++) {
/* 137 */         char c = name.charAt(i);
/* 138 */         if (c == '.') c = '/';
/* 139 */         sb.append(c);
/*     */       }
/* 141 */       if (trailingSemicolon) {
/* 142 */         sb.append(';');
/*     */       }
/*     */     }
/* 145 */     return sb;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\TypeBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */