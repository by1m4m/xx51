/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.attribute.PosixFilePermission;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class NativeLibraryLoader
/*     */ {
/*     */   private static final InternalLogger logger;
/*     */   private static final String NATIVE_RESOURCE_HOME = "META-INF/native/";
/*     */   private static final File WORKDIR;
/*     */   private static final boolean DELETE_NATIVE_LIB_AFTER_LOADING;
/*     */   private static final boolean TRY_TO_PATCH_SHADED_ID;
/*     */   private static final byte[] UNIQUE_ID_BYTES;
/*     */   
/*     */   static
/*     */   {
/*  46 */     logger = InternalLoggerFactory.getInstance(NativeLibraryLoader.class);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */     UNIQUE_ID_BYTES = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes(CharsetUtil.US_ASCII);
/*     */     
/*     */ 
/*  58 */     String workdir = SystemPropertyUtil.get("io.netty.native.workdir");
/*  59 */     if (workdir != null) {
/*  60 */       File f = new File(workdir);
/*  61 */       f.mkdirs();
/*     */       try
/*     */       {
/*  64 */         f = f.getAbsoluteFile();
/*     */       }
/*     */       catch (Exception localException) {}
/*     */       
/*     */ 
/*  69 */       WORKDIR = f;
/*  70 */       logger.debug("-Dio.netty.native.workdir: " + WORKDIR);
/*     */     } else {
/*  72 */       WORKDIR = PlatformDependent.tmpdir();
/*  73 */       logger.debug("-Dio.netty.native.workdir: " + WORKDIR + " (io.netty.tmpdir)");
/*     */     }
/*     */     
/*  76 */     DELETE_NATIVE_LIB_AFTER_LOADING = SystemPropertyUtil.getBoolean("io.netty.native.deleteLibAfterLoading", true);
/*     */     
/*  78 */     logger.debug("-Dio.netty.native.deleteLibAfterLoading: {}", Boolean.valueOf(DELETE_NATIVE_LIB_AFTER_LOADING));
/*     */     
/*  80 */     TRY_TO_PATCH_SHADED_ID = SystemPropertyUtil.getBoolean("io.netty.native.tryPatchShadedId", true);
/*     */     
/*  82 */     logger.debug("-Dio.netty.native.tryPatchShadedId: {}", Boolean.valueOf(TRY_TO_PATCH_SHADED_ID));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void loadFirstAvailable(ClassLoader loader, String... names)
/*     */   {
/*  93 */     List<Throwable> suppressed = new ArrayList();
/*  94 */     for (String name : names) {
/*     */       try {
/*  96 */         load(name, loader);
/*  97 */         return;
/*     */       } catch (Throwable t) {
/*  99 */         suppressed.add(t);
/* 100 */         logger.debug("Unable to load the library '{}', trying next name...", name, t);
/*     */       }
/*     */     }
/*     */     
/* 104 */     IllegalArgumentException iae = new IllegalArgumentException("Failed to load any of the given libraries: " + Arrays.toString(names));
/* 105 */     ThrowableUtil.addSuppressedAndClear(iae, suppressed);
/* 106 */     throw iae;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String calculatePackagePrefix()
/*     */   {
/* 115 */     String maybeShaded = NativeLibraryLoader.class.getName();
/*     */     
/* 117 */     String expected = "io!netty!util!internal!NativeLibraryLoader".replace('!', '.');
/* 118 */     if (!maybeShaded.endsWith(expected)) {
/* 119 */       throw new UnsatisfiedLinkError(String.format("Could not find prefix added to %s to get %s. When shading, only adding a package prefix is supported", new Object[] { expected, maybeShaded }));
/*     */     }
/*     */     
/*     */ 
/* 123 */     return maybeShaded.substring(0, maybeShaded.length() - expected.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void load(String originalName, ClassLoader loader)
/*     */   {
/* 131 */     String packagePrefix = calculatePackagePrefix().replace('.', '_');
/* 132 */     String name = packagePrefix + originalName;
/* 133 */     List<Throwable> suppressed = new ArrayList();
/*     */     try
/*     */     {
/* 136 */       loadLibrary(loader, name, false);
/* 137 */       return;
/*     */     } catch (Throwable ex) {
/* 139 */       suppressed.add(ex);
/* 140 */       logger.debug("{} cannot be loaded from java.libary.path, now trying export to -Dio.netty.native.workdir: {}", new Object[] { name, WORKDIR, ex });
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 145 */       String libname = System.mapLibraryName(name);
/* 146 */       String path = "META-INF/native/" + libname;
/*     */       
/* 148 */       InputStream in = null;
/* 149 */       OutputStream out = null;
/* 150 */       File tmpFile = null;
/*     */       URL url;
/* 152 */       URL url; if (loader == null) {
/* 153 */         url = ClassLoader.getSystemResource(path);
/*     */       } else {
/* 155 */         url = loader.getResource(path);
/*     */       }
/*     */       try {
/* 158 */         if (url == null) {
/* 159 */           if (PlatformDependent.isOsx()) {
/* 160 */             String fileName = "META-INF/native/lib" + name + ".jnilib";
/*     */             
/* 162 */             if (loader == null) {
/* 163 */               url = ClassLoader.getSystemResource(fileName);
/*     */             } else {
/* 165 */               url = loader.getResource(fileName);
/*     */             }
/* 167 */             if (url == null) {
/* 168 */               FileNotFoundException fnf = new FileNotFoundException(fileName);
/* 169 */               ThrowableUtil.addSuppressedAndClear(fnf, suppressed);
/* 170 */               throw fnf;
/*     */             }
/*     */           } else {
/* 173 */             FileNotFoundException fnf = new FileNotFoundException(path);
/* 174 */             ThrowableUtil.addSuppressedAndClear(fnf, suppressed);
/* 175 */             throw fnf;
/*     */           }
/*     */         }
/*     */         
/* 179 */         int index = libname.lastIndexOf('.');
/* 180 */         String prefix = libname.substring(0, index);
/* 181 */         String suffix = libname.substring(index, libname.length());
/*     */         
/* 183 */         tmpFile = File.createTempFile(prefix, suffix, WORKDIR);
/* 184 */         in = url.openStream();
/* 185 */         out = new FileOutputStream(tmpFile);
/*     */         
/* 187 */         byte[] buffer = new byte[' '];
/*     */         
/* 189 */         if ((TRY_TO_PATCH_SHADED_ID) && (PlatformDependent.isOsx()) && (!packagePrefix.isEmpty()))
/*     */         {
/* 191 */           ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(in.available());
/*     */           int length;
/* 193 */           while ((length = in.read(buffer)) > 0) {
/* 194 */             byteArrayOutputStream.write(buffer, 0, length);
/*     */           }
/* 196 */           byteArrayOutputStream.flush();
/* 197 */           byte[] bytes = byteArrayOutputStream.toByteArray();
/* 198 */           byteArrayOutputStream.close();
/*     */           
/*     */ 
/* 201 */           patchShadedLibraryId(bytes, originalName, name);
/*     */           
/* 203 */           out.write(bytes);
/*     */         } else { int length;
/* 205 */           while ((length = in.read(buffer)) > 0) {
/* 206 */             out.write(buffer, 0, length);
/*     */           }
/*     */         }
/* 209 */         out.flush();
/*     */         
/*     */ 
/*     */ 
/* 213 */         closeQuietly(out);
/* 214 */         out = null;
/* 215 */         loadLibrary(loader, tmpFile.getPath(), true);
/*     */       } catch (UnsatisfiedLinkError e) {
/*     */         try {
/* 218 */           if ((tmpFile != null) && (tmpFile.isFile()) && (tmpFile.canRead()) && 
/* 219 */             (!NoexecVolumeDetector.canExecuteExecutable(tmpFile))) {
/* 220 */             logger.info("{} exists but cannot be executed even when execute permissions set; check volume for \"noexec\" flag; use -Dio.netty.native.workdir=[path] to set native working directory separately.", tmpFile
/*     */             
/*     */ 
/* 223 */               .getPath());
/*     */           }
/*     */         } catch (Throwable t) {
/* 226 */           suppressed.add(t);
/* 227 */           logger.debug("Error checking if {} is on a file store mounted with noexec", tmpFile, t);
/*     */         }
/*     */         
/* 230 */         ThrowableUtil.addSuppressedAndClear(e, suppressed);
/* 231 */         throw e;
/*     */       } catch (Exception e) {
/* 233 */         UnsatisfiedLinkError ule = new UnsatisfiedLinkError("could not load a native library: " + name);
/* 234 */         ule.initCause(e);
/* 235 */         ThrowableUtil.addSuppressedAndClear(ule, suppressed);
/* 236 */         throw ule;
/*     */       } finally {
/* 238 */         closeQuietly(in);
/* 239 */         closeQuietly(out);
/*     */         
/*     */ 
/*     */ 
/* 243 */         if ((tmpFile != null) && ((!DELETE_NATIVE_LIB_AFTER_LOADING) || (!tmpFile.delete()))) {
/* 244 */           tmpFile.deleteOnExit();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void patchShadedLibraryId(byte[] bytes, String originalName, String name)
/*     */   {
/* 255 */     byte[] nameBytes = originalName.getBytes(CharsetUtil.UTF_8);
/* 256 */     int idIdx = -1;
/*     */     
/*     */ 
/*     */     int j;
/*     */     
/*     */ 
/* 262 */     for (int i = 0; (i < bytes.length) && (bytes.length - i >= nameBytes.length); i++) {
/* 263 */       int idx = i;
/* 264 */       for (j = 0; (j < nameBytes.length) && 
/* 265 */             (bytes[(idx++)] == nameBytes[(j++)]);)
/*     */       {
/*     */ 
/* 268 */         if (j == nameBytes.length)
/*     */         {
/* 270 */           idIdx = i;
/*     */           break label85;
/*     */         }
/*     */       }
/*     */     }
/*     */     label85:
/* 276 */     if (idIdx == -1) {
/* 277 */       logger.debug("Was not able to find the ID of the shaded native library {}, can't adjust it.", name);
/*     */     }
/*     */     else {
/* 280 */       for (int i = 0; i < nameBytes.length; i++)
/*     */       {
/*     */ 
/* 283 */         bytes[(idIdx + i)] = UNIQUE_ID_BYTES[PlatformDependent.threadLocalRandom().nextInt(UNIQUE_ID_BYTES.length)];
/*     */       }
/*     */       
/* 286 */       if (logger.isDebugEnabled()) {
/* 287 */         logger.debug("Found the ID of the shaded native library {}. Replacing ID part {} with {}", new Object[] { name, originalName, new String(bytes, idIdx, nameBytes.length, CharsetUtil.UTF_8) });
/*     */       }
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
/*     */   private static void loadLibrary(ClassLoader loader, String name, boolean absolute)
/*     */   {
/* 301 */     Throwable suppressed = null;
/*     */     try
/*     */     {
/*     */       try {
/* 305 */         Class<?> newHelper = tryToLoadClass(loader, NativeLibraryUtil.class);
/* 306 */         loadLibraryByHelper(newHelper, name, absolute);
/* 307 */         logger.debug("Successfully loaded the library {}", name);
/* 308 */         return;
/*     */       } catch (UnsatisfiedLinkError e) {
/* 310 */         suppressed = e;
/* 311 */         logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e);
/*     */       } catch (Exception e) {
/* 313 */         suppressed = e;
/* 314 */         logger.debug("Unable to load the library '{}', trying other loading mechanism.", name, e);
/*     */       }
/* 316 */       NativeLibraryUtil.loadLibrary(name, absolute);
/* 317 */       logger.debug("Successfully loaded the library {}", name);
/*     */     } catch (UnsatisfiedLinkError ule) {
/* 319 */       if (suppressed != null) {
/* 320 */         ThrowableUtil.addSuppressed(ule, suppressed);
/*     */       }
/* 322 */       throw ule;
/*     */     }
/*     */   }
/*     */   
/*     */   private static void loadLibraryByHelper(Class<?> helper, final String name, final boolean absolute) throws UnsatisfiedLinkError
/*     */   {
/* 328 */     Object ret = AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run()
/*     */       {
/*     */         try
/*     */         {
/* 334 */           Method method = this.val$helper.getMethod("loadLibrary", new Class[] { String.class, Boolean.TYPE });
/* 335 */           method.setAccessible(true);
/* 336 */           return method.invoke(null, new Object[] { name, Boolean.valueOf(absolute) });
/*     */         } catch (Exception e) {
/* 338 */           return e;
/*     */         }
/*     */       }
/*     */     });
/* 342 */     if ((ret instanceof Throwable)) {
/* 343 */       Throwable t = (Throwable)ret;
/* 344 */       assert (!(t instanceof UnsatisfiedLinkError)) : (t + " should be a wrapper throwable");
/* 345 */       Throwable cause = t.getCause();
/* 346 */       if ((cause instanceof UnsatisfiedLinkError)) {
/* 347 */         throw ((UnsatisfiedLinkError)cause);
/*     */       }
/* 349 */       UnsatisfiedLinkError ule = new UnsatisfiedLinkError(t.getMessage());
/* 350 */       ule.initCause(t);
/* 351 */       throw ule;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Class<?> tryToLoadClass(ClassLoader loader, final Class<?> helper)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 365 */       return Class.forName(helper.getName(), false, loader);
/*     */     } catch (ClassNotFoundException e1) {
/* 367 */       if (loader == null)
/*     */       {
/* 369 */         throw e1;
/*     */       }
/*     */       try
/*     */       {
/* 373 */         final byte[] classBinary = classToByteArray(helper);
/* 374 */         (Class)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Class<?> run()
/*     */           {
/*     */             try
/*     */             {
/* 380 */               Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, Integer.TYPE, Integer.TYPE });
/*     */               
/* 382 */               defineClass.setAccessible(true);
/* 383 */               return (Class)defineClass.invoke(this.val$loader, new Object[] { helper.getName(), classBinary, Integer.valueOf(0), 
/* 384 */                 Integer.valueOf(classBinary.length) });
/*     */             } catch (Exception e) {
/* 386 */               throw new IllegalStateException("Define class failed!", e);
/*     */             }
/*     */           }
/*     */         });
/*     */       } catch (ClassNotFoundException e2) {
/* 391 */         ThrowableUtil.addSuppressed(e2, e1);
/* 392 */         throw e2;
/*     */       } catch (RuntimeException e2) {
/* 394 */         ThrowableUtil.addSuppressed(e2, e1);
/* 395 */         throw e2;
/*     */       } catch (Error e2) {
/* 397 */         ThrowableUtil.addSuppressed(e2, e1);
/* 398 */         throw e2;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static byte[] classToByteArray(Class<?> clazz)
/*     */     throws ClassNotFoundException
/*     */   {
/* 410 */     String fileName = clazz.getName();
/* 411 */     int lastDot = fileName.lastIndexOf('.');
/* 412 */     if (lastDot > 0) {
/* 413 */       fileName = fileName.substring(lastDot + 1);
/*     */     }
/* 415 */     URL classUrl = clazz.getResource(fileName + ".class");
/* 416 */     if (classUrl == null) {
/* 417 */       throw new ClassNotFoundException(clazz.getName());
/*     */     }
/* 419 */     byte[] buf = new byte['Ѐ'];
/* 420 */     ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
/* 421 */     InputStream in = null;
/*     */     try {
/* 423 */       in = classUrl.openStream();
/* 424 */       int r; while ((r = in.read(buf)) != -1) {
/* 425 */         out.write(buf, 0, r);
/*     */       }
/* 427 */       return out.toByteArray();
/*     */     } catch (IOException ex) {
/* 429 */       throw new ClassNotFoundException(clazz.getName(), ex);
/*     */     } finally {
/* 431 */       closeQuietly(in);
/* 432 */       closeQuietly(out);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void closeQuietly(Closeable c) {
/* 437 */     if (c != null) {
/*     */       try {
/* 439 */         c.close();
/*     */       }
/*     */       catch (IOException localIOException) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class NoexecVolumeDetector
/*     */   {
/*     */     private static boolean canExecuteExecutable(File file)
/*     */       throws IOException
/*     */     {
/* 453 */       if (PlatformDependent.javaVersion() < 7)
/*     */       {
/*     */ 
/* 456 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 460 */       if (file.canExecute()) {
/* 461 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 471 */       Set<PosixFilePermission> existingFilePermissions = Files.getPosixFilePermissions(file.toPath(), new LinkOption[0]);
/*     */       
/* 473 */       Set<PosixFilePermission> executePermissions = EnumSet.of(PosixFilePermission.OWNER_EXECUTE, PosixFilePermission.GROUP_EXECUTE, PosixFilePermission.OTHERS_EXECUTE);
/*     */       
/*     */ 
/* 476 */       if (existingFilePermissions.containsAll(executePermissions)) {
/* 477 */         return false;
/*     */       }
/*     */       
/* 480 */       Set<PosixFilePermission> newPermissions = EnumSet.copyOf(existingFilePermissions);
/* 481 */       newPermissions.addAll(executePermissions);
/* 482 */       Files.setPosixFilePermissions(file.toPath(), newPermissions);
/* 483 */       return file.canExecute();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\NativeLibraryLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */