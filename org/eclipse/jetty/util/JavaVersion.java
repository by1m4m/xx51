/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JavaVersion
/*     */ {
/*     */   public static final String JAVA_TARGET_PLATFORM = "org.eclipse.jetty.javaTargetPlatform";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  36 */   public static final JavaVersion VERSION = parse(System.getProperty("java.version"));
/*     */   private final String version;
/*     */   private final int platform;
/*     */   
/*     */   public static JavaVersion parse(String v) {
/*  41 */     String[] split = v.split("[^0-9]");
/*  42 */     int len = Math.min(split.length, 3);
/*  43 */     int[] version = new int[len];
/*  44 */     for (int i = 0; i < len; i++)
/*     */     {
/*     */       try
/*     */       {
/*  48 */         version[i] = Integer.parseInt(split[i]);
/*     */       }
/*     */       catch (Throwable e)
/*     */       {
/*  52 */         len = i - 1;
/*  53 */         break;
/*     */       }
/*     */     }
/*     */     
/*  57 */     return new JavaVersion(v, 
/*     */     
/*  59 */       (version[0] >= 9) || (len == 1) ? version[0] : version[1], version[0], 
/*     */       
/*  61 */       len > 1 ? version[1] : 0, 
/*  62 */       len > 2 ? version[2] : 0);
/*     */   }
/*     */   
/*     */ 
/*     */   private final int major;
/*     */   
/*     */   private final int minor;
/*     */   
/*     */   private final int micro;
/*     */   private JavaVersion(String version, int platform, int major, int minor, int micro)
/*     */   {
/*  73 */     this.version = version;
/*  74 */     this.platform = platform;
/*  75 */     this.major = major;
/*  76 */     this.minor = minor;
/*  77 */     this.micro = micro;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getVersion()
/*     */   {
/*  85 */     return this.version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPlatform()
/*     */   {
/*  95 */     return this.platform;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMajor()
/*     */   {
/* 105 */     return this.major;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMinor()
/*     */   {
/* 115 */     return this.minor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMicro()
/*     */   {
/* 125 */     return this.micro;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public int getUpdate()
/*     */   {
/* 136 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String getSuffix()
/*     */   {
/* 148 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 154 */     return this.version;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\JavaVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */