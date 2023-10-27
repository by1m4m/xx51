/*    */ package org.eclipse.jetty.util;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Properties;
/*    */ import org.eclipse.jetty.util.log.Log;
/*    */ import org.eclipse.jetty.util.log.Logger;
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
/*    */ public class Jetty
/*    */ {
/* 31 */   private static final Logger LOG = Log.getLogger(Jetty.class);
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
/* 42 */   private static final Properties __buildProperties = new Properties();
/*    */   
/*    */ 
/*    */   static
/*    */   {
/*    */     try
/*    */     {
/* 49 */       InputStream inputStream = Jetty.class.getResourceAsStream("/org/eclipse/jetty/version/build.properties");Throwable localThrowable3 = null;
/*    */       try {
/* 51 */         __buildProperties.load(inputStream);
/*    */       }
/*    */       catch (Throwable localThrowable1)
/*    */       {
/* 48 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*    */       }
/*    */       finally
/*    */       {
/* 52 */         if (inputStream != null) if (localThrowable3 != null) try { inputStream.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else inputStream.close();
/*    */       }
/*    */     }
/*    */     catch (Exception e) {
/* 56 */       LOG.ignore(e);
/*    */     }
/*    */     
/* 59 */     String git_hash = __buildProperties.getProperty("buildNumber", "unknown");
/* 60 */     if (git_hash.startsWith("${"))
/* 61 */       git_hash = "unknown";
/* 62 */     GIT_HASH = git_hash;
/* 63 */     System.setProperty("jetty.git.hash", GIT_HASH);
/* 64 */     BUILD_TIMESTAMP = formatTimestamp(__buildProperties.getProperty("timestamp", "unknown"));
/*    */     
/*    */ 
/*    */ 
/* 68 */     Package pkg = Jetty.class.getPackage();
/* 69 */     if ((pkg != null) && 
/* 70 */       ("Eclipse.org - Jetty".equals(pkg.getImplementationVendor())) && 
/* 71 */       (pkg.getImplementationVersion() != null)) {
/* 72 */       VERSION = pkg.getImplementationVersion();
/*    */     } else
/* 74 */       VERSION = System.getProperty("jetty.version", "9.4.z-SNAPSHOT"); }
/*    */   
/* 76 */   public static final String POWERED_BY = "<a href=\"http://eclipse.org/jetty\">Powered by Jetty:// " + VERSION + "</a>";
/*    */   
/*    */ 
/* 79 */   public static final boolean STABLE = !VERSION.matches("^.*\\.(RC|M)[0-9]+$");
/*    */   
/*    */   public static final String VERSION;
/*    */   
/*    */   public static final String GIT_HASH;
/*    */   
/*    */   public static final String BUILD_TIMESTAMP;
/*    */   
/*    */   private static String formatTimestamp(String timestamp)
/*    */   {
/*    */     try
/*    */     {
/* 91 */       return 
/* 92 */         new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(new Date(Long.valueOf(timestamp).longValue()));
/*    */     }
/*    */     catch (NumberFormatException e)
/*    */     {
/* 96 */       LOG.ignore(e); }
/* 97 */     return "unknown";
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\Jetty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */