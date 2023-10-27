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
/*    */ public class ByteArrayStreamingContent
/*    */   implements StreamingContent
/*    */ {
/*    */   private final byte[] byteArray;
/*    */   private final int offset;
/*    */   private final int length;
/*    */   
/*    */   public ByteArrayStreamingContent(byte[] byteArray)
/*    */   {
/* 45 */     this(byteArray, 0, byteArray.length);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ByteArrayStreamingContent(byte[] byteArray, int offset, int length)
/*    */   {
/* 54 */     this.byteArray = ((byte[])Preconditions.checkNotNull(byteArray));
/* 55 */     Preconditions.checkArgument((offset >= 0) && (length >= 0) && (offset + length <= byteArray.length));
/* 56 */     this.offset = offset;
/* 57 */     this.length = length;
/*    */   }
/*    */   
/*    */   public void writeTo(OutputStream out) throws IOException {
/* 61 */     out.write(this.byteArray, this.offset, this.length);
/* 62 */     out.flush();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\ByteArrayStreamingContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */