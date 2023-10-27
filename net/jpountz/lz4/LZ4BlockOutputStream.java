/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public final class LZ4BlockOutputStream
/*     */   extends FilterOutputStream
/*     */ {
/*  37 */   static final byte[] MAGIC = { 76, 90, 52, 66, 108, 111, 99, 107 };
/*  38 */   static final int MAGIC_LENGTH = MAGIC.length;
/*     */   
/*  40 */   static final int HEADER_LENGTH = MAGIC_LENGTH + 1 + 4 + 4 + 4;
/*     */   static final int COMPRESSION_LEVEL_BASE = 10;
/*     */   static final int MIN_BLOCK_SIZE = 64;
/*     */   static final int MAX_BLOCK_SIZE = 33554432;
/*     */   static final int COMPRESSION_METHOD_RAW = 16;
/*     */   static final int COMPRESSION_METHOD_LZ4 = 32;
/*     */   static final int DEFAULT_SEED = -1756908916;
/*     */   private final int blockSize;
/*     */   private final int compressionLevel;
/*     */   private final LZ4Compressor compressor;
/*     */   private final Checksum checksum;
/*     */   private final byte[] buffer;
/*     */   private final byte[] compressedBuffer;
/*     */   private final boolean syncFlush;
/*     */   private boolean finished;
/*     */   private int o;
/*     */   
/*  57 */   private static int compressionLevel(int blockSize) { if (blockSize < 64)
/*  58 */       throw new IllegalArgumentException("blockSize must be >= 64, got " + blockSize);
/*  59 */     if (blockSize > 33554432) {
/*  60 */       throw new IllegalArgumentException("blockSize must be <= 33554432, got " + blockSize);
/*     */     }
/*  62 */     int compressionLevel = 32 - Integer.numberOfLeadingZeros(blockSize - 1);
/*  63 */     assert (1 << compressionLevel >= blockSize);
/*  64 */     assert (blockSize * 2 > 1 << compressionLevel);
/*  65 */     compressionLevel = Math.max(0, compressionLevel - 10);
/*  66 */     assert ((compressionLevel >= 0) && (compressionLevel <= 15));
/*  67 */     return compressionLevel;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4BlockOutputStream(OutputStream out, int blockSize, LZ4Compressor compressor, Checksum checksum, boolean syncFlush)
/*     */   {
/*  95 */     super(out);
/*  96 */     this.blockSize = blockSize;
/*  97 */     this.compressor = compressor;
/*  98 */     this.checksum = checksum;
/*  99 */     this.compressionLevel = compressionLevel(blockSize);
/* 100 */     this.buffer = new byte[blockSize];
/* 101 */     int compressedBlockSize = HEADER_LENGTH + compressor.maxCompressedLength(blockSize);
/* 102 */     this.compressedBuffer = new byte[compressedBlockSize];
/* 103 */     this.syncFlush = syncFlush;
/* 104 */     this.o = 0;
/* 105 */     this.finished = false;
/* 106 */     System.arraycopy(MAGIC, 0, this.compressedBuffer, 0, MAGIC_LENGTH);
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
/*     */   public LZ4BlockOutputStream(OutputStream out, int blockSize, LZ4Compressor compressor)
/*     */   {
/* 123 */     this(out, blockSize, compressor, XXHashFactory.fastestInstance().newStreamingHash32(-1756908916).asChecksum(), false);
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
/*     */   public LZ4BlockOutputStream(OutputStream out, int blockSize)
/*     */   {
/* 138 */     this(out, blockSize, LZ4Factory.fastestInstance().fastCompressor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4BlockOutputStream(OutputStream out)
/*     */   {
/* 149 */     this(out, 65536);
/*     */   }
/*     */   
/*     */   private void ensureNotFinished() {
/* 153 */     if (this.finished) {
/* 154 */       throw new IllegalStateException("This stream is already closed");
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(int b) throws IOException
/*     */   {
/* 160 */     ensureNotFinished();
/* 161 */     if (this.o == this.blockSize) {
/* 162 */       flushBufferedData();
/*     */     }
/* 164 */     this.buffer[(this.o++)] = ((byte)b);
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException
/*     */   {
/* 169 */     SafeUtils.checkRange(b, off, len);
/* 170 */     ensureNotFinished();
/*     */     
/* 172 */     while (this.o + len > this.blockSize) {
/* 173 */       int l = this.blockSize - this.o;
/* 174 */       System.arraycopy(b, off, this.buffer, this.o, this.blockSize - this.o);
/* 175 */       this.o = this.blockSize;
/* 176 */       flushBufferedData();
/* 177 */       off += l;
/* 178 */       len -= l;
/*     */     }
/* 180 */     System.arraycopy(b, off, this.buffer, this.o, len);
/* 181 */     this.o += len;
/*     */   }
/*     */   
/*     */   public void write(byte[] b) throws IOException
/*     */   {
/* 186 */     ensureNotFinished();
/* 187 */     write(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 192 */     if (!this.finished) {
/* 193 */       finish();
/*     */     }
/* 195 */     if (this.out != null) {
/* 196 */       this.out.close();
/* 197 */       this.out = null;
/*     */     }
/*     */   }
/*     */   
/*     */   private void flushBufferedData() throws IOException {
/* 202 */     if (this.o == 0) {
/* 203 */       return;
/*     */     }
/* 205 */     this.checksum.reset();
/* 206 */     this.checksum.update(this.buffer, 0, this.o);
/* 207 */     int check = (int)this.checksum.getValue();
/* 208 */     int compressedLength = this.compressor.compress(this.buffer, 0, this.o, this.compressedBuffer, HEADER_LENGTH);
/*     */     int compressMethod;
/* 210 */     if (compressedLength >= this.o) {
/* 211 */       int compressMethod = 16;
/* 212 */       compressedLength = this.o;
/* 213 */       System.arraycopy(this.buffer, 0, this.compressedBuffer, HEADER_LENGTH, this.o);
/*     */     } else {
/* 215 */       compressMethod = 32;
/*     */     }
/*     */     
/* 218 */     this.compressedBuffer[MAGIC_LENGTH] = ((byte)(compressMethod | this.compressionLevel));
/* 219 */     writeIntLE(compressedLength, this.compressedBuffer, MAGIC_LENGTH + 1);
/* 220 */     writeIntLE(this.o, this.compressedBuffer, MAGIC_LENGTH + 5);
/* 221 */     writeIntLE(check, this.compressedBuffer, MAGIC_LENGTH + 9);
/* 222 */     assert (MAGIC_LENGTH + 13 == HEADER_LENGTH);
/* 223 */     this.out.write(this.compressedBuffer, 0, HEADER_LENGTH + compressedLength);
/* 224 */     this.o = 0;
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
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 239 */     if (this.out != null) {
/* 240 */       if (this.syncFlush) {
/* 241 */         flushBufferedData();
/*     */       }
/* 243 */       this.out.flush();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void finish()
/*     */     throws IOException
/*     */   {
/* 254 */     ensureNotFinished();
/* 255 */     flushBufferedData();
/* 256 */     this.compressedBuffer[MAGIC_LENGTH] = ((byte)(0x10 | this.compressionLevel));
/* 257 */     writeIntLE(0, this.compressedBuffer, MAGIC_LENGTH + 1);
/* 258 */     writeIntLE(0, this.compressedBuffer, MAGIC_LENGTH + 5);
/* 259 */     writeIntLE(0, this.compressedBuffer, MAGIC_LENGTH + 9);
/* 260 */     assert (MAGIC_LENGTH + 13 == HEADER_LENGTH);
/* 261 */     this.out.write(this.compressedBuffer, 0, HEADER_LENGTH);
/* 262 */     this.finished = true;
/* 263 */     this.out.flush();
/*     */   }
/*     */   
/*     */   private static void writeIntLE(int i, byte[] buf, int off) {
/* 267 */     buf[(off++)] = ((byte)i);
/* 268 */     buf[(off++)] = ((byte)(i >>> 8));
/* 269 */     buf[(off++)] = ((byte)(i >>> 16));
/* 270 */     buf[(off++)] = ((byte)(i >>> 24));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 275 */     return getClass().getSimpleName() + "(out=" + this.out + ", blockSize=" + this.blockSize + ", compressor=" + this.compressor + ", checksum=" + this.checksum + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4BlockOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */