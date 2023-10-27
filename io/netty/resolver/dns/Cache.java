/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.Delayed;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class Cache<E>
/*     */ {
/*  43 */   private static final AtomicReferenceFieldUpdater<Entries, ScheduledFuture> FUTURE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(Entries.class, ScheduledFuture.class, "expirationFuture");
/*     */   
/*  45 */   private static final ScheduledFuture<?> CANCELLED = new ScheduledFuture()
/*     */   {
/*     */     public boolean cancel(boolean mayInterruptIfRunning)
/*     */     {
/*  49 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public long getDelay(TimeUnit unit)
/*     */     {
/*  56 */       return Long.MIN_VALUE;
/*     */     }
/*     */     
/*     */     public int compareTo(Delayed o)
/*     */     {
/*  61 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public boolean isCancelled()
/*     */     {
/*  66 */       return true;
/*     */     }
/*     */     
/*     */     public boolean isDone()
/*     */     {
/*  71 */       return true;
/*     */     }
/*     */     
/*     */     public Object get()
/*     */     {
/*  76 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Object get(long timeout, TimeUnit unit)
/*     */     {
/*  81 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*  87 */   static final int MAX_SUPPORTED_TTL_SECS = (int)TimeUnit.DAYS.toSeconds(730L);
/*     */   
/*  89 */   private final ConcurrentMap<String, Cache<E>.Entries> resolveCache = PlatformDependent.newConcurrentHashMap();
/*     */   
/*     */ 
/*     */   final void clear()
/*     */   {
/*     */     Iterator<Map.Entry<String, Cache<E>.Entries>> i;
/*  95 */     while (!this.resolveCache.isEmpty()) {
/*  96 */       for (i = this.resolveCache.entrySet().iterator(); i.hasNext();) {
/*  97 */         Map.Entry<String, Cache<E>.Entries> e = (Map.Entry)i.next();
/*  98 */         i.remove();
/*     */         
/* 100 */         ((Entries)e.getValue()).clearAndCancel();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final boolean clear(String hostname)
/*     */   {
/* 109 */     Cache<E>.Entries entries = (Entries)this.resolveCache.remove(hostname);
/* 110 */     return (entries != null) && (entries.clearAndCancel());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final List<? extends E> get(String hostname)
/*     */   {
/* 117 */     Cache<E>.Entries entries = (Entries)this.resolveCache.get(hostname);
/* 118 */     return entries == null ? null : (List)entries.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final void cache(String hostname, E value, int ttl, EventLoop loop)
/*     */   {
/* 125 */     Cache<E>.Entries entries = (Entries)this.resolveCache.get(hostname);
/* 126 */     if (entries == null) {
/* 127 */       entries = new Entries(hostname);
/* 128 */       Cache<E>.Entries oldEntries = (Entries)this.resolveCache.putIfAbsent(hostname, entries);
/* 129 */       if (oldEntries != null) {
/* 130 */         entries = oldEntries;
/*     */       }
/*     */     }
/* 133 */     entries.add(value, ttl, loop);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final int size()
/*     */   {
/* 140 */     return this.resolveCache.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract boolean shouldReplaceAll(E paramE);
/*     */   
/*     */ 
/*     */ 
/*     */   protected void sortEntries(String hostname, List<E> entries) {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract boolean equals(E paramE1, E paramE2);
/*     */   
/*     */ 
/*     */ 
/*     */   private final class Entries
/*     */     extends AtomicReference<List<E>>
/*     */     implements Runnable
/*     */   {
/*     */     private final String hostname;
/*     */     
/*     */ 
/*     */     volatile ScheduledFuture<?> expirationFuture;
/*     */     
/*     */ 
/*     */     Entries(String hostname)
/*     */     {
/* 169 */       super();
/* 170 */       this.hostname = hostname;
/*     */     }
/*     */     
/*     */     void add(E e, int ttl, EventLoop loop) {
/* 174 */       if (!Cache.this.shouldReplaceAll(e)) {
/*     */         for (;;) {
/* 176 */           List<E> entries = (List)get();
/* 177 */           if (!entries.isEmpty()) {
/* 178 */             E firstEntry = entries.get(0);
/* 179 */             if (Cache.this.shouldReplaceAll(firstEntry)) {
/* 180 */               assert (entries.size() == 1);
/*     */               
/* 182 */               if (compareAndSet(entries, Collections.singletonList(e))) {
/* 183 */                 scheduleCacheExpirationIfNeeded(ttl, loop);
/*     */ 
/*     */               }
/*     */               
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/*     */ 
/* 192 */               List<E> newEntries = new ArrayList(entries.size() + 1);
/* 193 */               int i = 0;
/* 194 */               E replacedEntry = null;
/*     */               do {
/* 196 */                 E entry = entries.get(i);
/*     */                 
/*     */ 
/*     */ 
/* 200 */                 if (!Cache.this.equals(e, entry)) {
/* 201 */                   newEntries.add(entry);
/*     */                 } else {
/* 203 */                   replacedEntry = entry;
/* 204 */                   newEntries.add(e);
/*     */                   
/* 206 */                   i++;
/* 207 */                   for (; i < entries.size(); i++) {
/* 208 */                     newEntries.add(entries.get(i));
/*     */                   }
/*     */                 }
/*     */                 
/* 212 */                 i++; } while (i < entries.size());
/* 213 */               if (replacedEntry == null) {
/* 214 */                 newEntries.add(e);
/*     */               }
/* 216 */               Cache.this.sortEntries(this.hostname, newEntries);
/*     */               
/* 218 */               if (compareAndSet(entries, Collections.unmodifiableList(newEntries))) {
/* 219 */                 scheduleCacheExpirationIfNeeded(ttl, loop);
/* 220 */                 return;
/*     */               }
/* 222 */             } } else if (compareAndSet(entries, Collections.singletonList(e))) {
/* 223 */             scheduleCacheExpirationIfNeeded(ttl, loop);
/* 224 */             return;
/*     */           }
/*     */         }
/*     */       }
/* 228 */       set(Collections.singletonList(e));
/* 229 */       scheduleCacheExpirationIfNeeded(ttl, loop);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void scheduleCacheExpirationIfNeeded(int ttl, EventLoop loop)
/*     */     {
/*     */       for (;;)
/*     */       {
/* 238 */         ScheduledFuture<?> oldFuture = (ScheduledFuture)Cache.FUTURE_UPDATER.get(this);
/* 239 */         if ((oldFuture != null) && (oldFuture.getDelay(TimeUnit.SECONDS) <= ttl)) break;
/* 240 */         ScheduledFuture<?> newFuture = loop.schedule(this, ttl, TimeUnit.SECONDS);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 247 */         if (Cache.FUTURE_UPDATER.compareAndSet(this, oldFuture, newFuture)) {
/* 248 */           if (oldFuture == null) break;
/* 249 */           oldFuture.cancel(true); break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 254 */         newFuture.cancel(true);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     boolean clearAndCancel()
/*     */     {
/* 263 */       List<E> entries = (List)getAndSet(Collections.emptyList());
/* 264 */       if (entries.isEmpty()) {
/* 265 */         return false;
/*     */       }
/*     */       
/* 268 */       ScheduledFuture<?> expirationFuture = (ScheduledFuture)Cache.FUTURE_UPDATER.getAndSet(this, Cache.CANCELLED);
/* 269 */       if (expirationFuture != null) {
/* 270 */         expirationFuture.cancel(false);
/*     */       }
/*     */       
/* 273 */       return true;
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
/*     */     public void run()
/*     */     {
/* 287 */       Cache.this.resolveCache.remove(this.hostname, this);
/*     */       
/* 289 */       clearAndCancel();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\Cache.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */