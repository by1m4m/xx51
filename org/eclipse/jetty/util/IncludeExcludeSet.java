/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IncludeExcludeSet<T, P>
/*     */   implements Predicate<P>
/*     */ {
/*     */   private final Set<T> _includes;
/*     */   private final Predicate<P> _includePredicate;
/*     */   private final Set<T> _excludes;
/*     */   private final Predicate<P> _excludePredicate;
/*     */   
/*     */   private static class SetContainsPredicate<T>
/*     */     implements Predicate<T>
/*     */   {
/*     */     private final Set<T> set;
/*     */     
/*     */     public SetContainsPredicate(Set<T> set)
/*     */     {
/*  50 */       this.set = set;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean test(T item)
/*     */     {
/*  56 */       return this.set.contains(item);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IncludeExcludeSet()
/*     */   {
/*  65 */     this(HashSet.class);
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
/*     */   public <SET extends Set<T>> IncludeExcludeSet(Class<SET> setClass)
/*     */   {
/*     */     try
/*     */     {
/*  80 */       this._includes = ((Set)setClass.newInstance());
/*  81 */       this._excludes = ((Set)setClass.newInstance());
/*     */       
/*  83 */       if ((this._includes instanceof Predicate))
/*     */       {
/*  85 */         this._includePredicate = ((Predicate)this._includes);
/*     */       }
/*     */       else
/*     */       {
/*  89 */         this._includePredicate = new SetContainsPredicate(this._includes);
/*     */       }
/*     */       
/*  92 */       if ((this._excludes instanceof Predicate))
/*     */       {
/*  94 */         this._excludePredicate = ((Predicate)this._excludes);
/*     */       }
/*     */       else
/*     */       {
/*  98 */         this._excludePredicate = new SetContainsPredicate(this._excludes);
/*     */       }
/*     */     }
/*     */     catch (InstantiationException|IllegalAccessException e)
/*     */     {
/* 103 */       throw new RuntimeException(e);
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
/*     */   public <SET extends Set<T>> IncludeExcludeSet(Set<T> includeSet, Predicate<P> includePredicate, Set<T> excludeSet, Predicate<P> excludePredicate)
/*     */   {
/* 118 */     Objects.requireNonNull(includeSet, "Include Set");
/* 119 */     Objects.requireNonNull(includePredicate, "Include Predicate");
/* 120 */     Objects.requireNonNull(excludeSet, "Exclude Set");
/* 121 */     Objects.requireNonNull(excludePredicate, "Exclude Predicate");
/*     */     
/* 123 */     this._includes = includeSet;
/* 124 */     this._includePredicate = includePredicate;
/* 125 */     this._excludes = excludeSet;
/* 126 */     this._excludePredicate = excludePredicate;
/*     */   }
/*     */   
/*     */   public void include(T element)
/*     */   {
/* 131 */     this._includes.add(element);
/*     */   }
/*     */   
/*     */   public void include(T... element)
/*     */   {
/* 136 */     for (T e : element) {
/* 137 */       this._includes.add(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void exclude(T element) {
/* 142 */     this._excludes.add(element);
/*     */   }
/*     */   
/*     */   public void exclude(T... element)
/*     */   {
/* 147 */     for (T e : element) {
/* 148 */       this._excludes.add(e);
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean matches(P t) {
/* 154 */     return test(t);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean test(P t)
/*     */   {
/* 160 */     if ((!this._includes.isEmpty()) && (!this._includePredicate.test(t)))
/* 161 */       return false;
/* 162 */     return !this._excludePredicate.test(t);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Boolean isIncludedAndNotExcluded(P t)
/*     */   {
/* 172 */     if (this._excludePredicate.test(t))
/* 173 */       return Boolean.FALSE;
/* 174 */     if (this._includePredicate.test(t)) {
/* 175 */       return Boolean.TRUE;
/*     */     }
/* 177 */     return null;
/*     */   }
/*     */   
/*     */   public boolean hasIncludes()
/*     */   {
/* 182 */     return !this._includes.isEmpty();
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 187 */     return this._includes.size() + this._excludes.size();
/*     */   }
/*     */   
/*     */   public Set<T> getIncluded()
/*     */   {
/* 192 */     return this._includes;
/*     */   }
/*     */   
/*     */   public Set<T> getExcluded()
/*     */   {
/* 197 */     return this._excludes;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 202 */     this._includes.clear();
/* 203 */     this._excludes.clear();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 209 */     return String.format("%s@%x{i=%s,ip=%s,e=%s,ep=%s}", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()), this._includes, this._includePredicate, this._excludes, this._excludePredicate });
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 214 */     return (this._includes.isEmpty()) && (this._excludes.isEmpty());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\IncludeExcludeSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */