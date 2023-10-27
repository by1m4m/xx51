/*     */ package com.google.api.client.util;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.Signature;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SecurityUtils
/*     */ {
/*     */   public static KeyStore getDefaultKeyStore()
/*     */     throws KeyStoreException
/*     */   {
/*  48 */     return KeyStore.getInstance(KeyStore.getDefaultType());
/*     */   }
/*     */   
/*     */   public static KeyStore getJavaKeyStore() throws KeyStoreException
/*     */   {
/*  53 */     return KeyStore.getInstance("JKS");
/*     */   }
/*     */   
/*     */   public static KeyStore getPkcs12KeyStore() throws KeyStoreException
/*     */   {
/*  58 */     return KeyStore.getInstance("PKCS12");
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
/*     */ 
/*     */   public static void loadKeyStore(KeyStore keyStore, InputStream keyStream, String storePass)
/*     */     throws IOException, GeneralSecurityException
/*     */   {
/*     */     try
/*     */     {
/*  82 */       keyStore.load(keyStream, storePass.toCharArray());
/*     */     } finally {
/*  84 */       keyStream.close();
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
/*     */   public static PrivateKey getPrivateKey(KeyStore keyStore, String alias, String keyPass)
/*     */     throws GeneralSecurityException
/*     */   {
/*  98 */     return (PrivateKey)keyStore.getKey(alias, keyPass.toCharArray());
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
/*     */   public static PrivateKey loadPrivateKeyFromKeyStore(KeyStore keyStore, InputStream keyStream, String storePass, String alias, String keyPass)
/*     */     throws IOException, GeneralSecurityException
/*     */   {
/* 115 */     loadKeyStore(keyStore, keyStream, storePass);
/* 116 */     return getPrivateKey(keyStore, alias, keyPass);
/*     */   }
/*     */   
/*     */   public static KeyFactory getRsaKeyFactory() throws NoSuchAlgorithmException
/*     */   {
/* 121 */     return KeyFactory.getInstance("RSA");
/*     */   }
/*     */   
/*     */   public static Signature getSha1WithRsaSignatureAlgorithm() throws NoSuchAlgorithmException
/*     */   {
/* 126 */     return Signature.getInstance("SHA1withRSA");
/*     */   }
/*     */   
/*     */   public static Signature getSha256WithRsaSignatureAlgorithm() throws NoSuchAlgorithmException
/*     */   {
/* 131 */     return Signature.getInstance("SHA256withRSA");
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
/*     */   public static byte[] sign(Signature signatureAlgorithm, PrivateKey privateKey, byte[] contentBytes)
/*     */     throws InvalidKeyException, SignatureException
/*     */   {
/* 145 */     signatureAlgorithm.initSign(privateKey);
/* 146 */     signatureAlgorithm.update(contentBytes);
/* 147 */     return signatureAlgorithm.sign();
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
/*     */   public static boolean verify(Signature signatureAlgorithm, PublicKey publicKey, byte[] signatureBytes, byte[] contentBytes)
/*     */     throws InvalidKeyException, SignatureException
/*     */   {
/* 162 */     signatureAlgorithm.initVerify(publicKey);
/* 163 */     signatureAlgorithm.update(contentBytes);
/* 164 */     return signatureAlgorithm.verify(signatureBytes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static X509Certificate verify(Signature signatureAlgorithm, X509TrustManager trustManager, List<String> certChainBase64, byte[] signatureBytes, byte[] contentBytes)
/*     */     throws InvalidKeyException, SignatureException
/*     */   {
/*     */     CertificateFactory certificateFactory;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 184 */       certificateFactory = getX509CertificateFactory();
/*     */     } catch (CertificateException e) {
/* 186 */       return null;
/*     */     }
/* 188 */     X509Certificate[] certificates = new X509Certificate[certChainBase64.size()];
/* 189 */     int currentCert = 0;
/* 190 */     for (String certBase64 : certChainBase64) {
/* 191 */       byte[] certDer = Base64.decodeBase64(certBase64);
/* 192 */       ByteArrayInputStream bis = new ByteArrayInputStream(certDer);
/*     */       try {
/* 194 */         Certificate cert = certificateFactory.generateCertificate(bis);
/* 195 */         if (!(cert instanceof X509Certificate)) {
/* 196 */           return null;
/*     */         }
/* 198 */         certificates[(currentCert++)] = ((X509Certificate)cert);
/*     */       } catch (CertificateException e) {
/* 200 */         return null;
/*     */       }
/*     */     }
/*     */     try {
/* 204 */       trustManager.checkServerTrusted(certificates, "RSA");
/*     */     } catch (CertificateException e) {
/* 206 */       return null;
/*     */     }
/* 208 */     PublicKey pubKey = certificates[0].getPublicKey();
/* 209 */     if (verify(signatureAlgorithm, pubKey, signatureBytes, contentBytes)) {
/* 210 */       return certificates[0];
/*     */     }
/* 212 */     return null;
/*     */   }
/*     */   
/*     */   public static CertificateFactory getX509CertificateFactory() throws CertificateException
/*     */   {
/* 217 */     return CertificateFactory.getInstance("X.509");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void loadKeyStoreFromCertificates(KeyStore keyStore, CertificateFactory certificateFactory, InputStream certificateStream)
/*     */     throws GeneralSecurityException
/*     */   {
/* 248 */     int i = 0;
/* 249 */     for (Certificate cert : certificateFactory.generateCertificates(certificateStream)) {
/* 250 */       keyStore.setCertificateEntry(String.valueOf(i), cert);
/* 251 */       i++;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\SecurityUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */