/*      */ package com.fasterxml.jackson.core.sym;
/*      */ 
/*      */ import com.fasterxml.jackson.core.JsonFactory.Feature;
/*      */ import com.fasterxml.jackson.core.util.InternCache;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.concurrent.atomic.AtomicReference;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class BytesToNameCanonicalizer
/*      */ {
/*      */   private static final int DEFAULT_T_SIZE = 64;
/*      */   private static final int MAX_T_SIZE = 65536;
/*      */   private static final int MAX_ENTRIES_FOR_REUSE = 6000;
/*      */   private static final int MAX_COLL_CHAIN_LENGTH = 200;
/*      */   static final int MIN_HASH_SIZE = 16;
/*      */   static final int INITIAL_COLLISION_LEN = 32;
/*      */   static final int LAST_VALID_BUCKET = 254;
/*      */   protected final BytesToNameCanonicalizer _parent;
/*      */   protected final AtomicReference<TableInfo> _tableInfo;
/*      */   private final int _seed;
/*      */   protected boolean _intern;
/*      */   protected final boolean _failOnDoS;
/*      */   protected int _count;
/*      */   protected int _longestCollisionList;
/*      */   protected int _hashMask;
/*      */   protected int[] _hash;
/*      */   protected Name[] _mainNames;
/*      */   protected Bucket[] _collList;
/*      */   protected int _collCount;
/*      */   protected int _collEnd;
/*      */   private transient boolean _needRehash;
/*      */   private boolean _hashShared;
/*      */   private boolean _namesShared;
/*      */   private boolean _collListShared;
/*      */   protected BitSet _overflows;
/*      */   private static final int MULT = 33;
/*      */   private static final int MULT2 = 65599;
/*      */   private static final int MULT3 = 31;
/*      */   
/*      */   private BytesToNameCanonicalizer(int sz, boolean intern, int seed, boolean failOnDoS)
/*      */   {
/*  270 */     this._parent = null;
/*  271 */     this._seed = seed;
/*  272 */     this._intern = intern;
/*  273 */     this._failOnDoS = failOnDoS;
/*      */     
/*  275 */     if (sz < 16) {
/*  276 */       sz = 16;
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  281 */     else if ((sz & sz - 1) != 0) {
/*  282 */       int curr = 16;
/*  283 */       while (curr < sz) {
/*  284 */         curr += curr;
/*      */       }
/*  286 */       sz = curr;
/*      */     }
/*      */     
/*  289 */     this._tableInfo = new AtomicReference(initTableInfo(sz));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private BytesToNameCanonicalizer(BytesToNameCanonicalizer parent, boolean intern, int seed, boolean failOnDoS, TableInfo state)
/*      */   {
/*  298 */     this._parent = parent;
/*  299 */     this._seed = seed;
/*  300 */     this._intern = intern;
/*  301 */     this._failOnDoS = failOnDoS;
/*  302 */     this._tableInfo = null;
/*      */     
/*      */ 
/*  305 */     this._count = state.count;
/*  306 */     this._hashMask = state.mainHashMask;
/*  307 */     this._hash = state.mainHash;
/*  308 */     this._mainNames = state.mainNames;
/*  309 */     this._collList = state.collList;
/*  310 */     this._collCount = state.collCount;
/*  311 */     this._collEnd = state.collEnd;
/*  312 */     this._longestCollisionList = state.longestCollisionList;
/*      */     
/*      */ 
/*  315 */     this._needRehash = false;
/*  316 */     this._hashShared = true;
/*  317 */     this._namesShared = true;
/*  318 */     this._collListShared = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private TableInfo initTableInfo(int sz)
/*      */   {
/*  326 */     return new TableInfo(0, sz - 1, new int[sz], new Name[sz], null, 0, 0, 0);
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
/*      */   public static BytesToNameCanonicalizer createRoot()
/*      */   {
/*  351 */     long now = System.currentTimeMillis();
/*      */     
/*  353 */     int seed = (int)now + (int)(now >>> 32) | 0x1;
/*  354 */     return createRoot(seed);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected static BytesToNameCanonicalizer createRoot(int seed)
/*      */   {
/*  362 */     return new BytesToNameCanonicalizer(64, true, seed, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public BytesToNameCanonicalizer makeChild(int flags)
/*      */   {
/*  370 */     return new BytesToNameCanonicalizer(this, JsonFactory.Feature.INTERN_FIELD_NAMES.enabledIn(flags), this._seed, JsonFactory.Feature.FAIL_ON_SYMBOL_HASH_OVERFLOW.enabledIn(flags), (TableInfo)this._tableInfo.get());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public BytesToNameCanonicalizer makeChild(boolean canonicalize, boolean intern)
/*      */   {
/*  379 */     return new BytesToNameCanonicalizer(this, intern, this._seed, true, (TableInfo)this._tableInfo.get());
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
/*      */   public void release()
/*      */   {
/*  394 */     if ((this._parent != null) && (maybeDirty())) {
/*  395 */       this._parent.mergeChild(new TableInfo(this));
/*      */       
/*      */ 
/*      */ 
/*  399 */       this._hashShared = true;
/*  400 */       this._namesShared = true;
/*  401 */       this._collListShared = true;
/*      */     }
/*      */   }
/*      */   
/*      */   private void mergeChild(TableInfo childState)
/*      */   {
/*  407 */     int childCount = childState.count;
/*  408 */     TableInfo currState = (TableInfo)this._tableInfo.get();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  414 */     if (childCount == currState.count) {
/*  415 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  424 */     if (childCount > 6000)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  430 */       childState = initTableInfo(64);
/*      */     }
/*  432 */     this._tableInfo.compareAndSet(currState, childState);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int size()
/*      */   {
/*  443 */     if (this._tableInfo != null) {
/*  444 */       return ((TableInfo)this._tableInfo.get()).count;
/*      */     }
/*      */     
/*  447 */     return this._count;
/*      */   }
/*      */   
/*      */ 
/*      */   public int bucketCount()
/*      */   {
/*  453 */     return this._hash.length;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean maybeDirty()
/*      */   {
/*  460 */     return !this._hashShared;
/*      */   }
/*      */   
/*      */   public int hashSeed()
/*      */   {
/*  465 */     return this._seed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int collisionCount()
/*      */   {
/*  474 */     return this._collCount;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int maxCollisionLength()
/*      */   {
/*  484 */     return this._longestCollisionList;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Name getEmptyName()
/*      */   {
/*  494 */     return Name1.getEmptyName();
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
/*      */   public Name findName(int q1)
/*      */   {
/*  514 */     int hash = calcHash(q1);
/*  515 */     int ix = hash & this._hashMask;
/*  516 */     int val = this._hash[ix];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  521 */     if ((val >> 8 ^ hash) << 8 == 0)
/*      */     {
/*  523 */       Name name = this._mainNames[ix];
/*  524 */       if (name == null) {
/*  525 */         return null;
/*      */       }
/*  527 */       if (name.equals(q1)) {
/*  528 */         return name;
/*      */       }
/*  530 */     } else if (val == 0) {
/*  531 */       return null;
/*      */     }
/*      */     
/*  534 */     val &= 0xFF;
/*  535 */     if (val > 0) {
/*  536 */       val--;
/*  537 */       Bucket bucket = this._collList[val];
/*  538 */       if (bucket != null) {
/*  539 */         return bucket.find(hash, q1, 0);
/*      */       }
/*      */     }
/*      */     
/*  543 */     return null;
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
/*      */   public Name findName(int q1, int q2)
/*      */   {
/*  563 */     int hash = q2 == 0 ? calcHash(q1) : calcHash(q1, q2);
/*  564 */     int ix = hash & this._hashMask;
/*  565 */     int val = this._hash[ix];
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  570 */     if ((val >> 8 ^ hash) << 8 == 0)
/*      */     {
/*  572 */       Name name = this._mainNames[ix];
/*  573 */       if (name == null) {
/*  574 */         return null;
/*      */       }
/*  576 */       if (name.equals(q1, q2)) {
/*  577 */         return name;
/*      */       }
/*  579 */     } else if (val == 0) {
/*  580 */       return null;
/*      */     }
/*      */     
/*  583 */     val &= 0xFF;
/*  584 */     if (val > 0) {
/*  585 */       val--;
/*  586 */       Bucket bucket = this._collList[val];
/*  587 */       if (bucket != null) {
/*  588 */         return bucket.find(hash, q1, q2);
/*      */       }
/*      */     }
/*      */     
/*  592 */     return null;
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
/*      */   public Name findName(int[] q, int qlen)
/*      */   {
/*  614 */     if (qlen < 3) {
/*  615 */       return findName(q[0], qlen < 2 ? 0 : q[1]);
/*      */     }
/*  617 */     int hash = calcHash(q, qlen);
/*      */     
/*  619 */     int ix = hash & this._hashMask;
/*  620 */     int val = this._hash[ix];
/*  621 */     if ((val >> 8 ^ hash) << 8 == 0) {
/*  622 */       Name name = this._mainNames[ix];
/*  623 */       if ((name == null) || (name.equals(q, qlen)))
/*      */       {
/*  625 */         return name;
/*      */       }
/*  627 */     } else if (val == 0) {
/*  628 */       return null;
/*      */     }
/*  630 */     val &= 0xFF;
/*  631 */     if (val > 0) {
/*  632 */       val--;
/*  633 */       Bucket bucket = this._collList[val];
/*  634 */       if (bucket != null) {
/*  635 */         return bucket.find(hash, q, qlen);
/*      */       }
/*      */     }
/*  638 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Name addName(String name, int q1, int q2)
/*      */   {
/*  649 */     if (this._intern) {
/*  650 */       name = InternCache.instance.intern(name);
/*      */     }
/*  652 */     int hash = q2 == 0 ? calcHash(q1) : calcHash(q1, q2);
/*  653 */     Name symbol = constructName(hash, name, q1, q2);
/*  654 */     _addSymbol(hash, symbol);
/*  655 */     return symbol;
/*      */   }
/*      */   
/*      */   public Name addName(String name, int[] q, int qlen)
/*      */   {
/*  660 */     if (this._intern)
/*  661 */       name = InternCache.instance.intern(name);
/*      */     int hash;
/*      */     int hash;
/*  664 */     if (qlen < 3) {
/*  665 */       hash = qlen == 1 ? calcHash(q[0]) : calcHash(q[0], q[1]);
/*      */     } else {
/*  667 */       hash = calcHash(q, qlen);
/*      */     }
/*  669 */     Name symbol = constructName(hash, name, q, qlen);
/*  670 */     _addSymbol(hash, symbol);
/*  671 */     return symbol;
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
/*      */   public int calcHash(int q1)
/*      */   {
/*  696 */     int hash = q1 ^ this._seed;
/*  697 */     hash += (hash >>> 15);
/*  698 */     hash ^= hash >>> 9;
/*  699 */     return hash;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int calcHash(int q1, int q2)
/*      */   {
/*  706 */     int hash = q1;
/*  707 */     hash ^= hash >>> 15;
/*  708 */     hash += q2 * 33;
/*  709 */     hash ^= this._seed;
/*  710 */     hash += (hash >>> 7);
/*      */     
/*      */ 
/*      */ 
/*  714 */     hash ^= hash >>> 4;
/*  715 */     return hash;
/*      */   }
/*      */   
/*      */ 
/*      */   public int calcHash(int[] q, int qlen)
/*      */   {
/*  721 */     if (qlen < 3) {
/*  722 */       throw new IllegalArgumentException();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  730 */     int hash = q[0] ^ this._seed;
/*  731 */     hash += (hash >>> 9);
/*  732 */     hash *= 33;
/*  733 */     hash += q[1];
/*  734 */     hash *= 65599;
/*  735 */     hash += (hash >>> 15);
/*  736 */     hash ^= q[2];
/*  737 */     hash += (hash >>> 17);
/*      */     
/*  739 */     for (int i = 3; i < qlen; i++) {
/*  740 */       hash = hash * 31 ^ q[i];
/*      */       
/*  742 */       hash += (hash >>> 3);
/*  743 */       hash ^= hash << 7;
/*      */     }
/*      */     
/*  746 */     hash += (hash >>> 15);
/*  747 */     hash ^= hash << 9;
/*  748 */     return hash;
/*      */   }
/*      */   
/*      */   protected static int[] calcQuads(byte[] wordBytes)
/*      */   {
/*  753 */     int blen = wordBytes.length;
/*  754 */     int[] result = new int[(blen + 3) / 4];
/*  755 */     for (int i = 0; i < blen; i++) {
/*  756 */       int x = wordBytes[i] & 0xFF;
/*      */       
/*  758 */       i++; if (i < blen) {
/*  759 */         x = x << 8 | wordBytes[i] & 0xFF;
/*  760 */         i++; if (i < blen) {
/*  761 */           x = x << 8 | wordBytes[i] & 0xFF;
/*  762 */           i++; if (i < blen) {
/*  763 */             x = x << 8 | wordBytes[i] & 0xFF;
/*      */           }
/*      */         }
/*      */       }
/*  767 */       result[(i >> 2)] = x;
/*      */     }
/*  769 */     return result;
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
/*      */   private void _addSymbol(int hash, Name symbol)
/*      */   {
/*  825 */     if (this._hashShared) {
/*  826 */       unshareMain();
/*      */     }
/*      */     
/*  829 */     if (this._needRehash) {
/*  830 */       rehash();
/*      */     }
/*      */     
/*  833 */     this._count += 1;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  838 */     int ix = hash & this._hashMask;
/*  839 */     if (this._mainNames[ix] == null) {
/*  840 */       this._hash[ix] = (hash << 8);
/*  841 */       if (this._namesShared) {
/*  842 */         unshareNames();
/*      */       }
/*  844 */       this._mainNames[ix] = symbol;
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  849 */       if (this._collListShared) {
/*  850 */         unshareCollision();
/*      */       }
/*  852 */       this._collCount += 1;
/*  853 */       int entryValue = this._hash[ix];
/*  854 */       int bucket = entryValue & 0xFF;
/*  855 */       if (bucket == 0) {
/*  856 */         if (this._collEnd <= 254) {
/*  857 */           bucket = this._collEnd;
/*  858 */           this._collEnd += 1;
/*      */           
/*  860 */           if (bucket >= this._collList.length) {
/*  861 */             expandCollision();
/*      */           }
/*      */         } else {
/*  864 */           bucket = findBestBucket();
/*      */         }
/*      */         
/*  867 */         this._hash[ix] = (entryValue & 0xFF00 | bucket + 1);
/*      */       } else {
/*  869 */         bucket--;
/*      */       }
/*      */       
/*      */ 
/*  873 */       Bucket newB = new Bucket(symbol, this._collList[bucket]);
/*  874 */       int collLen = newB.length;
/*  875 */       if (collLen > 200)
/*      */       {
/*      */ 
/*      */ 
/*  879 */         _handleSpillOverflow(bucket, newB);
/*      */       } else {
/*  881 */         this._collList[bucket] = newB;
/*      */         
/*  883 */         this._longestCollisionList = Math.max(newB.length, this._longestCollisionList);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  891 */     int hashSize = this._hash.length;
/*  892 */     if (this._count > hashSize >> 1) {
/*  893 */       int hashQuarter = hashSize >> 2;
/*      */       
/*      */ 
/*      */ 
/*  897 */       if (this._count > hashSize - hashQuarter) {
/*  898 */         this._needRehash = true;
/*  899 */       } else if (this._collCount >= hashQuarter) {
/*  900 */         this._needRehash = true;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void _handleSpillOverflow(int bindex, Bucket newBucket)
/*      */   {
/*  908 */     if (this._overflows == null) {
/*  909 */       this._overflows = new BitSet();
/*  910 */       this._overflows.set(bindex);
/*      */     }
/*  912 */     else if (this._overflows.get(bindex))
/*      */     {
/*  914 */       if (this._failOnDoS) {
/*  915 */         reportTooManyCollisions(200);
/*      */       }
/*      */       
/*  918 */       this._intern = false;
/*      */     } else {
/*  920 */       this._overflows.set(bindex);
/*      */     }
/*      */     
/*      */ 
/*  924 */     this._collList[bindex] = null;
/*  925 */     this._count -= newBucket.length;
/*      */     
/*  927 */     this._longestCollisionList = -1;
/*      */   }
/*      */   
/*      */   private void rehash()
/*      */   {
/*  932 */     this._needRehash = false;
/*      */     
/*  934 */     this._namesShared = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  940 */     int[] oldMainHash = this._hash;
/*  941 */     int len = oldMainHash.length;
/*  942 */     int newLen = len + len;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  947 */     if (newLen > 65536) {
/*  948 */       nukeSymbols();
/*  949 */       return;
/*      */     }
/*      */     
/*  952 */     this._hash = new int[newLen];
/*  953 */     this._hashMask = (newLen - 1);
/*  954 */     Name[] oldNames = this._mainNames;
/*  955 */     this._mainNames = new Name[newLen];
/*  956 */     int symbolsSeen = 0;
/*  957 */     for (int i = 0; i < len; i++) {
/*  958 */       Name symbol = oldNames[i];
/*  959 */       if (symbol != null) {
/*  960 */         symbolsSeen++;
/*  961 */         int hash = symbol.hashCode();
/*  962 */         int ix = hash & this._hashMask;
/*  963 */         this._mainNames[ix] = symbol;
/*  964 */         this._hash[ix] = (hash << 8);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  972 */     int oldEnd = this._collEnd;
/*  973 */     if (oldEnd == 0) {
/*  974 */       this._longestCollisionList = 0;
/*  975 */       return;
/*      */     }
/*      */     
/*  978 */     this._collCount = 0;
/*  979 */     this._collEnd = 0;
/*  980 */     this._collListShared = false;
/*      */     
/*  982 */     int maxColl = 0;
/*      */     
/*  984 */     Bucket[] oldBuckets = this._collList;
/*  985 */     this._collList = new Bucket[oldBuckets.length];
/*  986 */     for (int i = 0; i < oldEnd; i++) {
/*  987 */       for (Bucket curr = oldBuckets[i]; curr != null; curr = curr.next) {
/*  988 */         symbolsSeen++;
/*  989 */         Name symbol = curr.name;
/*  990 */         int hash = symbol.hashCode();
/*  991 */         int ix = hash & this._hashMask;
/*  992 */         int val = this._hash[ix];
/*  993 */         if (this._mainNames[ix] == null) {
/*  994 */           this._hash[ix] = (hash << 8);
/*  995 */           this._mainNames[ix] = symbol;
/*      */         } else {
/*  997 */           this._collCount += 1;
/*  998 */           int bucket = val & 0xFF;
/*  999 */           if (bucket == 0) {
/* 1000 */             if (this._collEnd <= 254) {
/* 1001 */               bucket = this._collEnd;
/* 1002 */               this._collEnd += 1;
/*      */               
/* 1004 */               if (bucket >= this._collList.length) {
/* 1005 */                 expandCollision();
/*      */               }
/*      */             } else {
/* 1008 */               bucket = findBestBucket();
/*      */             }
/*      */             
/* 1011 */             this._hash[ix] = (val & 0xFF00 | bucket + 1);
/*      */           } else {
/* 1013 */             bucket--;
/*      */           }
/*      */           
/* 1016 */           Bucket newB = new Bucket(symbol, this._collList[bucket]);
/* 1017 */           this._collList[bucket] = newB;
/* 1018 */           maxColl = Math.max(maxColl, newB.length);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1023 */     this._longestCollisionList = maxColl;
/*      */     
/* 1025 */     if (symbolsSeen != this._count) {
/* 1026 */       throw new RuntimeException("Internal error: count after rehash " + symbolsSeen + "; should be " + this._count);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void nukeSymbols()
/*      */   {
/* 1035 */     this._count = 0;
/* 1036 */     this._longestCollisionList = 0;
/* 1037 */     Arrays.fill(this._hash, 0);
/* 1038 */     Arrays.fill(this._mainNames, null);
/* 1039 */     Arrays.fill(this._collList, null);
/* 1040 */     this._collCount = 0;
/* 1041 */     this._collEnd = 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private int findBestBucket()
/*      */   {
/* 1050 */     Bucket[] buckets = this._collList;
/* 1051 */     int bestCount = Integer.MAX_VALUE;
/* 1052 */     int bestIx = -1;
/*      */     
/* 1054 */     int i = 0; for (int len = this._collEnd; i < len; i++) {
/* 1055 */       Bucket b = buckets[i];
/*      */       
/* 1057 */       if (b == null) {
/* 1058 */         return i;
/*      */       }
/* 1060 */       int count = b.length;
/* 1061 */       if (count < bestCount) {
/* 1062 */         if (count == 1) {
/* 1063 */           return i;
/*      */         }
/* 1065 */         bestCount = count;
/* 1066 */         bestIx = i;
/*      */       }
/*      */     }
/* 1069 */     return bestIx;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void unshareMain()
/*      */   {
/* 1079 */     int[] old = this._hash;
/* 1080 */     this._hash = Arrays.copyOf(old, old.length);
/* 1081 */     this._hashShared = false;
/*      */   }
/*      */   
/*      */   private void unshareCollision() {
/* 1085 */     Bucket[] old = this._collList;
/* 1086 */     if (old == null) {
/* 1087 */       this._collList = new Bucket[32];
/*      */     } else {
/* 1089 */       this._collList = ((Bucket[])Arrays.copyOf(old, old.length));
/*      */     }
/* 1091 */     this._collListShared = false;
/*      */   }
/*      */   
/*      */   private void unshareNames() {
/* 1095 */     Name[] old = this._mainNames;
/* 1096 */     this._mainNames = ((Name[])Arrays.copyOf(old, old.length));
/* 1097 */     this._namesShared = false;
/*      */   }
/*      */   
/*      */   private void expandCollision() {
/* 1101 */     Bucket[] old = this._collList;
/* 1102 */     this._collList = ((Bucket[])Arrays.copyOf(old, old.length * 2));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static Name constructName(int hash, String name, int q1, int q2)
/*      */   {
/* 1112 */     if (q2 == 0) {
/* 1113 */       return new Name1(name, hash, q1);
/*      */     }
/* 1115 */     return new Name2(name, hash, q1, q2);
/*      */   }
/*      */   
/*      */   private static Name constructName(int hash, String name, int[] quads, int qlen) {
/* 1119 */     if (qlen < 4) {
/* 1120 */       switch (qlen) {
/*      */       case 1: 
/* 1122 */         return new Name1(name, hash, quads[0]);
/*      */       case 2: 
/* 1124 */         return new Name2(name, hash, quads[0], quads[1]);
/*      */       case 3: 
/* 1126 */         return new Name3(name, hash, quads[0], quads[1], quads[2]);
/*      */       }
/*      */       
/*      */     }
/* 1130 */     return NameN.construct(name, hash, quads, qlen);
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
/*      */   protected void reportTooManyCollisions(int maxLen)
/*      */   {
/* 1144 */     throw new IllegalStateException("Longest collision chain in symbol table (of size " + this._count + ") now exceeds maximum, " + maxLen + " -- suspect a DoS attack based on hash collisions");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static final class TableInfo
/*      */   {
/*      */     public final int count;
/*      */     
/*      */ 
/*      */     public final int mainHashMask;
/*      */     
/*      */ 
/*      */     public final int[] mainHash;
/*      */     
/*      */ 
/*      */     public final Name[] mainNames;
/*      */     
/*      */ 
/*      */     public final BytesToNameCanonicalizer.Bucket[] collList;
/*      */     
/*      */ 
/*      */     public final int collCount;
/*      */     
/*      */     public final int collEnd;
/*      */     
/*      */     public final int longestCollisionList;
/*      */     
/*      */ 
/*      */     public TableInfo(int count, int mainHashMask, int[] mainHash, Name[] mainNames, BytesToNameCanonicalizer.Bucket[] collList, int collCount, int collEnd, int longestCollisionList)
/*      */     {
/* 1175 */       this.count = count;
/* 1176 */       this.mainHashMask = mainHashMask;
/* 1177 */       this.mainHash = mainHash;
/* 1178 */       this.mainNames = mainNames;
/* 1179 */       this.collList = collList;
/* 1180 */       this.collCount = collCount;
/* 1181 */       this.collEnd = collEnd;
/* 1182 */       this.longestCollisionList = longestCollisionList;
/*      */     }
/*      */     
/*      */     public TableInfo(BytesToNameCanonicalizer src)
/*      */     {
/* 1187 */       this.count = src._count;
/* 1188 */       this.mainHashMask = src._hashMask;
/* 1189 */       this.mainHash = src._hash;
/* 1190 */       this.mainNames = src._mainNames;
/* 1191 */       this.collList = src._collList;
/* 1192 */       this.collCount = src._collCount;
/* 1193 */       this.collEnd = src._collEnd;
/* 1194 */       this.longestCollisionList = src._longestCollisionList;
/*      */     }
/*      */   }
/*      */   
/*      */   private static final class Bucket
/*      */   {
/*      */     public final Name name;
/*      */     public final Bucket next;
/*      */     public final int hash;
/*      */     public final int length;
/*      */     
/*      */     Bucket(Name name, Bucket next) {
/* 1206 */       this.name = name;
/* 1207 */       this.next = next;
/* 1208 */       this.length = (next == null ? 1 : next.length + 1);
/* 1209 */       this.hash = name.hashCode();
/*      */     }
/*      */     
/*      */     public Name find(int h, int firstQuad, int secondQuad) {
/* 1213 */       if ((this.hash == h) && 
/* 1214 */         (this.name.equals(firstQuad, secondQuad))) {
/* 1215 */         return this.name;
/*      */       }
/*      */       
/* 1218 */       for (Bucket curr = this.next; curr != null; curr = curr.next) {
/* 1219 */         if (curr.hash == h) {
/* 1220 */           Name currName = curr.name;
/* 1221 */           if (currName.equals(firstQuad, secondQuad)) {
/* 1222 */             return currName;
/*      */           }
/*      */         }
/*      */       }
/* 1226 */       return null;
/*      */     }
/*      */     
/*      */     public Name find(int h, int[] quads, int qlen) {
/* 1230 */       if ((this.hash == h) && 
/* 1231 */         (this.name.equals(quads, qlen))) {
/* 1232 */         return this.name;
/*      */       }
/*      */       
/* 1235 */       for (Bucket curr = this.next; curr != null; curr = curr.next) {
/* 1236 */         if (curr.hash == h) {
/* 1237 */           Name currName = curr.name;
/* 1238 */           if (currName.equals(quads, qlen)) {
/* 1239 */             return currName;
/*      */           }
/*      */         }
/*      */       }
/* 1243 */       return null;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\sym\BytesToNameCanonicalizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */