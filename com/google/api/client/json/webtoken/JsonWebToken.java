/*     */ package com.google.api.client.json.webtoken;
/*     */ 
/*     */ import com.google.api.client.json.GenericJson;
/*     */ import com.google.api.client.util.Key;
/*     */ import com.google.api.client.util.Objects;
/*     */ import com.google.api.client.util.Objects.ToStringHelper;
/*     */ import com.google.api.client.util.Preconditions;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonWebToken
/*     */ {
/*     */   private final Header header;
/*     */   private final Payload payload;
/*     */   
/*     */   public JsonWebToken(Header header, Payload payload)
/*     */   {
/*  48 */     this.header = ((Header)Preconditions.checkNotNull(header));
/*  49 */     this.payload = ((Payload)Preconditions.checkNotNull(payload));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Header
/*     */     extends GenericJson
/*     */   {
/*     */     @Key("typ")
/*     */     private String type;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("cty")
/*     */     private String contentType;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getType()
/*     */     {
/*  74 */       return this.type;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Header setType(String type)
/*     */     {
/*  87 */       this.type = type;
/*  88 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getContentType()
/*     */     {
/*  96 */       return this.contentType;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Header setContentType(String contentType)
/*     */     {
/* 109 */       this.contentType = contentType;
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public Header set(String fieldName, Object value)
/*     */     {
/* 115 */       return (Header)super.set(fieldName, value);
/*     */     }
/*     */     
/*     */     public Header clone()
/*     */     {
/* 120 */       return (Header)super.clone();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Payload
/*     */     extends GenericJson
/*     */   {
/*     */     @Key("exp")
/*     */     private Long expirationTimeSeconds;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("nbf")
/*     */     private Long notBeforeTimeSeconds;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("iat")
/*     */     private Long issuedAtTimeSeconds;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("iss")
/*     */     private String issuer;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("aud")
/*     */     private Object audience;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("jti")
/*     */     private String jwtId;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("typ")
/*     */     private String type;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @Key("sub")
/*     */     private String subject;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final Long getExpirationTimeSeconds()
/*     */     {
/* 188 */       return this.expirationTimeSeconds;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Payload setExpirationTimeSeconds(Long expirationTimeSeconds)
/*     */     {
/* 201 */       this.expirationTimeSeconds = expirationTimeSeconds;
/* 202 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final Long getNotBeforeTimeSeconds()
/*     */     {
/* 210 */       return this.notBeforeTimeSeconds;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Payload setNotBeforeTimeSeconds(Long notBeforeTimeSeconds)
/*     */     {
/* 223 */       this.notBeforeTimeSeconds = notBeforeTimeSeconds;
/* 224 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final Long getIssuedAtTimeSeconds()
/*     */     {
/* 232 */       return this.issuedAtTimeSeconds;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Payload setIssuedAtTimeSeconds(Long issuedAtTimeSeconds)
/*     */     {
/* 245 */       this.issuedAtTimeSeconds = issuedAtTimeSeconds;
/* 246 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getIssuer()
/*     */     {
/* 254 */       return this.issuer;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Payload setIssuer(String issuer)
/*     */     {
/* 267 */       this.issuer = issuer;
/* 268 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final Object getAudience()
/*     */     {
/* 276 */       return this.audience;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public final List<String> getAudienceAsList()
/*     */     {
/* 285 */       if (this.audience == null) {
/* 286 */         return Collections.emptyList();
/*     */       }
/* 288 */       if ((this.audience instanceof String)) {
/* 289 */         return Collections.singletonList((String)this.audience);
/*     */       }
/* 291 */       return (List)this.audience;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Payload setAudience(Object audience)
/*     */     {
/* 304 */       this.audience = audience;
/* 305 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getJwtId()
/*     */     {
/* 313 */       return this.jwtId;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Payload setJwtId(String jwtId)
/*     */     {
/* 325 */       this.jwtId = jwtId;
/* 326 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getType()
/*     */     {
/* 334 */       return this.type;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Payload setType(String type)
/*     */     {
/* 347 */       this.type = type;
/* 348 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public final String getSubject()
/*     */     {
/* 356 */       return this.subject;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Payload setSubject(String subject)
/*     */     {
/* 369 */       this.subject = subject;
/* 370 */       return this;
/*     */     }
/*     */     
/*     */     public Payload set(String fieldName, Object value)
/*     */     {
/* 375 */       return (Payload)super.set(fieldName, value);
/*     */     }
/*     */     
/*     */     public Payload clone()
/*     */     {
/* 380 */       return (Payload)super.clone();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 386 */     return Objects.toStringHelper(this).add("header", this.header).add("payload", this.payload).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Header getHeader()
/*     */   {
/* 398 */     return this.header;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Payload getPayload()
/*     */   {
/* 410 */     return this.payload;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\json\webtoken\JsonWebToken.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */