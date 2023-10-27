/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.RandomAccess;
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
/*     */ @Beta
/*     */ final class SortedLists
/*     */ {
/*     */   static abstract enum KeyPresentBehavior
/*     */   {
/*  49 */     ANY_PRESENT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */     LAST_PRESENT, 
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
/*  79 */     FIRST_PRESENT, 
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
/* 105 */     FIRST_AFTER, 
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
/* 116 */     LAST_BEFORE;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private KeyPresentBehavior() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract <E> int resultIndex(Comparator<? super E> paramComparator, E paramE, List<? extends E> paramList, int paramInt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static abstract enum KeyAbsentBehavior
/*     */   {
/* 136 */     NEXT_LOWER, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */     NEXT_HIGHER, 
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
/* 164 */     INVERTED_INSERTION_INDEX;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private KeyAbsentBehavior() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     abstract int resultIndex(int paramInt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Comparable> int binarySearch(List<? extends E> list, E e, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
/*     */   {
/* 186 */     Preconditions.checkNotNull(e);
/* 187 */     return binarySearch(list, e, Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K extends Comparable> int binarySearch(List<E> list, Function<? super E, K> keyFunction, K key, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
/*     */   {
/* 202 */     return binarySearch(list, keyFunction, key, 
/* 203 */       Ordering.natural(), presentBehavior, absentBehavior);
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
/*     */   public static <E, K> int binarySearch(List<E> list, Function<? super E, K> keyFunction, K key, Comparator<? super K> keyComparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
/*     */   {
/* 220 */     return binarySearch(
/* 221 */       Lists.transform(list, keyFunction), key, keyComparator, presentBehavior, absentBehavior);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> int binarySearch(List<? extends E> list, E key, Comparator<? super E> comparator, KeyPresentBehavior presentBehavior, KeyAbsentBehavior absentBehavior)
/*     */   {
/* 253 */     Preconditions.checkNotNull(comparator);
/* 254 */     Preconditions.checkNotNull(list);
/* 255 */     Preconditions.checkNotNull(presentBehavior);
/* 256 */     Preconditions.checkNotNull(absentBehavior);
/* 257 */     if (!(list instanceof RandomAccess)) {
/* 258 */       list = Lists.newArrayList(list);
/*     */     }
/*     */     
/*     */ 
/* 262 */     int lower = 0;
/* 263 */     int upper = list.size() - 1;
/*     */     
/* 265 */     while (lower <= upper) {
/* 266 */       int middle = lower + upper >>> 1;
/* 267 */       int c = comparator.compare(key, list.get(middle));
/* 268 */       if (c < 0) {
/* 269 */         upper = middle - 1;
/* 270 */       } else if (c > 0) {
/* 271 */         lower = middle + 1;
/*     */       } else {
/* 273 */         return 
/* 274 */           lower + presentBehavior.resultIndex(comparator, key, list
/* 275 */           .subList(lower, upper + 1), middle - lower);
/*     */       }
/*     */     }
/* 278 */     return absentBehavior.resultIndex(lower);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\SortedLists.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */