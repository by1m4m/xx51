/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerationException;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import java.io.IOException;
/*    */ import java.text.DateFormat;
/*    */ import java.util.Date;
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
/*    */ @JacksonStdImpl
/*    */ public class DateSerializer
/*    */   extends DateTimeSerializerBase<Date>
/*    */ {
/* 24 */   public static final DateSerializer instance = new DateSerializer();
/*    */   
/*    */   public DateSerializer() {
/* 27 */     this(null, null);
/*    */   }
/*    */   
/*    */   public DateSerializer(Boolean useTimestamp, DateFormat customFormat) {
/* 31 */     super(Date.class, useTimestamp, customFormat);
/*    */   }
/*    */   
/*    */   public DateSerializer withFormat(Boolean timestamp, DateFormat customFormat)
/*    */   {
/* 36 */     return new DateSerializer(timestamp, customFormat);
/*    */   }
/*    */   
/*    */   protected long _timestamp(Date value)
/*    */   {
/* 41 */     return value == null ? 0L : value.getTime();
/*    */   }
/*    */   
/*    */ 
/*    */   public void serialize(Date value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 48 */     if (_asTimestamp(provider)) {
/* 49 */       jgen.writeNumber(_timestamp(value));
/* 50 */     } else if (this._customFormat != null)
/*    */     {
/* 52 */       synchronized (this._customFormat) {
/* 53 */         jgen.writeString(this._customFormat.format(value));
/*    */       }
/*    */     } else {
/* 56 */       provider.defaultSerializeDateValue(value, jgen);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\DateSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */