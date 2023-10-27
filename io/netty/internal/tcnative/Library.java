/*     */ package io.netty.internal.tcnative;
/*     */ 
/*     */ import java.io.File;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Library
/*     */ {
/*  40 */   private static final String[] NAMES = { "netty_tcnative", "libnetty_tcnative" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String PROVIDED = "provided";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   private static Library _instance = null;
/*     */   
/*     */   private Library() throws Exception {
/*  53 */     boolean loaded = false;
/*  54 */     String path = System.getProperty("java.library.path");
/*  55 */     String[] paths = path.split(File.pathSeparator);
/*  56 */     StringBuilder err = new StringBuilder();
/*  57 */     for (int i = 0; i < NAMES.length; i++) {
/*     */       try {
/*  59 */         loadLibrary(NAMES[i]);
/*  60 */         loaded = true;
/*     */       } catch (ThreadDeath t) {
/*  62 */         throw t;
/*     */       } catch (VirtualMachineError t) {
/*  64 */         throw t;
/*     */       } catch (Throwable t) {
/*  66 */         String name = System.mapLibraryName(NAMES[i]);
/*  67 */         for (int j = 0; j < paths.length; j++) {
/*  68 */           File fd = new File(paths[j], name);
/*  69 */           if (fd.exists())
/*     */           {
/*  71 */             throw new RuntimeException(t);
/*     */           }
/*     */         }
/*  74 */         if (i > 0) {
/*  75 */           err.append(", ");
/*     */         }
/*  77 */         err.append(t.getMessage());
/*     */       }
/*  79 */       if (loaded) {
/*     */         break;
/*     */       }
/*     */     }
/*  83 */     if (!loaded) {
/*  84 */       throw new UnsatisfiedLinkError(err.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private Library(String libraryName) {
/*  89 */     if (!"provided".equals(libraryName)) {
/*  90 */       loadLibrary(libraryName);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void loadLibrary(String libraryName) {
/*  95 */     System.loadLibrary(calculatePackagePrefix().replace('.', '_') + libraryName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String calculatePackagePrefix()
/*     */   {
/* 104 */     String maybeShaded = Library.class.getName();
/*     */     
/* 106 */     String expected = "io!netty!internal!tcnative!Library".replace('!', '.');
/* 107 */     if (!maybeShaded.endsWith(expected)) {
/* 108 */       throw new UnsatisfiedLinkError(String.format("Could not find prefix added to %s to get %s. When shading, only adding a package prefix is supported", new Object[] { expected, maybeShaded }));
/*     */     }
/*     */     
/*     */ 
/* 112 */     return maybeShaded.substring(0, maybeShaded.length() - expected.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static native boolean initialize0();
/*     */   
/*     */ 
/*     */ 
/*     */   private static native boolean aprHasThreads();
/*     */   
/*     */ 
/*     */ 
/*     */   private static native int aprMajorVersion();
/*     */   
/*     */ 
/*     */   private static native String aprVersionString();
/*     */   
/*     */ 
/*     */   public static boolean initialize()
/*     */     throws Exception
/*     */   {
/* 134 */     return initialize("provided", null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean initialize(String libraryName, String engine)
/*     */     throws Exception
/*     */   {
/* 146 */     if (_instance == null) {
/* 147 */       _instance = libraryName == null ? new Library() : new Library(libraryName);
/*     */       
/* 149 */       if (aprMajorVersion() < 1)
/*     */       {
/* 151 */         throw new UnsatisfiedLinkError("Unsupported APR Version (" + aprVersionString() + ")");
/*     */       }
/*     */       
/* 154 */       if (!aprHasThreads()) {
/* 155 */         throw new UnsatisfiedLinkError("Missing APR_HAS_THREADS");
/*     */       }
/*     */     }
/* 158 */     return (initialize0()) && (SSL.initialize(engine) == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\internal\tcnative\Library.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */