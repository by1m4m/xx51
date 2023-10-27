/*     */ package com.fasterxml.jackson.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Target({java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.TYPE})
/*     */ @Retention(RetentionPolicy.RUNTIME)
/*     */ @JacksonAnnotation
/*     */ public @interface JsonFormat
/*     */ {
/*     */   public static final String DEFAULT_LOCALE = "##default";
/*     */   public static final String DEFAULT_TIMEZONE = "##default";
/*     */   
/*     */   String pattern() default "";
/*     */   
/*     */   Shape shape() default Shape.ANY;
/*     */   
/*     */   String locale() default "##default";
/*     */   
/*     */   String timezone() default "##default";
/*     */   
/*     */   public static enum Shape
/*     */   {
/* 130 */     ANY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 136 */     SCALAR, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 141 */     ARRAY, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 146 */     OBJECT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 153 */     NUMBER, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 158 */     NUMBER_FLOAT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */     NUMBER_INT, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 169 */     STRING, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 175 */     BOOLEAN;
/*     */     
/*     */     private Shape() {}
/*     */     
/* 179 */     public boolean isNumeric() { return (this == NUMBER) || (this == NUMBER_INT) || (this == NUMBER_FLOAT); }
/*     */     
/*     */     public boolean isStructured()
/*     */     {
/* 183 */       return (this == OBJECT) || (this == ARRAY);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Value
/*     */   {
/*     */     private final String pattern;
/*     */     
/*     */     private final JsonFormat.Shape shape;
/*     */     
/*     */     private final Locale locale;
/*     */     
/*     */     private final String timezoneStr;
/*     */     
/*     */     private TimeZone _timezone;
/*     */     
/*     */ 
/*     */     public Value()
/*     */     {
/* 203 */       this("", JsonFormat.Shape.ANY, "", "");
/*     */     }
/*     */     
/*     */     public Value(JsonFormat ann) {
/* 207 */       this(ann.pattern(), ann.shape(), ann.locale(), ann.timezone());
/*     */     }
/*     */     
/*     */     public Value(String p, JsonFormat.Shape sh, String localeStr, String tzStr)
/*     */     {
/* 212 */       this(p, sh, (localeStr == null) || (localeStr.length() == 0) || ("##default".equals(localeStr)) ? null : new Locale(localeStr), (tzStr == null) || (tzStr.length() == 0) || ("##default".equals(tzStr)) ? null : tzStr, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Value(String p, JsonFormat.Shape sh, Locale l, TimeZone tz)
/*     */     {
/* 226 */       this.pattern = p;
/* 227 */       this.shape = (sh == null ? JsonFormat.Shape.ANY : sh);
/* 228 */       this.locale = l;
/* 229 */       this._timezone = tz;
/* 230 */       this.timezoneStr = null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Value(String p, JsonFormat.Shape sh, Locale l, String tzStr, TimeZone tz)
/*     */     {
/* 238 */       this.pattern = p;
/* 239 */       this.shape = (sh == null ? JsonFormat.Shape.ANY : sh);
/* 240 */       this.locale = l;
/* 241 */       this._timezone = tz;
/* 242 */       this.timezoneStr = tzStr;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Value withPattern(String p)
/*     */     {
/* 249 */       return new Value(p, this.shape, this.locale, this.timezoneStr, this._timezone);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Value withShape(JsonFormat.Shape s)
/*     */     {
/* 256 */       return new Value(this.pattern, s, this.locale, this.timezoneStr, this._timezone);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Value withLocale(Locale l)
/*     */     {
/* 263 */       return new Value(this.pattern, this.shape, l, this.timezoneStr, this._timezone);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Value withTimeZone(TimeZone tz)
/*     */     {
/* 270 */       return new Value(this.pattern, this.shape, this.locale, null, tz);
/*     */     }
/*     */     
/* 273 */     public String getPattern() { return this.pattern; }
/* 274 */     public JsonFormat.Shape getShape() { return this.shape; }
/* 275 */     public Locale getLocale() { return this.locale; }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String timeZoneAsString()
/*     */     {
/* 285 */       if (this._timezone != null) {
/* 286 */         return this._timezone.getID();
/*     */       }
/* 288 */       return this.timezoneStr;
/*     */     }
/*     */     
/*     */     public TimeZone getTimeZone() {
/* 292 */       TimeZone tz = this._timezone;
/* 293 */       if (tz == null) {
/* 294 */         if (this.timezoneStr == null) {
/* 295 */           return null;
/*     */         }
/* 297 */         tz = TimeZone.getTimeZone(this.timezoneStr);
/* 298 */         this._timezone = tz;
/*     */       }
/* 300 */       return tz;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean hasShape()
/*     */     {
/* 306 */       return this.shape != JsonFormat.Shape.ANY;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean hasPattern()
/*     */     {
/* 312 */       return (this.pattern != null) && (this.pattern.length() > 0);
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean hasLocale()
/*     */     {
/* 318 */       return this.locale != null;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean hasTimeZone()
/*     */     {
/* 324 */       return (this._timezone != null) || ((this.timezoneStr != null) && (!this.timezoneStr.isEmpty()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\annotation\JsonFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */