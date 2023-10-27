/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ @Immutable(containerOf={"R", "C", "V"})
/*     */ @GwtCompatible
/*     */ final class SparseImmutableTable<R, C, V>
/*     */   extends RegularImmutableTable<R, C, V>
/*     */ {
/*  27 */   static final ImmutableTable<Object, Object, Object> EMPTY = new SparseImmutableTable(
/*     */   
/*  29 */     ImmutableList.of(), ImmutableSet.of(), ImmutableSet.of());
/*     */   
/*     */ 
/*     */   private final ImmutableMap<R, ImmutableMap<C, V>> rowMap;
/*     */   
/*     */ 
/*     */   private final ImmutableMap<C, ImmutableMap<R, V>> columnMap;
/*     */   
/*     */ 
/*     */   private final int[] cellRowIndices;
/*     */   
/*     */ 
/*     */   private final int[] cellColumnInRowIndices;
/*     */   
/*     */ 
/*     */ 
/*     */   SparseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace)
/*     */   {
/*  47 */     Map<R, Integer> rowIndex = Maps.indexMap(rowSpace);
/*  48 */     Map<R, Map<C, V>> rows = Maps.newLinkedHashMap();
/*  49 */     for (UnmodifiableIterator localUnmodifiableIterator = rowSpace.iterator(); localUnmodifiableIterator.hasNext();) { row = localUnmodifiableIterator.next();
/*  50 */       rows.put(row, new LinkedHashMap());
/*     */     }
/*  52 */     Object columns = Maps.newLinkedHashMap();
/*  53 */     for (R row = columnSpace.iterator(); row.hasNext();) { C col = row.next();
/*  54 */       ((Map)columns).put(col, new LinkedHashMap());
/*     */     }
/*  56 */     int[] cellRowIndices = new int[cellList.size()];
/*  57 */     int[] cellColumnInRowIndices = new int[cellList.size()];
/*  58 */     for (int i = 0; i < cellList.size(); i++) {
/*  59 */       cell = (Table.Cell)cellList.get(i);
/*  60 */       R rowKey = cell.getRowKey();
/*  61 */       C columnKey = cell.getColumnKey();
/*  62 */       V value = cell.getValue();
/*     */       
/*  64 */       cellRowIndices[i] = ((Integer)rowIndex.get(rowKey)).intValue();
/*  65 */       Map<C, V> thisRow = (Map)rows.get(rowKey);
/*  66 */       cellColumnInRowIndices[i] = thisRow.size();
/*  67 */       V oldValue = thisRow.put(columnKey, value);
/*  68 */       if (oldValue != null) {
/*  69 */         throw new IllegalArgumentException("Duplicate value for row=" + rowKey + ", column=" + columnKey + ": " + value + ", " + oldValue);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */       ((Map)((Map)columns).get(columnKey)).put(rowKey, value);
/*     */     }
/*  81 */     this.cellRowIndices = cellRowIndices;
/*  82 */     this.cellColumnInRowIndices = cellColumnInRowIndices;
/*     */     
/*  84 */     ImmutableMap.Builder<R, ImmutableMap<C, V>> rowBuilder = new ImmutableMap.Builder(rows.size());
/*  85 */     for (Table.Cell<R, C, V> cell = rows.entrySet().iterator(); cell.hasNext();) { row = (Map.Entry)cell.next();
/*  86 */       rowBuilder.put(row.getKey(), ImmutableMap.copyOf((Map)row.getValue())); }
/*     */     Map.Entry<R, Map<C, V>> row;
/*  88 */     this.rowMap = rowBuilder.build();
/*     */     
/*     */ 
/*  91 */     ImmutableMap.Builder<C, ImmutableMap<R, V>> columnBuilder = new ImmutableMap.Builder(((Map)columns).size());
/*  92 */     for (Map.Entry<C, Map<R, V>> col : ((Map)columns).entrySet()) {
/*  93 */       columnBuilder.put(col.getKey(), ImmutableMap.copyOf((Map)col.getValue()));
/*     */     }
/*  95 */     this.columnMap = columnBuilder.build();
/*     */   }
/*     */   
/*     */ 
/*     */   public ImmutableMap<C, Map<R, V>> columnMap()
/*     */   {
/* 101 */     ImmutableMap<C, ImmutableMap<R, V>> columnMap = this.columnMap;
/* 102 */     return ImmutableMap.copyOf(columnMap);
/*     */   }
/*     */   
/*     */ 
/*     */   public ImmutableMap<R, Map<C, V>> rowMap()
/*     */   {
/* 108 */     ImmutableMap<R, ImmutableMap<C, V>> rowMap = this.rowMap;
/* 109 */     return ImmutableMap.copyOf(rowMap);
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 114 */     return this.cellRowIndices.length;
/*     */   }
/*     */   
/*     */   Table.Cell<R, C, V> getCell(int index)
/*     */   {
/* 119 */     int rowIndex = this.cellRowIndices[index];
/* 120 */     Map.Entry<R, ImmutableMap<C, V>> rowEntry = (Map.Entry)this.rowMap.entrySet().asList().get(rowIndex);
/* 121 */     ImmutableMap<C, V> row = (ImmutableMap)rowEntry.getValue();
/* 122 */     int columnIndex = this.cellColumnInRowIndices[index];
/* 123 */     Map.Entry<C, V> colEntry = (Map.Entry)row.entrySet().asList().get(columnIndex);
/* 124 */     return cellOf(rowEntry.getKey(), colEntry.getKey(), colEntry.getValue());
/*     */   }
/*     */   
/*     */   V getValue(int index)
/*     */   {
/* 129 */     int rowIndex = this.cellRowIndices[index];
/* 130 */     ImmutableMap<C, V> row = (ImmutableMap)this.rowMap.values().asList().get(rowIndex);
/* 131 */     int columnIndex = this.cellColumnInRowIndices[index];
/* 132 */     return (V)row.values().asList().get(columnIndex);
/*     */   }
/*     */   
/*     */   ImmutableTable.SerializedForm createSerializedForm()
/*     */   {
/* 137 */     Map<C, Integer> columnKeyToIndex = Maps.indexMap(columnKeySet());
/* 138 */     int[] cellColumnIndices = new int[cellSet().size()];
/* 139 */     int i = 0;
/* 140 */     for (UnmodifiableIterator localUnmodifiableIterator = cellSet().iterator(); localUnmodifiableIterator.hasNext();) { Table.Cell<R, C, V> cell = (Table.Cell)localUnmodifiableIterator.next();
/* 141 */       cellColumnIndices[(i++)] = ((Integer)columnKeyToIndex.get(cell.getColumnKey())).intValue();
/*     */     }
/* 143 */     return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, cellColumnIndices);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\SparseImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */