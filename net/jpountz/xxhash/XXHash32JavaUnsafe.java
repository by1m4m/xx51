/*     */ package net.jpountz.xxhash;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import net.jpountz.util.ByteBufferUtils;
/*     */ import net.jpountz.util.UnsafeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class XXHash32JavaUnsafe
/*     */   extends XXHash32
/*     */ {
/*  18 */   public static final XXHash32 INSTANCE = new XXHash32JavaUnsafe();
/*     */   
/*     */ 
/*     */   public int hash(byte[] buf, int off, int len, int seed)
/*     */   {
/*  23 */     UnsafeUtils.checkRange(buf, off, len);
/*     */     
/*  25 */     int end = off + len;
/*     */     int h32;
/*     */     int h32;
/*  28 */     if (len >= 16) {
/*  29 */       int limit = end - 16;
/*  30 */       int v1 = seed + -1640531535 + -2048144777;
/*  31 */       int v2 = seed + -2048144777;
/*  32 */       int v3 = seed + 0;
/*  33 */       int v4 = seed - -1640531535;
/*     */       do {
/*  35 */         v1 += UnsafeUtils.readIntLE(buf, off) * -2048144777;
/*  36 */         v1 = Integer.rotateLeft(v1, 13);
/*  37 */         v1 *= -1640531535;
/*  38 */         off += 4;
/*     */         
/*  40 */         v2 += UnsafeUtils.readIntLE(buf, off) * -2048144777;
/*  41 */         v2 = Integer.rotateLeft(v2, 13);
/*  42 */         v2 *= -1640531535;
/*  43 */         off += 4;
/*     */         
/*  45 */         v3 += UnsafeUtils.readIntLE(buf, off) * -2048144777;
/*  46 */         v3 = Integer.rotateLeft(v3, 13);
/*  47 */         v3 *= -1640531535;
/*  48 */         off += 4;
/*     */         
/*  50 */         v4 += UnsafeUtils.readIntLE(buf, off) * -2048144777;
/*  51 */         v4 = Integer.rotateLeft(v4, 13);
/*  52 */         v4 *= -1640531535;
/*  53 */         off += 4;
/*  54 */       } while (off <= limit);
/*     */       
/*  56 */       h32 = Integer.rotateLeft(v1, 1) + Integer.rotateLeft(v2, 7) + Integer.rotateLeft(v3, 12) + Integer.rotateLeft(v4, 18);
/*     */     } else {
/*  58 */       h32 = seed + 374761393;
/*     */     }
/*     */     
/*  61 */     h32 += len;
/*     */     
/*  63 */     while (off <= end - 4) {
/*  64 */       h32 += UnsafeUtils.readIntLE(buf, off) * -1028477379;
/*  65 */       h32 = Integer.rotateLeft(h32, 17) * 668265263;
/*  66 */       off += 4;
/*     */     }
/*     */     
/*  69 */     while (off < end) {
/*  70 */       h32 += (UnsafeUtils.readByte(buf, off) & 0xFF) * 374761393;
/*  71 */       h32 = Integer.rotateLeft(h32, 11) * -1640531535;
/*  72 */       off++;
/*     */     }
/*     */     
/*  75 */     h32 ^= h32 >>> 15;
/*  76 */     h32 *= -2048144777;
/*  77 */     h32 ^= h32 >>> 13;
/*  78 */     h32 *= -1028477379;
/*  79 */     h32 ^= h32 >>> 16;
/*     */     
/*  81 */     return h32;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hash(ByteBuffer buf, int off, int len, int seed)
/*     */   {
/*  87 */     if (buf.hasArray()) {
/*  88 */       return hash(buf.array(), off + buf.arrayOffset(), len, seed);
/*     */     }
/*  90 */     ByteBufferUtils.checkRange(buf, off, len);
/*  91 */     buf = ByteBufferUtils.inLittleEndianOrder(buf);
/*     */     
/*  93 */     int end = off + len;
/*     */     int h32;
/*     */     int h32;
/*  96 */     if (len >= 16) {
/*  97 */       int limit = end - 16;
/*  98 */       int v1 = seed + -1640531535 + -2048144777;
/*  99 */       int v2 = seed + -2048144777;
/* 100 */       int v3 = seed + 0;
/* 101 */       int v4 = seed - -1640531535;
/*     */       do {
/* 103 */         v1 += ByteBufferUtils.readIntLE(buf, off) * -2048144777;
/* 104 */         v1 = Integer.rotateLeft(v1, 13);
/* 105 */         v1 *= -1640531535;
/* 106 */         off += 4;
/*     */         
/* 108 */         v2 += ByteBufferUtils.readIntLE(buf, off) * -2048144777;
/* 109 */         v2 = Integer.rotateLeft(v2, 13);
/* 110 */         v2 *= -1640531535;
/* 111 */         off += 4;
/*     */         
/* 113 */         v3 += ByteBufferUtils.readIntLE(buf, off) * -2048144777;
/* 114 */         v3 = Integer.rotateLeft(v3, 13);
/* 115 */         v3 *= -1640531535;
/* 116 */         off += 4;
/*     */         
/* 118 */         v4 += ByteBufferUtils.readIntLE(buf, off) * -2048144777;
/* 119 */         v4 = Integer.rotateLeft(v4, 13);
/* 120 */         v4 *= -1640531535;
/* 121 */         off += 4;
/* 122 */       } while (off <= limit);
/*     */       
/* 124 */       h32 = Integer.rotateLeft(v1, 1) + Integer.rotateLeft(v2, 7) + Integer.rotateLeft(v3, 12) + Integer.rotateLeft(v4, 18);
/*     */     } else {
/* 126 */       h32 = seed + 374761393;
/*     */     }
/*     */     
/* 129 */     h32 += len;
/*     */     
/* 131 */     while (off <= end - 4) {
/* 132 */       h32 += ByteBufferUtils.readIntLE(buf, off) * -1028477379;
/* 133 */       h32 = Integer.rotateLeft(h32, 17) * 668265263;
/* 134 */       off += 4;
/*     */     }
/*     */     
/* 137 */     while (off < end) {
/* 138 */       h32 += (ByteBufferUtils.readByte(buf, off) & 0xFF) * 374761393;
/* 139 */       h32 = Integer.rotateLeft(h32, 11) * -1640531535;
/* 140 */       off++;
/*     */     }
/*     */     
/* 143 */     h32 ^= h32 >>> 15;
/* 144 */     h32 *= -2048144777;
/* 145 */     h32 ^= h32 >>> 13;
/* 146 */     h32 *= -1028477379;
/* 147 */     h32 ^= h32 >>> 16;
/*     */     
/* 149 */     return h32;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\XXHash32JavaUnsafe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */