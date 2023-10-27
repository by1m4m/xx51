/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ import java.util.List;
/*     */ import org.eclipse.jetty.client.api.ContentResponse;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.client.api.Response;
/*     */ import org.eclipse.jetty.client.api.Response.ResponseListener;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpVersion;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpContentResponse
/*     */   implements ContentResponse
/*     */ {
/*     */   private final Response response;
/*     */   private final byte[] content;
/*     */   private final String mediaType;
/*     */   private final String encoding;
/*     */   
/*     */   public HttpContentResponse(Response response, byte[] content, String mediaType, String encoding)
/*     */   {
/*  41 */     this.response = response;
/*  42 */     this.content = content;
/*  43 */     this.mediaType = mediaType;
/*  44 */     this.encoding = encoding;
/*     */   }
/*     */   
/*     */ 
/*     */   public Request getRequest()
/*     */   {
/*  50 */     return this.response.getRequest();
/*     */   }
/*     */   
/*     */ 
/*     */   public <T extends Response.ResponseListener> List<T> getListeners(Class<T> listenerClass)
/*     */   {
/*  56 */     return this.response.getListeners(listenerClass);
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpVersion getVersion()
/*     */   {
/*  62 */     return this.response.getVersion();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getStatus()
/*     */   {
/*  68 */     return this.response.getStatus();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getReason()
/*     */   {
/*  74 */     return this.response.getReason();
/*     */   }
/*     */   
/*     */ 
/*     */   public HttpFields getHeaders()
/*     */   {
/*  80 */     return this.response.getHeaders();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean abort(Throwable cause)
/*     */   {
/*  86 */     return this.response.abort(cause);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getMediaType()
/*     */   {
/*  92 */     return this.mediaType;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getEncoding()
/*     */   {
/*  98 */     return this.encoding;
/*     */   }
/*     */   
/*     */ 
/*     */   public byte[] getContent()
/*     */   {
/* 104 */     return this.content;
/*     */   }
/*     */   
/*     */ 
/*     */   public String getContentAsString()
/*     */   {
/* 110 */     String encoding = this.encoding;
/* 111 */     if (encoding == null)
/*     */     {
/* 113 */       return new String(getContent(), StandardCharsets.UTF_8);
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 119 */       return new String(getContent(), encoding);
/*     */     }
/*     */     catch (UnsupportedEncodingException e)
/*     */     {
/* 123 */       throw new UnsupportedCharsetException(encoding);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 131 */     return String.format("%s[%s %d %s - %d bytes]", new Object[] {HttpContentResponse.class
/* 132 */       .getSimpleName(), 
/* 133 */       getVersion(), 
/* 134 */       Integer.valueOf(getStatus()), 
/* 135 */       getReason(), 
/* 136 */       Integer.valueOf(getContent().length) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpContentResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */