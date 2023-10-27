/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.GatheringByteChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IO
/*     */ {
/*  48 */   private static final Logger LOG = Log.getLogger(IO.class);
/*     */   
/*     */ 
/*     */ 
/*     */   public static final String CRLF = "\r\n";
/*     */   
/*     */ 
/*     */ 
/*  56 */   public static final byte[] CRLF_BYTES = { 13, 10 };
/*     */   
/*     */   public static final int bufferSize = 65536;
/*     */   
/*     */ 
/*     */   static class Job
/*     */     implements Runnable
/*     */   {
/*     */     InputStream in;
/*     */     OutputStream out;
/*     */     Reader read;
/*     */     Writer write;
/*     */     
/*     */     Job(InputStream in, OutputStream out)
/*     */     {
/*  71 */       this.in = in;
/*  72 */       this.out = out;
/*  73 */       this.read = null;
/*  74 */       this.write = null;
/*     */     }
/*     */     
/*     */     Job(Reader read, Writer write) {
/*  78 */       this.in = null;
/*  79 */       this.out = null;
/*  80 */       this.read = read;
/*  81 */       this.write = write;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try
/*     */       {
/*  92 */         if (this.in != null) {
/*  93 */           IO.copy(this.in, this.out, -1L);
/*     */         } else {
/*  95 */           IO.copy(this.read, this.write, -1L);
/*     */         }
/*     */       }
/*     */       catch (IOException e) {
/*  99 */         IO.LOG.ignore(e);
/*     */         try {
/* 101 */           if (this.out != null)
/* 102 */             this.out.close();
/* 103 */           if (this.write != null) {
/* 104 */             this.write.close();
/*     */           }
/*     */         }
/*     */         catch (IOException e2) {
/* 108 */           IO.LOG.ignore(e2);
/*     */         }
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
/*     */   public static void copy(InputStream in, OutputStream out)
/*     */     throws IOException
/*     */   {
/* 123 */     copy(in, out, -1L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void copy(Reader in, Writer out)
/*     */     throws IOException
/*     */   {
/* 135 */     copy(in, out, -1L);
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
/*     */   public static void copy(InputStream in, OutputStream out, long byteCount)
/*     */     throws IOException
/*     */   {
/* 150 */     byte[] buffer = new byte[65536];
/* 151 */     int len = 65536;
/*     */     
/* 153 */     if (byteCount >= 0L)
/*     */     {
/* 155 */       while (byteCount > 0L)
/*     */       {
/* 157 */         int max = byteCount < 65536L ? (int)byteCount : 65536;
/* 158 */         len = in.read(buffer, 0, max);
/*     */         
/* 160 */         if (len == -1) {
/*     */           break;
/*     */         }
/* 163 */         byteCount -= len;
/* 164 */         out.write(buffer, 0, len);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     for (;;)
/*     */     {
/* 171 */       len = in.read(buffer, 0, 65536);
/* 172 */       if (len < 0)
/*     */         break;
/* 174 */       out.write(buffer, 0, len);
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
/*     */   public static void copy(Reader in, Writer out, long byteCount)
/*     */     throws IOException
/*     */   {
/* 191 */     char[] buffer = new char[65536];
/* 192 */     int len = 65536;
/*     */     
/* 194 */     if (byteCount >= 0L)
/*     */     {
/* 196 */       while (byteCount > 0L)
/*     */       {
/* 198 */         if (byteCount < 65536L) {
/* 199 */           len = in.read(buffer, 0, (int)byteCount);
/*     */         } else {
/* 201 */           len = in.read(buffer, 0, 65536);
/*     */         }
/* 203 */         if (len == -1) {
/*     */           break;
/*     */         }
/* 206 */         byteCount -= len;
/* 207 */         out.write(buffer, 0, len);
/*     */       }
/*     */     }
/* 210 */     if ((out instanceof PrintWriter))
/*     */     {
/* 212 */       PrintWriter pout = (PrintWriter)out;
/* 213 */       while (!pout.checkError())
/*     */       {
/* 215 */         len = in.read(buffer, 0, 65536);
/* 216 */         if (len == -1)
/*     */           break;
/* 218 */         out.write(buffer, 0, len);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       for (;;)
/*     */       {
/* 225 */         len = in.read(buffer, 0, 65536);
/* 226 */         if (len == -1)
/*     */           break;
/* 228 */         out.write(buffer, 0, len);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void copy(File from, File to)
/*     */     throws IOException
/*     */   {
/* 241 */     if (from.isDirectory()) {
/* 242 */       copyDir(from, to);
/*     */     } else {
/* 244 */       copyFile(from, to);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void copyDir(File from, File to) throws IOException
/*     */   {
/* 250 */     if (to.exists())
/*     */     {
/* 252 */       if (!to.isDirectory()) {
/* 253 */         throw new IllegalArgumentException(to.toString());
/*     */       }
/*     */     } else {
/* 256 */       to.mkdirs();
/*     */     }
/* 258 */     File[] files = from.listFiles();
/* 259 */     if (files != null)
/*     */     {
/* 261 */       for (int i = 0; i < files.length; i++)
/*     */       {
/* 263 */         String name = files[i].getName();
/* 264 */         if ((!".".equals(name)) && (!"..".equals(name)))
/*     */         {
/* 266 */           copy(files[i], new File(to, name));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void copyFile(File from, File to) throws IOException
/*     */   {
/* 274 */     InputStream in = new FileInputStream(from);Throwable localThrowable2 = null;
/* 275 */     try { OutputStream out = new FileOutputStream(to);Throwable localThrowable3 = null;
/*     */       try {}catch (Throwable localThrowable)
/*     */       {
/* 274 */         localThrowable3 = localThrowable;throw localThrowable; } finally {} } catch (Throwable localThrowable1) { localThrowable2 = localThrowable1;throw localThrowable1;
/*     */     }
/*     */     finally
/*     */     {
/* 278 */       $closeResource(localThrowable2, in);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(InputStream in)
/*     */     throws IOException
/*     */   {
/* 290 */     return toString(in, (Charset)null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(InputStream in, String encoding)
/*     */     throws IOException
/*     */   {
/* 303 */     return toString(in, encoding == null ? null : Charset.forName(encoding));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(InputStream in, Charset encoding)
/*     */     throws IOException
/*     */   {
/* 315 */     StringWriter writer = new StringWriter();
/* 316 */     InputStreamReader reader = encoding == null ? new InputStreamReader(in) : new InputStreamReader(in, encoding);
/*     */     
/* 318 */     copy(reader, writer);
/* 319 */     return writer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String toString(Reader in)
/*     */     throws IOException
/*     */   {
/* 331 */     StringWriter writer = new StringWriter();
/* 332 */     copy(in, writer);
/* 333 */     return writer.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean delete(File file)
/*     */   {
/* 345 */     if (!file.exists())
/* 346 */       return false;
/* 347 */     if (file.isDirectory())
/*     */     {
/* 349 */       File[] files = file.listFiles();
/* 350 */       for (int i = 0; (files != null) && (i < files.length); i++)
/* 351 */         delete(files[i]);
/*     */     }
/* 353 */     return file.delete();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void close(Closeable closeable)
/*     */   {
/*     */     try
/*     */     {
/* 365 */       if (closeable != null) {
/* 366 */         closeable.close();
/*     */       }
/*     */     }
/*     */     catch (IOException ignore) {
/* 370 */       LOG.ignore(ignore);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void close(InputStream is)
/*     */   {
/* 381 */     close(is);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void close(OutputStream os)
/*     */   {
/* 391 */     close(os);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void close(Reader reader)
/*     */   {
/* 401 */     close(reader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void close(Writer writer)
/*     */   {
/* 411 */     close(writer);
/*     */   }
/*     */   
/*     */ 
/*     */   public static byte[] readBytes(InputStream in)
/*     */     throws IOException
/*     */   {
/* 418 */     ByteArrayOutputStream bout = new ByteArrayOutputStream();
/* 419 */     copy(in, bout);
/* 420 */     return bout.toByteArray();
/*     */   }
/*     */   
/*     */ 
/*     */   private static class NullOS
/*     */     extends OutputStream
/*     */   {
/*     */     public void close() {}
/*     */     
/*     */ 
/*     */     public void flush() {}
/*     */     
/*     */ 
/*     */     public void write(byte[] b) {}
/*     */     
/*     */ 
/*     */     public void write(byte[] b, int i, int l) {}
/*     */     
/*     */ 
/*     */     public void write(int b) {}
/*     */   }
/*     */   
/*     */   public static long write(GatheringByteChannel out, ByteBuffer[] buffers, int offset, int length)
/*     */     throws IOException
/*     */   {
/* 445 */     long total = 0L;
/* 446 */     while (length > 0)
/*     */     {
/*     */ 
/* 449 */       long wrote = out.write(buffers, offset, length);
/*     */       
/*     */ 
/* 452 */       if (wrote == 0L) {
/*     */         break;
/*     */       }
/*     */       
/* 456 */       total += wrote;
/*     */       
/*     */ 
/* 459 */       for (int i = offset;; i++) { if (i >= buffers.length)
/*     */           break label74;
/* 461 */         if (buffers[i].hasRemaining())
/*     */         {
/*     */ 
/* 464 */           length -= i - offset;
/* 465 */           offset = i;
/* 466 */           break;
/*     */         } }
/*     */       label74:
/* 469 */       length = 0;
/*     */     }
/*     */     
/* 472 */     return total;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static OutputStream getNullStream()
/*     */   {
/* 481 */     return __nullStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static InputStream getClosedStream()
/*     */   {
/* 490 */     return __closedStream;
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
/* 508 */   private static NullOS __nullStream = new NullOS(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ClosedIS
/*     */     extends InputStream
/*     */   {
/* 518 */     public int read()
/* 518 */       throws IOException { return -1; }
/*     */   }
/*     */   
/* 521 */   private static ClosedIS __closedStream = new ClosedIS(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Writer getNullWriter()
/*     */   {
/* 529 */     return __nullWriter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PrintWriter getNullPrintWriter()
/*     */   {
/* 538 */     return __nullPrintWriter;
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
/* 560 */   private static NullWrite __nullWriter = new NullWrite(null);
/* 561 */   private static PrintWriter __nullPrintWriter = new PrintWriter(__nullWriter);
/*     */   
/*     */   private static class NullWrite
/*     */     extends Writer
/*     */   {
/*     */     public void close() {}
/*     */     
/*     */     public void flush() {}
/*     */     
/*     */     public void write(char[] b) {}
/*     */     
/*     */     public void write(char[] b, int o, int l) {}
/*     */     
/*     */     public void write(int b) {}
/*     */     
/*     */     public void write(String s) {}
/*     */     
/*     */     public void write(String s, int o, int l) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\IO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */