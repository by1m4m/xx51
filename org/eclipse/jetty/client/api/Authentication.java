/*     */ package org.eclipse.jetty.client.api;
/*     */ 
/*     */ import java.net.URI;
/*     */ import org.eclipse.jetty.http.HttpHeader;
/*     */ import org.eclipse.jetty.util.Attributes;
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
/*     */ public abstract interface Authentication
/*     */ {
/*     */   public static final String ANY_REALM = "<<ANY_REALM>>";
/*     */   
/*     */   public abstract boolean matches(String paramString1, URI paramURI, String paramString2);
/*     */   
/*     */   public abstract Result authenticate(Request paramRequest, ContentResponse paramContentResponse, HeaderInfo paramHeaderInfo, Attributes paramAttributes);
/*     */   
/*     */   public static abstract interface Result
/*     */   {
/*     */     public abstract URI getURI();
/*     */     
/*     */     public abstract void apply(Request paramRequest);
/*     */   }
/*     */   
/*     */   public static class HeaderInfo
/*     */   {
/*     */     private final String type;
/*     */     private final String realm;
/*     */     private final String params;
/*     */     private final HttpHeader header;
/*     */     
/*     */     public HeaderInfo(String type, String realm, String params, HttpHeader header)
/*     */     {
/*  86 */       this.type = type;
/*  87 */       this.realm = realm;
/*  88 */       this.params = params;
/*  89 */       this.header = header;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getType()
/*     */     {
/*  97 */       return this.type;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getRealm()
/*     */     {
/* 105 */       return this.realm;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getParameters()
/*     */     {
/* 113 */       return this.params;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public HttpHeader getHeader()
/*     */     {
/* 121 */       return this.header;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\api\Authentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */