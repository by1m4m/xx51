/*    */ package com.google.api.client.testing.util;
/*    */ 
/*    */ import com.google.api.client.util.Beta;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.IOException;
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
/*    */ @Beta
/*    */ public class TestableByteArrayInputStream
/*    */   extends ByteArrayInputStream
/*    */ {
/*    */   private boolean closed;
/*    */   
/*    */   public TestableByteArrayInputStream(byte[] buf)
/*    */   {
/* 37 */     super(buf);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TestableByteArrayInputStream(byte[] buf, int offset, int length)
/*    */   {
/* 46 */     super(buf);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 58 */     this.closed = true;
/*    */   }
/*    */   
/*    */   public final byte[] getBuffer()
/*    */   {
/* 63 */     return this.buf;
/*    */   }
/*    */   
/*    */   public final boolean isClosed()
/*    */   {
/* 68 */     return this.closed;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\util\TestableByteArrayInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */