/*    */ package com.google.api.client.http;
/*    */ 
/*    */ import com.google.api.client.util.StreamingContent;
/*    */ import java.io.BufferedOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
/*    */ import java.util.zip.GZIPOutputStream;
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
/*    */ public class GZipEncoding
/*    */   implements HttpEncoding
/*    */ {
/*    */   public String getName()
/*    */   {
/* 33 */     return "gzip";
/*    */   }
/*    */   
/*    */   public void encode(StreamingContent content, OutputStream out) throws IOException
/*    */   {
/* 38 */     OutputStream out2 = new BufferedOutputStream(out)
/*    */     {
/*    */       public void close() throws IOException
/*    */       {
/*    */         try {
/* 43 */           flush();
/*    */         }
/*    */         catch (IOException ignored) {}
/*    */       }
/* 47 */     };
/* 48 */     GZIPOutputStream zipper = new GZIPOutputStream(out2);
/* 49 */     content.writeTo(zipper);
/*    */     
/* 51 */     zipper.close();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\GZipEncoding.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */