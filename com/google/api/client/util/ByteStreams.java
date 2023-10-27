/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ByteStreams
/*     */ {
/*     */   private static final int BUF_SIZE = 4096;
/*     */   
/*     */   public static long copy(InputStream from, OutputStream to)
/*     */     throws IOException
/*     */   {
/*  46 */     Preconditions.checkNotNull(from);
/*  47 */     Preconditions.checkNotNull(to);
/*  48 */     byte[] buf = new byte['က'];
/*  49 */     long total = 0L;
/*     */     for (;;) {
/*  51 */       int r = from.read(buf);
/*  52 */       if (r == -1) {
/*     */         break;
/*     */       }
/*  55 */       to.write(buf, 0, r);
/*  56 */       total += r;
/*     */     }
/*  58 */     return total;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static InputStream limit(InputStream in, long limit)
/*     */   {
/*  69 */     return new LimitedInputStream(in, limit);
/*     */   }
/*     */   
/*     */   private static final class LimitedInputStream extends FilterInputStream
/*     */   {
/*     */     private long left;
/*  75 */     private long mark = -1L;
/*     */     
/*     */     LimitedInputStream(InputStream in, long limit) {
/*  78 */       super();
/*  79 */       Preconditions.checkNotNull(in);
/*  80 */       Preconditions.checkArgument(limit >= 0L, "limit must be non-negative");
/*  81 */       this.left = limit;
/*     */     }
/*     */     
/*     */     public int available() throws IOException
/*     */     {
/*  86 */       return (int)Math.min(this.in.available(), this.left);
/*     */     }
/*     */     
/*     */ 
/*     */     public synchronized void mark(int readLimit)
/*     */     {
/*  92 */       this.in.mark(readLimit);
/*  93 */       this.mark = this.left;
/*     */     }
/*     */     
/*     */     public int read() throws IOException
/*     */     {
/*  98 */       if (this.left == 0L) {
/*  99 */         return -1;
/*     */       }
/*     */       
/* 102 */       int result = this.in.read();
/* 103 */       if (result != -1) {
/* 104 */         this.left -= 1L;
/*     */       }
/* 106 */       return result;
/*     */     }
/*     */     
/*     */     public int read(byte[] b, int off, int len) throws IOException
/*     */     {
/* 111 */       if (this.left == 0L) {
/* 112 */         return -1;
/*     */       }
/*     */       
/* 115 */       len = (int)Math.min(len, this.left);
/* 116 */       int result = this.in.read(b, off, len);
/* 117 */       if (result != -1) {
/* 118 */         this.left -= result;
/*     */       }
/* 120 */       return result;
/*     */     }
/*     */     
/*     */     public synchronized void reset() throws IOException
/*     */     {
/* 125 */       if (!this.in.markSupported()) {
/* 126 */         throw new IOException("Mark not supported");
/*     */       }
/* 128 */       if (this.mark == -1L) {
/* 129 */         throw new IOException("Mark not set");
/*     */       }
/*     */       
/* 132 */       this.in.reset();
/* 133 */       this.left = this.mark;
/*     */     }
/*     */     
/*     */     public long skip(long n) throws IOException
/*     */     {
/* 138 */       n = Math.min(n, this.left);
/* 139 */       long skipped = this.in.skip(n);
/* 140 */       this.left -= skipped;
/* 141 */       return skipped;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int read(InputStream in, byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 174 */     Preconditions.checkNotNull(in);
/* 175 */     Preconditions.checkNotNull(b);
/* 176 */     if (len < 0) {
/* 177 */       throw new IndexOutOfBoundsException("len is negative");
/*     */     }
/* 179 */     int total = 0;
/* 180 */     while (total < len) {
/* 181 */       int result = in.read(b, off + total, len - total);
/* 182 */       if (result == -1) {
/*     */         break;
/*     */       }
/* 185 */       total += result;
/*     */     }
/* 187 */     return total;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\ByteStreams.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */