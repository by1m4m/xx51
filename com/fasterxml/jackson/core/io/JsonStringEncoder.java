/*     */ package com.fasterxml.jackson.core.io;
/*     */ 
/*     */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*     */ import com.fasterxml.jackson.core.util.TextBuffer;
/*     */ import java.lang.ref.SoftReference;
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
/*     */ public final class JsonStringEncoder
/*     */ {
/*  19 */   private static final char[] HC = ;
/*     */   
/*  21 */   private static final byte[] HB = CharTypes.copyHexBytes();
/*     */   
/*     */ 
/*     */ 
/*     */   private static final int SURR1_FIRST = 55296;
/*     */   
/*     */ 
/*     */   private static final int SURR1_LAST = 56319;
/*     */   
/*     */ 
/*     */   private static final int SURR2_FIRST = 56320;
/*     */   
/*     */ 
/*     */   private static final int SURR2_LAST = 57343;
/*     */   
/*     */ 
/*  37 */   protected static final ThreadLocal<SoftReference<JsonStringEncoder>> _threadEncoder = new ThreadLocal();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TextBuffer _text;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteArrayBuilder _bytes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final char[] _qbuf;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonStringEncoder()
/*     */   {
/*  64 */     this._qbuf = new char[6];
/*  65 */     this._qbuf[0] = '\\';
/*  66 */     this._qbuf[2] = '0';
/*  67 */     this._qbuf[3] = '0';
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonStringEncoder getInstance()
/*     */   {
/*  75 */     SoftReference<JsonStringEncoder> ref = (SoftReference)_threadEncoder.get();
/*  76 */     JsonStringEncoder enc = ref == null ? null : (JsonStringEncoder)ref.get();
/*     */     
/*  78 */     if (enc == null) {
/*  79 */       enc = new JsonStringEncoder();
/*  80 */       _threadEncoder.set(new SoftReference(enc));
/*     */     }
/*  82 */     return enc;
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
/*     */   public char[] quoteAsString(String input)
/*     */   {
/*  97 */     TextBuffer textBuffer = this._text;
/*  98 */     if (textBuffer == null)
/*     */     {
/* 100 */       this._text = (textBuffer = new TextBuffer(null));
/*     */     }
/* 102 */     char[] outputBuffer = textBuffer.emptyAndGetCurrentSegment();
/* 103 */     int[] escCodes = CharTypes.get7BitOutputEscapes();
/* 104 */     int escCodeCount = escCodes.length;
/* 105 */     int inPtr = 0;
/* 106 */     int inputLen = input.length();
/* 107 */     int outPtr = 0;
/*     */     
/*     */ 
/* 110 */     while (inPtr < inputLen)
/*     */     {
/*     */       for (;;) {
/* 113 */         char c = input.charAt(inPtr);
/* 114 */         if ((c < escCodeCount) && (escCodes[c] != 0)) {
/*     */           break;
/*     */         }
/* 117 */         if (outPtr >= outputBuffer.length) {
/* 118 */           outputBuffer = textBuffer.finishCurrentSegment();
/* 119 */           outPtr = 0;
/*     */         }
/* 121 */         outputBuffer[(outPtr++)] = c;
/* 122 */         inPtr++; if (inPtr >= inputLen) {
/*     */           break label261;
/*     */         }
/*     */       }
/*     */       
/* 127 */       char d = input.charAt(inPtr++);
/* 128 */       int escCode = escCodes[d];
/* 129 */       int length = escCode < 0 ? _appendNumeric(d, this._qbuf) : _appendNamed(escCode, this._qbuf);
/*     */       
/*     */ 
/*     */ 
/* 133 */       if (outPtr + length > outputBuffer.length) {
/* 134 */         int first = outputBuffer.length - outPtr;
/* 135 */         if (first > 0) {
/* 136 */           System.arraycopy(this._qbuf, 0, outputBuffer, outPtr, first);
/*     */         }
/* 138 */         outputBuffer = textBuffer.finishCurrentSegment();
/* 139 */         int second = length - first;
/* 140 */         System.arraycopy(this._qbuf, first, outputBuffer, 0, second);
/* 141 */         outPtr = second;
/*     */       } else {
/* 143 */         System.arraycopy(this._qbuf, 0, outputBuffer, outPtr, length);
/* 144 */         outPtr += length;
/*     */       } }
/*     */     label261:
/* 147 */     textBuffer.setCurrentLength(outPtr);
/* 148 */     return textBuffer.contentsAsArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] quoteAsUTF8(String text)
/*     */   {
/* 158 */     ByteArrayBuilder bb = this._bytes;
/* 159 */     if (bb == null)
/*     */     {
/* 161 */       this._bytes = (bb = new ByteArrayBuilder(null));
/*     */     }
/* 163 */     int inputPtr = 0;
/* 164 */     int inputEnd = text.length();
/* 165 */     int outputPtr = 0;
/* 166 */     byte[] outputBuffer = bb.resetAndGetFirstSegment();
/*     */     
/*     */ 
/* 169 */     while (inputPtr < inputEnd) {
/* 170 */       int[] escCodes = CharTypes.get7BitOutputEscapes();
/*     */       
/*     */       for (;;)
/*     */       {
/* 174 */         int ch = text.charAt(inputPtr);
/* 175 */         if ((ch > 127) || (escCodes[ch] != 0)) {
/*     */           break;
/*     */         }
/* 178 */         if (outputPtr >= outputBuffer.length) {
/* 179 */           outputBuffer = bb.finishCurrentSegment();
/* 180 */           outputPtr = 0;
/*     */         }
/* 182 */         outputBuffer[(outputPtr++)] = ((byte)ch);
/* 183 */         inputPtr++; if (inputPtr >= inputEnd) {
/*     */           break label492;
/*     */         }
/*     */       }
/* 187 */       if (outputPtr >= outputBuffer.length) {
/* 188 */         outputBuffer = bb.finishCurrentSegment();
/* 189 */         outputPtr = 0;
/*     */       }
/*     */       
/* 192 */       int ch = text.charAt(inputPtr++);
/* 193 */       if (ch <= 127) {
/* 194 */         int escape = escCodes[ch];
/*     */         
/* 196 */         outputPtr = _appendByte(ch, escape, bb, outputPtr);
/* 197 */         outputBuffer = bb.getCurrentSegment();
/*     */       }
/*     */       else {
/* 200 */         if (ch <= 2047) {
/* 201 */           outputBuffer[(outputPtr++)] = ((byte)(0xC0 | ch >> 6));
/* 202 */           ch = 0x80 | ch & 0x3F;
/*     */ 
/*     */         }
/* 205 */         else if ((ch < 55296) || (ch > 57343)) {
/* 206 */           outputBuffer[(outputPtr++)] = ((byte)(0xE0 | ch >> 12));
/* 207 */           if (outputPtr >= outputBuffer.length) {
/* 208 */             outputBuffer = bb.finishCurrentSegment();
/* 209 */             outputPtr = 0;
/*     */           }
/* 211 */           outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch >> 6 & 0x3F));
/* 212 */           ch = 0x80 | ch & 0x3F;
/*     */         } else {
/* 214 */           if (ch > 56319) {
/* 215 */             _illegal(ch);
/*     */           }
/*     */           
/* 218 */           if (inputPtr >= inputEnd) {
/* 219 */             _illegal(ch);
/*     */           }
/* 221 */           ch = _convert(ch, text.charAt(inputPtr++));
/* 222 */           if (ch > 1114111) {
/* 223 */             _illegal(ch);
/*     */           }
/* 225 */           outputBuffer[(outputPtr++)] = ((byte)(0xF0 | ch >> 18));
/* 226 */           if (outputPtr >= outputBuffer.length) {
/* 227 */             outputBuffer = bb.finishCurrentSegment();
/* 228 */             outputPtr = 0;
/*     */           }
/* 230 */           outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch >> 12 & 0x3F));
/* 231 */           if (outputPtr >= outputBuffer.length) {
/* 232 */             outputBuffer = bb.finishCurrentSegment();
/* 233 */             outputPtr = 0;
/*     */           }
/* 235 */           outputBuffer[(outputPtr++)] = ((byte)(0x80 | ch >> 6 & 0x3F));
/* 236 */           ch = 0x80 | ch & 0x3F;
/*     */         }
/*     */         
/* 239 */         if (outputPtr >= outputBuffer.length) {
/* 240 */           outputBuffer = bb.finishCurrentSegment();
/* 241 */           outputPtr = 0;
/*     */         }
/* 243 */         outputBuffer[(outputPtr++)] = ((byte)ch); } }
/*     */     label492:
/* 245 */     return this._bytes.completeAndCoalesce(outputPtr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] encodeAsUTF8(String text)
/*     */   {
/* 255 */     ByteArrayBuilder byteBuilder = this._bytes;
/* 256 */     if (byteBuilder == null)
/*     */     {
/* 258 */       this._bytes = (byteBuilder = new ByteArrayBuilder(null));
/*     */     }
/* 260 */     int inputPtr = 0;
/* 261 */     int inputEnd = text.length();
/* 262 */     int outputPtr = 0;
/* 263 */     byte[] outputBuffer = byteBuilder.resetAndGetFirstSegment();
/* 264 */     int outputEnd = outputBuffer.length;
/*     */     
/*     */ 
/* 267 */     while (inputPtr < inputEnd) {
/* 268 */       int c = text.charAt(inputPtr++);
/*     */       
/*     */ 
/* 271 */       while (c <= 127) {
/* 272 */         if (outputPtr >= outputEnd) {
/* 273 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 274 */           outputEnd = outputBuffer.length;
/* 275 */           outputPtr = 0;
/*     */         }
/* 277 */         outputBuffer[(outputPtr++)] = ((byte)c);
/* 278 */         if (inputPtr >= inputEnd) {
/*     */           break label443;
/*     */         }
/* 281 */         c = text.charAt(inputPtr++);
/*     */       }
/*     */       
/*     */ 
/* 285 */       if (outputPtr >= outputEnd) {
/* 286 */         outputBuffer = byteBuilder.finishCurrentSegment();
/* 287 */         outputEnd = outputBuffer.length;
/* 288 */         outputPtr = 0;
/*     */       }
/* 290 */       if (c < 2048) {
/* 291 */         outputBuffer[(outputPtr++)] = ((byte)(0xC0 | c >> 6));
/*     */ 
/*     */       }
/* 294 */       else if ((c < 55296) || (c > 57343)) {
/* 295 */         outputBuffer[(outputPtr++)] = ((byte)(0xE0 | c >> 12));
/* 296 */         if (outputPtr >= outputEnd) {
/* 297 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 298 */           outputEnd = outputBuffer.length;
/* 299 */           outputPtr = 0;
/*     */         }
/* 301 */         outputBuffer[(outputPtr++)] = ((byte)(0x80 | c >> 6 & 0x3F));
/*     */       } else {
/* 303 */         if (c > 56319) {
/* 304 */           _illegal(c);
/*     */         }
/*     */         
/* 307 */         if (inputPtr >= inputEnd) {
/* 308 */           _illegal(c);
/*     */         }
/* 310 */         c = _convert(c, text.charAt(inputPtr++));
/* 311 */         if (c > 1114111) {
/* 312 */           _illegal(c);
/*     */         }
/* 314 */         outputBuffer[(outputPtr++)] = ((byte)(0xF0 | c >> 18));
/* 315 */         if (outputPtr >= outputEnd) {
/* 316 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 317 */           outputEnd = outputBuffer.length;
/* 318 */           outputPtr = 0;
/*     */         }
/* 320 */         outputBuffer[(outputPtr++)] = ((byte)(0x80 | c >> 12 & 0x3F));
/* 321 */         if (outputPtr >= outputEnd) {
/* 322 */           outputBuffer = byteBuilder.finishCurrentSegment();
/* 323 */           outputEnd = outputBuffer.length;
/* 324 */           outputPtr = 0;
/*     */         }
/* 326 */         outputBuffer[(outputPtr++)] = ((byte)(0x80 | c >> 6 & 0x3F));
/*     */       }
/*     */       
/* 329 */       if (outputPtr >= outputEnd) {
/* 330 */         outputBuffer = byteBuilder.finishCurrentSegment();
/* 331 */         outputEnd = outputBuffer.length;
/* 332 */         outputPtr = 0;
/*     */       }
/* 334 */       outputBuffer[(outputPtr++)] = ((byte)(0x80 | c & 0x3F)); }
/*     */     label443:
/* 336 */     return this._bytes.completeAndCoalesce(outputPtr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int _appendNumeric(int value, char[] qbuf)
/*     */   {
/* 346 */     qbuf[1] = 'u';
/*     */     
/* 348 */     qbuf[4] = HC[(value >> 4)];
/* 349 */     qbuf[5] = HC[(value & 0xF)];
/* 350 */     return 6;
/*     */   }
/*     */   
/*     */   private int _appendNamed(int esc, char[] qbuf) {
/* 354 */     qbuf[1] = ((char)esc);
/* 355 */     return 2;
/*     */   }
/*     */   
/*     */   private int _appendByte(int ch, int esc, ByteArrayBuilder bb, int ptr)
/*     */   {
/* 360 */     bb.setCurrentSegmentLength(ptr);
/* 361 */     bb.append(92);
/* 362 */     if (esc < 0) {
/* 363 */       bb.append(117);
/* 364 */       if (ch > 255) {
/* 365 */         int hi = ch >> 8;
/* 366 */         bb.append(HB[(hi >> 4)]);
/* 367 */         bb.append(HB[(hi & 0xF)]);
/* 368 */         ch &= 0xFF;
/*     */       } else {
/* 370 */         bb.append(48);
/* 371 */         bb.append(48);
/*     */       }
/* 373 */       bb.append(HB[(ch >> 4)]);
/* 374 */       bb.append(HB[(ch & 0xF)]);
/*     */     } else {
/* 376 */       bb.append((byte)esc);
/*     */     }
/* 378 */     return bb.getCurrentSegmentLength();
/*     */   }
/*     */   
/*     */   private static int _convert(int p1, int p2)
/*     */   {
/* 383 */     if ((p2 < 56320) || (p2 > 57343)) {
/* 384 */       throw new IllegalArgumentException("Broken surrogate pair: first char 0x" + Integer.toHexString(p1) + ", second 0x" + Integer.toHexString(p2) + "; illegal combination");
/*     */     }
/* 386 */     return 65536 + (p1 - 55296 << 10) + (p2 - 56320);
/*     */   }
/*     */   
/*     */   private static void _illegal(int c) {
/* 390 */     throw new IllegalArgumentException(UTF8Writer.illegalSurrogateDesc(c));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\io\JsonStringEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */