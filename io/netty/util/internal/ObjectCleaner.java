/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.concurrent.FastThreadLocalThread;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObjectCleaner
/*     */ {
/*  36 */   private static final int REFERENCE_QUEUE_POLL_TIMEOUT_MS = Math.max(500, SystemPropertyUtil.getInt("io.netty.util.internal.ObjectCleaner.refQueuePollTimeout", 10000));
/*     */   
/*     */ 
/*  39 */   static final String CLEANER_THREAD_NAME = ObjectCleaner.class.getSimpleName() + "Thread";
/*     */   
/*  41 */   private static final Set<AutomaticCleanerReference> LIVE_SET = new ConcurrentSet();
/*  42 */   private static final ReferenceQueue<Object> REFERENCE_QUEUE = new ReferenceQueue();
/*  43 */   private static final AtomicBoolean CLEANER_RUNNING = new AtomicBoolean(false);
/*  44 */   private static final Runnable CLEANER_TASK = new Runnable()
/*     */   {
/*     */     public void run() {
/*  47 */       boolean interrupted = false;
/*     */       
/*     */       for (;;)
/*     */       {
/*  51 */         if (!ObjectCleaner.LIVE_SET.isEmpty())
/*     */         {
/*     */           try {
/*  54 */             reference = (ObjectCleaner.AutomaticCleanerReference)ObjectCleaner.REFERENCE_QUEUE.remove(ObjectCleaner.REFERENCE_QUEUE_POLL_TIMEOUT_MS);
/*     */           } catch (InterruptedException ex) {
/*     */             ObjectCleaner.AutomaticCleanerReference reference;
/*  57 */             interrupted = true; }
/*  58 */           continue;
/*     */           ObjectCleaner.AutomaticCleanerReference reference;
/*  60 */           if (reference != null) {
/*     */             try {
/*  62 */               reference.cleanup();
/*     */             }
/*     */             catch (Throwable localThrowable) {}
/*     */             
/*     */ 
/*  67 */             ObjectCleaner.LIVE_SET.remove(reference);
/*     */           }
/*     */         } else {
/*  70 */           ObjectCleaner.CLEANER_RUNNING.set(false);
/*     */           
/*     */ 
/*     */ 
/*  74 */           if (!ObjectCleaner.LIVE_SET.isEmpty()) { if (!ObjectCleaner.CLEANER_RUNNING.compareAndSet(false, true)) {
/*     */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*  80 */       if (interrupted)
/*     */       {
/*  82 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void register(Object object, Runnable cleanupTask)
/*     */   {
/*  96 */     AutomaticCleanerReference reference = new AutomaticCleanerReference(object, (Runnable)ObjectUtil.checkNotNull(cleanupTask, "cleanupTask"));
/*     */     
/*     */ 
/*  99 */     LIVE_SET.add(reference);
/*     */     
/*     */ 
/* 102 */     if (CLEANER_RUNNING.compareAndSet(false, true)) {
/* 103 */       Thread cleanupThread = new FastThreadLocalThread(CLEANER_TASK);
/* 104 */       cleanupThread.setPriority(1);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */       AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Void run() {
/* 113 */           this.val$cleanupThread.setContextClassLoader(null);
/* 114 */           return null;
/*     */         }
/* 116 */       });
/* 117 */       cleanupThread.setName(CLEANER_THREAD_NAME);
/*     */       
/*     */ 
/*     */ 
/* 121 */       cleanupThread.setDaemon(true);
/* 122 */       cleanupThread.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getLiveSetCount() {
/* 127 */     return LIVE_SET.size();
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class AutomaticCleanerReference
/*     */     extends WeakReference<Object>
/*     */   {
/*     */     private final Runnable cleanupTask;
/*     */     
/*     */     AutomaticCleanerReference(Object referent, Runnable cleanupTask)
/*     */     {
/* 138 */       super(ObjectCleaner.REFERENCE_QUEUE);
/* 139 */       this.cleanupTask = cleanupTask;
/*     */     }
/*     */     
/*     */     void cleanup() {
/* 143 */       this.cleanupTask.run();
/*     */     }
/*     */     
/*     */     public Thread get()
/*     */     {
/* 148 */       return null;
/*     */     }
/*     */     
/*     */     public void clear()
/*     */     {
/* 153 */       ObjectCleaner.LIVE_SET.remove(this);
/* 154 */       super.clear();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\ObjectCleaner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */