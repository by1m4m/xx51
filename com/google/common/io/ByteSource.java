/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Ascii;
/*     */ import com.google.common.base.Optional;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.hash.Funnels;
/*     */ import com.google.common.hash.HashCode;
/*     */ import com.google.common.hash.HashFunction;
/*     */ import com.google.common.hash.Hasher;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ public abstract class ByteSource
/*     */ {
/*     */   public CharSource asCharSource(Charset charset)
/*     */   {
/*  79 */     return new AsCharSource(charset);
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
/*     */   public abstract InputStream openStream()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public InputStream openBufferedStream()
/*     */     throws IOException
/*     */   {
/* 105 */     InputStream in = openStream();
/* 106 */     return (in instanceof BufferedInputStream) ? (BufferedInputStream)in : new BufferedInputStream(in);
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
/*     */   public ByteSource slice(long offset, long length)
/*     */   {
/* 121 */     return new SlicedByteSource(offset, length);
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
/*     */   public boolean isEmpty()
/*     */     throws IOException
/*     */   {
/* 138 */     Optional<Long> sizeIfKnown = sizeIfKnown();
/* 139 */     if (sizeIfKnown.isPresent()) {
/* 140 */       return ((Long)sizeIfKnown.get()).longValue() == 0L;
/*     */     }
/* 142 */     Closer closer = Closer.create();
/*     */     try {
/* 144 */       InputStream in = (InputStream)closer.register(openStream());
/* 145 */       return in.read() == -1;
/*     */     } catch (Throwable e) {
/* 147 */       throw closer.rethrow(e);
/*     */     } finally {
/* 149 */       closer.close();
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
/*     */   @Beta
/*     */   public Optional<Long> sizeIfKnown()
/*     */   {
/* 169 */     return Optional.absent();
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
/*     */   public long size()
/*     */     throws IOException
/*     */   {
/* 192 */     Optional<Long> sizeIfKnown = sizeIfKnown();
/* 193 */     if (sizeIfKnown.isPresent()) {
/* 194 */       return ((Long)sizeIfKnown.get()).longValue();
/*     */     }
/*     */     
/* 197 */     Closer closer = Closer.create();
/*     */     long l;
/* 199 */     try { InputStream in = (InputStream)closer.register(openStream());
/* 200 */       return countBySkipping(in);
/*     */     }
/*     */     catch (IOException localIOException) {}finally
/*     */     {
/* 204 */       closer.close();
/*     */     }
/*     */     
/* 207 */     closer = Closer.create();
/*     */     try {
/* 209 */       InputStream in = (InputStream)closer.register(openStream());
/* 210 */       return ByteStreams.exhaust(in);
/*     */     } catch (Throwable e) {
/* 212 */       throw closer.rethrow(e);
/*     */     } finally {
/* 214 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private long countBySkipping(InputStream in)
/*     */     throws IOException
/*     */   {
/* 223 */     long count = 0L;
/*     */     long skipped;
/* 225 */     while ((skipped = ByteStreams.skipUpTo(in, 2147483647L)) > 0L) {
/* 226 */       count += skipped;
/*     */     }
/* 228 */     return count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public long copyTo(OutputStream output)
/*     */     throws IOException
/*     */   {
/* 241 */     Preconditions.checkNotNull(output);
/*     */     
/* 243 */     Closer closer = Closer.create();
/*     */     try {
/* 245 */       InputStream in = (InputStream)closer.register(openStream());
/* 246 */       return ByteStreams.copy(in, output);
/*     */     } catch (Throwable e) {
/* 248 */       throw closer.rethrow(e);
/*     */     } finally {
/* 250 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public long copyTo(ByteSink sink)
/*     */     throws IOException
/*     */   {
/* 263 */     Preconditions.checkNotNull(sink);
/*     */     
/* 265 */     Closer closer = Closer.create();
/*     */     try {
/* 267 */       InputStream in = (InputStream)closer.register(openStream());
/* 268 */       OutputStream out = (OutputStream)closer.register(sink.openStream());
/* 269 */       return ByteStreams.copy(in, out);
/*     */     } catch (Throwable e) {
/* 271 */       throw closer.rethrow(e);
/*     */     } finally {
/* 273 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] read()
/*     */     throws IOException
/*     */   {
/* 283 */     Closer closer = Closer.create();
/*     */     try {
/* 285 */       InputStream in = (InputStream)closer.register(openStream());
/* 286 */       Optional<Long> size = sizeIfKnown();
/* 287 */       return size.isPresent() ? 
/* 288 */         ByteStreams.toByteArray(in, ((Long)size.get()).longValue()) : 
/* 289 */         ByteStreams.toByteArray(in);
/*     */     } catch (Throwable e) {
/* 291 */       throw closer.rethrow(e);
/*     */     } finally {
/* 293 */       closer.close();
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
/*     */   @Beta
/*     */   @CanIgnoreReturnValue
/*     */   public <T> T read(ByteProcessor<T> processor)
/*     */     throws IOException
/*     */   {
/* 309 */     Preconditions.checkNotNull(processor);
/*     */     
/* 311 */     Closer closer = Closer.create();
/*     */     try {
/* 313 */       InputStream in = (InputStream)closer.register(openStream());
/* 314 */       return (T)ByteStreams.readBytes(in, processor);
/*     */     } catch (Throwable e) {
/* 316 */       throw closer.rethrow(e);
/*     */     } finally {
/* 318 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HashCode hash(HashFunction hashFunction)
/*     */     throws IOException
/*     */   {
/* 328 */     Hasher hasher = hashFunction.newHasher();
/* 329 */     copyTo(Funnels.asOutputStream(hasher));
/* 330 */     return hasher.hash();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean contentEquals(ByteSource other)
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: invokestatic 29	com/google/common/base/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   4: pop
/*     */     //   5: invokestatic 40	com/google/common/io/ByteStreams:createBuffer	()[B
/*     */     //   8: astore_2
/*     */     //   9: invokestatic 40	com/google/common/io/ByteStreams:createBuffer	()[B
/*     */     //   12: astore_3
/*     */     //   13: invokestatic 14	com/google/common/io/Closer:create	()Lcom/google/common/io/Closer;
/*     */     //   16: astore 4
/*     */     //   18: aload 4
/*     */     //   20: aload_0
/*     */     //   21: invokevirtual 4	com/google/common/io/ByteSource:openStream	()Ljava/io/InputStream;
/*     */     //   24: invokevirtual 15	com/google/common/io/Closer:register	(Ljava/io/Closeable;)Ljava/io/Closeable;
/*     */     //   27: checkcast 16	java/io/InputStream
/*     */     //   30: astore 5
/*     */     //   32: aload 4
/*     */     //   34: aload_1
/*     */     //   35: invokevirtual 4	com/google/common/io/ByteSource:openStream	()Ljava/io/InputStream;
/*     */     //   38: invokevirtual 15	com/google/common/io/Closer:register	(Ljava/io/Closeable;)Ljava/io/Closeable;
/*     */     //   41: checkcast 16	java/io/InputStream
/*     */     //   44: astore 6
/*     */     //   46: aload 5
/*     */     //   48: aload_2
/*     */     //   49: iconst_0
/*     */     //   50: aload_2
/*     */     //   51: arraylength
/*     */     //   52: invokestatic 41	com/google/common/io/ByteStreams:read	(Ljava/io/InputStream;[BII)I
/*     */     //   55: istore 7
/*     */     //   57: aload 6
/*     */     //   59: aload_3
/*     */     //   60: iconst_0
/*     */     //   61: aload_3
/*     */     //   62: arraylength
/*     */     //   63: invokestatic 41	com/google/common/io/ByteStreams:read	(Ljava/io/InputStream;[BII)I
/*     */     //   66: istore 8
/*     */     //   68: iload 7
/*     */     //   70: iload 8
/*     */     //   72: if_icmpne +11 -> 83
/*     */     //   75: aload_2
/*     */     //   76: aload_3
/*     */     //   77: invokestatic 42	java/util/Arrays:equals	([B[B)Z
/*     */     //   80: ifne +14 -> 94
/*     */     //   83: iconst_0
/*     */     //   84: istore 9
/*     */     //   86: aload 4
/*     */     //   88: invokevirtual 18	com/google/common/io/Closer:close	()V
/*     */     //   91: iload 9
/*     */     //   93: ireturn
/*     */     //   94: iload 7
/*     */     //   96: aload_2
/*     */     //   97: arraylength
/*     */     //   98: if_icmpeq +14 -> 112
/*     */     //   101: iconst_1
/*     */     //   102: istore 9
/*     */     //   104: aload 4
/*     */     //   106: invokevirtual 18	com/google/common/io/Closer:close	()V
/*     */     //   109: iload 9
/*     */     //   111: ireturn
/*     */     //   112: goto -66 -> 46
/*     */     //   115: astore 5
/*     */     //   117: aload 4
/*     */     //   119: aload 5
/*     */     //   121: invokevirtual 20	com/google/common/io/Closer:rethrow	(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;
/*     */     //   124: athrow
/*     */     //   125: astore 10
/*     */     //   127: aload 4
/*     */     //   129: invokevirtual 18	com/google/common/io/Closer:close	()V
/*     */     //   132: aload 10
/*     */     //   134: athrow
/*     */     // Line number table:
/*     */     //   Java source line #340	-> byte code offset #0
/*     */     //   Java source line #342	-> byte code offset #5
/*     */     //   Java source line #343	-> byte code offset #9
/*     */     //   Java source line #345	-> byte code offset #13
/*     */     //   Java source line #347	-> byte code offset #18
/*     */     //   Java source line #348	-> byte code offset #32
/*     */     //   Java source line #350	-> byte code offset #46
/*     */     //   Java source line #351	-> byte code offset #57
/*     */     //   Java source line #352	-> byte code offset #68
/*     */     //   Java source line #353	-> byte code offset #83
/*     */     //   Java source line #361	-> byte code offset #86
/*     */     //   Java source line #353	-> byte code offset #91
/*     */     //   Java source line #354	-> byte code offset #94
/*     */     //   Java source line #355	-> byte code offset #101
/*     */     //   Java source line #361	-> byte code offset #104
/*     */     //   Java source line #355	-> byte code offset #109
/*     */     //   Java source line #357	-> byte code offset #112
/*     */     //   Java source line #358	-> byte code offset #115
/*     */     //   Java source line #359	-> byte code offset #117
/*     */     //   Java source line #361	-> byte code offset #125
/*     */     //   Java source line #362	-> byte code offset #132
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	135	0	this	ByteSource
/*     */     //   0	135	1	other	ByteSource
/*     */     //   8	89	2	buf1	byte[]
/*     */     //   12	65	3	buf2	byte[]
/*     */     //   16	112	4	closer	Closer
/*     */     //   30	17	5	in1	InputStream
/*     */     //   115	5	5	e	Throwable
/*     */     //   44	14	6	in2	InputStream
/*     */     //   55	40	7	read1	int
/*     */     //   66	5	8	read2	int
/*     */     //   84	26	9	bool	boolean
/*     */     //   125	8	10	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   18	86	115	java/lang/Throwable
/*     */     //   94	104	115	java/lang/Throwable
/*     */     //   112	115	115	java/lang/Throwable
/*     */     //   18	86	125	finally
/*     */     //   94	104	125	finally
/*     */     //   112	127	125	finally
/*     */   }
/*     */   
/*     */   public static ByteSource concat(Iterable<? extends ByteSource> sources)
/*     */   {
/* 377 */     return new ConcatenatedByteSource(sources);
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
/*     */   public static ByteSource concat(Iterator<? extends ByteSource> sources)
/*     */   {
/* 399 */     return concat(ImmutableList.copyOf(sources));
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
/*     */   public static ByteSource concat(ByteSource... sources)
/*     */   {
/* 415 */     return concat(ImmutableList.copyOf(sources));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteSource wrap(byte[] b)
/*     */   {
/* 425 */     return new ByteArrayByteSource(b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteSource empty()
/*     */   {
/* 434 */     return EmptyByteSource.INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */   class AsCharSource
/*     */     extends CharSource
/*     */   {
/*     */     final Charset charset;
/*     */     
/*     */     AsCharSource(Charset charset)
/*     */     {
/* 445 */       this.charset = ((Charset)Preconditions.checkNotNull(charset));
/*     */     }
/*     */     
/*     */     public ByteSource asByteSource(Charset charset)
/*     */     {
/* 450 */       if (charset.equals(this.charset)) {
/* 451 */         return ByteSource.this;
/*     */       }
/* 453 */       return super.asByteSource(charset);
/*     */     }
/*     */     
/*     */     public Reader openStream() throws IOException
/*     */     {
/* 458 */       return new InputStreamReader(ByteSource.this.openStream(), this.charset);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String read()
/*     */       throws IOException
/*     */     {
/* 470 */       return new String(ByteSource.this.read(), this.charset);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 475 */       return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private final class SlicedByteSource extends ByteSource
/*     */   {
/*     */     final long offset;
/*     */     final long length;
/*     */     
/*     */     SlicedByteSource(long offset, long length)
/*     */     {
/* 486 */       Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
/* 487 */       Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
/* 488 */       this.offset = offset;
/* 489 */       this.length = length;
/*     */     }
/*     */     
/*     */     public InputStream openStream() throws IOException
/*     */     {
/* 494 */       return sliceStream(ByteSource.this.openStream());
/*     */     }
/*     */     
/*     */     public InputStream openBufferedStream() throws IOException
/*     */     {
/* 499 */       return sliceStream(ByteSource.this.openBufferedStream());
/*     */     }
/*     */     
/*     */     private InputStream sliceStream(InputStream in) throws IOException {
/* 503 */       if (this.offset > 0L)
/*     */       {
/*     */         try {
/* 506 */           skipped = ByteStreams.skipUpTo(in, this.offset);
/*     */         } catch (Throwable e) { long skipped;
/* 508 */           Closer closer = Closer.create();
/* 509 */           closer.register(in);
/*     */           try {
/* 511 */             throw closer.rethrow(e);
/*     */           } finally {
/* 513 */             closer.close();
/*     */           }
/*     */         }
/*     */         long skipped;
/* 517 */         if (skipped < this.offset)
/*     */         {
/* 519 */           in.close();
/* 520 */           return new ByteArrayInputStream(new byte[0]);
/*     */         }
/*     */       }
/* 523 */       return ByteStreams.limit(in, this.length);
/*     */     }
/*     */     
/*     */     public ByteSource slice(long offset, long length)
/*     */     {
/* 528 */       Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
/* 529 */       Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
/* 530 */       long maxLength = this.length - offset;
/* 531 */       return ByteSource.this.slice(this.offset + offset, Math.min(length, maxLength));
/*     */     }
/*     */     
/*     */     public boolean isEmpty() throws IOException
/*     */     {
/* 536 */       return (this.length == 0L) || (super.isEmpty());
/*     */     }
/*     */     
/*     */     public Optional<Long> sizeIfKnown()
/*     */     {
/* 541 */       Optional<Long> optionalUnslicedSize = ByteSource.this.sizeIfKnown();
/* 542 */       if (optionalUnslicedSize.isPresent()) {
/* 543 */         long unslicedSize = ((Long)optionalUnslicedSize.get()).longValue();
/* 544 */         long off = Math.min(this.offset, unslicedSize);
/* 545 */         return Optional.of(Long.valueOf(Math.min(this.length, unslicedSize - off)));
/*     */       }
/* 547 */       return Optional.absent();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 552 */       return ByteSource.this.toString() + ".slice(" + this.offset + ", " + this.length + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ByteArrayByteSource extends ByteSource
/*     */   {
/*     */     final byte[] bytes;
/*     */     final int offset;
/*     */     final int length;
/*     */     
/*     */     ByteArrayByteSource(byte[] bytes) {
/* 563 */       this(bytes, 0, bytes.length);
/*     */     }
/*     */     
/*     */     ByteArrayByteSource(byte[] bytes, int offset, int length)
/*     */     {
/* 568 */       this.bytes = bytes;
/* 569 */       this.offset = offset;
/* 570 */       this.length = length;
/*     */     }
/*     */     
/*     */     public InputStream openStream()
/*     */     {
/* 575 */       return new ByteArrayInputStream(this.bytes, this.offset, this.length);
/*     */     }
/*     */     
/*     */     public InputStream openBufferedStream() throws IOException
/*     */     {
/* 580 */       return openStream();
/*     */     }
/*     */     
/*     */     public boolean isEmpty()
/*     */     {
/* 585 */       return this.length == 0;
/*     */     }
/*     */     
/*     */     public long size()
/*     */     {
/* 590 */       return this.length;
/*     */     }
/*     */     
/*     */     public Optional<Long> sizeIfKnown()
/*     */     {
/* 595 */       return Optional.of(Long.valueOf(this.length));
/*     */     }
/*     */     
/*     */     public byte[] read()
/*     */     {
/* 600 */       return Arrays.copyOfRange(this.bytes, this.offset, this.offset + this.length);
/*     */     }
/*     */     
/*     */     public <T> T read(ByteProcessor<T> processor)
/*     */       throws IOException
/*     */     {
/* 606 */       processor.processBytes(this.bytes, this.offset, this.length);
/* 607 */       return (T)processor.getResult();
/*     */     }
/*     */     
/*     */     public long copyTo(OutputStream output) throws IOException
/*     */     {
/* 612 */       output.write(this.bytes, this.offset, this.length);
/* 613 */       return this.length;
/*     */     }
/*     */     
/*     */     public HashCode hash(HashFunction hashFunction) throws IOException
/*     */     {
/* 618 */       return hashFunction.hashBytes(this.bytes, this.offset, this.length);
/*     */     }
/*     */     
/*     */     public ByteSource slice(long offset, long length)
/*     */     {
/* 623 */       Preconditions.checkArgument(offset >= 0L, "offset (%s) may not be negative", offset);
/* 624 */       Preconditions.checkArgument(length >= 0L, "length (%s) may not be negative", length);
/*     */       
/* 626 */       offset = Math.min(offset, this.length);
/* 627 */       length = Math.min(length, this.length - offset);
/* 628 */       int newOffset = this.offset + (int)offset;
/* 629 */       return new ByteArrayByteSource(this.bytes, newOffset, (int)length);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 634 */       return 
/* 635 */         "ByteSource.wrap(" + Ascii.truncate(BaseEncoding.base16().encode(this.bytes, this.offset, this.length), 30, "...") + ")";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyByteSource
/*     */     extends ByteSource.ByteArrayByteSource
/*     */   {
/* 642 */     static final EmptyByteSource INSTANCE = new EmptyByteSource();
/*     */     
/*     */     EmptyByteSource() {
/* 645 */       super();
/*     */     }
/*     */     
/*     */     public CharSource asCharSource(Charset charset)
/*     */     {
/* 650 */       Preconditions.checkNotNull(charset);
/* 651 */       return CharSource.empty();
/*     */     }
/*     */     
/*     */     public byte[] read()
/*     */     {
/* 656 */       return this.bytes;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 661 */       return "ByteSource.empty()";
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ConcatenatedByteSource extends ByteSource
/*     */   {
/*     */     final Iterable<? extends ByteSource> sources;
/*     */     
/*     */     ConcatenatedByteSource(Iterable<? extends ByteSource> sources) {
/* 670 */       this.sources = ((Iterable)Preconditions.checkNotNull(sources));
/*     */     }
/*     */     
/*     */     public InputStream openStream() throws IOException
/*     */     {
/* 675 */       return new MultiInputStream(this.sources.iterator());
/*     */     }
/*     */     
/*     */     public boolean isEmpty() throws IOException
/*     */     {
/* 680 */       for (ByteSource source : this.sources) {
/* 681 */         if (!source.isEmpty()) {
/* 682 */           return false;
/*     */         }
/*     */       }
/* 685 */       return true;
/*     */     }
/*     */     
/*     */     public Optional<Long> sizeIfKnown()
/*     */     {
/* 690 */       if (!(this.sources instanceof Collection))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 696 */         return Optional.absent();
/*     */       }
/* 698 */       long result = 0L;
/* 699 */       for (ByteSource source : this.sources) {
/* 700 */         Optional<Long> sizeIfKnown = source.sizeIfKnown();
/* 701 */         if (!sizeIfKnown.isPresent()) {
/* 702 */           return Optional.absent();
/*     */         }
/* 704 */         result += ((Long)sizeIfKnown.get()).longValue();
/* 705 */         if (result < 0L)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 711 */           return Optional.of(Long.valueOf(Long.MAX_VALUE));
/*     */         }
/*     */       }
/* 714 */       return Optional.of(Long.valueOf(result));
/*     */     }
/*     */     
/*     */     public long size() throws IOException
/*     */     {
/* 719 */       long result = 0L;
/* 720 */       for (ByteSource source : this.sources) {
/* 721 */         result += source.size();
/* 722 */         if (result < 0L)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 728 */           return Long.MAX_VALUE;
/*     */         }
/*     */       }
/* 731 */       return result;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 736 */       return "ByteSource.concat(" + this.sources + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\io\ByteSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */