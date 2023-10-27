/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpResponseFactory;
/*     */ import org.apache.http.NoHttpResponseException;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.StatusLine;
/*     */ import org.apache.http.io.SessionInputBuffer;
/*     */ import org.apache.http.message.LineParser;
/*     */ import org.apache.http.message.ParserCursor;
/*     */ import org.apache.http.params.HttpParams;
/*     */ import org.apache.http.util.CharArrayBuffer;
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
/*     */ public class HttpResponseParser
/*     */   extends AbstractMessageParser
/*     */ {
/*     */   private final HttpResponseFactory responseFactory;
/*     */   private final CharArrayBuffer lineBuf;
/*     */   
/*     */   public HttpResponseParser(SessionInputBuffer buffer, LineParser parser, HttpResponseFactory responseFactory, HttpParams params)
/*     */   {
/*  89 */     super(buffer, parser, params);
/*  90 */     if (responseFactory == null) {
/*  91 */       throw new IllegalArgumentException("Response factory may not be null");
/*     */     }
/*  93 */     this.responseFactory = responseFactory;
/*  94 */     this.lineBuf = new CharArrayBuffer(128);
/*     */   }
/*     */   
/*     */ 
/*     */   protected HttpMessage parseHead(SessionInputBuffer sessionBuffer)
/*     */     throws IOException, HttpException, ParseException
/*     */   {
/* 101 */     this.lineBuf.clear();
/* 102 */     int i = sessionBuffer.readLine(this.lineBuf);
/* 103 */     if (i == -1) {
/* 104 */       throw new NoHttpResponseException("The target server failed to respond");
/*     */     }
/*     */     
/* 107 */     ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
/* 108 */     StatusLine statusline = this.lineParser.parseStatusLine(this.lineBuf, cursor);
/* 109 */     return this.responseFactory.newHttpResponse(statusline, null);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\io\HttpResponseParser.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */