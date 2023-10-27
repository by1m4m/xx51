/*    */ package org.eclipse.jetty.http;
/*    */ 
/*    */ import org.eclipse.jetty.util.HostPort;
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
/*    */ public class HostPortHttpField
/*    */   extends HttpField
/*    */ {
/*    */   final HostPort _hostPort;
/*    */   
/*    */   public HostPortHttpField(String authority)
/*    */   {
/* 35 */     this(HttpHeader.HOST, HttpHeader.HOST.asString(), authority);
/*    */   }
/*    */   
/*    */ 
/*    */   protected HostPortHttpField(HttpHeader header, String name, String authority)
/*    */   {
/* 41 */     super(header, name, authority);
/*    */     try
/*    */     {
/* 44 */       this._hostPort = new HostPort(authority);
/*    */     }
/*    */     catch (Exception e)
/*    */     {
/* 48 */       throw new BadMessageException(400, "Bad HostPort", e);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getHost()
/*    */   {
/* 58 */     return this._hostPort.getHost();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getPort()
/*    */   {
/* 67 */     return this._hostPort.getPort();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getPort(int defaultPort)
/*    */   {
/* 77 */     return this._hostPort.getPort(defaultPort);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HostPortHttpField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */