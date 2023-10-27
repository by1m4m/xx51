/*    */ package com.google.common.base;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.lang.ref.WeakReference;
/*    */ import java.util.Locale;
/*    */ import java.util.Map;
/*    */ import java.util.ServiceConfigurationError;
/*    */ import java.util.logging.Level;
/*    */ import java.util.logging.Logger;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @GwtCompatible(emulated=true)
/*    */ final class Platform
/*    */ {
/* 33 */   private static final Logger logger = Logger.getLogger(Platform.class.getName());
/* 34 */   private static final PatternCompiler patternCompiler = loadPatternCompiler();
/*    */   
/*    */ 
/*    */ 
/*    */   static long systemNanoTime()
/*    */   {
/* 40 */     return System.nanoTime();
/*    */   }
/*    */   
/*    */   static CharMatcher precomputeCharMatcher(CharMatcher matcher) {
/* 44 */     return matcher.precomputedInternal();
/*    */   }
/*    */   
/*    */   static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> enumClass, String value) {
/* 48 */     WeakReference<? extends Enum<?>> ref = (WeakReference)Enums.getEnumConstants(enumClass).get(value);
/* 49 */     return ref == null ? Optional.absent() : Optional.of(enumClass.cast(ref.get()));
/*    */   }
/*    */   
/*    */   static String formatCompact4Digits(double value) {
/* 53 */     return String.format(Locale.ROOT, "%.4g", new Object[] { Double.valueOf(value) });
/*    */   }
/*    */   
/*    */   static boolean stringIsNullOrEmpty(String string) {
/* 57 */     return (string == null) || (string.isEmpty());
/*    */   }
/*    */   
/*    */   static String nullToEmpty(String string) {
/* 61 */     return string == null ? "" : string;
/*    */   }
/*    */   
/*    */   static String emptyToNull(String string) {
/* 65 */     return stringIsNullOrEmpty(string) ? null : string;
/*    */   }
/*    */   
/*    */   static CommonPattern compilePattern(String pattern) {
/* 69 */     Preconditions.checkNotNull(pattern);
/* 70 */     return patternCompiler.compile(pattern);
/*    */   }
/*    */   
/*    */   static boolean patternCompilerIsPcreLike() {
/* 74 */     return patternCompiler.isPcreLike();
/*    */   }
/*    */   
/*    */   private static PatternCompiler loadPatternCompiler() {
/* 78 */     return new JdkPatternCompiler(null);
/*    */   }
/*    */   
/*    */   private static void logPatternCompilerError(ServiceConfigurationError e) {
/* 82 */     logger.log(Level.WARNING, "Error loading regex compiler, falling back to next option", e);
/*    */   }
/*    */   
/*    */   private static final class JdkPatternCompiler implements PatternCompiler
/*    */   {
/*    */     public CommonPattern compile(String pattern) {
/* 88 */       return new JdkPattern(Pattern.compile(pattern));
/*    */     }
/*    */     
/*    */     public boolean isPcreLike()
/*    */     {
/* 93 */       return true;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\base\Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */