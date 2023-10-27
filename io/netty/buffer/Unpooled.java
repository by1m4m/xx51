/*     */ package io.netty.buffer;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Unpooled
/*     */ {
/*     */   private static final ByteBufAllocator ALLOC;
/*     */   public static final ByteOrder BIG_ENDIAN;
/*     */   public static final ByteOrder LITTLE_ENDIAN;
/*     */   public static final ByteBuf EMPTY_BUFFER;
/*     */   
/*     */   static
/*     */   {
/*  74 */     ALLOC = UnpooledByteBufAllocator.DEFAULT;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  79 */     BIG_ENDIAN = ByteOrder.BIG_ENDIAN;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  84 */     LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  89 */     EMPTY_BUFFER = ALLOC.buffer(0, 0);
/*     */     
/*     */ 
/*  92 */     assert ((EMPTY_BUFFER instanceof EmptyByteBuf)) : "EMPTY_BUFFER must be an EmptyByteBuf.";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf buffer()
/*     */   {
/* 100 */     return ALLOC.heapBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf directBuffer()
/*     */   {
/* 108 */     return ALLOC.directBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf buffer(int initialCapacity)
/*     */   {
/* 117 */     return ALLOC.heapBuffer(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf directBuffer(int initialCapacity)
/*     */   {
/* 126 */     return ALLOC.directBuffer(initialCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf buffer(int initialCapacity, int maxCapacity)
/*     */   {
/* 136 */     return ALLOC.heapBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf directBuffer(int initialCapacity, int maxCapacity)
/*     */   {
/* 146 */     return ALLOC.directBuffer(initialCapacity, maxCapacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(byte[] array)
/*     */   {
/* 155 */     if (array.length == 0) {
/* 156 */       return EMPTY_BUFFER;
/*     */     }
/* 158 */     return new UnpooledHeapByteBuf(ALLOC, array, array.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(byte[] array, int offset, int length)
/*     */   {
/* 167 */     if (length == 0) {
/* 168 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 171 */     if ((offset == 0) && (length == array.length)) {
/* 172 */       return wrappedBuffer(array);
/*     */     }
/*     */     
/* 175 */     return wrappedBuffer(array).slice(offset, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(ByteBuffer buffer)
/*     */   {
/* 184 */     if (!buffer.hasRemaining()) {
/* 185 */       return EMPTY_BUFFER;
/*     */     }
/* 187 */     if ((!buffer.isDirect()) && (buffer.hasArray()))
/* 188 */       return 
/*     */       
/*     */ 
/* 191 */         wrappedBuffer(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining()).order(buffer.order());
/* 192 */     if (PlatformDependent.hasUnsafe()) {
/* 193 */       if (buffer.isReadOnly()) {
/* 194 */         if (buffer.isDirect()) {
/* 195 */           return new ReadOnlyUnsafeDirectByteBuf(ALLOC, buffer);
/*     */         }
/* 197 */         return new ReadOnlyByteBufferBuf(ALLOC, buffer);
/*     */       }
/*     */       
/* 200 */       return new UnpooledUnsafeDirectByteBuf(ALLOC, buffer, buffer.remaining());
/*     */     }
/*     */     
/* 203 */     if (buffer.isReadOnly()) {
/* 204 */       return new ReadOnlyByteBufferBuf(ALLOC, buffer);
/*     */     }
/* 206 */     return new UnpooledDirectByteBuf(ALLOC, buffer, buffer.remaining());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(long memoryAddress, int size, boolean doFree)
/*     */   {
/* 216 */     return new WrappedUnpooledUnsafeDirectByteBuf(ALLOC, memoryAddress, size, doFree);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(ByteBuf buffer)
/*     */   {
/* 228 */     if (buffer.isReadable()) {
/* 229 */       return buffer.slice();
/*     */     }
/* 231 */     buffer.release();
/* 232 */     return EMPTY_BUFFER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(byte[]... arrays)
/*     */   {
/* 242 */     return wrappedBuffer(arrays.length, arrays);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(ByteBuf... buffers)
/*     */   {
/* 253 */     return wrappedBuffer(buffers.length, buffers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(ByteBuffer... buffers)
/*     */   {
/* 262 */     return wrappedBuffer(buffers.length, buffers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(int maxNumComponents, byte[]... arrays)
/*     */   {
/* 271 */     switch (arrays.length) {
/*     */     case 0: 
/*     */       break;
/*     */     case 1: 
/* 275 */       if (arrays[0].length != 0) {
/* 276 */         return wrappedBuffer(arrays[0]);
/*     */       }
/*     */       
/*     */       break;
/*     */     default: 
/* 281 */       List<ByteBuf> components = new ArrayList(arrays.length);
/* 282 */       for (byte[] a : arrays) {
/* 283 */         if (a == null) {
/*     */           break;
/*     */         }
/* 286 */         if (a.length > 0) {
/* 287 */           components.add(wrappedBuffer(a));
/*     */         }
/*     */       }
/*     */       
/* 291 */       if (!components.isEmpty()) {
/* 292 */         return new CompositeByteBuf(ALLOC, false, maxNumComponents, components);
/*     */       }
/*     */       break;
/*     */     }
/* 296 */     return EMPTY_BUFFER;
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
/*     */   public static ByteBuf wrappedBuffer(int maxNumComponents, ByteBuf... buffers)
/*     */   {
/* 309 */     switch (buffers.length) {
/*     */     case 0: 
/*     */       break;
/*     */     case 1: 
/* 313 */       ByteBuf buffer = buffers[0];
/* 314 */       if (buffer.isReadable()) {
/* 315 */         return wrappedBuffer(buffer.order(BIG_ENDIAN));
/*     */       }
/* 317 */       buffer.release();
/*     */       
/* 319 */       break;
/*     */     default: 
/* 321 */       for (int i = 0; i < buffers.length; i++) {
/* 322 */         ByteBuf buf = buffers[i];
/* 323 */         if (buf.isReadable()) {
/* 324 */           return new CompositeByteBuf(ALLOC, false, maxNumComponents, buffers, i, buffers.length);
/*     */         }
/* 326 */         buf.release();
/*     */       }
/*     */     }
/*     */     
/* 330 */     return EMPTY_BUFFER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedBuffer(int maxNumComponents, ByteBuffer... buffers)
/*     */   {
/* 339 */     switch (buffers.length) {
/*     */     case 0: 
/*     */       break;
/*     */     case 1: 
/* 343 */       if (buffers[0].hasRemaining()) {
/* 344 */         return wrappedBuffer(buffers[0].order(BIG_ENDIAN));
/*     */       }
/*     */       
/*     */       break;
/*     */     default: 
/* 349 */       List<ByteBuf> components = new ArrayList(buffers.length);
/* 350 */       for (ByteBuffer b : buffers) {
/* 351 */         if (b == null) {
/*     */           break;
/*     */         }
/* 354 */         if (b.remaining() > 0) {
/* 355 */           components.add(wrappedBuffer(b.order(BIG_ENDIAN)));
/*     */         }
/*     */       }
/*     */       
/* 359 */       if (!components.isEmpty()) {
/* 360 */         return new CompositeByteBuf(ALLOC, false, maxNumComponents, components);
/*     */       }
/*     */       break;
/*     */     }
/* 364 */     return EMPTY_BUFFER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static CompositeByteBuf compositeBuffer()
/*     */   {
/* 371 */     return compositeBuffer(16);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static CompositeByteBuf compositeBuffer(int maxNumComponents)
/*     */   {
/* 378 */     return new CompositeByteBuf(ALLOC, false, maxNumComponents);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(byte[] array)
/*     */   {
/* 387 */     if (array.length == 0) {
/* 388 */       return EMPTY_BUFFER;
/*     */     }
/* 390 */     return wrappedBuffer((byte[])array.clone());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(byte[] array, int offset, int length)
/*     */   {
/* 400 */     if (length == 0) {
/* 401 */       return EMPTY_BUFFER;
/*     */     }
/* 403 */     byte[] copy = new byte[length];
/* 404 */     System.arraycopy(array, offset, copy, 0, length);
/* 405 */     return wrappedBuffer(copy);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(ByteBuffer buffer)
/*     */   {
/* 415 */     int length = buffer.remaining();
/* 416 */     if (length == 0) {
/* 417 */       return EMPTY_BUFFER;
/*     */     }
/* 419 */     byte[] copy = new byte[length];
/*     */     
/*     */ 
/* 422 */     ByteBuffer duplicate = buffer.duplicate();
/* 423 */     duplicate.get(copy);
/* 424 */     return wrappedBuffer(copy).order(duplicate.order());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(ByteBuf buffer)
/*     */   {
/* 434 */     int readable = buffer.readableBytes();
/* 435 */     if (readable > 0) {
/* 436 */       ByteBuf copy = buffer(readable);
/* 437 */       copy.writeBytes(buffer, buffer.readerIndex(), readable);
/* 438 */       return copy;
/*     */     }
/* 440 */     return EMPTY_BUFFER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(byte[]... arrays)
/*     */   {
/* 451 */     switch (arrays.length) {
/*     */     case 0: 
/* 453 */       return EMPTY_BUFFER;
/*     */     case 1: 
/* 455 */       if (arrays[0].length == 0) {
/* 456 */         return EMPTY_BUFFER;
/*     */       }
/* 458 */       return copiedBuffer(arrays[0]);
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 463 */     int length = 0;
/* 464 */     for (byte[] a : arrays) {
/* 465 */       if (Integer.MAX_VALUE - length < a.length) {
/* 466 */         throw new IllegalArgumentException("The total length of the specified arrays is too big.");
/*     */       }
/*     */       
/* 469 */       length += a.length;
/*     */     }
/*     */     
/* 472 */     if (length == 0) {
/* 473 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 476 */     byte[] mergedArray = new byte[length];
/* 477 */     int i = 0; for (int j = 0; i < arrays.length; i++) {
/* 478 */       byte[] a = arrays[i];
/* 479 */       System.arraycopy(a, 0, mergedArray, j, a.length);
/* 480 */       j += a.length;
/*     */     }
/*     */     
/* 483 */     return wrappedBuffer(mergedArray);
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
/*     */   public static ByteBuf copiedBuffer(ByteBuf... buffers)
/*     */   {
/* 497 */     switch (buffers.length) {
/*     */     case 0: 
/* 499 */       return EMPTY_BUFFER;
/*     */     case 1: 
/* 501 */       return copiedBuffer(buffers[0]);
/*     */     }
/*     */     
/*     */     
/* 505 */     ByteOrder order = null;
/* 506 */     int length = 0;
/* 507 */     for (ByteBuf b : buffers) {
/* 508 */       int bLen = b.readableBytes();
/* 509 */       if (bLen > 0)
/*     */       {
/*     */ 
/* 512 */         if (Integer.MAX_VALUE - length < bLen) {
/* 513 */           throw new IllegalArgumentException("The total length of the specified buffers is too big.");
/*     */         }
/*     */         
/* 516 */         length += bLen;
/* 517 */         if (order != null) {
/* 518 */           if (!order.equals(b.order())) {
/* 519 */             throw new IllegalArgumentException("inconsistent byte order");
/*     */           }
/*     */         } else {
/* 522 */           order = b.order();
/*     */         }
/*     */       }
/*     */     }
/* 526 */     if (length == 0) {
/* 527 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 530 */     byte[] mergedArray = new byte[length];
/* 531 */     int i = 0; for (int j = 0; i < buffers.length; i++) {
/* 532 */       ByteBuf b = buffers[i];
/* 533 */       int bLen = b.readableBytes();
/* 534 */       b.getBytes(b.readerIndex(), mergedArray, j, bLen);
/* 535 */       j += bLen;
/*     */     }
/*     */     
/* 538 */     return wrappedBuffer(mergedArray).order(order);
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
/*     */   public static ByteBuf copiedBuffer(ByteBuffer... buffers)
/*     */   {
/* 552 */     switch (buffers.length) {
/*     */     case 0: 
/* 554 */       return EMPTY_BUFFER;
/*     */     case 1: 
/* 556 */       return copiedBuffer(buffers[0]);
/*     */     }
/*     */     
/*     */     
/* 560 */     ByteOrder order = null;
/* 561 */     int length = 0;
/* 562 */     for (ByteBuffer b : buffers) {
/* 563 */       int bLen = b.remaining();
/* 564 */       if (bLen > 0)
/*     */       {
/*     */ 
/* 567 */         if (Integer.MAX_VALUE - length < bLen) {
/* 568 */           throw new IllegalArgumentException("The total length of the specified buffers is too big.");
/*     */         }
/*     */         
/* 571 */         length += bLen;
/* 572 */         if (order != null) {
/* 573 */           if (!order.equals(b.order())) {
/* 574 */             throw new IllegalArgumentException("inconsistent byte order");
/*     */           }
/*     */         } else {
/* 577 */           order = b.order();
/*     */         }
/*     */       }
/*     */     }
/* 581 */     if (length == 0) {
/* 582 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 585 */     byte[] mergedArray = new byte[length];
/* 586 */     int i = 0; for (int j = 0; i < buffers.length; i++)
/*     */     {
/*     */ 
/* 589 */       ByteBuffer b = buffers[i].duplicate();
/* 590 */       int bLen = b.remaining();
/* 591 */       b.get(mergedArray, j, bLen);
/* 592 */       j += bLen;
/*     */     }
/*     */     
/* 595 */     return wrappedBuffer(mergedArray).order(order);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(CharSequence string, Charset charset)
/*     */   {
/* 605 */     if (string == null) {
/* 606 */       throw new NullPointerException("string");
/*     */     }
/*     */     
/* 609 */     if ((string instanceof CharBuffer)) {
/* 610 */       return copiedBuffer((CharBuffer)string, charset);
/*     */     }
/*     */     
/* 613 */     return copiedBuffer(CharBuffer.wrap(string), charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(CharSequence string, int offset, int length, Charset charset)
/*     */   {
/* 624 */     if (string == null) {
/* 625 */       throw new NullPointerException("string");
/*     */     }
/* 627 */     if (length == 0) {
/* 628 */       return EMPTY_BUFFER;
/*     */     }
/*     */     
/* 631 */     if ((string instanceof CharBuffer)) {
/* 632 */       CharBuffer buf = (CharBuffer)string;
/* 633 */       if (buf.hasArray()) {
/* 634 */         return copiedBuffer(buf
/* 635 */           .array(), buf
/* 636 */           .arrayOffset() + buf.position() + offset, length, charset);
/*     */       }
/*     */       
/*     */ 
/* 640 */       buf = buf.slice();
/* 641 */       buf.limit(length);
/* 642 */       buf.position(offset);
/* 643 */       return copiedBuffer(buf, charset);
/*     */     }
/*     */     
/* 646 */     return copiedBuffer(CharBuffer.wrap(string, offset, offset + length), charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(char[] array, Charset charset)
/*     */   {
/* 656 */     if (array == null) {
/* 657 */       throw new NullPointerException("array");
/*     */     }
/* 659 */     return copiedBuffer(array, 0, array.length, charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf copiedBuffer(char[] array, int offset, int length, Charset charset)
/*     */   {
/* 669 */     if (array == null) {
/* 670 */       throw new NullPointerException("array");
/*     */     }
/* 672 */     if (length == 0) {
/* 673 */       return EMPTY_BUFFER;
/*     */     }
/* 675 */     return copiedBuffer(CharBuffer.wrap(array, offset, length), charset);
/*     */   }
/*     */   
/*     */   private static ByteBuf copiedBuffer(CharBuffer buffer, Charset charset) {
/* 679 */     return ByteBufUtil.encodeString0(ALLOC, true, buffer, charset, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static ByteBuf unmodifiableBuffer(ByteBuf buffer)
/*     */   {
/* 692 */     ByteOrder endianness = buffer.order();
/* 693 */     if (endianness == BIG_ENDIAN) {
/* 694 */       return new ReadOnlyByteBuf(buffer);
/*     */     }
/*     */     
/* 697 */     return new ReadOnlyByteBuf(buffer.order(BIG_ENDIAN)).order(LITTLE_ENDIAN);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyInt(int value)
/*     */   {
/* 704 */     ByteBuf buf = buffer(4);
/* 705 */     buf.writeInt(value);
/* 706 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyInt(int... values)
/*     */   {
/* 713 */     if ((values == null) || (values.length == 0)) {
/* 714 */       return EMPTY_BUFFER;
/*     */     }
/* 716 */     ByteBuf buffer = buffer(values.length * 4);
/* 717 */     for (int v : values) {
/* 718 */       buffer.writeInt(v);
/*     */     }
/* 720 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyShort(int value)
/*     */   {
/* 727 */     ByteBuf buf = buffer(2);
/* 728 */     buf.writeShort(value);
/* 729 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyShort(short... values)
/*     */   {
/* 736 */     if ((values == null) || (values.length == 0)) {
/* 737 */       return EMPTY_BUFFER;
/*     */     }
/* 739 */     ByteBuf buffer = buffer(values.length * 2);
/* 740 */     for (int v : values) {
/* 741 */       buffer.writeShort(v);
/*     */     }
/* 743 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyShort(int... values)
/*     */   {
/* 750 */     if ((values == null) || (values.length == 0)) {
/* 751 */       return EMPTY_BUFFER;
/*     */     }
/* 753 */     ByteBuf buffer = buffer(values.length * 2);
/* 754 */     for (int v : values) {
/* 755 */       buffer.writeShort(v);
/*     */     }
/* 757 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyMedium(int value)
/*     */   {
/* 764 */     ByteBuf buf = buffer(3);
/* 765 */     buf.writeMedium(value);
/* 766 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyMedium(int... values)
/*     */   {
/* 773 */     if ((values == null) || (values.length == 0)) {
/* 774 */       return EMPTY_BUFFER;
/*     */     }
/* 776 */     ByteBuf buffer = buffer(values.length * 3);
/* 777 */     for (int v : values) {
/* 778 */       buffer.writeMedium(v);
/*     */     }
/* 780 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyLong(long value)
/*     */   {
/* 787 */     ByteBuf buf = buffer(8);
/* 788 */     buf.writeLong(value);
/* 789 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyLong(long... values)
/*     */   {
/* 796 */     if ((values == null) || (values.length == 0)) {
/* 797 */       return EMPTY_BUFFER;
/*     */     }
/* 799 */     ByteBuf buffer = buffer(values.length * 8);
/* 800 */     for (long v : values) {
/* 801 */       buffer.writeLong(v);
/*     */     }
/* 803 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyBoolean(boolean value)
/*     */   {
/* 810 */     ByteBuf buf = buffer(1);
/* 811 */     buf.writeBoolean(value);
/* 812 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyBoolean(boolean... values)
/*     */   {
/* 819 */     if ((values == null) || (values.length == 0)) {
/* 820 */       return EMPTY_BUFFER;
/*     */     }
/* 822 */     ByteBuf buffer = buffer(values.length);
/* 823 */     for (boolean v : values) {
/* 824 */       buffer.writeBoolean(v);
/*     */     }
/* 826 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyFloat(float value)
/*     */   {
/* 833 */     ByteBuf buf = buffer(4);
/* 834 */     buf.writeFloat(value);
/* 835 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyFloat(float... values)
/*     */   {
/* 842 */     if ((values == null) || (values.length == 0)) {
/* 843 */       return EMPTY_BUFFER;
/*     */     }
/* 845 */     ByteBuf buffer = buffer(values.length * 4);
/* 846 */     for (float v : values) {
/* 847 */       buffer.writeFloat(v);
/*     */     }
/* 849 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyDouble(double value)
/*     */   {
/* 856 */     ByteBuf buf = buffer(8);
/* 857 */     buf.writeDouble(value);
/* 858 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf copyDouble(double... values)
/*     */   {
/* 865 */     if ((values == null) || (values.length == 0)) {
/* 866 */       return EMPTY_BUFFER;
/*     */     }
/* 868 */     ByteBuf buffer = buffer(values.length * 8);
/* 869 */     for (double v : values) {
/* 870 */       buffer.writeDouble(v);
/*     */     }
/* 872 */     return buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static ByteBuf unreleasableBuffer(ByteBuf buf)
/*     */   {
/* 879 */     return new UnreleasableByteBuf(buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static ByteBuf unmodifiableBuffer(ByteBuf... buffers)
/*     */   {
/* 890 */     return wrappedUnmodifiableBuffer(true, buffers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ByteBuf wrappedUnmodifiableBuffer(ByteBuf... buffers)
/*     */   {
/* 900 */     return wrappedUnmodifiableBuffer(false, buffers);
/*     */   }
/*     */   
/*     */   private static ByteBuf wrappedUnmodifiableBuffer(boolean copy, ByteBuf... buffers) {
/* 904 */     switch (buffers.length) {
/*     */     case 0: 
/* 906 */       return EMPTY_BUFFER;
/*     */     case 1: 
/* 908 */       return buffers[0].asReadOnly();
/*     */     }
/* 910 */     if (copy) {
/* 911 */       buffers = (ByteBuf[])Arrays.copyOf(buffers, buffers.length, ByteBuf[].class);
/*     */     }
/* 913 */     return new FixedCompositeByteBuf(ALLOC, buffers);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\Unpooled.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */