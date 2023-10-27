/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class NativeLibrary
/*     */ {
/*     */   private long handle;
/*     */   private final String libraryName;
/*     */   private final String libraryPath;
/*  73 */   private final Map functions = new HashMap();
/*     */   
/*     */   final int callFlags;
/*     */   private String encoding;
/*     */   final Map options;
/*  78 */   private static final Map libraries = new HashMap();
/*  79 */   private static final Map searchPaths = Collections.synchronizedMap(new HashMap());
/*  80 */   private static final List librarySearchPath = new LinkedList();
/*     */   
/*     */ 
/*     */   private static final int DEFAULT_OPEN_OPTIONS = -1;
/*     */   
/*     */ 
/*     */ 
/*     */   private static String functionKey(String name, int flags, String encoding)
/*     */   {
/*  89 */     return name + "|" + flags + "|" + encoding;
/*     */   }
/*     */   
/*     */   private NativeLibrary(String libraryName, String libraryPath, long handle, Map options) {
/*  93 */     this.libraryName = getLibraryName(libraryName);
/*  94 */     this.libraryPath = libraryPath;
/*  95 */     this.handle = handle;
/*  96 */     Object option = options.get("calling-convention");
/*     */     
/*  98 */     int callingConvention = (option instanceof Number) ? ((Number)option).intValue() : 0;
/*  99 */     this.callFlags = callingConvention;
/* 100 */     this.options = options;
/* 101 */     this.encoding = ((String)options.get("string-encoding"));
/* 102 */     if (this.encoding == null) {
/* 103 */       this.encoding = Native.getDefaultStringEncoding();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 108 */     if ((Platform.isWindows()) && ("kernel32".equals(this.libraryName.toLowerCase()))) {
/* 109 */       synchronized (this.functions) {
/* 110 */         Function f = new Function(this, "GetLastError", 63, this.encoding) {
/*     */           Object invoke(Object[] args, Class returnType, boolean b) {
/* 112 */             return new Integer(Native.getLastError());
/*     */           }
/* 114 */         };
/* 115 */         this.functions.put(functionKey("GetLastError", this.callFlags, this.encoding), f);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static int openFlags(Map options)
/*     */   {
/* 122 */     Object opt = options.get("open-flags");
/* 123 */     if ((opt instanceof Number)) {
/* 124 */       return ((Number)opt).intValue();
/*     */     }
/* 126 */     return -1;
/*     */   }
/*     */   
/*     */   private static NativeLibrary loadLibrary(String libraryName, Map options) {
/* 130 */     if (Native.DEBUG_LOAD) {
/* 131 */       System.out.println("Looking for library '" + libraryName + "'");
/*     */     }
/*     */     
/* 134 */     boolean isAbsolutePath = new File(libraryName).isAbsolute();
/* 135 */     List searchPath = new LinkedList();
/* 136 */     int openFlags = openFlags(options);
/*     */     
/*     */ 
/*     */ 
/* 140 */     String webstartPath = Native.getWebStartLibraryPath(libraryName);
/* 141 */     if (webstartPath != null) {
/* 142 */       if (Native.DEBUG_LOAD) {
/* 143 */         System.out.println("Adding web start path " + webstartPath);
/*     */       }
/* 145 */       searchPath.add(webstartPath);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 151 */     List customPaths = (List)searchPaths.get(libraryName);
/* 152 */     if (customPaths != null) {
/* 153 */       synchronized (customPaths) {
/* 154 */         searchPath.addAll(0, customPaths);
/*     */       }
/*     */     }
/*     */     
/* 158 */     if (Native.DEBUG_LOAD) {
/* 159 */       System.out.println("Adding paths from jna.library.path: " + System.getProperty("jna.library.path"));
/*     */     }
/* 161 */     searchPath.addAll(initPaths("jna.library.path"));
/* 162 */     String libraryPath = findLibraryPath(libraryName, searchPath);
/* 163 */     long handle = 0L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 170 */       if (Native.DEBUG_LOAD) {
/* 171 */         System.out.println("Trying " + libraryPath);
/*     */       }
/* 173 */       handle = Native.open(libraryPath, openFlags);
/*     */     }
/*     */     catch (UnsatisfiedLinkError e)
/*     */     {
/* 177 */       if (Native.DEBUG_LOAD) {
/* 178 */         System.out.println("Adding system paths: " + librarySearchPath);
/*     */       }
/* 180 */       searchPath.addAll(librarySearchPath);
/*     */     }
/*     */     try {
/* 183 */       if (handle == 0L) {
/* 184 */         libraryPath = findLibraryPath(libraryName, searchPath);
/* 185 */         if (Native.DEBUG_LOAD) {
/* 186 */           System.out.println("Trying " + libraryPath);
/*     */         }
/* 188 */         handle = Native.open(libraryPath, openFlags);
/* 189 */         if (handle == 0L) {
/* 190 */           throw new UnsatisfiedLinkError("Failed to load library '" + libraryName + "'");
/*     */         }
/*     */         
/*     */       }
/*     */       
/*     */     }
/*     */     catch (UnsatisfiedLinkError e)
/*     */     {
/* 198 */       if (Platform.isAndroid()) {
/*     */         try {
/* 200 */           if (Native.DEBUG_LOAD) {
/* 201 */             System.out.println("Preload (via System.loadLibrary) " + libraryName);
/*     */           }
/* 203 */           System.loadLibrary(libraryName);
/* 204 */           handle = Native.open(libraryPath, openFlags);
/*     */         } catch (UnsatisfiedLinkError e2) {
/* 206 */           e = e2;
/*     */         }
/* 208 */       } else if ((Platform.isLinux()) || (Platform.isFreeBSD()))
/*     */       {
/*     */ 
/*     */ 
/* 212 */         if (Native.DEBUG_LOAD) {
/* 213 */           System.out.println("Looking for version variants");
/*     */         }
/* 215 */         libraryPath = matchLibrary(libraryName, searchPath);
/* 216 */         if (libraryPath != null) {
/* 217 */           if (Native.DEBUG_LOAD) {
/* 218 */             System.out.println("Trying " + libraryPath);
/*     */           }
/*     */           try {
/* 221 */             handle = Native.open(libraryPath, openFlags);
/*     */           } catch (UnsatisfiedLinkError e2) {
/* 223 */             e = e2;
/*     */           }
/*     */         }
/*     */       }
/* 227 */       else if ((Platform.isMac()) && 
/* 228 */         (!libraryName.endsWith(".dylib"))) {
/* 229 */         if (Native.DEBUG_LOAD) {
/* 230 */           System.out.println("Looking for matching frameworks");
/*     */         }
/* 232 */         libraryPath = matchFramework(libraryName);
/* 233 */         if (libraryPath != null) {
/*     */           try {
/* 235 */             if (Native.DEBUG_LOAD) {
/* 236 */               System.out.println("Trying " + libraryPath);
/*     */             }
/* 238 */             handle = Native.open(libraryPath, openFlags);
/*     */           } catch (UnsatisfiedLinkError e2) {
/* 240 */             e = e2;
/*     */           }
/*     */         }
/*     */       }
/* 244 */       else if ((Platform.isWindows()) && (!isAbsolutePath)) {
/* 245 */         if (Native.DEBUG_LOAD) {
/* 246 */           System.out.println("Looking for lib- prefix");
/*     */         }
/* 248 */         libraryPath = findLibraryPath("lib" + libraryName, searchPath);
/* 249 */         if (libraryPath != null) {
/* 250 */           if (Native.DEBUG_LOAD)
/* 251 */             System.out.println("Trying " + libraryPath);
/*     */           try {
/* 253 */             handle = Native.open(libraryPath, openFlags);
/* 254 */           } catch (UnsatisfiedLinkError e2) { e = e2;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 259 */       if (handle == 0L) {
/*     */         try {
/* 261 */           File embedded = Native.extractFromResourcePath(libraryName, (ClassLoader)options.get("classloader"));
/*     */           try {
/* 263 */             handle = Native.open(embedded.getAbsolutePath(), openFlags);
/* 264 */             libraryPath = embedded.getAbsolutePath();
/*     */           }
/*     */           finally {
/* 267 */             if (Native.isUnpacked(embedded)) {
/* 268 */               Native.deleteLibrary(embedded);
/*     */             }
/*     */           }
/*     */         } catch (IOException e2) {
/* 272 */           e = new UnsatisfiedLinkError(e2.getMessage());
/*     */         }
/*     */       }
/* 275 */       if (handle == 0L)
/*     */       {
/* 277 */         throw new UnsatisfiedLinkError("Unable to load library '" + libraryName + "': " + e.getMessage());
/*     */       }
/*     */     }
/* 280 */     if (Native.DEBUG_LOAD) {
/* 281 */       System.out.println("Found library '" + libraryName + "' at " + libraryPath);
/*     */     }
/* 283 */     return new NativeLibrary(libraryName, libraryPath, handle, options);
/*     */   }
/*     */   
/*     */   static String matchFramework(String libraryName)
/*     */   {
/* 288 */     File framework = new File(libraryName);
/* 289 */     if (framework.isAbsolute()) {
/* 290 */       if ((libraryName.indexOf(".framework") != -1) && 
/* 291 */         (framework.exists())) {
/* 292 */         return framework.getAbsolutePath();
/*     */       }
/* 294 */       framework = new File(new File(framework.getParentFile(), framework.getName() + ".framework"), framework.getName());
/* 295 */       if (framework.exists()) {
/* 296 */         return framework.getAbsolutePath();
/*     */       }
/*     */     }
/*     */     else {
/* 300 */       String[] PREFIXES = { System.getProperty("user.home"), "", "/System" };
/* 301 */       String suffix = libraryName.indexOf(".framework") == -1 ? libraryName + ".framework/" + libraryName : libraryName;
/*     */       
/* 303 */       for (int i = 0; i < PREFIXES.length; i++) {
/* 304 */         String libraryPath = PREFIXES[i] + "/Library/Frameworks/" + suffix;
/* 305 */         if (new File(libraryPath).exists()) {
/* 306 */           return libraryPath;
/*     */         }
/*     */       }
/*     */     }
/* 310 */     return null;
/*     */   }
/*     */   
/*     */   private String getLibraryName(String libraryName) {
/* 314 */     String simplified = libraryName;
/* 315 */     String BASE = "---";
/* 316 */     String template = mapSharedLibraryName("---");
/* 317 */     int prefixEnd = template.indexOf("---");
/* 318 */     if ((prefixEnd > 0) && (simplified.startsWith(template.substring(0, prefixEnd)))) {
/* 319 */       simplified = simplified.substring(prefixEnd);
/*     */     }
/* 321 */     String suffix = template.substring(prefixEnd + "---".length());
/* 322 */     int suffixStart = simplified.indexOf(suffix);
/* 323 */     if (suffixStart != -1) {
/* 324 */       simplified = simplified.substring(0, suffixStart);
/*     */     }
/* 326 */     return simplified;
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
/*     */   public static final NativeLibrary getInstance(String libraryName)
/*     */   {
/* 342 */     return getInstance(libraryName, Collections.EMPTY_MAP);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final NativeLibrary getInstance(String libraryName, ClassLoader classLoader)
/*     */   {
/* 362 */     Map map = new HashMap();
/* 363 */     map.put("classloader", classLoader);
/* 364 */     return getInstance(libraryName, map);
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
/*     */ 
/*     */ 
/*     */   public static final NativeLibrary getInstance(String libraryName, Map options)
/*     */   {
/* 383 */     options = new HashMap(options);
/* 384 */     if (options.get("calling-convention") == null) {
/* 385 */       options.put("calling-convention", new Integer(0));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 390 */     if (((Platform.isLinux()) || (Platform.isFreeBSD()) || (Platform.isAIX())) && 
/* 391 */       (Platform.C_LIBRARY_NAME.equals(libraryName))) {
/* 392 */       libraryName = null;
/*     */     }
/* 394 */     synchronized (libraries) {
/* 395 */       WeakReference ref = (WeakReference)libraries.get(libraryName + options);
/* 396 */       NativeLibrary library = ref != null ? (NativeLibrary)ref.get() : null;
/*     */       
/* 398 */       if (library == null) {
/* 399 */         if (libraryName == null) {
/* 400 */           library = new NativeLibrary("<process>", null, Native.open(null, openFlags(options)), options);
/*     */         }
/*     */         else {
/* 403 */           library = loadLibrary(libraryName, options);
/*     */         }
/* 405 */         ref = new WeakReference(library);
/* 406 */         libraries.put(library.getName() + options, ref);
/* 407 */         File file = library.getFile();
/* 408 */         if (file != null) {
/* 409 */           libraries.put(file.getAbsolutePath() + options, ref);
/* 410 */           libraries.put(file.getName() + options, ref);
/*     */         }
/*     */       }
/* 413 */       return library;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final synchronized NativeLibrary getProcess()
/*     */   {
/* 424 */     return getInstance(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final synchronized NativeLibrary getProcess(Map options)
/*     */   {
/* 434 */     return getInstance(null, options);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final void addSearchPath(String libraryName, String path)
/*     */   {
/* 446 */     synchronized (searchPaths) {
/* 447 */       List customPaths = (List)searchPaths.get(libraryName);
/* 448 */       if (customPaths == null) {
/* 449 */         customPaths = Collections.synchronizedList(new LinkedList());
/* 450 */         searchPaths.put(libraryName, customPaths);
/*     */       }
/*     */       
/* 453 */       customPaths.add(path);
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
/*     */ 
/*     */ 
/*     */   public Function getFunction(String functionName)
/*     */   {
/* 468 */     return getFunction(functionName, this.callFlags);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   Function getFunction(String name, Method method)
/*     */   {
/* 488 */     FunctionMapper mapper = (FunctionMapper)this.options.get("function-mapper");
/* 489 */     if (mapper != null) {
/* 490 */       name = mapper.getFunctionName(this, method);
/*     */     }
/*     */     
/* 493 */     String prefix = System.getProperty("jna.profiler.prefix", "$$YJP$$");
/* 494 */     if (name.startsWith(prefix)) {
/* 495 */       name = name.substring(prefix.length());
/*     */     }
/* 497 */     int flags = this.callFlags;
/* 498 */     Class[] etypes = method.getExceptionTypes();
/* 499 */     for (int i = 0; i < etypes.length; i++) {
/* 500 */       if (LastErrorException.class.isAssignableFrom(etypes[i])) {
/* 501 */         flags |= 0x40;
/*     */       }
/*     */     }
/* 504 */     return getFunction(name, flags);
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
/*     */   public Function getFunction(String functionName, int callFlags)
/*     */   {
/* 518 */     return getFunction(functionName, callFlags, this.encoding);
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
/*     */   public Function getFunction(String functionName, int callFlags, String encoding)
/*     */   {
/* 535 */     if (functionName == null)
/* 536 */       throw new NullPointerException("Function name may not be null");
/* 537 */     synchronized (this.functions) {
/* 538 */       String key = functionKey(functionName, callFlags, encoding);
/* 539 */       Function function = (Function)this.functions.get(key);
/* 540 */       if (function == null) {
/* 541 */         function = new Function(this, functionName, callFlags, encoding);
/* 542 */         this.functions.put(key, function);
/*     */       }
/* 544 */       return function;
/*     */     }
/*     */   }
/*     */   
/*     */   public Map getOptions()
/*     */   {
/* 550 */     return this.options;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Pointer getGlobalVariableAddress(String symbolName)
/*     */   {
/*     */     try
/*     */     {
/* 560 */       return new Pointer(getSymbolAddress(symbolName));
/*     */ 
/*     */     }
/*     */     catch (UnsatisfiedLinkError e)
/*     */     {
/* 565 */       throw new UnsatisfiedLinkError("Error looking up '" + symbolName + "': " + e.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   long getSymbolAddress(String name)
/*     */   {
/* 574 */     if (this.handle == 0L) {
/* 575 */       throw new UnsatisfiedLinkError("Library has been unloaded");
/*     */     }
/* 577 */     return Native.findSymbol(this.handle, name);
/*     */   }
/*     */   
/* 580 */   public String toString() { return "Native Library <" + this.libraryPath + "@" + this.handle + ">"; }
/*     */   
/*     */   public String getName()
/*     */   {
/* 584 */     return this.libraryName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public File getFile()
/*     */   {
/* 591 */     if (this.libraryPath == null)
/* 592 */       return null;
/* 593 */     return new File(this.libraryPath);
/*     */   }
/*     */   
/*     */   protected void finalize() {
/* 597 */     dispose();
/*     */   }
/*     */   
/*     */   static void disposeAll()
/*     */   {
/*     */     Set values;
/* 603 */     synchronized (libraries) {
/* 604 */       values = new HashSet(libraries.values());
/*     */     }
/* 606 */     for (Iterator i = values.iterator(); i.hasNext();) {
/* 607 */       Object ref = (WeakReference)i.next();
/* 608 */       NativeLibrary lib = (NativeLibrary)((Reference)ref).get();
/* 609 */       if (lib != null) {
/* 610 */         lib.dispose();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void dispose() {
/*     */     Iterator i;
/* 617 */     synchronized (libraries) {
/* 618 */       for (i = libraries.values().iterator(); i.hasNext();) {
/* 619 */         Reference ref = (WeakReference)i.next();
/* 620 */         if (ref.get() == this) {
/* 621 */           i.remove();
/*     */         }
/*     */       }
/*     */     }
/* 625 */     synchronized (this) {
/* 626 */       if (this.handle != 0L) {
/* 627 */         Native.close(this.handle);
/* 628 */         this.handle = 0L;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static List initPaths(String key) {
/* 634 */     String value = System.getProperty(key, "");
/* 635 */     if ("".equals(value)) {
/* 636 */       return Collections.EMPTY_LIST;
/*     */     }
/* 638 */     StringTokenizer st = new StringTokenizer(value, File.pathSeparator);
/* 639 */     List list = new ArrayList();
/* 640 */     while (st.hasMoreTokens()) {
/* 641 */       String path = st.nextToken();
/* 642 */       if (!"".equals(path)) {
/* 643 */         list.add(path);
/*     */       }
/*     */     }
/* 646 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String findLibraryPath(String libName, List searchPath)
/*     */   {
/* 655 */     if (new File(libName).isAbsolute()) {
/* 656 */       return libName;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 662 */     String name = mapSharedLibraryName(libName);
/*     */     
/*     */ 
/* 665 */     for (Iterator it = searchPath.iterator(); it.hasNext();) {
/* 666 */       String path = (String)it.next();
/* 667 */       File file = new File(path, name);
/* 668 */       if (file.exists()) {
/* 669 */         return file.getAbsolutePath();
/*     */       }
/* 671 */       if (Platform.isMac())
/*     */       {
/*     */ 
/* 674 */         if (name.endsWith(".dylib")) {
/* 675 */           file = new File(path, name.substring(0, name.lastIndexOf(".dylib")) + ".jnilib");
/* 676 */           if (file.exists()) {
/* 677 */             return file.getAbsolutePath();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 687 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static String mapSharedLibraryName(String libName)
/*     */   {
/* 695 */     if (Platform.isMac()) {
/* 696 */       if ((libName.startsWith("lib")) && (
/* 697 */         (libName.endsWith(".dylib")) || 
/* 698 */         (libName.endsWith(".jnilib")))) {
/* 699 */         return libName;
/*     */       }
/* 701 */       String name = System.mapLibraryName(libName);
/*     */       
/*     */ 
/*     */ 
/* 705 */       if (name.endsWith(".jnilib")) {
/* 706 */         return name.substring(0, name.lastIndexOf(".jnilib")) + ".dylib";
/*     */       }
/* 708 */       return name;
/*     */     }
/* 710 */     if ((Platform.isLinux()) || (Platform.isFreeBSD())) {
/* 711 */       if ((isVersionedName(libName)) || (libName.endsWith(".so")))
/*     */       {
/* 713 */         return libName;
/*     */       }
/*     */     }
/* 716 */     else if (Platform.isAIX()) {
/* 717 */       if (libName.startsWith("lib")) {
/* 718 */         return libName;
/*     */       }
/*     */     }
/* 721 */     else if ((Platform.isWindows()) && (
/* 722 */       (libName.endsWith(".drv")) || (libName.endsWith(".dll")))) {
/* 723 */       return libName;
/*     */     }
/*     */     
/*     */ 
/* 727 */     return System.mapLibraryName(libName);
/*     */   }
/*     */   
/*     */   private static boolean isVersionedName(String name) {
/* 731 */     if (name.startsWith("lib")) {
/* 732 */       int so = name.lastIndexOf(".so.");
/* 733 */       if ((so != -1) && (so + 4 < name.length())) {
/* 734 */         for (int i = so + 4; i < name.length(); i++) {
/* 735 */           char ch = name.charAt(i);
/* 736 */           if ((!Character.isDigit(ch)) && (ch != '.')) {
/* 737 */             return false;
/*     */           }
/*     */         }
/* 740 */         return true;
/*     */       }
/*     */     }
/* 743 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static String matchLibrary(String libName, List searchPath)
/*     */   {
/* 752 */     File lib = new File(libName);
/* 753 */     if (lib.isAbsolute()) {
/* 754 */       searchPath = Arrays.asList(new String[] { lib.getParent() });
/*     */     }
/* 756 */     FilenameFilter filter = new FilenameFilter()
/*     */     {
/*     */ 
/*     */       public boolean accept(File dir, String filename)
/*     */       {
/* 761 */         return ((filename.startsWith("lib" + this.val$libName + ".so")) || ((filename.startsWith(this.val$libName + ".so")) && (this.val$libName.startsWith("lib")))) && (NativeLibrary.isVersionedName(filename));
/*     */       }
/*     */       
/* 764 */     };
/* 765 */     List matches = new LinkedList();
/* 766 */     for (Iterator it = searchPath.iterator(); it.hasNext();) {
/* 767 */       File[] files = new File((String)it.next()).listFiles(filter);
/* 768 */       if ((files != null) && (files.length > 0)) {
/* 769 */         matches.addAll(Arrays.asList(files));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 776 */     double bestVersion = -1.0D;
/* 777 */     String bestMatch = null;
/* 778 */     for (Iterator it = matches.iterator(); it.hasNext();) {
/* 779 */       String path = ((File)it.next()).getAbsolutePath();
/* 780 */       String ver = path.substring(path.lastIndexOf(".so.") + 4);
/* 781 */       double version = parseVersion(ver);
/* 782 */       if (version > bestVersion) {
/* 783 */         bestVersion = version;
/* 784 */         bestMatch = path;
/*     */       }
/*     */     }
/* 787 */     return bestMatch;
/*     */   }
/*     */   
/*     */   static double parseVersion(String ver) {
/* 791 */     double v = 0.0D;
/* 792 */     double divisor = 1.0D;
/* 793 */     int dot = ver.indexOf(".");
/* 794 */     while (ver != null) {
/*     */       String num;
/* 796 */       if (dot != -1) {
/* 797 */         String num = ver.substring(0, dot);
/* 798 */         ver = ver.substring(dot + 1);
/* 799 */         dot = ver.indexOf(".");
/*     */       }
/*     */       else {
/* 802 */         num = ver;
/* 803 */         ver = null;
/*     */       }
/*     */       try {
/* 806 */         v += Integer.parseInt(num) / divisor;
/*     */       }
/*     */       catch (NumberFormatException e) {
/* 809 */         return 0.0D;
/*     */       }
/* 811 */       divisor *= 100.0D;
/*     */     }
/*     */     
/* 814 */     return v;
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*  84 */     if (Native.POINTER_SIZE == 0) {
/*  85 */       throw new Error("Native library not initialized");
/*     */     }
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
/* 818 */     String webstartPath = Native.getWebStartLibraryPath("jnidispatch");
/* 819 */     if (webstartPath != null) {
/* 820 */       librarySearchPath.add(webstartPath);
/*     */     }
/* 822 */     if ((System.getProperty("jna.platform.library.path") == null) && 
/* 823 */       (!Platform.isWindows()))
/*     */     {
/* 825 */       String platformPath = "";
/* 826 */       String sep = "";
/* 827 */       String archPath = "";
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
/* 840 */       if ((Platform.isLinux()) || (Platform.isSolaris()) || 
/* 841 */         (Platform.isFreeBSD()) || (Platform.iskFreeBSD()))
/*     */       {
/* 843 */         archPath = (Platform.isSolaris() ? "/" : "") + Pointer.SIZE * 8;
/*     */       }
/* 845 */       String[] paths = { "/usr/lib" + archPath, "/lib" + archPath, "/usr/lib", "/lib" };
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
/* 856 */       if ((Platform.isLinux()) || (Platform.iskFreeBSD()) || (Platform.isGNU())) {
/* 857 */         String multiArchPath = getMultiArchPath();
/*     */         
/*     */ 
/* 860 */         paths = new String[] { "/usr/lib/" + multiArchPath, "/lib/" + multiArchPath, "/usr/lib" + archPath, "/lib" + archPath, "/usr/lib", "/lib" };
/*     */       }
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
/* 876 */       if (Platform.isLinux()) {
/* 877 */         ArrayList<String> ldPaths = getLinuxLdPaths();
/*     */         
/* 879 */         for (int i = paths.length - 1; 0 <= i; i--) {
/* 880 */           int found = ldPaths.indexOf(paths[i]);
/* 881 */           if (found != -1) {
/* 882 */             ldPaths.remove(found);
/*     */           }
/* 884 */           ldPaths.add(0, paths[i]);
/*     */         }
/* 886 */         paths = (String[])ldPaths.toArray(new String[ldPaths.size()]);
/*     */       }
/*     */       
/* 889 */       for (int i = 0; i < paths.length; i++) {
/* 890 */         File dir = new File(paths[i]);
/* 891 */         if ((dir.exists()) && (dir.isDirectory())) {
/* 892 */           platformPath = platformPath + sep + paths[i];
/* 893 */           sep = File.pathSeparator;
/*     */         }
/*     */       }
/* 896 */       if (!"".equals(platformPath)) {
/* 897 */         System.setProperty("jna.platform.library.path", platformPath);
/*     */       }
/*     */     }
/* 900 */     librarySearchPath.addAll(initPaths("jna.platform.library.path"));
/*     */   }
/*     */   
/*     */   private static String getMultiArchPath() {
/* 904 */     String cpu = Platform.ARCH;
/*     */     
/*     */ 
/* 907 */     String kernel = Platform.isGNU() ? "" : Platform.iskFreeBSD() ? "-kfreebsd" : "-linux";
/* 908 */     String libc = "-gnu";
/*     */     
/* 910 */     if (Platform.isIntel()) {
/* 911 */       cpu = Platform.is64Bit() ? "x86_64" : "i386";
/*     */     }
/* 913 */     else if (Platform.isPPC()) {
/* 914 */       cpu = Platform.is64Bit() ? "powerpc64" : "powerpc";
/*     */     }
/* 916 */     else if (Platform.isARM()) {
/* 917 */       cpu = "arm";
/* 918 */       libc = "-gnueabi";
/*     */     }
/*     */     
/* 921 */     return cpu + kernel + libc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static ArrayList<String> getLinuxLdPaths()
/*     */   {
/* 928 */     ArrayList<String> ldPaths = new ArrayList();
/*     */     try {
/* 930 */       Process process = Runtime.getRuntime().exec("/sbin/ldconfig -p");
/* 931 */       BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
/* 932 */       String buffer = "";
/* 933 */       while ((buffer = reader.readLine()) != null) {
/* 934 */         int startPath = buffer.indexOf(" => ");
/* 935 */         int endPath = buffer.lastIndexOf('/');
/* 936 */         if ((startPath != -1) && (endPath != -1) && (startPath < endPath)) {
/* 937 */           String path = buffer.substring(startPath + 4, endPath);
/* 938 */           if (!ldPaths.contains(path)) {
/* 939 */             ldPaths.add(path);
/*     */           }
/*     */         }
/*     */       }
/* 943 */       reader.close();
/*     */     }
/*     */     catch (Exception e) {}
/* 946 */     return ldPaths;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\NativeLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */