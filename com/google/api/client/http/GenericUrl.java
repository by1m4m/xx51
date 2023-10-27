/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.GenericData;
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import com.google.api.client.util.escape.CharEscapers;
/*     */ import com.google.api.client.util.escape.Escaper;
/*     */ import com.google.api.client.util.escape.PercentEscaper;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenericUrl
/*     */   extends GenericData
/*     */ {
/*  61 */   private static final Escaper URI_FRAGMENT_ESCAPER = new PercentEscaper("=&-_.!~*'()@:$,;/?:", false);
/*     */   
/*     */ 
/*     */ 
/*     */   private String scheme;
/*     */   
/*     */ 
/*     */   private String host;
/*     */   
/*     */ 
/*     */   private String userInfo;
/*     */   
/*     */ 
/*  74 */   private int port = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<String> pathParts;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String fragment;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericUrl() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericUrl(String encodedUrl)
/*     */   {
/* 114 */     this(parseURL(encodedUrl));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericUrl(URI uri)
/*     */   {
/* 125 */     this(uri.getScheme(), uri.getHost(), uri.getPort(), uri.getRawPath(), uri.getRawFragment(), uri.getRawQuery(), uri.getRawUserInfo());
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
/*     */   public GenericUrl(URL url)
/*     */   {
/* 142 */     this(url.getProtocol(), url.getHost(), url.getPort(), url.getPath(), url.getRef(), url.getQuery(), url.getUserInfo());
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
/*     */   private GenericUrl(String scheme, String host, int port, String path, String fragment, String query, String userInfo)
/*     */   {
/* 158 */     this.scheme = scheme.toLowerCase();
/* 159 */     this.host = host;
/* 160 */     this.port = port;
/* 161 */     this.pathParts = toPathParts(path);
/* 162 */     this.fragment = (fragment != null ? CharEscapers.decodeUri(fragment) : null);
/* 163 */     if (query != null) {
/* 164 */       UrlEncodedParser.parse(query, this);
/*     */     }
/* 166 */     this.userInfo = (userInfo != null ? CharEscapers.decodeUri(userInfo) : null);
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 172 */     return build().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 177 */     if (this == obj) {
/* 178 */       return true;
/*     */     }
/* 180 */     if ((!super.equals(obj)) || (!(obj instanceof GenericUrl))) {
/* 181 */       return false;
/*     */     }
/* 183 */     GenericUrl other = (GenericUrl)obj;
/*     */     
/* 185 */     return build().equals(other.toString());
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 190 */     return build();
/*     */   }
/*     */   
/*     */   public GenericUrl clone()
/*     */   {
/* 195 */     GenericUrl result = (GenericUrl)super.clone();
/* 196 */     if (this.pathParts != null) {
/* 197 */       result.pathParts = new ArrayList(this.pathParts);
/*     */     }
/* 199 */     return result;
/*     */   }
/*     */   
/*     */   public GenericUrl set(String fieldName, Object value)
/*     */   {
/* 204 */     return (GenericUrl)super.set(fieldName, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getScheme()
/*     */   {
/* 213 */     return this.scheme;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setScheme(String scheme)
/*     */   {
/* 222 */     this.scheme = ((String)Preconditions.checkNotNull(scheme));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 231 */     return this.host;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setHost(String host)
/*     */   {
/* 240 */     this.host = ((String)Preconditions.checkNotNull(host));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final String getUserInfo()
/*     */   {
/* 249 */     return this.userInfo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setUserInfo(String userInfo)
/*     */   {
/* 258 */     this.userInfo = userInfo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 267 */     return this.port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setPort(int port)
/*     */   {
/* 276 */     Preconditions.checkArgument(port >= -1, "expected port >= -1");
/* 277 */     this.port = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<String> getPathParts()
/*     */   {
/* 287 */     return this.pathParts;
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
/*     */ 
/*     */   public void setPathParts(List<String> pathParts)
/*     */   {
/* 307 */     this.pathParts = pathParts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFragment()
/*     */   {
/* 316 */     return this.fragment;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setFragment(String fragment)
/*     */   {
/* 325 */     this.fragment = fragment;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public final String build()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 185	com/google/api/client/http/GenericUrl:buildAuthority	()Ljava/lang/String;
/*     */     //   4: invokestatic 189	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   7: aload_0
/*     */     //   8: invokevirtual 192	com/google/api/client/http/GenericUrl:buildRelativeUrl	()Ljava/lang/String;
/*     */     //   11: invokestatic 189	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   14: dup
/*     */     //   15: invokevirtual 195	java/lang/String:length	()I
/*     */     //   18: ifeq +9 -> 27
/*     */     //   21: invokevirtual 198	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   24: goto +12 -> 36
/*     */     //   27: pop
/*     */     //   28: new 93	java/lang/String
/*     */     //   31: dup_x1
/*     */     //   32: swap
/*     */     //   33: invokespecial 200	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   36: areturn
/*     */     // Line number table:
/*     */     //   Java source line #333	-> byte code offset #0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	37	0	this	GenericUrl
/*     */   }
/*     */   
/*     */   public final String buildAuthority()
/*     */   {
/* 349 */     StringBuilder buf = new StringBuilder();
/* 350 */     buf.append((String)Preconditions.checkNotNull(this.scheme));
/* 351 */     buf.append("://");
/* 352 */     if (this.userInfo != null) {
/* 353 */       buf.append(CharEscapers.escapeUriUserInfo(this.userInfo)).append('@');
/*     */     }
/* 355 */     buf.append((String)Preconditions.checkNotNull(this.host));
/* 356 */     int port = this.port;
/* 357 */     if (port != -1) {
/* 358 */       buf.append(':').append(port);
/*     */     }
/* 360 */     return buf.toString();
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
/*     */   public final String buildRelativeUrl()
/*     */   {
/* 375 */     StringBuilder buf = new StringBuilder();
/* 376 */     if (this.pathParts != null) {
/* 377 */       appendRawPathFromParts(buf);
/*     */     }
/* 379 */     addQueryParams(entrySet(), buf);
/*     */     
/*     */ 
/* 382 */     String fragment = this.fragment;
/* 383 */     if (fragment != null) {
/* 384 */       buf.append('#').append(URI_FRAGMENT_ESCAPER.escape(fragment));
/*     */     }
/* 386 */     return buf.toString();
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
/*     */   public final URI toURI()
/*     */   {
/* 401 */     return toURI(build());
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
/*     */   public final URL toURL()
/*     */   {
/* 416 */     return parseURL(build());
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
/*     */   public final URL toURL(String relativeUrl)
/*     */   {
/*     */     try
/*     */     {
/* 433 */       URL url = toURL();
/* 434 */       return new URL(url, relativeUrl);
/*     */     } catch (MalformedURLException e) {
/* 436 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getFirst(String name)
/*     */   {
/* 447 */     Object value = get(name);
/* 448 */     if ((value instanceof Collection))
/*     */     {
/* 450 */       Collection<Object> collectionValue = (Collection)value;
/* 451 */       Iterator<Object> iterator = collectionValue.iterator();
/* 452 */       return iterator.hasNext() ? iterator.next() : null;
/*     */     }
/* 454 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<Object> getAll(String name)
/*     */   {
/* 464 */     Object value = get(name);
/* 465 */     if (value == null) {
/* 466 */       return Collections.emptySet();
/*     */     }
/* 468 */     if ((value instanceof Collection))
/*     */     {
/* 470 */       Collection<Object> collectionValue = (Collection)value;
/* 471 */       return Collections.unmodifiableCollection(collectionValue);
/*     */     }
/* 473 */     return Collections.singleton(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRawPath()
/*     */   {
/* 483 */     List<String> pathParts = this.pathParts;
/* 484 */     if (pathParts == null) {
/* 485 */       return null;
/*     */     }
/* 487 */     StringBuilder buf = new StringBuilder();
/* 488 */     appendRawPathFromParts(buf);
/* 489 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRawPath(String encodedPath)
/*     */   {
/* 498 */     this.pathParts = toPathParts(encodedPath);
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void appendRawPath(String encodedPath)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_1
/*     */     //   1: ifnull +141 -> 142
/*     */     //   4: aload_1
/*     */     //   5: invokevirtual 195	java/lang/String:length	()I
/*     */     //   8: ifeq +134 -> 142
/*     */     //   11: aload_1
/*     */     //   12: invokestatic 104	com/google/api/client/http/GenericUrl:toPathParts	(Ljava/lang/String;)Ljava/util/List;
/*     */     //   15: astore_2
/*     */     //   16: aload_0
/*     */     //   17: getfield 106	com/google/api/client/http/GenericUrl:pathParts	Ljava/util/List;
/*     */     //   20: ifnull +15 -> 35
/*     */     //   23: aload_0
/*     */     //   24: getfield 106	com/google/api/client/http/GenericUrl:pathParts	Ljava/util/List;
/*     */     //   27: invokeinterface 312 1 0
/*     */     //   32: ifeq +11 -> 43
/*     */     //   35: aload_0
/*     */     //   36: aload_2
/*     */     //   37: putfield 106	com/google/api/client/http/GenericUrl:pathParts	Ljava/util/List;
/*     */     //   40: goto +102 -> 142
/*     */     //   43: aload_0
/*     */     //   44: getfield 106	com/google/api/client/http/GenericUrl:pathParts	Ljava/util/List;
/*     */     //   47: invokeinterface 315 1 0
/*     */     //   52: istore_3
/*     */     //   53: aload_0
/*     */     //   54: getfield 106	com/google/api/client/http/GenericUrl:pathParts	Ljava/util/List;
/*     */     //   57: iload_3
/*     */     //   58: iconst_1
/*     */     //   59: isub
/*     */     //   60: aload_0
/*     */     //   61: getfield 106	com/google/api/client/http/GenericUrl:pathParts	Ljava/util/List;
/*     */     //   64: iload_3
/*     */     //   65: iconst_1
/*     */     //   66: isub
/*     */     //   67: invokeinterface 318 2 0
/*     */     //   72: checkcast 93	java/lang/String
/*     */     //   75: invokestatic 189	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   78: aload_2
/*     */     //   79: iconst_0
/*     */     //   80: invokeinterface 318 2 0
/*     */     //   85: checkcast 93	java/lang/String
/*     */     //   88: invokestatic 189	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   91: dup
/*     */     //   92: invokevirtual 195	java/lang/String:length	()I
/*     */     //   95: ifeq +9 -> 104
/*     */     //   98: invokevirtual 198	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   101: goto +12 -> 113
/*     */     //   104: pop
/*     */     //   105: new 93	java/lang/String
/*     */     //   108: dup_x1
/*     */     //   109: swap
/*     */     //   110: invokespecial 200	java/lang/String:<init>	(Ljava/lang/String;)V
/*     */     //   113: invokeinterface 321 3 0
/*     */     //   118: pop
/*     */     //   119: aload_0
/*     */     //   120: getfield 106	com/google/api/client/http/GenericUrl:pathParts	Ljava/util/List;
/*     */     //   123: aload_2
/*     */     //   124: iconst_1
/*     */     //   125: aload_2
/*     */     //   126: invokeinterface 315 1 0
/*     */     //   131: invokeinterface 325 3 0
/*     */     //   136: invokeinterface 329 2 0
/*     */     //   141: pop
/*     */     //   142: return
/*     */     // Line number table:
/*     */     //   Java source line #513	-> byte code offset #0
/*     */     //   Java source line #514	-> byte code offset #11
/*     */     //   Java source line #515	-> byte code offset #16
/*     */     //   Java source line #516	-> byte code offset #35
/*     */     //   Java source line #518	-> byte code offset #43
/*     */     //   Java source line #519	-> byte code offset #53
/*     */     //   Java source line #520	-> byte code offset #119
/*     */     //   Java source line #523	-> byte code offset #142
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	143	0	this	GenericUrl
/*     */     //   0	143	1	encodedPath	String
/*     */     //   16	126	2	appendedPathParts	List<String>
/*     */     //   53	89	3	size	int
/*     */   }
/*     */   
/*     */   public static List<String> toPathParts(String encodedPath)
/*     */   {
/* 535 */     if ((encodedPath == null) || (encodedPath.length() == 0)) {
/* 536 */       return null;
/*     */     }
/* 538 */     List<String> result = new ArrayList();
/* 539 */     int cur = 0;
/* 540 */     boolean notDone = true;
/* 541 */     while (notDone) {
/* 542 */       int slash = encodedPath.indexOf('/', cur);
/* 543 */       notDone = slash != -1;
/*     */       String sub;
/* 545 */       String sub; if (notDone) {
/* 546 */         sub = encodedPath.substring(cur, slash);
/*     */       } else {
/* 548 */         sub = encodedPath.substring(cur);
/*     */       }
/* 550 */       result.add(CharEscapers.decodeUri(sub));
/* 551 */       cur = slash + 1;
/*     */     }
/* 553 */     return result;
/*     */   }
/*     */   
/*     */   private void appendRawPathFromParts(StringBuilder buf) {
/* 557 */     int size = this.pathParts.size();
/* 558 */     for (int i = 0; i < size; i++) {
/* 559 */       String pathPart = (String)this.pathParts.get(i);
/* 560 */       if (i != 0) {
/* 561 */         buf.append('/');
/*     */       }
/* 563 */       if (pathPart.length() != 0) {
/* 564 */         buf.append(CharEscapers.escapeUriPath(pathPart));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static void addQueryParams(Set<Map.Entry<String, Object>> entrySet, StringBuilder buf)
/*     */   {
/* 574 */     boolean first = true;
/* 575 */     for (Map.Entry<String, Object> nameValueEntry : entrySet) {
/* 576 */       Object value = nameValueEntry.getValue();
/* 577 */       if (value != null) {
/* 578 */         String name = CharEscapers.escapeUriQuery((String)nameValueEntry.getKey());
/* 579 */         if ((value instanceof Collection)) {
/* 580 */           Collection<?> collectionValue = (Collection)value;
/* 581 */           for (Object repeatedValue : collectionValue) {
/* 582 */             first = appendParam(first, buf, name, repeatedValue);
/*     */           }
/*     */         } else {
/* 585 */           first = appendParam(first, buf, name, value);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean appendParam(boolean first, StringBuilder buf, String name, Object value) {
/* 592 */     if (first) {
/* 593 */       first = false;
/* 594 */       buf.append('?');
/*     */     } else {
/* 596 */       buf.append('&');
/*     */     }
/* 598 */     buf.append(name);
/* 599 */     String stringValue = CharEscapers.escapeUriQuery(value.toString());
/* 600 */     if (stringValue.length() != 0) {
/* 601 */       buf.append('=').append(stringValue);
/*     */     }
/* 603 */     return first;
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
/*     */   private static URI toURI(String encodedUrl)
/*     */   {
/*     */     try
/*     */     {
/* 618 */       return new URI(encodedUrl);
/*     */     } catch (URISyntaxException e) {
/* 620 */       throw new IllegalArgumentException(e);
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
/*     */ 
/*     */   private static URL parseURL(String encodedUrl)
/*     */   {
/*     */     try
/*     */     {
/* 636 */       return new URL(encodedUrl);
/*     */     } catch (MalformedURLException e) {
/* 638 */       throw new IllegalArgumentException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\GenericUrl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */