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
/*    */ public class PingFrame
/*    */   extends ControlFrame
/*    */ {
/*    */   public PingFrame()
/*    */   {
/* 30 */     super((byte)9);
/*    */   }
/*    */   
/*    */   public PingFrame setPayload(byte[] bytes)
/*    */   {
/* 35 */     setPayload(ByteBuffer.wrap(bytes));
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public PingFrame setPayload(String payload)
/*    */   {
/* 41 */     setPayload(ByteBuffer.wrap(StringUtil.getUtf8Bytes(payload)));
/* 42 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */   public Frame.Type getType()
/*    */   {
/* 48 */     return Frame.Type.PING;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\frames\PingFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */