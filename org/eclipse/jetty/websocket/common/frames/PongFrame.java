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
/*    */ public class PongFrame
/*    */   extends ControlFrame
/*    */ {
/*    */   public PongFrame()
/*    */   {
/* 30 */     super((byte)10);
/*    */   }
/*    */   
/*    */   public PongFrame setPayload(byte[] bytes)
/*    */   {
/* 35 */     setPayload(ByteBuffer.wrap(bytes));
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public PongFrame setPayload(String payload)
/*    */   {
/* 41 */     setPayload(StringUtil.getUtf8Bytes(payload));
/* 42 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */   public Frame.Type getType()
/*    */   {
/* 48 */     return Frame.Type.PONG;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\frames\PongFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */