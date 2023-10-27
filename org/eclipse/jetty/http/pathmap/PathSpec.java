/*     */ package org.eclipse.jetty.http.pathmap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class PathSpec
/*     */   implements Comparable<PathSpec>
/*     */ {
/*     */   protected String pathSpec;
/*     */   
/*     */ 
/*     */ 
/*     */   protected PathSpecGroup group;
/*     */   
/*     */ 
/*     */ 
/*     */   protected int pathDepth;
/*     */   
/*     */ 
/*     */ 
/*     */   protected int specLength;
/*     */   
/*     */ 
/*     */ 
/*     */   protected String prefix;
/*     */   
/*     */ 
/*     */ 
/*     */   protected String suffix;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(PathSpec other)
/*     */   {
/*  37 */     int diff = this.group.ordinal() - other.group.ordinal();
/*  38 */     if (diff != 0)
/*     */     {
/*  40 */       return diff;
/*     */     }
/*     */     
/*     */ 
/*  44 */     diff = other.specLength - this.specLength;
/*  45 */     if (diff != 0)
/*     */     {
/*  47 */       return diff;
/*     */     }
/*     */     
/*     */ 
/*  51 */     return this.pathSpec.compareTo(other.pathSpec);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  57 */     if (this == obj)
/*     */     {
/*  59 */       return true;
/*     */     }
/*  61 */     if (obj == null)
/*     */     {
/*  63 */       return false;
/*     */     }
/*  65 */     if (getClass() != obj.getClass())
/*     */     {
/*  67 */       return false;
/*     */     }
/*  69 */     PathSpec other = (PathSpec)obj;
/*  70 */     if (this.pathSpec == null)
/*     */     {
/*  72 */       if (other.pathSpec != null)
/*     */       {
/*  74 */         return false;
/*     */       }
/*     */     }
/*  77 */     else if (!this.pathSpec.equals(other.pathSpec))
/*     */     {
/*  79 */       return false;
/*     */     }
/*  81 */     return true;
/*     */   }
/*     */   
/*     */   public PathSpecGroup getGroup()
/*     */   {
/*  86 */     return this.group;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPathDepth()
/*     */   {
/*  98 */     return this.pathDepth;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String getPathInfo(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String getPathMatch(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDeclaration()
/*     */   {
/* 126 */     return this.pathSpec;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPrefix()
/*     */   {
/* 135 */     return this.prefix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSuffix()
/*     */   {
/* 144 */     return this.suffix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String getRelativePath(String paramString1, String paramString2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 161 */     int prime = 31;
/* 162 */     int result = 1;
/* 163 */     result = 31 * result + (this.pathSpec == null ? 0 : this.pathSpec.hashCode());
/* 164 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean matches(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 179 */     StringBuilder str = new StringBuilder();
/* 180 */     str.append(getClass().getSimpleName()).append("[\"");
/* 181 */     str.append(this.pathSpec);
/* 182 */     str.append("\",pathDepth=").append(this.pathDepth);
/* 183 */     str.append(",group=").append(this.group);
/* 184 */     str.append("]");
/* 185 */     return str.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\pathmap\PathSpec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */