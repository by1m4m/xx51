/*     */ package io.netty.handler.codec.compression;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import java.util.List;
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
/*     */ public class Bzip2Decoder
/*     */   extends ByteToMessageDecoder
/*     */ {
/*     */   private static enum State
/*     */   {
/*  36 */     INIT, 
/*  37 */     INIT_BLOCK, 
/*  38 */     INIT_BLOCK_PARAMS, 
/*  39 */     RECEIVE_HUFFMAN_USED_MAP, 
/*  40 */     RECEIVE_HUFFMAN_USED_BITMAPS, 
/*  41 */     RECEIVE_SELECTORS_NUMBER, 
/*  42 */     RECEIVE_SELECTORS, 
/*  43 */     RECEIVE_HUFFMAN_LENGTH, 
/*  44 */     DECODE_HUFFMAN_DATA, 
/*  45 */     EOF;
/*     */     private State() {} }
/*  47 */   private State currentState = State.INIT;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  52 */   private final Bzip2BitReader reader = new Bzip2BitReader();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Bzip2BlockDecompressor blockDecompressor;
/*     */   
/*     */ 
/*     */ 
/*     */   private Bzip2HuffmanStageDecoder huffmanStageDecoder;
/*     */   
/*     */ 
/*     */ 
/*     */   private int blockSize;
/*     */   
/*     */ 
/*     */ 
/*     */   private int blockCRC;
/*     */   
/*     */ 
/*     */ 
/*     */   private int streamCRC;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  81 */     if (!in.isReadable()) {
/*  82 */       return;
/*     */     }
/*     */     
/*  85 */     Bzip2BitReader reader = this.reader;
/*  86 */     reader.setByteBuf(in);
/*     */     for (;;)
/*     */     {
/*  89 */       switch (this.currentState) {
/*     */       case INIT: 
/*  91 */         if (in.readableBytes() < 4) {
/*  92 */           return;
/*     */         }
/*  94 */         int magicNumber = in.readUnsignedMedium();
/*  95 */         if (magicNumber != 4348520) {
/*  96 */           throw new DecompressionException("Unexpected stream identifier contents. Mismatched bzip2 protocol version?");
/*     */         }
/*     */         
/*  99 */         int blockSize = in.readByte() - 48;
/* 100 */         if ((blockSize < 1) || (blockSize > 9)) {
/* 101 */           throw new DecompressionException("block size is invalid");
/*     */         }
/* 103 */         this.blockSize = (blockSize * 100000);
/*     */         
/* 105 */         this.streamCRC = 0;
/* 106 */         this.currentState = State.INIT_BLOCK;
/*     */       
/*     */       case INIT_BLOCK: 
/* 109 */         if (!reader.hasReadableBytes(10)) {
/* 110 */           return;
/*     */         }
/*     */         
/* 113 */         int magic1 = reader.readBits(24);
/* 114 */         int magic2 = reader.readBits(24);
/* 115 */         if ((magic1 == 1536581) && (magic2 == 3690640))
/*     */         {
/* 117 */           int storedCombinedCRC = reader.readInt();
/* 118 */           if (storedCombinedCRC != this.streamCRC) {
/* 119 */             throw new DecompressionException("stream CRC error");
/*     */           }
/* 121 */           this.currentState = State.EOF;
/*     */         }
/*     */         else {
/* 124 */           if ((magic1 != 3227993) || (magic2 != 2511705)) {
/* 125 */             throw new DecompressionException("bad block header");
/*     */           }
/* 127 */           this.blockCRC = reader.readInt();
/* 128 */           this.currentState = State.INIT_BLOCK_PARAMS;
/*     */         }
/*     */         break;
/* 131 */       case INIT_BLOCK_PARAMS:  if (!reader.hasReadableBits(25)) {
/* 132 */           return;
/*     */         }
/* 134 */         boolean blockRandomised = reader.readBoolean();
/* 135 */         int bwtStartPointer = reader.readBits(24);
/*     */         
/* 137 */         this.blockDecompressor = new Bzip2BlockDecompressor(this.blockSize, this.blockCRC, blockRandomised, bwtStartPointer, reader);
/*     */         
/* 139 */         this.currentState = State.RECEIVE_HUFFMAN_USED_MAP;
/*     */       
/*     */       case RECEIVE_HUFFMAN_USED_MAP: 
/* 142 */         if (!reader.hasReadableBits(16)) {
/* 143 */           return;
/*     */         }
/* 145 */         this.blockDecompressor.huffmanInUse16 = reader.readBits(16);
/* 146 */         this.currentState = State.RECEIVE_HUFFMAN_USED_BITMAPS;
/*     */       
/*     */       case RECEIVE_HUFFMAN_USED_BITMAPS: 
/* 149 */         Bzip2BlockDecompressor blockDecompressor = this.blockDecompressor;
/* 150 */         int inUse16 = blockDecompressor.huffmanInUse16;
/* 151 */         int bitNumber = Integer.bitCount(inUse16);
/* 152 */         byte[] huffmanSymbolMap = blockDecompressor.huffmanSymbolMap;
/*     */         
/* 154 */         if (!reader.hasReadableBits(bitNumber * 16 + 3)) {
/* 155 */           return;
/*     */         }
/*     */         
/* 158 */         int huffmanSymbolCount = 0;
/* 159 */         if (bitNumber > 0) {
/* 160 */           for (int i = 0; i < 16; i++) {
/* 161 */             if ((inUse16 & 32768 >>> i) != 0) {
/* 162 */               int j = 0; for (int k = i << 4; j < 16; k++) {
/* 163 */                 if (reader.readBoolean()) {
/* 164 */                   huffmanSymbolMap[(huffmanSymbolCount++)] = ((byte)k);
/*     */                 }
/* 162 */                 j++;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 170 */         blockDecompressor.huffmanEndOfBlockSymbol = (huffmanSymbolCount + 1);
/*     */         
/* 172 */         int totalTables = reader.readBits(3);
/* 173 */         if ((totalTables < 2) || (totalTables > 6)) {
/* 174 */           throw new DecompressionException("incorrect huffman groups number");
/*     */         }
/* 176 */         int alphaSize = huffmanSymbolCount + 2;
/* 177 */         if (alphaSize > 258) {
/* 178 */           throw new DecompressionException("incorrect alphabet size");
/*     */         }
/* 180 */         this.huffmanStageDecoder = new Bzip2HuffmanStageDecoder(reader, totalTables, alphaSize);
/* 181 */         this.currentState = State.RECEIVE_SELECTORS_NUMBER;
/*     */       
/*     */       case RECEIVE_SELECTORS_NUMBER: 
/* 184 */         if (!reader.hasReadableBits(15)) {
/* 185 */           return;
/*     */         }
/* 187 */         int totalSelectors = reader.readBits(15);
/* 188 */         if ((totalSelectors < 1) || (totalSelectors > 18002)) {
/* 189 */           throw new DecompressionException("incorrect selectors number");
/*     */         }
/* 191 */         this.huffmanStageDecoder.selectors = new byte[totalSelectors];
/*     */         
/* 193 */         this.currentState = State.RECEIVE_SELECTORS;
/*     */       
/*     */       case RECEIVE_SELECTORS: 
/* 196 */         Bzip2HuffmanStageDecoder huffmanStageDecoder = this.huffmanStageDecoder;
/* 197 */         byte[] selectors = huffmanStageDecoder.selectors;
/* 198 */         int totalSelectors = selectors.length;
/* 199 */         Bzip2MoveToFrontTable tableMtf = huffmanStageDecoder.tableMTF;
/*     */         
/*     */ 
/*     */ 
/* 203 */         for (int currSelector = huffmanStageDecoder.currentSelector; 
/* 204 */             currSelector < totalSelectors; currSelector++) {
/* 205 */           if (!reader.hasReadableBits(6))
/*     */           {
/* 207 */             huffmanStageDecoder.currentSelector = currSelector;
/* 208 */             return;
/*     */           }
/* 210 */           int index = 0;
/* 211 */           while (reader.readBoolean()) {
/* 212 */             index++;
/*     */           }
/* 214 */           selectors[currSelector] = tableMtf.indexToFront(index);
/*     */         }
/*     */         
/* 217 */         this.currentState = State.RECEIVE_HUFFMAN_LENGTH;
/*     */       
/*     */       case RECEIVE_HUFFMAN_LENGTH: 
/* 220 */         Bzip2HuffmanStageDecoder huffmanStageDecoder = this.huffmanStageDecoder;
/* 221 */         int totalTables = huffmanStageDecoder.totalTables;
/* 222 */         byte[][] codeLength = huffmanStageDecoder.tableCodeLengths;
/* 223 */         int alphaSize = huffmanStageDecoder.alphabetSize;
/*     */         
/*     */ 
/*     */ 
/* 227 */         int currLength = huffmanStageDecoder.currentLength;
/* 228 */         int currAlpha = 0;
/* 229 */         boolean modifyLength = huffmanStageDecoder.modifyLength;
/* 230 */         boolean saveStateAndReturn = false;
/* 231 */         for (int currGroup = huffmanStageDecoder.currentGroup; currGroup < totalTables; currGroup++)
/*     */         {
/* 233 */           if (!reader.hasReadableBits(5)) {
/* 234 */             saveStateAndReturn = true;
/* 235 */             break;
/*     */           }
/* 237 */           if (currLength < 0) {
/* 238 */             currLength = reader.readBits(5);
/*     */           }
/* 240 */           for (currAlpha = huffmanStageDecoder.currentAlpha; currAlpha < alphaSize; currAlpha++)
/*     */           {
/* 242 */             if (!reader.isReadable()) {
/* 243 */               saveStateAndReturn = true;
/*     */               break label970;
/*     */             }
/* 246 */             while ((modifyLength) || (reader.readBoolean())) {
/* 247 */               if (!reader.isReadable()) {
/* 248 */                 modifyLength = true;
/* 249 */                 saveStateAndReturn = true;
/*     */                 
/*     */                 break label970;
/*     */               }
/* 253 */               currLength += (reader.readBoolean() ? -1 : 1);
/* 254 */               modifyLength = false;
/* 255 */               if (!reader.isReadable()) {
/* 256 */                 saveStateAndReturn = true;
/*     */                 break label970;
/*     */               }
/*     */             }
/* 260 */             codeLength[currGroup][currAlpha] = ((byte)currLength);
/*     */           }
/* 262 */           currLength = -1;
/* 263 */           currAlpha = huffmanStageDecoder.currentAlpha = 0;
/* 264 */           modifyLength = false;
/*     */         }
/* 266 */         if (saveStateAndReturn)
/*     */         {
/* 268 */           huffmanStageDecoder.currentGroup = currGroup;
/* 269 */           huffmanStageDecoder.currentLength = currLength;
/* 270 */           huffmanStageDecoder.currentAlpha = currAlpha;
/* 271 */           huffmanStageDecoder.modifyLength = modifyLength;
/* 272 */           return;
/*     */         }
/*     */         
/*     */ 
/* 276 */         huffmanStageDecoder.createHuffmanDecodingTables();
/* 277 */         this.currentState = State.DECODE_HUFFMAN_DATA;
/*     */       case DECODE_HUFFMAN_DATA: 
/*     */         label970:
/* 280 */         Bzip2BlockDecompressor blockDecompressor = this.blockDecompressor;
/* 281 */         int oldReaderIndex = in.readerIndex();
/* 282 */         boolean decoded = blockDecompressor.decodeHuffmanData(this.huffmanStageDecoder);
/* 283 */         if (!decoded) {
/* 284 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 289 */         if ((in.readerIndex() == oldReaderIndex) && (in.isReadable())) {
/* 290 */           reader.refill();
/*     */         }
/*     */         
/* 293 */         int blockLength = blockDecompressor.blockLength();
/* 294 */         ByteBuf uncompressed = ctx.alloc().buffer(blockLength);
/* 295 */         boolean success = false;
/*     */         try {
/*     */           int uncByte;
/* 298 */           while ((uncByte = blockDecompressor.read()) >= 0) {
/* 299 */             uncompressed.writeByte(uncByte);
/*     */           }
/*     */           
/* 302 */           int currentBlockCRC = blockDecompressor.checkCRC();
/* 303 */           this.streamCRC = ((this.streamCRC << 1 | this.streamCRC >>> 31) ^ currentBlockCRC);
/*     */           
/* 305 */           out.add(uncompressed);
/* 306 */           success = true;
/*     */         } finally {
/* 308 */           if (!success) {
/* 309 */             uncompressed.release();
/*     */           }
/*     */         }
/* 312 */         this.currentState = State.INIT_BLOCK;
/*     */       }
/*     */     }
/* 315 */     in.skipBytes(in.readableBytes());
/* 316 */     return;
/*     */     
/* 318 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 328 */     return this.currentState == State.EOF;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\compression\Bzip2Decoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */