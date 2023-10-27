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
/*    */ public class BinaryFrame
/*    */   extends DataFrame
/*    */ {
/*    */   public BinaryFrame()
/*    */   {
/* 30 */     super((byte)2);
/*    */   }
/*    */   
/*    */ 
/*    */   public BinaryFrame setPayload(ByteBuffer buf)
/*    */   {
/* 36 */     super.setPayload(buf);
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public BinaryFrame setPayload(byte[] buf)
/*    */   {
/* 42 */     setPayload(ByteBuffer.wrap(buf));
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public BinaryFrame setPayload(String payload)
/*    */   {
/* 48 */     setPayload(StringUtil.getUtf8Bytes(payload));
/* 49 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */   public Frame.Type getType()
/*    */   {
/* 55 */     if (getOpCode() == 0)
/* 56 */       return Frame.Type.CONTINUATION;
/* 57 */     return Frame.Type.BINARY;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\frames\BinaryFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */