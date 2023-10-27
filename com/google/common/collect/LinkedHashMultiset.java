/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import com.google.common.annotations.GwtIncompatible;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.util.LinkedHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(serializable=true, emulated=true)
/*    */ public final class LinkedHashMultiset<E>
/*    */   extends AbstractMapBasedMultiset<E>
/*    */ {
/*    */   @GwtIncompatible
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   public static <E> LinkedHashMultiset<E> create()
/*    */   {
/* 47 */     return new LinkedHashMultiset();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <E> LinkedHashMultiset<E> create(int distinctElements)
/*    */   {
/* 58 */     return new LinkedHashMultiset(distinctElements);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static <E> LinkedHashMultiset<E> create(Iterable<? extends E> elements)
/*    */   {
/* 69 */     LinkedHashMultiset<E> multiset = create(Multisets.inferDistinctElements(elements));
/* 70 */     Iterables.addAll(multiset, elements);
/* 71 */     return multiset;
/*    */   }
/*    */   
/*    */   private LinkedHashMultiset() {
/* 75 */     super(new LinkedHashMap());
/*    */   }
/*    */   
/*    */   private LinkedHashMultiset(int distinctElements) {
/* 79 */     super(Maps.newLinkedHashMapWithExpectedSize(distinctElements));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @GwtIncompatible
/*    */   private void writeObject(ObjectOutputStream stream)
/*    */     throws IOException
/*    */   {
/* 88 */     stream.defaultWriteObject();
/* 89 */     Serialization.writeMultiset(this, stream);
/*    */   }
/*    */   
/*    */   @GwtIncompatible
/*    */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 94 */     stream.defaultReadObject();
/* 95 */     int distinctElements = Serialization.readCount(stream);
/* 96 */     setBackingMap(new LinkedHashMap());
/* 97 */     Serialization.populateMultiset(this, stream, distinctElements);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\LinkedHashMultiset.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */