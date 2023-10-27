/*    */ package org.eclipse.jetty.websocket.common.extensions.compress;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.zip.DataFormatException;
/*    */ import org.eclipse.jetty.websocket.api.BadPayloadException;
/*    */ import org.eclipse.jetty.websocket.api.extensions.Frame;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeflateFrameExtension
/*    */   extends CompressExtension
/*    */ {
/*    */   public String getName()
/*    */   {
/* 36 */     return "deflate-frame";
/*    */   }
/*    */   
/*    */ 
/*    */   int getRsvUseMode()
/*    */   {
/* 42 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */   int getTailDropMode()
/*    */   {
/* 48 */     return 1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void incomingFrame(Frame frame)
/*    */   {
/* 58 */     if ((frame.getType().isControl()) || (!frame.isRsv1()) || (!frame.hasPayload()))
/*    */     {
/* 60 */       nextIncomingFrame(frame);
/* 61 */       return;
/*    */     }
/*    */     
/*    */     try
/*    */     {
/* 66 */       ByteAccumulator accumulator = newByteAccumulator();
/* 67 */       decompress(accumulator, frame.getPayload());
/* 68 */       decompress(accumulator, TAIL_BYTES_BUF.slice());
/* 69 */       forwardIncoming(frame, accumulator);
/*    */     }
/*    */     catch (DataFormatException e)
/*    */     {
/* 73 */       throw new BadPayloadException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\extensions\compress\DeflateFrameExtension.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */