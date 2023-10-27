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
/*    */ public class BytesContentProvider
/*    */   extends AbstractTypedContentProvider
/*    */ {
/*    */   private final byte[][] bytes;
/*    */   private final long length;
/*    */   
/*    */   public BytesContentProvider(byte[]... bytes)
/*    */   {
/* 37 */     this("application/octet-stream", bytes);
/*    */   }
/*    */   
/*    */   public BytesContentProvider(String contentType, byte[]... bytes)
/*    */   {
/* 42 */     super(contentType);
/* 43 */     this.bytes = bytes;
/* 44 */     long length = 0L;
/* 45 */     for (byte[] buffer : bytes)
/* 46 */       length += buffer.length;
/* 47 */     this.length = length;
/*    */   }
/*    */   
/*    */ 
/*    */   public long getLength()
/*    */   {
/* 53 */     return this.length;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isReproducible()
/*    */   {
/* 59 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public Iterator<ByteBuffer> iterator()
/*    */   {
/* 65 */     new Iterator()
/*    */     {
/*    */       private int index;
/*    */       
/*    */ 
/*    */       public boolean hasNext()
/*    */       {
/* 72 */         return this.index < BytesContentProvider.this.bytes.length;
/*    */       }
/*    */       
/*    */ 
/*    */       public ByteBuffer next()
/*    */       {
/*    */         try
/*    */         {
/* 80 */           return ByteBuffer.wrap(BytesContentProvider.this.bytes[(this.index++)]);
/*    */         }
/*    */         catch (ArrayIndexOutOfBoundsException x)
/*    */         {
/* 84 */           throw new NoSuchElementException();
/*    */         }
/*    */       }
/*    */     };
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\util\BytesContentProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */