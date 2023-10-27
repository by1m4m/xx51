/*     */ package com.fasterxml.jackson.databind.ext;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.deser.Deserializers.Base;
/*     */ import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.TimeZone;
/*     */ import javax.xml.datatype.DatatypeConfigurationException;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class CoreXMLDeserializers extends Deserializers.Base
/*     */ {
/*     */   static final DatatypeFactory _dataTypeFactory;
/*     */   protected static final int TYPE_DURATION = 1;
/*     */   protected static final int TYPE_G_CALENDAR = 2;
/*     */   protected static final int TYPE_QNAME = 3;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  30 */       _dataTypeFactory = DatatypeFactory.newInstance();
/*     */     } catch (DatatypeConfigurationException e) {
/*  32 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public com.fasterxml.jackson.databind.JsonDeserializer<?> findBeanDeserializer(JavaType type, DeserializationConfig config, BeanDescription beanDesc)
/*     */   {
/*  40 */     Class<?> raw = type.getRawClass();
/*  41 */     if (raw == QName.class) {
/*  42 */       return new Std(raw, 3);
/*     */     }
/*  44 */     if (raw == XMLGregorianCalendar.class) {
/*  45 */       return new Std(raw, 2);
/*     */     }
/*  47 */     if (raw == javax.xml.datatype.Duration.class) {
/*  48 */       return new Std(raw, 1);
/*     */     }
/*  50 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Std
/*     */     extends FromStringDeserializer<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final int _kind;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Std(Class<?> raw, int kind)
/*     */     {
/*  78 */       super();
/*  79 */       this._kind = kind;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonProcessingException
/*     */     {
/*  87 */       if (this._kind == 2) {
/*  88 */         Date d = _parseDate(jp, ctxt);
/*  89 */         if (d == null) {
/*  90 */           return null;
/*     */         }
/*  92 */         GregorianCalendar calendar = new GregorianCalendar();
/*  93 */         calendar.setTime(d);
/*  94 */         TimeZone tz = ctxt.getTimeZone();
/*  95 */         if (tz != null) {
/*  96 */           calendar.setTimeZone(tz);
/*     */         }
/*  98 */         return CoreXMLDeserializers._dataTypeFactory.newXMLGregorianCalendar(calendar);
/*     */       }
/* 100 */       return super.deserialize(jp, ctxt);
/*     */     }
/*     */     
/*     */     protected Object _deserialize(String value, DeserializationContext ctxt)
/*     */       throws IllegalArgumentException
/*     */     {
/* 106 */       switch (this._kind) {
/*     */       case 1: 
/* 108 */         return CoreXMLDeserializers._dataTypeFactory.newDuration(value);
/*     */       case 3: 
/* 110 */         return QName.valueOf(value);
/*     */       }
/* 112 */       throw new IllegalStateException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ext\CoreXMLDeserializers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */