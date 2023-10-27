/*    */ package io.netty.util.internal.logging;
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
/*    */ public abstract class InternalLoggerFactory
/*    */ {
/*    */   private static volatile InternalLoggerFactory defaultFactory;
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
/*    */   private static InternalLoggerFactory newDefaultFactory(String name)
/*    */   {
/*    */     InternalLoggerFactory f;
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
/*    */     try
/*    */     {
/* 42 */       InternalLoggerFactory f = new Slf4JLoggerFactory(true);
/* 43 */       f.newInstance(name).debug("Using SLF4J as the default logging framework");
/*    */     } catch (Throwable ignore1) {
/*    */       try {
/* 46 */         InternalLoggerFactory f = Log4JLoggerFactory.INSTANCE;
/* 47 */         f.newInstance(name).debug("Using Log4J as the default logging framework");
/*    */       } catch (Throwable ignore2) {
/*    */         try {
/* 50 */           InternalLoggerFactory f = Log4J2LoggerFactory.INSTANCE;
/* 51 */           f.newInstance(name).debug("Using Log4J2 as the default logging framework");
/*    */         } catch (Throwable ignore3) {
/* 53 */           f = JdkLoggerFactory.INSTANCE;
/* 54 */           f.newInstance(name).debug("Using java.util.logging as the default logging framework");
/*    */         }
/*    */       }
/*    */     }
/* 58 */     return f;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static InternalLoggerFactory getDefaultFactory()
/*    */   {
/* 66 */     if (defaultFactory == null) {
/* 67 */       defaultFactory = newDefaultFactory(InternalLoggerFactory.class.getName());
/*    */     }
/* 69 */     return defaultFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static void setDefaultFactory(InternalLoggerFactory defaultFactory)
/*    */   {
/* 76 */     if (defaultFactory == null) {
/* 77 */       throw new NullPointerException("defaultFactory");
/*    */     }
/* 79 */     defaultFactory = defaultFactory;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static InternalLogger getInstance(Class<?> clazz)
/*    */   {
/* 86 */     return getInstance(clazz.getName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static InternalLogger getInstance(String name)
/*    */   {
/* 93 */     return getDefaultFactory().newInstance(name);
/*    */   }
/*    */   
/*    */   protected abstract InternalLogger newInstance(String paramString);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\logging\InternalLoggerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */