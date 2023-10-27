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
/*    */ public final class City
/*    */   extends AbstractNamedRecord
/*    */ {
/*    */   @JsonProperty
/*    */   private Integer confidence;
/*    */   
/*    */   public Integer getConfidence()
/*    */   {
/* 23 */     return this.confidence;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\record\City.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */