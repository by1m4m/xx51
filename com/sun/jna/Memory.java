/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Memory
/*     */   extends Pointer
/*     */ {
/*  51 */   private static final Map buffers = Collections.synchronizedMap(Platform.HAS_BUFFERS ? new WeakIdentityHashMap() : new HashMap());
/*     */   
/*     */ 
/*  54 */   private static final Map allocatedMemory = Collections.synchronizedMap(new WeakHashMap());
/*     */   
/*     */   protected long size;
/*     */   
/*     */ 
/*     */   public static void purge()
/*     */   {
/*  61 */     buffers.size();
/*     */   }
/*     */   
/*     */   public static void disposeAll()
/*     */   {
/*  66 */     for (Iterator i = allocatedMemory.keySet().iterator(); i.hasNext();) {
/*  67 */       ((Memory)i.next()).dispose();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class SharedMemory
/*     */     extends Memory
/*     */   {
/*     */     public SharedMemory(long offset, long size)
/*     */     {
/*  78 */       this.size = size;
/*  79 */       this.peer = (Memory.this.peer + offset);
/*     */     }
/*     */     
/*     */     protected void dispose() {
/*  83 */       this.peer = 0L;
/*     */     }
/*     */     
/*     */     protected void boundsCheck(long off, long sz) {
/*  87 */       Memory.this.boundsCheck(this.peer - Memory.this.peer + off, sz);
/*     */     }
/*     */     
/*  90 */     public String toString() { return super.toString() + " (shared from " + Memory.this.toString() + ")"; }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Memory(long size)
/*     */   {
/* 100 */     this.size = size;
/* 101 */     if (size <= 0L) {
/* 102 */       throw new IllegalArgumentException("Allocation size must be greater than zero");
/*     */     }
/* 104 */     this.peer = malloc(size);
/* 105 */     if (this.peer == 0L) {
/* 106 */       throw new OutOfMemoryError("Cannot allocate " + size + " bytes");
/*     */     }
/* 108 */     allocatedMemory.put(this, new WeakReference(this));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Memory() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public Pointer share(long offset)
/*     */   {
/* 120 */     return share(offset, size() - offset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Pointer share(long offset, long sz)
/*     */   {
/* 131 */     boundsCheck(offset, sz);
/* 132 */     return new SharedMemory(offset, sz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Memory align(int byteBoundary)
/*     */   {
/* 144 */     if (byteBoundary <= 0) {
/* 145 */       throw new IllegalArgumentException("Byte boundary must be positive: " + byteBoundary);
/*     */     }
/* 147 */     for (int i = 0; i < 32; i++) {
/* 148 */       if (byteBoundary == 1 << i) {
/* 149 */         long mask = byteBoundary - 1L ^ 0xFFFFFFFFFFFFFFFF;
/*     */         
/* 151 */         if ((this.peer & mask) != this.peer) {
/* 152 */           long newPeer = this.peer + byteBoundary - 1L & mask;
/* 153 */           long newSize = this.peer + this.size - newPeer;
/* 154 */           if (newSize <= 0L) {
/* 155 */             throw new IllegalArgumentException("Insufficient memory to align to the requested boundary");
/*     */           }
/* 157 */           return (Memory)share(newPeer - this.peer, newSize);
/*     */         }
/* 159 */         return this;
/*     */       }
/*     */     }
/* 162 */     throw new IllegalArgumentException("Byte boundary must be a power of two");
/*     */   }
/*     */   
/*     */   protected void finalize()
/*     */   {
/* 167 */     dispose();
/*     */   }
/*     */   
/*     */   protected synchronized void dispose()
/*     */   {
/* 172 */     free(this.peer);
/* 173 */     this.peer = 0L;
/* 174 */     allocatedMemory.remove(this);
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 179 */     clear(this.size);
/*     */   }
/*     */   
/*     */   public boolean valid()
/*     */   {
/* 184 */     return this.peer != 0L;
/*     */   }
/*     */   
/*     */   public long size() {
/* 188 */     return this.size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void boundsCheck(long off, long sz)
/*     */   {
/* 197 */     if (off < 0L) {
/* 198 */       throw new IndexOutOfBoundsException("Invalid offset: " + off);
/*     */     }
/* 200 */     if (off + sz > this.size) {
/* 201 */       String msg = "Bounds exceeds available space : size=" + this.size + ", offset=" + (off + sz);
/*     */       
/* 203 */       throw new IndexOutOfBoundsException(msg);
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
/*     */   public void read(long bOff, byte[] buf, int index, int length)
/*     */   {
/* 220 */     boundsCheck(bOff, length * 1L);
/* 221 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, short[] buf, int index, int length)
/*     */   {
/* 234 */     boundsCheck(bOff, length * 2L);
/* 235 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, char[] buf, int index, int length)
/*     */   {
/* 248 */     boundsCheck(bOff, length * 2L);
/* 249 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, int[] buf, int index, int length)
/*     */   {
/* 262 */     boundsCheck(bOff, length * 4L);
/* 263 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, long[] buf, int index, int length)
/*     */   {
/* 276 */     boundsCheck(bOff, length * 8L);
/* 277 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, float[] buf, int index, int length)
/*     */   {
/* 290 */     boundsCheck(bOff, length * 4L);
/* 291 */     super.read(bOff, buf, index, length);
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
/*     */   public void read(long bOff, double[] buf, int index, int length)
/*     */   {
/* 305 */     boundsCheck(bOff, length * 8L);
/* 306 */     super.read(bOff, buf, index, length);
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
/*     */   public void write(long bOff, byte[] buf, int index, int length)
/*     */   {
/* 325 */     boundsCheck(bOff, length * 1L);
/* 326 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, short[] buf, int index, int length)
/*     */   {
/* 339 */     boundsCheck(bOff, length * 2L);
/* 340 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, char[] buf, int index, int length)
/*     */   {
/* 353 */     boundsCheck(bOff, length * 2L);
/* 354 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, int[] buf, int index, int length)
/*     */   {
/* 367 */     boundsCheck(bOff, length * 4L);
/* 368 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, long[] buf, int index, int length)
/*     */   {
/* 381 */     boundsCheck(bOff, length * 8L);
/* 382 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, float[] buf, int index, int length)
/*     */   {
/* 395 */     boundsCheck(bOff, length * 4L);
/* 396 */     super.write(bOff, buf, index, length);
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
/*     */   public void write(long bOff, double[] buf, int index, int length)
/*     */   {
/* 409 */     boundsCheck(bOff, length * 8L);
/* 410 */     super.write(bOff, buf, index, length);
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
/*     */   public byte getByte(long offset)
/*     */   {
/* 429 */     boundsCheck(offset, 1L);
/* 430 */     return super.getByte(offset);
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
/*     */   public char getChar(long offset)
/*     */   {
/* 443 */     boundsCheck(offset, 1L);
/* 444 */     return super.getChar(offset);
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
/*     */   public short getShort(long offset)
/*     */   {
/* 457 */     boundsCheck(offset, 2L);
/* 458 */     return super.getShort(offset);
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
/*     */   public int getInt(long offset)
/*     */   {
/* 471 */     boundsCheck(offset, 4L);
/* 472 */     return super.getInt(offset);
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
/*     */   public long getLong(long offset)
/*     */   {
/* 485 */     boundsCheck(offset, 8L);
/* 486 */     return super.getLong(offset);
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
/*     */   public float getFloat(long offset)
/*     */   {
/* 499 */     boundsCheck(offset, 4L);
/* 500 */     return super.getFloat(offset);
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
/*     */   public double getDouble(long offset)
/*     */   {
/* 513 */     boundsCheck(offset, 8L);
/* 514 */     return super.getDouble(offset);
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
/*     */   public Pointer getPointer(long offset)
/*     */   {
/* 527 */     boundsCheck(offset, Pointer.SIZE);
/* 528 */     return super.getPointer(offset);
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
/*     */   public ByteBuffer getByteBuffer(long offset, long length)
/*     */   {
/* 544 */     boundsCheck(offset, length);
/* 545 */     ByteBuffer b = super.getByteBuffer(offset, length);
/*     */     
/*     */ 
/* 548 */     buffers.put(b, this);
/* 549 */     return b;
/*     */   }
/*     */   
/*     */   public String getString(long offset, String encoding)
/*     */   {
/* 554 */     boundsCheck(offset, 0L);
/* 555 */     return super.getString(offset, encoding);
/*     */   }
/*     */   
/*     */   public String getWideString(long offset)
/*     */   {
/* 560 */     boundsCheck(offset, 0L);
/* 561 */     return super.getWideString(offset);
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
/*     */   public void setByte(long offset, byte value)
/*     */   {
/* 577 */     boundsCheck(offset, 1L);
/* 578 */     super.setByte(offset, value);
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
/*     */   public void setChar(long offset, char value)
/*     */   {
/* 591 */     boundsCheck(offset, Native.WCHAR_SIZE);
/* 592 */     super.setChar(offset, value);
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
/*     */   public void setShort(long offset, short value)
/*     */   {
/* 605 */     boundsCheck(offset, 2L);
/* 606 */     super.setShort(offset, value);
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
/*     */   public void setInt(long offset, int value)
/*     */   {
/* 619 */     boundsCheck(offset, 4L);
/* 620 */     super.setInt(offset, value);
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
/*     */   public void setLong(long offset, long value)
/*     */   {
/* 633 */     boundsCheck(offset, 8L);
/* 634 */     super.setLong(offset, value);
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
/*     */   public void setFloat(long offset, float value)
/*     */   {
/* 647 */     boundsCheck(offset, 4L);
/* 648 */     super.setFloat(offset, value);
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
/*     */   public void setDouble(long offset, double value)
/*     */   {
/* 661 */     boundsCheck(offset, 8L);
/* 662 */     super.setDouble(offset, value);
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
/*     */   public void setPointer(long offset, Pointer value)
/*     */   {
/* 675 */     boundsCheck(offset, Pointer.SIZE);
/* 676 */     super.setPointer(offset, value);
/*     */   }
/*     */   
/*     */   public void setString(long offset, String value, String encoding) {
/* 680 */     boundsCheck(offset, Native.getBytes(value, encoding).length + 1L);
/* 681 */     super.setString(offset, value, encoding);
/*     */   }
/*     */   
/*     */   public void setWideString(long offset, String value) {
/* 685 */     boundsCheck(offset, (value.length() + 1L) * Native.WCHAR_SIZE);
/* 686 */     super.setWideString(offset, value);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 690 */     return "allocated@0x" + Long.toHexString(this.peer) + " (" + this.size + " bytes)";
/*     */   }
/*     */   
/*     */ 
/*     */   protected static void free(long p)
/*     */   {
/* 696 */     if (p != 0L) {
/* 697 */       Native.free(p);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static long malloc(long size) {
/* 702 */     return Native.malloc(size);
/*     */   }
/*     */   
/*     */   public String dump()
/*     */   {
/* 707 */     return dump(0L, (int)size());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\Memory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */