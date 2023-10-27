/*     */ package org.eclipse.jetty.util.ssl;
/*     */ 
/*     */ import java.net.Socket;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import javax.net.ssl.SNIMatcher;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.X509ExtendedKeyManager;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
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
/*     */ public class SniX509ExtendedKeyManager
/*     */   extends X509ExtendedKeyManager
/*     */ {
/*     */   public static final String SNI_X509 = "org.eclipse.jetty.util.ssl.snix509";
/*     */   private static final String NO_MATCHERS = "no_matchers";
/*  46 */   private static final Logger LOG = Log.getLogger(SniX509ExtendedKeyManager.class);
/*     */   
/*     */   private final X509ExtendedKeyManager _delegate;
/*     */   
/*     */   public SniX509ExtendedKeyManager(X509ExtendedKeyManager keyManager)
/*     */   {
/*  52 */     this._delegate = keyManager;
/*     */   }
/*     */   
/*     */ 
/*     */   public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket)
/*     */   {
/*  58 */     return this._delegate.chooseClientAlias(keyType, issuers, socket);
/*     */   }
/*     */   
/*     */ 
/*     */   public String chooseEngineClientAlias(String[] keyType, Principal[] issuers, SSLEngine engine)
/*     */   {
/*  64 */     return this._delegate.chooseEngineClientAlias(keyType, issuers, engine);
/*     */   }
/*     */   
/*     */ 
/*     */   protected String chooseServerAlias(String keyType, Principal[] issuers, Collection<SNIMatcher> matchers, SSLSession session)
/*     */   {
/*  70 */     String[] aliases = this._delegate.getServerAliases(keyType, issuers);
/*  71 */     if ((aliases == null) || (aliases.length == 0)) {
/*  72 */       return null;
/*     */     }
/*     */     
/*  75 */     String host = null;
/*  76 */     X509 x509 = null;
/*  77 */     Object localObject; if (matchers != null)
/*     */     {
/*  79 */       for (localObject = matchers.iterator(); ((Iterator)localObject).hasNext();) { m = (SNIMatcher)((Iterator)localObject).next();
/*     */         
/*  81 */         if ((m instanceof SslContextFactory.AliasSNIMatcher))
/*     */         {
/*  83 */           matcher = (SslContextFactory.AliasSNIMatcher)m;
/*  84 */           host = matcher.getHost();
/*  85 */           x509 = matcher.getX509();
/*  86 */           break;
/*     */         }
/*     */       } }
/*     */     SNIMatcher m;
/*     */     SslContextFactory.AliasSNIMatcher matcher;
/*  91 */     if (LOG.isDebugEnabled()) {
/*  92 */       LOG.debug("Matched {} with {} from {}", new Object[] { host, x509, Arrays.asList(aliases) });
/*     */     }
/*     */     
/*  95 */     if (x509 != null)
/*     */     {
/*  97 */       localObject = aliases;m = localObject.length; for (matcher = 0; matcher < m; matcher++) { String a = localObject[matcher];
/*     */         
/*  99 */         if (a.equals(x509.getAlias()))
/*     */         {
/* 101 */           session.putValue("org.eclipse.jetty.util.ssl.snix509", x509);
/* 102 */           return a;
/*     */         }
/*     */       }
/* 105 */       return null;
/*     */     }
/* 107 */     return "no_matchers";
/*     */   }
/*     */   
/*     */ 
/*     */   public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket)
/*     */   {
/* 113 */     SSLSocket sslSocket = (SSLSocket)socket;
/* 114 */     String alias = socket == null ? "no_matchers" : chooseServerAlias(keyType, issuers, sslSocket.getSSLParameters().getSNIMatchers(), sslSocket.getHandshakeSession());
/* 115 */     if (alias == "no_matchers")
/* 116 */       alias = this._delegate.chooseServerAlias(keyType, issuers, socket);
/* 117 */     if (LOG.isDebugEnabled())
/* 118 */       LOG.debug("Chose alias {}/{} on {}", new Object[] { alias, keyType, socket });
/* 119 */     return alias;
/*     */   }
/*     */   
/*     */ 
/*     */   public String chooseEngineServerAlias(String keyType, Principal[] issuers, SSLEngine engine)
/*     */   {
/* 125 */     String alias = engine == null ? "no_matchers" : chooseServerAlias(keyType, issuers, engine.getSSLParameters().getSNIMatchers(), engine.getHandshakeSession());
/* 126 */     if (alias == "no_matchers")
/* 127 */       alias = this._delegate.chooseEngineServerAlias(keyType, issuers, engine);
/* 128 */     if (LOG.isDebugEnabled())
/* 129 */       LOG.debug("Chose alias {}/{} on {}", new Object[] { alias, keyType, engine });
/* 130 */     return alias;
/*     */   }
/*     */   
/*     */ 
/*     */   public X509Certificate[] getCertificateChain(String alias)
/*     */   {
/* 136 */     return this._delegate.getCertificateChain(alias);
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] getClientAliases(String keyType, Principal[] issuers)
/*     */   {
/* 142 */     return this._delegate.getClientAliases(keyType, issuers);
/*     */   }
/*     */   
/*     */ 
/*     */   public PrivateKey getPrivateKey(String alias)
/*     */   {
/* 148 */     return this._delegate.getPrivateKey(alias);
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] getServerAliases(String keyType, Principal[] issuers)
/*     */   {
/* 154 */     return this._delegate.getServerAliases(keyType, issuers);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\ssl\SniX509ExtendedKeyManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */