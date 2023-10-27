/*     */ package org.eclipse.jetty.util.ssl;
/*     */ 
/*     */ import java.security.cert.CertificateParsingException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.naming.InvalidNameException;
/*     */ import javax.naming.ldap.LdapName;
/*     */ import javax.naming.ldap.Rdn;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import org.eclipse.jetty.util.StringUtil;
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
/*     */ public class X509
/*     */ {
/*  40 */   private static final Logger LOG = Log.getLogger(X509.class);
/*     */   
/*     */ 
/*     */   private static final int KEY_USAGE__KEY_CERT_SIGN = 5;
/*     */   
/*     */ 
/*     */   private static final int SUBJECT_ALTERNATIVE_NAMES__DNS_NAME = 2;
/*     */   
/*     */   private final X509Certificate _x509;
/*     */   
/*     */   private final String _alias;
/*     */   
/*     */ 
/*     */   public static boolean isCertSign(X509Certificate x509)
/*     */   {
/*  55 */     boolean[] key_usage = x509.getKeyUsage();
/*  56 */     if ((key_usage == null) || (key_usage.length <= 5))
/*     */     {
/*  58 */       return false;
/*     */     }
/*  60 */     return key_usage[5];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  65 */   private final List<String> _hosts = new ArrayList();
/*  66 */   private final List<String> _wilds = new ArrayList();
/*     */   
/*     */   public X509(String alias, X509Certificate x509) throws CertificateParsingException, InvalidNameException
/*     */   {
/*  70 */     this._alias = alias;
/*  71 */     this._x509 = x509;
/*     */     
/*     */ 
/*  74 */     boolean named = false;
/*  75 */     Collection<List<?>> altNames = x509.getSubjectAlternativeNames();
/*  76 */     Iterator localIterator; if (altNames != null)
/*     */     {
/*  78 */       for (localIterator = altNames.iterator(); localIterator.hasNext();) { list = (List)localIterator.next();
/*     */         
/*  80 */         if (((Number)list.get(0)).intValue() == 2)
/*     */         {
/*  82 */           String cn = list.get(1).toString();
/*  83 */           if (LOG.isDebugEnabled())
/*  84 */             LOG.debug("Certificate SAN alias={} CN={} in {}", new Object[] { alias, cn, this });
/*  85 */           if (cn != null)
/*     */           {
/*  87 */             named = true;
/*  88 */             addName(cn);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     List<?> list;
/*  95 */     if (!named)
/*     */     {
/*  97 */       LdapName name = new LdapName(x509.getSubjectX500Principal().getName("RFC2253"));
/*  98 */       for (Rdn rdn : name.getRdns())
/*     */       {
/* 100 */         if (rdn.getType().equalsIgnoreCase("CN"))
/*     */         {
/* 102 */           String cn = rdn.getValue().toString();
/* 103 */           if (LOG.isDebugEnabled())
/* 104 */             LOG.debug("Certificate CN alias={} CN={} in {}", new Object[] { alias, cn, this });
/* 105 */           if ((cn != null) && (cn.contains(".")) && (!cn.contains(" "))) {
/* 106 */             addName(cn);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void addName(String cn) {
/* 114 */     cn = StringUtil.asciiToLowerCase(cn);
/* 115 */     if (cn.startsWith("*.")) {
/* 116 */       this._wilds.add(cn.substring(2));
/*     */     } else {
/* 118 */       this._hosts.add(cn);
/*     */     }
/*     */   }
/*     */   
/*     */   public String getAlias() {
/* 123 */     return this._alias;
/*     */   }
/*     */   
/*     */   public X509Certificate getCertificate()
/*     */   {
/* 128 */     return this._x509;
/*     */   }
/*     */   
/*     */   public Set<String> getHosts()
/*     */   {
/* 133 */     return new HashSet(this._hosts);
/*     */   }
/*     */   
/*     */   public Set<String> getWilds()
/*     */   {
/* 138 */     return new HashSet(this._wilds);
/*     */   }
/*     */   
/*     */   public boolean matches(String host)
/*     */   {
/* 143 */     host = StringUtil.asciiToLowerCase(host);
/* 144 */     if ((this._hosts.contains(host)) || (this._wilds.contains(host))) {
/* 145 */       return true;
/*     */     }
/* 147 */     int dot = host.indexOf('.');
/* 148 */     if (dot >= 0)
/*     */     {
/* 150 */       String domain = host.substring(dot + 1);
/* 151 */       if (this._wilds.contains(domain))
/* 152 */         return true;
/*     */     }
/* 154 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 160 */     return String.format("%s@%x(%s,h=%s,w=%s)", new Object[] {
/* 161 */       getClass().getSimpleName(), 
/* 162 */       Integer.valueOf(hashCode()), this._alias, this._hosts, this._wilds });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\ssl\X509.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */