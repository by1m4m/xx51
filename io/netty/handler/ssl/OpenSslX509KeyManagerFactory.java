/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.UnpooledByteBufAllocator;
/*     */ import io.netty.internal.tcnative.SSL;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.KeyStoreSpi;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Provider;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.KeyManagerFactorySpi;
/*     */ import javax.net.ssl.ManagerFactoryParameters;
/*     */ import javax.net.ssl.X509KeyManager;
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
/*     */ public final class OpenSslX509KeyManagerFactory
/*     */   extends KeyManagerFactory
/*     */ {
/*     */   private final OpenSslKeyManagerFactorySpi spi;
/*     */   
/*     */   public OpenSslX509KeyManagerFactory()
/*     */   {
/*  66 */     this(newOpenSslKeyManagerFactorySpi(null));
/*     */   }
/*     */   
/*     */   public OpenSslX509KeyManagerFactory(Provider provider) {
/*  70 */     this(newOpenSslKeyManagerFactorySpi(provider));
/*     */   }
/*     */   
/*     */   public OpenSslX509KeyManagerFactory(String algorithm, Provider provider) throws NoSuchAlgorithmException {
/*  74 */     this(newOpenSslKeyManagerFactorySpi(algorithm, provider));
/*     */   }
/*     */   
/*     */   private OpenSslX509KeyManagerFactory(OpenSslKeyManagerFactorySpi spi) {
/*  78 */     super(spi, spi.kmf.getProvider(), spi.kmf.getAlgorithm());
/*  79 */     this.spi = spi;
/*     */   }
/*     */   
/*     */   private static OpenSslKeyManagerFactorySpi newOpenSslKeyManagerFactorySpi(Provider provider) {
/*     */     try {
/*  84 */       return newOpenSslKeyManagerFactorySpi(null, provider);
/*     */     }
/*     */     catch (NoSuchAlgorithmException e) {
/*  87 */       throw new IllegalStateException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static OpenSslKeyManagerFactorySpi newOpenSslKeyManagerFactorySpi(String algorithm, Provider provider) throws NoSuchAlgorithmException
/*     */   {
/*  93 */     if (algorithm == null) {
/*  94 */       algorithm = KeyManagerFactory.getDefaultAlgorithm();
/*     */     }
/*  96 */     return new OpenSslKeyManagerFactorySpi(provider == null ? 
/*  97 */       KeyManagerFactory.getInstance(algorithm) : 
/*  98 */       KeyManagerFactory.getInstance(algorithm, provider));
/*     */   }
/*     */   
/*     */   OpenSslKeyMaterialProvider newProvider() {
/* 102 */     return this.spi.newProvider();
/*     */   }
/*     */   
/*     */   private static final class OpenSslKeyManagerFactorySpi extends KeyManagerFactorySpi {
/*     */     final KeyManagerFactory kmf;
/*     */     private volatile ProviderFactory providerFactory;
/*     */     
/*     */     OpenSslKeyManagerFactorySpi(KeyManagerFactory kmf) {
/* 110 */       this.kmf = ((KeyManagerFactory)ObjectUtil.checkNotNull(kmf, "kmf"));
/*     */     }
/*     */     
/*     */     protected synchronized void engineInit(KeyStore keyStore, char[] chars)
/*     */       throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
/*     */     {
/* 116 */       if (this.providerFactory != null) {
/* 117 */         throw new KeyStoreException("Already initialized");
/*     */       }
/* 119 */       if (!keyStore.aliases().hasMoreElements()) {
/* 120 */         throw new KeyStoreException("No aliases found");
/*     */       }
/*     */       
/* 123 */       this.kmf.init(keyStore, chars);
/*     */       
/* 125 */       this.providerFactory = new ProviderFactory(ReferenceCountedOpenSslContext.chooseX509KeyManager(this.kmf.getKeyManagers()), password(chars), Collections.list(keyStore.aliases()));
/*     */     }
/*     */     
/*     */     private static String password(char[] password) {
/* 129 */       if ((password == null) || (password.length == 0)) {
/* 130 */         return null;
/*     */       }
/* 132 */       return new String(password);
/*     */     }
/*     */     
/*     */     protected void engineInit(ManagerFactoryParameters managerFactoryParameters)
/*     */       throws InvalidAlgorithmParameterException
/*     */     {
/* 138 */       throw new InvalidAlgorithmParameterException("Not supported");
/*     */     }
/*     */     
/*     */     protected KeyManager[] engineGetKeyManagers()
/*     */     {
/* 143 */       ProviderFactory providerFactory = this.providerFactory;
/* 144 */       if (providerFactory == null) {
/* 145 */         throw new IllegalStateException("engineInit(...) not called yet");
/*     */       }
/* 147 */       return new KeyManager[] { providerFactory.keyManager };
/*     */     }
/*     */     
/*     */     OpenSslKeyMaterialProvider newProvider() {
/* 151 */       ProviderFactory providerFactory = this.providerFactory;
/* 152 */       if (providerFactory == null) {
/* 153 */         throw new IllegalStateException("engineInit(...) not called yet");
/*     */       }
/* 155 */       return providerFactory.newProvider();
/*     */     }
/*     */     
/*     */     private static final class ProviderFactory {
/*     */       private final X509KeyManager keyManager;
/*     */       private final String password;
/*     */       private final Iterable<String> aliases;
/*     */       
/*     */       ProviderFactory(X509KeyManager keyManager, String password, Iterable<String> aliases) {
/* 164 */         this.keyManager = keyManager;
/* 165 */         this.password = password;
/* 166 */         this.aliases = aliases;
/*     */       }
/*     */       
/*     */       OpenSslKeyMaterialProvider newProvider() {
/* 170 */         return new OpenSslPopulatedKeyMaterialProvider(this.keyManager, this.password, this.aliases);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */       private static final class OpenSslPopulatedKeyMaterialProvider
/*     */         extends OpenSslKeyMaterialProvider
/*     */       {
/*     */         private final Map<String, Object> materialMap;
/*     */         
/*     */ 
/*     */         OpenSslPopulatedKeyMaterialProvider(X509KeyManager keyManager, String password, Iterable<String> aliases)
/*     */         {
/* 183 */           super(password);
/* 184 */           this.materialMap = new HashMap();
/* 185 */           boolean initComplete = false;
/*     */           try {
/* 187 */             for (String alias : aliases) {
/* 188 */               if ((alias != null) && (!this.materialMap.containsKey(alias))) {
/*     */                 try {
/* 190 */                   this.materialMap.put(alias, super.chooseKeyMaterial(UnpooledByteBufAllocator.DEFAULT, alias));
/*     */ 
/*     */                 }
/*     */                 catch (Exception e)
/*     */                 {
/* 195 */                   this.materialMap.put(alias, e);
/*     */                 }
/*     */               }
/*     */             }
/* 199 */             initComplete = true;
/*     */           } finally {
/* 201 */             if (!initComplete) {
/* 202 */               destroy();
/*     */             }
/*     */           }
/* 205 */           if (this.materialMap.isEmpty()) {
/* 206 */             throw new IllegalArgumentException("aliases must be non-empty");
/*     */           }
/*     */         }
/*     */         
/*     */         OpenSslKeyMaterial chooseKeyMaterial(ByteBufAllocator allocator, String alias) throws Exception
/*     */         {
/* 212 */           Object value = this.materialMap.get(alias);
/* 213 */           if (value == null)
/*     */           {
/* 215 */             return null;
/*     */           }
/* 217 */           if ((value instanceof OpenSslKeyMaterial)) {
/* 218 */             return ((OpenSslKeyMaterial)value).retain();
/*     */           }
/* 220 */           throw ((Exception)value);
/*     */         }
/*     */         
/*     */         void destroy()
/*     */         {
/* 225 */           for (Object material : this.materialMap.values()) {
/* 226 */             ReferenceCountUtil.release(material);
/*     */           }
/* 228 */           this.materialMap.clear();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static OpenSslX509KeyManagerFactory newEngineBased(File certificateChain, String password)
/*     */     throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
/*     */   {
/* 243 */     return newEngineBased(SslContext.toX509Certificates(certificateChain), password);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static OpenSslX509KeyManagerFactory newEngineBased(X509Certificate[] certificateChain, String password)
/*     */     throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
/*     */   {
/* 255 */     KeyStore store = new OpenSslEngineKeyStore((X509Certificate[])certificateChain.clone(), null);
/* 256 */     store.load(null, null);
/* 257 */     OpenSslX509KeyManagerFactory factory = new OpenSslX509KeyManagerFactory();
/* 258 */     factory.init(store, password == null ? null : password.toCharArray());
/* 259 */     return factory;
/*     */   }
/*     */   
/*     */   private static final class OpenSslEngineKeyStore extends KeyStore {
/*     */     private OpenSslEngineKeyStore(X509Certificate[] certificateChain) {
/* 264 */       super(
/*     */       {
/* 266 */         private final Date creationDate = new Date();
/*     */         
/*     */         public Key engineGetKey(String alias, char[] password) throws UnrecoverableKeyException
/*     */         {
/* 270 */           if (engineContainsAlias(alias)) {
/*     */             try {
/* 272 */               return new OpenSslPrivateKey(SSL.loadPrivateKeyFromEngine(alias, password == null ? null : new String(password)));
/*     */             }
/*     */             catch (Exception e) {
/* 275 */               UnrecoverableKeyException keyException = new UnrecoverableKeyException("Unable to load key from engine");
/*     */               
/* 277 */               keyException.initCause(e);
/* 278 */               throw keyException;
/*     */             }
/*     */           }
/* 281 */           return null;
/*     */         }
/*     */         
/*     */         public Certificate[] engineGetCertificateChain(String alias)
/*     */         {
/* 286 */           return engineContainsAlias(alias) ? (X509Certificate[])OpenSslX509KeyManagerFactory.OpenSslEngineKeyStore.this.clone() : null;
/*     */         }
/*     */         
/*     */         public Certificate engineGetCertificate(String alias)
/*     */         {
/* 291 */           return engineContainsAlias(alias) ? OpenSslX509KeyManagerFactory.OpenSslEngineKeyStore.this[0] : null;
/*     */         }
/*     */         
/*     */         public Date engineGetCreationDate(String alias)
/*     */         {
/* 296 */           return engineContainsAlias(alias) ? this.creationDate : null;
/*     */         }
/*     */         
/*     */         public void engineSetKeyEntry(String alias, Key key, char[] password, Certificate[] chain)
/*     */           throws KeyStoreException
/*     */         {
/* 302 */           throw new KeyStoreException("Not supported");
/*     */         }
/*     */         
/*     */         public void engineSetKeyEntry(String alias, byte[] key, Certificate[] chain) throws KeyStoreException
/*     */         {
/* 307 */           throw new KeyStoreException("Not supported");
/*     */         }
/*     */         
/*     */         public void engineSetCertificateEntry(String alias, Certificate cert) throws KeyStoreException
/*     */         {
/* 312 */           throw new KeyStoreException("Not supported");
/*     */         }
/*     */         
/*     */         public void engineDeleteEntry(String alias) throws KeyStoreException
/*     */         {
/* 317 */           throw new KeyStoreException("Not supported");
/*     */         }
/*     */         
/*     */         public Enumeration<String> engineAliases()
/*     */         {
/* 322 */           return Collections.enumeration(Collections.singleton("key"));
/*     */         }
/*     */         
/*     */         public boolean engineContainsAlias(String alias)
/*     */         {
/* 327 */           return "key".equals(alias);
/*     */         }
/*     */         
/*     */         public int engineSize()
/*     */         {
/* 332 */           return 1;
/*     */         }
/*     */         
/*     */         public boolean engineIsKeyEntry(String alias)
/*     */         {
/* 337 */           return engineContainsAlias(alias);
/*     */         }
/*     */         
/*     */         public boolean engineIsCertificateEntry(String alias)
/*     */         {
/* 342 */           return engineContainsAlias(alias);
/*     */         }
/*     */         
/*     */         public String engineGetCertificateAlias(Certificate cert)
/*     */         {
/* 347 */           if ((cert instanceof X509Certificate)) {
/* 348 */             for (X509Certificate x509Certificate : OpenSslX509KeyManagerFactory.OpenSslEngineKeyStore.this) {
/* 349 */               if (x509Certificate.equals(cert)) {
/* 350 */                 return "key";
/*     */               }
/*     */             }
/*     */           }
/* 354 */           return null;
/*     */         }
/*     */         
/*     */         public void engineStore(OutputStream stream, char[] password)
/*     */         {
/* 359 */           throw new UnsupportedOperationException();
/*     */         }
/*     */         
/*     */         public void engineLoad(InputStream stream, char[] password)
/*     */         {
/* 364 */           if ((stream != null) && (password != null))
/* 365 */             throw new UnsupportedOperationException(); } }, "native");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 370 */       OpenSsl.ensureAvailability();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslX509KeyManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */