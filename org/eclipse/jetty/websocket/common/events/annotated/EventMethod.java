/*     */ package org.eclipse.jetty.websocket.common.events.annotated;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.Session;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketException;
/*     */ import org.eclipse.jetty.websocket.api.util.QuoteUtil;
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
/*     */ public class EventMethod
/*     */ {
/*  34 */   private static final Logger LOG = Log.getLogger(EventMethod.class);
/*     */   protected Class<?> pojo;
/*     */   protected Method method;
/*     */   
/*  38 */   private static Object[] dropFirstArg(Object[] args) { if (args.length == 1)
/*     */     {
/*  40 */       return new Object[0];
/*     */     }
/*  42 */     Object[] ret = new Object[args.length - 1];
/*  43 */     System.arraycopy(args, 1, ret, 0, ret.length);
/*  44 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  49 */   private boolean hasSession = false;
/*  50 */   private boolean isStreaming = false;
/*     */   private Class<?>[] paramTypes;
/*     */   
/*     */   public EventMethod(Class<?> pojo, Method method)
/*     */   {
/*  55 */     this.pojo = pojo;
/*  56 */     this.paramTypes = method.getParameterTypes();
/*  57 */     this.method = method;
/*  58 */     identifyPresentParamTypes();
/*     */   }
/*     */   
/*     */   public EventMethod(Class<?> pojo, String methodName, Class<?>... paramTypes)
/*     */   {
/*     */     try
/*     */     {
/*  65 */       this.pojo = pojo;
/*  66 */       this.paramTypes = paramTypes;
/*  67 */       this.method = pojo.getMethod(methodName, paramTypes);
/*  68 */       identifyPresentParamTypes();
/*     */     }
/*     */     catch (NoSuchMethodException|SecurityException e)
/*     */     {
/*  72 */       LOG.warn("Cannot use method {}({}): {}", new Object[] { methodName, paramTypes, e.getMessage() });
/*  73 */       this.method = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void call(Object obj, Object... args)
/*     */   {
/*  79 */     if ((this.pojo == null) || (this.method == null))
/*     */     {
/*  81 */       LOG.warn("Cannot execute call: pojo={}, method={}", new Object[] { this.pojo, this.method });
/*  82 */       return;
/*     */     }
/*  84 */     if (obj == null)
/*     */     {
/*  86 */       LOG.warn("Cannot call {} on null object", new Object[] { this.method });
/*  87 */       return;
/*     */     }
/*  89 */     if (args.length > this.paramTypes.length)
/*     */     {
/*  91 */       Object[] trimArgs = dropFirstArg(args);
/*  92 */       call(obj, trimArgs);
/*  93 */       return;
/*     */     }
/*  95 */     if (args.length < this.paramTypes.length)
/*     */     {
/*  97 */       throw new IllegalArgumentException("Call arguments length [" + args.length + "] must always be greater than or equal to captured args length [" + this.paramTypes.length + "]");
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 103 */       this.method.invoke(obj, args);
/*     */     }
/*     */     catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e)
/*     */     {
/* 107 */       String err = String.format("Cannot call method %s on %s with args: %s", new Object[] { this.method, this.pojo, QuoteUtil.join(args, ",") });
/* 108 */       throw new WebSocketException(err, e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Method getMethod()
/*     */   {
/* 114 */     return this.method;
/*     */   }
/*     */   
/*     */   protected Class<?>[] getParamTypes()
/*     */   {
/* 119 */     return this.paramTypes;
/*     */   }
/*     */   
/*     */   private void identifyPresentParamTypes()
/*     */   {
/* 124 */     this.hasSession = false;
/* 125 */     this.isStreaming = false;
/*     */     
/* 127 */     if (this.paramTypes == null)
/*     */     {
/* 129 */       return;
/*     */     }
/*     */     
/* 132 */     for (Class<?> paramType : this.paramTypes)
/*     */     {
/* 134 */       if (Session.class.isAssignableFrom(paramType))
/*     */       {
/* 136 */         this.hasSession = true;
/*     */       }
/* 138 */       if ((Reader.class.isAssignableFrom(paramType)) || (InputStream.class.isAssignableFrom(paramType)))
/*     */       {
/* 140 */         this.isStreaming = true;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isHasSession()
/*     */   {
/* 147 */     return this.hasSession;
/*     */   }
/*     */   
/*     */   public boolean isStreaming()
/*     */   {
/* 152 */     return this.isStreaming;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\annotated\EventMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */