/*     */ package oshi.util;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.time.LocalDate;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.DateTimeParseException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class FormatUtil
/*     */ {
/*  36 */   private static Logger LOG = LoggerFactory.getLogger(FormatUtil.class);
/*     */   
/*     */   private static final long KIBI = 1024L;
/*     */   
/*     */   private static final long MEBI = 1048576L;
/*     */   
/*     */   private static final long GIBI = 1073741824L;
/*     */   
/*     */   private static final long TEBI = 1099511627776L;
/*     */   
/*     */   private static final long PEBI = 1125899906842624L;
/*     */   
/*     */   private static final long EXBI = 1152921504606846976L;
/*     */   
/*     */   private static final long KILO = 1000L;
/*     */   
/*     */   private static final long MEGA = 1000000L;
/*     */   
/*     */   private static final long GIGA = 1000000000L;
/*     */   
/*     */   private static final long TERA = 1000000000000L;
/*     */   
/*     */   private static final long PETA = 1000000000000000L;
/*     */   
/*     */   private static final long EXA = 1000000000000000000L;
/*     */   
/*  62 */   private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");
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
/*     */   public static String formatBytes(long bytes)
/*     */   {
/*  79 */     if (bytes == 1L)
/*  80 */       return String.format("%d byte", new Object[] { Long.valueOf(bytes) });
/*  81 */     if (bytes < 1024L)
/*  82 */       return String.format("%d bytes", new Object[] { Long.valueOf(bytes) });
/*  83 */     if (bytes < 1048576L)
/*  84 */       return formatUnits(bytes, 1024L, "KiB");
/*  85 */     if (bytes < 1073741824L)
/*  86 */       return formatUnits(bytes, 1048576L, "MiB");
/*  87 */     if (bytes < 1099511627776L)
/*  88 */       return formatUnits(bytes, 1073741824L, "GiB");
/*  89 */     if (bytes < 1125899906842624L)
/*  90 */       return formatUnits(bytes, 1099511627776L, "TiB");
/*  91 */     if (bytes < 1152921504606846976L) {
/*  92 */       return formatUnits(bytes, 1125899906842624L, "PiB");
/*     */     }
/*  94 */     return formatUnits(bytes, 1152921504606846976L, "EiB");
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
/*     */   private static String formatUnits(long value, long prefix, String unit)
/*     */   {
/* 111 */     if (value % prefix == 0L) {
/* 112 */       return String.format("%d %s", new Object[] { Long.valueOf(value / prefix), unit });
/*     */     }
/* 114 */     return String.format("%.1f %s", new Object[] { Double.valueOf(value / prefix), unit });
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
/*     */   public static String formatBytesDecimal(long bytes)
/*     */   {
/* 127 */     if (bytes == 1L)
/* 128 */       return String.format("%d byte", new Object[] { Long.valueOf(bytes) });
/* 129 */     if (bytes < 1000L) {
/* 130 */       return String.format("%d bytes", new Object[] { Long.valueOf(bytes) });
/*     */     }
/* 132 */     return formatValue(bytes, "B");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String formatHertz(long hertz)
/*     */   {
/* 144 */     return formatValue(hertz, "Hz");
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
/*     */   public static String formatValue(long value, String unit)
/*     */   {
/* 158 */     if (value < 1000L)
/* 159 */       return String.format("%d %s", new Object[] { Long.valueOf(value), unit });
/* 160 */     if (value < 1000000L)
/* 161 */       return formatUnits(value, 1000L, "K" + unit);
/* 162 */     if (value < 1000000000L)
/* 163 */       return formatUnits(value, 1000000L, "M" + unit);
/* 164 */     if (value < 1000000000000L)
/* 165 */       return formatUnits(value, 1000000000L, "G" + unit);
/* 166 */     if (value < 1000000000000000L)
/* 167 */       return formatUnits(value, 1000000000000L, "T" + unit);
/* 168 */     if (value < 1000000000000000000L) {
/* 169 */       return formatUnits(value, 1000000000000000L, "P" + unit);
/*     */     }
/* 171 */     return formatUnits(value, 1000000000000000000L, "E" + unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String formatElapsedSecs(long secs)
/*     */   {
/* 183 */     long eTime = secs;
/* 184 */     long days = TimeUnit.SECONDS.toDays(eTime);
/* 185 */     eTime -= TimeUnit.DAYS.toSeconds(days);
/* 186 */     long hr = TimeUnit.SECONDS.toHours(eTime);
/* 187 */     eTime -= TimeUnit.HOURS.toSeconds(hr);
/* 188 */     long min = TimeUnit.SECONDS.toMinutes(eTime);
/* 189 */     eTime -= TimeUnit.MINUTES.toSeconds(min);
/* 190 */     long sec = eTime;
/* 191 */     return String.format("%d days, %02d:%02d:%02d", new Object[] { Long.valueOf(days), Long.valueOf(hr), Long.valueOf(min), Long.valueOf(sec) });
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
/*     */   public static float round(float d, int decimalPlace)
/*     */   {
/* 204 */     BigDecimal bd = new BigDecimal(Float.toString(d)).setScale(decimalPlace, 4);
/* 205 */     return bd.floatValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static long getUnsignedInt(int x)
/*     */   {
/* 216 */     return x & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String formatDate(LocalDate date)
/*     */   {
/* 227 */     return date == null ? "null" : date.format(DATE_FORMATTER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static LocalDate formatStringDate(String date)
/*     */   {
/*     */     try
/*     */     {
/* 239 */       return date == null ? null : LocalDate.parse(date, DATE_FORMATTER);
/*     */     } catch (DateTimeParseException dtpe) {
/* 241 */       LOG.warn("Date parse error: " + dtpe); }
/* 242 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\util\FormatUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */