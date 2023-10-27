/*     */ package org.apache.http.client.methods;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import org.apache.http.ProtocolVersion;
/*     */ import org.apache.http.RequestLine;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.client.utils.CloneUtils;
/*     */ import org.apache.http.conn.ClientConnectionRequest;
/*     */ import org.apache.http.conn.ConnectionReleaseTrigger;
/*     */ import org.apache.http.message.AbstractHttpMessage;
/*     */ import org.apache.http.message.BasicRequestLine;
/*     */ import org.apache.http.message.HeaderGroup;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.params.HttpProtocolParams;
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
/*     */ @NotThreadSafe
/*     */ public abstract class HttpRequestBase
/*     */   extends AbstractHttpMessage
/*     */   implements HttpUriRequest, AbortableHttpRequest, Cloneable
/*     */ {
/*     */   private Lock abortLock;
/*     */   private boolean aborted;
/*     */   private URI uri;
/*     */   private ClientConnectionRequest connRequest;
/*     */   private ConnectionReleaseTrigger releaseTrigger;
/*     */   
/*     */   public HttpRequestBase()
/*     */   {
/*  68 */     this.abortLock = new ReentrantLock();
/*     */   }
/*     */   
/*     */   public abstract String getMethod();
/*     */   
/*     */   public ProtocolVersion getProtocolVersion() {
/*  74 */     return HttpProtocolParams.getVersion(getParams());
/*     */   }
/*     */   
/*     */   public URI getURI() {
/*  78 */     return this.uri;
/*     */   }
/*     */   
/*     */   public RequestLine getRequestLine() {
/*  82 */     String method = getMethod();
/*  83 */     ProtocolVersion ver = getProtocolVersion();
/*  84 */     URI uri = getURI();
/*  85 */     String uritext = null;
/*  86 */     if (uri != null) {
/*  87 */       uritext = uri.toASCIIString();
/*     */     }
/*  89 */     if ((uritext == null) || (uritext.length() == 0)) {
/*  90 */       uritext = "/";
/*     */     }
/*  92 */     return new BasicRequestLine(method, uritext, ver);
/*     */   }
/*     */   
/*     */   public void setURI(URI uri) {
/*  96 */     this.uri = uri;
/*     */   }
/*     */   
/*     */   public void setConnectionRequest(ClientConnectionRequest connRequest) throws IOException
/*     */   {
/* 101 */     this.abortLock.lock();
/*     */     try {
/* 103 */       if (this.aborted) {
/* 104 */         throw new IOException("Request already aborted");
/*     */       }
/*     */       
/* 107 */       this.releaseTrigger = null;
/* 108 */       this.connRequest = connRequest;
/*     */     } finally {
/* 110 */       this.abortLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setReleaseTrigger(ConnectionReleaseTrigger releaseTrigger) throws IOException
/*     */   {
/* 116 */     this.abortLock.lock();
/*     */     try {
/* 118 */       if (this.aborted) {
/* 119 */         throw new IOException("Request already aborted");
/*     */       }
/*     */       
/* 122 */       this.connRequest = null;
/* 123 */       this.releaseTrigger = releaseTrigger;
/*     */     } finally {
/* 125 */       this.abortLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void abort()
/*     */   {
/* 133 */     this.abortLock.lock();
/*     */     ClientConnectionRequest localRequest;
/* 135 */     ConnectionReleaseTrigger localTrigger; try { if (this.aborted) {
/*     */         return;
/*     */       }
/* 138 */       this.aborted = true;
/*     */       
/* 140 */       localRequest = this.connRequest;
/* 141 */       localTrigger = this.releaseTrigger;
/*     */     } finally {
/* 143 */       this.abortLock.unlock();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */     if (localRequest != null) {
/* 151 */       localRequest.abortRequest();
/*     */     }
/* 153 */     if (localTrigger != null) {
/*     */       try {
/* 155 */         localTrigger.abortConnection();
/*     */       }
/*     */       catch (IOException ex) {}
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isAborted()
/*     */   {
/* 163 */     return this.aborted;
/*     */   }
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException
/*     */   {
/* 168 */     HttpRequestBase clone = (HttpRequestBase)super.clone();
/* 169 */     clone.abortLock = new ReentrantLock();
/* 170 */     clone.aborted = false;
/* 171 */     clone.releaseTrigger = null;
/* 172 */     clone.connRequest = null;
/* 173 */     clone.headergroup = ((HeaderGroup)CloneUtils.clone(this.headergroup));
/* 174 */     clone.params = ((HttpParams)CloneUtils.clone(this.params));
/* 175 */     return clone;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\client\methods\HttpRequestBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */