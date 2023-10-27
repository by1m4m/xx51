/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @CanIgnoreReturnValue
/*     */ @GwtIncompatible
/*     */ public class CycleDetectingLockFactory
/*     */ {
/*     */   @Beta
/*     */   public static abstract interface Policy
/*     */   {
/*     */     public abstract void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException paramPotentialDeadlockException);
/*     */   }
/*     */   
/*     */   @Beta
/*     */   public static abstract enum Policies
/*     */     implements CycleDetectingLockFactory.Policy
/*     */   {
/* 201 */     THROW, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 213 */     WARN, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 228 */     DISABLED;
/*     */     
/*     */ 
/*     */     private Policies() {}
/*     */   }
/*     */   
/*     */   public static CycleDetectingLockFactory newInstance(Policy policy)
/*     */   {
/* 236 */     return new CycleDetectingLockFactory(policy);
/*     */   }
/*     */   
/*     */   public ReentrantLock newReentrantLock(String lockName)
/*     */   {
/* 241 */     return newReentrantLock(lockName, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReentrantLock newReentrantLock(String lockName, boolean fair)
/*     */   {
/* 249 */     return this.policy == Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingReentrantLock(new LockGraphNode(lockName), fair, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName)
/*     */   {
/* 256 */     return newReentrantReadWriteLock(lockName, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName, boolean fair)
/*     */   {
/* 265 */     return this.policy == Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(new LockGraphNode(lockName), fair, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 272 */   private static final ConcurrentMap<Class<? extends Enum>, Map<? extends Enum, LockGraphNode>> lockGraphNodesPerType = new MapMaker().weakKeys().makeMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> enumClass, Policy policy)
/*     */   {
/* 279 */     Preconditions.checkNotNull(enumClass);
/* 280 */     Preconditions.checkNotNull(policy);
/*     */     
/* 282 */     Map<E, LockGraphNode> lockGraphNodes = getOrCreateNodes(enumClass);
/* 283 */     return new WithExplicitOrdering(policy, lockGraphNodes);
/*     */   }
/*     */   
/*     */   private static Map<? extends Enum, LockGraphNode> getOrCreateNodes(Class<? extends Enum> clazz) {
/* 287 */     Map<? extends Enum, LockGraphNode> existing = (Map)lockGraphNodesPerType.get(clazz);
/* 288 */     if (existing != null) {
/* 289 */       return existing;
/*     */     }
/* 291 */     Map<? extends Enum, LockGraphNode> created = createNodes(clazz);
/* 292 */     existing = (Map)lockGraphNodesPerType.putIfAbsent(clazz, created);
/* 293 */     return (Map)MoreObjects.firstNonNull(existing, created);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(Class<E> clazz)
/*     */   {
/* 304 */     EnumMap<E, LockGraphNode> map = Maps.newEnumMap(clazz);
/* 305 */     E[] keys = (Enum[])clazz.getEnumConstants();
/* 306 */     int numKeys = keys.length;
/* 307 */     ArrayList<LockGraphNode> nodes = Lists.newArrayListWithCapacity(numKeys);
/*     */     
/* 309 */     for (E key : keys) {
/* 310 */       LockGraphNode node = new LockGraphNode(getLockName(key));
/* 311 */       nodes.add(node);
/* 312 */       map.put(key, node);
/*     */     }
/*     */     
/* 315 */     for (int i = 1; i < numKeys; i++) {
/* 316 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.THROW, nodes.subList(0, i));
/*     */     }
/*     */     
/* 319 */     for (int i = 0; i < numKeys - 1; i++) {
/* 320 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.DISABLED, nodes.subList(i + 1, numKeys));
/*     */     }
/* 322 */     return Collections.unmodifiableMap(map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getLockName(Enum<?> rank)
/*     */   {
/* 330 */     return rank.getDeclaringClass().getSimpleName() + "." + rank.name();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static final class WithExplicitOrdering<E extends Enum<E>>
/*     */     extends CycleDetectingLockFactory
/*     */   {
/*     */     private final Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @VisibleForTesting
/*     */     WithExplicitOrdering(CycleDetectingLockFactory.Policy policy, Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes)
/*     */     {
/* 400 */       super(null);
/* 401 */       this.lockGraphNodes = lockGraphNodes;
/*     */     }
/*     */     
/*     */     public ReentrantLock newReentrantLock(E rank)
/*     */     {
/* 406 */       return newReentrantLock(rank, false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ReentrantLock newReentrantLock(E rank, boolean fair)
/*     */     {
/* 418 */       return this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantLock(this, 
/*     */       
/* 420 */         (CycleDetectingLockFactory.LockGraphNode)this.lockGraphNodes.get(rank), fair, null);
/*     */     }
/*     */     
/*     */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank)
/*     */     {
/* 425 */       return newReentrantReadWriteLock(rank, false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank, boolean fair)
/*     */     {
/* 437 */       return this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock(this, 
/*     */       
/* 439 */         (CycleDetectingLockFactory.LockGraphNode)this.lockGraphNodes.get(rank), fair, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 445 */   private static final Logger logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
/*     */   final Policy policy;
/*     */   
/*     */   private CycleDetectingLockFactory(Policy policy)
/*     */   {
/* 450 */     this.policy = ((Policy)Preconditions.checkNotNull(policy));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 459 */   private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks = new ThreadLocal()
/*     */   {
/*     */     protected ArrayList<CycleDetectingLockFactory.LockGraphNode> initialValue()
/*     */     {
/* 463 */       return Lists.newArrayListWithCapacity(3);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ExampleStackTrace
/*     */     extends IllegalStateException
/*     */   {
/* 482 */     static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
/*     */     
/*     */ 
/* 485 */     static final ImmutableSet<String> EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class
/* 486 */       .getName(), ExampleStackTrace.class
/* 487 */       .getName(), CycleDetectingLockFactory.LockGraphNode.class
/* 488 */       .getName());
/*     */     
/*     */     ExampleStackTrace(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2) {
/* 491 */       super();
/* 492 */       StackTraceElement[] origStackTrace = getStackTrace();
/* 493 */       int i = 0; for (int n = origStackTrace.length; i < n; i++) {
/* 494 */         if (CycleDetectingLockFactory.WithExplicitOrdering.class.getName().equals(origStackTrace[i].getClassName()))
/*     */         {
/* 496 */           setStackTrace(EMPTY_STACK_TRACE);
/* 497 */           break;
/*     */         }
/* 499 */         if (!EXCLUDED_CLASS_NAMES.contains(origStackTrace[i].getClassName())) {
/* 500 */           setStackTrace((StackTraceElement[])Arrays.copyOfRange(origStackTrace, i, n));
/* 501 */           break;
/*     */         }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static final class PotentialDeadlockException
/*     */     extends CycleDetectingLockFactory.ExampleStackTrace
/*     */   {
/*     */     private final CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private PotentialDeadlockException(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2, CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace)
/*     */     {
/* 534 */       super(node2);
/* 535 */       this.conflictingStackTrace = conflictingStackTrace;
/* 536 */       initCause(conflictingStackTrace);
/*     */     }
/*     */     
/*     */     public CycleDetectingLockFactory.ExampleStackTrace getConflictingStackTrace() {
/* 540 */       return this.conflictingStackTrace;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getMessage()
/*     */     {
/* 549 */       StringBuilder message = new StringBuilder(super.getMessage());
/* 550 */       for (Throwable t = this.conflictingStackTrace; t != null; t = t.getCause()) {
/* 551 */         message.append(", ").append(t.getMessage());
/*     */       }
/* 553 */       return message.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static abstract interface CycleDetectingLock
/*     */   {
/*     */     public abstract CycleDetectingLockFactory.LockGraphNode getLockGraphNode();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract boolean isAcquiredByCurrentThread();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class LockGraphNode
/*     */   {
/* 581 */     final Map<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> allowedPriorLocks = new MapMaker()
/* 582 */       .weakKeys().makeMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 588 */     final Map<LockGraphNode, CycleDetectingLockFactory.PotentialDeadlockException> disallowedPriorLocks = new MapMaker()
/* 589 */       .weakKeys().makeMap();
/*     */     final String lockName;
/*     */     
/*     */     LockGraphNode(String lockName)
/*     */     {
/* 594 */       this.lockName = ((String)Preconditions.checkNotNull(lockName));
/*     */     }
/*     */     
/*     */     String getLockName() {
/* 598 */       return this.lockName;
/*     */     }
/*     */     
/*     */     void checkAcquiredLocks(CycleDetectingLockFactory.Policy policy, List<LockGraphNode> acquiredLocks) {
/* 602 */       int i = 0; for (int size = acquiredLocks.size(); i < size; i++) {
/* 603 */         checkAcquiredLock(policy, (LockGraphNode)acquiredLocks.get(i));
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */     void checkAcquiredLock(CycleDetectingLockFactory.Policy policy, LockGraphNode acquiredLock)
/*     */     {
/* 623 */       Preconditions.checkState(this != acquiredLock, "Attempted to acquire multiple locks with the same rank %s", acquiredLock
/*     */       
/*     */ 
/* 626 */         .getLockName());
/*     */       
/* 628 */       if (this.allowedPriorLocks.containsKey(acquiredLock))
/*     */       {
/*     */ 
/*     */ 
/* 632 */         return;
/*     */       }
/* 634 */       CycleDetectingLockFactory.PotentialDeadlockException previousDeadlockException = (CycleDetectingLockFactory.PotentialDeadlockException)this.disallowedPriorLocks.get(acquiredLock);
/* 635 */       if (previousDeadlockException != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 641 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace(), null);
/* 642 */         policy.handlePotentialDeadlock(exception);
/* 643 */         return;
/*     */       }
/*     */       
/*     */ 
/* 647 */       Set<LockGraphNode> seen = Sets.newIdentityHashSet();
/* 648 */       CycleDetectingLockFactory.ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
/*     */       
/* 650 */       if (path == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 659 */         this.allowedPriorLocks.put(acquiredLock, new CycleDetectingLockFactory.ExampleStackTrace(acquiredLock, this));
/*     */       }
/*     */       else
/*     */       {
/* 663 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, path, null);
/*     */         
/* 665 */         this.disallowedPriorLocks.put(acquiredLock, exception);
/* 666 */         policy.handlePotentialDeadlock(exception);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private CycleDetectingLockFactory.ExampleStackTrace findPathTo(LockGraphNode node, Set<LockGraphNode> seen)
/*     */     {
/* 678 */       if (!seen.add(this)) {
/* 679 */         return null;
/*     */       }
/* 681 */       CycleDetectingLockFactory.ExampleStackTrace found = (CycleDetectingLockFactory.ExampleStackTrace)this.allowedPriorLocks.get(node);
/* 682 */       if (found != null) {
/* 683 */         return found;
/*     */       }
/*     */       
/* 686 */       for (Map.Entry<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> entry : this.allowedPriorLocks.entrySet()) {
/* 687 */         LockGraphNode preAcquiredLock = (LockGraphNode)entry.getKey();
/* 688 */         found = preAcquiredLock.findPathTo(node, seen);
/* 689 */         if (found != null)
/*     */         {
/*     */ 
/*     */ 
/* 693 */           CycleDetectingLockFactory.ExampleStackTrace path = new CycleDetectingLockFactory.ExampleStackTrace(preAcquiredLock, this);
/* 694 */           path.setStackTrace(((CycleDetectingLockFactory.ExampleStackTrace)entry.getValue()).getStackTrace());
/* 695 */           path.initCause(found);
/* 696 */           return path;
/*     */         }
/*     */       }
/* 699 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void aboutToAcquire(CycleDetectingLock lock)
/*     */   {
/* 707 */     if (!lock.isAcquiredByCurrentThread()) {
/* 708 */       ArrayList<LockGraphNode> acquiredLockList = (ArrayList)acquiredLocks.get();
/* 709 */       LockGraphNode node = lock.getLockGraphNode();
/* 710 */       node.checkAcquiredLocks(this.policy, acquiredLockList);
/* 711 */       acquiredLockList.add(node);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void lockStateChanged(CycleDetectingLock lock)
/*     */   {
/* 721 */     if (!lock.isAcquiredByCurrentThread()) {
/* 722 */       ArrayList<LockGraphNode> acquiredLockList = (ArrayList)acquiredLocks.get();
/* 723 */       LockGraphNode node = lock.getLockGraphNode();
/*     */       
/*     */ 
/* 726 */       for (int i = acquiredLockList.size() - 1; i >= 0; i--) {
/* 727 */         if (acquiredLockList.get(i) == node) {
/* 728 */           acquiredLockList.remove(i);
/* 729 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   final class CycleDetectingReentrantLock extends ReentrantLock implements CycleDetectingLockFactory.CycleDetectingLock
/*     */   {
/*     */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*     */     
/*     */     private CycleDetectingReentrantLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
/* 740 */       super();
/* 741 */       this.lockGraphNode = ((CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode()
/*     */     {
/* 748 */       return this.lockGraphNode;
/*     */     }
/*     */     
/*     */     public boolean isAcquiredByCurrentThread()
/*     */     {
/* 753 */       return isHeldByCurrentThread();
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: invokestatic 8	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   8: aload_0
/*     */       //   9: invokespecial 9	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */       //   12: aload_0
/*     */       //   13: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   16: goto +10 -> 26
/*     */       //   19: astore_1
/*     */       //   20: aload_0
/*     */       //   21: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   24: aload_1
/*     */       //   25: athrow
/*     */       //   26: return
/*     */       // Line number table:
/*     */       //   Java source line #760	-> byte code offset #0
/*     */       //   Java source line #762	-> byte code offset #8
/*     */       //   Java source line #764	-> byte code offset #12
/*     */       //   Java source line #765	-> byte code offset #16
/*     */       //   Java source line #764	-> byte code offset #19
/*     */       //   Java source line #765	-> byte code offset #24
/*     */       //   Java source line #766	-> byte code offset #26
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	27	0	this	CycleDetectingReentrantLock
/*     */       //   19	6	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   8	12	19	finally
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lockInterruptibly()
/*     */       throws InterruptedException
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: invokestatic 8	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   8: aload_0
/*     */       //   9: invokespecial 11	java/util/concurrent/locks/ReentrantLock:lockInterruptibly	()V
/*     */       //   12: aload_0
/*     */       //   13: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   16: goto +10 -> 26
/*     */       //   19: astore_1
/*     */       //   20: aload_0
/*     */       //   21: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   24: aload_1
/*     */       //   25: athrow
/*     */       //   26: return
/*     */       // Line number table:
/*     */       //   Java source line #770	-> byte code offset #0
/*     */       //   Java source line #772	-> byte code offset #8
/*     */       //   Java source line #774	-> byte code offset #12
/*     */       //   Java source line #775	-> byte code offset #16
/*     */       //   Java source line #774	-> byte code offset #19
/*     */       //   Java source line #775	-> byte code offset #24
/*     */       //   Java source line #776	-> byte code offset #26
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	27	0	this	CycleDetectingReentrantLock
/*     */       //   19	6	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   8	12	19	finally
/*     */     }
/*     */     
/*     */     public boolean tryLock()
/*     */     {
/* 780 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 782 */         return super.tryLock();
/*     */       } finally {
/* 784 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException
/*     */     {
/* 790 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 792 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 794 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void unlock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokespecial 14	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */       //   4: aload_0
/*     */       //   5: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   8: goto +10 -> 18
/*     */       //   11: astore_1
/*     */       //   12: aload_0
/*     */       //   13: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   16: aload_1
/*     */       //   17: athrow
/*     */       //   18: return
/*     */       // Line number table:
/*     */       //   Java source line #801	-> byte code offset #0
/*     */       //   Java source line #803	-> byte code offset #4
/*     */       //   Java source line #804	-> byte code offset #8
/*     */       //   Java source line #803	-> byte code offset #11
/*     */       //   Java source line #804	-> byte code offset #16
/*     */       //   Java source line #805	-> byte code offset #18
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	19	0	this	CycleDetectingReentrantLock
/*     */       //   11	6	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	4	11	finally
/*     */     }
/*     */   }
/*     */   
/*     */   final class CycleDetectingReentrantReadWriteLock
/*     */     extends ReentrantReadWriteLock
/*     */     implements CycleDetectingLockFactory.CycleDetectingLock
/*     */   {
/*     */     private final CycleDetectingLockFactory.CycleDetectingReentrantReadLock readLock;
/*     */     private final CycleDetectingLockFactory.CycleDetectingReentrantWriteLock writeLock;
/*     */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*     */     
/*     */     private CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair)
/*     */     {
/* 821 */       super();
/* 822 */       this.readLock = new CycleDetectingLockFactory.CycleDetectingReentrantReadLock(CycleDetectingLockFactory.this, this);
/* 823 */       this.writeLock = new CycleDetectingLockFactory.CycleDetectingReentrantWriteLock(CycleDetectingLockFactory.this, this);
/* 824 */       this.lockGraphNode = ((CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public ReentrantReadWriteLock.ReadLock readLock()
/*     */     {
/* 831 */       return this.readLock;
/*     */     }
/*     */     
/*     */     public ReentrantReadWriteLock.WriteLock writeLock()
/*     */     {
/* 836 */       return this.writeLock;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode()
/*     */     {
/* 843 */       return this.lockGraphNode;
/*     */     }
/*     */     
/*     */     public boolean isAcquiredByCurrentThread()
/*     */     {
/* 848 */       return (isWriteLockedByCurrentThread()) || (getReadHoldCount() > 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private class CycleDetectingReentrantReadLock extends ReentrantReadWriteLock.ReadLock {
/*     */     @Weak
/*     */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*     */     
/*     */     CycleDetectingReentrantReadLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
/* 857 */       super();
/* 858 */       this.readWriteLock = readWriteLock;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 4	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: aload_0
/*     */       //   12: invokespecial 5	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: goto +13 -> 35
/*     */       //   25: astore_1
/*     */       //   26: aload_0
/*     */       //   27: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   30: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   33: aload_1
/*     */       //   34: athrow
/*     */       //   35: return
/*     */       // Line number table:
/*     */       //   Java source line #863	-> byte code offset #0
/*     */       //   Java source line #865	-> byte code offset #11
/*     */       //   Java source line #867	-> byte code offset #15
/*     */       //   Java source line #868	-> byte code offset #22
/*     */       //   Java source line #867	-> byte code offset #25
/*     */       //   Java source line #868	-> byte code offset #33
/*     */       //   Java source line #869	-> byte code offset #35
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	36	0	this	CycleDetectingReentrantReadLock
/*     */       //   25	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	15	25	finally
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lockInterruptibly()
/*     */       throws InterruptedException
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 4	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: aload_0
/*     */       //   12: invokespecial 7	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lockInterruptibly	()V
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: goto +13 -> 35
/*     */       //   25: astore_1
/*     */       //   26: aload_0
/*     */       //   27: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   30: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   33: aload_1
/*     */       //   34: athrow
/*     */       //   35: return
/*     */       // Line number table:
/*     */       //   Java source line #873	-> byte code offset #0
/*     */       //   Java source line #875	-> byte code offset #11
/*     */       //   Java source line #877	-> byte code offset #15
/*     */       //   Java source line #878	-> byte code offset #22
/*     */       //   Java source line #877	-> byte code offset #25
/*     */       //   Java source line #878	-> byte code offset #33
/*     */       //   Java source line #879	-> byte code offset #35
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	36	0	this	CycleDetectingReentrantReadLock
/*     */       //   25	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	15	25	finally
/*     */     }
/*     */     
/*     */     public boolean tryLock()
/*     */     {
/* 883 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 885 */         return super.tryLock();
/*     */       } finally {
/* 887 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException
/*     */     {
/* 893 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 895 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 897 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void unlock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokespecial 10	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: goto +13 -> 24
/*     */       //   14: astore_1
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: aload_1
/*     */       //   23: athrow
/*     */       //   24: return
/*     */       // Line number table:
/*     */       //   Java source line #904	-> byte code offset #0
/*     */       //   Java source line #906	-> byte code offset #4
/*     */       //   Java source line #907	-> byte code offset #11
/*     */       //   Java source line #906	-> byte code offset #14
/*     */       //   Java source line #907	-> byte code offset #22
/*     */       //   Java source line #908	-> byte code offset #24
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	25	0	this	CycleDetectingReentrantReadLock
/*     */       //   14	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	4	14	finally
/*     */     }
/*     */   }
/*     */   
/*     */   private class CycleDetectingReentrantWriteLock
/*     */     extends ReentrantReadWriteLock.WriteLock
/*     */   {
/*     */     @Weak
/*     */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*     */     
/*     */     CycleDetectingReentrantWriteLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock)
/*     */     {
/* 916 */       super();
/* 917 */       this.readWriteLock = readWriteLock;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 4	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: aload_0
/*     */       //   12: invokespecial 5	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: goto +13 -> 35
/*     */       //   25: astore_1
/*     */       //   26: aload_0
/*     */       //   27: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   30: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   33: aload_1
/*     */       //   34: athrow
/*     */       //   35: return
/*     */       // Line number table:
/*     */       //   Java source line #922	-> byte code offset #0
/*     */       //   Java source line #924	-> byte code offset #11
/*     */       //   Java source line #926	-> byte code offset #15
/*     */       //   Java source line #927	-> byte code offset #22
/*     */       //   Java source line #926	-> byte code offset #25
/*     */       //   Java source line #927	-> byte code offset #33
/*     */       //   Java source line #928	-> byte code offset #35
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	36	0	this	CycleDetectingReentrantWriteLock
/*     */       //   25	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	15	25	finally
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lockInterruptibly()
/*     */       throws InterruptedException
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 4	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: aload_0
/*     */       //   12: invokespecial 7	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lockInterruptibly	()V
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: goto +13 -> 35
/*     */       //   25: astore_1
/*     */       //   26: aload_0
/*     */       //   27: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   30: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   33: aload_1
/*     */       //   34: athrow
/*     */       //   35: return
/*     */       // Line number table:
/*     */       //   Java source line #932	-> byte code offset #0
/*     */       //   Java source line #934	-> byte code offset #11
/*     */       //   Java source line #936	-> byte code offset #15
/*     */       //   Java source line #937	-> byte code offset #22
/*     */       //   Java source line #936	-> byte code offset #25
/*     */       //   Java source line #937	-> byte code offset #33
/*     */       //   Java source line #938	-> byte code offset #35
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	36	0	this	CycleDetectingReentrantWriteLock
/*     */       //   25	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	15	25	finally
/*     */     }
/*     */     
/*     */     public boolean tryLock()
/*     */     {
/* 942 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 944 */         return super.tryLock();
/*     */       } finally {
/* 946 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException
/*     */     {
/* 952 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 954 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 956 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void unlock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokespecial 10	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: goto +13 -> 24
/*     */       //   14: astore_1
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: aload_1
/*     */       //   23: athrow
/*     */       //   24: return
/*     */       // Line number table:
/*     */       //   Java source line #963	-> byte code offset #0
/*     */       //   Java source line #965	-> byte code offset #4
/*     */       //   Java source line #966	-> byte code offset #11
/*     */       //   Java source line #965	-> byte code offset #14
/*     */       //   Java source line #966	-> byte code offset #22
/*     */       //   Java source line #967	-> byte code offset #24
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	25	0	this	CycleDetectingReentrantWriteLock
/*     */       //   14	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	4	14	finally
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\CycleDetectingLockFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */