/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.X509ExtendedKeyManager;
/*     */ import javax.net.ssl.X509KeyManager;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class OpenSslKeyMaterialManager
/*     */ {
/*     */   static final String KEY_TYPE_RSA = "RSA";
/*     */   static final String KEY_TYPE_DH_RSA = "DH_RSA";
/*     */   static final String KEY_TYPE_EC = "EC";
/*     */   static final String KEY_TYPE_EC_EC = "EC_EC";
/*     */   static final String KEY_TYPE_EC_RSA = "EC_RSA";
/*  51 */   private static final Map<String, String> KEY_TYPES = new HashMap();
/*     */   
/*  53 */   static { KEY_TYPES.put("RSA", "RSA");
/*  54 */     KEY_TYPES.put("DHE_RSA", "RSA");
/*  55 */     KEY_TYPES.put("ECDHE_RSA", "RSA");
/*  56 */     KEY_TYPES.put("ECDHE_ECDSA", "EC");
/*  57 */     KEY_TYPES.put("ECDH_RSA", "EC_RSA");
/*  58 */     KEY_TYPES.put("ECDH_ECDSA", "EC_EC");
/*  59 */     KEY_TYPES.put("DH_RSA", "DH_RSA");
/*     */   }
/*     */   
/*     */   private final OpenSslKeyMaterialProvider provider;
/*     */   OpenSslKeyMaterialManager(OpenSslKeyMaterialProvider provider)
/*     */   {
/*  65 */     this.provider = provider;
/*     */   }
/*     */   
/*     */   void setKeyMaterialServerSide(ReferenceCountedOpenSslEngine engine) throws SSLException {
/*  69 */     long ssl = engine.sslPointer();
/*  70 */     String[] authMethods = SSL.authenticationMethods(ssl);
/*  71 */     Set<String> aliases = new HashSet(authMethods.length);
/*  72 */     for (String authMethod : authMethods) {
/*  73 */       String type = (String)KEY_TYPES.get(authMethod);
/*  74 */       if (type != null) {
/*  75 */         String alias = chooseServerAlias(engine, type);
/*  76 */         if ((alias != null) && (aliases.add(alias))) {
/*  77 */           OpenSslKeyMaterial keyMaterial = null;
/*     */           try {
/*  79 */             keyMaterial = this.provider.chooseKeyMaterial(engine.alloc, alias);
/*  80 */             if (keyMaterial != null) {
/*  81 */               SSL.setKeyMaterialServerSide(ssl, keyMaterial
/*  82 */                 .certificateChainAddress(), keyMaterial.privateKeyAddress());
/*     */             }
/*     */           } catch (SSLException e) {
/*  85 */             throw e;
/*     */           } catch (Exception e) {
/*  87 */             throw new SSLException(e);
/*     */           } finally {
/*  89 */             if (keyMaterial != null) {
/*  90 */               keyMaterial.release();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   void setKeyMaterialClientSide(ReferenceCountedOpenSslEngine engine, long certOut, long keyOut, String[] keyTypes, X500Principal[] issuer) throws SSLException
/*     */   {
/* 100 */     String alias = chooseClientAlias(engine, keyTypes, issuer);
/* 101 */     OpenSslKeyMaterial keyMaterial = null;
/*     */     try {
/* 103 */       keyMaterial = this.provider.chooseKeyMaterial(engine.alloc, alias);
/* 104 */       if (keyMaterial != null) {
/* 105 */         SSL.setKeyMaterialClientSide(engine.sslPointer(), certOut, keyOut, keyMaterial
/* 106 */           .certificateChainAddress(), keyMaterial.privateKeyAddress());
/*     */       }
/*     */     } catch (SSLException e) {
/* 109 */       throw e;
/*     */     } catch (Exception e) {
/* 111 */       throw new SSLException(e);
/*     */     } finally {
/* 113 */       if (keyMaterial != null) {
/* 114 */         keyMaterial.release();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private String chooseClientAlias(ReferenceCountedOpenSslEngine engine, String[] keyTypes, X500Principal[] issuer)
/*     */   {
/* 121 */     X509KeyManager manager = this.provider.keyManager();
/* 122 */     if ((manager instanceof X509ExtendedKeyManager)) {
/* 123 */       return ((X509ExtendedKeyManager)manager).chooseEngineClientAlias(keyTypes, issuer, engine);
/*     */     }
/* 125 */     return manager.chooseClientAlias(keyTypes, issuer, null);
/*     */   }
/*     */   
/*     */   private String chooseServerAlias(ReferenceCountedOpenSslEngine engine, String type) {
/* 129 */     X509KeyManager manager = this.provider.keyManager();
/* 130 */     if ((manager instanceof X509ExtendedKeyManager)) {
/* 131 */       return ((X509ExtendedKeyManager)manager).chooseEngineServerAlias(type, null, engine);
/*     */     }
/* 133 */     return manager.chooseServerAlias(type, null, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslKeyMaterialManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */