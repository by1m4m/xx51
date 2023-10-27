/*    */ package org.eclipse.jetty.util;
/*    */ 
/*    */ import java.net.URL;
/*    */ import java.util.Locale;
/*    */ import java.util.MissingResourceException;
/*    */ import java.util.ResourceBundle;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Loader
/*    */ {
/*    */   public static URL getResource(String name)
/*    */   {
/* 47 */     ClassLoader loader = Thread.currentThread().getContextClassLoader();
/* 48 */     return loader == null ? ClassLoader.getSystemResource(name) : loader.getResource(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Class loadClass(String name)
/*    */     throws ClassNotFoundException
/*    */   {
/* 64 */     ClassLoader loader = Thread.currentThread().getContextClassLoader();
/* 65 */     return loader == null ? Class.forName(name) : loader.loadClass(name);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Class loadClass(Class loaderClass, String name)
/*    */     throws ClassNotFoundException
/*    */   {
/* 82 */     if ((loaderClass != null) && (loaderClass.getClassLoader() != null))
/* 83 */       return loaderClass.getClassLoader().loadClass(name);
/* 84 */     return loadClass(name);
/*    */   }
/*    */   
/*    */ 
/*    */   public static ResourceBundle getResourceBundle(String name, boolean checkParents, Locale locale)
/*    */     throws MissingResourceException
/*    */   {
/* 91 */     ClassLoader loader = Thread.currentThread().getContextClassLoader();
/* 92 */     return loader == null ? ResourceBundle.getBundle(name, locale) : ResourceBundle.getBundle(name, locale, loader);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\Loader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */