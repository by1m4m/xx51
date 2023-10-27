/*    */ package org.eclipse.jetty.websocket.common;
/*    */ 
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.security.MessageDigest;
/*    */ import org.eclipse.jetty.util.B64Code;
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
/*    */ public class AcceptHash
/*    */ {
/* 38 */   private static final byte[] MAGIC = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11".getBytes(StandardCharsets.ISO_8859_1);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String hashKey(String key)
/*    */   {
/*    */     try
/*    */     {
/* 51 */       MessageDigest md = MessageDigest.getInstance("SHA1");
/* 52 */       md.update(key.getBytes(StandardCharsets.UTF_8));
/* 53 */       md.update(MAGIC);
/* 54 */       return new String(B64Code.encode(md.digest()));
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 58 */       throw new RuntimeException(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\AcceptHash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */