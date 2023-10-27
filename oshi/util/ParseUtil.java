/*     */ package oshi.util;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.time.LocalTime;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.DateTimeParseException;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class ParseUtil
/*     */ {
/*  42 */   private static final Logger LOG = LoggerFactory.getLogger(ParseUtil.class);
/*     */   
/*     */ 
/*     */   private static final String DEFAULT_LOG_MSG = "{} didn't parse. Returning default. {}";
/*     */   
/*     */ 
/*  48 */   private static final Pattern HERTZ_PATTERN = Pattern.compile("(\\d+(.\\d+)?) ?([kMGT]?Hz).*");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  53 */   private static final Pattern VALID_HEX = Pattern.compile("[0-9a-fA-F]+");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  59 */   private static final DateTimeFormatter CIM_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss.SSSSSSZZZZZ", Locale.US);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */   private static final Pattern DHMS = Pattern.compile("(?:(\\d+)-)?(?:(\\d+):)?(\\d+):(\\d+)(?:\\.(\\d+))?");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private static final Pattern UUID_PATTERN = Pattern.compile(".*([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}).*");
/*     */   
/*     */ 
/*     */   private static final String HZ = "Hz";
/*     */   
/*     */   private static final String KHZ = "kHz";
/*     */   
/*     */   private static final String MHZ = "MHz";
/*     */   
/*     */   private static final String GHZ = "GHz";
/*     */   
/*     */   private static final String THZ = "THz";
/*     */   
/*     */   private static final String PHZ = "PHz";
/*     */   
/*  86 */   private static final Map<String, Long> multipliers = new HashMap();
/*  87 */   static { multipliers.put("Hz", Long.valueOf(1L));
/*  88 */     multipliers.put("kHz", Long.valueOf(1000L));
/*  89 */     multipliers.put("MHz", Long.valueOf(1000000L));
/*  90 */     multipliers.put("GHz", Long.valueOf(1000000000L));
/*  91 */     multipliers.put("THz", Long.valueOf(1000000000000L));
/*  92 */     multipliers.put("PHz", Long.valueOf(1000000000000000L));
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
/*     */   public static long parseHertz(String hertz)
/*     */   {
/* 106 */     Matcher matcher = HERTZ_PATTERN.matcher(hertz.trim());
/* 107 */     if ((matcher.find()) && (matcher.groupCount() == 3))
/*     */     {
/* 109 */       Double value = Double.valueOf(Double.valueOf(matcher.group(1)).doubleValue() * ((Long)multipliers.getOrDefault(matcher.group(3), Long.valueOf(-1L))).longValue());
/* 110 */       return value.doubleValue() < 0.0D ? -1L : value.longValue();
/*     */     }
/* 112 */     return -1L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int parseLastInt(String s, int i)
/*     */   {
/*     */     try
/*     */     {
/* 126 */       return Integer.parseInt(parseLastString(s));
/*     */     } catch (NumberFormatException e) {
/* 128 */       LOG.trace("{} didn't parse. Returning default. {}", s, e); }
/* 129 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String parseLastString(String s)
/*     */   {
/* 141 */     String[] ss = s.split("\\s+");
/* 142 */     if (ss.length < 1) {
/* 143 */       return s;
/*     */     }
/* 145 */     return ss[(ss.length - 1)];
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
/*     */   public static byte[] hexStringToByteArray(String digits)
/*     */   {
/* 158 */     int len = digits.length();
/*     */     
/* 160 */     if ((!VALID_HEX.matcher(digits).matches()) || ((len & 0x1) != 0)) {
/* 161 */       LOG.warn("Invalid hexadecimal string: {}", digits);
/* 162 */       return new byte[0];
/*     */     }
/* 164 */     byte[] data = new byte[len / 2];
/* 165 */     for (int i = 0; i < len; i += 2)
/*     */     {
/* 167 */       data[(i / 2)] = ((byte)(Character.digit(digits.charAt(i), 16) << 4 | Character.digit(digits.charAt(i + 1), 16)));
/*     */     }
/* 169 */     return data;
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
/*     */   public static byte[] stringToByteArray(String text, int length)
/*     */   {
/* 185 */     return Arrays.copyOf(text.getBytes(), length);
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
/*     */   public static byte[] longToByteArray(long value, int valueSize, int length)
/*     */   {
/* 202 */     long val = value;
/*     */     
/* 204 */     byte[] b = new byte[8];
/* 205 */     for (int i = 7; (i >= 0) && (val != 0L); i--) {
/* 206 */       b[i] = ((byte)(int)val);
/* 207 */       val >>>= 8;
/*     */     }
/*     */     
/*     */ 
/* 211 */     return Arrays.copyOfRange(b, 8 - valueSize, 8 + length - valueSize);
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
/*     */   public static long strToLong(String str, int size)
/*     */   {
/* 225 */     return byteArrayToLong(str.getBytes(), size);
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
/*     */   public static long byteArrayToLong(byte[] bytes, int size)
/*     */   {
/* 238 */     if (size > 8) {
/* 239 */       throw new IllegalArgumentException("Can't convert more than 8 bytes.");
/*     */     }
/* 241 */     if (size > bytes.length) {
/* 242 */       throw new IllegalArgumentException("Size can't be larger than array length.");
/*     */     }
/* 244 */     long total = 0L;
/* 245 */     for (int i = 0; i < size; i++) {
/* 246 */       total = total << 8 | bytes[i] & 0xFF;
/*     */     }
/* 248 */     return total;
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
/*     */   public static float byteArrayToFloat(byte[] bytes, int size, int fpBits)
/*     */   {
/* 265 */     return (float)byteArrayToLong(bytes, size) / (1 << fpBits);
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
/*     */   public static long cimDateTimeToMillis(String cimDate)
/*     */   {
/*     */     try
/*     */     {
/* 281 */       int tzInMinutes = Integer.parseInt(cimDate.substring(22));
/* 282 */       LocalTime offsetAsLocalTime = LocalTime.MIN.plusMinutes(tzInMinutes);
/* 283 */       OffsetDateTime dateTime = OffsetDateTime.parse(cimDate
/* 284 */         .substring(0, 22) + offsetAsLocalTime.format(DateTimeFormatter.ISO_LOCAL_TIME), CIM_FORMAT);
/* 285 */       return dateTime.toInstant().toEpochMilli();
/*     */     }
/*     */     catch (IndexOutOfBoundsException|NumberFormatException|DateTimeParseException e)
/*     */     {
/* 289 */       LOG.trace("{} didn't parse. Returning default. {}", cimDate, e); }
/* 290 */     return 0L;
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
/*     */   public static String hexStringToString(String hexString)
/*     */   {
/* 305 */     if (hexString.length() % 2 > 0) {
/* 306 */       return hexString;
/*     */     }
/*     */     
/* 309 */     StringBuilder sb = new StringBuilder();
/*     */     try {
/* 311 */       for (int pos = 0; pos < hexString.length(); pos += 2) {
/* 312 */         int charAsInt = Integer.parseInt(hexString.substring(pos, pos + 2), 16);
/* 313 */         if ((charAsInt < 32) || (charAsInt > 127)) {
/* 314 */           return hexString;
/*     */         }
/* 316 */         sb.append((char)charAsInt);
/*     */       }
/*     */     } catch (NumberFormatException e) {
/* 319 */       LOG.trace("{} didn't parse. Returning default. {}", hexString, e);
/*     */       
/* 321 */       return hexString;
/*     */     }
/* 323 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int parseIntOrDefault(String s, int defaultInt)
/*     */   {
/*     */     try
/*     */     {
/* 337 */       return Integer.parseInt(s);
/*     */     } catch (NumberFormatException e) {
/* 339 */       LOG.trace("{} didn't parse. Returning default. {}", s, e); }
/* 340 */     return defaultInt;
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
/*     */   public static long parseLongOrDefault(String s, long defaultLong)
/*     */   {
/*     */     try
/*     */     {
/* 355 */       return Long.parseLong(s);
/*     */     } catch (NumberFormatException e) {
/* 357 */       LOG.trace("{} didn't parse. Returning default. {}", s, e); }
/* 358 */     return defaultLong;
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
/*     */   public static double parseDoubleOrDefault(String s, double defaultDouble)
/*     */   {
/*     */     try
/*     */     {
/* 373 */       return Double.parseDouble(s);
/*     */     } catch (NumberFormatException e) {
/* 375 */       LOG.trace("{} didn't parse. Returning default. {}", s, e); }
/* 376 */     return defaultDouble;
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
/*     */   public static long parseDHMSOrDefault(String s, long defaultLong)
/*     */   {
/* 391 */     Matcher m = DHMS.matcher(s);
/* 392 */     if (m.matches()) {
/* 393 */       long milliseconds = 0L;
/* 394 */       if (m.group(1) != null) {
/* 395 */         milliseconds += parseLongOrDefault(m.group(1), 0L) * 86400000L;
/*     */       }
/* 397 */       if (m.group(2) != null) {
/* 398 */         milliseconds += parseLongOrDefault(m.group(2), 0L) * 3600000L;
/*     */       }
/* 400 */       milliseconds += parseLongOrDefault(m.group(3), 0L) * 60000L;
/* 401 */       milliseconds += parseLongOrDefault(m.group(4), 0L) * 1000L;
/* 402 */       milliseconds = (milliseconds + 1000.0D * parseDoubleOrDefault("0." + m.group(5), 0.0D));
/* 403 */       return milliseconds;
/*     */     }
/* 405 */     return defaultLong;
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
/*     */   public static String parseUuidOrDefault(String s, String defaultStr)
/*     */   {
/* 418 */     Matcher m = UUID_PATTERN.matcher(s.toLowerCase());
/* 419 */     if (m.matches()) {
/* 420 */       return m.group(1);
/*     */     }
/* 422 */     return defaultStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getSingleQuoteStringValue(String line)
/*     */   {
/* 433 */     String[] split = line.split("'");
/* 434 */     if (split.length < 2) {
/* 435 */       return "";
/*     */     }
/* 437 */     return split[1];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int getFirstIntValue(String line)
/*     */   {
/* 449 */     return getNthIntValue(line, 1);
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
/*     */   public static int getNthIntValue(String line, int n)
/*     */   {
/* 462 */     String[] split = line.replaceFirst("^[^0-9]*", "").split("[^0-9]+");
/* 463 */     if (split.length >= n) {
/* 464 */       return parseIntOrDefault(split[(n - 1)], 0);
/*     */     }
/* 466 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\util\ParseUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */