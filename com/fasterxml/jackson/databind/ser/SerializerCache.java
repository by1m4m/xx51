/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ReadOnlyClassToSerializerMap;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SerializerCache
/*     */ {
/*  31 */   private HashMap<TypeKey, JsonSerializer<Object>> _sharedMap = new HashMap(64);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  36 */   private volatile ReadOnlyClassToSerializerMap _readOnlyMap = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReadOnlyClassToSerializerMap getReadOnlyLookupMap()
/*     */   {
/*  46 */     ReadOnlyClassToSerializerMap m = this._readOnlyMap;
/*  47 */     if (m == null) {
/*  48 */       synchronized (this) {
/*  49 */         m = this._readOnlyMap;
/*  50 */         if (m == null) {
/*  51 */           this._readOnlyMap = (m = ReadOnlyClassToSerializerMap.from(this._sharedMap));
/*     */         }
/*     */       }
/*     */     }
/*  55 */     return m.instance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized int size()
/*     */   {
/*  65 */     return this._sharedMap.size();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public JsonSerializer<Object> untypedValueSerializer(Class<?> type)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 4	com/fasterxml/jackson/databind/ser/SerializerCache:_sharedMap	Ljava/util/HashMap;
/*     */     //   8: new 9	com/fasterxml/jackson/databind/ser/SerializerCache$TypeKey
/*     */     //   11: dup
/*     */     //   12: aload_1
/*     */     //   13: iconst_0
/*     */     //   14: invokespecial 10	com/fasterxml/jackson/databind/ser/SerializerCache$TypeKey:<init>	(Ljava/lang/Class;Z)V
/*     */     //   17: invokevirtual 11	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   20: checkcast 12	com/fasterxml/jackson/databind/JsonSerializer
/*     */     //   23: aload_2
/*     */     //   24: monitorexit
/*     */     //   25: areturn
/*     */     //   26: astore_3
/*     */     //   27: aload_2
/*     */     //   28: monitorexit
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     // Line number table:
/*     */     //   Java source line #74	-> byte code offset #0
/*     */     //   Java source line #75	-> byte code offset #4
/*     */     //   Java source line #76	-> byte code offset #26
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	SerializerCache
/*     */     //   0	31	1	type	Class<?>
/*     */     //   2	26	2	Ljava/lang/Object;	Object
/*     */     //   26	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	25	26	finally
/*     */     //   26	29	26	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public JsonSerializer<Object> untypedValueSerializer(JavaType type)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 4	com/fasterxml/jackson/databind/ser/SerializerCache:_sharedMap	Ljava/util/HashMap;
/*     */     //   8: new 9	com/fasterxml/jackson/databind/ser/SerializerCache$TypeKey
/*     */     //   11: dup
/*     */     //   12: aload_1
/*     */     //   13: iconst_0
/*     */     //   14: invokespecial 13	com/fasterxml/jackson/databind/ser/SerializerCache$TypeKey:<init>	(Lcom/fasterxml/jackson/databind/JavaType;Z)V
/*     */     //   17: invokevirtual 11	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   20: checkcast 12	com/fasterxml/jackson/databind/JsonSerializer
/*     */     //   23: aload_2
/*     */     //   24: monitorexit
/*     */     //   25: areturn
/*     */     //   26: astore_3
/*     */     //   27: aload_2
/*     */     //   28: monitorexit
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     // Line number table:
/*     */     //   Java source line #81	-> byte code offset #0
/*     */     //   Java source line #82	-> byte code offset #4
/*     */     //   Java source line #83	-> byte code offset #26
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	SerializerCache
/*     */     //   0	31	1	type	JavaType
/*     */     //   2	26	2	Ljava/lang/Object;	Object
/*     */     //   26	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	25	26	finally
/*     */     //   26	29	26	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public JsonSerializer<Object> typedValueSerializer(JavaType type)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 4	com/fasterxml/jackson/databind/ser/SerializerCache:_sharedMap	Ljava/util/HashMap;
/*     */     //   8: new 9	com/fasterxml/jackson/databind/ser/SerializerCache$TypeKey
/*     */     //   11: dup
/*     */     //   12: aload_1
/*     */     //   13: iconst_1
/*     */     //   14: invokespecial 13	com/fasterxml/jackson/databind/ser/SerializerCache$TypeKey:<init>	(Lcom/fasterxml/jackson/databind/JavaType;Z)V
/*     */     //   17: invokevirtual 11	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   20: checkcast 12	com/fasterxml/jackson/databind/JsonSerializer
/*     */     //   23: aload_2
/*     */     //   24: monitorexit
/*     */     //   25: areturn
/*     */     //   26: astore_3
/*     */     //   27: aload_2
/*     */     //   28: monitorexit
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     // Line number table:
/*     */     //   Java source line #88	-> byte code offset #0
/*     */     //   Java source line #89	-> byte code offset #4
/*     */     //   Java source line #90	-> byte code offset #26
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	SerializerCache
/*     */     //   0	31	1	type	JavaType
/*     */     //   2	26	2	Ljava/lang/Object;	Object
/*     */     //   26	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	25	26	finally
/*     */     //   26	29	26	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public JsonSerializer<Object> typedValueSerializer(Class<?> cls)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_2
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 4	com/fasterxml/jackson/databind/ser/SerializerCache:_sharedMap	Ljava/util/HashMap;
/*     */     //   8: new 9	com/fasterxml/jackson/databind/ser/SerializerCache$TypeKey
/*     */     //   11: dup
/*     */     //   12: aload_1
/*     */     //   13: iconst_1
/*     */     //   14: invokespecial 10	com/fasterxml/jackson/databind/ser/SerializerCache$TypeKey:<init>	(Ljava/lang/Class;Z)V
/*     */     //   17: invokevirtual 11	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   20: checkcast 12	com/fasterxml/jackson/databind/JsonSerializer
/*     */     //   23: aload_2
/*     */     //   24: monitorexit
/*     */     //   25: areturn
/*     */     //   26: astore_3
/*     */     //   27: aload_2
/*     */     //   28: monitorexit
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     // Line number table:
/*     */     //   Java source line #95	-> byte code offset #0
/*     */     //   Java source line #96	-> byte code offset #4
/*     */     //   Java source line #97	-> byte code offset #26
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	SerializerCache
/*     */     //   0	31	1	cls	Class<?>
/*     */     //   2	26	2	Ljava/lang/Object;	Object
/*     */     //   26	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	25	26	finally
/*     */     //   26	29	26	finally
/*     */   }
/*     */   
/*     */   public void addTypedSerializer(JavaType type, JsonSerializer<Object> ser)
/*     */   {
/* 113 */     synchronized (this) {
/* 114 */       if (this._sharedMap.put(new TypeKey(type, true), ser) == null)
/*     */       {
/* 116 */         this._readOnlyMap = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addTypedSerializer(Class<?> cls, JsonSerializer<Object> ser)
/*     */   {
/* 123 */     synchronized (this) {
/* 124 */       if (this._sharedMap.put(new TypeKey(cls, true), ser) == null)
/*     */       {
/* 126 */         this._readOnlyMap = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addAndResolveNonTypedSerializer(Class<?> type, JsonSerializer<Object> ser, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 135 */     synchronized (this) {
/* 136 */       if (this._sharedMap.put(new TypeKey(type, false), ser) == null)
/*     */       {
/* 138 */         this._readOnlyMap = null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 147 */       if ((ser instanceof ResolvableSerializer)) {
/* 148 */         ((ResolvableSerializer)ser).resolve(provider);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addAndResolveNonTypedSerializer(JavaType type, JsonSerializer<Object> ser, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 157 */     synchronized (this) {
/* 158 */       if (this._sharedMap.put(new TypeKey(type, false), ser) == null)
/*     */       {
/* 160 */         this._readOnlyMap = null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 169 */       if ((ser instanceof ResolvableSerializer)) {
/* 170 */         ((ResolvableSerializer)ser).resolve(provider);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void flush()
/*     */   {
/* 180 */     this._sharedMap.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class TypeKey
/*     */   {
/*     */     protected int _hashCode;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Class<?> _class;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected JavaType _type;
/*     */     
/*     */ 
/*     */ 
/*     */     protected boolean _isTyped;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public TypeKey(Class<?> key, boolean typed)
/*     */     {
/* 210 */       this._class = key;
/* 211 */       this._type = null;
/* 212 */       this._isTyped = typed;
/* 213 */       this._hashCode = hash(key, typed);
/*     */     }
/*     */     
/*     */     public TypeKey(JavaType key, boolean typed) {
/* 217 */       this._type = key;
/* 218 */       this._class = null;
/* 219 */       this._isTyped = typed;
/* 220 */       this._hashCode = hash(key, typed);
/*     */     }
/*     */     
/*     */     private static final int hash(Class<?> cls, boolean typed) {
/* 224 */       int hash = cls.getName().hashCode();
/* 225 */       if (typed) {
/* 226 */         hash++;
/*     */       }
/* 228 */       return hash;
/*     */     }
/*     */     
/*     */     private static final int hash(JavaType type, boolean typed) {
/* 232 */       int hash = type.hashCode() - 1;
/* 233 */       if (typed) {
/* 234 */         hash--;
/*     */       }
/* 236 */       return hash;
/*     */     }
/*     */     
/*     */     public void resetTyped(Class<?> cls) {
/* 240 */       this._type = null;
/* 241 */       this._class = cls;
/* 242 */       this._isTyped = true;
/* 243 */       this._hashCode = hash(cls, true);
/*     */     }
/*     */     
/*     */     public void resetUntyped(Class<?> cls) {
/* 247 */       this._type = null;
/* 248 */       this._class = cls;
/* 249 */       this._isTyped = false;
/* 250 */       this._hashCode = hash(cls, false);
/*     */     }
/*     */     
/*     */     public void resetTyped(JavaType type) {
/* 254 */       this._type = type;
/* 255 */       this._class = null;
/* 256 */       this._isTyped = true;
/* 257 */       this._hashCode = hash(type, true);
/*     */     }
/*     */     
/*     */     public void resetUntyped(JavaType type) {
/* 261 */       this._type = type;
/* 262 */       this._class = null;
/* 263 */       this._isTyped = false;
/* 264 */       this._hashCode = hash(type, false);
/*     */     }
/*     */     
/* 267 */     public final int hashCode() { return this._hashCode; }
/*     */     
/*     */     public final String toString() {
/* 270 */       if (this._class != null) {
/* 271 */         return "{class: " + this._class.getName() + ", typed? " + this._isTyped + "}";
/*     */       }
/* 273 */       return "{type: " + this._type + ", typed? " + this._isTyped + "}";
/*     */     }
/*     */     
/*     */ 
/*     */     public final boolean equals(Object o)
/*     */     {
/* 279 */       if (o == null) return false;
/* 280 */       if (o == this) return true;
/* 281 */       if (o.getClass() != getClass()) {
/* 282 */         return false;
/*     */       }
/* 284 */       TypeKey other = (TypeKey)o;
/* 285 */       if (other._isTyped == this._isTyped) {
/* 286 */         if (this._class != null) {
/* 287 */           return other._class == this._class;
/*     */         }
/* 289 */         return this._type.equals(other._type);
/*     */       }
/* 291 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\SerializerCache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */