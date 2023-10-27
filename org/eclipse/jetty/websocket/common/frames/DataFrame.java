/*    */ package org.eclipse.jetty.websocket.common.frames;
/*    */ 
/*    */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*    */ import org.eclipse.jetty.websocket.common.WebSocketFrame;
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
/*    */ public class DataFrame
/*    */   extends WebSocketFrame
/*    */ {
/*    */   protected DataFrame(byte opcode)
/*    */   {
/* 32 */     super(opcode);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DataFrame(Frame basedOn)
/*    */   {
/* 43 */     this(basedOn, false);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DataFrame(Frame basedOn, boolean continuation)
/*    */   {
/* 55 */     super(basedOn.getOpCode());
/* 56 */     copyHeaders(basedOn);
/* 57 */     if (continuation)
/*    */     {
/* 59 */       setOpCode((byte)0);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void assertValid() {}
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isControlFrame()
/*    */   {
/* 72 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isDataFrame()
/*    */   {
/* 78 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setIsContinuation()
/*    */   {
/* 86 */     setOpCode((byte)0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\frames\DataFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */