/*      */ package org.eclipse.jetty.util;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.RandomAccessFile;
/*      */ import java.nio.BufferOverflowException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.MappedByteBuffer;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.FileChannel.MapMode;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.nio.file.OpenOption;
/*      */ import java.nio.file.StandardOpenOption;
/*      */ import java.util.Arrays;
/*      */ import org.eclipse.jetty.util.log.Log;
/*      */ import org.eclipse.jetty.util.log.Logger;
/*      */ import org.eclipse.jetty.util.resource.Resource;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class BufferUtil
/*      */ {
/*      */   static final int TEMP_BUFFER_SIZE = 4096;
/*      */   static final byte SPACE = 32;
/*      */   static final byte MINUS = 45;
/*  102 */   static final byte[] DIGIT = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
/*      */   
/*      */ 
/*      */ 
/*  106 */   public static final ByteBuffer EMPTY_BUFFER = ByteBuffer.wrap(new byte[0]);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuffer allocate(int capacity)
/*      */   {
/*  117 */     ByteBuffer buf = ByteBuffer.allocate(capacity);
/*  118 */     buf.limit(0);
/*  119 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuffer allocateDirect(int capacity)
/*      */   {
/*  131 */     ByteBuffer buf = ByteBuffer.allocateDirect(capacity);
/*  132 */     buf.limit(0);
/*  133 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void clear(ByteBuffer buffer)
/*      */   {
/*  144 */     if (buffer != null)
/*      */     {
/*  146 */       buffer.position(0);
/*  147 */       buffer.limit(0);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void clearToFill(ByteBuffer buffer)
/*      */   {
/*  158 */     if (buffer != null)
/*      */     {
/*  160 */       buffer.position(0);
/*  161 */       buffer.limit(buffer.capacity());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int flipToFill(ByteBuffer buffer)
/*      */   {
/*  181 */     int position = buffer.position();
/*  182 */     int limit = buffer.limit();
/*  183 */     if (position == limit)
/*      */     {
/*  185 */       buffer.position(0);
/*  186 */       buffer.limit(buffer.capacity());
/*  187 */       return 0;
/*      */     }
/*      */     
/*  190 */     int capacity = buffer.capacity();
/*  191 */     if (limit == capacity)
/*      */     {
/*  193 */       buffer.compact();
/*  194 */       return 0;
/*      */     }
/*      */     
/*  197 */     buffer.position(limit);
/*  198 */     buffer.limit(capacity);
/*  199 */     return position;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void flipToFlush(ByteBuffer buffer, int position)
/*      */   {
/*  215 */     buffer.limit(buffer.position());
/*  216 */     buffer.position(position);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static byte[] toArray(ByteBuffer buffer)
/*      */   {
/*  227 */     if (buffer.hasArray())
/*      */     {
/*  229 */       byte[] array = buffer.array();
/*  230 */       int from = buffer.arrayOffset() + buffer.position();
/*  231 */       return Arrays.copyOfRange(array, from, from + buffer.remaining());
/*      */     }
/*      */     
/*      */ 
/*  235 */     byte[] to = new byte[buffer.remaining()];
/*  236 */     buffer.slice().get(to);
/*  237 */     return to;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isTheEmptyBuffer(ByteBuffer buf)
/*      */   {
/*  248 */     boolean isTheEmptyBuffer_ = buf == EMPTY_BUFFER;
/*  249 */     return isTheEmptyBuffer_;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isEmpty(ByteBuffer buf)
/*      */   {
/*  259 */     return (buf == null) || (buf.remaining() == 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean hasContent(ByteBuffer buf)
/*      */   {
/*  269 */     return (buf != null) && (buf.remaining() > 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isFull(ByteBuffer buf)
/*      */   {
/*  279 */     return (buf != null) && (buf.limit() == buf.capacity());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int length(ByteBuffer buffer)
/*      */   {
/*  289 */     return buffer == null ? 0 : buffer.remaining();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int space(ByteBuffer buffer)
/*      */   {
/*  299 */     if (buffer == null)
/*  300 */       return 0;
/*  301 */     return buffer.capacity() - buffer.limit();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean compact(ByteBuffer buffer)
/*      */   {
/*  311 */     if (buffer.position() == 0)
/*  312 */       return false;
/*  313 */     boolean full = buffer.limit() == buffer.capacity();
/*  314 */     buffer.compact().flip();
/*  315 */     return (full) && (buffer.limit() < buffer.capacity());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int put(ByteBuffer from, ByteBuffer to)
/*      */   {
/*  328 */     int remaining = from.remaining();
/*  329 */     int put; if (remaining > 0)
/*      */     {
/*  331 */       if (remaining <= to.remaining())
/*      */       {
/*  333 */         to.put(from);
/*  334 */         int put = remaining;
/*  335 */         from.position(from.limit());
/*      */       }
/*  337 */       else if (from.hasArray())
/*      */       {
/*  339 */         int put = to.remaining();
/*  340 */         to.put(from.array(), from.arrayOffset() + from.position(), put);
/*  341 */         from.position(from.position() + put);
/*      */       }
/*      */       else
/*      */       {
/*  345 */         int put = to.remaining();
/*  346 */         ByteBuffer slice = from.slice();
/*  347 */         slice.limit(put);
/*  348 */         to.put(slice);
/*  349 */         from.position(from.position() + put);
/*      */       }
/*      */     }
/*      */     else {
/*  353 */       put = 0;
/*      */     }
/*  355 */     return put;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public static int flipPutFlip(ByteBuffer from, ByteBuffer to)
/*      */   {
/*  368 */     return append(to, from);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void append(ByteBuffer to, byte[] b, int off, int len)
/*      */     throws BufferOverflowException
/*      */   {
/*  381 */     int pos = flipToFill(to);
/*      */     try
/*      */     {
/*  384 */       to.put(b, off, len);
/*      */     }
/*      */     finally
/*      */     {
/*  388 */       flipToFlush(to, pos);
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public static void append(ByteBuffer to, byte b)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokestatic 25	org/eclipse/jetty/util/BufferUtil:flipToFill	(Ljava/nio/ByteBuffer;)I
/*      */     //   4: istore_2
/*      */     //   5: aload_0
/*      */     //   6: iload_1
/*      */     //   7: invokevirtual 27	java/nio/ByteBuffer:put	(B)Ljava/nio/ByteBuffer;
/*      */     //   10: pop
/*      */     //   11: aload_0
/*      */     //   12: iload_2
/*      */     //   13: invokestatic 26	org/eclipse/jetty/util/BufferUtil:flipToFlush	(Ljava/nio/ByteBuffer;I)V
/*      */     //   16: goto +11 -> 27
/*      */     //   19: astore_3
/*      */     //   20: aload_0
/*      */     //   21: iload_2
/*      */     //   22: invokestatic 26	org/eclipse/jetty/util/BufferUtil:flipToFlush	(Ljava/nio/ByteBuffer;I)V
/*      */     //   25: aload_3
/*      */     //   26: athrow
/*      */     //   27: return
/*      */     // Line number table:
/*      */     //   Java source line #399	-> byte code offset #0
/*      */     //   Java source line #402	-> byte code offset #5
/*      */     //   Java source line #406	-> byte code offset #11
/*      */     //   Java source line #407	-> byte code offset #16
/*      */     //   Java source line #406	-> byte code offset #19
/*      */     //   Java source line #408	-> byte code offset #27
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	28	0	to	ByteBuffer
/*      */     //   0	28	1	b	byte
/*      */     //   4	18	2	pos	int
/*      */     //   19	7	3	localObject	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   5	11	19	finally
/*      */   }
/*      */   
/*      */   public static int append(ByteBuffer to, ByteBuffer b)
/*      */   {
/*  418 */     int pos = flipToFill(to);
/*      */     try
/*      */     {
/*  421 */       return put(b, to);
/*      */     }
/*      */     finally
/*      */     {
/*  425 */       flipToFlush(to, pos);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int fill(ByteBuffer to, byte[] b, int off, int len)
/*      */   {
/*  440 */     int pos = flipToFill(to);
/*      */     try
/*      */     {
/*  443 */       int remaining = to.remaining();
/*  444 */       int take = remaining < len ? remaining : len;
/*  445 */       to.put(b, off, take);
/*  446 */       return take;
/*      */     }
/*      */     finally
/*      */     {
/*  450 */       flipToFlush(to, pos);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public static void readFrom(File file, ByteBuffer buffer)
/*      */     throws IOException
/*      */   {
/*  458 */     RandomAccessFile raf = new RandomAccessFile(file, "r");Throwable localThrowable1 = null;
/*      */     try {
/*  460 */       FileChannel channel = raf.getChannel();
/*  461 */       long needed = raf.length();
/*      */       
/*  463 */       while ((needed > 0L) && (buffer.hasRemaining())) {
/*  464 */         needed -= channel.read(buffer);
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable)
/*      */     {
/*  458 */       localThrowable1 = localThrowable;throw localThrowable;
/*      */ 
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/*      */ 
/*  465 */       $closeResource(localThrowable1, raf);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void readFrom(InputStream is, int needed, ByteBuffer buffer) throws IOException
/*      */   {
/*  471 */     ByteBuffer tmp = allocate(8192);
/*      */     
/*  473 */     while ((needed > 0) && (buffer.hasRemaining()))
/*      */     {
/*  475 */       int l = is.read(tmp.array(), 0, 8192);
/*  476 */       if (l < 0)
/*      */         break;
/*  478 */       tmp.position(0);
/*  479 */       tmp.limit(l);
/*  480 */       buffer.put(tmp);
/*      */     }
/*      */   }
/*      */   
/*      */   public static void writeTo(ByteBuffer buffer, OutputStream out)
/*      */     throws IOException
/*      */   {
/*  487 */     if (buffer.hasArray())
/*      */     {
/*  489 */       out.write(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());
/*      */       
/*  491 */       buffer.position(buffer.position() + buffer.remaining());
/*      */     }
/*      */     else
/*      */     {
/*  495 */       byte[] bytes = new byte['က'];
/*  496 */       while (buffer.hasRemaining()) {
/*  497 */         int byteCountToWrite = Math.min(buffer.remaining(), 4096);
/*  498 */         buffer.get(bytes, 0, byteCountToWrite);
/*  499 */         out.write(bytes, 0, byteCountToWrite);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toString(ByteBuffer buffer)
/*      */   {
/*  511 */     return toString(buffer, StandardCharsets.ISO_8859_1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toUTF8String(ByteBuffer buffer)
/*      */   {
/*  521 */     return toString(buffer, StandardCharsets.UTF_8);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toString(ByteBuffer buffer, Charset charset)
/*      */   {
/*  532 */     if (buffer == null)
/*  533 */       return null;
/*  534 */     byte[] array = buffer.hasArray() ? buffer.array() : null;
/*  535 */     if (array == null)
/*      */     {
/*  537 */       byte[] to = new byte[buffer.remaining()];
/*  538 */       buffer.slice().get(to);
/*  539 */       return new String(to, 0, to.length, charset);
/*      */     }
/*  541 */     return new String(array, buffer.arrayOffset() + buffer.position(), buffer.remaining(), charset);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toString(ByteBuffer buffer, int position, int length, Charset charset)
/*      */   {
/*  555 */     if (buffer == null)
/*  556 */       return null;
/*  557 */     byte[] array = buffer.hasArray() ? buffer.array() : null;
/*  558 */     if (array == null)
/*      */     {
/*  560 */       ByteBuffer ro = buffer.asReadOnlyBuffer();
/*  561 */       ro.position(position);
/*  562 */       ro.limit(position + length);
/*  563 */       byte[] to = new byte[length];
/*  564 */       ro.get(to);
/*  565 */       return new String(to, 0, to.length, charset);
/*      */     }
/*  567 */     return new String(array, buffer.arrayOffset() + position, length, charset);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int toInt(ByteBuffer buffer)
/*      */   {
/*  580 */     return toInt(buffer, buffer.position(), buffer.remaining());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int toInt(ByteBuffer buffer, int position, int length)
/*      */   {
/*  598 */     int val = 0;
/*  599 */     boolean started = false;
/*  600 */     boolean minus = false;
/*      */     
/*  602 */     int limit = position + length;
/*      */     
/*  604 */     if (length <= 0) {
/*  605 */       throw new NumberFormatException(toString(buffer, position, length, StandardCharsets.UTF_8));
/*      */     }
/*  607 */     for (int i = position; i < limit; i++)
/*      */     {
/*  609 */       byte b = buffer.get(i);
/*  610 */       if (b <= 32)
/*      */       {
/*  612 */         if (started) {
/*      */           break;
/*      */         }
/*  615 */       } else if ((b >= 48) && (b <= 57))
/*      */       {
/*  617 */         val = val * 10 + (b - 48);
/*  618 */         started = true;
/*      */       } else {
/*  620 */         if ((b != 45) || (started))
/*      */           break;
/*  622 */         minus = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  628 */     if (started)
/*  629 */       return minus ? -val : val;
/*  630 */     throw new NumberFormatException(toString(buffer));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int takeInt(ByteBuffer buffer)
/*      */   {
/*  643 */     int val = 0;
/*  644 */     boolean started = false;
/*  645 */     boolean minus = false;
/*      */     
/*  647 */     for (int i = buffer.position(); i < buffer.limit(); i++)
/*      */     {
/*  649 */       byte b = buffer.get(i);
/*  650 */       if (b <= 32)
/*      */       {
/*  652 */         if (started) {
/*      */           break;
/*      */         }
/*  655 */       } else if ((b >= 48) && (b <= 57))
/*      */       {
/*  657 */         val = val * 10 + (b - 48);
/*  658 */         started = true;
/*      */       } else {
/*  660 */         if ((b != 45) || (started))
/*      */           break;
/*  662 */         minus = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  668 */     if (started)
/*      */     {
/*  670 */       buffer.position(i);
/*  671 */       return minus ? -val : val;
/*      */     }
/*  673 */     throw new NumberFormatException(toString(buffer));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static long toLong(ByteBuffer buffer)
/*      */   {
/*  685 */     long val = 0L;
/*  686 */     boolean started = false;
/*  687 */     boolean minus = false;
/*      */     
/*  689 */     for (int i = buffer.position(); i < buffer.limit(); i++)
/*      */     {
/*  691 */       byte b = buffer.get(i);
/*  692 */       if (b <= 32)
/*      */       {
/*  694 */         if (started) {
/*      */           break;
/*      */         }
/*  697 */       } else if ((b >= 48) && (b <= 57))
/*      */       {
/*  699 */         val = val * 10L + (b - 48);
/*  700 */         started = true;
/*      */       } else {
/*  702 */         if ((b != 45) || (started))
/*      */           break;
/*  704 */         minus = true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  710 */     if (started)
/*  711 */       return minus ? -val : val;
/*  712 */     throw new NumberFormatException(toString(buffer));
/*      */   }
/*      */   
/*      */   public static void putHexInt(ByteBuffer buffer, int n)
/*      */   {
/*  717 */     if (n < 0)
/*      */     {
/*  719 */       buffer.put((byte)45);
/*      */       
/*  721 */       if (n == Integer.MIN_VALUE)
/*      */       {
/*  723 */         buffer.put((byte)56);
/*  724 */         buffer.put((byte)48);
/*  725 */         buffer.put((byte)48);
/*  726 */         buffer.put((byte)48);
/*  727 */         buffer.put((byte)48);
/*  728 */         buffer.put((byte)48);
/*  729 */         buffer.put((byte)48);
/*  730 */         buffer.put((byte)48);
/*      */         
/*  732 */         return;
/*      */       }
/*  734 */       n = -n;
/*      */     }
/*      */     
/*  737 */     if (n < 16)
/*      */     {
/*  739 */       buffer.put(DIGIT[n]);
/*      */     }
/*      */     else
/*      */     {
/*  743 */       boolean started = false;
/*      */       
/*  745 */       for (int hexDivisor : hexDivisors)
/*      */       {
/*  747 */         if (n < hexDivisor)
/*      */         {
/*  749 */           if (started) {
/*  750 */             buffer.put((byte)48);
/*      */           }
/*      */         }
/*      */         else {
/*  754 */           started = true;
/*  755 */           int d = n / hexDivisor;
/*  756 */           buffer.put(DIGIT[d]);
/*  757 */           n -= d * hexDivisor;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void putDecInt(ByteBuffer buffer, int n)
/*      */   {
/*  765 */     if (n < 0)
/*      */     {
/*  767 */       buffer.put((byte)45);
/*      */       
/*  769 */       if (n == Integer.MIN_VALUE)
/*      */       {
/*  771 */         buffer.put((byte)50);
/*  772 */         n = 147483648;
/*      */       }
/*      */       else {
/*  775 */         n = -n;
/*      */       }
/*      */     }
/*  778 */     if (n < 10)
/*      */     {
/*  780 */       buffer.put(DIGIT[n]);
/*      */     }
/*      */     else
/*      */     {
/*  784 */       boolean started = false;
/*      */       
/*  786 */       for (int decDivisor : decDivisors)
/*      */       {
/*  788 */         if (n < decDivisor)
/*      */         {
/*  790 */           if (started) {
/*  791 */             buffer.put((byte)48);
/*      */           }
/*      */         }
/*      */         else {
/*  795 */           started = true;
/*  796 */           int d = n / decDivisor;
/*  797 */           buffer.put(DIGIT[d]);
/*  798 */           n -= d * decDivisor;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static void putDecLong(ByteBuffer buffer, long n) {
/*  805 */     if (n < 0L)
/*      */     {
/*  807 */       buffer.put((byte)45);
/*      */       
/*  809 */       if (n == Long.MIN_VALUE)
/*      */       {
/*  811 */         buffer.put((byte)57);
/*  812 */         n = 223372036854775808L;
/*      */       }
/*      */       else {
/*  815 */         n = -n;
/*      */       }
/*      */     }
/*  818 */     if (n < 10L)
/*      */     {
/*  820 */       buffer.put(DIGIT[((int)n)]);
/*      */     }
/*      */     else
/*      */     {
/*  824 */       boolean started = false;
/*      */       
/*  826 */       for (long aDecDivisorsL : decDivisorsL)
/*      */       {
/*  828 */         if (n < aDecDivisorsL)
/*      */         {
/*  830 */           if (started) {
/*  831 */             buffer.put((byte)48);
/*      */           }
/*      */         }
/*      */         else {
/*  835 */           started = true;
/*  836 */           long d = n / aDecDivisorsL;
/*  837 */           buffer.put(DIGIT[((int)d)]);
/*  838 */           n -= d * aDecDivisorsL;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public static ByteBuffer toBuffer(int value) {
/*  845 */     ByteBuffer buf = ByteBuffer.allocate(32);
/*  846 */     putDecInt(buf, value);
/*  847 */     return buf;
/*      */   }
/*      */   
/*      */   public static ByteBuffer toBuffer(long value)
/*      */   {
/*  852 */     ByteBuffer buf = ByteBuffer.allocate(32);
/*  853 */     putDecLong(buf, value);
/*  854 */     return buf;
/*      */   }
/*      */   
/*      */   public static ByteBuffer toBuffer(String s)
/*      */   {
/*  859 */     return toBuffer(s, StandardCharsets.ISO_8859_1);
/*      */   }
/*      */   
/*      */   public static ByteBuffer toBuffer(String s, Charset charset)
/*      */   {
/*  864 */     if (s == null)
/*  865 */       return EMPTY_BUFFER;
/*  866 */     return toBuffer(s.getBytes(charset));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuffer toBuffer(byte[] array)
/*      */   {
/*  878 */     if (array == null)
/*  879 */       return EMPTY_BUFFER;
/*  880 */     return toBuffer(array, 0, array.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ByteBuffer toBuffer(byte[] array, int offset, int length)
/*      */   {
/*  896 */     if (array == null)
/*  897 */       return EMPTY_BUFFER;
/*  898 */     return ByteBuffer.wrap(array, offset, length);
/*      */   }
/*      */   
/*      */   public static ByteBuffer toDirectBuffer(String s)
/*      */   {
/*  903 */     return toDirectBuffer(s, StandardCharsets.ISO_8859_1);
/*      */   }
/*      */   
/*      */   public static ByteBuffer toDirectBuffer(String s, Charset charset)
/*      */   {
/*  908 */     if (s == null)
/*  909 */       return EMPTY_BUFFER;
/*  910 */     byte[] bytes = s.getBytes(charset);
/*  911 */     ByteBuffer buf = ByteBuffer.allocateDirect(bytes.length);
/*  912 */     buf.put(bytes);
/*  913 */     buf.flip();
/*  914 */     return buf;
/*      */   }
/*      */   
/*      */   public static ByteBuffer toMappedBuffer(File file) throws IOException
/*      */   {
/*  919 */     FileChannel channel = FileChannel.open(file.toPath(), new OpenOption[] { StandardOpenOption.READ });Throwable localThrowable1 = null;
/*      */     try {
/*  921 */       return channel.map(FileChannel.MapMode.READ_ONLY, 0L, file.length());
/*      */     }
/*      */     catch (Throwable localThrowable2)
/*      */     {
/*  919 */       localThrowable1 = localThrowable2;throw localThrowable2;
/*      */     }
/*      */     finally {
/*  922 */       if (channel != null) $closeResource(localThrowable1, channel);
/*      */     }
/*      */   }
/*      */   
/*      */   public static boolean isMappedBuffer(ByteBuffer buffer) {
/*  927 */     if (!(buffer instanceof MappedByteBuffer))
/*  928 */       return false;
/*  929 */     MappedByteBuffer mapped = (MappedByteBuffer)buffer;
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  934 */       mapped.isLoaded();
/*  935 */       return true;
/*      */     }
/*      */     catch (UnsupportedOperationException e) {}
/*      */     
/*  939 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   public static ByteBuffer toBuffer(Resource resource, boolean direct)
/*      */     throws IOException
/*      */   {
/*  946 */     int len = (int)resource.length();
/*  947 */     if (len < 0) {
/*  948 */       throw new IllegalArgumentException("invalid resource: " + String.valueOf(resource) + " len=" + len);
/*      */     }
/*  950 */     ByteBuffer buffer = direct ? allocateDirect(len) : allocate(len);
/*      */     
/*  952 */     int pos = flipToFill(buffer);
/*  953 */     if (resource.getFile() != null) {
/*  954 */       readFrom(resource.getFile(), buffer);
/*      */     }
/*      */     else {
/*  957 */       InputStream is = resource.getInputStream();Throwable localThrowable1 = null;
/*      */       try {
/*  959 */         readFrom(is, len, buffer);
/*      */       }
/*      */       catch (Throwable localThrowable)
/*      */       {
/*  957 */         localThrowable1 = localThrowable;throw localThrowable;
/*      */       }
/*      */       finally {
/*  960 */         if (is != null) $closeResource(localThrowable1, is);
/*      */       } }
/*  962 */     flipToFlush(buffer, pos);
/*      */     
/*  964 */     return buffer;
/*      */   }
/*      */   
/*      */   public static String toSummaryString(ByteBuffer buffer)
/*      */   {
/*  969 */     if (buffer == null)
/*  970 */       return "null";
/*  971 */     StringBuilder buf = new StringBuilder();
/*  972 */     buf.append("[p=");
/*  973 */     buf.append(buffer.position());
/*  974 */     buf.append(",l=");
/*  975 */     buf.append(buffer.limit());
/*  976 */     buf.append(",c=");
/*  977 */     buf.append(buffer.capacity());
/*  978 */     buf.append(",r=");
/*  979 */     buf.append(buffer.remaining());
/*  980 */     buf.append("]");
/*  981 */     return buf.toString();
/*      */   }
/*      */   
/*      */   public static String toDetailString(ByteBuffer[] buffer)
/*      */   {
/*  986 */     StringBuilder builder = new StringBuilder();
/*  987 */     builder.append('[');
/*  988 */     for (int i = 0; i < buffer.length; i++)
/*      */     {
/*  990 */       if (i > 0) builder.append(',');
/*  991 */       builder.append(toDetailString(buffer[i]));
/*      */     }
/*  993 */     builder.append(']');
/*  994 */     return builder.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void idString(ByteBuffer buffer, StringBuilder out)
/*      */   {
/* 1004 */     out.append(buffer.getClass().getSimpleName());
/* 1005 */     out.append("@");
/* 1006 */     if ((buffer.hasArray()) && (buffer.arrayOffset() == 4))
/*      */     {
/* 1008 */       out.append('T');
/* 1009 */       byte[] array = buffer.array();
/* 1010 */       TypeUtil.toHex(array[0], out);
/* 1011 */       TypeUtil.toHex(array[1], out);
/* 1012 */       TypeUtil.toHex(array[2], out);
/* 1013 */       TypeUtil.toHex(array[3], out);
/*      */     }
/*      */     else {
/* 1016 */       out.append(Integer.toHexString(System.identityHashCode(buffer)));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toIDString(ByteBuffer buffer)
/*      */   {
/* 1026 */     StringBuilder buf = new StringBuilder();
/* 1027 */     idString(buffer, buf);
/* 1028 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toDetailString(ByteBuffer buffer)
/*      */   {
/* 1039 */     if (buffer == null) {
/* 1040 */       return "null";
/*      */     }
/* 1042 */     StringBuilder buf = new StringBuilder();
/* 1043 */     idString(buffer, buf);
/* 1044 */     buf.append("[p=");
/* 1045 */     buf.append(buffer.position());
/* 1046 */     buf.append(",l=");
/* 1047 */     buf.append(buffer.limit());
/* 1048 */     buf.append(",c=");
/* 1049 */     buf.append(buffer.capacity());
/* 1050 */     buf.append(",r=");
/* 1051 */     buf.append(buffer.remaining());
/* 1052 */     buf.append("]={");
/*      */     
/* 1054 */     appendDebugString(buf, buffer);
/*      */     
/* 1056 */     buf.append("}");
/*      */     
/* 1058 */     return buf.toString();
/*      */   }
/*      */   
/*      */   private static void appendDebugString(StringBuilder buf, ByteBuffer buffer)
/*      */   {
/*      */     try
/*      */     {
/* 1065 */       for (int i = 0; i < buffer.position(); i++)
/*      */       {
/* 1067 */         appendContentChar(buf, buffer.get(i));
/* 1068 */         if ((i == 16) && (buffer.position() > 32))
/*      */         {
/* 1070 */           buf.append("...");
/* 1071 */           i = buffer.position() - 16;
/*      */         }
/*      */       }
/* 1074 */       buf.append("<<<");
/* 1075 */       for (int i = buffer.position(); i < buffer.limit(); i++)
/*      */       {
/* 1077 */         appendContentChar(buf, buffer.get(i));
/* 1078 */         if ((i == buffer.position() + 16) && (buffer.limit() > buffer.position() + 32))
/*      */         {
/* 1080 */           buf.append("...");
/* 1081 */           i = buffer.limit() - 16;
/*      */         }
/*      */       }
/* 1084 */       buf.append(">>>");
/* 1085 */       int limit = buffer.limit();
/* 1086 */       buffer.limit(buffer.capacity());
/* 1087 */       for (int i = limit; i < buffer.capacity(); i++)
/*      */       {
/* 1089 */         appendContentChar(buf, buffer.get(i));
/* 1090 */         if ((i == limit + 16) && (buffer.capacity() > limit + 32))
/*      */         {
/* 1092 */           buf.append("...");
/* 1093 */           i = buffer.capacity() - 16;
/*      */         }
/*      */       }
/* 1096 */       buffer.limit(limit);
/*      */     }
/*      */     catch (Throwable x)
/*      */     {
/* 1100 */       Log.getRootLogger().ignore(x);
/* 1101 */       buf.append("!!concurrent mod!!");
/*      */     }
/*      */   }
/*      */   
/*      */   private static void appendContentChar(StringBuilder buf, byte b)
/*      */   {
/* 1107 */     if (b == 92) {
/* 1108 */       buf.append("\\\\");
/* 1109 */     } else if ((b >= 32) && (b <= 126)) {
/* 1110 */       buf.append((char)b);
/* 1111 */     } else if (b == 13) {
/* 1112 */       buf.append("\\r");
/* 1113 */     } else if (b == 10) {
/* 1114 */       buf.append("\\n");
/* 1115 */     } else if (b == 9) {
/* 1116 */       buf.append("\\t");
/*      */     } else {
/* 1118 */       buf.append("\\x").append(TypeUtil.toHexString(b));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toHexSummary(ByteBuffer buffer)
/*      */   {
/* 1128 */     if (buffer == null)
/* 1129 */       return "null";
/* 1130 */     StringBuilder buf = new StringBuilder();
/*      */     
/* 1132 */     buf.append("b[").append(buffer.remaining()).append("]=");
/* 1133 */     for (int i = buffer.position(); i < buffer.limit(); i++)
/*      */     {
/* 1135 */       TypeUtil.toHex(buffer.get(i), buf);
/* 1136 */       if ((i == buffer.position() + 24) && (buffer.limit() > buffer.position() + 32))
/*      */       {
/* 1138 */         buf.append("...");
/* 1139 */         i = buffer.limit() - 8;
/*      */       }
/*      */     }
/* 1142 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toHexString(ByteBuffer buffer)
/*      */   {
/* 1152 */     if (buffer == null)
/* 1153 */       return "null";
/* 1154 */     return TypeUtil.toHexString(toArray(buffer));
/*      */   }
/*      */   
/*      */ 
/* 1158 */   private static final int[] decDivisors = { 1000000000, 100000000, 10000000, 1000000, 100000, 10000, 1000, 100, 10, 1 };
/*      */   
/*      */ 
/* 1161 */   private static final int[] hexDivisors = { 268435456, 16777216, 1048576, 65536, 4096, 256, 16, 1 };
/*      */   
/*      */ 
/* 1164 */   private static final long[] decDivisorsL = { 1000000000000000000L, 100000000000000000L, 10000000000000000L, 1000000000000000L, 100000000000000L, 10000000000000L, 1000000000000L, 100000000000L, 10000000000L, 1000000000L, 100000000L, 10000000L, 1000000L, 100000L, 10000L, 1000L, 100L, 10L, 1L };
/*      */   
/*      */ 
/*      */ 
/*      */   public static void putCRLF(ByteBuffer buffer)
/*      */   {
/* 1170 */     buffer.put((byte)13);
/* 1171 */     buffer.put((byte)10);
/*      */   }
/*      */   
/*      */   public static boolean isPrefix(ByteBuffer prefix, ByteBuffer buffer)
/*      */   {
/* 1176 */     if (prefix.remaining() > buffer.remaining())
/* 1177 */       return false;
/* 1178 */     int bi = buffer.position();
/* 1179 */     for (int i = prefix.position(); i < prefix.limit(); i++)
/* 1180 */       if (prefix.get(i) != buffer.get(bi++))
/* 1181 */         return false;
/* 1182 */     return true;
/*      */   }
/*      */   
/*      */   public static ByteBuffer ensureCapacity(ByteBuffer buffer, int capacity)
/*      */   {
/* 1187 */     if (buffer == null) {
/* 1188 */       return allocate(capacity);
/*      */     }
/* 1190 */     if (buffer.capacity() >= capacity) {
/* 1191 */       return buffer;
/*      */     }
/* 1193 */     if (buffer.hasArray()) {
/* 1194 */       return ByteBuffer.wrap(Arrays.copyOfRange(buffer.array(), buffer.arrayOffset(), buffer.arrayOffset() + capacity), buffer.position(), buffer.remaining());
/*      */     }
/* 1196 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\BufferUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */