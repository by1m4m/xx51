/*     */ package org.eclipse.jetty.io;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.LeakDetector;
/*     */ import org.eclipse.jetty.util.LeakDetector.LeakInfo;
/*     */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LeakTrackingByteBufferPool
/*     */   extends ContainerLifeCycle
/*     */   implements ByteBufferPool
/*     */ {
/*  32 */   private static final Logger LOG = Log.getLogger(LeakTrackingByteBufferPool.class);
/*     */   
/*  34 */   private final LeakDetector<ByteBuffer> leakDetector = new LeakDetector()
/*     */   {
/*     */ 
/*     */     public String id(ByteBuffer resource)
/*     */     {
/*  39 */       return BufferUtil.toIDString(resource);
/*     */     }
/*     */     
/*     */ 
/*     */     protected void leaked(LeakDetector<ByteBuffer>.LeakInfo leakInfo)
/*     */     {
/*  45 */       LeakTrackingByteBufferPool.this.leaked.incrementAndGet();
/*  46 */       LeakTrackingByteBufferPool.this.leaked(leakInfo);
/*     */     }
/*     */   };
/*     */   
/*  50 */   private static final boolean NOISY = Boolean.getBoolean(LeakTrackingByteBufferPool.class.getName() + ".NOISY");
/*     */   private final ByteBufferPool delegate;
/*  52 */   private final AtomicLong leakedReleases = new AtomicLong(0L);
/*  53 */   private final AtomicLong leakedAcquires = new AtomicLong(0L);
/*  54 */   private final AtomicLong leaked = new AtomicLong(0L);
/*     */   
/*     */   public LeakTrackingByteBufferPool(ByteBufferPool delegate)
/*     */   {
/*  58 */     this.delegate = delegate;
/*  59 */     addBean(this.leakDetector);
/*  60 */     addBean(delegate);
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer acquire(int size, boolean direct)
/*     */   {
/*  66 */     ByteBuffer buffer = this.delegate.acquire(size, direct);
/*  67 */     boolean leaked = this.leakDetector.acquired(buffer);
/*  68 */     if ((NOISY) || (!leaked))
/*     */     {
/*  70 */       this.leakedAcquires.incrementAndGet();
/*  71 */       LOG.info(String.format("ByteBuffer acquire %s leaked.acquired=%s", new Object[] { this.leakDetector.id(buffer), leaked ? "normal" : "LEAK" }), new Throwable("LeakStack.Acquire"));
/*     */     }
/*     */     
/*  74 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */   public void release(ByteBuffer buffer)
/*     */   {
/*  80 */     if (buffer == null)
/*  81 */       return;
/*  82 */     boolean leaked = this.leakDetector.released(buffer);
/*  83 */     if ((NOISY) || (!leaked))
/*     */     {
/*  85 */       this.leakedReleases.incrementAndGet();
/*  86 */       LOG.info(String.format("ByteBuffer release %s leaked.released=%s", new Object[] { this.leakDetector.id(buffer), leaked ? "normal" : "LEAK" }), new Throwable("LeakStack.Release"));
/*     */     }
/*     */     
/*  89 */     this.delegate.release(buffer);
/*     */   }
/*     */   
/*     */   public void clearTracking()
/*     */   {
/*  94 */     this.leakedAcquires.set(0L);
/*  95 */     this.leakedReleases.set(0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLeakedAcquires()
/*     */   {
/* 103 */     return this.leakedAcquires.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLeakedReleases()
/*     */   {
/* 111 */     return this.leakedReleases.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLeakedResources()
/*     */   {
/* 119 */     return this.leaked.get();
/*     */   }
/*     */   
/*     */   protected void leaked(LeakDetector<ByteBuffer>.LeakInfo leakInfo)
/*     */   {
/* 124 */     LOG.warn("ByteBuffer " + leakInfo.getResourceDescription() + " leaked at:", leakInfo.getStackFrames());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\LeakTrackingByteBufferPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */