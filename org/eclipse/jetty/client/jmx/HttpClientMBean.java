/*    */ package org.eclipse.jetty.client.jmx;
/*    */ 
/*    */ import org.eclipse.jetty.client.HttpClient;
/*    */ import org.eclipse.jetty.jmx.ObjectMBean;
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
/*    */ public class HttpClientMBean
/*    */   extends ObjectMBean
/*    */ {
/*    */   public HttpClientMBean(Object managedObject)
/*    */   {
/* 28 */     super(managedObject);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getObjectContextBasis()
/*    */   {
/* 37 */     HttpClient httpClient = (HttpClient)getManagedObject();
/* 38 */     return httpClient.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\jmx\HttpClientMBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */