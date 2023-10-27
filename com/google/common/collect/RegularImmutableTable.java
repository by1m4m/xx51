/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ abstract class RegularImmutableTable<R, C, V>
/*     */   extends ImmutableTable<R, C, V>
/*     */ {
/*     */   abstract Table.Cell<R, C, V> getCell(int paramInt);
/*     */   
/*  41 */   final ImmutableSet<Table.Cell<R, C, V>> createCellSet() { return isEmpty() ? ImmutableSet.of() : new CellSet(null); }
/*     */   
/*     */   abstract V getValue(int paramInt);
/*     */   
/*     */   private final class CellSet extends IndexedImmutableSet<Table.Cell<R, C, V>> {
/*     */     private CellSet() {}
/*     */     
/*  48 */     public int size() { return RegularImmutableTable.this.size(); }
/*     */     
/*     */ 
/*     */     Table.Cell<R, C, V> get(int index)
/*     */     {
/*  53 */       return RegularImmutableTable.this.getCell(index);
/*     */     }
/*     */     
/*     */     public boolean contains(Object object)
/*     */     {
/*  58 */       if ((object instanceof Table.Cell)) {
/*  59 */         Table.Cell<?, ?, ?> cell = (Table.Cell)object;
/*  60 */         Object value = RegularImmutableTable.this.get(cell.getRowKey(), cell.getColumnKey());
/*  61 */         return (value != null) && (value.equals(cell.getValue()));
/*     */       }
/*  63 */       return false;
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/*  68 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final ImmutableCollection<V> createValues()
/*     */   {
/*  76 */     return isEmpty() ? ImmutableList.of() : new Values(null);
/*     */   }
/*     */   
/*     */   private final class Values extends ImmutableList<V> {
/*     */     private Values() {}
/*     */     
/*     */     public int size() {
/*  83 */       return RegularImmutableTable.this.size();
/*     */     }
/*     */     
/*     */     public V get(int index)
/*     */     {
/*  88 */       return (V)RegularImmutableTable.this.getValue(index);
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/*  93 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(List<Table.Cell<R, C, V>> cells, Comparator<? super R> rowComparator, final Comparator<? super C> columnComparator)
/*     */   {
/* 101 */     Preconditions.checkNotNull(cells);
/* 102 */     if ((rowComparator != null) || (columnComparator != null))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */       Comparator<Table.Cell<R, C, V>> comparator = new Comparator()
/*     */       {
/*     */ 
/*     */ 
/*     */         public int compare(Table.Cell<R, C, V> cell1, Table.Cell<R, C, V> cell2)
/*     */         {
/*     */ 
/* 117 */           int rowCompare = this.val$rowComparator == null ? 0 : this.val$rowComparator.compare(cell1.getRowKey(), cell2.getRowKey());
/* 118 */           if (rowCompare != 0) {
/* 119 */             return rowCompare;
/*     */           }
/* 121 */           return columnComparator == null ? 0 : columnComparator
/*     */           
/* 123 */             .compare(cell1.getColumnKey(), cell2.getColumnKey());
/*     */         }
/* 125 */       };
/* 126 */       Collections.sort(cells, comparator);
/*     */     }
/* 128 */     return forCellsInternal(cells, rowComparator, columnComparator);
/*     */   }
/*     */   
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forCells(Iterable<Table.Cell<R, C, V>> cells) {
/* 132 */     return forCellsInternal(cells, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static <R, C, V> RegularImmutableTable<R, C, V> forCellsInternal(Iterable<Table.Cell<R, C, V>> cells, Comparator<? super R> rowComparator, Comparator<? super C> columnComparator)
/*     */   {
/* 139 */     Set<R> rowSpaceBuilder = new LinkedHashSet();
/* 140 */     Set<C> columnSpaceBuilder = new LinkedHashSet();
/* 141 */     ImmutableList<Table.Cell<R, C, V>> cellList = ImmutableList.copyOf(cells);
/* 142 */     for (Table.Cell<R, C, V> cell : cells) {
/* 143 */       rowSpaceBuilder.add(cell.getRowKey());
/* 144 */       columnSpaceBuilder.add(cell.getColumnKey());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 150 */     Object rowSpace = rowComparator == null ? ImmutableSet.copyOf(rowSpaceBuilder) : ImmutableSet.copyOf(ImmutableList.sortedCopyOf(rowComparator, rowSpaceBuilder));
/*     */     
/*     */ 
/*     */ 
/* 154 */     ImmutableSet<C> columnSpace = columnComparator == null ? ImmutableSet.copyOf(columnSpaceBuilder) : ImmutableSet.copyOf(ImmutableList.sortedCopyOf(columnComparator, columnSpaceBuilder));
/*     */     
/* 156 */     return forOrderedComponents(cellList, (ImmutableSet)rowSpace, columnSpace);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static <R, C, V> RegularImmutableTable<R, C, V> forOrderedComponents(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace)
/*     */   {
/* 166 */     return cellList.size() > rowSpace.size() * columnSpace.size() / 2L ? new DenseImmutableTable(cellList, rowSpace, columnSpace) : new SparseImmutableTable(cellList, rowSpace, columnSpace);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\RegularImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */