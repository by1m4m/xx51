/*    */ package org.eclipse.jetty.websocket.client.io;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ import java.util.concurrent.Executor;
/*    */ import org.eclipse.jetty.io.ByteBufferPool;
/*    */ import org.eclipse.jetty.io.EndPoint;
/*    */ import org.eclipse.jetty.util.thread.Scheduler;
/*    */ import org.eclipse.jetty.websocket.api.BatchMode;
/*    */ import org.eclipse.jetty.websocket.api.WebSocketPolicy;
/*    */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*    */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*    */ import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
/*    */ import org.eclipse.jetty.websocket.client.masks.Masker;
/*    */ import org.eclipse.jetty.websocket.client.masks.RandomMasker;
/*    */ import org.eclipse.jetty.websocket.common.Parser;
/*    */ import org.eclipse.jetty.websocket.common.WebSocketFrame;
/*    */ import org.eclipse.jetty.websocket.common.io.AbstractWebSocketConnection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WebSocketClientConnection
/*    */   extends AbstractWebSocketConnection
/*    */ {
/*    */   private final Masker masker;
/*    */   
/*    */   public WebSocketClientConnection(EndPoint endp, Executor executor, Scheduler scheduler, WebSocketPolicy websocketPolicy, ByteBufferPool bufferPool)
/*    */   {
/* 46 */     super(endp, executor, scheduler, websocketPolicy, bufferPool);
/* 47 */     this.masker = new RandomMasker();
/*    */   }
/*    */   
/*    */ 
/*    */   public InetSocketAddress getLocalAddress()
/*    */   {
/* 53 */     return getEndPoint().getLocalAddress();
/*    */   }
/*    */   
/*    */ 
/*    */   public InetSocketAddress getRemoteAddress()
/*    */   {
/* 59 */     return getEndPoint().getRemoteAddress();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
/*    */   {
/* 68 */     if ((frame instanceof WebSocketFrame))
/*    */     {
/* 70 */       this.masker.setMask((WebSocketFrame)frame);
/*    */     }
/* 72 */     super.outgoingFrame(frame, callback, batchMode);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setNextIncomingFrames(IncomingFrames incoming)
/*    */   {
/* 78 */     getParser().setIncomingFramesHandler(incoming);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\io\WebSocketClientConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */