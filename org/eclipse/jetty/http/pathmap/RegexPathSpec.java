/*     */ package org.eclipse.jetty.http.pathmap;
/*     */ 
/*     */ import java.util.regex.Matcher;
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
/*     */ public class RegexPathSpec
/*     */   extends PathSpec
/*     */ {
/*     */   protected Pattern pattern;
/*     */   
/*     */   protected RegexPathSpec() {}
/*     */   
/*     */   public RegexPathSpec(String regex)
/*     */   {
/*  35 */     this.pathSpec = regex;
/*  36 */     if (regex.startsWith("regex|"))
/*  37 */       this.pathSpec = regex.substring("regex|".length());
/*  38 */     this.pathDepth = 0;
/*  39 */     this.specLength = this.pathSpec.length();
/*     */     
/*  41 */     boolean inGrouping = false;
/*  42 */     StringBuilder signature = new StringBuilder();
/*  43 */     for (char c : this.pathSpec.toCharArray())
/*     */     {
/*  45 */       switch (c)
/*     */       {
/*     */       case '[': 
/*  48 */         inGrouping = true;
/*  49 */         break;
/*     */       case ']': 
/*  51 */         inGrouping = false;
/*  52 */         signature.append('g');
/*  53 */         break;
/*     */       case '*': 
/*  55 */         signature.append('g');
/*  56 */         break;
/*     */       case '/': 
/*  58 */         if (!inGrouping)
/*     */         {
/*  60 */           this.pathDepth += 1;
/*     */         }
/*     */         break;
/*     */       default: 
/*  64 */         if (!inGrouping)
/*     */         {
/*  66 */           if (Character.isLetterOrDigit(c))
/*     */           {
/*  68 */             signature.append('l');
/*     */           }
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*  74 */     this.pattern = Pattern.compile(this.pathSpec);
/*     */     
/*     */ 
/*  77 */     String sig = signature.toString();
/*     */     
/*  79 */     if (Pattern.matches("^l*$", sig))
/*     */     {
/*  81 */       this.group = PathSpecGroup.EXACT;
/*     */     }
/*  83 */     else if (Pattern.matches("^l*g+", sig))
/*     */     {
/*  85 */       this.group = PathSpecGroup.PREFIX_GLOB;
/*     */     }
/*  87 */     else if (Pattern.matches("^g+l+$", sig))
/*     */     {
/*  89 */       this.group = PathSpecGroup.SUFFIX_GLOB;
/*     */     }
/*     */     else
/*     */     {
/*  93 */       this.group = PathSpecGroup.MIDDLE_GLOB;
/*     */     }
/*     */   }
/*     */   
/*     */   public Matcher getMatcher(String path)
/*     */   {
/*  99 */     return this.pattern.matcher(path);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPathInfo(String path)
/*     */   {
/* 106 */     if (this.group == PathSpecGroup.PREFIX_GLOB)
/*     */     {
/* 108 */       Matcher matcher = getMatcher(path);
/* 109 */       if (matcher.matches())
/*     */       {
/* 111 */         if (matcher.groupCount() >= 1)
/*     */         {
/* 113 */           String pathInfo = matcher.group(1);
/* 114 */           if ("".equals(pathInfo))
/*     */           {
/* 116 */             return "/";
/*     */           }
/*     */           
/*     */ 
/* 120 */           return pathInfo;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 125 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getPathMatch(String path)
/*     */   {
/* 131 */     Matcher matcher = getMatcher(path);
/* 132 */     if (matcher.matches())
/*     */     {
/* 134 */       if (matcher.groupCount() >= 1)
/*     */       {
/* 136 */         int idx = matcher.start(1);
/* 137 */         if (idx > 0)
/*     */         {
/* 139 */           if (path.charAt(idx - 1) == '/')
/*     */           {
/* 141 */             idx--;
/*     */           }
/* 143 */           return path.substring(0, idx);
/*     */         }
/*     */       }
/* 146 */       return path;
/*     */     }
/* 148 */     return null;
/*     */   }
/*     */   
/*     */   public Pattern getPattern()
/*     */   {
/* 153 */     return this.pattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getRelativePath(String base, String path)
/*     */   {
/* 160 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean matches(String path)
/*     */   {
/* 166 */     int idx = path.indexOf('?');
/* 167 */     if (idx >= 0)
/*     */     {
/*     */ 
/* 170 */       return getMatcher(path.substring(0, idx)).matches();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 175 */     return getMatcher(path).matches();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\pathmap\RegexPathSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */