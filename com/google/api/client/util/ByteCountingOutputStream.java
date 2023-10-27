/*    */ package com.google.api.client.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ final class ByteCountingOutputStream
/*    */   extends OutputStream
/*    */ {
/*    */   long count;
/*    */   
/*    */   public void write(byte[] b, int off, int len)
/*    */     throws IOException
/*    */   {
/* 33 */     this.count += len;
/*    */   }
/*    */   
/*    */   public void write(int b) throws IOException
/*    */   {
/* 38 */     this.count += 1L;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\ByteCountingOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */