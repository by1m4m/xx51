/*    */ package com.maxmind.geoip2.record;
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
/*    */ 
/*    */ public class Country
/*    */   extends AbstractNamedRecord
/*    */ {
/*    */   @JsonProperty
/*    */   private Integer confidence;
/*    */   @JsonProperty("iso_code")
/*    */   private String isoCode;
/*    */   
/*    */   public Integer getConfidence()
/*    */   {
/* 26 */     return this.confidence;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getIsoCode()
/*    */   {
/* 36 */     return this.isoCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\record\Country.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */