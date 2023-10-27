/*    */ package org.eclipse.jetty.websocket.common;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.eclipse.jetty.util.Callback;
/*    */ import org.eclipse.jetty.util.SharedBlockingCallback;
/*    */ import org.eclipse.jetty.util.SharedBlockingCallback.Blocker;
/*    */ import org.eclipse.jetty.util.thread.Invocable.InvocationType;
/*    */ import org.eclipse.jetty.websocket.api.WriteCallback;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockingWriteCallback
/*    */   extends SharedBlockingCallback
/*    */ {
/*    */   public WriteBlocker acquireWriteBlocker()
/*    */     throws IOException
/*    */   {
/* 39 */     return new WriteBlocker(acquire());
/*    */   }
/*    */   
/*    */   public static class WriteBlocker implements WriteCallback, Callback, AutoCloseable
/*    */   {
/*    */     private final SharedBlockingCallback.Blocker blocker;
/*    */     
/*    */     protected WriteBlocker(SharedBlockingCallback.Blocker blocker)
/*    */     {
/* 48 */       this.blocker = blocker;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */     public Invocable.InvocationType getInvocationType()
/*    */     {
/* 55 */       return Invocable.InvocationType.NON_BLOCKING;
/*    */     }
/*    */     
/*    */ 
/*    */     public void writeFailed(Throwable x)
/*    */     {
/* 61 */       this.blocker.failed(x);
/*    */     }
/*    */     
/*    */ 
/*    */     public void writeSuccess()
/*    */     {
/* 67 */       this.blocker.succeeded();
/*    */     }
/*    */     
/*    */ 
/*    */     public void succeeded()
/*    */     {
/* 73 */       this.blocker.succeeded();
/*    */     }
/*    */     
/*    */ 
/*    */     public void failed(Throwable x)
/*    */     {
/* 79 */       this.blocker.failed(x);
/*    */     }
/*    */     
/*    */ 
/*    */     public void close()
/*    */     {
/* 85 */       this.blocker.close();
/*    */     }
/*    */     
/*    */     public void block() throws IOException
/*    */     {
/* 90 */       this.blocker.block();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\BlockingWriteCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */