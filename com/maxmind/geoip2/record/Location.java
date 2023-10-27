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
/*     */ public class Location
/*     */ {
/*     */   @JsonProperty("accuracy_radius")
/*     */   private Integer accuracyRadius;
/*     */   @JsonProperty("average_income")
/*     */   private Integer averageIncome;
/*     */   @JsonProperty
/*     */   private Double latitude;
/*     */   @JsonProperty
/*     */   private Double longitude;
/*     */   @JsonProperty("metro_code")
/*     */   private Integer metroCode;
/*     */   @JsonProperty("population_density")
/*     */   private Integer populationDensity;
/*     */   @JsonProperty("time_zone")
/*     */   private String timeZone;
/*     */   
/*     */   public Integer getAccuracyRadius()
/*     */   {
/*  41 */     return this.accuracyRadius;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getAverageIncome()
/*     */   {
/*  49 */     return this.averageIncome;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Double getLatitude()
/*     */   {
/*  58 */     return this.latitude;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Double getLongitude()
/*     */   {
/*  67 */     return this.longitude;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getMetroCode()
/*     */   {
/*  78 */     return this.metroCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getPopulationDensity()
/*     */   {
/*  86 */     return this.populationDensity;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getTimeZone()
/*     */   {
/*  96 */     return this.timeZone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 106 */     return "Location [" + (this.accuracyRadius != null ? "accuracyRadius=" + this.accuracyRadius + ", " : "") + (this.latitude != null ? "latitude=" + this.latitude + ", " : "") + (this.longitude != null ? "longitude=" + this.longitude + ", " : "") + (this.metroCode != null ? "metroCode=" + this.metroCode + ", " : "") + (this.timeZone != null ? "timeZone=" + this.timeZone : "") + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\record\Location.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */