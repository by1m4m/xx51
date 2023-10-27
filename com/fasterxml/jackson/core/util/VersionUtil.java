/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.core.Versioned;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Properties;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionUtil
/*     */ {
/*  25 */   private static final Pattern V_SEP = Pattern.compile("[-_./;:]");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Version _v;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected VersionUtil()
/*     */   {
/*  37 */     Version v = null;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  42 */       v = versionFor(getClass());
/*     */     } catch (Exception e) {
/*  44 */       System.err.println("ERROR: Failed to load Version information from " + getClass());
/*     */     }
/*  46 */     if (v == null) {
/*  47 */       v = Version.unknownVersion();
/*     */     }
/*  49 */     this._v = v;
/*     */   }
/*     */   
/*  52 */   public Version version() { return this._v; }
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
/*     */   public static Version versionFor(Class<?> cls)
/*     */   {
/*  76 */     Version packageVersion = packageVersionFor(cls);
/*  77 */     if (packageVersion != null) {
/*  78 */       return packageVersion;
/*     */     }
/*  80 */     InputStream in = cls.getResourceAsStream("VERSION.txt");
/*  81 */     if (in == null) {
/*  82 */       return Version.unknownVersion();
/*     */     }
/*     */     try {
/*  85 */       InputStreamReader reader = new InputStreamReader(in, "UTF-8");
/*  86 */       return doReadVersion(reader);
/*     */     } catch (UnsupportedEncodingException e) { Version localVersion1;
/*  88 */       return Version.unknownVersion();
/*     */     } finally {
/*  90 */       _close(in);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Version packageVersionFor(Class<?> cls)
/*     */   {
/*     */     try
/*     */     {
/* 104 */       String versionInfoClassName = cls.getPackage().getName() + ".PackageVersion";
/* 105 */       Class<?> vClass = Class.forName(versionInfoClassName, true, cls.getClassLoader());
/*     */       try
/*     */       {
/* 108 */         return ((Versioned)vClass.newInstance()).version();
/*     */       } catch (Exception e) {
/* 110 */         throw new IllegalArgumentException("Failed to get Versioned out of " + vClass);
/*     */       }
/*     */       
/* 113 */       return null;
/*     */     }
/*     */     catch (Exception e) {}
/*     */   }
/*     */   
/*     */   private static Version doReadVersion(Reader r) {
/* 119 */     String version = null;String group = null;String artifact = null;
/*     */     
/* 121 */     BufferedReader br = new BufferedReader(r);
/*     */     try {
/* 123 */       version = br.readLine();
/* 124 */       if (version != null) {
/* 125 */         group = br.readLine();
/* 126 */         if (group != null) {
/* 127 */           artifact = br.readLine();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IOException ignored) {}finally {
/* 132 */       _close(br);
/*     */     }
/*     */     
/* 135 */     if (group != null) {
/* 136 */       group = group.trim();
/*     */     }
/* 138 */     if (artifact != null) {
/* 139 */       artifact = artifact.trim();
/*     */     }
/* 141 */     return parseVersion(version, group, artifact);
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
/*     */   public static Version mavenVersionFor(ClassLoader cl, String groupId, String artifactId)
/*     */   {
/* 158 */     InputStream pomProperties = cl.getResourceAsStream("META-INF/maven/" + groupId.replaceAll("\\.", "/") + "/" + artifactId + "/pom.properties");
/*     */     
/* 160 */     if (pomProperties != null) {
/*     */       try {
/* 162 */         Properties props = new Properties();
/* 163 */         props.load(pomProperties);
/* 164 */         String versionStr = props.getProperty("version");
/* 165 */         String pomPropertiesArtifactId = props.getProperty("artifactId");
/* 166 */         String pomPropertiesGroupId = props.getProperty("groupId");
/* 167 */         return parseVersion(versionStr, pomPropertiesGroupId, pomPropertiesArtifactId);
/*     */       }
/*     */       catch (IOException e) {}finally
/*     */       {
/* 171 */         _close(pomProperties);
/*     */       }
/*     */     }
/* 174 */     return Version.unknownVersion();
/*     */   }
/*     */   
/*     */   public static Version parseVersion(String s, String groupId, String artifactId)
/*     */   {
/* 179 */     if ((s != null) && ((s = s.trim()).length() > 0)) {
/* 180 */       String[] parts = V_SEP.split(s);
/* 181 */       return new Version(parseVersionPart(parts[0]), parts.length > 1 ? parseVersionPart(parts[1]) : 0, parts.length > 2 ? parseVersionPart(parts[2]) : 0, parts.length > 3 ? parts[3] : null, groupId, artifactId);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 187 */     return null;
/*     */   }
/*     */   
/*     */   protected static int parseVersionPart(String s) {
/* 191 */     int number = 0;
/* 192 */     int i = 0; for (int len = s.length(); i < len; i++) {
/* 193 */       char c = s.charAt(i);
/* 194 */       if ((c > '9') || (c < '0')) break;
/* 195 */       number = number * 10 + (c - '0');
/*     */     }
/* 197 */     return number;
/*     */   }
/*     */   
/*     */   private static final void _close(Closeable c) {
/*     */     try {
/* 202 */       c.close();
/*     */     }
/*     */     catch (IOException e) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void throwInternal()
/*     */   {
/* 213 */     throw new RuntimeException("Internal error: this code path should never get executed");
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\core\util\VersionUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */