/*     */ package org.checkerframework.checker.formatter;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.checkerframework.checker.formatter.qual.ConversionCategory;
/*     */ 
/*     */ public class FormatUtil
/*     */ {
/*     */   private static final String formatSpecifier = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";
/*     */   
/*     */   private static class Conversion
/*     */   {
/*     */     private final int index;
/*     */     private final ConversionCategory cath;
/*     */     
/*     */     public Conversion(char c, int index)
/*     */     {
/*  21 */       this.index = index;
/*  22 */       this.cath = ConversionCategory.fromConversionChar(c);
/*     */     }
/*     */     
/*     */     int index() {
/*  26 */       return this.index;
/*     */     }
/*     */     
/*     */     ConversionCategory category() {
/*  30 */       return this.cath;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @org.checkerframework.checker.formatter.qual.ReturnsFormat
/*     */   public static String asFormat(String format, ConversionCategory... cc)
/*     */     throws IllegalFormatException
/*     */   {
/*  43 */     ConversionCategory[] fcc = formatParameterCategories(format);
/*  44 */     if (fcc.length != cc.length) {
/*  45 */       throw new ExcessiveOrMissingFormatArgumentException(cc.length, fcc.length);
/*     */     }
/*     */     
/*  48 */     for (int i = 0; i < cc.length; i++) {
/*  49 */       if (cc[i] != fcc[i]) {
/*  50 */         throw new IllegalFormatConversionCategoryException(cc[i], fcc[i]);
/*     */       }
/*     */     }
/*     */     
/*  54 */     return format;
/*     */   }
/*     */   
/*     */   public static void tryFormatSatisfiability(String format)
/*     */     throws IllegalFormatException
/*     */   {
/*  60 */     String unused = String.format(format, (Object[])null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ConversionCategory[] formatParameterCategories(String format)
/*     */     throws IllegalFormatException
/*     */   {
/*  70 */     tryFormatSatisfiability(format);
/*     */     
/*  72 */     int last = -1;
/*  73 */     int lasto = -1;
/*  74 */     int maxindex = -1;
/*     */     
/*  76 */     Conversion[] cs = parse(format);
/*  77 */     Map<Integer, ConversionCategory> conv = new java.util.HashMap();
/*     */     
/*  79 */     for (Conversion c : cs) {
/*  80 */       int index = c.index();
/*  81 */       switch (index) {
/*     */       case -1: 
/*     */         break;
/*     */       case 0: 
/*  85 */         lasto++;
/*  86 */         last = lasto;
/*  87 */         break;
/*     */       default: 
/*  89 */         last = index - 1;
/*     */       }
/*     */       
/*  92 */       maxindex = Math.max(maxindex, last);
/*  93 */       conv.put(
/*  94 */         Integer.valueOf(last), 
/*  95 */         ConversionCategory.intersect(conv
/*  96 */         .containsKey(Integer.valueOf(last)) ? (ConversionCategory)conv.get(Integer.valueOf(last)) : ConversionCategory.UNUSED, c
/*  97 */         .category()));
/*     */     }
/*     */     
/* 100 */     ConversionCategory[] res = new ConversionCategory[maxindex + 1];
/* 101 */     for (int i = 0; i <= maxindex; i++) {
/* 102 */       res[i] = (conv.containsKey(Integer.valueOf(i)) ? (ConversionCategory)conv.get(Integer.valueOf(i)) : ConversionCategory.UNUSED);
/*     */     }
/* 104 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 111 */   private static Pattern fsPattern = Pattern.compile("%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])");
/*     */   
/*     */   private static int indexFromFormat(Matcher m)
/*     */   {
/* 115 */     String s = m.group(1);
/* 116 */     int index; int index; if (s != null) {
/* 117 */       index = Integer.parseInt(s.substring(0, s.length() - 1));
/*     */     } else { int index;
/* 119 */       if ((m.group(2) != null) && (m.group(2).contains(String.valueOf('<')))) {
/* 120 */         index = -1;
/*     */       } else {
/* 122 */         index = 0;
/*     */       }
/*     */     }
/* 125 */     return index;
/*     */   }
/*     */   
/*     */   private static char conversionCharFromFormat(Matcher m) {
/* 129 */     String dt = m.group(5);
/* 130 */     if (dt == null) {
/* 131 */       return m.group(6).charAt(0);
/*     */     }
/* 133 */     return dt.charAt(0);
/*     */   }
/*     */   
/*     */   private static Conversion[] parse(String format)
/*     */   {
/* 138 */     ArrayList<Conversion> cs = new ArrayList();
/* 139 */     Matcher m = fsPattern.matcher(format);
/* 140 */     while (m.find()) {
/* 141 */       char c = conversionCharFromFormat(m);
/* 142 */       switch (c) {
/*     */       case '%': 
/*     */       case 'n': 
/*     */         break;
/*     */       default: 
/* 147 */         cs.add(new Conversion(c, indexFromFormat(m)));
/*     */       }
/*     */     }
/* 150 */     return (Conversion[])cs.toArray(new Conversion[cs.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */   public static class ExcessiveOrMissingFormatArgumentException
/*     */     extends java.util.MissingFormatArgumentException
/*     */   {
/*     */     private static final long serialVersionUID = 17000126L;
/*     */     
/*     */     private final int expected;
/*     */     
/*     */     private final int found;
/*     */     
/*     */     public ExcessiveOrMissingFormatArgumentException(int expected, int found)
/*     */     {
/* 165 */       super();
/* 166 */       this.expected = expected;
/* 167 */       this.found = found;
/*     */     }
/*     */     
/*     */     public int getExpected() {
/* 171 */       return this.expected;
/*     */     }
/*     */     
/*     */     public int getFound() {
/* 175 */       return this.found;
/*     */     }
/*     */     
/*     */     public String getMessage()
/*     */     {
/* 180 */       return String.format("Expected %d arguments but found %d.", new Object[] { Integer.valueOf(this.expected), Integer.valueOf(this.found) });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class IllegalFormatConversionCategoryException
/*     */     extends java.util.IllegalFormatConversionException
/*     */   {
/*     */     private static final long serialVersionUID = 17000126L;
/*     */     
/*     */     private final ConversionCategory expected;
/*     */     
/*     */     private final ConversionCategory found;
/*     */     
/*     */ 
/*     */     public IllegalFormatConversionCategoryException(ConversionCategory expected, ConversionCategory found)
/*     */     {
/* 198 */       super(
/* 199 */         found.types == null ? Object.class : found.types[0]);
/*     */       
/* 201 */       this.expected = expected;
/* 202 */       this.found = found;
/*     */     }
/*     */     
/*     */     public ConversionCategory getExpected() {
/* 206 */       return this.expected;
/*     */     }
/*     */     
/*     */     public ConversionCategory getFound() {
/* 210 */       return this.found;
/*     */     }
/*     */     
/*     */     public String getMessage()
/*     */     {
/* 215 */       return String.format("Expected category %s but found %s.", new Object[] { this.expected, this.found });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\formatter\FormatUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */