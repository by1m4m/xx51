/*     */ package org.eclipse.jetty.websocket.common;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.websocket.api.ProtocolException;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketBehavior;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Extension;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
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
/*     */ public class Generator
/*     */ {
/*     */   public static final int MAX_HEADER_LENGTH = 28;
/*     */   private final WebSocketBehavior behavior;
/*     */   private final ByteBufferPool bufferPool;
/*     */   private final boolean validating;
/*     */   private final boolean readOnly;
/*  78 */   private byte flagsInUse = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Generator(WebSocketPolicy policy, ByteBufferPool bufferPool)
/*     */   {
/*  90 */     this(policy, bufferPool, true, false);
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
/*     */   public Generator(WebSocketPolicy policy, ByteBufferPool bufferPool, boolean validating)
/*     */   {
/* 105 */     this(policy, bufferPool, validating, false);
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
/*     */   public Generator(WebSocketPolicy policy, ByteBufferPool bufferPool, boolean validating, boolean readOnly)
/*     */   {
/* 122 */     this.behavior = policy.getBehavior();
/* 123 */     this.bufferPool = bufferPool;
/* 124 */     this.validating = validating;
/* 125 */     this.readOnly = readOnly;
/*     */   }
/*     */   
/*     */   public void assertFrameValid(Frame frame)
/*     */   {
/* 130 */     if (!this.validating)
/*     */     {
/* 132 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 141 */     if ((frame.isRsv1()) && (!isRsv1InUse()))
/*     */     {
/* 143 */       throw new ProtocolException("RSV1 not allowed to be set");
/*     */     }
/*     */     
/* 146 */     if ((frame.isRsv2()) && (!isRsv2InUse()))
/*     */     {
/* 148 */       throw new ProtocolException("RSV2 not allowed to be set");
/*     */     }
/*     */     
/* 151 */     if ((frame.isRsv3()) && (!isRsv3InUse()))
/*     */     {
/* 153 */       throw new ProtocolException("RSV3 not allowed to be set");
/*     */     }
/*     */     
/* 156 */     if (OpCode.isControlFrame(frame.getOpCode()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */       if (frame.getPayloadLength() > 125)
/*     */       {
/* 165 */         throw new ProtocolException("Invalid control frame payload length");
/*     */       }
/*     */       
/* 168 */       if (!frame.isFin())
/*     */       {
/* 170 */         throw new ProtocolException("Control Frames must be FIN=true");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 178 */       if (frame.getOpCode() == 8)
/*     */       {
/*     */ 
/* 181 */         ByteBuffer payload = frame.getPayload();
/* 182 */         if (payload != null)
/*     */         {
/* 184 */           new CloseInfo(payload, true);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void configureFromExtensions(List<? extends Extension> exts)
/*     */   {
/* 193 */     this.flagsInUse = 0;
/*     */     
/*     */ 
/* 196 */     for (Extension ext : exts)
/*     */     {
/* 198 */       if (ext.isRsv1User())
/*     */       {
/* 200 */         this.flagsInUse = ((byte)(this.flagsInUse | 0x40));
/*     */       }
/* 202 */       if (ext.isRsv2User())
/*     */       {
/* 204 */         this.flagsInUse = ((byte)(this.flagsInUse | 0x20));
/*     */       }
/* 206 */       if (ext.isRsv3User())
/*     */       {
/* 208 */         this.flagsInUse = ((byte)(this.flagsInUse | 0x10));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ByteBuffer generateHeaderBytes(Frame frame)
/*     */   {
/* 215 */     ByteBuffer buffer = this.bufferPool.acquire(28, true);
/* 216 */     generateHeaderBytes(frame, buffer);
/* 217 */     return buffer;
/*     */   }
/*     */   
/*     */   public void generateHeaderBytes(Frame frame, ByteBuffer buffer)
/*     */   {
/* 222 */     int p = BufferUtil.flipToFill(buffer);
/*     */     
/*     */ 
/* 225 */     assertFrameValid(frame);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 230 */     byte b = 0;
/*     */     
/*     */ 
/* 233 */     if (frame.isFin())
/*     */     {
/* 235 */       b = (byte)(b | 0x80);
/*     */     }
/*     */     
/*     */ 
/* 239 */     if (frame.isRsv1())
/*     */     {
/* 241 */       b = (byte)(b | 0x40);
/*     */     }
/* 243 */     if (frame.isRsv2())
/*     */     {
/* 245 */       b = (byte)(b | 0x20);
/*     */     }
/* 247 */     if (frame.isRsv3())
/*     */     {
/* 249 */       b = (byte)(b | 0x10);
/*     */     }
/*     */     
/*     */ 
/* 253 */     byte opcode = frame.getOpCode();
/*     */     
/* 255 */     if (frame.getOpCode() == 0)
/*     */     {
/*     */ 
/* 258 */       opcode = 0;
/*     */     }
/*     */     
/* 261 */     b = (byte)(b | opcode & 0xF);
/*     */     
/* 263 */     buffer.put(b);
/*     */     
/*     */ 
/* 266 */     b = frame.isMasked() ? Byte.MIN_VALUE : 0;
/*     */     
/*     */ 
/* 269 */     int payloadLength = frame.getPayloadLength();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 274 */     if (payloadLength > 65535)
/*     */     {
/*     */ 
/* 277 */       b = (byte)(b | 0x7F);
/* 278 */       buffer.put(b);
/* 279 */       buffer.put((byte)0);
/* 280 */       buffer.put((byte)0);
/* 281 */       buffer.put((byte)0);
/* 282 */       buffer.put((byte)0);
/* 283 */       buffer.put((byte)(payloadLength >> 24 & 0xFF));
/* 284 */       buffer.put((byte)(payloadLength >> 16 & 0xFF));
/* 285 */       buffer.put((byte)(payloadLength >> 8 & 0xFF));
/* 286 */       buffer.put((byte)(payloadLength & 0xFF));
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 291 */     else if (payloadLength >= 126)
/*     */     {
/* 293 */       b = (byte)(b | 0x7E);
/* 294 */       buffer.put(b);
/* 295 */       buffer.put((byte)(payloadLength >> 8));
/* 296 */       buffer.put((byte)(payloadLength & 0xFF));
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 303 */       b = (byte)(b | payloadLength & 0x7F);
/* 304 */       buffer.put(b);
/*     */     }
/*     */     
/*     */ 
/* 308 */     if ((frame.isMasked()) && (!this.readOnly))
/*     */     {
/* 310 */       byte[] mask = frame.getMask();
/* 311 */       buffer.put(mask);
/* 312 */       int maskInt = 0;
/* 313 */       for (byte maskByte : mask) {
/* 314 */         maskInt = (maskInt << 8) + (maskByte & 0xFF);
/*     */       }
/*     */       
/* 317 */       ByteBuffer payload = frame.getPayload();
/* 318 */       if ((payload != null) && (payload.remaining() > 0))
/*     */       {
/* 320 */         int maskOffset = 0;
/* 321 */         int start = payload.position();
/* 322 */         int end = payload.limit();
/*     */         int remaining;
/* 324 */         while ((remaining = end - start) > 0)
/*     */         {
/* 326 */           if (remaining >= 4)
/*     */           {
/* 328 */             payload.putInt(start, payload.getInt(start) ^ maskInt);
/* 329 */             start += 4;
/*     */           }
/*     */           else
/*     */           {
/* 333 */             payload.put(start, (byte)(payload.get(start) ^ mask[(maskOffset & 0x3)]));
/* 334 */             start++;
/* 335 */             maskOffset++;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 341 */     BufferUtil.flipToFlush(buffer, p);
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
/*     */   public void generateWholeFrame(Frame frame, ByteBuffer buf)
/*     */   {
/* 356 */     buf.put(generateHeaderBytes(frame));
/* 357 */     if (frame.hasPayload())
/*     */     {
/* 359 */       if (this.readOnly)
/*     */       {
/* 361 */         buf.put(frame.getPayload().slice());
/*     */       }
/*     */       else
/*     */       {
/* 365 */         buf.put(frame.getPayload());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ByteBufferPool getBufferPool()
/*     */   {
/* 372 */     return this.bufferPool;
/*     */   }
/*     */   
/*     */   public void setRsv1InUse(boolean rsv1InUse)
/*     */   {
/* 377 */     if (this.readOnly)
/*     */     {
/* 379 */       throw new RuntimeException("Not allowed to modify read-only frame");
/*     */     }
/* 381 */     this.flagsInUse = ((byte)(this.flagsInUse & 0xBF | (rsv1InUse ? 64 : 0)));
/*     */   }
/*     */   
/*     */   public void setRsv2InUse(boolean rsv2InUse)
/*     */   {
/* 386 */     if (this.readOnly)
/*     */     {
/* 388 */       throw new RuntimeException("Not allowed to modify read-only frame");
/*     */     }
/* 390 */     this.flagsInUse = ((byte)(this.flagsInUse & 0xDF | (rsv2InUse ? 32 : 0)));
/*     */   }
/*     */   
/*     */   public void setRsv3InUse(boolean rsv3InUse)
/*     */   {
/* 395 */     if (this.readOnly)
/*     */     {
/* 397 */       throw new RuntimeException("Not allowed to modify read-only frame");
/*     */     }
/* 399 */     this.flagsInUse = ((byte)(this.flagsInUse & 0xEF | (rsv3InUse ? 16 : 0)));
/*     */   }
/*     */   
/*     */   public boolean isRsv1InUse()
/*     */   {
/* 404 */     return (this.flagsInUse & 0x40) != 0;
/*     */   }
/*     */   
/*     */   public boolean isRsv2InUse()
/*     */   {
/* 409 */     return (this.flagsInUse & 0x20) != 0;
/*     */   }
/*     */   
/*     */   public boolean isRsv3InUse()
/*     */   {
/* 414 */     return (this.flagsInUse & 0x10) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 420 */     StringBuilder builder = new StringBuilder();
/* 421 */     builder.append("Generator[");
/* 422 */     builder.append(this.behavior);
/* 423 */     if (this.validating)
/*     */     {
/* 425 */       builder.append(",validating");
/*     */     }
/* 427 */     if (isRsv1InUse())
/*     */     {
/* 429 */       builder.append(",+rsv1");
/*     */     }
/* 431 */     if (isRsv2InUse())
/*     */     {
/* 433 */       builder.append(",+rsv2");
/*     */     }
/* 435 */     if (isRsv3InUse())
/*     */     {
/* 437 */       builder.append(",+rsv3");
/*     */     }
/* 439 */     builder.append("]");
/* 440 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\Generator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */