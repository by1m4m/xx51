/*     */ package org.eclipse.jetty.http.pathmap;
/*     */ 
/*     */ import org.eclipse.jetty.util.StringUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServletPathSpec
/*     */   extends PathSpec
/*     */ {
/*     */   public static String normalize(String pathSpec)
/*     */   {
/*  35 */     if ((StringUtil.isNotBlank(pathSpec)) && (!pathSpec.startsWith("/")) && (!pathSpec.startsWith("*")))
/*  36 */       return "/" + pathSpec;
/*  37 */     return pathSpec;
/*     */   }
/*     */   
/*     */ 
/*     */   public ServletPathSpec(String servletPathSpec)
/*     */   {
/*  43 */     if (servletPathSpec == null)
/*  44 */       servletPathSpec = "";
/*  45 */     if (servletPathSpec.startsWith("servlet|"))
/*  46 */       servletPathSpec = servletPathSpec.substring("servlet|".length());
/*  47 */     assertValidServletPathSpec(servletPathSpec);
/*     */     
/*     */ 
/*  50 */     if (servletPathSpec.length() == 0)
/*     */     {
/*  52 */       this.pathSpec = "";
/*  53 */       this.pathDepth = -1;
/*  54 */       this.specLength = 1;
/*  55 */       this.group = PathSpecGroup.ROOT;
/*  56 */       return;
/*     */     }
/*     */     
/*     */ 
/*  60 */     if ("/".equals(servletPathSpec))
/*     */     {
/*  62 */       this.pathSpec = "/";
/*  63 */       this.pathDepth = -1;
/*  64 */       this.specLength = 1;
/*  65 */       this.group = PathSpecGroup.DEFAULT;
/*  66 */       return;
/*     */     }
/*     */     
/*  69 */     this.specLength = servletPathSpec.length();
/*  70 */     this.pathDepth = 0;
/*  71 */     char lastChar = servletPathSpec.charAt(this.specLength - 1);
/*     */     
/*  73 */     if ((servletPathSpec.charAt(0) == '/') && (this.specLength > 1) && (lastChar == '*'))
/*     */     {
/*  75 */       this.group = PathSpecGroup.PREFIX_GLOB;
/*  76 */       this.prefix = servletPathSpec.substring(0, this.specLength - 2);
/*     */ 
/*     */     }
/*  79 */     else if (servletPathSpec.charAt(0) == '*')
/*     */     {
/*  81 */       this.group = PathSpecGroup.SUFFIX_GLOB;
/*  82 */       this.suffix = servletPathSpec.substring(2, this.specLength);
/*     */     }
/*     */     else
/*     */     {
/*  86 */       this.group = PathSpecGroup.EXACT;
/*  87 */       this.prefix = servletPathSpec;
/*     */     }
/*     */     
/*  90 */     for (int i = 0; i < this.specLength; i++)
/*     */     {
/*  92 */       int cp = servletPathSpec.codePointAt(i);
/*  93 */       if (cp < 128)
/*     */       {
/*  95 */         char c = (char)cp;
/*  96 */         switch (c)
/*     */         {
/*     */         case '/': 
/*  99 */           this.pathDepth += 1;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/* 105 */     this.pathSpec = servletPathSpec;
/*     */   }
/*     */   
/*     */   private void assertValidServletPathSpec(String servletPathSpec)
/*     */   {
/* 110 */     if ((servletPathSpec == null) || (servletPathSpec.equals("")))
/*     */     {
/* 112 */       return;
/*     */     }
/*     */     
/* 115 */     int len = servletPathSpec.length();
/*     */     
/* 117 */     if (servletPathSpec.charAt(0) == '/')
/*     */     {
/*     */ 
/* 120 */       if (len == 1)
/*     */       {
/* 122 */         return;
/*     */       }
/* 124 */       int idx = servletPathSpec.indexOf('*');
/* 125 */       if (idx < 0)
/*     */       {
/* 127 */         return;
/*     */       }
/*     */       
/* 130 */       if (idx != len - 1)
/*     */       {
/* 132 */         throw new IllegalArgumentException("Servlet Spec 12.2 violation: glob '*' can only exist at end of prefix based matches: bad spec \"" + servletPathSpec + "\"");
/*     */       }
/*     */       
/* 135 */       if ((idx < 1) || (servletPathSpec.charAt(idx - 1) != '/'))
/*     */       {
/* 137 */         throw new IllegalArgumentException("Servlet Spec 12.2 violation: suffix glob '*' can only exist after '/': bad spec \"" + servletPathSpec + "\"");
/*     */       }
/*     */     }
/* 140 */     else if (servletPathSpec.startsWith("*."))
/*     */     {
/*     */ 
/* 143 */       int idx = servletPathSpec.indexOf('/');
/*     */       
/* 145 */       if (idx >= 0)
/*     */       {
/* 147 */         throw new IllegalArgumentException("Servlet Spec 12.2 violation: suffix based path spec cannot have path separators: bad spec \"" + servletPathSpec + "\"");
/*     */       }
/*     */       
/* 150 */       idx = servletPathSpec.indexOf('*', 2);
/*     */       
/* 152 */       if (idx >= 1)
/*     */       {
/* 154 */         throw new IllegalArgumentException("Servlet Spec 12.2 violation: suffix based path spec cannot have multiple glob '*': bad spec \"" + servletPathSpec + "\"");
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 159 */       throw new IllegalArgumentException("Servlet Spec 12.2 violation: path spec must start with \"/\" or \"*.\": bad spec \"" + servletPathSpec + "\"");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPathInfo(String path)
/*     */   {
/* 167 */     if (this.group == PathSpecGroup.PREFIX_GLOB)
/*     */     {
/* 169 */       if (path.length() == this.specLength - 2)
/*     */       {
/* 171 */         return null;
/*     */       }
/* 173 */       return path.substring(this.specLength - 2);
/*     */     }
/*     */     
/* 176 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getPathMatch(String path)
/*     */   {
/* 182 */     switch (this.group)
/*     */     {
/*     */     case EXACT: 
/* 185 */       if (this.pathSpec.equals(path))
/*     */       {
/* 187 */         return path;
/*     */       }
/*     */       
/*     */ 
/* 191 */       return null;
/*     */     
/*     */     case PREFIX_GLOB: 
/* 194 */       if (isWildcardMatch(path))
/*     */       {
/* 196 */         return path.substring(0, this.specLength - 2);
/*     */       }
/*     */       
/*     */ 
/* 200 */       return null;
/*     */     
/*     */     case SUFFIX_GLOB: 
/* 203 */       if (path.regionMatches(path.length() - (this.specLength - 1), this.pathSpec, 1, this.specLength - 1))
/*     */       {
/* 205 */         return path;
/*     */       }
/*     */       
/*     */ 
/* 209 */       return null;
/*     */     
/*     */     case DEFAULT: 
/* 212 */       return path;
/*     */     }
/* 214 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getRelativePath(String base, String path)
/*     */   {
/* 221 */     String info = getPathInfo(path);
/* 222 */     if (info == null)
/*     */     {
/* 224 */       info = path;
/*     */     }
/*     */     
/* 227 */     if (info.startsWith("./"))
/*     */     {
/* 229 */       info = info.substring(2);
/*     */     }
/* 231 */     if (base.endsWith("/"))
/*     */     {
/* 233 */       if (info.startsWith("/"))
/*     */       {
/* 235 */         path = base + info.substring(1);
/*     */       }
/*     */       else
/*     */       {
/* 239 */         path = base + info;
/*     */       }
/*     */     }
/* 242 */     else if (info.startsWith("/"))
/*     */     {
/* 244 */       path = base + info;
/*     */     }
/*     */     else
/*     */     {
/* 248 */       path = base + "/" + info;
/*     */     }
/* 250 */     return path;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean isWildcardMatch(String path)
/*     */   {
/* 256 */     int cpl = this.specLength - 2;
/* 257 */     if ((this.group == PathSpecGroup.PREFIX_GLOB) && (path.regionMatches(0, this.pathSpec, 0, cpl)))
/*     */     {
/* 259 */       if ((path.length() == cpl) || ('/' == path.charAt(cpl)))
/*     */       {
/* 261 */         return true;
/*     */       }
/*     */     }
/* 264 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean matches(String path)
/*     */   {
/* 270 */     switch (this.group)
/*     */     {
/*     */     case EXACT: 
/* 273 */       return this.pathSpec.equals(path);
/*     */     case PREFIX_GLOB: 
/* 275 */       return isWildcardMatch(path);
/*     */     case SUFFIX_GLOB: 
/* 277 */       return path.regionMatches(path.length() - this.specLength + 1, this.pathSpec, 1, this.specLength - 1);
/*     */     
/*     */     case ROOT: 
/* 280 */       return "/".equals(path);
/*     */     
/*     */     case DEFAULT: 
/* 283 */       return true;
/*     */     }
/* 285 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\pathmap\ServletPathSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */