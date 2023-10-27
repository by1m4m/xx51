/*      */ package com.google.api.client.http;
/*      */ 
/*      */ import com.google.api.client.util.ArrayValueMap;
/*      */ import com.google.api.client.util.ClassInfo;
/*      */ import com.google.api.client.util.Data;
/*      */ import com.google.api.client.util.FieldInfo;
/*      */ import com.google.api.client.util.GenericData;
/*      */ import com.google.api.client.util.GenericData.Flags;
/*      */ import com.google.api.client.util.Key;
/*      */ import com.google.api.client.util.Preconditions;
/*      */ import com.google.api.client.util.StringUtils;
/*      */ import com.google.api.client.util.Throwables;
/*      */ import com.google.api.client.util.Types;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Type;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class HttpHeaders
/*      */   extends GenericData
/*      */ {
/*      */   @Key("Accept")
/*      */   private List<String> accept;
/*      */   
/*      */   public HttpHeaders()
/*      */   {
/*   61 */     super(EnumSet.of(GenericData.Flags.IGNORE_CASE));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Key("Accept-Encoding")
/*   69 */   private List<String> acceptEncoding = new ArrayList(Collections.singleton("gzip"));
/*      */   
/*      */ 
/*      */   @Key("Authorization")
/*      */   private List<String> authorization;
/*      */   
/*      */ 
/*      */   @Key("Cache-Control")
/*      */   private List<String> cacheControl;
/*      */   
/*      */ 
/*      */   @Key("Content-Encoding")
/*      */   private List<String> contentEncoding;
/*      */   
/*      */ 
/*      */   @Key("Content-Length")
/*      */   private List<Long> contentLength;
/*      */   
/*      */ 
/*      */   @Key("Content-MD5")
/*      */   private List<String> contentMD5;
/*      */   
/*      */ 
/*      */   @Key("Content-Range")
/*      */   private List<String> contentRange;
/*      */   
/*      */ 
/*      */   @Key("Content-Type")
/*      */   private List<String> contentType;
/*      */   
/*      */ 
/*      */   @Key("Cookie")
/*      */   private List<String> cookie;
/*      */   
/*      */ 
/*      */   @Key("Date")
/*      */   private List<String> date;
/*      */   
/*      */ 
/*      */   @Key("ETag")
/*      */   private List<String> etag;
/*      */   
/*      */ 
/*      */   @Key("Expires")
/*      */   private List<String> expires;
/*      */   
/*      */ 
/*      */   @Key("If-Modified-Since")
/*      */   private List<String> ifModifiedSince;
/*      */   
/*      */ 
/*      */   @Key("If-Match")
/*      */   private List<String> ifMatch;
/*      */   
/*      */ 
/*      */   @Key("If-None-Match")
/*      */   private List<String> ifNoneMatch;
/*      */   
/*      */ 
/*      */   @Key("If-Unmodified-Since")
/*      */   private List<String> ifUnmodifiedSince;
/*      */   
/*      */ 
/*      */   @Key("If-Range")
/*      */   private List<String> ifRange;
/*      */   
/*      */ 
/*      */   @Key("Last-Modified")
/*      */   private List<String> lastModified;
/*      */   
/*      */ 
/*      */   @Key("Location")
/*      */   private List<String> location;
/*      */   
/*      */ 
/*      */   @Key("MIME-Version")
/*      */   private List<String> mimeVersion;
/*      */   
/*      */ 
/*      */   @Key("Range")
/*      */   private List<String> range;
/*      */   
/*      */ 
/*      */   @Key("Retry-After")
/*      */   private List<String> retryAfter;
/*      */   
/*      */ 
/*      */   @Key("User-Agent")
/*      */   private List<String> userAgent;
/*      */   
/*      */ 
/*      */   @Key("WWW-Authenticate")
/*      */   private List<String> authenticate;
/*      */   
/*      */ 
/*      */   @Key("Age")
/*      */   private List<Long> age;
/*      */   
/*      */ 
/*      */   public HttpHeaders clone()
/*      */   {
/*  170 */     return (HttpHeaders)super.clone();
/*      */   }
/*      */   
/*      */   public HttpHeaders set(String fieldName, Object value)
/*      */   {
/*  175 */     return (HttpHeaders)super.set(fieldName, value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getAccept()
/*      */   {
/*  184 */     return (String)getFirstHeaderValue(this.accept);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setAccept(String accept)
/*      */   {
/*  198 */     this.accept = getAsList(accept);
/*  199 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getAcceptEncoding()
/*      */   {
/*  208 */     return (String)getFirstHeaderValue(this.acceptEncoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setAcceptEncoding(String acceptEncoding)
/*      */   {
/*  221 */     this.acceptEncoding = getAsList(acceptEncoding);
/*  222 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getAuthorization()
/*      */   {
/*  231 */     return (String)getFirstHeaderValue(this.authorization);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final List<String> getAuthorizationAsList()
/*      */   {
/*  240 */     return this.authorization;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setAuthorization(String authorization)
/*      */   {
/*  254 */     return setAuthorization(getAsList(authorization));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setAuthorization(List<String> authorization)
/*      */   {
/*  268 */     this.authorization = authorization;
/*  269 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getCacheControl()
/*      */   {
/*  278 */     return (String)getFirstHeaderValue(this.cacheControl);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setCacheControl(String cacheControl)
/*      */   {
/*  292 */     this.cacheControl = getAsList(cacheControl);
/*  293 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getContentEncoding()
/*      */   {
/*  302 */     return (String)getFirstHeaderValue(this.contentEncoding);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setContentEncoding(String contentEncoding)
/*      */   {
/*  316 */     this.contentEncoding = getAsList(contentEncoding);
/*  317 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Long getContentLength()
/*      */   {
/*  326 */     return (Long)getFirstHeaderValue(this.contentLength);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setContentLength(Long contentLength)
/*      */   {
/*  340 */     this.contentLength = getAsList(contentLength);
/*  341 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getContentMD5()
/*      */   {
/*  350 */     return (String)getFirstHeaderValue(this.contentMD5);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setContentMD5(String contentMD5)
/*      */   {
/*  364 */     this.contentMD5 = getAsList(contentMD5);
/*  365 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getContentRange()
/*      */   {
/*  374 */     return (String)getFirstHeaderValue(this.contentRange);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setContentRange(String contentRange)
/*      */   {
/*  388 */     this.contentRange = getAsList(contentRange);
/*  389 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getContentType()
/*      */   {
/*  398 */     return (String)getFirstHeaderValue(this.contentType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setContentType(String contentType)
/*      */   {
/*  412 */     this.contentType = getAsList(contentType);
/*  413 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getCookie()
/*      */   {
/*  426 */     return (String)getFirstHeaderValue(this.cookie);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setCookie(String cookie)
/*      */   {
/*  440 */     this.cookie = getAsList(cookie);
/*  441 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getDate()
/*      */   {
/*  450 */     return (String)getFirstHeaderValue(this.date);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setDate(String date)
/*      */   {
/*  464 */     this.date = getAsList(date);
/*  465 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getETag()
/*      */   {
/*  474 */     return (String)getFirstHeaderValue(this.etag);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setETag(String etag)
/*      */   {
/*  488 */     this.etag = getAsList(etag);
/*  489 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getExpires()
/*      */   {
/*  498 */     return (String)getFirstHeaderValue(this.expires);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setExpires(String expires)
/*      */   {
/*  512 */     this.expires = getAsList(expires);
/*  513 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getIfModifiedSince()
/*      */   {
/*  522 */     return (String)getFirstHeaderValue(this.ifModifiedSince);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setIfModifiedSince(String ifModifiedSince)
/*      */   {
/*  536 */     this.ifModifiedSince = getAsList(ifModifiedSince);
/*  537 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getIfMatch()
/*      */   {
/*  546 */     return (String)getFirstHeaderValue(this.ifMatch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setIfMatch(String ifMatch)
/*      */   {
/*  560 */     this.ifMatch = getAsList(ifMatch);
/*  561 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getIfNoneMatch()
/*      */   {
/*  570 */     return (String)getFirstHeaderValue(this.ifNoneMatch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setIfNoneMatch(String ifNoneMatch)
/*      */   {
/*  584 */     this.ifNoneMatch = getAsList(ifNoneMatch);
/*  585 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getIfUnmodifiedSince()
/*      */   {
/*  594 */     return (String)getFirstHeaderValue(this.ifUnmodifiedSince);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setIfUnmodifiedSince(String ifUnmodifiedSince)
/*      */   {
/*  608 */     this.ifUnmodifiedSince = getAsList(ifUnmodifiedSince);
/*  609 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getIfRange()
/*      */   {
/*  618 */     return (String)getFirstHeaderValue(this.ifRange);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setIfRange(String ifRange)
/*      */   {
/*  632 */     this.ifRange = getAsList(ifRange);
/*  633 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getLastModified()
/*      */   {
/*  642 */     return (String)getFirstHeaderValue(this.lastModified);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setLastModified(String lastModified)
/*      */   {
/*  656 */     this.lastModified = getAsList(lastModified);
/*  657 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getLocation()
/*      */   {
/*  666 */     return (String)getFirstHeaderValue(this.location);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setLocation(String location)
/*      */   {
/*  680 */     this.location = getAsList(location);
/*  681 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getMimeVersion()
/*      */   {
/*  690 */     return (String)getFirstHeaderValue(this.mimeVersion);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setMimeVersion(String mimeVersion)
/*      */   {
/*  704 */     this.mimeVersion = getAsList(mimeVersion);
/*  705 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getRange()
/*      */   {
/*  714 */     return (String)getFirstHeaderValue(this.range);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setRange(String range)
/*      */   {
/*  728 */     this.range = getAsList(range);
/*  729 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getRetryAfter()
/*      */   {
/*  738 */     return (String)getFirstHeaderValue(this.retryAfter);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setRetryAfter(String retryAfter)
/*      */   {
/*  752 */     this.retryAfter = getAsList(retryAfter);
/*  753 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getUserAgent()
/*      */   {
/*  762 */     return (String)getFirstHeaderValue(this.userAgent);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setUserAgent(String userAgent)
/*      */   {
/*  776 */     this.userAgent = getAsList(userAgent);
/*  777 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final String getAuthenticate()
/*      */   {
/*  786 */     return (String)getFirstHeaderValue(this.authenticate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final List<String> getAuthenticateAsList()
/*      */   {
/*  795 */     return this.authenticate;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setAuthenticate(String authenticate)
/*      */   {
/*  809 */     this.authenticate = getAsList(authenticate);
/*  810 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Long getAge()
/*      */   {
/*  819 */     return (Long)getFirstHeaderValue(this.age);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public HttpHeaders setAge(Long age)
/*      */   {
/*  833 */     this.age = getAsList(age);
/*  834 */     return this;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public HttpHeaders setBasicAuthentication(String username, String password)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: invokestatic 254	com/google/api/client/util/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   4: checkcast 131	java/lang/String
/*      */     //   7: invokestatic 258	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   10: invokestatic 258	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   13: astore_3
/*      */     //   14: aload_2
/*      */     //   15: invokestatic 254	com/google/api/client/util/Preconditions:checkNotNull	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   18: checkcast 131	java/lang/String
/*      */     //   21: invokestatic 258	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   24: invokestatic 258	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   27: astore 4
/*      */     //   29: new 260	java/lang/StringBuilder
/*      */     //   32: dup
/*      */     //   33: iconst_1
/*      */     //   34: aload_3
/*      */     //   35: invokevirtual 264	java/lang/String:length	()I
/*      */     //   38: iadd
/*      */     //   39: aload 4
/*      */     //   41: invokevirtual 264	java/lang/String:length	()I
/*      */     //   44: iadd
/*      */     //   45: invokespecial 267	java/lang/StringBuilder:<init>	(I)V
/*      */     //   48: aload_3
/*      */     //   49: invokevirtual 271	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   52: ldc_w 273
/*      */     //   55: invokevirtual 271	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   58: aload 4
/*      */     //   60: invokevirtual 271	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   63: invokevirtual 276	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   66: astore 5
/*      */     //   68: aload 5
/*      */     //   70: invokestatic 282	com/google/api/client/util/StringUtils:getBytesUtf8	(Ljava/lang/String;)[B
/*      */     //   73: invokestatic 288	com/google/api/client/util/Base64:encodeBase64String	([B)Ljava/lang/String;
/*      */     //   76: astore 6
/*      */     //   78: aload_0
/*      */     //   79: ldc_w 290
/*      */     //   82: aload 6
/*      */     //   84: invokestatic 258	java/lang/String:valueOf	(Ljava/lang/Object;)Ljava/lang/String;
/*      */     //   87: dup
/*      */     //   88: invokevirtual 264	java/lang/String:length	()I
/*      */     //   91: ifeq +9 -> 100
/*      */     //   94: invokevirtual 294	java/lang/String:concat	(Ljava/lang/String;)Ljava/lang/String;
/*      */     //   97: goto +12 -> 109
/*      */     //   100: pop
/*      */     //   101: new 131	java/lang/String
/*      */     //   104: dup_x1
/*      */     //   105: swap
/*      */     //   106: invokespecial 297	java/lang/String:<init>	(Ljava/lang/String;)V
/*      */     //   109: invokevirtual 299	com/google/api/client/http/HttpHeaders:setAuthorization	(Ljava/lang/String;)Lcom/google/api/client/http/HttpHeaders;
/*      */     //   112: areturn
/*      */     // Line number table:
/*      */     //   Java source line #849	-> byte code offset #0
/*      */     //   Java source line #851	-> byte code offset #68
/*      */     //   Java source line #852	-> byte code offset #78
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	113	0	this	HttpHeaders
/*      */     //   0	113	1	username	String
/*      */     //   0	113	2	password	String
/*      */     //   68	45	5	userPass	String
/*      */     //   78	35	6	encoded	String
/*      */   }
/*      */   
/*      */   private static void addHeader(Logger logger, StringBuilder logbuf, StringBuilder curlbuf, LowLevelHttpRequest lowLevelHttpRequest, String name, Object value, Writer writer)
/*      */     throws IOException
/*      */   {
/*  863 */     if ((value == null) || (Data.isNull(value))) {
/*  864 */       return;
/*      */     }
/*      */     
/*  867 */     String stringValue = toStringValue(value);
/*      */     
/*  869 */     String loggedStringValue = stringValue;
/*  870 */     if ((("Authorization".equalsIgnoreCase(name)) || ("Cookie".equalsIgnoreCase(name))) && ((logger == null) || (!logger.isLoggable(Level.ALL))))
/*      */     {
/*  872 */       loggedStringValue = "<Not Logged>";
/*      */     }
/*  874 */     if (logbuf != null) {
/*  875 */       logbuf.append(name).append(": ");
/*  876 */       logbuf.append(loggedStringValue);
/*  877 */       logbuf.append(StringUtils.LINE_SEPARATOR);
/*      */     }
/*  879 */     if (curlbuf != null) {
/*  880 */       curlbuf.append(" -H '").append(name).append(": ").append(loggedStringValue).append("'");
/*      */     }
/*      */     
/*  883 */     if (lowLevelHttpRequest != null) {
/*  884 */       lowLevelHttpRequest.addHeader(name, stringValue);
/*      */     }
/*      */     
/*  887 */     if (writer != null) {
/*  888 */       writer.write(name);
/*  889 */       writer.write(": ");
/*  890 */       writer.write(stringValue);
/*  891 */       writer.write("\r\n");
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static String toStringValue(Object headerValue)
/*      */   {
/*  899 */     return (headerValue instanceof Enum) ? FieldInfo.of((Enum)headerValue).getName() : headerValue.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static void serializeHeaders(HttpHeaders headers, StringBuilder logbuf, StringBuilder curlbuf, Logger logger, LowLevelHttpRequest lowLevelHttpRequest)
/*      */     throws IOException
/*      */   {
/*  916 */     serializeHeaders(headers, logbuf, curlbuf, logger, lowLevelHttpRequest, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void serializeHeadersForMultipartRequests(HttpHeaders headers, StringBuilder logbuf, Logger logger, Writer writer)
/*      */     throws IOException
/*      */   {
/*  932 */     serializeHeaders(headers, logbuf, null, logger, null, writer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static void serializeHeaders(HttpHeaders headers, StringBuilder logbuf, StringBuilder curlbuf, Logger logger, LowLevelHttpRequest lowLevelHttpRequest, Writer writer)
/*      */     throws IOException
/*      */   {
/*  941 */     HashSet<String> headerNames = new HashSet();
/*  942 */     for (Map.Entry<String, Object> headerEntry : headers.entrySet()) {
/*  943 */       String name = (String)headerEntry.getKey();
/*  944 */       Preconditions.checkArgument(headerNames.add(name), "multiple headers of the same name (headers are case insensitive): %s", new Object[] { name });
/*      */       
/*  946 */       Object value = headerEntry.getValue();
/*  947 */       if (value != null)
/*      */       {
/*  949 */         String displayName = name;
/*  950 */         FieldInfo fieldInfo = headers.getClassInfo().getFieldInfo(name);
/*  951 */         if (fieldInfo != null) {
/*  952 */           displayName = fieldInfo.getName();
/*      */         }
/*  954 */         Class<? extends Object> valueClass = value.getClass();
/*  955 */         if (((value instanceof Iterable)) || (valueClass.isArray())) {
/*  956 */           for (Object repeatedValue : Types.iterableOf(value)) {
/*  957 */             addHeader(logger, logbuf, curlbuf, lowLevelHttpRequest, displayName, repeatedValue, writer);
/*      */ 
/*      */           }
/*      */           
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*  966 */           addHeader(logger, logbuf, curlbuf, lowLevelHttpRequest, displayName, value, writer);
/*      */         }
/*      */       }
/*      */     }
/*  970 */     if (writer != null) {
/*  971 */       writer.flush();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void fromHttpResponse(LowLevelHttpResponse response, StringBuilder logger)
/*      */     throws IOException
/*      */   {
/*  985 */     clear();
/*  986 */     ParseHeaderState state = new ParseHeaderState(this, logger);
/*  987 */     int headerCount = response.getHeaderCount();
/*  988 */     for (int i = 0; i < headerCount; i++) {
/*  989 */       parseHeader(response.getHeaderName(i), response.getHeaderValue(i), state);
/*      */     }
/*  991 */     state.finish();
/*      */   }
/*      */   
/*      */   private static class HeaderParsingFakeLevelHttpRequest extends LowLevelHttpRequest
/*      */   {
/*      */     private final HttpHeaders target;
/*      */     private final HttpHeaders.ParseHeaderState state;
/*      */     
/*      */     HeaderParsingFakeLevelHttpRequest(HttpHeaders target, HttpHeaders.ParseHeaderState state) {
/* 1000 */       this.target = target;
/* 1001 */       this.state = state;
/*      */     }
/*      */     
/*      */     public void addHeader(String name, String value)
/*      */     {
/* 1006 */       this.target.parseHeader(name, value, this.state);
/*      */     }
/*      */     
/*      */     public LowLevelHttpResponse execute() throws IOException
/*      */     {
/* 1011 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */   private <T> T getFirstHeaderValue(List<T> internalValue)
/*      */   {
/* 1017 */     return internalValue == null ? null : internalValue.get(0);
/*      */   }
/*      */   
/*      */   private <T> List<T> getAsList(T passedValue)
/*      */   {
/* 1022 */     if (passedValue == null) {
/* 1023 */       return null;
/*      */     }
/* 1025 */     List<T> result = new ArrayList();
/* 1026 */     result.add(passedValue);
/* 1027 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getFirstHeaderStringValue(String name)
/*      */   {
/* 1038 */     Object value = get(name.toLowerCase());
/* 1039 */     if (value == null) {
/* 1040 */       return null;
/*      */     }
/* 1042 */     Class<? extends Object> valueClass = value.getClass();
/* 1043 */     if (((value instanceof Iterable)) || (valueClass.isArray())) {
/* 1044 */       Iterator i$ = Types.iterableOf(value).iterator(); if (i$.hasNext()) { Object repeatedValue = i$.next();
/* 1045 */         return toStringValue(repeatedValue);
/*      */       }
/*      */     }
/* 1048 */     return toStringValue(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<String> getHeaderStringValues(String name)
/*      */   {
/* 1059 */     Object value = get(name.toLowerCase());
/* 1060 */     if (value == null) {
/* 1061 */       return Collections.emptyList();
/*      */     }
/* 1063 */     Class<? extends Object> valueClass = value.getClass();
/* 1064 */     if (((value instanceof Iterable)) || (valueClass.isArray())) {
/* 1065 */       List<String> values = new ArrayList();
/* 1066 */       for (Object repeatedValue : Types.iterableOf(value)) {
/* 1067 */         values.add(toStringValue(repeatedValue));
/*      */       }
/* 1069 */       return Collections.unmodifiableList(values);
/*      */     }
/* 1071 */     return Collections.singletonList(toStringValue(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void fromHttpHeaders(HttpHeaders headers)
/*      */   {
/*      */     try
/*      */     {
/* 1082 */       ParseHeaderState state = new ParseHeaderState(this, null);
/* 1083 */       serializeHeaders(headers, null, null, null, new HeaderParsingFakeLevelHttpRequest(this, state));
/*      */       
/* 1085 */       state.finish();
/*      */     }
/*      */     catch (IOException ex) {
/* 1088 */       throw Throwables.propagate(ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final class ParseHeaderState
/*      */   {
/*      */     final ArrayValueMap arrayValueMap;
/*      */     
/*      */ 
/*      */ 
/*      */     final StringBuilder logger;
/*      */     
/*      */ 
/*      */     final ClassInfo classInfo;
/*      */     
/*      */ 
/*      */     final List<Type> context;
/*      */     
/*      */ 
/*      */ 
/*      */     public ParseHeaderState(HttpHeaders headers, StringBuilder logger)
/*      */     {
/* 1113 */       Class<? extends HttpHeaders> clazz = headers.getClass();
/* 1114 */       this.context = Arrays.asList(new Type[] { clazz });
/* 1115 */       this.classInfo = ClassInfo.of(clazz, true);
/* 1116 */       this.logger = logger;
/* 1117 */       this.arrayValueMap = new ArrayValueMap(headers);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     void finish()
/*      */     {
/* 1124 */       this.arrayValueMap.setValues();
/*      */     }
/*      */   }
/*      */   
/*      */   void parseHeader(String headerName, String headerValue, ParseHeaderState state)
/*      */   {
/* 1130 */     List<Type> context = state.context;
/* 1131 */     ClassInfo classInfo = state.classInfo;
/* 1132 */     ArrayValueMap arrayValueMap = state.arrayValueMap;
/* 1133 */     StringBuilder logger = state.logger;
/*      */     
/* 1135 */     if (logger != null) {
/* 1136 */       String str1 = String.valueOf(String.valueOf(headerName));String str2 = String.valueOf(String.valueOf(headerValue));logger.append(2 + str1.length() + str2.length() + str1 + ": " + str2).append(StringUtils.LINE_SEPARATOR);
/*      */     }
/*      */     
/* 1139 */     FieldInfo fieldInfo = classInfo.getFieldInfo(headerName);
/* 1140 */     if (fieldInfo != null) {
/* 1141 */       Type type = Data.resolveWildcardTypeOrTypeVariable(context, fieldInfo.getGenericType());
/*      */       
/* 1143 */       if (Types.isArray(type))
/*      */       {
/* 1145 */         Class<?> rawArrayComponentType = Types.getRawArrayComponentType(context, Types.getArrayComponentType(type));
/*      */         
/* 1147 */         arrayValueMap.put(fieldInfo.getField(), rawArrayComponentType, parseValue(rawArrayComponentType, context, headerValue));
/*      */       }
/* 1149 */       else if (Types.isAssignableToOrFrom(Types.getRawArrayComponentType(context, type), Iterable.class))
/*      */       {
/*      */ 
/*      */ 
/* 1153 */         Collection<Object> collection = (Collection)fieldInfo.getValue(this);
/* 1154 */         if (collection == null) {
/* 1155 */           collection = Data.newCollectionInstance(type);
/* 1156 */           fieldInfo.setValue(this, collection);
/*      */         }
/* 1158 */         Type subFieldType = type == Object.class ? null : Types.getIterableParameter(type);
/* 1159 */         collection.add(parseValue(subFieldType, context, headerValue));
/*      */       }
/*      */       else {
/* 1162 */         fieldInfo.setValue(this, parseValue(type, context, headerValue));
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1167 */       Object listValue = (ArrayList)get(headerName);
/* 1168 */       if (listValue == null) {
/* 1169 */         listValue = new ArrayList();
/* 1170 */         set(headerName, listValue);
/*      */       }
/* 1172 */       ((ArrayList)listValue).add(headerValue);
/*      */     }
/*      */   }
/*      */   
/*      */   private static Object parseValue(Type valueType, List<Type> context, String value) {
/* 1177 */     Type resolved = Data.resolveWildcardTypeOrTypeVariable(context, valueType);
/* 1178 */     return Data.parsePrimitiveValue(resolved, value);
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpHeaders.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */