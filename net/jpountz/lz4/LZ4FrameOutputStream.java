/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Arrays;
/*     */ import java.util.BitSet;
/*     */ import java.util.Locale;
/*     */ import net.jpountz.xxhash.StreamingXXHash32;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LZ4FrameOutputStream
/*     */   extends FilterOutputStream
/*     */ {
/*     */   static final int INTEGER_BYTES = 4;
/*     */   static final int LONG_BYTES = 8;
/*     */   static final int MAGIC = 407708164;
/*     */   static final int LZ4_MAX_HEADER_LENGTH = 15;
/*     */   static final int LZ4_FRAME_INCOMPRESSIBLE_MASK = Integer.MIN_VALUE;
/*  57 */   static final LZ4FrameOutputStream.FLG.Bits[] DEFAULT_FEATURES = { LZ4FrameOutputStream.FLG.Bits.BLOCK_INDEPENDENCE };
/*     */   static final String CLOSED_STREAM = "The stream is already closed";
/*     */   private final LZ4Compressor compressor;
/*     */   private final XXHash32 checksum;
/*     */   
/*  62 */   public static enum BLOCKSIZE { SIZE_64KB(4),  SIZE_256KB(5),  SIZE_1MB(6),  SIZE_4MB(7);
/*     */     
/*     */     private BLOCKSIZE(int indicator) {
/*  65 */       this.indicator = indicator;
/*     */     }
/*     */     
/*  68 */     public int getIndicator() { return this.indicator; }
/*     */     
/*     */     public static BLOCKSIZE valueOf(int indicator) {
/*  71 */       switch (indicator) {
/*  72 */       case 7:  return SIZE_4MB;
/*  73 */       case 6:  return SIZE_1MB;
/*  74 */       case 5:  return SIZE_256KB;
/*  75 */       case 4:  return SIZE_64KB; }
/*  76 */       throw new IllegalArgumentException(String.format(Locale.ROOT, "Block size must be 4-7. Cannot use value of [%d]", new Object[] { Integer.valueOf(indicator) }));
/*     */     }
/*     */     
/*     */ 
/*     */     private final int indicator;
/*     */   }
/*     */   
/*     */   private final ByteBuffer buffer;
/*     */   private final byte[] compressedBuffer;
/*     */   private final int maxBlockSize;
/*     */   private final long knownSize;
/*  87 */   private final ByteBuffer intLEBuffer = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
/*     */   
/*  89 */   private FrameInfo frameInfo = null;
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
/*     */   public LZ4FrameOutputStream(OutputStream out, BLOCKSIZE blockSize, LZ4FrameOutputStream.FLG.Bits... bits)
/*     */     throws IOException
/*     */   {
/* 103 */     this(out, blockSize, -1L, bits);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4FrameOutputStream(OutputStream out, BLOCKSIZE blockSize, long knownSize, LZ4FrameOutputStream.FLG.Bits... bits)
/*     */     throws IOException
/*     */   {
/* 116 */     super(out);
/* 117 */     this.compressor = LZ4Factory.fastestInstance().fastCompressor();
/* 118 */     this.checksum = XXHashFactory.fastestInstance().hash32();
/* 119 */     this.frameInfo = new FrameInfo(new FLG(1, bits), new BD(blockSize, null));
/* 120 */     this.maxBlockSize = this.frameInfo.getBD().getBlockMaximumSize();
/* 121 */     this.buffer = ByteBuffer.allocate(this.maxBlockSize).order(ByteOrder.LITTLE_ENDIAN);
/* 122 */     this.compressedBuffer = new byte[this.compressor.maxCompressedLength(this.maxBlockSize)];
/* 123 */     if ((this.frameInfo.getFLG().isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_SIZE)) && (knownSize < 0L)) {
/* 124 */       throw new IllegalArgumentException("Known size must be greater than zero in order to use the known size feature");
/*     */     }
/* 126 */     this.knownSize = knownSize;
/* 127 */     writeHeader();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4FrameOutputStream(OutputStream out, BLOCKSIZE blockSize)
/*     */     throws IOException
/*     */   {
/* 140 */     this(out, blockSize, DEFAULT_FEATURES);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4FrameOutputStream(OutputStream out)
/*     */     throws IOException
/*     */   {
/* 152 */     this(out, BLOCKSIZE.SIZE_4MB);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeHeader()
/*     */     throws IOException
/*     */   {
/* 161 */     ByteBuffer headerBuffer = ByteBuffer.allocate(15).order(ByteOrder.LITTLE_ENDIAN);
/* 162 */     headerBuffer.putInt(407708164);
/* 163 */     headerBuffer.put(this.frameInfo.getFLG().toByte());
/* 164 */     headerBuffer.put(this.frameInfo.getBD().toByte());
/* 165 */     if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_SIZE)) {
/* 166 */       headerBuffer.putLong(this.knownSize);
/*     */     }
/*     */     
/* 169 */     int hash = this.checksum.hash(headerBuffer.array(), 4, headerBuffer.position() - 4, 0) >> 8 & 0xFF;
/* 170 */     headerBuffer.put((byte)hash);
/*     */     
/* 172 */     this.out.write(headerBuffer.array(), 0, headerBuffer.position());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeBlock()
/*     */     throws IOException
/*     */   {
/* 182 */     if (this.buffer.position() == 0) {
/* 183 */       return;
/*     */     }
/*     */     
/* 186 */     Arrays.fill(this.compressedBuffer, (byte)0);
/*     */     
/* 188 */     int compressedLength = this.compressor.compress(this.buffer.array(), 0, this.buffer.position(), this.compressedBuffer, 0);
/*     */     
/*     */     int compressMethod;
/*     */     byte[] bufferToWrite;
/*     */     int compressMethod;
/* 193 */     if (compressedLength >= this.buffer.position()) {
/* 194 */       compressedLength = this.buffer.position();
/* 195 */       byte[] bufferToWrite = Arrays.copyOf(this.buffer.array(), compressedLength);
/* 196 */       compressMethod = Integer.MIN_VALUE;
/*     */     } else {
/* 198 */       bufferToWrite = this.compressedBuffer;
/* 199 */       compressMethod = 0;
/*     */     }
/*     */     
/*     */ 
/* 203 */     this.intLEBuffer.putInt(0, compressedLength | compressMethod);
/* 204 */     this.out.write(this.intLEBuffer.array());
/* 205 */     this.out.write(bufferToWrite, 0, compressedLength);
/*     */     
/*     */ 
/* 208 */     if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.BLOCK_CHECKSUM)) {
/* 209 */       this.intLEBuffer.putInt(0, this.checksum.hash(bufferToWrite, 0, compressedLength, 0));
/* 210 */       this.out.write(this.intLEBuffer.array());
/*     */     }
/* 212 */     this.buffer.rewind();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void writeEndMark()
/*     */     throws IOException
/*     */   {
/* 222 */     this.intLEBuffer.putInt(0, 0);
/* 223 */     this.out.write(this.intLEBuffer.array());
/* 224 */     if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_CHECKSUM)) {
/* 225 */       this.intLEBuffer.putInt(0, this.frameInfo.currentStreamHash());
/* 226 */       this.out.write(this.intLEBuffer.array());
/*     */     }
/* 228 */     this.frameInfo.finish();
/*     */   }
/*     */   
/*     */   public void write(int b) throws IOException
/*     */   {
/* 233 */     ensureNotFinished();
/* 234 */     if (this.buffer.position() == this.maxBlockSize) {
/* 235 */       writeBlock();
/*     */     }
/* 237 */     this.buffer.put((byte)b);
/*     */     
/* 239 */     if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_CHECKSUM)) {
/* 240 */       this.frameInfo.updateStreamHash(new byte[] { (byte)b }, 0, 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException
/*     */   {
/* 246 */     if ((off < 0) || (len < 0) || (off + len > b.length)) {
/* 247 */       throw new IndexOutOfBoundsException();
/*     */     }
/* 249 */     ensureNotFinished();
/*     */     
/*     */ 
/* 252 */     while (len > this.buffer.remaining()) {
/* 253 */       int sizeWritten = this.buffer.remaining();
/*     */       
/* 255 */       this.buffer.put(b, off, sizeWritten);
/* 256 */       if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_CHECKSUM)) {
/* 257 */         this.frameInfo.updateStreamHash(b, off, sizeWritten);
/*     */       }
/* 259 */       writeBlock();
/*     */       
/* 261 */       off += sizeWritten;
/* 262 */       len -= sizeWritten;
/*     */     }
/* 264 */     this.buffer.put(b, off, len);
/*     */     
/* 266 */     if (this.frameInfo.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_CHECKSUM)) {
/* 267 */       this.frameInfo.updateStreamHash(b, off, len);
/*     */     }
/*     */   }
/*     */   
/*     */   public void flush() throws IOException
/*     */   {
/* 273 */     if (!this.frameInfo.isFinished()) {
/* 274 */       writeBlock();
/*     */     }
/* 276 */     super.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void ensureNotFinished()
/*     */   {
/* 283 */     if (this.frameInfo.isFinished()) {
/* 284 */       throw new IllegalStateException("The stream is already closed");
/*     */     }
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 290 */     if (!this.frameInfo.isFinished()) {
/* 291 */       flush();
/* 292 */       writeEndMark();
/*     */     }
/* 294 */     super.close();
/*     */   }
/*     */   
/*     */   public static class FLG
/*     */   {
/*     */     private static final int DEFAULT_VERSION = 1;
/*     */     private final BitSet bitSet;
/*     */     private final int version;
/*     */     
/*     */     public static enum Bits {
/* 304 */       RESERVED_0(0), 
/* 305 */       RESERVED_1(1), 
/* 306 */       CONTENT_CHECKSUM(2), 
/* 307 */       CONTENT_SIZE(3), 
/* 308 */       BLOCK_CHECKSUM(4), 
/* 309 */       BLOCK_INDEPENDENCE(5);
/*     */       
/*     */       private final int position;
/*     */       
/* 313 */       private Bits(int position) { this.position = position; }
/*     */     }
/*     */     
/*     */     public FLG(int version, Bits... bits)
/*     */     {
/* 318 */       this.bitSet = new BitSet(8);
/* 319 */       this.version = version;
/* 320 */       if (bits != null) {
/* 321 */         for (Bits bit : bits) {
/* 322 */           this.bitSet.set(bit.position);
/*     */         }
/*     */       }
/* 325 */       validate();
/*     */     }
/*     */     
/*     */     private FLG(int version, byte b) {
/* 329 */       this.bitSet = BitSet.valueOf(new byte[] { b });
/* 330 */       this.version = version;
/* 331 */       validate();
/*     */     }
/*     */     
/*     */     public static FLG fromByte(byte flg) {
/* 335 */       byte versionMask = (byte)(flg & 0xC0);
/* 336 */       return new FLG(versionMask >>> 6, (byte)(flg ^ versionMask));
/*     */     }
/*     */     
/*     */     public byte toByte() {
/* 340 */       return (byte)(this.bitSet.toByteArray()[0] | (this.version & 0x3) << 6);
/*     */     }
/*     */     
/*     */     private void validate() {
/* 344 */       if (this.bitSet.get(Bits.RESERVED_0.position)) {
/* 345 */         throw new RuntimeException("Reserved0 field must be 0");
/*     */       }
/* 347 */       if (this.bitSet.get(Bits.RESERVED_1.position)) {
/* 348 */         throw new RuntimeException("Reserved1 field must be 0");
/*     */       }
/* 350 */       if (!this.bitSet.get(Bits.BLOCK_INDEPENDENCE.position)) {
/* 351 */         throw new RuntimeException("Dependent block stream is unsupported (BLOCK_INDEPENDENCE must be set)");
/*     */       }
/* 353 */       if (this.version != 1) {
/* 354 */         throw new RuntimeException(String.format(Locale.ROOT, "Version %d is unsupported", new Object[] { Integer.valueOf(this.version) }));
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isEnabled(Bits bit) {
/* 359 */       return this.bitSet.get(bit.position);
/*     */     }
/*     */     
/*     */     public int getVersion() {
/* 363 */       return this.version;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class BD
/*     */   {
/*     */     private static final int RESERVED_MASK = 143;
/*     */     private final LZ4FrameOutputStream.BLOCKSIZE blockSizeValue;
/*     */     
/*     */     private BD(LZ4FrameOutputStream.BLOCKSIZE blockSizeValue) {
/* 373 */       this.blockSizeValue = blockSizeValue;
/*     */     }
/*     */     
/*     */     public static BD fromByte(byte bd) {
/* 377 */       int blockMaximumSize = bd >>> 4 & 0x7;
/* 378 */       if ((bd & 0x8F) > 0) {
/* 379 */         throw new RuntimeException("Reserved fields must be 0");
/*     */       }
/*     */       
/* 382 */       return new BD(LZ4FrameOutputStream.BLOCKSIZE.valueOf(blockMaximumSize));
/*     */     }
/*     */     
/*     */     public int getBlockMaximumSize()
/*     */     {
/* 387 */       return 1 << 2 * this.blockSizeValue.getIndicator() + 8;
/*     */     }
/*     */     
/*     */     public byte toByte() {
/* 391 */       return (byte)((this.blockSizeValue.getIndicator() & 0x7) << 4);
/*     */     }
/*     */   }
/*     */   
/*     */   static class FrameInfo {
/*     */     private final LZ4FrameOutputStream.FLG flg;
/*     */     private final LZ4FrameOutputStream.BD bd;
/*     */     private final StreamingXXHash32 streamHash;
/* 399 */     private boolean finished = false;
/*     */     
/*     */     public FrameInfo(LZ4FrameOutputStream.FLG flg, LZ4FrameOutputStream.BD bd) {
/* 402 */       this.flg = flg;
/* 403 */       this.bd = bd;
/* 404 */       this.streamHash = (flg.isEnabled(LZ4FrameOutputStream.FLG.Bits.CONTENT_CHECKSUM) ? XXHashFactory.fastestInstance().newStreamingHash32(0) : null);
/*     */     }
/*     */     
/*     */     public boolean isEnabled(LZ4FrameOutputStream.FLG.Bits bit) {
/* 408 */       return this.flg.isEnabled(bit);
/*     */     }
/*     */     
/*     */     public LZ4FrameOutputStream.FLG getFLG() {
/* 412 */       return this.flg;
/*     */     }
/*     */     
/*     */     public LZ4FrameOutputStream.BD getBD() {
/* 416 */       return this.bd;
/*     */     }
/*     */     
/*     */     public void updateStreamHash(byte[] buff, int off, int len) {
/* 420 */       this.streamHash.update(buff, off, len);
/*     */     }
/*     */     
/*     */     public int currentStreamHash() {
/* 424 */       return this.streamHash.getValue();
/*     */     }
/*     */     
/*     */     public void finish() {
/* 428 */       this.finished = true;
/*     */     }
/*     */     
/*     */     public boolean isFinished() {
/* 432 */       return this.finished;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4FrameOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */