/*    */ package org.eclipse.jetty.websocket.common.util;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.eclipse.jetty.util.Utf8StringBuilder;
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
/*    */ @Deprecated
/*    */ public class Utf8PartialBuilder
/*    */ {
/* 36 */   private final Utf8StringBuilder utf8 = new Utf8StringBuilder();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toPartialString(ByteBuffer buf)
/*    */   {
/* 44 */     if (buf == null)
/*    */     {
/*    */ 
/* 47 */       return "";
/*    */     }
/* 49 */     this.utf8.append(buf);
/* 50 */     return this.utf8.takePartialString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\util\Utf8PartialBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */