/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Iterator;
/*     */ import java.util.stream.Stream;
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
/*     */ public abstract class CharSink
/*     */ {
/*     */   public abstract Writer openStream()
/*     */     throws IOException;
/*     */   
/*     */   public Writer openBufferedStream()
/*     */     throws IOException
/*     */   {
/*  81 */     Writer writer = openStream();
/*  82 */     return (writer instanceof BufferedWriter) ? (BufferedWriter)writer : new BufferedWriter(writer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(CharSequence charSequence)
/*     */     throws IOException
/*     */   {
/*  93 */     Preconditions.checkNotNull(charSequence);
/*     */     
/*  95 */     Closer closer = Closer.create();
/*     */     try {
/*  97 */       Writer out = (Writer)closer.register(openStream());
/*  98 */       out.append(charSequence);
/*  99 */       out.flush();
/*     */     } catch (Throwable e) {
/* 101 */       throw closer.rethrow(e);
/*     */     } finally {
/* 103 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeLines(Iterable<? extends CharSequence> lines)
/*     */     throws IOException
/*     */   {
/* 115 */     writeLines(lines, System.getProperty("line.separator"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator)
/*     */     throws IOException
/*     */   {
/* 126 */     writeLines(lines.iterator(), lineSeparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public void writeLines(Stream<? extends CharSequence> lines)
/*     */     throws IOException
/*     */   {
/* 139 */     writeLines(lines, System.getProperty("line.separator"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public void writeLines(Stream<? extends CharSequence> lines, String lineSeparator)
/*     */     throws IOException
/*     */   {
/* 152 */     writeLines(lines.iterator(), lineSeparator);
/*     */   }
/*     */   
/*     */   private void writeLines(Iterator<? extends CharSequence> lines, String lineSeparator) throws IOException
/*     */   {
/* 157 */     Preconditions.checkNotNull(lineSeparator);
/*     */     
/* 159 */     Writer out = openBufferedStream();Throwable localThrowable3 = null;
/* 160 */     try { while (lines.hasNext()) {
/* 161 */         out.append((CharSequence)lines.next()).append(lineSeparator);
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/* 159 */       localThrowable3 = localThrowable1;throw localThrowable1;
/*     */     }
/*     */     finally
/*     */     {
/* 163 */       if (out != null) { if (localThrowable3 != null) try { out.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { out.close();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public long writeFrom(Readable readable)
/*     */     throws IOException
/*     */   {
/* 176 */     Preconditions.checkNotNull(readable);
/*     */     
/* 178 */     Closer closer = Closer.create();
/*     */     try {
/* 180 */       Writer out = (Writer)closer.register(openStream());
/* 181 */       long written = CharStreams.copy(readable, out);
/* 182 */       out.flush();
/* 183 */       return written;
/*     */     } catch (Throwable e) {
/* 185 */       throw closer.rethrow(e);
/*     */     } finally {
/* 187 */       closer.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\io\CharSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */