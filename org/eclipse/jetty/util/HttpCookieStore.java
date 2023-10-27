/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.net.CookieManager;
/*     */ import java.net.CookieStore;
/*     */ import java.net.HttpCookie;
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class HttpCookieStore
/*     */   implements CookieStore
/*     */ {
/*     */   private final CookieStore delegate;
/*     */   
/*     */   public HttpCookieStore()
/*     */   {
/*  39 */     this.delegate = new CookieManager().getCookieStore();
/*     */   }
/*     */   
/*     */ 
/*     */   public void add(URI uri, HttpCookie cookie)
/*     */   {
/*  45 */     this.delegate.add(uri, cookie);
/*     */   }
/*     */   
/*     */ 
/*     */   public List<HttpCookie> get(URI uri)
/*     */   {
/*  51 */     return this.delegate.get(uri);
/*     */   }
/*     */   
/*     */ 
/*     */   public List<HttpCookie> getCookies()
/*     */   {
/*  57 */     return this.delegate.getCookies();
/*     */   }
/*     */   
/*     */ 
/*     */   public List<URI> getURIs()
/*     */   {
/*  63 */     return this.delegate.getURIs();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean remove(URI uri, HttpCookie cookie)
/*     */   {
/*  69 */     return this.delegate.remove(uri, cookie);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean removeAll()
/*     */   {
/*  75 */     return this.delegate.removeAll();
/*     */   }
/*     */   
/*     */   public static List<HttpCookie> matchPath(URI uri, List<HttpCookie> cookies)
/*     */   {
/*  80 */     if ((cookies == null) || (cookies.isEmpty()))
/*  81 */       return Collections.emptyList();
/*  82 */     List<HttpCookie> result = new ArrayList(4);
/*  83 */     String path = uri.getPath();
/*  84 */     if ((path == null) || (path.trim().isEmpty()))
/*  85 */       path = "/";
/*  86 */     for (HttpCookie cookie : cookies)
/*     */     {
/*  88 */       String cookiePath = cookie.getPath();
/*  89 */       if (cookiePath == null)
/*     */       {
/*  91 */         result.add(cookie);
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*  96 */       else if (path.equals(cookiePath))
/*     */       {
/*  98 */         result.add(cookie);
/*     */       }
/* 100 */       else if (path.startsWith(cookiePath))
/*     */       {
/* 102 */         if ((cookiePath.endsWith("/")) || (path.charAt(cookiePath.length()) == '/')) {
/* 103 */           result.add(cookie);
/*     */         }
/*     */       }
/*     */     }
/* 107 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class Empty
/*     */     implements CookieStore
/*     */   {
/*     */     public void add(URI uri, HttpCookie cookie) {}
/*     */     
/*     */ 
/*     */     public List<HttpCookie> get(URI uri)
/*     */     {
/* 120 */       return Collections.emptyList();
/*     */     }
/*     */     
/*     */ 
/*     */     public List<HttpCookie> getCookies()
/*     */     {
/* 126 */       return Collections.emptyList();
/*     */     }
/*     */     
/*     */ 
/*     */     public List<URI> getURIs()
/*     */     {
/* 132 */       return Collections.emptyList();
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean remove(URI uri, HttpCookie cookie)
/*     */     {
/* 138 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean removeAll()
/*     */     {
/* 144 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\HttpCookieStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */