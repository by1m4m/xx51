/*    */ package org.eclipse.jetty.websocket.common.extensions.compress;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.eclipse.jetty.util.BufferUtil;
/*    */ import org.eclipse.jetty.websocket.api.MessageTooLargeException;
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
/*    */ public class ByteAccumulator
/*    */ {
/* 30 */   private final List<byte[]> chunks = new ArrayList();
/*    */   private final int maxSize;
/* 32 */   private int length = 0;
/*    */   
/*    */   public ByteAccumulator(int maxOverallBufferSize)
/*    */   {
/* 36 */     this.maxSize = maxOverallBufferSize;
/*    */   }
/*    */   
/*    */   public void copyChunk(byte[] buf, int offset, int length)
/*    */   {
/* 41 */     if (this.length + length > this.maxSize)
/*    */     {
/* 43 */       throw new MessageTooLargeException("Frame is too large");
/*    */     }
/*    */     
/* 46 */     byte[] copy = new byte[length - offset];
/* 47 */     System.arraycopy(buf, offset, copy, 0, length);
/*    */     
/* 49 */     this.chunks.add(copy);
/* 50 */     this.length += length;
/*    */   }
/*    */   
/*    */   public int getLength()
/*    */   {
/* 55 */     return this.length;
/*    */   }
/*    */   
/*    */   public void transferTo(ByteBuffer buffer)
/*    */   {
/* 60 */     if (buffer.remaining() < this.length)
/*    */     {
/* 62 */       throw new IllegalArgumentException(String.format("Not enough space in ByteBuffer remaining [%d] for accumulated buffers length [%d]", new Object[] {
/* 63 */         Integer.valueOf(buffer.remaining()), Integer.valueOf(this.length) }));
/*    */     }
/*    */     
/* 66 */     int position = buffer.position();
/* 67 */     for (byte[] chunk : this.chunks)
/*    */     {
/* 69 */       buffer.put(chunk, 0, chunk.length);
/*    */     }
/* 71 */     BufferUtil.flipToFlush(buffer, position);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\extensions\compress\ByteAccumulator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */