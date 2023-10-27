/*    */ package com.google.api.client.http;
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
/*    */ public class EmptyContent
/*    */   implements HttpContent
/*    */ {
/*    */   public long getLength()
/*    */     throws IOException
/*    */   {
/* 37 */     return 0L;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 41 */     return null;
/*    */   }
/*    */   
/*    */   public void writeTo(OutputStream out) throws IOException {
/* 45 */     out.flush();
/*    */   }
/*    */   
/*    */   public boolean retrySupported() {
/* 49 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\EmptyContent.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */