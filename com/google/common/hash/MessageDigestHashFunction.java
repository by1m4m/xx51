/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.Immutable;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ final class MessageDigestHashFunction
/*     */   extends AbstractHashFunction
/*     */   implements Serializable
/*     */ {
/*     */   private final MessageDigest prototype;
/*     */   private final int bytes;
/*     */   private final boolean supportsClone;
/*     */   private final String toString;
/*     */   
/*     */   MessageDigestHashFunction(String algorithmName, String toString)
/*     */   {
/*  44 */     this.prototype = getMessageDigest(algorithmName);
/*  45 */     this.bytes = this.prototype.getDigestLength();
/*  46 */     this.toString = ((String)Preconditions.checkNotNull(toString));
/*  47 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */   
/*     */   MessageDigestHashFunction(String algorithmName, int bytes, String toString) {
/*  51 */     this.toString = ((String)Preconditions.checkNotNull(toString));
/*  52 */     this.prototype = getMessageDigest(algorithmName);
/*  53 */     int maxLength = this.prototype.getDigestLength();
/*  54 */     Preconditions.checkArgument((bytes >= 4) && (bytes <= maxLength), "bytes (%s) must be >= 4 and < %s", bytes, maxLength);
/*     */     
/*  56 */     this.bytes = bytes;
/*  57 */     this.supportsClone = supportsClone(this.prototype);
/*     */   }
/*     */   
/*     */   private static boolean supportsClone(MessageDigest digest) {
/*     */     try {
/*  62 */       digest.clone();
/*  63 */       return true;
/*     */     } catch (CloneNotSupportedException e) {}
/*  65 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int bits()
/*     */   {
/*  71 */     return this.bytes * 8;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  76 */     return this.toString;
/*     */   }
/*     */   
/*     */   private static MessageDigest getMessageDigest(String algorithmName) {
/*     */     try {
/*  81 */       return MessageDigest.getInstance(algorithmName);
/*     */     } catch (NoSuchAlgorithmException e) {
/*  83 */       throw new AssertionError(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Hasher newHasher()
/*     */   {
/*  89 */     if (this.supportsClone) {
/*     */       try {
/*  91 */         return new MessageDigestHasher((MessageDigest)this.prototype.clone(), this.bytes, null);
/*     */       }
/*     */       catch (CloneNotSupportedException localCloneNotSupportedException) {}
/*     */     }
/*     */     
/*  96 */     return new MessageDigestHasher(getMessageDigest(this.prototype.getAlgorithm()), this.bytes, null);
/*     */   }
/*     */   
/*     */   private static final class SerializedForm implements Serializable {
/*     */     private final String algorithmName;
/*     */     private final int bytes;
/*     */     private final String toString;
/*     */     private static final long serialVersionUID = 0L;
/*     */     
/* 105 */     private SerializedForm(String algorithmName, int bytes, String toString) { this.algorithmName = algorithmName;
/* 106 */       this.bytes = bytes;
/* 107 */       this.toString = toString;
/*     */     }
/*     */     
/*     */     private Object readResolve() {
/* 111 */       return new MessageDigestHashFunction(this.algorithmName, this.bytes, this.toString);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 118 */     return new SerializedForm(this.prototype.getAlgorithm(), this.bytes, this.toString, null);
/*     */   }
/*     */   
/*     */   private static final class MessageDigestHasher extends AbstractByteHasher
/*     */   {
/*     */     private final MessageDigest digest;
/*     */     private final int bytes;
/*     */     private boolean done;
/*     */     
/*     */     private MessageDigestHasher(MessageDigest digest, int bytes) {
/* 128 */       this.digest = digest;
/* 129 */       this.bytes = bytes;
/*     */     }
/*     */     
/*     */     protected void update(byte b)
/*     */     {
/* 134 */       checkNotDone();
/* 135 */       this.digest.update(b);
/*     */     }
/*     */     
/*     */     protected void update(byte[] b, int off, int len)
/*     */     {
/* 140 */       checkNotDone();
/* 141 */       this.digest.update(b, off, len);
/*     */     }
/*     */     
/*     */     protected void update(ByteBuffer bytes)
/*     */     {
/* 146 */       checkNotDone();
/* 147 */       this.digest.update(bytes);
/*     */     }
/*     */     
/*     */     private void checkNotDone() {
/* 151 */       Preconditions.checkState(!this.done, "Cannot re-use a Hasher after calling hash() on it");
/*     */     }
/*     */     
/*     */     public HashCode hash()
/*     */     {
/* 156 */       checkNotDone();
/* 157 */       this.done = true;
/* 158 */       return this.bytes == this.digest.getDigestLength() ? 
/* 159 */         HashCode.fromBytesNoCopy(this.digest.digest()) : 
/* 160 */         HashCode.fromBytesNoCopy(Arrays.copyOf(this.digest.digest(), this.bytes));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\MessageDigestHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */