/*     */ package org.eclipse.jetty.websocket.common;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.MessageTooLargeException;
/*     */ import org.eclipse.jetty.websocket.api.ProtocolException;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketBehavior;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketException;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Extension;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
/*     */ import org.eclipse.jetty.websocket.common.frames.BinaryFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.CloseFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.ContinuationFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.PingFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.PongFrame;
/*     */ import org.eclipse.jetty.websocket.common.frames.TextFrame;
/*     */ import org.eclipse.jetty.websocket.common.io.payload.DeMaskProcessor;
/*     */ import org.eclipse.jetty.websocket.common.io.payload.PayloadProcessor;
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
/*     */ public class Parser
/*     */ {
/*     */   private static enum State
/*     */   {
/*  53 */     START, 
/*  54 */     PAYLOAD_LEN, 
/*  55 */     PAYLOAD_LEN_BYTES, 
/*  56 */     MASK, 
/*  57 */     MASK_BYTES, 
/*  58 */     PAYLOAD;
/*     */     
/*     */     private State() {} }
/*  61 */   private static final Logger LOG = Log.getLogger(Parser.class);
/*     */   
/*     */   private final WebSocketPolicy policy;
/*     */   
/*     */   private final ByteBufferPool bufferPool;
/*  66 */   private State state = State.START;
/*  67 */   private int cursor = 0;
/*     */   
/*     */   private WebSocketFrame frame;
/*     */   
/*     */   private boolean priorDataFrame;
/*     */   private ByteBuffer payload;
/*     */   private int payloadLength;
/*  74 */   private PayloadProcessor maskProcessor = new DeMaskProcessor();
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
/*  87 */   private byte flagsInUse = 0;
/*     */   
/*     */   private IncomingFrames incomingFramesHandler;
/*     */   
/*     */   public Parser(WebSocketPolicy wspolicy, ByteBufferPool bufferPool)
/*     */   {
/*  93 */     this.bufferPool = bufferPool;
/*  94 */     this.policy = wspolicy;
/*     */   }
/*     */   
/*     */   private void assertSanePayloadLength(long len)
/*     */   {
/*  99 */     if (LOG.isDebugEnabled()) {
/* 100 */       LOG.debug("{} Payload Length: {} - {}", new Object[] { this.policy.getBehavior(), Long.valueOf(len), this });
/*     */     }
/*     */     
/*     */ 
/* 104 */     if (len > 2147483647L)
/*     */     {
/*     */ 
/* 107 */       throw new MessageTooLargeException("[int-sane!] cannot handle payload lengths larger than 2147483647");
/*     */     }
/*     */     
/* 110 */     switch (this.frame.getOpCode())
/*     */     {
/*     */     case 8: 
/* 113 */       if (len == 1L)
/*     */       {
/* 115 */         throw new ProtocolException("Invalid close frame payload length, [" + this.payloadLength + "]");
/*     */       }
/*     */     
/*     */     case 9: 
/*     */     case 10: 
/* 120 */       if (len > 125L)
/*     */       {
/* 122 */         throw new ProtocolException("Invalid control frame payload length, [" + this.payloadLength + "] cannot exceed [" + 125 + "]");
/*     */       }
/*     */       
/*     */       break;
/*     */     case 1: 
/* 127 */       this.policy.assertValidTextMessageSize((int)len);
/* 128 */       break;
/*     */     case 2: 
/* 130 */       this.policy.assertValidBinaryMessageSize((int)len);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public void configureFromExtensions(List<? extends Extension> exts)
/*     */   {
/* 138 */     this.flagsInUse = 0;
/*     */     
/*     */ 
/* 141 */     for (Extension ext : exts)
/*     */     {
/* 143 */       if (ext.isRsv1User())
/*     */       {
/* 145 */         this.flagsInUse = ((byte)(this.flagsInUse | 0x40));
/*     */       }
/* 147 */       if (ext.isRsv2User())
/*     */       {
/* 149 */         this.flagsInUse = ((byte)(this.flagsInUse | 0x20));
/*     */       }
/* 151 */       if (ext.isRsv3User())
/*     */       {
/* 153 */         this.flagsInUse = ((byte)(this.flagsInUse | 0x10));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public IncomingFrames getIncomingFramesHandler()
/*     */   {
/* 160 */     return this.incomingFramesHandler;
/*     */   }
/*     */   
/*     */   public WebSocketPolicy getPolicy()
/*     */   {
/* 165 */     return this.policy;
/*     */   }
/*     */   
/*     */   public boolean isRsv1InUse()
/*     */   {
/* 170 */     return (this.flagsInUse & 0x40) != 0;
/*     */   }
/*     */   
/*     */   public boolean isRsv2InUse()
/*     */   {
/* 175 */     return (this.flagsInUse & 0x20) != 0;
/*     */   }
/*     */   
/*     */   public boolean isRsv3InUse()
/*     */   {
/* 180 */     return (this.flagsInUse & 0x10) != 0;
/*     */   }
/*     */   
/*     */   protected void notifyFrame(Frame f) throws WebSocketException
/*     */   {
/* 185 */     if (LOG.isDebugEnabled()) {
/* 186 */       LOG.debug("{} Notify {}", new Object[] { this.policy.getBehavior(), getIncomingFramesHandler() });
/*     */     }
/* 188 */     if (this.policy.getBehavior() == WebSocketBehavior.SERVER)
/*     */     {
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
/* 200 */       if (!f.isMasked())
/*     */       {
/* 202 */         throw new ProtocolException("Client MUST mask all frames (RFC-6455: Section 5.1)");
/*     */       }
/*     */     }
/* 205 */     else if (this.policy.getBehavior() == WebSocketBehavior.CLIENT)
/*     */     {
/*     */ 
/* 208 */       if (f.isMasked())
/*     */       {
/* 210 */         throw new ProtocolException("Server MUST NOT mask any frames (RFC-6455: Section 5.1)");
/*     */       }
/*     */     }
/*     */     
/* 214 */     if (this.incomingFramesHandler == null)
/*     */     {
/* 216 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 220 */       this.incomingFramesHandler.incomingFrame(f);
/*     */     }
/*     */     catch (WebSocketException e)
/*     */     {
/* 224 */       throw e;
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 228 */       throw new WebSocketException(t);
/*     */     }
/*     */   }
/*     */   
/*     */   public void parse(ByteBuffer buffer) throws WebSocketException
/*     */   {
/* 234 */     if (buffer.remaining() <= 0)
/*     */     {
/* 236 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 241 */       while (parseFrame(buffer))
/*     */       {
/* 243 */         if (LOG.isDebugEnabled())
/* 244 */           LOG.debug("{} Parsed Frame: {}", new Object[] { this.policy.getBehavior(), this.frame });
/* 245 */         notifyFrame(this.frame);
/* 246 */         if (this.frame.isDataFrame())
/*     */         {
/* 248 */           this.priorDataFrame = (!this.frame.isFin());
/*     */         }
/* 250 */         reset();
/*     */       }
/*     */     }
/*     */     catch (WebSocketException e)
/*     */     {
/* 255 */       buffer.position(buffer.limit());
/* 256 */       reset();
/*     */       
/* 258 */       throw e;
/*     */     }
/*     */     catch (Throwable t)
/*     */     {
/* 262 */       buffer.position(buffer.limit());
/* 263 */       reset();
/*     */       
/* 265 */       throw new WebSocketException(t);
/*     */     }
/*     */   }
/*     */   
/*     */   private void reset()
/*     */   {
/* 271 */     if (this.frame != null)
/* 272 */       this.frame.reset();
/* 273 */     this.frame = null;
/* 274 */     this.bufferPool.release(this.payload);
/* 275 */     this.payload = null;
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
/*     */   private boolean parseFrame(ByteBuffer buffer)
/*     */   {
/* 291 */     if (LOG.isDebugEnabled())
/*     */     {
/* 293 */       LOG.debug("{} Parsing {} bytes", new Object[] { this.policy.getBehavior(), Integer.valueOf(buffer.remaining()) });
/*     */     }
/* 295 */     while (buffer.hasRemaining())
/*     */     {
/* 297 */       switch (this.state)
/*     */       {
/*     */ 
/*     */ 
/*     */       case START: 
/* 302 */         byte b = buffer.get();
/* 303 */         boolean fin = (b & 0x80) != 0;
/*     */         
/* 305 */         byte opcode = (byte)(b & 0xF);
/*     */         
/* 307 */         if (!OpCode.isKnown(opcode))
/*     */         {
/* 309 */           throw new ProtocolException("Unknown opcode: " + opcode);
/*     */         }
/*     */         
/* 312 */         if (LOG.isDebugEnabled()) {
/* 313 */           LOG.debug("{} OpCode {}, fin={} rsv={}{}{}", new Object[] {this.policy
/* 314 */             .getBehavior(), 
/* 315 */             OpCode.name(opcode), 
/* 316 */             Boolean.valueOf(fin), 
/* 317 */             Character.valueOf((b & 0x40) != 0 ? 49 : '.'), 
/* 318 */             Character.valueOf((b & 0x20) != 0 ? 49 : '.'), 
/* 319 */             Character.valueOf((b & 0x10) != 0 ? 49 : '.') });
/*     */         }
/*     */         
/* 322 */         switch (opcode)
/*     */         {
/*     */         case 1: 
/* 325 */           this.frame = new TextFrame();
/*     */           
/* 327 */           if (this.priorDataFrame)
/*     */           {
/* 329 */             throw new ProtocolException("Unexpected " + OpCode.name(opcode) + " frame, was expecting CONTINUATION");
/*     */           }
/*     */           break;
/*     */         case 2: 
/* 333 */           this.frame = new BinaryFrame();
/*     */           
/* 335 */           if (this.priorDataFrame)
/*     */           {
/* 337 */             throw new ProtocolException("Unexpected " + OpCode.name(opcode) + " frame, was expecting CONTINUATION");
/*     */           }
/*     */           break;
/*     */         case 0: 
/* 341 */           this.frame = new ContinuationFrame();
/*     */           
/* 343 */           if (!this.priorDataFrame)
/*     */           {
/* 345 */             throw new ProtocolException("CONTINUATION frame without prior !FIN");
/*     */           }
/*     */           
/*     */           break;
/*     */         case 8: 
/* 350 */           this.frame = new CloseFrame();
/*     */           
/* 352 */           if (!fin)
/*     */           {
/* 354 */             throw new ProtocolException("Fragmented Close Frame [" + OpCode.name(opcode) + "]");
/*     */           }
/*     */           break;
/*     */         case 9: 
/* 358 */           this.frame = new PingFrame();
/*     */           
/* 360 */           if (!fin)
/*     */           {
/* 362 */             throw new ProtocolException("Fragmented Ping Frame [" + OpCode.name(opcode) + "]");
/*     */           }
/*     */           break;
/*     */         case 10: 
/* 366 */           this.frame = new PongFrame();
/*     */           
/* 368 */           if (!fin)
/*     */           {
/* 370 */             throw new ProtocolException("Fragmented Pong Frame [" + OpCode.name(opcode) + "]");
/*     */           }
/*     */           break;
/*     */         }
/*     */         
/* 375 */         this.frame.setFin(fin);
/*     */         
/*     */ 
/* 378 */         if ((b & 0x70) != 0)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 386 */           if ((b & 0x40) != 0)
/*     */           {
/* 388 */             if (isRsv1InUse()) {
/* 389 */               this.frame.setRsv1(true);
/*     */             }
/*     */             else {
/* 392 */               String err = "RSV1 not allowed to be set";
/* 393 */               if (LOG.isDebugEnabled())
/*     */               {
/* 395 */                 LOG.debug(err + ": Remaining buffer: {}", new Object[] { BufferUtil.toDetailString(buffer) });
/*     */               }
/* 397 */               throw new ProtocolException(err);
/*     */             }
/*     */           }
/* 400 */           if ((b & 0x20) != 0)
/*     */           {
/* 402 */             if (isRsv2InUse()) {
/* 403 */               this.frame.setRsv2(true);
/*     */             }
/*     */             else {
/* 406 */               String err = "RSV2 not allowed to be set";
/* 407 */               if (LOG.isDebugEnabled())
/*     */               {
/* 409 */                 LOG.debug(err + ": Remaining buffer: {}", new Object[] { BufferUtil.toDetailString(buffer) });
/*     */               }
/* 411 */               throw new ProtocolException(err);
/*     */             }
/*     */           }
/* 414 */           if ((b & 0x10) != 0)
/*     */           {
/* 416 */             if (isRsv3InUse()) {
/* 417 */               this.frame.setRsv3(true);
/*     */             }
/*     */             else {
/* 420 */               String err = "RSV3 not allowed to be set";
/* 421 */               if (LOG.isDebugEnabled())
/*     */               {
/* 423 */                 LOG.debug(err + ": Remaining buffer: {}", new Object[] { BufferUtil.toDetailString(buffer) });
/*     */               }
/* 425 */               throw new ProtocolException(err);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 430 */         this.state = State.PAYLOAD_LEN;
/* 431 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case PAYLOAD_LEN: 
/* 436 */         byte b = buffer.get();
/* 437 */         this.frame.setMasked((b & 0x80) != 0);
/* 438 */         this.payloadLength = ((byte)(0x7F & b));
/*     */         
/* 440 */         if (this.payloadLength == 127)
/*     */         {
/*     */ 
/* 443 */           this.payloadLength = 0;
/* 444 */           this.state = State.PAYLOAD_LEN_BYTES;
/* 445 */           this.cursor = 8;
/*     */ 
/*     */         }
/* 448 */         else if (this.payloadLength == 126)
/*     */         {
/*     */ 
/* 451 */           this.payloadLength = 0;
/* 452 */           this.state = State.PAYLOAD_LEN_BYTES;
/* 453 */           this.cursor = 2;
/*     */         }
/*     */         else
/*     */         {
/* 457 */           assertSanePayloadLength(this.payloadLength);
/* 458 */           if (this.frame.isMasked())
/*     */           {
/* 460 */             this.state = State.MASK;
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 465 */             if (this.payloadLength == 0)
/*     */             {
/* 467 */               this.state = State.START;
/* 468 */               return true;
/*     */             }
/*     */             
/* 471 */             this.maskProcessor.reset(this.frame);
/* 472 */             this.state = State.PAYLOAD;
/*     */           }
/*     */         }
/* 475 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case PAYLOAD_LEN_BYTES: 
/* 480 */         byte b = buffer.get();
/* 481 */         this.cursor -= 1;
/* 482 */         this.payloadLength |= (b & 0xFF) << 8 * this.cursor;
/* 483 */         if (this.cursor == 0)
/*     */         {
/* 485 */           assertSanePayloadLength(this.payloadLength);
/* 486 */           if (this.frame.isMasked())
/*     */           {
/* 488 */             this.state = State.MASK;
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 493 */             if (this.payloadLength == 0)
/*     */             {
/* 495 */               this.state = State.START;
/* 496 */               return true;
/*     */             }
/*     */             
/* 499 */             this.maskProcessor.reset(this.frame);
/* 500 */             this.state = State.PAYLOAD;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */         break;
/*     */       case MASK: 
/* 508 */         byte[] m = new byte[4];
/* 509 */         this.frame.setMask(m);
/* 510 */         if (buffer.remaining() >= 4)
/*     */         {
/* 512 */           buffer.get(m, 0, 4);
/*     */           
/* 514 */           if (this.payloadLength == 0)
/*     */           {
/* 516 */             this.state = State.START;
/* 517 */             return true;
/*     */           }
/*     */           
/* 520 */           this.maskProcessor.reset(this.frame);
/* 521 */           this.state = State.PAYLOAD;
/*     */         }
/*     */         else
/*     */         {
/* 525 */           this.state = State.MASK_BYTES;
/* 526 */           this.cursor = 4;
/*     */         }
/* 528 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case MASK_BYTES: 
/* 533 */         byte b = buffer.get();
/* 534 */         this.frame.getMask()[(4 - this.cursor)] = b;
/* 535 */         this.cursor -= 1;
/* 536 */         if (this.cursor == 0)
/*     */         {
/*     */ 
/* 539 */           if (this.payloadLength == 0)
/*     */           {
/* 541 */             this.state = State.START;
/* 542 */             return true;
/*     */           }
/*     */           
/* 545 */           this.maskProcessor.reset(this.frame);
/* 546 */           this.state = State.PAYLOAD;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */         break;
/*     */       case PAYLOAD: 
/* 553 */         this.frame.assertValid();
/* 554 */         if (parsePayload(buffer))
/*     */         {
/*     */ 
/* 557 */           if (this.frame.getOpCode() == 8)
/*     */           {
/*     */ 
/* 560 */             new CloseInfo(this.frame);
/*     */           }
/* 562 */           this.state = State.START;
/*     */           
/* 564 */           return true;
/*     */         }
/*     */         
/*     */         break;
/*     */       }
/*     */       
/*     */     }
/* 571 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean parsePayload(ByteBuffer buffer)
/*     */   {
/* 583 */     if (this.payloadLength == 0)
/*     */     {
/* 585 */       return true;
/*     */     }
/*     */     
/* 588 */     if (buffer.hasRemaining())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 593 */       int bytesSoFar = this.payload == null ? 0 : this.payload.position();
/* 594 */       int bytesExpected = this.payloadLength - bytesSoFar;
/* 595 */       int bytesAvailable = buffer.remaining();
/* 596 */       int windowBytes = Math.min(bytesAvailable, bytesExpected);
/* 597 */       int limit = buffer.limit();
/* 598 */       buffer.limit(buffer.position() + windowBytes);
/* 599 */       ByteBuffer window = buffer.slice();
/* 600 */       buffer.limit(limit);
/* 601 */       buffer.position(buffer.position() + window.remaining());
/*     */       
/* 603 */       if (LOG.isDebugEnabled()) {
/* 604 */         LOG.debug("{} Window: {}", new Object[] { this.policy.getBehavior(), BufferUtil.toDetailString(window) });
/*     */       }
/*     */       
/* 607 */       this.maskProcessor.process(window);
/*     */       
/* 609 */       if (window.remaining() == this.payloadLength)
/*     */       {
/*     */ 
/* 612 */         this.frame.setPayload(window);
/* 613 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 617 */       if (this.payload == null)
/*     */       {
/* 619 */         this.payload = this.bufferPool.acquire(this.payloadLength, false);
/* 620 */         BufferUtil.clearToFill(this.payload);
/*     */       }
/*     */       
/* 623 */       this.payload.put(window);
/*     */       
/* 625 */       if (this.payload.position() == this.payloadLength)
/*     */       {
/* 627 */         BufferUtil.flipToFlush(this.payload, 0);
/* 628 */         this.frame.setPayload(this.payload);
/* 629 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 633 */     return false;
/*     */   }
/*     */   
/*     */   public void setIncomingFramesHandler(IncomingFrames incoming)
/*     */   {
/* 638 */     this.incomingFramesHandler = incoming;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 644 */     StringBuilder builder = new StringBuilder();
/* 645 */     builder.append("Parser@").append(Integer.toHexString(hashCode()));
/* 646 */     builder.append("[");
/* 647 */     if (this.incomingFramesHandler == null)
/*     */     {
/* 649 */       builder.append("NO_HANDLER");
/*     */     }
/*     */     else
/*     */     {
/* 653 */       builder.append(this.incomingFramesHandler.getClass().getSimpleName());
/*     */     }
/* 655 */     builder.append(",s=").append(this.state);
/* 656 */     builder.append(",c=").append(this.cursor);
/* 657 */     builder.append(",len=").append(this.payloadLength);
/* 658 */     builder.append(",f=").append(this.frame);
/*     */     
/* 660 */     builder.append("]");
/* 661 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\Parser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */