/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import java.security.Principal;
/*     */ import java.security.cert.Certificate;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.ExtendedSSLSession;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSessionContext;
/*     */ import javax.security.cert.X509Certificate;
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
/*     */ abstract class ExtendedOpenSslSession
/*     */   extends ExtendedSSLSession
/*     */   implements OpenSslSession
/*     */ {
/*  39 */   private static final String[] LOCAL_SUPPORTED_SIGNATURE_ALGORITHMS = { "SHA512withRSA", "SHA512withECDSA", "SHA384withRSA", "SHA384withECDSA", "SHA256withRSA", "SHA256withECDSA", "SHA224withRSA", "SHA224withECDSA", "SHA1withRSA", "SHA1withECDSA" };
/*     */   
/*     */ 
/*     */   private final OpenSslSession wrapped;
/*     */   
/*     */ 
/*     */   ExtendedOpenSslSession(OpenSslSession wrapped)
/*     */   {
/*  47 */     assert (!(wrapped instanceof ExtendedSSLSession));
/*  48 */     this.wrapped = wrapped;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract List getRequestedServerNames();
/*     */   
/*     */ 
/*     */ 
/*     */   public List<byte[]> getStatusResponses()
/*     */   {
/*  59 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public void handshakeFinished() throws SSLException
/*     */   {
/*  64 */     this.wrapped.handshakeFinished();
/*     */   }
/*     */   
/*     */   public void tryExpandApplicationBufferSize(int packetLengthDataOnly)
/*     */   {
/*  69 */     this.wrapped.tryExpandApplicationBufferSize(packetLengthDataOnly);
/*     */   }
/*     */   
/*     */   public String[] getLocalSupportedSignatureAlgorithms()
/*     */   {
/*  74 */     return (String[])LOCAL_SUPPORTED_SIGNATURE_ALGORITHMS.clone();
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] getPeerSupportedSignatureAlgorithms()
/*     */   {
/*  80 */     return EmptyArrays.EMPTY_STRINGS;
/*     */   }
/*     */   
/*     */   public byte[] getId()
/*     */   {
/*  85 */     return this.wrapped.getId();
/*     */   }
/*     */   
/*     */   public SSLSessionContext getSessionContext()
/*     */   {
/*  90 */     return this.wrapped.getSessionContext();
/*     */   }
/*     */   
/*     */   public long getCreationTime()
/*     */   {
/*  95 */     return this.wrapped.getCreationTime();
/*     */   }
/*     */   
/*     */   public long getLastAccessedTime()
/*     */   {
/* 100 */     return this.wrapped.getLastAccessedTime();
/*     */   }
/*     */   
/*     */   public void invalidate()
/*     */   {
/* 105 */     this.wrapped.invalidate();
/*     */   }
/*     */   
/*     */   public boolean isValid()
/*     */   {
/* 110 */     return this.wrapped.isValid();
/*     */   }
/*     */   
/*     */   public void putValue(String s, Object o)
/*     */   {
/* 115 */     this.wrapped.putValue(s, o);
/*     */   }
/*     */   
/*     */   public Object getValue(String s)
/*     */   {
/* 120 */     return this.wrapped.getValue(s);
/*     */   }
/*     */   
/*     */   public void removeValue(String s)
/*     */   {
/* 125 */     this.wrapped.removeValue(s);
/*     */   }
/*     */   
/*     */   public String[] getValueNames()
/*     */   {
/* 130 */     return this.wrapped.getValueNames();
/*     */   }
/*     */   
/*     */   public Certificate[] getPeerCertificates() throws SSLPeerUnverifiedException
/*     */   {
/* 135 */     return this.wrapped.getPeerCertificates();
/*     */   }
/*     */   
/*     */   public Certificate[] getLocalCertificates()
/*     */   {
/* 140 */     return this.wrapped.getLocalCertificates();
/*     */   }
/*     */   
/*     */   public X509Certificate[] getPeerCertificateChain() throws SSLPeerUnverifiedException
/*     */   {
/* 145 */     return this.wrapped.getPeerCertificateChain();
/*     */   }
/*     */   
/*     */   public Principal getPeerPrincipal() throws SSLPeerUnverifiedException
/*     */   {
/* 150 */     return this.wrapped.getPeerPrincipal();
/*     */   }
/*     */   
/*     */   public Principal getLocalPrincipal()
/*     */   {
/* 155 */     return this.wrapped.getLocalPrincipal();
/*     */   }
/*     */   
/*     */   public String getCipherSuite()
/*     */   {
/* 160 */     return this.wrapped.getCipherSuite();
/*     */   }
/*     */   
/*     */   public String getProtocol()
/*     */   {
/* 165 */     return this.wrapped.getProtocol();
/*     */   }
/*     */   
/*     */   public String getPeerHost()
/*     */   {
/* 170 */     return this.wrapped.getPeerHost();
/*     */   }
/*     */   
/*     */   public int getPeerPort()
/*     */   {
/* 175 */     return this.wrapped.getPeerPort();
/*     */   }
/*     */   
/*     */   public int getPacketBufferSize()
/*     */   {
/* 180 */     return this.wrapped.getPacketBufferSize();
/*     */   }
/*     */   
/*     */   public int getApplicationBufferSize()
/*     */   {
/* 185 */     return this.wrapped.getApplicationBufferSize();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\ExtendedOpenSslSession.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */