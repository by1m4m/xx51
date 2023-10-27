/*      */ package com.google.common.collect;
/*      */ 
/*      */ import com.google.common.annotations.Beta;
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Function;
/*      */ import com.google.common.base.Objects;
/*      */ import com.google.common.base.Optional;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.base.Predicates;
/*      */ import com.google.common.primitives.Ints;
/*      */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Deque;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.PriorityQueue;
/*      */ import java.util.Queue;
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
/*      */ @GwtCompatible(emulated=true)
/*      */ public final class Iterators
/*      */ {
/*      */   static <T> UnmodifiableIterator<T> emptyIterator()
/*      */   {
/*   77 */     return emptyListIterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static <T> UnmodifiableListIterator<T> emptyListIterator()
/*      */   {
/*   88 */     return ArrayItr.EMPTY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static enum EmptyModifiableIterator
/*      */     implements Iterator<Object>
/*      */   {
/*   96 */     INSTANCE;
/*      */     
/*      */     private EmptyModifiableIterator() {}
/*      */     
/*  100 */     public boolean hasNext() { return false; }
/*      */     
/*      */ 
/*      */     public Object next()
/*      */     {
/*  105 */       throw new NoSuchElementException();
/*      */     }
/*      */     
/*      */     public void remove()
/*      */     {
/*  110 */       CollectPreconditions.checkRemove(false);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static <T> Iterator<T> emptyModifiableIterator()
/*      */   {
/*  121 */     return EmptyModifiableIterator.INSTANCE;
/*      */   }
/*      */   
/*      */ 
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(Iterator<? extends T> iterator)
/*      */   {
/*  127 */     Preconditions.checkNotNull(iterator);
/*  128 */     if ((iterator instanceof UnmodifiableIterator))
/*      */     {
/*  130 */       UnmodifiableIterator<T> result = (UnmodifiableIterator)iterator;
/*  131 */       return result;
/*      */     }
/*  133 */     new UnmodifiableIterator()
/*      */     {
/*      */       public boolean hasNext() {
/*  136 */         return this.val$iterator.hasNext();
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/*  141 */         return (T)this.val$iterator.next();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static <T> UnmodifiableIterator<T> unmodifiableIterator(UnmodifiableIterator<T> iterator)
/*      */   {
/*  154 */     return (UnmodifiableIterator)Preconditions.checkNotNull(iterator);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int size(Iterator<?> iterator)
/*      */   {
/*  162 */     long count = 0L;
/*  163 */     while (iterator.hasNext()) {
/*  164 */       iterator.next();
/*  165 */       count += 1L;
/*      */     }
/*  167 */     return Ints.saturatedCast(count);
/*      */   }
/*      */   
/*      */   public static boolean contains(Iterator<?> iterator, Object element)
/*      */   {
/*  172 */     if (element == null) {
/*  173 */       do { if (!iterator.hasNext()) break;
/*  174 */       } while (iterator.next() != null);
/*  175 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  179 */     while (iterator.hasNext()) {
/*  180 */       if (element.equals(iterator.next())) {
/*  181 */         return true;
/*      */       }
/*      */     }
/*      */     
/*  185 */     return false;
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
/*      */   public static boolean removeAll(Iterator<?> removeFrom, Collection<?> elementsToRemove)
/*      */   {
/*  198 */     Preconditions.checkNotNull(elementsToRemove);
/*  199 */     boolean result = false;
/*  200 */     while (removeFrom.hasNext()) {
/*  201 */       if (elementsToRemove.contains(removeFrom.next())) {
/*  202 */         removeFrom.remove();
/*  203 */         result = true;
/*      */       }
/*      */     }
/*  206 */     return result;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean removeIf(Iterator<T> removeFrom, Predicate<? super T> predicate)
/*      */   {
/*  220 */     Preconditions.checkNotNull(predicate);
/*  221 */     boolean modified = false;
/*  222 */     while (removeFrom.hasNext()) {
/*  223 */       if (predicate.apply(removeFrom.next())) {
/*  224 */         removeFrom.remove();
/*  225 */         modified = true;
/*      */       }
/*      */     }
/*  228 */     return modified;
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
/*      */   @CanIgnoreReturnValue
/*      */   public static boolean retainAll(Iterator<?> removeFrom, Collection<?> elementsToRetain)
/*      */   {
/*  242 */     Preconditions.checkNotNull(elementsToRetain);
/*  243 */     boolean result = false;
/*  244 */     while (removeFrom.hasNext()) {
/*  245 */       if (!elementsToRetain.contains(removeFrom.next())) {
/*  246 */         removeFrom.remove();
/*  247 */         result = true;
/*      */       }
/*      */     }
/*  250 */     return result;
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
/*      */   public static boolean elementsEqual(Iterator<?> iterator1, Iterator<?> iterator2)
/*      */   {
/*  263 */     while (iterator1.hasNext()) {
/*  264 */       if (!iterator2.hasNext()) {
/*  265 */         return false;
/*      */       }
/*  267 */       Object o1 = iterator1.next();
/*  268 */       Object o2 = iterator2.next();
/*  269 */       if (!Objects.equal(o1, o2)) {
/*  270 */         return false;
/*      */       }
/*      */     }
/*  273 */     return !iterator2.hasNext();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toString(Iterator<?> iterator)
/*      */   {
/*  281 */     StringBuilder sb = new StringBuilder().append('[');
/*  282 */     boolean first = true;
/*  283 */     while (iterator.hasNext()) {
/*  284 */       if (!first) {
/*  285 */         sb.append(", ");
/*      */       }
/*  287 */       first = false;
/*  288 */       sb.append(iterator.next());
/*      */     }
/*  290 */     return ']';
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> T getOnlyElement(Iterator<T> iterator)
/*      */   {
/*  302 */     T first = iterator.next();
/*  303 */     if (!iterator.hasNext()) {
/*  304 */       return first;
/*      */     }
/*      */     
/*  307 */     StringBuilder sb = new StringBuilder().append("expected one element but was: <").append(first);
/*  308 */     for (int i = 0; (i < 4) && (iterator.hasNext()); i++) {
/*  309 */       sb.append(", ").append(iterator.next());
/*      */     }
/*  311 */     if (iterator.hasNext()) {
/*  312 */       sb.append(", ...");
/*      */     }
/*  314 */     sb.append('>');
/*      */     
/*  316 */     throw new IllegalArgumentException(sb.toString());
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
/*      */   public static <T> T getOnlyElement(Iterator<? extends T> iterator, T defaultValue)
/*      */   {
/*  329 */     return (T)(iterator.hasNext() ? getOnlyElement(iterator) : defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static <T> T[] toArray(Iterator<? extends T> iterator, Class<T> type)
/*      */   {
/*  342 */     List<T> list = Lists.newArrayList(iterator);
/*  343 */     return Iterables.toArray(list, type);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static <T> boolean addAll(Collection<T> addTo, Iterator<? extends T> iterator)
/*      */   {
/*  354 */     Preconditions.checkNotNull(addTo);
/*  355 */     Preconditions.checkNotNull(iterator);
/*  356 */     boolean wasModified = false;
/*  357 */     while (iterator.hasNext()) {
/*  358 */       wasModified |= addTo.add(iterator.next());
/*      */     }
/*  360 */     return wasModified;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int frequency(Iterator<?> iterator, Object element)
/*      */   {
/*  370 */     int count = 0;
/*  371 */     while (contains(iterator, element))
/*      */     {
/*      */ 
/*  374 */       count++;
/*      */     }
/*  376 */     return count;
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
/*      */   public static <T> Iterator<T> cycle(Iterable<T> iterable)
/*      */   {
/*  392 */     Preconditions.checkNotNull(iterable);
/*  393 */     new Iterator() {
/*  394 */       Iterator<T> iterator = Iterators.emptyModifiableIterator();
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
/*      */       public boolean hasNext()
/*      */       {
/*  407 */         return (this.iterator.hasNext()) || (this.val$iterable.iterator().hasNext());
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/*  412 */         if (!this.iterator.hasNext()) {
/*  413 */           this.iterator = this.val$iterable.iterator();
/*  414 */           if (!this.iterator.hasNext()) {
/*  415 */             throw new NoSuchElementException();
/*      */           }
/*      */         }
/*  418 */         return (T)this.iterator.next();
/*      */       }
/*      */       
/*      */       public void remove()
/*      */       {
/*  423 */         this.iterator.remove();
/*      */       }
/*      */     };
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
/*      */   @SafeVarargs
/*      */   public static <T> Iterator<T> cycle(T... elements)
/*      */   {
/*  442 */     return cycle(Lists.newArrayList(elements));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static <T> Iterator<T> consumingForArray(T... elements)
/*      */   {
/*  452 */     new UnmodifiableIterator() {
/*  453 */       int index = 0;
/*      */       
/*      */       public boolean hasNext()
/*      */       {
/*  457 */         return this.index < this.val$elements.length;
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/*  462 */         if (!hasNext()) {
/*  463 */           throw new NoSuchElementException();
/*      */         }
/*  465 */         T result = this.val$elements[this.index];
/*  466 */         this.val$elements[this.index] = null;
/*  467 */         this.index += 1;
/*  468 */         return result;
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b)
/*      */   {
/*  482 */     Preconditions.checkNotNull(a);
/*  483 */     Preconditions.checkNotNull(b);
/*  484 */     return concat(consumingForArray(new Iterator[] { a, b }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c)
/*      */   {
/*  497 */     Preconditions.checkNotNull(a);
/*  498 */     Preconditions.checkNotNull(b);
/*  499 */     Preconditions.checkNotNull(c);
/*  500 */     return concat(consumingForArray(new Iterator[] { a, b, c }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T> a, Iterator<? extends T> b, Iterator<? extends T> c, Iterator<? extends T> d)
/*      */   {
/*  517 */     Preconditions.checkNotNull(a);
/*  518 */     Preconditions.checkNotNull(b);
/*  519 */     Preconditions.checkNotNull(c);
/*  520 */     Preconditions.checkNotNull(d);
/*  521 */     return concat(consumingForArray(new Iterator[] { a, b, c, d }));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends T>... inputs)
/*      */   {
/*  535 */     return concatNoDefensiveCopy((Iterator[])Arrays.copyOf(inputs, inputs.length));
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
/*      */   public static <T> Iterator<T> concat(Iterator<? extends Iterator<? extends T>> inputs)
/*      */   {
/*  548 */     return new ConcatenatedIterator(inputs);
/*      */   }
/*      */   
/*      */   static <T> Iterator<T> concatNoDefensiveCopy(Iterator<? extends T>... inputs)
/*      */   {
/*  553 */     for (Iterator<? extends T> input : (Iterator[])Preconditions.checkNotNull(inputs)) {
/*  554 */       Preconditions.checkNotNull(input);
/*      */     }
/*  556 */     return concat(consumingForArray(inputs));
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
/*      */   public static <T> UnmodifiableIterator<List<T>> partition(Iterator<T> iterator, int size)
/*      */   {
/*  574 */     return partitionImpl(iterator, size, false);
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
/*      */   public static <T> UnmodifiableIterator<List<T>> paddedPartition(Iterator<T> iterator, int size)
/*      */   {
/*  592 */     return partitionImpl(iterator, size, true);
/*      */   }
/*      */   
/*      */   private static <T> UnmodifiableIterator<List<T>> partitionImpl(Iterator<T> iterator, final int size, final boolean pad)
/*      */   {
/*  597 */     Preconditions.checkNotNull(iterator);
/*  598 */     Preconditions.checkArgument(size > 0);
/*  599 */     new UnmodifiableIterator()
/*      */     {
/*      */       public boolean hasNext() {
/*  602 */         return this.val$iterator.hasNext();
/*      */       }
/*      */       
/*      */       public List<T> next()
/*      */       {
/*  607 */         if (!hasNext()) {
/*  608 */           throw new NoSuchElementException();
/*      */         }
/*  610 */         Object[] array = new Object[size];
/*  611 */         for (int count = 0; 
/*  612 */             (count < size) && (this.val$iterator.hasNext()); count++) {
/*  613 */           array[count] = this.val$iterator.next();
/*      */         }
/*  615 */         for (int i = count; i < size; i++) {
/*  616 */           array[i] = null;
/*      */         }
/*      */         
/*      */ 
/*  620 */         List<T> list = Collections.unmodifiableList(Arrays.asList(array));
/*  621 */         return (pad) || (count == size) ? list : list.subList(0, count);
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> UnmodifiableIterator<T> filter(Iterator<T> unfiltered, final Predicate<? super T> retainIfTrue)
/*      */   {
/*  632 */     Preconditions.checkNotNull(unfiltered);
/*  633 */     Preconditions.checkNotNull(retainIfTrue);
/*  634 */     new AbstractIterator()
/*      */     {
/*      */       protected T computeNext() {
/*  637 */         while (this.val$unfiltered.hasNext()) {
/*  638 */           T element = this.val$unfiltered.next();
/*  639 */           if (retainIfTrue.apply(element)) {
/*  640 */             return element;
/*      */           }
/*      */         }
/*  643 */         return (T)endOfData();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static <T> UnmodifiableIterator<T> filter(Iterator<?> unfiltered, Class<T> desiredType)
/*      */   {
/*  655 */     return filter(unfiltered, Predicates.instanceOf(desiredType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> boolean any(Iterator<T> iterator, Predicate<? super T> predicate)
/*      */   {
/*  663 */     return indexOf(iterator, predicate) != -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> boolean all(Iterator<T> iterator, Predicate<? super T> predicate)
/*      */   {
/*  671 */     Preconditions.checkNotNull(predicate);
/*  672 */     while (iterator.hasNext()) {
/*  673 */       T element = iterator.next();
/*  674 */       if (!predicate.apply(element)) {
/*  675 */         return false;
/*      */       }
/*      */     }
/*  678 */     return true;
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
/*      */   public static <T> T find(Iterator<T> iterator, Predicate<? super T> predicate)
/*      */   {
/*  691 */     Preconditions.checkNotNull(iterator);
/*  692 */     Preconditions.checkNotNull(predicate);
/*  693 */     while (iterator.hasNext()) {
/*  694 */       T t = iterator.next();
/*  695 */       if (predicate.apply(t)) {
/*  696 */         return t;
/*      */       }
/*      */     }
/*  699 */     throw new NoSuchElementException();
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
/*      */   public static <T> T find(Iterator<? extends T> iterator, Predicate<? super T> predicate, T defaultValue)
/*      */   {
/*  712 */     Preconditions.checkNotNull(iterator);
/*  713 */     Preconditions.checkNotNull(predicate);
/*  714 */     while (iterator.hasNext()) {
/*  715 */       T t = iterator.next();
/*  716 */       if (predicate.apply(t)) {
/*  717 */         return t;
/*      */       }
/*      */     }
/*  720 */     return defaultValue;
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
/*      */   public static <T> Optional<T> tryFind(Iterator<T> iterator, Predicate<? super T> predicate)
/*      */   {
/*  735 */     Preconditions.checkNotNull(iterator);
/*  736 */     Preconditions.checkNotNull(predicate);
/*  737 */     while (iterator.hasNext()) {
/*  738 */       T t = iterator.next();
/*  739 */       if (predicate.apply(t)) {
/*  740 */         return Optional.of(t);
/*      */       }
/*      */     }
/*  743 */     return Optional.absent();
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
/*      */   public static <T> int indexOf(Iterator<T> iterator, Predicate<? super T> predicate)
/*      */   {
/*  761 */     Preconditions.checkNotNull(predicate, "predicate");
/*  762 */     for (int i = 0; iterator.hasNext(); i++) {
/*  763 */       T current = iterator.next();
/*  764 */       if (predicate.apply(current)) {
/*  765 */         return i;
/*      */       }
/*      */     }
/*  768 */     return -1;
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
/*      */   public static <F, T> Iterator<T> transform(Iterator<F> fromIterator, final Function<? super F, ? extends T> function)
/*      */   {
/*  781 */     Preconditions.checkNotNull(function);
/*  782 */     new TransformedIterator(fromIterator)
/*      */     {
/*      */       T transform(F from) {
/*  785 */         return (T)function.apply(from);
/*      */       }
/*      */     };
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
/*      */   public static <T> T get(Iterator<T> iterator, int position)
/*      */   {
/*  800 */     checkNonnegative(position);
/*  801 */     int skipped = advance(iterator, position);
/*  802 */     if (!iterator.hasNext()) {
/*  803 */       throw new IndexOutOfBoundsException("position (" + position + ") must be less than the number of elements that remained (" + skipped + ")");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  810 */     return (T)iterator.next();
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
/*      */   public static <T> T get(Iterator<? extends T> iterator, int position, T defaultValue)
/*      */   {
/*  827 */     checkNonnegative(position);
/*  828 */     advance(iterator, position);
/*  829 */     return (T)getNext(iterator, defaultValue);
/*      */   }
/*      */   
/*      */   static void checkNonnegative(int position) {
/*  833 */     if (position < 0) {
/*  834 */       throw new IndexOutOfBoundsException("position (" + position + ") must not be negative");
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
/*      */   public static <T> T getNext(Iterator<? extends T> iterator, T defaultValue)
/*      */   {
/*  847 */     return (T)(iterator.hasNext() ? iterator.next() : defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> T getLast(Iterator<T> iterator)
/*      */   {
/*      */     for (;;)
/*      */     {
/*  858 */       T current = iterator.next();
/*  859 */       if (!iterator.hasNext()) {
/*  860 */         return current;
/*      */       }
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
/*      */   public static <T> T getLast(Iterator<? extends T> iterator, T defaultValue)
/*      */   {
/*  874 */     return (T)(iterator.hasNext() ? getLast(iterator) : defaultValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @CanIgnoreReturnValue
/*      */   public static int advance(Iterator<?> iterator, int numberToAdvance)
/*      */   {
/*  886 */     Preconditions.checkNotNull(iterator);
/*  887 */     Preconditions.checkArgument(numberToAdvance >= 0, "numberToAdvance must be nonnegative");
/*      */     
/*      */ 
/*  890 */     for (int i = 0; (i < numberToAdvance) && (iterator.hasNext()); i++) {
/*  891 */       iterator.next();
/*      */     }
/*  893 */     return i;
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
/*      */   public static <T> Iterator<T> limit(final Iterator<T> iterator, int limitSize)
/*      */   {
/*  907 */     Preconditions.checkNotNull(iterator);
/*  908 */     Preconditions.checkArgument(limitSize >= 0, "limit is negative");
/*  909 */     new Iterator()
/*      */     {
/*      */       private int count;
/*      */       
/*      */       public boolean hasNext() {
/*  914 */         return (this.count < this.val$limitSize) && (iterator.hasNext());
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/*  919 */         if (!hasNext()) {
/*  920 */           throw new NoSuchElementException();
/*      */         }
/*  922 */         this.count += 1;
/*  923 */         return (T)iterator.next();
/*      */       }
/*      */       
/*      */       public void remove()
/*      */       {
/*  928 */         iterator.remove();
/*      */       }
/*      */     };
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
/*      */   public static <T> Iterator<T> consumingIterator(Iterator<T> iterator)
/*      */   {
/*  945 */     Preconditions.checkNotNull(iterator);
/*  946 */     new UnmodifiableIterator()
/*      */     {
/*      */       public boolean hasNext() {
/*  949 */         return this.val$iterator.hasNext();
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/*  954 */         T next = this.val$iterator.next();
/*  955 */         this.val$iterator.remove();
/*  956 */         return next;
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/*  961 */         return "Iterators.consumingIterator(...)";
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static <T> T pollNext(Iterator<T> iterator)
/*      */   {
/*  971 */     if (iterator.hasNext()) {
/*  972 */       T result = iterator.next();
/*  973 */       iterator.remove();
/*  974 */       return result;
/*      */     }
/*  976 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static void clear(Iterator<?> iterator)
/*      */   {
/*  984 */     Preconditions.checkNotNull(iterator);
/*  985 */     while (iterator.hasNext()) {
/*  986 */       iterator.next();
/*  987 */       iterator.remove();
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
/*      */   @SafeVarargs
/*      */   public static <T> UnmodifiableIterator<T> forArray(T... array)
/*      */   {
/* 1003 */     return forArray(array, 0, array.length, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static <T> UnmodifiableListIterator<T> forArray(T[] array, int offset, int length, int index)
/*      */   {
/* 1015 */     Preconditions.checkArgument(length >= 0);
/* 1016 */     int end = offset + length;
/*      */     
/*      */ 
/* 1019 */     Preconditions.checkPositionIndexes(offset, end, array.length);
/* 1020 */     Preconditions.checkPositionIndex(index, length);
/* 1021 */     if (length == 0) {
/* 1022 */       return emptyListIterator();
/*      */     }
/* 1024 */     return new ArrayItr(array, offset, length, index);
/*      */   }
/*      */   
/*      */   private static final class ArrayItr<T> extends AbstractIndexedListIterator<T> {
/* 1028 */     static final UnmodifiableListIterator<Object> EMPTY = new ArrayItr(new Object[0], 0, 0, 0);
/*      */     private final T[] array;
/*      */     private final int offset;
/*      */     
/*      */     ArrayItr(T[] array, int offset, int length, int index)
/*      */     {
/* 1034 */       super(index);
/* 1035 */       this.array = array;
/* 1036 */       this.offset = offset;
/*      */     }
/*      */     
/*      */     protected T get(int index)
/*      */     {
/* 1041 */       return (T)this.array[(this.offset + index)];
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> UnmodifiableIterator<T> singletonIterator(T value)
/*      */   {
/* 1051 */     new UnmodifiableIterator()
/*      */     {
/*      */       boolean done;
/*      */       
/*      */       public boolean hasNext() {
/* 1056 */         return !this.done;
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/* 1061 */         if (this.done) {
/* 1062 */           throw new NoSuchElementException();
/*      */         }
/* 1064 */         this.done = true;
/* 1065 */         return (T)this.val$value;
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> UnmodifiableIterator<T> forEnumeration(Enumeration<T> enumeration)
/*      */   {
/* 1078 */     Preconditions.checkNotNull(enumeration);
/* 1079 */     new UnmodifiableIterator()
/*      */     {
/*      */       public boolean hasNext() {
/* 1082 */         return this.val$enumeration.hasMoreElements();
/*      */       }
/*      */       
/*      */       public T next()
/*      */       {
/* 1087 */         return (T)this.val$enumeration.nextElement();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Enumeration<T> asEnumeration(Iterator<T> iterator)
/*      */   {
/* 1099 */     Preconditions.checkNotNull(iterator);
/* 1100 */     new Enumeration()
/*      */     {
/*      */       public boolean hasMoreElements() {
/* 1103 */         return this.val$iterator.hasNext();
/*      */       }
/*      */       
/*      */       public T nextElement()
/*      */       {
/* 1108 */         return (T)this.val$iterator.next();
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */   private static class PeekingImpl<E> implements PeekingIterator<E>
/*      */   {
/*      */     private final Iterator<? extends E> iterator;
/*      */     private boolean hasPeeked;
/*      */     private E peekedElement;
/*      */     
/*      */     public PeekingImpl(Iterator<? extends E> iterator)
/*      */     {
/* 1121 */       this.iterator = ((Iterator)Preconditions.checkNotNull(iterator));
/*      */     }
/*      */     
/*      */     public boolean hasNext()
/*      */     {
/* 1126 */       return (this.hasPeeked) || (this.iterator.hasNext());
/*      */     }
/*      */     
/*      */     public E next()
/*      */     {
/* 1131 */       if (!this.hasPeeked) {
/* 1132 */         return (E)this.iterator.next();
/*      */       }
/* 1134 */       E result = this.peekedElement;
/* 1135 */       this.hasPeeked = false;
/* 1136 */       this.peekedElement = null;
/* 1137 */       return result;
/*      */     }
/*      */     
/*      */     public void remove()
/*      */     {
/* 1142 */       Preconditions.checkState(!this.hasPeeked, "Can't remove after you've peeked at next");
/* 1143 */       this.iterator.remove();
/*      */     }
/*      */     
/*      */     public E peek()
/*      */     {
/* 1148 */       if (!this.hasPeeked) {
/* 1149 */         this.peekedElement = this.iterator.next();
/* 1150 */         this.hasPeeked = true;
/*      */       }
/* 1152 */       return (E)this.peekedElement;
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
/*      */   public static <T> PeekingIterator<T> peekingIterator(Iterator<? extends T> iterator)
/*      */   {
/* 1193 */     if ((iterator instanceof PeekingImpl))
/*      */     {
/*      */ 
/*      */ 
/* 1197 */       PeekingImpl<T> peeking = (PeekingImpl)iterator;
/* 1198 */       return peeking;
/*      */     }
/* 1200 */     return new PeekingImpl(iterator);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static <T> PeekingIterator<T> peekingIterator(PeekingIterator<T> iterator)
/*      */   {
/* 1211 */     return (PeekingIterator)Preconditions.checkNotNull(iterator);
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
/*      */   @Beta
/*      */   public static <T> UnmodifiableIterator<T> mergeSorted(Iterable<? extends Iterator<? extends T>> iterators, Comparator<? super T> comparator)
/*      */   {
/* 1229 */     Preconditions.checkNotNull(iterators, "iterators");
/* 1230 */     Preconditions.checkNotNull(comparator, "comparator");
/*      */     
/* 1232 */     return new MergingIterator(iterators, comparator);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class MergingIterator<T>
/*      */     extends UnmodifiableIterator<T>
/*      */   {
/*      */     final Queue<PeekingIterator<T>> queue;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public MergingIterator(Iterable<? extends Iterator<? extends T>> iterators, final Comparator<? super T> itemComparator)
/*      */     {
/* 1252 */       Comparator<PeekingIterator<T>> heapComparator = new Comparator()
/*      */       {
/*      */         public int compare(PeekingIterator<T> o1, PeekingIterator<T> o2)
/*      */         {
/* 1256 */           return itemComparator.compare(o1.peek(), o2.peek());
/*      */         }
/*      */         
/* 1259 */       };
/* 1260 */       this.queue = new PriorityQueue(2, heapComparator);
/*      */       
/* 1262 */       for (Iterator<? extends T> iterator : iterators) {
/* 1263 */         if (iterator.hasNext()) {
/* 1264 */           this.queue.add(Iterators.peekingIterator(iterator));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public boolean hasNext()
/*      */     {
/* 1271 */       return !this.queue.isEmpty();
/*      */     }
/*      */     
/*      */     public T next()
/*      */     {
/* 1276 */       PeekingIterator<T> nextIter = (PeekingIterator)this.queue.remove();
/* 1277 */       T next = nextIter.next();
/* 1278 */       if (nextIter.hasNext()) {
/* 1279 */         this.queue.add(nextIter);
/*      */       }
/* 1281 */       return next;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class ConcatenatedIterator<T>
/*      */     implements Iterator<T>
/*      */   {
/*      */     private Iterator<? extends T> toRemove;
/*      */     
/*      */ 
/*      */     private Iterator<? extends T> iterator;
/*      */     
/*      */ 
/*      */     private Iterator<? extends Iterator<? extends T>> topMetaIterator;
/*      */     
/*      */ 
/*      */     private Deque<Iterator<? extends Iterator<? extends T>>> metaIterators;
/*      */     
/*      */ 
/*      */ 
/*      */     ConcatenatedIterator(Iterator<? extends Iterator<? extends T>> metaIterator)
/*      */     {
/* 1305 */       this.iterator = Iterators.emptyIterator();
/* 1306 */       this.topMetaIterator = ((Iterator)Preconditions.checkNotNull(metaIterator));
/*      */     }
/*      */     
/*      */     private Iterator<? extends Iterator<? extends T>> getTopMetaIterator()
/*      */     {
/* 1311 */       while ((this.topMetaIterator == null) || (!this.topMetaIterator.hasNext())) {
/* 1312 */         if ((this.metaIterators != null) && (!this.metaIterators.isEmpty())) {
/* 1313 */           this.topMetaIterator = ((Iterator)this.metaIterators.removeFirst());
/*      */         } else {
/* 1315 */           return null;
/*      */         }
/*      */       }
/* 1318 */       return this.topMetaIterator;
/*      */     }
/*      */     
/*      */     public boolean hasNext()
/*      */     {
/* 1323 */       while (!((Iterator)Preconditions.checkNotNull(this.iterator)).hasNext())
/*      */       {
/*      */ 
/*      */ 
/* 1327 */         this.topMetaIterator = getTopMetaIterator();
/* 1328 */         if (this.topMetaIterator == null) {
/* 1329 */           return false;
/*      */         }
/*      */         
/* 1332 */         this.iterator = ((Iterator)this.topMetaIterator.next());
/*      */         
/* 1334 */         if ((this.iterator instanceof ConcatenatedIterator))
/*      */         {
/*      */ 
/*      */ 
/* 1338 */           ConcatenatedIterator<T> topConcat = (ConcatenatedIterator)this.iterator;
/* 1339 */           this.iterator = topConcat.iterator;
/*      */           
/*      */ 
/*      */ 
/*      */ 
/* 1344 */           if (this.metaIterators == null) {
/* 1345 */             this.metaIterators = new ArrayDeque();
/*      */           }
/* 1347 */           this.metaIterators.addFirst(this.topMetaIterator);
/* 1348 */           if (topConcat.metaIterators != null) {
/* 1349 */             while (!topConcat.metaIterators.isEmpty()) {
/* 1350 */               this.metaIterators.addFirst(topConcat.metaIterators.removeLast());
/*      */             }
/*      */           }
/* 1353 */           this.topMetaIterator = topConcat.topMetaIterator;
/*      */         }
/*      */       }
/* 1356 */       return true;
/*      */     }
/*      */     
/*      */     public T next()
/*      */     {
/* 1361 */       if (hasNext()) {
/* 1362 */         this.toRemove = this.iterator;
/* 1363 */         return (T)this.iterator.next();
/*      */       }
/* 1365 */       throw new NoSuchElementException();
/*      */     }
/*      */     
/*      */ 
/*      */     public void remove()
/*      */     {
/* 1371 */       CollectPreconditions.checkRemove(this.toRemove != null);
/* 1372 */       this.toRemove.remove();
/* 1373 */       this.toRemove = null;
/*      */     }
/*      */   }
/*      */   
/*      */   static <T> ListIterator<T> cast(Iterator<T> iterator)
/*      */   {
/* 1379 */     return (ListIterator)iterator;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\Iterators.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */