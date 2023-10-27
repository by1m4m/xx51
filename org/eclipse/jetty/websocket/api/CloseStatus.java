/*    */ package org.eclipse.jetty.websocket.api;
/*    */ 
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ public class CloseStatus
/*    */ {
/*    */   private static final int MAX_CONTROL_PAYLOAD = 125;
/*    */   public static final int MAX_REASON_PHRASE = 123;
/*    */   private int code;
/*    */   private String phrase;
/*    */   
/*    */   @Deprecated
/*    */   public static String trimMaxReasonLength(String reason)
/*    */   {
/* 39 */     if (reason == null)
/*    */     {
/* 41 */       return null;
/*    */     }
/*    */     
/* 44 */     byte[] reasonBytes = reason.getBytes(StandardCharsets.UTF_8);
/* 45 */     if (reasonBytes.length > 123)
/*    */     {
/* 47 */       byte[] trimmed = new byte[123];
/* 48 */       System.arraycopy(reasonBytes, 0, trimmed, 0, 123);
/* 49 */       return new String(trimmed, StandardCharsets.UTF_8);
/*    */     }
/*    */     
/* 52 */     return reason;
/*    */   }
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
/*    */   public CloseStatus(int closeCode, String reasonPhrase)
/*    */   {
/* 69 */     this.code = closeCode;
/* 70 */     this.phrase = reasonPhrase;
/* 71 */     if (reasonPhrase.length() > 123)
/*    */     {
/* 73 */       throw new IllegalArgumentException("Phrase exceeds maximum length of 123");
/*    */     }
/*    */   }
/*    */   
/*    */   public int getCode()
/*    */   {
/* 79 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getPhrase()
/*    */   {
/* 84 */     return this.phrase;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\CloseStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */