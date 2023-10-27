/*     */ package com.maxmind.geoip2.record;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Traits
/*     */ {
/*     */   @JsonProperty("autonomous_system_number")
/*     */   private Integer autonomousSystemNumber;
/*     */   @JsonProperty("autonomous_system_organization")
/*     */   private String autonomousSystemOrganization;
/*     */   @JsonProperty
/*     */   private String domain;
/*     */   @JsonProperty("ip_address")
/*     */   private String ipAddress;
/*     */   @JsonProperty("is_anonymous_proxy")
/*     */   private boolean anonymousProxy;
/*     */   @JsonProperty("is_satellite_provider")
/*     */   private boolean satelliteProvider;
/*     */   @JsonProperty
/*     */   private String isp;
/*     */   @JsonProperty
/*     */   private String organization;
/*     */   @JsonProperty("user_type")
/*     */   private String userType;
/*     */   
/*     */   public Integer getAutonomousSystemNumber()
/*     */   {
/*  49 */     return this.autonomousSystemNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAutonomousSystemOrganization()
/*     */   {
/*  60 */     return this.autonomousSystemOrganization;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDomain()
/*     */   {
/*  70 */     return this.domain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIpAddress()
/*     */   {
/*  82 */     return this.ipAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIsp()
/*     */   {
/*  91 */     return this.isp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getOrganization()
/*     */   {
/* 100 */     return this.organization;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUserType()
/*     */   {
/* 130 */     return this.userType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public boolean isAnonymousProxy()
/*     */   {
/* 145 */     return this.anonymousProxy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public boolean isSatelliteProvider()
/*     */   {
/* 158 */     return this.satelliteProvider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 168 */     return "Traits [" + (this.autonomousSystemNumber != null ? "autonomousSystemNumber=" + this.autonomousSystemNumber + ", " : "") + (this.autonomousSystemOrganization != null ? "autonomousSystemOrganization=" + this.autonomousSystemOrganization + ", " : "") + (this.domain != null ? "domain=" + this.domain + ", " : "") + (this.ipAddress != null ? "ipAddress=" + this.ipAddress + ", " : "") + "anonymousProxy=" + this.anonymousProxy + ", satelliteProvider=" + this.satelliteProvider + ", " + (this.isp != null ? "isp=" + this.isp + ", " : "") + (this.organization != null ? "organization=" + this.organization + ", " : "") + (this.userType != null ? "userType=" + this.userType : "") + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\record\Traits.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */