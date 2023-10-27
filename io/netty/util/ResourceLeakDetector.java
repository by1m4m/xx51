/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResourceLeakDetector<T>
/*     */ {
/*     */   private static final String PROP_LEVEL_OLD = "io.netty.leakDetectionLevel";
/*     */   private static final String PROP_LEVEL = "io.netty.leakDetection.level";
/*  44 */   private static final Level DEFAULT_LEVEL = Level.SIMPLE;
/*     */   
/*     */ 
/*     */   private static final String PROP_TARGET_RECORDS = "io.netty.leakDetection.targetRecords";
/*     */   
/*     */   private static final int DEFAULT_TARGET_RECORDS = 4;
/*     */   
/*     */   private static final int TARGET_RECORDS;
/*     */   
/*     */   private static Level level;
/*     */   
/*     */ 
/*     */   public static enum Level
/*     */   {
/*  58 */     DISABLED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  63 */     SIMPLE, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  68 */     ADVANCED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  73 */     PARANOID;
/*     */     
/*     */ 
/*     */ 
/*     */     private Level() {}
/*     */     
/*     */ 
/*     */     static Level parseLevel(String levelStr)
/*     */     {
/*  82 */       String trimmedLevelStr = levelStr.trim();
/*  83 */       for (Level l : values()) {
/*  84 */         if ((trimmedLevelStr.equalsIgnoreCase(l.name())) || (trimmedLevelStr.equals(String.valueOf(l.ordinal())))) {
/*  85 */           return l;
/*     */         }
/*     */       }
/*  88 */       return ResourceLeakDetector.DEFAULT_LEVEL;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  94 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ResourceLeakDetector.class);
/*     */   static final int DEFAULT_SAMPLING_INTERVAL = 128;
/*     */   
/*     */   static { boolean disabled;
/*  98 */     if (SystemPropertyUtil.get("io.netty.noResourceLeakDetection") != null) {
/*  99 */       boolean disabled = SystemPropertyUtil.getBoolean("io.netty.noResourceLeakDetection", false);
/* 100 */       logger.debug("-Dio.netty.noResourceLeakDetection: {}", Boolean.valueOf(disabled));
/* 101 */       logger.warn("-Dio.netty.noResourceLeakDetection is deprecated. Use '-D{}={}' instead.", "io.netty.leakDetection.level", DEFAULT_LEVEL
/*     */       
/* 103 */         .name().toLowerCase());
/*     */     } else {
/* 105 */       disabled = false;
/*     */     }
/*     */     
/* 108 */     Level defaultLevel = disabled ? Level.DISABLED : DEFAULT_LEVEL;
/*     */     
/*     */ 
/* 111 */     String levelStr = SystemPropertyUtil.get("io.netty.leakDetectionLevel", defaultLevel.name());
/*     */     
/*     */ 
/* 114 */     levelStr = SystemPropertyUtil.get("io.netty.leakDetection.level", levelStr);
/* 115 */     Level level = Level.parseLevel(levelStr);
/*     */     
/* 117 */     TARGET_RECORDS = SystemPropertyUtil.getInt("io.netty.leakDetection.targetRecords", 4);
/*     */     
/* 119 */     level = level;
/* 120 */     if (logger.isDebugEnabled()) {
/* 121 */       logger.debug("-D{}: {}", "io.netty.leakDetection.level", level.name().toLowerCase());
/* 122 */       logger.debug("-D{}: {}", "io.netty.leakDetection.targetRecords", Integer.valueOf(TARGET_RECORDS));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static void setEnabled(boolean enabled)
/*     */   {
/* 134 */     setLevel(enabled ? Level.SIMPLE : Level.DISABLED);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isEnabled()
/*     */   {
/* 141 */     return getLevel().ordinal() > Level.DISABLED.ordinal();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void setLevel(Level level)
/*     */   {
/* 148 */     if (level == null) {
/* 149 */       throw new NullPointerException("level");
/*     */     }
/* 151 */     level = level;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Level getLevel()
/*     */   {
/* 158 */     return level;
/*     */   }
/*     */   
/*     */ 
/* 162 */   private final ConcurrentMap<DefaultResourceLeak<?>, LeakEntry> allLeaks = PlatformDependent.newConcurrentHashMap();
/*     */   
/* 164 */   private final ReferenceQueue<Object> refQueue = new ReferenceQueue();
/* 165 */   private final ConcurrentMap<String, Boolean> reportedLeaks = PlatformDependent.newConcurrentHashMap();
/*     */   
/*     */   private final String resourceType;
/*     */   
/*     */   private final int samplingInterval;
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public ResourceLeakDetector(Class<?> resourceType)
/*     */   {
/* 175 */     this(StringUtil.simpleClassName(resourceType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public ResourceLeakDetector(String resourceType)
/*     */   {
/* 183 */     this(resourceType, 128, Long.MAX_VALUE);
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
/*     */   @Deprecated
/*     */   public ResourceLeakDetector(Class<?> resourceType, int samplingInterval, long maxActive)
/*     */   {
/* 197 */     this(resourceType, samplingInterval);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceLeakDetector(Class<?> resourceType, int samplingInterval)
/*     */   {
/* 207 */     this(StringUtil.simpleClassName(resourceType), samplingInterval, Long.MAX_VALUE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public ResourceLeakDetector(String resourceType, int samplingInterval, long maxActive)
/*     */   {
/* 217 */     if (resourceType == null) {
/* 218 */       throw new NullPointerException("resourceType");
/*     */     }
/*     */     
/* 221 */     this.resourceType = resourceType;
/* 222 */     this.samplingInterval = samplingInterval;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final ResourceLeak open(T obj)
/*     */   {
/* 234 */     return track0(obj);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ResourceLeakTracker<T> track(T obj)
/*     */   {
/* 245 */     return track0(obj);
/*     */   }
/*     */   
/*     */   private DefaultResourceLeak track0(T obj)
/*     */   {
/* 250 */     Level level = level;
/* 251 */     if (level == Level.DISABLED) {
/* 252 */       return null;
/*     */     }
/*     */     
/* 255 */     if (level.ordinal() < Level.PARANOID.ordinal()) {
/* 256 */       if (PlatformDependent.threadLocalRandom().nextInt(this.samplingInterval) == 0) {
/* 257 */         reportLeak();
/* 258 */         return new DefaultResourceLeak(obj, this.refQueue, this.allLeaks);
/*     */       }
/* 260 */       return null;
/*     */     }
/* 262 */     reportLeak();
/* 263 */     return new DefaultResourceLeak(obj, this.refQueue, this.allLeaks);
/*     */   }
/*     */   
/*     */   private void clearRefQueue()
/*     */   {
/*     */     for (;;) {
/* 269 */       DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll();
/* 270 */       if (ref == null) {
/*     */         break;
/*     */       }
/* 273 */       ref.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */   private void reportLeak() {
/* 278 */     if (!logger.isErrorEnabled()) {
/* 279 */       clearRefQueue();
/*     */     }
/*     */     else
/*     */     {
/*     */       for (;;)
/*     */       {
/*     */ 
/* 286 */         DefaultResourceLeak ref = (DefaultResourceLeak)this.refQueue.poll();
/* 287 */         if (ref == null) {
/*     */           break;
/*     */         }
/*     */         
/* 291 */         if (ref.dispose())
/*     */         {
/*     */ 
/*     */ 
/* 295 */           String records = ref.toString();
/* 296 */           if (this.reportedLeaks.putIfAbsent(records, Boolean.TRUE) == null) {
/* 297 */             if (records.isEmpty()) {
/* 298 */               reportUntracedLeak(this.resourceType);
/*     */             } else {
/* 300 */               reportTracedLeak(this.resourceType, records);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void reportTracedLeak(String resourceType, String records)
/*     */   {
/* 311 */     logger.error("LEAK: {}.release() was not called before it's garbage-collected. See http://netty.io/wiki/reference-counted-objects.html for more information.{}", resourceType, records);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void reportUntracedLeak(String resourceType)
/*     */   {
/* 322 */     logger.error("LEAK: {}.release() was not called before it's garbage-collected. Enable advanced leak reporting to find out where the leak occurred. To enable advanced leak reporting, specify the JVM option '-D{}={}' or call {}.setLevel() See http://netty.io/wiki/reference-counted-objects.html for more information.", new Object[] { resourceType, "io.netty.leakDetection.level", Level.ADVANCED
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 327 */       .name().toLowerCase(), StringUtil.simpleClassName(this) });
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
/*     */   private static final class DefaultResourceLeak<T>
/*     */     extends WeakReference<Object>
/*     */     implements ResourceLeakTracker<T>, ResourceLeak
/*     */   {
/* 344 */     private static final AtomicReferenceFieldUpdater<DefaultResourceLeak<?>, ResourceLeakDetector.Record> headUpdater = AtomicReferenceFieldUpdater.newUpdater(DefaultResourceLeak.class, ResourceLeakDetector.Record.class, "head");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 349 */     private static final AtomicIntegerFieldUpdater<DefaultResourceLeak<?>> droppedRecordsUpdater = AtomicIntegerFieldUpdater.newUpdater(DefaultResourceLeak.class, "droppedRecords");
/*     */     
/*     */ 
/*     */     private volatile ResourceLeakDetector.Record head;
/*     */     
/*     */     private volatile int droppedRecords;
/*     */     
/*     */     private final ConcurrentMap<DefaultResourceLeak<?>, ResourceLeakDetector.LeakEntry> allLeaks;
/*     */     
/*     */     private final int trackedHash;
/*     */     
/*     */ 
/*     */     DefaultResourceLeak(Object referent, ReferenceQueue<Object> refQueue, ConcurrentMap<DefaultResourceLeak<?>, ResourceLeakDetector.LeakEntry> allLeaks)
/*     */     {
/* 363 */       super(refQueue);
/*     */       
/* 365 */       assert (referent != null);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 370 */       this.trackedHash = System.identityHashCode(referent);
/* 371 */       allLeaks.put(this, ResourceLeakDetector.LeakEntry.INSTANCE);
/*     */       
/* 373 */       headUpdater.set(this, new ResourceLeakDetector.Record(ResourceLeakDetector.Record.BOTTOM));
/* 374 */       this.allLeaks = allLeaks;
/*     */     }
/*     */     
/*     */     public void record()
/*     */     {
/* 379 */       record0(null);
/*     */     }
/*     */     
/*     */     public void record(Object hint)
/*     */     {
/* 384 */       record0(hint);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void record0(Object hint)
/*     */     {
/* 415 */       if (ResourceLeakDetector.TARGET_RECORDS > 0) {
/*     */         ResourceLeakDetector.Record oldHead;
/*     */         boolean dropped;
/*     */         ResourceLeakDetector.Record newHead;
/*     */         do {
/*     */           ResourceLeakDetector.Record prevHead;
/* 421 */           if ((prevHead = oldHead = (ResourceLeakDetector.Record)headUpdater.get(this)) == null)
/*     */           {
/* 423 */             return;
/*     */           }
/* 425 */           int numElements = oldHead.pos + 1;
/* 426 */           if (numElements >= ResourceLeakDetector.TARGET_RECORDS) {
/* 427 */             int backOffFactor = Math.min(numElements - ResourceLeakDetector.TARGET_RECORDS, 30);
/* 428 */             boolean dropped; if ((dropped = PlatformDependent.threadLocalRandom().nextInt(1 << backOffFactor) != 0 ? 1 : 0) != 0) {
/* 429 */               prevHead = oldHead.next;
/*     */             }
/*     */           } else {
/* 432 */             dropped = false;
/*     */           }
/* 434 */           newHead = hint != null ? new ResourceLeakDetector.Record(prevHead, hint) : new ResourceLeakDetector.Record(prevHead);
/* 435 */         } while (!headUpdater.compareAndSet(this, oldHead, newHead));
/* 436 */         if (dropped) {
/* 437 */           droppedRecordsUpdater.incrementAndGet(this);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     boolean dispose() {
/* 443 */       clear();
/* 444 */       return this.allLeaks.remove(this, ResourceLeakDetector.LeakEntry.INSTANCE);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean close()
/*     */     {
/* 450 */       if (this.allLeaks.remove(this, ResourceLeakDetector.LeakEntry.INSTANCE))
/*     */       {
/* 452 */         clear();
/* 453 */         headUpdater.set(this, null);
/* 454 */         return true;
/*     */       }
/* 456 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean close(T trackedObject)
/*     */     {
/* 462 */       assert (this.trackedHash == System.identityHashCode(trackedObject));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 468 */       return (close()) && (trackedObject != null);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 473 */       ResourceLeakDetector.Record oldHead = (ResourceLeakDetector.Record)headUpdater.getAndSet(this, null);
/* 474 */       if (oldHead == null)
/*     */       {
/* 476 */         return "";
/*     */       }
/*     */       
/* 479 */       int dropped = droppedRecordsUpdater.get(this);
/* 480 */       int duped = 0;
/*     */       
/* 482 */       int present = oldHead.pos + 1;
/*     */       
/* 484 */       StringBuilder buf = new StringBuilder(present * 2048).append(StringUtil.NEWLINE);
/* 485 */       buf.append("Recent access records: ").append(StringUtil.NEWLINE);
/*     */       
/* 487 */       int i = 1;
/* 488 */       Set<String> seen = new HashSet(present);
/* 489 */       for (; oldHead != ResourceLeakDetector.Record.BOTTOM; oldHead = oldHead.next) {
/* 490 */         String s = oldHead.toString();
/* 491 */         if (seen.add(s)) {
/* 492 */           if (oldHead.next == ResourceLeakDetector.Record.BOTTOM) {
/* 493 */             buf.append("Created at:").append(StringUtil.NEWLINE).append(s);
/*     */           } else {
/* 495 */             buf.append('#').append(i++).append(':').append(StringUtil.NEWLINE).append(s);
/*     */           }
/*     */         } else {
/* 498 */           duped++;
/*     */         }
/*     */       }
/*     */       
/* 502 */       if (duped > 0)
/*     */       {
/*     */ 
/*     */ 
/* 506 */         buf.append(": ").append(dropped).append(" leak records were discarded because they were duplicates").append(StringUtil.NEWLINE);
/*     */       }
/*     */       
/* 509 */       if (dropped > 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 517 */         buf.append(": ").append(dropped).append(" leak records were discarded because the leak record count is targeted to ").append(ResourceLeakDetector.TARGET_RECORDS).append(". Use system property ").append("io.netty.leakDetection.targetRecords").append(" to increase the limit.").append(StringUtil.NEWLINE);
/*     */       }
/*     */       
/* 520 */       buf.setLength(buf.length() - StringUtil.NEWLINE.length());
/* 521 */       return buf.toString();
/*     */     }
/*     */   }
/*     */   
/* 525 */   private static final AtomicReference<String[]> excludedMethods = new AtomicReference(EmptyArrays.EMPTY_STRINGS);
/*     */   
/*     */   public static void addExclusions(Class clz, String... methodNames)
/*     */   {
/* 529 */     Set<String> nameSet = new HashSet(Arrays.asList(methodNames));
/*     */     
/*     */ 
/* 532 */     for (Method method : clz.getDeclaredMethods()) {
/* 533 */       if ((nameSet.remove(method.getName())) && (nameSet.isEmpty())) {
/*     */         break;
/*     */       }
/*     */     }
/* 537 */     if (!nameSet.isEmpty()) {
/* 538 */       throw new IllegalArgumentException("Can't find '" + nameSet + "' in " + clz.getName());
/*     */     }
/*     */     String[] oldMethods;
/*     */     String[] newMethods;
/*     */     do {
/* 543 */       oldMethods = (String[])excludedMethods.get();
/* 544 */       newMethods = (String[])Arrays.copyOf(oldMethods, oldMethods.length + 2 * methodNames.length);
/* 545 */       for (int i = 0; i < methodNames.length; i++) {
/* 546 */         newMethods[(oldMethods.length + i * 2)] = clz.getName();
/* 547 */         newMethods[(oldMethods.length + i * 2 + 1)] = methodNames[i];
/*     */       }
/* 549 */     } while (!excludedMethods.compareAndSet(oldMethods, newMethods)); }
/*     */   
/*     */   @Deprecated
/*     */   protected void reportInstancesLeak(String resourceType) {}
/*     */   
/*     */   private static final class Record extends Throwable { private static final long serialVersionUID = 6065153674892850720L;
/* 555 */     private static final Record BOTTOM = new Record();
/*     */     
/*     */     private final String hintString;
/*     */     private final Record next;
/*     */     private final int pos;
/*     */     
/*     */     Record(Record next, Object hint)
/*     */     {
/* 563 */       this.hintString = ((hint instanceof ResourceLeakHint) ? ((ResourceLeakHint)hint).toHintString() : hint.toString());
/* 564 */       this.next = next;
/* 565 */       next.pos += 1;
/*     */     }
/*     */     
/*     */     Record(Record next) {
/* 569 */       this.hintString = null;
/* 570 */       this.next = next;
/* 571 */       next.pos += 1;
/*     */     }
/*     */     
/*     */     private Record()
/*     */     {
/* 576 */       this.hintString = null;
/* 577 */       this.next = null;
/* 578 */       this.pos = -1;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 583 */       StringBuilder buf = new StringBuilder(2048);
/* 584 */       if (this.hintString != null) {
/* 585 */         buf.append("\tHint: ").append(this.hintString).append(StringUtil.NEWLINE);
/*     */       }
/*     */       
/*     */ 
/* 589 */       StackTraceElement[] array = getStackTrace();
/*     */       label146:
/* 591 */       for (int i = 3; i < array.length; i++) {
/* 592 */         StackTraceElement element = array[i];
/*     */         
/* 594 */         String[] exclusions = (String[])ResourceLeakDetector.excludedMethods.get();
/* 595 */         for (int k = 0; k < exclusions.length; k += 2) {
/* 596 */           if ((exclusions[k].equals(element.getClassName())) && 
/* 597 */             (exclusions[(k + 1)].equals(element.getMethodName()))) {
/*     */             break label146;
/*     */           }
/*     */         }
/*     */         
/* 602 */         buf.append('\t');
/* 603 */         buf.append(element.toString());
/* 604 */         buf.append(StringUtil.NEWLINE);
/*     */       }
/* 606 */       return buf.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LeakEntry {
/* 611 */     static final LeakEntry INSTANCE = new LeakEntry();
/* 612 */     private static final int HASH = System.identityHashCode(INSTANCE);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 619 */       return HASH;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 624 */       return obj == this;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\ResourceLeakDetector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */