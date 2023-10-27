/*    */ package org.eclipse.jetty.websocket.common.io.payload;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.eclipse.jetty.websocket.api.extensions.Frame;
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
/*    */ public class DeMaskProcessor
/*    */   implements PayloadProcessor
/*    */ {
/*    */   private byte[] maskBytes;
/*    */   private int maskInt;
/*    */   private int maskOffset;
/*    */   
/*    */   public void process(ByteBuffer payload)
/*    */   {
/* 34 */     if (this.maskBytes == null)
/*    */     {
/* 36 */       return;
/*    */     }
/*    */     
/* 39 */     int maskInt = this.maskInt;
/* 40 */     int start = payload.position();
/* 41 */     int end = payload.limit();
/* 42 */     int offset = this.maskOffset;
/*    */     int remaining;
/* 44 */     while ((remaining = end - start) > 0)
/*    */     {
/* 46 */       if ((remaining >= 4) && ((offset & 0x3) == 0))
/*    */       {
/* 48 */         payload.putInt(start, payload.getInt(start) ^ maskInt);
/* 49 */         start += 4;
/* 50 */         offset += 4;
/*    */       }
/*    */       else
/*    */       {
/* 54 */         payload.put(start, (byte)(payload.get(start) ^ this.maskBytes[(offset & 0x3)]));
/* 55 */         start++;
/* 56 */         offset++;
/*    */       }
/*    */     }
/* 59 */     this.maskOffset = offset;
/*    */   }
/*    */   
/*    */   public void reset(byte[] mask)
/*    */   {
/* 64 */     this.maskBytes = mask;
/* 65 */     int maskInt = 0;
/* 66 */     if (mask != null)
/*    */     {
/* 68 */       for (byte maskByte : mask)
/* 69 */         maskInt = (maskInt << 8) + (maskByte & 0xFF);
/*    */     }
/* 71 */     this.maskInt = maskInt;
/* 72 */     this.maskOffset = 0;
/*    */   }
/*    */   
/*    */ 
/*    */   public void reset(Frame frame)
/*    */   {
/* 78 */     reset(frame.getMask());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\io\payload\DeMaskProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */