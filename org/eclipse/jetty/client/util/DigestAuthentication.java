/*     */ package org.eclipse.jetty.client.util;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.eclipse.jetty.client.api.Authentication.HeaderInfo;
/*     */ import org.eclipse.jetty.client.api.Authentication.Result;
/*     */ import org.eclipse.jetty.client.api.ContentResponse;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.http.HttpHeader;
/*     */ import org.eclipse.jetty.util.Attributes;
/*     */ import org.eclipse.jetty.util.StringUtil;
/*     */ import org.eclipse.jetty.util.TypeUtil;
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
/*     */ public class DigestAuthentication
/*     */   extends AbstractAuthentication
/*     */ {
/*  53 */   private static final Pattern PARAM_PATTERN = Pattern.compile("([^=]+)=(.*)");
/*     */   
/*     */ 
/*     */ 
/*     */   private final String user;
/*     */   
/*     */ 
/*     */   private final String password;
/*     */   
/*     */ 
/*     */ 
/*     */   public DigestAuthentication(URI uri, String realm, String user, String password)
/*     */   {
/*  66 */     super(uri, realm);
/*  67 */     this.user = user;
/*  68 */     this.password = password;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getType()
/*     */   {
/*  74 */     return "Digest";
/*     */   }
/*     */   
/*     */ 
/*     */   public Authentication.Result authenticate(Request request, ContentResponse response, Authentication.HeaderInfo headerInfo, Attributes context)
/*     */   {
/*  80 */     Map<String, String> params = parseParameters(headerInfo.getParameters());
/*  81 */     String nonce = (String)params.get("nonce");
/*  82 */     if ((nonce == null) || (nonce.length() == 0))
/*  83 */       return null;
/*  84 */     String opaque = (String)params.get("opaque");
/*  85 */     String algorithm = (String)params.get("algorithm");
/*  86 */     if (algorithm == null)
/*  87 */       algorithm = "MD5";
/*  88 */     MessageDigest digester = getMessageDigest(algorithm);
/*  89 */     if (digester == null)
/*  90 */       return null;
/*  91 */     String serverQOP = (String)params.get("qop");
/*  92 */     String clientQOP = null;
/*  93 */     if (serverQOP != null)
/*     */     {
/*  95 */       List<String> serverQOPValues = StringUtil.csvSplit(null, serverQOP, 0, serverQOP.length());
/*  96 */       if (serverQOPValues.contains("auth")) {
/*  97 */         clientQOP = "auth";
/*  98 */       } else if (serverQOPValues.contains("auth-int")) {
/*  99 */         clientQOP = "auth-int";
/*     */       }
/*     */     }
/* 102 */     String realm = getRealm();
/* 103 */     if ("<<ANY_REALM>>".equals(realm))
/* 104 */       realm = headerInfo.getRealm();
/* 105 */     return new DigestResult(headerInfo.getHeader(), response.getContent(), realm, this.user, this.password, algorithm, nonce, clientQOP, opaque);
/*     */   }
/*     */   
/*     */   private Map<String, String> parseParameters(String wwwAuthenticate)
/*     */   {
/* 110 */     Map<String, String> result = new HashMap();
/* 111 */     List<String> parts = splitParams(wwwAuthenticate);
/* 112 */     for (String part : parts)
/*     */     {
/* 114 */       Matcher matcher = PARAM_PATTERN.matcher(part);
/* 115 */       if (matcher.matches())
/*     */       {
/* 117 */         String name = matcher.group(1).trim().toLowerCase(Locale.ENGLISH);
/* 118 */         String value = matcher.group(2).trim();
/* 119 */         if ((value.startsWith("\"")) && (value.endsWith("\"")))
/* 120 */           value = value.substring(1, value.length() - 1);
/* 121 */         result.put(name, value);
/*     */       }
/*     */     }
/* 124 */     return result;
/*     */   }
/*     */   
/*     */   private List<String> splitParams(String paramString)
/*     */   {
/* 129 */     List<String> result = new ArrayList();
/* 130 */     int start = 0;
/* 131 */     for (int i = 0; i < paramString.length(); i++)
/*     */     {
/* 133 */       int quotes = 0;
/* 134 */       char ch = paramString.charAt(i);
/* 135 */       switch (ch)
/*     */       {
/*     */       case '\\': 
/* 138 */         i++;
/* 139 */         break;
/*     */       case '"': 
/* 141 */         quotes++;
/* 142 */         break;
/*     */       case ',': 
/* 144 */         if (quotes % 2 == 0)
/*     */         {
/* 146 */           String element = paramString.substring(start, i).trim();
/* 147 */           if (element.length() > 0)
/* 148 */             result.add(element);
/* 149 */           start = i + 1;
/*     */         }
/*     */         
/*     */         break;
/*     */       }
/*     */       
/*     */     }
/* 156 */     result.add(paramString.substring(start, paramString.length()).trim());
/* 157 */     return result;
/*     */   }
/*     */   
/*     */   private MessageDigest getMessageDigest(String algorithm)
/*     */   {
/*     */     try
/*     */     {
/* 164 */       return MessageDigest.getInstance(algorithm);
/*     */     }
/*     */     catch (NoSuchAlgorithmException x) {}
/*     */     
/* 168 */     return null;
/*     */   }
/*     */   
/*     */   private class DigestResult
/*     */     implements Authentication.Result
/*     */   {
/* 174 */     private final AtomicInteger nonceCount = new AtomicInteger();
/*     */     private final HttpHeader header;
/*     */     private final byte[] content;
/*     */     private final String realm;
/*     */     private final String user;
/*     */     private final String password;
/*     */     private final String algorithm;
/*     */     private final String nonce;
/*     */     private final String qop;
/*     */     private final String opaque;
/*     */     
/*     */     public DigestResult(HttpHeader header, byte[] content, String realm, String user, String password, String algorithm, String nonce, String qop, String opaque)
/*     */     {
/* 187 */       this.header = header;
/* 188 */       this.content = content;
/* 189 */       this.realm = realm;
/* 190 */       this.user = user;
/* 191 */       this.password = password;
/* 192 */       this.algorithm = algorithm;
/* 193 */       this.nonce = nonce;
/* 194 */       this.qop = qop;
/* 195 */       this.opaque = opaque;
/*     */     }
/*     */     
/*     */ 
/*     */     public URI getURI()
/*     */     {
/* 201 */       return DigestAuthentication.this.getURI();
/*     */     }
/*     */     
/*     */ 
/*     */     public void apply(Request request)
/*     */     {
/* 207 */       MessageDigest digester = DigestAuthentication.this.getMessageDigest(this.algorithm);
/* 208 */       if (digester == null) {
/* 209 */         return;
/*     */       }
/* 211 */       String A1 = this.user + ":" + this.realm + ":" + this.password;
/* 212 */       String hashA1 = toHexString(digester.digest(A1.getBytes(StandardCharsets.ISO_8859_1)));
/*     */       
/* 214 */       String query = request.getQuery();
/* 215 */       String path = request.getPath();
/* 216 */       String uri = path + "?" + query;
/* 217 */       String A2 = request.getMethod() + ":" + uri;
/* 218 */       if ("auth-int".equals(this.qop))
/* 219 */         A2 = A2 + ":" + toHexString(digester.digest(this.content));
/* 220 */       String hashA2 = toHexString(digester.digest(A2.getBytes(StandardCharsets.ISO_8859_1)));
/*     */       String A3;
/*     */       String nonceCount;
/*     */       String clientNonce;
/*     */       String A3;
/* 225 */       if (this.qop != null)
/*     */       {
/* 227 */         String nonceCount = nextNonceCount();
/* 228 */         String clientNonce = newClientNonce();
/* 229 */         A3 = hashA1 + ":" + this.nonce + ":" + nonceCount + ":" + clientNonce + ":" + this.qop + ":" + hashA2;
/*     */       }
/*     */       else
/*     */       {
/* 233 */         nonceCount = null;
/* 234 */         clientNonce = null;
/* 235 */         A3 = hashA1 + ":" + this.nonce + ":" + hashA2;
/*     */       }
/* 237 */       String hashA3 = toHexString(digester.digest(A3.getBytes(StandardCharsets.ISO_8859_1)));
/*     */       
/* 239 */       StringBuilder value = new StringBuilder("Digest");
/* 240 */       value.append(" username=\"").append(this.user).append("\"");
/* 241 */       value.append(", realm=\"").append(this.realm).append("\"");
/* 242 */       value.append(", nonce=\"").append(this.nonce).append("\"");
/* 243 */       if (this.opaque != null)
/* 244 */         value.append(", opaque=\"").append(this.opaque).append("\"");
/* 245 */       value.append(", algorithm=\"").append(this.algorithm).append("\"");
/* 246 */       value.append(", uri=\"").append(uri).append("\"");
/* 247 */       if (this.qop != null)
/*     */       {
/* 249 */         value.append(", qop=\"").append(this.qop).append("\"");
/* 250 */         value.append(", nc=\"").append(nonceCount).append("\"");
/* 251 */         value.append(", cnonce=\"").append(clientNonce).append("\"");
/*     */       }
/* 253 */       value.append(", response=\"").append(hashA3).append("\"");
/*     */       
/* 255 */       request.header(this.header, value.toString());
/*     */     }
/*     */     
/*     */     private String nextNonceCount()
/*     */     {
/* 260 */       String padding = "00000000";
/* 261 */       String next = Integer.toHexString(this.nonceCount.incrementAndGet()).toLowerCase(Locale.ENGLISH);
/* 262 */       return padding.substring(0, padding.length() - next.length()) + next;
/*     */     }
/*     */     
/*     */     private String newClientNonce()
/*     */     {
/* 267 */       Random random = new Random();
/* 268 */       byte[] bytes = new byte[8];
/* 269 */       random.nextBytes(bytes);
/* 270 */       return toHexString(bytes);
/*     */     }
/*     */     
/*     */     private String toHexString(byte[] bytes)
/*     */     {
/* 275 */       return TypeUtil.toHexString(bytes).toLowerCase(Locale.ENGLISH);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\util\DigestAuthentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */