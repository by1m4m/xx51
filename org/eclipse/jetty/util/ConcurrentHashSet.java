/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class ConcurrentHashSet<E>
/*     */   extends AbstractSet<E>
/*     */   implements Set<E>
/*     */ {
/*  34 */   private final Map<E, Boolean> _map = new ConcurrentHashMap();
/*  35 */   private transient Set<E> _keys = this._map.keySet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean add(E e)
/*     */   {
/*  44 */     return this._map.put(e, Boolean.TRUE) == null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void clear()
/*     */   {
/*  50 */     this._map.clear();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean contains(Object o)
/*     */   {
/*  56 */     return this._map.containsKey(o);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean containsAll(Collection<?> c)
/*     */   {
/*  62 */     return this._keys.containsAll(c);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  68 */     return (o == this) || (this._keys.equals(o));
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  74 */     return this._keys.hashCode();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/*  80 */     return this._map.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<E> iterator()
/*     */   {
/*  86 */     return this._keys.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean remove(Object o)
/*     */   {
/*  92 */     return this._map.remove(o) != null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean removeAll(Collection<?> c)
/*     */   {
/*  98 */     return this._keys.removeAll(c);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean retainAll(Collection<?> c)
/*     */   {
/* 104 */     return this._keys.retainAll(c);
/*     */   }
/*     */   
/*     */ 
/*     */   public int size()
/*     */   {
/* 110 */     return this._map.size();
/*     */   }
/*     */   
/*     */ 
/*     */   public Object[] toArray()
/*     */   {
/* 116 */     return this._keys.toArray();
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T[] toArray(T[] a)
/*     */   {
/* 122 */     return this._keys.toArray(a);
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 128 */     return this._keys.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\ConcurrentHashSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */