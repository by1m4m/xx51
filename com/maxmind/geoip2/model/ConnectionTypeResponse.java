/*    */ package com.maxmind.geoip2.model;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import com.fasterxml.jackson.annotation.JsonValue;
/*    */ 
/*    */ public class ConnectionTypeResponse extends AbstractResponse
/*    */ {
/*    */   @JsonProperty("connection_type")
/*    */   private ConnectionType connectionType;
/*    */   @JsonProperty("ip_address")
/*    */   private String ipAddress;
/*    */   
/*    */   public static enum ConnectionType
/*    */   {
/* 15 */     DIALUP("Dialup"),  CABLE_DSL("Cable/DSL"),  CORPORATE("Corporate"),  CELLULAR("Cellular");
/*    */     
/*    */     private final String name;
/*    */     
/*    */     private ConnectionType(String name)
/*    */     {
/* 21 */       this.name = name;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     @JsonValue
/*    */     public String toString()
/*    */     {
/* 32 */       return this.name;
/*    */     }
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
/*    */   public ConnectionType getConnectionType()
/*    */   {
/* 46 */     return this.connectionType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getIpAddress()
/*    */   {
/* 53 */     return this.ipAddress;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 63 */     return "ConnectionTypeResponse [connectionType=" + this.connectionType + ", ipAddress=" + this.ipAddress + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\model\ConnectionTypeResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */