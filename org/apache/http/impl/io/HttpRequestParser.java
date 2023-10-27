/*     */ package org.apache.http.impl.io;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.http.ConnectionClosedException;
/*     */ import org.apache.http.HttpException;
/*     */ import org.apache.http.HttpMessage;
/*     */ import org.apache.http.HttpRequestFactory;
/*     */ import org.apache.http.ParseException;
/*     */ import org.apache.http.RequestLine;
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
/*     */ public class HttpRequestParser
/*     */   extends AbstractMessageParser
/*     */ {
/*     */   private final HttpRequestFactory requestFactory;
/*     */   private final CharArrayBuffer lineBuf;
/*     */   
/*     */   public HttpRequestParser(SessionInputBuffer buffer, LineParser parser, HttpRequestFactory requestFactory, HttpParams params)
/*     */   {
/*  89 */     super(buffer, parser, params);
/*  90 */     if (requestFactory == null) {
/*  91 */       throw new IllegalArgumentException("Request factory may not be null");
/*     */     }
/*  93 */     this.requestFactory = requestFactory;
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
/* 104 */       throw new ConnectionClosedException("Client closed connection");
/*     */     }
/* 106 */     ParserCursor cursor = new ParserCursor(0, this.lineBuf.length());
/* 107 */     RequestLine requestline = this.lineParser.parseRequestLine(this.lineBuf, cursor);
/* 108 */     return this.requestFactory.newHttpRequest(requestline);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\io\HttpRequestParser.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */