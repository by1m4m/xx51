/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.eclipse.jetty.io.ClientConnectionFactory;
/*     */ import org.eclipse.jetty.util.HostPort;
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
/*     */ public class ProxyConfiguration
/*     */ {
/*  43 */   private final List<Proxy> proxies = new ArrayList();
/*     */   
/*     */   public List<Proxy> getProxies()
/*     */   {
/*  47 */     return this.proxies;
/*     */   }
/*     */   
/*     */   public Proxy match(Origin origin)
/*     */   {
/*  52 */     for (Proxy proxy : getProxies())
/*     */     {
/*  54 */       if (proxy.matches(origin))
/*  55 */         return proxy;
/*     */     }
/*  57 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract class Proxy
/*     */   {
/*  63 */     private final Set<String> included = new HashSet();
/*  64 */     private final Set<String> excluded = new HashSet();
/*     */     private final Origin.Address address;
/*     */     private final boolean secure;
/*     */     
/*     */     protected Proxy(Origin.Address address, boolean secure)
/*     */     {
/*  70 */       this.address = address;
/*  71 */       this.secure = secure;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Origin.Address getAddress()
/*     */     {
/*  79 */       return this.address;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean isSecure()
/*     */     {
/*  87 */       return this.secure;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Set<String> getIncludedAddresses()
/*     */     {
/*  97 */       return this.included;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Set<String> getExcludedAddresses()
/*     */     {
/* 107 */       return this.excluded;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public URI getURI()
/*     */     {
/* 115 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean matches(Origin origin)
/*     */     {
/* 127 */       if (getAddress().equals(origin.getAddress())) {
/* 128 */         return false;
/*     */       }
/* 130 */       boolean result = this.included.isEmpty();
/* 131 */       Origin.Address address = origin.getAddress();
/* 132 */       for (String included : this.included)
/*     */       {
/* 134 */         if (matches(address, included))
/*     */         {
/* 136 */           result = true;
/* 137 */           break;
/*     */         }
/*     */       }
/* 140 */       for (String excluded : this.excluded)
/*     */       {
/* 142 */         if (matches(address, excluded))
/*     */         {
/* 144 */           result = false;
/* 145 */           break;
/*     */         }
/*     */       }
/* 148 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */     private boolean matches(Origin.Address address, String pattern)
/*     */     {
/* 154 */       HostPort hostPort = new HostPort(pattern);
/* 155 */       String host = hostPort.getHost();
/* 156 */       int port = hostPort.getPort();
/* 157 */       return (host.equals(address.getHost())) && ((port <= 0) || (port == address.getPort()));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract ClientConnectionFactory newClientConnectionFactory(ClientConnectionFactory paramClientConnectionFactory);
/*     */     
/*     */ 
/*     */ 
/*     */     public String toString()
/*     */     {
/* 169 */       return this.address.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\ProxyConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */