/*    */ package org.eclipse.jetty.client;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.eclipse.jetty.client.api.Request;
/*    */ import org.eclipse.jetty.client.api.Response;
/*    */ import org.eclipse.jetty.http.HttpHeader;
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
/*    */ public class ProxyAuthenticationProtocolHandler
/*    */   extends AuthenticationProtocolHandler
/*    */ {
/*    */   public static final String NAME = "proxy-authenticate";
/* 37 */   private static final String ATTRIBUTE = ProxyAuthenticationProtocolHandler.class.getName() + ".attribute";
/*    */   
/*    */   public ProxyAuthenticationProtocolHandler(HttpClient client)
/*    */   {
/* 41 */     this(client, 16384);
/*    */   }
/*    */   
/*    */   public ProxyAuthenticationProtocolHandler(HttpClient client, int maxContentLength)
/*    */   {
/* 46 */     super(client, maxContentLength);
/*    */   }
/*    */   
/*    */ 
/*    */   public String getName()
/*    */   {
/* 52 */     return "proxy-authenticate";
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean accept(Request request, Response response)
/*    */   {
/* 58 */     return response.getStatus() == 407;
/*    */   }
/*    */   
/*    */ 
/*    */   protected HttpHeader getAuthenticateHeader()
/*    */   {
/* 64 */     return HttpHeader.PROXY_AUTHENTICATE;
/*    */   }
/*    */   
/*    */ 
/*    */   protected HttpHeader getAuthorizationHeader()
/*    */   {
/* 70 */     return HttpHeader.PROXY_AUTHORIZATION;
/*    */   }
/*    */   
/*    */ 
/*    */   protected URI getAuthenticationURI(Request request)
/*    */   {
/* 76 */     HttpDestination destination = getHttpClient().destinationFor(request.getScheme(), request.getHost(), request.getPort());
/* 77 */     ProxyConfiguration.Proxy proxy = destination.getProxy();
/* 78 */     return proxy != null ? proxy.getURI() : request.getURI();
/*    */   }
/*    */   
/*    */ 
/*    */   protected String getAuthenticationAttribute()
/*    */   {
/* 84 */     return ATTRIBUTE;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\ProxyAuthenticationProtocolHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */