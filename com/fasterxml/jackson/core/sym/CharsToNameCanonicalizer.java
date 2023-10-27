/*     */ package com.fasterxml.jackson.core.sym;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonFactory.Feature;
/*     */ import com.fasterxml.jackson.core.util.InternCache;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharsToNameCanonicalizer
/*     */ {
/*     */   public static final int HASH_MULT = 33;
/*     */   protected static final int DEFAULT_T_SIZE = 64;
/*     */   protected static final int MAX_T_SIZE = 65536;
/*     */   static final int MAX_ENTRIES_FOR_REUSE = 12000;
/*     */   static final int MAX_COLL_CHAIN_LENGTH = 100;
/*  93 */   static final CharsToNameCanonicalizer sBootstrapSymbolTable = new CharsToNameCanonicalizer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CharsToNameCanonicalizer _parent;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int _hashSeed;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int _flags;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _canonicalize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] _symbols;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Bucket[] _buckets;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _size;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _sizeThreshold;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _indexMask;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _longestCollisionList;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _dirty;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BitSet _overflows;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static CharsToNameCanonicalizer createRoot()
/*     */   {
/* 223 */     long now = System.currentTimeMillis();
/*     */     
/* 225 */     int seed = (int)now + (int)(now >>> 32) | 0x1;
/* 226 */     return createRoot(seed);
/*     */   }
/*     */   
/*     */   protected static CharsToNameCanonicalizer createRoot(int hashSeed) {
/* 230 */     return sBootstrapSymbolTable.makeOrphan(hashSeed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private CharsToNameCanonicalizer()
/*     */   {
/* 238 */     this._canonicalize = true;
/* 239 */     this._flags = -1;
/*     */     
/* 241 */     this._dirty = true;
/* 242 */     this._hashSeed = 0;
/* 243 */     this._longestCollisionList = 0;
/* 244 */     initTables(64);
/*     */   }
/*     */   
/*     */   private void initTables(int initialSize)
/*     */   {
/* 249 */     this._symbols = new String[initialSize];
/* 250 */     this._buckets = new Bucket[initialSize >> 1];
/*     */     
/* 252 */     this._indexMask = (initialSize - 1);
/* 253 */     this._size = 0;
/* 254 */     this._longestCollisionList = 0;
/*     */     
/* 256 */     this._sizeThreshold = _thresholdSize(initialSize);
/*     */   }
/*     */   
/* 259 */   private static int _thresholdSize(int hashAreaSize) { return hashAreaSize - (hashAreaSize >> 2); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private CharsToNameCanonicalizer(CharsToNameCanonicalizer parent, int flags, String[] symbols, Bucket[] buckets, int size, int hashSeed, int longestColl)
/*     */   {
/* 266 */     this._parent = parent;
/*     */     
/* 268 */     this._flags = flags;
/* 269 */     this._canonicalize = JsonFactory.Feature.CANONICALIZE_FIELD_NAMES.enabledIn(flags);
/*     */     
/* 271 */     this._symbols = symbols;
/* 272 */     this._buckets = buckets;
/* 273 */     this._size = size;
/* 274 */     this._hashSeed = hashSeed;
/*     */     
/* 276 */     int arrayLen = symbols.length;
/* 277 */     this._sizeThreshold = _thresholdSize(arrayLen);
/* 278 */     this._indexMask = (arrayLen - 1);
/* 279 */     this._longestCollisionList = longestColl;
/*     */     
/*     */ 
/* 282 */     this._dirty = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CharsToNameCanonicalizer makeChild(int flags)
/*     */   {
/*     */     String[] symbols;
/*     */     
/*     */ 
/*     */ 
/*     */     Bucket[] buckets;
/*     */     
/*     */ 
/*     */ 
/*     */     int size;
/*     */     
/*     */ 
/*     */ 
/*     */     int hashSeed;
/*     */     
/*     */ 
/*     */     int longestCollisionList;
/*     */     
/*     */ 
/* 308 */     synchronized (this) {
/* 309 */       symbols = this._symbols;
/* 310 */       buckets = this._buckets;
/* 311 */       size = this._size;
/* 312 */       hashSeed = this._hashSeed;
/* 313 */       longestCollisionList = this._longestCollisionList;
/*     */     }
/* 315 */     return new CharsToNameCanonicalizer(this, flags, symbols, buckets, size, hashSeed, longestCollisionList);
/*     */   }
/*     */   
/*     */   private CharsToNameCanonicalizer makeOrphan(int seed)
/*     */   {
/* 320 */     return new CharsToNameCanonicalizer(null, -1, this._symbols, this._buckets, this._size, seed, this._longestCollisionList);
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
/*     */ 
/*     */ 
/*     */   private void mergeChild(CharsToNameCanonicalizer child)
/*     */   {
/* 337 */     if (child.size() > 12000)
/*     */     {
/*     */ 
/*     */ 
/* 341 */       synchronized (this) {
/* 342 */         initTables(256);
/*     */         
/*     */ 
/* 345 */         this._dirty = false;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 350 */       if (child.size() <= size()) {
/* 351 */         return;
/*     */       }
/*     */       
/* 354 */       synchronized (this) {
/* 355 */         this._symbols = child._symbols;
/* 356 */         this._buckets = child._buckets;
/* 357 */         this._size = child._size;
/* 358 */         this._sizeThreshold = child._sizeThreshold;
/* 359 */         this._indexMask = child._indexMask;
/* 360 */         this._longestCollisionList = child._longestCollisionList;
/*     */         
/*     */ 
/* 363 */         this._dirty = false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void release()
/*     */   {
/* 370 */     if (!maybeDirty()) return;
/* 371 */     if ((this._parent != null) && (this._canonicalize)) {
/* 372 */       this._parent.mergeChild(this);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 377 */       this._dirty = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 387 */     return this._size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 395 */   public int bucketCount() { return this._symbols.length; }
/* 396 */   public boolean maybeDirty() { return this._dirty; }
/* 397 */   public int hashSeed() { return this._hashSeed; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int collisionCount()
/*     */   {
/* 407 */     int count = 0;
/*     */     
/* 409 */     for (Bucket bucket : this._buckets) {
/* 410 */       if (bucket != null) {
/* 411 */         count += bucket.length;
/*     */       }
/*     */     }
/* 414 */     return count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int maxCollisionLength()
/*     */   {
/* 424 */     return this._longestCollisionList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String findSymbol(char[] buffer, int start, int len, int h)
/*     */   {
/* 434 */     if (len < 1) {
/* 435 */       return "";
/*     */     }
/* 437 */     if (!this._canonicalize) {
/* 438 */       return new String(buffer, start, len);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 446 */     int index = _hashToIndex(h);
/* 447 */     String sym = this._symbols[index];
/*     */     
/*     */ 
/* 450 */     if (sym != null)
/*     */     {
/* 452 */       if (sym.length() == len) {
/* 453 */         int i = 0;
/* 454 */         while (sym.charAt(i) == buffer[(start + i)])
/*     */         {
/* 456 */           i++; if (i == len) {
/* 457 */             return sym;
/*     */           }
/*     */         }
/*     */       }
/* 461 */       Bucket b = this._buckets[(index >> 1)];
/* 462 */       if (b != null) {
/* 463 */         sym = b.has(buffer, start, len);
/* 464 */         if (sym != null) {
/* 465 */           return sym;
/*     */         }
/* 467 */         sym = _findSymbol2(buffer, start, len, b.next);
/* 468 */         if (sym != null) {
/* 469 */           return sym;
/*     */         }
/*     */       }
/*     */     }
/* 473 */     return _addSymbol(buffer, start, len, h, index);
/*     */   }
/*     */   
/*     */   private String _findSymbol2(char[] buffer, int start, int len, Bucket b) {
/* 477 */     while (b != null) {
/* 478 */       String sym = b.has(buffer, start, len);
/* 479 */       if (sym != null) {
/* 480 */         return sym;
/*     */       }
/* 482 */       b = b.next;
/*     */     }
/* 484 */     return null;
/*     */   }
/*     */   
/*     */   private String _addSymbol(char[] buffer, int start, int len, int h, int index)
/*     */   {
/* 489 */     if (!this._dirty) {
/* 490 */       copyArrays();
/* 491 */       this._dirty = true;
/* 492 */     } else if (this._size >= this._sizeThreshold) {
/* 493 */       rehash();
/*     */       
/*     */ 
/*     */ 
/* 497 */       index = _hashToIndex(calcHash(buffer, start, len));
/*     */     }
/*     */     
/* 500 */     String newSymbol = new String(buffer, start, len);
/* 501 */     if (JsonFactory.Feature.INTERN_FIELD_NAMES.enabledIn(this._flags)) {
/* 502 */       newSymbol = InternCache.instance.intern(newSymbol);
/*     */     }
/* 504 */     this._size += 1;
/*     */     
/* 506 */     if (this._symbols[index] == null) {
/* 507 */       this._symbols[index] = newSymbol;
/*     */     } else {
/* 509 */       int bix = index >> 1;
/* 510 */       Bucket newB = new Bucket(newSymbol, this._buckets[bix]);
/* 511 */       int collLen = newB.length;
/* 512 */       if (collLen > 100)
/*     */       {
/*     */ 
/*     */ 
/* 516 */         _handleSpillOverflow(bix, newB);
/*     */       } else {
/* 518 */         this._buckets[bix] = newB;
/* 519 */         this._longestCollisionList = Math.max(collLen, this._longestCollisionList);
/*     */       }
/*     */     }
/*     */     
/* 523 */     return newSymbol;
/*     */   }
/*     */   
/*     */   private void _handleSpillOverflow(int bindex, Bucket newBucket)
/*     */   {
/* 528 */     if (this._overflows == null) {
/* 529 */       this._overflows = new BitSet();
/* 530 */       this._overflows.set(bindex);
/*     */     }
/* 532 */     else if (this._overflows.get(bindex))
/*     */     {
/* 534 */       if (JsonFactory.Feature.FAIL_ON_SYMBOL_HASH_OVERFLOW.enabledIn(this._flags)) {
/* 535 */         reportTooManyCollisions(100);
/*     */       }
/*     */       
/* 538 */       this._canonicalize = false;
/*     */     } else {
/* 540 */       this._overflows.set(bindex);
/*     */     }
/*     */     
/*     */ 
/* 544 */     this._symbols[(bindex + bindex)] = newBucket.symbol;
/* 545 */     this._buckets[bindex] = null;
/*     */     
/* 547 */     this._size -= newBucket.length;
/*     */     
/* 549 */     this._longestCollisionList = -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int _hashToIndex(int rawHash)
/*     */   {
/* 557 */     rawHash += (rawHash >>> 15);
/* 558 */     return rawHash & this._indexMask;
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
/*     */   public int calcHash(char[] buffer, int start, int len)
/*     */   {
/* 571 */     int hash = this._hashSeed;
/* 572 */     int i = start; for (int end = start + len; i < end; i++) {
/* 573 */       hash = hash * 33 + buffer[i];
/*     */     }
/*     */     
/* 576 */     return hash == 0 ? 1 : hash;
/*     */   }
/*     */   
/*     */   public int calcHash(String key)
/*     */   {
/* 581 */     int len = key.length();
/*     */     
/* 583 */     int hash = this._hashSeed;
/* 584 */     for (int i = 0; i < len; i++) {
/* 585 */       hash = hash * 33 + key.charAt(i);
/*     */     }
/*     */     
/* 588 */     return hash == 0 ? 1 : hash;
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
/*     */   private void copyArrays()
/*     */   {
/* 602 */     String[] oldSyms = this._symbols;
/* 603 */     this._symbols = ((String[])Arrays.copyOf(oldSyms, oldSyms.length));
/* 604 */     Bucket[] oldBuckets = this._buckets;
/* 605 */     this._buckets = ((Bucket[])Arrays.copyOf(oldBuckets, oldBuckets.length));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void rehash()
/*     */   {
/* 616 */     int size = this._symbols.length;
/* 617 */     int newSize = size + size;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 623 */     if (newSize > 65536)
/*     */     {
/*     */ 
/*     */ 
/* 627 */       this._size = 0;
/* 628 */       this._canonicalize = false;
/*     */       
/* 630 */       this._symbols = new String[64];
/* 631 */       this._buckets = new Bucket[32];
/* 632 */       this._indexMask = 63;
/* 633 */       this._dirty = true;
/* 634 */       return;
/*     */     }
/*     */     
/* 637 */     String[] oldSyms = this._symbols;
/* 638 */     Bucket[] oldBuckets = this._buckets;
/* 639 */     this._symbols = new String[newSize];
/* 640 */     this._buckets = new Bucket[newSize >> 1];
/*     */     
/* 642 */     this._indexMask = (newSize - 1);
/* 643 */     this._sizeThreshold = _thresholdSize(newSize);
/*     */     
/* 645 */     int count = 0;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 650 */     int maxColl = 0;
/* 651 */     for (int i = 0; i < size; i++) {
/* 652 */       String symbol = oldSyms[i];
/* 653 */       if (symbol != null) {
/* 654 */         count++;
/* 655 */         int index = _hashToIndex(calcHash(symbol));
/* 656 */         if (this._symbols[index] == null) {
/* 657 */           this._symbols[index] = symbol;
/*     */         } else {
/* 659 */           int bix = index >> 1;
/* 660 */           Bucket newB = new Bucket(symbol, this._buckets[bix]);
/* 661 */           this._buckets[bix] = newB;
/* 662 */           maxColl = Math.max(maxColl, newB.length);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 667 */     size >>= 1;
/* 668 */     for (int i = 0; i < size; i++) {
/* 669 */       Bucket b = oldBuckets[i];
/* 670 */       while (b != null) {
/* 671 */         count++;
/* 672 */         String symbol = b.symbol;
/* 673 */         int index = _hashToIndex(calcHash(symbol));
/* 674 */         if (this._symbols[index] == null) {
/* 675 */           this._symbols[index] = symbol;
/*     */         } else {
/* 677 */           int bix = index >> 1;
/* 678 */           Bucket newB = new Bucket(symbol, this._buckets[bix]);
/* 679 */           this._buckets[bix] = newB;
/* 680 */           maxColl = Math.max(maxColl, newB.length);
/*     */         }
/* 682 */         b = b.next;
/*     */       }
/*     */     }
/* 685 */     this._longestCollisionList = maxColl;
/* 686 */     this._overflows = null;
/*     */     
/* 688 */     if (count != this._size) {
/* 689 */       throw new Error("Internal error on SymbolTable.rehash(): had " + this._size + " entries; now have " + count + ".");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void reportTooManyCollisions(int maxLen)
/*     */   {
/* 697 */     throw new IllegalStateException("Longest collision chain in symbol table (of size " + this._size + ") now exceeds maximum, " + maxLen + " -- suspect a DoS attack based on hash collisions");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class Bucket
/*     */   {
/*     */     public final String symbol;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final Bucket next;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final int length;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Bucket(String s, Bucket n)
/*     */     {
/* 767 */       this.symbol = s;
/* 768 */       this.next = n;
/* 769 */       this.length = (n == null ? 1 : n.length + 1);
/*     */     }
/*     */     
/*     */     public String has(char[] buf, int start, int len) {
/* 773 */       if (this.symbol.length() != len) {
/* 774 */         return null;
/*     */       }
/* 776 */       int i = 0;
/*     */       do {
/* 778 */         if (this.symbol.charAt(i) != buf[(start + i)]) {
/* 779 */           return null;
/*     */         }
/* 781 */         i++; } while (i < len);
/* 782 */       return this.symbol;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\sym\CharsToNameCanonicalizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */