/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanUtil
/*     */ {
/*     */   public static String okNameForGetter(AnnotatedMethod am, boolean stdNaming)
/*     */   {
/*  21 */     String name = am.getName();
/*  22 */     String str = okNameForIsGetter(am, name, stdNaming);
/*  23 */     if (str == null) {
/*  24 */       str = okNameForRegularGetter(am, name, stdNaming);
/*     */     }
/*  26 */     return str;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String okNameForRegularGetter(AnnotatedMethod am, String name, boolean stdNaming)
/*     */   {
/*  35 */     if (name.startsWith("get"))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  43 */       if ("getCallbacks".equals(name)) {
/*  44 */         if (isCglibGetCallbacks(am)) {
/*  45 */           return null;
/*     */         }
/*  47 */       } else if ("getMetaClass".equals(name))
/*     */       {
/*  49 */         if (isGroovyMetaClassGetter(am)) {
/*  50 */           return null;
/*     */         }
/*     */       }
/*  53 */       return stdNaming ? stdManglePropertyName(name, 3) : legacyManglePropertyName(name, 3);
/*     */     }
/*     */     
/*     */ 
/*  57 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String okNameForIsGetter(AnnotatedMethod am, String name, boolean stdNaming)
/*     */   {
/*  66 */     if (name.startsWith("is")) {
/*  67 */       Class<?> rt = am.getRawType();
/*  68 */       if ((rt == Boolean.class) || (rt == Boolean.TYPE)) {
/*  69 */         return stdNaming ? stdManglePropertyName(name, 2) : legacyManglePropertyName(name, 2);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  74 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String okNameForSetter(AnnotatedMethod am, boolean stdNaming)
/*     */   {
/*  81 */     String name = okNameForMutator(am, "set", stdNaming);
/*  82 */     if ((name != null) && ((!"metaClass".equals(name)) || (!isGroovyMetaClassSetter(am))))
/*     */     {
/*     */ 
/*  85 */       return name;
/*     */     }
/*  87 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String okNameForMutator(AnnotatedMethod am, String prefix, boolean stdNaming)
/*     */   {
/*  95 */     String name = am.getName();
/*  96 */     if (name.startsWith(prefix)) {
/*  97 */       return stdNaming ? stdManglePropertyName(name, prefix.length()) : legacyManglePropertyName(name, prefix.length());
/*     */     }
/*     */     
/*     */ 
/* 101 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static String okNameForGetter(AnnotatedMethod am)
/*     */   {
/* 112 */     return okNameForGetter(am, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static String okNameForRegularGetter(AnnotatedMethod am, String name) {
/* 117 */     return okNameForRegularGetter(am, name, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static String okNameForIsGetter(AnnotatedMethod am, String name) {
/* 122 */     return okNameForIsGetter(am, name, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static String okNameForSetter(AnnotatedMethod am) {
/* 127 */     return okNameForSetter(am, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static String okNameForMutator(AnnotatedMethod am, String prefix) {
/* 132 */     return okNameForMutator(am, prefix, false);
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
/*     */   protected static boolean isCglibGetCallbacks(AnnotatedMethod am)
/*     */   {
/* 151 */     Class<?> rt = am.getRawType();
/*     */     
/* 153 */     if ((rt == null) || (!rt.isArray())) {
/* 154 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 160 */     Class<?> compType = rt.getComponentType();
/*     */     
/* 162 */     Package pkg = compType.getPackage();
/* 163 */     if (pkg != null) {
/* 164 */       String pname = pkg.getName();
/* 165 */       if ((pname.contains(".cglib")) && (
/* 166 */         (pname.startsWith("net.sf.cglib")) || (pname.startsWith("org.hibernate.repackage.cglib")) || (pname.startsWith("org.springframework.cglib"))))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 176 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static boolean isGroovyMetaClassSetter(AnnotatedMethod am)
/*     */   {
/* 185 */     Class<?> argType = am.getRawParameterType(0);
/* 186 */     Package pkg = argType.getPackage();
/* 187 */     if ((pkg != null) && (pkg.getName().startsWith("groovy.lang"))) {
/* 188 */       return true;
/*     */     }
/* 190 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static boolean isGroovyMetaClassGetter(AnnotatedMethod am)
/*     */   {
/* 198 */     Class<?> rt = am.getRawType();
/* 199 */     if ((rt == null) || (rt.isArray())) {
/* 200 */       return false;
/*     */     }
/* 202 */     Package pkg = rt.getPackage();
/* 203 */     if ((pkg != null) && (pkg.getName().startsWith("groovy.lang"))) {
/* 204 */       return true;
/*     */     }
/* 206 */     return false;
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
/*     */   protected static String legacyManglePropertyName(String basename, int offset)
/*     */   {
/* 224 */     int end = basename.length();
/* 225 */     if (end == offset) {
/* 226 */       return null;
/*     */     }
/*     */     
/* 229 */     StringBuilder sb = null;
/* 230 */     for (int i = offset; i < end; i++) {
/* 231 */       char upper = basename.charAt(i);
/* 232 */       char lower = Character.toLowerCase(upper);
/* 233 */       if (upper == lower) {
/*     */         break;
/*     */       }
/* 236 */       if (sb == null) {
/* 237 */         int l = end - offset;
/* 238 */         sb = new StringBuilder(l);
/* 239 */         sb.append(basename, offset, end);
/*     */       }
/* 241 */       sb.setCharAt(i - offset, lower);
/*     */     }
/* 243 */     return sb == null ? basename.substring(offset) : sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static String stdManglePropertyName(String basename, int offset)
/*     */   {
/* 251 */     int end = basename.length();
/* 252 */     if (end == offset) {
/* 253 */       return null;
/*     */     }
/*     */     
/* 256 */     char c0 = basename.charAt(offset);
/* 257 */     char c1 = Character.toLowerCase(c0);
/* 258 */     if (c0 == c1) {
/* 259 */       return basename.substring(offset);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 264 */     if ((offset + 1 < end) && 
/* 265 */       (Character.isUpperCase(basename.charAt(offset + 1)))) {
/* 266 */       return basename.substring(offset);
/*     */     }
/*     */     
/* 269 */     StringBuilder sb = new StringBuilder(end - offset);
/* 270 */     sb.append(c1);
/* 271 */     sb.append(basename, offset + 1, end);
/* 272 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\util\BeanUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */