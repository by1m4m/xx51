/*    */ package org.eclipse.jetty.websocket.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import org.eclipse.jetty.client.HttpResponse;
/*    */ import org.eclipse.jetty.http.HttpField;
/*    */ import org.eclipse.jetty.http.HttpFields;
/*    */ import org.eclipse.jetty.http.HttpHeader;
/*    */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
/*    */ import org.eclipse.jetty.websocket.common.UpgradeResponseAdapter;
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
/*    */ public class ClientUpgradeResponse
/*    */   extends UpgradeResponseAdapter
/*    */ {
/*    */   private List<ExtensionConfig> extensions;
/*    */   
/*    */   public ClientUpgradeResponse() {}
/*    */   
/*    */   public ClientUpgradeResponse(HttpResponse response)
/*    */   {
/* 43 */     setStatusCode(response.getStatus());
/* 44 */     setStatusReason(response.getReason());
/*    */     
/* 46 */     HttpFields fields = response.getHeaders();
/* 47 */     for (HttpField field : fields)
/*    */     {
/* 49 */       addHeader(field.getName(), field.getValue());
/*    */     }
/*    */     
/* 52 */     HttpField extensionsField = fields.getField(HttpHeader.SEC_WEBSOCKET_EXTENSIONS);
/* 53 */     if (extensionsField != null)
/* 54 */       this.extensions = ExtensionConfig.parseList(extensionsField.getValues());
/* 55 */     setAcceptedSubProtocol(fields.get(HttpHeader.SEC_WEBSOCKET_SUBPROTOCOL));
/*    */   }
/*    */   
/*    */ 
/*    */   public List<ExtensionConfig> getExtensions()
/*    */   {
/* 61 */     return this.extensions;
/*    */   }
/*    */   
/*    */   public void sendForbidden(String message)
/*    */     throws IOException
/*    */   {
/* 67 */     throw new UnsupportedOperationException("Not supported on client implementation");
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\client\ClientUpgradeResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */