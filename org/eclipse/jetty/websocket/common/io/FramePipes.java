/*    */ package org.eclipse.jetty.websocket.common.io;
/*    */ 
/*    */ import org.eclipse.jetty.websocket.api.BatchMode;
/*    */ import org.eclipse.jetty.websocket.api.WriteCallback;
/*    */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*    */ import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
/*    */ import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
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
/*    */ public class FramePipes
/*    */ {
/*    */   private static class In2Out
/*    */     implements IncomingFrames
/*    */   {
/*    */     private OutgoingFrames outgoing;
/*    */     
/*    */     public In2Out(OutgoingFrames outgoing)
/*    */     {
/* 35 */       this.outgoing = outgoing;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public void incomingError(Throwable t) {}
/*    */     
/*    */ 
/*    */ 
/*    */     public void incomingFrame(Frame frame)
/*    */     {
/* 47 */       this.outgoing.outgoingFrame(frame, null, BatchMode.OFF);
/*    */     }
/*    */   }
/*    */   
/*    */   private static class Out2In implements OutgoingFrames
/*    */   {
/*    */     private IncomingFrames incoming;
/*    */     
/*    */     public Out2In(IncomingFrames incoming)
/*    */     {
/* 57 */       this.incoming = incoming;
/*    */     }
/*    */     
/*    */ 
/*    */     public void outgoingFrame(Frame frame, WriteCallback callback, BatchMode batchMode)
/*    */     {
/*    */       try
/*    */       {
/* 65 */         this.incoming.incomingFrame(frame);
/* 66 */         callback.writeSuccess();
/*    */       }
/*    */       catch (Throwable t)
/*    */       {
/* 70 */         callback.writeFailed(t);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static OutgoingFrames to(IncomingFrames incoming)
/*    */   {
/* 77 */     return new Out2In(incoming);
/*    */   }
/*    */   
/*    */   public static IncomingFrames to(OutgoingFrames outgoing)
/*    */   {
/* 82 */     return new In2Out(outgoing);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\io\FramePipes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */