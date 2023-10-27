/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
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
/*     */ @GwtIncompatible
/*     */ final class FuturesGetChecked
/*     */ {
/*     */   @CanIgnoreReturnValue
/*     */   static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass)
/*     */     throws Exception
/*     */   {
/*  46 */     return (V)getChecked(bestGetCheckedTypeValidator(), future, exceptionClass);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   @VisibleForTesting
/*     */   static <V, X extends Exception> V getChecked(GetCheckedTypeValidator validator, Future<V> future, Class<X> exceptionClass)
/*     */     throws Exception
/*     */   {
/*  54 */     validator.validateClass(exceptionClass);
/*     */     try {
/*  56 */       return (V)future.get();
/*     */     } catch (InterruptedException e) {
/*  58 */       Thread.currentThread().interrupt();
/*  59 */       throw newWithCause(exceptionClass, e);
/*     */     } catch (ExecutionException e) {
/*  61 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/*  62 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   static <V, X extends Exception> V getChecked(Future<V> future, Class<X> exceptionClass, long timeout, TimeUnit unit)
/*     */     throws Exception
/*     */   {
/*  71 */     bestGetCheckedTypeValidator().validateClass(exceptionClass);
/*     */     try {
/*  73 */       return (V)future.get(timeout, unit);
/*     */     } catch (InterruptedException e) {
/*  75 */       Thread.currentThread().interrupt();
/*  76 */       throw newWithCause(exceptionClass, e);
/*     */     } catch (TimeoutException e) {
/*  78 */       throw newWithCause(exceptionClass, e);
/*     */     } catch (ExecutionException e) {
/*  80 */       wrapAndThrowExceptionOrError(e.getCause(), exceptionClass);
/*  81 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static GetCheckedTypeValidator bestGetCheckedTypeValidator()
/*     */   {
/*  91 */     return GetCheckedTypeValidatorHolder.BEST_VALIDATOR;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static GetCheckedTypeValidator weakSetValidator() {
/*  96 */     return FuturesGetChecked.GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static GetCheckedTypeValidator classValueValidator()
/*     */   {
/* 102 */     return FuturesGetChecked.GetCheckedTypeValidatorHolder.ClassValueValidator.INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static class GetCheckedTypeValidatorHolder
/*     */   {
/* 113 */     static final String CLASS_VALUE_VALIDATOR_NAME = GetCheckedTypeValidatorHolder.class
/* 114 */       .getName() + "$ClassValueValidator";
/*     */     
/* 116 */     static final FuturesGetChecked.GetCheckedTypeValidator BEST_VALIDATOR = getBestValidator();
/*     */     
/*     */     @IgnoreJRERequirement
/*     */     static enum ClassValueValidator implements FuturesGetChecked.GetCheckedTypeValidator
/*     */     {
/* 121 */       INSTANCE;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */       private static final ClassValue<Boolean> isValidClass = new ClassValue()
/*     */       {
/*     */         protected Boolean computeValue(Class<?> type)
/*     */         {
/* 131 */           FuturesGetChecked.checkExceptionClassValidity(type.asSubclass(Exception.class));
/* 132 */           return Boolean.valueOf(true);
/*     */         }
/*     */       };
/*     */       
/*     */       private ClassValueValidator() {}
/*     */       
/* 138 */       public void validateClass(Class<? extends Exception> exceptionClass) { isValidClass.get(exceptionClass); }
/*     */     }
/*     */     
/*     */     static enum WeakSetValidator implements FuturesGetChecked.GetCheckedTypeValidator
/*     */     {
/* 143 */       INSTANCE;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */       private static final Set<WeakReference<Class<? extends Exception>>> validClasses = new CopyOnWriteArraySet();
/*     */       
/*     */       private WeakSetValidator() {}
/*     */       
/*     */       public void validateClass(Class<? extends Exception> exceptionClass) {
/* 158 */         for (WeakReference<Class<? extends Exception>> knownGood : validClasses) {
/* 159 */           if (exceptionClass.equals(knownGood.get())) {
/* 160 */             return;
/*     */           }
/*     */         }
/*     */         
/* 164 */         FuturesGetChecked.checkExceptionClassValidity(exceptionClass);
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
/* 175 */         if (validClasses.size() > 1000) {
/* 176 */           validClasses.clear();
/*     */         }
/*     */         
/* 179 */         validClasses.add(new WeakReference(exceptionClass));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     static FuturesGetChecked.GetCheckedTypeValidator getBestValidator()
/*     */     {
/*     */       try
/*     */       {
/* 189 */         Class<?> theClass = Class.forName(CLASS_VALUE_VALIDATOR_NAME);
/* 190 */         return (FuturesGetChecked.GetCheckedTypeValidator)theClass.getEnumConstants()[0];
/*     */       } catch (Throwable t) {}
/* 192 */       return FuturesGetChecked.weakSetValidator();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable cause, Class<X> exceptionClass)
/*     */     throws Exception
/*     */   {
/* 200 */     if ((cause instanceof Error)) {
/* 201 */       throw new ExecutionError((Error)cause);
/*     */     }
/* 203 */     if ((cause instanceof RuntimeException)) {
/* 204 */       throw new UncheckedExecutionException(cause);
/*     */     }
/* 206 */     throw newWithCause(exceptionClass, cause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean hasConstructorUsableByGetChecked(Class<? extends Exception> exceptionClass)
/*     */   {
/*     */     try
/*     */     {
/* 217 */       Exception unused = newWithCause(exceptionClass, new Exception());
/* 218 */       return true;
/*     */     } catch (Exception e) {}
/* 220 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static <X extends Exception> X newWithCause(Class<X> exceptionClass, Throwable cause)
/*     */   {
/* 227 */     List<Constructor<X>> constructors = Arrays.asList(exceptionClass.getConstructors());
/* 228 */     for (Constructor<X> constructor : preferringStrings(constructors)) {
/* 229 */       X instance = (Exception)newFromConstructor(constructor, cause);
/* 230 */       if (instance != null) {
/* 231 */         if (instance.getCause() == null) {
/* 232 */           instance.initCause(cause);
/*     */         }
/* 234 */         return instance;
/*     */       }
/*     */     }
/* 237 */     throw new IllegalArgumentException("No appropriate constructor for exception of type " + exceptionClass + " in response to chained exception", cause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <X extends Exception> List<Constructor<X>> preferringStrings(List<Constructor<X>> constructors)
/*     */   {
/* 246 */     return WITH_STRING_PARAM_FIRST.sortedCopy(constructors);
/*     */   }
/*     */   
/*     */ 
/* 250 */   private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST = Ordering.natural()
/* 251 */     .onResultOf(new Function()
/*     */   {
/*     */ 
/*     */     public Boolean apply(Constructor<?> input)
/*     */     {
/* 255 */       return Boolean.valueOf(Arrays.asList(input.getParameterTypes()).contains(String.class));
/*     */     }
/* 251 */   })
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 258 */     .reverse();
/*     */   
/*     */   private static <X> X newFromConstructor(Constructor<X> constructor, Throwable cause) {
/* 261 */     Class<?>[] paramTypes = constructor.getParameterTypes();
/* 262 */     Object[] params = new Object[paramTypes.length];
/* 263 */     for (int i = 0; i < paramTypes.length; i++) {
/* 264 */       Class<?> paramType = paramTypes[i];
/* 265 */       if (paramType.equals(String.class)) {
/* 266 */         params[i] = cause.toString();
/* 267 */       } else if (paramType.equals(Throwable.class)) {
/* 268 */         params[i] = cause;
/*     */       } else {
/* 270 */         return null;
/*     */       }
/*     */     }
/*     */     try {
/* 274 */       return (X)constructor.newInstance(params);
/*     */     }
/*     */     catch (IllegalArgumentException|InstantiationException|IllegalAccessException|InvocationTargetException e) {}
/*     */     
/*     */ 
/* 279 */     return null;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static boolean isCheckedException(Class<? extends Exception> type)
/*     */   {
/* 285 */     return !RuntimeException.class.isAssignableFrom(type);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static void checkExceptionClassValidity(Class<? extends Exception> exceptionClass) {
/* 290 */     Preconditions.checkArgument(
/* 291 */       isCheckedException(exceptionClass), "Futures.getChecked exception type (%s) must not be a RuntimeException", exceptionClass);
/*     */     
/*     */ 
/* 294 */     Preconditions.checkArgument(
/* 295 */       hasConstructorUsableByGetChecked(exceptionClass), "Futures.getChecked exception type (%s) must be an accessible class with an accessible constructor whose parameters (if any) must be of type String and/or Throwable", exceptionClass);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static abstract interface GetCheckedTypeValidator
/*     */   {
/*     */     public abstract void validateClass(Class<? extends Exception> paramClass);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\FuturesGetChecked.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */