/*      */ package com.sun.jna;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Window;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FilenameFilter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationHandler;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.WeakHashMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Native
/*      */   implements Version
/*      */ {
/*      */   public static final String DEFAULT_ENCODING = "utf8";
/*   99 */   public static boolean DEBUG_LOAD = Boolean.getBoolean("jna.debug_load");
/*  100 */   public static boolean DEBUG_JNA_LOAD = Boolean.getBoolean("jna.debug_load.jna");
/*      */   
/*      */ 
/*  103 */   static String jnidispatchPath = null;
/*  104 */   private static Map options = new WeakHashMap();
/*  105 */   private static Map libraries = new WeakHashMap();
/*  106 */   private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler()
/*      */   {
/*      */     public void uncaughtException(Callback c, Throwable e)
/*      */     {
/*  110 */       System.err.println("JNA: Callback " + c + " threw the following exception:");
/*  111 */       e.printStackTrace();
/*      */     }
/*      */   };
/*  114 */   private static Callback.UncaughtExceptionHandler callbackExceptionHandler = DEFAULT_HANDLER;
/*      */   
/*      */   public static final int POINTER_SIZE;
/*      */   
/*      */   public static final int LONG_SIZE;
/*      */   
/*      */   public static final int WCHAR_SIZE;
/*      */   
/*      */   public static final int SIZE_T_SIZE;
/*      */   
/*      */   private static final int TYPE_VOIDP = 0;
/*      */   
/*      */   private static final int TYPE_LONG = 1;
/*      */   
/*      */   private static final int TYPE_WCHAR_T = 2;
/*      */   
/*      */   private static final int TYPE_SIZE_T = 3;
/*      */   static final int MAX_ALIGNMENT;
/*      */   static final int MAX_PADDING;
/*      */   
/*      */   public static float parseVersion(String v)
/*      */   {
/*  136 */     return Float.parseFloat(v.substring(0, v.lastIndexOf(".")));
/*      */   }
/*      */   
/*      */   static {
/*  140 */     loadNativeDispatchLibrary();
/*  141 */     POINTER_SIZE = sizeof(0);
/*  142 */     LONG_SIZE = sizeof(1);
/*  143 */     WCHAR_SIZE = sizeof(2);
/*  144 */     SIZE_T_SIZE = sizeof(3);
/*      */     
/*      */ 
/*      */ 
/*  148 */     initIDs();
/*  149 */     if (Boolean.getBoolean("jna.protected")) {
/*  150 */       setProtected(true);
/*      */     }
/*  152 */     float version = parseVersion(getNativeVersion());
/*  153 */     if (version != parseVersion("4.0.1")) {
/*  154 */       String LS = System.getProperty("line.separator");
/*      */       
/*      */ 
/*      */ 
/*  158 */       throw new Error(LS + LS + "There is an incompatible JNA native library installed on this system" + LS + (jnidispatchPath != null ? "(at " + jnidispatchPath + ")" : System.getProperty("java.library.path")) + "." + LS + "To resolve this issue you may do one of the following:" + LS + " - remove or uninstall the offending library" + LS + " - set the system property jna.nosys=true" + LS + " - set jna.boot.library.path to include the path to the version of the " + LS + "   jnidispatch library included with the JNA jar file you are using" + LS);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  169 */     MAX_ALIGNMENT = (Platform.isSPARC()) || (Platform.isWindows()) || ((Platform.isLinux()) && ((Platform.isARM()) || (Platform.isPPC()))) || (Platform.isAIX()) || (Platform.isAndroid()) ? 8 : LONG_SIZE;
/*      */     
/*  171 */     MAX_PADDING = (Platform.isMac()) && (Platform.isPPC()) ? 8 : MAX_ALIGNMENT;
/*  172 */     System.setProperty("jna.loaded", "true");
/*      */   }
/*      */   
/*      */ 
/*  176 */   private static final Object finalizer = new Object()
/*      */   {
/*      */     protected void finalize() {}
/*      */   };
/*      */   
/*      */ 
/*      */   static final String JNA_TMPLIB_PREFIX = "jna";
/*      */   
/*      */ 
/*      */ 
/*      */   private static void dispose()
/*      */   {
/*  188 */     CallbackReference.disposeAll();
/*  189 */     Memory.disposeAll();
/*  190 */     NativeLibrary.disposeAll();
/*  191 */     unregisterAll();
/*  192 */     jnidispatchPath = null;
/*  193 */     System.setProperty("jna.loaded", "false");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static boolean deleteLibrary(File lib)
/*      */   {
/*  208 */     if (lib.delete()) {
/*  209 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  213 */     markTemporaryFile(lib);
/*      */     
/*  215 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public static boolean getPreserveLastError()
/*      */   {
/*  261 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static long getWindowID(Window w)
/*      */     throws HeadlessException
/*      */   {
/*  270 */     return AWT.getWindowID(w);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static long getComponentID(Component c)
/*      */     throws HeadlessException
/*      */   {
/*  280 */     return AWT.getComponentID(c);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Pointer getWindowPointer(Window w)
/*      */     throws HeadlessException
/*      */   {
/*  290 */     return new Pointer(AWT.getWindowID(w));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Pointer getComponentPointer(Component c)
/*      */     throws HeadlessException
/*      */   {
/*  300 */     return new Pointer(AWT.getComponentID(c));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Pointer getDirectBufferPointer(Buffer b)
/*      */   {
/*  309 */     long peer = _getDirectBufferPointer(b);
/*  310 */     return peer == 0L ? null : new Pointer(peer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toString(byte[] buf)
/*      */   {
/*  324 */     return toString(buf, getDefaultStringEncoding());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toString(byte[] buf, String encoding)
/*      */   {
/*  337 */     int len = buf.length;
/*      */     
/*  339 */     for (int index = 0; index < len; index++) {
/*  340 */       if (buf[index] == 0) {
/*  341 */         len = index;
/*  342 */         break;
/*      */       }
/*      */     }
/*      */     
/*  346 */     if (len == 0) {
/*  347 */       return "";
/*      */     }
/*      */     
/*  350 */     if (encoding != null) {
/*      */       try {
/*  352 */         return new String(buf, 0, len, encoding);
/*      */       }
/*      */       catch (UnsupportedEncodingException e) {
/*  355 */         System.err.println("JNA Warning: Encoding '" + encoding + "' is unsupported");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  360 */     System.err.println("JNA Warning: Decoding with fallback " + System.getProperty("file.encoding"));
/*  361 */     return new String(buf, 0, len);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String toString(char[] buf)
/*      */   {
/*  371 */     int len = buf.length;
/*  372 */     for (int index = 0; index < len; index++) {
/*  373 */       if (buf[index] == 0) {
/*  374 */         len = index;
/*  375 */         break;
/*      */       }
/*      */     }
/*      */     
/*  379 */     if (len == 0) {
/*  380 */       return "";
/*      */     }
/*  382 */     return new String(buf, 0, len);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static List<String> toStringList(char[] buf)
/*      */   {
/*  396 */     return toStringList(buf, 0, buf.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static List<String> toStringList(char[] buf, int offset, int len)
/*      */   {
/*  410 */     List<String> list = new ArrayList();
/*  411 */     int lastPos = offset;
/*  412 */     int maxPos = offset + len;
/*  413 */     for (int curPos = offset; curPos < maxPos; curPos++) {
/*  414 */       if (buf[curPos] == 0)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  419 */         if (lastPos == curPos) {
/*  420 */           return list;
/*      */         }
/*      */         
/*  423 */         String value = new String(buf, lastPos, curPos - lastPos);
/*  424 */         list.add(value);
/*  425 */         lastPos = curPos + 1;
/*      */       }
/*      */     }
/*      */     
/*  429 */     if (lastPos < maxPos) {
/*  430 */       String value = new String(buf, lastPos, maxPos - lastPos);
/*  431 */       list.add(value);
/*      */     }
/*      */     
/*  434 */     return list;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object loadLibrary(Class interfaceClass)
/*      */   {
/*  448 */     return loadLibrary(null, interfaceClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object loadLibrary(Class interfaceClass, Map options)
/*      */   {
/*  465 */     return loadLibrary(null, interfaceClass, options);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object loadLibrary(String name, Class interfaceClass)
/*      */   {
/*  481 */     return loadLibrary(name, interfaceClass, Collections.EMPTY_MAP);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Object loadLibrary(String name, Class interfaceClass, Map options)
/*      */   {
/*  502 */     Library.Handler handler = new Library.Handler(name, interfaceClass, options);
/*      */     
/*  504 */     ClassLoader loader = interfaceClass.getClassLoader();
/*      */     
/*  506 */     Library proxy = (Library)Proxy.newProxyInstance(loader, new Class[] { interfaceClass }, handler);
/*      */     
/*  508 */     cacheOptions(interfaceClass, options, proxy);
/*  509 */     return proxy;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void loadLibraryInstance(Class cls)
/*      */   {
/*  518 */     synchronized (libraries) {
/*  519 */       if ((cls != null) && (!libraries.containsKey(cls))) {
/*      */         try {
/*  521 */           Field[] fields = cls.getFields();
/*  522 */           for (int i = 0; i < fields.length; i++) {
/*  523 */             Field field = fields[i];
/*  524 */             if ((field.getType() == cls) && 
/*  525 */               (Modifier.isStatic(field.getModifiers())))
/*      */             {
/*  527 */               libraries.put(cls, new WeakReference(field.get(null)));
/*  528 */               break;
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (Exception e) {
/*  533 */           throw new IllegalArgumentException("Could not access instance of " + cls + " (" + e + ")");
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static Class findEnclosingLibraryClass(Class cls)
/*      */   {
/*  545 */     if (cls == null) {
/*  546 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  550 */     synchronized (libraries) {
/*  551 */       if (options.containsKey(cls)) {
/*  552 */         return cls;
/*      */       }
/*      */     }
/*  555 */     if (Library.class.isAssignableFrom(cls)) {
/*  556 */       return cls;
/*      */     }
/*  558 */     if (Callback.class.isAssignableFrom(cls)) {
/*  559 */       cls = CallbackReference.findCallbackClass(cls);
/*      */     }
/*  561 */     Class declaring = cls.getDeclaringClass();
/*  562 */     Class fromDeclaring = findEnclosingLibraryClass(declaring);
/*  563 */     if (fromDeclaring != null) {
/*  564 */       return fromDeclaring;
/*      */     }
/*  566 */     return findEnclosingLibraryClass(cls.getSuperclass());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Map getLibraryOptions(Class type)
/*      */   {
/*  581 */     synchronized (libraries) {
/*  582 */       if (options.containsKey(type)) {
/*  583 */         return (Map)options.get(type);
/*      */       }
/*      */     }
/*  586 */     Class mappingClass = findEnclosingLibraryClass(type);
/*  587 */     if (mappingClass != null) {
/*  588 */       loadLibraryInstance(mappingClass);
/*      */     }
/*      */     else {
/*  591 */       mappingClass = type;
/*      */     }
/*  593 */     synchronized (libraries) {
/*  594 */       if (options.containsKey(mappingClass)) {
/*  595 */         Map libraryOptions = (Map)options.get(mappingClass);
/*  596 */         options.put(type, libraryOptions);
/*  597 */         return libraryOptions;
/*      */       }
/*  599 */       Map libraryOptions = null;
/*      */       try {
/*  601 */         Field field = mappingClass.getField("OPTIONS");
/*  602 */         field.setAccessible(true);
/*  603 */         libraryOptions = (Map)field.get(null);
/*      */       }
/*      */       catch (NoSuchFieldException e) {
/*  606 */         libraryOptions = Collections.EMPTY_MAP;
/*      */       }
/*      */       catch (Exception e) {
/*  609 */         throw new IllegalArgumentException("OPTIONS must be a public field of type java.util.Map (" + e + "): " + mappingClass);
/*      */       }
/*      */       
/*      */ 
/*  613 */       libraryOptions = new HashMap(libraryOptions);
/*  614 */       if (!libraryOptions.containsKey("type-mapper")) {
/*  615 */         libraryOptions.put("type-mapper", lookupField(mappingClass, "TYPE_MAPPER", TypeMapper.class));
/*      */       }
/*  617 */       if (!libraryOptions.containsKey("structure-alignment")) {
/*  618 */         libraryOptions.put("structure-alignment", lookupField(mappingClass, "STRUCTURE_ALIGNMENT", Integer.class));
/*      */       }
/*  620 */       if (!libraryOptions.containsKey("string-encoding")) {
/*  621 */         libraryOptions.put("string-encoding", lookupField(mappingClass, "STRING_ENCODING", String.class));
/*      */       }
/*  623 */       options.put(mappingClass, libraryOptions);
/*      */       
/*  625 */       if (type != mappingClass) {
/*  626 */         options.put(type, libraryOptions);
/*      */       }
/*  628 */       return libraryOptions;
/*      */     }
/*      */   }
/*      */   
/*      */   private static Object lookupField(Class mappingClass, String fieldName, Class resultClass) {
/*      */     try {
/*  634 */       Field field = mappingClass.getField(fieldName);
/*  635 */       field.setAccessible(true);
/*  636 */       return field.get(null);
/*      */     }
/*      */     catch (NoSuchFieldException e) {
/*  639 */       return null;
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  643 */       throw new IllegalArgumentException(fieldName + " must be a public field of type " + resultClass.getName() + " (" + e + "): " + mappingClass);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static TypeMapper getTypeMapper(Class cls)
/*      */   {
/*  652 */     return (TypeMapper)getLibraryOptions(cls).get("type-mapper");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getStringEncoding(Class cls)
/*      */   {
/*  661 */     String encoding = (String)getLibraryOptions(cls).get("string-encoding");
/*  662 */     return encoding != null ? encoding : getDefaultStringEncoding();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static String getDefaultStringEncoding()
/*      */   {
/*  669 */     return System.getProperty("jna.encoding", "utf8");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int getStructureAlignment(Class cls)
/*      */   {
/*  676 */     Integer alignment = (Integer)getLibraryOptions(cls).get("structure-alignment");
/*  677 */     return alignment == null ? 0 : alignment.intValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static byte[] getBytes(String s)
/*      */   {
/*  684 */     return getBytes(s, getDefaultStringEncoding());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static byte[] getBytes(String s, String encoding)
/*      */   {
/*  692 */     if (encoding != null) {
/*      */       try {
/*  694 */         return s.getBytes(encoding);
/*      */       }
/*      */       catch (UnsupportedEncodingException e) {
/*  697 */         System.err.println("JNA Warning: Encoding '" + encoding + "' is unsupported");
/*      */       }
/*      */     }
/*      */     
/*  701 */     System.err.println("JNA Warning: Encoding with fallback " + 
/*  702 */       System.getProperty("file.encoding"));
/*  703 */     return s.getBytes();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static byte[] toByteArray(String s)
/*      */   {
/*  710 */     return toByteArray(s, getDefaultStringEncoding());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static byte[] toByteArray(String s, String encoding)
/*      */   {
/*  717 */     byte[] bytes = getBytes(s, encoding);
/*  718 */     byte[] buf = new byte[bytes.length + 1];
/*  719 */     System.arraycopy(bytes, 0, buf, 0, bytes.length);
/*  720 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static char[] toCharArray(String s)
/*      */   {
/*  727 */     char[] chars = s.toCharArray();
/*  728 */     char[] buf = new char[chars.length + 1];
/*  729 */     System.arraycopy(chars, 0, buf, 0, chars.length);
/*  730 */     return buf;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void loadNativeDispatchLibrary()
/*      */   {
/*  740 */     if (!Boolean.getBoolean("jna.nounpack")) {
/*      */       try {
/*  742 */         removeTemporaryFiles();
/*      */       }
/*      */       catch (IOException e) {
/*  745 */         System.err.println("JNA Warning: IOException removing temporary files: " + e.getMessage());
/*      */       }
/*      */     }
/*      */     
/*  749 */     String libName = System.getProperty("jna.boot.library.name", "jnidispatch");
/*  750 */     String bootPath = System.getProperty("jna.boot.library.path");
/*  751 */     if (bootPath != null)
/*      */     {
/*  753 */       StringTokenizer dirs = new StringTokenizer(bootPath, File.pathSeparator);
/*  754 */       while (dirs.hasMoreTokens()) {
/*  755 */         String dir = dirs.nextToken();
/*  756 */         File file = new File(new File(dir), System.mapLibraryName(libName).replace(".dylib", ".jnilib"));
/*  757 */         String path = file.getAbsolutePath();
/*  758 */         if (DEBUG_JNA_LOAD) {
/*  759 */           System.out.println("Looking in " + path);
/*      */         }
/*  761 */         if (file.exists()) {
/*      */           try {
/*  763 */             if (DEBUG_JNA_LOAD) {
/*  764 */               System.out.println("Trying " + path);
/*      */             }
/*  766 */             System.setProperty("jnidispatch.path", path);
/*  767 */             System.load(path);
/*  768 */             jnidispatchPath = path;
/*  769 */             if (DEBUG_JNA_LOAD) {
/*  770 */               System.out.println("Found jnidispatch at " + path);
/*      */             }
/*  772 */             return;
/*      */           }
/*      */           catch (UnsatisfiedLinkError ex) {}
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  779 */         if (Platform.isMac()) { String ext;
/*      */           String orig;
/*  781 */           String ext; if (path.endsWith("dylib")) {
/*  782 */             String orig = "dylib";
/*  783 */             ext = "jnilib";
/*      */           } else {
/*  785 */             orig = "jnilib";
/*  786 */             ext = "dylib";
/*      */           }
/*  788 */           path = path.substring(0, path.lastIndexOf(orig)) + ext;
/*  789 */           if (DEBUG_JNA_LOAD) {
/*  790 */             System.out.println("Looking in " + path);
/*      */           }
/*  792 */           if (new File(path).exists()) {
/*      */             try {
/*  794 */               if (DEBUG_JNA_LOAD) {
/*  795 */                 System.out.println("Trying " + path);
/*      */               }
/*  797 */               System.setProperty("jnidispatch.path", path);
/*  798 */               System.load(path);
/*  799 */               jnidispatchPath = path;
/*  800 */               if (DEBUG_JNA_LOAD) {
/*  801 */                 System.out.println("Found jnidispatch at " + path);
/*      */               }
/*  803 */               return;
/*      */             } catch (UnsatisfiedLinkError ex) {
/*  805 */               System.err.println("File found at " + path + " but not loadable: " + ex.getMessage());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  811 */     if (!Boolean.getBoolean("jna.nosys")) {
/*      */       try {
/*  813 */         if (DEBUG_JNA_LOAD) {
/*  814 */           System.out.println("Trying (via loadLibrary) " + libName);
/*      */         }
/*  816 */         System.loadLibrary(libName);
/*  817 */         if (DEBUG_JNA_LOAD) {
/*  818 */           System.out.println("Found jnidispatch on system path");
/*      */         }
/*  820 */         return;
/*      */       }
/*      */       catch (UnsatisfiedLinkError e) {}
/*      */     }
/*      */     
/*  825 */     if (!Boolean.getBoolean("jna.noclasspath")) {
/*  826 */       loadNativeDispatchLibraryFromClasspath();
/*      */     }
/*      */     else {
/*  829 */       throw new UnsatisfiedLinkError("Unable to locate JNA native support library");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void loadNativeDispatchLibraryFromClasspath()
/*      */   {
/*      */     try
/*      */     {
/*  840 */       String libName = "/com/sun/jna/" + Platform.RESOURCE_PREFIX + "/" + System.mapLibraryName("jnidispatch").replace(".dylib", ".jnilib");
/*  841 */       File lib = extractFromResourcePath(libName, Native.class.getClassLoader());
/*  842 */       if ((lib == null) && 
/*  843 */         (lib == null)) {
/*  844 */         throw new UnsatisfiedLinkError("Could not find JNA native support");
/*      */       }
/*      */       
/*  847 */       if (DEBUG_JNA_LOAD) {
/*  848 */         System.out.println("Trying " + lib.getAbsolutePath());
/*      */       }
/*  850 */       System.setProperty("jnidispatch.path", lib.getAbsolutePath());
/*  851 */       System.load(lib.getAbsolutePath());
/*  852 */       jnidispatchPath = lib.getAbsolutePath();
/*  853 */       if (DEBUG_JNA_LOAD) {
/*  854 */         System.out.println("Found jnidispatch at " + jnidispatchPath);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  860 */       if ((isUnpacked(lib)) && 
/*  861 */         (!Boolean.getBoolean("jnidispatch.preserve"))) {
/*  862 */         deleteLibrary(lib);
/*      */       }
/*      */     }
/*      */     catch (IOException e) {
/*  866 */       throw new UnsatisfiedLinkError(e.getMessage());
/*      */     }
/*      */   }
/*      */   
/*      */   static boolean isUnpacked(File file)
/*      */   {
/*  872 */     return file.getName().startsWith("jna");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static File extractFromResourcePath(String name)
/*      */     throws IOException
/*      */   {
/*  887 */     return extractFromResourcePath(name, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static File extractFromResourcePath(String name, ClassLoader loader)
/*      */     throws IOException
/*      */   {
/*  904 */     boolean DEBUG = (DEBUG_LOAD) || ((DEBUG_JNA_LOAD) && (name.indexOf("jnidispatch") != -1));
/*  905 */     if (loader == null) {
/*  906 */       loader = Thread.currentThread().getContextClassLoader();
/*      */       
/*  908 */       if (loader == null) {
/*  909 */         loader = Native.class.getClassLoader();
/*      */       }
/*      */     }
/*  912 */     if (DEBUG) {
/*  913 */       System.out.println("Looking in classpath from " + loader + " for " + name);
/*      */     }
/*  915 */     String libname = name.startsWith("/") ? name : NativeLibrary.mapSharedLibraryName(name);
/*  916 */     String resourcePath = Platform.RESOURCE_PREFIX + "/" + libname;
/*  917 */     if (resourcePath.startsWith("/")) {
/*  918 */       resourcePath = resourcePath.substring(1);
/*      */     }
/*  920 */     URL url = loader.getResource(resourcePath);
/*  921 */     if ((url == null) && (resourcePath.startsWith(Platform.RESOURCE_PREFIX)))
/*      */     {
/*  923 */       url = loader.getResource(libname);
/*      */     }
/*  925 */     if (url == null) {
/*  926 */       String path = System.getProperty("java.class.path");
/*  927 */       if ((loader instanceof URLClassLoader)) {
/*  928 */         path = Arrays.asList(((URLClassLoader)loader).getURLs()).toString();
/*      */       }
/*  930 */       throw new IOException("Native library (" + resourcePath + ") not found in resource path (" + path + ")");
/*      */     }
/*  932 */     if (DEBUG) {
/*  933 */       System.out.println("Found library resource at " + url);
/*      */     }
/*      */     
/*  936 */     lib = null;
/*  937 */     if (url.getProtocol().toLowerCase().equals("file")) {
/*      */       try {
/*  939 */         lib = new File(new URI(url.toString()));
/*      */       }
/*      */       catch (URISyntaxException e) {
/*  942 */         lib = new File(url.getPath());
/*      */       }
/*  944 */       if (DEBUG) {
/*  945 */         System.out.println("Looking in " + lib.getAbsolutePath());
/*      */       }
/*  947 */       if (!lib.exists()) {
/*  948 */         throw new IOException("File URL " + url + " could not be properly decoded");
/*      */       }
/*      */     }
/*  951 */     else if (!Boolean.getBoolean("jna.nounpack")) {
/*  952 */       InputStream is = loader.getResourceAsStream(resourcePath);
/*  953 */       if (is == null) {
/*  954 */         throw new IOException("Can't obtain InputStream for " + resourcePath);
/*      */       }
/*      */       
/*  957 */       FileOutputStream fos = null;
/*      */       
/*      */ 
/*      */       try
/*      */       {
/*  962 */         File dir = getTempDir();
/*  963 */         lib = File.createTempFile("jna", Platform.isWindows() ? ".dll" : null, dir);
/*  964 */         if (!Boolean.getBoolean("jnidispatch.preserve")) {
/*  965 */           lib.deleteOnExit();
/*      */         }
/*  967 */         fos = new FileOutputStream(lib);
/*      */         
/*  969 */         byte[] buf = new byte['Ѐ'];
/*  970 */         int count; while ((count = is.read(buf, 0, buf.length)) > 0) {
/*  971 */           fos.write(buf, 0, count);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  984 */         return lib;
/*      */       }
/*      */       catch (IOException e)
/*      */       {
/*  975 */         throw new IOException("Failed to create temporary file for " + name + " library: " + e.getMessage());
/*      */       } finally {
/*      */         try {
/*  978 */           is.close(); } catch (IOException e) {}
/*  979 */         if (fos != null) {
/*  980 */           try { fos.close();
/*      */           }
/*      */           catch (IOException e) {}
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Library synchronizedLibrary(final Library library)
/*      */   {
/* 1027 */     Class cls = library.getClass();
/* 1028 */     if (!Proxy.isProxyClass(cls)) {
/* 1029 */       throw new IllegalArgumentException("Library must be a proxy class");
/*      */     }
/* 1031 */     InvocationHandler ih = Proxy.getInvocationHandler(library);
/* 1032 */     if (!(ih instanceof Library.Handler)) {
/* 1033 */       throw new IllegalArgumentException("Unrecognized proxy handler: " + ih);
/*      */     }
/* 1035 */     Library.Handler handler = (Library.Handler)ih;
/* 1036 */     InvocationHandler newHandler = new InvocationHandler()
/*      */     {
/*      */       /* Error */
/*      */       public Object invoke(Object proxy, Method method, Object[] args)
/*      */         throws Throwable
/*      */       {
/*      */         // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: getfield 1	com/sun/jna/Native$3:val$handler	Lcom/sun/jna/Library$Handler;
/*      */         //   4: invokevirtual 4	com/sun/jna/Library$Handler:getNativeLibrary	()Lcom/sun/jna/NativeLibrary;
/*      */         //   7: dup
/*      */         //   8: astore 4
/*      */         //   10: monitorenter
/*      */         //   11: aload_0
/*      */         //   12: getfield 1	com/sun/jna/Native$3:val$handler	Lcom/sun/jna/Library$Handler;
/*      */         //   15: aload_0
/*      */         //   16: getfield 2	com/sun/jna/Native$3:val$library	Lcom/sun/jna/Library;
/*      */         //   19: aload_2
/*      */         //   20: aload_3
/*      */         //   21: invokevirtual 5	com/sun/jna/Library$Handler:invoke	(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;
/*      */         //   24: aload 4
/*      */         //   26: monitorexit
/*      */         //   27: areturn
/*      */         //   28: astore 5
/*      */         //   30: aload 4
/*      */         //   32: monitorexit
/*      */         //   33: aload 5
/*      */         //   35: athrow
/*      */         // Line number table:
/*      */         //   Java source line #1039	-> byte code offset #0
/*      */         //   Java source line #1040	-> byte code offset #11
/*      */         //   Java source line #1041	-> byte code offset #28
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	signature
/*      */         //   0	36	0	this	3
/*      */         //   0	36	1	proxy	Object
/*      */         //   0	36	2	method	Method
/*      */         //   0	36	3	args	Object[]
/*      */         //   8	23	4	Ljava/lang/Object;	Object
/*      */         //   28	6	5	localObject1	Object
/*      */         // Exception table:
/*      */         //   from	to	target	type
/*      */         //   11	27	28	finally
/*      */         //   28	33	28	finally
/*      */       }
/* 1043 */     };
/* 1044 */     return (Library)Proxy.newProxyInstance(cls.getClassLoader(), cls
/* 1045 */       .getInterfaces(), newHandler);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getWebStartLibraryPath(String libName)
/*      */   {
/* 1065 */     if (System.getProperty("javawebstart.version") == null) {
/* 1066 */       return null;
/*      */     }
/*      */     try {
/* 1069 */       ClassLoader cl = Native.class.getClassLoader();
/* 1070 */       Method m = (Method)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Object run() {
/*      */           try {
/* 1074 */             Method m = ClassLoader.class.getDeclaredMethod("findLibrary", new Class[] { String.class });
/* 1075 */             m.setAccessible(true);
/* 1076 */             return m;
/*      */           }
/*      */           catch (Exception e) {}
/* 1079 */           return null;
/*      */         }
/*      */         
/* 1082 */       });
/* 1083 */       String libpath = (String)m.invoke(cl, new Object[] { libName });
/* 1084 */       if (libpath != null) {
/* 1085 */         return new File(libpath).getParent();
/*      */       }
/* 1087 */       return null;
/*      */     }
/*      */     catch (Exception e) {}
/* 1090 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static void markTemporaryFile(File file)
/*      */   {
/*      */     try
/*      */     {
/* 1100 */       File marker = new File(file.getParentFile(), file.getName() + ".x");
/* 1101 */       marker.createNewFile();
/*      */     } catch (IOException e) {
/* 1103 */       e.printStackTrace();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   static File getTempDir()
/*      */     throws IOException
/*      */   {
/* 1111 */     String prop = System.getProperty("jna.tmpdir");
/* 1112 */     File jnatmp; if (prop != null) {
/* 1113 */       File jnatmp = new File(prop);
/* 1114 */       jnatmp.mkdirs();
/*      */     }
/*      */     else {
/* 1117 */       File tmp = new File(System.getProperty("java.io.tmpdir"));
/*      */       
/*      */ 
/*      */ 
/* 1121 */       jnatmp = new File(tmp, "jna-" + System.getProperty("user.name").hashCode());
/* 1122 */       jnatmp.mkdirs();
/* 1123 */       if ((!jnatmp.exists()) || (!jnatmp.canWrite())) {
/* 1124 */         jnatmp = tmp;
/*      */       }
/*      */     }
/* 1127 */     if (!jnatmp.exists()) {
/* 1128 */       throw new IOException("JNA temporary directory '" + jnatmp + "' does not exist");
/*      */     }
/* 1130 */     if (!jnatmp.canWrite()) {
/* 1131 */       throw new IOException("JNA temporary directory '" + jnatmp + "' is not writable");
/*      */     }
/* 1133 */     return jnatmp;
/*      */   }
/*      */   
/*      */   static void removeTemporaryFiles() throws IOException
/*      */   {
/* 1138 */     File dir = getTempDir();
/* 1139 */     FilenameFilter filter = new FilenameFilter()
/*      */     {
/*      */       public boolean accept(File dir, String name) {
/* 1142 */         return (name.endsWith(".x")) && (name.startsWith("jna"));
/*      */       }
/* 1144 */     };
/* 1145 */     File[] files = dir.listFiles(filter);
/* 1146 */     for (int i = 0; (files != null) && (i < files.length); i++) {
/* 1147 */       File marker = files[i];
/* 1148 */       String name = marker.getName();
/* 1149 */       name = name.substring(0, name.length() - 2);
/* 1150 */       File target = new File(marker.getParentFile(), name);
/* 1151 */       if ((!target.exists()) || (target.delete())) {
/* 1152 */         marker.delete();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static int getNativeSize(Class type, Object value)
/*      */   {
/* 1161 */     if (type.isArray()) {
/* 1162 */       int len = Array.getLength(value);
/* 1163 */       if (len > 0) {
/* 1164 */         Object o = Array.get(value, 0);
/* 1165 */         return len * getNativeSize(type.getComponentType(), o);
/*      */       }
/*      */       
/* 1168 */       throw new IllegalArgumentException("Arrays of length zero not allowed: " + type);
/*      */     }
/* 1170 */     if ((Structure.class.isAssignableFrom(type)) && 
/* 1171 */       (!Structure.ByReference.class.isAssignableFrom(type))) {
/* 1172 */       return Structure.size(type, (Structure)value);
/*      */     }
/*      */     try {
/* 1175 */       return getNativeSize(type);
/*      */ 
/*      */     }
/*      */     catch (IllegalArgumentException e)
/*      */     {
/* 1180 */       throw new IllegalArgumentException("The type \"" + type.getName() + "\" is not supported: " + e.getMessage());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getNativeSize(Class cls)
/*      */   {
/* 1189 */     if (NativeMapped.class.isAssignableFrom(cls)) {
/* 1190 */       cls = NativeMappedConverter.getInstance(cls).nativeType();
/*      */     }
/*      */     
/* 1193 */     if ((cls == Boolean.TYPE) || (cls == Boolean.class)) return 4;
/* 1194 */     if ((cls == Byte.TYPE) || (cls == Byte.class)) return 1;
/* 1195 */     if ((cls == Short.TYPE) || (cls == Short.class)) return 2;
/* 1196 */     if ((cls == Character.TYPE) || (cls == Character.class)) return WCHAR_SIZE;
/* 1197 */     if ((cls == Integer.TYPE) || (cls == Integer.class)) return 4;
/* 1198 */     if ((cls == Long.TYPE) || (cls == Long.class)) return 8;
/* 1199 */     if ((cls == Float.TYPE) || (cls == Float.class)) return 4;
/* 1200 */     if ((cls == Double.TYPE) || (cls == Double.class)) return 8;
/* 1201 */     if (Structure.class.isAssignableFrom(cls)) {
/* 1202 */       if (Structure.ByValue.class.isAssignableFrom(cls)) {
/* 1203 */         return Structure.size(cls);
/*      */       }
/* 1205 */       return POINTER_SIZE;
/*      */     }
/* 1207 */     if ((Pointer.class.isAssignableFrom(cls)) || ((Platform.HAS_BUFFERS) && 
/* 1208 */       (Buffers.isBuffer(cls))) || 
/* 1209 */       (Callback.class.isAssignableFrom(cls)) || (String.class == cls) || (WString.class == cls))
/*      */     {
/*      */ 
/* 1212 */       return POINTER_SIZE;
/*      */     }
/* 1214 */     throw new IllegalArgumentException("Native size for type \"" + cls.getName() + "\" is unknown");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isSupportedNativeType(Class cls)
/*      */   {
/* 1222 */     if (Structure.class.isAssignableFrom(cls)) {
/* 1223 */       return true;
/*      */     }
/*      */     try {
/* 1226 */       return getNativeSize(cls) != 0;
/*      */     }
/*      */     catch (IllegalArgumentException e) {}
/* 1229 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setCallbackExceptionHandler(Callback.UncaughtExceptionHandler eh)
/*      */   {
/* 1238 */     callbackExceptionHandler = eh == null ? DEFAULT_HANDLER : eh;
/*      */   }
/*      */   
/*      */   public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler()
/*      */   {
/* 1243 */     return callbackExceptionHandler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void register(String libName)
/*      */   {
/* 1252 */     register(findDirectMappedClass(getCallingClass()), libName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void register(NativeLibrary lib)
/*      */   {
/* 1261 */     register(findDirectMappedClass(getCallingClass()), lib);
/*      */   }
/*      */   
/*      */   static Class findDirectMappedClass(Class cls)
/*      */   {
/* 1266 */     Method[] methods = cls.getDeclaredMethods();
/* 1267 */     for (int i = 0; i < methods.length; i++) {
/* 1268 */       if ((methods[i].getModifiers() & 0x100) != 0) {
/* 1269 */         return cls;
/*      */       }
/*      */     }
/* 1272 */     int idx = cls.getName().lastIndexOf("$");
/* 1273 */     if (idx != -1) {
/* 1274 */       String name = cls.getName().substring(0, idx);
/*      */       try {
/* 1276 */         return findDirectMappedClass(Class.forName(name, true, cls.getClassLoader()));
/*      */       }
/*      */       catch (ClassNotFoundException e) {}
/*      */     }
/*      */     
/* 1281 */     throw new IllegalArgumentException("Can't determine class with native methods from the current context (" + cls + ")");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static Class getCallingClass()
/*      */   {
/* 1293 */     Class[] context = new SecurityManager()
/*      */     {
/*      */       public Class[] getClassContext()
/*      */       {
/* 1291 */         return super.getClassContext();
/*      */       }
/*      */     }.getClassContext();
/* 1294 */     if (context == null) {
/* 1295 */       throw new IllegalStateException("The SecurityManager implementation on this platform is broken; you must explicitly provide the class to register");
/*      */     }
/* 1297 */     if (context.length < 4) {
/* 1298 */       throw new IllegalStateException("This method must be called from the static initializer of a class");
/*      */     }
/* 1300 */     return context[3];
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void setCallbackThreadInitializer(Callback cb, CallbackThreadInitializer initializer)
/*      */   {
/* 1309 */     CallbackReference.setCallbackThreadInitializer(cb, initializer);
/*      */   }
/*      */   
/*      */ 
/* 1313 */   private static Map registeredClasses = new WeakHashMap();
/* 1314 */   private static Map registeredLibraries = new WeakHashMap();
/*      */   static final int CB_HAS_INITIALIZER = 1;
/*      */   
/* 1317 */   private static void unregisterAll() { Iterator i; synchronized (registeredClasses) {
/* 1318 */       for (i = registeredClasses.entrySet().iterator(); i.hasNext();) {
/* 1319 */         Map.Entry e = (Map.Entry)i.next();
/* 1320 */         unregister((Class)e.getKey(), (long[])e.getValue());
/* 1321 */         i.remove();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static final int CVT_UNSUPPORTED = -1;
/*      */   private static final int CVT_DEFAULT = 0;
/*      */   public static void unregister()
/*      */   {
/* 1331 */     unregister(findDirectMappedClass(getCallingClass()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void unregister(Class cls)
/*      */   {
/* 1339 */     synchronized (registeredClasses) {
/* 1340 */       if (registeredClasses.containsKey(cls)) {
/* 1341 */         unregister(cls, (long[])registeredClasses.get(cls));
/* 1342 */         registeredClasses.remove(cls);
/* 1343 */         registeredLibraries.remove(cls);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static final int CVT_POINTER = 1;
/*      */   
/*      */   private static final int CVT_STRING = 2;
/*      */   
/*      */   private static final int CVT_STRUCTURE = 3;
/*      */   
/*      */   private static final int CVT_STRUCTURE_BYVAL = 4;
/*      */   
/*      */   private static String getSignature(Class cls)
/*      */   {
/* 1359 */     if (cls.isArray()) {
/* 1360 */       return "[" + getSignature(cls.getComponentType());
/*      */     }
/* 1362 */     if (cls.isPrimitive()) {
/* 1363 */       if (cls == Void.TYPE) return "V";
/* 1364 */       if (cls == Boolean.TYPE) return "Z";
/* 1365 */       if (cls == Byte.TYPE) return "B";
/* 1366 */       if (cls == Short.TYPE) return "S";
/* 1367 */       if (cls == Character.TYPE) return "C";
/* 1368 */       if (cls == Integer.TYPE) return "I";
/* 1369 */       if (cls == Long.TYPE) return "J";
/* 1370 */       if (cls == Float.TYPE) return "F";
/* 1371 */       if (cls == Double.TYPE) return "D";
/*      */     }
/* 1373 */     return "L" + replace(".", "/", cls.getName()) + ";";
/*      */   }
/*      */   
/*      */   static String replace(String s1, String s2, String str)
/*      */   {
/* 1378 */     StringBuilder buf = new StringBuilder();
/*      */     for (;;) {
/* 1380 */       int idx = str.indexOf(s1);
/* 1381 */       if (idx == -1) {
/* 1382 */         buf.append(str);
/* 1383 */         break;
/*      */       }
/*      */       
/* 1386 */       buf.append(str.substring(0, idx));
/* 1387 */       buf.append(s2);
/* 1388 */       str = str.substring(idx + s1.length());
/*      */     }
/*      */     
/* 1391 */     return buf.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_BUFFER = 5;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CVT_ARRAY_BYTE = 6;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CVT_ARRAY_SHORT = 7;
/*      */   
/*      */ 
/*      */ 
/*      */   private static final int CVT_ARRAY_CHAR = 8;
/*      */   
/*      */ 
/*      */   private static final int CVT_ARRAY_INT = 9;
/*      */   
/*      */ 
/*      */   private static final int CVT_ARRAY_LONG = 10;
/*      */   
/*      */ 
/*      */   private static final int CVT_ARRAY_FLOAT = 11;
/*      */   
/*      */ 
/*      */   private static final int CVT_ARRAY_DOUBLE = 12;
/*      */   
/*      */ 
/*      */   private static int getConversion(Class type, TypeMapper mapper)
/*      */   {
/* 1426 */     if (type == Boolean.class) { type = Boolean.TYPE;
/* 1427 */     } else if (type == Byte.class) { type = Byte.TYPE;
/* 1428 */     } else if (type == Short.class) { type = Short.TYPE;
/* 1429 */     } else if (type == Character.class) { type = Character.TYPE;
/* 1430 */     } else if (type == Integer.class) { type = Integer.TYPE;
/* 1431 */     } else if (type == Long.class) { type = Long.TYPE;
/* 1432 */     } else if (type == Float.class) { type = Float.TYPE;
/* 1433 */     } else if (type == Double.class) { type = Double.TYPE;
/* 1434 */     } else if (type == Void.class) { type = Void.TYPE;
/*      */     }
/* 1436 */     if (mapper != null) {
/* 1437 */       FromNativeConverter fromNative = mapper.getFromNativeConverter(type);
/* 1438 */       ToNativeConverter toNative = mapper.getToNativeConverter(type);
/* 1439 */       if (fromNative != null) {
/* 1440 */         Class nativeType = fromNative.nativeType();
/* 1441 */         if (nativeType == String.class) {
/* 1442 */           return 24;
/*      */         }
/* 1444 */         if (nativeType == WString.class) {
/* 1445 */           return 25;
/*      */         }
/* 1447 */         return 23;
/*      */       }
/* 1449 */       if (toNative != null) {
/* 1450 */         Class nativeType = toNative.nativeType();
/* 1451 */         if (nativeType == String.class) {
/* 1452 */           return 24;
/*      */         }
/* 1454 */         if (nativeType == WString.class) {
/* 1455 */           return 25;
/*      */         }
/* 1457 */         return 23;
/*      */       }
/*      */     }
/*      */     
/* 1461 */     if (Pointer.class.isAssignableFrom(type)) {
/* 1462 */       return 1;
/*      */     }
/* 1464 */     if (String.class == type) {
/* 1465 */       return 2;
/*      */     }
/* 1467 */     if (WString.class.isAssignableFrom(type)) {
/* 1468 */       return 20;
/*      */     }
/* 1470 */     if ((Platform.HAS_BUFFERS) && (Buffers.isBuffer(type))) {
/* 1471 */       return 5;
/*      */     }
/* 1473 */     if (Structure.class.isAssignableFrom(type)) {
/* 1474 */       if (Structure.ByValue.class.isAssignableFrom(type)) {
/* 1475 */         return 4;
/*      */       }
/* 1477 */       return 3;
/*      */     }
/* 1479 */     if (type.isArray()) {
/* 1480 */       switch (type.getName().charAt(1)) {
/* 1481 */       case 'Z':  return 13;
/* 1482 */       case 'B':  return 6;
/* 1483 */       case 'S':  return 7;
/* 1484 */       case 'C':  return 8;
/* 1485 */       case 'I':  return 9;
/* 1486 */       case 'J':  return 10;
/* 1487 */       case 'F':  return 11;
/* 1488 */       case 'D':  return 12;
/*      */       }
/*      */       
/*      */     }
/* 1492 */     if (type.isPrimitive()) {
/* 1493 */       return type == Boolean.TYPE ? 14 : 0;
/*      */     }
/* 1495 */     if (Callback.class.isAssignableFrom(type)) {
/* 1496 */       return 15;
/*      */     }
/* 1498 */     if (IntegerType.class.isAssignableFrom(type)) {
/* 1499 */       return 21;
/*      */     }
/* 1501 */     if (PointerType.class.isAssignableFrom(type)) {
/* 1502 */       return 22;
/*      */     }
/* 1504 */     if (NativeMapped.class.isAssignableFrom(type)) {
/* 1505 */       Class nativeType = NativeMappedConverter.getInstance(type).nativeType();
/* 1506 */       if (nativeType == String.class) {
/* 1507 */         return 18;
/*      */       }
/* 1509 */       if (nativeType == WString.class) {
/* 1510 */         return 19;
/*      */       }
/* 1512 */       return 17;
/*      */     }
/* 1514 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void register(Class cls, String libName)
/*      */   {
/* 1527 */     Map options = new HashMap();
/* 1528 */     options.put("classloader", cls.getClassLoader());
/* 1529 */     register(cls, NativeLibrary.getInstance(libName, options));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void register(Class cls, NativeLibrary lib)
/*      */   {
/* 1542 */     Method[] methods = cls.getDeclaredMethods();
/* 1543 */     List mlist = new ArrayList();
/*      */     
/* 1545 */     TypeMapper mapper = (TypeMapper)lib.getOptions().get("type-mapper");
/* 1546 */     cacheOptions(cls, lib.getOptions(), null);
/*      */     
/* 1548 */     for (int i = 0; i < methods.length; i++) {
/* 1549 */       if ((methods[i].getModifiers() & 0x100) != 0) {
/* 1550 */         mlist.add(methods[i]);
/*      */       }
/*      */     }
/* 1553 */     long[] handles = new long[mlist.size()];
/* 1554 */     for (int i = 0; i < handles.length; i++) {
/* 1555 */       Method method = (Method)mlist.get(i);
/* 1556 */       String sig = "(";
/* 1557 */       Class rclass = method.getReturnType();
/*      */       
/* 1559 */       Class[] ptypes = method.getParameterTypes();
/* 1560 */       long[] atypes = new long[ptypes.length];
/* 1561 */       long[] closure_atypes = new long[ptypes.length];
/* 1562 */       int[] cvt = new int[ptypes.length];
/* 1563 */       ToNativeConverter[] toNative = new ToNativeConverter[ptypes.length];
/* 1564 */       FromNativeConverter fromNative = null;
/* 1565 */       int rcvt = getConversion(rclass, mapper);
/* 1566 */       boolean throwLastError = false;
/* 1567 */       long closure_rtype; long rtype; long rtype; long rtype; long rtype; switch (rcvt) {
/*      */       case -1: 
/* 1569 */         throw new IllegalArgumentException(rclass + " is not a supported return type (in method " + method.getName() + " in " + cls + ")");
/*      */       case 23: 
/*      */       case 24: 
/*      */       case 25: 
/* 1573 */         fromNative = mapper.getFromNativeConverter(rclass);
/*      */         
/*      */ 
/*      */ 
/* 1577 */         closure_rtype = Structure.FFIType.get(rclass.isPrimitive() ? rclass : Pointer.class).peer;
/* 1578 */         rtype = Structure.FFIType.get(fromNative.nativeType()).peer;
/* 1579 */         break;
/*      */       case 17: 
/*      */       case 18: 
/*      */       case 19: 
/*      */       case 21: 
/*      */       case 22: 
/* 1585 */         closure_rtype = Structure.FFIType.get(Pointer.class).peer;
/* 1586 */         rtype = Structure.FFIType.get(NativeMappedConverter.getInstance(rclass).nativeType()).peer;
/* 1587 */         break;
/*      */       case 3:  long rtype;
/* 1589 */         closure_rtype = rtype = Structure.FFIType.get(Pointer.class).peer;
/* 1590 */         break;
/*      */       case 4: 
/* 1592 */         closure_rtype = Structure.FFIType.get(Pointer.class).peer;
/* 1593 */         rtype = Structure.FFIType.get(rclass).peer;
/* 1594 */         break;
/*      */       case 0: case 1: case 2: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 20: default: 
/* 1596 */         closure_rtype = rtype = Structure.FFIType.get(rclass).peer;
/*      */       }
/*      */       
/* 1599 */       for (int t = 0; t < ptypes.length; t++) {
/* 1600 */         Class type = ptypes[t];
/* 1601 */         sig = sig + getSignature(type);
/* 1602 */         cvt[t] = getConversion(type, mapper);
/* 1603 */         if (cvt[t] == -1) {
/* 1604 */           throw new IllegalArgumentException(type + " is not a supported argument type (in method " + method.getName() + " in " + cls + ")");
/*      */         }
/* 1606 */         if ((cvt[t] == 17) || (cvt[t] == 18) || (cvt[t] == 19) || (cvt[t] == 21))
/*      */         {
/*      */ 
/*      */ 
/* 1610 */           type = NativeMappedConverter.getInstance(type).nativeType();
/*      */         }
/* 1612 */         else if ((cvt[t] == 23) || (cvt[t] == 24) || (cvt[t] == 25))
/*      */         {
/*      */ 
/* 1615 */           toNative[t] = mapper.getToNativeConverter(type);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/* 1620 */         switch (cvt[t]) {
/*      */         case 4: 
/*      */         case 17: 
/*      */         case 18: 
/*      */         case 19: 
/*      */         case 21: 
/*      */         case 22: 
/* 1627 */           atypes[t] = Structure.FFIType.get(type).peer;
/* 1628 */           closure_atypes[t] = Structure.FFIType.get(Pointer.class).peer;
/* 1629 */           break;
/*      */         case 23: 
/*      */         case 24: 
/*      */         case 25: 
/* 1633 */           closure_atypes[t] = Structure.FFIType.get(type.isPrimitive() ? type : Pointer.class).peer;
/* 1634 */           atypes[t] = Structure.FFIType.get(toNative[t].nativeType()).peer;
/* 1635 */           break;
/*      */         case 0: 
/* 1637 */           closure_atypes[t] = (atypes[t] = Structure.FFIType.get(type).peer);
/* 1638 */           break;
/*      */         case 1: case 2: case 3: case 5: case 6: case 7: case 8: case 9: case 10: case 11: case 12: case 13: case 14: case 15: case 16: case 20: default: 
/* 1640 */           closure_atypes[t] = (atypes[t] = Structure.FFIType.get(Pointer.class).peer);
/*      */         }
/*      */         
/*      */       }
/* 1644 */       sig = sig + ")";
/* 1645 */       sig = sig + getSignature(rclass);
/*      */       
/* 1647 */       Class[] etypes = method.getExceptionTypes();
/* 1648 */       for (int e = 0; e < etypes.length; e++) {
/* 1649 */         if (LastErrorException.class.isAssignableFrom(etypes[e])) {
/* 1650 */           throwLastError = true;
/* 1651 */           break;
/*      */         }
/*      */       }
/*      */       
/* 1655 */       Function f = lib.getFunction(method.getName(), method);
/*      */       try {
/* 1657 */         handles[i] = registerMethod(cls, method.getName(), sig, cvt, closure_atypes, atypes, rcvt, closure_rtype, rtype, rclass, f.peer, f
/*      */         
/*      */ 
/*      */ 
/*      */ 
/* 1662 */           .getCallingConvention(), throwLastError, toNative, fromNative, f.encoding);
/*      */ 
/*      */       }
/*      */       catch (NoSuchMethodError e)
/*      */       {
/*      */ 
/* 1668 */         throw new UnsatisfiedLinkError("No method " + method.getName() + " with signature " + sig + " in " + cls);
/*      */       }
/*      */     }
/* 1671 */     synchronized (registeredClasses) {
/* 1672 */       registeredClasses.put(cls, handles);
/* 1673 */       registeredLibraries.put(cls, lib);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static void cacheOptions(Class cls, Map libOptions, Object proxy)
/*      */   {
/* 1681 */     libOptions = new HashMap(libOptions);
/* 1682 */     synchronized (libraries) {
/* 1683 */       options.put(cls, libOptions);
/* 1684 */       if (proxy != null) {
/* 1685 */         libraries.put(cls, new WeakReference(proxy));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1691 */       if ((!cls.isInterface()) && 
/* 1692 */         (Library.class.isAssignableFrom(cls))) {
/* 1693 */         Class[] ifaces = cls.getInterfaces();
/* 1694 */         for (int i = 0; i < ifaces.length; i++) {
/* 1695 */           if (Library.class.isAssignableFrom(ifaces[i])) {
/* 1696 */             cacheOptions(ifaces[i], libOptions, proxy);
/* 1697 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static NativeMapped fromNative(Class cls, Object value)
/*      */   {
/* 1726 */     return (NativeMapped)NativeMappedConverter.getInstance(cls).fromNative(value, new FromNativeContext(cls));
/*      */   }
/*      */   
/*      */   private static Class nativeType(Class cls) {
/* 1730 */     return NativeMappedConverter.getInstance(cls).nativeType();
/*      */   }
/*      */   
/*      */ 
/*      */   private static Object toNative(ToNativeConverter cvt, Object o)
/*      */   {
/* 1736 */     return cvt.toNative(o, new ToNativeContext());
/*      */   }
/*      */   
/*      */   private static Object fromNative(FromNativeConverter cvt, Object o, Class cls)
/*      */   {
/* 1741 */     return cvt.fromNative(o, new FromNativeContext(cls));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void main(String[] args)
/*      */   {
/* 1760 */     String DEFAULT_TITLE = "Java Native Access (JNA)";
/* 1761 */     String DEFAULT_VERSION = "4.2.2";
/* 1762 */     String DEFAULT_BUILD = "4.2.2 (package information missing)";
/* 1763 */     Package pkg = Native.class.getPackage();
/*      */     
/* 1765 */     String title = pkg != null ? pkg.getSpecificationTitle() : "Java Native Access (JNA)";
/* 1766 */     if (title == null) { title = "Java Native Access (JNA)";
/*      */     }
/* 1768 */     String version = pkg != null ? pkg.getSpecificationVersion() : "4.2.2";
/* 1769 */     if (version == null) version = "4.2.2";
/* 1770 */     title = title + " API Version " + version;
/* 1771 */     System.out.println(title);
/*      */     
/* 1773 */     version = pkg != null ? pkg.getImplementationVersion() : "4.2.2 (package information missing)";
/* 1774 */     if (version == null) version = "4.2.2 (package information missing)";
/* 1775 */     System.out.println("Version: " + version);
/* 1776 */     System.out.println(" Native: " + getNativeVersion() + " (" + 
/* 1777 */       getAPIChecksum() + ")");
/* 1778 */     System.out.println(" Prefix: " + Platform.RESOURCE_PREFIX);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_ARRAY_BOOLEAN = 13;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_BOOLEAN = 14;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_CALLBACK = 15;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_FLOAT = 16;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_NATIVE_MAPPED = 17;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_NATIVE_MAPPED_STRING = 18;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_NATIVE_MAPPED_WSTRING = 19;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_WSTRING = 20;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_INTEGER_TYPE = 21;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_POINTER_TYPE = 22;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_TYPE_MAPPER = 23;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_TYPE_MAPPER_STRING = 24;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final int CVT_TYPE_MAPPER_WSTRING = 25;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int CB_OPTION_DIRECT = 1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static final int CB_OPTION_IN_DLL = 2;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static Structure invokeStructure(long fp, int callFlags, Object[] args, Structure s)
/*      */   {
/* 1886 */     invokeStructure(fp, callFlags, args, s.getPointer().peer, 
/* 1887 */       s.getTypeInfo().peer);
/* 1888 */     return s;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static long open(String name)
/*      */   {
/* 1904 */     return open(name, -1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static Pointer getPointer(long addr)
/*      */   {
/* 1962 */     long peer = _getPointer(addr);
/* 1963 */     return peer == 0L ? null : new Pointer(peer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static String getString(long addr)
/*      */   {
/* 1971 */     return getString(addr, getDefaultStringEncoding());
/*      */   }
/*      */   
/*      */   static String getString(long addr, String encoding) {
/* 1975 */     byte[] data = getStringBytes(addr);
/* 1976 */     if (encoding != null) {
/*      */       try {
/* 1978 */         return new String(data, encoding);
/*      */       }
/*      */       catch (UnsupportedEncodingException e) {}
/*      */     }
/*      */     
/* 1983 */     return new String(data);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void detach(boolean detach)
/*      */   {
/* 2045 */     Thread thread = Thread.currentThread();
/* 2046 */     if (detach)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2053 */       nativeThreads.remove(thread);
/* 2054 */       Pointer p = (Pointer)nativeThreadTerminationFlag.get();
/* 2055 */       setDetachState(true, 0L);
/*      */ 
/*      */     }
/* 2058 */     else if (!nativeThreads.containsKey(thread)) {
/* 2059 */       Pointer p = (Pointer)nativeThreadTerminationFlag.get();
/* 2060 */       nativeThreads.put(thread, p);
/* 2061 */       setDetachState(false, p.peer);
/*      */     }
/*      */   }
/*      */   
/*      */   static Pointer getTerminationFlag(Thread t)
/*      */   {
/* 2067 */     return (Pointer)nativeThreads.get(t);
/*      */   }
/*      */   
/* 2070 */   private static Map nativeThreads = Collections.synchronizedMap(new WeakHashMap());
/*      */   
/* 2072 */   private static ThreadLocal nativeThreadTerminationFlag = new ThreadLocal()
/*      */   {
/*      */     protected Object initialValue()
/*      */     {
/* 2076 */       Memory m = new Memory(4L);
/* 2077 */       m.clear();
/* 2078 */       return m;
/*      */     }
/*      */   };
/*      */   
/*      */   private static native void initIDs();
/*      */   
/*      */   public static synchronized native void setProtected(boolean paramBoolean);
/*      */   
/*      */   public static synchronized native boolean isProtected();
/*      */   
/*      */   @Deprecated
/*      */   public static void setPreserveLastError(boolean enable) {}
/*      */   
/*      */   static native long getWindowHandle0(Component paramComponent);
/*      */   
/*      */   private static native long _getDirectBufferPointer(Buffer paramBuffer);
/*      */   
/*      */   private static native int sizeof(int paramInt);
/*      */   
/*      */   private static native String getNativeVersion();
/*      */   
/*      */   private static native String getAPIChecksum();
/*      */   
/*      */   public static native int getLastError();
/*      */   
/*      */   public static native void setLastError(int paramInt);
/*      */   
/*      */   /* Error */
/*      */   public static boolean registered(Class cls)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: getstatic 312	com/sun/jna/Native:registeredClasses	Ljava/util/Map;
/*      */     //   3: dup
/*      */     //   4: astore_1
/*      */     //   5: monitorenter
/*      */     //   6: getstatic 312	com/sun/jna/Native:registeredClasses	Ljava/util/Map;
/*      */     //   9: aload_0
/*      */     //   10: invokeinterface 56 2 0
/*      */     //   15: aload_1
/*      */     //   16: monitorexit
/*      */     //   17: ireturn
/*      */     //   18: astore_2
/*      */     //   19: aload_1
/*      */     //   20: monitorexit
/*      */     //   21: aload_2
/*      */     //   22: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1350	-> byte code offset #0
/*      */     //   Java source line #1351	-> byte code offset #6
/*      */     //   Java source line #1352	-> byte code offset #18
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	23	0	cls	Class
/*      */     //   4	16	1	Ljava/lang/Object;	Object
/*      */     //   18	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   6	17	18	finally
/*      */     //   18	21	18	finally
/*      */   }
/*      */   
/*      */   private static native void unregister(Class paramClass, long[] paramArrayOfLong);
/*      */   
/*      */   private static native long registerMethod(Class paramClass1, String paramString1, String paramString2, int[] paramArrayOfInt, long[] paramArrayOfLong1, long[] paramArrayOfLong2, int paramInt1, long paramLong1, long paramLong2, Class paramClass2, long paramLong3, int paramInt2, boolean paramBoolean, ToNativeConverter[] paramArrayOfToNativeConverter, FromNativeConverter paramFromNativeConverter, String paramString3);
/*      */   
/*      */   public static native long ffi_prep_cif(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   
/*      */   public static native void ffi_call(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*      */   
/*      */   public static native long ffi_prep_closure(long paramLong, ffi_callback paramffi_callback);
/*      */   
/*      */   public static native void ffi_free_closure(long paramLong);
/*      */   
/*      */   static native int initialize_ffi_type(long paramLong);
/*      */   
/*      */   static synchronized native void freeNativeCallback(long paramLong);
/*      */   
/*      */   static synchronized native long createNativeCallback(Callback paramCallback, Method paramMethod, Class[] paramArrayOfClass, Class paramClass, int paramInt1, int paramInt2, String paramString);
/*      */   
/*      */   static native int invokeInt(long paramLong, int paramInt, Object[] paramArrayOfObject);
/*      */   
/*      */   static native long invokeLong(long paramLong, int paramInt, Object[] paramArrayOfObject);
/*      */   
/*      */   static native void invokeVoid(long paramLong, int paramInt, Object[] paramArrayOfObject);
/*      */   
/*      */   static native float invokeFloat(long paramLong, int paramInt, Object[] paramArrayOfObject);
/*      */   
/*      */   static native double invokeDouble(long paramLong, int paramInt, Object[] paramArrayOfObject);
/*      */   
/*      */   static native long invokePointer(long paramLong, int paramInt, Object[] paramArrayOfObject);
/*      */   
/*      */   private static native void invokeStructure(long paramLong1, int paramInt, Object[] paramArrayOfObject, long paramLong2, long paramLong3);
/*      */   
/*      */   static native Object invokeObject(long paramLong, int paramInt, Object[] paramArrayOfObject);
/*      */   
/*      */   static native long open(String paramString, int paramInt);
/*      */   
/*      */   static native void close(long paramLong);
/*      */   
/*      */   static native long findSymbol(long paramLong, String paramString);
/*      */   
/*      */   static native long indexOf(long paramLong, byte paramByte);
/*      */   
/*      */   static native void read(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void read(long paramLong, short[] paramArrayOfShort, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void read(long paramLong, char[] paramArrayOfChar, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void read(long paramLong, int[] paramArrayOfInt, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void read(long paramLong, long[] paramArrayOfLong, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void read(long paramLong, float[] paramArrayOfFloat, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void read(long paramLong, double[] paramArrayOfDouble, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void write(long paramLong, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void write(long paramLong, short[] paramArrayOfShort, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void write(long paramLong, char[] paramArrayOfChar, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void write(long paramLong, int[] paramArrayOfInt, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void write(long paramLong, long[] paramArrayOfLong, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void write(long paramLong, float[] paramArrayOfFloat, int paramInt1, int paramInt2);
/*      */   
/*      */   static native void write(long paramLong, double[] paramArrayOfDouble, int paramInt1, int paramInt2);
/*      */   
/*      */   static native byte getByte(long paramLong);
/*      */   
/*      */   static native char getChar(long paramLong);
/*      */   
/*      */   static native short getShort(long paramLong);
/*      */   
/*      */   static native int getInt(long paramLong);
/*      */   
/*      */   static native long getLong(long paramLong);
/*      */   
/*      */   static native float getFloat(long paramLong);
/*      */   
/*      */   static native double getDouble(long paramLong);
/*      */   
/*      */   private static native long _getPointer(long paramLong);
/*      */   
/*      */   static native String getWideString(long paramLong);
/*      */   
/*      */   static native byte[] getStringBytes(long paramLong);
/*      */   
/*      */   static native void setMemory(long paramLong1, long paramLong2, byte paramByte);
/*      */   
/*      */   static native void setByte(long paramLong, byte paramByte);
/*      */   
/*      */   static native void setShort(long paramLong, short paramShort);
/*      */   
/*      */   static native void setChar(long paramLong, char paramChar);
/*      */   
/*      */   static native void setInt(long paramLong, int paramInt);
/*      */   
/*      */   static native void setLong(long paramLong1, long paramLong2);
/*      */   
/*      */   static native void setFloat(long paramLong, float paramFloat);
/*      */   
/*      */   static native void setDouble(long paramLong, double paramDouble);
/*      */   
/*      */   static native void setPointer(long paramLong1, long paramLong2);
/*      */   
/*      */   static native void setWideString(long paramLong, String paramString);
/*      */   
/*      */   public static native long malloc(long paramLong);
/*      */   
/*      */   public static native void free(long paramLong);
/*      */   
/*      */   public static native ByteBuffer getDirectByteBuffer(long paramLong1, long paramLong2);
/*      */   
/*      */   private static native void setDetachState(boolean paramBoolean, long paramLong);
/*      */   
/*      */   public static abstract interface ffi_callback
/*      */   {
/*      */     public abstract void invoke(long paramLong1, long paramLong2, long paramLong3);
/*      */   }
/*      */   
/*      */   private static class Buffers
/*      */   {
/*      */     static boolean isBuffer(Class cls)
/*      */     {
/* 2086 */       return Buffer.class.isAssignableFrom(cls);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class AWT
/*      */   {
/*      */     static long getWindowID(Window w)
/*      */       throws HeadlessException
/*      */     {
/* 2095 */       return getComponentID(w);
/*      */     }
/*      */     
/*      */     static long getComponentID(Object o) throws HeadlessException
/*      */     {
/* 2100 */       if (GraphicsEnvironment.isHeadless()) {
/* 2101 */         throw new HeadlessException("No native windows when headless");
/*      */       }
/* 2103 */       Component c = (Component)o;
/* 2104 */       if (c.isLightweight()) {
/* 2105 */         throw new IllegalArgumentException("Component must be heavyweight");
/*      */       }
/* 2107 */       if (!c.isDisplayable()) {
/* 2108 */         throw new IllegalStateException("Component must be displayable");
/*      */       }
/* 2110 */       if ((Platform.isX11()) && 
/* 2111 */         (System.getProperty("java.version").startsWith("1.4")) && 
/* 2112 */         (!c.isVisible())) {
/* 2113 */         throw new IllegalStateException("Component must be visible");
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 2119 */       return Native.getWindowHandle0(c);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\Native.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */