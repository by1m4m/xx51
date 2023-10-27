/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TopologicalSort<T>
/*     */ {
/*  50 */   private final Map<T, Set<T>> _dependencies = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addDependency(T dependent, T dependency)
/*     */   {
/*  59 */     Set<T> set = (Set)this._dependencies.get(dependent);
/*  60 */     if (set == null)
/*     */     {
/*  62 */       set = new HashSet();
/*  63 */       this._dependencies.put(dependent, set);
/*     */     }
/*  65 */     set.add(dependency);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sort(T[] array)
/*     */   {
/*  75 */     List<T> sorted = new ArrayList();
/*  76 */     Set<T> visited = new HashSet();
/*  77 */     Comparator<T> comparator = new InitialOrderComparator(array);
/*     */     
/*     */ 
/*  80 */     for (T t : array) {
/*  81 */       visit(t, visited, sorted, comparator);
/*     */     }
/*  83 */     sorted.toArray(array);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sort(Collection<T> list)
/*     */   {
/*  93 */     List<T> sorted = new ArrayList();
/*  94 */     Set<T> visited = new HashSet();
/*  95 */     Comparator<T> comparator = new InitialOrderComparator(list);
/*     */     
/*     */ 
/*  98 */     for (T t : list) {
/*  99 */       visit(t, visited, sorted, comparator);
/*     */     }
/* 101 */     list.clear();
/* 102 */     list.addAll(sorted);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void visit(T item, Set<T> visited, List<T> sorted, Comparator<T> comparator)
/*     */   {
/* 114 */     if (!visited.contains(item))
/*     */     {
/*     */ 
/* 117 */       visited.add(item);
/*     */       
/*     */ 
/* 120 */       Set<T> dependencies = (Set)this._dependencies.get(item);
/* 121 */       if (dependencies != null)
/*     */       {
/*     */ 
/* 124 */         SortedSet<T> ordered_deps = new TreeSet(comparator);
/* 125 */         ordered_deps.addAll(dependencies);
/*     */         
/*     */ 
/*     */         try
/*     */         {
/* 130 */           for (T d : ordered_deps) {
/* 131 */             visit(d, visited, sorted, comparator);
/*     */           }
/*     */         }
/*     */         catch (CyclicException e) {
/* 135 */           throw new CyclicException(item, e);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 142 */       sorted.add(item);
/*     */     }
/* 144 */     else if (!sorted.contains(item))
/*     */     {
/*     */ 
/* 147 */       throw new CyclicException(item);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class InitialOrderComparator<T>
/*     */     implements Comparator<T>
/*     */   {
/* 158 */     private final Map<T, Integer> _indexes = new HashMap();
/*     */     
/*     */     InitialOrderComparator(T[] initial) {
/* 161 */       int i = 0;
/* 162 */       for (T t : initial) {
/* 163 */         this._indexes.put(t, Integer.valueOf(i++));
/*     */       }
/*     */     }
/*     */     
/*     */     InitialOrderComparator(Collection<T> initial) {
/* 168 */       int i = 0;
/* 169 */       for (T t : initial) {
/* 170 */         this._indexes.put(t, Integer.valueOf(i++));
/*     */       }
/*     */     }
/*     */     
/*     */     public int compare(T o1, T o2)
/*     */     {
/* 176 */       Integer i1 = (Integer)this._indexes.get(o1);
/* 177 */       Integer i2 = (Integer)this._indexes.get(o2);
/* 178 */       if ((i1 == null) || (i2 == null) || (i1.equals(o2)))
/* 179 */         return 0;
/* 180 */       if (i1.intValue() < i2.intValue())
/* 181 */         return -1;
/* 182 */       return 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 189 */     return "TopologicalSort " + this._dependencies;
/*     */   }
/*     */   
/*     */   private static class CyclicException extends IllegalStateException
/*     */   {
/*     */     CyclicException(Object item)
/*     */     {
/* 196 */       super();
/*     */     }
/*     */     
/*     */     CyclicException(Object item, CyclicException e)
/*     */     {
/* 201 */       super(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\TopologicalSort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */