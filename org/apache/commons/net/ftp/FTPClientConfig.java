/*     */ package org.apache.commons.net.ftp;
/*     */ 
/*     */ import java.text.DateFormatSymbols;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.TreeMap;
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
/*     */ public class FTPClientConfig
/*     */ {
/*     */   public static final String SYST_UNIX = "UNIX";
/*     */   public static final String SYST_UNIX_TRIM_LEADING = "UNIX_LTRIM";
/*     */   public static final String SYST_VMS = "VMS";
/*     */   public static final String SYST_NT = "WINDOWS";
/*     */   public static final String SYST_OS2 = "OS/2";
/*     */   public static final String SYST_OS400 = "OS/400";
/*     */   public static final String SYST_AS400 = "AS/400";
/*     */   public static final String SYST_MVS = "MVS";
/*     */   public static final String SYST_L8 = "TYPE: L8";
/*     */   public static final String SYST_NETWARE = "NETWARE";
/*     */   public static final String SYST_MACOS_PETER = "MACOS PETER";
/*     */   private final String serverSystemKey;
/* 223 */   private String defaultDateFormatStr = null;
/* 224 */   private String recentDateFormatStr = null;
/* 225 */   private boolean lenientFutureDates = true;
/* 226 */   private String serverLanguageCode = null;
/* 227 */   private String shortMonthNames = null;
/* 228 */   private String serverTimeZoneId = null;
/* 229 */   private boolean saveUnparseableEntries = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FTPClientConfig(String systemKey)
/*     */   {
/* 241 */     this.serverSystemKey = systemKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FTPClientConfig()
/*     */   {
/* 249 */     this("UNIX");
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
/*     */   public FTPClientConfig(String systemKey, String defaultDateFormatStr, String recentDateFormatStr)
/*     */   {
/* 267 */     this(systemKey);
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
/*     */   public FTPClientConfig(String systemKey, String defaultDateFormatStr, String recentDateFormatStr, String serverLanguageCode, String shortMonthNames, String serverTimeZoneId)
/*     */   {
/* 295 */     this(systemKey);
/*     */     
/*     */ 
/* 298 */     this.serverLanguageCode = serverLanguageCode;
/* 299 */     this.shortMonthNames = shortMonthNames;
/* 300 */     this.serverTimeZoneId = serverTimeZoneId;
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
/*     */   public FTPClientConfig(String systemKey, String defaultDateFormatStr, String recentDateFormatStr, String serverLanguageCode, String shortMonthNames, String serverTimeZoneId, boolean lenientFutureDates, boolean saveUnparseableEntries)
/*     */   {
/* 332 */     this(systemKey);
/*     */     
/* 334 */     this.lenientFutureDates = lenientFutureDates;
/*     */     
/* 336 */     this.saveUnparseableEntries = saveUnparseableEntries;
/* 337 */     this.serverLanguageCode = serverLanguageCode;
/* 338 */     this.shortMonthNames = shortMonthNames;
/* 339 */     this.serverTimeZoneId = serverTimeZoneId;
/*     */   }
/*     */   
/*     */   FTPClientConfig(String systemKey, FTPClientConfig config)
/*     */   {
/* 344 */     this.serverSystemKey = systemKey;
/* 345 */     this.defaultDateFormatStr = config.defaultDateFormatStr;
/* 346 */     this.lenientFutureDates = config.lenientFutureDates;
/* 347 */     this.recentDateFormatStr = config.recentDateFormatStr;
/* 348 */     this.saveUnparseableEntries = config.saveUnparseableEntries;
/* 349 */     this.serverLanguageCode = config.serverLanguageCode;
/* 350 */     this.serverTimeZoneId = config.serverTimeZoneId;
/* 351 */     this.shortMonthNames = config.shortMonthNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FTPClientConfig(FTPClientConfig config)
/*     */   {
/* 360 */     this.serverSystemKey = config.serverSystemKey;
/* 361 */     this.defaultDateFormatStr = config.defaultDateFormatStr;
/* 362 */     this.lenientFutureDates = config.lenientFutureDates;
/* 363 */     this.recentDateFormatStr = config.recentDateFormatStr;
/* 364 */     this.saveUnparseableEntries = config.saveUnparseableEntries;
/* 365 */     this.serverLanguageCode = config.serverLanguageCode;
/* 366 */     this.serverTimeZoneId = config.serverTimeZoneId;
/* 367 */     this.shortMonthNames = config.shortMonthNames;
/*     */   }
/*     */   
/* 370 */   private static final Map<String, Object> LANGUAGE_CODE_MAP = new TreeMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/* 380 */     LANGUAGE_CODE_MAP.put("en", Locale.ENGLISH);
/* 381 */     LANGUAGE_CODE_MAP.put("de", Locale.GERMAN);
/* 382 */     LANGUAGE_CODE_MAP.put("it", Locale.ITALIAN);
/* 383 */     LANGUAGE_CODE_MAP.put("es", new Locale("es", "", ""));
/* 384 */     LANGUAGE_CODE_MAP.put("pt", new Locale("pt", "", ""));
/* 385 */     LANGUAGE_CODE_MAP.put("da", new Locale("da", "", ""));
/* 386 */     LANGUAGE_CODE_MAP.put("sv", new Locale("sv", "", ""));
/* 387 */     LANGUAGE_CODE_MAP.put("no", new Locale("no", "", ""));
/* 388 */     LANGUAGE_CODE_MAP.put("nl", new Locale("nl", "", ""));
/* 389 */     LANGUAGE_CODE_MAP.put("ro", new Locale("ro", "", ""));
/* 390 */     LANGUAGE_CODE_MAP.put("sq", new Locale("sq", "", ""));
/* 391 */     LANGUAGE_CODE_MAP.put("sh", new Locale("sh", "", ""));
/* 392 */     LANGUAGE_CODE_MAP.put("sk", new Locale("sk", "", ""));
/* 393 */     LANGUAGE_CODE_MAP.put("sl", new Locale("sl", "", ""));
/*     */     
/*     */ 
/*     */ 
/* 397 */     LANGUAGE_CODE_MAP.put("fr", "jan|fév|mar|avr|mai|jun|jui|aoû|sep|oct|nov|déc");
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
/*     */   public String getServerSystemKey()
/*     */   {
/* 412 */     return this.serverSystemKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDefaultDateFormatStr()
/*     */   {
/* 421 */     return this.defaultDateFormatStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRecentDateFormatStr()
/*     */   {
/* 430 */     return this.recentDateFormatStr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServerTimeZoneId()
/*     */   {
/* 438 */     return this.serverTimeZoneId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getShortMonthNames()
/*     */   {
/* 449 */     return this.shortMonthNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServerLanguageCode()
/*     */   {
/* 459 */     return this.serverLanguageCode;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLenientFutureDates()
/*     */   {
/* 470 */     return this.lenientFutureDates;
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
/*     */   public void setDefaultDateFormatStr(String defaultDateFormatStr)
/*     */   {
/* 487 */     this.defaultDateFormatStr = defaultDateFormatStr;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRecentDateFormatStr(String recentDateFormatStr)
/*     */   {
/* 508 */     this.recentDateFormatStr = recentDateFormatStr;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLenientFutureDates(boolean lenientFutureDates)
/*     */   {
/* 532 */     this.lenientFutureDates = lenientFutureDates;
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
/*     */   public void setServerTimeZoneId(String serverTimeZoneId)
/*     */   {
/* 549 */     this.serverTimeZoneId = serverTimeZoneId;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setShortMonthNames(String shortMonthNames)
/*     */   {
/* 570 */     this.shortMonthNames = shortMonthNames;
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
/*     */   public void setServerLanguageCode(String serverLanguageCode)
/*     */   {
/* 614 */     this.serverLanguageCode = serverLanguageCode;
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
/*     */   public static DateFormatSymbols lookupDateFormatSymbols(String languageCode)
/*     */   {
/* 631 */     Object lang = LANGUAGE_CODE_MAP.get(languageCode);
/* 632 */     if (lang != null) {
/* 633 */       if ((lang instanceof Locale))
/* 634 */         return new DateFormatSymbols((Locale)lang);
/* 635 */       if ((lang instanceof String)) {
/* 636 */         return getDateFormatSymbols((String)lang);
/*     */       }
/*     */     }
/* 639 */     return new DateFormatSymbols(Locale.US);
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
/*     */   public static DateFormatSymbols getDateFormatSymbols(String shortmonths)
/*     */   {
/* 652 */     String[] months = splitShortMonthString(shortmonths);
/* 653 */     DateFormatSymbols dfs = new DateFormatSymbols(Locale.US);
/* 654 */     dfs.setShortMonths(months);
/* 655 */     return dfs;
/*     */   }
/*     */   
/*     */   private static String[] splitShortMonthString(String shortmonths) {
/* 659 */     StringTokenizer st = new StringTokenizer(shortmonths, "|");
/* 660 */     int monthcnt = st.countTokens();
/* 661 */     if (12 != monthcnt) {
/* 662 */       throw new IllegalArgumentException("expecting a pipe-delimited string containing 12 tokens");
/*     */     }
/*     */     
/* 665 */     String[] months = new String[13];
/* 666 */     int pos = 0;
/* 667 */     while (st.hasMoreTokens()) {
/* 668 */       months[(pos++)] = st.nextToken();
/*     */     }
/* 670 */     months[pos] = "";
/* 671 */     return months;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Collection<String> getSupportedLanguageCodes()
/*     */   {
/* 683 */     return LANGUAGE_CODE_MAP.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUnparseableEntries(boolean saveUnparseable)
/*     */   {
/* 695 */     this.saveUnparseableEntries = saveUnparseable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean getUnparseableEntries()
/*     */   {
/* 706 */     return this.saveUnparseableEntries;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\ftp\FTPClientConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */