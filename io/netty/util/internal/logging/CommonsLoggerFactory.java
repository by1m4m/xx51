/*    */ package io.netty.util.internal.logging;
/*    */ 
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ 
/*    */ @Deprecated
/*    */ public class CommonsLoggerFactory
/*    */   extends InternalLoggerFactory
/*    */ {
/* 32 */   public static final InternalLoggerFactory INSTANCE = new CommonsLoggerFactory();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public InternalLogger newInstance(String name)
/*    */   {
/* 43 */     return new CommonsLogger(LogFactory.getLog(name), name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\logging\CommonsLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */