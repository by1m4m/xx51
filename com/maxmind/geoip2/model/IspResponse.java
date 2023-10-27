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
/*    */ public class IspResponse
/*    */   extends AbstractResponse
/*    */ {
/*    */   @JsonProperty("autonomous_system_number")
/*    */   private Integer autonomousSystemNumber;
/*    */   @JsonProperty("autonomous_system_organization")
/*    */   private String autonomousSystemOrganization;
/*    */   @JsonProperty
/*    */   private String isp;
/*    */   @JsonProperty
/*    */   private String organization;
/*    */   @JsonProperty("ip_address")
/*    */   private String ipAddress;
/*    */   
/*    */   public Integer getAutonomousSystemNumber()
/*    */   {
/* 29 */     return this.autonomousSystemNumber;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getAutonomousSystemOrganization()
/*    */   {
/* 37 */     return this.autonomousSystemOrganization;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getIsp()
/*    */   {
/* 44 */     return this.isp;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getOrganization()
/*    */   {
/* 51 */     return this.organization;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getIpAddress()
/*    */   {
/* 58 */     return this.ipAddress;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 68 */     return "IspResponse [autonomousSystemNumber=" + this.autonomousSystemNumber + ", autonomousSystemOrganization=" + this.autonomousSystemOrganization + ", isp=" + this.isp + ", organization=" + this.organization + ", ipAddress=" + this.ipAddress + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\model\IspResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */