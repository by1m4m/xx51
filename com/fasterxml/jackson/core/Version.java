/*     */ package com.fasterxml.jackson.core;
/*     */ 
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
/*     */ public class Version
/*     */   implements Comparable<Version>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  21 */   private static final Version UNKNOWN_VERSION = new Version(0, 0, 0, null, null, null);
/*     */   
/*     */ 
/*     */ 
/*     */   protected final int _majorVersion;
/*     */   
/*     */ 
/*     */   protected final int _minorVersion;
/*     */   
/*     */ 
/*     */   protected final int _patchLevel;
/*     */   
/*     */ 
/*     */   protected final String _groupId;
/*     */   
/*     */ 
/*     */   protected final String _artifactId;
/*     */   
/*     */ 
/*     */   protected final String _snapshotInfo;
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Version(int major, int minor, int patchLevel, String snapshotInfo)
/*     */   {
/*  47 */     this(major, minor, patchLevel, snapshotInfo, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public Version(int major, int minor, int patchLevel, String snapshotInfo, String groupId, String artifactId)
/*     */   {
/*  53 */     this._majorVersion = major;
/*  54 */     this._minorVersion = minor;
/*  55 */     this._patchLevel = patchLevel;
/*  56 */     this._snapshotInfo = snapshotInfo;
/*  57 */     this._groupId = (groupId == null ? "" : groupId);
/*  58 */     this._artifactId = (artifactId == null ? "" : artifactId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */   public static Version unknownVersion() { return UNKNOWN_VERSION; }
/*     */   
/*  67 */   public boolean isUknownVersion() { return this == UNKNOWN_VERSION; }
/*  68 */   public boolean isSnapshot() { return (this._snapshotInfo != null) && (this._snapshotInfo.length() > 0); }
/*     */   
/*  70 */   public int getMajorVersion() { return this._majorVersion; }
/*  71 */   public int getMinorVersion() { return this._minorVersion; }
/*  72 */   public int getPatchLevel() { return this._patchLevel; }
/*     */   
/*  74 */   public String getGroupId() { return this._groupId; }
/*  75 */   public String getArtifactId() { return this._artifactId; }
/*     */   
/*     */   public String toFullString() {
/*  78 */     return this._groupId + '/' + this._artifactId + '/' + toString();
/*     */   }
/*     */   
/*     */   public String toString() {
/*  82 */     StringBuilder sb = new StringBuilder();
/*  83 */     sb.append(this._majorVersion).append('.');
/*  84 */     sb.append(this._minorVersion).append('.');
/*  85 */     sb.append(this._patchLevel);
/*  86 */     if (isSnapshot()) {
/*  87 */       sb.append('-').append(this._snapshotInfo);
/*     */     }
/*  89 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public int hashCode() {
/*  93 */     return this._artifactId.hashCode() ^ this._groupId.hashCode() + this._majorVersion - this._minorVersion + this._patchLevel;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  99 */     if (o == this) return true;
/* 100 */     if (o == null) return false;
/* 101 */     if (o.getClass() != getClass()) return false;
/* 102 */     Version other = (Version)o;
/* 103 */     return (other._majorVersion == this._majorVersion) && (other._minorVersion == this._minorVersion) && (other._patchLevel == this._patchLevel) && (other._artifactId.equals(this._artifactId)) && (other._groupId.equals(this._groupId));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(Version other)
/*     */   {
/* 114 */     if (other == this) { return 0;
/*     */     }
/* 116 */     int diff = this._groupId.compareTo(other._groupId);
/* 117 */     if (diff == 0) {
/* 118 */       diff = this._artifactId.compareTo(other._artifactId);
/* 119 */       if (diff == 0) {
/* 120 */         diff = this._majorVersion - other._majorVersion;
/* 121 */         if (diff == 0) {
/* 122 */           diff = this._minorVersion - other._minorVersion;
/* 123 */           if (diff == 0) {
/* 124 */             diff = this._patchLevel - other._patchLevel;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 129 */     return diff;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\Version.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */