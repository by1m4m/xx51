/*     */ package org.eclipse.jetty.websocket.client;
/*     */ 
/*     */ import java.net.CookieStore;
/*     */ import java.net.URI;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import org.eclipse.jetty.http.HttpField;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpVersion;
/*     */ import org.eclipse.jetty.util.B64Code;
/*     */ import org.eclipse.jetty.util.MultiMap;
/*     */ import org.eclipse.jetty.util.StringUtil;
/*     */ import org.eclipse.jetty.util.UrlEncoded;
/*     */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
/*     */ import org.eclipse.jetty.websocket.common.UpgradeRequestAdapter;
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
/*     */ public class ClientUpgradeRequest
/*     */   extends UpgradeRequestAdapter
/*     */ {
/*  53 */   private static final Set<String> FORBIDDEN_HEADERS = new TreeSet(String.CASE_INSENSITIVE_ORDER);
/*     */   
/*  55 */   static { FORBIDDEN_HEADERS.add("cookie");
/*     */     
/*  57 */     FORBIDDEN_HEADERS.add("upgrade");
/*  58 */     FORBIDDEN_HEADERS.add("host");
/*  59 */     FORBIDDEN_HEADERS.add("connection");
/*  60 */     FORBIDDEN_HEADERS.add("sec-websocket-key");
/*  61 */     FORBIDDEN_HEADERS.add("sec-websocket-extensions");
/*  62 */     FORBIDDEN_HEADERS.add("sec-websocket-accept");
/*  63 */     FORBIDDEN_HEADERS.add("sec-websocket-protocol");
/*  64 */     FORBIDDEN_HEADERS.add("sec-websocket-version");
/*  65 */     FORBIDDEN_HEADERS.add("pragma");
/*  66 */     FORBIDDEN_HEADERS.add("cache-control");
/*     */   }
/*     */   
/*     */ 
/*     */   private final String key;
/*     */   
/*     */   private Object localEndpoint;
/*     */   public ClientUpgradeRequest()
/*     */   {
/*  75 */     this.key = genRandomKey();
/*     */   }
/*     */   
/*     */   protected ClientUpgradeRequest(URI requestURI)
/*     */   {
/*  80 */     super(requestURI);
/*  81 */     this.key = genRandomKey();
/*     */   }
/*     */   
/*     */   public ClientUpgradeRequest(WebSocketUpgradeRequest wsRequest)
/*     */   {
/*  86 */     this(wsRequest.getURI());
/*     */     
/*  88 */     setCookies(wsRequest.getCookies());
/*     */     
/*  90 */     Map<String, List<String>> headers = new HashMap();
/*  91 */     HttpFields fields = wsRequest.getHeaders();
/*  92 */     for (HttpField field : fields)
/*     */     {
/*  94 */       String key = field.getName();
/*  95 */       List<String> values = (List)headers.get(key);
/*  96 */       if (values == null)
/*     */       {
/*  98 */         values = new ArrayList();
/*     */       }
/* 100 */       values.addAll(Arrays.asList(field.getValues()));
/* 101 */       headers.put(key, values);
/*     */       
/* 103 */       if (key.equalsIgnoreCase("Sec-WebSocket-Protocol"))
/*     */       {
/* 105 */         for (String subProtocol : field.getValue().split(","))
/*     */         {
/* 107 */           setSubProtocols(new String[] { subProtocol });
/*     */         }
/*     */       }
/*     */       
/* 111 */       if (key.equalsIgnoreCase("Sec-WebSocket-Extensions"))
/*     */       {
/* 113 */         for (??? = ExtensionConfig.parseList(field.getValues()).iterator(); ((Iterator)???).hasNext();) { ExtensionConfig ext = (ExtensionConfig)((Iterator)???).next();
/*     */           
/* 115 */           addExtensions(new ExtensionConfig[] { ext });
/*     */         }
/*     */       }
/*     */     }
/* 119 */     super.setHeaders(headers);
/*     */     
/* 121 */     setHttpVersion(wsRequest.getVersion().toString());
/* 122 */     setMethod(wsRequest.getMethod());
/*     */   }
/*     */   
/*     */   private final String genRandomKey()
/*     */   {
/* 127 */     byte[] bytes = new byte[16];
/* 128 */     ThreadLocalRandom.current().nextBytes(bytes);
/* 129 */     return new String(B64Code.encode(bytes));
/*     */   }
/*     */   
/*     */   public String getKey()
/*     */   {
/* 134 */     return this.key;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void setCookiesFrom(CookieStore cookieStore)
/*     */   {
/* 144 */     throw new UnsupportedOperationException("Request specific CookieStore no longer supported");
/*     */   }
/*     */   
/*     */ 
/*     */   public void setRequestURI(URI uri)
/*     */   {
/* 150 */     super.setRequestURI(uri);
/*     */     
/*     */ 
/* 153 */     Map<String, List<String>> pmap = new HashMap();
/*     */     
/* 155 */     String query = uri.getQuery();
/*     */     
/* 157 */     if (StringUtil.isNotBlank(query))
/*     */     {
/* 159 */       MultiMap<String> params = new MultiMap();
/* 160 */       UrlEncoded.decodeTo(uri.getQuery(), params, StandardCharsets.UTF_8);
/*     */       
/* 162 */       for (String key : params.keySet())
/*     */       {
/* 164 */         List<String> values = params.getValues(key);
/* 165 */         if (values == null)
/*     */         {
/* 167 */           pmap.put(key, new ArrayList());
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 172 */           List<String> copy = new ArrayList();
/* 173 */           copy.addAll(values);
/* 174 */           pmap.put(key, copy);
/*     */         }
/*     */       }
/*     */       
/* 178 */       super.setParameterMap(pmap);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setLocalEndpoint(Object websocket)
/*     */   {
/* 184 */     this.localEndpoint = websocket;
/*     */   }
/*     */   
/*     */   public Object getLocalEndpoint()
/*     */   {
/* 189 */     return this.localEndpoint;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\ClientUpgradeRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */