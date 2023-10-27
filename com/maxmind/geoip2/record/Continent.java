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
/*    */ public final class Continent
/*    */   extends AbstractNamedRecord
/*    */ {
/*    */   @JsonProperty("code")
/*    */   private String code;
/*    */   
/*    */   public String getCode()
/*    */   {
/* 22 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\record\Continent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */