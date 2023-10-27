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
/*    */ public final class RepresentedCountry
/*    */   extends Country
/*    */ {
/*    */   @JsonProperty
/*    */   private String type;
/*    */   
/*    */   public String getType()
/*    */   {
/* 25 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\record\RepresentedCountry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */