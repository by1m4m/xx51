/*     */ package org.checkerframework.checker.nullness;
/*     */ 
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
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
/*     */ public final class Opt
/*     */ {
/*     */   private Opt()
/*     */   {
/*  36 */     throw new AssertionError("shouldn't be instantiated");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T get(T primary)
/*     */   {
/*  45 */     if (primary == null) {
/*  46 */       throw new NoSuchElementException("No value present");
/*     */     }
/*  48 */     return primary;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @EnsuresNonNullIf(expression={"#1"}, result=true)
/*     */   public static boolean isPresent(Object primary)
/*     */   {
/*  58 */     return primary != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> void ifPresent(T primary, Consumer<? super T> consumer)
/*     */   {
/*  67 */     if (primary != null) {
/*  68 */       consumer.accept(primary);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T filter(T primary, Predicate<? super T> predicate)
/*     */   {
/*  80 */     if (primary == null) {
/*  81 */       return null;
/*     */     }
/*  83 */     return predicate.test(primary) ? primary : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T, U> U map(T primary, Function<? super T, ? extends U> mapper)
/*     */   {
/*  95 */     if (primary == null) {
/*  96 */       return null;
/*     */     }
/*  98 */     return (U)mapper.apply(primary);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T orElse(T primary, T other)
/*     */   {
/* 110 */     return primary != null ? primary : other;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T orElseGet(T primary, Supplier<? extends T> other)
/*     */   {
/* 120 */     return primary != null ? primary : other.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T, X extends Throwable> T orElseThrow(T primary, Supplier<? extends X> exceptionSupplier)
/*     */     throws Throwable
/*     */   {
/* 131 */     if (primary != null) {
/* 132 */       return primary;
/*     */     }
/* 134 */     throw ((Throwable)exceptionSupplier.get());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\nullness\Opt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */