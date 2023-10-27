/*     */ package oshi.software.common;
/*     */ 
/*     */ import oshi.software.os.OperatingSystemVersion;
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
/*     */ 
/*     */ public class AbstractOSVersionInfoEx
/*     */   implements OperatingSystemVersion
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected String version;
/*     */   protected String codeName;
/*     */   protected String versionStr;
/*     */   protected String buildNumber;
/*     */   
/*     */   public String getVersion()
/*     */   {
/*  45 */     return this.version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVersion(String version)
/*     */   {
/*  53 */     this.version = version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getCodeName()
/*     */   {
/*  61 */     return this.codeName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCodeName(String codeName)
/*     */   {
/*  69 */     this.codeName = codeName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getBuildNumber()
/*     */   {
/*  77 */     return this.buildNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBuildNumber(String buildNumber)
/*     */   {
/*  85 */     this.buildNumber = buildNumber;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  90 */     if (this.versionStr == null) {
/*  91 */       StringBuilder sb = new StringBuilder(getVersion() != null ? getVersion() : "Unknown");
/*  92 */       if (getCodeName().length() > 0) {
/*  93 */         sb.append(" (").append(getCodeName()).append(')');
/*     */       }
/*  95 */       if (getBuildNumber().length() > 0) {
/*  96 */         sb.append(" build ").append(getBuildNumber());
/*     */       }
/*  98 */       this.versionStr = sb.toString();
/*     */     }
/* 100 */     return this.versionStr;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\common\AbstractOSVersionInfoEx.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */