/*     */ package com.google.common.base;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.Serializable;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ @GwtCompatible
/*     */ public final class Suppliers
/*     */ {
/*  46 */   public static <F, T> Supplier<T> compose(Function<? super F, T> function, Supplier<F> supplier) { return new SupplierComposition(function, supplier); }
/*     */   
/*     */   private static class SupplierComposition<F, T> implements Supplier<T>, Serializable {
/*     */     final Function<? super F, T> function;
/*     */     final Supplier<F> supplier;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     SupplierComposition(Function<? super F, T> function, Supplier<F> supplier) {
/*  54 */       this.function = ((Function)Preconditions.checkNotNull(function));
/*  55 */       this.supplier = ((Supplier)Preconditions.checkNotNull(supplier));
/*     */     }
/*     */     
/*     */     public T get()
/*     */     {
/*  60 */       return (T)this.function.apply(this.supplier.get());
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/*  65 */       if ((obj instanceof SupplierComposition)) {
/*  66 */         SupplierComposition<?, ?> that = (SupplierComposition)obj;
/*  67 */         return (this.function.equals(that.function)) && (this.supplier.equals(that.supplier));
/*     */       }
/*  69 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/*  74 */       return Objects.hashCode(new Object[] { this.function, this.supplier });
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  79 */       return "Suppliers.compose(" + this.function + ", " + this.supplier + ")";
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Supplier<T> memoize(Supplier<T> delegate)
/*     */   {
/* 102 */     if (((delegate instanceof NonSerializableMemoizingSupplier)) || ((delegate instanceof MemoizingSupplier)))
/*     */     {
/* 104 */       return delegate;
/*     */     }
/* 106 */     return (delegate instanceof Serializable) ? new MemoizingSupplier(delegate) : new NonSerializableMemoizingSupplier(delegate);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class MemoizingSupplier<T>
/*     */     implements Supplier<T>, Serializable
/*     */   {
/*     */     final Supplier<T> delegate;
/*     */     volatile transient boolean initialized;
/*     */     transient T value;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     MemoizingSupplier(Supplier<T> delegate)
/*     */     {
/* 120 */       this.delegate = ((Supplier)Preconditions.checkNotNull(delegate));
/*     */     }
/*     */     
/*     */ 
/*     */     public T get()
/*     */     {
/* 126 */       if (!this.initialized) {
/* 127 */         synchronized (this) {
/* 128 */           if (!this.initialized) {
/* 129 */             T t = this.delegate.get();
/* 130 */             this.value = t;
/* 131 */             this.initialized = true;
/* 132 */             return t;
/*     */           }
/*     */         }
/*     */       }
/* 136 */       return (T)this.value;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 141 */       return "Suppliers.memoize(" + (this.initialized ? "<supplier that returned " + this.value + ">" : this.delegate) + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @VisibleForTesting
/*     */   static class NonSerializableMemoizingSupplier<T>
/*     */     implements Supplier<T>
/*     */   {
/*     */     volatile Supplier<T> delegate;
/*     */     
/*     */     volatile boolean initialized;
/*     */     
/*     */     T value;
/*     */     
/*     */     NonSerializableMemoizingSupplier(Supplier<T> delegate)
/*     */     {
/* 158 */       this.delegate = ((Supplier)Preconditions.checkNotNull(delegate));
/*     */     }
/*     */     
/*     */ 
/*     */     public T get()
/*     */     {
/* 164 */       if (!this.initialized) {
/* 165 */         synchronized (this) {
/* 166 */           if (!this.initialized) {
/* 167 */             T t = this.delegate.get();
/* 168 */             this.value = t;
/* 169 */             this.initialized = true;
/*     */             
/* 171 */             this.delegate = null;
/* 172 */             return t;
/*     */           }
/*     */         }
/*     */       }
/* 176 */       return (T)this.value;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 181 */       Supplier<T> delegate = this.delegate;
/* 182 */       return "Suppliers.memoize(" + (delegate == null ? "<supplier that returned " + this.value + ">" : delegate) + ")";
/*     */     }
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
/*     */   public static <T> Supplier<T> memoizeWithExpiration(Supplier<T> delegate, long duration, TimeUnit unit)
/*     */   {
/* 212 */     return new ExpiringMemoizingSupplier(delegate, duration, unit);
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   static class ExpiringMemoizingSupplier<T> implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     final long durationNanos;
/*     */     volatile transient T value;
/*     */     volatile transient long expirationNanos;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/*     */     ExpiringMemoizingSupplier(Supplier<T> delegate, long duration, TimeUnit unit) {
/* 224 */       this.delegate = ((Supplier)Preconditions.checkNotNull(delegate));
/* 225 */       this.durationNanos = unit.toNanos(duration);
/* 226 */       Preconditions.checkArgument(duration > 0L, "duration (%s %s) must be > 0", duration, unit);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public T get()
/*     */     {
/* 237 */       long nanos = this.expirationNanos;
/* 238 */       long now = Platform.systemNanoTime();
/* 239 */       if ((nanos == 0L) || (now - nanos >= 0L)) {
/* 240 */         synchronized (this) {
/* 241 */           if (nanos == this.expirationNanos) {
/* 242 */             T t = this.delegate.get();
/* 243 */             this.value = t;
/* 244 */             nanos = now + this.durationNanos;
/*     */             
/*     */ 
/* 247 */             this.expirationNanos = (nanos == 0L ? 1L : nanos);
/* 248 */             return t;
/*     */           }
/*     */         }
/*     */       }
/* 252 */       return (T)this.value;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 259 */       return "Suppliers.memoizeWithExpiration(" + this.delegate + ", " + this.durationNanos + ", NANOS)";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <T> Supplier<T> ofInstance(T instance)
/*     */   {
/* 267 */     return new SupplierOfInstance(instance);
/*     */   }
/*     */   
/*     */   private static class SupplierOfInstance<T> implements Supplier<T>, Serializable {
/*     */     final T instance;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 274 */     SupplierOfInstance(T instance) { this.instance = instance; }
/*     */     
/*     */ 
/*     */     public T get()
/*     */     {
/* 279 */       return (T)this.instance;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 284 */       if ((obj instanceof SupplierOfInstance)) {
/* 285 */         SupplierOfInstance<?> that = (SupplierOfInstance)obj;
/* 286 */         return Objects.equal(this.instance, that.instance);
/*     */       }
/* 288 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 293 */       return Objects.hashCode(new Object[] { this.instance });
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 298 */       return "Suppliers.ofInstance(" + this.instance + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> Supplier<T> synchronizedSupplier(Supplier<T> delegate)
/*     */   {
/* 309 */     return new ThreadSafeSupplier(delegate);
/*     */   }
/*     */   
/*     */   private static class ThreadSafeSupplier<T> implements Supplier<T>, Serializable {
/*     */     final Supplier<T> delegate;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 316 */     ThreadSafeSupplier(Supplier<T> delegate) { this.delegate = ((Supplier)Preconditions.checkNotNull(delegate)); }
/*     */     
/*     */     /* Error */
/*     */     public T get()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 4	com/google/common/base/Suppliers$ThreadSafeSupplier:delegate	Lcom/google/common/base/Supplier;
/*     */       //   4: dup
/*     */       //   5: astore_1
/*     */       //   6: monitorenter
/*     */       //   7: aload_0
/*     */       //   8: getfield 4	com/google/common/base/Suppliers$ThreadSafeSupplier:delegate	Lcom/google/common/base/Supplier;
/*     */       //   11: invokeinterface 5 1 0
/*     */       //   16: aload_1
/*     */       //   17: monitorexit
/*     */       //   18: areturn
/*     */       //   19: astore_2
/*     */       //   20: aload_1
/*     */       //   21: monitorexit
/*     */       //   22: aload_2
/*     */       //   23: athrow
/*     */       // Line number table:
/*     */       //   Java source line #321	-> byte code offset #0
/*     */       //   Java source line #322	-> byte code offset #7
/*     */       //   Java source line #323	-> byte code offset #19
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	24	0	this	ThreadSafeSupplier<T>
/*     */       //   5	16	1	Ljava/lang/Object;	Object
/*     */       //   19	4	2	localObject1	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   7	18	19	finally
/*     */       //   19	22	19	finally
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 328 */       return "Suppliers.synchronizedSupplier(" + this.delegate + ")";
/*     */     }
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
/*     */   public static <T> Function<Supplier<T>, T> supplierFunction()
/*     */   {
/* 344 */     SupplierFunction<T> sf = SupplierFunctionImpl.INSTANCE;
/* 345 */     return sf;
/*     */   }
/*     */   
/*     */   private static abstract interface SupplierFunction<T> extends Function<Supplier<T>, T>
/*     */   {}
/*     */   
/* 351 */   private static enum SupplierFunctionImpl implements Suppliers.SupplierFunction<Object> { INSTANCE;
/*     */     
/*     */     private SupplierFunctionImpl() {}
/*     */     
/*     */     public Object apply(Supplier<Object> input) {
/* 356 */       return input.get();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 361 */       return "Suppliers.supplierFunction()";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\base\Suppliers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */