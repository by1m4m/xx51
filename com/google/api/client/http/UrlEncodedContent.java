/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.Data;
/*     */ import com.google.api.client.util.FieldInfo;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.Types;
/*     */ import com.google.api.client.util.escape.CharEscapers;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ public class UrlEncodedContent
/*     */   extends AbstractHttpContent
/*     */ {
/*     */   private Object data;
/*     */   
/*     */   public UrlEncodedContent(Object data)
/*     */   {
/*  62 */     super(UrlEncodedParser.MEDIA_TYPE);
/*  63 */     setData(data);
/*     */   }
/*     */   
/*     */   public void writeTo(OutputStream out) throws IOException {
/*  67 */     Writer writer = new BufferedWriter(new OutputStreamWriter(out, getCharset()));
/*  68 */     boolean first = true;
/*  69 */     for (Map.Entry<String, Object> nameValueEntry : Data.mapOf(this.data).entrySet()) {
/*  70 */       Object value = nameValueEntry.getValue();
/*  71 */       if (value != null) {
/*  72 */         String name = CharEscapers.escapeUri((String)nameValueEntry.getKey());
/*  73 */         Class<? extends Object> valueClass = value.getClass();
/*  74 */         if (((value instanceof Iterable)) || (valueClass.isArray())) {
/*  75 */           for (Object repeatedValue : Types.iterableOf(value)) {
/*  76 */             first = appendParam(first, writer, name, repeatedValue);
/*     */           }
/*     */         } else {
/*  79 */           first = appendParam(first, writer, name, value);
/*     */         }
/*     */       }
/*     */     }
/*  83 */     writer.flush();
/*     */   }
/*     */   
/*     */   public UrlEncodedContent setMediaType(HttpMediaType mediaType)
/*     */   {
/*  88 */     super.setMediaType(mediaType);
/*  89 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Object getData()
/*     */   {
/*  98 */     return this.data;
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
/*     */   public UrlEncodedContent setData(Object data)
/*     */   {
/* 112 */     this.data = Preconditions.checkNotNull(data);
/* 113 */     return this;
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
/*     */   public static UrlEncodedContent getContent(HttpRequest request)
/*     */   {
/* 128 */     HttpContent content = request.getContent();
/* 129 */     if (content != null) {
/* 130 */       return (UrlEncodedContent)content;
/*     */     }
/* 132 */     UrlEncodedContent result = new UrlEncodedContent(new HashMap());
/* 133 */     request.setContent(result);
/* 134 */     return result;
/*     */   }
/*     */   
/*     */   private static boolean appendParam(boolean first, Writer writer, String name, Object value)
/*     */     throws IOException
/*     */   {
/* 140 */     if ((value == null) || (Data.isNull(value))) {
/* 141 */       return first;
/*     */     }
/*     */     
/* 144 */     if (first) {
/* 145 */       first = false;
/*     */     } else {
/* 147 */       writer.write("&");
/*     */     }
/* 149 */     writer.write(name);
/* 150 */     String stringValue = CharEscapers.escapeUri((value instanceof Enum) ? FieldInfo.of((Enum)value).getName() : value.toString());
/*     */     
/* 152 */     if (stringValue.length() != 0) {
/* 153 */       writer.write("=");
/* 154 */       writer.write(stringValue);
/*     */     }
/* 156 */     return first;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\UrlEncodedContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */