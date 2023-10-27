/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractReferenceCountedByteBuf
/*     */   extends AbstractByteBuf
/*     */ {
/*     */   private static final long REFCNT_FIELD_OFFSET;
/*  32 */   private static final AtomicIntegerFieldUpdater<AbstractReferenceCountedByteBuf> refCntUpdater = AtomicIntegerFieldUpdater.newUpdater(AbstractReferenceCountedByteBuf.class, "refCnt");
/*     */   
/*  34 */   private volatile int refCnt = 1;
/*     */   
/*     */   static {
/*  37 */     long refCntFieldOffset = -1L;
/*     */     try {
/*  39 */       if (PlatformDependent.hasUnsafe()) {
/*  40 */         refCntFieldOffset = PlatformDependent.objectFieldOffset(AbstractReferenceCountedByteBuf.class
/*  41 */           .getDeclaredField("refCnt"));
/*     */       }
/*     */     } catch (Throwable ignore) {
/*  44 */       refCntFieldOffset = -1L;
/*     */     }
/*     */     
/*  47 */     REFCNT_FIELD_OFFSET = refCntFieldOffset;
/*     */   }
/*     */   
/*     */   protected AbstractReferenceCountedByteBuf(int maxCapacity) {
/*  51 */     super(maxCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int internalRefCnt()
/*     */   {
/*  60 */     return REFCNT_FIELD_OFFSET != -1L ? PlatformDependent.getInt(this, REFCNT_FIELD_OFFSET) : refCnt();
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/*  65 */     return this.refCnt;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void setRefCnt(int refCnt)
/*     */   {
/*  72 */     refCntUpdater.set(this, refCnt);
/*     */   }
/*     */   
/*     */   public ByteBuf retain()
/*     */   {
/*  77 */     return retain0(1);
/*     */   }
/*     */   
/*     */   public ByteBuf retain(int increment)
/*     */   {
/*  82 */     return retain0(ObjectUtil.checkPositive(increment, "increment"));
/*     */   }
/*     */   
/*     */   private ByteBuf retain0(int increment) {
/*  86 */     int oldRef = refCntUpdater.getAndAdd(this, increment);
/*  87 */     if ((oldRef <= 0) || (oldRef + increment < oldRef))
/*     */     {
/*  89 */       refCntUpdater.getAndAdd(this, -increment);
/*  90 */       throw new IllegalReferenceCountException(oldRef, increment);
/*     */     }
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf touch()
/*     */   {
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public ByteBuf touch(Object hint)
/*     */   {
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/* 107 */     return release0(1);
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 112 */     return release0(ObjectUtil.checkPositive(decrement, "decrement"));
/*     */   }
/*     */   
/*     */   private boolean release0(int decrement) {
/* 116 */     int oldRef = refCntUpdater.getAndAdd(this, -decrement);
/* 117 */     if (oldRef == decrement) {
/* 118 */       deallocate();
/* 119 */       return true;
/*     */     }
/* 121 */     if ((oldRef < decrement) || (oldRef - decrement > oldRef))
/*     */     {
/* 123 */       refCntUpdater.getAndAdd(this, decrement);
/* 124 */       throw new IllegalReferenceCountException(oldRef, -decrement);
/*     */     }
/* 126 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract void deallocate();
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\AbstractReferenceCountedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */