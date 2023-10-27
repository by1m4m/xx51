/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayUtil
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   public static <T> T[] removeFromArray(T[] array, Object item)
/*     */   {
/*  38 */     if ((item == null) || (array == null))
/*  39 */       return array;
/*  40 */     for (int i = array.length; i-- > 0;)
/*     */     {
/*  42 */       if (item.equals(array[i]))
/*     */       {
/*  44 */         Class<?> c = array == null ? item.getClass() : array.getClass().getComponentType();
/*     */         
/*  46 */         T[] na = (Object[])Array.newInstance(c, Array.getLength(array) - 1);
/*  47 */         if (i > 0)
/*  48 */           System.arraycopy(array, 0, na, 0, i);
/*  49 */         if (i + 1 < array.length)
/*  50 */           System.arraycopy(array, i + 1, na, i, array.length - (i + 1));
/*  51 */         return na;
/*     */       }
/*     */     }
/*  54 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T> T[] add(T[] array1, T[] array2)
/*     */   {
/*  66 */     if ((array1 == null) || (array1.length == 0))
/*  67 */       return array2;
/*  68 */     if ((array2 == null) || (array2.length == 0)) {
/*  69 */       return array1;
/*     */     }
/*  71 */     T[] na = Arrays.copyOf(array1, array1.length + array2.length);
/*  72 */     System.arraycopy(array2, 0, na, array1.length, array2.length);
/*  73 */     return na;
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
/*     */   public static <T> T[] addToArray(T[] array, T item, Class<?> type)
/*     */   {
/*  86 */     if (array == null)
/*     */     {
/*  88 */       if ((type == null) && (item != null)) {
/*  89 */         type = item.getClass();
/*     */       }
/*  91 */       T[] na = (Object[])Array.newInstance(type, 1);
/*  92 */       na[0] = item;
/*  93 */       return na;
/*     */     }
/*     */     
/*     */ 
/*  97 */     T[] na = Arrays.copyOf(array, array.length + 1);
/*  98 */     na[array.length] = item;
/*  99 */     return na;
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
/*     */   public static <T> T[] prependToArray(T item, T[] array, Class<?> type)
/*     */   {
/* 113 */     if (array == null)
/*     */     {
/* 115 */       if ((type == null) && (item != null)) {
/* 116 */         type = item.getClass();
/*     */       }
/* 118 */       T[] na = (Object[])Array.newInstance(type, 1);
/* 119 */       na[0] = item;
/* 120 */       return na;
/*     */     }
/*     */     
/*     */ 
/* 124 */     Class<?> c = array.getClass().getComponentType();
/*     */     
/* 126 */     T[] na = (Object[])Array.newInstance(c, Array.getLength(array) + 1);
/* 127 */     System.arraycopy(array, 0, na, 1, array.length);
/* 128 */     na[0] = item;
/* 129 */     return na;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> List<E> asMutableList(E[] array)
/*     */   {
/* 141 */     if ((array == null) || (array.length == 0))
/* 142 */       return new ArrayList();
/* 143 */     return new ArrayList(Arrays.asList(array));
/*     */   }
/*     */   
/*     */ 
/*     */   public static <T> T[] removeNulls(T[] array)
/*     */   {
/* 149 */     for (T t : array)
/*     */     {
/* 151 */       if (t == null)
/*     */       {
/* 153 */         List<T> list = new ArrayList();
/* 154 */         for (T t2 : array)
/* 155 */           if (t2 != null)
/* 156 */             list.add(t2);
/* 157 */         return list.toArray(Arrays.copyOf(array, list.size()));
/*     */       }
/*     */     }
/* 160 */     return array;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\ArrayUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */