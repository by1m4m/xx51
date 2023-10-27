/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class ByteSink
/*     */ {
/*     */   public CharSink asCharSink(Charset charset)
/*     */   {
/*  59 */     return new AsCharSink(charset, null);
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
/*     */   public abstract OutputStream openStream()
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
/*     */   public OutputStream openBufferedStream()
/*     */     throws IOException
/*     */   {
/*  85 */     OutputStream out = openStream();
/*  86 */     return (out instanceof BufferedOutputStream) ? (BufferedOutputStream)out : new BufferedOutputStream(out);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(byte[] bytes)
/*     */     throws IOException
/*     */   {
/*  97 */     Preconditions.checkNotNull(bytes);
/*     */     
/*  99 */     Closer closer = Closer.create();
/*     */     try {
/* 101 */       OutputStream out = (OutputStream)closer.register(openStream());
/* 102 */       out.write(bytes);
/* 103 */       out.flush();
/*     */     } catch (Throwable e) {
/* 105 */       throw closer.rethrow(e);
/*     */     } finally {
/* 107 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public long writeFrom(InputStream input)
/*     */     throws IOException
/*     */   {
/* 120 */     Preconditions.checkNotNull(input);
/*     */     
/* 122 */     Closer closer = Closer.create();
/*     */     try {
/* 124 */       OutputStream out = (OutputStream)closer.register(openStream());
/* 125 */       long written = ByteStreams.copy(input, out);
/* 126 */       out.flush();
/* 127 */       return written;
/*     */     } catch (Throwable e) {
/* 129 */       throw closer.rethrow(e);
/*     */     } finally {
/* 131 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private final class AsCharSink
/*     */     extends CharSink
/*     */   {
/*     */     private final Charset charset;
/*     */     
/*     */ 
/*     */     private AsCharSink(Charset charset)
/*     */     {
/* 144 */       this.charset = ((Charset)Preconditions.checkNotNull(charset));
/*     */     }
/*     */     
/*     */     public Writer openStream() throws IOException
/*     */     {
/* 149 */       return new OutputStreamWriter(ByteSink.this.openStream(), this.charset);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 154 */       return ByteSink.this.toString() + ".asCharSink(" + this.charset + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\io\ByteSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */