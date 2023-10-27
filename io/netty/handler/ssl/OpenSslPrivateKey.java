/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import java.security.PrivateKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class OpenSslPrivateKey
/*     */   extends AbstractReferenceCounted
/*     */   implements PrivateKey
/*     */ {
/*     */   private long privateKeyAddress;
/*     */   
/*     */   OpenSslPrivateKey(long privateKeyAddress)
/*     */   {
/*  30 */     this.privateKeyAddress = privateKeyAddress;
/*     */   }
/*     */   
/*     */   public String getAlgorithm()
/*     */   {
/*  35 */     return "unkown";
/*     */   }
/*     */   
/*     */ 
/*     */   public String getFormat()
/*     */   {
/*  41 */     return null;
/*     */   }
/*     */   
/*     */   public byte[] getEncoded()
/*     */   {
/*  46 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   long privateKeyAddress()
/*     */   {
/*  53 */     if (refCnt() <= 0) {
/*  54 */       throw new IllegalReferenceCountException();
/*     */     }
/*  56 */     return this.privateKeyAddress;
/*     */   }
/*     */   
/*     */   protected void deallocate()
/*     */   {
/*  61 */     SSL.freePrivateKey(this.privateKeyAddress);
/*  62 */     this.privateKeyAddress = 0L;
/*     */   }
/*     */   
/*     */   public OpenSslPrivateKey retain()
/*     */   {
/*  67 */     super.retain();
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public OpenSslPrivateKey retain(int increment)
/*     */   {
/*  73 */     super.retain(increment);
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public OpenSslPrivateKey touch()
/*     */   {
/*  79 */     super.touch();
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public OpenSslPrivateKey touch(Object hint)
/*     */   {
/*  85 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/*  96 */     release(refCnt());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDestroyed()
/*     */   {
/* 107 */     return refCnt() == 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   OpenSslKeyMaterial toKeyMaterial(long certificateChain)
/*     */   {
/* 114 */     return new OpenSslPrivateKeyMaterial(certificateChain);
/*     */   }
/*     */   
/*     */   private final class OpenSslPrivateKeyMaterial implements OpenSslKeyMaterial
/*     */   {
/*     */     private long certificateChain;
/*     */     
/*     */     OpenSslPrivateKeyMaterial(long certificateChain) {
/* 122 */       this.certificateChain = certificateChain;
/*     */     }
/*     */     
/*     */     public long certificateChainAddress()
/*     */     {
/* 127 */       if (refCnt() <= 0) {
/* 128 */         throw new IllegalReferenceCountException();
/*     */       }
/* 130 */       return this.certificateChain;
/*     */     }
/*     */     
/*     */     public long privateKeyAddress()
/*     */     {
/* 135 */       return OpenSslPrivateKey.this.privateKeyAddress();
/*     */     }
/*     */     
/*     */     public OpenSslKeyMaterial retain()
/*     */     {
/* 140 */       OpenSslPrivateKey.this.retain();
/* 141 */       return this;
/*     */     }
/*     */     
/*     */     public OpenSslKeyMaterial retain(int increment)
/*     */     {
/* 146 */       OpenSslPrivateKey.this.retain(increment);
/* 147 */       return this;
/*     */     }
/*     */     
/*     */     public OpenSslKeyMaterial touch()
/*     */     {
/* 152 */       OpenSslPrivateKey.this.touch();
/* 153 */       return this;
/*     */     }
/*     */     
/*     */     public OpenSslKeyMaterial touch(Object hint)
/*     */     {
/* 158 */       OpenSslPrivateKey.this.touch(hint);
/* 159 */       return this;
/*     */     }
/*     */     
/*     */     public boolean release()
/*     */     {
/* 164 */       if (OpenSslPrivateKey.this.release()) {
/* 165 */         releaseChain();
/* 166 */         return true;
/*     */       }
/* 168 */       return false;
/*     */     }
/*     */     
/*     */     public boolean release(int decrement)
/*     */     {
/* 173 */       if (OpenSslPrivateKey.this.release(decrement)) {
/* 174 */         releaseChain();
/* 175 */         return true;
/*     */       }
/* 177 */       return false;
/*     */     }
/*     */     
/*     */     private void releaseChain() {
/* 181 */       SSL.freeX509Chain(this.certificateChain);
/* 182 */       this.certificateChain = 0L;
/*     */     }
/*     */     
/*     */     public int refCnt()
/*     */     {
/* 187 */       return OpenSslPrivateKey.this.refCnt();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslPrivateKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */