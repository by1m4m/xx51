/*    */ package org.eclipse.jetty.client.util;
/*    */ 
/*    */ import org.eclipse.jetty.client.api.ContentProvider.Typed;
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
/*    */ public abstract class AbstractTypedContentProvider
/*    */   implements ContentProvider.Typed
/*    */ {
/*    */   private final String contentType;
/*    */   
/*    */   protected AbstractTypedContentProvider(String contentType)
/*    */   {
/* 29 */     this.contentType = contentType;
/*    */   }
/*    */   
/*    */ 
/*    */   public String getContentType()
/*    */   {
/* 35 */     return this.contentType;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\util\AbstractTypedContentProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */