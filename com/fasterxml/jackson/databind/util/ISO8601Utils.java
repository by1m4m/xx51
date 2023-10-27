/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
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
/*     */ public class ISO8601Utils
/*     */ {
/*     */   private static final String GMT_ID = "GMT";
/*  25 */   private static final TimeZone TIMEZONE_GMT = TimeZone.getTimeZone("GMT");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static TimeZone timeZoneGMT()
/*     */   {
/*  37 */     return TIMEZONE_GMT;
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
/*     */ 
/*     */   public static String format(Date date)
/*     */   {
/*  53 */     return format(date, false, TIMEZONE_GMT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String format(Date date, boolean millis)
/*     */   {
/*  64 */     return format(date, millis, TIMEZONE_GMT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String format(Date date, boolean millis, TimeZone tz)
/*     */   {
/*  76 */     Calendar calendar = new GregorianCalendar(tz, Locale.US);
/*  77 */     calendar.setTime(date);
/*     */     
/*     */ 
/*  80 */     int capacity = "yyyy-MM-ddThh:mm:ss".length();
/*  81 */     capacity += (millis ? ".sss".length() : 0);
/*  82 */     capacity += (tz.getRawOffset() == 0 ? "Z".length() : "+hh:mm".length());
/*  83 */     StringBuilder formatted = new StringBuilder(capacity);
/*     */     
/*  85 */     padInt(formatted, calendar.get(1), "yyyy".length());
/*  86 */     formatted.append('-');
/*  87 */     padInt(formatted, calendar.get(2) + 1, "MM".length());
/*  88 */     formatted.append('-');
/*  89 */     padInt(formatted, calendar.get(5), "dd".length());
/*  90 */     formatted.append('T');
/*  91 */     padInt(formatted, calendar.get(11), "hh".length());
/*  92 */     formatted.append(':');
/*  93 */     padInt(formatted, calendar.get(12), "mm".length());
/*  94 */     formatted.append(':');
/*  95 */     padInt(formatted, calendar.get(13), "ss".length());
/*  96 */     if (millis) {
/*  97 */       formatted.append('.');
/*  98 */       padInt(formatted, calendar.get(14), "sss".length());
/*     */     }
/*     */     
/* 101 */     int offset = tz.getOffset(calendar.getTimeInMillis());
/* 102 */     if (offset != 0) {
/* 103 */       int hours = Math.abs(offset / 60000 / 60);
/* 104 */       int minutes = Math.abs(offset / 60000 % 60);
/* 105 */       formatted.append(offset < 0 ? '-' : '+');
/* 106 */       padInt(formatted, hours, "hh".length());
/* 107 */       formatted.append(':');
/* 108 */       padInt(formatted, minutes, "mm".length());
/*     */     } else {
/* 110 */       formatted.append('Z');
/*     */     }
/*     */     
/* 113 */     return formatted.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Date parse(String date, ParsePosition pos)
/*     */     throws ParseException
/*     */   {
/* 132 */     Exception fail = null;
/*     */     try {
/* 134 */       int offset = pos.getIndex();
/*     */       
/*     */ 
/* 137 */       int year = parseInt(date, , offset);
/* 138 */       if (checkOffset(date, offset, '-')) {
/* 139 */         offset++;
/*     */       }
/*     */       
/*     */ 
/* 143 */       int month = parseInt(date, , offset);
/* 144 */       if (checkOffset(date, offset, '-')) {
/* 145 */         offset++;
/*     */       }
/*     */       
/*     */ 
/* 149 */       int day = parseInt(date, , offset);
/*     */       
/* 151 */       int hour = 0;
/* 152 */       int minutes = 0;
/* 153 */       int seconds = 0;
/* 154 */       int milliseconds = 0;
/* 155 */       if (checkOffset(date, offset, 'T'))
/*     */       {
/*     */ 
/* 158 */         offset += 2;hour = parseInt(date, ++offset, offset);
/* 159 */         if (checkOffset(date, offset, ':')) {
/* 160 */           offset++;
/*     */         }
/*     */         
/* 163 */         minutes = parseInt(date, , offset);
/* 164 */         if (checkOffset(date, offset, ':')) {
/* 165 */           offset++;
/*     */         }
/*     */         
/* 168 */         if (date.length() > offset) {
/* 169 */           char c = date.charAt(offset);
/* 170 */           if ((c != 'Z') && (c != '+') && (c != '-')) {
/* 171 */             seconds = parseInt(date, , offset);
/*     */             
/* 173 */             if (checkOffset(date, offset, '.')) {
/* 174 */               offset += 3;milliseconds = parseInt(date, ++offset, offset);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 182 */       if (date.length() <= offset) {
/* 183 */         throw new IllegalArgumentException("No time zone indicator");
/*     */       }
/* 185 */       char timezoneIndicator = date.charAt(offset);
/* 186 */       if ((timezoneIndicator == '+') || (timezoneIndicator == '-')) {
/* 187 */         String timezoneOffset = date.substring(offset);
/* 188 */         String timezoneId = "GMT" + timezoneOffset;
/* 189 */         offset += timezoneOffset.length();
/* 190 */       } else if (timezoneIndicator == 'Z') {
/* 191 */         String timezoneId = "GMT";
/* 192 */         offset++;
/*     */       } else {
/* 194 */         throw new IndexOutOfBoundsException("Invalid time zone indicator " + timezoneIndicator);
/*     */       }
/*     */       String timezoneId;
/* 197 */       TimeZone timezone = TimeZone.getTimeZone(timezoneId);
/* 198 */       String act = timezone.getID();
/* 199 */       if (!act.equals(timezoneId))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 205 */         String cleaned = act.replace(":", "");
/* 206 */         if (!cleaned.equals(timezoneId)) {
/* 207 */           throw new IndexOutOfBoundsException("Mismatching time zone indicator: " + timezoneId + " given, resolves to " + timezone.getID());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 212 */       Calendar calendar = new GregorianCalendar(timezone);
/* 213 */       calendar.setLenient(false);
/* 214 */       calendar.set(1, year);
/* 215 */       calendar.set(2, month - 1);
/* 216 */       calendar.set(5, day);
/* 217 */       calendar.set(11, hour);
/* 218 */       calendar.set(12, minutes);
/* 219 */       calendar.set(13, seconds);
/* 220 */       calendar.set(14, milliseconds);
/*     */       
/* 222 */       pos.setIndex(offset);
/* 223 */       return calendar.getTime();
/*     */     }
/*     */     catch (IndexOutOfBoundsException e)
/*     */     {
/* 227 */       fail = e;
/*     */     } catch (NumberFormatException e) {
/* 229 */       fail = e;
/*     */     } catch (IllegalArgumentException e) {
/* 231 */       fail = e;
/*     */     }
/* 233 */     String input = '"' + date + "'";
/* 234 */     String msg = fail.getMessage();
/* 235 */     if ((msg == null) || (msg.isEmpty())) {
/* 236 */       msg = "(" + fail.getClass().getName() + ")";
/*     */     }
/* 238 */     ParseException ex = new ParseException("Failed to parse date [" + input + "]: " + msg, pos.getIndex());
/* 239 */     ex.initCause(fail);
/* 240 */     throw ex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean checkOffset(String value, int offset, char expected)
/*     */   {
/* 252 */     return (offset < value.length()) && (value.charAt(offset) == expected);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int parseInt(String value, int beginIndex, int endIndex)
/*     */     throws NumberFormatException
/*     */   {
/* 265 */     if ((beginIndex < 0) || (endIndex > value.length()) || (beginIndex > endIndex)) {
/* 266 */       throw new NumberFormatException(value);
/*     */     }
/*     */     
/* 269 */     int i = beginIndex;
/* 270 */     int result = 0;
/*     */     
/* 272 */     if (i < endIndex) {
/* 273 */       int digit = Character.digit(value.charAt(i++), 10);
/* 274 */       if (digit < 0) {
/* 275 */         throw new NumberFormatException("Invalid number: " + value);
/*     */       }
/* 277 */       result = -digit;
/*     */     }
/* 279 */     while (i < endIndex) {
/* 280 */       int digit = Character.digit(value.charAt(i++), 10);
/* 281 */       if (digit < 0) {
/* 282 */         throw new NumberFormatException("Invalid number: " + value);
/*     */       }
/* 284 */       result *= 10;
/* 285 */       result -= digit;
/*     */     }
/* 287 */     return -result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void padInt(StringBuilder buffer, int value, int length)
/*     */   {
/* 298 */     String strValue = Integer.toString(value);
/* 299 */     for (int i = length - strValue.length(); i > 0; i--) {
/* 300 */       buffer.append('0');
/*     */     }
/* 302 */     buffer.append(strValue);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\ISO8601Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */