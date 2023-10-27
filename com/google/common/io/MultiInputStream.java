/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @GwtIncompatible
/*     */ final class MultiInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private Iterator<? extends ByteSource> it;
/*     */   private InputStream in;
/*     */   
/*     */   public MultiInputStream(Iterator<? extends ByteSource> it)
/*     */     throws IOException
/*     */   {
/*  44 */     this.it = ((Iterator)Preconditions.checkNotNull(it));
/*  45 */     advance();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 6	com/google/common/io/MultiInputStream:in	Ljava/io/InputStream;
/*     */     //   4: ifnull +26 -> 30
/*     */     //   7: aload_0
/*     */     //   8: getfield 6	com/google/common/io/MultiInputStream:in	Ljava/io/InputStream;
/*     */     //   11: invokevirtual 7	java/io/InputStream:close	()V
/*     */     //   14: aload_0
/*     */     //   15: aconst_null
/*     */     //   16: putfield 6	com/google/common/io/MultiInputStream:in	Ljava/io/InputStream;
/*     */     //   19: goto +11 -> 30
/*     */     //   22: astore_1
/*     */     //   23: aload_0
/*     */     //   24: aconst_null
/*     */     //   25: putfield 6	com/google/common/io/MultiInputStream:in	Ljava/io/InputStream;
/*     */     //   28: aload_1
/*     */     //   29: athrow
/*     */     //   30: return
/*     */     // Line number table:
/*     */     //   Java source line #50	-> byte code offset #0
/*     */     //   Java source line #52	-> byte code offset #7
/*     */     //   Java source line #54	-> byte code offset #14
/*     */     //   Java source line #55	-> byte code offset #19
/*     */     //   Java source line #54	-> byte code offset #22
/*     */     //   Java source line #55	-> byte code offset #28
/*     */     //   Java source line #57	-> byte code offset #30
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	MultiInputStream
/*     */     //   22	7	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	14	22	finally
/*     */   }
/*     */   
/*     */   private void advance()
/*     */     throws IOException
/*     */   {
/*  61 */     close();
/*  62 */     if (this.it.hasNext()) {
/*  63 */       this.in = ((ByteSource)this.it.next()).openStream();
/*     */     }
/*     */   }
/*     */   
/*     */   public int available() throws IOException
/*     */   {
/*  69 */     if (this.in == null) {
/*  70 */       return 0;
/*     */     }
/*  72 */     return this.in.available();
/*     */   }
/*     */   
/*     */   public boolean markSupported()
/*     */   {
/*  77 */     return false;
/*     */   }
/*     */   
/*     */   public int read() throws IOException
/*     */   {
/*  82 */     while (this.in != null) {
/*  83 */       int result = this.in.read();
/*  84 */       if (result != -1) {
/*  85 */         return result;
/*     */       }
/*  87 */       advance();
/*     */     }
/*  89 */     return -1;
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException
/*     */   {
/*  94 */     while (this.in != null) {
/*  95 */       int result = this.in.read(b, off, len);
/*  96 */       if (result != -1) {
/*  97 */         return result;
/*     */       }
/*  99 */       advance();
/*     */     }
/* 101 */     return -1;
/*     */   }
/*     */   
/*     */   public long skip(long n) throws IOException
/*     */   {
/* 106 */     if ((this.in == null) || (n <= 0L)) {
/* 107 */       return 0L;
/*     */     }
/* 109 */     long result = this.in.skip(n);
/* 110 */     if (result != 0L) {
/* 111 */       return result;
/*     */     }
/* 113 */     if (read() == -1) {
/* 114 */       return 0L;
/*     */     }
/* 116 */     return 1L + this.in.skip(n - 1L);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\io\MultiInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */