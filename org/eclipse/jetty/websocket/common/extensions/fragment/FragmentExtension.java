/*     */ package org.eclipse.jetty.websocket.common.extensions.fragment;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Queue;
/*     */ import org.eclipse.jetty.util.IteratingCallback;
/*     */ import org.eclipse.jetty.util.IteratingCallback.Action;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FragmentExtension
/*     */   extends AbstractExtension
/*     */ {
/*  42 */   private static final Logger LOG = Log.getLogger(FragmentExtension.class);
/*     */   
/*  44 */   private final Queue<FrameEntry> entries = new ArrayDeque();
/*  45 */   private final IteratingCallback flusher = new Flusher(null);
/*     */   
/*     */   private int maxLength;
/*     */   
/*     */   public String getName()
/*     */   {
/*  51 */     return "fragment";
/*     */   }
/*     */   
/*     */ 
/*     */   public void incomingFrame(Frame frame)
/*     */   {
/*  57 */     nextIncomingFrame(frame);
/*     */   }
/*     */   
/*     */ 
/*     */   public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
/*     */   {
/*  63 */     ByteBuffer payload = frame.getPayload();
/*  64 */     int length = payload != null ? payload.remaining() : 0;
/*  65 */     if ((OpCode.isControlFrame(frame.getOpCode())) || (this.maxLength <= 0) || (length <= this.maxLength))
/*     */     {
/*  67 */       nextOutgoingFrame(frame, callback, batchMode);
/*  68 */       return;
/*     */     }
/*     */     
/*  71 */     FrameEntry entry = new FrameEntry(frame, callback, batchMode, null);
/*  72 */     if (LOG.isDebugEnabled())
/*  73 */       LOG.debug("Queuing {}", new Object[] { entry });
/*  74 */     offerEntry(entry);
/*  75 */     this.flusher.iterate();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setConfig(ExtensionConfig config)
/*     */   {
/*  81 */     super.setConfig(config);
/*  82 */     this.maxLength = config.getParameter("maxLength", -1);
/*     */   }
/*     */   
/*     */   private void offerEntry(FrameEntry entry)
/*     */   {
/*  87 */     synchronized (this)
/*     */     {
/*  89 */       this.entries.offer(entry);
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
/*     */     //   5: getfield 8	org/eclipse/jetty/websocket/common/extensions/fragment/FragmentExtension:entries	Ljava/util/Queue;
/*     */     //   8: invokeinterface 30 1 0
/*     */     //   13: checkcast 18	org/eclipse/jetty/websocket/common/extensions/fragment/FragmentExtension$FrameEntry
/*     */     //   16: aload_1
/*     */     //   17: monitorexit
/*     */     //   18: areturn
/*     */     //   19: astore_2
/*     */     //   20: aload_1
/*     */     //   21: monitorexit
/*     */     //   22: aload_2
/*     */     //   23: athrow
/*     */     // Line number table:
/*     */     //   Java source line #95	-> byte code offset #0
/*     */     //   Java source line #97	-> byte code offset #4
/*     */     //   Java source line #98	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	24	0	this	FragmentExtension
/*     */     //   2	19	1	Ljava/lang/Object;	Object
/*     */     //   19	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	18	19	finally
/*     */     //   19	22	19	finally
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
/* 109 */       this.frame = frame;
/* 110 */       this.callback = callback;
/* 111 */       this.batchMode = batchMode;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 117 */       return this.frame.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private class Flusher extends IteratingCallback implements WriteCallback
/*     */   {
/*     */     private FragmentExtension.FrameEntry current;
/* 124 */     private boolean finished = true;
/*     */     
/*     */     private Flusher() {}
/*     */     
/*     */     protected IteratingCallback.Action process() throws Exception {
/* 129 */       if (this.finished)
/*     */       {
/* 131 */         this.current = FragmentExtension.this.pollEntry();
/* 132 */         FragmentExtension.LOG.debug("Processing {}", new Object[] { this.current });
/* 133 */         if (this.current == null)
/* 134 */           return IteratingCallback.Action.IDLE;
/* 135 */         fragment(this.current, true);
/*     */       }
/*     */       else
/*     */       {
/* 139 */         fragment(this.current, false);
/*     */       }
/* 141 */       return IteratingCallback.Action.SCHEDULED;
/*     */     }
/*     */     
/*     */     private void fragment(FragmentExtension.FrameEntry entry, boolean first)
/*     */     {
/* 146 */       Frame frame = FragmentExtension.FrameEntry.access$400(entry);
/* 147 */       ByteBuffer payload = frame.getPayload();
/* 148 */       int remaining = payload.remaining();
/* 149 */       int length = Math.min(remaining, FragmentExtension.this.maxLength);
/* 150 */       this.finished = (length == remaining);
/*     */       
/* 152 */       boolean continuation = (frame.getType().isContinuation()) || (!first);
/* 153 */       DataFrame fragment = new DataFrame(frame, continuation);
/* 154 */       boolean fin = (frame.isFin()) && (this.finished);
/* 155 */       fragment.setFin(fin);
/*     */       
/* 157 */       int limit = payload.limit();
/* 158 */       int newLimit = payload.position() + length;
/* 159 */       payload.limit(newLimit);
/* 160 */       ByteBuffer payloadFragment = payload.slice();
/* 161 */       payload.limit(limit);
/* 162 */       fragment.setPayload(payloadFragment);
/* 163 */       if (FragmentExtension.LOG.isDebugEnabled())
/* 164 */         FragmentExtension.LOG.debug("Fragmented {}->{}", new Object[] { frame, fragment });
/* 165 */       payload.position(newLimit);
/*     */       
/* 167 */       FragmentExtension.this.nextOutgoingFrame(fragment, this, FragmentExtension.FrameEntry.access$600(entry));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void onCompleteSuccess() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected void onCompleteFailure(Throwable x) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void writeSuccess()
/*     */     {
/* 189 */       notifyCallbackSuccess(FragmentExtension.FrameEntry.access$800(this.current));
/* 190 */       succeeded();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void writeFailed(Throwable x)
/*     */     {
/* 201 */       notifyCallbackFailure(FragmentExtension.FrameEntry.access$800(this.current), x);
/* 202 */       succeeded();
/*     */     }
/*     */     
/*     */     private void notifyCallbackSuccess(WriteCallback callback)
/*     */     {
/*     */       try
/*     */       {
/* 209 */         if (callback != null) {
/* 210 */           callback.writeSuccess();
/*     */         }
/*     */       }
/*     */       catch (Throwable x) {
/* 214 */         if (FragmentExtension.LOG.isDebugEnabled()) {
/* 215 */           FragmentExtension.LOG.debug("Exception while notifying success of callback " + callback, x);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private void notifyCallbackFailure(WriteCallback callback, Throwable failure)
/*     */     {
/*     */       try {
/* 223 */         if (callback != null) {
/* 224 */           callback.writeFailed(failure);
/*     */         }
/*     */       }
/*     */       catch (Throwable x) {
/* 228 */         if (FragmentExtension.LOG.isDebugEnabled()) {
/* 229 */           FragmentExtension.LOG.debug("Exception while notifying failure of callback " + callback, x);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\extensions\fragment\FragmentExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */