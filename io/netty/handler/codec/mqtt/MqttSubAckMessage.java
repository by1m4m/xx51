/*    */ package io.netty.handler.codec.mqtt;
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
/*    */ public final class MqttSubAckMessage
/*    */   extends MqttMessage
/*    */ {
/*    */   public MqttSubAckMessage(MqttFixedHeader mqttFixedHeader, MqttMessageIdVariableHeader variableHeader, MqttSubAckPayload payload)
/*    */   {
/* 28 */     super(mqttFixedHeader, variableHeader, payload);
/*    */   }
/*    */   
/*    */   public MqttMessageIdVariableHeader variableHeader()
/*    */   {
/* 33 */     return (MqttMessageIdVariableHeader)super.variableHeader();
/*    */   }
/*    */   
/*    */   public MqttSubAckPayload payload()
/*    */   {
/* 38 */     return (MqttSubAckPayload)super.payload();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\mqtt\MqttSubAckMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */