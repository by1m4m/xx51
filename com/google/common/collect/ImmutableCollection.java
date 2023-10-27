/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Spliterator;
/*     */ import java.util.Spliterators;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtCompatible(emulated=true)
/*     */ public abstract class ImmutableCollection<E>
/*     */   extends AbstractCollection<E>
/*     */   implements Serializable
/*     */ {
/*     */   static final int SPLITERATOR_CHARACTERISTICS = 1296;
/*     */   
/*     */   public abstract UnmodifiableIterator<E> iterator();
/*     */   
/*     */   public Spliterator<E> spliterator()
/*     */   {
/* 179 */     return Spliterators.spliterator(this, 1296);
/*     */   }
/*     */   
/* 182 */   private static final Object[] EMPTY_ARRAY = new Object[0];
/*     */   
/*     */   public final Object[] toArray()
/*     */   {
/* 186 */     return toArray(EMPTY_ARRAY);
/*     */   }
/*     */   
/*     */   @CanIgnoreReturnValue
/*     */   public final <T> T[] toArray(T[] other)
/*     */   {
/* 192 */     Preconditions.checkNotNull(other);
/* 193 */     int size = size();
/*     */     
/* 195 */     if (other.length < size) {
/* 196 */       Object[] internal = internalArray();
/* 197 */       if (internal != null) {
/* 198 */         return Platform.copy(internal, internalArrayStart(), internalArrayEnd(), other);
/*     */       }
/* 200 */       other = ObjectArrays.newArray(other, size);
/* 201 */     } else if (other.length > size) {
/* 202 */       other[size] = null;
/*     */     }
/* 204 */     copyIntoArray(other, 0);
/* 205 */     return other;
/*     */   }
/*     */   
/*     */ 
/*     */   Object[] internalArray()
/*     */   {
/* 211 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int internalArrayStart()
/*     */   {
/* 219 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int internalArrayEnd()
/*     */   {
/* 227 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean contains(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean add(E e)
/*     */   {
/* 243 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean remove(Object object)
/*     */   {
/* 256 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean addAll(Collection<? extends E> newElements)
/*     */   {
/* 269 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean removeAll(Collection<?> oldElements)
/*     */   {
/* 282 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   @CanIgnoreReturnValue
/*     */   public final boolean removeIf(Predicate<? super E> filter)
/*     */   {
/* 295 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final boolean retainAll(Collection<?> elementsToKeep)
/*     */   {
/* 307 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final void clear()
/*     */   {
/* 319 */     throw new UnsupportedOperationException();
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
/*     */   public ImmutableList<E> asList()
/*     */   {
/* 333 */     switch (size()) {
/*     */     case 0: 
/* 335 */       return ImmutableList.of();
/*     */     case 1: 
/* 337 */       return ImmutableList.of(iterator().next());
/*     */     }
/* 339 */     return new RegularImmutableAsList(this, toArray());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract boolean isPartialView();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   int copyIntoArray(Object[] dst, int offset)
/*     */   {
/* 357 */     for (UnmodifiableIterator localUnmodifiableIterator = iterator(); localUnmodifiableIterator.hasNext();) { E e = localUnmodifiableIterator.next();
/* 358 */       dst[(offset++)] = e;
/*     */     }
/* 360 */     return offset;
/*     */   }
/*     */   
/*     */   Object writeReplace()
/*     */   {
/* 365 */     return new ImmutableList.SerializedForm(toArray());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static abstract class Builder<E>
/*     */   {
/*     */     static final int DEFAULT_INITIAL_CAPACITY = 4;
/*     */     
/*     */ 
/*     */     static int expandedCapacity(int oldCapacity, int minCapacity)
/*     */     {
/* 377 */       if (minCapacity < 0) {
/* 378 */         throw new AssertionError("cannot store more than MAX_VALUE elements");
/*     */       }
/*     */       
/* 381 */       int newCapacity = oldCapacity + (oldCapacity >> 1) + 1;
/* 382 */       if (newCapacity < minCapacity) {
/* 383 */         newCapacity = Integer.highestOneBit(minCapacity - 1) << 1;
/*     */       }
/* 385 */       if (newCapacity < 0) {
/* 386 */         newCapacity = Integer.MAX_VALUE;
/*     */       }
/*     */       
/* 389 */       return newCapacity;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public abstract Builder<E> add(E paramE);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> add(E... elements)
/*     */     {
/* 418 */       for (E element : elements) {
/* 419 */         add(element);
/*     */       }
/* 421 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterable<? extends E> elements)
/*     */     {
/* 436 */       for (E element : elements) {
/* 437 */         add(element);
/*     */       }
/* 439 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @CanIgnoreReturnValue
/*     */     public Builder<E> addAll(Iterator<? extends E> elements)
/*     */     {
/* 454 */       while (elements.hasNext()) {
/* 455 */         add(elements.next());
/*     */       }
/* 457 */       return this;
/*     */     }
/*     */     
/*     */     public abstract ImmutableCollection<E> build();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\ImmutableCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */