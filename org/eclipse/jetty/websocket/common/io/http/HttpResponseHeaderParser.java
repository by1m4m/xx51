/*     */ package org.eclipse.jetty.websocket.common.io.http;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.StringUtil;
/*     */ import org.eclipse.jetty.util.Utf8LineParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpResponseHeaderParser
/*     */ {
/*     */   public static class ParseException
/*     */     extends RuntimeException
/*     */   {
/*     */     public ParseException(String message)
/*     */     {
/*  39 */       super();
/*     */     }
/*     */     
/*     */     public ParseException(String message, Throwable cause)
/*     */     {
/*  44 */       super(cause);
/*     */     }
/*     */   }
/*     */   
/*     */   private static enum State
/*     */   {
/*  50 */     STATUS_LINE, 
/*  51 */     HEADER, 
/*  52 */     END;
/*     */     
/*     */     private State() {} }
/*  55 */   private static final Pattern PAT_HEADER = Pattern.compile("([^:]+):\\s*(.*)");
/*  56 */   private static final Pattern PAT_STATUS_LINE = Pattern.compile("^HTTP/1.[01]\\s+(\\d+)\\s+(.*)", 2);
/*     */   
/*     */   private final HttpResponseHeaderParseListener listener;
/*     */   private final Utf8LineParser lineParser;
/*     */   private State state;
/*     */   
/*     */   public HttpResponseHeaderParser(HttpResponseHeaderParseListener listener)
/*     */   {
/*  64 */     this.listener = listener;
/*  65 */     this.lineParser = new Utf8LineParser();
/*  66 */     this.state = State.STATUS_LINE;
/*     */   }
/*     */   
/*     */   public boolean isDone()
/*     */   {
/*  71 */     return this.state == State.END;
/*     */   }
/*     */   
/*     */   public HttpResponseHeaderParseListener parse(ByteBuffer buf) throws HttpResponseHeaderParser.ParseException
/*     */   {
/*  76 */     while ((!isDone()) && (buf.remaining() > 0))
/*     */     {
/*  78 */       String line = this.lineParser.parse(buf);
/*  79 */       if (line != null)
/*     */       {
/*  81 */         if (parseHeader(line))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*  86 */           ByteBuffer copy = ByteBuffer.allocate(buf.remaining());
/*  87 */           BufferUtil.put(buf, copy);
/*  88 */           BufferUtil.flipToFlush(copy, 0);
/*  89 */           this.listener.setRemainingBuffer(copy);
/*  90 */           return this.listener;
/*     */         }
/*     */       }
/*     */     }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   private boolean parseHeader(String line) throws HttpResponseHeaderParser.ParseException
/*     */   {
/*  99 */     switch (this.state)
/*     */     {
/*     */ 
/*     */     case STATUS_LINE: 
/* 103 */       Matcher mat = PAT_STATUS_LINE.matcher(line);
/* 104 */       if (!mat.matches())
/*     */       {
/* 106 */         throw new ParseException("Unexpected HTTP response status line [" + line + "]");
/*     */       }
/*     */       
/*     */       try
/*     */       {
/* 111 */         this.listener.setStatusCode(Integer.parseInt(mat.group(1)));
/*     */       }
/*     */       catch (NumberFormatException e)
/*     */       {
/* 115 */         throw new ParseException("Unexpected HTTP response status code", e);
/*     */       }
/* 117 */       this.listener.setStatusReason(mat.group(2));
/* 118 */       this.state = State.HEADER;
/* 119 */       break;
/*     */     
/*     */ 
/*     */     case HEADER: 
/* 123 */       if (StringUtil.isBlank(line))
/*     */       {
/* 125 */         this.state = State.END;
/* 126 */         return parseHeader(line);
/*     */       }
/*     */       
/* 129 */       Matcher header = PAT_HEADER.matcher(line);
/* 130 */       if (header.matches())
/*     */       {
/* 132 */         String headerName = header.group(1);
/* 133 */         String headerValue = header.group(2);
/*     */         
/* 135 */         this.listener.addHeader(headerName, headerValue); }
/* 136 */       break;
/*     */     
/*     */ 
/*     */     case END: 
/* 140 */       this.state = State.STATUS_LINE;
/* 141 */       return true; }
/*     */     
/* 143 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\io\http\HttpResponseHeaderParser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */