/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.io.SessionOutputBuffer;
/*    */ import org.apache.http.message.LineFormatter;
/*    */ import org.apache.http.params.HttpParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpResponseWriter
/*    */   extends AbstractMessageWriter
/*    */ {
/*    */   public HttpResponseWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params)
/*    */   {
/* 53 */     super(buffer, formatter, params);
/*    */   }
/*    */   
/*    */   protected void writeHeadLine(HttpMessage message)
/*    */     throws IOException
/*    */   {
/* 59 */     this.lineFormatter.formatStatusLine(this.lineBuf, ((HttpResponse)message).getStatusLine());
/*    */     
/* 61 */     this.sessionBuffer.writeLine(this.lineBuf);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\io\HttpResponseWriter.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */