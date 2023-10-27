/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.AbstractMap.SimpleEntry;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayTernaryTrie<V>
/*     */   extends AbstractTrie<V>
/*     */ {
/*  63 */   private static int LO = 1;
/*  64 */   private static int EQ = 2;
/*  65 */   private static int HI = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int ROW_SIZE = 4;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final char[] _tree;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String[] _key;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final V[] _value;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private char _rows;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayTernaryTrie()
/*     */   {
/* 102 */     this(128);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayTernaryTrie(boolean insensitive)
/*     */   {
/* 111 */     this(insensitive, 128);
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
/*     */   public ArrayTernaryTrie(int capacity)
/*     */   {
/* 125 */     this(true, capacity);
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
/*     */ 
/*     */   public ArrayTernaryTrie(boolean insensitive, int capacity)
/*     */   {
/* 140 */     super(insensitive);
/* 141 */     this._value = new Object[capacity];
/* 142 */     this._tree = new char[capacity * 4];
/* 143 */     this._key = new String[capacity];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayTernaryTrie(ArrayTernaryTrie<V> trie, double factor)
/*     */   {
/* 153 */     super(trie.isCaseInsensitive());
/* 154 */     int capacity = (int)(trie._value.length * factor);
/* 155 */     this._rows = trie._rows;
/* 156 */     this._value = Arrays.copyOf(trie._value, capacity);
/* 157 */     this._tree = Arrays.copyOf(trie._tree, capacity * 4);
/* 158 */     this._key = ((String[])Arrays.copyOf(trie._key, capacity));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clear()
/*     */   {
/* 165 */     this._rows = '\000';
/* 166 */     Arrays.fill(this._value, null);
/* 167 */     Arrays.fill(this._tree, '\000');
/* 168 */     Arrays.fill(this._key, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean put(String s, V v)
/*     */   {
/* 175 */     int t = 0;
/* 176 */     int limit = s.length();
/* 177 */     int last = 0;
/* 178 */     for (int k = 0; k < limit; k++)
/*     */     {
/* 180 */       char c = s.charAt(k);
/* 181 */       if ((isCaseInsensitive()) && (c < '')) {
/* 182 */         c = StringUtil.lowercases[c];
/*     */       }
/*     */       for (;;)
/*     */       {
/* 186 */         int row = 4 * t;
/*     */         
/*     */ 
/* 189 */         if (t == this._rows)
/*     */         {
/* 191 */           this._rows = ((char)(this._rows + '\001'));
/* 192 */           if (this._rows >= this._key.length)
/*     */           {
/* 194 */             this._rows = ((char)(this._rows - '\001'));
/* 195 */             return false;
/*     */           }
/* 197 */           this._tree[row] = c;
/*     */         }
/*     */         
/* 200 */         char n = this._tree[row];
/* 201 */         int diff = n - c;
/* 202 */         if (diff == 0) {
/* 203 */           t = this._tree[(last = row + EQ)];
/* 204 */         } else if (diff < 0) {
/* 205 */           t = this._tree[(last = row + LO)];
/*     */         } else {
/* 207 */           t = this._tree[(last = row + HI)];
/*     */         }
/*     */         
/* 210 */         if (t == 0)
/*     */         {
/* 212 */           t = this._rows;
/* 213 */           this._tree[last] = ((char)t);
/*     */         }
/*     */         
/* 216 */         if (diff == 0) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 222 */     if (t == this._rows)
/*     */     {
/* 224 */       this._rows = ((char)(this._rows + '\001'));
/* 225 */       if (this._rows >= this._key.length)
/*     */       {
/* 227 */         this._rows = ((char)(this._rows - '\001'));
/* 228 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 233 */     this._key[t] = (v == null ? null : s);
/* 234 */     this._value[t] = v;
/*     */     
/* 236 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public V get(String s, int offset, int len)
/*     */   {
/* 244 */     int t = 0;
/* 245 */     for (int i = 0; i < len;)
/*     */     {
/* 247 */       char c = s.charAt(offset + i++);
/* 248 */       if ((isCaseInsensitive()) && (c < '')) {
/* 249 */         c = StringUtil.lowercases[c];
/*     */       }
/*     */       for (;;)
/*     */       {
/* 253 */         int row = 4 * t;
/* 254 */         char n = this._tree[row];
/* 255 */         int diff = n - c;
/*     */         
/* 257 */         if (diff == 0)
/*     */         {
/* 259 */           t = this._tree[(row + EQ)];
/* 260 */           if (t != 0) break;
/* 261 */           return null;
/*     */         }
/*     */         
/*     */ 
/* 265 */         t = this._tree[(row + hilo(diff))];
/* 266 */         if (t == 0) {
/* 267 */           return null;
/*     */         }
/*     */       }
/*     */     }
/* 271 */     return (V)this._value[t];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public V get(ByteBuffer b, int offset, int len)
/*     */   {
/* 278 */     int t = 0;
/* 279 */     offset += b.position();
/*     */     
/* 281 */     for (int i = 0; i < len;)
/*     */     {
/* 283 */       byte c = (byte)(b.get(offset + i++) & 0x7F);
/* 284 */       if (isCaseInsensitive()) {
/* 285 */         c = (byte)StringUtil.lowercases[c];
/*     */       }
/*     */       for (;;)
/*     */       {
/* 289 */         int row = 4 * t;
/* 290 */         char n = this._tree[row];
/* 291 */         int diff = n - c;
/*     */         
/* 293 */         if (diff == 0)
/*     */         {
/* 295 */           t = this._tree[(row + EQ)];
/* 296 */           if (t != 0) break;
/* 297 */           return null;
/*     */         }
/*     */         
/*     */ 
/* 301 */         t = this._tree[(row + hilo(diff))];
/* 302 */         if (t == 0) {
/* 303 */           return null;
/*     */         }
/*     */       }
/*     */     }
/* 307 */     return (V)this._value[t];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public V getBest(String s)
/*     */   {
/* 314 */     return (V)getBest(0, s, 0, s.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public V getBest(String s, int offset, int length)
/*     */   {
/* 321 */     return (V)getBest(0, s, offset, length);
/*     */   }
/*     */   
/*     */ 
/*     */   private V getBest(int t, String s, int offset, int len)
/*     */   {
/* 327 */     int node = t;
/* 328 */     int end = offset + len;
/* 329 */     while (offset < end)
/*     */     {
/* 331 */       char c = s.charAt(offset++);
/* 332 */       len--;
/* 333 */       if ((isCaseInsensitive()) && (c < '')) {
/* 334 */         c = StringUtil.lowercases[c];
/*     */       }
/*     */       for (;;)
/*     */       {
/* 338 */         int row = 4 * t;
/* 339 */         char n = this._tree[row];
/* 340 */         int diff = n - c;
/*     */         
/* 342 */         if (diff == 0)
/*     */         {
/* 344 */           t = this._tree[(row + EQ)];
/* 345 */           if (t == 0) {
/*     */             break label157;
/*     */           }
/*     */           
/* 349 */           if (this._key[t] == null)
/*     */             break;
/* 351 */           node = t;
/* 352 */           V better = getBest(t, s, offset, len);
/* 353 */           if (better != null)
/* 354 */             return better;
/* 355 */           break;
/*     */         }
/*     */         
/*     */ 
/* 359 */         t = this._tree[(row + hilo(diff))];
/* 360 */         if (t == 0) break label157;
/*     */       }
/*     */     }
/*     */     label157:
/* 364 */     return (V)this._value[node];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public V getBest(ByteBuffer b, int offset, int len)
/*     */   {
/* 372 */     if (b.hasArray())
/* 373 */       return (V)getBest(0, b.array(), b.arrayOffset() + b.position() + offset, len);
/* 374 */     return (V)getBest(0, b, offset, len);
/*     */   }
/*     */   
/*     */ 
/*     */   private V getBest(int t, byte[] b, int offset, int len)
/*     */   {
/* 380 */     int node = t;
/* 381 */     int end = offset + len;
/* 382 */     while (offset < end)
/*     */     {
/* 384 */       byte c = (byte)(b[(offset++)] & 0x7F);
/* 385 */       len--;
/* 386 */       if (isCaseInsensitive()) {
/* 387 */         c = (byte)StringUtil.lowercases[c];
/*     */       }
/*     */       for (;;)
/*     */       {
/* 391 */         int row = 4 * t;
/* 392 */         char n = this._tree[row];
/* 393 */         int diff = n - c;
/*     */         
/* 395 */         if (diff == 0)
/*     */         {
/* 397 */           t = this._tree[(row + EQ)];
/* 398 */           if (t == 0) {
/*     */             break label152;
/*     */           }
/*     */           
/* 402 */           if (this._key[t] == null)
/*     */             break;
/* 404 */           node = t;
/* 405 */           V better = getBest(t, b, offset, len);
/* 406 */           if (better != null)
/* 407 */             return better;
/* 408 */           break;
/*     */         }
/*     */         
/*     */ 
/* 412 */         t = this._tree[(row + hilo(diff))];
/* 413 */         if (t == 0) break label152;
/*     */       }
/*     */     }
/*     */     label152:
/* 417 */     return (V)this._value[node];
/*     */   }
/*     */   
/*     */ 
/*     */   private V getBest(int t, ByteBuffer b, int offset, int len)
/*     */   {
/* 423 */     int node = t;
/* 424 */     int o = offset + b.position();
/*     */     
/* 426 */     for (int i = 0; i < len; i++)
/*     */     {
/* 428 */       byte c = (byte)(b.get(o + i) & 0x7F);
/* 429 */       if (isCaseInsensitive()) {
/* 430 */         c = (byte)StringUtil.lowercases[c];
/*     */       }
/*     */       for (;;)
/*     */       {
/* 434 */         int row = 4 * t;
/* 435 */         char n = this._tree[row];
/* 436 */         int diff = n - c;
/*     */         
/* 438 */         if (diff == 0)
/*     */         {
/* 440 */           t = this._tree[(row + EQ)];
/* 441 */           if (t == 0) {
/*     */             break label171;
/*     */           }
/*     */           
/* 445 */           if (this._key[t] == null)
/*     */             break;
/* 447 */           node = t;
/* 448 */           V best = getBest(t, b, offset + i + 1, len - i - 1);
/* 449 */           if (best != null)
/* 450 */             return best;
/* 451 */           break;
/*     */         }
/*     */         
/*     */ 
/* 455 */         t = this._tree[(row + hilo(diff))];
/* 456 */         if (t == 0) break label171;
/*     */       }
/*     */     }
/*     */     label171:
/* 460 */     return (V)this._value[node];
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 466 */     StringBuilder buf = new StringBuilder();
/* 467 */     for (int r = 0; r <= this._rows; r++)
/*     */     {
/* 469 */       if ((this._key[r] != null) && (this._value[r] != null))
/*     */       {
/* 471 */         buf.append(',');
/* 472 */         buf.append(this._key[r]);
/* 473 */         buf.append('=');
/* 474 */         buf.append(this._value[r].toString());
/*     */       }
/*     */     }
/* 477 */     if (buf.length() == 0) {
/* 478 */       return "{}";
/*     */     }
/* 480 */     buf.setCharAt(0, '{');
/* 481 */     buf.append('}');
/* 482 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> keySet()
/*     */   {
/* 490 */     Set<String> keys = new HashSet();
/*     */     
/* 492 */     for (int r = 0; r <= this._rows; r++)
/*     */     {
/* 494 */       if ((this._key[r] != null) && (this._value[r] != null))
/* 495 */         keys.add(this._key[r]);
/*     */     }
/* 497 */     return keys;
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 502 */     int s = 0;
/* 503 */     for (int r = 0; r <= this._rows; r++)
/*     */     {
/* 505 */       if ((this._key[r] != null) && (this._value[r] != null))
/* 506 */         s++;
/*     */     }
/* 508 */     return s;
/*     */   }
/*     */   
/*     */   public boolean isEmpty()
/*     */   {
/* 513 */     for (int r = 0; r <= this._rows; r++)
/*     */     {
/* 515 */       if ((this._key[r] != null) && (this._value[r] != null))
/* 516 */         return false;
/*     */     }
/* 518 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<Map.Entry<String, V>> entrySet()
/*     */   {
/* 524 */     Set<Map.Entry<String, V>> entries = new HashSet();
/* 525 */     for (int r = 0; r <= this._rows; r++)
/*     */     {
/* 527 */       if ((this._key[r] != null) && (this._value[r] != null))
/* 528 */         entries.add(new AbstractMap.SimpleEntry(this._key[r], this._value[r]));
/*     */     }
/* 530 */     return entries;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isFull()
/*     */   {
/* 536 */     return this._rows + '\001' == this._key.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int hilo(int diff)
/*     */   {
/* 543 */     return 1 + (diff | 0x7FFFFFFF) / 1073741823;
/*     */   }
/*     */   
/*     */   public void dump()
/*     */   {
/* 548 */     for (int r = 0; r < this._rows; r++)
/*     */     {
/* 550 */       char c = this._tree[(r * 4 + 0)];
/* 551 */       System.err.printf("%4d [%s,%d,%d,%d] '%s':%s%n", new Object[] {
/* 552 */         Integer.valueOf(r), 
/* 553 */         "'" + c + "'", 
/* 554 */         Integer.valueOf(this._tree[(r * 4 + LO)]), 
/* 555 */         Integer.valueOf(this._tree[(r * 4 + EQ)]), 
/* 556 */         Integer.valueOf(this._tree[(r * 4 + HI)]), this._key[r], this._value[r] });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Growing<V>
/*     */     implements Trie<V>
/*     */   {
/*     */     private final int _growby;
/*     */     
/*     */     private ArrayTernaryTrie<V> _trie;
/*     */     
/*     */     public Growing()
/*     */     {
/* 570 */       this(1024, 1024);
/*     */     }
/*     */     
/*     */     public Growing(int capacity, int growby)
/*     */     {
/* 575 */       this._growby = growby;
/* 576 */       this._trie = new ArrayTernaryTrie(capacity);
/*     */     }
/*     */     
/*     */     public Growing(boolean insensitive, int capacity, int growby)
/*     */     {
/* 581 */       this._growby = growby;
/* 582 */       this._trie = new ArrayTernaryTrie(insensitive, capacity);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean put(V v)
/*     */     {
/* 588 */       return put(v.toString(), v);
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 594 */       return this._trie.hashCode();
/*     */     }
/*     */     
/*     */ 
/*     */     public V remove(String s)
/*     */     {
/* 600 */       return (V)this._trie.remove(s);
/*     */     }
/*     */     
/*     */ 
/*     */     public V get(String s)
/*     */     {
/* 606 */       return (V)this._trie.get(s);
/*     */     }
/*     */     
/*     */ 
/*     */     public V get(ByteBuffer b)
/*     */     {
/* 612 */       return (V)this._trie.get(b);
/*     */     }
/*     */     
/*     */ 
/*     */     public V getBest(byte[] b, int offset, int len)
/*     */     {
/* 618 */       return (V)this._trie.getBest(b, offset, len);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean isCaseInsensitive()
/*     */     {
/* 624 */       return this._trie.isCaseInsensitive();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean equals(Object obj)
/*     */     {
/* 630 */       return this._trie.equals(obj);
/*     */     }
/*     */     
/*     */ 
/*     */     public void clear()
/*     */     {
/* 636 */       this._trie.clear();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean put(String s, V v)
/*     */     {
/* 642 */       boolean added = this._trie.put(s, v);
/* 643 */       while ((!added) && (this._growby > 0))
/*     */       {
/* 645 */         ArrayTernaryTrie<V> bigger = new ArrayTernaryTrie(this._trie._key.length + this._growby);
/* 646 */         for (Map.Entry<String, V> entry : this._trie.entrySet())
/* 647 */           bigger.put((String)entry.getKey(), entry.getValue());
/* 648 */         this._trie = bigger;
/* 649 */         added = this._trie.put(s, v);
/*     */       }
/*     */       
/* 652 */       return added;
/*     */     }
/*     */     
/*     */ 
/*     */     public V get(String s, int offset, int len)
/*     */     {
/* 658 */       return (V)this._trie.get(s, offset, len);
/*     */     }
/*     */     
/*     */ 
/*     */     public V get(ByteBuffer b, int offset, int len)
/*     */     {
/* 664 */       return (V)this._trie.get(b, offset, len);
/*     */     }
/*     */     
/*     */ 
/*     */     public V getBest(String s)
/*     */     {
/* 670 */       return (V)this._trie.getBest(s);
/*     */     }
/*     */     
/*     */ 
/*     */     public V getBest(String s, int offset, int length)
/*     */     {
/* 676 */       return (V)this._trie.getBest(s, offset, length);
/*     */     }
/*     */     
/*     */ 
/*     */     public V getBest(ByteBuffer b, int offset, int len)
/*     */     {
/* 682 */       return (V)this._trie.getBest(b, offset, len);
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 688 */       return this._trie.toString();
/*     */     }
/*     */     
/*     */ 
/*     */     public Set<String> keySet()
/*     */     {
/* 694 */       return this._trie.keySet();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean isFull()
/*     */     {
/* 700 */       return false;
/*     */     }
/*     */     
/*     */     public void dump()
/*     */     {
/* 705 */       this._trie.dump();
/*     */     }
/*     */     
/*     */     public boolean isEmpty()
/*     */     {
/* 710 */       return this._trie.isEmpty();
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 715 */       return this._trie.size();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\ArrayTernaryTrie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */