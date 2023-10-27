/*      */ package com.google.common.cache;
/*      */ 
/*      */ import com.google.common.annotations.GwtCompatible;
/*      */ import com.google.common.annotations.GwtIncompatible;
/*      */ import com.google.common.base.Ascii;
/*      */ import com.google.common.base.Equivalence;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.MoreObjects.ToStringHelper;
/*      */ import com.google.common.base.Preconditions;
/*      */ import com.google.common.base.Supplier;
/*      */ import com.google.common.base.Suppliers;
/*      */ import com.google.common.base.Ticker;
/*      */ import com.google.errorprone.annotations.CheckReturnValue;
/*      */ import java.time.Duration;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/*      */ public final class CacheBuilder<K, V>
/*      */ {
/*      */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*      */   private static final int DEFAULT_CONCURRENCY_LEVEL = 4;
/*      */   private static final int DEFAULT_EXPIRATION_NANOS = 0;
/*      */   private static final int DEFAULT_REFRESH_NANOS = 0;
/*  164 */   static final Supplier<? extends AbstractCache.StatsCounter> NULL_STATS_COUNTER = Suppliers.ofInstance(new AbstractCache.StatsCounter()
/*      */   {
/*      */     public void recordHits(int count) {}
/*      */     
/*      */ 
/*      */ 
/*      */     public void recordMisses(int count) {}
/*      */     
/*      */ 
/*      */     public void recordLoadSuccess(long loadTime) {}
/*      */     
/*      */ 
/*      */     public void recordLoadException(long loadTime) {}
/*      */     
/*      */ 
/*      */     public void recordEviction() {}
/*      */     
/*      */ 
/*      */     public CacheStats snapshot()
/*      */     {
/*  183 */       return CacheBuilder.EMPTY_STATS;
/*      */     }
/*  164 */   });
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  186 */   static final CacheStats EMPTY_STATS = new CacheStats(0L, 0L, 0L, 0L, 0L, 0L);
/*      */   
/*  188 */   static final Supplier<AbstractCache.StatsCounter> CACHE_STATS_COUNTER = new Supplier()
/*      */   {
/*      */     public AbstractCache.StatsCounter get()
/*      */     {
/*  192 */       return new AbstractCache.SimpleStatsCounter();
/*      */     }
/*      */   };
/*      */   
/*      */   static enum NullListener implements RemovalListener<Object, Object> {
/*  197 */     INSTANCE;
/*      */     
/*      */     private NullListener() {}
/*      */     
/*      */     public void onRemoval(RemovalNotification<Object, Object> notification) {}
/*      */   }
/*      */   
/*  204 */   static enum OneWeigher implements Weigher<Object, Object> { INSTANCE;
/*      */     
/*      */     private OneWeigher() {}
/*      */     
/*  208 */     public int weigh(Object key, Object value) { return 1; }
/*      */   }
/*      */   
/*      */ 
/*  212 */   static final Ticker NULL_TICKER = new Ticker()
/*      */   {
/*      */     public long read()
/*      */     {
/*  216 */       return 0L;
/*      */     }
/*      */   };
/*      */   
/*  220 */   private static final Logger logger = Logger.getLogger(CacheBuilder.class.getName());
/*      */   
/*      */   static final int UNSET_INT = -1;
/*      */   
/*  224 */   boolean strictParsing = true;
/*      */   
/*  226 */   int initialCapacity = -1;
/*  227 */   int concurrencyLevel = -1;
/*  228 */   long maximumSize = -1L;
/*  229 */   long maximumWeight = -1L;
/*      */   
/*      */   Weigher<? super K, ? super V> weigher;
/*      */   
/*      */   LocalCache.Strength keyStrength;
/*      */   LocalCache.Strength valueStrength;
/*  235 */   long expireAfterWriteNanos = -1L;
/*  236 */   long expireAfterAccessNanos = -1L;
/*  237 */   long refreshNanos = -1L;
/*      */   
/*      */   Equivalence<Object> keyEquivalence;
/*      */   
/*      */   Equivalence<Object> valueEquivalence;
/*      */   
/*      */   RemovalListener<? super K, ? super V> removalListener;
/*      */   Ticker ticker;
/*  245 */   Supplier<? extends AbstractCache.StatsCounter> statsCounterSupplier = NULL_STATS_COUNTER;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static CacheBuilder<Object, Object> newBuilder()
/*      */   {
/*  257 */     return new CacheBuilder();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static CacheBuilder<Object, Object> from(CacheBuilderSpec spec)
/*      */   {
/*  267 */     return spec.toCacheBuilder().lenientParsing();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   public static CacheBuilder<Object, Object> from(String spec)
/*      */   {
/*  279 */     return from(CacheBuilderSpec.parse(spec));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @GwtIncompatible
/*      */   CacheBuilder<K, V> lenientParsing()
/*      */   {
/*  289 */     this.strictParsing = false;
/*  290 */     return this;
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
/*      */   CacheBuilder<K, V> keyEquivalence(Equivalence<Object> equivalence)
/*      */   {
/*  303 */     Preconditions.checkState(this.keyEquivalence == null, "key equivalence was already set to %s", this.keyEquivalence);
/*  304 */     this.keyEquivalence = ((Equivalence)Preconditions.checkNotNull(equivalence));
/*  305 */     return this;
/*      */   }
/*      */   
/*      */   Equivalence<Object> getKeyEquivalence() {
/*  309 */     return (Equivalence)MoreObjects.firstNonNull(this.keyEquivalence, getKeyStrength().defaultEquivalence());
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
/*      */   @GwtIncompatible
/*      */   CacheBuilder<K, V> valueEquivalence(Equivalence<Object> equivalence)
/*      */   {
/*  323 */     Preconditions.checkState(this.valueEquivalence == null, "value equivalence was already set to %s", this.valueEquivalence);
/*      */     
/*  325 */     this.valueEquivalence = ((Equivalence)Preconditions.checkNotNull(equivalence));
/*  326 */     return this;
/*      */   }
/*      */   
/*      */   Equivalence<Object> getValueEquivalence() {
/*  330 */     return (Equivalence)MoreObjects.firstNonNull(this.valueEquivalence, getValueStrength().defaultEquivalence());
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
/*      */   public CacheBuilder<K, V> initialCapacity(int initialCapacity)
/*      */   {
/*  345 */     Preconditions.checkState(this.initialCapacity == -1, "initial capacity was already set to %s", this.initialCapacity);
/*      */     
/*      */ 
/*      */ 
/*  349 */     Preconditions.checkArgument(initialCapacity >= 0);
/*  350 */     this.initialCapacity = initialCapacity;
/*  351 */     return this;
/*      */   }
/*      */   
/*      */   int getInitialCapacity() {
/*  355 */     return this.initialCapacity == -1 ? 16 : this.initialCapacity;
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
/*      */   public CacheBuilder<K, V> concurrencyLevel(int concurrencyLevel)
/*      */   {
/*  390 */     Preconditions.checkState(this.concurrencyLevel == -1, "concurrency level was already set to %s", this.concurrencyLevel);
/*      */     
/*      */ 
/*      */ 
/*  394 */     Preconditions.checkArgument(concurrencyLevel > 0);
/*  395 */     this.concurrencyLevel = concurrencyLevel;
/*  396 */     return this;
/*      */   }
/*      */   
/*      */   int getConcurrencyLevel() {
/*  400 */     return this.concurrencyLevel == -1 ? 4 : this.concurrencyLevel;
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
/*      */   public CacheBuilder<K, V> maximumSize(long maximumSize)
/*      */   {
/*  425 */     Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", this.maximumSize);
/*      */     
/*  427 */     Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", this.maximumWeight);
/*      */     
/*      */ 
/*      */ 
/*  431 */     Preconditions.checkState(this.weigher == null, "maximum size can not be combined with weigher");
/*  432 */     Preconditions.checkArgument(maximumSize >= 0L, "maximum size must not be negative");
/*  433 */     this.maximumSize = maximumSize;
/*  434 */     return this;
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> maximumWeight(long maximumWeight)
/*      */   {
/*  466 */     Preconditions.checkState(this.maximumWeight == -1L, "maximum weight was already set to %s", this.maximumWeight);
/*      */     
/*      */ 
/*      */ 
/*  470 */     Preconditions.checkState(this.maximumSize == -1L, "maximum size was already set to %s", this.maximumSize);
/*      */     
/*  472 */     this.maximumWeight = maximumWeight;
/*  473 */     Preconditions.checkArgument(maximumWeight >= 0L, "maximum weight must not be negative");
/*  474 */     return this;
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
/*      */   @GwtIncompatible
/*      */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> weigher(Weigher<? super K1, ? super V1> weigher)
/*      */   {
/*  509 */     Preconditions.checkState(this.weigher == null);
/*  510 */     if (this.strictParsing) {
/*  511 */       Preconditions.checkState(this.maximumSize == -1L, "weigher can not be combined with maximum size", this.maximumSize);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  519 */     CacheBuilder<K1, V1> me = this;
/*  520 */     me.weigher = ((Weigher)Preconditions.checkNotNull(weigher));
/*  521 */     return me;
/*      */   }
/*      */   
/*      */   long getMaximumWeight() {
/*  525 */     if ((this.expireAfterWriteNanos == 0L) || (this.expireAfterAccessNanos == 0L)) {
/*  526 */       return 0L;
/*      */     }
/*  528 */     return this.weigher == null ? this.maximumSize : this.maximumWeight;
/*      */   }
/*      */   
/*      */ 
/*      */   <K1 extends K, V1 extends V> Weigher<K1, V1> getWeigher()
/*      */   {
/*  534 */     return (Weigher)MoreObjects.firstNonNull(this.weigher, OneWeigher.INSTANCE);
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> weakKeys()
/*      */   {
/*  555 */     return setKeyStrength(LocalCache.Strength.WEAK);
/*      */   }
/*      */   
/*      */   CacheBuilder<K, V> setKeyStrength(LocalCache.Strength strength) {
/*  559 */     Preconditions.checkState(this.keyStrength == null, "Key strength was already set to %s", this.keyStrength);
/*  560 */     this.keyStrength = ((LocalCache.Strength)Preconditions.checkNotNull(strength));
/*  561 */     return this;
/*      */   }
/*      */   
/*      */   LocalCache.Strength getKeyStrength() {
/*  565 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.keyStrength, LocalCache.Strength.STRONG);
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> weakValues()
/*      */   {
/*  587 */     return setValueStrength(LocalCache.Strength.WEAK);
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> softValues()
/*      */   {
/*  612 */     return setValueStrength(LocalCache.Strength.SOFT);
/*      */   }
/*      */   
/*      */   CacheBuilder<K, V> setValueStrength(LocalCache.Strength strength) {
/*  616 */     Preconditions.checkState(this.valueStrength == null, "Value strength was already set to %s", this.valueStrength);
/*  617 */     this.valueStrength = ((LocalCache.Strength)Preconditions.checkNotNull(strength));
/*  618 */     return this;
/*      */   }
/*      */   
/*      */   LocalCache.Strength getValueStrength() {
/*  622 */     return (LocalCache.Strength)MoreObjects.firstNonNull(this.valueStrength, LocalCache.Strength.STRONG);
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> expireAfterWrite(Duration duration)
/*      */   {
/*  648 */     return expireAfterWrite(duration.toNanos(), TimeUnit.NANOSECONDS);
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
/*      */   public CacheBuilder<K, V> expireAfterWrite(long duration, TimeUnit unit)
/*      */   {
/*  675 */     Preconditions.checkState(this.expireAfterWriteNanos == -1L, "expireAfterWrite was already set to %s ns", this.expireAfterWriteNanos);
/*      */     
/*      */ 
/*      */ 
/*  679 */     Preconditions.checkArgument(duration >= 0L, "duration cannot be negative: %s %s", duration, unit);
/*  680 */     this.expireAfterWriteNanos = unit.toNanos(duration);
/*  681 */     return this;
/*      */   }
/*      */   
/*      */   long getExpireAfterWriteNanos() {
/*  685 */     return this.expireAfterWriteNanos == -1L ? 0L : this.expireAfterWriteNanos;
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> expireAfterAccess(Duration duration)
/*      */   {
/*  714 */     return expireAfterAccess(duration.toNanos(), TimeUnit.NANOSECONDS);
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
/*      */   public CacheBuilder<K, V> expireAfterAccess(long duration, TimeUnit unit)
/*      */   {
/*  744 */     Preconditions.checkState(this.expireAfterAccessNanos == -1L, "expireAfterAccess was already set to %s ns", this.expireAfterAccessNanos);
/*      */     
/*      */ 
/*      */ 
/*  748 */     Preconditions.checkArgument(duration >= 0L, "duration cannot be negative: %s %s", duration, unit);
/*  749 */     this.expireAfterAccessNanos = unit.toNanos(duration);
/*  750 */     return this;
/*      */   }
/*      */   
/*      */   long getExpireAfterAccessNanos() {
/*  754 */     return this.expireAfterAccessNanos == -1L ? 0L : this.expireAfterAccessNanos;
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> refreshAfterWrite(Duration duration)
/*      */   {
/*  788 */     return refreshAfterWrite(duration.toNanos(), TimeUnit.NANOSECONDS);
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
/*      */   @GwtIncompatible
/*      */   public CacheBuilder<K, V> refreshAfterWrite(long duration, TimeUnit unit)
/*      */   {
/*  823 */     Preconditions.checkNotNull(unit);
/*  824 */     Preconditions.checkState(this.refreshNanos == -1L, "refresh was already set to %s ns", this.refreshNanos);
/*  825 */     Preconditions.checkArgument(duration > 0L, "duration must be positive: %s %s", duration, unit);
/*  826 */     this.refreshNanos = unit.toNanos(duration);
/*  827 */     return this;
/*      */   }
/*      */   
/*      */   long getRefreshNanos() {
/*  831 */     return this.refreshNanos == -1L ? 0L : this.refreshNanos;
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
/*      */   public CacheBuilder<K, V> ticker(Ticker ticker)
/*      */   {
/*  845 */     Preconditions.checkState(this.ticker == null);
/*  846 */     this.ticker = ((Ticker)Preconditions.checkNotNull(ticker));
/*  847 */     return this;
/*      */   }
/*      */   
/*      */   Ticker getTicker(boolean recordsTime) {
/*  851 */     if (this.ticker != null) {
/*  852 */       return this.ticker;
/*      */     }
/*  854 */     return recordsTime ? Ticker.systemTicker() : NULL_TICKER;
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
/*      */   @CheckReturnValue
/*      */   public <K1 extends K, V1 extends V> CacheBuilder<K1, V1> removalListener(RemovalListener<? super K1, ? super V1> listener)
/*      */   {
/*  881 */     Preconditions.checkState(this.removalListener == null);
/*      */     
/*      */ 
/*      */ 
/*  885 */     CacheBuilder<K1, V1> me = this;
/*  886 */     me.removalListener = ((RemovalListener)Preconditions.checkNotNull(listener));
/*  887 */     return me;
/*      */   }
/*      */   
/*      */ 
/*      */   <K1 extends K, V1 extends V> RemovalListener<K1, V1> getRemovalListener()
/*      */   {
/*  893 */     return 
/*  894 */       (RemovalListener)MoreObjects.firstNonNull(this.removalListener, NullListener.INSTANCE);
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
/*      */   public CacheBuilder<K, V> recordStats()
/*      */   {
/*  907 */     this.statsCounterSupplier = CACHE_STATS_COUNTER;
/*  908 */     return this;
/*      */   }
/*      */   
/*      */   boolean isRecordingStats() {
/*  912 */     return this.statsCounterSupplier == CACHE_STATS_COUNTER;
/*      */   }
/*      */   
/*      */   Supplier<? extends AbstractCache.StatsCounter> getStatsCounterSupplier() {
/*  916 */     return this.statsCounterSupplier;
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
/*      */   public <K1 extends K, V1 extends V> LoadingCache<K1, V1> build(CacheLoader<? super K1, V1> loader)
/*      */   {
/*  933 */     checkWeightWithWeigher();
/*  934 */     return new LocalCache.LocalLoadingCache(this, loader);
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
/*      */   public <K1 extends K, V1 extends V> Cache<K1, V1> build()
/*      */   {
/*  950 */     checkWeightWithWeigher();
/*  951 */     checkNonLoadingCache();
/*  952 */     return new LocalCache.LocalManualCache(this);
/*      */   }
/*      */   
/*      */   private void checkNonLoadingCache() {
/*  956 */     Preconditions.checkState(this.refreshNanos == -1L, "refreshAfterWrite requires a LoadingCache");
/*      */   }
/*      */   
/*      */   private void checkWeightWithWeigher() {
/*  960 */     if (this.weigher == null) {
/*  961 */       Preconditions.checkState(this.maximumWeight == -1L, "maximumWeight requires weigher");
/*      */     }
/*  963 */     else if (this.strictParsing) {
/*  964 */       Preconditions.checkState(this.maximumWeight != -1L, "weigher requires maximumWeight");
/*      */     }
/*  966 */     else if (this.maximumWeight == -1L) {
/*  967 */       logger.log(Level.WARNING, "ignoring weigher specified without maximumWeight");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  979 */     MoreObjects.ToStringHelper s = MoreObjects.toStringHelper(this);
/*  980 */     if (this.initialCapacity != -1) {
/*  981 */       s.add("initialCapacity", this.initialCapacity);
/*      */     }
/*  983 */     if (this.concurrencyLevel != -1) {
/*  984 */       s.add("concurrencyLevel", this.concurrencyLevel);
/*      */     }
/*  986 */     if (this.maximumSize != -1L) {
/*  987 */       s.add("maximumSize", this.maximumSize);
/*      */     }
/*  989 */     if (this.maximumWeight != -1L) {
/*  990 */       s.add("maximumWeight", this.maximumWeight);
/*      */     }
/*  992 */     if (this.expireAfterWriteNanos != -1L) {
/*  993 */       s.add("expireAfterWrite", this.expireAfterWriteNanos + "ns");
/*      */     }
/*  995 */     if (this.expireAfterAccessNanos != -1L) {
/*  996 */       s.add("expireAfterAccess", this.expireAfterAccessNanos + "ns");
/*      */     }
/*  998 */     if (this.keyStrength != null) {
/*  999 */       s.add("keyStrength", Ascii.toLowerCase(this.keyStrength.toString()));
/*      */     }
/* 1001 */     if (this.valueStrength != null) {
/* 1002 */       s.add("valueStrength", Ascii.toLowerCase(this.valueStrength.toString()));
/*      */     }
/* 1004 */     if (this.keyEquivalence != null) {
/* 1005 */       s.addValue("keyEquivalence");
/*      */     }
/* 1007 */     if (this.valueEquivalence != null) {
/* 1008 */       s.addValue("valueEquivalence");
/*      */     }
/* 1010 */     if (this.removalListener != null) {
/* 1011 */       s.addValue("removalListener");
/*      */     }
/* 1013 */     return s.toString();
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\cache\CacheBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */