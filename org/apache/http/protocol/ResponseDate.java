/*    */ package org.apache.http.protocol;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.apache.http.HttpException;
/*    */ import org.apache.http.HttpResponse;
/*    */ import org.apache.http.HttpResponseInterceptor;
/*    */ import org.apache.http.StatusLine;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResponseDate
/*    */   implements HttpResponseInterceptor
/*    */ {
/* 53 */   private static final HttpDateGenerator DATE_GENERATOR = new HttpDateGenerator();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void process(HttpResponse response, HttpContext context)
/*    */     throws HttpException, IOException
/*    */   {
/* 61 */     if (response == null) {
/* 62 */       throw new IllegalArgumentException("HTTP response may not be null.");
/*    */     }
/*    */     
/* 65 */     int status = response.getStatusLine().getStatusCode();
/* 66 */     if ((status >= 200) && (!response.containsHeader("Date")))
/*    */     {
/* 68 */       String httpdate = DATE_GENERATOR.getCurrentDate();
/* 69 */       response.setHeader("Date", httpdate);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\protocol\ResponseDate.class
 * Java compiler version: 3 (47.0)
 * JD-Core Version:       0.7.1
 */