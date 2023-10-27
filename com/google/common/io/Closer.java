/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Throwables;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Deque;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class Closer
/*     */   implements Closeable
/*     */ {
/*  96 */   private static final Suppressor SUPPRESSOR = SuppressingSuppressor.isAvailable() ? SuppressingSuppressor.INSTANCE : LoggingSuppressor.INSTANCE;
/*     */   @VisibleForTesting
/*     */   final Suppressor suppressor;
/*     */   
/*     */   public static Closer create()
/*     */   {
/* 102 */     return new Closer(SUPPRESSOR);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 108 */   private final Deque<Closeable> stack = new ArrayDeque(4);
/*     */   private Throwable thrown;
/*     */   
/*     */   @VisibleForTesting
/*     */   Closer(Suppressor suppressor) {
/* 113 */     this.suppressor = ((Suppressor)Preconditions.checkNotNull(suppressor));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public <C extends Closeable> C register(C closeable)
/*     */   {
/* 125 */     if (closeable != null) {
/* 126 */       this.stack.addFirst(closeable);
/*     */     }
/*     */     
/* 129 */     return closeable;
/*     */   }
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
/*     */   public RuntimeException rethrow(Throwable e)
/*     */     throws IOException
/*     */   {
/* 146 */     Preconditions.checkNotNull(e);
/* 147 */     this.thrown = e;
/* 148 */     Throwables.propagateIfPossible(e, IOException.class);
/* 149 */     throw new RuntimeException(e);
/*     */   }
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
/*     */   public <X extends Exception> RuntimeException rethrow(Throwable e, Class<X> declaredType)
/*     */     throws IOException, Exception
/*     */   {
/* 168 */     Preconditions.checkNotNull(e);
/* 169 */     this.thrown = e;
/* 170 */     Throwables.propagateIfPossible(e, IOException.class);
/* 171 */     Throwables.propagateIfPossible(e, declaredType);
/* 172 */     throw new RuntimeException(e);
/*     */   }
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
/*     */   public <X1 extends Exception, X2 extends Exception> RuntimeException rethrow(Throwable e, Class<X1> declaredType1, Class<X2> declaredType2)
/*     */     throws IOException, Exception, Exception
/*     */   {
/* 192 */     Preconditions.checkNotNull(e);
/* 193 */     this.thrown = e;
/* 194 */     Throwables.propagateIfPossible(e, IOException.class);
/* 195 */     Throwables.propagateIfPossible(e, declaredType1, declaredType2);
/* 196 */     throw new RuntimeException(e);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 208 */     Throwable throwable = this.thrown;
/*     */     
/*     */ 
/* 211 */     while (!this.stack.isEmpty()) {
/* 212 */       Closeable closeable = (Closeable)this.stack.removeFirst();
/*     */       try {
/* 214 */         closeable.close();
/*     */       } catch (Throwable e) {
/* 216 */         if (throwable == null) {
/* 217 */           throwable = e;
/*     */         } else {
/* 219 */           this.suppressor.suppress(closeable, throwable, e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 224 */     if ((this.thrown == null) && (throwable != null)) {
/* 225 */       Throwables.propagateIfPossible(throwable, IOException.class);
/* 226 */       throw new AssertionError(throwable);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static abstract interface Suppressor
/*     */   {
/*     */     public abstract void suppress(Closeable paramCloseable, Throwable paramThrowable1, Throwable paramThrowable2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static final class LoggingSuppressor
/*     */     implements Closer.Suppressor
/*     */   {
/* 245 */     static final LoggingSuppressor INSTANCE = new LoggingSuppressor();
/*     */     
/*     */ 
/*     */     public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed)
/*     */     {
/* 250 */       Closeables.logger.log(Level.WARNING, "Suppressing exception thrown when closing " + closeable, suppressed);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static final class SuppressingSuppressor
/*     */     implements Closer.Suppressor
/*     */   {
/* 262 */     static final SuppressingSuppressor INSTANCE = new SuppressingSuppressor();
/*     */     
/*     */     static boolean isAvailable() {
/* 265 */       return addSuppressed != null;
/*     */     }
/*     */     
/* 268 */     static final Method addSuppressed = getAddSuppressed();
/*     */     
/*     */     private static Method getAddSuppressed() {
/*     */       try {
/* 272 */         return Throwable.class.getMethod("addSuppressed", new Class[] { Throwable.class });
/*     */       } catch (Throwable e) {}
/* 274 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void suppress(Closeable closeable, Throwable thrown, Throwable suppressed)
/*     */     {
/* 281 */       if (thrown == suppressed) {
/* 282 */         return;
/*     */       }
/*     */       try {
/* 285 */         addSuppressed.invoke(thrown, new Object[] { suppressed });
/*     */       }
/*     */       catch (Throwable e) {
/* 288 */         Closer.LoggingSuppressor.INSTANCE.suppress(closeable, thrown, suppressed);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\io\Closer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */