/*    */ package com.maxmind.geoip2.model;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
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
/*    */ public class AnonymousIpResponse
/*    */   extends AbstractResponse
/*    */ {
/*    */   @JsonProperty("is_anonymous")
/*    */   private boolean isAnonymous;
/*    */   @JsonProperty("is_anonymous_vpn")
/*    */   private boolean isAnonymousVpn;
/*    */   @JsonProperty("is_hosting_provider")
/*    */   private boolean isHostingProvider;
/*    */   @JsonProperty("is_public_proxy")
/*    */   private boolean isPublicProxy;
/*    */   @JsonProperty("is_tor_exit_node")
/*    */   private boolean isTorExitNode;
/*    */   @JsonProperty("ip_address")
/*    */   private String ipAddress;
/*    */   
/*    */   public boolean isAnonymous()
/*    */   {
/* 33 */     return this.isAnonymous;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isAnonymousVpn()
/*    */   {
/* 40 */     return this.isAnonymousVpn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isHostingProvider()
/*    */   {
/* 47 */     return this.isHostingProvider;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isPublicProxy()
/*    */   {
/* 54 */     return this.isPublicProxy;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isTorExitNode()
/*    */   {
/* 61 */     return this.isTorExitNode;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getIpAddress()
/*    */   {
/* 69 */     return this.ipAddress;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 74 */     return "AnonymousIpResponse[isAnonymous=" + this.isAnonymous + ", isAnonymousVpn=" + this.isAnonymousVpn + ", isHostingProvider=" + this.isHostingProvider + ", isPublicProxy=" + this.isPublicProxy + ", isTorExitNode=" + this.isTorExitNode + ", ipAddress='" + this.ipAddress + '\'' + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\model\AnonymousIpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */