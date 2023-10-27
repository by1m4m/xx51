/*     */ package net.jpountz.xxhash;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import net.jpountz.util.ByteBufferUtils;
/*     */ import net.jpountz.util.SafeUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class XXHash64JavaSafe
/*     */   extends XXHash64
/*     */ {
/*  18 */   public static final XXHash64 INSTANCE = new XXHash64JavaSafe();
/*     */   
/*     */ 
/*     */   public long hash(byte[] buf, int off, int len, long seed)
/*     */   {
/*  23 */     SafeUtils.checkRange(buf, off, len);
/*     */     
/*  25 */     int end = off + len;
/*     */     
/*     */     long h64;
/*  28 */     if (len >= 32) {
/*  29 */       int limit = end - 32;
/*  30 */       long v1 = seed + -7046029288634856825L + -4417276706812531889L;
/*  31 */       long v2 = seed + -4417276706812531889L;
/*  32 */       long v3 = seed + 0L;
/*  33 */       long v4 = seed - -7046029288634856825L;
/*     */       do {
/*  35 */         v1 += SafeUtils.readLongLE(buf, off) * -4417276706812531889L;
/*  36 */         v1 = Long.rotateLeft(v1, 31);
/*  37 */         v1 *= -7046029288634856825L;
/*  38 */         off += 8;
/*     */         
/*  40 */         v2 += SafeUtils.readLongLE(buf, off) * -4417276706812531889L;
/*  41 */         v2 = Long.rotateLeft(v2, 31);
/*  42 */         v2 *= -7046029288634856825L;
/*  43 */         off += 8;
/*     */         
/*  45 */         v3 += SafeUtils.readLongLE(buf, off) * -4417276706812531889L;
/*  46 */         v3 = Long.rotateLeft(v3, 31);
/*  47 */         v3 *= -7046029288634856825L;
/*  48 */         off += 8;
/*     */         
/*  50 */         v4 += SafeUtils.readLongLE(buf, off) * -4417276706812531889L;
/*  51 */         v4 = Long.rotateLeft(v4, 31);
/*  52 */         v4 *= -7046029288634856825L;
/*  53 */         off += 8;
/*  54 */       } while (off <= limit);
/*     */       
/*  56 */       long h64 = Long.rotateLeft(v1, 1) + Long.rotateLeft(v2, 7) + Long.rotateLeft(v3, 12) + Long.rotateLeft(v4, 18);
/*     */       
/*  58 */       v1 *= -4417276706812531889L;v1 = Long.rotateLeft(v1, 31);v1 *= -7046029288634856825L;h64 ^= v1;
/*  59 */       h64 = h64 * -7046029288634856825L + -8796714831421723037L;
/*     */       
/*  61 */       v2 *= -4417276706812531889L;v2 = Long.rotateLeft(v2, 31);v2 *= -7046029288634856825L;h64 ^= v2;
/*  62 */       h64 = h64 * -7046029288634856825L + -8796714831421723037L;
/*     */       
/*  64 */       v3 *= -4417276706812531889L;v3 = Long.rotateLeft(v3, 31);v3 *= -7046029288634856825L;h64 ^= v3;
/*  65 */       h64 = h64 * -7046029288634856825L + -8796714831421723037L;
/*     */       
/*  67 */       v4 *= -4417276706812531889L;v4 = Long.rotateLeft(v4, 31);v4 *= -7046029288634856825L;h64 ^= v4;
/*  68 */       h64 = h64 * -7046029288634856825L + -8796714831421723037L;
/*     */     } else {
/*  70 */       h64 = seed + 2870177450012600261L;
/*     */     }
/*     */     
/*  73 */     h64 += len;
/*     */     
/*  75 */     while (off <= end - 8) {
/*  76 */       long k1 = SafeUtils.readLongLE(buf, off);
/*  77 */       k1 *= -4417276706812531889L;k1 = Long.rotateLeft(k1, 31);k1 *= -7046029288634856825L;h64 ^= k1;
/*  78 */       h64 = Long.rotateLeft(h64, 27) * -7046029288634856825L + -8796714831421723037L;
/*  79 */       off += 8;
/*     */     }
/*     */     
/*  82 */     if (off <= end - 4) {
/*  83 */       h64 ^= (SafeUtils.readIntLE(buf, off) & 0xFFFFFFFF) * -7046029288634856825L;
/*  84 */       h64 = Long.rotateLeft(h64, 23) * -4417276706812531889L + 1609587929392839161L;
/*  85 */       off += 4;
/*     */     }
/*     */     
/*  88 */     while (off < end) {
/*  89 */       h64 ^= (SafeUtils.readByte(buf, off) & 0xFF) * 2870177450012600261L;
/*  90 */       h64 = Long.rotateLeft(h64, 11) * -7046029288634856825L;
/*  91 */       off++;
/*     */     }
/*     */     
/*  94 */     h64 ^= h64 >>> 33;
/*  95 */     h64 *= -4417276706812531889L;
/*  96 */     h64 ^= h64 >>> 29;
/*  97 */     h64 *= 1609587929392839161L;
/*  98 */     h64 ^= h64 >>> 32;
/*     */     
/* 100 */     return h64;
/*     */   }
/*     */   
/*     */ 
/*     */   public long hash(ByteBuffer buf, int off, int len, long seed)
/*     */   {
/* 106 */     if (buf.hasArray()) {
/* 107 */       return hash(buf.array(), off + buf.arrayOffset(), len, seed);
/*     */     }
/* 109 */     ByteBufferUtils.checkRange(buf, off, len);
/* 110 */     buf = ByteBufferUtils.inLittleEndianOrder(buf);
/*     */     
/* 112 */     int end = off + len;
/*     */     
/*     */     long h64;
/* 115 */     if (len >= 32) {
/* 116 */       int limit = end - 32;
/* 117 */       long v1 = seed + -7046029288634856825L + -4417276706812531889L;
/* 118 */       long v2 = seed + -4417276706812531889L;
/* 119 */       long v3 = seed + 0L;
/* 120 */       long v4 = seed - -7046029288634856825L;
/*     */       do {
/* 122 */         v1 += ByteBufferUtils.readLongLE(buf, off) * -4417276706812531889L;
/* 123 */         v1 = Long.rotateLeft(v1, 31);
/* 124 */         v1 *= -7046029288634856825L;
/* 125 */         off += 8;
/*     */         
/* 127 */         v2 += ByteBufferUtils.readLongLE(buf, off) * -4417276706812531889L;
/* 128 */         v2 = Long.rotateLeft(v2, 31);
/* 129 */         v2 *= -7046029288634856825L;
/* 130 */         off += 8;
/*     */         
/* 132 */         v3 += ByteBufferUtils.readLongLE(buf, off) * -4417276706812531889L;
/* 133 */         v3 = Long.rotateLeft(v3, 31);
/* 134 */         v3 *= -7046029288634856825L;
/* 135 */         off += 8;
/*     */         
/* 137 */         v4 += ByteBufferUtils.readLongLE(buf, off) * -4417276706812531889L;
/* 138 */         v4 = Long.rotateLeft(v4, 31);
/* 139 */         v4 *= -7046029288634856825L;
/* 140 */         off += 8;
/* 141 */       } while (off <= limit);
/*     */       
/* 143 */       long h64 = Long.rotateLeft(v1, 1) + Long.rotateLeft(v2, 7) + Long.rotateLeft(v3, 12) + Long.rotateLeft(v4, 18);
/*     */       
/* 145 */       v1 *= -4417276706812531889L;v1 = Long.rotateLeft(v1, 31);v1 *= -7046029288634856825L;h64 ^= v1;
/* 146 */       h64 = h64 * -7046029288634856825L + -8796714831421723037L;
/*     */       
/* 148 */       v2 *= -4417276706812531889L;v2 = Long.rotateLeft(v2, 31);v2 *= -7046029288634856825L;h64 ^= v2;
/* 149 */       h64 = h64 * -7046029288634856825L + -8796714831421723037L;
/*     */       
/* 151 */       v3 *= -4417276706812531889L;v3 = Long.rotateLeft(v3, 31);v3 *= -7046029288634856825L;h64 ^= v3;
/* 152 */       h64 = h64 * -7046029288634856825L + -8796714831421723037L;
/*     */       
/* 154 */       v4 *= -4417276706812531889L;v4 = Long.rotateLeft(v4, 31);v4 *= -7046029288634856825L;h64 ^= v4;
/* 155 */       h64 = h64 * -7046029288634856825L + -8796714831421723037L;
/*     */     } else {
/* 157 */       h64 = seed + 2870177450012600261L;
/*     */     }
/*     */     
/* 160 */     h64 += len;
/*     */     
/* 162 */     while (off <= end - 8) {
/* 163 */       long k1 = ByteBufferUtils.readLongLE(buf, off);
/* 164 */       k1 *= -4417276706812531889L;k1 = Long.rotateLeft(k1, 31);k1 *= -7046029288634856825L;h64 ^= k1;
/* 165 */       h64 = Long.rotateLeft(h64, 27) * -7046029288634856825L + -8796714831421723037L;
/* 166 */       off += 8;
/*     */     }
/*     */     
/* 169 */     if (off <= end - 4) {
/* 170 */       h64 ^= (ByteBufferUtils.readIntLE(buf, off) & 0xFFFFFFFF) * -7046029288634856825L;
/* 171 */       h64 = Long.rotateLeft(h64, 23) * -4417276706812531889L + 1609587929392839161L;
/* 172 */       off += 4;
/*     */     }
/*     */     
/* 175 */     while (off < end) {
/* 176 */       h64 ^= (ByteBufferUtils.readByte(buf, off) & 0xFF) * 2870177450012600261L;
/* 177 */       h64 = Long.rotateLeft(h64, 11) * -7046029288634856825L;
/* 178 */       off++;
/*     */     }
/*     */     
/* 181 */     h64 ^= h64 >>> 33;
/* 182 */     h64 *= -4417276706812531889L;
/* 183 */     h64 ^= h64 >>> 29;
/* 184 */     h64 *= 1609587929392839161L;
/* 185 */     h64 ^= h64 >>> 32;
/*     */     
/* 187 */     return h64;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\XXHash64JavaSafe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */