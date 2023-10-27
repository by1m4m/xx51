/*     */ package com.google.api.client.json.webtoken;
/*     */ 
/*     */ import com.google.api.client.json.JsonFactory;
/*     */ import com.google.api.client.util.Base64;
/*     */ import com.google.api.client.util.Beta;
/*     */ import com.google.api.client.util.Key;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.SecurityUtils;
/*     */ import com.google.api.client.util.StringUtils;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.Signature;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonWebSignature
/*     */   extends JsonWebToken
/*     */ {
/*     */   private final byte[] signatureBytes;
/*     */   private final byte[] signedContentBytes;
/*     */   
/*     */   public JsonWebSignature(Header header, JsonWebToken.Payload payload, byte[] signatureBytes, byte[] signedContentBytes)
/*     */   {
/*  80 */     super(header, payload);
/*  81 */     this.signatureBytes = ((byte[])Preconditions.checkNotNull(signatureBytes));
/*  82 */     this.signedContentBytes = ((byte[])Preconditions.checkNotNull(signedContentBytes));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Header
/*     */     extends JsonWebToken.Header
/*     */   {
/*     */     @Key("alg")
/*     */     private String algorithm;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("jku")
/*     */     private String jwkUrl;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("jwk")
/*     */     private String jwk;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("kid")
/*     */     private String keyId;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("x5u")
/*     */     private String x509Url;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("x5t")
/*     */     private String x509Thumbprint;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("x5c")
/*     */     private List<String> x509Certificates;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("crit")
/*     */     private List<String> critical;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Header setType(String type)
/*     */     {
/* 156 */       super.setType(type);
/* 157 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getAlgorithm()
/*     */     {
/* 165 */       return this.algorithm;
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
/*     */     public Header setAlgorithm(String algorithm)
/*     */     {
/* 178 */       this.algorithm = algorithm;
/* 179 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getJwkUrl()
/*     */     {
/* 188 */       return this.jwkUrl;
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
/*     */     public Header setJwkUrl(String jwkUrl)
/*     */     {
/* 202 */       this.jwkUrl = jwkUrl;
/* 203 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getJwk()
/*     */     {
/* 211 */       return this.jwk;
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
/*     */     public Header setJwk(String jwk)
/*     */     {
/* 224 */       this.jwk = jwk;
/* 225 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getKeyId()
/*     */     {
/* 233 */       return this.keyId;
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
/*     */     public Header setKeyId(String keyId)
/*     */     {
/* 246 */       this.keyId = keyId;
/* 247 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getX509Url()
/*     */     {
/* 256 */       return this.x509Url;
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
/*     */     public Header setX509Url(String x509Url)
/*     */     {
/* 270 */       this.x509Url = x509Url;
/* 271 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getX509Thumbprint()
/*     */     {
/* 280 */       return this.x509Thumbprint;
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
/*     */     public Header setX509Thumbprint(String x509Thumbprint)
/*     */     {
/* 294 */       this.x509Thumbprint = x509Thumbprint;
/* 295 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Deprecated
/*     */     public final String getX509Certificate()
/*     */     {
/* 307 */       if ((this.x509Certificates == null) || (this.x509Certificates.isEmpty())) {
/* 308 */         return null;
/*     */       }
/* 310 */       return (String)this.x509Certificates.get(0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final List<String> getX509Certificates()
/*     */     {
/* 321 */       return this.x509Certificates;
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
/*     */     @Deprecated
/*     */     public Header setX509Certificate(String x509Certificate)
/*     */     {
/* 338 */       ArrayList<String> x509Certificates = new ArrayList();
/* 339 */       x509Certificates.add(x509Certificate);
/* 340 */       this.x509Certificates = x509Certificates;
/* 341 */       return this;
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
/*     */     public Header setX509Certificates(List<String> x509Certificates)
/*     */     {
/* 357 */       this.x509Certificates = x509Certificates;
/* 358 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final List<String> getCritical()
/*     */     {
/* 368 */       return this.critical;
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
/*     */     public Header setCritical(List<String> critical)
/*     */     {
/* 383 */       this.critical = critical;
/* 384 */       return this;
/*     */     }
/*     */     
/*     */     public Header set(String fieldName, Object value)
/*     */     {
/* 389 */       return (Header)super.set(fieldName, value);
/*     */     }
/*     */     
/*     */     public Header clone()
/*     */     {
/* 394 */       return (Header)super.clone();
/*     */     }
/*     */   }
/*     */   
/*     */   public Header getHeader()
/*     */   {
/* 400 */     return (Header)super.getHeader();
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
/*     */   public final boolean verifySignature(PublicKey publicKey)
/*     */     throws GeneralSecurityException
/*     */   {
/* 416 */     Signature signatureAlg = null;
/* 417 */     String algorithm = getHeader().getAlgorithm();
/* 418 */     if ("RS256".equals(algorithm)) {
/* 419 */       signatureAlg = SecurityUtils.getSha256WithRsaSignatureAlgorithm();
/*     */     } else {
/* 421 */       return false;
/*     */     }
/* 423 */     return SecurityUtils.verify(signatureAlg, publicKey, this.signatureBytes, this.signedContentBytes);
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
/*     */   @Beta
/*     */   public final X509Certificate verifySignature(X509TrustManager trustManager)
/*     */     throws GeneralSecurityException
/*     */   {
/* 448 */     List<String> x509Certificates = getHeader().getX509Certificates();
/* 449 */     if ((x509Certificates == null) || (x509Certificates.isEmpty())) {
/* 450 */       return null;
/*     */     }
/* 452 */     String algorithm = getHeader().getAlgorithm();
/* 453 */     Signature signatureAlg = null;
/* 454 */     if ("RS256".equals(algorithm)) {
/* 455 */       signatureAlg = SecurityUtils.getSha256WithRsaSignatureAlgorithm();
/*     */     } else {
/* 457 */       return null;
/*     */     }
/* 459 */     return SecurityUtils.verify(signatureAlg, trustManager, x509Certificates, this.signatureBytes, this.signedContentBytes);
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
/*     */   @Beta
/*     */   public final X509Certificate verifySignature()
/*     */     throws GeneralSecurityException
/*     */   {
/* 486 */     X509TrustManager trustManager = getDefaultX509TrustManager();
/* 487 */     if (trustManager == null) {
/* 488 */       return null;
/*     */     }
/* 490 */     return verifySignature(trustManager);
/*     */   }
/*     */   
/*     */   private static X509TrustManager getDefaultX509TrustManager() {
/*     */     try {
/* 495 */       TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*     */       
/* 497 */       factory.init((KeyStore)null);
/* 498 */       for (TrustManager manager : factory.getTrustManagers()) {
/* 499 */         if ((manager instanceof X509TrustManager)) {
/* 500 */           return (X509TrustManager)manager;
/*     */         }
/*     */       }
/* 503 */       return null;
/*     */     } catch (NoSuchAlgorithmException e) {
/* 505 */       return null;
/*     */     } catch (KeyStoreException e) {}
/* 507 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public final byte[] getSignatureBytes()
/*     */   {
/* 513 */     return this.signatureBytes;
/*     */   }
/*     */   
/*     */   public final byte[] getSignedContentBytes()
/*     */   {
/* 518 */     return this.signedContentBytes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonWebSignature parse(JsonFactory jsonFactory, String tokenString)
/*     */     throws IOException
/*     */   {
/* 530 */     return parser(jsonFactory).parse(tokenString);
/*     */   }
/*     */   
/*     */   public static Parser parser(JsonFactory jsonFactory)
/*     */   {
/* 535 */     return new Parser(jsonFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Parser
/*     */   {
/*     */     private final JsonFactory jsonFactory;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 551 */     private Class<? extends JsonWebSignature.Header> headerClass = JsonWebSignature.Header.class;
/*     */     
/*     */ 
/* 554 */     private Class<? extends JsonWebToken.Payload> payloadClass = JsonWebToken.Payload.class;
/*     */     
/*     */ 
/*     */ 
/*     */     public Parser(JsonFactory jsonFactory)
/*     */     {
/* 560 */       this.jsonFactory = ((JsonFactory)Preconditions.checkNotNull(jsonFactory));
/*     */     }
/*     */     
/*     */     public Class<? extends JsonWebSignature.Header> getHeaderClass()
/*     */     {
/* 565 */       return this.headerClass;
/*     */     }
/*     */     
/*     */     public Parser setHeaderClass(Class<? extends JsonWebSignature.Header> headerClass)
/*     */     {
/* 570 */       this.headerClass = headerClass;
/* 571 */       return this;
/*     */     }
/*     */     
/*     */     public Class<? extends JsonWebToken.Payload> getPayloadClass()
/*     */     {
/* 576 */       return this.payloadClass;
/*     */     }
/*     */     
/*     */     public Parser setPayloadClass(Class<? extends JsonWebToken.Payload> payloadClass)
/*     */     {
/* 581 */       this.payloadClass = payloadClass;
/* 582 */       return this;
/*     */     }
/*     */     
/*     */     public JsonFactory getJsonFactory()
/*     */     {
/* 587 */       return this.jsonFactory;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public JsonWebSignature parse(String tokenString)
/*     */       throws IOException
/*     */     {
/* 598 */       int firstDot = tokenString.indexOf('.');
/* 599 */       Preconditions.checkArgument(firstDot != -1);
/* 600 */       byte[] headerBytes = Base64.decodeBase64(tokenString.substring(0, firstDot));
/* 601 */       int secondDot = tokenString.indexOf('.', firstDot + 1);
/* 602 */       Preconditions.checkArgument(secondDot != -1);
/* 603 */       Preconditions.checkArgument(tokenString.indexOf('.', secondDot + 1) == -1);
/*     */       
/* 605 */       byte[] payloadBytes = Base64.decodeBase64(tokenString.substring(firstDot + 1, secondDot));
/* 606 */       byte[] signatureBytes = Base64.decodeBase64(tokenString.substring(secondDot + 1));
/* 607 */       byte[] signedContentBytes = StringUtils.getBytesUtf8(tokenString.substring(0, secondDot));
/*     */       
/* 609 */       JsonWebSignature.Header header = (JsonWebSignature.Header)this.jsonFactory.fromInputStream(new ByteArrayInputStream(headerBytes), this.headerClass);
/*     */       
/* 611 */       Preconditions.checkArgument(header.getAlgorithm() != null);
/* 612 */       JsonWebToken.Payload payload = (JsonWebToken.Payload)this.jsonFactory.fromInputStream(new ByteArrayInputStream(payloadBytes), this.payloadClass);
/*     */       
/* 614 */       return new JsonWebSignature(header, payload, signatureBytes, signedContentBytes);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String signUsingRsaSha256(PrivateKey privateKey, JsonFactory jsonFactory, Header header, JsonWebToken.Payload payload)
/*     */     throws GeneralSecurityException, IOException
/*     */   {
/* 634 */     String str1 = String.valueOf(String.valueOf(Base64.encodeBase64URLSafeString(jsonFactory.toByteArray(header))));String str2 = String.valueOf(String.valueOf(Base64.encodeBase64URLSafeString(jsonFactory.toByteArray(payload))));String content = 1 + str1.length() + str2.length() + str1 + "." + str2;
/*     */     
/* 636 */     byte[] contentBytes = StringUtils.getBytesUtf8(content);
/* 637 */     byte[] signature = SecurityUtils.sign(SecurityUtils.getSha256WithRsaSignatureAlgorithm(), privateKey, contentBytes);
/*     */     
/* 639 */     String str3 = String.valueOf(String.valueOf(content));String str4 = String.valueOf(String.valueOf(Base64.encodeBase64URLSafeString(signature)));return 1 + str3.length() + str4.length() + str3 + "." + str4;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\json\webtoken\JsonWebSignature.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */