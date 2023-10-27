/*     */ package org.checkerframework.checker.nullness;
/*     */ 
/*     */ import org.checkerframework.checker.nullness.qual.EnsuresNonNull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NullnessUtil
/*     */ {
/*     */   private NullnessUtil()
/*     */   {
/*  31 */     throw new AssertionError("shouldn't be instantiated");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T castNonNull(T ref)
/*     */   {
/*  70 */     assert (ref != null) : "Misuse of castNonNull: called with a null argument";
/*  71 */     return ref;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T[] castNonNullDeep(T[] arr)
/*     */   {
/*  83 */     return (Object[])castNonNullArray(arr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T[][] castNonNullDeep(T[][] arr)
/*     */   {
/*  95 */     return (Object[][])castNonNullArray(arr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T[][][] castNonNullDeep(T[][][] arr)
/*     */   {
/* 107 */     return (Object[][][])castNonNullArray(arr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T[][][][] castNonNullDeep(T[][][][] arr)
/*     */   {
/* 120 */     return (Object[][][][])castNonNullArray(arr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @EnsuresNonNull({"#1"})
/*     */   public static <T> T[][][][][] castNonNullDeep(T[][][][][] arr)
/*     */   {
/* 133 */     return (Object[][][][][])castNonNullArray(arr);
/*     */   }
/*     */   
/*     */   private static <T> T[] castNonNullArray(T[] arr)
/*     */   {
/* 138 */     assert (arr != null) : "Misuse of castNonNullArray: called with a null array argument";
/* 139 */     for (int i = 0; i < arr.length; i++) {
/* 140 */       assert (arr[i] != null) : "Misuse of castNonNull: called with a null array element";
/* 141 */       checkIfArray(arr[i]);
/*     */     }
/* 143 */     return (Object[])arr;
/*     */   }
/*     */   
/*     */   private static void checkIfArray(Object ref) {
/* 147 */     assert (ref != null) : "Misuse of checkIfArray: called with a null argument";
/* 148 */     Class<?> comp = ref.getClass().getComponentType();
/* 149 */     if (comp != null)
/*     */     {
/* 151 */       if (!comp.isPrimitive())
/*     */       {
/*     */ 
/*     */ 
/* 155 */         castNonNullArray((Object[])ref);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\nullness\NullnessUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */