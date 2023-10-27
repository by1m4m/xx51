/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @CanIgnoreReturnValue
/*     */ abstract class AbstractByteHasher
/*     */   extends AbstractHasher
/*     */ {
/*  36 */   private final ByteBuffer scratch = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
/*     */   
/*     */ 
/*     */   protected abstract void update(byte paramByte);
/*     */   
/*     */   protected void update(byte[] b)
/*     */   {
/*  43 */     update(b, 0, b.length);
/*     */   }
/*     */   
/*     */   protected void update(byte[] b, int off, int len)
/*     */   {
/*  48 */     for (int i = off; i < off + len; i++) {
/*  49 */       update(b[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void update(ByteBuffer b)
/*     */   {
/*  55 */     if (b.hasArray()) {
/*  56 */       update(b.array(), b.arrayOffset() + b.position(), b.remaining());
/*  57 */       b.position(b.limit());
/*     */     } else {
/*  59 */       for (int remaining = b.remaining(); remaining > 0; remaining--) {
/*  60 */         update(b.get());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private Hasher update(int bytes)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_0
/*     */     //   2: getfield 5	com/google/common/hash/AbstractByteHasher:scratch	Ljava/nio/ByteBuffer;
/*     */     //   5: invokevirtual 9	java/nio/ByteBuffer:array	()[B
/*     */     //   8: iconst_0
/*     */     //   9: iload_1
/*     */     //   10: invokevirtual 6	com/google/common/hash/AbstractByteHasher:update	([BII)V
/*     */     //   13: aload_0
/*     */     //   14: getfield 5	com/google/common/hash/AbstractByteHasher:scratch	Ljava/nio/ByteBuffer;
/*     */     //   17: invokevirtual 16	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
/*     */     //   20: pop
/*     */     //   21: goto +14 -> 35
/*     */     //   24: astore_2
/*     */     //   25: aload_0
/*     */     //   26: getfield 5	com/google/common/hash/AbstractByteHasher:scratch	Ljava/nio/ByteBuffer;
/*     */     //   29: invokevirtual 16	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
/*     */     //   32: pop
/*     */     //   33: aload_2
/*     */     //   34: athrow
/*     */     //   35: aload_0
/*     */     //   36: areturn
/*     */     // Line number table:
/*     */     //   Java source line #68	-> byte code offset #0
/*     */     //   Java source line #70	-> byte code offset #13
/*     */     //   Java source line #71	-> byte code offset #21
/*     */     //   Java source line #70	-> byte code offset #24
/*     */     //   Java source line #71	-> byte code offset #33
/*     */     //   Java source line #72	-> byte code offset #35
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	37	0	this	AbstractByteHasher
/*     */     //   0	37	1	bytes	int
/*     */     //   24	10	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	13	24	finally
/*     */   }
/*     */   
/*     */   public Hasher putByte(byte b)
/*     */   {
/*  77 */     update(b);
/*  78 */     return this;
/*     */   }
/*     */   
/*     */   public Hasher putBytes(byte[] bytes)
/*     */   {
/*  83 */     Preconditions.checkNotNull(bytes);
/*  84 */     update(bytes);
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public Hasher putBytes(byte[] bytes, int off, int len)
/*     */   {
/*  90 */     Preconditions.checkPositionIndexes(off, off + len, bytes.length);
/*  91 */     update(bytes, off, len);
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public Hasher putBytes(ByteBuffer bytes)
/*     */   {
/*  97 */     update(bytes);
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public Hasher putShort(short s)
/*     */   {
/* 103 */     this.scratch.putShort(s);
/* 104 */     return update(2);
/*     */   }
/*     */   
/*     */   public Hasher putInt(int i)
/*     */   {
/* 109 */     this.scratch.putInt(i);
/* 110 */     return update(4);
/*     */   }
/*     */   
/*     */   public Hasher putLong(long l)
/*     */   {
/* 115 */     this.scratch.putLong(l);
/* 116 */     return update(8);
/*     */   }
/*     */   
/*     */   public Hasher putChar(char c)
/*     */   {
/* 121 */     this.scratch.putChar(c);
/* 122 */     return update(2);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\AbstractByteHasher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */