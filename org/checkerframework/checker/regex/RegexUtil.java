/*     */ package org.checkerframework.checker.regex;
/*     */ 
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
/*     */ import org.checkerframework.checker.regex.qual.Regex;
/*     */ import org.checkerframework.dataflow.qual.Pure;
/*     */ import org.checkerframework.dataflow.qual.SideEffectFree;
/*     */ import org.checkerframework.framework.qual.EnsuresQualifierIf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RegexUtil
/*     */ {
/*     */   private RegexUtil()
/*     */   {
/*  32 */     throw new Error("do not instantiate");
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
/*     */   public static class CheckedPatternSyntaxException
/*     */     extends Exception
/*     */   {
/*     */     private static final long serialVersionUID = 6266881831979001480L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private final PatternSyntaxException pse;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public CheckedPatternSyntaxException(PatternSyntaxException pse)
/*     */     {
/*  79 */       this.pse = pse;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public CheckedPatternSyntaxException(String desc, String regex, int index)
/*     */     {
/*  91 */       this(new PatternSyntaxException(desc, regex, index));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getDescription()
/*     */     {
/* 100 */       return this.pse.getDescription();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public int getIndex()
/*     */     {
/* 110 */       return this.pse.getIndex();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Pure
/*     */     public String getMessage()
/*     */     {
/* 123 */       return this.pse.getMessage();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getPattern()
/*     */     {
/* 132 */       return this.pse.getPattern();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Pure
/*     */   @EnsuresQualifierIf(result=true, expression={"#1"}, qualifier=Regex.class)
/*     */   public static boolean isRegex(String s)
/*     */   {
/* 145 */     return isRegex(s, 0);
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
/*     */   @Pure
/*     */   @EnsuresQualifierIf(result=true, expression={"#1"}, qualifier=Regex.class)
/*     */   public static boolean isRegex(String s, int groups)
/*     */   {
/*     */     try
/*     */     {
/* 164 */       p = Pattern.compile(s);
/*     */     } catch (PatternSyntaxException e) { Pattern p;
/* 166 */       return false; }
/*     */     Pattern p;
/* 168 */     return getGroupCount(p) >= groups;
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
/*     */   @Pure
/*     */   @EnsuresQualifierIf(result=true, expression={"#1"}, qualifier=Regex.class)
/*     */   public static boolean isRegex(char c)
/*     */   {
/* 184 */     return isRegex(Character.toString(c));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @SideEffectFree
/*     */   public static String regexError(String s)
/*     */   {
/* 197 */     return regexError(s, 0);
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
/*     */   @SideEffectFree
/*     */   public static String regexError(String s, int groups)
/*     */   {
/*     */     try
/*     */     {
/* 213 */       Pattern p = Pattern.compile(s);
/* 214 */       int actualGroups = getGroupCount(p);
/* 215 */       if (actualGroups < groups) {
/* 216 */         return regexErrorMessage(s, groups, actualGroups);
/*     */       }
/*     */     } catch (PatternSyntaxException e) {
/* 219 */       return e.getMessage();
/*     */     }
/* 221 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @SideEffectFree
/*     */   public static PatternSyntaxException regexException(String s)
/*     */   {
/* 234 */     return regexException(s, 0);
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
/*     */   @SideEffectFree
/*     */   public static PatternSyntaxException regexException(String s, int groups)
/*     */   {
/*     */     try
/*     */     {
/* 250 */       Pattern p = Pattern.compile(s);
/* 251 */       int actualGroups = getGroupCount(p);
/* 252 */       if (actualGroups < groups) {
/* 253 */         return new PatternSyntaxException(
/* 254 */           regexErrorMessage(s, groups, actualGroups), s, -1);
/*     */       }
/*     */     } catch (PatternSyntaxException pse) {
/* 257 */       return pse;
/*     */     }
/* 259 */     return null;
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
/*     */   @SideEffectFree
/*     */   public static String asRegex(String s)
/*     */   {
/* 274 */     return asRegex(s, 0);
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
/*     */   @SideEffectFree
/*     */   public static String asRegex(String s, int groups)
/*     */   {
/*     */     try
/*     */     {
/* 293 */       Pattern p = Pattern.compile(s);
/* 294 */       int actualGroups = getGroupCount(p);
/* 295 */       if (actualGroups < groups) {
/* 296 */         throw new Error(regexErrorMessage(s, groups, actualGroups));
/*     */       }
/* 298 */       return s;
/*     */     } catch (PatternSyntaxException e) {
/* 300 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @SideEffectFree
/*     */   private static String regexErrorMessage(String s, int expectedGroups, int actualGroups)
/*     */   {
/* 313 */     return "regex \"" + s + "\" has " + actualGroups + " groups, but " + expectedGroups + " groups are needed.";
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
/*     */   @Pure
/*     */   private static int getGroupCount(Pattern p)
/*     */   {
/* 331 */     return p.matcher("").groupCount();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\regex\RegexUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */