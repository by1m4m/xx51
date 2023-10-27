/*    */ package com.maxmind.geoip2.model;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import com.maxmind.geoip2.record.Continent;
/*    */ import com.maxmind.geoip2.record.Country;
/*    */ import com.maxmind.geoip2.record.RepresentedCountry;
/*    */ 
/*    */ abstract class AbstractCountryResponse extends AbstractResponse
/*    */ {
/*    */   @JsonProperty
/* 11 */   private final Continent continent = new Continent();
/*    */   
/*    */   @JsonProperty
/* 14 */   private final Country country = new Country();
/*    */   
/*    */   @JsonProperty("registered_country")
/* 17 */   private final Country registeredCountry = new Country();
/*    */   
/*    */   @JsonProperty("maxmind")
/* 20 */   private final com.maxmind.geoip2.record.MaxMind maxmind = new com.maxmind.geoip2.record.MaxMind();
/*    */   
/*    */   @JsonProperty("represented_country")
/* 23 */   private final RepresentedCountry representedCountry = new RepresentedCountry();
/*    */   
/*    */   @JsonProperty
/* 26 */   private final com.maxmind.geoip2.record.Traits traits = new com.maxmind.geoip2.record.Traits();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Continent getContinent()
/*    */   {
/* 33 */     return this.continent;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Country getCountry()
/*    */   {
/* 42 */     return this.country;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @JsonProperty("maxmind")
/*    */   public com.maxmind.geoip2.record.MaxMind getMaxMind()
/*    */   {
/* 50 */     return this.maxmind;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Country getRegisteredCountry()
/*    */   {
/* 59 */     return this.registeredCountry;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public RepresentedCountry getRepresentedCountry()
/*    */   {
/* 69 */     return this.representedCountry;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public com.maxmind.geoip2.record.Traits getTraits()
/*    */   {
/* 76 */     return this.traits;
/*    */   }
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
/*    */   public String toString()
/*    */   {
/* 97 */     return "Country [" + (getContinent() != null ? "getContinent()=" + getContinent() + ", " : "") + (getCountry() != null ? "getCountry()=" + getCountry() + ", " : "") + (getRegisteredCountry() != null ? "getRegisteredCountry()=" + getRegisteredCountry() + ", " : "") + (getRepresentedCountry() != null ? "getRepresentedCountry()=" + getRepresentedCountry() + ", " : "") + (getTraits() != null ? "getTraits()=" + getTraits() : "") + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\model\AbstractCountryResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */