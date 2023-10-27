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
/*    */ public final class MqttTopicSubscription
/*    */ {
/*    */   private final String topicFilter;
/*    */   private final MqttQoS qualityOfService;
/*    */   
/*    */   public MqttTopicSubscription(String topicFilter, MqttQoS qualityOfService)
/*    */   {
/* 31 */     this.topicFilter = topicFilter;
/* 32 */     this.qualityOfService = qualityOfService;
/*    */   }
/*    */   
/*    */   public String topicName() {
/* 36 */     return this.topicFilter;
/*    */   }
/*    */   
/*    */   public MqttQoS qualityOfService() {
/* 40 */     return this.qualityOfService;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 45 */     return 
/*    */     
/*    */ 
/*    */ 
/* 49 */       StringUtil.simpleClassName(this) + '[' + "topicFilter=" + this.topicFilter + ", qualityOfService=" + this.qualityOfService + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\mqtt\MqttTopicSubscription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */