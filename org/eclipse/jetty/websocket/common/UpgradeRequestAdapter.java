/*     */ package org.eclipse.jetty.websocket.common;
/*     */ 
/*     */ import java.net.HttpCookie;
/*     */ import java.net.URI;
/*     */ import java.security.Principal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeMap;
/*     */ import org.eclipse.jetty.websocket.api.UpgradeRequest;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
/*     */ import org.eclipse.jetty.websocket.api.util.QuoteUtil;
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
/*     */ public class UpgradeRequestAdapter
/*     */   implements UpgradeRequest
/*     */ {
/*     */   private URI requestURI;
/*  38 */   private List<String> subProtocols = new ArrayList(1);
/*  39 */   private List<ExtensionConfig> extensions = new ArrayList(1);
/*  40 */   private List<HttpCookie> cookies = new ArrayList(1);
/*  41 */   private Map<String, List<String>> headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*  42 */   private Map<String, List<String>> parameters = new HashMap(1);
/*     */   
/*     */   private Object session;
/*     */   
/*     */   private String httpVersion;
/*     */   
/*     */   private String method;
/*     */   private String host;
/*     */   private boolean secure;
/*     */   
/*     */   protected UpgradeRequestAdapter() {}
/*     */   
/*     */   public UpgradeRequestAdapter(String requestURI)
/*     */   {
/*  56 */     this(URI.create(requestURI));
/*     */   }
/*     */   
/*     */   public UpgradeRequestAdapter(URI requestURI)
/*     */   {
/*  61 */     setRequestURI(requestURI);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addExtensions(ExtensionConfig... configs)
/*     */   {
/*  67 */     Collections.addAll(this.extensions, configs);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addExtensions(String... configs)
/*     */   {
/*  73 */     for (String config : configs)
/*     */     {
/*  75 */       this.extensions.add(ExtensionConfig.parse(config));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void clearHeaders()
/*     */   {
/*  82 */     this.headers.clear();
/*     */   }
/*     */   
/*     */ 
/*     */   public List<HttpCookie> getCookies()
/*     */   {
/*  88 */     return this.cookies;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<ExtensionConfig> getExtensions()
/*     */   {
/*  94 */     return this.extensions;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getHeader(String name)
/*     */   {
/* 100 */     List<String> values = (List)this.headers.get(name);
/*     */     
/* 102 */     if (values == null)
/*     */     {
/* 104 */       return null;
/*     */     }
/* 106 */     int size = values.size();
/*     */     
/* 108 */     if (size <= 0)
/*     */     {
/* 110 */       return null;
/*     */     }
/*     */     
/* 113 */     if (size == 1)
/*     */     {
/* 115 */       return (String)values.get(0);
/*     */     }
/*     */     
/* 118 */     boolean needsDelim = false;
/* 119 */     StringBuilder ret = new StringBuilder();
/* 120 */     for (String value : values)
/*     */     {
/* 122 */       if (needsDelim)
/*     */       {
/* 124 */         ret.append(", ");
/*     */       }
/* 126 */       QuoteUtil.quoteIfNeeded(ret, value, "\"'\\\n\r\t\f\b%+ ;=");
/* 127 */       needsDelim = true;
/*     */     }
/* 129 */     return ret.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getHeaderInt(String name)
/*     */   {
/* 135 */     List<String> values = (List)this.headers.get(name);
/*     */     
/* 137 */     if (values == null)
/*     */     {
/* 139 */       return -1;
/*     */     }
/* 141 */     int size = values.size();
/*     */     
/* 143 */     if (size <= 0)
/*     */     {
/* 145 */       return -1;
/*     */     }
/*     */     
/* 148 */     if (size == 1)
/*     */     {
/* 150 */       return Integer.parseInt((String)values.get(0));
/*     */     }
/* 152 */     throw new NumberFormatException("Cannot convert multi-value header into int");
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, List<String>> getHeaders()
/*     */   {
/* 158 */     return this.headers;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> getHeaders(String name)
/*     */   {
/* 164 */     return (List)this.headers.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 170 */     return this.host;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getHttpVersion()
/*     */   {
/* 176 */     return this.httpVersion;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getMethod()
/*     */   {
/* 182 */     return this.method;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getOrigin()
/*     */   {
/* 188 */     return getHeader("Origin");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, List<String>> getParameterMap()
/*     */   {
/* 199 */     return Collections.unmodifiableMap(this.parameters);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getProtocolVersion()
/*     */   {
/* 205 */     String version = getHeader("Sec-WebSocket-Version");
/* 206 */     if (version == null)
/*     */     {
/* 208 */       return "13";
/*     */     }
/* 210 */     return version;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getQueryString()
/*     */   {
/* 216 */     return this.requestURI.getQuery();
/*     */   }
/*     */   
/*     */ 
/*     */   public URI getRequestURI()
/*     */   {
/* 222 */     return this.requestURI;
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
/*     */   public Object getSession()
/*     */   {
/* 235 */     return this.session;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> getSubProtocols()
/*     */   {
/* 241 */     return this.subProtocols;
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
/*     */   public Principal getUserPrincipal()
/*     */   {
/* 255 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasSubProtocol(String test)
/*     */   {
/* 261 */     for (String protocol : this.subProtocols)
/*     */     {
/* 263 */       if (protocol.equalsIgnoreCase(test))
/*     */       {
/* 265 */         return true;
/*     */       }
/*     */     }
/* 268 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isOrigin(String test)
/*     */   {
/* 274 */     return test.equalsIgnoreCase(getOrigin());
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSecure()
/*     */   {
/* 280 */     return this.secure;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setCookies(List<HttpCookie> cookies)
/*     */   {
/* 286 */     this.cookies.clear();
/* 287 */     if ((cookies != null) && (!cookies.isEmpty()))
/*     */     {
/* 289 */       this.cookies.addAll(cookies);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setExtensions(List<ExtensionConfig> configs)
/*     */   {
/* 296 */     this.extensions.clear();
/* 297 */     if (configs != null)
/*     */     {
/* 299 */       this.extensions.addAll(configs);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setHeader(String name, List<String> values)
/*     */   {
/* 306 */     this.headers.put(name, values);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setHeader(String name, String value)
/*     */   {
/* 312 */     List<String> values = new ArrayList();
/* 313 */     values.add(value);
/* 314 */     setHeader(name, values);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setHeaders(Map<String, List<String>> headers)
/*     */   {
/* 320 */     clearHeaders();
/*     */     
/* 322 */     for (Map.Entry<String, List<String>> entry : headers.entrySet())
/*     */     {
/* 324 */       String name = (String)entry.getKey();
/* 325 */       List<String> values = (List)entry.getValue();
/* 326 */       setHeader(name, values);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setHttpVersion(String httpVersion)
/*     */   {
/* 333 */     this.httpVersion = httpVersion;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setMethod(String method)
/*     */   {
/* 339 */     this.method = method;
/*     */   }
/*     */   
/*     */   protected void setParameterMap(Map<String, List<String>> parameters)
/*     */   {
/* 344 */     this.parameters.clear();
/* 345 */     this.parameters.putAll(parameters);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setRequestURI(URI uri)
/*     */   {
/* 351 */     this.requestURI = uri;
/* 352 */     String scheme = uri.getScheme();
/* 353 */     if ("ws".equalsIgnoreCase(scheme))
/*     */     {
/* 355 */       this.secure = false;
/*     */     }
/* 357 */     else if ("wss".equalsIgnoreCase(scheme))
/*     */     {
/* 359 */       this.secure = true;
/*     */     }
/*     */     else
/*     */     {
/* 363 */       throw new IllegalArgumentException("URI scheme must be 'ws' or 'wss'");
/*     */     }
/* 365 */     this.host = this.requestURI.getHost();
/* 366 */     this.parameters.clear();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setSession(Object session)
/*     */   {
/* 372 */     this.session = session;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setSubProtocols(List<String> subProtocols)
/*     */   {
/* 378 */     this.subProtocols.clear();
/* 379 */     if (subProtocols != null)
/*     */     {
/* 381 */       this.subProtocols.addAll(subProtocols);
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
/*     */   public void setSubProtocols(String... protocols)
/*     */   {
/* 394 */     this.subProtocols.clear();
/* 395 */     Collections.addAll(this.subProtocols, protocols);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\UpgradeRequestAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */