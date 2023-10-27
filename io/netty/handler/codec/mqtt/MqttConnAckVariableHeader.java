/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public final class MqttConnAckVariableHeader
/*    */ {
/*    */   private final MqttConnectReturnCode connectReturnCode;
/*    */   private final boolean sessionPresent;
/*    */   
/*    */   public MqttConnAckVariableHeader(MqttConnectReturnCode connectReturnCode, boolean sessionPresent)
/*    */   {
/* 31 */     this.connectReturnCode = connectReturnCode;
/* 32 */     this.sessionPresent = sessionPresent;
/*    */   }
/*    */   
/*    */   public MqttConnectReturnCode connectReturnCode() {
/* 36 */     return this.connectReturnCode;
/*    */   }
/*    */   
/*    */   public boolean isSessionPresent() {
/* 40 */     return this.sessionPresent;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 45 */     return 
/*    */     
/*    */ 
/*    */ 
/* 49 */       StringUtil.simpleClassName(this) + '[' + "connectReturnCode=" + this.connectReturnCode + ", sessionPresent=" + this.sessionPresent + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\mqtt\MqttConnAckVariableHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */