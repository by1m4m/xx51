/*    */ package org.eclipse.jetty.websocket.api;
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
/*    */ public enum BatchMode
/*    */ {
/* 32 */   AUTO, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 37 */   ON, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 42 */   OFF;
/*    */   
/*    */   private BatchMode() {}
/*    */   
/*    */   public static BatchMode max(BatchMode one, BatchMode two) {
/* 47 */     return one.ordinal() < two.ordinal() ? two : one;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\BatchMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */