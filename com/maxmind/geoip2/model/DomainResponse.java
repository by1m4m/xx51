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
/*    */ public class DomainResponse
/*    */   extends AbstractResponse
/*    */ {
/*    */   @JsonProperty
/*    */   private String domain;
/*    */   @JsonProperty("ip_address")
/*    */   private String ipAddress;
/*    */   
/*    */   public String getDomain()
/*    */   {
/* 22 */     return this.domain;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getIpAddress()
/*    */   {
/* 29 */     return this.ipAddress;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 39 */     return "DomainResponse [domain=" + this.domain + ", ipAddress=" + this.ipAddress + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\model\DomainResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */