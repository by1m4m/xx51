/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.TimeZone;
/*    */ 
/*    */ public class TimeZoneSerializer extends StdScalarSerializer<TimeZone>
/*    */ {
/*    */   public TimeZoneSerializer()
/*    */   {
/* 13 */     super(TimeZone.class);
/*    */   }
/*    */   
/*    */   public void serialize(TimeZone value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
/* 17 */     jgen.writeString(value.getID());
/*    */   }
/*    */   
/*    */   public void serializeWithType(TimeZone value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*    */     throws IOException
/*    */   {
/* 23 */     typeSer.writeTypePrefixForScalar(value, jgen, TimeZone.class);
/* 24 */     serialize(value, jgen, provider);
/* 25 */     typeSer.writeTypeSuffixForScalar(value, jgen);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\TimeZoneSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */