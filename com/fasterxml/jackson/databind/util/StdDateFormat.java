/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
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
/*     */ public class StdDateFormat
/*     */   extends DateFormat
/*     */ {
/*     */   protected static final String DATE_FORMAT_STR_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
/*     */   protected static final String DATE_FORMAT_STR_ISO8601_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
/*     */   protected static final String DATE_FORMAT_STR_PLAIN = "yyyy-MM-dd";
/*     */   protected static final String DATE_FORMAT_STR_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
/*  56 */   protected static final String[] ALL_FORMATS = { "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd" };
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
/*  68 */   private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("GMT");
/*     */   
/*     */ 
/*  71 */   private static final Locale DEFAULT_LOCALE = Locale.US;
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
/*  89 */   protected static final DateFormat DATE_FORMAT_RFC1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", DEFAULT_LOCALE);
/*  90 */   static { DATE_FORMAT_RFC1123.setTimeZone(DEFAULT_TIMEZONE);
/*  91 */     DATE_FORMAT_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", DEFAULT_LOCALE);
/*  92 */     DATE_FORMAT_ISO8601.setTimeZone(DEFAULT_TIMEZONE);
/*  93 */     DATE_FORMAT_ISO8601_Z = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", DEFAULT_LOCALE);
/*  94 */     DATE_FORMAT_ISO8601_Z.setTimeZone(DEFAULT_TIMEZONE);
/*  95 */     DATE_FORMAT_PLAIN = new SimpleDateFormat("yyyy-MM-dd", DEFAULT_LOCALE);
/*  96 */     DATE_FORMAT_PLAIN.setTimeZone(DEFAULT_TIMEZONE);
/*     */   }
/*     */   
/*     */   protected static final DateFormat DATE_FORMAT_ISO8601;
/*     */   protected static final DateFormat DATE_FORMAT_ISO8601_Z;
/*     */   protected static final DateFormat DATE_FORMAT_PLAIN;
/* 102 */   public static final StdDateFormat instance = new StdDateFormat();
/*     */   
/*     */ 
/*     */   protected transient TimeZone _timezone;
/*     */   
/*     */ 
/*     */   protected final Locale _locale;
/*     */   
/*     */ 
/*     */   protected transient DateFormat _formatRFC1123;
/*     */   
/*     */ 
/*     */   protected transient DateFormat _formatISO8601;
/*     */   
/*     */ 
/*     */   protected transient DateFormat _formatISO8601_z;
/*     */   
/*     */   protected transient DateFormat _formatPlain;
/*     */   
/*     */ 
/*     */   public StdDateFormat()
/*     */   {
/* 124 */     this._locale = DEFAULT_LOCALE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public StdDateFormat(TimeZone tz)
/*     */   {
/* 132 */     this(tz, DEFAULT_LOCALE);
/*     */   }
/*     */   
/*     */   public StdDateFormat(TimeZone tz, Locale loc) {
/* 136 */     this._timezone = tz;
/* 137 */     this._locale = loc;
/*     */   }
/*     */   
/*     */   public static TimeZone getDefaultTimeZone() {
/* 141 */     return DEFAULT_TIMEZONE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StdDateFormat withTimeZone(TimeZone tz)
/*     */   {
/* 149 */     if (tz == null) {
/* 150 */       tz = DEFAULT_TIMEZONE;
/*     */     }
/* 152 */     if (tz.equals(this._timezone)) {
/* 153 */       return this;
/*     */     }
/* 155 */     return new StdDateFormat(tz, this._locale);
/*     */   }
/*     */   
/*     */   public StdDateFormat withLocale(Locale loc) {
/* 159 */     if (loc.equals(this._locale)) {
/* 160 */       return this;
/*     */     }
/* 162 */     return new StdDateFormat(this._timezone, loc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StdDateFormat clone()
/*     */   {
/* 170 */     return new StdDateFormat(this._timezone, this._locale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static DateFormat getBlueprintISO8601Format()
/*     */   {
/* 182 */     return DATE_FORMAT_ISO8601;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static DateFormat getISO8601Format(TimeZone tz)
/*     */   {
/* 190 */     return getISO8601Format(tz, DEFAULT_LOCALE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DateFormat getISO8601Format(TimeZone tz, Locale loc)
/*     */   {
/* 201 */     return _cloneFormat(DATE_FORMAT_ISO8601, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", tz, loc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static DateFormat getBlueprintRFC1123Format()
/*     */   {
/* 213 */     return DATE_FORMAT_RFC1123;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DateFormat getRFC1123Format(TimeZone tz, Locale loc)
/*     */   {
/* 224 */     return _cloneFormat(DATE_FORMAT_RFC1123, "EEE, dd MMM yyyy HH:mm:ss zzz", tz, loc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static DateFormat getRFC1123Format(TimeZone tz)
/*     */   {
/* 232 */     return getRFC1123Format(tz, DEFAULT_LOCALE);
/*     */   }
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
/*     */   public void setTimeZone(TimeZone tz)
/*     */   {
/* 247 */     if (!tz.equals(this._timezone)) {
/* 248 */       this._formatRFC1123 = null;
/* 249 */       this._formatISO8601 = null;
/* 250 */       this._formatISO8601_z = null;
/* 251 */       this._formatPlain = null;
/* 252 */       this._timezone = tz;
/*     */     }
/*     */   }
/*     */   
/*     */   public Date parse(String dateStr)
/*     */     throws ParseException
/*     */   {
/* 259 */     dateStr = dateStr.trim();
/* 260 */     ParsePosition pos = new ParsePosition(0);
/* 261 */     Date result = parse(dateStr, pos);
/* 262 */     if (result != null) {
/* 263 */       return result;
/*     */     }
/*     */     
/* 266 */     StringBuilder sb = new StringBuilder();
/* 267 */     for (String f : ALL_FORMATS) {
/* 268 */       if (sb.length() > 0) {
/* 269 */         sb.append("\", \"");
/*     */       } else {
/* 271 */         sb.append('"');
/*     */       }
/* 273 */       sb.append(f);
/*     */     }
/* 275 */     sb.append('"');
/* 276 */     throw new ParseException(String.format("Can not parse date \"%s\": not compatible with any of standard forms (%s)", new Object[] { dateStr, sb.toString() }), pos.getErrorIndex());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date parse(String dateStr, ParsePosition pos)
/*     */   {
/* 284 */     if (looksLikeISO8601(dateStr)) {
/* 285 */       return parseAsISO8601(dateStr, pos);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 290 */     int i = dateStr.length();
/* 291 */     for (;;) { i--; if (i < 0) break;
/* 292 */       char ch = dateStr.charAt(i);
/* 293 */       if (((ch < '0') || (ch > '9')) && (
/*     */       
/* 295 */         (i > 0) || (ch != '-'))) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 300 */     if (i < 0)
/*     */     {
/* 302 */       if ((dateStr.charAt(0) == '-') || (NumberInput.inLongRange(dateStr, false))) {
/* 303 */         return new Date(Long.parseLong(dateStr));
/*     */       }
/*     */     }
/*     */     
/* 307 */     return parseAsRFC1123(dateStr, pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition)
/*     */   {
/* 314 */     if (this._formatISO8601 == null) {
/* 315 */       this._formatISO8601 = _cloneFormat(DATE_FORMAT_ISO8601, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", this._timezone, this._locale);
/*     */     }
/* 317 */     return this._formatISO8601.format(date, toAppendTo, fieldPosition);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 328 */     String str = "DateFormat " + getClass().getName();
/* 329 */     TimeZone tz = this._timezone;
/* 330 */     if (tz != null) {
/* 331 */       str = str + " (timezone: " + tz + ")";
/*     */     }
/* 333 */     str = str + "(locale: " + this._locale + ")";
/* 334 */     return str;
/*     */   }
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
/*     */   protected boolean looksLikeISO8601(String dateStr)
/*     */   {
/* 349 */     if ((dateStr.length() >= 5) && (Character.isDigit(dateStr.charAt(0))) && (Character.isDigit(dateStr.charAt(3))) && (dateStr.charAt(4) == '-'))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 354 */       return true;
/*     */     }
/* 356 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Date parseAsISO8601(String dateStr, ParsePosition pos)
/*     */   {
/* 369 */     int len = dateStr.length();
/* 370 */     char c = dateStr.charAt(len - 1);
/*     */     
/*     */     DateFormat df;
/*     */     
/* 374 */     if ((len <= 10) && (Character.isDigit(c))) {
/* 375 */       DateFormat df = this._formatPlain;
/* 376 */       if (df == null) {
/* 377 */         df = this._formatPlain = _cloneFormat(DATE_FORMAT_PLAIN, "yyyy-MM-dd", this._timezone, this._locale);
/*     */       }
/* 379 */     } else if (c == 'Z') {
/* 380 */       DateFormat df = this._formatISO8601_z;
/* 381 */       if (df == null) {
/* 382 */         df = this._formatISO8601_z = _cloneFormat(DATE_FORMAT_ISO8601_Z, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", this._timezone, this._locale);
/*     */       }
/*     */       
/* 385 */       if (dateStr.charAt(len - 4) == ':') {
/* 386 */         StringBuilder sb = new StringBuilder(dateStr);
/* 387 */         sb.insert(len - 1, ".000");
/* 388 */         dateStr = sb.toString();
/*     */       }
/*     */       
/*     */     }
/* 392 */     else if (hasTimeZone(dateStr)) {
/* 393 */       c = dateStr.charAt(len - 3);
/* 394 */       if (c == ':')
/*     */       {
/* 396 */         StringBuilder sb = new StringBuilder(dateStr);
/* 397 */         sb.delete(len - 3, len - 2);
/* 398 */         dateStr = sb.toString();
/* 399 */       } else if ((c == '+') || (c == '-'))
/*     */       {
/* 401 */         dateStr = dateStr + "00";
/*     */       }
/*     */       
/* 404 */       len = dateStr.length();
/*     */       
/* 406 */       int timeLen = len - dateStr.lastIndexOf('T') - 6;
/* 407 */       if (timeLen < 12) {
/* 408 */         int offset = len - 5;
/* 409 */         StringBuilder sb = new StringBuilder(dateStr);
/* 410 */         switch (timeLen) {
/*     */         case 11: 
/* 412 */           sb.insert(offset, '0'); break;
/*     */         case 10: 
/* 414 */           sb.insert(offset, "00"); break;
/*     */         case 9: 
/* 416 */           sb.insert(offset, "000"); break;
/*     */         case 8: 
/* 418 */           sb.insert(offset, ".000"); break;
/*     */         case 7: 
/*     */           break;
/*     */         case 6: 
/* 422 */           sb.insert(offset, "00.000");
/*     */         case 5: 
/* 424 */           sb.insert(offset, ":00.000");
/*     */         }
/* 426 */         dateStr = sb.toString();
/*     */       }
/* 428 */       DateFormat df = this._formatISO8601;
/* 429 */       if (this._formatISO8601 == null) {
/* 430 */         df = this._formatISO8601 = _cloneFormat(DATE_FORMAT_ISO8601, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", this._timezone, this._locale);
/*     */       }
/*     */     }
/*     */     else {
/* 434 */       StringBuilder sb = new StringBuilder(dateStr);
/*     */       
/* 436 */       int timeLen = len - dateStr.lastIndexOf('T') - 1;
/* 437 */       if (timeLen < 12) {
/* 438 */         switch (timeLen) {
/* 439 */         case 11:  sb.append('0');
/* 440 */         case 10:  sb.append('0');
/* 441 */         case 9:  sb.append('0');
/* 442 */           break;
/*     */         default: 
/* 444 */           sb.append(".000");
/*     */         }
/*     */       }
/* 447 */       sb.append('Z');
/* 448 */       dateStr = sb.toString();
/* 449 */       df = this._formatISO8601_z;
/* 450 */       if (df == null) {
/* 451 */         df = this._formatISO8601_z = _cloneFormat(DATE_FORMAT_ISO8601_Z, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", this._timezone, this._locale);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 456 */     return df.parse(dateStr, pos);
/*     */   }
/*     */   
/*     */   protected Date parseAsRFC1123(String dateStr, ParsePosition pos)
/*     */   {
/* 461 */     if (this._formatRFC1123 == null) {
/* 462 */       this._formatRFC1123 = _cloneFormat(DATE_FORMAT_RFC1123, "EEE, dd MMM yyyy HH:mm:ss zzz", this._timezone, this._locale);
/*     */     }
/* 464 */     return this._formatRFC1123.parse(dateStr, pos);
/*     */   }
/*     */   
/*     */ 
/*     */   private static final boolean hasTimeZone(String str)
/*     */   {
/* 470 */     int len = str.length();
/* 471 */     if (len >= 6) {
/* 472 */       char c = str.charAt(len - 6);
/* 473 */       if ((c == '+') || (c == '-')) return true;
/* 474 */       c = str.charAt(len - 5);
/* 475 */       if ((c == '+') || (c == '-')) return true;
/* 476 */       c = str.charAt(len - 3);
/* 477 */       if ((c == '+') || (c == '-')) return true;
/*     */     }
/* 479 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private static final DateFormat _cloneFormat(DateFormat df, String format, TimeZone tz, Locale loc)
/*     */   {
/* 485 */     if (!loc.equals(DEFAULT_LOCALE)) {
/* 486 */       df = new SimpleDateFormat(format, loc);
/* 487 */       df.setTimeZone(tz == null ? DEFAULT_TIMEZONE : tz);
/*     */     } else {
/* 489 */       df = (DateFormat)df.clone();
/* 490 */       if (tz != null) {
/* 491 */         df.setTimeZone(tz);
/*     */       }
/*     */     }
/* 494 */     return df;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\StdDateFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */