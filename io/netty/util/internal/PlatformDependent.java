/*      */ package io.netty.util.internal;
/*      */ 
/*      */ import io.netty.util.internal.logging.InternalLogger;
/*      */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.MpscArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.MpscChunkedArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.MpscUnboundedArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.SpscLinkedQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscAtomicArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscGrowableAtomicArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.atomic.MpscUnboundedAtomicArrayQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.queues.atomic.SpscLinkedAtomicQueue;
/*      */ import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
/*      */ import java.io.File;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Deque;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Random;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentLinkedDeque;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.LinkedBlockingDeque;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class PlatformDependent
/*      */ {
/*      */   private static final InternalLogger logger;
/*      */   private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN;
/*      */   private static final boolean IS_WINDOWS;
/*      */   private static final boolean IS_OSX;
/*      */   private static final boolean MAYBE_SUPER_USER;
/*      */   private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
/*      */   private static final Throwable UNSAFE_UNAVAILABILITY_CAUSE;
/*      */   private static final boolean DIRECT_BUFFER_PREFERRED;
/*      */   private static final long MAX_DIRECT_MEMORY;
/*      */   private static final int MPSC_CHUNK_SIZE = 1024;
/*      */   private static final int MIN_MAX_MPSC_CAPACITY = 2048;
/*      */   private static final int MAX_ALLOWED_MPSC_CAPACITY = 1073741824;
/*      */   private static final long BYTE_ARRAY_BASE_OFFSET;
/*      */   private static final File TMPDIR;
/*      */   private static final int BIT_MODE;
/*      */   private static final String NORMALIZED_ARCH;
/*      */   private static final String NORMALIZED_OS;
/*      */   private static final int ADDRESS_SIZE;
/*      */   private static final boolean USE_DIRECT_BUFFER_NO_CLEANER;
/*      */   private static final AtomicLong DIRECT_MEMORY_COUNTER;
/*      */   private static final long DIRECT_MEMORY_LIMIT;
/*      */   private static final ThreadLocalRandomProvider RANDOM_PROVIDER;
/*      */   private static final Cleaner CLEANER;
/*      */   private static final int UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD;
/*      */   public static final boolean BIG_ENDIAN_NATIVE_ORDER;
/*      */   private static final Cleaner NOOP;
/*      */   
/*      */   static
/*      */   {
/*   70 */     logger = InternalLoggerFactory.getInstance(PlatformDependent.class);
/*      */     
/*   72 */     MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile("\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");
/*      */     
/*      */ 
/*   75 */     IS_WINDOWS = isWindows0();
/*   76 */     IS_OSX = isOsx0();
/*      */     
/*      */ 
/*      */ 
/*   80 */     CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !isAndroid();
/*      */     
/*   82 */     UNSAFE_UNAVAILABILITY_CAUSE = unsafeUnavailabilityCause0();
/*      */     
/*   84 */     MAX_DIRECT_MEMORY = maxDirectMemory0();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   90 */     BYTE_ARRAY_BASE_OFFSET = byteArrayBaseOffset0();
/*      */     
/*   92 */     TMPDIR = tmpdir0();
/*      */     
/*   94 */     BIT_MODE = bitMode0();
/*   95 */     NORMALIZED_ARCH = normalizeArch(SystemPropertyUtil.get("os.arch", ""));
/*   96 */     NORMALIZED_OS = normalizeOs(SystemPropertyUtil.get("os.name", ""));
/*      */     
/*   98 */     ADDRESS_SIZE = addressSize0();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  106 */     BIG_ENDIAN_NATIVE_ORDER = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
/*      */     
/*  108 */     NOOP = new Cleaner()
/*      */     {
/*      */       public void freeDirectBuffer(ByteBuffer buffer) {}
/*      */     };
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  116 */     if (javaVersion() >= 7) {
/*  117 */       RANDOM_PROVIDER = new ThreadLocalRandomProvider()
/*      */       {
/*      */         public Random current() {
/*  120 */           return java.util.concurrent.ThreadLocalRandom.current();
/*      */         }
/*      */       };
/*      */     } else {
/*  124 */       RANDOM_PROVIDER = new ThreadLocalRandomProvider()
/*      */       {
/*      */         public Random current() {
/*  127 */           return ThreadLocalRandom.current();
/*      */         }
/*      */       };
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  139 */     long maxDirectMemory = SystemPropertyUtil.getLong("io.netty.maxDirectMemory", -1L);
/*      */     
/*  141 */     if ((maxDirectMemory == 0L) || (!hasUnsafe()) || (!PlatformDependent0.hasDirectBufferNoCleanerConstructor())) {
/*  142 */       USE_DIRECT_BUFFER_NO_CLEANER = false;
/*  143 */       DIRECT_MEMORY_COUNTER = null;
/*      */     } else {
/*  145 */       USE_DIRECT_BUFFER_NO_CLEANER = true;
/*  146 */       if (maxDirectMemory < 0L) {
/*  147 */         maxDirectMemory = MAX_DIRECT_MEMORY;
/*  148 */         if (maxDirectMemory <= 0L) {
/*  149 */           DIRECT_MEMORY_COUNTER = null;
/*      */         } else {
/*  151 */           DIRECT_MEMORY_COUNTER = new AtomicLong();
/*      */         }
/*      */       } else {
/*  154 */         DIRECT_MEMORY_COUNTER = new AtomicLong();
/*      */       }
/*      */     }
/*  157 */     DIRECT_MEMORY_LIMIT = maxDirectMemory;
/*  158 */     logger.debug("-Dio.netty.maxDirectMemory: {} bytes", Long.valueOf(maxDirectMemory));
/*      */     
/*      */ 
/*  161 */     int tryAllocateUninitializedArray = SystemPropertyUtil.getInt("io.netty.uninitializedArrayAllocationThreshold", 1024);
/*  162 */     UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD = (javaVersion() >= 9) && (PlatformDependent0.hasAllocateArrayMethod()) ? tryAllocateUninitializedArray : -1;
/*      */     
/*  164 */     logger.debug("-Dio.netty.uninitializedArrayAllocationThreshold: {}", Integer.valueOf(UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD));
/*      */     
/*  166 */     MAYBE_SUPER_USER = maybeSuperUser0();
/*      */     
/*  168 */     if (!isAndroid())
/*      */     {
/*      */ 
/*  171 */       if (javaVersion() >= 9) {
/*  172 */         CLEANER = CleanerJava9.isSupported() ? new CleanerJava9() : NOOP;
/*      */       } else {
/*  174 */         CLEANER = CleanerJava6.isSupported() ? new CleanerJava6() : NOOP;
/*      */       }
/*      */     } else {
/*  177 */       CLEANER = NOOP;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  182 */     DIRECT_BUFFER_PREFERRED = (CLEANER != NOOP) && (!SystemPropertyUtil.getBoolean("io.netty.noPreferDirect", false));
/*  183 */     if (logger.isDebugEnabled()) {
/*  184 */       logger.debug("-Dio.netty.noPreferDirect: {}", Boolean.valueOf(!DIRECT_BUFFER_PREFERRED));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  191 */     if ((CLEANER == NOOP) && (!PlatformDependent0.isExplicitNoUnsafe())) {
/*  192 */       logger.info("Your platform does not provide complete low-level API for accessing direct buffers reliably. Unless explicitly requested, heap buffer will always be preferred to avoid potential system instability.");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean hasDirectBufferNoCleanerConstructor()
/*      */   {
/*  200 */     return PlatformDependent0.hasDirectBufferNoCleanerConstructor();
/*      */   }
/*      */   
/*      */   public static byte[] allocateUninitializedArray(int size) {
/*  204 */     return (UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD < 0) || (UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD > size) ? new byte[size] : 
/*  205 */       PlatformDependent0.allocateUninitializedArray(size);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean isAndroid()
/*      */   {
/*  212 */     return PlatformDependent0.isAndroid();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean isWindows()
/*      */   {
/*  219 */     return IS_WINDOWS;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean isOsx()
/*      */   {
/*  226 */     return IS_OSX;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean maybeSuperUser()
/*      */   {
/*  234 */     return MAYBE_SUPER_USER;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int javaVersion()
/*      */   {
/*  241 */     return PlatformDependent0.javaVersion();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean canEnableTcpNoDelayByDefault()
/*      */   {
/*  248 */     return CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean hasUnsafe()
/*      */   {
/*  256 */     return UNSAFE_UNAVAILABILITY_CAUSE == null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static Throwable getUnsafeUnavailabilityCause()
/*      */   {
/*  263 */     return UNSAFE_UNAVAILABILITY_CAUSE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isUnaligned()
/*      */   {
/*  272 */     return PlatformDependent0.isUnaligned();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean directBufferPreferred()
/*      */   {
/*  280 */     return DIRECT_BUFFER_PREFERRED;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static long maxDirectMemory()
/*      */   {
/*  287 */     return MAX_DIRECT_MEMORY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static File tmpdir()
/*      */   {
/*  294 */     return TMPDIR;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int bitMode()
/*      */   {
/*  301 */     return BIT_MODE;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int addressSize()
/*      */   {
/*  309 */     return ADDRESS_SIZE;
/*      */   }
/*      */   
/*      */   public static long allocateMemory(long size) {
/*  313 */     return PlatformDependent0.allocateMemory(size);
/*      */   }
/*      */   
/*      */   public static void freeMemory(long address) {
/*  317 */     PlatformDependent0.freeMemory(address);
/*      */   }
/*      */   
/*      */   public static long reallocateMemory(long address, long newSize) {
/*  321 */     return PlatformDependent0.reallocateMemory(address, newSize);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static void throwException(Throwable t)
/*      */   {
/*  328 */     if (hasUnsafe()) {
/*  329 */       PlatformDependent0.throwException(t);
/*      */     } else {
/*  331 */       throwException0(t);
/*      */     }
/*      */   }
/*      */   
/*      */   private static <E extends Throwable> void throwException0(Throwable t) throws Throwable
/*      */   {
/*  337 */     throw t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap()
/*      */   {
/*  344 */     return new ConcurrentHashMap();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static LongCounter newLongCounter()
/*      */   {
/*  351 */     if (javaVersion() >= 8) {
/*  352 */       return new LongAdderCounter();
/*      */     }
/*  354 */     return new AtomicLongCounter(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity)
/*      */   {
/*  362 */     return new ConcurrentHashMap(initialCapacity);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity, float loadFactor)
/*      */   {
/*  369 */     return new ConcurrentHashMap(initialCapacity, loadFactor);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel)
/*      */   {
/*  377 */     return new ConcurrentHashMap(initialCapacity, loadFactor, concurrencyLevel);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static <K, V> ConcurrentMap<K, V> newConcurrentHashMap(Map<? extends K, ? extends V> map)
/*      */   {
/*  384 */     return new ConcurrentHashMap(map);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void freeDirectBuffer(ByteBuffer buffer)
/*      */   {
/*  392 */     CLEANER.freeDirectBuffer(buffer);
/*      */   }
/*      */   
/*      */   public static long directBufferAddress(ByteBuffer buffer) {
/*  396 */     return PlatformDependent0.directBufferAddress(buffer);
/*      */   }
/*      */   
/*      */   public static ByteBuffer directBuffer(long memoryAddress, int size) {
/*  400 */     if (PlatformDependent0.hasDirectBufferNoCleanerConstructor()) {
/*  401 */       return PlatformDependent0.newDirectBuffer(memoryAddress, size);
/*      */     }
/*  403 */     throw new UnsupportedOperationException("sun.misc.Unsafe or java.nio.DirectByteBuffer.<init>(long, int) not available");
/*      */   }
/*      */   
/*      */   public static int getInt(Object object, long fieldOffset)
/*      */   {
/*  408 */     return PlatformDependent0.getInt(object, fieldOffset);
/*      */   }
/*      */   
/*      */   public static byte getByte(long address) {
/*  412 */     return PlatformDependent0.getByte(address);
/*      */   }
/*      */   
/*      */   public static short getShort(long address) {
/*  416 */     return PlatformDependent0.getShort(address);
/*      */   }
/*      */   
/*      */   public static int getInt(long address) {
/*  420 */     return PlatformDependent0.getInt(address);
/*      */   }
/*      */   
/*      */   public static long getLong(long address) {
/*  424 */     return PlatformDependent0.getLong(address);
/*      */   }
/*      */   
/*      */   public static byte getByte(byte[] data, int index) {
/*  428 */     return PlatformDependent0.getByte(data, index);
/*      */   }
/*      */   
/*      */   public static short getShort(byte[] data, int index) {
/*  432 */     return PlatformDependent0.getShort(data, index);
/*      */   }
/*      */   
/*      */   public static int getInt(byte[] data, int index) {
/*  436 */     return PlatformDependent0.getInt(data, index);
/*      */   }
/*      */   
/*      */   public static long getLong(byte[] data, int index) {
/*  440 */     return PlatformDependent0.getLong(data, index);
/*      */   }
/*      */   
/*      */   private static long getLongSafe(byte[] bytes, int offset) {
/*  444 */     if (BIG_ENDIAN_NATIVE_ORDER) {
/*  445 */       return bytes[offset] << 56 | (bytes[(offset + 1)] & 0xFF) << 48 | (bytes[(offset + 2)] & 0xFF) << 40 | (bytes[(offset + 3)] & 0xFF) << 32 | (bytes[(offset + 4)] & 0xFF) << 24 | (bytes[(offset + 5)] & 0xFF) << 16 | (bytes[(offset + 6)] & 0xFF) << 8 | bytes[(offset + 7)] & 0xFF;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  454 */     return bytes[offset] & 0xFF | (bytes[(offset + 1)] & 0xFF) << 8 | (bytes[(offset + 2)] & 0xFF) << 16 | (bytes[(offset + 3)] & 0xFF) << 24 | (bytes[(offset + 4)] & 0xFF) << 32 | (bytes[(offset + 5)] & 0xFF) << 40 | (bytes[(offset + 6)] & 0xFF) << 48 | bytes[(offset + 7)] << 56;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static int getIntSafe(byte[] bytes, int offset)
/*      */   {
/*  465 */     if (BIG_ENDIAN_NATIVE_ORDER) {
/*  466 */       return bytes[offset] << 24 | (bytes[(offset + 1)] & 0xFF) << 16 | (bytes[(offset + 2)] & 0xFF) << 8 | bytes[(offset + 3)] & 0xFF;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  471 */     return bytes[offset] & 0xFF | (bytes[(offset + 1)] & 0xFF) << 8 | (bytes[(offset + 2)] & 0xFF) << 16 | bytes[(offset + 3)] << 24;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static short getShortSafe(byte[] bytes, int offset)
/*      */   {
/*  478 */     if (BIG_ENDIAN_NATIVE_ORDER) {
/*  479 */       return (short)(bytes[offset] << 8 | bytes[(offset + 1)] & 0xFF);
/*      */     }
/*  481 */     return (short)(bytes[offset] & 0xFF | bytes[(offset + 1)] << 8);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static int hashCodeAsciiCompute(CharSequence value, int offset, int hash)
/*      */   {
/*  488 */     if (BIG_ENDIAN_NATIVE_ORDER) {
/*  489 */       return 
/*      */       
/*      */ 
/*      */ 
/*  493 */         hash * -862048943 + hashCodeAsciiSanitizeInt(value, offset + 4) * 461845907 + hashCodeAsciiSanitizeInt(value, offset);
/*      */     }
/*  495 */     return 
/*      */     
/*      */ 
/*      */ 
/*  499 */       hash * -862048943 + hashCodeAsciiSanitizeInt(value, offset) * 461845907 + hashCodeAsciiSanitizeInt(value, offset + 4);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static int hashCodeAsciiSanitizeInt(CharSequence value, int offset)
/*      */   {
/*  506 */     if (BIG_ENDIAN_NATIVE_ORDER)
/*      */     {
/*  508 */       return 
/*      */       
/*      */ 
/*  511 */         value.charAt(offset + 3) & 0x1F | (value.charAt(offset + 2) & 0x1F) << '\b' | (value.charAt(offset + 1) & 0x1F) << '\020' | (value.charAt(offset) & 0x1F) << '\030';
/*      */     }
/*  513 */     return 
/*      */     
/*      */ 
/*  516 */       (value.charAt(offset + 3) & 0x1F) << '\030' | (value.charAt(offset + 2) & 0x1F) << '\020' | (value.charAt(offset + 1) & 0x1F) << '\b' | value.charAt(offset) & 0x1F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static int hashCodeAsciiSanitizeShort(CharSequence value, int offset)
/*      */   {
/*  523 */     if (BIG_ENDIAN_NATIVE_ORDER)
/*      */     {
/*  525 */       return 
/*  526 */         value.charAt(offset + 1) & 0x1F | (value.charAt(offset) & 0x1F) << '\b';
/*      */     }
/*  528 */     return 
/*  529 */       (value.charAt(offset + 1) & 0x1F) << '\b' | value.charAt(offset) & 0x1F;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static int hashCodeAsciiSanitizeByte(char value)
/*      */   {
/*  536 */     return value & 0x1F;
/*      */   }
/*      */   
/*      */   public static void putByte(long address, byte value) {
/*  540 */     PlatformDependent0.putByte(address, value);
/*      */   }
/*      */   
/*      */   public static void putShort(long address, short value) {
/*  544 */     PlatformDependent0.putShort(address, value);
/*      */   }
/*      */   
/*      */   public static void putInt(long address, int value) {
/*  548 */     PlatformDependent0.putInt(address, value);
/*      */   }
/*      */   
/*      */   public static void putLong(long address, long value) {
/*  552 */     PlatformDependent0.putLong(address, value);
/*      */   }
/*      */   
/*      */   public static void putByte(byte[] data, int index, byte value) {
/*  556 */     PlatformDependent0.putByte(data, index, value);
/*      */   }
/*      */   
/*      */   public static void putShort(byte[] data, int index, short value) {
/*  560 */     PlatformDependent0.putShort(data, index, value);
/*      */   }
/*      */   
/*      */   public static void putInt(byte[] data, int index, int value) {
/*  564 */     PlatformDependent0.putInt(data, index, value);
/*      */   }
/*      */   
/*      */   public static void putLong(byte[] data, int index, long value) {
/*  568 */     PlatformDependent0.putLong(data, index, value);
/*      */   }
/*      */   
/*      */   public static void putObject(Object o, long offset, Object x) {
/*  572 */     PlatformDependent0.putObject(o, offset, x);
/*      */   }
/*      */   
/*      */   public static long objectFieldOffset(Field field) {
/*  576 */     return PlatformDependent0.objectFieldOffset(field);
/*      */   }
/*      */   
/*      */   public static void copyMemory(long srcAddr, long dstAddr, long length) {
/*  580 */     PlatformDependent0.copyMemory(srcAddr, dstAddr, length);
/*      */   }
/*      */   
/*      */   public static void copyMemory(byte[] src, int srcIndex, long dstAddr, long length) {
/*  584 */     PlatformDependent0.copyMemory(src, BYTE_ARRAY_BASE_OFFSET + srcIndex, null, dstAddr, length);
/*      */   }
/*      */   
/*      */   public static void copyMemory(long srcAddr, byte[] dst, int dstIndex, long length) {
/*  588 */     PlatformDependent0.copyMemory(null, srcAddr, dst, BYTE_ARRAY_BASE_OFFSET + dstIndex, length);
/*      */   }
/*      */   
/*      */   public static void setMemory(byte[] dst, int dstIndex, long bytes, byte value) {
/*  592 */     PlatformDependent0.setMemory(dst, BYTE_ARRAY_BASE_OFFSET + dstIndex, bytes, value);
/*      */   }
/*      */   
/*      */   public static void setMemory(long address, long bytes, byte value) {
/*  596 */     PlatformDependent0.setMemory(address, bytes, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuffer allocateDirectNoCleaner(int capacity)
/*      */   {
/*  604 */     assert (USE_DIRECT_BUFFER_NO_CLEANER);
/*      */     
/*  606 */     incrementMemoryCounter(capacity);
/*      */     try {
/*  608 */       return PlatformDependent0.allocateDirectNoCleaner(capacity);
/*      */     } catch (Throwable e) {
/*  610 */       decrementMemoryCounter(capacity);
/*  611 */       throwException(e); }
/*  612 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuffer reallocateDirectNoCleaner(ByteBuffer buffer, int capacity)
/*      */   {
/*  621 */     assert (USE_DIRECT_BUFFER_NO_CLEANER);
/*      */     
/*  623 */     int len = capacity - buffer.capacity();
/*  624 */     incrementMemoryCounter(len);
/*      */     try {
/*  626 */       return PlatformDependent0.reallocateDirectNoCleaner(buffer, capacity);
/*      */     } catch (Throwable e) {
/*  628 */       decrementMemoryCounter(len);
/*  629 */       throwException(e); }
/*  630 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void freeDirectNoCleaner(ByteBuffer buffer)
/*      */   {
/*  639 */     assert (USE_DIRECT_BUFFER_NO_CLEANER);
/*      */     
/*  641 */     int capacity = buffer.capacity();
/*  642 */     PlatformDependent0.freeMemory(PlatformDependent0.directBufferAddress(buffer));
/*  643 */     decrementMemoryCounter(capacity);
/*      */   }
/*      */   
/*      */   private static void incrementMemoryCounter(int capacity) {
/*  647 */     if (DIRECT_MEMORY_COUNTER != null) {
/*      */       for (;;) {
/*  649 */         long usedMemory = DIRECT_MEMORY_COUNTER.get();
/*  650 */         long newUsedMemory = usedMemory + capacity;
/*  651 */         if (newUsedMemory > DIRECT_MEMORY_LIMIT) {
/*  652 */           throw new OutOfDirectMemoryError("failed to allocate " + capacity + " byte(s) of direct memory (used: " + usedMemory + ", max: " + DIRECT_MEMORY_LIMIT + ')');
/*      */         }
/*      */         
/*  655 */         if (DIRECT_MEMORY_COUNTER.compareAndSet(usedMemory, newUsedMemory)) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static void decrementMemoryCounter(int capacity) {
/*  663 */     if (DIRECT_MEMORY_COUNTER != null) {
/*  664 */       long usedMemory = DIRECT_MEMORY_COUNTER.addAndGet(-capacity);
/*  665 */       assert (usedMemory >= 0L);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean useDirectBufferNoCleaner() {
/*  670 */     return USE_DIRECT_BUFFER_NO_CLEANER;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean equals(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length)
/*      */   {
/*  685 */     return (!hasUnsafe()) || (!PlatformDependent0.unalignedAccess()) ? 
/*  686 */       equalsSafe(bytes1, startPos1, bytes2, startPos2, length) : 
/*  687 */       PlatformDependent0.equals(bytes1, startPos1, bytes2, startPos2, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isZero(byte[] bytes, int startPos, int length)
/*      */   {
/*  698 */     return (!hasUnsafe()) || (!PlatformDependent0.unalignedAccess()) ? 
/*  699 */       isZeroSafe(bytes, startPos, length) : 
/*  700 */       PlatformDependent0.isZero(bytes, startPos, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int equalsConstantTime(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length)
/*      */   {
/*  725 */     return (!hasUnsafe()) || (!PlatformDependent0.unalignedAccess()) ? 
/*  726 */       ConstantTimeUtils.equalsConstantTime(bytes1, startPos1, bytes2, startPos2, length) : 
/*  727 */       PlatformDependent0.equalsConstantTime(bytes1, startPos1, bytes2, startPos2, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int hashCodeAscii(byte[] bytes, int startPos, int length)
/*      */   {
/*  740 */     return (!hasUnsafe()) || (!PlatformDependent0.unalignedAccess()) ? 
/*  741 */       hashCodeAsciiSafe(bytes, startPos, length) : 
/*  742 */       PlatformDependent0.hashCodeAscii(bytes, startPos, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int hashCodeAscii(CharSequence bytes)
/*      */   {
/*  756 */     int hash = -1028477387;
/*  757 */     int remainingBytes = bytes.length() & 0x7;
/*      */     
/*      */ 
/*      */ 
/*  761 */     switch (bytes.length()) {
/*      */     case 24: 
/*      */     case 25: 
/*      */     case 26: 
/*      */     case 27: 
/*      */     case 28: 
/*      */     case 29: 
/*      */     case 30: 
/*      */     case 31: 
/*  770 */       hash = hashCodeAsciiCompute(bytes, bytes.length() - 24, 
/*  771 */         hashCodeAsciiCompute(bytes, bytes.length() - 16, 
/*  772 */         hashCodeAsciiCompute(bytes, bytes.length() - 8, hash)));
/*  773 */       break;
/*      */     case 16: 
/*      */     case 17: 
/*      */     case 18: 
/*      */     case 19: 
/*      */     case 20: 
/*      */     case 21: 
/*      */     case 22: 
/*      */     case 23: 
/*  782 */       hash = hashCodeAsciiCompute(bytes, bytes.length() - 16, 
/*  783 */         hashCodeAsciiCompute(bytes, bytes.length() - 8, hash));
/*  784 */       break;
/*      */     case 8: 
/*      */     case 9: 
/*      */     case 10: 
/*      */     case 11: 
/*      */     case 12: 
/*      */     case 13: 
/*      */     case 14: 
/*      */     case 15: 
/*  793 */       hash = hashCodeAsciiCompute(bytes, bytes.length() - 8, hash);
/*  794 */       break;
/*      */     case 0: 
/*      */     case 1: 
/*      */     case 2: 
/*      */     case 3: 
/*      */     case 4: 
/*      */     case 5: 
/*      */     case 6: 
/*      */     case 7: 
/*      */       break;
/*      */     default: 
/*  805 */       for (int i = bytes.length() - 8; i >= remainingBytes; i -= 8) {
/*  806 */         hash = hashCodeAsciiCompute(bytes, i, hash);
/*      */       }
/*      */     }
/*      */     
/*  810 */     switch (remainingBytes) {
/*      */     case 7: 
/*  812 */       return 
/*      */       
/*  814 */         ((hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0))) * 461845907 + hashCodeAsciiSanitizeShort(bytes, 1)) * -862048943 + hashCodeAsciiSanitizeInt(bytes, 3);
/*      */     case 6: 
/*  816 */       return 
/*  817 */         (hash * -862048943 + hashCodeAsciiSanitizeShort(bytes, 0)) * 461845907 + hashCodeAsciiSanitizeInt(bytes, 2);
/*      */     case 5: 
/*  819 */       return 
/*  820 */         (hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0))) * 461845907 + hashCodeAsciiSanitizeInt(bytes, 1);
/*      */     case 4: 
/*  822 */       return hash * -862048943 + hashCodeAsciiSanitizeInt(bytes, 0);
/*      */     case 3: 
/*  824 */       return 
/*  825 */         (hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0))) * 461845907 + hashCodeAsciiSanitizeShort(bytes, 1);
/*      */     case 2: 
/*  827 */       return hash * -862048943 + hashCodeAsciiSanitizeShort(bytes, 0);
/*      */     case 1: 
/*  829 */       return hash * -862048943 + hashCodeAsciiSanitizeByte(bytes.charAt(0));
/*      */     }
/*  831 */     return hash;
/*      */   }
/*      */   
/*      */ 
/*      */   private static final class Mpsc
/*      */   {
/*      */     private static final boolean USE_MPSC_CHUNKED_ARRAY_QUEUE;
/*      */     
/*      */ 
/*      */     static
/*      */     {
/*  842 */       Object unsafe = null;
/*  843 */       if (PlatformDependent.hasUnsafe())
/*      */       {
/*      */ 
/*      */ 
/*  847 */         unsafe = AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run()
/*      */           {
/*  851 */             return UnsafeAccess.UNSAFE;
/*      */           }
/*      */         });
/*      */       }
/*      */       
/*  856 */       if (unsafe == null) {
/*  857 */         PlatformDependent.logger.debug("org.jctools-core.MpscChunkedArrayQueue: unavailable");
/*  858 */         USE_MPSC_CHUNKED_ARRAY_QUEUE = false;
/*      */       } else {
/*  860 */         PlatformDependent.logger.debug("org.jctools-core.MpscChunkedArrayQueue: available");
/*  861 */         USE_MPSC_CHUNKED_ARRAY_QUEUE = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     static <T> Queue<T> newMpscQueue(int maxCapacity)
/*      */     {
/*  869 */       int capacity = Math.max(Math.min(maxCapacity, 1073741824), 2048);
/*  870 */       return USE_MPSC_CHUNKED_ARRAY_QUEUE ? new MpscChunkedArrayQueue(1024, capacity) : new MpscGrowableAtomicArrayQueue(1024, capacity);
/*      */     }
/*      */     
/*      */     static <T> Queue<T> newMpscQueue()
/*      */     {
/*  875 */       return USE_MPSC_CHUNKED_ARRAY_QUEUE ? new MpscUnboundedArrayQueue(1024) : new MpscUnboundedAtomicArrayQueue(1024);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Queue<T> newMpscQueue()
/*      */   {
/*  886 */     return Mpsc.newMpscQueue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Queue<T> newMpscQueue(int maxCapacity)
/*      */   {
/*  894 */     return Mpsc.newMpscQueue(maxCapacity);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Queue<T> newSpscQueue()
/*      */   {
/*  902 */     return hasUnsafe() ? new SpscLinkedQueue() : new SpscLinkedAtomicQueue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Queue<T> newFixedMpscQueue(int capacity)
/*      */   {
/*  910 */     return hasUnsafe() ? new MpscArrayQueue(capacity) : new MpscAtomicArrayQueue(capacity);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static ClassLoader getClassLoader(Class<?> clazz)
/*      */   {
/*  917 */     return PlatformDependent0.getClassLoader(clazz);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static ClassLoader getContextClassLoader()
/*      */   {
/*  924 */     return PlatformDependent0.getContextClassLoader();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static ClassLoader getSystemClassLoader()
/*      */   {
/*  931 */     return PlatformDependent0.getSystemClassLoader();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static <C> Deque<C> newConcurrentDeque()
/*      */   {
/*  938 */     if (javaVersion() < 7) {
/*  939 */       return new LinkedBlockingDeque();
/*      */     }
/*  941 */     return new ConcurrentLinkedDeque();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Random threadLocalRandom()
/*      */   {
/*  949 */     return RANDOM_PROVIDER.current();
/*      */   }
/*      */   
/*      */   private static boolean isWindows0() {
/*  953 */     boolean windows = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
/*  954 */     if (windows) {
/*  955 */       logger.debug("Platform: Windows");
/*      */     }
/*  957 */     return windows;
/*      */   }
/*      */   
/*      */   private static boolean isOsx0()
/*      */   {
/*  962 */     String osname = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
/*  963 */     boolean osx = (osname.startsWith("macosx")) || (osname.startsWith("osx"));
/*      */     
/*  965 */     if (osx) {
/*  966 */       logger.debug("Platform: MacOS");
/*      */     }
/*  968 */     return osx;
/*      */   }
/*      */   
/*      */   private static boolean maybeSuperUser0() {
/*  972 */     String username = SystemPropertyUtil.get("user.name");
/*  973 */     if (isWindows()) {
/*  974 */       return "Administrator".equals(username);
/*      */     }
/*      */     
/*  977 */     return ("root".equals(username)) || ("toor".equals(username));
/*      */   }
/*      */   
/*      */   private static Throwable unsafeUnavailabilityCause0() {
/*  981 */     if (isAndroid()) {
/*  982 */       logger.debug("sun.misc.Unsafe: unavailable (Android)");
/*  983 */       return new UnsupportedOperationException("sun.misc.Unsafe: unavailable (Android)");
/*      */     }
/*  985 */     Throwable cause = PlatformDependent0.getUnsafeUnavailabilityCause();
/*  986 */     if (cause != null) {
/*  987 */       return cause;
/*      */     }
/*      */     try
/*      */     {
/*  991 */       boolean hasUnsafe = PlatformDependent0.hasUnsafe();
/*  992 */       logger.debug("sun.misc.Unsafe: {}", hasUnsafe ? "available" : "unavailable");
/*  993 */       return hasUnsafe ? null : PlatformDependent0.getUnsafeUnavailabilityCause();
/*      */     } catch (Throwable t) {
/*  995 */       logger.trace("Could not determine if Unsafe is available", t);
/*      */       
/*  997 */       return new UnsupportedOperationException("Could not determine if Unsafe is available", t);
/*      */     }
/*      */   }
/*      */   
/*      */   private static long maxDirectMemory0() {
/* 1002 */     long maxDirectMemory = 0L;
/*      */     
/* 1004 */     ClassLoader systemClassLoader = null;
/*      */     try {
/* 1006 */       systemClassLoader = getSystemClassLoader();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1012 */       String vmName = SystemPropertyUtil.get("java.vm.name", "").toLowerCase();
/* 1013 */       if (!vmName.startsWith("ibm j9"))
/*      */       {
/* 1015 */         if (!vmName.startsWith("eclipse openj9"))
/*      */         {
/* 1017 */           Class<?> vmClass = Class.forName("sun.misc.VM", true, systemClassLoader);
/* 1018 */           Method m = vmClass.getDeclaredMethod("maxDirectMemory", new Class[0]);
/* 1019 */           maxDirectMemory = ((Number)m.invoke(null, new Object[0])).longValue();
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable) {}
/*      */     
/* 1025 */     if (maxDirectMemory > 0L) {
/* 1026 */       return maxDirectMemory;
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/* 1032 */       Class<?> mgmtFactoryClass = Class.forName("java.lang.management.ManagementFactory", true, systemClassLoader);
/*      */       
/* 1034 */       Class<?> runtimeClass = Class.forName("java.lang.management.RuntimeMXBean", true, systemClassLoader);
/*      */       
/*      */ 
/* 1037 */       Object runtime = mgmtFactoryClass.getDeclaredMethod("getRuntimeMXBean", new Class[0]).invoke(null, new Object[0]);
/*      */       
/*      */ 
/* 1040 */       List<String> vmArgs = (List)runtimeClass.getDeclaredMethod("getInputArguments", new Class[0]).invoke(runtime, new Object[0]);
/* 1041 */       for (int i = vmArgs.size() - 1; i >= 0; i--) {
/* 1042 */         Matcher m = MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN.matcher((CharSequence)vmArgs.get(i));
/* 1043 */         if (m.matches())
/*      */         {
/*      */ 
/*      */ 
/* 1047 */           maxDirectMemory = Long.parseLong(m.group(1));
/* 1048 */           switch (m.group(2).charAt(0)) {
/*      */           case 'K': case 'k': 
/* 1050 */             maxDirectMemory *= 1024L;
/* 1051 */             break;
/*      */           case 'M': case 'm': 
/* 1053 */             maxDirectMemory *= 1048576L;
/* 1054 */             break;
/*      */           case 'G': case 'g': 
/* 1056 */             maxDirectMemory *= 1073741824L;
/*      */           }
/*      */           
/* 1059 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable1) {}
/*      */     
/* 1065 */     if (maxDirectMemory <= 0L) {
/* 1066 */       maxDirectMemory = Runtime.getRuntime().maxMemory();
/* 1067 */       logger.debug("maxDirectMemory: {} bytes (maybe)", Long.valueOf(maxDirectMemory));
/*      */     } else {
/* 1069 */       logger.debug("maxDirectMemory: {} bytes", Long.valueOf(maxDirectMemory));
/*      */     }
/*      */     
/* 1072 */     return maxDirectMemory;
/*      */   }
/*      */   
/*      */   private static File tmpdir0()
/*      */   {
/*      */     try {
/* 1078 */       File f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
/* 1079 */       if (f != null) {
/* 1080 */         logger.debug("-Dio.netty.tmpdir: {}", f);
/* 1081 */         return f;
/*      */       }
/*      */       
/* 1084 */       f = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
/* 1085 */       if (f != null) {
/* 1086 */         logger.debug("-Dio.netty.tmpdir: {} (java.io.tmpdir)", f);
/* 1087 */         return f;
/*      */       }
/*      */       
/*      */ 
/* 1091 */       if (isWindows()) {
/* 1092 */         f = toDirectory(System.getenv("TEMP"));
/* 1093 */         if (f != null) {
/* 1094 */           logger.debug("-Dio.netty.tmpdir: {} (%TEMP%)", f);
/* 1095 */           return f;
/*      */         }
/*      */         
/* 1098 */         String userprofile = System.getenv("USERPROFILE");
/* 1099 */         if (userprofile != null) {
/* 1100 */           f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
/* 1101 */           if (f != null) {
/* 1102 */             logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\AppData\\Local\\Temp)", f);
/* 1103 */             return f;
/*      */           }
/*      */           
/* 1106 */           f = toDirectory(userprofile + "\\Local Settings\\Temp");
/* 1107 */           if (f != null) {
/* 1108 */             logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\Local Settings\\Temp)", f);
/* 1109 */             return f;
/*      */           }
/*      */         }
/*      */       } else {
/* 1113 */         f = toDirectory(System.getenv("TMPDIR"));
/* 1114 */         if (f != null) {
/* 1115 */           logger.debug("-Dio.netty.tmpdir: {} ($TMPDIR)", f);
/* 1116 */           return f;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable) {}
/*      */     
/*      */     File f;
/*      */     File f;
/* 1124 */     if (isWindows()) {
/* 1125 */       f = new File("C:\\Windows\\Temp");
/*      */     } else {
/* 1127 */       f = new File("/tmp");
/*      */     }
/*      */     
/* 1130 */     logger.warn("Failed to get the temporary directory; falling back to: {}", f);
/* 1131 */     return f;
/*      */   }
/*      */   
/*      */   private static File toDirectory(String path)
/*      */   {
/* 1136 */     if (path == null) {
/* 1137 */       return null;
/*      */     }
/*      */     
/* 1140 */     File f = new File(path);
/* 1141 */     f.mkdirs();
/*      */     
/* 1143 */     if (!f.isDirectory()) {
/* 1144 */       return null;
/*      */     }
/*      */     try
/*      */     {
/* 1148 */       return f.getAbsoluteFile();
/*      */     } catch (Exception ignored) {}
/* 1150 */     return f;
/*      */   }
/*      */   
/*      */ 
/*      */   private static int bitMode0()
/*      */   {
/* 1156 */     int bitMode = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
/* 1157 */     if (bitMode > 0) {
/* 1158 */       logger.debug("-Dio.netty.bitMode: {}", Integer.valueOf(bitMode));
/* 1159 */       return bitMode;
/*      */     }
/*      */     
/*      */ 
/* 1163 */     bitMode = SystemPropertyUtil.getInt("sun.arch.data.model", 0);
/* 1164 */     if (bitMode > 0) {
/* 1165 */       logger.debug("-Dio.netty.bitMode: {} (sun.arch.data.model)", Integer.valueOf(bitMode));
/* 1166 */       return bitMode;
/*      */     }
/* 1168 */     bitMode = SystemPropertyUtil.getInt("com.ibm.vm.bitmode", 0);
/* 1169 */     if (bitMode > 0) {
/* 1170 */       logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", Integer.valueOf(bitMode));
/* 1171 */       return bitMode;
/*      */     }
/*      */     
/*      */ 
/* 1175 */     String arch = SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim();
/* 1176 */     if (("amd64".equals(arch)) || ("x86_64".equals(arch))) {
/* 1177 */       bitMode = 64;
/* 1178 */     } else if (("i386".equals(arch)) || ("i486".equals(arch)) || ("i586".equals(arch)) || ("i686".equals(arch))) {
/* 1179 */       bitMode = 32;
/*      */     }
/*      */     
/* 1182 */     if (bitMode > 0) {
/* 1183 */       logger.debug("-Dio.netty.bitMode: {} (os.arch: {})", Integer.valueOf(bitMode), arch);
/*      */     }
/*      */     
/*      */ 
/* 1187 */     String vm = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
/* 1188 */     Pattern BIT_PATTERN = Pattern.compile("([1-9][0-9]+)-?bit");
/* 1189 */     Matcher m = BIT_PATTERN.matcher(vm);
/* 1190 */     if (m.find()) {
/* 1191 */       return Integer.parseInt(m.group(1));
/*      */     }
/* 1193 */     return 64;
/*      */   }
/*      */   
/*      */   private static int addressSize0()
/*      */   {
/* 1198 */     if (!hasUnsafe()) {
/* 1199 */       return -1;
/*      */     }
/* 1201 */     return PlatformDependent0.addressSize();
/*      */   }
/*      */   
/*      */   private static long byteArrayBaseOffset0() {
/* 1205 */     if (!hasUnsafe()) {
/* 1206 */       return -1L;
/*      */     }
/* 1208 */     return PlatformDependent0.byteArrayBaseOffset();
/*      */   }
/*      */   
/*      */   private static boolean equalsSafe(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
/* 1212 */     int end = startPos1 + length;
/* 1213 */     for (; startPos1 < end; startPos2++) {
/* 1214 */       if (bytes1[startPos1] != bytes2[startPos2]) {
/* 1215 */         return false;
/*      */       }
/* 1213 */       startPos1++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1218 */     return true;
/*      */   }
/*      */   
/*      */   private static boolean isZeroSafe(byte[] bytes, int startPos, int length) {
/* 1222 */     int end = startPos + length;
/* 1223 */     for (; startPos < end; startPos++) {
/* 1224 */       if (bytes[startPos] != 0) {
/* 1225 */         return false;
/*      */       }
/*      */     }
/* 1228 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static int hashCodeAsciiSafe(byte[] bytes, int startPos, int length)
/*      */   {
/* 1235 */     int hash = -1028477387;
/* 1236 */     int remainingBytes = length & 0x7;
/* 1237 */     int end = startPos + remainingBytes;
/* 1238 */     for (int i = startPos - 8 + length; i >= end; i -= 8) {
/* 1239 */       hash = PlatformDependent0.hashCodeAsciiCompute(getLongSafe(bytes, i), hash);
/*      */     }
/* 1241 */     switch (remainingBytes) {
/*      */     case 7: 
/* 1243 */       return 
/*      */       
/* 1245 */         ((hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos + 1))) * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 3));
/*      */     case 6: 
/* 1247 */       return 
/* 1248 */         (hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos))) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 2));
/*      */     case 5: 
/* 1250 */       return 
/* 1251 */         (hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos + 1));
/*      */     case 4: 
/* 1253 */       return hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(getIntSafe(bytes, startPos));
/*      */     case 3: 
/* 1255 */       return 
/* 1256 */         (hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos])) * 461845907 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos + 1));
/*      */     case 2: 
/* 1258 */       return hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(getShortSafe(bytes, startPos));
/*      */     case 1: 
/* 1260 */       return hash * -862048943 + PlatformDependent0.hashCodeAsciiSanitize(bytes[startPos]);
/*      */     }
/* 1262 */     return hash;
/*      */   }
/*      */   
/*      */   public static String normalizedArch()
/*      */   {
/* 1267 */     return NORMALIZED_ARCH;
/*      */   }
/*      */   
/*      */   public static String normalizedOs() {
/* 1271 */     return NORMALIZED_OS;
/*      */   }
/*      */   
/*      */   private static String normalize(String value) {
/* 1275 */     return value.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
/*      */   }
/*      */   
/*      */   private static String normalizeArch(String value) {
/* 1279 */     value = normalize(value);
/* 1280 */     if (value.matches("^(x8664|amd64|ia32e|em64t|x64)$")) {
/* 1281 */       return "x86_64";
/*      */     }
/* 1283 */     if (value.matches("^(x8632|x86|i[3-6]86|ia32|x32)$")) {
/* 1284 */       return "x86_32";
/*      */     }
/* 1286 */     if (value.matches("^(ia64|itanium64)$")) {
/* 1287 */       return "itanium_64";
/*      */     }
/* 1289 */     if (value.matches("^(sparc|sparc32)$")) {
/* 1290 */       return "sparc_32";
/*      */     }
/* 1292 */     if (value.matches("^(sparcv9|sparc64)$")) {
/* 1293 */       return "sparc_64";
/*      */     }
/* 1295 */     if (value.matches("^(arm|arm32)$")) {
/* 1296 */       return "arm_32";
/*      */     }
/* 1298 */     if ("aarch64".equals(value)) {
/* 1299 */       return "aarch_64";
/*      */     }
/* 1301 */     if (value.matches("^(ppc|ppc32)$")) {
/* 1302 */       return "ppc_32";
/*      */     }
/* 1304 */     if ("ppc64".equals(value)) {
/* 1305 */       return "ppc_64";
/*      */     }
/* 1307 */     if ("ppc64le".equals(value)) {
/* 1308 */       return "ppcle_64";
/*      */     }
/* 1310 */     if ("s390".equals(value)) {
/* 1311 */       return "s390_32";
/*      */     }
/* 1313 */     if ("s390x".equals(value)) {
/* 1314 */       return "s390_64";
/*      */     }
/*      */     
/* 1317 */     return "unknown";
/*      */   }
/*      */   
/*      */   private static String normalizeOs(String value) {
/* 1321 */     value = normalize(value);
/* 1322 */     if (value.startsWith("aix")) {
/* 1323 */       return "aix";
/*      */     }
/* 1325 */     if (value.startsWith("hpux")) {
/* 1326 */       return "hpux";
/*      */     }
/* 1328 */     if (value.startsWith("os400"))
/*      */     {
/* 1330 */       if ((value.length() <= 5) || (!Character.isDigit(value.charAt(5)))) {
/* 1331 */         return "os400";
/*      */       }
/*      */     }
/* 1334 */     if (value.startsWith("linux")) {
/* 1335 */       return "linux";
/*      */     }
/* 1337 */     if ((value.startsWith("macosx")) || (value.startsWith("osx"))) {
/* 1338 */       return "osx";
/*      */     }
/* 1340 */     if (value.startsWith("freebsd")) {
/* 1341 */       return "freebsd";
/*      */     }
/* 1343 */     if (value.startsWith("openbsd")) {
/* 1344 */       return "openbsd";
/*      */     }
/* 1346 */     if (value.startsWith("netbsd")) {
/* 1347 */       return "netbsd";
/*      */     }
/* 1349 */     if ((value.startsWith("solaris")) || (value.startsWith("sunos"))) {
/* 1350 */       return "sunos";
/*      */     }
/* 1352 */     if (value.startsWith("windows")) {
/* 1353 */       return "windows";
/*      */     }
/*      */     
/* 1356 */     return "unknown";
/*      */   }
/*      */   
/*      */   private static final class AtomicLongCounter extends AtomicLong implements LongCounter
/*      */   {
/*      */     private static final long serialVersionUID = 4074772784610639305L;
/*      */     
/*      */     public void add(long delta) {
/* 1364 */       addAndGet(delta);
/*      */     }
/*      */     
/*      */     public void increment()
/*      */     {
/* 1369 */       incrementAndGet();
/*      */     }
/*      */     
/*      */     public void decrement()
/*      */     {
/* 1374 */       decrementAndGet();
/*      */     }
/*      */     
/*      */     public long value()
/*      */     {
/* 1379 */       return get();
/*      */     }
/*      */   }
/*      */   
/*      */   private static abstract interface ThreadLocalRandomProvider
/*      */   {
/*      */     public abstract Random current();
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\PlatformDependent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */