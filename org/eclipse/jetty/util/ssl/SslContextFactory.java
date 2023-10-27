/*      */ package org.eclipse.jetty.util.ssl;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.security.KeyStore;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.SecureRandom;
/*      */ import java.security.Security;
/*      */ import java.security.cert.CRL;
/*      */ import java.security.cert.CertStore;
/*      */ import java.security.cert.Certificate;
/*      */ import java.security.cert.CertificateFactory;
/*      */ import java.security.cert.CollectionCertStoreParameters;
/*      */ import java.security.cert.PKIXBuilderParameters;
/*      */ import java.security.cert.PKIXCertPathChecker;
/*      */ import java.security.cert.X509CertSelector;
/*      */ import java.security.cert.X509Certificate;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Set;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import javax.net.ssl.CertPathTrustManagerParameters;
/*      */ import javax.net.ssl.KeyManager;
/*      */ import javax.net.ssl.KeyManagerFactory;
/*      */ import javax.net.ssl.SNIHostName;
/*      */ import javax.net.ssl.SNIMatcher;
/*      */ import javax.net.ssl.SNIServerName;
/*      */ import javax.net.ssl.SSLContext;
/*      */ import javax.net.ssl.SSLEngine;
/*      */ import javax.net.ssl.SSLParameters;
/*      */ import javax.net.ssl.SSLPeerUnverifiedException;
/*      */ import javax.net.ssl.SSLServerSocket;
/*      */ import javax.net.ssl.SSLServerSocketFactory;
/*      */ import javax.net.ssl.SSLSession;
/*      */ import javax.net.ssl.SSLSessionContext;
/*      */ import javax.net.ssl.SSLSocket;
/*      */ import javax.net.ssl.SSLSocketFactory;
/*      */ import javax.net.ssl.TrustManager;
/*      */ import javax.net.ssl.TrustManagerFactory;
/*      */ import javax.net.ssl.X509ExtendedKeyManager;
/*      */ import javax.net.ssl.X509TrustManager;
/*      */ import org.eclipse.jetty.util.StringUtil;
/*      */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*      */ import org.eclipse.jetty.util.annotation.ManagedObject;
/*      */ import org.eclipse.jetty.util.component.AbstractLifeCycle;
/*      */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*      */ import org.eclipse.jetty.util.component.Dumpable;
/*      */ import org.eclipse.jetty.util.log.Log;
/*      */ import org.eclipse.jetty.util.log.Logger;
/*      */ import org.eclipse.jetty.util.resource.Resource;
/*      */ import org.eclipse.jetty.util.security.CertificateUtils;
/*      */ import org.eclipse.jetty.util.security.CertificateValidator;
/*      */ import org.eclipse.jetty.util.security.Password;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @ManagedObject
/*      */ public class SslContextFactory
/*      */   extends AbstractLifeCycle
/*      */   implements Dumpable
/*      */ {
/*   97 */   public static final TrustManager[] TRUST_ALL_CERTS = { new X509TrustManager()
/*      */   {
/*      */ 
/*      */     public X509Certificate[] getAcceptedIssuers()
/*      */     {
/*  102 */       return new X509Certificate[0];
/*      */     }
/*      */     
/*      */     public void checkClientTrusted(X509Certificate[] certs, String authType) {}
/*      */     
/*      */     public void checkServerTrusted(X509Certificate[] certs, String authType) {}
/*   97 */   } };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  116 */   private static final Logger LOG = Log.getLogger(SslContextFactory.class);
/*      */   
/*      */ 
/*  119 */   public static final String DEFAULT_KEYMANAGERFACTORY_ALGORITHM = Security.getProperty("ssl.KeyManagerFactory.algorithm") == null ? 
/*  120 */     KeyManagerFactory.getDefaultAlgorithm() : Security.getProperty("ssl.KeyManagerFactory.algorithm");
/*      */   
/*      */ 
/*  123 */   public static final String DEFAULT_TRUSTMANAGERFACTORY_ALGORITHM = Security.getProperty("ssl.TrustManagerFactory.algorithm") == null ? 
/*  124 */     TrustManagerFactory.getDefaultAlgorithm() : Security.getProperty("ssl.TrustManagerFactory.algorithm");
/*      */   
/*      */ 
/*      */   public static final String KEYPASSWORD_PROPERTY = "org.eclipse.jetty.ssl.keypassword";
/*      */   
/*      */ 
/*      */   public static final String PASSWORD_PROPERTY = "org.eclipse.jetty.ssl.password";
/*      */   
/*  132 */   private final Set<String> _excludeProtocols = new LinkedHashSet();
/*  133 */   private final Set<String> _includeProtocols = new LinkedHashSet();
/*  134 */   private final Set<String> _excludeCipherSuites = new LinkedHashSet();
/*  135 */   private final List<String> _includeCipherSuites = new ArrayList();
/*  136 */   private final Map<String, X509> _aliasX509 = new HashMap();
/*  137 */   private final Map<String, X509> _certHosts = new HashMap();
/*  138 */   private final Map<String, X509> _certWilds = new HashMap();
/*      */   private String[] _selectedProtocols;
/*  140 */   private boolean _useCipherSuitesOrder = true;
/*      */   private Comparator<String> _cipherComparator;
/*      */   private String[] _selectedCipherSuites;
/*      */   private Resource _keyStoreResource;
/*      */   private String _keyStoreProvider;
/*  145 */   private String _keyStoreType = "JKS";
/*      */   private String _certAlias;
/*      */   private Resource _trustStoreResource;
/*      */   private String _trustStoreProvider;
/*      */   private String _trustStoreType;
/*  150 */   private boolean _needClientAuth = false;
/*  151 */   private boolean _wantClientAuth = false;
/*      */   private Password _keyStorePassword;
/*      */   private Password _keyManagerPassword;
/*      */   private Password _trustStorePassword;
/*      */   private String _sslProvider;
/*  156 */   private String _sslProtocol = "TLS";
/*      */   private String _secureRandomAlgorithm;
/*  158 */   private String _keyManagerFactoryAlgorithm = DEFAULT_KEYMANAGERFACTORY_ALGORITHM;
/*  159 */   private String _trustManagerFactoryAlgorithm = DEFAULT_TRUSTMANAGERFACTORY_ALGORITHM;
/*      */   private boolean _validateCerts;
/*      */   private boolean _validatePeerCerts;
/*  162 */   private int _maxCertPathLength = -1;
/*      */   private String _crlPath;
/*  164 */   private boolean _enableCRLDP = false;
/*  165 */   private boolean _enableOCSP = false;
/*      */   private String _ocspResponderURL;
/*      */   private KeyStore _setKeyStore;
/*      */   private KeyStore _setTrustStore;
/*  169 */   private boolean _sessionCachingEnabled = true;
/*  170 */   private int _sslSessionCacheSize = -1;
/*  171 */   private int _sslSessionTimeout = -1;
/*      */   private SSLContext _setContext;
/*  173 */   private String _endpointIdentificationAlgorithm = null;
/*      */   private boolean _trustAll;
/*  175 */   private boolean _renegotiationAllowed = true;
/*  176 */   private int _renegotiationLimit = 5;
/*      */   
/*      */ 
/*      */   private Factory _factory;
/*      */   
/*      */   private PKIXCertPathChecker _pkixCertPathChecker;
/*      */   
/*      */ 
/*      */   public SslContextFactory()
/*      */   {
/*  186 */     this(false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SslContextFactory(boolean trustAll)
/*      */   {
/*  198 */     this(trustAll, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SslContextFactory(String keyStorePath)
/*      */   {
/*  208 */     this(false, keyStorePath);
/*      */   }
/*      */   
/*      */   private SslContextFactory(boolean trustAll, String keyStorePath)
/*      */   {
/*  213 */     setTrustAll(trustAll);
/*  214 */     addExcludeProtocols(new String[] { "SSL", "SSLv2", "SSLv2Hello", "SSLv3" });
/*  215 */     setExcludeCipherSuites(new String[] { "^.*_(MD5|SHA|SHA1)$" });
/*  216 */     if (keyStorePath != null) {
/*  217 */       setKeyStorePath(keyStorePath);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void doStart()
/*      */     throws Exception
/*      */   {
/*  226 */     super.doStart();
/*  227 */     synchronized (this)
/*      */     {
/*  229 */       load();
/*      */     }
/*      */   }
/*      */   
/*      */   private void load() throws Exception
/*      */   {
/*  235 */     SSLContext context = this._setContext;
/*  236 */     KeyStore keyStore = this._setKeyStore;
/*  237 */     KeyStore trustStore = this._setTrustStore;
/*      */     
/*  239 */     if (context == null)
/*      */     {
/*      */       String algorithm;
/*  242 */       if ((keyStore == null) && (this._keyStoreResource == null) && (trustStore == null) && (this._trustStoreResource == null))
/*      */       {
/*  244 */         TrustManager[] trust_managers = null;
/*      */         
/*  246 */         if (isTrustAll())
/*      */         {
/*  248 */           if (LOG.isDebugEnabled()) {
/*  249 */             LOG.debug("No keystore or trust store configured.  ACCEPTING UNTRUSTED CERTIFICATES!!!!!", new Object[0]);
/*      */           }
/*  251 */           trust_managers = TRUST_ALL_CERTS;
/*      */         }
/*      */         
/*  254 */         algorithm = getSecureRandomAlgorithm();
/*  255 */         SecureRandom secureRandom = algorithm == null ? null : SecureRandom.getInstance(algorithm);
/*  256 */         context = this._sslProvider == null ? SSLContext.getInstance(this._sslProtocol) : SSLContext.getInstance(this._sslProtocol, this._sslProvider);
/*  257 */         context.init(null, trust_managers, secureRandom);
/*      */       }
/*      */       else
/*      */       {
/*  261 */         if (keyStore == null)
/*  262 */           keyStore = loadKeyStore(this._keyStoreResource);
/*  263 */         if (trustStore == null) {
/*  264 */           trustStore = loadTrustStore(this._trustStoreResource);
/*      */         }
/*  266 */         Collection<? extends CRL> crls = loadCRL(getCrlPath());
/*      */         
/*      */ 
/*  269 */         if (keyStore != null)
/*      */         {
/*  271 */           for (String alias : Collections.list(keyStore.aliases()))
/*      */           {
/*  273 */             Certificate certificate = keyStore.getCertificate(alias);
/*  274 */             if ((certificate != null) && ("X.509".equals(certificate.getType())))
/*      */             {
/*  276 */               X509Certificate x509C = (X509Certificate)certificate;
/*      */               
/*      */ 
/*  279 */               if (X509.isCertSign(x509C))
/*      */               {
/*  281 */                 if (LOG.isDebugEnabled()) {
/*  282 */                   LOG.debug("Skipping " + x509C, new Object[0]);
/*      */                 }
/*      */               } else {
/*  285 */                 x509 = new X509(alias, x509C);
/*  286 */                 this._aliasX509.put(alias, x509);
/*      */                 
/*  288 */                 if (isValidateCerts())
/*      */                 {
/*  290 */                   validator = new CertificateValidator(trustStore, crls);
/*  291 */                   validator.setMaxCertPathLength(getMaxCertPathLength());
/*  292 */                   validator.setEnableCRLDP(isEnableCRLDP());
/*  293 */                   validator.setEnableOCSP(isEnableOCSP());
/*  294 */                   validator.setOcspResponderURL(getOcspResponderURL());
/*  295 */                   validator.validate(keyStore, x509C);
/*      */                 }
/*      */                 
/*  298 */                 LOG.info("x509={} for {}", new Object[] { x509, this });
/*      */                 
/*  300 */                 for (String h : x509.getHosts())
/*  301 */                   this._certHosts.put(h, x509);
/*  302 */                 for (String w : x509.getWilds())
/*  303 */                   this._certWilds.put(w, x509);
/*      */               }
/*      */             }
/*      */           } }
/*      */         X509 x509;
/*      */         CertificateValidator validator;
/*  309 */         KeyManager[] keyManagers = getKeyManagers(keyStore);
/*  310 */         TrustManager[] trustManagers = getTrustManagers(trustStore, crls);
/*      */         
/*      */ 
/*  313 */         SecureRandom secureRandom = this._secureRandomAlgorithm == null ? null : SecureRandom.getInstance(this._secureRandomAlgorithm);
/*  314 */         context = this._sslProvider == null ? SSLContext.getInstance(this._sslProtocol) : SSLContext.getInstance(this._sslProtocol, this._sslProvider);
/*  315 */         context.init(keyManagers, trustManagers, secureRandom);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  320 */     SSLSessionContext serverContext = context.getServerSessionContext();
/*  321 */     if (serverContext != null)
/*      */     {
/*  323 */       if (getSslSessionCacheSize() > -1)
/*  324 */         serverContext.setSessionCacheSize(getSslSessionCacheSize());
/*  325 */       if (getSslSessionTimeout() > -1) {
/*  326 */         serverContext.setSessionTimeout(getSslSessionTimeout());
/*      */       }
/*      */     }
/*      */     
/*  330 */     SSLParameters enabled = context.getDefaultSSLParameters();
/*  331 */     SSLParameters supported = context.getSupportedSSLParameters();
/*  332 */     selectCipherSuites(enabled.getCipherSuites(), supported.getCipherSuites());
/*  333 */     selectProtocols(enabled.getProtocols(), supported.getProtocols());
/*      */     
/*  335 */     this._factory = new Factory(keyStore, trustStore, context);
/*  336 */     if (LOG.isDebugEnabled())
/*      */     {
/*  338 */       LOG.debug("Selected Protocols {} of {}", new Object[] { Arrays.asList(this._selectedProtocols), Arrays.asList(supported.getProtocols()) });
/*  339 */       LOG.debug("Selected Ciphers   {} of {}", new Object[] { Arrays.asList(this._selectedCipherSuites), Arrays.asList(supported.getCipherSuites()) });
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public String dump()
/*      */   {
/*  346 */     return ContainerLifeCycle.dump(this);
/*      */   }
/*      */   
/*      */   public void dump(Appendable out, String indent)
/*      */     throws IOException
/*      */   {
/*  352 */     out.append(String.valueOf(this)).append(" trustAll=").append(Boolean.toString(this._trustAll)).append(System.lineSeparator());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  360 */       SSLEngine sslEngine = SSLContext.getDefault().createSSLEngine();
/*      */       
/*  362 */       List<Object> selections = new ArrayList();
/*      */       
/*      */ 
/*  365 */       selections.add(new SslSelectionDump("Protocol", sslEngine
/*  366 */         .getSupportedProtocols(), sslEngine
/*  367 */         .getEnabledProtocols(), 
/*  368 */         getExcludeProtocols(), 
/*  369 */         getIncludeProtocols()));
/*      */       
/*      */ 
/*  372 */       selections.add(new SslSelectionDump("Cipher Suite", sslEngine
/*  373 */         .getSupportedCipherSuites(), sslEngine
/*  374 */         .getEnabledCipherSuites(), 
/*  375 */         getExcludeCipherSuites(), 
/*  376 */         getIncludeCipherSuites()));
/*      */       
/*  378 */       ContainerLifeCycle.dump(out, indent, new Collection[] { selections });
/*      */     }
/*      */     catch (NoSuchAlgorithmException ignore)
/*      */     {
/*  382 */       LOG.ignore(ignore);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void doStop()
/*      */     throws Exception
/*      */   {
/*  389 */     synchronized (this)
/*      */     {
/*  391 */       unload();
/*      */     }
/*  393 */     super.doStop();
/*      */   }
/*      */   
/*      */   private void unload()
/*      */   {
/*  398 */     this._factory = null;
/*  399 */     this._selectedProtocols = null;
/*  400 */     this._selectedCipherSuites = null;
/*  401 */     this._aliasX509.clear();
/*  402 */     this._certHosts.clear();
/*  403 */     this._certWilds.clear();
/*      */   }
/*      */   
/*      */   @ManagedAttribute(value="The selected TLS protocol versions", readonly=true)
/*      */   public String[] getSelectedProtocols()
/*      */   {
/*  409 */     return (String[])Arrays.copyOf(this._selectedProtocols, this._selectedProtocols.length);
/*      */   }
/*      */   
/*      */   @ManagedAttribute(value="The selected cipher suites", readonly=true)
/*      */   public String[] getSelectedCipherSuites()
/*      */   {
/*  415 */     return (String[])Arrays.copyOf(this._selectedCipherSuites, this._selectedCipherSuites.length);
/*      */   }
/*      */   
/*      */   public Comparator<String> getCipherComparator()
/*      */   {
/*  420 */     return this._cipherComparator;
/*      */   }
/*      */   
/*      */   public void setCipherComparator(Comparator<String> cipherComparator)
/*      */   {
/*  425 */     if (cipherComparator != null)
/*  426 */       setUseCipherSuitesOrder(true);
/*  427 */     this._cipherComparator = cipherComparator;
/*      */   }
/*      */   
/*      */   public Set<String> getAliases()
/*      */   {
/*  432 */     return Collections.unmodifiableSet(this._aliasX509.keySet());
/*      */   }
/*      */   
/*      */   public X509 getX509(String alias)
/*      */   {
/*  437 */     return (X509)this._aliasX509.get(alias);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The excluded TLS protocols")
/*      */   public String[] getExcludeProtocols()
/*      */   {
/*  447 */     return (String[])this._excludeProtocols.toArray(new String[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExcludeProtocols(String... protocols)
/*      */   {
/*  456 */     this._excludeProtocols.clear();
/*  457 */     this._excludeProtocols.addAll(Arrays.asList(protocols));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addExcludeProtocols(String... protocol)
/*      */   {
/*  465 */     this._excludeProtocols.addAll(Arrays.asList(protocol));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The included TLS protocols")
/*      */   public String[] getIncludeProtocols()
/*      */   {
/*  475 */     return (String[])this._includeProtocols.toArray(new String[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIncludeProtocols(String... protocols)
/*      */   {
/*  484 */     this._includeProtocols.clear();
/*  485 */     this._includeProtocols.addAll(Arrays.asList(protocols));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The excluded cipher suites")
/*      */   public String[] getExcludeCipherSuites()
/*      */   {
/*  495 */     return (String[])this._excludeCipherSuites.toArray(new String[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExcludeCipherSuites(String... cipherSuites)
/*      */   {
/*  506 */     this._excludeCipherSuites.clear();
/*  507 */     this._excludeCipherSuites.addAll(Arrays.asList(cipherSuites));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addExcludeCipherSuites(String... cipher)
/*      */   {
/*  515 */     this._excludeCipherSuites.addAll(Arrays.asList(cipher));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The included cipher suites")
/*      */   public String[] getIncludeCipherSuites()
/*      */   {
/*  525 */     return (String[])this._includeCipherSuites.toArray(new String[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setIncludeCipherSuites(String... cipherSuites)
/*      */   {
/*  536 */     this._includeCipherSuites.clear();
/*  537 */     this._includeCipherSuites.addAll(Arrays.asList(cipherSuites));
/*      */   }
/*      */   
/*      */   @ManagedAttribute("Whether to respect the cipher suites order")
/*      */   public boolean isUseCipherSuitesOrder()
/*      */   {
/*  543 */     return this._useCipherSuitesOrder;
/*      */   }
/*      */   
/*      */   public void setUseCipherSuitesOrder(boolean useCipherSuitesOrder)
/*      */   {
/*  548 */     this._useCipherSuitesOrder = useCipherSuitesOrder;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The keyStore path")
/*      */   public String getKeyStorePath()
/*      */   {
/*  557 */     return Objects.toString(this._keyStoreResource, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setKeyStorePath(String keyStorePath)
/*      */   {
/*      */     try
/*      */     {
/*  567 */       this._keyStoreResource = Resource.newResource(keyStorePath);
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  571 */       throw new IllegalArgumentException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The keyStore provider name")
/*      */   public String getKeyStoreProvider()
/*      */   {
/*  581 */     return this._keyStoreProvider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setKeyStoreProvider(String keyStoreProvider)
/*      */   {
/*  589 */     this._keyStoreProvider = keyStoreProvider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The keyStore type")
/*      */   public String getKeyStoreType()
/*      */   {
/*  598 */     return this._keyStoreType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setKeyStoreType(String keyStoreType)
/*      */   {
/*  606 */     this._keyStoreType = keyStoreType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The certificate alias")
/*      */   public String getCertAlias()
/*      */   {
/*  615 */     return this._certAlias;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCertAlias(String certAlias)
/*      */   {
/*  629 */     this._certAlias = certAlias;
/*      */   }
/*      */   
/*      */   @ManagedAttribute("The trustStore path")
/*      */   public String getTrustStorePath()
/*      */   {
/*  635 */     return Objects.toString(this._trustStoreResource, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustStorePath(String trustStorePath)
/*      */   {
/*      */     try
/*      */     {
/*  645 */       this._trustStoreResource = Resource.newResource(trustStorePath);
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/*  649 */       throw new IllegalArgumentException(e);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The trustStore provider name")
/*      */   public String getTrustStoreProvider()
/*      */   {
/*  659 */     return this._trustStoreProvider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustStoreProvider(String trustStoreProvider)
/*      */   {
/*  667 */     this._trustStoreProvider = trustStoreProvider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The trustStore type")
/*      */   public String getTrustStoreType()
/*      */   {
/*  676 */     return this._trustStoreType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustStoreType(String trustStoreType)
/*      */   {
/*  684 */     this._trustStoreType = trustStoreType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether client authentication is needed")
/*      */   public boolean getNeedClientAuth()
/*      */   {
/*  694 */     return this._needClientAuth;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNeedClientAuth(boolean needClientAuth)
/*      */   {
/*  703 */     this._needClientAuth = needClientAuth;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether client authentication is wanted")
/*      */   public boolean getWantClientAuth()
/*      */   {
/*  713 */     return this._wantClientAuth;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setWantClientAuth(boolean wantClientAuth)
/*      */   {
/*  722 */     this._wantClientAuth = wantClientAuth;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether certificates are validated")
/*      */   public boolean isValidateCerts()
/*      */   {
/*  731 */     return this._validateCerts;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setValidateCerts(boolean validateCerts)
/*      */   {
/*  739 */     this._validateCerts = validateCerts;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether peer certificates are validated")
/*      */   public boolean isValidatePeerCerts()
/*      */   {
/*  748 */     return this._validatePeerCerts;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setValidatePeerCerts(boolean validatePeerCerts)
/*      */   {
/*  756 */     this._validatePeerCerts = validatePeerCerts;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setKeyStorePassword(String password)
/*      */   {
/*  768 */     if (password == null)
/*      */     {
/*  770 */       if (this._keyStoreResource != null) {
/*  771 */         this._keyStorePassword = getPassword("org.eclipse.jetty.ssl.password");
/*      */       } else {
/*  773 */         this._keyStorePassword = null;
/*      */       }
/*      */     }
/*      */     else {
/*  777 */       this._keyStorePassword = newPassword(password);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setKeyManagerPassword(String password)
/*      */   {
/*  789 */     if (password == null)
/*      */     {
/*  791 */       if (System.getProperty("org.eclipse.jetty.ssl.keypassword") != null) {
/*  792 */         this._keyManagerPassword = getPassword("org.eclipse.jetty.ssl.keypassword");
/*      */       } else {
/*  794 */         this._keyManagerPassword = null;
/*      */       }
/*      */     }
/*      */     else {
/*  798 */       this._keyManagerPassword = newPassword(password);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustStorePassword(String password)
/*      */   {
/*  811 */     if (password == null)
/*      */     {
/*  813 */       if ((this._trustStoreResource != null) && (!this._trustStoreResource.equals(this._keyStoreResource))) {
/*  814 */         this._trustStorePassword = getPassword("org.eclipse.jetty.ssl.password");
/*      */       } else {
/*  816 */         this._trustStorePassword = null;
/*      */       }
/*      */     }
/*      */     else {
/*  820 */       this._trustStorePassword = newPassword(password);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The provider name")
/*      */   public String getProvider()
/*      */   {
/*  831 */     return this._sslProvider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProvider(String provider)
/*      */   {
/*  840 */     this._sslProvider = provider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The TLS protocol")
/*      */   public String getProtocol()
/*      */   {
/*  850 */     return this._sslProtocol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setProtocol(String protocol)
/*      */   {
/*  859 */     this._sslProtocol = protocol;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The SecureRandom algorithm")
/*      */   public String getSecureRandomAlgorithm()
/*      */   {
/*  870 */     return this._secureRandomAlgorithm;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSecureRandomAlgorithm(String algorithm)
/*      */   {
/*  880 */     this._secureRandomAlgorithm = algorithm;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The KeyManagerFactory algorithm")
/*      */   public String getKeyManagerFactoryAlgorithm()
/*      */   {
/*  889 */     return this._keyManagerFactoryAlgorithm;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setKeyManagerFactoryAlgorithm(String algorithm)
/*      */   {
/*  897 */     this._keyManagerFactoryAlgorithm = algorithm;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The TrustManagerFactory algorithm")
/*      */   public String getTrustManagerFactoryAlgorithm()
/*      */   {
/*  906 */     return this._trustManagerFactoryAlgorithm;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether certificates should be trusted even if they are invalid")
/*      */   public boolean isTrustAll()
/*      */   {
/*  915 */     return this._trustAll;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustAll(boolean trustAll)
/*      */   {
/*  923 */     this._trustAll = trustAll;
/*  924 */     if (trustAll) {
/*  925 */       setEndpointIdentificationAlgorithm(null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustManagerFactoryAlgorithm(String algorithm)
/*      */   {
/*  934 */     this._trustManagerFactoryAlgorithm = algorithm;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether renegotiation is allowed")
/*      */   public boolean isRenegotiationAllowed()
/*      */   {
/*  943 */     return this._renegotiationAllowed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRenegotiationAllowed(boolean renegotiationAllowed)
/*      */   {
/*  951 */     this._renegotiationAllowed = renegotiationAllowed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The max number of renegotiations allowed")
/*      */   public int getRenegotiationLimit()
/*      */   {
/*  961 */     return this._renegotiationLimit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRenegotiationLimit(int renegotiationLimit)
/*      */   {
/*  971 */     this._renegotiationLimit = renegotiationLimit;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The path to the certificate revocation list file")
/*      */   public String getCrlPath()
/*      */   {
/*  980 */     return this._crlPath;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCrlPath(String crlPath)
/*      */   {
/*  988 */     this._crlPath = crlPath;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The maximum number of intermediate certificates")
/*      */   public int getMaxCertPathLength()
/*      */   {
/*  998 */     return this._maxCertPathLength;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMaxCertPathLength(int maxCertPathLength)
/*      */   {
/* 1007 */     this._maxCertPathLength = maxCertPathLength;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public SSLContext getSslContext()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 195	org/eclipse/jetty/util/ssl/SslContextFactory:isStarted	()Z
/*      */     //   4: ifne +8 -> 12
/*      */     //   7: aload_0
/*      */     //   8: getfield 50	org/eclipse/jetty/util/ssl/SslContextFactory:_setContext	Ljavax/net/ssl/SSLContext;
/*      */     //   11: areturn
/*      */     //   12: aload_0
/*      */     //   13: dup
/*      */     //   14: astore_1
/*      */     //   15: monitorenter
/*      */     //   16: aload_0
/*      */     //   17: getfield 124	org/eclipse/jetty/util/ssl/SslContextFactory:_factory	Lorg/eclipse/jetty/util/ssl/SslContextFactory$Factory;
/*      */     //   20: invokestatic 196	org/eclipse/jetty/util/ssl/SslContextFactory$Factory:access$000	(Lorg/eclipse/jetty/util/ssl/SslContextFactory$Factory;)Ljavax/net/ssl/SSLContext;
/*      */     //   23: aload_1
/*      */     //   24: monitorexit
/*      */     //   25: areturn
/*      */     //   26: astore_2
/*      */     //   27: aload_1
/*      */     //   28: monitorexit
/*      */     //   29: aload_2
/*      */     //   30: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1015	-> byte code offset #0
/*      */     //   Java source line #1016	-> byte code offset #7
/*      */     //   Java source line #1018	-> byte code offset #12
/*      */     //   Java source line #1020	-> byte code offset #16
/*      */     //   Java source line #1021	-> byte code offset #26
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	31	0	this	SslContextFactory
/*      */     //   14	14	1	Ljava/lang/Object;	Object
/*      */     //   26	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   16	25	26	finally
/*      */     //   26	29	26	finally
/*      */   }
/*      */   
/*      */   public void setSslContext(SSLContext sslContext)
/*      */   {
/* 1029 */     this._setContext = sslContext;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The endpoint identification algorithm")
/*      */   public String getEndpointIdentificationAlgorithm()
/*      */   {
/* 1038 */     return this._endpointIdentificationAlgorithm;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEndpointIdentificationAlgorithm(String endpointIdentificationAlgorithm)
/*      */   {
/* 1048 */     this._endpointIdentificationAlgorithm = endpointIdentificationAlgorithm;
/*      */   }
/*      */   
/*      */   public PKIXCertPathChecker getPkixCertPathChecker()
/*      */   {
/* 1053 */     return this._pkixCertPathChecker;
/*      */   }
/*      */   
/*      */   public void setPkixCertPathChecker(PKIXCertPathChecker pkixCertPatchChecker)
/*      */   {
/* 1058 */     this._pkixCertPathChecker = pkixCertPatchChecker;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected KeyStore loadKeyStore(Resource resource)
/*      */     throws Exception
/*      */   {
/* 1070 */     String storePassword = Objects.toString(this._keyStorePassword, null);
/* 1071 */     return CertificateUtils.getKeyStore(resource, getKeyStoreType(), getKeyStoreProvider(), storePassword);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected KeyStore loadTrustStore(Resource resource)
/*      */     throws Exception
/*      */   {
/* 1083 */     String type = Objects.toString(getTrustStoreType(), getKeyStoreType());
/* 1084 */     String provider = Objects.toString(getTrustStoreProvider(), getKeyStoreProvider());
/* 1085 */     String passwd = Objects.toString(this._trustStorePassword, Objects.toString(this._keyStorePassword, null));
/* 1086 */     if (resource == null)
/* 1087 */       resource = this._keyStoreResource;
/* 1088 */     return CertificateUtils.getKeyStore(resource, type, provider, passwd);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Collection<? extends CRL> loadCRL(String crlPath)
/*      */     throws Exception
/*      */   {
/* 1103 */     return CertificateUtils.loadCRL(crlPath);
/*      */   }
/*      */   
/*      */   protected KeyManager[] getKeyManagers(KeyStore keyStore) throws Exception
/*      */   {
/* 1108 */     KeyManager[] managers = null;
/*      */     
/* 1110 */     if (keyStore != null)
/*      */     {
/* 1112 */       KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(getKeyManagerFactoryAlgorithm());
/* 1113 */       keyManagerFactory.init(keyStore, this._keyManagerPassword == null ? this._keyStorePassword.toString().toCharArray() : this._keyStorePassword == null ? null : this._keyManagerPassword.toString().toCharArray());
/* 1114 */       managers = keyManagerFactory.getKeyManagers();
/*      */       
/* 1116 */       if (managers != null)
/*      */       {
/* 1118 */         String alias = getCertAlias();
/* 1119 */         if (alias != null)
/*      */         {
/* 1121 */           for (int idx = 0; idx < managers.length; idx++)
/*      */           {
/* 1123 */             if ((managers[idx] instanceof X509ExtendedKeyManager)) {
/* 1124 */               managers[idx] = new AliasedX509ExtendedKeyManager((X509ExtendedKeyManager)managers[idx], alias);
/*      */             }
/*      */           }
/*      */         }
/* 1128 */         if ((!this._certWilds.isEmpty()) || (this._certHosts.size() > 1))
/*      */         {
/* 1130 */           for (int idx = 0; idx < managers.length; idx++)
/*      */           {
/* 1132 */             if ((managers[idx] instanceof X509ExtendedKeyManager)) {
/* 1133 */               managers[idx] = new SniX509ExtendedKeyManager((X509ExtendedKeyManager)managers[idx]);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1139 */     if (LOG.isDebugEnabled()) {
/* 1140 */       LOG.debug("managers={} for {}", new Object[] { managers, this });
/*      */     }
/* 1142 */     return managers;
/*      */   }
/*      */   
/*      */   protected TrustManager[] getTrustManagers(KeyStore trustStore, Collection<? extends CRL> crls) throws Exception
/*      */   {
/* 1147 */     TrustManager[] managers = null;
/* 1148 */     if (trustStore != null)
/*      */     {
/*      */ 
/* 1151 */       if ((isValidatePeerCerts()) && ("PKIX".equalsIgnoreCase(getTrustManagerFactoryAlgorithm())))
/*      */       {
/* 1153 */         PKIXBuilderParameters pbParams = newPKIXBuilderParameters(trustStore, crls);
/*      */         
/* 1155 */         TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(this._trustManagerFactoryAlgorithm);
/* 1156 */         trustManagerFactory.init(new CertPathTrustManagerParameters(pbParams));
/*      */         
/* 1158 */         managers = trustManagerFactory.getTrustManagers();
/*      */       }
/*      */       else
/*      */       {
/* 1162 */         TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(this._trustManagerFactoryAlgorithm);
/* 1163 */         trustManagerFactory.init(trustStore);
/*      */         
/* 1165 */         managers = trustManagerFactory.getTrustManagers();
/*      */       }
/*      */     }
/*      */     
/* 1169 */     return managers;
/*      */   }
/*      */   
/*      */   protected PKIXBuilderParameters newPKIXBuilderParameters(KeyStore trustStore, Collection<? extends CRL> crls) throws Exception
/*      */   {
/* 1174 */     PKIXBuilderParameters pbParams = new PKIXBuilderParameters(trustStore, new X509CertSelector());
/*      */     
/*      */ 
/* 1177 */     pbParams.setMaxPathLength(this._maxCertPathLength);
/*      */     
/*      */ 
/* 1180 */     pbParams.setRevocationEnabled(true);
/*      */     
/* 1182 */     if (this._pkixCertPathChecker != null) {
/* 1183 */       pbParams.addCertPathChecker(this._pkixCertPathChecker);
/*      */     }
/* 1185 */     if ((crls != null) && (!crls.isEmpty()))
/*      */     {
/* 1187 */       pbParams.addCertStore(CertStore.getInstance("Collection", new CollectionCertStoreParameters(crls)));
/*      */     }
/*      */     
/* 1190 */     if (this._enableCRLDP)
/*      */     {
/*      */ 
/* 1193 */       System.setProperty("com.sun.security.enableCRLDP", "true");
/*      */     }
/*      */     
/* 1196 */     if (this._enableOCSP)
/*      */     {
/*      */ 
/* 1199 */       Security.setProperty("ocsp.enable", "true");
/*      */       
/* 1201 */       if (this._ocspResponderURL != null)
/*      */       {
/*      */ 
/* 1204 */         Security.setProperty("ocsp.responderURL", this._ocspResponderURL);
/*      */       }
/*      */     }
/*      */     
/* 1208 */     return pbParams;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void selectProtocols(String[] enabledProtocols, String[] supportedProtocols)
/*      */   {
/* 1221 */     Set<String> selected_protocols = new LinkedHashSet();
/*      */     
/*      */ 
/* 1224 */     if (!this._includeProtocols.isEmpty())
/*      */     {
/*      */ 
/* 1227 */       for (String protocol : this._includeProtocols)
/*      */       {
/* 1229 */         if (Arrays.asList(supportedProtocols).contains(protocol)) {
/* 1230 */           selected_protocols.add(protocol);
/*      */         } else {
/* 1232 */           LOG.info("Protocol {} not supported in {}", new Object[] { protocol, Arrays.asList(supportedProtocols) });
/*      */         }
/*      */       }
/*      */     } else {
/* 1236 */       selected_protocols.addAll(Arrays.asList(enabledProtocols));
/*      */     }
/*      */     
/* 1239 */     selected_protocols.removeAll(this._excludeProtocols);
/*      */     
/* 1241 */     if (selected_protocols.isEmpty()) {
/* 1242 */       LOG.warn("No selected protocols from {}", new Object[] { Arrays.asList(supportedProtocols) });
/*      */     }
/* 1244 */     this._selectedProtocols = ((String[])selected_protocols.toArray(new String[0]));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void selectCipherSuites(String[] enabledCipherSuites, String[] supportedCipherSuites)
/*      */   {
/* 1257 */     List<String> selected_ciphers = new ArrayList();
/*      */     
/*      */ 
/* 1260 */     if (this._includeCipherSuites.isEmpty()) {
/* 1261 */       selected_ciphers.addAll(Arrays.asList(enabledCipherSuites));
/*      */     } else {
/* 1263 */       processIncludeCipherSuites(supportedCipherSuites, selected_ciphers);
/*      */     }
/* 1265 */     removeExcludedCipherSuites(selected_ciphers);
/*      */     
/* 1267 */     if (selected_ciphers.isEmpty()) {
/* 1268 */       LOG.warn("No supported ciphers from {}", new Object[] { Arrays.asList(supportedCipherSuites) });
/*      */     }
/* 1270 */     Comparator<String> comparator = getCipherComparator();
/* 1271 */     if (comparator != null)
/*      */     {
/* 1273 */       if (LOG.isDebugEnabled())
/* 1274 */         LOG.debug("Sorting selected ciphers with {}", new Object[] { comparator });
/* 1275 */       selected_ciphers.sort(comparator);
/*      */     }
/*      */     
/* 1278 */     this._selectedCipherSuites = ((String[])selected_ciphers.toArray(new String[0]));
/*      */   }
/*      */   
/*      */   protected void processIncludeCipherSuites(String[] supportedCipherSuites, List<String> selected_ciphers)
/*      */   {
/* 1283 */     for (String cipherSuite : this._includeCipherSuites)
/*      */     {
/* 1285 */       Pattern p = Pattern.compile(cipherSuite);
/* 1286 */       boolean added = false;
/* 1287 */       for (String supportedCipherSuite : supportedCipherSuites)
/*      */       {
/* 1289 */         Matcher m = p.matcher(supportedCipherSuite);
/* 1290 */         if (m.matches())
/*      */         {
/* 1292 */           added = true;
/* 1293 */           selected_ciphers.add(supportedCipherSuite);
/*      */         }
/*      */       }
/*      */       
/* 1297 */       if (!added) {
/* 1298 */         LOG.info("No Cipher matching '{}' is supported", new Object[] { cipherSuite });
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void removeExcludedCipherSuites(List<String> selected_ciphers) {
/* 1304 */     for (String excludeCipherSuite : this._excludeCipherSuites)
/*      */     {
/* 1306 */       excludeCipherPattern = Pattern.compile(excludeCipherSuite);
/* 1307 */       for (i = selected_ciphers.iterator(); i.hasNext();)
/*      */       {
/* 1309 */         String selectedCipherSuite = (String)i.next();
/* 1310 */         Matcher m = excludeCipherPattern.matcher(selectedCipherSuite);
/* 1311 */         if (m.matches()) {
/* 1312 */           i.remove();
/*      */         }
/*      */       }
/*      */     }
/*      */     Pattern excludeCipherPattern;
/*      */     Iterator<String> i;
/*      */   }
/*      */   
/*      */   private void checkIsStarted()
/*      */   {
/* 1322 */     if (!isStarted()) {
/* 1323 */       throw new IllegalStateException("!STARTED: " + this);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether certificate revocation list distribution points is enabled")
/*      */   public boolean isEnableCRLDP()
/*      */   {
/* 1332 */     return this._enableCRLDP;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEnableCRLDP(boolean enableCRLDP)
/*      */   {
/* 1342 */     this._enableCRLDP = enableCRLDP;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether online certificate status protocol support is enabled")
/*      */   public boolean isEnableOCSP()
/*      */   {
/* 1351 */     return this._enableOCSP;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setEnableOCSP(boolean enableOCSP)
/*      */   {
/* 1361 */     this._enableOCSP = enableOCSP;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The online certificate status protocol URL")
/*      */   public String getOcspResponderURL()
/*      */   {
/* 1370 */     return this._ocspResponderURL;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOcspResponderURL(String ocspResponderURL)
/*      */   {
/* 1380 */     this._ocspResponderURL = ocspResponderURL;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setKeyStore(KeyStore keyStore)
/*      */   {
/* 1390 */     this._setKeyStore = keyStore;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public KeyStore getKeyStore()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 195	org/eclipse/jetty/util/ssl/SslContextFactory:isStarted	()Z
/*      */     //   4: ifne +8 -> 12
/*      */     //   7: aload_0
/*      */     //   8: getfield 51	org/eclipse/jetty/util/ssl/SslContextFactory:_setKeyStore	Ljava/security/KeyStore;
/*      */     //   11: areturn
/*      */     //   12: aload_0
/*      */     //   13: dup
/*      */     //   14: astore_1
/*      */     //   15: monitorenter
/*      */     //   16: aload_0
/*      */     //   17: getfield 124	org/eclipse/jetty/util/ssl/SslContextFactory:_factory	Lorg/eclipse/jetty/util/ssl/SslContextFactory$Factory;
/*      */     //   20: invokestatic 273	org/eclipse/jetty/util/ssl/SslContextFactory$Factory:access$100	(Lorg/eclipse/jetty/util/ssl/SslContextFactory$Factory;)Ljava/security/KeyStore;
/*      */     //   23: aload_1
/*      */     //   24: monitorexit
/*      */     //   25: areturn
/*      */     //   26: astore_2
/*      */     //   27: aload_1
/*      */     //   28: monitorexit
/*      */     //   29: aload_2
/*      */     //   30: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1395	-> byte code offset #0
/*      */     //   Java source line #1396	-> byte code offset #7
/*      */     //   Java source line #1398	-> byte code offset #12
/*      */     //   Java source line #1400	-> byte code offset #16
/*      */     //   Java source line #1401	-> byte code offset #26
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	31	0	this	SslContextFactory
/*      */     //   14	14	1	Ljava/lang/Object;	Object
/*      */     //   26	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   16	25	26	finally
/*      */     //   26	29	26	finally
/*      */   }
/*      */   
/*      */   public void setTrustStore(KeyStore trustStore)
/*      */   {
/* 1411 */     this._setTrustStore = trustStore;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public KeyStore getTrustStore()
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: invokevirtual 195	org/eclipse/jetty/util/ssl/SslContextFactory:isStarted	()Z
/*      */     //   4: ifne +8 -> 12
/*      */     //   7: aload_0
/*      */     //   8: getfield 52	org/eclipse/jetty/util/ssl/SslContextFactory:_setTrustStore	Ljava/security/KeyStore;
/*      */     //   11: areturn
/*      */     //   12: aload_0
/*      */     //   13: dup
/*      */     //   14: astore_1
/*      */     //   15: monitorenter
/*      */     //   16: aload_0
/*      */     //   17: getfield 124	org/eclipse/jetty/util/ssl/SslContextFactory:_factory	Lorg/eclipse/jetty/util/ssl/SslContextFactory$Factory;
/*      */     //   20: invokestatic 274	org/eclipse/jetty/util/ssl/SslContextFactory$Factory:access$200	(Lorg/eclipse/jetty/util/ssl/SslContextFactory$Factory;)Ljava/security/KeyStore;
/*      */     //   23: aload_1
/*      */     //   24: monitorexit
/*      */     //   25: areturn
/*      */     //   26: astore_2
/*      */     //   27: aload_1
/*      */     //   28: monitorexit
/*      */     //   29: aload_2
/*      */     //   30: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1416	-> byte code offset #0
/*      */     //   Java source line #1417	-> byte code offset #7
/*      */     //   Java source line #1419	-> byte code offset #12
/*      */     //   Java source line #1421	-> byte code offset #16
/*      */     //   Java source line #1422	-> byte code offset #26
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	31	0	this	SslContextFactory
/*      */     //   14	14	1	Ljava/lang/Object;	Object
/*      */     //   26	4	2	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   16	25	26	finally
/*      */     //   26	29	26	finally
/*      */   }
/*      */   
/*      */   public void setKeyStoreResource(Resource resource)
/*      */   {
/* 1432 */     this._keyStoreResource = resource;
/*      */   }
/*      */   
/*      */   public Resource getKeyStoreResource()
/*      */   {
/* 1437 */     return this._keyStoreResource;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTrustStoreResource(Resource resource)
/*      */   {
/* 1447 */     this._trustStoreResource = resource;
/*      */   }
/*      */   
/*      */   public Resource getTrustStoreResource()
/*      */   {
/* 1452 */     return this._trustStoreResource;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("Whether TLS session caching is enabled")
/*      */   public boolean isSessionCachingEnabled()
/*      */   {
/* 1461 */     return this._sessionCachingEnabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionCachingEnabled(boolean enableSessionCaching)
/*      */   {
/* 1476 */     this._sessionCachingEnabled = enableSessionCaching;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The maximum TLS session cache size")
/*      */   public int getSslSessionCacheSize()
/*      */   {
/* 1488 */     return this._sslSessionCacheSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSslSessionCacheSize(int sslSessionCacheSize)
/*      */   {
/* 1501 */     this._sslSessionCacheSize = sslSessionCacheSize;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @ManagedAttribute("The TLS session cache timeout, in seconds")
/*      */   public int getSslSessionTimeout()
/*      */   {
/* 1512 */     return this._sslSessionTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSslSessionTimeout(int sslSessionTimeout)
/*      */   {
/* 1525 */     this._sslSessionTimeout = sslSessionTimeout;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Password getPassword(String realm)
/*      */   {
/* 1536 */     return Password.getPassword(realm, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Password newPassword(String password)
/*      */   {
/* 1547 */     return new Password(password);
/*      */   }
/*      */   
/*      */   public SSLServerSocket newSslServerSocket(String host, int port, int backlog) throws IOException
/*      */   {
/* 1552 */     checkIsStarted();
/*      */     
/* 1554 */     SSLContext context = getSslContext();
/* 1555 */     SSLServerSocketFactory factory = context.getServerSocketFactory();
/*      */     
/*      */ 
/*      */ 
/* 1559 */     SSLServerSocket socket = (SSLServerSocket)(host == null ? factory.createServerSocket(port, backlog) : factory.createServerSocket(port, backlog, InetAddress.getByName(host)));
/* 1560 */     socket.setSSLParameters(customize(socket.getSSLParameters()));
/*      */     
/* 1562 */     return socket;
/*      */   }
/*      */   
/*      */   public SSLSocket newSslSocket() throws IOException
/*      */   {
/* 1567 */     checkIsStarted();
/*      */     
/* 1569 */     SSLContext context = getSslContext();
/* 1570 */     SSLSocketFactory factory = context.getSocketFactory();
/* 1571 */     SSLSocket socket = (SSLSocket)factory.createSocket();
/* 1572 */     socket.setSSLParameters(customize(socket.getSSLParameters()));
/*      */     
/* 1574 */     return socket;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SSLEngine newSSLEngine()
/*      */   {
/* 1588 */     checkIsStarted();
/*      */     
/* 1590 */     SSLContext context = getSslContext();
/* 1591 */     SSLEngine sslEngine = context.createSSLEngine();
/* 1592 */     customize(sslEngine);
/*      */     
/* 1594 */     return sslEngine;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SSLEngine newSSLEngine(String host, int port)
/*      */   {
/* 1607 */     checkIsStarted();
/*      */     
/* 1609 */     SSLContext context = getSslContext();
/*      */     
/*      */ 
/* 1612 */     SSLEngine sslEngine = isSessionCachingEnabled() ? context.createSSLEngine(host, port) : context.createSSLEngine();
/* 1613 */     customize(sslEngine);
/*      */     
/* 1615 */     return sslEngine;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SSLEngine newSSLEngine(InetSocketAddress address)
/*      */   {
/* 1631 */     if (address == null)
/* 1632 */       return newSSLEngine();
/* 1633 */     return newSSLEngine(address.getHostString(), address.getPort());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void customize(SSLEngine sslEngine)
/*      */   {
/* 1644 */     if (LOG.isDebugEnabled()) {
/* 1645 */       LOG.debug("Customize {}", new Object[] { sslEngine });
/*      */     }
/* 1647 */     sslEngine.setSSLParameters(customize(sslEngine.getSSLParameters()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SSLParameters customize(SSLParameters sslParams)
/*      */   {
/* 1658 */     sslParams.setEndpointIdentificationAlgorithm(getEndpointIdentificationAlgorithm());
/* 1659 */     sslParams.setUseCipherSuitesOrder(isUseCipherSuitesOrder());
/* 1660 */     if ((!this._certHosts.isEmpty()) || (!this._certWilds.isEmpty()))
/* 1661 */       sslParams.setSNIMatchers(Collections.singletonList(new AliasSNIMatcher()));
/* 1662 */     if (this._selectedCipherSuites != null)
/* 1663 */       sslParams.setCipherSuites(this._selectedCipherSuites);
/* 1664 */     if (this._selectedProtocols != null)
/* 1665 */       sslParams.setProtocols(this._selectedProtocols);
/* 1666 */     if (getWantClientAuth())
/* 1667 */       sslParams.setWantClientAuth(true);
/* 1668 */     if (getNeedClientAuth())
/* 1669 */       sslParams.setNeedClientAuth(true);
/* 1670 */     return sslParams;
/*      */   }
/*      */   
/*      */   public void reload(Consumer<SslContextFactory> consumer) throws Exception
/*      */   {
/* 1675 */     synchronized (this)
/*      */     {
/* 1677 */       consumer.accept(this);
/* 1678 */       unload();
/* 1679 */       load();
/*      */     }
/*      */   }
/*      */   
/*      */   public static X509Certificate[] getCertChain(SSLSession sslSession)
/*      */   {
/*      */     try
/*      */     {
/* 1687 */       Certificate[] javaxCerts = sslSession.getPeerCertificates();
/* 1688 */       if ((javaxCerts == null) || (javaxCerts.length == 0)) {
/* 1689 */         return null;
/*      */       }
/* 1691 */       int length = javaxCerts.length;
/* 1692 */       X509Certificate[] javaCerts = new X509Certificate[length];
/*      */       
/* 1694 */       CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 1695 */       for (int i = 0; i < length; i++)
/*      */       {
/* 1697 */         byte[] bytes = javaxCerts[i].getEncoded();
/* 1698 */         ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
/* 1699 */         javaCerts[i] = ((X509Certificate)cf.generateCertificate(stream));
/*      */       }
/*      */       
/* 1702 */       return javaCerts;
/*      */     }
/*      */     catch (SSLPeerUnverifiedException pue)
/*      */     {
/* 1706 */       return null;
/*      */     }
/*      */     catch (Exception e)
/*      */     {
/* 1710 */       LOG.warn("EXCEPTION ", e); }
/* 1711 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int deduceKeyLength(String cipherSuite)
/*      */   {
/* 1742 */     if (cipherSuite == null)
/* 1743 */       return 0;
/* 1744 */     if (cipherSuite.contains("WITH_AES_256_"))
/* 1745 */       return 256;
/* 1746 */     if (cipherSuite.contains("WITH_RC4_128_"))
/* 1747 */       return 128;
/* 1748 */     if (cipherSuite.contains("WITH_AES_128_"))
/* 1749 */       return 128;
/* 1750 */     if (cipherSuite.contains("WITH_RC4_40_"))
/* 1751 */       return 40;
/* 1752 */     if (cipherSuite.contains("WITH_3DES_EDE_CBC_"))
/* 1753 */       return 168;
/* 1754 */     if (cipherSuite.contains("WITH_IDEA_CBC_"))
/* 1755 */       return 128;
/* 1756 */     if (cipherSuite.contains("WITH_RC2_CBC_40_"))
/* 1757 */       return 40;
/* 1758 */     if (cipherSuite.contains("WITH_DES40_CBC_"))
/* 1759 */       return 40;
/* 1760 */     if (cipherSuite.contains("WITH_DES_CBC_")) {
/* 1761 */       return 56;
/*      */     }
/* 1763 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1769 */     return String.format("%s@%x[provider=%s,keyStore=%s,trustStore=%s]", new Object[] {
/* 1770 */       getClass().getSimpleName(), 
/* 1771 */       Integer.valueOf(hashCode()), this._sslProvider, this._keyStoreResource, this._trustStoreResource });
/*      */   }
/*      */   
/*      */ 
/*      */   class Factory
/*      */   {
/*      */     private final KeyStore _keyStore;
/*      */     
/*      */     private final KeyStore _trustStore;
/*      */     
/*      */     private final SSLContext _context;
/*      */     
/*      */ 
/*      */     Factory(KeyStore keyStore, KeyStore trustStore, SSLContext context)
/*      */     {
/* 1786 */       this._keyStore = keyStore;
/* 1787 */       this._trustStore = trustStore;
/* 1788 */       this._context = context;
/*      */     }
/*      */   }
/*      */   
/*      */   class AliasSNIMatcher extends SNIMatcher
/*      */   {
/*      */     private String _host;
/*      */     private X509 _x509;
/*      */     
/*      */     AliasSNIMatcher()
/*      */     {
/* 1799 */       super();
/*      */     }
/*      */     
/*      */ 
/*      */     public boolean matches(SNIServerName serverName)
/*      */     {
/* 1805 */       if (SslContextFactory.LOG.isDebugEnabled()) {
/* 1806 */         SslContextFactory.LOG.debug("SNI matching for {}", new Object[] { serverName });
/*      */       }
/* 1808 */       if ((serverName instanceof SNIHostName))
/*      */       {
/* 1810 */         String host = this._host = ((SNIHostName)serverName).getAsciiName();
/* 1811 */         host = StringUtil.asciiToLowerCase(host);
/*      */         
/*      */ 
/* 1814 */         this._x509 = ((X509)SslContextFactory.this._certHosts.get(host));
/*      */         
/*      */ 
/* 1817 */         if (this._x509 == null)
/*      */         {
/* 1819 */           this._x509 = ((X509)SslContextFactory.this._certWilds.get(host));
/*      */           
/*      */ 
/* 1822 */           if (this._x509 == null)
/*      */           {
/* 1824 */             int dot = host.indexOf('.');
/* 1825 */             if (dot >= 0)
/*      */             {
/* 1827 */               String domain = host.substring(dot + 1);
/* 1828 */               this._x509 = ((X509)SslContextFactory.this._certWilds.get(domain));
/*      */             }
/*      */           }
/*      */         }
/*      */         
/* 1833 */         if (SslContextFactory.LOG.isDebugEnabled()) {
/* 1834 */           SslContextFactory.LOG.debug("SNI matched {}->{}", new Object[] { host, this._x509 });
/*      */         }
/*      */         
/*      */       }
/* 1838 */       else if (SslContextFactory.LOG.isDebugEnabled()) {
/* 1839 */         SslContextFactory.LOG.debug("SNI no match for {}", new Object[] { serverName });
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1845 */       return true;
/*      */     }
/*      */     
/*      */     public String getHost()
/*      */     {
/* 1850 */       return this._host;
/*      */     }
/*      */     
/*      */     public X509 getX509()
/*      */     {
/* 1855 */       return this._x509;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\ssl\SslContextFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */