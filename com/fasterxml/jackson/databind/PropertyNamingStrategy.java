/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PropertyNamingStrategy
/*     */   implements Serializable
/*     */ {
/*  36 */   public static final PropertyNamingStrategy CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES = new LowerCaseWithUnderscoresStrategy();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  44 */   public static final PropertyNamingStrategy PASCAL_CASE_TO_CAMEL_CASE = new PascalCaseStrategy();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */   public static final PropertyNamingStrategy LOWER_CASE = new LowerCaseStrategy();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName)
/*     */   {
/*  76 */     return defaultName;
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
/*     */   public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName)
/*     */   {
/*  97 */     return defaultName;
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
/*     */   public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName)
/*     */   {
/* 117 */     return defaultName;
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
/*     */   public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter ctorParam, String defaultName)
/*     */   {
/* 135 */     return defaultName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static abstract class PropertyNamingStrategyBase
/*     */     extends PropertyNamingStrategy
/*     */   {
/*     */     public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName)
/*     */     {
/* 149 */       return translate(defaultName);
/*     */     }
/*     */     
/*     */ 
/*     */     public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName)
/*     */     {
/* 155 */       return translate(defaultName);
/*     */     }
/*     */     
/*     */ 
/*     */     public String nameForSetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName)
/*     */     {
/* 161 */       return translate(defaultName);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String nameForConstructorParameter(MapperConfig<?> config, AnnotatedParameter ctorParam, String defaultName)
/*     */     {
/* 168 */       return translate(defaultName);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract String translate(String paramString);
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
/*     */   public static class LowerCaseWithUnderscoresStrategy
/*     */     extends PropertyNamingStrategy.PropertyNamingStrategyBase
/*     */   {
/*     */     public String translate(String input)
/*     */     {
/* 235 */       if (input == null) return input;
/* 236 */       int length = input.length();
/* 237 */       StringBuilder result = new StringBuilder(length * 2);
/* 238 */       int resultLength = 0;
/* 239 */       boolean wasPrevTranslated = false;
/* 240 */       for (int i = 0; i < length; i++)
/*     */       {
/* 242 */         char c = input.charAt(i);
/* 243 */         if ((i > 0) || (c != '_'))
/*     */         {
/* 245 */           if (Character.isUpperCase(c))
/*     */           {
/* 247 */             if ((!wasPrevTranslated) && (resultLength > 0) && (result.charAt(resultLength - 1) != '_'))
/*     */             {
/* 249 */               result.append('_');
/* 250 */               resultLength++;
/*     */             }
/* 252 */             c = Character.toLowerCase(c);
/* 253 */             wasPrevTranslated = true;
/*     */           }
/*     */           else
/*     */           {
/* 257 */             wasPrevTranslated = false;
/*     */           }
/* 259 */           result.append(c);
/* 260 */           resultLength++;
/*     */         }
/*     */       }
/* 263 */       return resultLength > 0 ? result.toString() : input;
/*     */     }
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
/*     */   public static class PascalCaseStrategy
/*     */     extends PropertyNamingStrategy.PropertyNamingStrategyBase
/*     */   {
/*     */     public String translate(String input)
/*     */     {
/* 295 */       if ((input == null) || (input.length() == 0)) {
/* 296 */         return input;
/*     */       }
/*     */       
/* 299 */       char c = input.charAt(0);
/* 300 */       char uc = Character.toUpperCase(c);
/* 301 */       if (c == uc) {
/* 302 */         return input;
/*     */       }
/* 304 */       StringBuilder sb = new StringBuilder(input);
/* 305 */       sb.setCharAt(0, uc);
/* 306 */       return sb.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class LowerCaseStrategy
/*     */     extends PropertyNamingStrategy.PropertyNamingStrategyBase
/*     */   {
/*     */     public String translate(String input)
/*     */     {
/* 322 */       return input.toLowerCase();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\PropertyNamingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */