/*     */ package org.apache.http.impl.cookie;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.cookie.CookieAttributeHandler;
/*     */ import org.apache.http.cookie.CookieSpec;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public abstract class AbstractCookieSpec
/*     */   implements CookieSpec
/*     */ {
/*     */   private final Map<String, CookieAttributeHandler> attribHandlerMap;
/*     */   
/*     */   public AbstractCookieSpec()
/*     */   {
/*  60 */     this.attribHandlerMap = new HashMap(10);
/*     */   }
/*     */   
/*     */   public void registerAttribHandler(String name, CookieAttributeHandler handler)
/*     */   {
/*  65 */     if (name == null) {
/*  66 */       throw new IllegalArgumentException("Attribute name may not be null");
/*     */     }
/*  68 */     if (handler == null) {
/*  69 */       throw new IllegalArgumentException("Attribute handler may not be null");
/*     */     }
/*  71 */     this.attribHandlerMap.put(name, handler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CookieAttributeHandler findAttribHandler(String name)
/*     */   {
/*  83 */     return (CookieAttributeHandler)this.attribHandlerMap.get(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CookieAttributeHandler getAttribHandler(String name)
/*     */   {
/*  95 */     CookieAttributeHandler handler = findAttribHandler(name);
/*  96 */     if (handler == null) {
/*  97 */       throw new IllegalStateException("Handler not registered for " + name + " attribute.");
/*     */     }
/*     */     
/* 100 */     return handler;
/*     */   }
/*     */   
/*     */   protected Collection<CookieAttributeHandler> getAttribHandlers()
/*     */   {
/* 105 */     return this.attribHandlerMap.values();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\http\impl\cookie\AbstractCookieSpec.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */