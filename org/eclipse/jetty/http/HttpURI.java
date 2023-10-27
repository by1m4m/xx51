/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.eclipse.jetty.util.MultiMap;
/*     */ import org.eclipse.jetty.util.TypeUtil;
/*     */ import org.eclipse.jetty.util.URIUtil;
/*     */ import org.eclipse.jetty.util.UrlEncoded;
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
/*     */ public class HttpURI
/*     */ {
/*     */   private String _scheme;
/*     */   private String _user;
/*     */   private String _host;
/*     */   private int _port;
/*     */   private String _path;
/*     */   private String _param;
/*     */   private String _query;
/*     */   private String _fragment;
/*     */   String _uri;
/*     */   String _decodedPath;
/*     */   
/*     */   private static enum State
/*     */   {
/*  55 */     START, 
/*  56 */     HOST_OR_PATH, 
/*  57 */     SCHEME_OR_PATH, 
/*  58 */     HOST, 
/*  59 */     IPV6, 
/*  60 */     PORT, 
/*  61 */     PATH, 
/*  62 */     PARAM, 
/*  63 */     QUERY, 
/*  64 */     FRAGMENT, 
/*  65 */     ASTERISK;
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
/*     */     private State() {}
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
/*     */   public static HttpURI createHttpURI(String scheme, String host, int port, String path, String param, String query, String fragment)
/*     */   {
/*  94 */     if ((port == 80) && (HttpScheme.HTTP.is(scheme)))
/*  95 */       port = 0;
/*  96 */     if ((port == 443) && (HttpScheme.HTTPS.is(scheme)))
/*  97 */       port = 0;
/*  98 */     return new HttpURI(scheme, host, port, path, param, query, fragment);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpURI() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpURI(String scheme, String host, int port, String path, String param, String query, String fragment)
/*     */   {
/* 109 */     this._scheme = scheme;
/* 110 */     this._host = host;
/* 111 */     this._port = port;
/* 112 */     this._path = path;
/* 113 */     this._param = param;
/* 114 */     this._query = query;
/* 115 */     this._fragment = fragment;
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpURI(HttpURI uri)
/*     */   {
/* 121 */     this(uri._scheme, uri._host, uri._port, uri._path, uri._param, uri._query, uri._fragment);
/* 122 */     this._uri = uri._uri;
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpURI(String uri)
/*     */   {
/* 128 */     this._port = -1;
/* 129 */     parse(State.START, uri, 0, uri.length());
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpURI(URI uri)
/*     */   {
/* 135 */     this._uri = null;
/*     */     
/* 137 */     this._scheme = uri.getScheme();
/* 138 */     this._host = uri.getHost();
/* 139 */     if ((this._host == null) && (uri.getRawSchemeSpecificPart().startsWith("//")))
/* 140 */       this._host = "";
/* 141 */     this._port = uri.getPort();
/* 142 */     this._user = uri.getUserInfo();
/* 143 */     this._path = uri.getRawPath();
/*     */     
/* 145 */     this._decodedPath = uri.getPath();
/* 146 */     if (this._decodedPath != null)
/*     */     {
/* 148 */       int p = this._decodedPath.lastIndexOf(';');
/* 149 */       if (p >= 0)
/* 150 */         this._param = this._decodedPath.substring(p + 1);
/*     */     }
/* 152 */     this._query = uri.getRawQuery();
/* 153 */     this._fragment = uri.getFragment();
/*     */     
/* 155 */     this._decodedPath = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpURI(String scheme, String host, int port, String pathQuery)
/*     */   {
/* 161 */     this._uri = null;
/*     */     
/* 163 */     this._scheme = scheme;
/* 164 */     this._host = host;
/* 165 */     this._port = port;
/*     */     
/* 167 */     parse(State.PATH, pathQuery, 0, pathQuery.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void parse(String uri)
/*     */   {
/* 174 */     clear();
/* 175 */     this._uri = uri;
/* 176 */     parse(State.START, uri, 0, uri.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void parseRequestTarget(String method, String uri)
/*     */   {
/* 187 */     clear();
/* 188 */     this._uri = uri;
/*     */     
/* 190 */     if (HttpMethod.CONNECT.is(method)) {
/* 191 */       this._path = uri;
/*     */     } else {
/* 193 */       parse(uri.startsWith("/") ? State.PATH : State.START, uri, 0, uri.length());
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void parseConnect(String uri)
/*     */   {
/* 200 */     clear();
/* 201 */     this._uri = uri;
/* 202 */     this._path = uri;
/*     */   }
/*     */   
/*     */ 
/*     */   public void parse(String uri, int offset, int length)
/*     */   {
/* 208 */     clear();
/* 209 */     int end = offset + length;
/* 210 */     this._uri = uri.substring(offset, end);
/* 211 */     parse(State.START, uri, offset, end);
/*     */   }
/*     */   
/*     */ 
/*     */   private void parse(State state, String uri, int offset, int end)
/*     */   {
/* 217 */     boolean encoded = false;
/* 218 */     int mark = offset;
/* 219 */     int path_mark = 0;
/*     */     
/* 221 */     for (int i = offset; i < end; i++)
/*     */     {
/* 223 */       char c = uri.charAt(i);
/*     */       
/* 225 */       switch (state)
/*     */       {
/*     */ 
/*     */       case START: 
/* 229 */         switch (c)
/*     */         {
/*     */         case '/': 
/* 232 */           mark = i;
/* 233 */           state = State.HOST_OR_PATH;
/* 234 */           break;
/*     */         case ';': 
/* 236 */           mark = i + 1;
/* 237 */           state = State.PARAM;
/* 238 */           break;
/*     */         
/*     */         case '?': 
/* 241 */           this._path = "";
/* 242 */           mark = i + 1;
/* 243 */           state = State.QUERY;
/* 244 */           break;
/*     */         case '#': 
/* 246 */           mark = i + 1;
/* 247 */           state = State.FRAGMENT;
/* 248 */           break;
/*     */         case '*': 
/* 250 */           this._path = "*";
/* 251 */           state = State.ASTERISK;
/* 252 */           break;
/*     */         
/*     */         default: 
/* 255 */           mark = i;
/* 256 */           if (this._scheme == null) {
/* 257 */             state = State.SCHEME_OR_PATH;
/*     */           }
/*     */           else {
/* 260 */             path_mark = i;
/* 261 */             state = State.PATH;
/*     */           }
/*     */           break;
/*     */         }
/* 265 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case SCHEME_OR_PATH: 
/* 270 */         switch (c)
/*     */         {
/*     */ 
/*     */         case ':': 
/* 274 */           this._scheme = uri.substring(mark, i);
/*     */           
/* 276 */           state = State.START;
/* 277 */           break;
/*     */         
/*     */ 
/*     */         case '/': 
/* 281 */           state = State.PATH;
/* 282 */           break;
/*     */         
/*     */ 
/*     */         case ';': 
/* 286 */           mark = i + 1;
/* 287 */           state = State.PARAM;
/* 288 */           break;
/*     */         
/*     */ 
/*     */         case '?': 
/* 292 */           this._path = uri.substring(mark, i);
/* 293 */           mark = i + 1;
/* 294 */           state = State.QUERY;
/* 295 */           break;
/*     */         
/*     */ 
/*     */         case '%': 
/* 299 */           encoded = true;
/* 300 */           state = State.PATH;
/* 301 */           break;
/*     */         
/*     */ 
/*     */         case '#': 
/* 305 */           this._path = uri.substring(mark, i);
/* 306 */           state = State.FRAGMENT;
/*     */         }
/*     */         
/* 309 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case HOST_OR_PATH: 
/* 314 */         switch (c)
/*     */         {
/*     */         case '/': 
/* 317 */           this._host = "";
/* 318 */           mark = i + 1;
/* 319 */           state = State.HOST;
/* 320 */           break;
/*     */         
/*     */ 
/*     */         case '#': 
/*     */         case ';': 
/*     */         case '?': 
/*     */         case '@': 
/* 327 */           i--;
/* 328 */           path_mark = mark;
/* 329 */           state = State.PATH;
/* 330 */           break;
/*     */         
/*     */         default: 
/* 333 */           path_mark = mark;
/* 334 */           state = State.PATH;
/*     */         }
/* 336 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case HOST: 
/* 341 */         switch (c)
/*     */         {
/*     */         case '/': 
/* 344 */           this._host = uri.substring(mark, i);
/* 345 */           path_mark = mark = i;
/* 346 */           state = State.PATH;
/* 347 */           break;
/*     */         case ':': 
/* 349 */           if (i > mark)
/* 350 */             this._host = uri.substring(mark, i);
/* 351 */           mark = i + 1;
/* 352 */           state = State.PORT;
/* 353 */           break;
/*     */         case '@': 
/* 355 */           if (this._user != null)
/* 356 */             throw new IllegalArgumentException("Bad authority");
/* 357 */           this._user = uri.substring(mark, i);
/* 358 */           mark = i + 1;
/* 359 */           break;
/*     */         
/*     */         case '[': 
/* 362 */           state = State.IPV6;
/*     */         }
/*     */         
/* 365 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case IPV6: 
/* 370 */         switch (c)
/*     */         {
/*     */         case '/': 
/* 373 */           throw new IllegalArgumentException("No closing ']' for ipv6 in " + uri);
/*     */         case ']': 
/* 375 */           c = uri.charAt(++i);
/* 376 */           this._host = uri.substring(mark, i);
/* 377 */           if (c == ':')
/*     */           {
/* 379 */             mark = i + 1;
/* 380 */             state = State.PORT;
/*     */           }
/*     */           else
/*     */           {
/* 384 */             path_mark = mark = i;
/* 385 */             state = State.PATH;
/*     */           }
/*     */           break;
/*     */         }
/*     */         
/* 390 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case PORT: 
/* 395 */         if (c == '@')
/*     */         {
/* 397 */           if (this._user != null) {
/* 398 */             throw new IllegalArgumentException("Bad authority");
/*     */           }
/* 400 */           this._user = (this._host + ":" + uri.substring(mark, i));
/* 401 */           mark = i + 1;
/* 402 */           state = State.HOST;
/*     */         }
/* 404 */         else if (c == '/')
/*     */         {
/* 406 */           this._port = TypeUtil.parseInt(uri, mark, i - mark, 10);
/* 407 */           path_mark = mark = i;
/* 408 */           state = State.PATH;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */         break;
/*     */       case PATH: 
/* 415 */         switch (c)
/*     */         {
/*     */         case ';': 
/* 418 */           mark = i + 1;
/* 419 */           state = State.PARAM;
/* 420 */           break;
/*     */         case '?': 
/* 422 */           this._path = uri.substring(path_mark, i);
/* 423 */           mark = i + 1;
/* 424 */           state = State.QUERY;
/* 425 */           break;
/*     */         case '#': 
/* 427 */           this._path = uri.substring(path_mark, i);
/* 428 */           mark = i + 1;
/* 429 */           state = State.FRAGMENT;
/* 430 */           break;
/*     */         case '%': 
/* 432 */           encoded = true;
/*     */         }
/*     */         
/* 435 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case PARAM: 
/* 440 */         switch (c)
/*     */         {
/*     */         case '?': 
/* 443 */           this._path = uri.substring(path_mark, i);
/* 444 */           this._param = uri.substring(mark, i);
/* 445 */           mark = i + 1;
/* 446 */           state = State.QUERY;
/* 447 */           break;
/*     */         case '#': 
/* 449 */           this._path = uri.substring(path_mark, i);
/* 450 */           this._param = uri.substring(mark, i);
/* 451 */           mark = i + 1;
/* 452 */           state = State.FRAGMENT;
/* 453 */           break;
/*     */         case '/': 
/* 455 */           encoded = true;
/*     */           
/* 457 */           state = State.PATH;
/* 458 */           break;
/*     */         
/*     */         case ';': 
/* 461 */           mark = i + 1;
/*     */         }
/*     */         
/* 464 */         break;
/*     */       
/*     */ 
/*     */ 
/*     */       case QUERY: 
/* 469 */         if (c == '#')
/*     */         {
/* 471 */           this._query = uri.substring(mark, i);
/* 472 */           mark = i + 1;
/* 473 */           state = State.FRAGMENT;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */         break;
/*     */       case ASTERISK: 
/* 480 */         throw new IllegalArgumentException("Bad character '*'");
/*     */       
/*     */ 
/*     */ 
/*     */       case FRAGMENT: 
/* 485 */         this._fragment = uri.substring(mark, end);
/* 486 */         i = end;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/* 492 */     switch (state)
/*     */     {
/*     */     case START: 
/*     */       break;
/*     */     case SCHEME_OR_PATH: 
/* 497 */       this._path = uri.substring(mark, end);
/* 498 */       break;
/*     */     
/*     */     case HOST_OR_PATH: 
/* 501 */       this._path = uri.substring(mark, end);
/* 502 */       break;
/*     */     
/*     */     case HOST: 
/* 505 */       if (end > mark) {
/* 506 */         this._host = uri.substring(mark, end);
/*     */       }
/*     */       break;
/*     */     case IPV6: 
/* 510 */       throw new IllegalArgumentException("No closing ']' for ipv6 in " + uri);
/*     */     
/*     */     case PORT: 
/* 513 */       this._port = TypeUtil.parseInt(uri, mark, end - mark, 10);
/* 514 */       break;
/*     */     
/*     */     case ASTERISK: 
/*     */       break;
/*     */     
/*     */     case FRAGMENT: 
/* 520 */       this._fragment = uri.substring(mark, end);
/* 521 */       break;
/*     */     
/*     */     case PARAM: 
/* 524 */       this._path = uri.substring(path_mark, end);
/* 525 */       this._param = uri.substring(mark, end);
/* 526 */       break;
/*     */     
/*     */     case PATH: 
/* 529 */       this._path = uri.substring(path_mark, end);
/* 530 */       break;
/*     */     
/*     */     case QUERY: 
/* 533 */       this._query = uri.substring(mark, end);
/*     */     }
/*     */     
/*     */     
/* 537 */     if (!encoded)
/*     */     {
/* 539 */       if (this._param == null) {
/* 540 */         this._decodedPath = this._path;
/*     */       } else {
/* 542 */         this._decodedPath = this._path.substring(0, this._path.length() - this._param.length() - 1);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getScheme()
/*     */   {
/* 549 */     return this._scheme;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getHost()
/*     */   {
/* 556 */     if ((this._host != null) && (this._host.length() == 0))
/* 557 */       return null;
/* 558 */     return this._host;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 564 */     return this._port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPath()
/*     */   {
/* 575 */     return this._path;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getDecodedPath()
/*     */   {
/* 581 */     if ((this._decodedPath == null) && (this._path != null))
/* 582 */       this._decodedPath = URIUtil.decodePath(this._path);
/* 583 */     return this._decodedPath;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getParam()
/*     */   {
/* 589 */     return this._param;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getQuery()
/*     */   {
/* 595 */     return this._query;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean hasQuery()
/*     */   {
/* 601 */     return (this._query != null) && (this._query.length() > 0);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getFragment()
/*     */   {
/* 607 */     return this._fragment;
/*     */   }
/*     */   
/*     */ 
/*     */   public void decodeQueryTo(MultiMap<String> parameters)
/*     */   {
/* 613 */     if (this._query == null)
/* 614 */       return;
/* 615 */     UrlEncoded.decodeUtf8To(this._query, parameters);
/*     */   }
/*     */   
/*     */   public void decodeQueryTo(MultiMap<String> parameters, String encoding)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 621 */     decodeQueryTo(parameters, Charset.forName(encoding));
/*     */   }
/*     */   
/*     */   public void decodeQueryTo(MultiMap<String> parameters, Charset encoding)
/*     */     throws UnsupportedEncodingException
/*     */   {
/* 627 */     if (this._query == null) {
/* 628 */       return;
/*     */     }
/* 630 */     if ((encoding == null) || (StandardCharsets.UTF_8.equals(encoding))) {
/* 631 */       UrlEncoded.decodeUtf8To(this._query, parameters);
/*     */     } else {
/* 633 */       UrlEncoded.decodeTo(this._query, parameters, encoding);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 639 */     this._uri = null;
/*     */     
/* 641 */     this._scheme = null;
/* 642 */     this._host = null;
/* 643 */     this._port = -1;
/* 644 */     this._path = null;
/* 645 */     this._param = null;
/* 646 */     this._query = null;
/* 647 */     this._fragment = null;
/*     */     
/* 649 */     this._decodedPath = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isAbsolute()
/*     */   {
/* 655 */     return (this._scheme != null) && (this._scheme.length() > 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 662 */     if (this._uri == null)
/*     */     {
/* 664 */       StringBuilder out = new StringBuilder();
/*     */       
/* 666 */       if (this._scheme != null) {
/* 667 */         out.append(this._scheme).append(':');
/*     */       }
/* 669 */       if (this._host != null)
/*     */       {
/* 671 */         out.append("//");
/* 672 */         if (this._user != null)
/* 673 */           out.append(this._user).append('@');
/* 674 */         out.append(this._host);
/*     */       }
/*     */       
/* 677 */       if (this._port > 0) {
/* 678 */         out.append(':').append(this._port);
/*     */       }
/* 680 */       if (this._path != null) {
/* 681 */         out.append(this._path);
/*     */       }
/* 683 */       if (this._query != null) {
/* 684 */         out.append('?').append(this._query);
/*     */       }
/* 686 */       if (this._fragment != null) {
/* 687 */         out.append('#').append(this._fragment);
/*     */       }
/* 689 */       if (out.length() > 0) {
/* 690 */         this._uri = out.toString();
/*     */       } else
/* 692 */         this._uri = "";
/*     */     }
/* 694 */     return this._uri;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 701 */     if (o == this)
/* 702 */       return true;
/* 703 */     if (!(o instanceof HttpURI))
/* 704 */       return false;
/* 705 */     return toString().equals(o.toString());
/*     */   }
/*     */   
/*     */ 
/*     */   public void setScheme(String scheme)
/*     */   {
/* 711 */     this._scheme = scheme;
/* 712 */     this._uri = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAuthority(String host, int port)
/*     */   {
/* 722 */     this._host = host;
/* 723 */     this._port = port;
/* 724 */     this._uri = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPath(String path)
/*     */   {
/* 733 */     this._uri = null;
/* 734 */     this._path = path;
/* 735 */     this._decodedPath = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPathQuery(String path)
/*     */   {
/* 741 */     this._uri = null;
/* 742 */     this._path = null;
/* 743 */     this._decodedPath = null;
/* 744 */     this._param = null;
/* 745 */     this._fragment = null;
/* 746 */     if (path != null) {
/* 747 */       parse(State.PATH, path, 0, path.length());
/*     */     }
/*     */   }
/*     */   
/*     */   public void setQuery(String query)
/*     */   {
/* 753 */     this._query = query;
/* 754 */     this._uri = null;
/*     */   }
/*     */   
/*     */   public URI toURI()
/*     */     throws URISyntaxException
/*     */   {
/* 760 */     return new URI(this._scheme, null, this._host, this._port, this._path, this._query == null ? null : UrlEncoded.decodeString(this._query), this._fragment);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getPathQuery()
/*     */   {
/* 766 */     if (this._query == null)
/* 767 */       return this._path;
/* 768 */     return this._path + "?" + this._query;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getAuthority()
/*     */   {
/* 774 */     if (this._port > 0)
/* 775 */       return this._host + ":" + this._port;
/* 776 */     return this._host;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getUser()
/*     */   {
/* 782 */     return this._user;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpURI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */