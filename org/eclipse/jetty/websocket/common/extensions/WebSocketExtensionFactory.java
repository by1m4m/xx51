/*    */ package org.eclipse.jetty.websocket.common.extensions;
/*    */ 
/*    */ import org.eclipse.jetty.util.DecoratedObjectFactory;
/*    */ import org.eclipse.jetty.util.StringUtil;
/*    */ import org.eclipse.jetty.websocket.api.WebSocketException;
/*    */ import org.eclipse.jetty.websocket.api.extensions.Extension;
/*    */ import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
/*    */ import org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
/*    */ import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
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
/*    */ public class WebSocketExtensionFactory
/*    */   extends ExtensionFactory
/*    */ {
/*    */   private WebSocketContainerScope container;
/*    */   
/*    */   public WebSocketExtensionFactory(WebSocketContainerScope container)
/*    */   {
/* 35 */     this.container = container;
/*    */   }
/*    */   
/*    */ 
/*    */   public Extension newInstance(ExtensionConfig config)
/*    */   {
/* 41 */     if (config == null)
/*    */     {
/* 43 */       return null;
/*    */     }
/*    */     
/* 46 */     String name = config.getName();
/* 47 */     if (StringUtil.isBlank(name))
/*    */     {
/* 49 */       return null;
/*    */     }
/*    */     
/* 52 */     Class<? extends Extension> extClass = getExtension(name);
/* 53 */     if (extClass == null)
/*    */     {
/* 55 */       return null;
/*    */     }
/*    */     
/*    */     try
/*    */     {
/* 60 */       Extension ext = (Extension)this.container.getObjectFactory().createInstance(extClass);
/* 61 */       if ((ext instanceof AbstractExtension))
/*    */       {
/* 63 */         AbstractExtension aext = (AbstractExtension)ext;
/* 64 */         aext.init(this.container);
/* 65 */         aext.setConfig(config);
/*    */       }
/* 67 */       return ext;
/*    */     }
/*    */     catch (InstantiationException|IllegalAccessException e)
/*    */     {
/* 71 */       throw new WebSocketException("Cannot instantiate extension: " + extClass, e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\extensions\WebSocketExtensionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */