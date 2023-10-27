/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.ArrayValueMap;
/*     */ import com.google.api.client.util.Charsets;
/*     */ import com.google.api.client.util.ClassInfo;
/*     */ import com.google.api.client.util.Data;
/*     */ import com.google.api.client.util.FieldInfo;
/*     */ import com.google.api.client.util.GenericData;
/*     */ import com.google.api.client.util.ObjectParser;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.Throwables;
/*     */ import com.google.api.client.util.Types;
/*     */ import com.google.api.client.util.escape.CharEscapers;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Type;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UrlEncodedParser
/*     */   implements ObjectParser
/*     */ {
/*     */   public static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
/*  81 */   public static final String MEDIA_TYPE = new HttpMediaType("application/x-www-form-urlencoded").setCharsetParameter(Charsets.UTF_8).build();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void parse(String content, Object data)
/*     */   {
/*  92 */     if (content == null) {
/*  93 */       return;
/*     */     }
/*     */     try {
/*  96 */       parse(new StringReader(content), data);
/*     */     }
/*     */     catch (IOException exception) {
/*  99 */       throw Throwables.propagate(exception);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void parse(Reader reader, Object data)
/*     */     throws IOException
/*     */   {
/* 128 */     Class<?> clazz = data.getClass();
/* 129 */     ClassInfo classInfo = ClassInfo.of(clazz);
/* 130 */     List<Type> context = Arrays.asList(new Type[] { clazz });
/* 131 */     GenericData genericData = GenericData.class.isAssignableFrom(clazz) ? (GenericData)data : null;
/*     */     
/* 133 */     Map<Object, Object> map = Map.class.isAssignableFrom(clazz) ? (Map)data : null;
/* 134 */     ArrayValueMap arrayValueMap = new ArrayValueMap(data);
/* 135 */     StringWriter nameWriter = new StringWriter();
/* 136 */     StringWriter valueWriter = new StringWriter();
/* 137 */     boolean readingName = true;
/*     */     for (;;) {
/* 139 */       int read = reader.read();
/* 140 */       switch (read)
/*     */       {
/*     */ 
/*     */       case -1: 
/*     */       case 38: 
/* 145 */         String name = CharEscapers.decodeUri(nameWriter.toString());
/* 146 */         if (name.length() != 0) {
/* 147 */           String stringValue = CharEscapers.decodeUri(valueWriter.toString());
/*     */           
/* 149 */           FieldInfo fieldInfo = classInfo.getFieldInfo(name);
/* 150 */           if (fieldInfo != null) {
/* 151 */             Type type = Data.resolveWildcardTypeOrTypeVariable(context, fieldInfo.getGenericType());
/*     */             
/*     */ 
/* 154 */             if (Types.isArray(type))
/*     */             {
/* 156 */               Class<?> rawArrayComponentType = Types.getRawArrayComponentType(context, Types.getArrayComponentType(type));
/*     */               
/* 158 */               arrayValueMap.put(fieldInfo.getField(), rawArrayComponentType, parseValue(rawArrayComponentType, context, stringValue));
/*     */             }
/* 160 */             else if (Types.isAssignableToOrFrom(Types.getRawArrayComponentType(context, type), Iterable.class))
/*     */             {
/*     */ 
/*     */ 
/* 164 */               Collection<Object> collection = (Collection)fieldInfo.getValue(data);
/* 165 */               if (collection == null) {
/* 166 */                 collection = Data.newCollectionInstance(type);
/* 167 */                 fieldInfo.setValue(data, collection);
/*     */               }
/* 169 */               Type subFieldType = type == Object.class ? null : Types.getIterableParameter(type);
/* 170 */               collection.add(parseValue(subFieldType, context, stringValue));
/*     */             }
/*     */             else {
/* 173 */               fieldInfo.setValue(data, parseValue(type, context, stringValue));
/*     */             }
/* 175 */           } else if (map != null)
/*     */           {
/*     */ 
/* 178 */             ArrayList<String> listValue = (ArrayList)map.get(name);
/* 179 */             if (listValue == null) {
/* 180 */               listValue = new ArrayList();
/* 181 */               if (genericData != null) {
/* 182 */                 genericData.set(name, listValue);
/*     */               } else {
/* 184 */                 map.put(name, listValue);
/*     */               }
/*     */             }
/* 187 */             listValue.add(stringValue);
/*     */           }
/*     */         }
/*     */         
/* 191 */         readingName = true;
/* 192 */         nameWriter = new StringWriter();
/* 193 */         valueWriter = new StringWriter();
/* 194 */         if (read != -1) break;
/* 195 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case 61: 
/* 200 */         readingName = false;
/* 201 */         break;
/*     */       
/*     */       default: 
/* 204 */         if (readingName) {
/* 205 */           nameWriter.write(read);
/*     */         } else
/* 207 */           valueWriter.write(read);
/*     */         break; }
/*     */       
/*     */     }
/* 211 */     arrayValueMap.setValues();
/*     */   }
/*     */   
/*     */   private static Object parseValue(Type valueType, List<Type> context, String value) {
/* 215 */     Type resolved = Data.resolveWildcardTypeOrTypeVariable(context, valueType);
/* 216 */     return Data.parsePrimitiveValue(resolved, value);
/*     */   }
/*     */   
/*     */   public <T> T parseAndClose(InputStream in, Charset charset, Class<T> dataClass) throws IOException
/*     */   {
/* 221 */     InputStreamReader r = new InputStreamReader(in, charset);
/* 222 */     return (T)parseAndClose(r, dataClass);
/*     */   }
/*     */   
/*     */   public Object parseAndClose(InputStream in, Charset charset, Type dataType) throws IOException {
/* 226 */     InputStreamReader r = new InputStreamReader(in, charset);
/* 227 */     return parseAndClose(r, dataType);
/*     */   }
/*     */   
/*     */   public <T> T parseAndClose(Reader reader, Class<T> dataClass) throws IOException
/*     */   {
/* 232 */     return (T)parseAndClose(reader, dataClass);
/*     */   }
/*     */   
/*     */   public Object parseAndClose(Reader reader, Type dataType) throws IOException {
/* 236 */     Preconditions.checkArgument(dataType instanceof Class, "dataType has to be of type Class<?>");
/*     */     
/*     */ 
/* 239 */     Object newInstance = Types.newInstance((Class)dataType);
/* 240 */     parse(new BufferedReader(reader), newInstance);
/* 241 */     return newInstance;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\UrlEncodedParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */