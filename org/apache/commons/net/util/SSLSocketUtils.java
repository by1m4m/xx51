/*    */ package org.apache.commons.net.util;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import javax.net.ssl.SSLSocket;
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
/*    */ public class SSLSocketUtils
/*    */ {
/*    */   public static boolean enableEndpointNameVerification(SSLSocket socket)
/*    */   {
/*    */     try
/*    */     {
/* 42 */       Class<?> cls = Class.forName("javax.net.ssl.SSLParameters");
/* 43 */       Method setEndpointIdentificationAlgorithm = cls.getDeclaredMethod("setEndpointIdentificationAlgorithm", new Class[] { String.class });
/* 44 */       Method getSSLParameters = SSLSocket.class.getDeclaredMethod("getSSLParameters", new Class[0]);
/* 45 */       Method setSSLParameters = SSLSocket.class.getDeclaredMethod("setSSLParameters", new Class[] { cls });
/* 46 */       if ((setEndpointIdentificationAlgorithm != null) && (getSSLParameters != null) && (setSSLParameters != null)) {
/* 47 */         Object sslParams = getSSLParameters.invoke(socket, new Object[0]);
/* 48 */         if (sslParams != null) {
/* 49 */           setEndpointIdentificationAlgorithm.invoke(sslParams, new Object[] { "HTTPS" });
/* 50 */           setSSLParameters.invoke(socket, new Object[] { sslParams });
/* 51 */           return true;
/*    */         }
/*    */       }
/*    */     }
/*    */     catch (SecurityException e) {}catch (ClassNotFoundException e) {}catch (NoSuchMethodException e) {}catch (IllegalArgumentException e) {}catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\commons\net\util\SSLSocketUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */