/*     */ package org.eclipse.jetty.client.util;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.eclipse.jetty.client.api.Authentication.HeaderInfo;
/*     */ import org.eclipse.jetty.client.api.Authentication.Result;
/*     */ import org.eclipse.jetty.client.api.ContentResponse;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.http.HttpHeader;
/*     */ import org.eclipse.jetty.util.Attributes;
/*     */ import org.eclipse.jetty.util.B64Code;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicAuthentication
/*     */   extends AbstractAuthentication
/*     */ {
/*     */   private final String user;
/*     */   private final String password;
/*     */   
/*     */   public BasicAuthentication(URI uri, String realm, String user, String password)
/*     */   {
/*  52 */     super(uri, realm);
/*  53 */     this.user = user;
/*  54 */     this.password = password;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getType()
/*     */   {
/*  60 */     return "Basic";
/*     */   }
/*     */   
/*     */ 
/*     */   public Authentication.Result authenticate(Request request, ContentResponse response, Authentication.HeaderInfo headerInfo, Attributes context)
/*     */   {
/*  66 */     return new BasicResult(getURI(), headerInfo.getHeader(), this.user, this.password);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class BasicResult
/*     */     implements Authentication.Result
/*     */   {
/*     */     private final URI uri;
/*     */     
/*     */ 
/*     */     private final HttpHeader header;
/*     */     
/*     */ 
/*     */     private final String value;
/*     */     
/*     */ 
/*     */ 
/*     */     public BasicResult(URI uri, String user, String password)
/*     */     {
/*  87 */       this(uri, HttpHeader.AUTHORIZATION, user, password);
/*     */     }
/*     */     
/*     */     public BasicResult(URI uri, HttpHeader header, String user, String password)
/*     */     {
/*  92 */       this.uri = uri;
/*  93 */       this.header = header;
/*  94 */       this.value = ("Basic " + B64Code.encode(new StringBuilder().append(user).append(":").append(password).toString(), StandardCharsets.ISO_8859_1));
/*     */     }
/*     */     
/*     */ 
/*     */     public URI getURI()
/*     */     {
/* 100 */       return this.uri;
/*     */     }
/*     */     
/*     */ 
/*     */     public void apply(Request request)
/*     */     {
/* 106 */       request.header(this.header, this.value);
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 112 */       return String.format("Basic authentication result for %s", new Object[] { getURI() });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\util\BasicAuthentication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */