/*     */ package org.eclipse.jetty.websocket.common;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import org.eclipse.jetty.websocket.api.UpgradeResponse;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UpgradeResponseAdapter
/*     */   implements UpgradeResponse
/*     */ {
/*     */   public static final String SEC_WEBSOCKET_PROTOCOL = "Sec-WebSocket-Protocol";
/*     */   private int statusCode;
/*     */   private String statusReason;
/*  39 */   private Map<String, List<String>> headers = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*  40 */   private List<ExtensionConfig> extensions = new ArrayList();
/*  41 */   private boolean success = false;
/*     */   
/*     */ 
/*     */   public void addHeader(String name, String value)
/*     */   {
/*  46 */     String key = name;
/*  47 */     List<String> values = (List)this.headers.get(key);
/*  48 */     if (values == null)
/*     */     {
/*  50 */       values = new ArrayList();
/*     */     }
/*  52 */     values.add(value);
/*  53 */     this.headers.put(key, values);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAcceptedSubProtocol()
/*     */   {
/*  64 */     return getHeader("Sec-WebSocket-Protocol");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ExtensionConfig> getExtensions()
/*     */   {
/*  75 */     return this.extensions;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getHeader(String name)
/*     */   {
/*  81 */     List<String> values = getHeaders(name);
/*     */     
/*  83 */     if (values == null)
/*     */     {
/*  85 */       return null;
/*     */     }
/*  87 */     int size = values.size();
/*     */     
/*  89 */     if (size <= 0)
/*     */     {
/*  91 */       return null;
/*     */     }
/*     */     
/*  94 */     if (size == 1)
/*     */     {
/*  96 */       return (String)values.get(0);
/*     */     }
/*     */     
/*  99 */     boolean needsDelim = false;
/* 100 */     StringBuilder ret = new StringBuilder();
/* 101 */     for (String value : values)
/*     */     {
/* 103 */       if (needsDelim)
/*     */       {
/* 105 */         ret.append(", ");
/*     */       }
/* 107 */       QuoteUtil.quoteIfNeeded(ret, value, "\"'\\\n\r\t\f\b%+ ;=");
/* 108 */       needsDelim = true;
/*     */     }
/* 110 */     return ret.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<String> getHeaderNames()
/*     */   {
/* 116 */     return this.headers.keySet();
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, List<String>> getHeaders()
/*     */   {
/* 122 */     return this.headers;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<String> getHeaders(String name)
/*     */   {
/* 128 */     return (List)this.headers.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */   public int getStatusCode()
/*     */   {
/* 134 */     return this.statusCode;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getStatusReason()
/*     */   {
/* 140 */     return this.statusReason;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSuccess()
/*     */   {
/* 146 */     return this.success;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendForbidden(String message)
/*     */     throws IOException
/*     */   {
/* 165 */     throw new UnsupportedOperationException("Not supported");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAcceptedSubProtocol(String protocol)
/*     */   {
/* 177 */     setHeader("Sec-WebSocket-Protocol", protocol);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExtensions(List<ExtensionConfig> extensions)
/*     */   {
/* 196 */     this.extensions.clear();
/* 197 */     if (extensions != null)
/*     */     {
/* 199 */       this.extensions.addAll(extensions);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setHeader(String name, String value)
/*     */   {
/* 206 */     List<String> values = new ArrayList();
/* 207 */     values.add(value);
/* 208 */     this.headers.put(name, values);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setStatusCode(int statusCode)
/*     */   {
/* 214 */     this.statusCode = statusCode;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setStatusReason(String statusReason)
/*     */   {
/* 220 */     this.statusReason = statusReason;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setSuccess(boolean success)
/*     */   {
/* 226 */     this.success = success;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\UpgradeResponseAdapter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */