/*    */ package org.eclipse.jetty.websocket.common.frames;
/*    */ 
/*    */ import org.eclipse.jetty.util.StringUtil;
/*    */ import org.eclipse.jetty.websocket.api.extensions.Frame.Type;
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
/*    */ public class CloseFrame
/*    */   extends ControlFrame
/*    */ {
/*    */   public CloseFrame()
/*    */   {
/* 28 */     super((byte)8);
/*    */   }
/*    */   
/*    */ 
/*    */   public Frame.Type getType()
/*    */   {
/* 34 */     return Frame.Type.CLOSE;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String truncate(String reason)
/*    */   {
/* 46 */     return StringUtil.truncate(reason, 123);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\frames\CloseFrame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */