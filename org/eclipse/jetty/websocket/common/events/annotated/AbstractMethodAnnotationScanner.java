/*     */ package org.eclipse.jetty.websocket.common.events.annotated;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.eclipse.jetty.websocket.api.InvalidWebSocketException;
/*     */ import org.eclipse.jetty.websocket.common.events.ParamList;
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
/*     */ public abstract class AbstractMethodAnnotationScanner<T>
/*     */ {
/*     */   protected void assertIsPublicNonStatic(Method method)
/*     */   {
/*  36 */     int mods = method.getModifiers();
/*  37 */     if (!Modifier.isPublic(mods))
/*     */     {
/*  39 */       StringBuilder err = new StringBuilder();
/*  40 */       err.append("Invalid declaration of ");
/*  41 */       err.append(method);
/*  42 */       err.append(System.lineSeparator());
/*     */       
/*  44 */       err.append("Method modifier must be public");
/*     */       
/*  46 */       throw new InvalidWebSocketException(err.toString());
/*     */     }
/*     */     
/*  49 */     if (Modifier.isStatic(mods))
/*     */     {
/*  51 */       StringBuilder err = new StringBuilder();
/*  52 */       err.append("Invalid declaration of ");
/*  53 */       err.append(method);
/*  54 */       err.append(System.lineSeparator());
/*     */       
/*  56 */       err.append("Method modifier may not be static");
/*     */       
/*  58 */       throw new InvalidWebSocketException(err.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void assertIsReturn(Method method, Class<?> type)
/*     */   {
/*  64 */     if (!type.equals(method.getReturnType()))
/*     */     {
/*  66 */       StringBuilder err = new StringBuilder();
/*  67 */       err.append("Invalid declaration of ");
/*  68 */       err.append(method);
/*  69 */       err.append(System.lineSeparator());
/*     */       
/*  71 */       err.append("Return type must be ").append(type);
/*     */       
/*  73 */       throw new InvalidWebSocketException(err.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void assertIsVoidReturn(Method method)
/*     */   {
/*  79 */     assertIsReturn(method, Void.TYPE);
/*     */   }
/*     */   
/*     */   protected void assertUnset(CallableMethod callable, Class<? extends Annotation> annoClass, Method method)
/*     */   {
/*  84 */     if (callable != null)
/*     */     {
/*     */ 
/*  87 */       StringBuilder err = new StringBuilder();
/*  88 */       err.append("Duplicate @").append(annoClass.getSimpleName()).append(" declaration on ");
/*  89 */       err.append(method);
/*  90 */       err.append(System.lineSeparator());
/*     */       
/*  92 */       err.append("@").append(annoClass.getSimpleName()).append(" previously declared at ");
/*  93 */       err.append(callable.getMethod());
/*     */       
/*  95 */       throw new InvalidWebSocketException(err.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void assertValidSignature(Method method, Class<? extends Annotation> annoClass, ParamList validParams)
/*     */   {
/* 101 */     assertIsPublicNonStatic(method);
/* 102 */     assertIsReturn(method, Void.TYPE);
/*     */     
/* 104 */     boolean valid = false;
/*     */     
/*     */ 
/* 107 */     Class<?>[] actual = method.getParameterTypes();
/* 108 */     for (Class<?>[] params : validParams)
/*     */     {
/* 110 */       if (isSameParameters(actual, params))
/*     */       {
/* 112 */         valid = true;
/* 113 */         break;
/*     */       }
/*     */     }
/*     */     
/* 117 */     if (!valid)
/*     */     {
/* 119 */       throw InvalidSignatureException.build(method, annoClass, new ParamList[] { validParams });
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAnnotation(Annotation annotation, Class<? extends Annotation> annotationClass)
/*     */   {
/* 125 */     return annotation.annotationType().equals(annotationClass);
/*     */   }
/*     */   
/*     */   public boolean isSameParameters(Class<?>[] actual, Class<?>[] params)
/*     */   {
/* 130 */     if (actual.length != params.length)
/*     */     {
/*     */ 
/* 133 */       return false;
/*     */     }
/*     */     
/* 136 */     int len = params.length;
/* 137 */     for (int i = 0; i < len; i++)
/*     */     {
/* 139 */       if (!actual[i].equals(params[i]))
/*     */       {
/* 141 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean isSignatureMatch(Method method, ParamList validParams)
/*     */   {
/* 150 */     assertIsPublicNonStatic(method);
/* 151 */     assertIsReturn(method, Void.TYPE);
/*     */     
/*     */ 
/* 154 */     Class<?>[] actual = method.getParameterTypes();
/* 155 */     for (Class<?>[] params : validParams)
/*     */     {
/* 157 */       if (isSameParameters(actual, params))
/*     */       {
/* 159 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 163 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean isTypeAnnotated(Class<?> pojo, Class<? extends Annotation> expectedAnnotation)
/*     */   {
/* 168 */     return pojo.getAnnotation(expectedAnnotation) != null;
/*     */   }
/*     */   
/*     */   public abstract void onMethodAnnotation(T paramT, Class<?> paramClass, Method paramMethod, Annotation paramAnnotation);
/*     */   
/*     */   public void scanMethodAnnotations(T metadata, Class<?> pojo)
/*     */   {
/* 175 */     Class<?> clazz = pojo;
/*     */     
/* 177 */     while ((clazz != null) && (Object.class.isAssignableFrom(clazz)))
/*     */     {
/* 179 */       for (Method method : clazz.getDeclaredMethods())
/*     */       {
/* 181 */         Annotation[] annotations = method.getAnnotations();
/* 182 */         if ((annotations != null) && (annotations.length > 0))
/*     */         {
/*     */ 
/*     */ 
/* 186 */           for (Annotation annotation : annotations)
/*     */           {
/* 188 */             onMethodAnnotation(metadata, clazz, method, annotation);
/*     */           }
/*     */         }
/*     */       }
/* 192 */       clazz = clazz.getSuperclass();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\annotated\AbstractMethodAnnotationScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */