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
/*    */ public final class MaxMind
/*    */ {
/*    */   @JsonProperty("queries_remaining")
/*    */   private Integer queriesRemaining;
/*    */   
/*    */   public Integer getQueriesRemaining()
/*    */   {
/* 22 */     return this.queriesRemaining;
/*    */   }
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
/* 34 */     return "MaxMind [" + (getQueriesRemaining() != null ? "getQueriesRemaining()=" + getQueriesRemaining() : "") + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\record\MaxMind.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */