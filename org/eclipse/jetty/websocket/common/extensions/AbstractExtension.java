/*     */ package org.eclipse.jetty.websocket.common.extensions;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.eclipse.jetty.io.ByteBufferPool;
/*     */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*     */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*     */ import org.eclipse.jetty.util.component.AbstractLifeCycle;
/*     */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*     */ import org.eclipse.jetty.util.component.Dumpable;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.BatchMode;
/*     */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*     */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Extension;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
/*     */ import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
/*     */ import org.eclipse.jetty.websocket.common.LogicalConnection;
/*     */ import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
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
/*     */ @ManagedObject("Abstract Extension")
/*     */ public abstract class AbstractExtension
/*     */   extends AbstractLifeCycle
/*     */   implements Dumpable, Extension
/*     */ {
/*     */   private final Logger log;
/*     */   private WebSocketPolicy policy;
/*     */   private ByteBufferPool bufferPool;
/*     */   private ExtensionConfig config;
/*     */   private LogicalConnection connection;
/*     */   private OutgoingFrames nextOutgoing;
/*     */   private IncomingFrames nextIncoming;
/*     */   
/*     */   public AbstractExtension()
/*     */   {
/*  55 */     this.log = Log.getLogger(getClass());
/*     */   }
/*     */   
/*     */ 
/*     */   public String dump()
/*     */   {
/*  61 */     return ContainerLifeCycle.dump(this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void dump(Appendable out, String indent)
/*     */     throws IOException
/*     */   {
/*  68 */     dumpWithHeading(out, indent, "incoming", this.nextIncoming);
/*  69 */     dumpWithHeading(out, indent, "outgoing", this.nextOutgoing);
/*     */   }
/*     */   
/*     */   protected void dumpWithHeading(Appendable out, String indent, String heading, Object bean) throws IOException
/*     */   {
/*  74 */     out.append(indent).append(" +- ");
/*  75 */     out.append(heading).append(" : ");
/*  76 */     out.append(bean.toString());
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void init(WebSocketContainerScope container)
/*     */   {
/*  82 */     init(container.getPolicy(), container.getBufferPool());
/*     */   }
/*     */   
/*     */   public void init(WebSocketPolicy policy, ByteBufferPool bufferPool)
/*     */   {
/*  87 */     this.policy = policy;
/*  88 */     this.bufferPool = bufferPool;
/*     */   }
/*     */   
/*     */   public ByteBufferPool getBufferPool()
/*     */   {
/*  93 */     return this.bufferPool;
/*     */   }
/*     */   
/*     */ 
/*     */   public ExtensionConfig getConfig()
/*     */   {
/*  99 */     return this.config;
/*     */   }
/*     */   
/*     */   public LogicalConnection getConnection()
/*     */   {
/* 104 */     return this.connection;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getName()
/*     */   {
/* 110 */     return this.config.getName();
/*     */   }
/*     */   
/*     */   @ManagedAttribute(name="Next Incoming Frame Handler", readonly=true)
/*     */   public IncomingFrames getNextIncoming()
/*     */   {
/* 116 */     return this.nextIncoming;
/*     */   }
/*     */   
/*     */   @ManagedAttribute(name="Next Outgoing Frame Handler", readonly=true)
/*     */   public OutgoingFrames getNextOutgoing()
/*     */   {
/* 122 */     return this.nextOutgoing;
/*     */   }
/*     */   
/*     */   public WebSocketPolicy getPolicy()
/*     */   {
/* 127 */     return this.policy;
/*     */   }
/*     */   
/*     */ 
/*     */   public void incomingError(Throwable e)
/*     */   {
/* 133 */     nextIncomingError(e);
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
/*     */   public boolean isRsv1User()
/*     */   {
/* 146 */     return false;
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
/*     */   public boolean isRsv2User()
/*     */   {
/* 159 */     return false;
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
/*     */   public boolean isRsv3User()
/*     */   {
/* 172 */     return false;
/*     */   }
/*     */   
/*     */   protected void nextIncomingError(Throwable e)
/*     */   {
/* 177 */     this.nextIncoming.incomingError(e);
/*     */   }
/*     */   
/*     */   protected void nextIncomingFrame(Frame frame)
/*     */   {
/* 182 */     this.log.debug("nextIncomingFrame({})", new Object[] { frame });
/* 183 */     this.nextIncoming.incomingFrame(frame);
/*     */   }
/*     */   
/*     */   protected void nextOutgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
/*     */   {
/* 188 */     this.log.debug("nextOutgoingFrame({})", new Object[] { frame });
/* 189 */     this.nextOutgoing.outgoingFrame(frame, callback, batchMode);
/*     */   }
/*     */   
/*     */   public void setBufferPool(ByteBufferPool bufferPool)
/*     */   {
/* 194 */     this.bufferPool = bufferPool;
/*     */   }
/*     */   
/*     */   public void setConfig(ExtensionConfig config)
/*     */   {
/* 199 */     this.config = config;
/*     */   }
/*     */   
/*     */   public void setConnection(LogicalConnection connection)
/*     */   {
/* 204 */     this.connection = connection;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setNextIncomingFrames(IncomingFrames nextIncoming)
/*     */   {
/* 210 */     this.nextIncoming = nextIncoming;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setNextOutgoingFrames(OutgoingFrames nextOutgoing)
/*     */   {
/* 216 */     this.nextOutgoing = nextOutgoing;
/*     */   }
/*     */   
/*     */   public void setPolicy(WebSocketPolicy policy)
/*     */   {
/* 221 */     this.policy = policy;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 227 */     return String.format("%s[%s]", new Object[] { getClass().getSimpleName(), this.config.getParameterizedName() });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\extensions\AbstractExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */