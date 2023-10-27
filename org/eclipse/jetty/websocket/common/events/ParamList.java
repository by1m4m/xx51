/*    */ package org.eclipse.jetty.websocket.common.events;
/*    */ 
/*    */ import java.util.ArrayList;
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
/*    */ public class ParamList
/*    */   extends ArrayList<Class<?>[]>
/*    */ {
/*    */   public void addParams(Class<?>... paramTypes)
/*    */   {
/* 31 */     add(paramTypes);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\ParamList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */