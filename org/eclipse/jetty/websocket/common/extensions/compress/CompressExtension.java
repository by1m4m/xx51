/*     */ package org.eclipse.jetty.websocket.common.extensions.compress;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.zip.DataFormatException;
/*     */ import java.util.zip.Deflater;
/*     */ import java.util.zip.Inflater;
/*     */ import java.util.zip.ZipException;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.IteratingCallback;
/*     */ import org.eclipse.jetty.util.IteratingCallback.Action;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
/*     */ import org.eclipse.jetty.websocket.common.OpCode;
/*     */ import org.eclipse.jetty.websocket.common.extensions.AbstractExtension;
/*     */ import org.eclipse.jetty.websocket.common.frames.DataFrame;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CompressExtension
/*     */   extends AbstractExtension
/*     */ {
/*  44 */   protected static final byte[] TAIL_BYTES = { 0, 0, -1, -1 };
/*  45 */   protected static final ByteBuffer TAIL_BYTES_BUF = ByteBuffer.wrap(TAIL_BYTES);
/*  46 */   private static final Logger LOG = Log.getLogger(CompressExtension.class);
/*     */   
/*     */ 
/*     */   protected static final int TAIL_DROP_NEVER = 0;
/*     */   
/*     */ 
/*     */   protected static final int TAIL_DROP_ALWAYS = 1;
/*     */   
/*     */ 
/*     */   protected static final int TAIL_DROP_FIN_ONLY = 2;
/*     */   
/*     */ 
/*     */   protected static final int RSV_USE_ALWAYS = 0;
/*     */   
/*     */ 
/*     */   protected static final int RSV_USE_ONLY_FIRST = 1;
/*     */   
/*     */ 
/*     */   protected static final int INFLATE_BUFFER_SIZE = 8192;
/*     */   
/*     */ 
/*     */   protected static final int INPUT_MAX_BUFFER_SIZE = 8192;
/*     */   
/*     */ 
/*     */   private static final int DECOMPRESS_BUF_SIZE = 8192;
/*     */   
/*     */ 
/*     */   private static final boolean NOWRAP = true;
/*     */   
/*  75 */   private final Queue<FrameEntry> entries = new ArrayDeque();
/*  76 */   private final IteratingCallback flusher = new Flusher(null);
/*     */   private Deflater deflaterImpl;
/*     */   private Inflater inflaterImpl;
/*  79 */   protected AtomicInteger decompressCount = new AtomicInteger(0);
/*  80 */   private int tailDrop = 0;
/*  81 */   private int rsvUse = 0;
/*     */   
/*     */   protected CompressExtension()
/*     */   {
/*  85 */     this.tailDrop = getTailDropMode();
/*  86 */     this.rsvUse = getRsvUseMode();
/*     */   }
/*     */   
/*     */   public Deflater getDeflater()
/*     */   {
/*  91 */     if (this.deflaterImpl == null)
/*     */     {
/*  93 */       this.deflaterImpl = new Deflater(-1, true);
/*     */     }
/*  95 */     return this.deflaterImpl;
/*     */   }
/*     */   
/*     */   public Inflater getInflater()
/*     */   {
/* 100 */     if (this.inflaterImpl == null)
/*     */     {
/* 102 */       this.inflaterImpl = new Inflater(true);
/*     */     }
/* 104 */     return this.inflaterImpl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRsv1User()
/*     */   {
/* 113 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract int getTailDropMode();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract int getRsvUseMode();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void forwardIncoming(Frame frame, ByteAccumulator accumulator)
/*     */   {
/* 132 */     DataFrame newFrame = new DataFrame(frame);
/*     */     
/* 134 */     newFrame.setRsv1(false);
/*     */     
/* 136 */     ByteBuffer buffer = getBufferPool().acquire(accumulator.getLength(), false);
/*     */     try
/*     */     {
/* 139 */       BufferUtil.flipToFill(buffer);
/* 140 */       accumulator.transferTo(buffer);
/* 141 */       newFrame.setPayload(buffer);
/* 142 */       nextIncomingFrame(newFrame);
/*     */     }
/*     */     finally
/*     */     {
/* 146 */       getBufferPool().release(buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   protected ByteAccumulator newByteAccumulator()
/*     */   {
/* 152 */     int maxSize = Math.max(getPolicy().getMaxTextMessageSize(), getPolicy().getMaxBinaryMessageBufferSize());
/* 153 */     return new ByteAccumulator(maxSize);
/*     */   }
/*     */   
/*     */   protected void decompress(ByteAccumulator accumulator, ByteBuffer buf) throws DataFormatException
/*     */   {
/* 158 */     if ((buf == null) || (!buf.hasRemaining()))
/*     */     {
/* 160 */       return;
/*     */     }
/* 162 */     byte[] output = new byte[' '];
/*     */     
/* 164 */     Inflater inflater = getInflater();
/*     */     
/* 166 */     while ((buf.hasRemaining()) && (inflater.needsInput()))
/*     */     {
/* 168 */       if (!supplyInput(inflater, buf))
/*     */       {
/* 170 */         LOG.debug("Needed input, but no buffer could supply input", new Object[0]); return;
/*     */       }
/*     */       
/*     */       int read;
/*     */       
/* 175 */       while ((read = inflater.inflate(output)) >= 0)
/*     */       {
/* 177 */         if (read == 0)
/*     */         {
/* 179 */           LOG.debug("Decompress: read 0 {}", new Object[] { toDetail(inflater) });
/* 180 */           break;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 185 */         if (LOG.isDebugEnabled())
/*     */         {
/* 187 */           LOG.debug("Decompressed {} bytes: {}", new Object[] { Integer.valueOf(read), toDetail(inflater) });
/*     */         }
/*     */         
/* 190 */         accumulator.copyChunk(output, 0, read);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 195 */     if (LOG.isDebugEnabled())
/*     */     {
/* 197 */       LOG.debug("Decompress: exiting {}", new Object[] { toDetail(inflater) });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
/*     */   {
/* 208 */     if (this.flusher.isFailed())
/*     */     {
/* 210 */       notifyCallbackFailure(callback, new ZipException());
/* 211 */       return;
/*     */     }
/*     */     
/* 214 */     FrameEntry entry = new FrameEntry(frame, callback, batchMode, null);
/* 215 */     if (LOG.isDebugEnabled())
/* 216 */       LOG.debug("Queuing {}", new Object[] { entry });
/* 217 */     offerEntry(entry);
/* 218 */     this.flusher.iterate();
/*     */   }
/*     */   
/*     */   private void offerEntry(FrameEntry entry)
/*     */   {
/* 223 */     synchronized (this)
/*     */     {
/* 225 */       this.entries.offer(entry);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private FrameEntry pollEntry()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_1
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 10	org/eclipse/jetty/websocket/common/extensions/compress/CompressExtension:entries	Ljava/util/Queue;
/*     */     //   8: invokeinterface 68 1 0
/*     */     //   13: checkcast 62	org/eclipse/jetty/websocket/common/extensions/compress/CompressExtension$FrameEntry
/*     */     //   16: aload_1
/*     */     //   17: monitorexit
/*     */     //   18: areturn
/*     */     //   19: astore_2
/*     */     //   20: aload_1
/*     */     //   21: monitorexit
/*     */     //   22: aload_2
/*     */     //   23: athrow
/*     */     // Line number table:
/*     */     //   Java source line #231	-> byte code offset #0
/*     */     //   Java source line #233	-> byte code offset #4
/*     */     //   Java source line #234	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	24	0	this	CompressExtension
/*     */     //   2	19	1	Ljava/lang/Object;	Object
/*     */     //   19	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	18	19	finally
/*     */     //   19	22	19	finally
/*     */   }
/*     */   
/*     */   protected void notifyCallbackSuccess(WriteCallback callback)
/*     */   {
/*     */     try
/*     */     {
/* 241 */       if (callback != null) {
/* 242 */         callback.writeSuccess();
/*     */       }
/*     */     }
/*     */     catch (Throwable x) {
/* 246 */       if (LOG.isDebugEnabled()) {
/* 247 */         LOG.debug("Exception while notifying success of callback " + callback, x);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void notifyCallbackFailure(WriteCallback callback, Throwable failure)
/*     */   {
/*     */     try {
/* 255 */       if (callback != null) {
/* 256 */         callback.writeFailed(failure);
/*     */       }
/*     */     }
/*     */     catch (Throwable x) {
/* 260 */       if (LOG.isDebugEnabled()) {
/* 261 */         LOG.debug("Exception while notifying failure of callback " + callback, x);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean supplyInput(Inflater inflater, ByteBuffer buf) {
/* 267 */     if ((buf == null) || (buf.remaining() <= 0))
/*     */     {
/* 269 */       if (LOG.isDebugEnabled())
/*     */       {
/* 271 */         LOG.debug("No data left left to supply to Inflater", new Object[0]);
/*     */       }
/* 273 */       return false;
/*     */     }
/*     */     
/*     */     int len;
/*     */     
/*     */     byte[] input;
/*     */     int inputOffset;
/* 280 */     if (buf.hasArray())
/*     */     {
/*     */ 
/* 283 */       int len = buf.remaining();
/* 284 */       byte[] input = buf.array();
/* 285 */       int inputOffset = buf.position() + buf.arrayOffset();
/* 286 */       buf.position(buf.position() + len);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 291 */       len = Math.min(8192, buf.remaining());
/* 292 */       input = new byte[len];
/* 293 */       inputOffset = 0;
/* 294 */       buf.get(input, 0, len);
/*     */     }
/*     */     
/* 297 */     inflater.setInput(input, inputOffset, len);
/* 298 */     if (LOG.isDebugEnabled())
/*     */     {
/* 300 */       LOG.debug("Supplied {} input bytes: {}", new Object[] { Integer.valueOf(input.length), toDetail(inflater) });
/*     */     }
/* 302 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean supplyInput(Deflater deflater, ByteBuffer buf)
/*     */   {
/* 307 */     if ((buf == null) || (buf.remaining() <= 0))
/*     */     {
/* 309 */       if (LOG.isDebugEnabled())
/*     */       {
/* 311 */         LOG.debug("No data left left to supply to Deflater", new Object[0]);
/*     */       }
/* 313 */       return false;
/*     */     }
/*     */     
/*     */     int len;
/*     */     
/*     */     byte[] input;
/*     */     int inputOffset;
/* 320 */     if (buf.hasArray())
/*     */     {
/*     */ 
/* 323 */       int len = buf.remaining();
/* 324 */       byte[] input = buf.array();
/* 325 */       int inputOffset = buf.position() + buf.arrayOffset();
/* 326 */       buf.position(buf.position() + len);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 331 */       len = Math.min(8192, buf.remaining());
/* 332 */       input = new byte[len];
/* 333 */       inputOffset = 0;
/* 334 */       buf.get(input, 0, len);
/*     */     }
/*     */     
/* 337 */     deflater.setInput(input, inputOffset, len);
/* 338 */     if (LOG.isDebugEnabled())
/*     */     {
/* 340 */       LOG.debug("Supplied {} input bytes: {}", new Object[] { Integer.valueOf(input.length), toDetail(deflater) });
/*     */     }
/* 342 */     return true;
/*     */   }
/*     */   
/*     */   private static String toDetail(Inflater inflater)
/*     */   {
/* 347 */     return String.format("Inflater[finished=%b,read=%d,written=%d,remaining=%d,in=%d,out=%d]", new Object[] { Boolean.valueOf(inflater.finished()), Long.valueOf(inflater.getBytesRead()), 
/* 348 */       Long.valueOf(inflater.getBytesWritten()), Integer.valueOf(inflater.getRemaining()), Integer.valueOf(inflater.getTotalIn()), Integer.valueOf(inflater.getTotalOut()) });
/*     */   }
/*     */   
/*     */   private static String toDetail(Deflater deflater)
/*     */   {
/* 353 */     return String.format("Deflater[finished=%b,read=%d,written=%d,in=%d,out=%d]", new Object[] { Boolean.valueOf(deflater.finished()), Long.valueOf(deflater.getBytesRead()), Long.valueOf(deflater.getBytesWritten()), 
/* 354 */       Integer.valueOf(deflater.getTotalIn()), Integer.valueOf(deflater.getTotalOut()) });
/*     */   }
/*     */   
/*     */   public static boolean endsWithTail(ByteBuffer buf)
/*     */   {
/* 359 */     if ((buf == null) || (buf.remaining() < TAIL_BYTES.length))
/*     */     {
/* 361 */       return false;
/*     */     }
/* 363 */     int limit = buf.limit();
/* 364 */     for (int i = TAIL_BYTES.length; i > 0; i--)
/*     */     {
/* 366 */       if (buf.get(limit - i) != TAIL_BYTES[(TAIL_BYTES.length - i)])
/*     */       {
/* 368 */         return false;
/*     */       }
/*     */     }
/* 371 */     return true;
/*     */   }
/*     */   
/*     */   protected void doStop()
/*     */     throws Exception
/*     */   {
/* 377 */     if (this.deflaterImpl != null)
/* 378 */       this.deflaterImpl.end();
/* 379 */     if (this.inflaterImpl != null)
/* 380 */       this.inflaterImpl.end();
/* 381 */     super.doStop();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 387 */     return getClass().getSimpleName();
/*     */   }
/*     */   
/*     */   private static class FrameEntry
/*     */   {
/*     */     private final Frame frame;
/*     */     private final WriteCallback callback;
/*     */     private final BatchMode batchMode;
/*     */     
/*     */     private FrameEntry(Frame frame, WriteCallback callback, BatchMode batchMode)
/*     */     {
/* 398 */       this.frame = frame;
/* 399 */       this.callback = callback;
/* 400 */       this.batchMode = batchMode;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 406 */       return this.frame.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private class Flusher extends IteratingCallback implements WriteCallback
/*     */   {
/*     */     private CompressExtension.FrameEntry current;
/* 413 */     private boolean finished = true;
/*     */     
/*     */     private Flusher() {}
/*     */     
/*     */     public void failed(Throwable x) {
/* 418 */       CompressExtension.LOG.warn(x);
/* 419 */       super.failed(x);
/*     */     }
/*     */     
/*     */     protected IteratingCallback.Action process()
/*     */       throws Exception
/*     */     {
/* 425 */       if (this.finished)
/*     */       {
/* 427 */         this.current = CompressExtension.this.pollEntry();
/* 428 */         CompressExtension.LOG.debug("Processing {}", new Object[] { this.current });
/* 429 */         if (this.current == null)
/* 430 */           return IteratingCallback.Action.IDLE;
/* 431 */         deflate(this.current);
/*     */       }
/*     */       else
/*     */       {
/* 435 */         compress(this.current, false);
/*     */       }
/* 437 */       return IteratingCallback.Action.SCHEDULED;
/*     */     }
/*     */     
/*     */     private void deflate(CompressExtension.FrameEntry entry)
/*     */     {
/* 442 */       Frame frame = CompressExtension.FrameEntry.access$400(entry);
/* 443 */       BatchMode batchMode = CompressExtension.FrameEntry.access$500(entry);
/* 444 */       if (OpCode.isControlFrame(frame.getOpCode()))
/*     */       {
/*     */ 
/* 447 */         CompressExtension.this.nextOutgoingFrame(frame, this, batchMode);
/* 448 */         return;
/*     */       }
/*     */       
/* 451 */       compress(entry, true);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void compress(CompressExtension.FrameEntry entry, boolean first)
/*     */     {
/* 458 */       Frame frame = CompressExtension.FrameEntry.access$400(entry);
/* 459 */       ByteBuffer data = frame.getPayload();
/*     */       
/* 461 */       if (data == null) {
/* 462 */         data = BufferUtil.EMPTY_BUFFER;
/*     */       }
/* 464 */       int remaining = data.remaining();
/* 465 */       int outputLength = Math.max(256, data.remaining());
/* 466 */       if (CompressExtension.LOG.isDebugEnabled()) {
/* 467 */         CompressExtension.LOG.debug("Compressing {}: {} bytes in {} bytes chunk", new Object[] { entry, Integer.valueOf(remaining), Integer.valueOf(outputLength) });
/*     */       }
/* 469 */       boolean needsCompress = true;
/*     */       
/* 471 */       Deflater deflater = CompressExtension.this.getDeflater();
/*     */       
/* 473 */       if ((deflater.needsInput()) && (!CompressExtension.supplyInput(deflater, data)))
/*     */       {
/*     */ 
/* 476 */         needsCompress = false;
/*     */       }
/*     */       
/* 479 */       ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */       
/* 481 */       byte[] output = new byte[outputLength];
/*     */       
/* 483 */       boolean fin = frame.isFin();
/*     */       
/*     */ 
/* 486 */       while (needsCompress)
/*     */       {
/* 488 */         int compressed = deflater.deflate(output, 0, outputLength, 2);
/*     */         
/*     */ 
/* 491 */         if (CompressExtension.LOG.isDebugEnabled())
/* 492 */           CompressExtension.LOG.debug("Wrote {} bytes to output buffer", compressed);
/* 493 */         out.write(output, 0, compressed);
/*     */         
/* 495 */         if (compressed < outputLength)
/*     */         {
/* 497 */           needsCompress = false;
/*     */         }
/*     */       }
/*     */       
/* 501 */       ByteBuffer payload = ByteBuffer.wrap(out.toByteArray());
/*     */       
/* 503 */       if (payload.remaining() > 0)
/*     */       {
/*     */ 
/* 506 */         if (CompressExtension.LOG.isDebugEnabled()) {
/* 507 */           CompressExtension.LOG.debug("compressed bytes[] = {}", new Object[] { BufferUtil.toDetailString(payload) });
/*     */         }
/* 509 */         if (CompressExtension.this.tailDrop == 1)
/*     */         {
/* 511 */           if (CompressExtension.endsWithTail(payload))
/*     */           {
/* 513 */             payload.limit(payload.limit() - CompressExtension.TAIL_BYTES.length);
/*     */           }
/* 515 */           if (CompressExtension.LOG.isDebugEnabled()) {
/* 516 */             CompressExtension.LOG.debug("payload (TAIL_DROP_ALWAYS) = {}", new Object[] { BufferUtil.toDetailString(payload) });
/*     */           }
/* 518 */         } else if (CompressExtension.this.tailDrop == 2)
/*     */         {
/* 520 */           if ((frame.isFin()) && (CompressExtension.endsWithTail(payload)))
/*     */           {
/* 522 */             payload.limit(payload.limit() - CompressExtension.TAIL_BYTES.length);
/*     */           }
/* 524 */           if (CompressExtension.LOG.isDebugEnabled()) {
/* 525 */             CompressExtension.LOG.debug("payload (TAIL_DROP_FIN_ONLY) = {}", new Object[] { BufferUtil.toDetailString(payload) });
/*     */           }
/*     */         }
/* 528 */       } else if (fin)
/*     */       {
/*     */ 
/*     */ 
/* 532 */         payload = ByteBuffer.wrap(new byte[] { 0 });
/*     */       }
/*     */       
/* 535 */       if (CompressExtension.LOG.isDebugEnabled())
/*     */       {
/* 537 */         CompressExtension.LOG.debug("Compressed {}: input:{} -> payload:{}", new Object[] { entry, Integer.valueOf(outputLength), Integer.valueOf(payload.remaining()) });
/*     */       }
/*     */       
/* 540 */       boolean continuation = (frame.getType().isContinuation()) || (!first);
/* 541 */       DataFrame chunk = new DataFrame(frame, continuation);
/* 542 */       if (CompressExtension.this.rsvUse == 1)
/*     */       {
/* 544 */         chunk.setRsv1(!continuation);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 549 */         chunk.setRsv1(true);
/*     */       }
/* 551 */       chunk.setPayload(payload);
/* 552 */       chunk.setFin(fin);
/*     */       
/* 554 */       CompressExtension.this.nextOutgoingFrame(chunk, this, CompressExtension.FrameEntry.access$500(entry));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     protected void onCompleteSuccess() {}
/*     */     
/*     */ 
/*     */ 
/*     */     protected void onCompleteFailure(Throwable x)
/*     */     {
/*     */       CompressExtension.FrameEntry entry;
/*     */       
/*     */ 
/* 568 */       while ((entry = CompressExtension.this.pollEntry()) != null) {
/* 569 */         CompressExtension.this.notifyCallbackFailure(CompressExtension.FrameEntry.access$1100(entry), x);
/*     */       }
/*     */     }
/*     */     
/*     */     public void writeSuccess()
/*     */     {
/* 575 */       if (this.finished)
/* 576 */         CompressExtension.this.notifyCallbackSuccess(CompressExtension.FrameEntry.access$1100(this.current));
/* 577 */       succeeded();
/*     */     }
/*     */     
/*     */ 
/*     */     public void writeFailed(Throwable x)
/*     */     {
/* 583 */       CompressExtension.this.notifyCallbackFailure(CompressExtension.FrameEntry.access$1100(this.current), x);
/*     */       
/*     */ 
/* 586 */       failed(x);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\extensions\compress\CompressExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */