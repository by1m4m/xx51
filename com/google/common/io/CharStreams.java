/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class CharStreams
/*     */ {
/*     */   private static final int DEFAULT_BUF_SIZE = 2048;
/*     */   
/*     */   static CharBuffer createBuffer()
/*     */   {
/*  55 */     return CharBuffer.allocate(2048);
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
/*     */   @CanIgnoreReturnValue
/*     */   public static long copy(Readable from, Appendable to)
/*     */     throws IOException
/*     */   {
/*  73 */     if ((from instanceof Reader))
/*     */     {
/*  75 */       if ((to instanceof StringBuilder)) {
/*  76 */         return copyReaderToBuilder((Reader)from, (StringBuilder)to);
/*     */       }
/*  78 */       return copyReaderToWriter((Reader)from, asWriter(to));
/*     */     }
/*     */     
/*  81 */     Preconditions.checkNotNull(from);
/*  82 */     Preconditions.checkNotNull(to);
/*  83 */     long total = 0L;
/*  84 */     CharBuffer buf = createBuffer();
/*  85 */     while (from.read(buf) != -1) {
/*  86 */       buf.flip();
/*  87 */       to.append(buf);
/*  88 */       total += buf.remaining();
/*  89 */       buf.clear();
/*     */     }
/*  91 */     return total;
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
/*     */   @CanIgnoreReturnValue
/*     */   static long copyReaderToBuilder(Reader from, StringBuilder to)
/*     */     throws IOException
/*     */   {
/* 115 */     Preconditions.checkNotNull(from);
/* 116 */     Preconditions.checkNotNull(to);
/* 117 */     char[] buf = new char['ࠀ'];
/*     */     
/* 119 */     long total = 0L;
/* 120 */     int nRead; while ((nRead = from.read(buf)) != -1) {
/* 121 */       to.append(buf, 0, nRead);
/* 122 */       total += nRead;
/*     */     }
/* 124 */     return total;
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
/*     */   @CanIgnoreReturnValue
/*     */   static long copyReaderToWriter(Reader from, Writer to)
/*     */     throws IOException
/*     */   {
/* 143 */     Preconditions.checkNotNull(from);
/* 144 */     Preconditions.checkNotNull(to);
/* 145 */     char[] buf = new char['ࠀ'];
/*     */     
/* 147 */     long total = 0L;
/* 148 */     int nRead; while ((nRead = from.read(buf)) != -1) {
/* 149 */       to.write(buf, 0, nRead);
/* 150 */       total += nRead;
/*     */     }
/* 152 */     return total;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(Readable r)
/*     */     throws IOException
/*     */   {
/* 164 */     return toStringBuilder(r).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static StringBuilder toStringBuilder(Readable r)
/*     */     throws IOException
/*     */   {
/* 176 */     StringBuilder sb = new StringBuilder();
/* 177 */     if ((r instanceof Reader)) {
/* 178 */       copyReaderToBuilder((Reader)r, sb);
/*     */     } else {
/* 180 */       copy(r, sb);
/*     */     }
/* 182 */     return sb;
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
/*     */   public static List<String> readLines(Readable r)
/*     */     throws IOException
/*     */   {
/* 197 */     List<String> result = new ArrayList();
/* 198 */     LineReader lineReader = new LineReader(r);
/*     */     String line;
/* 200 */     while ((line = lineReader.readLine()) != null) {
/* 201 */       result.add(line);
/*     */     }
/* 203 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public static <T> T readLines(Readable readable, LineProcessor<T> processor)
/*     */     throws IOException
/*     */   {
/* 217 */     Preconditions.checkNotNull(readable);
/* 218 */     Preconditions.checkNotNull(processor);
/*     */     
/* 220 */     LineReader lineReader = new LineReader(readable);
/*     */     String line;
/* 222 */     while ((line = lineReader.readLine()) != null) {
/* 223 */       if (!processor.processLine(line)) {
/*     */         break;
/*     */       }
/*     */     }
/* 227 */     return (T)processor.getResult();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public static long exhaust(Readable readable)
/*     */     throws IOException
/*     */   {
/* 238 */     long total = 0L;
/*     */     
/* 240 */     CharBuffer buf = createBuffer();
/* 241 */     long read; while ((read = readable.read(buf)) != -1L) {
/* 242 */       total += read;
/* 243 */       buf.clear();
/*     */     }
/* 245 */     return total;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void skipFully(Reader reader, long n)
/*     */     throws IOException
/*     */   {
/* 258 */     Preconditions.checkNotNull(reader);
/* 259 */     while (n > 0L) {
/* 260 */       long amt = reader.skip(n);
/* 261 */       if (amt == 0L) {
/* 262 */         throw new EOFException();
/*     */       }
/* 264 */       n -= amt;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Writer nullWriter()
/*     */   {
/* 274 */     return NullWriter.INSTANCE;
/*     */   }
/*     */   
/*     */   private static final class NullWriter extends Writer
/*     */   {
/* 279 */     private static final NullWriter INSTANCE = new NullWriter();
/*     */     
/*     */ 
/*     */     public void write(int c) {}
/*     */     
/*     */     public void write(char[] cbuf)
/*     */     {
/* 286 */       Preconditions.checkNotNull(cbuf);
/*     */     }
/*     */     
/*     */     public void write(char[] cbuf, int off, int len)
/*     */     {
/* 291 */       Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
/*     */     }
/*     */     
/*     */     public void write(String str)
/*     */     {
/* 296 */       Preconditions.checkNotNull(str);
/*     */     }
/*     */     
/*     */     public void write(String str, int off, int len)
/*     */     {
/* 301 */       Preconditions.checkPositionIndexes(off, off + len, str.length());
/*     */     }
/*     */     
/*     */     public Writer append(CharSequence csq)
/*     */     {
/* 306 */       Preconditions.checkNotNull(csq);
/* 307 */       return this;
/*     */     }
/*     */     
/*     */     public Writer append(CharSequence csq, int start, int end)
/*     */     {
/* 312 */       Preconditions.checkPositionIndexes(start, end, csq.length());
/* 313 */       return this;
/*     */     }
/*     */     
/*     */     public Writer append(char c)
/*     */     {
/* 318 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public void flush() {}
/*     */     
/*     */ 
/*     */     public void close() {}
/*     */     
/*     */     public String toString()
/*     */     {
/* 329 */       return "CharStreams.nullWriter()";
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
/*     */   public static Writer asWriter(Appendable target)
/*     */   {
/* 342 */     if ((target instanceof Writer)) {
/* 343 */       return (Writer)target;
/*     */     }
/* 345 */     return new AppendableWriter(target);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\io\CharStreams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */