/*    */ package org.eclipse.jetty.websocket.common.io;
/*    */ 
/*    */ import org.eclipse.jetty.util.FutureCallback;
/*    */ import org.eclipse.jetty.util.log.Log;
/*    */ import org.eclipse.jetty.util.log.Logger;
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
/*    */ public class FutureWriteCallback
/*    */   extends FutureCallback
/*    */   implements WriteCallback
/*    */ {
/* 33 */   private static final Logger LOG = Log.getLogger(FutureWriteCallback.class);
/*    */   
/*    */ 
/*    */   public void writeFailed(Throwable cause)
/*    */   {
/* 38 */     if (LOG.isDebugEnabled())
/* 39 */       LOG.debug(".writeFailed", cause);
/* 40 */     failed(cause);
/*    */   }
/*    */   
/*    */ 
/*    */   public void writeSuccess()
/*    */   {
/* 46 */     if (LOG.isDebugEnabled())
/* 47 */       LOG.debug(".writeSuccess", new Object[0]);
/* 48 */     succeeded();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\io\FutureWriteCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */