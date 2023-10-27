/*    */ package org.eclipse.jetty.websocket.common.frames;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.eclipse.jetty.util.BufferUtil;
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
/*    */ public class TextFrame
/*    */   extends DataFrame
/*    */ {
/*    */   public TextFrame()
/*    */   {
/* 31 */     super((byte)1);
/*    */   }
/*    */   
/*    */ 
/*    */   public Frame.Type getType()
/*    */   {
/* 37 */     if (getOpCode() == 0)
/* 38 */       return Frame.Type.CONTINUATION;
/* 39 */     return Frame.Type.TEXT;
/*    */   }
/*    */   
/*    */   public TextFrame setPayload(String str)
/*    */   {
/* 44 */     setPayload(ByteBuffer.wrap(StringUtil.getUtf8Bytes(str)));
/* 45 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getPayloadAsUTF8()
/*    */   {
/* 51 */     if (this.data == null)
/*    */     {
/* 53 */       return null;
/*    */     }
/* 55 */     return BufferUtil.toUTF8String(this.data);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\frames\TextFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */