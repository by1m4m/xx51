/*    */ package org.eclipse.jetty.client;
/*    */ 
/*    */ import org.eclipse.jetty.io.ByteBufferPool;
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
/*    */ public class GZIPContentDecoder
/*    */   extends org.eclipse.jetty.http.GZIPContentDecoder
/*    */   implements ContentDecoder
/*    */ {
/*    */   private static final int DEFAULT_BUFFER_SIZE = 2048;
/*    */   
/*    */   public GZIPContentDecoder()
/*    */   {
/* 32 */     this(2048);
/*    */   }
/*    */   
/*    */   public GZIPContentDecoder(int bufferSize)
/*    */   {
/* 37 */     this(null, bufferSize);
/*    */   }
/*    */   
/*    */   public GZIPContentDecoder(ByteBufferPool byteBufferPool, int bufferSize)
/*    */   {
/* 42 */     super(byteBufferPool, bufferSize);
/*    */   }
/*    */   
/*    */ 
/*    */   public static class Factory
/*    */     extends ContentDecoder.Factory
/*    */   {
/*    */     private final int bufferSize;
/*    */     
/*    */     private final ByteBufferPool byteBufferPool;
/*    */     
/*    */     public Factory()
/*    */     {
/* 55 */       this(2048);
/*    */     }
/*    */     
/*    */     public Factory(int bufferSize)
/*    */     {
/* 60 */       this(null, bufferSize);
/*    */     }
/*    */     
/*    */     public Factory(ByteBufferPool byteBufferPool)
/*    */     {
/* 65 */       this(byteBufferPool, 2048);
/*    */     }
/*    */     
/*    */     public Factory(ByteBufferPool byteBufferPool, int bufferSize)
/*    */     {
/* 70 */       super();
/* 71 */       this.byteBufferPool = byteBufferPool;
/* 72 */       this.bufferSize = bufferSize;
/*    */     }
/*    */     
/*    */ 
/*    */     public ContentDecoder newContentDecoder()
/*    */     {
/* 78 */       return new GZIPContentDecoder(this.byteBufferPool, this.bufferSize);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\GZIPContentDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */