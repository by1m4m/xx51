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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Postal
/*    */ {
/*    */   @JsonProperty
/*    */   private String code;
/*    */   @JsonProperty
/*    */   private Integer confidence;
/*    */   
/*    */   public String getCode()
/*    */   {
/* 30 */     return this.code;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Integer getConfidence()
/*    */   {
/* 39 */     return this.confidence;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 49 */     return this.code != null ? this.code : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\record\Postal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */