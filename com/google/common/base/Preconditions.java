/*      */ package com.google.common.base;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @GwtCompatible
/*      */ public final class Preconditions
/*      */ {
/*      */   public static void checkArgument(boolean expression)
/*      */   {
/*  126 */     if (!expression) {
/*  127 */       throw new IllegalArgumentException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean expression, Object errorMessage)
/*      */   {
/*  140 */     if (!expression) {
/*  141 */       throw new IllegalArgumentException(String.valueOf(errorMessage));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean expression, String errorMessageTemplate, Object... errorMessageArgs)
/*      */   {
/*  162 */     if (!expression) {
/*  163 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, char p1)
/*      */   {
/*  175 */     if (!b) {
/*  176 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, int p1)
/*      */   {
/*  188 */     if (!b) {
/*  189 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, long p1)
/*      */   {
/*  201 */     if (!b) {
/*  202 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1)
/*      */   {
/*  215 */     if (!b) {
/*  216 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, char p1, char p2)
/*      */   {
/*  229 */     if (!b) {
/*  230 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, char p1, int p2)
/*      */   {
/*  243 */     if (!b) {
/*  244 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, char p1, long p2)
/*      */   {
/*  257 */     if (!b) {
/*  258 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, char p1, Object p2)
/*      */   {
/*  271 */     if (!b) {
/*  272 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, int p1, char p2)
/*      */   {
/*  285 */     if (!b) {
/*  286 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, int p1, int p2)
/*      */   {
/*  299 */     if (!b) {
/*  300 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, int p1, long p2)
/*      */   {
/*  313 */     if (!b) {
/*  314 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, int p1, Object p2)
/*      */   {
/*  327 */     if (!b) {
/*  328 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, long p1, char p2)
/*      */   {
/*  341 */     if (!b) {
/*  342 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, long p1, int p2)
/*      */   {
/*  355 */     if (!b) {
/*  356 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, long p1, long p2)
/*      */   {
/*  369 */     if (!b) {
/*  370 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, long p1, Object p2)
/*      */   {
/*  383 */     if (!b) {
/*  384 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, char p2)
/*      */   {
/*  397 */     if (!b) {
/*  398 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, int p2)
/*      */   {
/*  411 */     if (!b) {
/*  412 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, long p2)
/*      */   {
/*  425 */     if (!b) {
/*  426 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, Object p2)
/*      */   {
/*  439 */     if (!b) {
/*  440 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3)
/*      */   {
/*  457 */     if (!b) {
/*  458 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkArgument(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3, Object p4)
/*      */   {
/*  476 */     if (!b) {
/*  477 */       throw new IllegalArgumentException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean expression)
/*      */   {
/*  490 */     if (!expression) {
/*  491 */       throw new IllegalStateException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean expression, Object errorMessage)
/*      */   {
/*  506 */     if (!expression) {
/*  507 */       throw new IllegalStateException(String.valueOf(errorMessage));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean expression, String errorMessageTemplate, Object... errorMessageArgs)
/*      */   {
/*  530 */     if (!expression) {
/*  531 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, char p1)
/*      */   {
/*  544 */     if (!b) {
/*  545 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, int p1)
/*      */   {
/*  558 */     if (!b) {
/*  559 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, long p1)
/*      */   {
/*  572 */     if (!b) {
/*  573 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1)
/*      */   {
/*  587 */     if (!b) {
/*  588 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, char p1, char p2)
/*      */   {
/*  602 */     if (!b) {
/*  603 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, char p1, int p2)
/*      */   {
/*  616 */     if (!b) {
/*  617 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, char p1, long p2)
/*      */   {
/*  631 */     if (!b) {
/*  632 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, char p1, Object p2)
/*      */   {
/*  646 */     if (!b) {
/*  647 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, int p1, char p2)
/*      */   {
/*  660 */     if (!b) {
/*  661 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, int p1, int p2)
/*      */   {
/*  674 */     if (!b) {
/*  675 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, int p1, long p2)
/*      */   {
/*  688 */     if (!b) {
/*  689 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, int p1, Object p2)
/*      */   {
/*  703 */     if (!b) {
/*  704 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, long p1, char p2)
/*      */   {
/*  718 */     if (!b) {
/*  719 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, long p1, int p2)
/*      */   {
/*  732 */     if (!b) {
/*  733 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, long p1, long p2)
/*      */   {
/*  747 */     if (!b) {
/*  748 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, long p1, Object p2)
/*      */   {
/*  762 */     if (!b) {
/*  763 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, char p2)
/*      */   {
/*  777 */     if (!b) {
/*  778 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, int p2)
/*      */   {
/*  792 */     if (!b) {
/*  793 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, long p2)
/*      */   {
/*  807 */     if (!b) {
/*  808 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, Object p2)
/*      */   {
/*  822 */     if (!b) {
/*  823 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3)
/*      */   {
/*  841 */     if (!b) {
/*  842 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkState(boolean b, String errorMessageTemplate, Object p1, Object p2, Object p3, Object p4)
/*      */   {
/*  861 */     if (!b) {
/*  862 */       throw new IllegalStateException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference)
/*      */   {
/*  876 */     if (reference == null) {
/*  877 */       throw new NullPointerException();
/*      */     }
/*  879 */     return reference;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference, Object errorMessage)
/*      */   {
/*  894 */     if (reference == null) {
/*  895 */       throw new NullPointerException(String.valueOf(errorMessage));
/*      */     }
/*  897 */     return reference;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T reference, String errorMessageTemplate, Object... errorMessageArgs)
/*      */   {
/*  918 */     if (reference == null) {
/*  919 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, errorMessageArgs));
/*      */     }
/*  921 */     return reference;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1)
/*      */   {
/*  933 */     if (obj == null) {
/*  934 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1) }));
/*      */     }
/*  936 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1)
/*      */   {
/*  948 */     if (obj == null) {
/*  949 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1) }));
/*      */     }
/*  951 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1)
/*      */   {
/*  963 */     if (obj == null) {
/*  964 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1) }));
/*      */     }
/*  966 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1)
/*      */   {
/*  979 */     if (obj == null) {
/*  980 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1 }));
/*      */     }
/*  982 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, char p2)
/*      */   {
/*  994 */     if (obj == null) {
/*  995 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/*  997 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, int p2)
/*      */   {
/* 1009 */     if (obj == null) {
/* 1010 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/* 1012 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, long p2)
/*      */   {
/* 1024 */     if (obj == null) {
/* 1025 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/* 1027 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, char p1, Object p2)
/*      */   {
/* 1040 */     if (obj == null) {
/* 1041 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Character.valueOf(p1), p2 }));
/*      */     }
/* 1043 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, char p2)
/*      */   {
/* 1055 */     if (obj == null) {
/* 1056 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/* 1058 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, int p2)
/*      */   {
/* 1070 */     if (obj == null) {
/* 1071 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/* 1073 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, long p2)
/*      */   {
/* 1085 */     if (obj == null) {
/* 1086 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/* 1088 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, int p1, Object p2)
/*      */   {
/* 1101 */     if (obj == null) {
/* 1102 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Integer.valueOf(p1), p2 }));
/*      */     }
/* 1104 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, char p2)
/*      */   {
/* 1116 */     if (obj == null) {
/* 1117 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Character.valueOf(p2) }));
/*      */     }
/* 1119 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, int p2)
/*      */   {
/* 1131 */     if (obj == null) {
/* 1132 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Integer.valueOf(p2) }));
/*      */     }
/* 1134 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, long p2)
/*      */   {
/* 1146 */     if (obj == null) {
/* 1147 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), Long.valueOf(p2) }));
/*      */     }
/* 1149 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, long p1, Object p2)
/*      */   {
/* 1162 */     if (obj == null) {
/* 1163 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { Long.valueOf(p1), p2 }));
/*      */     }
/* 1165 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, char p2)
/*      */   {
/* 1178 */     if (obj == null) {
/* 1179 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Character.valueOf(p2) }));
/*      */     }
/* 1181 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, int p2)
/*      */   {
/* 1194 */     if (obj == null) {
/* 1195 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Integer.valueOf(p2) }));
/*      */     }
/* 1197 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, long p2)
/*      */   {
/* 1210 */     if (obj == null) {
/* 1211 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, Long.valueOf(p2) }));
/*      */     }
/* 1213 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, Object p2)
/*      */   {
/* 1226 */     if (obj == null) {
/* 1227 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2 }));
/*      */     }
/* 1229 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, Object p2, Object p3)
/*      */   {
/* 1246 */     if (obj == null) {
/* 1247 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3 }));
/*      */     }
/* 1249 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T checkNotNull(T obj, String errorMessageTemplate, Object p1, Object p2, Object p3, Object p4)
/*      */   {
/* 1267 */     if (obj == null) {
/* 1268 */       throw new NullPointerException(Strings.lenientFormat(errorMessageTemplate, new Object[] { p1, p2, p3, p4 }));
/*      */     }
/* 1270 */     return obj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkElementIndex(int index, int size)
/*      */   {
/* 1311 */     return checkElementIndex(index, size, "index");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkElementIndex(int index, int size, String desc)
/*      */   {
/* 1328 */     if ((index < 0) || (index >= size)) {
/* 1329 */       throw new IndexOutOfBoundsException(badElementIndex(index, size, desc));
/*      */     }
/* 1331 */     return index;
/*      */   }
/*      */   
/*      */   private static String badElementIndex(int index, int size, String desc) {
/* 1335 */     if (index < 0)
/* 1336 */       return Strings.lenientFormat("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) });
/* 1337 */     if (size < 0) {
/* 1338 */       throw new IllegalArgumentException("negative size: " + size);
/*      */     }
/* 1340 */     return Strings.lenientFormat("%s (%s) must be less than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkPositionIndex(int index, int size)
/*      */   {
/* 1356 */     return checkPositionIndex(index, size, "index");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static int checkPositionIndex(int index, int size, String desc)
/*      */   {
/* 1373 */     if ((index < 0) || (index > size)) {
/* 1374 */       throw new IndexOutOfBoundsException(badPositionIndex(index, size, desc));
/*      */     }
/* 1376 */     return index;
/*      */   }
/*      */   
/*      */   private static String badPositionIndex(int index, int size, String desc) {
/* 1380 */     if (index < 0)
/* 1381 */       return Strings.lenientFormat("%s (%s) must not be negative", new Object[] { desc, Integer.valueOf(index) });
/* 1382 */     if (size < 0) {
/* 1383 */       throw new IllegalArgumentException("negative size: " + size);
/*      */     }
/* 1385 */     return Strings.lenientFormat("%s (%s) must not be greater than size (%s)", new Object[] { desc, Integer.valueOf(index), Integer.valueOf(size) });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void checkPositionIndexes(int start, int end, int size)
/*      */   {
/* 1403 */     if ((start < 0) || (end < start) || (end > size)) {
/* 1404 */       throw new IndexOutOfBoundsException(badPositionIndexes(start, end, size));
/*      */     }
/*      */   }
/*      */   
/*      */   private static String badPositionIndexes(int start, int end, int size) {
/* 1409 */     if ((start < 0) || (start > size)) {
/* 1410 */       return badPositionIndex(start, size, "start index");
/*      */     }
/* 1412 */     if ((end < 0) || (end > size)) {
/* 1413 */       return badPositionIndex(end, size, "end index");
/*      */     }
/*      */     
/* 1416 */     return Strings.lenientFormat("end index (%s) must not be less than start index (%s)", new Object[] { Integer.valueOf(end), Integer.valueOf(start) });
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\base\Preconditions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */