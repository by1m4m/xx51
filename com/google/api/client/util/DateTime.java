/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DateTime
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  41 */   private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
/*     */   
/*     */ 
/*  44 */   private static final Pattern RFC3339_PATTERN = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})([Tt](\\d{2}):(\\d{2}):(\\d{2})(\\.\\d+)?)?([Zz]|([+-])(\\d{2}):(\\d{2}))?");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final long value;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean dateOnly;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int tzShift;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateTime(Date date, TimeZone zone)
/*     */   {
/*  72 */     this(false, date.getTime(), zone == null ? null : Integer.valueOf(zone.getOffset(date.getTime()) / 60000));
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
/*     */   public DateTime(long value)
/*     */   {
/*  86 */     this(false, value, null);
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
/*     */   public DateTime(Date value)
/*     */   {
/* 100 */     this(value.getTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateTime(long value, int tzShift)
/*     */   {
/* 111 */     this(false, value, Integer.valueOf(tzShift));
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
/*     */   public DateTime(boolean dateOnly, long value, Integer tzShift)
/*     */   {
/* 124 */     this.dateOnly = dateOnly;
/* 125 */     this.value = value;
/* 126 */     this.tzShift = (tzShift == null ? TimeZone.getDefault().getOffset(value) / 60000 : dateOnly ? 0 : tzShift.intValue());
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
/*     */   public DateTime(String value)
/*     */   {
/* 150 */     DateTime dateTime = parseRfc3339(value);
/* 151 */     this.dateOnly = dateTime.dateOnly;
/* 152 */     this.value = dateTime.value;
/* 153 */     this.tzShift = dateTime.tzShift;
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
/*     */   public long getValue()
/*     */   {
/* 167 */     return this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDateOnly()
/*     */   {
/* 176 */     return this.dateOnly;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getTimeZoneShift()
/*     */   {
/* 185 */     return this.tzShift;
/*     */   }
/*     */   
/*     */   public String toStringRfc3339()
/*     */   {
/* 190 */     StringBuilder sb = new StringBuilder();
/* 191 */     Calendar dateTime = new GregorianCalendar(GMT);
/* 192 */     long localTime = this.value + this.tzShift * 60000L;
/* 193 */     dateTime.setTimeInMillis(localTime);
/*     */     
/* 195 */     appendInt(sb, dateTime.get(1), 4);
/* 196 */     sb.append('-');
/* 197 */     appendInt(sb, dateTime.get(2) + 1, 2);
/* 198 */     sb.append('-');
/* 199 */     appendInt(sb, dateTime.get(5), 2);
/* 200 */     if (!this.dateOnly)
/*     */     {
/* 202 */       sb.append('T');
/* 203 */       appendInt(sb, dateTime.get(11), 2);
/* 204 */       sb.append(':');
/* 205 */       appendInt(sb, dateTime.get(12), 2);
/* 206 */       sb.append(':');
/* 207 */       appendInt(sb, dateTime.get(13), 2);
/*     */       
/* 209 */       if (dateTime.isSet(14)) {
/* 210 */         sb.append('.');
/* 211 */         appendInt(sb, dateTime.get(14), 3);
/*     */       }
/*     */       
/* 214 */       if (this.tzShift == 0) {
/* 215 */         sb.append('Z');
/*     */       } else {
/* 217 */         int absTzShift = this.tzShift;
/* 218 */         if (this.tzShift > 0) {
/* 219 */           sb.append('+');
/*     */         } else {
/* 221 */           sb.append('-');
/* 222 */           absTzShift = -absTzShift;
/*     */         }
/*     */         
/* 225 */         int tzHours = absTzShift / 60;
/* 226 */         int tzMinutes = absTzShift % 60;
/* 227 */         appendInt(sb, tzHours, 2);
/* 228 */         sb.append(':');
/* 229 */         appendInt(sb, tzMinutes, 2);
/*     */       }
/*     */     }
/* 232 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 237 */     return toStringRfc3339();
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
/*     */   public boolean equals(Object o)
/*     */   {
/* 250 */     if (o == this) {
/* 251 */       return true;
/*     */     }
/* 253 */     if (!(o instanceof DateTime)) {
/* 254 */       return false;
/*     */     }
/* 256 */     DateTime other = (DateTime)o;
/* 257 */     return (this.dateOnly == other.dateOnly) && (this.value == other.value) && (this.tzShift == other.tzShift);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 262 */     return Arrays.hashCode(new long[] { this.value, this.dateOnly ? 1L : 0L, this.tzShift });
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public static DateTime parseRfc3339(String str)
/*     */     throws java.lang.NumberFormatException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 145	com/google/api/client/util/DateTime:RFC3339_PATTERN	Ljava/util/regex/Pattern;
/*     */     //   3: aload_0
/*     */     //   4: invokevirtual 151	java/util/regex/Pattern:matcher	(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;
/*     */     //   7: astore_1
/*     */     //   8: aload_1
/*     */     //   9: invokevirtual 156	java/util/regex/Matcher:matches	()Z
/*     */     //   12: ifne +39 -> 51
/*     */     //   15: new 143	java/lang/NumberFormatException
/*     */     //   18: dup
/*     */     //   19: ldc -98
/*     */     //   21: aload_0
/*     */     //   22: invokestatic 163	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   25: dup
/*     */     //   26: invokevirtual 166	java/lang/String:length	()I
/*     */     //   29: ifeq +9 -> 38
/*     */     //   32: invokevirtual 170	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   35: goto +12 -> 47
/*     */     //   38: pop
/*     */     //   39: new 160	java/lang/String
/*     */     //   42: dup_x1
/*     */     //   43: swap
/*     */     //   44: invokespecial 172	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   47: invokespecial 173	java/lang/NumberFormatException:<init>	(Ljava/lang/String;)V
/*     */     //   50: athrow
/*     */     //   51: aload_1
/*     */     //   52: iconst_1
/*     */     //   53: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   56: invokestatic 181	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*     */     //   59: istore_2
/*     */     //   60: aload_1
/*     */     //   61: iconst_2
/*     */     //   62: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   65: invokestatic 181	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*     */     //   68: iconst_1
/*     */     //   69: isub
/*     */     //   70: istore_3
/*     */     //   71: aload_1
/*     */     //   72: iconst_3
/*     */     //   73: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   76: invokestatic 181	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*     */     //   79: istore 4
/*     */     //   81: aload_1
/*     */     //   82: iconst_4
/*     */     //   83: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   86: ifnull +7 -> 93
/*     */     //   89: iconst_1
/*     */     //   90: goto +4 -> 94
/*     */     //   93: iconst_0
/*     */     //   94: istore 5
/*     */     //   96: aload_1
/*     */     //   97: bipush 9
/*     */     //   99: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   102: astore 6
/*     */     //   104: aload 6
/*     */     //   106: ifnull +7 -> 113
/*     */     //   109: iconst_1
/*     */     //   110: goto +4 -> 114
/*     */     //   113: iconst_0
/*     */     //   114: istore 7
/*     */     //   116: iconst_0
/*     */     //   117: istore 8
/*     */     //   119: iconst_0
/*     */     //   120: istore 9
/*     */     //   122: iconst_0
/*     */     //   123: istore 10
/*     */     //   125: iconst_0
/*     */     //   126: istore 11
/*     */     //   128: aconst_null
/*     */     //   129: astore 12
/*     */     //   131: iload 7
/*     */     //   133: ifeq +47 -> 180
/*     */     //   136: iload 5
/*     */     //   138: ifne +42 -> 180
/*     */     //   141: new 143	java/lang/NumberFormatException
/*     */     //   144: dup
/*     */     //   145: ldc -73
/*     */     //   147: invokestatic 163	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   150: aload_0
/*     */     //   151: invokestatic 163	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   154: dup
/*     */     //   155: invokevirtual 166	java/lang/String:length	()I
/*     */     //   158: ifeq +9 -> 167
/*     */     //   161: invokevirtual 170	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   164: goto +12 -> 176
/*     */     //   167: pop
/*     */     //   168: new 160	java/lang/String
/*     */     //   171: dup_x1
/*     */     //   172: swap
/*     */     //   173: invokespecial 172	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   176: invokespecial 173	java/lang/NumberFormatException:<init>	(Ljava/lang/String;)V
/*     */     //   179: athrow
/*     */     //   180: iload 5
/*     */     //   182: ifeq +59 -> 241
/*     */     //   185: aload_1
/*     */     //   186: iconst_5
/*     */     //   187: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   190: invokestatic 181	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*     */     //   193: istore 8
/*     */     //   195: aload_1
/*     */     //   196: bipush 6
/*     */     //   198: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   201: invokestatic 181	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*     */     //   204: istore 9
/*     */     //   206: aload_1
/*     */     //   207: bipush 7
/*     */     //   209: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   212: invokestatic 181	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*     */     //   215: istore 10
/*     */     //   217: aload_1
/*     */     //   218: bipush 8
/*     */     //   220: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   223: ifnull +18 -> 241
/*     */     //   226: aload_1
/*     */     //   227: bipush 8
/*     */     //   229: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   232: iconst_1
/*     */     //   233: invokevirtual 186	java/lang/String:substring	(I)Ljava/lang/String;
/*     */     //   236: invokestatic 181	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*     */     //   239: istore 11
/*     */     //   241: new 90	java/util/GregorianCalendar
/*     */     //   244: dup
/*     */     //   245: getstatic 92	com/google/api/client/util/DateTime:GMT	Ljava/util/TimeZone;
/*     */     //   248: invokespecial 95	java/util/GregorianCalendar:<init>	(Ljava/util/TimeZone;)V
/*     */     //   251: astore 13
/*     */     //   253: aload 13
/*     */     //   255: iload_2
/*     */     //   256: iload_3
/*     */     //   257: iload 4
/*     */     //   259: iload 8
/*     */     //   261: iload 9
/*     */     //   263: iload 10
/*     */     //   265: invokevirtual 190	java/util/Calendar:set	(IIIIII)V
/*     */     //   268: aload 13
/*     */     //   270: bipush 14
/*     */     //   272: iload 11
/*     */     //   274: invokevirtual 193	java/util/Calendar:set	(II)V
/*     */     //   277: aload 13
/*     */     //   279: invokevirtual 196	java/util/Calendar:getTimeInMillis	()J
/*     */     //   282: lstore 14
/*     */     //   284: iload 5
/*     */     //   286: ifeq +91 -> 377
/*     */     //   289: iload 7
/*     */     //   291: ifeq +86 -> 377
/*     */     //   294: aload 6
/*     */     //   296: iconst_0
/*     */     //   297: invokevirtual 200	java/lang/String:charAt	(I)C
/*     */     //   300: invokestatic 206	java/lang/Character:toUpperCase	(C)C
/*     */     //   303: bipush 90
/*     */     //   305: if_icmpne +9 -> 314
/*     */     //   308: iconst_0
/*     */     //   309: istore 16
/*     */     //   311: goto +59 -> 370
/*     */     //   314: aload_1
/*     */     //   315: bipush 11
/*     */     //   317: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   320: invokestatic 181	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*     */     //   323: bipush 60
/*     */     //   325: imul
/*     */     //   326: aload_1
/*     */     //   327: bipush 12
/*     */     //   329: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   332: invokestatic 181	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*     */     //   335: iadd
/*     */     //   336: istore 16
/*     */     //   338: aload_1
/*     */     //   339: bipush 10
/*     */     //   341: invokevirtual 177	java/util/regex/Matcher:group	(I)Ljava/lang/String;
/*     */     //   344: iconst_0
/*     */     //   345: invokevirtual 200	java/lang/String:charAt	(I)C
/*     */     //   348: bipush 45
/*     */     //   350: if_icmpne +8 -> 358
/*     */     //   353: iload 16
/*     */     //   355: ineg
/*     */     //   356: istore 16
/*     */     //   358: lload 14
/*     */     //   360: iload 16
/*     */     //   362: i2l
/*     */     //   363: ldc2_w 96
/*     */     //   366: lmul
/*     */     //   367: lsub
/*     */     //   368: lstore 14
/*     */     //   370: iload 16
/*     */     //   372: invokestatic 41	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   375: astore 12
/*     */     //   377: new 2	com/google/api/client/util/DateTime
/*     */     //   380: dup
/*     */     //   381: iload 5
/*     */     //   383: ifne +7 -> 390
/*     */     //   386: iconst_1
/*     */     //   387: goto +4 -> 391
/*     */     //   390: iconst_0
/*     */     //   391: lload 14
/*     */     //   393: aload 12
/*     */     //   395: invokespecial 44	com/google/api/client/util/DateTime:<init>	(ZJLjava/lang/Integer;)V
/*     */     //   398: areturn
/*     */     // Line number table:
/*     */     //   Java source line #288	-> byte code offset #0
/*     */     //   Java source line #289	-> byte code offset #8
/*     */     //   Java source line #290	-> byte code offset #15
/*     */     //   Java source line #293	-> byte code offset #51
/*     */     //   Java source line #294	-> byte code offset #60
/*     */     //   Java source line #295	-> byte code offset #71
/*     */     //   Java source line #296	-> byte code offset #81
/*     */     //   Java source line #297	-> byte code offset #96
/*     */     //   Java source line #298	-> byte code offset #104
/*     */     //   Java source line #299	-> byte code offset #116
/*     */     //   Java source line #300	-> byte code offset #119
/*     */     //   Java source line #301	-> byte code offset #122
/*     */     //   Java source line #302	-> byte code offset #125
/*     */     //   Java source line #303	-> byte code offset #128
/*     */     //   Java source line #305	-> byte code offset #131
/*     */     //   Java source line #306	-> byte code offset #141
/*     */     //   Java source line #310	-> byte code offset #180
/*     */     //   Java source line #311	-> byte code offset #185
/*     */     //   Java source line #312	-> byte code offset #195
/*     */     //   Java source line #313	-> byte code offset #206
/*     */     //   Java source line #314	-> byte code offset #217
/*     */     //   Java source line #315	-> byte code offset #226
/*     */     //   Java source line #318	-> byte code offset #241
/*     */     //   Java source line #319	-> byte code offset #253
/*     */     //   Java source line #320	-> byte code offset #268
/*     */     //   Java source line #321	-> byte code offset #277
/*     */     //   Java source line #323	-> byte code offset #284
/*     */     //   Java source line #325	-> byte code offset #294
/*     */     //   Java source line #326	-> byte code offset #308
/*     */     //   Java source line #328	-> byte code offset #314
/*     */     //   Java source line #330	-> byte code offset #338
/*     */     //   Java source line #331	-> byte code offset #353
/*     */     //   Java source line #333	-> byte code offset #358
/*     */     //   Java source line #335	-> byte code offset #370
/*     */     //   Java source line #337	-> byte code offset #377
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	399	0	str	String
/*     */     //   8	391	1	matcher	java.util.regex.Matcher
/*     */     //   60	339	2	year	int
/*     */     //   71	328	3	month	int
/*     */     //   81	318	4	day	int
/*     */     //   96	303	5	isTimeGiven	boolean
/*     */     //   104	295	6	tzShiftRegexGroup	String
/*     */     //   116	283	7	isTzShiftGiven	boolean
/*     */     //   119	280	8	hourOfDay	int
/*     */     //   122	277	9	minute	int
/*     */     //   125	274	10	second	int
/*     */     //   128	271	11	milliseconds	int
/*     */     //   131	268	12	tzShiftInteger	Integer
/*     */     //   253	146	13	dateTime	Calendar
/*     */     //   284	115	14	value	long
/*     */     //   311	3	16	tzShift	int
/*     */     //   338	39	16	tzShift	int
/*     */   }
/*     */   
/*     */   private static void appendInt(StringBuilder sb, int num, int numDigits)
/*     */   {
/* 342 */     if (num < 0) {
/* 343 */       sb.append('-');
/* 344 */       num = -num;
/*     */     }
/* 346 */     int x = num;
/* 347 */     while (x > 0) {
/* 348 */       x /= 10;
/* 349 */       numDigits--;
/*     */     }
/* 351 */     for (int i = 0; i < numDigits; i++) {
/* 352 */       sb.append('0');
/*     */     }
/* 354 */     if (num != 0) {
/* 355 */       sb.append(num);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\DateTime.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */