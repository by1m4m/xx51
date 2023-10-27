/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import io.netty.util.ResourceLeakDetector;
/*     */ import io.netty.util.ResourceLeakDetectorFactory;
/*     */ import io.netty.util.ResourceLeakTracker;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DefaultOpenSslKeyMaterial
/*     */   extends AbstractReferenceCounted
/*     */   implements OpenSslKeyMaterial
/*     */ {
/*  28 */   private static final ResourceLeakDetector<DefaultOpenSslKeyMaterial> leakDetector = ResourceLeakDetectorFactory.instance().newResourceLeakDetector(DefaultOpenSslKeyMaterial.class);
/*     */   private final ResourceLeakTracker<DefaultOpenSslKeyMaterial> leak;
/*     */   private long chain;
/*     */   private long privateKey;
/*     */   
/*     */   DefaultOpenSslKeyMaterial(long chain, long privateKey) {
/*  34 */     this.chain = chain;
/*  35 */     this.privateKey = privateKey;
/*  36 */     this.leak = leakDetector.track(this);
/*     */   }
/*     */   
/*     */   public long certificateChainAddress()
/*     */   {
/*  41 */     if (refCnt() <= 0) {
/*  42 */       throw new IllegalReferenceCountException();
/*     */     }
/*  44 */     return this.chain;
/*     */   }
/*     */   
/*     */   public long privateKeyAddress()
/*     */   {
/*  49 */     if (refCnt() <= 0) {
/*  50 */       throw new IllegalReferenceCountException();
/*     */     }
/*  52 */     return this.privateKey;
/*     */   }
/*     */   
/*     */   protected void deallocate()
/*     */   {
/*  57 */     SSL.freeX509Chain(this.chain);
/*  58 */     this.chain = 0L;
/*  59 */     SSL.freePrivateKey(this.privateKey);
/*  60 */     this.privateKey = 0L;
/*  61 */     if (this.leak != null) {
/*  62 */       boolean closed = this.leak.close(this);
/*  63 */       assert (closed);
/*     */     }
/*     */   }
/*     */   
/*     */   public DefaultOpenSslKeyMaterial retain()
/*     */   {
/*  69 */     if (this.leak != null) {
/*  70 */       this.leak.record();
/*     */     }
/*  72 */     super.retain();
/*  73 */     return this;
/*     */   }
/*     */   
/*     */   public DefaultOpenSslKeyMaterial retain(int increment)
/*     */   {
/*  78 */     if (this.leak != null) {
/*  79 */       this.leak.record();
/*     */     }
/*  81 */     super.retain(increment);
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public DefaultOpenSslKeyMaterial touch()
/*     */   {
/*  87 */     if (this.leak != null) {
/*  88 */       this.leak.record();
/*     */     }
/*  90 */     super.touch();
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public DefaultOpenSslKeyMaterial touch(Object hint)
/*     */   {
/*  96 */     if (this.leak != null) {
/*  97 */       this.leak.record(hint);
/*     */     }
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/* 104 */     if (this.leak != null) {
/* 105 */       this.leak.record();
/*     */     }
/* 107 */     return super.release();
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 112 */     if (this.leak != null) {
/* 113 */       this.leak.record();
/*     */     }
/* 115 */     return super.release(decrement);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\DefaultOpenSslKeyMaterial.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */