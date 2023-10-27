/*    */ package rdp.gold.brute;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
/*    */ 
/*    */ public class CounterPool
/*    */ {
/*  7 */   public static AtomicLong pool = new AtomicLong(0L);
/*  8 */   public static AtomicLong poolUsed = new AtomicLong(0L);
/*  9 */   public static AtomicLong sniffedPorts = new AtomicLong(0L);
/* 10 */   public static AtomicLong scannedIps = new AtomicLong(0L);
/* 11 */   public static AtomicLong notSupportedRdp = new AtomicLong(0L);
/* 12 */   public static AtomicLong useBytes = new AtomicLong(0L);
/* 13 */   public static AtomicLong countNotValidRdp = new AtomicLong(0L);
/* 14 */   public static AtomicLong countCheckedCombinations = new AtomicLong(0L);
/* 15 */   public static AtomicLong countCheckedIp = new AtomicLong(0L);
/* 16 */   public static AtomicLong countValid = new AtomicLong(0L);
/* 17 */   public static AtomicLong countX224RDP = new AtomicLong(0L);
/* 18 */   public static AtomicLong countCredSSPRDP = new AtomicLong(0L);
/* 19 */   public static AtomicLong countScanQueue = new AtomicLong(0L);
/* 20 */   public static AtomicLong countBruteQueue = new AtomicLong(0L);
/*    */   
/*    */   public static void reset() {
/* 23 */     pool.set(0L);
/* 24 */     sniffedPorts.set(0L);
/* 25 */     scannedIps.set(0L);
/* 26 */     notSupportedRdp.set(0L);
/* 27 */     useBytes.set(0L);
/* 28 */     countNotValidRdp.set(0L);
/* 29 */     countCheckedCombinations.set(0L);
/* 30 */     countCheckedIp.set(0L);
/* 31 */     countValid.set(0L);
/*    */     
/* 33 */     countX224RDP.set(0L);
/*    */     
/* 35 */     countCredSSPRDP.set(0L);
/*    */     
/* 37 */     countScanQueue.set(0L);
/*    */     
/* 39 */     countBruteQueue.set(0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\CounterPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */