/*    */ package org.eclipse.jetty.util;
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
/*    */ public class ProcessorUtils
/*    */ {
/*    */   public static final String AVAILABLE_PROCESSORS = "JETTY_AVAILABLE_PROCESSORS";
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
/* 30 */   private static int __availableProcessors = Runtime.getRuntime().availableProcessors();
/*    */   
/*    */   static
/*    */   {
/* 34 */     String avlProcEnv = System.getProperty("JETTY_AVAILABLE_PROCESSORS", System.getenv("JETTY_AVAILABLE_PROCESSORS"));
/* 35 */     if (avlProcEnv != null)
/*    */     {
/*    */       try
/*    */       {
/* 39 */         __availableProcessors = Integer.parseInt(avlProcEnv);
/*    */       }
/*    */       catch (NumberFormatException localNumberFormatException) {}
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
/*    */   public static int availableProcessors()
/*    */   {
/* 56 */     return __availableProcessors;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\ProcessorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */