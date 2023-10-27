/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.zip.Checksum;
/*     */ import net.jpountz.util.SafeUtils;
/*     */ import net.jpountz.xxhash.StreamingXXHash32;
/*     */ import net.jpountz.xxhash.XXHashFactory;
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
/*     */ public final class LZ4BlockInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   private final LZ4FastDecompressor decompressor;
/*     */   private final Checksum checksum;
/*     */   private final boolean stopOnEmptyBlock;
/*     */   private byte[] buffer;
/*     */   private byte[] compressedBuffer;
/*     */   private int originalLen;
/*     */   private int o;
/*     */   private boolean finished;
/*     */   
/*     */   public LZ4BlockInputStream(InputStream in, LZ4FastDecompressor decompressor, Checksum checksum, boolean stopOnEmptyBlock)
/*     */   {
/*  65 */     super(in);
/*  66 */     this.decompressor = decompressor;
/*  67 */     this.checksum = checksum;
/*  68 */     this.stopOnEmptyBlock = stopOnEmptyBlock;
/*  69 */     this.buffer = new byte[0];
/*  70 */     this.compressedBuffer = new byte[LZ4BlockOutputStream.HEADER_LENGTH];
/*  71 */     this.o = (this.originalLen = 0);
/*  72 */     this.finished = false;
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
/*     */   public LZ4BlockInputStream(InputStream in, LZ4FastDecompressor decompressor, Checksum checksum)
/*     */   {
/*  88 */     this(in, decompressor, checksum, true);
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
/*     */   public LZ4BlockInputStream(InputStream in, LZ4FastDecompressor decompressor)
/*     */   {
/* 102 */     this(in, decompressor, XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum(), true);
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
/*     */   public LZ4BlockInputStream(InputStream in, boolean stopOnEmptyBlock)
/*     */   {
/* 116 */     this(in, LZ4Factory.fastestInstance().fastDecompressor(), XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum(), stopOnEmptyBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4BlockInputStream(InputStream in)
/*     */   {
/* 128 */     this(in, LZ4Factory.fastestInstance().fastDecompressor());
/*     */   }
/*     */   
/*     */   public int available() throws IOException
/*     */   {
/* 133 */     return this.originalLen - this.o;
/*     */   }
/*     */   
/*     */   public int read() throws IOException
/*     */   {
/* 138 */     if (this.finished) {
/* 139 */       return -1;
/*     */     }
/* 141 */     if (this.o == this.originalLen) {
/* 142 */       refill();
/*     */     }
/* 144 */     if (this.finished) {
/* 145 */       return -1;
/*     */     }
/* 147 */     return this.buffer[(this.o++)] & 0xFF;
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException
/*     */   {
/* 152 */     SafeUtils.checkRange(b, off, len);
/* 153 */     if (this.finished) {
/* 154 */       return -1;
/*     */     }
/* 156 */     if (this.o == this.originalLen) {
/* 157 */       refill();
/*     */     }
/* 159 */     if (this.finished) {
/* 160 */       return -1;
/*     */     }
/* 162 */     len = Math.min(len, this.originalLen - this.o);
/* 163 */     System.arraycopy(this.buffer, this.o, b, off, len);
/* 164 */     this.o += len;
/* 165 */     return len;
/*     */   }
/*     */   
/*     */   public int read(byte[] b) throws IOException
/*     */   {
/* 170 */     return read(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public long skip(long n) throws IOException
/*     */   {
/* 175 */     if ((n <= 0L) || (this.finished)) {
/* 176 */       return 0L;
/*     */     }
/* 178 */     if (this.o == this.originalLen) {
/* 179 */       refill();
/*     */     }
/* 181 */     if (this.finished) {
/* 182 */       return 0L;
/*     */     }
/* 184 */     int skipped = (int)Math.min(n, this.originalLen - this.o);
/* 185 */     this.o += skipped;
/* 186 */     return skipped;
/*     */   }
/*     */   
/*     */   private void refill() throws IOException {
/*     */     try {
/* 191 */       readFully(this.compressedBuffer, LZ4BlockOutputStream.HEADER_LENGTH);
/*     */     } catch (EOFException e) {
/* 193 */       if (!this.stopOnEmptyBlock) {
/* 194 */         this.finished = true;
/*     */       } else
/* 196 */         throw e;
/*     */     }
/* 198 */     return;
/*     */     
/* 200 */     for (int i = 0; i < LZ4BlockOutputStream.MAGIC_LENGTH; i++) {
/* 201 */       if (this.compressedBuffer[i] != LZ4BlockOutputStream.MAGIC[i]) {
/* 202 */         throw new IOException("Stream is corrupted");
/*     */       }
/*     */     }
/* 205 */     int token = this.compressedBuffer[LZ4BlockOutputStream.MAGIC_LENGTH] & 0xFF;
/* 206 */     int compressionMethod = token & 0xF0;
/* 207 */     int compressionLevel = 10 + (token & 0xF);
/* 208 */     if ((compressionMethod != 16) && (compressionMethod != 32)) {
/* 209 */       throw new IOException("Stream is corrupted");
/*     */     }
/* 211 */     int compressedLen = SafeUtils.readIntLE(this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 1);
/* 212 */     this.originalLen = SafeUtils.readIntLE(this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 5);
/* 213 */     int check = SafeUtils.readIntLE(this.compressedBuffer, LZ4BlockOutputStream.MAGIC_LENGTH + 9);
/* 214 */     assert (LZ4BlockOutputStream.HEADER_LENGTH == LZ4BlockOutputStream.MAGIC_LENGTH + 13);
/* 215 */     if ((this.originalLen > 1 << compressionLevel) || (this.originalLen < 0) || (compressedLen < 0) || ((this.originalLen == 0) && (compressedLen != 0)) || ((this.originalLen != 0) && (compressedLen == 0)) || ((compressionMethod == 16) && (this.originalLen != compressedLen)))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 221 */       throw new IOException("Stream is corrupted");
/*     */     }
/* 223 */     if ((this.originalLen == 0) && (compressedLen == 0)) {
/* 224 */       if (check != 0) {
/* 225 */         throw new IOException("Stream is corrupted");
/*     */       }
/* 227 */       if (!this.stopOnEmptyBlock) {
/* 228 */         refill();
/*     */       } else {
/* 230 */         this.finished = true;
/*     */       }
/* 232 */       return;
/*     */     }
/* 234 */     if (this.buffer.length < this.originalLen) {
/* 235 */       this.buffer = new byte[Math.max(this.originalLen, this.buffer.length * 3 / 2)];
/*     */     }
/* 237 */     switch (compressionMethod) {
/*     */     case 16: 
/* 239 */       readFully(this.buffer, this.originalLen);
/* 240 */       break;
/*     */     case 32: 
/* 242 */       if (this.compressedBuffer.length < compressedLen) {
/* 243 */         this.compressedBuffer = new byte[Math.max(compressedLen, this.compressedBuffer.length * 3 / 2)];
/*     */       }
/* 245 */       readFully(this.compressedBuffer, compressedLen);
/*     */       try {
/* 247 */         int compressedLen2 = this.decompressor.decompress(this.compressedBuffer, 0, this.buffer, 0, this.originalLen);
/* 248 */         if (compressedLen != compressedLen2) {
/* 249 */           throw new IOException("Stream is corrupted");
/*     */         }
/*     */       } catch (LZ4Exception e) {
/* 252 */         throw new IOException("Stream is corrupted", e);
/*     */       }
/*     */     
/*     */     default: 
/* 256 */       throw new AssertionError();
/*     */     }
/* 258 */     this.checksum.reset();
/* 259 */     this.checksum.update(this.buffer, 0, this.originalLen);
/* 260 */     if ((int)this.checksum.getValue() != check) {
/* 261 */       throw new IOException("Stream is corrupted");
/*     */     }
/* 263 */     this.o = 0;
/*     */   }
/*     */   
/*     */   private void readFully(byte[] b, int len) throws IOException {
/* 267 */     int read = 0;
/* 268 */     while (read < len) {
/* 269 */       int r = this.in.read(b, read, len - read);
/* 270 */       if (r < 0) {
/* 271 */         throw new EOFException("Stream ended prematurely");
/*     */       }
/* 273 */       read += r;
/*     */     }
/* 275 */     assert (len == read);
/*     */   }
/*     */   
/*     */   public boolean markSupported()
/*     */   {
/* 280 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void mark(int readlimit) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */     throws IOException
/*     */   {
/* 292 */     throw new IOException("mark/reset not supported");
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 297 */     return getClass().getSimpleName() + "(in=" + this.in + ", decompressor=" + this.decompressor + ", checksum=" + this.checksum + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4BlockInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */