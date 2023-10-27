/*     */ package org.eclipse.jetty.websocket.api.util;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.Objects;
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
/*     */ 
/*     */ public final class WSURI
/*     */ {
/*     */   public static URI toHttp(URI inputUri)
/*     */     throws URISyntaxException
/*     */   {
/*  43 */     Objects.requireNonNull(inputUri, "Input URI must not be null");
/*  44 */     String wsScheme = inputUri.getScheme();
/*  45 */     if (("http".equalsIgnoreCase(wsScheme)) || ("https".equalsIgnoreCase(wsScheme)))
/*     */     {
/*     */ 
/*  48 */       return inputUri;
/*     */     }
/*     */     
/*  51 */     if ("ws".equalsIgnoreCase(wsScheme))
/*     */     {
/*     */ 
/*  54 */       return new URI("http" + inputUri.toString().substring(wsScheme.length()));
/*     */     }
/*     */     
/*  57 */     if ("wss".equalsIgnoreCase(wsScheme))
/*     */     {
/*     */ 
/*  60 */       return new URI("https" + inputUri.toString().substring(wsScheme.length()));
/*     */     }
/*     */     
/*  63 */     throw new URISyntaxException(inputUri.toString(), "Unrecognized WebSocket scheme");
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
/*     */   public static URI toWebsocket(CharSequence inputUrl)
/*     */     throws URISyntaxException
/*     */   {
/*  79 */     return toWebsocket(new URI(inputUrl.toString()));
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
/*     */   public static URI toWebsocket(CharSequence inputUrl, String query)
/*     */     throws URISyntaxException
/*     */   {
/*  97 */     if (query == null)
/*     */     {
/*  99 */       return toWebsocket(new URI(inputUrl.toString()));
/*     */     }
/* 101 */     return toWebsocket(new URI(inputUrl.toString() + '?' + query));
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
/*     */   public static URI toWebsocket(URI inputUri)
/*     */     throws URISyntaxException
/*     */   {
/* 118 */     Objects.requireNonNull(inputUri, "Input URI must not be null");
/* 119 */     String httpScheme = inputUri.getScheme();
/* 120 */     if (("ws".equalsIgnoreCase(httpScheme)) || ("wss".equalsIgnoreCase(httpScheme)))
/*     */     {
/*     */ 
/* 123 */       return inputUri;
/*     */     }
/*     */     
/* 126 */     if ("http".equalsIgnoreCase(httpScheme))
/*     */     {
/*     */ 
/* 129 */       return new URI("ws" + inputUri.toString().substring(httpScheme.length()));
/*     */     }
/*     */     
/* 132 */     if ("https".equalsIgnoreCase(httpScheme))
/*     */     {
/*     */ 
/* 135 */       return new URI("wss" + inputUri.toString().substring(httpScheme.length()));
/*     */     }
/*     */     
/* 138 */     throw new URISyntaxException(inputUri.toString(), "Unrecognized HTTP scheme");
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\util\WSURI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */