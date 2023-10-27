/*    */ package oshi.util;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Util
/*    */ {
/* 30 */   private static final Logger LOG = LoggerFactory.getLogger(Util.class);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void sleep(long ms)
/*    */   {
/*    */     try
/*    */     {
/* 43 */       LOG.trace("Sleeping for {} ms", Long.valueOf(ms));
/* 44 */       Thread.sleep(ms);
/*    */     } catch (InterruptedException e) {
/* 46 */       LOG.trace("", e);
/* 47 */       LOG.warn("Interrupted while sleeping for {} ms", Long.valueOf(ms));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void sleepAfter(long startTime, long ms)
/*    */   {
/* 62 */     long now = System.currentTimeMillis();
/* 63 */     long until = startTime + ms;
/* 64 */     LOG.trace("Sleeping until {}", Long.valueOf(until));
/* 65 */     if (now < until) {
/* 66 */       sleep(until - now);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\util\Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */