/*     */ package com.google.api.client.http;
/*     */ 
/*     */ import com.google.api.client.util.Preconditions;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ 
/*     */ public final class HttpMediaType
/*     */ {
/*  54 */   private String type = "application";
/*     */   
/*     */ 
/*  57 */   private String subType = "octet-stream";
/*     */   
/*     */ 
/*  60 */   private final SortedMap<String, String> parameters = new TreeMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private static final Pattern TYPE_REGEX = Pattern.compile("[\\w!#$&.+\\-\\^_]+|[*]");
/*     */   
/*     */ 
/*     */ 
/*  73 */   private static final Pattern TOKEN_REGEX = Pattern.compile("[\\p{ASCII}&&[^\\p{Cntrl} ;/=\\[\\]\\(\\)\\<\\>\\@\\,\\:\\\"\\?\\=]]+");
/*     */   private static final Pattern FULL_MEDIA_TYPE_REGEX;
/*     */   private static final Pattern PARAMETER_REGEX;
/*     */   private String cachedBuildResult;
/*     */   
/*     */   static {
/*  79 */     String typeOrKey = "[^\\s/=;\"]+";
/*  80 */     String wholeParameterSection = ";.*";
/*  81 */     String str1 = String.valueOf(String.valueOf(typeOrKey));String str2 = String.valueOf(String.valueOf(typeOrKey));String str3 = String.valueOf(String.valueOf(wholeParameterSection));FULL_MEDIA_TYPE_REGEX = Pattern.compile(14 + str1.length() + str2.length() + str3.length() + "\\s*(" + str1 + ")/(" + str2 + ")" + "\\s*(" + str3 + ")?", 32);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */     String quotedParameterValue = "\"([^\"]*)\"";
/*  89 */     String unquotedParameterValue = "[^\\s;\"]*";
/*  90 */     String str4 = String.valueOf(String.valueOf(quotedParameterValue));String str5 = String.valueOf(String.valueOf(unquotedParameterValue));String parameterValue = 1 + str4.length() + str5.length() + str4 + "|" + str5;
/*  91 */     String str6 = String.valueOf(String.valueOf(typeOrKey));String str7 = String.valueOf(String.valueOf(parameterValue));PARAMETER_REGEX = Pattern.compile(12 + str6.length() + str7.length() + "\\s*;\\s*(" + str6 + ")" + "=(" + str7 + ")");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpMediaType(String type, String subType)
/*     */   {
/* 101 */     setType(type);
/* 102 */     setSubType(subType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpMediaType(String mediaType)
/*     */   {
/* 111 */     fromString(mediaType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpMediaType setType(String type)
/*     */   {
/* 120 */     Preconditions.checkArgument(TYPE_REGEX.matcher(type).matches(), "Type contains reserved characters");
/*     */     
/* 122 */     this.type = type;
/* 123 */     this.cachedBuildResult = null;
/* 124 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 131 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpMediaType setSubType(String subType)
/*     */   {
/* 140 */     Preconditions.checkArgument(TYPE_REGEX.matcher(subType).matches(), "Subtype contains reserved characters");
/*     */     
/* 142 */     this.subType = subType;
/* 143 */     this.cachedBuildResult = null;
/* 144 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getSubType()
/*     */   {
/* 151 */     return this.subType;
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
/*     */   private HttpMediaType fromString(String combinedType)
/*     */   {
/* 166 */     Matcher matcher = FULL_MEDIA_TYPE_REGEX.matcher(combinedType);
/* 167 */     Preconditions.checkArgument(matcher.matches(), "Type must be in the 'maintype/subtype; parameter=value' format");
/*     */     
/*     */ 
/* 170 */     setType(matcher.group(1));
/* 171 */     setSubType(matcher.group(2));
/* 172 */     String params = matcher.group(3);
/* 173 */     if (params != null) {
/* 174 */       matcher = PARAMETER_REGEX.matcher(params);
/* 175 */       while (matcher.find())
/*     */       {
/* 177 */         String key = matcher.group(1);
/* 178 */         String value = matcher.group(3);
/* 179 */         if (value == null) {
/* 180 */           value = matcher.group(2);
/*     */         }
/* 182 */         setParameter(key, value);
/*     */       }
/*     */     }
/* 185 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpMediaType setParameter(String name, String value)
/*     */   {
/* 195 */     if (value == null) {
/* 196 */       removeParameter(name);
/* 197 */       return this;
/*     */     }
/*     */     
/* 200 */     Preconditions.checkArgument(TOKEN_REGEX.matcher(name).matches(), "Name contains reserved characters");
/*     */     
/* 202 */     this.cachedBuildResult = null;
/* 203 */     this.parameters.put(name.toLowerCase(), value);
/* 204 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getParameter(String name)
/*     */   {
/* 213 */     return (String)this.parameters.get(name.toLowerCase());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpMediaType removeParameter(String name)
/*     */   {
/* 222 */     this.cachedBuildResult = null;
/* 223 */     this.parameters.remove(name.toLowerCase());
/* 224 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void clearParameters()
/*     */   {
/* 231 */     this.cachedBuildResult = null;
/* 232 */     this.parameters.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String> getParameters()
/*     */   {
/* 240 */     return Collections.unmodifiableMap(this.parameters);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static boolean matchesToken(String value)
/*     */   {
/* 248 */     return TOKEN_REGEX.matcher(value).matches();
/*     */   }
/*     */   
/*     */   private static String quoteString(String unquotedString) {
/* 252 */     String escapedString = unquotedString.replace("\\", "\\\\");
/* 253 */     escapedString = escapedString.replace("\"", "\\\"");
/* 254 */     String str1 = String.valueOf(String.valueOf(escapedString));return 2 + str1.length() + "\"" + str1 + "\"";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String build()
/*     */   {
/* 261 */     if (this.cachedBuildResult != null) {
/* 262 */       return this.cachedBuildResult;
/*     */     }
/*     */     
/* 265 */     StringBuilder str = new StringBuilder();
/* 266 */     str.append(this.type);
/* 267 */     str.append('/');
/* 268 */     str.append(this.subType);
/* 269 */     if (this.parameters != null) {
/* 270 */       for (Map.Entry<String, String> entry : this.parameters.entrySet()) {
/* 271 */         String value = (String)entry.getValue();
/* 272 */         str.append("; ");
/* 273 */         str.append((String)entry.getKey());
/* 274 */         str.append("=");
/* 275 */         str.append(!matchesToken(value) ? quoteString(value) : value);
/*     */       }
/*     */     }
/* 278 */     this.cachedBuildResult = str.toString();
/* 279 */     return this.cachedBuildResult;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 284 */     return build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equalsIgnoreParameters(HttpMediaType mediaType)
/*     */   {
/* 292 */     return (mediaType != null) && (getType().equalsIgnoreCase(mediaType.getType())) && (getSubType().equalsIgnoreCase(mediaType.getSubType()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean equalsIgnoreParameters(String mediaTypeA, String mediaTypeB)
/*     */   {
/* 302 */     return ((mediaTypeA == null) && (mediaTypeB == null)) || ((mediaTypeA != null) && (mediaTypeB != null) && (new HttpMediaType(mediaTypeA).equalsIgnoreParameters(new HttpMediaType(mediaTypeB))));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpMediaType setCharsetParameter(Charset charset)
/*     */   {
/* 312 */     setParameter("charset", charset == null ? null : charset.name());
/* 313 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Charset getCharsetParameter()
/*     */   {
/* 320 */     String value = getParameter("charset");
/* 321 */     return value == null ? null : Charset.forName(value);
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 326 */     return build().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 331 */     if (!(obj instanceof HttpMediaType)) {
/* 332 */       return false;
/*     */     }
/*     */     
/* 335 */     HttpMediaType otherType = (HttpMediaType)obj;
/*     */     
/* 337 */     return (equalsIgnoreParameters(otherType)) && (this.parameters.equals(otherType.parameters));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\http\HttpMediaType.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */