/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.MoreObjects.ToStringHelper;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.collect.Collections2;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSetMultimap;
/*     */ import com.google.common.collect.ImmutableSetMultimap.Builder;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.MultimapBuilder;
/*     */ import com.google.common.collect.MultimapBuilder.MultimapBuilderWithKeys;
/*     */ import com.google.common.collect.MultimapBuilder.SetMultimapBuilder;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.errorprone.annotations.concurrent.GuardedBy;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class ServiceManager
/*     */ {
/* 125 */   private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
/* 126 */   private static final ListenerCallQueue.Event<Listener> HEALTHY_EVENT = new ListenerCallQueue.Event()
/*     */   {
/*     */     public void call(ServiceManager.Listener listener)
/*     */     {
/* 130 */       listener.healthy();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 135 */       return "healthy()";
/*     */     }
/*     */   };
/* 138 */   private static final ListenerCallQueue.Event<Listener> STOPPED_EVENT = new ListenerCallQueue.Event()
/*     */   {
/*     */     public void call(ServiceManager.Listener listener)
/*     */     {
/* 142 */       listener.stopped();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 147 */       return "stopped()";
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ServiceManagerState state;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ImmutableList<Service> services;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static abstract class Listener
/*     */   {
/*     */     public void healthy() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void stopped() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void failure(Service service) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServiceManager(Iterable<? extends Service> services)
/*     */   {
/* 205 */     ImmutableList<Service> copy = ImmutableList.copyOf(services);
/* 206 */     if (copy.isEmpty())
/*     */     {
/*     */ 
/* 209 */       logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning(null));
/*     */       
/*     */ 
/*     */ 
/* 213 */       copy = ImmutableList.of(new NoOpService(null));
/*     */     }
/* 215 */     this.state = new ServiceManagerState(copy);
/* 216 */     this.services = copy;
/* 217 */     WeakReference<ServiceManagerState> stateReference = new WeakReference(this.state);
/* 218 */     for (UnmodifiableIterator localUnmodifiableIterator = copy.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/* 219 */       service.addListener(new ServiceListener(service, stateReference), MoreExecutors.directExecutor());
/*     */       
/*     */ 
/* 222 */       Preconditions.checkArgument(service.state() == Service.State.NEW, "Can only manage NEW services, %s", service);
/*     */     }
/*     */     
/*     */ 
/* 226 */     this.state.markReady();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addListener(Listener listener, Executor executor)
/*     */   {
/* 253 */     this.state.addListener(listener, executor);
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
/*     */   public void addListener(Listener listener)
/*     */   {
/* 273 */     this.state.addListener(listener, MoreExecutors.directExecutor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager startAsync()
/*     */   {
/* 286 */     for (UnmodifiableIterator localUnmodifiableIterator = this.services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/* 287 */       Service.State state = service.state();
/* 288 */       Preconditions.checkState(state == Service.State.NEW, "Service %s is %s, cannot start it.", service, state);
/*     */     }
/* 290 */     for (localUnmodifiableIterator = this.services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/*     */       try {
/* 292 */         this.state.tryStartTiming(service);
/* 293 */         service.startAsync();
/*     */ 
/*     */       }
/*     */       catch (IllegalStateException e)
/*     */       {
/*     */ 
/* 299 */         logger.log(Level.WARNING, "Unable to start Service " + service, e);
/*     */       }
/*     */     }
/* 302 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void awaitHealthy()
/*     */   {
/* 314 */     this.state.awaitHealthy();
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
/*     */   public void awaitHealthy(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 330 */     this.state.awaitHealthy(timeout, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager stopAsync()
/*     */   {
/* 341 */     for (UnmodifiableIterator localUnmodifiableIterator = this.services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/* 342 */       service.stopAsync();
/*     */     }
/* 344 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void awaitStopped()
/*     */   {
/* 353 */     this.state.awaitStopped();
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
/*     */   public void awaitStopped(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 367 */     this.state.awaitStopped(timeout, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isHealthy()
/*     */   {
/* 377 */     for (UnmodifiableIterator localUnmodifiableIterator = this.services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/* 378 */       if (!service.isRunning()) {
/* 379 */         return false;
/*     */       }
/*     */     }
/* 382 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableMultimap<Service.State, Service> servicesByState()
/*     */   {
/* 392 */     return this.state.servicesByState();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableMap<Service, Long> startupTimes()
/*     */   {
/* 403 */     return this.state.startupTimes();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 408 */     return 
/*     */     
/* 410 */       MoreObjects.toStringHelper(ServiceManager.class).add("services", Collections2.filter(this.services, Predicates.not(Predicates.instanceOf(NoOpService.class)))).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class ServiceManagerState
/*     */   {
/* 418 */     final Monitor monitor = new Monitor();
/*     */     
/*     */ 
/*     */     @GuardedBy("monitor")
/* 422 */     final SetMultimap<Service.State, Service> servicesByState = MultimapBuilder.enumKeys(Service.State.class).linkedHashSetValues().build();
/*     */     @GuardedBy("monitor")
/* 424 */     final Multiset<Service.State> states = this.servicesByState
/* 425 */       .keys();
/*     */     
/*     */     @GuardedBy("monitor")
/* 428 */     final Map<Service, Stopwatch> startupTimers = Maps.newIdentityHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @GuardedBy("monitor")
/*     */     boolean ready;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @GuardedBy("monitor")
/*     */     boolean transitioned;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     final int numberOfServices;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 455 */     final Monitor.Guard awaitHealthGuard = new AwaitHealthGuard();
/*     */     
/*     */     final class AwaitHealthGuard extends Monitor.Guard
/*     */     {
/*     */       AwaitHealthGuard() {
/* 460 */         super();
/*     */       }
/*     */       
/*     */ 
/*     */       @GuardedBy("ServiceManagerState.this.monitor")
/*     */       public boolean isSatisfied()
/*     */       {
/* 467 */         return (ServiceManager.ServiceManagerState.this.states.count(Service.State.RUNNING) == ServiceManager.ServiceManagerState.this.numberOfServices) || 
/* 468 */           (ServiceManager.ServiceManagerState.this.states.contains(Service.State.STOPPING)) || 
/* 469 */           (ServiceManager.ServiceManagerState.this.states.contains(Service.State.TERMINATED)) || 
/* 470 */           (ServiceManager.ServiceManagerState.this.states.contains(Service.State.FAILED));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 475 */     final Monitor.Guard stoppedGuard = new StoppedGuard();
/*     */     
/*     */     final class StoppedGuard extends Monitor.Guard
/*     */     {
/*     */       StoppedGuard() {
/* 480 */         super();
/*     */       }
/*     */       
/*     */       @GuardedBy("ServiceManagerState.this.monitor")
/*     */       public boolean isSatisfied()
/*     */       {
/* 486 */         return ServiceManager.ServiceManagerState.this.states.count(Service.State.TERMINATED) + ServiceManager.ServiceManagerState.this.states.count(Service.State.FAILED) == ServiceManager.ServiceManagerState.this.numberOfServices;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 491 */     final ListenerCallQueue<ServiceManager.Listener> listeners = new ListenerCallQueue();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     ServiceManagerState(ImmutableCollection<Service> services)
/*     */     {
/* 500 */       this.numberOfServices = services.size();
/* 501 */       this.servicesByState.putAll(Service.State.NEW, services);
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     void tryStartTiming(Service service)
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   4: invokevirtual 27	com/google/common/util/concurrent/Monitor:enter	()V
/*     */       //   7: aload_0
/*     */       //   8: getfield 13	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:startupTimers	Ljava/util/Map;
/*     */       //   11: aload_1
/*     */       //   12: invokeinterface 28 2 0
/*     */       //   17: checkcast 29	com/google/common/base/Stopwatch
/*     */       //   20: astore_2
/*     */       //   21: aload_2
/*     */       //   22: ifnonnull +17 -> 39
/*     */       //   25: aload_0
/*     */       //   26: getfield 13	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:startupTimers	Ljava/util/Map;
/*     */       //   29: aload_1
/*     */       //   30: invokestatic 30	com/google/common/base/Stopwatch:createStarted	()Lcom/google/common/base/Stopwatch;
/*     */       //   33: invokeinterface 31 3 0
/*     */       //   38: pop
/*     */       //   39: aload_0
/*     */       //   40: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   43: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */       //   46: goto +13 -> 59
/*     */       //   49: astore_3
/*     */       //   50: aload_0
/*     */       //   51: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   54: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */       //   57: aload_3
/*     */       //   58: athrow
/*     */       //   59: return
/*     */       // Line number table:
/*     */       //   Java source line #509	-> byte code offset #0
/*     */       //   Java source line #511	-> byte code offset #7
/*     */       //   Java source line #512	-> byte code offset #21
/*     */       //   Java source line #513	-> byte code offset #25
/*     */       //   Java source line #516	-> byte code offset #39
/*     */       //   Java source line #517	-> byte code offset #46
/*     */       //   Java source line #516	-> byte code offset #49
/*     */       //   Java source line #517	-> byte code offset #57
/*     */       //   Java source line #518	-> byte code offset #59
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	60	0	this	ServiceManagerState
/*     */       //   0	60	1	service	Service
/*     */       //   20	2	2	stopwatch	Stopwatch
/*     */       //   49	9	3	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   7	39	49	finally
/*     */     }
/*     */     
/*     */     void markReady()
/*     */     {
/* 525 */       this.monitor.enter();
/*     */       try {
/* 527 */         if (!this.transitioned)
/*     */         {
/* 529 */           this.ready = true;
/*     */         }
/*     */         else {
/* 532 */           List<Service> servicesInBadStates = Lists.newArrayList();
/* 533 */           for (UnmodifiableIterator localUnmodifiableIterator = servicesByState().values().iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/* 534 */             if (service.state() != Service.State.NEW) {
/* 535 */               servicesInBadStates.add(service);
/*     */             }
/*     */           }
/* 538 */           throw new IllegalArgumentException("Services started transitioning asynchronously before the ServiceManager was constructed: " + servicesInBadStates);
/*     */         }
/*     */         
/*     */       }
/*     */       finally
/*     */       {
/* 544 */         this.monitor.leave();
/*     */       }
/*     */     }
/*     */     
/*     */     void addListener(ServiceManager.Listener listener, Executor executor) {
/* 549 */       this.listeners.addListener(listener, executor);
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     void awaitHealthy()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   4: aload_0
/*     */       //   5: getfield 16	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:awaitHealthGuard	Lcom/google/common/util/concurrent/Monitor$Guard;
/*     */       //   8: invokevirtual 53	com/google/common/util/concurrent/Monitor:enterWhenUninterruptibly	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*     */       //   11: aload_0
/*     */       //   12: invokevirtual 54	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:checkHealthy	()V
/*     */       //   15: aload_0
/*     */       //   16: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   19: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */       //   22: goto +13 -> 35
/*     */       //   25: astore_1
/*     */       //   26: aload_0
/*     */       //   27: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   30: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */       //   33: aload_1
/*     */       //   34: athrow
/*     */       //   35: return
/*     */       // Line number table:
/*     */       //   Java source line #553	-> byte code offset #0
/*     */       //   Java source line #555	-> byte code offset #11
/*     */       //   Java source line #557	-> byte code offset #15
/*     */       //   Java source line #558	-> byte code offset #22
/*     */       //   Java source line #557	-> byte code offset #25
/*     */       //   Java source line #558	-> byte code offset #33
/*     */       //   Java source line #559	-> byte code offset #35
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	36	0	this	ServiceManagerState
/*     */       //   25	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	15	25	finally
/*     */     }
/*     */     
/*     */     void awaitHealthy(long timeout, TimeUnit unit)
/*     */       throws TimeoutException
/*     */     {
/* 562 */       this.monitor.enter();
/*     */       try {
/* 564 */         if (!this.monitor.waitForUninterruptibly(this.awaitHealthGuard, timeout, unit))
/*     */         {
/*     */ 
/*     */ 
/* 568 */           throw new TimeoutException("Timeout waiting for the services to become healthy. The following services have not started: " + Multimaps.filterKeys(this.servicesByState, Predicates.in(ImmutableSet.of(Service.State.NEW, Service.State.STARTING))));
/*     */         }
/* 570 */         checkHealthy();
/*     */       } finally {
/* 572 */         this.monitor.leave();
/*     */       }
/*     */     }
/*     */     
/*     */     void awaitStopped() {
/* 577 */       this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
/* 578 */       this.monitor.leave();
/*     */     }
/*     */     
/*     */     void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
/* 582 */       this.monitor.enter();
/*     */       try {
/* 584 */         if (!this.monitor.waitForUninterruptibly(this.stoppedGuard, timeout, unit))
/*     */         {
/*     */ 
/*     */ 
/* 588 */           throw new TimeoutException("Timeout waiting for the services to stop. The following services have not stopped: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.in(EnumSet.of(Service.State.TERMINATED, Service.State.FAILED)))));
/*     */         }
/*     */       } finally {
/* 591 */         this.monitor.leave();
/*     */       }
/*     */     }
/*     */     
/*     */     ImmutableMultimap<Service.State, Service> servicesByState() {
/* 596 */       ImmutableSetMultimap.Builder<Service.State, Service> builder = ImmutableSetMultimap.builder();
/* 597 */       this.monitor.enter();
/*     */       try {
/* 599 */         for (Map.Entry<Service.State, Service> entry : this.servicesByState.entries()) {
/* 600 */           if (!(entry.getValue() instanceof ServiceManager.NoOpService)) {
/* 601 */             builder.put(entry);
/*     */           }
/*     */         }
/*     */       } finally {
/* 605 */         this.monitor.leave();
/*     */       }
/* 607 */       return builder.build();
/*     */     }
/*     */     
/*     */     ImmutableMap<Service, Long> startupTimes()
/*     */     {
/* 612 */       this.monitor.enter();
/*     */       try {
/* 614 */         loadTimes = Lists.newArrayListWithCapacity(this.startupTimers.size());
/*     */         
/* 616 */         for (Map.Entry<Service, Stopwatch> entry : this.startupTimers.entrySet()) {
/* 617 */           Service service = (Service)entry.getKey();
/* 618 */           Stopwatch stopWatch = (Stopwatch)entry.getValue();
/* 619 */           if ((!stopWatch.isRunning()) && (!(service instanceof ServiceManager.NoOpService)))
/* 620 */             loadTimes.add(Maps.immutableEntry(service, Long.valueOf(stopWatch.elapsed(TimeUnit.MILLISECONDS))));
/*     */         }
/*     */       } finally {
/*     */         List<Map.Entry<Service, Long>> loadTimes;
/* 624 */         this.monitor.leave(); }
/*     */       List<Map.Entry<Service, Long>> loadTimes;
/* 626 */       Collections.sort(loadTimes, 
/*     */       
/* 628 */         Ordering.natural()
/* 629 */         .onResultOf(new Function()
/*     */         {
/*     */ 
/*     */           public Long apply(Map.Entry<Service, Long> input)
/*     */           {
/* 633 */             return (Long)input.getValue();
/*     */           }
/* 635 */         }));
/* 636 */       return ImmutableMap.copyOf(loadTimes);
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
/*     */ 
/*     */ 
/*     */     void transitionService(Service service, Service.State from, Service.State to)
/*     */     {
/* 652 */       Preconditions.checkNotNull(service);
/* 653 */       Preconditions.checkArgument(from != to);
/* 654 */       this.monitor.enter();
/*     */       try {
/* 656 */         this.transitioned = true;
/* 657 */         if (!this.ready) {
/* 658 */           return;
/*     */         }
/*     */         
/* 661 */         Preconditions.checkState(
/* 662 */           this.servicesByState.remove(from, service), "Service %s not at the expected location in the state map %s", service, from);
/*     */         
/*     */ 
/*     */ 
/* 666 */         Preconditions.checkState(this.servicesByState
/* 667 */           .put(to, service), "Service %s in the state map unexpectedly at %s", service, to);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 672 */         Stopwatch stopwatch = (Stopwatch)this.startupTimers.get(service);
/* 673 */         if (stopwatch == null)
/*     */         {
/* 675 */           stopwatch = Stopwatch.createStarted();
/* 676 */           this.startupTimers.put(service, stopwatch);
/*     */         }
/* 678 */         if ((to.compareTo(Service.State.RUNNING) >= 0) && (stopwatch.isRunning()))
/*     */         {
/* 680 */           stopwatch.stop();
/* 681 */           if (!(service instanceof ServiceManager.NoOpService)) {
/* 682 */             ServiceManager.logger.log(Level.FINE, "Started {0} in {1}.", new Object[] { service, stopwatch });
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 688 */         if (to == Service.State.FAILED) {
/* 689 */           enqueueFailedEvent(service);
/*     */         }
/*     */         
/* 692 */         if (this.states.count(Service.State.RUNNING) == this.numberOfServices)
/*     */         {
/*     */ 
/* 695 */           enqueueHealthyEvent();
/* 696 */         } else if (this.states.count(Service.State.TERMINATED) + this.states.count(Service.State.FAILED) == this.numberOfServices) {
/* 697 */           enqueueStoppedEvent();
/*     */         }
/*     */       } finally {
/* 700 */         this.monitor.leave();
/*     */         
/* 702 */         dispatchListenerEvents();
/*     */       }
/*     */     }
/*     */     
/*     */     void enqueueStoppedEvent() {
/* 707 */       this.listeners.enqueue(ServiceManager.STOPPED_EVENT);
/*     */     }
/*     */     
/*     */     void enqueueHealthyEvent() {
/* 711 */       this.listeners.enqueue(ServiceManager.HEALTHY_EVENT);
/*     */     }
/*     */     
/*     */     void enqueueFailedEvent(final Service service) {
/* 715 */       this.listeners.enqueue(new ListenerCallQueue.Event()
/*     */       {
/*     */         public void call(ServiceManager.Listener listener)
/*     */         {
/* 719 */           listener.failure(service);
/*     */         }
/*     */         
/*     */         public String toString()
/*     */         {
/* 724 */           return "failed({service=" + service + "})";
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     void dispatchListenerEvents()
/*     */     {
/* 731 */       Preconditions.checkState(
/* 732 */         !this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
/*     */       
/* 734 */       this.listeners.dispatch();
/*     */     }
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     void checkHealthy() {
/* 739 */       if (this.states.count(Service.State.RUNNING) != this.numberOfServices)
/*     */       {
/*     */ 
/*     */ 
/* 743 */         IllegalStateException exception = new IllegalStateException("Expected to be healthy after starting. The following services are not running: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.equalTo(Service.State.RUNNING))));
/* 744 */         for (Service service : this.servicesByState.get(Service.State.FAILED)) {
/* 745 */           exception.addSuppressed(new ServiceManager.FailedService(service));
/*     */         }
/* 747 */         throw exception;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class ServiceListener
/*     */     extends Service.Listener
/*     */   {
/*     */     final Service service;
/*     */     
/*     */     final WeakReference<ServiceManager.ServiceManagerState> state;
/*     */     
/*     */ 
/*     */     ServiceListener(Service service, WeakReference<ServiceManager.ServiceManagerState> state)
/*     */     {
/* 764 */       this.service = service;
/* 765 */       this.state = state;
/*     */     }
/*     */     
/*     */     public void starting()
/*     */     {
/* 770 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 771 */       if (state != null) {
/* 772 */         state.transitionService(this.service, Service.State.NEW, Service.State.STARTING);
/* 773 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 774 */           ServiceManager.logger.log(Level.FINE, "Starting {0}.", this.service);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void running()
/*     */     {
/* 781 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 782 */       if (state != null) {
/* 783 */         state.transitionService(this.service, Service.State.STARTING, Service.State.RUNNING);
/*     */       }
/*     */     }
/*     */     
/*     */     public void stopping(Service.State from)
/*     */     {
/* 789 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 790 */       if (state != null) {
/* 791 */         state.transitionService(this.service, from, Service.State.STOPPING);
/*     */       }
/*     */     }
/*     */     
/*     */     public void terminated(Service.State from)
/*     */     {
/* 797 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 798 */       if (state != null) {
/* 799 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 800 */           ServiceManager.logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[] { this.service, from });
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 805 */         state.transitionService(this.service, from, Service.State.TERMINATED);
/*     */       }
/*     */     }
/*     */     
/*     */     public void failed(Service.State from, Throwable failure)
/*     */     {
/* 811 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 812 */       if (state != null)
/*     */       {
/*     */ 
/* 815 */         boolean log = !(this.service instanceof ServiceManager.NoOpService);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 820 */         log &= from != Service.State.STARTING;
/* 821 */         if (log) {
/* 822 */           ServiceManager.logger.log(Level.SEVERE, "Service " + this.service + " has failed in the " + from + " state.", failure);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 827 */         state.transitionService(this.service, from, Service.State.FAILED);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class NoOpService
/*     */     extends AbstractService
/*     */   {
/*     */     protected void doStart()
/*     */     {
/* 843 */       notifyStarted();
/*     */     }
/*     */     
/*     */     protected void doStop()
/*     */     {
/* 848 */       notifyStopped();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyServiceManagerWarning extends Throwable
/*     */   {}
/*     */   
/*     */   private static final class FailedService extends Throwable {
/*     */     FailedService(Service service) {
/* 857 */       super(
/* 858 */         service
/* 859 */         .failureCause(), false, false);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\ServiceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */