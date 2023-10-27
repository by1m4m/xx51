/*     */ package org.eclipse.jetty.util;
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
/*     */ public class HostPort
/*     */ {
/*  30 */   private static final boolean STRIP_IPV6 = Boolean.parseBoolean(System.getProperty("org.eclipse.jetty.util.HostPort.STRIP_IPV6", "false"));
/*     */   private final String _host;
/*     */   private final int _port;
/*     */   
/*     */   public HostPort(String authority)
/*     */     throws IllegalArgumentException
/*     */   {
/*  37 */     if (authority == null) {
/*  38 */       throw new IllegalArgumentException("No Authority");
/*     */     }
/*     */     try {
/*  41 */       if (authority.isEmpty())
/*     */       {
/*  43 */         this._host = authority;
/*  44 */         this._port = 0;
/*     */       }
/*  46 */       else if (authority.charAt(0) == '[')
/*     */       {
/*     */ 
/*  49 */         int close = authority.lastIndexOf(']');
/*  50 */         if (close < 0)
/*  51 */           throw new IllegalArgumentException("Bad IPv6 host");
/*  52 */         this._host = (STRIP_IPV6 ? authority.substring(1, close) : authority.substring(0, close + 1));
/*     */         
/*  54 */         if (authority.length() > close + 1)
/*     */         {
/*  56 */           if (authority.charAt(close + 1) != ':')
/*  57 */             throw new IllegalArgumentException("Bad IPv6 port");
/*  58 */           this._port = StringUtil.toInt(authority, close + 2);
/*     */         }
/*     */         else {
/*  61 */           this._port = 0;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  66 */         int c = authority.lastIndexOf(':');
/*  67 */         if (c >= 0)
/*     */         {
/*  69 */           this._host = authority.substring(0, c);
/*  70 */           this._port = StringUtil.toInt(authority, c + 1);
/*     */         }
/*     */         else
/*     */         {
/*  74 */           this._host = authority;
/*  75 */           this._port = 0;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (IllegalArgumentException iae)
/*     */     {
/*  81 */       throw iae;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/*  85 */       throw new IllegalArgumentException("Bad HostPort") {};
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  90 */     if (this._host == null)
/*  91 */       throw new IllegalArgumentException("Bad host");
/*  92 */     if (this._port < 0) {
/*  93 */       throw new IllegalArgumentException("Bad port");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 102 */     return this._host;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 111 */     return this._port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPort(int defaultPort)
/*     */   {
/* 121 */     return this._port > 0 ? this._port : defaultPort;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String normalizeHost(String host)
/*     */   {
/* 132 */     if ((host.isEmpty()) || (host.charAt(0) == '[') || (host.indexOf(':') < 0)) {
/* 133 */       return host;
/*     */     }
/*     */     
/* 136 */     return "[" + host + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\HostPort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */