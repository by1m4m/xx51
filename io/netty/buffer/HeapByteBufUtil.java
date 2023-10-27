/*     */ package io.netty.buffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class HeapByteBufUtil
/*     */ {
/*     */   static byte getByte(byte[] memory, int index)
/*     */   {
/*  24 */     return memory[index];
/*     */   }
/*     */   
/*     */   static short getShort(byte[] memory, int index) {
/*  28 */     return (short)(memory[index] << 8 | memory[(index + 1)] & 0xFF);
/*     */   }
/*     */   
/*     */   static short getShortLE(byte[] memory, int index) {
/*  32 */     return (short)(memory[index] & 0xFF | memory[(index + 1)] << 8);
/*     */   }
/*     */   
/*     */   static int getUnsignedMedium(byte[] memory, int index) {
/*  36 */     return (memory[index] & 0xFF) << 16 | (memory[(index + 1)] & 0xFF) << 8 | memory[(index + 2)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */   static int getUnsignedMediumLE(byte[] memory, int index)
/*     */   {
/*  42 */     return memory[index] & 0xFF | (memory[(index + 1)] & 0xFF) << 8 | (memory[(index + 2)] & 0xFF) << 16;
/*     */   }
/*     */   
/*     */ 
/*     */   static int getInt(byte[] memory, int index)
/*     */   {
/*  48 */     return (memory[index] & 0xFF) << 24 | (memory[(index + 1)] & 0xFF) << 16 | (memory[(index + 2)] & 0xFF) << 8 | memory[(index + 3)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static int getIntLE(byte[] memory, int index)
/*     */   {
/*  55 */     return memory[index] & 0xFF | (memory[(index + 1)] & 0xFF) << 8 | (memory[(index + 2)] & 0xFF) << 16 | (memory[(index + 3)] & 0xFF) << 24;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static long getLong(byte[] memory, int index)
/*     */   {
/*  62 */     return (memory[index] & 0xFF) << 56 | (memory[(index + 1)] & 0xFF) << 48 | (memory[(index + 2)] & 0xFF) << 40 | (memory[(index + 3)] & 0xFF) << 32 | (memory[(index + 4)] & 0xFF) << 24 | (memory[(index + 5)] & 0xFF) << 16 | (memory[(index + 6)] & 0xFF) << 8 | memory[(index + 7)] & 0xFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static long getLongLE(byte[] memory, int index)
/*     */   {
/*  73 */     return memory[index] & 0xFF | (memory[(index + 1)] & 0xFF) << 8 | (memory[(index + 2)] & 0xFF) << 16 | (memory[(index + 3)] & 0xFF) << 24 | (memory[(index + 4)] & 0xFF) << 32 | (memory[(index + 5)] & 0xFF) << 40 | (memory[(index + 6)] & 0xFF) << 48 | (memory[(index + 7)] & 0xFF) << 56;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void setByte(byte[] memory, int index, int value)
/*     */   {
/*  84 */     memory[index] = ((byte)value);
/*     */   }
/*     */   
/*     */   static void setShort(byte[] memory, int index, int value) {
/*  88 */     memory[index] = ((byte)(value >>> 8));
/*  89 */     memory[(index + 1)] = ((byte)value);
/*     */   }
/*     */   
/*     */   static void setShortLE(byte[] memory, int index, int value) {
/*  93 */     memory[index] = ((byte)value);
/*  94 */     memory[(index + 1)] = ((byte)(value >>> 8));
/*     */   }
/*     */   
/*     */   static void setMedium(byte[] memory, int index, int value) {
/*  98 */     memory[index] = ((byte)(value >>> 16));
/*  99 */     memory[(index + 1)] = ((byte)(value >>> 8));
/* 100 */     memory[(index + 2)] = ((byte)value);
/*     */   }
/*     */   
/*     */   static void setMediumLE(byte[] memory, int index, int value) {
/* 104 */     memory[index] = ((byte)value);
/* 105 */     memory[(index + 1)] = ((byte)(value >>> 8));
/* 106 */     memory[(index + 2)] = ((byte)(value >>> 16));
/*     */   }
/*     */   
/*     */   static void setInt(byte[] memory, int index, int value) {
/* 110 */     memory[index] = ((byte)(value >>> 24));
/* 111 */     memory[(index + 1)] = ((byte)(value >>> 16));
/* 112 */     memory[(index + 2)] = ((byte)(value >>> 8));
/* 113 */     memory[(index + 3)] = ((byte)value);
/*     */   }
/*     */   
/*     */   static void setIntLE(byte[] memory, int index, int value) {
/* 117 */     memory[index] = ((byte)value);
/* 118 */     memory[(index + 1)] = ((byte)(value >>> 8));
/* 119 */     memory[(index + 2)] = ((byte)(value >>> 16));
/* 120 */     memory[(index + 3)] = ((byte)(value >>> 24));
/*     */   }
/*     */   
/*     */   static void setLong(byte[] memory, int index, long value) {
/* 124 */     memory[index] = ((byte)(int)(value >>> 56));
/* 125 */     memory[(index + 1)] = ((byte)(int)(value >>> 48));
/* 126 */     memory[(index + 2)] = ((byte)(int)(value >>> 40));
/* 127 */     memory[(index + 3)] = ((byte)(int)(value >>> 32));
/* 128 */     memory[(index + 4)] = ((byte)(int)(value >>> 24));
/* 129 */     memory[(index + 5)] = ((byte)(int)(value >>> 16));
/* 130 */     memory[(index + 6)] = ((byte)(int)(value >>> 8));
/* 131 */     memory[(index + 7)] = ((byte)(int)value);
/*     */   }
/*     */   
/*     */   static void setLongLE(byte[] memory, int index, long value) {
/* 135 */     memory[index] = ((byte)(int)value);
/* 136 */     memory[(index + 1)] = ((byte)(int)(value >>> 8));
/* 137 */     memory[(index + 2)] = ((byte)(int)(value >>> 16));
/* 138 */     memory[(index + 3)] = ((byte)(int)(value >>> 24));
/* 139 */     memory[(index + 4)] = ((byte)(int)(value >>> 32));
/* 140 */     memory[(index + 5)] = ((byte)(int)(value >>> 40));
/* 141 */     memory[(index + 6)] = ((byte)(int)(value >>> 48));
/* 142 */     memory[(index + 7)] = ((byte)(int)(value >>> 56));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\HeapByteBufUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */