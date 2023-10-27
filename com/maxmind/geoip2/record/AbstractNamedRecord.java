/*    */ package com.maxmind.geoip2.record;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JacksonInject;
/*    */ import com.fasterxml.jackson.annotation.JsonProperty;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractNamedRecord
/*    */ {
/*    */   @JsonProperty
/* 15 */   private final HashMap<String, String> names = new HashMap();
/*    */   
/*    */   @JsonProperty("geoname_id")
/*    */   private Integer geoNameId;
/*    */   @JacksonInject("locales")
/* 20 */   private final List<String> locales = new ArrayList();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Integer getGeoNameId()
/*    */   {
/* 31 */     return this.geoNameId;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 40 */     for (String lang : this.locales) {
/* 41 */       if (this.names.containsKey(lang)) {
/* 42 */         return (String)this.names.get(lang);
/*    */       }
/*    */     }
/* 45 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Map<String, String> getNames()
/*    */   {
/* 53 */     return new HashMap(this.names);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 63 */     return getName() != null ? getName() : "";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\record\AbstractNamedRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */