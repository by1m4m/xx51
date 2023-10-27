/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Locale;
/*     */ import net.jpountz.xxhash.XXHash32;
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
/*     */ public class LZ4FrameInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   static final String PREMATURE_EOS = "Stream ended prematurely";
/*     */   static final String NOT_SUPPORTED = "Stream unsupported";
/*     */   static final String BLOCK_HASH_MISMATCH = "Block checksum mismatch";
/*     */   static final String DESCRIPTOR_HASH_MISMATCH = "Stream frame descriptor corrupted";
/*     */   static final int MAGIC_SKIPPABLE_BASE = 407710288;
/*     */   private final LZ4SafeDecompressor decompressor;
/*     */   private final XXHash32 checksum;
/*  49 */   private final byte[] headerArray = new byte[15];
/*  50 */   private final ByteBuffer headerBuffer = ByteBuffer.wrap(this.headerArray).order(ByteOrder.LITTLE_ENDIAN);
/*     */   private byte[] compressedBuffer;
/*  52 */   private ByteBuffer buffer = null;
/*  53 */   private byte[] rawBuffer = null;
/*  54 */   private int maxBlockSize = -1;
/*  55 */   private long expectedContentSize = -1L;
/*  56 */   private long totalContentSize = 0L;
/*     */   
/*  58 */   private LZ4FrameOutputStream.FrameInfo frameInfo = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4FrameInputStream(InputStream in)
/*     */     throws IOException
/*     */   {
/*  71 */     this(in, LZ4Factory.fastestInstance().safeDecompressor(), XXHashFactory.fastestInstance().hash32());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4FrameInputStream(InputStream in, LZ4SafeDecompressor decompressor, XXHash32 checksum)
/*     */     throws IOException
/*     */   {
/*  83 */     super(in);
/*  84 */     this.decompressor = decompressor;
/*  85 */     this.checksum = checksum;
/*  86 */     nextFrameInfo();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean nextFrameInfo()
/*     */     throws IOException
/*     */   {
/*     */     for (;;)
/*     */     {
/*  98 */       int size = 0;
/*     */       do {
/* 100 */         int mySize = this.in.read(this.readNumberBuff.array(), size, 4 - size);
/* 101 */         if (mySize < 0) {
/* 102 */           return false;
/*     */         }
/* 104 */         size += mySize;
/* 105 */       } while (size < 4);
/* 106 */       int magic = this.readNumberBuff.getInt(0);
/* 107 */       if (magic == 407708164) {
/* 108 */         readHeader();
/* 109 */         return true; }
/* 110 */       if (magic >>> 4 == 25481893) {
/* 111 */         skippableFrame();
/*     */       } else {
/* 113 */         throw new IOException("Stream unsupported");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void skippableFrame() throws IOException {
/* 119 */     int skipSize = readInt(this.in);
/* 120 */     byte[] skipBuffer = new byte['Ѐ'];
/* 121 */     while (skipSize > 0) {
/* 122 */       int mySize = this.in.read(skipBuffer, 0, Math.min(skipSize, skipBuffer.length));
/* 123 */       if (mySize < 0) {
/* 124 */         throw new IOException("Stream ended prematurely");
/*     */       }
/* 126 */       skipSize -= mySize;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readHeader()
/*     */     throws IOException
/*     */   {
/* 136 */     this.headerBuffer.rewind();
/*     */     
/* 138 */     int flgRead = this.in.read();
/* 139 */     if (flgRead < 0) {
/* 140 */       throw new IOException("Stream ended prematurely");
/*     */     }
/* 142 */     int bdRead = this.in.read();
/* 143 */     if (bdRead < 0) {
/* 144 */       throw new IOException("Stream ended prematurely");
/*     */     }
/*     */     
/* 147 */     byte flgByte = (byte)(flgRead & 0xFF);
/* 148 */     LZ4FrameOutputStream.FLG flg = LZ4FrameOutputStream.FLG.fromByte(flgByte);
/* 149 */     this.headerBuffer.put(flgByte);
/* 150 */     byte bdByte = (byte)(bdRead & 0xFF);
/* 151 */     LZ4FrameOutputStream.BD bd = LZ4FrameOutputStream.BD.fromByte(bdByte);
/* 152 */     this.headerBuffer.put(bdByte);
/*     */     
/* 154 */     this.frameInfo = new LZ4FrameOutputStream.FrameInfo(flg, bd);
/*     */     
/* 156 */     if (flg.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_SIZE)) {
/* 157 */       this.expectedContentSize = readLong(this.in);
/* 158 */       this.headerBuffer.putLong(this.expectedContentSize);
/*     */     }
/* 160 */     this.totalContentSize = 0L;
/*     */     
/*     */ 
/* 163 */     byte hash = (byte)(this.checksum.hash(this.headerArray, 0, this.headerBuffer.position(), 0) >> 8 & 0xFF);
/* 164 */     int expectedHash = this.in.read();
/* 165 */     if (expectedHash < 0) {
/* 166 */       throw new IOException("Stream ended prematurely");
/*     */     }
/*     */     
/* 169 */     if (hash != (byte)(expectedHash & 0xFF)) {
/* 170 */       throw new IOException("Stream frame descriptor corrupted");
/*     */     }
/*     */     
/* 173 */     this.maxBlockSize = this.frameInfo.getBD().getBlockMaximumSize();
/* 174 */     this.compressedBuffer = new byte[this.maxBlockSize];
/* 175 */     this.rawBuffer = new byte[this.maxBlockSize];
/* 176 */     this.buffer = ByteBuffer.wrap(this.rawBuffer);
/* 177 */     this.buffer.limit(0);
/*     */   }
/*     */   
/* 180 */   private final ByteBuffer readNumberBuff = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
/*     */   
/*     */   private long readLong(InputStream stream) throws IOException {
/* 183 */     int offset = 0;
/*     */     do {
/* 185 */       int mySize = stream.read(this.readNumberBuff.array(), offset, 8 - offset);
/* 186 */       if (mySize < 0) {
/* 187 */         throw new IOException("Stream ended prematurely");
/*     */       }
/* 189 */       offset += mySize;
/* 190 */     } while (offset < 8);
/* 191 */     return this.readNumberBuff.getLong(0);
/*     */   }
/*     */   
/*     */   private int readInt(InputStream stream) throws IOException {
/* 195 */     int offset = 0;
/*     */     do {
/* 197 */       int mySize = stream.read(this.readNumberBuff.array(), offset, 4 - offset);
/* 198 */       if (mySize < 0) {
/* 199 */         throw new IOException("Stream ended prematurely");
/*     */       }
/* 201 */       offset += mySize;
/* 202 */     } while (offset < 4);
/* 203 */     return this.readNumberBuff.getInt(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readBlock()
/*     */     throws IOException
/*     */   {
/* 213 */     int blockSize = readInt(this.in);
/* 214 */     boolean compressed = (blockSize & 0x80000000) == 0;
/* 215 */     blockSize &= 0x7FFFFFFF;
/*     */     
/*     */ 
/* 218 */     if (blockSize == 0) {
/* 219 */       if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_CHECKSUM)) {
/* 220 */         int contentChecksum = readInt(this.in);
/* 221 */         if (contentChecksum != this.frameInfo.currentStreamHash()) {
/* 222 */           throw new IOException("Content checksum mismatch");
/*     */         }
/*     */       }
/* 225 */       if ((this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_SIZE)) && (this.expectedContentSize != this.totalContentSize)) {
/* 226 */         throw new IOException("Size check mismatch");
/*     */       }
/* 228 */       this.frameInfo.finish(); return;
/*     */     }
/*     */     
/*     */     byte[] tmpBuffer;
/*     */     byte[] tmpBuffer;
/* 233 */     if (compressed) {
/* 234 */       tmpBuffer = this.compressedBuffer;
/*     */     } else {
/* 236 */       tmpBuffer = this.rawBuffer;
/*     */     }
/* 238 */     if (blockSize > this.maxBlockSize) {
/* 239 */       throw new IOException(String.format(Locale.ROOT, "Block size %s exceeded max: %s", new Object[] { Integer.valueOf(blockSize), Integer.valueOf(this.maxBlockSize) }));
/*     */     }
/*     */     
/* 242 */     int offset = 0;
/* 243 */     while (offset < blockSize) {
/* 244 */       int lastRead = this.in.read(tmpBuffer, offset, blockSize - offset);
/* 245 */       if (lastRead < 0) {
/* 246 */         throw new IOException("Stream ended prematurely");
/*     */       }
/* 248 */       offset += lastRead;
/*     */     }
/*     */     
/*     */ 
/* 252 */     if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.BLOCK_CHECKSUM)) {
/* 253 */       int hashCheck = readInt(this.in);
/* 254 */       if (hashCheck != this.checksum.hash(tmpBuffer, 0, blockSize, 0)) {
/* 255 */         throw new IOException("Block checksum mismatch");
/*     */       }
/*     */     }
/*     */     
/*     */     int currentBufferSize;
/* 260 */     if (compressed) {
/*     */       try {
/* 262 */         currentBufferSize = this.decompressor.decompress(tmpBuffer, 0, blockSize, this.rawBuffer, 0, this.rawBuffer.length);
/*     */       } catch (LZ4Exception e) { int currentBufferSize;
/* 264 */         throw new IOException(e);
/*     */       }
/*     */     } else {
/* 267 */       currentBufferSize = blockSize;
/*     */     }
/* 269 */     if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_CHECKSUM)) {
/* 270 */       this.frameInfo.updateStreamHash(this.rawBuffer, 0, currentBufferSize);
/*     */     }
/* 272 */     this.totalContentSize += currentBufferSize;
/* 273 */     this.buffer.limit(currentBufferSize);
/* 274 */     this.buffer.rewind();
/*     */   }
/*     */   
/*     */   public int read() throws IOException
/*     */   {
/* 279 */     while (this.buffer.remaining() == 0) {
/* 280 */       if ((this.frameInfo.isFinished()) && 
/* 281 */         (!nextFrameInfo())) {
/* 282 */         return -1;
/*     */       }
/*     */       
/* 285 */       readBlock();
/*     */     }
/* 287 */     return this.buffer.get() & 0xFF;
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException
/*     */   {
/* 292 */     if ((off < 0) || (len < 0) || (off + len > b.length)) {
/* 293 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 295 */     while (this.buffer.remaining() == 0) {
/* 296 */       if ((this.frameInfo.isFinished()) && 
/* 297 */         (!nextFrameInfo())) {
/* 298 */         return -1;
/*     */       }
/*     */       
/* 301 */       readBlock();
/*     */     }
/* 303 */     len = Math.min(len, this.buffer.remaining());
/* 304 */     this.buffer.get(b, off, len);
/* 305 */     return len;
/*     */   }
/*     */   
/*     */   public long skip(long n) throws IOException
/*     */   {
/* 310 */     if (n <= 0L) {
/* 311 */       return 0L;
/*     */     }
/* 313 */     while (this.buffer.remaining() == 0) {
/* 314 */       if ((this.frameInfo.isFinished()) && 
/* 315 */         (!nextFrameInfo())) {
/* 316 */         return 0L;
/*     */       }
/*     */       
/* 319 */       readBlock();
/*     */     }
/* 321 */     n = Math.min(n, this.buffer.remaining());
/* 322 */     this.buffer.position(this.buffer.position() + (int)n);
/* 323 */     return n;
/*     */   }
/*     */   
/*     */   public int available() throws IOException
/*     */   {
/* 328 */     return this.buffer.remaining();
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 333 */     super.close();
/*     */   }
/*     */   
/*     */   public synchronized void mark(int readlimit)
/*     */   {
/* 338 */     throw new UnsupportedOperationException("mark not supported");
/*     */   }
/*     */   
/*     */   public synchronized void reset() throws IOException
/*     */   {
/* 343 */     throw new UnsupportedOperationException("reset not supported");
/*     */   }
/*     */   
/*     */   public boolean markSupported()
/*     */   {
/* 348 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4FrameInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */