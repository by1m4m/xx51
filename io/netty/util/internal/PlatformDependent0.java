/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ 
/*     */ final class PlatformDependent0
/*     */ {
/*     */   private static final InternalLogger logger;
/*     */   private static final long ADDRESS_FIELD_OFFSET;
/*     */   private static final long BYTE_ARRAY_BASE_OFFSET;
/*     */   private static final Constructor<?> DIRECT_BUFFER_CONSTRUCTOR;
/*     */   private static final Throwable EXPLICIT_NO_UNSAFE_CAUSE;
/*     */   private static final Method ALLOCATE_ARRAY_METHOD;
/*     */   private static final int JAVA_VERSION;
/*     */   private static final boolean IS_ANDROID;
/*     */   private static final Throwable UNSAFE_UNAVAILABILITY_CAUSE;
/*     */   private static final Object INTERNAL_UNSAFE;
/*     */   private static final boolean IS_EXPLICIT_TRY_REFLECTION_SET_ACCESSIBLE;
/*     */   static final Unsafe UNSAFE;
/*     */   static final int HASH_CODE_ASCII_SEED = -1028477387;
/*     */   static final int HASH_CODE_C1 = -862048943;
/*     */   static final int HASH_CODE_C2 = 461845907;
/*     */   private static final long UNSAFE_COPY_THRESHOLD = 1048576L;
/*     */   private static final boolean UNALIGNED;
/*     */   
/*     */   static
/*     */   {
/*  38 */     logger = InternalLoggerFactory.getInstance(PlatformDependent0.class);
/*     */     
/*     */ 
/*     */ 
/*  42 */     EXPLICIT_NO_UNSAFE_CAUSE = explicitNoUnsafeCause0();
/*     */     
/*  44 */     JAVA_VERSION = javaVersion0();
/*  45 */     IS_ANDROID = isAndroid0();
/*     */     
/*     */ 
/*     */ 
/*  49 */     IS_EXPLICIT_TRY_REFLECTION_SET_ACCESSIBLE = explicitTryReflectionSetAccessible0();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */     Field addressField = null;
/*  69 */     Method allocateArrayMethod = null;
/*  70 */     Throwable unsafeUnavailabilityCause = null;
/*     */     
/*  72 */     Object internalUnsafe = null;
/*     */     final ByteBuffer direct;
/*  74 */     Unsafe unsafe; if ((unsafeUnavailabilityCause = EXPLICIT_NO_UNSAFE_CAUSE) != null) {
/*  75 */       ByteBuffer direct = null;
/*  76 */       addressField = null;
/*  77 */       Unsafe unsafe = null;
/*  78 */       internalUnsafe = null;
/*     */     } else {
/*  80 */       direct = ByteBuffer.allocateDirect(1);
/*     */       
/*     */ 
/*  83 */       Object maybeUnsafe = AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run() {
/*     */           try {
/*  87 */             Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
/*     */             
/*     */ 
/*  90 */             Throwable cause = ReflectionUtil.trySetAccessible(unsafeField, false);
/*  91 */             if (cause != null) {
/*  92 */               return cause;
/*     */             }
/*     */             
/*  95 */             return unsafeField.get(null);
/*     */           } catch (NoSuchFieldException e) {
/*  97 */             return e;
/*     */           } catch (SecurityException e) {
/*  99 */             return e;
/*     */           } catch (IllegalAccessException e) {
/* 101 */             return e;
/*     */           }
/*     */           catch (NoClassDefFoundError e)
/*     */           {
/* 105 */             return e;
/*     */           }
/*     */         }
/*     */       });
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */       if ((maybeUnsafe instanceof Throwable)) {
/* 115 */         Unsafe unsafe = null;
/* 116 */         unsafeUnavailabilityCause = (Throwable)maybeUnsafe;
/* 117 */         logger.debug("sun.misc.Unsafe.theUnsafe: unavailable", (Throwable)maybeUnsafe);
/*     */       } else {
/* 119 */         unsafe = (Unsafe)maybeUnsafe;
/* 120 */         logger.debug("sun.misc.Unsafe.theUnsafe: available");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 126 */       if (unsafe != null) {
/* 127 */         Unsafe finalUnsafe = unsafe;
/* 128 */         Object maybeException = AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/*     */             try {
/* 132 */               this.val$finalUnsafe.getClass().getDeclaredMethod("copyMemory", new Class[] { Object.class, Long.TYPE, Object.class, Long.TYPE, Long.TYPE });
/*     */               
/* 134 */               return null;
/*     */             } catch (NoSuchMethodException e) {
/* 136 */               return e;
/*     */             } catch (SecurityException e) {
/* 138 */               return e;
/*     */             }
/*     */           }
/*     */         });
/*     */         
/* 143 */         if (maybeException == null) {
/* 144 */           logger.debug("sun.misc.Unsafe.copyMemory: available");
/*     */         }
/*     */         else {
/* 147 */           unsafe = null;
/* 148 */           unsafeUnavailabilityCause = (Throwable)maybeException;
/* 149 */           logger.debug("sun.misc.Unsafe.copyMemory: unavailable", (Throwable)maybeException);
/*     */         }
/*     */       }
/*     */       
/* 153 */       if (unsafe != null) {
/* 154 */         Unsafe finalUnsafe = unsafe;
/*     */         
/*     */ 
/* 157 */         Object maybeAddressField = AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/*     */             try {
/* 161 */               Field field = Buffer.class.getDeclaredField("address");
/*     */               
/*     */ 
/* 164 */               long offset = this.val$finalUnsafe.objectFieldOffset(field);
/* 165 */               long address = this.val$finalUnsafe.getLong(direct, offset);
/*     */               
/*     */ 
/* 168 */               if (address == 0L) {
/* 169 */                 return null;
/*     */               }
/* 171 */               return field;
/*     */             } catch (NoSuchFieldException e) {
/* 173 */               return e;
/*     */             } catch (SecurityException e) {
/* 175 */               return e;
/*     */             }
/*     */           }
/*     */         });
/*     */         
/* 180 */         if ((maybeAddressField instanceof Field)) {
/* 181 */           addressField = (Field)maybeAddressField;
/* 182 */           logger.debug("java.nio.Buffer.address: available");
/*     */         } else {
/* 184 */           unsafeUnavailabilityCause = (Throwable)maybeAddressField;
/* 185 */           logger.debug("java.nio.Buffer.address: unavailable", (Throwable)maybeAddressField);
/*     */           
/*     */ 
/*     */ 
/* 189 */           unsafe = null;
/*     */         }
/*     */       }
/*     */       
/* 193 */       if (unsafe != null)
/*     */       {
/*     */ 
/* 196 */         long byteArrayIndexScale = unsafe.arrayIndexScale(byte[].class);
/* 197 */         if (byteArrayIndexScale != 1L) {
/* 198 */           logger.debug("unsafe.arrayIndexScale is {} (expected: 1). Not using unsafe.", Long.valueOf(byteArrayIndexScale));
/* 199 */           unsafeUnavailabilityCause = new UnsupportedOperationException("Unexpected unsafe.arrayIndexScale");
/* 200 */           unsafe = null;
/*     */         }
/*     */       }
/*     */     }
/* 204 */     UNSAFE_UNAVAILABILITY_CAUSE = unsafeUnavailabilityCause;
/* 205 */     UNSAFE = unsafe;
/*     */     
/* 207 */     if (unsafe == null) {
/* 208 */       ADDRESS_FIELD_OFFSET = -1L;
/* 209 */       BYTE_ARRAY_BASE_OFFSET = -1L;
/* 210 */       UNALIGNED = false;
/* 211 */       DIRECT_BUFFER_CONSTRUCTOR = null;
/* 212 */       ALLOCATE_ARRAY_METHOD = null;
/*     */     }
/*     */     else {
/* 215 */       long address = -1L;
/*     */       try
/*     */       {
/* 218 */         Object maybeDirectBufferConstructor = AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run()
/*     */           {
/*     */             try {
/* 223 */               Constructor<?> constructor = this.val$direct.getClass().getDeclaredConstructor(new Class[] { Long.TYPE, Integer.TYPE });
/* 224 */               Throwable cause = ReflectionUtil.trySetAccessible(constructor, true);
/* 225 */               if (cause != null) {
/* 226 */                 return cause;
/*     */               }
/* 228 */               return constructor;
/*     */             } catch (NoSuchMethodException e) {
/* 230 */               return e;
/*     */             } catch (SecurityException e) {
/* 232 */               return e;
/*     */             }
/*     */           }
/*     */         });
/*     */         
/* 237 */         if ((maybeDirectBufferConstructor instanceof Constructor)) {
/* 238 */           address = UNSAFE.allocateMemory(1L);
/*     */           Constructor<?> directBufferConstructor;
/*     */           try {
/* 241 */             ((Constructor)maybeDirectBufferConstructor).newInstance(new Object[] { Long.valueOf(address), Integer.valueOf(1) });
/* 242 */             Constructor<?> directBufferConstructor = (Constructor)maybeDirectBufferConstructor;
/* 243 */             logger.debug("direct buffer constructor: available");
/*     */           } catch (InstantiationException e) {
/* 245 */             directBufferConstructor = null;
/*     */           } catch (IllegalAccessException e) { Constructor<?> directBufferConstructor;
/* 247 */             directBufferConstructor = null;
/*     */           } catch (InvocationTargetException e) { Constructor<?> directBufferConstructor;
/* 249 */             directBufferConstructor = null;
/*     */           }
/*     */         } else {
/* 252 */           logger.debug("direct buffer constructor: unavailable", (Throwable)maybeDirectBufferConstructor);
/*     */           
/*     */ 
/* 255 */           directBufferConstructor = null;
/*     */         }
/*     */       } finally { Constructor<?> directBufferConstructor;
/* 258 */         if (address != -1L)
/* 259 */           UNSAFE.freeMemory(address);
/*     */       }
/*     */       Constructor<?> directBufferConstructor;
/* 262 */       DIRECT_BUFFER_CONSTRUCTOR = directBufferConstructor;
/* 263 */       ADDRESS_FIELD_OFFSET = objectFieldOffset(addressField);
/* 264 */       BYTE_ARRAY_BASE_OFFSET = UNSAFE.arrayBaseOffset(byte[].class);
/*     */       
/* 266 */       Object maybeUnaligned = AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Object run()
/*     */         {
/*     */           try {
/* 271 */             Class<?> bitsClass = Class.forName("java.nio.Bits", false, PlatformDependent0.getSystemClassLoader());
/* 272 */             int version = PlatformDependent0.javaVersion();
/* 273 */             if (version >= 9)
/*     */             {
/* 275 */               String fieldName = version >= 11 ? "UNALIGNED" : "unaligned";
/*     */               
/*     */               try
/*     */               {
/* 279 */                 Field unalignedField = bitsClass.getDeclaredField(fieldName);
/* 280 */                 if (unalignedField.getType() == Boolean.TYPE) {
/* 281 */                   long offset = PlatformDependent0.UNSAFE.staticFieldOffset(unalignedField);
/* 282 */                   Object object = PlatformDependent0.UNSAFE.staticFieldBase(unalignedField);
/* 283 */                   return Boolean.valueOf(PlatformDependent0.UNSAFE.getBoolean(object, offset));
/*     */                 }
/*     */               }
/*     */               catch (NoSuchFieldException localNoSuchFieldException) {}
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 291 */             Method unalignedMethod = bitsClass.getDeclaredMethod("unaligned", new Class[0]);
/* 292 */             Throwable cause = ReflectionUtil.trySetAccessible(unalignedMethod, true);
/* 293 */             if (cause != null) {
/* 294 */               return cause;
/*     */             }
/* 296 */             return unalignedMethod.invoke(null, new Object[0]);
/*     */           } catch (NoSuchMethodException e) {
/* 298 */             return e;
/*     */           } catch (SecurityException e) {
/* 300 */             return e;
/*     */           } catch (IllegalAccessException e) {
/* 302 */             return e;
/*     */           } catch (ClassNotFoundException e) {
/* 304 */             return e;
/*     */           } catch (InvocationTargetException e) {
/* 306 */             return e;
/*     */           }
/*     */         }
/*     */       });
/*     */       boolean unaligned;
/* 311 */       if ((maybeUnaligned instanceof Boolean)) {
/* 312 */         boolean unaligned = ((Boolean)maybeUnaligned).booleanValue();
/* 313 */         logger.debug("java.nio.Bits.unaligned: available, {}", Boolean.valueOf(unaligned));
/*     */       } else {
/* 315 */         String arch = SystemPropertyUtil.get("os.arch", "");
/*     */         
/* 317 */         unaligned = arch.matches("^(i[3-6]86|x86(_64)?|x64|amd64)$");
/* 318 */         Throwable t = (Throwable)maybeUnaligned;
/* 319 */         logger.debug("java.nio.Bits.unaligned: unavailable {}", Boolean.valueOf(unaligned), t);
/*     */       }
/*     */       
/* 322 */       UNALIGNED = unaligned;
/*     */       
/* 324 */       if (javaVersion() >= 9) {
/* 325 */         Object maybeException = AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */ 
/*     */           public Object run()
/*     */           {
/*     */             try
/*     */             {
/* 332 */               Class<?> internalUnsafeClass = PlatformDependent0.getClassLoader(PlatformDependent0.class).loadClass("jdk.internal.misc.Unsafe");
/* 333 */               Method method = internalUnsafeClass.getDeclaredMethod("getUnsafe", new Class[0]);
/* 334 */               return method.invoke(null, new Object[0]);
/*     */             } catch (Throwable e) {
/* 336 */               return e;
/*     */             }
/*     */           }
/*     */         });
/* 340 */         if (!(maybeException instanceof Throwable)) {
/* 341 */           internalUnsafe = maybeException;
/* 342 */           Object finalInternalUnsafe = internalUnsafe;
/* 343 */           maybeException = AccessController.doPrivileged(new PrivilegedAction()
/*     */           {
/*     */             public Object run() {
/*     */               try {
/* 347 */                 return this.val$finalInternalUnsafe.getClass().getDeclaredMethod("allocateUninitializedArray", new Class[] { Class.class, Integer.TYPE });
/*     */               }
/*     */               catch (NoSuchMethodException e) {
/* 350 */                 return e;
/*     */               } catch (SecurityException e) {
/* 352 */                 return e;
/*     */               }
/*     */             }
/*     */           });
/*     */           
/* 357 */           if ((maybeException instanceof Method)) {
/*     */             try {
/* 359 */               Method m = (Method)maybeException;
/* 360 */               byte[] bytes = (byte[])m.invoke(finalInternalUnsafe, new Object[] { Byte.TYPE, Integer.valueOf(8) });
/* 361 */               assert (bytes.length == 8);
/* 362 */               allocateArrayMethod = m;
/*     */             } catch (IllegalAccessException e) {
/* 364 */               maybeException = e;
/*     */             } catch (InvocationTargetException e) {
/* 366 */               maybeException = e;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 371 */         if ((maybeException instanceof Throwable)) {
/* 372 */           logger.debug("jdk.internal.misc.Unsafe.allocateUninitializedArray(int): unavailable", (Throwable)maybeException);
/*     */         }
/*     */         else {
/* 375 */           logger.debug("jdk.internal.misc.Unsafe.allocateUninitializedArray(int): available");
/*     */         }
/*     */       } else {
/* 378 */         logger.debug("jdk.internal.misc.Unsafe.allocateUninitializedArray(int): unavailable prior to Java9");
/*     */       }
/* 380 */       ALLOCATE_ARRAY_METHOD = allocateArrayMethod;
/*     */     }
/*     */     
/* 383 */     INTERNAL_UNSAFE = internalUnsafe;
/*     */     
/* 385 */     logger.debug("java.nio.DirectByteBuffer.<init>(long, int): {}", DIRECT_BUFFER_CONSTRUCTOR != null ? "available" : "unavailable");
/*     */   }
/*     */   
/*     */   static boolean isExplicitNoUnsafe()
/*     */   {
/* 390 */     return EXPLICIT_NO_UNSAFE_CAUSE != null;
/*     */   }
/*     */   
/*     */   private static Throwable explicitNoUnsafeCause0() {
/* 394 */     boolean noUnsafe = SystemPropertyUtil.getBoolean("io.netty.noUnsafe", false);
/* 395 */     logger.debug("-Dio.netty.noUnsafe: {}", Boolean.valueOf(noUnsafe));
/*     */     
/* 397 */     if (noUnsafe) {
/* 398 */       logger.debug("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
/* 399 */       return new UnsupportedOperationException("sun.misc.Unsafe: unavailable (io.netty.noUnsafe)");
/*     */     }
/*     */     
/*     */     String unsafePropName;
/*     */     String unsafePropName;
/* 404 */     if (SystemPropertyUtil.contains("io.netty.tryUnsafe")) {
/* 405 */       unsafePropName = "io.netty.tryUnsafe";
/*     */     } else {
/* 407 */       unsafePropName = "org.jboss.netty.tryUnsafe";
/*     */     }
/*     */     
/* 410 */     if (!SystemPropertyUtil.getBoolean(unsafePropName, true)) {
/* 411 */       String msg = "sun.misc.Unsafe: unavailable (" + unsafePropName + ")";
/* 412 */       logger.debug(msg);
/* 413 */       return new UnsupportedOperationException(msg);
/*     */     }
/*     */     
/* 416 */     return null;
/*     */   }
/*     */   
/*     */   static boolean isUnaligned() {
/* 420 */     return UNALIGNED;
/*     */   }
/*     */   
/*     */   static boolean hasUnsafe() {
/* 424 */     return UNSAFE != null;
/*     */   }
/*     */   
/*     */   static Throwable getUnsafeUnavailabilityCause() {
/* 428 */     return UNSAFE_UNAVAILABILITY_CAUSE;
/*     */   }
/*     */   
/*     */   static boolean unalignedAccess() {
/* 432 */     return UNALIGNED;
/*     */   }
/*     */   
/*     */   static void throwException(Throwable cause)
/*     */   {
/* 437 */     UNSAFE.throwException((Throwable)ObjectUtil.checkNotNull(cause, "cause"));
/*     */   }
/*     */   
/*     */   static boolean hasDirectBufferNoCleanerConstructor() {
/* 441 */     return DIRECT_BUFFER_CONSTRUCTOR != null;
/*     */   }
/*     */   
/*     */   static ByteBuffer reallocateDirectNoCleaner(ByteBuffer buffer, int capacity) {
/* 445 */     return newDirectBuffer(UNSAFE.reallocateMemory(directBufferAddress(buffer), capacity), capacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static ByteBuffer allocateDirectNoCleaner(int capacity)
/*     */   {
/* 452 */     return newDirectBuffer(UNSAFE.allocateMemory(Math.max(1, capacity)), capacity);
/*     */   }
/*     */   
/*     */   static boolean hasAllocateArrayMethod() {
/* 456 */     return ALLOCATE_ARRAY_METHOD != null;
/*     */   }
/*     */   
/*     */   static byte[] allocateUninitializedArray(int size) {
/*     */     try {
/* 461 */       return (byte[])ALLOCATE_ARRAY_METHOD.invoke(INTERNAL_UNSAFE, new Object[] { Byte.TYPE, Integer.valueOf(size) });
/*     */     } catch (IllegalAccessException e) {
/* 463 */       throw new Error(e);
/*     */     } catch (InvocationTargetException e) {
/* 465 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */   
/*     */   static ByteBuffer newDirectBuffer(long address, int capacity) {
/* 470 */     ObjectUtil.checkPositiveOrZero(capacity, "capacity");
/*     */     try
/*     */     {
/* 473 */       return (ByteBuffer)DIRECT_BUFFER_CONSTRUCTOR.newInstance(new Object[] { Long.valueOf(address), Integer.valueOf(capacity) });
/*     */     }
/*     */     catch (Throwable cause) {
/* 476 */       if ((cause instanceof Error)) {
/* 477 */         throw ((Error)cause);
/*     */       }
/* 479 */       throw new Error(cause);
/*     */     }
/*     */   }
/*     */   
/*     */   static long directBufferAddress(ByteBuffer buffer) {
/* 484 */     return getLong(buffer, ADDRESS_FIELD_OFFSET);
/*     */   }
/*     */   
/*     */   static long byteArrayBaseOffset() {
/* 488 */     return BYTE_ARRAY_BASE_OFFSET;
/*     */   }
/*     */   
/*     */   static Object getObject(Object object, long fieldOffset) {
/* 492 */     return UNSAFE.getObject(object, fieldOffset);
/*     */   }
/*     */   
/*     */   static int getInt(Object object, long fieldOffset) {
/* 496 */     return UNSAFE.getInt(object, fieldOffset);
/*     */   }
/*     */   
/*     */   private static long getLong(Object object, long fieldOffset) {
/* 500 */     return UNSAFE.getLong(object, fieldOffset);
/*     */   }
/*     */   
/*     */   static long objectFieldOffset(Field field) {
/* 504 */     return UNSAFE.objectFieldOffset(field);
/*     */   }
/*     */   
/*     */   static byte getByte(long address) {
/* 508 */     return UNSAFE.getByte(address);
/*     */   }
/*     */   
/*     */   static short getShort(long address) {
/* 512 */     return UNSAFE.getShort(address);
/*     */   }
/*     */   
/*     */   static int getInt(long address) {
/* 516 */     return UNSAFE.getInt(address);
/*     */   }
/*     */   
/*     */   static long getLong(long address) {
/* 520 */     return UNSAFE.getLong(address);
/*     */   }
/*     */   
/*     */   static byte getByte(byte[] data, int index) {
/* 524 */     return UNSAFE.getByte(data, BYTE_ARRAY_BASE_OFFSET + index);
/*     */   }
/*     */   
/*     */   static short getShort(byte[] data, int index) {
/* 528 */     return UNSAFE.getShort(data, BYTE_ARRAY_BASE_OFFSET + index);
/*     */   }
/*     */   
/*     */   static int getInt(byte[] data, int index) {
/* 532 */     return UNSAFE.getInt(data, BYTE_ARRAY_BASE_OFFSET + index);
/*     */   }
/*     */   
/*     */   static long getLong(byte[] data, int index) {
/* 536 */     return UNSAFE.getLong(data, BYTE_ARRAY_BASE_OFFSET + index);
/*     */   }
/*     */   
/*     */   static void putByte(long address, byte value) {
/* 540 */     UNSAFE.putByte(address, value);
/*     */   }
/*     */   
/*     */   static void putShort(long address, short value) {
/* 544 */     UNSAFE.putShort(address, value);
/*     */   }
/*     */   
/*     */   static void putInt(long address, int value) {
/* 548 */     UNSAFE.putInt(address, value);
/*     */   }
/*     */   
/*     */   static void putLong(long address, long value) {
/* 552 */     UNSAFE.putLong(address, value);
/*     */   }
/*     */   
/*     */   static void putByte(byte[] data, int index, byte value) {
/* 556 */     UNSAFE.putByte(data, BYTE_ARRAY_BASE_OFFSET + index, value);
/*     */   }
/*     */   
/*     */   static void putShort(byte[] data, int index, short value) {
/* 560 */     UNSAFE.putShort(data, BYTE_ARRAY_BASE_OFFSET + index, value);
/*     */   }
/*     */   
/*     */   static void putInt(byte[] data, int index, int value) {
/* 564 */     UNSAFE.putInt(data, BYTE_ARRAY_BASE_OFFSET + index, value);
/*     */   }
/*     */   
/*     */   static void putLong(byte[] data, int index, long value) {
/* 568 */     UNSAFE.putLong(data, BYTE_ARRAY_BASE_OFFSET + index, value);
/*     */   }
/*     */   
/*     */   static void putObject(Object o, long offset, Object x) {
/* 572 */     UNSAFE.putObject(o, offset, x);
/*     */   }
/*     */   
/*     */ 
/*     */   static void copyMemory(long srcAddr, long dstAddr, long length)
/*     */   {
/* 578 */     if (javaVersion() <= 8) {
/* 579 */       copyMemoryWithSafePointPolling(srcAddr, dstAddr, length);
/*     */     } else {
/* 581 */       UNSAFE.copyMemory(srcAddr, dstAddr, length);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void copyMemoryWithSafePointPolling(long srcAddr, long dstAddr, long length) {
/* 586 */     while (length > 0L) {
/* 587 */       long size = Math.min(length, 1048576L);
/* 588 */       UNSAFE.copyMemory(srcAddr, dstAddr, size);
/* 589 */       length -= size;
/* 590 */       srcAddr += size;
/* 591 */       dstAddr += size;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static void copyMemory(Object src, long srcOffset, Object dst, long dstOffset, long length)
/*     */   {
/* 598 */     if (javaVersion() <= 8) {
/* 599 */       copyMemoryWithSafePointPolling(src, srcOffset, dst, dstOffset, length);
/*     */     } else {
/* 601 */       UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, length);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void copyMemoryWithSafePointPolling(Object src, long srcOffset, Object dst, long dstOffset, long length)
/*     */   {
/* 607 */     while (length > 0L) {
/* 608 */       long size = Math.min(length, 1048576L);
/* 609 */       UNSAFE.copyMemory(src, srcOffset, dst, dstOffset, size);
/* 610 */       length -= size;
/* 611 */       srcOffset += size;
/* 612 */       dstOffset += size;
/*     */     }
/*     */   }
/*     */   
/*     */   static void setMemory(long address, long bytes, byte value) {
/* 617 */     UNSAFE.setMemory(address, bytes, value);
/*     */   }
/*     */   
/*     */   static void setMemory(Object o, long offset, long bytes, byte value) {
/* 621 */     UNSAFE.setMemory(o, offset, bytes, value);
/*     */   }
/*     */   
/*     */   static boolean equals(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
/* 625 */     if (length <= 0) {
/* 626 */       return true;
/*     */     }
/* 628 */     long baseOffset1 = BYTE_ARRAY_BASE_OFFSET + startPos1;
/* 629 */     long baseOffset2 = BYTE_ARRAY_BASE_OFFSET + startPos2;
/* 630 */     int remainingBytes = length & 0x7;
/* 631 */     long end = baseOffset1 + remainingBytes;
/* 632 */     long i = baseOffset1 - 8L + length; for (long j = baseOffset2 - 8L + length; i >= end; j -= 8L) {
/* 633 */       if (UNSAFE.getLong(bytes1, i) != UNSAFE.getLong(bytes2, j)) {
/* 634 */         return false;
/*     */       }
/* 632 */       i -= 8L;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 638 */     if (remainingBytes >= 4) {
/* 639 */       remainingBytes -= 4;
/*     */       
/* 641 */       if (UNSAFE.getInt(bytes1, baseOffset1 + remainingBytes) != UNSAFE.getInt(bytes2, baseOffset2 + remainingBytes)) {
/* 642 */         return false;
/*     */       }
/*     */     }
/* 645 */     if (remainingBytes >= 2) {
/* 646 */       return (UNSAFE.getChar(bytes1, baseOffset1) == UNSAFE.getChar(bytes2, baseOffset2)) && ((remainingBytes == 2) || (bytes1[(startPos1 + 2)] == bytes2[(startPos2 + 2)]));
/*     */     }
/*     */     
/* 649 */     return bytes1[startPos1] == bytes2[startPos2];
/*     */   }
/*     */   
/*     */   static int equalsConstantTime(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
/* 653 */     long result = 0L;
/* 654 */     long baseOffset1 = BYTE_ARRAY_BASE_OFFSET + startPos1;
/* 655 */     long baseOffset2 = BYTE_ARRAY_BASE_OFFSET + startPos2;
/* 656 */     int remainingBytes = length & 0x7;
/* 657 */     long end = baseOffset1 + remainingBytes;
/* 658 */     long i = baseOffset1 - 8L + length; for (long j = baseOffset2 - 8L + length; i >= end; j -= 8L) {
/* 659 */       result |= UNSAFE.getLong(bytes1, i) ^ UNSAFE.getLong(bytes2, j);i -= 8L;
/*     */     }
/* 661 */     switch (remainingBytes) {
/*     */     case 7: 
/* 663 */       return ConstantTimeUtils.equalsConstantTime(result | UNSAFE
/* 664 */         .getInt(bytes1, baseOffset1 + 3L) ^ UNSAFE.getInt(bytes2, baseOffset2 + 3L) | UNSAFE
/* 665 */         .getChar(bytes1, baseOffset1 + 1L) ^ UNSAFE.getChar(bytes2, baseOffset2 + 1L) | UNSAFE
/* 666 */         .getByte(bytes1, baseOffset1) ^ UNSAFE.getByte(bytes2, baseOffset2), 0L);
/*     */     case 6: 
/* 668 */       return ConstantTimeUtils.equalsConstantTime(result | UNSAFE
/* 669 */         .getInt(bytes1, baseOffset1 + 2L) ^ UNSAFE.getInt(bytes2, baseOffset2 + 2L) | UNSAFE
/* 670 */         .getChar(bytes1, baseOffset1) ^ UNSAFE.getChar(bytes2, baseOffset2), 0L);
/*     */     case 5: 
/* 672 */       return ConstantTimeUtils.equalsConstantTime(result | UNSAFE
/* 673 */         .getInt(bytes1, baseOffset1 + 1L) ^ UNSAFE.getInt(bytes2, baseOffset2 + 1L) | UNSAFE
/* 674 */         .getByte(bytes1, baseOffset1) ^ UNSAFE.getByte(bytes2, baseOffset2), 0L);
/*     */     case 4: 
/* 676 */       return ConstantTimeUtils.equalsConstantTime(result | UNSAFE
/* 677 */         .getInt(bytes1, baseOffset1) ^ UNSAFE.getInt(bytes2, baseOffset2), 0L);
/*     */     case 3: 
/* 679 */       return ConstantTimeUtils.equalsConstantTime(result | UNSAFE
/* 680 */         .getChar(bytes1, baseOffset1 + 1L) ^ UNSAFE.getChar(bytes2, baseOffset2 + 1L) | UNSAFE
/* 681 */         .getByte(bytes1, baseOffset1) ^ UNSAFE.getByte(bytes2, baseOffset2), 0L);
/*     */     case 2: 
/* 683 */       return ConstantTimeUtils.equalsConstantTime(result | UNSAFE
/* 684 */         .getChar(bytes1, baseOffset1) ^ UNSAFE.getChar(bytes2, baseOffset2), 0L);
/*     */     case 1: 
/* 686 */       return ConstantTimeUtils.equalsConstantTime(result | UNSAFE
/* 687 */         .getByte(bytes1, baseOffset1) ^ UNSAFE.getByte(bytes2, baseOffset2), 0L);
/*     */     }
/* 689 */     return ConstantTimeUtils.equalsConstantTime(result, 0L);
/*     */   }
/*     */   
/*     */   static boolean isZero(byte[] bytes, int startPos, int length)
/*     */   {
/* 694 */     if (length <= 0) {
/* 695 */       return true;
/*     */     }
/* 697 */     long baseOffset = BYTE_ARRAY_BASE_OFFSET + startPos;
/* 698 */     int remainingBytes = length & 0x7;
/* 699 */     long end = baseOffset + remainingBytes;
/* 700 */     for (long i = baseOffset - 8L + length; i >= end; i -= 8L) {
/* 701 */       if (UNSAFE.getLong(bytes, i) != 0L) {
/* 702 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 706 */     if (remainingBytes >= 4) {
/* 707 */       remainingBytes -= 4;
/* 708 */       if (UNSAFE.getInt(bytes, baseOffset + remainingBytes) != 0) {
/* 709 */         return false;
/*     */       }
/*     */     }
/* 712 */     if (remainingBytes >= 2) {
/* 713 */       return (UNSAFE.getChar(bytes, baseOffset) == 0) && ((remainingBytes == 2) || (bytes[(startPos + 2)] == 0));
/*     */     }
/*     */     
/* 716 */     return bytes[startPos] == 0;
/*     */   }
/*     */   
/*     */   static int hashCodeAscii(byte[] bytes, int startPos, int length) {
/* 720 */     int hash = -1028477387;
/* 721 */     long baseOffset = BYTE_ARRAY_BASE_OFFSET + startPos;
/* 722 */     int remainingBytes = length & 0x7;
/* 723 */     long end = baseOffset + remainingBytes;
/* 724 */     for (long i = baseOffset - 8L + length; i >= end; i -= 8L) {
/* 725 */       hash = hashCodeAsciiCompute(UNSAFE.getLong(bytes, i), hash);
/*     */     }
/* 727 */     switch (remainingBytes) {
/*     */     case 7: 
/* 729 */       return 
/*     */       
/* 731 */         ((hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getByte(bytes, baseOffset))) * 461845907 + hashCodeAsciiSanitize(UNSAFE.getShort(bytes, baseOffset + 1L))) * -862048943 + hashCodeAsciiSanitize(UNSAFE.getInt(bytes, baseOffset + 3L));
/*     */     case 6: 
/* 733 */       return 
/* 734 */         (hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getShort(bytes, baseOffset))) * 461845907 + hashCodeAsciiSanitize(UNSAFE.getInt(bytes, baseOffset + 2L));
/*     */     case 5: 
/* 736 */       return 
/* 737 */         (hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getByte(bytes, baseOffset))) * 461845907 + hashCodeAsciiSanitize(UNSAFE.getInt(bytes, baseOffset + 1L));
/*     */     case 4: 
/* 739 */       return hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getInt(bytes, baseOffset));
/*     */     case 3: 
/* 741 */       return 
/* 742 */         (hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getByte(bytes, baseOffset))) * 461845907 + hashCodeAsciiSanitize(UNSAFE.getShort(bytes, baseOffset + 1L));
/*     */     case 2: 
/* 744 */       return hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getShort(bytes, baseOffset));
/*     */     case 1: 
/* 746 */       return hash * -862048943 + hashCodeAsciiSanitize(UNSAFE.getByte(bytes, baseOffset));
/*     */     }
/* 748 */     return hash;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static int hashCodeAsciiCompute(long value, int hash)
/*     */   {
/* 755 */     return 
/*     */     
/* 757 */       hash * -862048943 + hashCodeAsciiSanitize((int)value) * 461845907 + (int)((value & 0x1F1F1F1F00000000) >>> 32);
/*     */   }
/*     */   
/*     */ 
/*     */   static int hashCodeAsciiSanitize(int value)
/*     */   {
/* 763 */     return value & 0x1F1F1F1F;
/*     */   }
/*     */   
/*     */   static int hashCodeAsciiSanitize(short value) {
/* 767 */     return value & 0x1F1F;
/*     */   }
/*     */   
/*     */   static int hashCodeAsciiSanitize(byte value) {
/* 771 */     return value & 0x1F;
/*     */   }
/*     */   
/*     */   static ClassLoader getClassLoader(Class<?> clazz) {
/* 775 */     if (System.getSecurityManager() == null) {
/* 776 */       return clazz.getClassLoader();
/*     */     }
/* 778 */     (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ClassLoader run() {
/* 781 */         return this.val$clazz.getClassLoader();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   static ClassLoader getContextClassLoader()
/*     */   {
/* 788 */     if (System.getSecurityManager() == null) {
/* 789 */       return Thread.currentThread().getContextClassLoader();
/*     */     }
/* 791 */     (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ClassLoader run() {
/* 794 */         return Thread.currentThread().getContextClassLoader();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   static ClassLoader getSystemClassLoader()
/*     */   {
/* 801 */     if (System.getSecurityManager() == null) {
/* 802 */       return ClassLoader.getSystemClassLoader();
/*     */     }
/* 804 */     (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public ClassLoader run() {
/* 807 */         return ClassLoader.getSystemClassLoader();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   static int addressSize()
/*     */   {
/* 814 */     return UNSAFE.addressSize();
/*     */   }
/*     */   
/*     */   static long allocateMemory(long size) {
/* 818 */     return UNSAFE.allocateMemory(size);
/*     */   }
/*     */   
/*     */   static void freeMemory(long address) {
/* 822 */     UNSAFE.freeMemory(address);
/*     */   }
/*     */   
/*     */   static long reallocateMemory(long address, long newSize) {
/* 826 */     return UNSAFE.reallocateMemory(address, newSize);
/*     */   }
/*     */   
/*     */   static boolean isAndroid() {
/* 830 */     return IS_ANDROID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isAndroid0()
/*     */   {
/* 841 */     String vmName = SystemPropertyUtil.get("java.vm.name");
/* 842 */     boolean isAndroid = "Dalvik".equals(vmName);
/* 843 */     if (isAndroid) {
/* 844 */       logger.debug("Platform: Android");
/*     */     }
/* 846 */     return isAndroid;
/*     */   }
/*     */   
/*     */   private static boolean explicitTryReflectionSetAccessible0()
/*     */   {
/* 851 */     return SystemPropertyUtil.getBoolean("io.netty.tryReflectionSetAccessible", javaVersion() < 9);
/*     */   }
/*     */   
/*     */   static boolean isExplicitTryReflectionSetAccessible() {
/* 855 */     return IS_EXPLICIT_TRY_REFLECTION_SET_ACCESSIBLE;
/*     */   }
/*     */   
/*     */   static int javaVersion() {
/* 859 */     return JAVA_VERSION;
/*     */   }
/*     */   
/*     */   private static int javaVersion0() {
/*     */     int majorVersion;
/*     */     int majorVersion;
/* 865 */     if (isAndroid0()) {
/* 866 */       majorVersion = 6;
/*     */     } else {
/* 868 */       majorVersion = majorVersionFromJavaSpecificationVersion();
/*     */     }
/*     */     
/* 871 */     logger.debug("Java version: {}", Integer.valueOf(majorVersion));
/*     */     
/* 873 */     return majorVersion;
/*     */   }
/*     */   
/*     */   static int majorVersionFromJavaSpecificationVersion()
/*     */   {
/* 878 */     return majorVersion(SystemPropertyUtil.get("java.specification.version", "1.6"));
/*     */   }
/*     */   
/*     */   static int majorVersion(String javaSpecVersion)
/*     */   {
/* 883 */     String[] components = javaSpecVersion.split("\\.");
/* 884 */     int[] version = new int[components.length];
/* 885 */     for (int i = 0; i < components.length; i++) {
/* 886 */       version[i] = Integer.parseInt(components[i]);
/*     */     }
/*     */     
/* 889 */     if (version[0] == 1) {
/* 890 */       assert (version[1] >= 6);
/* 891 */       return version[1];
/*     */     }
/* 893 */     return version[0];
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\PlatformDependent0.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */