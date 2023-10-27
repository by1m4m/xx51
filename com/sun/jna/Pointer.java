/*      */ package com.sun.jna;
/*      */ 
/*      */ import java.lang.reflect.Array;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
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
/*      */ public class Pointer
/*      */ {
/*      */   public static final int SIZE;
/*      */   
/*      */   static
/*      */   {
/*   41 */     if ((SIZE = Native.POINTER_SIZE) == 0) {
/*   42 */       throw new Error("Native library not initialized");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*   47 */   public static final Pointer NULL = null;
/*      */   protected long peer;
/*      */   
/*      */   public static final Pointer createConstant(long peer) {
/*   51 */     return new Opaque(peer, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final Pointer createConstant(int peer)
/*      */   {
/*   59 */     return new Opaque(peer & 0xFFFFFFFFFFFFFFFF, null);
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
/*      */   public Pointer(long peer)
/*      */   {
/*   73 */     this.peer = peer;
/*      */   }
/*      */   
/*      */   public Pointer share(long offset)
/*      */   {
/*   78 */     return share(offset, 0L);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Pointer share(long offset, long sz)
/*      */   {
/*   85 */     if (offset == 0L) return this;
/*   86 */     return new Pointer(this.peer + offset);
/*      */   }
/*      */   
/*      */   public void clear(long size)
/*      */   {
/*   91 */     setMemory(0L, size, (byte)0);
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
/*      */   public boolean equals(Object o)
/*      */   {
/*  104 */     if (o == this) return true;
/*  105 */     if (o == null) return false;
/*  106 */     return ((o instanceof Pointer)) && (((Pointer)o).peer == this.peer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  116 */     return (int)((this.peer >>> 32) + (this.peer & 0xFFFFFFFFFFFFFFFF));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long indexOf(long offset, byte value)
/*      */   {
/*  128 */     return Native.indexOf(this.peer + offset, value);
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
/*      */   public void read(long offset, byte[] buf, int index, int length)
/*      */   {
/*  141 */     Native.read(this.peer + offset, buf, index, length);
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
/*      */   public void read(long offset, short[] buf, int index, int length)
/*      */   {
/*  154 */     Native.read(this.peer + offset, buf, index, length);
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
/*      */   public void read(long offset, char[] buf, int index, int length)
/*      */   {
/*  167 */     Native.read(this.peer + offset, buf, index, length);
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
/*      */   public void read(long offset, int[] buf, int index, int length)
/*      */   {
/*  180 */     Native.read(this.peer + offset, buf, index, length);
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
/*      */   public void read(long offset, long[] buf, int index, int length)
/*      */   {
/*  193 */     Native.read(this.peer + offset, buf, index, length);
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
/*      */   public void read(long offset, float[] buf, int index, int length)
/*      */   {
/*  206 */     Native.read(this.peer + offset, buf, index, length);
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
/*      */   public void read(long offset, double[] buf, int index, int length)
/*      */   {
/*  219 */     Native.read(this.peer + offset, buf, index, length);
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
/*      */   public void read(long offset, Pointer[] buf, int index, int length)
/*      */   {
/*  232 */     for (int i = 0; i < length; i++) {
/*  233 */       Pointer p = getPointer(offset + i * SIZE);
/*  234 */       Pointer oldp = buf[(i + index)];
/*      */       
/*  236 */       if ((oldp == null) || (p == null) || (p.peer != oldp.peer)) {
/*  237 */         buf[(i + index)] = p;
/*      */       }
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
/*      */   public void write(long offset, byte[] buf, int index, int length)
/*      */   {
/*  258 */     Native.write(this.peer + offset, buf, index, length);
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
/*      */   public void write(long offset, short[] buf, int index, int length)
/*      */   {
/*  272 */     Native.write(this.peer + offset, buf, index, length);
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
/*      */   public void write(long offset, char[] buf, int index, int length)
/*      */   {
/*  286 */     Native.write(this.peer + offset, buf, index, length);
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
/*      */   public void write(long offset, int[] buf, int index, int length)
/*      */   {
/*  300 */     Native.write(this.peer + offset, buf, index, length);
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
/*      */   public void write(long offset, long[] buf, int index, int length)
/*      */   {
/*  314 */     Native.write(this.peer + offset, buf, index, length);
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
/*      */   public void write(long offset, float[] buf, int index, int length)
/*      */   {
/*  328 */     Native.write(this.peer + offset, buf, index, length);
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
/*      */   public void write(long offset, double[] buf, int index, int length)
/*      */   {
/*  342 */     Native.write(this.peer + offset, buf, index, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void write(long bOff, Pointer[] buf, int index, int length)
/*      */   {
/*  353 */     for (int i = 0; i < length; i++) {
/*  354 */       setPointer(bOff + i * SIZE, buf[(index + i)]);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   Object getValue(long offset, Class type, Object currentValue)
/*      */   {
/*  364 */     Object result = null;
/*  365 */     if (Structure.class.isAssignableFrom(type)) {
/*  366 */       Structure s = (Structure)currentValue;
/*  367 */       if (Structure.ByReference.class.isAssignableFrom(type)) {
/*  368 */         s = Structure.updateStructureByReference(type, s, getPointer(offset));
/*      */       }
/*      */       else {
/*  371 */         s.useMemory(this, (int)offset, true);
/*  372 */         s.read();
/*      */       }
/*  374 */       result = s;
/*      */     }
/*  376 */     else if ((type == Boolean.TYPE) || (type == Boolean.class)) {
/*  377 */       result = Function.valueOf(getInt(offset) != 0);
/*      */     }
/*  379 */     else if ((type == Byte.TYPE) || (type == Byte.class)) {
/*  380 */       result = new Byte(getByte(offset));
/*      */     }
/*  382 */     else if ((type == Short.TYPE) || (type == Short.class)) {
/*  383 */       result = new Short(getShort(offset));
/*      */     }
/*  385 */     else if ((type == Character.TYPE) || (type == Character.class)) {
/*  386 */       result = new Character(getChar(offset));
/*      */     }
/*  388 */     else if ((type == Integer.TYPE) || (type == Integer.class)) {
/*  389 */       result = new Integer(getInt(offset));
/*      */     }
/*  391 */     else if ((type == Long.TYPE) || (type == Long.class)) {
/*  392 */       result = new Long(getLong(offset));
/*      */     }
/*  394 */     else if ((type == Float.TYPE) || (type == Float.class)) {
/*  395 */       result = new Float(getFloat(offset));
/*      */     }
/*  397 */     else if ((type == Double.TYPE) || (type == Double.class)) {
/*  398 */       result = new Double(getDouble(offset));
/*      */     }
/*  400 */     else if (Pointer.class.isAssignableFrom(type)) {
/*  401 */       Pointer p = getPointer(offset);
/*  402 */       if (p != null) {
/*  403 */         Pointer oldp = (currentValue instanceof Pointer) ? (Pointer)currentValue : null;
/*      */         
/*  405 */         if ((oldp == null) || (p.peer != oldp.peer)) {
/*  406 */           result = p;
/*      */         } else {
/*  408 */           result = oldp;
/*      */         }
/*      */       }
/*  411 */     } else if (type == String.class) {
/*  412 */       Pointer p = getPointer(offset);
/*  413 */       result = p != null ? p.getString(0L) : null;
/*      */     }
/*  415 */     else if (type == WString.class) {
/*  416 */       Pointer p = getPointer(offset);
/*  417 */       result = p != null ? new WString(p.getWideString(0L)) : null;
/*      */     }
/*  419 */     else if (Callback.class.isAssignableFrom(type))
/*      */     {
/*      */ 
/*  422 */       Pointer fp = getPointer(offset);
/*  423 */       if (fp == null) {
/*  424 */         result = null;
/*      */       }
/*      */       else {
/*  427 */         Callback cb = (Callback)currentValue;
/*  428 */         Pointer oldfp = CallbackReference.getFunctionPointer(cb);
/*  429 */         if (!fp.equals(oldfp)) {
/*  430 */           cb = CallbackReference.getCallback(type, fp);
/*      */         }
/*  432 */         result = cb;
/*      */       }
/*      */     }
/*  435 */     else if ((Platform.HAS_BUFFERS) && (Buffer.class.isAssignableFrom(type))) {
/*  436 */       Pointer bp = getPointer(offset);
/*  437 */       if (bp == null) {
/*  438 */         result = null;
/*      */       }
/*      */       else
/*      */       {
/*  442 */         Pointer oldbp = currentValue == null ? null : Native.getDirectBufferPointer((Buffer)currentValue);
/*  443 */         if ((oldbp == null) || (!oldbp.equals(bp))) {
/*  444 */           throw new IllegalStateException("Can't autogenerate a direct buffer on memory read");
/*      */         }
/*  446 */         result = currentValue;
/*      */       }
/*      */     }
/*  449 */     else if (NativeMapped.class.isAssignableFrom(type)) {
/*  450 */       NativeMapped nm = (NativeMapped)currentValue;
/*  451 */       if (nm != null) {
/*  452 */         Object value = getValue(offset, nm.nativeType(), null);
/*  453 */         result = nm.fromNative(value, new FromNativeContext(type));
/*  454 */         if (nm.equals(result)) {
/*  455 */           result = nm;
/*      */         }
/*      */       }
/*      */       else {
/*  459 */         NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/*  460 */         Object value = getValue(offset, tc.nativeType(), null);
/*  461 */         result = tc.fromNative(value, new FromNativeContext(type));
/*      */       }
/*      */     }
/*  464 */     else if (type.isArray()) {
/*  465 */       result = currentValue;
/*  466 */       if (result == null) {
/*  467 */         throw new IllegalStateException("Need an initialized array");
/*      */       }
/*  469 */       readArray(offset, result, type.getComponentType());
/*      */     }
/*      */     else {
/*  472 */       throw new IllegalArgumentException("Reading \"" + type + "\" from memory is not supported");
/*      */     }
/*      */     
/*  475 */     return result;
/*      */   }
/*      */   
/*      */   private void readArray(long offset, Object o, Class cls)
/*      */   {
/*  480 */     int length = 0;
/*  481 */     length = Array.getLength(o);
/*  482 */     Object result = o;
/*      */     
/*  484 */     if (cls == Byte.TYPE) {
/*  485 */       read(offset, (byte[])result, 0, length);
/*      */     }
/*  487 */     else if (cls == Short.TYPE) {
/*  488 */       read(offset, (short[])result, 0, length);
/*      */     }
/*  490 */     else if (cls == Character.TYPE) {
/*  491 */       read(offset, (char[])result, 0, length);
/*      */     }
/*  493 */     else if (cls == Integer.TYPE) {
/*  494 */       read(offset, (int[])result, 0, length);
/*      */     }
/*  496 */     else if (cls == Long.TYPE) {
/*  497 */       read(offset, (long[])result, 0, length);
/*      */     }
/*  499 */     else if (cls == Float.TYPE) {
/*  500 */       read(offset, (float[])result, 0, length);
/*      */     }
/*  502 */     else if (cls == Double.TYPE) {
/*  503 */       read(offset, (double[])result, 0, length);
/*      */     }
/*  505 */     else if (Pointer.class.isAssignableFrom(cls)) {
/*  506 */       read(offset, (Pointer[])result, 0, length);
/*      */     }
/*  508 */     else if (Structure.class.isAssignableFrom(cls)) {
/*  509 */       Structure[] sarray = (Structure[])result;
/*  510 */       if (Structure.ByReference.class.isAssignableFrom(cls)) {
/*  511 */         Pointer[] parray = getPointerArray(offset, sarray.length);
/*  512 */         for (int i = 0; i < sarray.length; i++) {
/*  513 */           sarray[i] = Structure.updateStructureByReference(cls, sarray[i], parray[i]);
/*      */         }
/*      */       }
/*      */       else {
/*  517 */         Structure first = sarray[0];
/*  518 */         if (first == null) {
/*  519 */           first = Structure.newInstance(cls, share(offset));
/*  520 */           first.conditionalAutoRead();
/*  521 */           sarray[0] = first;
/*      */         }
/*      */         else {
/*  524 */           first.useMemory(this, (int)offset, true);
/*  525 */           first.read();
/*      */         }
/*  527 */         Structure[] tmp = first.toArray(sarray.length);
/*  528 */         for (int i = 1; i < sarray.length; i++) {
/*  529 */           if (sarray[i] == null)
/*      */           {
/*  531 */             sarray[i] = tmp[i];
/*      */           }
/*      */           else {
/*  534 */             sarray[i].useMemory(this, (int)(offset + i * sarray[i].size()), true);
/*  535 */             sarray[i].read();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  540 */     else if (NativeMapped.class.isAssignableFrom(cls)) {
/*  541 */       NativeMapped[] array = (NativeMapped[])result;
/*  542 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
/*  543 */       int size = Native.getNativeSize(result.getClass(), result) / array.length;
/*  544 */       for (int i = 0; i < array.length; i++) {
/*  545 */         Object value = getValue(offset + size * i, tc.nativeType(), array[i]);
/*  546 */         array[i] = ((NativeMapped)tc.fromNative(value, new FromNativeContext(cls)));
/*      */       }
/*      */     }
/*      */     else {
/*  550 */       throw new IllegalArgumentException("Reading array of " + cls + " from memory not supported");
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
/*      */   public byte getByte(long offset)
/*      */   {
/*  565 */     return Native.getByte(this.peer + offset);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char getChar(long offset)
/*      */   {
/*  577 */     return Native.getChar(this.peer + offset);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public short getShort(long offset)
/*      */   {
/*  589 */     return Native.getShort(this.peer + offset);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getInt(long offset)
/*      */   {
/*  601 */     return Native.getInt(this.peer + offset);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getLong(long offset)
/*      */   {
/*  613 */     return Native.getLong(this.peer + offset);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public NativeLong getNativeLong(long offset)
/*      */   {
/*  625 */     return new NativeLong(NativeLong.SIZE == 8 ? getLong(offset) : getInt(offset));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public float getFloat(long offset)
/*      */   {
/*  637 */     return Native.getFloat(this.peer + offset);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getDouble(long offset)
/*      */   {
/*  649 */     return Native.getDouble(this.peer + offset);
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
/*      */   public Pointer getPointer(long offset)
/*      */   {
/*  663 */     return Native.getPointer(this.peer + offset);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ByteBuffer getByteBuffer(long offset, long length)
/*      */   {
/*  675 */     return Native.getDirectByteBuffer(this.peer + offset, length).order(ByteOrder.nativeOrder());
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String getString(long offset, boolean wide)
/*      */   {
/*  691 */     return wide ? getWideString(offset) : getString(offset);
/*      */   }
/*      */   
/*      */   public String getWideString(long offset)
/*      */   {
/*  696 */     return Native.getWideString(this.peer + offset);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getString(long offset)
/*      */   {
/*  707 */     return getString(offset, Native.getDefaultStringEncoding());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getString(long offset, String encoding)
/*      */   {
/*  718 */     return Native.getString(this.peer + offset, encoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public byte[] getByteArray(long offset, int arraySize)
/*      */   {
/*  725 */     byte[] buf = new byte[arraySize];
/*  726 */     read(offset, buf, 0, arraySize);
/*  727 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public char[] getCharArray(long offset, int arraySize)
/*      */   {
/*  734 */     char[] buf = new char[arraySize];
/*  735 */     read(offset, buf, 0, arraySize);
/*  736 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public short[] getShortArray(long offset, int arraySize)
/*      */   {
/*  743 */     short[] buf = new short[arraySize];
/*  744 */     read(offset, buf, 0, arraySize);
/*  745 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int[] getIntArray(long offset, int arraySize)
/*      */   {
/*  752 */     int[] buf = new int[arraySize];
/*  753 */     read(offset, buf, 0, arraySize);
/*  754 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long[] getLongArray(long offset, int arraySize)
/*      */   {
/*  761 */     long[] buf = new long[arraySize];
/*  762 */     read(offset, buf, 0, arraySize);
/*  763 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public float[] getFloatArray(long offset, int arraySize)
/*      */   {
/*  770 */     float[] buf = new float[arraySize];
/*  771 */     read(offset, buf, 0, arraySize);
/*  772 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public double[] getDoubleArray(long offset, int arraySize)
/*      */   {
/*  779 */     double[] buf = new double[arraySize];
/*  780 */     read(offset, buf, 0, arraySize);
/*  781 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Pointer[] getPointerArray(long offset)
/*      */   {
/*  788 */     List array = new ArrayList();
/*  789 */     int addOffset = 0;
/*  790 */     Pointer p = getPointer(offset);
/*  791 */     while (p != null) {
/*  792 */       array.add(p);
/*  793 */       addOffset += SIZE;
/*  794 */       p = getPointer(offset + addOffset);
/*      */     }
/*  796 */     return (Pointer[])array.toArray(new Pointer[array.size()]);
/*      */   }
/*      */   
/*      */   public Pointer[] getPointerArray(long offset, int arraySize)
/*      */   {
/*  801 */     Pointer[] buf = new Pointer[arraySize];
/*  802 */     read(offset, buf, 0, arraySize);
/*  803 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getStringArray(long offset)
/*      */   {
/*  814 */     return getStringArray(offset, -1, Native.getDefaultStringEncoding());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getStringArray(long offset, String encoding)
/*      */   {
/*  822 */     return getStringArray(offset, -1, encoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getStringArray(long offset, int length)
/*      */   {
/*  832 */     return getStringArray(offset, length, Native.getDefaultStringEncoding());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String[] getStringArray(long offset, boolean wide)
/*      */   {
/*  844 */     return getStringArray(offset, -1, wide);
/*      */   }
/*      */   
/*      */   public String[] getWideStringArray(long offset) {
/*  848 */     return getWideStringArray(offset, -1);
/*      */   }
/*      */   
/*      */   public String[] getWideStringArray(long offset, int length) {
/*  852 */     return getStringArray(offset, length, "--WIDE-STRING--");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public String[] getStringArray(long offset, int length, boolean wide)
/*      */   {
/*  863 */     return getStringArray(offset, length, wide ? "--WIDE-STRING--" : Native.getDefaultStringEncoding());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] getStringArray(long offset, int length, String encoding)
/*      */   {
/*  874 */     List strings = new ArrayList();
/*      */     
/*  876 */     int addOffset = 0;
/*  877 */     if (length != -1) {
/*  878 */       Pointer p = getPointer(offset + addOffset);
/*  879 */       int count = 0;
/*  880 */       while (count++ < length)
/*      */       {
/*      */ 
/*      */ 
/*  884 */         String s = encoding == "--WIDE-STRING--" ? p.getWideString(0L) : p == null ? null : p.getString(0L, encoding);
/*  885 */         strings.add(s);
/*  886 */         if (count < length) {
/*  887 */           addOffset += SIZE;
/*  888 */           p = getPointer(offset + addOffset);
/*      */         }
/*      */       }
/*      */     } else {
/*      */       Pointer p;
/*  893 */       while ((p = getPointer(offset + addOffset)) != null)
/*      */       {
/*      */ 
/*      */ 
/*  897 */         String s = encoding == "--WIDE-STRING--" ? p.getWideString(0L) : p == null ? null : p.getString(0L, encoding);
/*  898 */         strings.add(s);
/*  899 */         addOffset += SIZE;
/*      */       }
/*      */     }
/*  902 */     return (String[])strings.toArray(new String[strings.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void setValue(long offset, Object value, Class type)
/*      */   {
/*  912 */     if ((type == Boolean.TYPE) || (type == Boolean.class)) {
/*  913 */       setInt(offset, Boolean.TRUE.equals(value) ? -1 : 0);
/*      */     }
/*  915 */     else if ((type == Byte.TYPE) || (type == Byte.class)) {
/*  916 */       setByte(offset, value == null ? 0 : ((Byte)value).byteValue());
/*      */     }
/*  918 */     else if ((type == Short.TYPE) || (type == Short.class)) {
/*  919 */       setShort(offset, value == null ? 0 : ((Short)value).shortValue());
/*      */     }
/*  921 */     else if ((type == Character.TYPE) || (type == Character.class)) {
/*  922 */       setChar(offset, value == null ? '\000' : ((Character)value).charValue());
/*      */     }
/*  924 */     else if ((type == Integer.TYPE) || (type == Integer.class)) {
/*  925 */       setInt(offset, value == null ? 0 : ((Integer)value).intValue());
/*      */     }
/*  927 */     else if ((type == Long.TYPE) || (type == Long.class)) {
/*  928 */       setLong(offset, value == null ? 0L : ((Long)value).longValue());
/*      */     }
/*  930 */     else if ((type == Float.TYPE) || (type == Float.class)) {
/*  931 */       setFloat(offset, value == null ? 0.0F : ((Float)value).floatValue());
/*      */     }
/*  933 */     else if ((type == Double.TYPE) || (type == Double.class)) {
/*  934 */       setDouble(offset, value == null ? 0.0D : ((Double)value).doubleValue());
/*      */     }
/*  936 */     else if (type == Pointer.class) {
/*  937 */       setPointer(offset, (Pointer)value);
/*      */     }
/*  939 */     else if (type == String.class) {
/*  940 */       setPointer(offset, (Pointer)value);
/*      */     }
/*  942 */     else if (type == WString.class) {
/*  943 */       setPointer(offset, (Pointer)value);
/*      */     }
/*  945 */     else if (Structure.class.isAssignableFrom(type)) {
/*  946 */       Structure s = (Structure)value;
/*  947 */       if (Structure.ByReference.class.isAssignableFrom(type)) {
/*  948 */         setPointer(offset, s == null ? null : s.getPointer());
/*  949 */         if (s != null) {
/*  950 */           s.autoWrite();
/*      */         }
/*      */       }
/*      */       else {
/*  954 */         s.useMemory(this, (int)offset, true);
/*  955 */         s.write();
/*      */       }
/*      */     }
/*  958 */     else if (Callback.class.isAssignableFrom(type)) {
/*  959 */       setPointer(offset, CallbackReference.getFunctionPointer((Callback)value));
/*      */     }
/*  961 */     else if ((Platform.HAS_BUFFERS) && (Buffer.class.isAssignableFrom(type)))
/*      */     {
/*  963 */       Pointer p = value == null ? null : Native.getDirectBufferPointer((Buffer)value);
/*  964 */       setPointer(offset, p);
/*      */     }
/*  966 */     else if (NativeMapped.class.isAssignableFrom(type)) {
/*  967 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/*  968 */       Class nativeType = tc.nativeType();
/*  969 */       setValue(offset, tc.toNative(value, new ToNativeContext()), nativeType);
/*      */     }
/*  971 */     else if (type.isArray()) {
/*  972 */       writeArray(offset, value, type.getComponentType());
/*      */     }
/*      */     else {
/*  975 */       throw new IllegalArgumentException("Writing " + type + " to memory is not supported");
/*      */     }
/*      */   }
/*      */   
/*      */   private void writeArray(long offset, Object value, Class cls)
/*      */   {
/*  981 */     if (cls == Byte.TYPE) {
/*  982 */       byte[] buf = (byte[])value;
/*  983 */       write(offset, buf, 0, buf.length);
/*      */     }
/*  985 */     else if (cls == Short.TYPE) {
/*  986 */       short[] buf = (short[])value;
/*  987 */       write(offset, buf, 0, buf.length);
/*      */     }
/*  989 */     else if (cls == Character.TYPE) {
/*  990 */       char[] buf = (char[])value;
/*  991 */       write(offset, buf, 0, buf.length);
/*      */     }
/*  993 */     else if (cls == Integer.TYPE) {
/*  994 */       int[] buf = (int[])value;
/*  995 */       write(offset, buf, 0, buf.length);
/*      */     }
/*  997 */     else if (cls == Long.TYPE) {
/*  998 */       long[] buf = (long[])value;
/*  999 */       write(offset, buf, 0, buf.length);
/*      */     }
/* 1001 */     else if (cls == Float.TYPE) {
/* 1002 */       float[] buf = (float[])value;
/* 1003 */       write(offset, buf, 0, buf.length);
/*      */     }
/* 1005 */     else if (cls == Double.TYPE) {
/* 1006 */       double[] buf = (double[])value;
/* 1007 */       write(offset, buf, 0, buf.length);
/*      */     }
/* 1009 */     else if (Pointer.class.isAssignableFrom(cls)) {
/* 1010 */       Pointer[] buf = (Pointer[])value;
/* 1011 */       write(offset, buf, 0, buf.length);
/*      */     }
/* 1013 */     else if (Structure.class.isAssignableFrom(cls)) {
/* 1014 */       Structure[] sbuf = (Structure[])value;
/* 1015 */       if (Structure.ByReference.class.isAssignableFrom(cls)) {
/* 1016 */         Pointer[] buf = new Pointer[sbuf.length];
/* 1017 */         for (int i = 0; i < sbuf.length; i++) {
/* 1018 */           if (sbuf[i] == null) {
/* 1019 */             buf[i] = null;
/*      */           }
/*      */           else {
/* 1022 */             buf[i] = sbuf[i].getPointer();
/* 1023 */             sbuf[i].write();
/*      */           }
/*      */         }
/* 1026 */         write(offset, buf, 0, buf.length);
/*      */       }
/*      */       else {
/* 1029 */         Structure first = sbuf[0];
/* 1030 */         if (first == null) {
/* 1031 */           first = Structure.newInstance(cls, share(offset));
/* 1032 */           sbuf[0] = first;
/*      */         }
/*      */         else {
/* 1035 */           first.useMemory(this, (int)offset, true);
/*      */         }
/* 1037 */         first.write();
/* 1038 */         Structure[] tmp = first.toArray(sbuf.length);
/* 1039 */         for (int i = 1; i < sbuf.length; i++) {
/* 1040 */           if (sbuf[i] == null) {
/* 1041 */             sbuf[i] = tmp[i];
/*      */           }
/*      */           else {
/* 1044 */             sbuf[i].useMemory(this, (int)(offset + i * sbuf[i].size()), true);
/*      */           }
/* 1046 */           sbuf[i].write();
/*      */         }
/*      */       }
/*      */     }
/* 1050 */     else if (NativeMapped.class.isAssignableFrom(cls)) {
/* 1051 */       NativeMapped[] buf = (NativeMapped[])value;
/* 1052 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
/* 1053 */       Class nativeType = tc.nativeType();
/* 1054 */       int size = Native.getNativeSize(value.getClass(), value) / buf.length;
/* 1055 */       for (int i = 0; i < buf.length; i++) {
/* 1056 */         Object element = tc.toNative(buf[i], new ToNativeContext());
/* 1057 */         setValue(offset + i * size, element, nativeType);
/*      */       }
/*      */     }
/*      */     else {
/* 1061 */       throw new IllegalArgumentException("Writing array of " + cls + " to memory not supported");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMemory(long offset, long length, byte value)
/*      */   {
/* 1072 */     Native.setMemory(this.peer + offset, length, value);
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
/*      */   public void setByte(long offset, byte value)
/*      */   {
/* 1085 */     Native.setByte(this.peer + offset, value);
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
/*      */   public void setShort(long offset, short value)
/*      */   {
/* 1098 */     Native.setShort(this.peer + offset, value);
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
/*      */   public void setChar(long offset, char value)
/*      */   {
/* 1111 */     Native.setChar(this.peer + offset, value);
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
/*      */   public void setInt(long offset, int value)
/*      */   {
/* 1124 */     Native.setInt(this.peer + offset, value);
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
/*      */   public void setLong(long offset, long value)
/*      */   {
/* 1137 */     Native.setLong(this.peer + offset, value);
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
/*      */   public void setNativeLong(long offset, NativeLong value)
/*      */   {
/* 1150 */     if (NativeLong.SIZE == 8) {
/* 1151 */       setLong(offset, value.longValue());
/*      */     } else {
/* 1153 */       setInt(offset, value.intValue());
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
/*      */   public void setFloat(long offset, float value)
/*      */   {
/* 1167 */     Native.setFloat(this.peer + offset, value);
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
/*      */   public void setDouble(long offset, double value)
/*      */   {
/* 1180 */     Native.setDouble(this.peer + offset, value);
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
/*      */   public void setPointer(long offset, Pointer value)
/*      */   {
/* 1195 */     Native.setPointer(this.peer + offset, value != null ? value.peer : 0L);
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
/*      */   /**
/*      */    * @deprecated
/*      */    */
/*      */   public void setString(long offset, String value, boolean wide)
/*      */   {
/* 1212 */     if (wide) {
/* 1213 */       setWideString(offset, value);
/*      */     }
/*      */     else {
/* 1216 */       setString(offset, value);
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
/*      */   public void setWideString(long offset, String value)
/*      */   {
/* 1229 */     Native.setWideString(this.peer + offset, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setString(long offset, WString value)
/*      */   {
/* 1241 */     setWideString(offset, value == null ? null : value.toString());
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
/*      */   public void setString(long offset, String value)
/*      */   {
/* 1254 */     setString(offset, value, Native.getDefaultStringEncoding());
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
/*      */   public void setString(long offset, String value, String encoding)
/*      */   {
/* 1267 */     byte[] data = Native.getBytes(value, encoding);
/* 1268 */     write(offset, data, 0, data.length);
/* 1269 */     setByte(offset + data.length, (byte)0);
/*      */   }
/*      */   
/*      */   public String dump(long offset, int size)
/*      */   {
/* 1274 */     String LS = System.getProperty("line.separator");
/* 1275 */     String contents = "memory dump" + LS;
/* 1276 */     int BYTES_PER_ROW = 4;
/* 1277 */     byte[] buf = getByteArray(offset, size);
/* 1278 */     for (int i = 0; i < buf.length; i++) {
/* 1279 */       if (i % 4 == 0) contents = contents + "[";
/* 1280 */       if ((buf[i] >= 0) && (buf[i] < 16))
/* 1281 */         contents = contents + "0";
/* 1282 */       contents = contents + Integer.toHexString(buf[i] & 0xFF);
/* 1283 */       if ((i % 4 == 3) && (i < buf.length - 1))
/* 1284 */         contents = contents + "]" + LS;
/*      */     }
/* 1286 */     if (!contents.endsWith("]" + LS)) {
/* 1287 */       contents = contents + "]" + LS;
/*      */     }
/* 1289 */     return contents;
/*      */   }
/*      */   
/*      */   public String toString() {
/* 1293 */     return "native@0x" + Long.toHexString(this.peer);
/*      */   }
/*      */   
/*      */   public static long nativeValue(Pointer p)
/*      */   {
/* 1298 */     return p == null ? 0L : p.peer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/* 1303 */   public static void nativeValue(Pointer p, long value) { p.peer = value; }
/*      */   
/*      */   Pointer() {}
/*      */   
/*      */   private static class Opaque extends Pointer {
/* 1308 */     private Opaque(long peer) { super(); }
/* 1309 */     private final String MSG = "This pointer is opaque: " + this;
/*      */     
/* 1311 */     public Pointer share(long offset, long size) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void clear(long size) {
/* 1314 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1317 */     public long indexOf(long offset, byte value) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void read(long bOff, byte[] buf, int index, int length) {
/* 1320 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1323 */     public void read(long bOff, char[] buf, int index, int length) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void read(long bOff, short[] buf, int index, int length) {
/* 1326 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1329 */     public void read(long bOff, int[] buf, int index, int length) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void read(long bOff, long[] buf, int index, int length) {
/* 1332 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1335 */     public void read(long bOff, float[] buf, int index, int length) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void read(long bOff, double[] buf, int index, int length) {
/* 1338 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1341 */     public void read(long bOff, Pointer[] buf, int index, int length) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void write(long bOff, byte[] buf, int index, int length) {
/* 1344 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1347 */     public void write(long bOff, char[] buf, int index, int length) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void write(long bOff, short[] buf, int index, int length) {
/* 1350 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1353 */     public void write(long bOff, int[] buf, int index, int length) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void write(long bOff, long[] buf, int index, int length) {
/* 1356 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1359 */     public void write(long bOff, float[] buf, int index, int length) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void write(long bOff, double[] buf, int index, int length) {
/* 1362 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1365 */     public void write(long bOff, Pointer[] buf, int index, int length) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public ByteBuffer getByteBuffer(long offset, long length) {
/* 1368 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1371 */     public byte getByte(long bOff) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public char getChar(long bOff) {
/* 1374 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1377 */     public short getShort(long bOff) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public int getInt(long bOff) {
/* 1380 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1383 */     public long getLong(long bOff) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public float getFloat(long bOff) {
/* 1386 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1389 */     public double getDouble(long bOff) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public Pointer getPointer(long bOff) {
/* 1392 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1395 */     public String getString(long bOff, String encoding) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public String getWideString(long bOff) {
/* 1398 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1401 */     public void setByte(long bOff, byte value) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void setChar(long bOff, char value) {
/* 1404 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1407 */     public void setShort(long bOff, short value) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void setInt(long bOff, int value) {
/* 1410 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1413 */     public void setLong(long bOff, long value) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void setFloat(long bOff, float value) {
/* 1416 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1419 */     public void setDouble(long bOff, double value) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void setPointer(long offset, Pointer value) {
/* 1422 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1425 */     public void setString(long offset, String value, String encoding) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public void setWideString(long offset, String value) {
/* 1428 */       throw new UnsupportedOperationException(this.MSG);
/*      */     }
/*      */     
/* 1431 */     public void setMemory(long offset, long size, byte value) { throw new UnsupportedOperationException(this.MSG); }
/*      */     
/*      */     public String toString() {
/* 1434 */       return "const@0x" + Long.toHexString(this.peer);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\Pointer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */