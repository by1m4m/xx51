/*    */ package org.eclipse.jetty.websocket.common.events;
/*    */ 
/*    */ import org.eclipse.jetty.websocket.common.events.annotated.CallableMethod;
/*    */ import org.eclipse.jetty.websocket.common.events.annotated.OptionalSessionCallableMethod;
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
/*    */ public class JettyAnnotatedMetadata
/*    */ {
/*    */   public CallableMethod onConnect;
/*    */   public OptionalSessionCallableMethod onBinary;
/*    */   public OptionalSessionCallableMethod onText;
/*    */   public OptionalSessionCallableMethod onFrame;
/*    */   public OptionalSessionCallableMethod onError;
/*    */   public OptionalSessionCallableMethod onClose;
/*    */   
/*    */   public String toString()
/*    */   {
/* 42 */     StringBuilder s = new StringBuilder();
/* 43 */     s.append("JettyPojoMetadata[");
/* 44 */     s.append("onConnect=").append(this.onConnect);
/* 45 */     s.append(",onBinary=").append(this.onBinary);
/* 46 */     s.append(",onText=").append(this.onText);
/* 47 */     s.append(",onFrame=").append(this.onFrame);
/* 48 */     s.append(",onError=").append(this.onError);
/* 49 */     s.append(",onClose=").append(this.onClose);
/* 50 */     s.append("]");
/* 51 */     return s.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\JettyAnnotatedMetadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */