/*    */ package io.netty.util.internal.logging;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.slf4j.helpers.NOPLoggerFactory;
/*    */ import org.slf4j.spi.LocationAwareLogger;
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
/*    */ public class Slf4JLoggerFactory
/*    */   extends InternalLoggerFactory
/*    */ {
/* 31 */   public static final InternalLoggerFactory INSTANCE = new Slf4JLoggerFactory();
/*    */   
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public Slf4JLoggerFactory() {}
/*    */   
/*    */ 
/*    */   Slf4JLoggerFactory(boolean failIfNOP)
/*    */   {
/* 41 */     assert (failIfNOP);
/* 42 */     if ((LoggerFactory.getILoggerFactory() instanceof NOPLoggerFactory)) {
/* 43 */       throw new NoClassDefFoundError("NOPLoggerFactory not supported");
/*    */     }
/*    */   }
/*    */   
/*    */   public InternalLogger newInstance(String name)
/*    */   {
/* 49 */     return wrapLogger(LoggerFactory.getLogger(name));
/*    */   }
/*    */   
/*    */   static InternalLogger wrapLogger(Logger logger)
/*    */   {
/* 54 */     return (logger instanceof LocationAwareLogger) ? new LocationAwareSlf4JLogger((LocationAwareLogger)logger) : new Slf4JLogger(logger);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\logging\Slf4JLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */