/*    */ package org.eclipse.jetty.websocket.common.util;
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
/*    */ public final class TextUtil
/*    */ {
/*    */   public static String hint(String text)
/*    */   {
/* 37 */     if (text == null)
/*    */     {
/* 39 */       return "<null>";
/*    */     }
/* 41 */     return '"' + maxStringLength(30, text) + '"';
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String maxStringLength(int max, String raw)
/*    */   {
/* 62 */     int length = raw.length();
/* 63 */     if (length <= max)
/*    */     {
/*    */ 
/* 66 */       return raw;
/*    */     }
/*    */     
/* 69 */     if (max < 9)
/*    */     {
/*    */ 
/* 72 */       return raw.substring(0, max);
/*    */     }
/*    */     
/* 75 */     StringBuilder ret = new StringBuilder();
/* 76 */     int startLen = (int)Math.round(max / 3.0D);
/* 77 */     ret.append(raw.substring(0, startLen));
/* 78 */     ret.append("...");
/* 79 */     ret.append(raw.substring(length - (max - startLen - 3)));
/*    */     
/* 81 */     return ret.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\util\TextUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */