/*    */ package com.google.api.client.testing.util;
/*    */ 
/*    */ import com.google.api.client.util.Beta;
/*    */ import java.io.ByteArrayOutputStream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Beta
/*    */ public class TestableByteArrayOutputStream
/*    */   extends ByteArrayOutputStream
/*    */ {
/*    */   private boolean closed;
/*    */   
/*    */   public void close()
/*    */     throws IOException
/*    */   {
/* 44 */     this.closed = true;
/*    */   }
/*    */   
/*    */   public final byte[] getBuffer()
/*    */   {
/* 49 */     return this.buf;
/*    */   }
/*    */   
/*    */   public final boolean isClosed()
/*    */   {
/* 54 */     return this.closed;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\testing\util\TestableByteArrayOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */