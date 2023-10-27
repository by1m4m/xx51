/*    */ package com.google.common.hash;
/*    */ 
/*    */ import com.google.common.base.Preconditions;
/*    */ import com.google.errorprone.annotations.Immutable;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.charset.Charset;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Immutable
/*    */ abstract class AbstractCompositeHashFunction
/*    */   extends AbstractHashFunction
/*    */ {
/*    */   final HashFunction[] functions;
/*    */   private static final long serialVersionUID = 0L;
/*    */   
/*    */   AbstractCompositeHashFunction(HashFunction... functions)
/*    */   {
/* 38 */     for (HashFunction function : functions) {
/* 39 */       Preconditions.checkNotNull(function);
/*    */     }
/* 41 */     this.functions = functions;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   abstract HashCode makeHash(Hasher[] paramArrayOfHasher);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Hasher newHasher()
/*    */   {
/* 54 */     Hasher[] hashers = new Hasher[this.functions.length];
/* 55 */     for (int i = 0; i < hashers.length; i++) {
/* 56 */       hashers[i] = this.functions[i].newHasher();
/*    */     }
/* 58 */     return fromHashers(hashers);
/*    */   }
/*    */   
/*    */   public Hasher newHasher(int expectedInputSize)
/*    */   {
/* 63 */     Preconditions.checkArgument(expectedInputSize >= 0);
/* 64 */     Hasher[] hashers = new Hasher[this.functions.length];
/* 65 */     for (int i = 0; i < hashers.length; i++) {
/* 66 */       hashers[i] = this.functions[i].newHasher(expectedInputSize);
/*    */     }
/* 68 */     return fromHashers(hashers);
/*    */   }
/*    */   
/*    */   private Hasher fromHashers(final Hasher[] hashers) {
/* 72 */     new Hasher()
/*    */     {
/*    */       public Hasher putByte(byte b) {
/* 75 */         for (Hasher hasher : hashers) {
/* 76 */           hasher.putByte(b);
/*    */         }
/* 78 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putBytes(byte[] bytes)
/*    */       {
/* 83 */         for (Hasher hasher : hashers) {
/* 84 */           hasher.putBytes(bytes);
/*    */         }
/* 86 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putBytes(byte[] bytes, int off, int len)
/*    */       {
/* 91 */         for (Hasher hasher : hashers) {
/* 92 */           hasher.putBytes(bytes, off, len);
/*    */         }
/* 94 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putBytes(ByteBuffer bytes)
/*    */       {
/* 99 */         int pos = bytes.position();
/* :0 */         for (Hasher hasher : hashers) {
/* :1 */           bytes.position(pos);
/* :2 */           hasher.putBytes(bytes);
/*    */         }
/* :4 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putShort(short s)
/*    */       {
/* :9 */         for (Hasher hasher : hashers) {
/* ;0 */           hasher.putShort(s);
/*    */         }
/* ;2 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putInt(int i)
/*    */       {
/* ;7 */         for (Hasher hasher : hashers) {
/* ;8 */           hasher.putInt(i);
/*    */         }
/* <0 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putLong(long l)
/*    */       {
/* <5 */         for (Hasher hasher : hashers) {
/* <6 */           hasher.putLong(l);
/*    */         }
/* <8 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putFloat(float f)
/*    */       {
/* =3 */         for (Hasher hasher : hashers) {
/* =4 */           hasher.putFloat(f);
/*    */         }
/* =6 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putDouble(double d)
/*    */       {
/* >1 */         for (Hasher hasher : hashers) {
/* >2 */           hasher.putDouble(d);
/*    */         }
/* >4 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putBoolean(boolean b)
/*    */       {
/* >9 */         for (Hasher hasher : hashers) {
/* ?0 */           hasher.putBoolean(b);
/*    */         }
/* ?2 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putChar(char c)
/*    */       {
/* ?7 */         for (Hasher hasher : hashers) {
/* ?8 */           hasher.putChar(c);
/*    */         }
/* @0 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putUnencodedChars(CharSequence chars)
/*    */       {
/* @5 */         for (Hasher hasher : hashers) {
/* @6 */           hasher.putUnencodedChars(chars);
/*    */         }
/* @8 */         return this;
/*    */       }
/*    */       
/*    */       public Hasher putString(CharSequence chars, Charset charset)
/*    */       {
/* A3 */         for (Hasher hasher : hashers) {
/* A4 */           hasher.putString(chars, charset);
/*    */         }
/* A6 */         return this;
/*    */       }
/*    */       
/*    */       public <T> Hasher putObject(T instance, Funnel<? super T> funnel)
/*    */       {
/* B1 */         for (Hasher hasher : hashers) {
/* B2 */           hasher.putObject(instance, funnel);
/*    */         }
/* B4 */         return this;
/*    */       }
/*    */       
/*    */       public HashCode hash()
/*    */       {
/* B9 */         return AbstractCompositeHashFunction.this.makeHash(hashers);
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\AbstractCompositeHashFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */