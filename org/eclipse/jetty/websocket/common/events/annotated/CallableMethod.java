/*     */ package org.eclipse.jetty.websocket.common.events.annotated;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Objects;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.common.util.ReflectUtils;
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
/*     */ public class CallableMethod
/*     */ {
/*  34 */   private static final Logger LOG = Log.getLogger(CallableMethod.class);
/*     */   protected final Class<?> pojo;
/*     */   protected final Method method;
/*     */   protected Class<?>[] paramTypes;
/*     */   
/*     */   public CallableMethod(Class<?> pojo, Method method)
/*     */   {
/*  41 */     Objects.requireNonNull(pojo, "Pojo cannot be null");
/*  42 */     Objects.requireNonNull(method, "Method cannot be null");
/*  43 */     this.pojo = pojo;
/*  44 */     this.method = method;
/*  45 */     this.paramTypes = method.getParameterTypes();
/*     */   }
/*     */   
/*     */   public Object call(Object obj, Object... args)
/*     */   {
/*  50 */     if ((this.pojo == null) || (this.method == null))
/*     */     {
/*  52 */       LOG.warn("Cannot execute call: pojo={}, method={}", new Object[] { this.pojo, this.method });
/*  53 */       return null;
/*     */     }
/*     */     
/*  56 */     if (obj == null)
/*     */     {
/*  58 */       String err = String.format("Cannot call %s on null object", new Object[] { this.method });
/*  59 */       LOG.warn(new RuntimeException(err));
/*  60 */       return null;
/*     */     }
/*     */     
/*  63 */     if (args.length < this.paramTypes.length)
/*     */     {
/*  65 */       throw new IllegalArgumentException("Call arguments length [" + args.length + "] must always be greater than or equal to captured args length [" + this.paramTypes.length + "]");
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  71 */       return this.method.invoke(obj, args);
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/*  75 */       String err = formatMethodCallError(args);
/*  76 */       throw unwrapRuntimeException(err, t);
/*     */     }
/*     */   }
/*     */   
/*     */   private RuntimeException unwrapRuntimeException(String err, Throwable t)
/*     */   {
/*  82 */     Throwable ret = t;
/*     */     
/*  84 */     while ((ret instanceof InvocationTargetException))
/*     */     {
/*  86 */       ret = ((InvocationTargetException)ret).getCause();
/*     */     }
/*     */     
/*  89 */     if ((ret instanceof RuntimeException))
/*     */     {
/*  91 */       return (RuntimeException)ret;
/*     */     }
/*     */     
/*  94 */     return new RuntimeException(err, ret);
/*     */   }
/*     */   
/*     */   public String formatMethodCallError(Object... args)
/*     */   {
/*  99 */     StringBuilder err = new StringBuilder();
/* 100 */     err.append("Cannot call method ");
/* 101 */     err.append(ReflectUtils.toString(this.pojo, this.method));
/* 102 */     err.append(" with args: [");
/*     */     
/* 104 */     boolean delim = false;
/* 105 */     for (Object arg : args)
/*     */     {
/* 107 */       if (delim)
/*     */       {
/* 109 */         err.append(", ");
/*     */       }
/* 111 */       if (arg == null)
/*     */       {
/* 113 */         err.append("<null>");
/*     */       }
/*     */       else
/*     */       {
/* 117 */         err.append(arg.getClass().getName());
/*     */       }
/* 119 */       delim = true;
/*     */     }
/* 121 */     err.append("]");
/* 122 */     return err.toString();
/*     */   }
/*     */   
/*     */   public Method getMethod()
/*     */   {
/* 127 */     return this.method;
/*     */   }
/*     */   
/*     */   public Class<?>[] getParamTypes()
/*     */   {
/* 132 */     return this.paramTypes;
/*     */   }
/*     */   
/*     */   public Class<?> getPojo()
/*     */   {
/* 137 */     return this.pojo;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 143 */     return String.format("%s[%s]", new Object[] { getClass().getSimpleName(), this.method.toGenericString() });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\annotated\CallableMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */