/*     */ package org.eclipse.jetty.websocket.common;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
/*     */ import org.eclipse.jetty.websocket.common.frames.BinaryFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.CloseFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.ContinuationFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.PingFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.PongFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.TextFrame;
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
/*     */ public abstract class WebSocketFrame
/*     */   implements Frame
/*     */ {
/*     */   protected byte finRsvOp;
/*     */   
/*     */   public static WebSocketFrame copy(Frame original)
/*     */   {
/*     */     WebSocketFrame copy;
/*     */     WebSocketFrame copy;
/*     */     WebSocketFrame copy;
/*     */     WebSocketFrame copy;
/*     */     WebSocketFrame copy;
/*     */     WebSocketFrame copy;
/*  62 */     switch (original.getOpCode())
/*     */     {
/*     */     case 2: 
/*  65 */       copy = new BinaryFrame();
/*  66 */       break;
/*     */     case 1: 
/*  68 */       copy = new TextFrame();
/*  69 */       break;
/*     */     case 8: 
/*  71 */       copy = new CloseFrame();
/*  72 */       break;
/*     */     case 0: 
/*  74 */       copy = new ContinuationFrame();
/*  75 */       break;
/*     */     case 9: 
/*  77 */       copy = new PingFrame();
/*  78 */       break;
/*     */     case 10: 
/*  80 */       copy = new PongFrame();
/*  81 */       break;
/*     */     case 3: case 4: case 5: case 6: case 7: default: 
/*  83 */       throw new IllegalArgumentException("Cannot copy frame with opcode " + original.getOpCode() + " - " + original);
/*     */     }
/*     */     WebSocketFrame copy;
/*  86 */     copy.copyHeaders(original);
/*  87 */     ByteBuffer payload = original.getPayload();
/*  88 */     if (payload != null)
/*     */     {
/*  90 */       ByteBuffer payloadCopy = ByteBuffer.allocate(payload.remaining());
/*  91 */       payloadCopy.put(payload.slice()).flip();
/*  92 */       copy.setPayload(payloadCopy);
/*     */     }
/*  94 */     return copy;
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
/* 109 */   protected boolean masked = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected byte[] mask;
/*     */   
/*     */ 
/*     */ 
/*     */   protected ByteBuffer data;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected WebSocketFrame(byte opcode)
/*     */   {
/* 125 */     reset();
/* 126 */     setOpCode(opcode);
/*     */   }
/*     */   
/*     */   public abstract void assertValid();
/*     */   
/*     */   protected void copyHeaders(Frame frame)
/*     */   {
/* 133 */     this.finRsvOp = 0;
/* 134 */     this.finRsvOp = ((byte)(this.finRsvOp | (frame.isFin() ? 128 : 0)));
/* 135 */     this.finRsvOp = ((byte)(this.finRsvOp | (frame.isRsv1() ? 64 : 0)));
/* 136 */     this.finRsvOp = ((byte)(this.finRsvOp | (frame.isRsv2() ? 32 : 0)));
/* 137 */     this.finRsvOp = ((byte)(this.finRsvOp | (frame.isRsv3() ? 16 : 0)));
/* 138 */     this.finRsvOp = ((byte)(this.finRsvOp | frame.getOpCode() & 0xF));
/*     */     
/* 140 */     this.masked = frame.isMasked();
/* 141 */     if (this.masked)
/*     */     {
/* 143 */       this.mask = frame.getMask();
/*     */     }
/*     */     else
/*     */     {
/* 147 */       this.mask = null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected void copyHeaders(WebSocketFrame copy)
/*     */   {
/* 153 */     this.finRsvOp = copy.finRsvOp;
/* 154 */     this.masked = copy.masked;
/* 155 */     this.mask = null;
/* 156 */     if (copy.mask != null) {
/* 157 */       this.mask = Arrays.copyOf(copy.mask, copy.mask.length);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 163 */     if (this == obj)
/*     */     {
/* 165 */       return true;
/*     */     }
/* 167 */     if (obj == null)
/*     */     {
/* 169 */       return false;
/*     */     }
/* 171 */     if (getClass() != obj.getClass())
/*     */     {
/* 173 */       return false;
/*     */     }
/* 175 */     WebSocketFrame other = (WebSocketFrame)obj;
/* 176 */     if (this.data == null)
/*     */     {
/* 178 */       if (other.data != null)
/*     */       {
/* 180 */         return false;
/*     */       }
/*     */     }
/* 183 */     else if (!this.data.equals(other.data))
/*     */     {
/* 185 */       return false;
/*     */     }
/* 187 */     if (this.finRsvOp != other.finRsvOp)
/*     */     {
/* 189 */       return false;
/*     */     }
/* 191 */     if (!Arrays.equals(this.mask, other.mask))
/*     */     {
/* 193 */       return false;
/*     */     }
/* 195 */     if (this.masked != other.masked)
/*     */     {
/* 197 */       return false;
/*     */     }
/* 199 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public byte[] getMask()
/*     */   {
/* 205 */     return this.mask;
/*     */   }
/*     */   
/*     */ 
/*     */   public final byte getOpCode()
/*     */   {
/* 211 */     return (byte)(this.finRsvOp & 0xF);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuffer getPayload()
/*     */   {
/* 220 */     return this.data;
/*     */   }
/*     */   
/*     */   public String getPayloadAsUTF8()
/*     */   {
/* 225 */     return BufferUtil.toUTF8String(getPayload());
/*     */   }
/*     */   
/*     */ 
/*     */   public int getPayloadLength()
/*     */   {
/* 231 */     if (this.data == null)
/*     */     {
/* 233 */       return 0;
/*     */     }
/* 235 */     return this.data.remaining();
/*     */   }
/*     */   
/*     */ 
/*     */   public Frame.Type getType()
/*     */   {
/* 241 */     return Frame.Type.from(getOpCode());
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 247 */     int prime = 31;
/* 248 */     int result = 1;
/* 249 */     result = 31 * result + (this.data == null ? 0 : this.data.hashCode());
/* 250 */     result = 31 * result + this.finRsvOp;
/* 251 */     result = 31 * result + Arrays.hashCode(this.mask);
/* 252 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasPayload()
/*     */   {
/* 258 */     return (this.data != null) && (this.data.hasRemaining());
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract boolean isControlFrame();
/*     */   
/*     */   public abstract boolean isDataFrame();
/*     */   
/*     */   public boolean isFin()
/*     */   {
/* 268 */     return (byte)(this.finRsvOp & 0x80) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isLast()
/*     */   {
/* 274 */     return isFin();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isMasked()
/*     */   {
/* 280 */     return this.masked;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isRsv1()
/*     */   {
/* 286 */     return (byte)(this.finRsvOp & 0x40) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isRsv2()
/*     */   {
/* 292 */     return (byte)(this.finRsvOp & 0x20) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isRsv3()
/*     */   {
/* 298 */     return (byte)(this.finRsvOp & 0x10) != 0;
/*     */   }
/*     */   
/*     */   public void reset()
/*     */   {
/* 303 */     this.finRsvOp = Byte.MIN_VALUE;
/* 304 */     this.masked = false;
/* 305 */     this.data = null;
/* 306 */     this.mask = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketFrame setFin(boolean fin)
/*     */   {
/* 312 */     this.finRsvOp = ((byte)(this.finRsvOp & 0x7F | (fin ? 128 : 0)));
/* 313 */     return this;
/*     */   }
/*     */   
/*     */   public Frame setMask(byte[] maskingKey)
/*     */   {
/* 318 */     this.mask = maskingKey;
/* 319 */     this.masked = (this.mask != null);
/* 320 */     return this;
/*     */   }
/*     */   
/*     */   public Frame setMasked(boolean mask)
/*     */   {
/* 325 */     this.masked = mask;
/* 326 */     return this;
/*     */   }
/*     */   
/*     */   protected WebSocketFrame setOpCode(byte op)
/*     */   {
/* 331 */     this.finRsvOp = ((byte)(this.finRsvOp & 0xF0 | op & 0xF));
/* 332 */     return this;
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
/*     */   public WebSocketFrame setPayload(ByteBuffer buf)
/*     */   {
/* 348 */     this.data = buf;
/* 349 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketFrame setRsv1(boolean rsv1)
/*     */   {
/* 355 */     this.finRsvOp = ((byte)(this.finRsvOp & 0xBF | (rsv1 ? 64 : 0)));
/* 356 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketFrame setRsv2(boolean rsv2)
/*     */   {
/* 362 */     this.finRsvOp = ((byte)(this.finRsvOp & 0xDF | (rsv2 ? 32 : 0)));
/* 363 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public WebSocketFrame setRsv3(boolean rsv3)
/*     */   {
/* 369 */     this.finRsvOp = ((byte)(this.finRsvOp & 0xEF | (rsv3 ? 16 : 0)));
/* 370 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 376 */     StringBuilder b = new StringBuilder();
/* 377 */     b.append(OpCode.name((byte)(this.finRsvOp & 0xF)));
/* 378 */     b.append('[');
/* 379 */     b.append("len=").append(getPayloadLength());
/* 380 */     b.append(",fin=").append((this.finRsvOp & 0x80) != 0);
/* 381 */     b.append(",rsv=");
/* 382 */     b.append((this.finRsvOp & 0x40) != 0 ? '1' : '.');
/* 383 */     b.append((this.finRsvOp & 0x20) != 0 ? '1' : '.');
/* 384 */     b.append((this.finRsvOp & 0x10) != 0 ? '1' : '.');
/* 385 */     b.append(",masked=").append(this.masked);
/* 386 */     b.append(']');
/* 387 */     return b.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\WebSocketFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */