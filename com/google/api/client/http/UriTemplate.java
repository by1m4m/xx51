/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.Data;
/*     */ import com.google.api.client.util.FieldInfo;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.Types;
/*     */ import com.google.api.client.util.escape.CharEscapers;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ public class UriTemplate
/*     */ {
/*  79 */   static final Map<Character, CompositeOutput> COMPOSITE_PREFIXES = new HashMap();
/*     */   private static final String COMPOSITE_NON_EXPLODE_JOINER = ",";
/*     */   
/*     */   static {
/*  83 */     CompositeOutput.values();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static enum CompositeOutput
/*     */   {
/*  94 */     PLUS(Character.valueOf('+'), "", ",", false, true), 
/*     */     
/*     */ 
/*  97 */     HASH(Character.valueOf('#'), "#", ",", false, true), 
/*     */     
/*     */ 
/* 100 */     DOT(Character.valueOf('.'), ".", ".", false, false), 
/*     */     
/*     */ 
/* 103 */     FORWARD_SLASH(Character.valueOf('/'), "/", "/", false, false), 
/*     */     
/*     */ 
/* 106 */     SEMI_COLON(Character.valueOf(';'), ";", ";", true, false), 
/*     */     
/*     */ 
/* 109 */     QUERY(Character.valueOf('?'), "?", "&", true, false), 
/*     */     
/*     */ 
/* 112 */     AMP(Character.valueOf('&'), "&", "&", true, false), 
/*     */     
/*     */ 
/* 115 */     SIMPLE(null, "", ",", false, false);
/*     */     
/*     */ 
/*     */ 
/*     */     private final Character propertyPrefix;
/*     */     
/*     */ 
/*     */     private final String outputPrefix;
/*     */     
/*     */ 
/*     */     private final String explodeJoiner;
/*     */     
/*     */ 
/*     */     private final boolean requiresVarAssignment;
/*     */     
/*     */     private final boolean reservedExpansion;
/*     */     
/*     */ 
/*     */     private CompositeOutput(Character propertyPrefix, String outputPrefix, String explodeJoiner, boolean requiresVarAssignment, boolean reservedExpansion)
/*     */     {
/* 135 */       this.propertyPrefix = propertyPrefix;
/* 136 */       this.outputPrefix = ((String)Preconditions.checkNotNull(outputPrefix));
/* 137 */       this.explodeJoiner = ((String)Preconditions.checkNotNull(explodeJoiner));
/* 138 */       this.requiresVarAssignment = requiresVarAssignment;
/* 139 */       this.reservedExpansion = reservedExpansion;
/* 140 */       if (propertyPrefix != null) {
/* 141 */         UriTemplate.COMPOSITE_PREFIXES.put(propertyPrefix, this);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     String getOutputPrefix()
/*     */     {
/* 149 */       return this.outputPrefix;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     String getExplodeJoiner()
/*     */     {
/* 156 */       return this.explodeJoiner;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     boolean requiresVarAssignment()
/*     */     {
/* 163 */       return this.requiresVarAssignment;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     int getVarNameStartIndex()
/*     */     {
/* 171 */       return this.propertyPrefix == null ? 0 : 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     String getEncodedValue(String value)
/*     */     {
/*     */       String encodedValue;
/*     */       
/*     */ 
/*     */       String encodedValue;
/*     */       
/*     */ 
/* 184 */       if (this.reservedExpansion)
/*     */       {
/* 186 */         encodedValue = CharEscapers.escapeUriPath(value);
/*     */       } else {
/* 188 */         encodedValue = CharEscapers.escapeUri(value);
/*     */       }
/* 190 */       return encodedValue;
/*     */     }
/*     */     
/*     */     boolean getReservedExpansion() {
/* 194 */       return this.reservedExpansion;
/*     */     }
/*     */   }
/*     */   
/*     */   static CompositeOutput getCompositeOutput(String propertyName) {
/* 199 */     CompositeOutput compositeOutput = (CompositeOutput)COMPOSITE_PREFIXES.get(Character.valueOf(propertyName.charAt(0)));
/* 200 */     return compositeOutput == null ? CompositeOutput.SIMPLE : compositeOutput;
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
/*     */   private static Map<String, Object> getMap(Object obj)
/*     */   {
/* 213 */     Map<String, Object> map = new LinkedHashMap();
/* 214 */     for (Map.Entry<String, Object> entry : Data.mapOf(obj).entrySet()) {
/* 215 */       Object value = entry.getValue();
/* 216 */       if ((value != null) && (!Data.isNull(value))) {
/* 217 */         map.put(entry.getKey(), value);
/*     */       }
/*     */     }
/* 220 */     return map;
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
/*     */   public static String expand(String pathUri, Object parameters, boolean addUnusedParamsAsQueryParams)
/*     */   {
/* 281 */     Map<String, Object> variableMap = getMap(parameters);
/* 282 */     StringBuilder pathBuf = new StringBuilder();
/* 283 */     int cur = 0;
/* 284 */     int length = pathUri.length();
/* 285 */     while (cur < length) {
/* 286 */       int next = pathUri.indexOf('{', cur);
/* 287 */       if (next == -1) {
/* 288 */         if ((cur == 0) && (!addUnusedParamsAsQueryParams))
/*     */         {
/* 290 */           return pathUri;
/*     */         }
/* 292 */         pathBuf.append(pathUri.substring(cur));
/* 293 */         break;
/*     */       }
/* 295 */       pathBuf.append(pathUri.substring(cur, next));
/* 296 */       int close = pathUri.indexOf('}', next + 2);
/* 297 */       String template = pathUri.substring(next + 1, close);
/* 298 */       cur = close + 1;
/*     */       
/* 300 */       boolean containsExplodeModifier = template.endsWith("*");
/* 301 */       CompositeOutput compositeOutput = getCompositeOutput(template);
/*     */       
/* 303 */       int varNameStartIndex = compositeOutput.getVarNameStartIndex();
/* 304 */       int varNameEndIndex = template.length();
/* 305 */       if (containsExplodeModifier)
/*     */       {
/* 307 */         varNameEndIndex -= 1;
/*     */       }
/*     */       
/* 310 */       String varName = template.substring(varNameStartIndex, varNameEndIndex);
/*     */       
/* 312 */       Object value = variableMap.remove(varName);
/* 313 */       if (value != null)
/*     */       {
/*     */ 
/*     */ 
/* 317 */         if ((value instanceof Iterator))
/*     */         {
/* 319 */           Iterator<?> iterator = (Iterator)value;
/* 320 */           value = getListPropertyValue(varName, iterator, containsExplodeModifier, compositeOutput);
/* 321 */         } else if (((value instanceof Iterable)) || (value.getClass().isArray()))
/*     */         {
/* 323 */           Iterator<?> iterator = Types.iterableOf(value).iterator();
/* 324 */           value = getListPropertyValue(varName, iterator, containsExplodeModifier, compositeOutput);
/* 325 */         } else if (value.getClass().isEnum()) {
/* 326 */           String name = FieldInfo.of((Enum)value).getName();
/* 327 */           if (name != null) {
/* 328 */             value = CharEscapers.escapeUriPath(name);
/*     */           }
/* 330 */         } else if (!Data.isValueOfPrimitiveType(value))
/*     */         {
/* 332 */           Map<String, Object> map = getMap(value);
/* 333 */           value = getMapPropertyValue(varName, map, containsExplodeModifier, compositeOutput);
/*     */ 
/*     */         }
/* 336 */         else if (compositeOutput.getReservedExpansion()) {
/* 337 */           value = CharEscapers.escapeUriPathWithoutReserved(value.toString());
/*     */         } else {
/* 339 */           value = CharEscapers.escapeUriPath(value.toString());
/*     */         }
/*     */         
/* 342 */         pathBuf.append(value);
/*     */       } }
/* 344 */     if (addUnusedParamsAsQueryParams)
/*     */     {
/* 346 */       GenericUrl.addQueryParams(variableMap.entrySet(), pathBuf);
/*     */     }
/* 348 */     return pathBuf.toString();
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
/*     */   private static String getListPropertyValue(String varName, Iterator<?> iterator, boolean containsExplodeModifier, CompositeOutput compositeOutput)
/*     */   {
/* 366 */     if (!iterator.hasNext()) {
/* 367 */       return "";
/*     */     }
/* 369 */     StringBuilder retBuf = new StringBuilder();
/* 370 */     retBuf.append(compositeOutput.getOutputPrefix());
/*     */     String joiner;
/* 372 */     String joiner; if (containsExplodeModifier) {
/* 373 */       joiner = compositeOutput.getExplodeJoiner();
/*     */     } else {
/* 375 */       joiner = ",";
/* 376 */       if (compositeOutput.requiresVarAssignment()) {
/* 377 */         retBuf.append(CharEscapers.escapeUriPath(varName));
/* 378 */         retBuf.append("=");
/*     */       }
/*     */     }
/* 381 */     while (iterator.hasNext()) {
/* 382 */       if ((containsExplodeModifier) && (compositeOutput.requiresVarAssignment())) {
/* 383 */         retBuf.append(CharEscapers.escapeUriPath(varName));
/* 384 */         retBuf.append("=");
/*     */       }
/* 386 */       retBuf.append(compositeOutput.getEncodedValue(iterator.next().toString()));
/* 387 */       if (iterator.hasNext()) {
/* 388 */         retBuf.append(joiner);
/*     */       }
/*     */     }
/* 391 */     return retBuf.toString();
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
/*     */   private static String getMapPropertyValue(String varName, Map<String, Object> map, boolean containsExplodeModifier, CompositeOutput compositeOutput)
/*     */   {
/* 409 */     if (map.isEmpty()) {
/* 410 */       return "";
/*     */     }
/* 412 */     StringBuilder retBuf = new StringBuilder();
/* 413 */     retBuf.append(compositeOutput.getOutputPrefix());
/*     */     String mapElementsJoiner;
/*     */     String joiner;
/* 416 */     String mapElementsJoiner; if (containsExplodeModifier) {
/* 417 */       String joiner = compositeOutput.getExplodeJoiner();
/* 418 */       mapElementsJoiner = "=";
/*     */     } else {
/* 420 */       joiner = ",";
/* 421 */       mapElementsJoiner = ",";
/* 422 */       if (compositeOutput.requiresVarAssignment()) {
/* 423 */         retBuf.append(CharEscapers.escapeUriPath(varName));
/* 424 */         retBuf.append("=");
/*     */       }
/*     */     }
/* 427 */     Iterator<Map.Entry<String, Object>> mapIterator = map.entrySet().iterator();
/* 428 */     while (mapIterator.hasNext()) {
/* 429 */       Map.Entry<String, Object> entry = (Map.Entry)mapIterator.next();
/* 430 */       String encodedKey = compositeOutput.getEncodedValue((String)entry.getKey());
/* 431 */       String encodedValue = compositeOutput.getEncodedValue(entry.getValue().toString());
/* 432 */       retBuf.append(encodedKey);
/* 433 */       retBuf.append(mapElementsJoiner);
/* 434 */       retBuf.append(encodedValue);
/* 435 */       if (mapIterator.hasNext()) {
/* 436 */         retBuf.append(joiner);
/*     */       }
/*     */     }
/* 439 */     return retBuf.toString();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static String expand(String baseUrl, String uriTemplate, Object parameters, boolean addUnusedParamsAsQueryParams)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ldc 110
/*     */     //   3: invokevirtual 114	java/lang/String:startsWith	(Ljava/lang/String;)Z
/*     */     //   6: ifeq +58 -> 64
/*     */     //   9: new 116	com/google/api/client/http/GenericUrl
/*     */     //   12: dup
/*     */     //   13: aload_0
/*     */     //   14: invokespecial 119	com/google/api/client/http/GenericUrl:<init>	(Ljava/lang/String;)V
/*     */     //   17: astore 4
/*     */     //   19: aload 4
/*     */     //   21: aconst_null
/*     */     //   22: invokevirtual 122	com/google/api/client/http/GenericUrl:setRawPath	(Ljava/lang/String;)V
/*     */     //   25: aload 4
/*     */     //   27: invokevirtual 126	com/google/api/client/http/GenericUrl:build	()Ljava/lang/String;
/*     */     //   30: invokestatic 129	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   33: aload_1
/*     */     //   34: invokestatic 129	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   37: dup
/*     */     //   38: invokevirtual 133	java/lang/String:length	()I
/*     */     //   41: ifeq +9 -> 50
/*     */     //   44: invokevirtual 137	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   47: goto +12 -> 59
/*     */     //   50: pop
/*     */     //   51: new 32	java/lang/String
/*     */     //   54: dup_x1
/*     */     //   55: swap
/*     */     //   56: invokespecial 138	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   59: astore 5
/*     */     //   61: goto +59 -> 120
/*     */     //   64: aload_1
/*     */     //   65: ldc -116
/*     */     //   67: invokevirtual 114	java/lang/String:startsWith	(Ljava/lang/String;)Z
/*     */     //   70: ifne +12 -> 82
/*     */     //   73: aload_1
/*     */     //   74: ldc -114
/*     */     //   76: invokevirtual 114	java/lang/String:startsWith	(Ljava/lang/String;)Z
/*     */     //   79: ifeq +9 -> 88
/*     */     //   82: aload_1
/*     */     //   83: astore 5
/*     */     //   85: goto +35 -> 120
/*     */     //   88: aload_0
/*     */     //   89: invokestatic 129	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   92: aload_1
/*     */     //   93: invokestatic 129	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   96: dup
/*     */     //   97: invokevirtual 133	java/lang/String:length	()I
/*     */     //   100: ifeq +9 -> 109
/*     */     //   103: invokevirtual 137	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   106: goto +12 -> 118
/*     */     //   109: pop
/*     */     //   110: new 32	java/lang/String
/*     */     //   113: dup_x1
/*     */     //   114: swap
/*     */     //   115: invokespecial 138	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   118: astore 5
/*     */     //   120: aload 5
/*     */     //   122: aload_2
/*     */     //   123: iload_3
/*     */     //   124: invokestatic 145	com/google/api/client/http/UriTemplate:expand	(Ljava/lang/String;Ljava/lang/Object;Z)Ljava/lang/String;
/*     */     //   127: areturn
/*     */     // Line number table:
/*     */     //   Java source line #249	-> byte code offset #0
/*     */     //   Java source line #251	-> byte code offset #9
/*     */     //   Java source line #252	-> byte code offset #19
/*     */     //   Java source line #253	-> byte code offset #25
/*     */     //   Java source line #254	-> byte code offset #61
/*     */     //   Java source line #255	-> byte code offset #82
/*     */     //   Java source line #257	-> byte code offset #88
/*     */     //   Java source line #259	-> byte code offset #120
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	128	0	baseUrl	String
/*     */     //   0	128	1	uriTemplate	String
/*     */     //   0	128	2	parameters	Object
/*     */     //   0	128	3	addUnusedParamsAsQueryParams	boolean
/*     */     //   19	42	4	url	GenericUrl
/*     */     //   61	3	5	pathUri	String
/*     */     //   85	3	5	pathUri	String
/*     */     //   120	8	5	pathUri	String
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\UriTemplate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */