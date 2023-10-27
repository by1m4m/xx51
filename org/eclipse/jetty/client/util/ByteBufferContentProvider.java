/*    */ package org.eclipse.jetty.client.util;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ByteBufferContentProvider
/*    */   extends AbstractTypedContentProvider
/*    */ {
/*    */   private final ByteBuffer[] buffers;
/*    */   private final int length;
/*    */   
/*    */   public ByteBufferContentProvider(ByteBuffer... buffers)
/*    */   {
/* 41 */     this("application/octet-stream", buffers);
/*    */   }
/*    */   
/*    */   public ByteBufferContentProvider(String contentType, ByteBuffer... buffers)
/*    */   {
/* 46 */     super(contentType);
/* 47 */     this.buffers = buffers;
/* 48 */     int length = 0;
/* 49 */     for (ByteBuffer buffer : buffers)
/* 50 */       length += buffer.remaining();
/* 51 */     this.length = length;
/*    */   }
/*    */   
/*    */ 
/*    */   public long getLength()
/*    */   {
/* 57 */     return this.length;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isReproducible()
/*    */   {
/* 63 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public Iterator<ByteBuffer> iterator()
/*    */   {
/* 69 */     new Iterator()
/*    */     {
/*    */       private int index;
/*    */       
/*    */ 
/*    */       public boolean hasNext()
/*    */       {
/* 76 */         return this.index < ByteBufferContentProvider.this.buffers.length;
/*    */       }
/*    */       
/*    */ 
/*    */       public ByteBuffer next()
/*    */       {
/*    */         try
/*    */         {
/* 84 */           ByteBuffer buffer = ByteBufferContentProvider.this.buffers[this.index];
/* 85 */           ByteBufferContentProvider.this.buffers[this.index] = buffer.slice();
/* 86 */           this.index += 1;
/* 87 */           return buffer;
/*    */         }
/*    */         catch (ArrayIndexOutOfBoundsException x)
/*    */         {
/* 91 */           throw new NoSuchElementException();
/*    */         }
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\util\ByteBufferContentProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */