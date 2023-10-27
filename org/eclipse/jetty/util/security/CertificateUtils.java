/*    */ package org.eclipse.jetty.util.security;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.security.KeyStore;
/*    */ import java.util.Objects;
/*    */ import org.eclipse.jetty.util.resource.Resource;
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
/*    */ public class CertificateUtils
/*    */ {
/*    */   public static KeyStore getKeyStore(Resource store, String storeType, String storeProvider, String storePassword)
/*    */     throws Exception
/*    */   {
/* 35 */     KeyStore keystore = null;
/*    */     
/* 37 */     if (store != null)
/*    */     {
/* 39 */       Objects.requireNonNull(storeType, "storeType cannot be null");
/* 40 */       if (storeProvider != null)
/*    */       {
/* 42 */         keystore = KeyStore.getInstance(storeType, storeProvider);
/*    */       }
/*    */       else
/*    */       {
/* 46 */         keystore = KeyStore.getInstance(storeType);
/*    */       }
/*    */       
/* 49 */       if (!store.exists()) {
/* 50 */         throw new IllegalStateException("no valid keystore");
/*    */       }
/* 52 */       InputStream inStream = store.getInputStream();Throwable localThrowable3 = null;
/*    */       try {
/* 54 */         keystore.load(inStream, storePassword == null ? null : storePassword.toCharArray());
/*    */       }
/*    */       catch (Throwable localThrowable1)
/*    */       {
/* 52 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*    */       }
/*    */       finally {
/* 55 */         if (inStream != null) if (localThrowable3 != null) try { inStream.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else inStream.close();
/*    */       }
/*    */     }
/* 58 */     return keystore;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public static java.util.Collection<? extends java.security.cert.CRL> loadCRL(String crlPath)
/*    */     throws Exception
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aconst_null
/*    */     //   1: astore_1
/*    */     //   2: aload_0
/*    */     //   3: ifnull +45 -> 48
/*    */     //   6: aconst_null
/*    */     //   7: astore_2
/*    */     //   8: aload_0
/*    */     //   9: invokestatic 16	org/eclipse/jetty/util/resource/Resource:newResource	(Ljava/lang/String;)Lorg/eclipse/jetty/util/resource/Resource;
/*    */     //   12: invokevirtual 10	org/eclipse/jetty/util/resource/Resource:getInputStream	()Ljava/io/InputStream;
/*    */     //   15: astore_2
/*    */     //   16: ldc 17
/*    */     //   18: invokestatic 18	java/security/cert/CertificateFactory:getInstance	(Ljava/lang/String;)Ljava/security/cert/CertificateFactory;
/*    */     //   21: aload_2
/*    */     //   22: invokevirtual 19	java/security/cert/CertificateFactory:generateCRLs	(Ljava/io/InputStream;)Ljava/util/Collection;
/*    */     //   25: astore_1
/*    */     //   26: aload_2
/*    */     //   27: ifnull +21 -> 48
/*    */     //   30: aload_2
/*    */     //   31: invokevirtual 13	java/io/InputStream:close	()V
/*    */     //   34: goto +14 -> 48
/*    */     //   37: astore_3
/*    */     //   38: aload_2
/*    */     //   39: ifnull +7 -> 46
/*    */     //   42: aload_2
/*    */     //   43: invokevirtual 13	java/io/InputStream:close	()V
/*    */     //   46: aload_3
/*    */     //   47: athrow
/*    */     //   48: aload_1
/*    */     //   49: areturn
/*    */     // Line number table:
/*    */     //   Java source line #64	-> byte code offset #0
/*    */     //   Java source line #66	-> byte code offset #2
/*    */     //   Java source line #68	-> byte code offset #6
/*    */     //   Java source line #71	-> byte code offset #8
/*    */     //   Java source line #72	-> byte code offset #16
/*    */     //   Java source line #76	-> byte code offset #26
/*    */     //   Java source line #78	-> byte code offset #30
/*    */     //   Java source line #76	-> byte code offset #37
/*    */     //   Java source line #78	-> byte code offset #42
/*    */     //   Java source line #83	-> byte code offset #48
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	50	0	crlPath	String
/*    */     //   1	48	1	crlList	java.util.Collection<? extends java.security.cert.CRL>
/*    */     //   7	36	2	in	InputStream
/*    */     //   37	10	3	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   8	26	37	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\security\CertificateUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */