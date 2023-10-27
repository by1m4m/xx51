/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import javax.net.ssl.SNIHostName;
/*    */ import javax.net.ssl.SNIMatcher;
/*    */ import javax.net.ssl.SNIServerName;
/*    */ import javax.net.ssl.SSLParameters;
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
/*    */ final class Java8SslUtils
/*    */ {
/*    */   static List<String> getSniHostNames(SSLParameters sslParameters)
/*    */   {
/* 33 */     List<SNIServerName> names = sslParameters.getServerNames();
/* 34 */     if ((names == null) || (names.isEmpty())) {
/* 35 */       return Collections.emptyList();
/*    */     }
/* 37 */     List<String> strings = new ArrayList(names.size());
/*    */     
/* 39 */     for (SNIServerName serverName : names) {
/* 40 */       if ((serverName instanceof SNIHostName)) {
/* 41 */         strings.add(((SNIHostName)serverName).getAsciiName());
/*    */       } else {
/* 43 */         throw new IllegalArgumentException("Only " + SNIHostName.class.getName() + " instances are supported, but found: " + serverName);
/*    */       }
/*    */     }
/*    */     
/* 47 */     return strings;
/*    */   }
/*    */   
/*    */   static void setSniHostNames(SSLParameters sslParameters, List<String> names) {
/* 51 */     sslParameters.setServerNames(getSniHostNames(names));
/*    */   }
/*    */   
/*    */   static List getSniHostNames(List<String> names) {
/* 55 */     if ((names == null) || (names.isEmpty())) {
/* 56 */       return Collections.emptyList();
/*    */     }
/* 58 */     List<SNIServerName> sniServerNames = new ArrayList(names.size());
/* 59 */     for (String name : names) {
/* 60 */       sniServerNames.add(new SNIHostName(name));
/*    */     }
/* 62 */     return sniServerNames;
/*    */   }
/*    */   
/*    */   static boolean getUseCipherSuitesOrder(SSLParameters sslParameters) {
/* 66 */     return sslParameters.getUseCipherSuitesOrder();
/*    */   }
/*    */   
/*    */   static void setUseCipherSuitesOrder(SSLParameters sslParameters, boolean useOrder) {
/* 70 */     sslParameters.setUseCipherSuitesOrder(useOrder);
/*    */   }
/*    */   
/*    */   static void setSNIMatchers(SSLParameters sslParameters, Collection<?> matchers)
/*    */   {
/* 75 */     sslParameters.setSNIMatchers(matchers);
/*    */   }
/*    */   
/*    */   static boolean checkSniHostnameMatch(Collection<?> matchers, byte[] hostname)
/*    */   {
/* 80 */     if ((matchers != null) && (!matchers.isEmpty())) {
/* 81 */       SNIHostName name = new SNIHostName(hostname);
/* 82 */       Iterator<SNIMatcher> matcherIt = matchers.iterator();
/* 83 */       while (matcherIt.hasNext()) {
/* 84 */         SNIMatcher matcher = (SNIMatcher)matcherIt.next();
/*    */         
/* 86 */         if ((matcher.getType() == 0) && (matcher.matches(name))) {
/* 87 */           return true;
/*    */         }
/*    */       }
/* 90 */       return false;
/*    */     }
/* 92 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\Java8SslUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */