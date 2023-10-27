/*     */ package rdp.gold.brute.rdp.Messages;
/*     */ 
/*     */ import java.security.MessageDigest;
/*     */ 
/*     */ public class MD4 extends MessageDigest implements Cloneable {
/*     */   private static final int BLOCK_LENGTH = 64;
/*   7 */   private int[] context = new int[4];
/*     */   private long count;
/*   9 */   private byte[] buffer = new byte[64];
/*  10 */   private int[] X = new int[16];
/*     */   
/*     */   public MD4() {
/*  13 */     super("MD4");
/*  14 */     engineReset();
/*     */   }
/*     */   
/*     */   private MD4(MD4 md) {
/*  18 */     this();
/*     */     
/*     */ 
/*  21 */     this.count = md.count;
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  25 */     return new MD4(this);
/*     */   }
/*     */   
/*     */   public void engineReset() {
/*  29 */     this.context[0] = 1732584193;
/*  30 */     this.context[1] = -271733879;
/*  31 */     this.context[2] = -1732584194;
/*  32 */     this.context[3] = 271733878;
/*  33 */     this.count = 0L;
/*  34 */     for (int i = 0; i < 64; i++)
/*  35 */       this.buffer[i] = 0;
/*     */   }
/*     */   
/*     */   public void engineUpdate(byte b) {
/*  39 */     int i = (int)(this.count % 64L);
/*  40 */     this.count += 1L;
/*  41 */     this.buffer[i] = b;
/*  42 */     if (i == 63)
/*  43 */       transform(this.buffer, 0);
/*     */   }
/*     */   
/*     */   public void engineUpdate(byte[] input, int offset, int len) {
/*  47 */     if ((offset < 0) || (len < 0) || (offset + len > input.length)) {
/*  48 */       throw new ArrayIndexOutOfBoundsException();
/*     */     }
/*  50 */     int bufferNdx = (int)(this.count % 64L);
/*  51 */     this.count += len;
/*  52 */     int partLen = 64 - bufferNdx;
/*  53 */     int i = 0;
/*  54 */     if (len >= partLen) {
/*  55 */       System.arraycopy(input, offset, this.buffer, bufferNdx, partLen);
/*     */       
/*  57 */       transform(this.buffer, 0);
/*     */       
/*  59 */       for (i = partLen; i + 64 - 1 < len; i += 64)
/*  60 */         transform(input, offset + i);
/*  61 */       bufferNdx = 0;
/*     */     }
/*     */     
/*  64 */     if (i < len)
/*  65 */       System.arraycopy(input, offset + i, this.buffer, bufferNdx, len - i);
/*     */   }
/*     */   
/*     */   public byte[] engineDigest() {
/*  69 */     int bufferNdx = (int)(this.count % 64L);
/*  70 */     int padLen = bufferNdx < 56 ? 56 - bufferNdx : 120 - bufferNdx;
/*     */     
/*  72 */     byte[] tail = new byte[padLen + 8];
/*  73 */     tail[0] = Byte.MIN_VALUE;
/*     */     
/*  75 */     for (int i = 0; i < 8; i++) {
/*  76 */       tail[(padLen + i)] = ((byte)(int)(this.count * 8L >>> 8 * i));
/*     */     }
/*  78 */     engineUpdate(tail, 0, tail.length);
/*     */     
/*  80 */     byte[] result = new byte[16];
/*     */     
/*  82 */     for (int i = 0; i < 4; i++) {
/*  83 */       for (int j = 0; j < 4; j++) {
/*  84 */         result[(i * 4 + j)] = ((byte)(this.context[i] >>> 8 * j));
/*     */       }
/*     */     }
/*     */     
/*  88 */     engineReset();
/*     */     
/*  90 */     return result;
/*     */   }
/*     */   
/*     */   private void transform(byte[] block, int offset) {
/*  94 */     for (int i = 0; i < 16; i++) {
/*  95 */       this.X[i] = (block[(offset++)] & 0xFF | (block[(offset++)] & 0xFF) << 8 | (block[(offset++)] & 0xFF) << 16 | (block[(offset++)] & 0xFF) << 24);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 101 */     int A = this.context[0];
/* 102 */     int B = this.context[1];
/* 103 */     int C = this.context[2];
/* 104 */     int D = this.context[3];
/*     */     
/* 106 */     A = FF(A, B, C, D, this.X[0], 3);
/* 107 */     D = FF(D, A, B, C, this.X[1], 7);
/* 108 */     C = FF(C, D, A, B, this.X[2], 11);
/* 109 */     B = FF(B, C, D, A, this.X[3], 19);
/* 110 */     A = FF(A, B, C, D, this.X[4], 3);
/* 111 */     D = FF(D, A, B, C, this.X[5], 7);
/* 112 */     C = FF(C, D, A, B, this.X[6], 11);
/* 113 */     B = FF(B, C, D, A, this.X[7], 19);
/* 114 */     A = FF(A, B, C, D, this.X[8], 3);
/* 115 */     D = FF(D, A, B, C, this.X[9], 7);
/* 116 */     C = FF(C, D, A, B, this.X[10], 11);
/* 117 */     B = FF(B, C, D, A, this.X[11], 19);
/* 118 */     A = FF(A, B, C, D, this.X[12], 3);
/* 119 */     D = FF(D, A, B, C, this.X[13], 7);
/* 120 */     C = FF(C, D, A, B, this.X[14], 11);
/* 121 */     B = FF(B, C, D, A, this.X[15], 19);
/*     */     
/* 123 */     A = GG(A, B, C, D, this.X[0], 3);
/* 124 */     D = GG(D, A, B, C, this.X[4], 5);
/* 125 */     C = GG(C, D, A, B, this.X[8], 9);
/* 126 */     B = GG(B, C, D, A, this.X[12], 13);
/* 127 */     A = GG(A, B, C, D, this.X[1], 3);
/* 128 */     D = GG(D, A, B, C, this.X[5], 5);
/* 129 */     C = GG(C, D, A, B, this.X[9], 9);
/* 130 */     B = GG(B, C, D, A, this.X[13], 13);
/* 131 */     A = GG(A, B, C, D, this.X[2], 3);
/* 132 */     D = GG(D, A, B, C, this.X[6], 5);
/* 133 */     C = GG(C, D, A, B, this.X[10], 9);
/* 134 */     B = GG(B, C, D, A, this.X[14], 13);
/* 135 */     A = GG(A, B, C, D, this.X[3], 3);
/* 136 */     D = GG(D, A, B, C, this.X[7], 5);
/* 137 */     C = GG(C, D, A, B, this.X[11], 9);
/* 138 */     B = GG(B, C, D, A, this.X[15], 13);
/*     */     
/* 140 */     A = HH(A, B, C, D, this.X[0], 3);
/* 141 */     D = HH(D, A, B, C, this.X[8], 9);
/* 142 */     C = HH(C, D, A, B, this.X[4], 11);
/* 143 */     B = HH(B, C, D, A, this.X[12], 15);
/* 144 */     A = HH(A, B, C, D, this.X[2], 3);
/* 145 */     D = HH(D, A, B, C, this.X[10], 9);
/* 146 */     C = HH(C, D, A, B, this.X[6], 11);
/* 147 */     B = HH(B, C, D, A, this.X[14], 15);
/* 148 */     A = HH(A, B, C, D, this.X[1], 3);
/* 149 */     D = HH(D, A, B, C, this.X[9], 9);
/* 150 */     C = HH(C, D, A, B, this.X[5], 11);
/* 151 */     B = HH(B, C, D, A, this.X[13], 15);
/* 152 */     A = HH(A, B, C, D, this.X[3], 3);
/* 153 */     D = HH(D, A, B, C, this.X[11], 9);
/* 154 */     C = HH(C, D, A, B, this.X[7], 11);
/* 155 */     B = HH(B, C, D, A, this.X[15], 15);
/*     */     
/* 157 */     this.context[0] += A;
/* 158 */     this.context[1] += B;
/* 159 */     this.context[2] += C;
/* 160 */     this.context[3] += D;
/*     */   }
/*     */   
/*     */   private int FF(int a, int b, int c, int d, int x, int s) {
/* 164 */     int t = a + (b & c | (b ^ 0xFFFFFFFF) & d) + x;
/* 165 */     return t << s | t >>> 32 - s;
/*     */   }
/*     */   
/*     */   private int GG(int a, int b, int c, int d, int x, int s) {
/* 169 */     int t = a + (b & (c | d) | c & d) + x + 1518500249;
/* 170 */     return t << s | t >>> 32 - s;
/*     */   }
/*     */   
/*     */   private int HH(int a, int b, int c, int d, int x, int s) {
/* 174 */     int t = a + (b ^ c ^ d) + x + 1859775393;
/* 175 */     return t << s | t >>> 32 - s;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\Messages\MD4.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */