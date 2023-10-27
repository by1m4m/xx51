/*     */ package org.eclipse.jetty.websocket.common.extensions;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Queue;
/*     */ import org.eclipse.jetty.util.IteratingCallback;
/*     */ import org.eclipse.jetty.util.IteratingCallback.Action;
/*     */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*     */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*     */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*     */ import org.eclipse.jetty.util.component.LifeCycle;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Extension;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
/*     */ import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
/*     */ import org.eclipse.jetty.websocket.common.Generator;
/*     */ import org.eclipse.jetty.websocket.common.Parser;
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
/*     */ @ManagedObject("Extension Stack")
/*     */ public class ExtensionStack
/*     */   extends ContainerLifeCycle
/*     */   implements IncomingFrames, OutgoingFrames
/*     */ {
/*  53 */   private static final Logger LOG = Log.getLogger(ExtensionStack.class);
/*     */   
/*  55 */   private final Queue<FrameEntry> entries = new ArrayDeque();
/*  56 */   private final IteratingCallback flusher = new Flusher(null);
/*     */   private final ExtensionFactory factory;
/*     */   private List<Extension> extensions;
/*     */   private IncomingFrames nextIncoming;
/*     */   private OutgoingFrames nextOutgoing;
/*     */   
/*     */   public ExtensionStack(ExtensionFactory factory)
/*     */   {
/*  64 */     this.factory = factory;
/*     */   }
/*     */   
/*     */   public void configure(Generator generator)
/*     */   {
/*  69 */     generator.configureFromExtensions(this.extensions);
/*     */   }
/*     */   
/*     */   public void configure(Parser parser)
/*     */   {
/*  74 */     parser.configureFromExtensions(this.extensions);
/*     */   }
/*     */   
/*     */   protected void doStart()
/*     */     throws Exception
/*     */   {
/*  80 */     super.doStart();
/*     */     
/*     */ 
/*  83 */     if ((this.extensions != null) && (this.extensions.size() > 0))
/*     */     {
/*  85 */       ListIterator<Extension> exts = this.extensions.listIterator();
/*     */       
/*     */ 
/*  88 */       while (exts.hasNext())
/*     */       {
/*  90 */         Extension ext = (Extension)exts.next();
/*  91 */         ext.setNextOutgoingFrames(this.nextOutgoing);
/*  92 */         this.nextOutgoing = ext;
/*     */         
/*  94 */         if ((ext instanceof LifeCycle))
/*     */         {
/*  96 */           addBean(ext, true);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 101 */       while (exts.hasPrevious())
/*     */       {
/* 103 */         Extension ext = (Extension)exts.previous();
/* 104 */         ext.setNextIncomingFrames(this.nextIncoming);
/* 105 */         this.nextIncoming = ext;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void dump(Appendable out, String indent)
/*     */     throws IOException
/*     */   {
/* 113 */     super.dump(out, indent);
/*     */     
/* 115 */     IncomingFrames websocket = getLastIncoming();
/* 116 */     OutgoingFrames network = getLastOutgoing();
/*     */     
/* 118 */     out.append(indent).append(" +- Stack").append(System.lineSeparator());
/* 119 */     out.append(indent).append("     +- Network  : ").append(network.toString()).append(System.lineSeparator());
/* 120 */     for (Extension ext : this.extensions)
/*     */     {
/* 122 */       out.append(indent).append("     +- Extension: ").append(ext.toString()).append(System.lineSeparator());
/*     */     }
/* 124 */     out.append(indent).append("     +- Websocket: ").append(websocket.toString()).append(System.lineSeparator());
/*     */   }
/*     */   
/*     */   @ManagedAttribute(name="Extension List", readonly=true)
/*     */   public List<Extension> getExtensions()
/*     */   {
/* 130 */     return this.extensions;
/*     */   }
/*     */   
/*     */   private IncomingFrames getLastIncoming()
/*     */   {
/* 135 */     IncomingFrames last = this.nextIncoming;
/* 136 */     boolean done = false;
/* 137 */     while (!done)
/*     */     {
/* 139 */       if ((last instanceof AbstractExtension))
/*     */       {
/* 141 */         last = ((AbstractExtension)last).getNextIncoming();
/*     */       }
/*     */       else
/*     */       {
/* 145 */         done = true;
/*     */       }
/*     */     }
/* 148 */     return last;
/*     */   }
/*     */   
/*     */   private OutgoingFrames getLastOutgoing()
/*     */   {
/* 153 */     OutgoingFrames last = this.nextOutgoing;
/* 154 */     boolean done = false;
/* 155 */     while (!done)
/*     */     {
/* 157 */       if ((last instanceof AbstractExtension))
/*     */       {
/* 159 */         last = ((AbstractExtension)last).getNextOutgoing();
/*     */       }
/*     */       else
/*     */       {
/* 163 */         done = true;
/*     */       }
/*     */     }
/* 166 */     return last;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ExtensionConfig> getNegotiatedExtensions()
/*     */   {
/* 176 */     List<ExtensionConfig> ret = new ArrayList();
/* 177 */     if (this.extensions == null)
/*     */     {
/* 179 */       return ret;
/*     */     }
/*     */     
/* 182 */     for (Extension ext : this.extensions)
/*     */     {
/* 184 */       if (ext.getName().charAt(0) != '@')
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 189 */         ret.add(ext.getConfig()); }
/*     */     }
/* 191 */     return ret;
/*     */   }
/*     */   
/*     */   @ManagedAttribute(name="Next Incoming Frames Handler", readonly=true)
/*     */   public IncomingFrames getNextIncoming()
/*     */   {
/* 197 */     return this.nextIncoming;
/*     */   }
/*     */   
/*     */   @ManagedAttribute(name="Next Outgoing Frames Handler", readonly=true)
/*     */   public OutgoingFrames getNextOutgoing()
/*     */   {
/* 203 */     return this.nextOutgoing;
/*     */   }
/*     */   
/*     */   public boolean hasNegotiatedExtensions()
/*     */   {
/* 208 */     return (this.extensions != null) && (this.extensions.size() > 0);
/*     */   }
/*     */   
/*     */ 
/*     */   public void incomingError(Throwable e)
/*     */   {
/* 214 */     this.nextIncoming.incomingError(e);
/*     */   }
/*     */   
/*     */ 
/*     */   public void incomingFrame(Frame frame)
/*     */   {
/* 220 */     this.nextIncoming.incomingFrame(frame);
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
/*     */   public void negotiate(List<ExtensionConfig> configs)
/*     */   {
/* 233 */     if (LOG.isDebugEnabled()) {
/* 234 */       LOG.debug("Extension Configs={}", new Object[] { configs });
/*     */     }
/* 236 */     this.extensions = new ArrayList();
/*     */     
/* 238 */     String[] rsvClaims = new String[3];
/*     */     
/* 240 */     for (ExtensionConfig config : configs)
/*     */     {
/* 242 */       Extension ext = this.factory.newInstance(config);
/* 243 */       if (ext != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 250 */         if ((ext.isRsv1User()) && (rsvClaims[0] != null))
/*     */         {
/* 252 */           LOG.debug("Not adding extension {}. Extension {} already claimed RSV1", new Object[] { config, rsvClaims[0] });
/*     */ 
/*     */         }
/* 255 */         else if ((ext.isRsv2User()) && (rsvClaims[1] != null))
/*     */         {
/* 257 */           LOG.debug("Not adding extension {}. Extension {} already claimed RSV2", new Object[] { config, rsvClaims[1] });
/*     */ 
/*     */         }
/* 260 */         else if ((ext.isRsv3User()) && (rsvClaims[2] != null))
/*     */         {
/* 262 */           LOG.debug("Not adding extension {}. Extension {} already claimed RSV3", new Object[] { config, rsvClaims[2] });
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 267 */           this.extensions.add(ext);
/* 268 */           addBean(ext);
/*     */           
/* 270 */           if (LOG.isDebugEnabled()) {
/* 271 */             LOG.debug("Adding Extension: {}", new Object[] { config });
/*     */           }
/*     */           
/* 274 */           if (ext.isRsv1User())
/*     */           {
/* 276 */             rsvClaims[0] = ext.getName();
/*     */           }
/* 278 */           if (ext.isRsv2User())
/*     */           {
/* 280 */             rsvClaims[1] = ext.getName();
/*     */           }
/* 282 */           if (ext.isRsv3User())
/*     */           {
/* 284 */             rsvClaims[2] = ext.getName();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode) {
/* 292 */     FrameEntry entry = new FrameEntry(frame, callback, batchMode, null);
/* 293 */     if (LOG.isDebugEnabled())
/* 294 */       LOG.debug("Queuing {}", new Object[] { entry });
/* 295 */     offerEntry(entry);
/* 296 */     this.flusher.iterate();
/*     */   }
/*     */   
/*     */   public void setNextIncoming(IncomingFrames nextIncoming)
/*     */   {
/* 301 */     this.nextIncoming = nextIncoming;
/*     */   }
/*     */   
/*     */   public void setNextOutgoing(OutgoingFrames nextOutgoing)
/*     */   {
/* 306 */     this.nextOutgoing = nextOutgoing;
/*     */   }
/*     */   
/*     */   public void setPolicy(WebSocketPolicy policy)
/*     */   {
/* 311 */     for (Extension extension : this.extensions)
/*     */     {
/* 313 */       if ((extension instanceof AbstractExtension))
/*     */       {
/* 315 */         ((AbstractExtension)extension).setPolicy(policy);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void offerEntry(FrameEntry entry)
/*     */   {
/* 322 */     synchronized (this)
/*     */     {
/* 324 */       this.entries.offer(entry);
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
/*     */     //   5: getfield 7	org/eclipse/jetty/websocket/common/extensions/ExtensionStack:entries	Ljava/util/Queue;
/*     */     //   8: invokeinterface 74 1 0
/*     */     //   13: checkcast 67	org/eclipse/jetty/websocket/common/extensions/ExtensionStack$FrameEntry
/*     */     //   16: aload_1
/*     */     //   17: monitorexit
/*     */     //   18: areturn
/*     */     //   19: astore_2
/*     */     //   20: aload_1
/*     */     //   21: monitorexit
/*     */     //   22: aload_2
/*     */     //   23: athrow
/*     */     // Line number table:
/*     */     //   Java source line #330	-> byte code offset #0
/*     */     //   Java source line #332	-> byte code offset #4
/*     */     //   Java source line #333	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	24	0	this	ExtensionStack
/*     */     //   2	19	1	Ljava/lang/Object;	Object
/*     */     //   19	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	18	19	finally
/*     */     //   19	22	19	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private int getQueueSize()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: dup
/*     */     //   2: astore_1
/*     */     //   3: monitorenter
/*     */     //   4: aload_0
/*     */     //   5: getfield 7	org/eclipse/jetty/websocket/common/extensions/ExtensionStack:entries	Ljava/util/Queue;
/*     */     //   8: invokeinterface 75 1 0
/*     */     //   13: aload_1
/*     */     //   14: monitorexit
/*     */     //   15: ireturn
/*     */     //   16: astore_2
/*     */     //   17: aload_1
/*     */     //   18: monitorexit
/*     */     //   19: aload_2
/*     */     //   20: athrow
/*     */     // Line number table:
/*     */     //   Java source line #338	-> byte code offset #0
/*     */     //   Java source line #340	-> byte code offset #4
/*     */     //   Java source line #341	-> byte code offset #16
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	21	0	this	ExtensionStack
/*     */     //   2	16	1	Ljava/lang/Object;	Object
/*     */     //   16	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	15	16	finally
/*     */     //   16	19	16	finally
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 347 */     StringBuilder s = new StringBuilder();
/* 348 */     s.append("ExtensionStack[");
/* 349 */     s.append("queueSize=").append(getQueueSize());
/* 350 */     s.append(",extensions=");
/* 351 */     if (this.extensions == null)
/*     */     {
/* 353 */       s.append("<null>");
/*     */     }
/*     */     else
/*     */     {
/* 357 */       s.append('[');
/* 358 */       boolean delim = false;
/* 359 */       for (Extension ext : this.extensions)
/*     */       {
/* 361 */         if (delim)
/*     */         {
/* 363 */           s.append(',');
/*     */         }
/* 365 */         if (ext == null)
/*     */         {
/* 367 */           s.append("<null>");
/*     */         }
/*     */         else
/*     */         {
/* 371 */           s.append(ext.getName());
/*     */         }
/* 373 */         delim = true;
/*     */       }
/* 375 */       s.append(']');
/*     */     }
/* 377 */     s.append(",incoming=").append(this.nextIncoming == null ? "<null>" : this.nextIncoming.getClass().getName());
/* 378 */     s.append(",outgoing=").append(this.nextOutgoing == null ? "<null>" : this.nextOutgoing.getClass().getName());
/* 379 */     s.append("]");
/* 380 */     return s.toString();
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
/* 391 */       this.frame = frame;
/* 392 */       this.callback = callback;
/* 393 */       this.batchMode = batchMode;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 399 */       return this.frame.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private class Flusher extends IteratingCallback implements WriteCallback
/*     */   {
/*     */     private ExtensionStack.FrameEntry current;
/*     */     
/*     */     private Flusher() {}
/*     */     
/*     */     protected IteratingCallback.Action process() throws Exception {
/* 410 */       this.current = ExtensionStack.this.pollEntry();
/* 411 */       if (this.current == null)
/*     */       {
/* 413 */         if (ExtensionStack.LOG.isDebugEnabled())
/* 414 */           ExtensionStack.LOG.debug("Entering IDLE", new Object[0]);
/* 415 */         return IteratingCallback.Action.IDLE;
/*     */       }
/* 417 */       if (ExtensionStack.LOG.isDebugEnabled())
/* 418 */         ExtensionStack.LOG.debug("Processing {}", new Object[] { this.current });
/* 419 */       ExtensionStack.this.nextOutgoing.outgoingFrame(ExtensionStack.FrameEntry.access$400(this.current), this, ExtensionStack.FrameEntry.access$500(this.current));
/* 420 */       return IteratingCallback.Action.SCHEDULED;
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
/* 442 */       notifyCallbackSuccess(ExtensionStack.FrameEntry.access$700(this.current));
/* 443 */       succeeded();
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
/* 454 */       notifyCallbackFailure(ExtensionStack.FrameEntry.access$700(this.current), x);
/* 455 */       succeeded();
/*     */     }
/*     */     
/*     */     private void notifyCallbackSuccess(WriteCallback callback)
/*     */     {
/*     */       try
/*     */       {
/* 462 */         if (callback != null) {
/* 463 */           callback.writeSuccess();
/*     */         }
/*     */       }
/*     */       catch (Throwable x) {
/* 467 */         ExtensionStack.LOG.debug("Exception while notifying success of callback " + callback, x);
/*     */       }
/*     */     }
/*     */     
/*     */     private void notifyCallbackFailure(WriteCallback callback, Throwable failure)
/*     */     {
/*     */       try
/*     */       {
/* 475 */         if (callback != null) {
/* 476 */           callback.writeFailed(failure);
/*     */         }
/*     */       }
/*     */       catch (Throwable x) {
/* 480 */         ExtensionStack.LOG.debug("Exception while notifying failure of callback " + callback, x);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\extensions\ExtensionStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */