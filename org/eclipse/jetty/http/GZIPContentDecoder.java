/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Inflater;
/*     */ import java.util.zip.ZipException;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.component.Destroyable;
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
/*     */ public class GZIPContentDecoder
/*     */   implements Destroyable
/*     */ {
/*  38 */   private final Inflater _inflater = new Inflater(true);
/*     */   private final ByteBufferPool _pool;
/*     */   private final int _bufferSize;
/*     */   private State _state;
/*     */   private int _size;
/*     */   private int _value;
/*     */   private byte _flags;
/*     */   private ByteBuffer _inflated;
/*     */   
/*     */   public GZIPContentDecoder()
/*     */   {
/*  49 */     this(null, 2048);
/*     */   }
/*     */   
/*     */   public GZIPContentDecoder(int bufferSize)
/*     */   {
/*  54 */     this(null, bufferSize);
/*     */   }
/*     */   
/*     */   public GZIPContentDecoder(ByteBufferPool pool, int bufferSize)
/*     */   {
/*  59 */     this._bufferSize = bufferSize;
/*  60 */     this._pool = pool;
/*  61 */     reset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuffer decode(ByteBuffer compressed)
/*     */   {
/*  71 */     decodeChunks(compressed);
/*  72 */     if ((BufferUtil.isEmpty(this._inflated)) || (this._state == State.CRC) || (this._state == State.ISIZE)) {
/*  73 */       return BufferUtil.EMPTY_BUFFER;
/*     */     }
/*  75 */     ByteBuffer result = this._inflated;
/*  76 */     this._inflated = null;
/*  77 */     return result;
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
/*     */   protected boolean decodedChunk(ByteBuffer chunk)
/*     */   {
/*  94 */     if (this._inflated == null)
/*     */     {
/*  96 */       this._inflated = chunk;
/*     */     }
/*     */     else
/*     */     {
/* 100 */       int size = this._inflated.remaining() + chunk.remaining();
/* 101 */       if (size <= this._inflated.capacity())
/*     */       {
/* 103 */         BufferUtil.append(this._inflated, chunk);
/* 104 */         release(chunk);
/*     */       }
/*     */       else
/*     */       {
/* 108 */         ByteBuffer bigger = acquire(size);
/* 109 */         int pos = BufferUtil.flipToFill(bigger);
/* 110 */         BufferUtil.put(this._inflated, bigger);
/* 111 */         BufferUtil.put(chunk, bigger);
/* 112 */         BufferUtil.flipToFlush(bigger, pos);
/* 113 */         release(this._inflated);
/* 114 */         release(chunk);
/* 115 */         this._inflated = bigger;
/*     */       }
/*     */     }
/* 118 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void decodeChunks(ByteBuffer compressed)
/*     */   {
/* 129 */     ByteBuffer buffer = null;
/*     */     try
/*     */     {
/*     */       for (;;)
/*     */       {
/* 134 */         switch (this._state)
/*     */         {
/*     */ 
/*     */         case INITIAL: 
/* 138 */           this._state = State.ID;
/* 139 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case FLAGS: 
/* 144 */           if ((this._flags & 0x4) == 4)
/*     */           {
/* 146 */             this._state = State.EXTRA_LENGTH;
/* 147 */             this._size = 0;
/* 148 */             this._value = 0;
/*     */           }
/* 150 */           else if ((this._flags & 0x8) == 8) {
/* 151 */             this._state = State.NAME;
/* 152 */           } else if ((this._flags & 0x10) == 16) {
/* 153 */             this._state = State.COMMENT;
/* 154 */           } else if ((this._flags & 0x2) == 2)
/*     */           {
/* 156 */             this._state = State.HCRC;
/* 157 */             this._size = 0;
/* 158 */             this._value = 0;
/*     */           }
/*     */           else
/*     */           {
/* 162 */             this._state = State.DATA; }
/* 163 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */         case DATA: 
/*     */           do
/*     */           {
/*     */             for (;;)
/*     */             {
/* 172 */               if (buffer == null) {
/* 173 */                 buffer = acquire(this._bufferSize);
/*     */               }
/*     */               try
/*     */               {
/* 177 */                 int length = this._inflater.inflate(buffer.array(), buffer.arrayOffset(), buffer.capacity());
/* 178 */                 buffer.limit(length);
/*     */               }
/*     */               catch (DataFormatException x)
/*     */               {
/* 182 */                 throw new ZipException(x.getMessage());
/*     */               }
/*     */               
/* 185 */               if (buffer.hasRemaining())
/*     */               {
/* 187 */                 ByteBuffer chunk = buffer;
/* 188 */                 buffer = null;
/* 189 */                 if (decodedChunk(chunk))
/* 190 */                   return;
/*     */               } else {
/* 192 */                 if (!this._inflater.needsInput())
/*     */                   break;
/* 194 */                 if (!compressed.hasRemaining())
/* 195 */                   return;
/* 196 */                 if (compressed.hasArray())
/*     */                 {
/* 198 */                   this._inflater.setInput(compressed.array(), compressed.arrayOffset() + compressed.position(), compressed.remaining());
/* 199 */                   compressed.position(compressed.limit());
/*     */ 
/*     */                 }
/*     */                 else
/*     */                 {
/* 204 */                   byte[] input = new byte[compressed.remaining()];
/* 205 */                   compressed.get(input);
/* 206 */                   this._inflater.setInput(input);
/*     */                 }
/*     */               }
/* 209 */             } } while (!this._inflater.finished());
/*     */           
/* 211 */           int remaining = this._inflater.getRemaining();
/* 212 */           compressed.position(compressed.limit() - remaining);
/* 213 */           this._state = State.CRC;
/* 214 */           this._size = 0;
/* 215 */           this._value = 0;
/* 216 */           break;
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         default: 
/* 226 */           if (!compressed.hasRemaining()) {
/*     */             break label992;
/*     */           }
/* 229 */           byte currByte = compressed.get();
/* 230 */           switch (this._state)
/*     */           {
/*     */ 
/*     */           case ID: 
/* 234 */             this._value += ((currByte & 0xFF) << 8 * this._size);
/* 235 */             this._size += 1;
/* 236 */             if (this._size == 2)
/*     */             {
/* 238 */               if (this._value != 35615)
/* 239 */                 throw new ZipException("Invalid gzip bytes");
/* 240 */               this._state = State.CM;
/*     */             }
/*     */             
/*     */ 
/*     */             break;
/*     */           case CM: 
/* 246 */             if ((currByte & 0xFF) != 8)
/* 247 */               throw new ZipException("Invalid gzip compression method");
/* 248 */             this._state = State.FLG;
/* 249 */             break;
/*     */           
/*     */ 
/*     */           case FLG: 
/* 253 */             this._flags = currByte;
/* 254 */             this._state = State.MTIME;
/* 255 */             this._size = 0;
/* 256 */             this._value = 0;
/* 257 */             break;
/*     */           
/*     */ 
/*     */ 
/*     */           case MTIME: 
/* 262 */             this._size += 1;
/* 263 */             if (this._size == 4) {
/* 264 */               this._state = State.XFL;
/*     */             }
/*     */             
/*     */ 
/*     */             break;
/*     */           case XFL: 
/* 270 */             this._state = State.OS;
/* 271 */             break;
/*     */           
/*     */ 
/*     */ 
/*     */           case OS: 
/* 276 */             this._state = State.FLAGS;
/* 277 */             break;
/*     */           
/*     */ 
/*     */           case EXTRA_LENGTH: 
/* 281 */             this._value += ((currByte & 0xFF) << 8 * this._size);
/* 282 */             this._size += 1;
/* 283 */             if (this._size == 2) {
/* 284 */               this._state = State.EXTRA;
/*     */             }
/*     */             
/*     */ 
/*     */             break;
/*     */           case EXTRA: 
/* 290 */             this._value -= 1;
/* 291 */             if (this._value == 0)
/*     */             {
/*     */ 
/* 294 */               this._flags = ((byte)(this._flags & 0xFFFFFFFB));
/* 295 */               this._state = State.FLAGS;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */             break;
/*     */           case NAME: 
/* 302 */             if (currByte == 0)
/*     */             {
/*     */ 
/* 305 */               this._flags = ((byte)(this._flags & 0xFFFFFFF7));
/* 306 */               this._state = State.FLAGS;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */             break;
/*     */           case COMMENT: 
/* 313 */             if (currByte == 0)
/*     */             {
/*     */ 
/* 316 */               this._flags = ((byte)(this._flags & 0xFFFFFFEF));
/* 317 */               this._state = State.FLAGS;
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */             break;
/*     */           case HCRC: 
/* 324 */             this._size += 1;
/* 325 */             if (this._size == 2)
/*     */             {
/*     */ 
/* 328 */               this._flags = ((byte)(this._flags & 0xFFFFFFFD));
/* 329 */               this._state = State.FLAGS;
/*     */             }
/*     */             
/*     */ 
/*     */             break;
/*     */           case CRC: 
/* 335 */             this._value += ((currByte & 0xFF) << 8 * this._size);
/* 336 */             this._size += 1;
/* 337 */             if (this._size == 4)
/*     */             {
/*     */ 
/* 340 */               this._state = State.ISIZE;
/* 341 */               this._size = 0;
/* 342 */               this._value = 0;
/*     */             }
/*     */             
/*     */ 
/*     */             break;
/*     */           case ISIZE: 
/* 348 */             this._value += ((currByte & 0xFF) << 8 * this._size);
/* 349 */             this._size += 1;
/* 350 */             if (this._size == 4)
/*     */             {
/* 352 */               if (this._value != this._inflater.getBytesWritten()) {
/* 353 */                 throw new ZipException("Invalid input size");
/*     */               }
/*     */               
/* 356 */               reset(); return;
/*     */             }
/*     */             
/*     */ 
/*     */             break;
/*     */           default: 
/* 362 */             throw new ZipException(); }
/*     */           
/*     */           break; }
/*     */         
/*     */       }
/*     */     } catch (ZipException x) { label992:
/* 368 */       throw new RuntimeException(x);
/*     */     }
/*     */     finally
/*     */     {
/* 372 */       if (buffer != null) {
/* 373 */         release(buffer);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void reset() {
/* 379 */     this._inflater.reset();
/* 380 */     this._state = State.INITIAL;
/* 381 */     this._size = 0;
/* 382 */     this._value = 0;
/* 383 */     this._flags = 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 389 */     this._inflater.end();
/*     */   }
/*     */   
/*     */   public boolean isFinished()
/*     */   {
/* 394 */     return this._state == State.INITIAL;
/*     */   }
/*     */   
/*     */   private static enum State
/*     */   {
/* 399 */     INITIAL,  ID,  CM,  FLG,  MTIME,  XFL,  OS,  FLAGS,  EXTRA_LENGTH,  EXTRA,  NAME,  COMMENT,  HCRC,  DATA,  CRC,  ISIZE;
/*     */     
/*     */ 
/*     */     private State() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer acquire(int capacity)
/*     */   {
/* 408 */     return this._pool == null ? BufferUtil.allocate(capacity) : this._pool.acquire(capacity, false);
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
/*     */   public void release(ByteBuffer buffer)
/*     */   {
/* 421 */     boolean isTheEmptyBuffer = buffer == BufferUtil.EMPTY_BUFFER;
/* 422 */     if ((this._pool != null) && (!isTheEmptyBuffer)) {
/* 423 */       this._pool.release(buffer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\GZIPContentDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */