/*    */ package org.eclipse.jetty.websocket.common.frames;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.eclipse.jetty.util.StringUtil;
/*    */ import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
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
/*    */ public class ContinuationFrame
/*    */   extends DataFrame
/*    */ {
/*    */   public ContinuationFrame()
/*    */   {
/* 30 */     super((byte)0);
/*    */   }
/*    */   
/*    */ 
/*    */   public ContinuationFrame setPayload(ByteBuffer buf)
/*    */   {
/* 36 */     super.setPayload(buf);
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public ContinuationFrame setPayload(byte[] buf)
/*    */   {
/* 42 */     return setPayload(ByteBuffer.wrap(buf));
/*    */   }
/*    */   
/*    */   public ContinuationFrame setPayload(String message)
/*    */   {
/* 47 */     return setPayload(StringUtil.getUtf8Bytes(message));
/*    */   }
/*    */   
/*    */ 
/*    */   public Frame.Type getType()
/*    */   {
/* 53 */     return Frame.Type.CONTINUATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\frames\ContinuationFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */