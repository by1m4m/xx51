/*     */ package com.google.common.hash;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
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
/*     */ @Beta
/*     */ public final class Funnels
/*     */ {
/*     */   public static Funnel<byte[]> byteArrayFunnel()
/*     */   {
/*  36 */     return ByteArrayFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private static enum ByteArrayFunnel implements Funnel<byte[]> {
/*  40 */     INSTANCE;
/*     */     
/*     */     private ByteArrayFunnel() {}
/*  43 */     public void funnel(byte[] from, PrimitiveSink into) { into.putBytes(from); }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/*  48 */       return "Funnels.byteArrayFunnel()";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Funnel<CharSequence> unencodedCharsFunnel()
/*     */   {
/*  60 */     return UnencodedCharsFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private static enum UnencodedCharsFunnel implements Funnel<CharSequence> {
/*  64 */     INSTANCE;
/*     */     
/*     */     private UnencodedCharsFunnel() {}
/*  67 */     public void funnel(CharSequence from, PrimitiveSink into) { into.putUnencodedChars(from); }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/*  72 */       return "Funnels.unencodedCharsFunnel()";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Funnel<CharSequence> stringFunnel(Charset charset)
/*     */   {
/*  83 */     return new StringCharsetFunnel(charset);
/*     */   }
/*     */   
/*     */   private static class StringCharsetFunnel implements Funnel<CharSequence>, Serializable {
/*     */     private final Charset charset;
/*     */     
/*     */     StringCharsetFunnel(Charset charset) {
/*  90 */       this.charset = ((Charset)Preconditions.checkNotNull(charset));
/*     */     }
/*     */     
/*     */     public void funnel(CharSequence from, PrimitiveSink into) {
/*  94 */       into.putString(from, this.charset);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  99 */       return "Funnels.stringFunnel(" + this.charset.name() + ")";
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 104 */       if ((o instanceof StringCharsetFunnel)) {
/* 105 */         StringCharsetFunnel funnel = (StringCharsetFunnel)o;
/* 106 */         return this.charset.equals(funnel.charset);
/*     */       }
/* 108 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 113 */       return StringCharsetFunnel.class.hashCode() ^ this.charset.hashCode();
/*     */     }
/*     */     
/*     */     Object writeReplace() {
/* 117 */       return new SerializedForm(this.charset);
/*     */     }
/*     */     
/*     */     private static class SerializedForm implements Serializable {
/*     */       private final String charsetCanonicalName;
/*     */       private static final long serialVersionUID = 0L;
/*     */       
/* 124 */       SerializedForm(Charset charset) { this.charsetCanonicalName = charset.name(); }
/*     */       
/*     */       private Object readResolve()
/*     */       {
/* 128 */         return Funnels.stringFunnel(Charset.forName(this.charsetCanonicalName));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Funnel<Integer> integerFunnel()
/*     */   {
/* 141 */     return IntegerFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private static enum IntegerFunnel implements Funnel<Integer> {
/* 145 */     INSTANCE;
/*     */     
/*     */     private IntegerFunnel() {}
/* 148 */     public void funnel(Integer from, PrimitiveSink into) { into.putInt(from.intValue()); }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 153 */       return "Funnels.integerFunnel()";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E> Funnel<Iterable<? extends E>> sequentialFunnel(Funnel<E> elementFunnel)
/*     */   {
/* 164 */     return new SequentialFunnel(elementFunnel);
/*     */   }
/*     */   
/*     */   private static class SequentialFunnel<E> implements Funnel<Iterable<? extends E>>, Serializable {
/*     */     private final Funnel<E> elementFunnel;
/*     */     
/*     */     SequentialFunnel(Funnel<E> elementFunnel) {
/* 171 */       this.elementFunnel = ((Funnel)Preconditions.checkNotNull(elementFunnel));
/*     */     }
/*     */     
/*     */     public void funnel(Iterable<? extends E> from, PrimitiveSink into) {
/* 175 */       for (E e : from) {
/* 176 */         this.elementFunnel.funnel(e, into);
/*     */       }
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 182 */       return "Funnels.sequentialFunnel(" + this.elementFunnel + ")";
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 187 */       if ((o instanceof SequentialFunnel)) {
/* 188 */         SequentialFunnel<?> funnel = (SequentialFunnel)o;
/* 189 */         return this.elementFunnel.equals(funnel.elementFunnel);
/*     */       }
/* 191 */       return false;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 196 */       return SequentialFunnel.class.hashCode() ^ this.elementFunnel.hashCode();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Funnel<Long> longFunnel()
/*     */   {
/* 206 */     return LongFunnel.INSTANCE;
/*     */   }
/*     */   
/*     */   private static enum LongFunnel implements Funnel<Long> {
/* 210 */     INSTANCE;
/*     */     
/*     */     private LongFunnel() {}
/* 213 */     public void funnel(Long from, PrimitiveSink into) { into.putLong(from.longValue()); }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 218 */       return "Funnels.longFunnel()";
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
/*     */   public static OutputStream asOutputStream(PrimitiveSink sink)
/*     */   {
/* 233 */     return new SinkAsStream(sink);
/*     */   }
/*     */   
/*     */   private static class SinkAsStream extends OutputStream {
/*     */     final PrimitiveSink sink;
/*     */     
/*     */     SinkAsStream(PrimitiveSink sink) {
/* 240 */       this.sink = ((PrimitiveSink)Preconditions.checkNotNull(sink));
/*     */     }
/*     */     
/*     */     public void write(int b)
/*     */     {
/* 245 */       this.sink.putByte((byte)b);
/*     */     }
/*     */     
/*     */     public void write(byte[] bytes)
/*     */     {
/* 250 */       this.sink.putBytes(bytes);
/*     */     }
/*     */     
/*     */     public void write(byte[] bytes, int off, int len)
/*     */     {
/* 255 */       this.sink.putBytes(bytes, off, len);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 260 */       return "Funnels.asOutputStream(" + this.sink + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\hash\Funnels.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */