/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import javax.crypto.Mac;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Immutable
/*     */ final class MacHashFunction
/*     */   extends AbstractHashFunction
/*     */ {
/*     */   private final Mac prototype;
/*     */   private final Key key;
/*     */   private final String toString;
/*     */   private final int bits;
/*     */   private final boolean supportsClone;
/*     */   
/*     */   MacHashFunction(String algorithmName, Key key, String toString)
/*     */   {
/*  44 */     this.prototype = getMac(algorithmName, key);
/*  45 */     this.key = ((Key)Preconditions.checkNotNull(key));
/*  46 */     this.toString = ((String)Preconditions.checkNotNull(toString));
/*  47 */     this.bits = (this.prototype.getMacLength() * 8);
/*  48 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */   
/*     */   public int bits()
/*     */   {
/*  53 */     return this.bits;
/*     */   }
/*     */   
/*     */   private static boolean supportsClone(Mac mac) {
/*     */     try {
/*  58 */       mac.clone();
/*  59 */       return true;
/*     */     } catch (CloneNotSupportedException e) {}
/*  61 */     return false;
/*     */   }
/*     */   
/*     */   private static Mac getMac(String algorithmName, Key key)
/*     */   {
/*     */     try {
/*  67 */       Mac mac = Mac.getInstance(algorithmName);
/*  68 */       mac.init(key);
/*  69 */       return mac;
/*     */     } catch (NoSuchAlgorithmException e) {
/*  71 */       throw new IllegalStateException(e);
/*     */     } catch (InvalidKeyException e) {
/*  73 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Hasher newHasher()
/*     */   {
/*  79 */     if (this.supportsClone) {
/*     */       try {
/*  81 */         return new MacHasher((Mac)this.prototype.clone(), null);
/*     */       }
/*     */       catch (CloneNotSupportedException localCloneNotSupportedException) {}
/*     */     }
/*     */     
/*  86 */     return new MacHasher(getMac(this.prototype.getAlgorithm(), this.key), null);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  91 */     return this.toString;
/*     */   }
/*     */   
/*     */   private static final class MacHasher extends AbstractByteHasher
/*     */   {
/*     */     private final Mac mac;
/*     */     private boolean done;
/*     */     
/*     */     private MacHasher(Mac mac) {
/* 100 */       this.mac = mac;
/*     */     }
/*     */     
/*     */     protected void update(byte b)
/*     */     {
/* 105 */       checkNotDone();
/* 106 */       this.mac.update(b);
/*     */     }
/*     */     
/*     */     protected void update(byte[] b)
/*     */     {
/* 111 */       checkNotDone();
/* 112 */       this.mac.update(b);
/*     */     }
/*     */     
/*     */     protected void update(byte[] b, int off, int len)
/*     */     {
/* 117 */       checkNotDone();
/* 118 */       this.mac.update(b, off, len);
/*     */     }
/*     */     
/*     */     protected void update(ByteBuffer bytes)
/*     */     {
/* 123 */       checkNotDone();
/* 124 */       Preconditions.checkNotNull(bytes);
/* 125 */       this.mac.update(bytes);
/*     */     }
/*     */     
/*     */     private void checkNotDone() {
/* 129 */       Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
/*     */     }
/*     */     
/*     */     public HashCode hash()
/*     */     {
/* 134 */       checkNotDone();
/* 135 */       this.done = true;
/* 136 */       return HashCode.fromBytesNoCopy(this.mac.doFinal());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\MacHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */