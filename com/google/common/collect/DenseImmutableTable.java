/*     */ package com.google.common.collect;
/*     */ 
/*     */ import com.google.common.annotations.GwtCompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ @Immutable(containerOf={"R", "C", "V"})
/*     */ @GwtCompatible
/*     */ final class DenseImmutableTable<R, C, V>
/*     */   extends RegularImmutableTable<R, C, V>
/*     */ {
/*     */   private final ImmutableMap<R, Integer> rowKeyToIndex;
/*     */   private final ImmutableMap<C, Integer> columnKeyToIndex;
/*     */   private final ImmutableMap<R, ImmutableMap<C, V>> rowMap;
/*     */   private final ImmutableMap<C, ImmutableMap<R, V>> columnMap;
/*     */   private final int[] rowCounts;
/*     */   private final int[] columnCounts;
/*     */   private final V[][] values;
/*     */   private final int[] cellRowIndices;
/*     */   private final int[] cellColumnIndices;
/*     */   
/*     */   DenseImmutableTable(ImmutableList<Table.Cell<R, C, V>> cellList, ImmutableSet<R> rowSpace, ImmutableSet<C> columnSpace)
/*     */   {
/*  57 */     V[][] array = (Object[][])new Object[rowSpace.size()][columnSpace.size()];
/*  58 */     this.values = array;
/*  59 */     this.rowKeyToIndex = Maps.indexMap(rowSpace);
/*  60 */     this.columnKeyToIndex = Maps.indexMap(columnSpace);
/*  61 */     this.rowCounts = new int[this.rowKeyToIndex.size()];
/*  62 */     this.columnCounts = new int[this.columnKeyToIndex.size()];
/*  63 */     int[] cellRowIndices = new int[cellList.size()];
/*  64 */     int[] cellColumnIndices = new int[cellList.size()];
/*  65 */     for (int i = 0; i < cellList.size(); i++) {
/*  66 */       Table.Cell<R, C, V> cell = (Table.Cell)cellList.get(i);
/*  67 */       R rowKey = cell.getRowKey();
/*  68 */       C columnKey = cell.getColumnKey();
/*  69 */       int rowIndex = ((Integer)this.rowKeyToIndex.get(rowKey)).intValue();
/*  70 */       int columnIndex = ((Integer)this.columnKeyToIndex.get(columnKey)).intValue();
/*  71 */       V existingValue = this.values[rowIndex][columnIndex];
/*  72 */       Preconditions.checkArgument(existingValue == null, "duplicate key: (%s, %s)", rowKey, columnKey);
/*  73 */       this.values[rowIndex][columnIndex] = cell.getValue();
/*  74 */       this.rowCounts[rowIndex] += 1;
/*  75 */       this.columnCounts[columnIndex] += 1;
/*  76 */       cellRowIndices[i] = rowIndex;
/*  77 */       cellColumnIndices[i] = columnIndex;
/*     */     }
/*  79 */     this.cellRowIndices = cellRowIndices;
/*  80 */     this.cellColumnIndices = cellColumnIndices;
/*  81 */     this.rowMap = new RowMap(null);
/*  82 */     this.columnMap = new ColumnMap(null);
/*     */   }
/*     */   
/*     */   private static abstract class ImmutableArrayMap<K, V> extends ImmutableMap.IteratorBasedImmutableMap<K, V>
/*     */   {
/*     */     private final int size;
/*     */     
/*     */     ImmutableArrayMap(int size) {
/*  90 */       this.size = size;
/*     */     }
/*     */     
/*     */     abstract ImmutableMap<K, Integer> keyToIndex();
/*     */     
/*     */     private boolean isFull()
/*     */     {
/*  97 */       return this.size == keyToIndex().size();
/*     */     }
/*     */     
/*     */     K getKey(int index) {
/* 101 */       return (K)keyToIndex().keySet().asList().get(index);
/*     */     }
/*     */     
/*     */     abstract V getValue(int paramInt);
/*     */     
/*     */     ImmutableSet<K> createKeySet()
/*     */     {
/* 108 */       return isFull() ? keyToIndex().keySet() : super.createKeySet();
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 113 */       return this.size;
/*     */     }
/*     */     
/*     */     public V get(Object key)
/*     */     {
/* 118 */       Integer keyIndex = (Integer)keyToIndex().get(key);
/* 119 */       return keyIndex == null ? null : getValue(keyIndex.intValue());
/*     */     }
/*     */     
/*     */     UnmodifiableIterator<Map.Entry<K, V>> entryIterator()
/*     */     {
/* 124 */       new AbstractIterator() {
/* 125 */         private int index = -1;
/* 126 */         private final int maxIndex = DenseImmutableTable.ImmutableArrayMap.this.keyToIndex().size();
/*     */         
/*     */         protected Map.Entry<K, V> computeNext()
/*     */         {
/* 130 */           for (this.index += 1; this.index < this.maxIndex; this.index += 1) {
/* 131 */             V value = DenseImmutableTable.ImmutableArrayMap.this.getValue(this.index);
/* 132 */             if (value != null) {
/* 133 */               return Maps.immutableEntry(DenseImmutableTable.ImmutableArrayMap.this.getKey(this.index), value);
/*     */             }
/*     */           }
/* 136 */           return (Map.Entry)endOfData();
/*     */         }
/*     */       };
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Row extends DenseImmutableTable.ImmutableArrayMap<C, V> {
/*     */     private final int rowIndex;
/*     */     
/*     */     Row(int rowIndex) {
/* 146 */       super();
/* 147 */       this.rowIndex = rowIndex;
/*     */     }
/*     */     
/*     */     ImmutableMap<C, Integer> keyToIndex()
/*     */     {
/* 152 */       return DenseImmutableTable.this.columnKeyToIndex;
/*     */     }
/*     */     
/*     */     V getValue(int keyIndex)
/*     */     {
/* 157 */       return (V)DenseImmutableTable.this.values[this.rowIndex][keyIndex];
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 162 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class Column extends DenseImmutableTable.ImmutableArrayMap<R, V> {
/*     */     private final int columnIndex;
/*     */     
/*     */     Column(int columnIndex) {
/* 170 */       super();
/* 171 */       this.columnIndex = columnIndex;
/*     */     }
/*     */     
/*     */     ImmutableMap<R, Integer> keyToIndex()
/*     */     {
/* 176 */       return DenseImmutableTable.this.rowKeyToIndex;
/*     */     }
/*     */     
/*     */     V getValue(int keyIndex)
/*     */     {
/* 181 */       return (V)DenseImmutableTable.this.values[keyIndex][this.columnIndex];
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 186 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class RowMap extends DenseImmutableTable.ImmutableArrayMap<R, ImmutableMap<C, V>>
/*     */   {
/*     */     private RowMap() {
/* 193 */       super();
/*     */     }
/*     */     
/*     */     ImmutableMap<R, Integer> keyToIndex()
/*     */     {
/* 198 */       return DenseImmutableTable.this.rowKeyToIndex;
/*     */     }
/*     */     
/*     */     ImmutableMap<C, V> getValue(int keyIndex)
/*     */     {
/* 203 */       return new DenseImmutableTable.Row(DenseImmutableTable.this, keyIndex);
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 208 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   private final class ColumnMap extends DenseImmutableTable.ImmutableArrayMap<C, ImmutableMap<R, V>>
/*     */   {
/*     */     private ColumnMap() {
/* 215 */       super();
/*     */     }
/*     */     
/*     */     ImmutableMap<C, Integer> keyToIndex()
/*     */     {
/* 220 */       return DenseImmutableTable.this.columnKeyToIndex;
/*     */     }
/*     */     
/*     */     ImmutableMap<R, V> getValue(int keyIndex)
/*     */     {
/* 225 */       return new DenseImmutableTable.Column(DenseImmutableTable.this, keyIndex);
/*     */     }
/*     */     
/*     */     boolean isPartialView()
/*     */     {
/* 230 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ImmutableMap<C, Map<R, V>> columnMap()
/*     */   {
/* 237 */     ImmutableMap<C, ImmutableMap<R, V>> columnMap = this.columnMap;
/* 238 */     return ImmutableMap.copyOf(columnMap);
/*     */   }
/*     */   
/*     */ 
/*     */   public ImmutableMap<R, Map<C, V>> rowMap()
/*     */   {
/* 244 */     ImmutableMap<R, ImmutableMap<C, V>> rowMap = this.rowMap;
/* 245 */     return ImmutableMap.copyOf(rowMap);
/*     */   }
/*     */   
/*     */   public V get(Object rowKey, Object columnKey)
/*     */   {
/* 250 */     Integer rowIndex = (Integer)this.rowKeyToIndex.get(rowKey);
/* 251 */     Integer columnIndex = (Integer)this.columnKeyToIndex.get(columnKey);
/* 252 */     return (rowIndex == null) || (columnIndex == null) ? null : this.values[rowIndex.intValue()][columnIndex.intValue()];
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 257 */     return this.cellRowIndices.length;
/*     */   }
/*     */   
/*     */   Table.Cell<R, C, V> getCell(int index)
/*     */   {
/* 262 */     int rowIndex = this.cellRowIndices[index];
/* 263 */     int columnIndex = this.cellColumnIndices[index];
/* 264 */     R rowKey = rowKeySet().asList().get(rowIndex);
/* 265 */     C columnKey = columnKeySet().asList().get(columnIndex);
/* 266 */     V value = this.values[rowIndex][columnIndex];
/* 267 */     return cellOf(rowKey, columnKey, value);
/*     */   }
/*     */   
/*     */   V getValue(int index)
/*     */   {
/* 272 */     return (V)this.values[this.cellRowIndices[index]][this.cellColumnIndices[index]];
/*     */   }
/*     */   
/*     */   ImmutableTable.SerializedForm createSerializedForm()
/*     */   {
/* 277 */     return ImmutableTable.SerializedForm.create(this, this.cellRowIndices, this.cellColumnIndices);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\collect\DenseImmutableTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */