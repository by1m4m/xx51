/*    */ package org.eclipse.jetty.websocket.api.extensions;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.ServiceLoader;
/*    */ import java.util.Set;
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
/*    */ public abstract class ExtensionFactory
/*    */   implements Iterable<Class<? extends Extension>>
/*    */ {
/* 29 */   private ServiceLoader<Extension> extensionLoader = ServiceLoader.load(Extension.class);
/*    */   private Map<String, Class<? extends Extension>> availableExtensions;
/*    */   
/*    */   public ExtensionFactory()
/*    */   {
/* 34 */     this.availableExtensions = new HashMap();
/* 35 */     for (Extension ext : this.extensionLoader)
/*    */     {
/* 37 */       if (ext != null)
/*    */       {
/* 39 */         this.availableExtensions.put(ext.getName(), ext.getClass());
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public Map<String, Class<? extends Extension>> getAvailableExtensions()
/*    */   {
/* 46 */     return this.availableExtensions;
/*    */   }
/*    */   
/*    */   public Class<? extends Extension> getExtension(String name)
/*    */   {
/* 51 */     return (Class)this.availableExtensions.get(name);
/*    */   }
/*    */   
/*    */   public Set<String> getExtensionNames()
/*    */   {
/* 56 */     return this.availableExtensions.keySet();
/*    */   }
/*    */   
/*    */   public boolean isAvailable(String name)
/*    */   {
/* 61 */     return this.availableExtensions.containsKey(name);
/*    */   }
/*    */   
/*    */ 
/*    */   public Iterator<Class<? extends Extension>> iterator()
/*    */   {
/* 67 */     return this.availableExtensions.values().iterator();
/*    */   }
/*    */   
/*    */   public abstract Extension newInstance(ExtensionConfig paramExtensionConfig);
/*    */   
/*    */   public void register(String name, Class<? extends Extension> extension)
/*    */   {
/* 74 */     this.availableExtensions.put(name, extension);
/*    */   }
/*    */   
/*    */   public void unregister(String name)
/*    */   {
/* 79 */     this.availableExtensions.remove(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\extensions\ExtensionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */