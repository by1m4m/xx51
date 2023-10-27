/*     */ package io.netty.util.internal.logging;
/*     */ 
/*     */ import org.slf4j.spi.LocationAwareLogger;
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
/*     */ final class LocationAwareSlf4JLogger
/*     */   extends AbstractInternalLogger
/*     */ {
/*  31 */   private static final String FQCN = LocationAwareSlf4JLogger.class.getName();
/*     */   private static final long serialVersionUID = -8292030083201538180L;
/*     */   private final transient LocationAwareLogger logger;
/*     */   
/*     */   LocationAwareSlf4JLogger(LocationAwareLogger logger)
/*     */   {
/*  37 */     super(logger.getName());
/*  38 */     this.logger = logger;
/*     */   }
/*     */   
/*     */   private void log(int level, String message, Object... params) {
/*  42 */     this.logger.log(null, FQCN, level, message, params, null);
/*     */   }
/*     */   
/*     */   private void log(int level, String message, Throwable throwable, Object... params) {
/*  46 */     this.logger.log(null, FQCN, level, message, params, throwable);
/*     */   }
/*     */   
/*     */   public boolean isTraceEnabled()
/*     */   {
/*  51 */     return this.logger.isTraceEnabled();
/*     */   }
/*     */   
/*     */   public void trace(String msg)
/*     */   {
/*  56 */     if (isTraceEnabled()) {
/*  57 */       log(0, msg, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void trace(String format, Object arg)
/*     */   {
/*  63 */     if (isTraceEnabled()) {
/*  64 */       log(0, format, new Object[] { arg });
/*     */     }
/*     */   }
/*     */   
/*     */   public void trace(String format, Object argA, Object argB)
/*     */   {
/*  70 */     if (isTraceEnabled()) {
/*  71 */       log(0, format, new Object[] { argA, argB });
/*     */     }
/*     */   }
/*     */   
/*     */   public void trace(String format, Object... argArray)
/*     */   {
/*  77 */     if (isTraceEnabled()) {
/*  78 */       log(0, format, argArray);
/*     */     }
/*     */   }
/*     */   
/*     */   public void trace(String msg, Throwable t)
/*     */   {
/*  84 */     if (isTraceEnabled()) {
/*  85 */       log(0, msg, t, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled()
/*     */   {
/*  91 */     return this.logger.isDebugEnabled();
/*     */   }
/*     */   
/*     */   public void debug(String msg)
/*     */   {
/*  96 */     if (isDebugEnabled()) {
/*  97 */       log(10, msg, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(String format, Object arg)
/*     */   {
/* 103 */     if (isDebugEnabled()) {
/* 104 */       log(10, format, new Object[] { arg });
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(String format, Object argA, Object argB)
/*     */   {
/* 110 */     if (isDebugEnabled()) {
/* 111 */       log(10, format, new Object[] { argA, argB });
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(String format, Object... argArray)
/*     */   {
/* 117 */     if (isDebugEnabled()) {
/* 118 */       log(10, format, argArray);
/*     */     }
/*     */   }
/*     */   
/*     */   public void debug(String msg, Throwable t)
/*     */   {
/* 124 */     if (isDebugEnabled()) {
/* 125 */       log(10, msg, t, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled()
/*     */   {
/* 131 */     return this.logger.isInfoEnabled();
/*     */   }
/*     */   
/*     */   public void info(String msg)
/*     */   {
/* 136 */     if (isInfoEnabled()) {
/* 137 */       log(20, msg, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(String format, Object arg)
/*     */   {
/* 143 */     if (isInfoEnabled()) {
/* 144 */       log(20, format, new Object[] { arg });
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(String format, Object argA, Object argB)
/*     */   {
/* 150 */     if (isInfoEnabled()) {
/* 151 */       log(20, format, new Object[] { argA, argB });
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(String format, Object... argArray)
/*     */   {
/* 157 */     if (isInfoEnabled()) {
/* 158 */       log(20, format, argArray);
/*     */     }
/*     */   }
/*     */   
/*     */   public void info(String msg, Throwable t)
/*     */   {
/* 164 */     if (isInfoEnabled()) {
/* 165 */       log(20, msg, t, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled()
/*     */   {
/* 171 */     return this.logger.isWarnEnabled();
/*     */   }
/*     */   
/*     */   public void warn(String msg)
/*     */   {
/* 176 */     if (isWarnEnabled()) {
/* 177 */       log(30, msg, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(String format, Object arg)
/*     */   {
/* 183 */     if (isWarnEnabled()) {
/* 184 */       log(30, format, new Object[] { arg });
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(String format, Object... argArray)
/*     */   {
/* 190 */     if (isWarnEnabled()) {
/* 191 */       log(30, format, argArray);
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(String format, Object argA, Object argB)
/*     */   {
/* 197 */     if (isWarnEnabled()) {
/* 198 */       log(30, format, new Object[] { argA, argB });
/*     */     }
/*     */   }
/*     */   
/*     */   public void warn(String msg, Throwable t)
/*     */   {
/* 204 */     if (isWarnEnabled()) {
/* 205 */       log(30, msg, t, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled()
/*     */   {
/* 211 */     return this.logger.isErrorEnabled();
/*     */   }
/*     */   
/*     */   public void error(String msg)
/*     */   {
/* 216 */     if (isErrorEnabled()) {
/* 217 */       log(40, msg, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(String format, Object arg)
/*     */   {
/* 223 */     if (isErrorEnabled()) {
/* 224 */       log(40, format, new Object[] { arg });
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(String format, Object argA, Object argB)
/*     */   {
/* 230 */     if (isErrorEnabled()) {
/* 231 */       log(40, format, new Object[] { argA, argB });
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(String format, Object... argArray)
/*     */   {
/* 237 */     if (isErrorEnabled()) {
/* 238 */       log(40, format, argArray);
/*     */     }
/*     */   }
/*     */   
/*     */   public void error(String msg, Throwable t)
/*     */   {
/* 244 */     if (isErrorEnabled()) {
/* 245 */       log(40, msg, t, new Object[0]);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\logging\LocationAwareSlf4JLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */