/*     */ package com.maxmind.geoip2.model;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnore;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty;
/*     */ import com.maxmind.geoip2.record.City;
/*     */ import com.maxmind.geoip2.record.Location;
/*     */ import com.maxmind.geoip2.record.Postal;
/*     */ import com.maxmind.geoip2.record.Subdivision;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ abstract class AbstractCityResponse extends AbstractCountryResponse
/*     */ {
/*     */   @JsonProperty
/*  14 */   private final City city = new City();
/*     */   @JsonProperty
/*  16 */   private final Location location = new Location();
/*     */   @JsonProperty
/*  18 */   private final Postal postal = new Postal();
/*     */   
/*     */   @JsonProperty("subdivisions")
/*  21 */   private final ArrayList<Subdivision> subdivisions = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public City getCity()
/*     */   {
/*  28 */     return this.city;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Location getLocation()
/*     */   {
/*  35 */     return this.location;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Postal getPostal()
/*     */   {
/*  42 */     return this.postal;
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
/*     */   public java.util.List<Subdivision> getSubdivisions()
/*     */   {
/*  55 */     return new ArrayList(this.subdivisions);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JsonIgnore
/*     */   public Subdivision getMostSpecificSubdivision()
/*     */   {
/*  65 */     if (this.subdivisions.isEmpty()) {
/*  66 */       return new Subdivision();
/*     */     }
/*  68 */     return (Subdivision)this.subdivisions.get(this.subdivisions.size() - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JsonIgnore
/*     */   public Subdivision getLeastSpecificSubdivision()
/*     */   {
/*  78 */     if (this.subdivisions.isEmpty()) {
/*  79 */       return new Subdivision();
/*     */     }
/*  81 */     return (Subdivision)this.subdivisions.get(0);
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
/*     */   public String toString()
/*     */   {
/* 106 */     return getClass().getSimpleName() + " [" + (getCity() != null ? "getCity()=" + getCity() + ", " : "") + (getLocation() != null ? "getLocation()=" + getLocation() + ", " : "") + (getPostal() != null ? "getPostal()=" + getPostal() + ", " : "") + (getSubdivisions() != null ? "getSubdivisionsList()=" + getSubdivisions() + ", " : "") + (getContinent() != null ? "getContinent()=" + getContinent() + ", " : "") + (getCountry() != null ? "getCountry()=" + getCountry() + ", " : "") + (getRegisteredCountry() != null ? "getRegisteredCountry()=" + getRegisteredCountry() + ", " : "") + (getRepresentedCountry() != null ? "getRepresentedCountry()=" + getRepresentedCountry() + ", " : "") + (getTraits() != null ? "getTraits()=" + getTraits() : "") + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\model\AbstractCityResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */