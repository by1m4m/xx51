/*    */ package org.apache.http.impl.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpMessage;
/*    */ import org.apache.http.HttpRequest;
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
/*    */ public class HttpRequestWriter
/*    */   extends AbstractMessageWriter
/*    */ {
/*    */   public HttpRequestWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params)
/*    */   {
/* 53 */     super(buffer, formatter, params);
/*    */   }
/*    */   
/*    */   protected void writeHeadLine(HttpMessage message)
/*    */     throws IOException
/*    */   {
/* 59 */     this.lineFormatter.formatRequestLine(this.lineBuf, ((HttpRequest)message).getRequestLine());
/*    */     
/* 61 */     this.sessionBuffer.writeLine(this.lineBuf);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\io\HttpRequestWriter.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */