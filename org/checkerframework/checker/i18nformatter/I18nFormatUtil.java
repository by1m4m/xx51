/*     */ package org.checkerframework.checker.i18nformatter;
/*     */ 
/*     */ import java.text.ChoiceFormat;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.DecimalFormatSymbols;
/*     */ import java.text.MessageFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Locale.Category;
/*     */ import java.util.Map;
/*     */ import org.checkerframework.checker.i18nformatter.qual.I18nChecksFormat;
/*     */ import org.checkerframework.checker.i18nformatter.qual.I18nConversionCategory;
/*     */ import org.checkerframework.checker.i18nformatter.qual.I18nValidFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class I18nFormatUtil
/*     */ {
/*     */   public static void tryFormatSatisfiability(String format)
/*     */     throws IllegalFormatException
/*     */   {
/*  27 */     MessageFormat.format(format, (Object[])null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static I18nConversionCategory[] formatParameterCategories(String format)
/*     */     throws IllegalFormatException
/*     */   {
/*  37 */     tryFormatSatisfiability(format);
/*  38 */     I18nConversion[] cs = MessageFormatParser.parse(format);
/*     */     
/*  40 */     int max_index = -1;
/*  41 */     Map<Integer, I18nConversionCategory> conv = new HashMap();
/*     */     
/*  43 */     for (I18nConversion c : cs) {
/*  44 */       int index = c.index;
/*  45 */       conv.put(
/*  46 */         Integer.valueOf(index), 
/*  47 */         I18nConversionCategory.intersect(c.category, conv
/*     */         
/*  49 */         .containsKey(Integer.valueOf(index)) ? 
/*  50 */         (I18nConversionCategory)conv.get(Integer.valueOf(index)) : I18nConversionCategory.UNUSED));
/*     */       
/*  52 */       max_index = Math.max(max_index, index);
/*     */     }
/*     */     
/*  55 */     I18nConversionCategory[] res = new I18nConversionCategory[max_index + 1];
/*  56 */     for (int i = 0; i <= max_index; i++) {
/*  57 */       res[i] = (conv.containsKey(Integer.valueOf(i)) ? (I18nConversionCategory)conv.get(Integer.valueOf(i)) : I18nConversionCategory.UNUSED);
/*     */     }
/*  59 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @I18nChecksFormat
/*     */   public static boolean hasFormat(String format, I18nConversionCategory... cc)
/*     */   {
/*  69 */     I18nConversionCategory[] fcc = formatParameterCategories(format);
/*  70 */     if (fcc.length != cc.length) {
/*  71 */       return false;
/*     */     }
/*     */     
/*  74 */     for (int i = 0; i < cc.length; i++) {
/*  75 */       if (!I18nConversionCategory.isSubsetOf(cc[i], fcc[i])) {
/*  76 */         return false;
/*     */       }
/*     */     }
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   @I18nValidFormat
/*     */   public static boolean isFormat(String format) {
/*     */     try {
/*  85 */       formatParameterCategories(format);
/*     */     } catch (Exception e) {
/*  87 */       return false;
/*     */     }
/*  89 */     return true;
/*     */   }
/*     */   
/*     */   private static class I18nConversion {
/*     */     public int index;
/*     */     public I18nConversionCategory category;
/*     */     
/*     */     public I18nConversion(int index, I18nConversionCategory category) {
/*  97 */       this.index = index;
/*  98 */       this.category = category;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 103 */       return this.category.toString() + "(index: " + this.index + ")";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class MessageFormatParser
/*     */   {
/*     */     public static int maxOffset;
/*     */     
/*     */     private static Locale locale;
/*     */     
/*     */     private static List<I18nConversionCategory> categories;
/*     */     
/*     */     private static List<Integer> argumentIndices;
/*     */     
/*     */     private static int numFormat;
/*     */     
/*     */     private static final int SEG_RAW = 0;
/*     */     
/*     */     private static final int SEG_INDEX = 1;
/*     */     
/*     */     private static final int SEG_TYPE = 2;
/*     */     
/*     */     private static final int SEG_MODIFIER = 3;
/*     */     
/*     */     private static final int TYPE_NULL = 0;
/*     */     
/*     */     private static final int TYPE_NUMBER = 1;
/*     */     
/*     */     private static final int TYPE_DATE = 2;
/*     */     
/*     */     private static final int TYPE_TIME = 3;
/*     */     
/*     */     private static final int TYPE_CHOICE = 4;
/*     */     
/* 139 */     private static final String[] TYPE_KEYWORDS = { "", "number", "date", "time", "choice" };
/*     */     
/*     */     private static final int MODIFIER_DEFAULT = 0;
/*     */     
/*     */     private static final int MODIFIER_CURRENCY = 1;
/*     */     
/*     */     private static final int MODIFIER_PERCENT = 2;
/*     */     
/*     */     private static final int MODIFIER_INTEGER = 3;
/* 148 */     private static final String[] NUMBER_MODIFIER_KEYWORDS = { "", "currency", "percent", "integer" };
/*     */     
/*     */ 
/*     */ 
/* 152 */     private static final String[] DATE_TIME_MODIFIER_KEYWORDS = { "", "short", "medium", "long", "full" };
/*     */     
/*     */ 
/*     */     public static I18nFormatUtil.I18nConversion[] parse(String pattern)
/*     */     {
/* 157 */       categories = new ArrayList();
/* 158 */       argumentIndices = new ArrayList();
/* 159 */       locale = Locale.getDefault(Locale.Category.FORMAT);
/* 160 */       applyPattern(pattern);
/*     */       
/* 162 */       I18nFormatUtil.I18nConversion[] ret = new I18nFormatUtil.I18nConversion[numFormat];
/* 163 */       for (int i = 0; i < numFormat; i++) {
/* 164 */         ret[i] = new I18nFormatUtil.I18nConversion(((Integer)argumentIndices.get(i)).intValue(), (I18nConversionCategory)categories.get(i));
/*     */       }
/* 166 */       return ret;
/*     */     }
/*     */     
/*     */     private static void applyPattern(String pattern) {
/* 170 */       StringBuilder[] segments = new StringBuilder[4];
/*     */       
/*     */ 
/* 173 */       segments[0] = new StringBuilder();
/*     */       
/* 175 */       int part = 0;
/* 176 */       numFormat = 0;
/* 177 */       boolean inQuote = false;
/* 178 */       int braceStack = 0;
/* 179 */       maxOffset = -1;
/* 180 */       for (int i = 0; i < pattern.length(); i++) {
/* 181 */         char ch = pattern.charAt(i);
/* 182 */         if (part == 0) {
/* 183 */           if (ch == '\'') {
/* 184 */             if ((i + 1 < pattern.length()) && (pattern.charAt(i + 1) == '\'')) {
/* 185 */               segments[part].append(ch);
/* 186 */               i++;
/*     */             } else {
/* 188 */               inQuote = !inQuote;
/*     */             }
/* 190 */           } else if ((ch == '{') && (!inQuote)) {
/* 191 */             part = 1;
/* 192 */             if (segments[1] == null) {
/* 193 */               segments[1] = new StringBuilder();
/*     */             }
/*     */           } else {
/* 196 */             segments[part].append(ch);
/*     */           }
/*     */         }
/* 199 */         else if (inQuote) {
/* 200 */           segments[part].append(ch);
/* 201 */           if (ch == '\'') {
/* 202 */             inQuote = false;
/*     */           }
/*     */         } else {
/* 205 */           switch (ch) {
/*     */           case ',': 
/* 207 */             if (part < 3) {
/* 208 */               if (segments[(++part)] == null) {
/* 209 */                 segments[part] = new StringBuilder();
/*     */               }
/*     */             } else {
/* 212 */               segments[part].append(ch);
/*     */             }
/* 214 */             break;
/*     */           case '{': 
/* 216 */             braceStack++;
/* 217 */             segments[part].append(ch);
/* 218 */             break;
/*     */           case '}': 
/* 220 */             if (braceStack == 0) {
/* 221 */               part = 0;
/* 222 */               makeFormat(i, numFormat, segments);
/* 223 */               numFormat += 1;
/*     */               
/* 225 */               segments[1] = null;
/* 226 */               segments[2] = null;
/* 227 */               segments[3] = null;
/*     */             } else {
/* 229 */               braceStack--;
/* 230 */               segments[part].append(ch);
/*     */             }
/* 232 */             break;
/*     */           
/*     */           case ' ': 
/* 235 */             if ((part != 2) || (segments[2].length() > 0)) {
/* 236 */               segments[part].append(ch);
/*     */             }
/*     */             break;
/*     */           case '\'': 
/* 240 */             inQuote = true;
/* 241 */             segments[part].append(ch);
/* 242 */             break;
/*     */           default: 
/* 244 */             segments[part].append(ch);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */       
/* 250 */       if ((braceStack == 0) && (part != 0)) {
/* 251 */         maxOffset = -1;
/* 252 */         throw new IllegalArgumentException("Unmatched braces in the pattern");
/*     */       }
/*     */     }
/*     */     
/*     */     private static void makeFormat(int position, int offsetNumber, StringBuilder[] textSegments)
/*     */     {
/* 258 */       String[] segments = new String[textSegments.length];
/* 259 */       for (int i = 0; i < textSegments.length; i++) {
/* 260 */         StringBuilder oneseg = textSegments[i];
/* 261 */         segments[i] = (oneseg != null ? oneseg.toString() : "");
/*     */       }
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 267 */         argumentNumber = Integer.parseInt(segments[1]);
/*     */       } catch (NumberFormatException e) {
/*     */         int argumentNumber;
/* 270 */         throw new IllegalArgumentException("can't parse argument number: " + segments[1], e);
/*     */       }
/*     */       int argumentNumber;
/* 273 */       if (argumentNumber < 0) {
/* 274 */         throw new IllegalArgumentException("negative argument number: " + argumentNumber);
/*     */       }
/*     */       
/* 277 */       int oldMaxOffset = maxOffset;
/* 278 */       maxOffset = offsetNumber;
/* 279 */       argumentIndices.add(Integer.valueOf(argumentNumber));
/*     */       
/*     */ 
/* 282 */       I18nConversionCategory category = null;
/* 283 */       if (segments[2].length() != 0) {
/* 284 */         int type = findKeyword(segments[2], TYPE_KEYWORDS);
/* 285 */         switch (type) {
/*     */         case 0: 
/* 287 */           category = I18nConversionCategory.GENERAL;
/* 288 */           break;
/*     */         case 1: 
/* 290 */           switch (findKeyword(segments[3], NUMBER_MODIFIER_KEYWORDS))
/*     */           {
/*     */           case 0: 
/*     */           case 1: 
/*     */           case 2: 
/*     */           case 3: 
/*     */             break;
/*     */           default: 
/*     */             try
/*     */             {
/* 300 */               new DecimalFormat(segments[3], DecimalFormatSymbols.getInstance(locale));
/*     */             } catch (IllegalArgumentException e) {
/* 302 */               maxOffset = oldMaxOffset;
/*     */               
/* 304 */               throw e;
/*     */             }
/*     */           }
/*     */           
/* 308 */           category = I18nConversionCategory.NUMBER;
/* 309 */           break;
/*     */         case 2: 
/*     */         case 3: 
/* 312 */           int mod = findKeyword(segments[3], DATE_TIME_MODIFIER_KEYWORDS);
/* 313 */           if ((mod < 0) || (mod >= DATE_TIME_MODIFIER_KEYWORDS.length))
/*     */           {
/*     */             try
/*     */             {
/*     */ 
/* 318 */               new SimpleDateFormat(segments[3], locale);
/*     */             } catch (IllegalArgumentException e) {
/* 320 */               maxOffset = oldMaxOffset;
/*     */               
/* 322 */               throw e;
/*     */             }
/*     */           }
/* 325 */           category = I18nConversionCategory.DATE;
/* 326 */           break;
/*     */         case 4: 
/* 328 */           if (segments[3].length() == 0) {
/* 329 */             throw new IllegalArgumentException("Choice Pattern requires Subformat Pattern: " + segments[3]);
/*     */           }
/*     */           
/*     */ 
/*     */           try
/*     */           {
/* 335 */             new ChoiceFormat(segments[3]);
/*     */           } catch (Exception e) {
/* 337 */             maxOffset = oldMaxOffset;
/*     */             
/* 339 */             throw new IllegalArgumentException("Choice Pattern incorrect: " + segments[3], e);
/*     */           }
/*     */           
/* 342 */           category = I18nConversionCategory.NUMBER;
/* 343 */           break;
/*     */         default: 
/* 345 */           maxOffset = oldMaxOffset;
/* 346 */           throw new IllegalArgumentException("unknown format type: " + segments[2]);
/*     */         }
/*     */       }
/*     */       else {
/* 350 */         category = I18nConversionCategory.GENERAL;
/*     */       }
/* 352 */       categories.add(category);
/*     */     }
/*     */     
/*     */     private static final int findKeyword(String s, String[] list) {
/* 356 */       for (int i = 0; i < list.length; i++) {
/* 357 */         if (s.equals(list[i])) {
/* 358 */           return i;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 363 */       String ls = s.trim().toLowerCase(Locale.ROOT);
/* 364 */       if (ls != s) {
/* 365 */         for (int i = 0; i < list.length; i++) {
/* 366 */           if (ls.equals(list[i])) {
/* 367 */             return i;
/*     */           }
/*     */         }
/*     */       }
/* 371 */       return -1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\i18nformatter\I18nFormatUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */