/*     */ package net.jpountz.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ public enum Native
/*     */ {
/*     */   private Native() {}
/*     */   
/*     */   private static enum OS
/*     */   {
/*  28 */     WINDOWS("win32", "so"),  LINUX("linux", "so"),  MAC("darwin", "dylib"),  SOLARIS("solaris", "so");
/*     */     
/*     */     public final String name;
/*     */     
/*  32 */     private OS(String name, String libExtension) { this.name = name;
/*  33 */       this.libExtension = libExtension;
/*     */     }
/*     */     
/*     */     public final String libExtension; }
/*     */   
/*  38 */   private static String arch() { return System.getProperty("os.arch"); }
/*     */   
/*     */ 
/*     */   private static OS os() {
/*  42 */     String osName = System.getProperty("os.name");
/*  43 */     if (osName.contains("Linux"))
/*  44 */       return OS.LINUX;
/*  45 */     if (osName.contains("Mac"))
/*  46 */       return OS.MAC;
/*  47 */     if (osName.contains("Windows"))
/*  48 */       return OS.WINDOWS;
/*  49 */     if ((osName.contains("Solaris")) || (osName.contains("SunOS"))) {
/*  50 */       return OS.SOLARIS;
/*     */     }
/*  52 */     throw new UnsupportedOperationException("Unsupported operating system: " + osName);
/*     */   }
/*     */   
/*     */ 
/*     */   private static String resourceName()
/*     */   {
/*  58 */     OS os = os();
/*  59 */     String packagePrefix = Native.class.getPackage().getName().replace('.', '/');
/*     */     
/*  61 */     return "/" + packagePrefix + "/" + os.name + "/" + arch() + "/liblz4-java." + os.libExtension;
/*     */   }
/*     */   
/*  64 */   private static boolean loaded = false;
/*     */   
/*     */   public static synchronized boolean isLoaded() {
/*  67 */     return loaded;
/*     */   }
/*     */   
/*     */   public static synchronized void load() {
/*  71 */     if (loaded) {
/*  72 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  77 */       System.loadLibrary("lz4-java");
/*  78 */       loaded = true;
/*  79 */       return;
/*     */ 
/*     */     }
/*     */     catch (UnsatisfiedLinkError localUnsatisfiedLinkError)
/*     */     {
/*  84 */       String resourceName = resourceName();
/*  85 */       InputStream is = Native.class.getResourceAsStream(resourceName);
/*  86 */       if (is == null) {
/*  87 */         throw new UnsupportedOperationException("Unsupported OS/arch, cannot find " + resourceName + ". Please try building from source.");
/*     */       }
/*     */       try
/*     */       {
/*  91 */         File tempLib = File.createTempFile("liblz4-java", "." + os().libExtension);
/*     */         
/*  93 */         FileOutputStream out = new FileOutputStream(tempLib);
/*     */         try {
/*  95 */           byte[] buf = new byte['က'];
/*     */           for (;;) {
/*  97 */             int read = is.read(buf);
/*  98 */             if (read == -1) {
/*     */               break;
/*     */             }
/* 101 */             out.write(buf, 0, read);
/*     */           }
/*     */           try {
/* 104 */             out.close();
/* 105 */             out = null;
/*     */           }
/*     */           catch (IOException localIOException1) {}
/*     */           
/* 109 */           System.load(tempLib.getAbsolutePath());
/* 110 */           loaded = true;
/*     */         } finally {
/*     */           try {
/* 113 */             if (out != null) {
/* 114 */               out.close();
/*     */             }
/*     */           }
/*     */           catch (IOException localIOException3) {}
/*     */           
/* 119 */           if ((tempLib != null) && (tempLib.exists())) {
/* 120 */             if (!loaded) {
/* 121 */               tempLib.delete();
/*     */             }
/*     */             else {
/* 124 */               tempLib.deleteOnExit();
/*     */             }
/*     */           }
/*     */         }
/*     */       } catch (IOException e) {
/* 129 */         throw new ExceptionInInitializerError("Cannot unpack liblz4-java");
/*     */       }
/*     */     }
/*     */     File tempLib;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\util\Native.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */