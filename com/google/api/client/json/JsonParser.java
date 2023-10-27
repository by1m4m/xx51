/*     */ package com.google.api.client.json;
/*     */ 
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.ClassInfo;
/*     */ import com.google.api.client.util.Data;
/*     */ import com.google.api.client.util.FieldInfo;
/*     */ import com.google.api.client.util.GenericData;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.Sets;
/*     */ import com.google.api.client.util.Types;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Type;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JsonParser
/*     */ {
/*  68 */   private static WeakHashMap<Class<?>, Field> cachedTypemapFields = new WeakHashMap();
/*     */   
/*     */ 
/*     */ 
/*  72 */   private static final Lock lock = new ReentrantLock();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract JsonFactory getFactory();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract JsonToken nextToken()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract JsonToken getCurrentToken();
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract String getCurrentName()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract JsonParser skipChildren()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract String getText()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract byte getByteValue()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract short getShortValue()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract int getIntValue()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract float getFloatValue()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract long getLongValue()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract double getDoubleValue()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract BigInteger getBigIntegerValue()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public abstract BigDecimal getDecimalValue()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public final <T> T parseAndClose(Class<T> destinationClass)
/*     */     throws IOException
/*     */   {
/* 147 */     return (T)parseAndClose(destinationClass, null);
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
/*     */   @Beta
/*     */   public final <T> T parseAndClose(Class<T> destinationClass, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 165 */       return (T)parse(destinationClass, customizeParser);
/*     */     } finally {
/* 167 */       close();
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
/*     */   public final void skipToKey(String keyToFind)
/*     */     throws IOException
/*     */   {
/* 184 */     skipToKey(Collections.singleton(keyToFind));
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
/*     */   public final String skipToKey(Set<String> keysToFind)
/*     */     throws IOException
/*     */   {
/* 202 */     JsonToken curToken = startParsingObjectOrArray();
/* 203 */     while (curToken == JsonToken.FIELD_NAME) {
/* 204 */       String key = getText();
/* 205 */       nextToken();
/* 206 */       if (keysToFind.contains(key)) {
/* 207 */         return key;
/*     */       }
/* 209 */       skipChildren();
/* 210 */       curToken = nextToken();
/*     */     }
/* 212 */     return null;
/*     */   }
/*     */   
/*     */   private JsonToken startParsing() throws IOException
/*     */   {
/* 217 */     JsonToken currentToken = getCurrentToken();
/*     */     
/* 219 */     if (currentToken == null) {
/* 220 */       currentToken = nextToken();
/*     */     }
/* 222 */     Preconditions.checkArgument(currentToken != null, "no JSON input found");
/* 223 */     return currentToken;
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
/*     */   private JsonToken startParsingObjectOrArray()
/*     */     throws IOException
/*     */   {
/* 239 */     JsonToken currentToken = startParsing();
/* 240 */     switch (currentToken) {
/*     */     case START_OBJECT: 
/* 242 */       currentToken = nextToken();
/* 243 */       Preconditions.checkArgument((currentToken == JsonToken.FIELD_NAME) || (currentToken == JsonToken.END_OBJECT), currentToken);
/*     */       
/*     */ 
/* 246 */       break;
/*     */     case START_ARRAY: 
/* 248 */       currentToken = nextToken();
/* 249 */       break;
/*     */     }
/*     */     
/*     */     
/* 253 */     return currentToken;
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
/*     */   public final void parseAndClose(Object destination)
/*     */     throws IOException
/*     */   {
/* 269 */     parseAndClose(destination, null);
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
/*     */   @Beta
/*     */   public final void parseAndClose(Object destination, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 289 */       parse(destination, customizeParser);
/*     */     } finally {
/* 291 */       close();
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
/*     */   public final <T> T parse(Class<T> destinationClass)
/*     */     throws IOException
/*     */   {
/* 311 */     return (T)parse(destinationClass, null);
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
/*     */   @Beta
/*     */   public final <T> T parse(Class<T> destinationClass, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/* 335 */     T result = parse(destinationClass, false, customizeParser);
/* 336 */     return result;
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
/*     */   public Object parse(Type dataType, boolean close)
/*     */     throws IOException
/*     */   {
/* 354 */     return parse(dataType, close, null);
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
/*     */   @Beta
/*     */   public Object parse(Type dataType, boolean close, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 378 */       if (!Void.class.equals(dataType)) {
/* 379 */         startParsing();
/*     */       }
/* 381 */       return parseValue(null, dataType, new ArrayList(), null, customizeParser, true);
/*     */     } finally {
/* 383 */       if (close) {
/* 384 */         close();
/*     */       }
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
/*     */   public final void parse(Object destination)
/*     */     throws IOException
/*     */   {
/* 402 */     parse(destination, null);
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
/*     */   @Beta
/*     */   public final void parse(Object destination, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/* 422 */     ArrayList<Type> context = new ArrayList();
/* 423 */     context.add(destination.getClass());
/* 424 */     parse(context, destination, customizeParser);
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
/*     */   private void parse(ArrayList<Type> context, Object destination, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/* 438 */     if ((destination instanceof GenericJson)) {
/* 439 */       ((GenericJson)destination).setFactory(getFactory());
/*     */     }
/* 441 */     JsonToken curToken = startParsingObjectOrArray();
/* 442 */     Class<?> destinationClass = destination.getClass();
/* 443 */     ClassInfo classInfo = ClassInfo.of(destinationClass);
/* 444 */     boolean isGenericData = GenericData.class.isAssignableFrom(destinationClass);
/* 445 */     if ((!isGenericData) && (Map.class.isAssignableFrom(destinationClass)))
/*     */     {
/*     */ 
/*     */ 
/* 449 */       Map<String, Object> destinationMap = (Map)destination;
/* 450 */       parseMap(null, destinationMap, Types.getMapValueParameter(destinationClass), context, customizeParser);
/*     */       
/* 452 */       return;
/*     */     }
/* 454 */     while (curToken == JsonToken.FIELD_NAME) {
/* 455 */       String key = getText();
/* 456 */       nextToken();
/*     */       
/* 458 */       if ((customizeParser != null) && (customizeParser.stopAt(destination, key))) {
/* 459 */         return;
/*     */       }
/*     */       
/* 462 */       FieldInfo fieldInfo = classInfo.getFieldInfo(key);
/* 463 */       if (fieldInfo != null)
/*     */       {
/* 465 */         if ((fieldInfo.isFinal()) && (!fieldInfo.isPrimitive())) {
/* 466 */           throw new IllegalArgumentException("final array/object fields are not supported");
/*     */         }
/* 468 */         Field field = fieldInfo.getField();
/* 469 */         int contextSize = context.size();
/* 470 */         context.add(field.getGenericType());
/* 471 */         Object fieldValue = parseValue(field, fieldInfo.getGenericType(), context, destination, customizeParser, true);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 477 */         context.remove(contextSize);
/* 478 */         fieldInfo.setValue(destination, fieldValue);
/* 479 */       } else if (isGenericData)
/*     */       {
/* 481 */         GenericData object = (GenericData)destination;
/* 482 */         object.set(key, parseValue(null, null, context, destination, customizeParser, true));
/*     */       }
/*     */       else {
/* 485 */         if (customizeParser != null) {
/* 486 */           customizeParser.handleUnrecognizedKey(destination, key);
/*     */         }
/* 488 */         skipChildren();
/*     */       }
/* 490 */       curToken = nextToken();
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
/*     */   public final <T> Collection<T> parseArrayAndClose(Class<?> destinationCollectionClass, Class<T> destinationItemClass)
/*     */     throws IOException
/*     */   {
/* 506 */     return parseArrayAndClose(destinationCollectionClass, destinationItemClass, null);
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
/*     */   @Beta
/*     */   public final <T> Collection<T> parseArrayAndClose(Class<?> destinationCollectionClass, Class<T> destinationItemClass, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 524 */       return parseArray(destinationCollectionClass, destinationItemClass, customizeParser);
/*     */     } finally {
/* 526 */       close();
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
/*     */   public final <T> void parseArrayAndClose(Collection<? super T> destinationCollection, Class<T> destinationItemClass)
/*     */     throws IOException
/*     */   {
/* 542 */     parseArrayAndClose(destinationCollection, destinationItemClass, null);
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
/*     */   @Beta
/*     */   public final <T> void parseArrayAndClose(Collection<? super T> destinationCollection, Class<T> destinationItemClass, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 559 */       parseArray(destinationCollection, destinationItemClass, customizeParser);
/*     */     } finally {
/* 561 */       close();
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
/*     */   public final <T> Collection<T> parseArray(Class<?> destinationCollectionClass, Class<T> destinationItemClass)
/*     */     throws IOException
/*     */   {
/* 576 */     return parseArray(destinationCollectionClass, destinationItemClass, null);
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
/*     */   @Beta
/*     */   public final <T> Collection<T> parseArray(Class<?> destinationCollectionClass, Class<T> destinationItemClass, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/* 594 */     Collection<T> destinationCollection = Data.newCollectionInstance(destinationCollectionClass);
/*     */     
/* 596 */     parseArray(destinationCollection, destinationItemClass, customizeParser);
/* 597 */     return destinationCollection;
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
/*     */   public final <T> void parseArray(Collection<? super T> destinationCollection, Class<T> destinationItemClass)
/*     */     throws IOException
/*     */   {
/* 611 */     parseArray(destinationCollection, destinationItemClass, null);
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
/*     */   @Beta
/*     */   public final <T> void parseArray(Collection<? super T> destinationCollection, Class<T> destinationItemClass, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/* 627 */     parseArray(null, destinationCollection, destinationItemClass, new ArrayList(), customizeParser);
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
/*     */   private <T> void parseArray(Field fieldContext, Collection<T> destinationCollection, Type destinationItemType, ArrayList<Type> context, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/* 644 */     JsonToken curToken = startParsingObjectOrArray();
/* 645 */     while (curToken != JsonToken.END_ARRAY)
/*     */     {
/* 647 */       T parsedValue = parseValue(fieldContext, destinationItemType, context, destinationCollection, customizeParser, true);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 653 */       destinationCollection.add(parsedValue);
/* 654 */       curToken = nextToken();
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
/*     */   private void parseMap(Field fieldContext, Map<String, Object> destinationMap, Type valueType, ArrayList<Type> context, CustomizeJsonParser customizeParser)
/*     */     throws IOException
/*     */   {
/* 670 */     JsonToken curToken = startParsingObjectOrArray();
/* 671 */     while (curToken == JsonToken.FIELD_NAME) {
/* 672 */       String key = getText();
/* 673 */       nextToken();
/*     */       
/* 675 */       if ((customizeParser != null) && (customizeParser.stopAt(destinationMap, key))) {
/* 676 */         return;
/*     */       }
/* 678 */       Object value = parseValue(fieldContext, valueType, context, destinationMap, customizeParser, true);
/*     */       
/* 680 */       destinationMap.put(key, value);
/* 681 */       curToken = nextToken();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private final Object parseValue(Field fieldContext, Type valueType, ArrayList<Type> context, Object destination, CustomizeJsonParser customizeParser, boolean handlePolymorphic)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_3
/*     */     //   1: aload_2
/*     */     //   2: invokestatic 326	com/google/api/client/util/Data:resolveWildcardTypeOrTypeVariable	(Ljava/util/List;Ljava/lang/reflect/Type;)Ljava/lang/reflect/Type;
/*     */     //   5: astore_2
/*     */     //   6: aload_2
/*     */     //   7: instanceof 191
/*     */     //   10: ifeq +10 -> 20
/*     */     //   13: aload_2
/*     */     //   14: checkcast 191	java/lang/Class
/*     */     //   17: goto +4 -> 21
/*     */     //   20: aconst_null
/*     */     //   21: astore 7
/*     */     //   23: aload_2
/*     */     //   24: instanceof 328
/*     */     //   27: ifeq +12 -> 39
/*     */     //   30: aload_2
/*     */     //   31: checkcast 328	java/lang/reflect/ParameterizedType
/*     */     //   34: invokestatic 332	com/google/api/client/util/Types:getRawClass	(Ljava/lang/reflect/ParameterizedType;)Ljava/lang/Class;
/*     */     //   37: astore 7
/*     */     //   39: aload 7
/*     */     //   41: ldc -106
/*     */     //   43: if_acmpne +10 -> 53
/*     */     //   46: aload_0
/*     */     //   47: invokevirtual 102	com/google/api/client/json/JsonParser:skipChildren	()Lcom/google/api/client/json/JsonParser;
/*     */     //   50: pop
/*     */     //   51: aconst_null
/*     */     //   52: areturn
/*     */     //   53: aload_0
/*     */     //   54: invokevirtual 110	com/google/api/client/json/JsonParser:getCurrentToken	()Lcom/google/api/client/json/JsonToken;
/*     */     //   57: astore 8
/*     */     //   59: getstatic 125	com/google/api/client/json/JsonParser$1:$SwitchMap$com$google$api$client$json$JsonToken	[I
/*     */     //   62: aload_0
/*     */     //   63: invokevirtual 110	com/google/api/client/json/JsonParser:getCurrentToken	()Lcom/google/api/client/json/JsonToken;
/*     */     //   66: invokevirtual 128	com/google/api/client/json/JsonToken:ordinal	()I
/*     */     //   69: iaload
/*     */     //   70: tableswitch	default:+1121->1191, 1:+220->290, 2:+58->128, 3:+58->128, 4:+220->290, 5:+220->290, 6:+662->732, 7:+662->732, 8:+727->797, 9:+727->797, 10:+976->1046, 11:+1026->1096
/*     */     //   128: aload_2
/*     */     //   129: invokestatic 336	com/google/api/client/util/Types:isArray	(Ljava/lang/reflect/Type;)Z
/*     */     //   132: istore 9
/*     */     //   134: aload_2
/*     */     //   135: ifnull +24 -> 159
/*     */     //   138: iload 9
/*     */     //   140: ifne +19 -> 159
/*     */     //   143: aload 7
/*     */     //   145: ifnull +18 -> 163
/*     */     //   148: aload 7
/*     */     //   150: ldc_w 312
/*     */     //   153: invokestatic 340	com/google/api/client/util/Types:isAssignableToOrFrom	(Ljava/lang/Class;Ljava/lang/Class;)Z
/*     */     //   156: ifeq +7 -> 163
/*     */     //   159: iconst_1
/*     */     //   160: goto +4 -> 164
/*     */     //   163: iconst_0
/*     */     //   164: ldc_w 342
/*     */     //   167: iconst_1
/*     */     //   168: anewarray 4	java/lang/Object
/*     */     //   171: dup
/*     */     //   172: iconst_0
/*     */     //   173: aload_2
/*     */     //   174: aastore
/*     */     //   175: invokestatic 345	com/google/api/client/util/Preconditions:checkArgument	(ZLjava/lang/String;[Ljava/lang/Object;)V
/*     */     //   178: aconst_null
/*     */     //   179: astore 10
/*     */     //   181: aload 5
/*     */     //   183: ifnull +17 -> 200
/*     */     //   186: aload_1
/*     */     //   187: ifnull +13 -> 200
/*     */     //   190: aload 5
/*     */     //   192: aload 4
/*     */     //   194: aload_1
/*     */     //   195: invokevirtual 349	com/google/api/client/json/CustomizeJsonParser:newInstanceForArray	(Ljava/lang/Object;Ljava/lang/reflect/Field;)Ljava/util/Collection;
/*     */     //   198: astore 10
/*     */     //   200: aload 10
/*     */     //   202: ifnonnull +9 -> 211
/*     */     //   205: aload_2
/*     */     //   206: invokestatic 303	com/google/api/client/util/Data:newCollectionInstance	(Ljava/lang/reflect/Type;)Ljava/util/Collection;
/*     */     //   209: astore 10
/*     */     //   211: aconst_null
/*     */     //   212: astore 11
/*     */     //   214: iload 9
/*     */     //   216: ifeq +12 -> 228
/*     */     //   219: aload_2
/*     */     //   220: invokestatic 352	com/google/api/client/util/Types:getArrayComponentType	(Ljava/lang/reflect/Type;)Ljava/lang/reflect/Type;
/*     */     //   223: astore 11
/*     */     //   225: goto +25 -> 250
/*     */     //   228: aload 7
/*     */     //   230: ifnull +20 -> 250
/*     */     //   233: ldc_w 354
/*     */     //   236: aload 7
/*     */     //   238: invokevirtual 195	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
/*     */     //   241: ifeq +9 -> 250
/*     */     //   244: aload_2
/*     */     //   245: invokestatic 357	com/google/api/client/util/Types:getIterableParameter	(Ljava/lang/reflect/Type;)Ljava/lang/reflect/Type;
/*     */     //   248: astore 11
/*     */     //   250: aload_3
/*     */     //   251: aload 11
/*     */     //   253: invokestatic 326	com/google/api/client/util/Data:resolveWildcardTypeOrTypeVariable	(Ljava/util/List;Ljava/lang/reflect/Type;)Ljava/lang/reflect/Type;
/*     */     //   256: astore 11
/*     */     //   258: aload_0
/*     */     //   259: aload_1
/*     */     //   260: aload 10
/*     */     //   262: aload 11
/*     */     //   264: aload_3
/*     */     //   265: aload 5
/*     */     //   267: invokespecial 307	com/google/api/client/json/JsonParser:parseArray	(Ljava/lang/reflect/Field;Ljava/util/Collection;Ljava/lang/reflect/Type;Ljava/util/ArrayList;Lcom/google/api/client/json/CustomizeJsonParser;)V
/*     */     //   270: iload 9
/*     */     //   272: ifeq +15 -> 287
/*     */     //   275: aload 10
/*     */     //   277: aload_3
/*     */     //   278: aload 11
/*     */     //   280: invokestatic 361	com/google/api/client/util/Types:getRawArrayComponentType	(Ljava/util/List;Ljava/lang/reflect/Type;)Ljava/lang/Class;
/*     */     //   283: invokestatic 365	com/google/api/client/util/Types:toArray	(Ljava/util/Collection;Ljava/lang/Class;)Ljava/lang/Object;
/*     */     //   286: areturn
/*     */     //   287: aload 10
/*     */     //   289: areturn
/*     */     //   290: aload_2
/*     */     //   291: invokestatic 336	com/google/api/client/util/Types:isArray	(Ljava/lang/reflect/Type;)Z
/*     */     //   294: ifne +7 -> 301
/*     */     //   297: iconst_1
/*     */     //   298: goto +4 -> 302
/*     */     //   301: iconst_0
/*     */     //   302: ldc_w 367
/*     */     //   305: iconst_1
/*     */     //   306: anewarray 4	java/lang/Object
/*     */     //   309: dup
/*     */     //   310: iconst_0
/*     */     //   311: aload_2
/*     */     //   312: aastore
/*     */     //   313: invokestatic 345	com/google/api/client/util/Preconditions:checkArgument	(ZLjava/lang/String;[Ljava/lang/Object;)V
/*     */     //   316: iload 6
/*     */     //   318: ifeq +11 -> 329
/*     */     //   321: aload 7
/*     */     //   323: invokestatic 371	com/google/api/client/json/JsonParser:getCachedTypemapFieldFor	(Ljava/lang/Class;)Ljava/lang/reflect/Field;
/*     */     //   326: goto +4 -> 330
/*     */     //   329: aconst_null
/*     */     //   330: astore 12
/*     */     //   332: aconst_null
/*     */     //   333: astore 13
/*     */     //   335: aload 7
/*     */     //   337: ifnull +19 -> 356
/*     */     //   340: aload 5
/*     */     //   342: ifnull +14 -> 356
/*     */     //   345: aload 5
/*     */     //   347: aload 4
/*     */     //   349: aload 7
/*     */     //   351: invokevirtual 375	com/google/api/client/json/CustomizeJsonParser:newInstanceForObject	(Ljava/lang/Object;Ljava/lang/Class;)Ljava/lang/Object;
/*     */     //   354: astore 13
/*     */     //   356: aload 7
/*     */     //   358: ifnull +17 -> 375
/*     */     //   361: aload 7
/*     */     //   363: ldc -59
/*     */     //   365: invokestatic 340	com/google/api/client/util/Types:isAssignableToOrFrom	(Ljava/lang/Class;Ljava/lang/Class;)Z
/*     */     //   368: ifeq +7 -> 375
/*     */     //   371: iconst_1
/*     */     //   372: goto +4 -> 376
/*     */     //   375: iconst_0
/*     */     //   376: istore 14
/*     */     //   378: aload 12
/*     */     //   380: ifnull +15 -> 395
/*     */     //   383: new 175	com/google/api/client/json/GenericJson
/*     */     //   386: dup
/*     */     //   387: invokespecial 376	com/google/api/client/json/GenericJson:<init>	()V
/*     */     //   390: astore 13
/*     */     //   392: goto +35 -> 427
/*     */     //   395: aload 13
/*     */     //   397: ifnonnull +30 -> 427
/*     */     //   400: iload 14
/*     */     //   402: ifne +8 -> 410
/*     */     //   405: aload 7
/*     */     //   407: ifnonnull +13 -> 420
/*     */     //   410: aload 7
/*     */     //   412: invokestatic 380	com/google/api/client/util/Data:newMapInstance	(Ljava/lang/Class;)Ljava/util/Map;
/*     */     //   415: astore 13
/*     */     //   417: goto +10 -> 427
/*     */     //   420: aload 7
/*     */     //   422: invokestatic 383	com/google/api/client/util/Types:newInstance	(Ljava/lang/Class;)Ljava/lang/Object;
/*     */     //   425: astore 13
/*     */     //   427: aload_3
/*     */     //   428: invokevirtual 239	java/util/ArrayList:size	()I
/*     */     //   431: istore 15
/*     */     //   433: aload_2
/*     */     //   434: ifnull +9 -> 443
/*     */     //   437: aload_3
/*     */     //   438: aload_2
/*     */     //   439: invokevirtual 167	java/util/ArrayList:add	(Ljava/lang/Object;)Z
/*     */     //   442: pop
/*     */     //   443: iload 14
/*     */     //   445: ifeq +60 -> 505
/*     */     //   448: ldc -67
/*     */     //   450: aload 7
/*     */     //   452: invokevirtual 195	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
/*     */     //   455: ifne +50 -> 505
/*     */     //   458: ldc -59
/*     */     //   460: aload 7
/*     */     //   462: invokevirtual 195	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
/*     */     //   465: ifeq +10 -> 475
/*     */     //   468: aload_2
/*     */     //   469: invokestatic 203	com/google/api/client/util/Types:getMapValueParameter	(Ljava/lang/reflect/Type;)Ljava/lang/reflect/Type;
/*     */     //   472: goto +4 -> 476
/*     */     //   475: aconst_null
/*     */     //   476: astore 16
/*     */     //   478: aload 16
/*     */     //   480: ifnull +25 -> 505
/*     */     //   483: aload 13
/*     */     //   485: checkcast 197	java/util/Map
/*     */     //   488: astore 17
/*     */     //   490: aload_0
/*     */     //   491: aload_1
/*     */     //   492: aload 17
/*     */     //   494: aload 16
/*     */     //   496: aload_3
/*     */     //   497: aload 5
/*     */     //   499: invokespecial 207	com/google/api/client/json/JsonParser:parseMap	(Ljava/lang/reflect/Field;Ljava/util/Map;Ljava/lang/reflect/Type;Ljava/util/ArrayList;Lcom/google/api/client/json/CustomizeJsonParser;)V
/*     */     //   502: aload 13
/*     */     //   504: areturn
/*     */     //   505: aload_0
/*     */     //   506: aload_3
/*     */     //   507: aload 13
/*     */     //   509: aload 5
/*     */     //   511: invokespecial 170	com/google/api/client/json/JsonParser:parse	(Ljava/util/ArrayList;Ljava/lang/Object;Lcom/google/api/client/json/CustomizeJsonParser;)V
/*     */     //   514: aload_2
/*     */     //   515: ifnull +10 -> 525
/*     */     //   518: aload_3
/*     */     //   519: iload 15
/*     */     //   521: invokevirtual 250	java/util/ArrayList:remove	(I)Ljava/lang/Object;
/*     */     //   524: pop
/*     */     //   525: aload 12
/*     */     //   527: ifnonnull +6 -> 533
/*     */     //   530: aload 13
/*     */     //   532: areturn
/*     */     //   533: aload 13
/*     */     //   535: checkcast 175	com/google/api/client/json/GenericJson
/*     */     //   538: aload 12
/*     */     //   540: invokevirtual 386	java/lang/reflect/Field:getName	()Ljava/lang/String;
/*     */     //   543: invokevirtual 390	com/google/api/client/json/GenericJson:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   546: astore 16
/*     */     //   548: aload 16
/*     */     //   550: ifnull +7 -> 557
/*     */     //   553: iconst_1
/*     */     //   554: goto +4 -> 558
/*     */     //   557: iconst_0
/*     */     //   558: ldc_w 392
/*     */     //   561: invokestatic 118	com/google/api/client/util/Preconditions:checkArgument	(ZLjava/lang/Object;)V
/*     */     //   564: aload 16
/*     */     //   566: invokevirtual 395	java/lang/Object:toString	()Ljava/lang/String;
/*     */     //   569: astore 17
/*     */     //   571: aload 12
/*     */     //   573: ldc 11
/*     */     //   575: invokevirtual 399	java/lang/reflect/Field:getAnnotation	(Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
/*     */     //   578: checkcast 11	com/google/api/client/json/JsonPolymorphicTypeMap
/*     */     //   581: astore 18
/*     */     //   583: aconst_null
/*     */     //   584: astore 19
/*     */     //   586: aload 18
/*     */     //   588: invokeinterface 403 1 0
/*     */     //   593: astore 20
/*     */     //   595: aload 20
/*     */     //   597: arraylength
/*     */     //   598: istore 21
/*     */     //   600: iconst_0
/*     */     //   601: istore 22
/*     */     //   603: iload 22
/*     */     //   605: iload 21
/*     */     //   607: if_icmpge +43 -> 650
/*     */     //   610: aload 20
/*     */     //   612: iload 22
/*     */     //   614: aaload
/*     */     //   615: astore 23
/*     */     //   617: aload 23
/*     */     //   619: invokeinterface 405 1 0
/*     */     //   624: aload 17
/*     */     //   626: invokevirtual 408	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   629: ifeq +15 -> 644
/*     */     //   632: aload 23
/*     */     //   634: invokeinterface 411 1 0
/*     */     //   639: astore 19
/*     */     //   641: goto +9 -> 650
/*     */     //   644: iinc 22 1
/*     */     //   647: goto -44 -> 603
/*     */     //   650: aload 19
/*     */     //   652: ifnull +7 -> 659
/*     */     //   655: iconst_1
/*     */     //   656: goto +4 -> 660
/*     */     //   659: iconst_0
/*     */     //   660: ldc_w 413
/*     */     //   663: aload 17
/*     */     //   665: invokestatic 417	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   668: dup
/*     */     //   669: invokevirtual 420	java/lang/String:length	()I
/*     */     //   672: ifeq +9 -> 681
/*     */     //   675: invokevirtual 424	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   678: goto +12 -> 690
/*     */     //   681: pop
/*     */     //   682: new 407	java/lang/String
/*     */     //   685: dup_x1
/*     */     //   686: swap
/*     */     //   687: invokespecial 425	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   690: invokestatic 118	com/google/api/client/util/Preconditions:checkArgument	(ZLjava/lang/Object;)V
/*     */     //   693: aload_0
/*     */     //   694: invokevirtual 177	com/google/api/client/json/JsonParser:getFactory	()Lcom/google/api/client/json/JsonFactory;
/*     */     //   697: astore 20
/*     */     //   699: aload 20
/*     */     //   701: aload 20
/*     */     //   703: aload 13
/*     */     //   705: invokevirtual 429	com/google/api/client/json/JsonFactory:toString	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   708: invokevirtual 433	com/google/api/client/json/JsonFactory:createJsonParser	(Ljava/lang/String;)Lcom/google/api/client/json/JsonParser;
/*     */     //   711: astore 21
/*     */     //   713: aload 21
/*     */     //   715: invokespecial 121	com/google/api/client/json/JsonParser:startParsing	()Lcom/google/api/client/json/JsonToken;
/*     */     //   718: pop
/*     */     //   719: aload 21
/*     */     //   721: aload_1
/*     */     //   722: aload 19
/*     */     //   724: aload_3
/*     */     //   725: aconst_null
/*     */     //   726: aconst_null
/*     */     //   727: iconst_0
/*     */     //   728: invokespecial 160	com/google/api/client/json/JsonParser:parseValue	(Ljava/lang/reflect/Field;Ljava/lang/reflect/Type;Ljava/util/ArrayList;Ljava/lang/Object;Lcom/google/api/client/json/CustomizeJsonParser;Z)Ljava/lang/Object;
/*     */     //   731: areturn
/*     */     //   732: aload_2
/*     */     //   733: ifnull +27 -> 760
/*     */     //   736: aload 7
/*     */     //   738: getstatic 438	java/lang/Boolean:TYPE	Ljava/lang/Class;
/*     */     //   741: if_acmpeq +19 -> 760
/*     */     //   744: aload 7
/*     */     //   746: ifnull +18 -> 764
/*     */     //   749: aload 7
/*     */     //   751: ldc_w 435
/*     */     //   754: invokevirtual 195	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
/*     */     //   757: ifeq +7 -> 764
/*     */     //   760: iconst_1
/*     */     //   761: goto +4 -> 765
/*     */     //   764: iconst_0
/*     */     //   765: ldc_w 440
/*     */     //   768: iconst_1
/*     */     //   769: anewarray 4	java/lang/Object
/*     */     //   772: dup
/*     */     //   773: iconst_0
/*     */     //   774: aload_2
/*     */     //   775: aastore
/*     */     //   776: invokestatic 345	com/google/api/client/util/Preconditions:checkArgument	(ZLjava/lang/String;[Ljava/lang/Object;)V
/*     */     //   779: aload 8
/*     */     //   781: getstatic 443	com/google/api/client/json/JsonToken:VALUE_TRUE	Lcom/google/api/client/json/JsonToken;
/*     */     //   784: if_acmpne +9 -> 793
/*     */     //   787: getstatic 447	java/lang/Boolean:TRUE	Ljava/lang/Boolean;
/*     */     //   790: goto +6 -> 796
/*     */     //   793: getstatic 450	java/lang/Boolean:FALSE	Ljava/lang/Boolean;
/*     */     //   796: areturn
/*     */     //   797: aload_1
/*     */     //   798: ifnull +13 -> 811
/*     */     //   801: aload_1
/*     */     //   802: ldc_w 452
/*     */     //   805: invokevirtual 399	java/lang/reflect/Field:getAnnotation	(Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
/*     */     //   808: ifnonnull +7 -> 815
/*     */     //   811: iconst_1
/*     */     //   812: goto +4 -> 816
/*     */     //   815: iconst_0
/*     */     //   816: ldc_w 454
/*     */     //   819: invokestatic 118	com/google/api/client/util/Preconditions:checkArgument	(ZLjava/lang/Object;)V
/*     */     //   822: aload 7
/*     */     //   824: ifnull +14 -> 838
/*     */     //   827: aload 7
/*     */     //   829: ldc_w 456
/*     */     //   832: invokevirtual 195	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
/*     */     //   835: ifeq +8 -> 843
/*     */     //   838: aload_0
/*     */     //   839: invokevirtual 458	com/google/api/client/json/JsonParser:getDecimalValue	()Ljava/math/BigDecimal;
/*     */     //   842: areturn
/*     */     //   843: aload 7
/*     */     //   845: ldc_w 460
/*     */     //   848: if_acmpne +8 -> 856
/*     */     //   851: aload_0
/*     */     //   852: invokevirtual 462	com/google/api/client/json/JsonParser:getBigIntegerValue	()Ljava/math/BigInteger;
/*     */     //   855: areturn
/*     */     //   856: aload 7
/*     */     //   858: ldc_w 464
/*     */     //   861: if_acmpeq +11 -> 872
/*     */     //   864: aload 7
/*     */     //   866: getstatic 465	java/lang/Double:TYPE	Ljava/lang/Class;
/*     */     //   869: if_acmpne +11 -> 880
/*     */     //   872: aload_0
/*     */     //   873: invokevirtual 467	com/google/api/client/json/JsonParser:getDoubleValue	()D
/*     */     //   876: invokestatic 470	java/lang/Double:valueOf	(D)Ljava/lang/Double;
/*     */     //   879: areturn
/*     */     //   880: aload 7
/*     */     //   882: ldc_w 472
/*     */     //   885: if_acmpeq +11 -> 896
/*     */     //   888: aload 7
/*     */     //   890: getstatic 473	java/lang/Long:TYPE	Ljava/lang/Class;
/*     */     //   893: if_acmpne +11 -> 904
/*     */     //   896: aload_0
/*     */     //   897: invokevirtual 475	com/google/api/client/json/JsonParser:getLongValue	()J
/*     */     //   900: invokestatic 478	java/lang/Long:valueOf	(J)Ljava/lang/Long;
/*     */     //   903: areturn
/*     */     //   904: aload 7
/*     */     //   906: ldc_w 480
/*     */     //   909: if_acmpeq +11 -> 920
/*     */     //   912: aload 7
/*     */     //   914: getstatic 481	java/lang/Float:TYPE	Ljava/lang/Class;
/*     */     //   917: if_acmpne +11 -> 928
/*     */     //   920: aload_0
/*     */     //   921: invokevirtual 483	com/google/api/client/json/JsonParser:getFloatValue	()F
/*     */     //   924: invokestatic 486	java/lang/Float:valueOf	(F)Ljava/lang/Float;
/*     */     //   927: areturn
/*     */     //   928: aload 7
/*     */     //   930: ldc_w 488
/*     */     //   933: if_acmpeq +11 -> 944
/*     */     //   936: aload 7
/*     */     //   938: getstatic 489	java/lang/Integer:TYPE	Ljava/lang/Class;
/*     */     //   941: if_acmpne +11 -> 952
/*     */     //   944: aload_0
/*     */     //   945: invokevirtual 491	com/google/api/client/json/JsonParser:getIntValue	()I
/*     */     //   948: invokestatic 494	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   951: areturn
/*     */     //   952: aload 7
/*     */     //   954: ldc_w 496
/*     */     //   957: if_acmpeq +11 -> 968
/*     */     //   960: aload 7
/*     */     //   962: getstatic 497	java/lang/Short:TYPE	Ljava/lang/Class;
/*     */     //   965: if_acmpne +11 -> 976
/*     */     //   968: aload_0
/*     */     //   969: invokevirtual 499	com/google/api/client/json/JsonParser:getShortValue	()S
/*     */     //   972: invokestatic 502	java/lang/Short:valueOf	(S)Ljava/lang/Short;
/*     */     //   975: areturn
/*     */     //   976: aload 7
/*     */     //   978: ldc_w 504
/*     */     //   981: if_acmpeq +11 -> 992
/*     */     //   984: aload 7
/*     */     //   986: getstatic 505	java/lang/Byte:TYPE	Ljava/lang/Class;
/*     */     //   989: if_acmpne +11 -> 1000
/*     */     //   992: aload_0
/*     */     //   993: invokevirtual 507	com/google/api/client/json/JsonParser:getByteValue	()B
/*     */     //   996: invokestatic 510	java/lang/Byte:valueOf	(B)Ljava/lang/Byte;
/*     */     //   999: areturn
/*     */     //   1000: new 228	java/lang/IllegalArgumentException
/*     */     //   1003: dup
/*     */     //   1004: aload_2
/*     */     //   1005: invokestatic 417	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   1008: invokestatic 417	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   1011: astore 22
/*     */     //   1013: new 512	java/lang/StringBuilder
/*     */     //   1016: dup
/*     */     //   1017: bipush 30
/*     */     //   1019: aload 22
/*     */     //   1021: invokevirtual 420	java/lang/String:length	()I
/*     */     //   1024: iadd
/*     */     //   1025: invokespecial 515	java/lang/StringBuilder:<init>	(I)V
/*     */     //   1028: ldc_w 517
/*     */     //   1031: invokevirtual 521	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1034: aload 22
/*     */     //   1036: invokevirtual 521	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1039: invokevirtual 522	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   1042: invokespecial 232	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
/*     */     //   1045: athrow
/*     */     //   1046: aload 7
/*     */     //   1048: ifnull +28 -> 1076
/*     */     //   1051: ldc_w 524
/*     */     //   1054: aload 7
/*     */     //   1056: invokevirtual 195	java/lang/Class:isAssignableFrom	(Ljava/lang/Class;)Z
/*     */     //   1059: ifeq +17 -> 1076
/*     */     //   1062: aload_1
/*     */     //   1063: ifnull +17 -> 1080
/*     */     //   1066: aload_1
/*     */     //   1067: ldc_w 452
/*     */     //   1070: invokevirtual 399	java/lang/reflect/Field:getAnnotation	(Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
/*     */     //   1073: ifnull +7 -> 1080
/*     */     //   1076: iconst_1
/*     */     //   1077: goto +4 -> 1081
/*     */     //   1080: iconst_0
/*     */     //   1081: ldc_w 526
/*     */     //   1084: invokestatic 118	com/google/api/client/util/Preconditions:checkArgument	(ZLjava/lang/Object;)V
/*     */     //   1087: aload_2
/*     */     //   1088: aload_0
/*     */     //   1089: invokevirtual 92	com/google/api/client/json/JsonParser:getText	()Ljava/lang/String;
/*     */     //   1092: invokestatic 530	com/google/api/client/util/Data:parsePrimitiveValue	(Ljava/lang/reflect/Type;Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   1095: areturn
/*     */     //   1096: aload 7
/*     */     //   1098: ifnull +11 -> 1109
/*     */     //   1101: aload 7
/*     */     //   1103: invokevirtual 531	java/lang/Class:isPrimitive	()Z
/*     */     //   1106: ifne +7 -> 1113
/*     */     //   1109: iconst_1
/*     */     //   1110: goto +4 -> 1114
/*     */     //   1113: iconst_0
/*     */     //   1114: ldc_w 533
/*     */     //   1117: invokestatic 118	com/google/api/client/util/Preconditions:checkArgument	(ZLjava/lang/Object;)V
/*     */     //   1120: aload 7
/*     */     //   1122: ifnull +60 -> 1182
/*     */     //   1125: iconst_0
/*     */     //   1126: aload 7
/*     */     //   1128: invokevirtual 536	java/lang/Class:getModifiers	()I
/*     */     //   1131: sipush 1536
/*     */     //   1134: iand
/*     */     //   1135: if_icmpeq +47 -> 1182
/*     */     //   1138: aload 7
/*     */     //   1140: ldc_w 312
/*     */     //   1143: invokestatic 340	com/google/api/client/util/Types:isAssignableToOrFrom	(Ljava/lang/Class;Ljava/lang/Class;)Z
/*     */     //   1146: ifeq +14 -> 1160
/*     */     //   1149: aload_2
/*     */     //   1150: invokestatic 303	com/google/api/client/util/Data:newCollectionInstance	(Ljava/lang/reflect/Type;)Ljava/util/Collection;
/*     */     //   1153: invokevirtual 164	java/lang/Object:getClass	()Ljava/lang/Class;
/*     */     //   1156: invokestatic 539	com/google/api/client/util/Data:nullOf	(Ljava/lang/Class;)Ljava/lang/Object;
/*     */     //   1159: areturn
/*     */     //   1160: aload 7
/*     */     //   1162: ldc -59
/*     */     //   1164: invokestatic 340	com/google/api/client/util/Types:isAssignableToOrFrom	(Ljava/lang/Class;Ljava/lang/Class;)Z
/*     */     //   1167: ifeq +15 -> 1182
/*     */     //   1170: aload 7
/*     */     //   1172: invokestatic 380	com/google/api/client/util/Data:newMapInstance	(Ljava/lang/Class;)Ljava/util/Map;
/*     */     //   1175: invokevirtual 164	java/lang/Object:getClass	()Ljava/lang/Class;
/*     */     //   1178: invokestatic 539	com/google/api/client/util/Data:nullOf	(Ljava/lang/Class;)Ljava/lang/Object;
/*     */     //   1181: areturn
/*     */     //   1182: aload_3
/*     */     //   1183: aload_2
/*     */     //   1184: invokestatic 361	com/google/api/client/util/Types:getRawArrayComponentType	(Ljava/util/List;Ljava/lang/reflect/Type;)Ljava/lang/Class;
/*     */     //   1187: invokestatic 539	com/google/api/client/util/Data:nullOf	(Ljava/lang/Class;)Ljava/lang/Object;
/*     */     //   1190: areturn
/*     */     //   1191: new 228	java/lang/IllegalArgumentException
/*     */     //   1194: dup
/*     */     //   1195: aload 8
/*     */     //   1197: invokestatic 417	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   1200: invokestatic 417	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   1203: astore 23
/*     */     //   1205: new 512	java/lang/StringBuilder
/*     */     //   1208: dup
/*     */     //   1209: bipush 27
/*     */     //   1211: aload 23
/*     */     //   1213: invokevirtual 420	java/lang/String:length	()I
/*     */     //   1216: iadd
/*     */     //   1217: invokespecial 515	java/lang/StringBuilder:<init>	(I)V
/*     */     //   1220: ldc_w 541
/*     */     //   1223: invokevirtual 521	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1226: aload 23
/*     */     //   1228: invokevirtual 521	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1231: invokevirtual 522	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   1234: invokespecial 232	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
/*     */     //   1237: athrow
/*     */     //   1238: astore 9
/*     */     //   1240: new 512	java/lang/StringBuilder
/*     */     //   1243: dup
/*     */     //   1244: invokespecial 542	java/lang/StringBuilder:<init>	()V
/*     */     //   1247: astore 10
/*     */     //   1249: aload_0
/*     */     //   1250: invokevirtual 544	com/google/api/client/json/JsonParser:getCurrentName	()Ljava/lang/String;
/*     */     //   1253: astore 11
/*     */     //   1255: aload 11
/*     */     //   1257: ifnull +17 -> 1274
/*     */     //   1260: aload 10
/*     */     //   1262: ldc_w 546
/*     */     //   1265: invokevirtual 521	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1268: aload 11
/*     */     //   1270: invokevirtual 521	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1273: pop
/*     */     //   1274: aload_1
/*     */     //   1275: ifnull +30 -> 1305
/*     */     //   1278: aload 11
/*     */     //   1280: ifnull +12 -> 1292
/*     */     //   1283: aload 10
/*     */     //   1285: ldc_w 548
/*     */     //   1288: invokevirtual 521	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1291: pop
/*     */     //   1292: aload 10
/*     */     //   1294: ldc_w 550
/*     */     //   1297: invokevirtual 521	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   1300: aload_1
/*     */     //   1301: invokevirtual 553	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   1304: pop
/*     */     //   1305: new 228	java/lang/IllegalArgumentException
/*     */     //   1308: dup
/*     */     //   1309: aload 10
/*     */     //   1311: invokevirtual 522	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   1314: aload 9
/*     */     //   1316: invokespecial 556	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   1319: athrow
/*     */     // Line number table:
/*     */     //   Java source line #704	-> byte code offset #0
/*     */     //   Java source line #706	-> byte code offset #6
/*     */     //   Java source line #707	-> byte code offset #23
/*     */     //   Java source line #708	-> byte code offset #30
/*     */     //   Java source line #711	-> byte code offset #39
/*     */     //   Java source line #712	-> byte code offset #46
/*     */     //   Java source line #713	-> byte code offset #51
/*     */     //   Java source line #716	-> byte code offset #53
/*     */     //   Java source line #718	-> byte code offset #59
/*     */     //   Java source line #721	-> byte code offset #128
/*     */     //   Java source line #722	-> byte code offset #134
/*     */     //   Java source line #725	-> byte code offset #178
/*     */     //   Java source line #726	-> byte code offset #181
/*     */     //   Java source line #727	-> byte code offset #190
/*     */     //   Java source line #729	-> byte code offset #200
/*     */     //   Java source line #730	-> byte code offset #205
/*     */     //   Java source line #732	-> byte code offset #211
/*     */     //   Java source line #733	-> byte code offset #214
/*     */     //   Java source line #734	-> byte code offset #219
/*     */     //   Java source line #735	-> byte code offset #228
/*     */     //   Java source line #736	-> byte code offset #244
/*     */     //   Java source line #738	-> byte code offset #250
/*     */     //   Java source line #739	-> byte code offset #258
/*     */     //   Java source line #740	-> byte code offset #270
/*     */     //   Java source line #741	-> byte code offset #275
/*     */     //   Java source line #743	-> byte code offset #287
/*     */     //   Java source line #747	-> byte code offset #290
/*     */     //   Java source line #750	-> byte code offset #316
/*     */     //   Java source line #751	-> byte code offset #332
/*     */     //   Java source line #752	-> byte code offset #335
/*     */     //   Java source line #753	-> byte code offset #345
/*     */     //   Java source line #755	-> byte code offset #356
/*     */     //   Java source line #756	-> byte code offset #378
/*     */     //   Java source line #757	-> byte code offset #383
/*     */     //   Java source line #758	-> byte code offset #395
/*     */     //   Java source line #760	-> byte code offset #400
/*     */     //   Java source line #761	-> byte code offset #410
/*     */     //   Java source line #763	-> byte code offset #420
/*     */     //   Java source line #766	-> byte code offset #427
/*     */     //   Java source line #767	-> byte code offset #433
/*     */     //   Java source line #768	-> byte code offset #437
/*     */     //   Java source line #770	-> byte code offset #443
/*     */     //   Java source line #771	-> byte code offset #458
/*     */     //   Java source line #773	-> byte code offset #478
/*     */     //   Java source line #775	-> byte code offset #483
/*     */     //   Java source line #776	-> byte code offset #490
/*     */     //   Java source line #777	-> byte code offset #502
/*     */     //   Java source line #780	-> byte code offset #505
/*     */     //   Java source line #781	-> byte code offset #514
/*     */     //   Java source line #782	-> byte code offset #518
/*     */     //   Java source line #784	-> byte code offset #525
/*     */     //   Java source line #785	-> byte code offset #530
/*     */     //   Java source line #789	-> byte code offset #533
/*     */     //   Java source line #790	-> byte code offset #548
/*     */     //   Java source line #792	-> byte code offset #564
/*     */     //   Java source line #793	-> byte code offset #571
/*     */     //   Java source line #794	-> byte code offset #583
/*     */     //   Java source line #795	-> byte code offset #586
/*     */     //   Java source line #796	-> byte code offset #617
/*     */     //   Java source line #797	-> byte code offset #632
/*     */     //   Java source line #798	-> byte code offset #641
/*     */     //   Java source line #795	-> byte code offset #644
/*     */     //   Java source line #801	-> byte code offset #650
/*     */     //   Java source line #803	-> byte code offset #693
/*     */     //   Java source line #805	-> byte code offset #699
/*     */     //   Java source line #806	-> byte code offset #713
/*     */     //   Java source line #807	-> byte code offset #719
/*     */     //   Java source line #810	-> byte code offset #732
/*     */     //   Java source line #813	-> byte code offset #779
/*     */     //   Java source line #816	-> byte code offset #797
/*     */     //   Java source line #819	-> byte code offset #822
/*     */     //   Java source line #820	-> byte code offset #838
/*     */     //   Java source line #822	-> byte code offset #843
/*     */     //   Java source line #823	-> byte code offset #851
/*     */     //   Java source line #825	-> byte code offset #856
/*     */     //   Java source line #826	-> byte code offset #872
/*     */     //   Java source line #828	-> byte code offset #880
/*     */     //   Java source line #829	-> byte code offset #896
/*     */     //   Java source line #831	-> byte code offset #904
/*     */     //   Java source line #832	-> byte code offset #920
/*     */     //   Java source line #834	-> byte code offset #928
/*     */     //   Java source line #835	-> byte code offset #944
/*     */     //   Java source line #837	-> byte code offset #952
/*     */     //   Java source line #838	-> byte code offset #968
/*     */     //   Java source line #840	-> byte code offset #976
/*     */     //   Java source line #841	-> byte code offset #992
/*     */     //   Java source line #843	-> byte code offset #1000
/*     */     //   Java source line #845	-> byte code offset #1046
/*     */     //   Java source line #850	-> byte code offset #1087
/*     */     //   Java source line #852	-> byte code offset #1096
/*     */     //   Java source line #854	-> byte code offset #1120
/*     */     //   Java source line #856	-> byte code offset #1138
/*     */     //   Java source line #857	-> byte code offset #1149
/*     */     //   Java source line #859	-> byte code offset #1160
/*     */     //   Java source line #860	-> byte code offset #1170
/*     */     //   Java source line #863	-> byte code offset #1182
/*     */     //   Java source line #865	-> byte code offset #1191
/*     */     //   Java source line #867	-> byte code offset #1238
/*     */     //   Java source line #869	-> byte code offset #1240
/*     */     //   Java source line #870	-> byte code offset #1249
/*     */     //   Java source line #871	-> byte code offset #1255
/*     */     //   Java source line #872	-> byte code offset #1260
/*     */     //   Java source line #874	-> byte code offset #1274
/*     */     //   Java source line #875	-> byte code offset #1278
/*     */     //   Java source line #876	-> byte code offset #1283
/*     */     //   Java source line #878	-> byte code offset #1292
/*     */     //   Java source line #880	-> byte code offset #1305
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	1320	0	this	JsonParser
/*     */     //   0	1320	1	fieldContext	Field
/*     */     //   0	1320	2	valueType	Type
/*     */     //   0	1320	3	context	ArrayList<Type>
/*     */     //   0	1320	4	destination	Object
/*     */     //   0	1320	5	customizeParser	CustomizeJsonParser
/*     */     //   0	1320	6	handlePolymorphic	boolean
/*     */     //   23	1297	7	valueClass	Class<?>
/*     */     //   59	1261	8	token	JsonToken
/*     */     //   134	1104	9	isArray	boolean
/*     */     //   1240	80	9	e	IllegalArgumentException
/*     */     //   181	1057	10	collectionValue	Collection<Object>
/*     */     //   1249	71	10	contextStringBuilder	StringBuilder
/*     */     //   214	1024	11	subType	Type
/*     */     //   1255	65	11	currentName	String
/*     */     //   332	906	12	typemapField	Field
/*     */     //   335	903	13	newInstance	Object
/*     */     //   378	860	14	isMap	boolean
/*     */     //   433	805	15	contextSize	int
/*     */     //   478	27	16	subValueType	Type
/*     */     //   548	690	16	typeValueObject	Object
/*     */     //   490	15	17	destinationMap	Map<String, Object>
/*     */     //   571	667	17	typeValue	String
/*     */     //   583	655	18	typeMap	JsonPolymorphicTypeMap
/*     */     //   586	652	19	typeClass	Class<?>
/*     */     //   595	55	20	arr$	JsonPolymorphicTypeMap.TypeDef[]
/*     */     //   699	539	20	factory	JsonFactory
/*     */     //   600	50	21	len$	int
/*     */     //   713	525	21	parser	JsonParser
/*     */     //   603	47	22	i$	int
/*     */     //   617	27	23	typeDefinition	JsonPolymorphicTypeMap.TypeDef
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   59	286	1238	java/lang/IllegalArgumentException
/*     */     //   287	289	1238	java/lang/IllegalArgumentException
/*     */     //   290	504	1238	java/lang/IllegalArgumentException
/*     */     //   505	532	1238	java/lang/IllegalArgumentException
/*     */     //   533	731	1238	java/lang/IllegalArgumentException
/*     */     //   732	796	1238	java/lang/IllegalArgumentException
/*     */     //   797	842	1238	java/lang/IllegalArgumentException
/*     */     //   843	855	1238	java/lang/IllegalArgumentException
/*     */     //   856	879	1238	java/lang/IllegalArgumentException
/*     */     //   880	903	1238	java/lang/IllegalArgumentException
/*     */     //   904	927	1238	java/lang/IllegalArgumentException
/*     */     //   928	951	1238	java/lang/IllegalArgumentException
/*     */     //   952	975	1238	java/lang/IllegalArgumentException
/*     */     //   976	999	1238	java/lang/IllegalArgumentException
/*     */     //   1000	1095	1238	java/lang/IllegalArgumentException
/*     */     //   1096	1159	1238	java/lang/IllegalArgumentException
/*     */     //   1160	1181	1238	java/lang/IllegalArgumentException
/*     */     //   1182	1190	1238	java/lang/IllegalArgumentException
/*     */     //   1191	1238	1238	java/lang/IllegalArgumentException
/*     */   }
/*     */   
/*     */   private static Field getCachedTypemapFieldFor(Class<?> key)
/*     */   {
/* 897 */     if (key == null) {
/* 898 */       return null;
/*     */     }
/* 900 */     lock.lock();
/*     */     
/*     */     try
/*     */     {
/* 904 */       if (cachedTypemapFields.containsKey(key)) {
/* 905 */         return (Field)cachedTypemapFields.get(key);
/*     */       }
/*     */       
/* 908 */       Field value = null;
/* 909 */       Collection<FieldInfo> fieldInfos = ClassInfo.of(key).getFieldInfos();
/* 910 */       for (FieldInfo fieldInfo : fieldInfos) {
/* 911 */         Field field = fieldInfo.getField();
/* 912 */         JsonPolymorphicTypeMap typemapAnnotation = (JsonPolymorphicTypeMap)field.getAnnotation(JsonPolymorphicTypeMap.class);
/*     */         
/* 914 */         if (typemapAnnotation != null) {
/* 915 */           Preconditions.checkArgument(value == null, "Class contains more than one field with @JsonPolymorphicTypeMap annotation: %s", new Object[] { key });
/*     */           
/*     */ 
/* 918 */           Preconditions.checkArgument(Data.isPrimitive(field.getType()), "Field which has the @JsonPolymorphicTypeMap, %s, is not a supported type: %s", new Object[] { key, field.getType() });
/*     */           
/*     */ 
/* 921 */           value = field;
/*     */           
/* 923 */           JsonPolymorphicTypeMap.TypeDef[] typeDefs = typemapAnnotation.typeDefinitions();
/* 924 */           HashSet<String> typeDefKeys = Sets.newHashSet();
/* 925 */           Preconditions.checkArgument(typeDefs.length > 0, "@JsonPolymorphicTypeMap must have at least one @TypeDef");
/*     */           
/* 927 */           for (JsonPolymorphicTypeMap.TypeDef typeDef : typeDefs) {
/* 928 */             Preconditions.checkArgument(typeDefKeys.add(typeDef.key()), "Class contains two @TypeDef annotations with identical key: %s", new Object[] { typeDef.key() });
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 933 */       cachedTypemapFields.put(key, value);
/* 934 */       return value;
/*     */     } finally {
/* 936 */       lock.unlock();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\json\JsonParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */