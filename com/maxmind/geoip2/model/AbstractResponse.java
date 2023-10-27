/*    */ package com.maxmind.geoip2.model;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractResponse
/*    */ {
/*    */   public String toJson()
/*    */     throws IOException
/*    */   {
/* 16 */     ObjectMapper mapper = new ObjectMapper();
/* 17 */     mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
/* 18 */     mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
/* 19 */     return mapper.writeValueAsString(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\maxmind\geoip2\model\AbstractResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */