/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import io.netty.buffer.ByteBufAllocator;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import javax.net.ssl.X509KeyManager;
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
/*    */ final class OpenSslCachingKeyMaterialProvider
/*    */   extends OpenSslKeyMaterialProvider
/*    */ {
/* 31 */   private final ConcurrentMap<String, OpenSslKeyMaterial> cache = new ConcurrentHashMap();
/*    */   
/*    */   OpenSslCachingKeyMaterialProvider(X509KeyManager keyManager, String password) {
/* 34 */     super(keyManager, password);
/*    */   }
/*    */   
/*    */   OpenSslKeyMaterial chooseKeyMaterial(ByteBufAllocator allocator, String alias) throws Exception
/*    */   {
/* 39 */     OpenSslKeyMaterial material = (OpenSslKeyMaterial)this.cache.get(alias);
/* 40 */     if (material == null) {
/* 41 */       material = super.chooseKeyMaterial(allocator, alias);
/* 42 */       if (material == null)
/*    */       {
/* 44 */         return null;
/*    */       }
/*    */       
/* 47 */       OpenSslKeyMaterial old = (OpenSslKeyMaterial)this.cache.putIfAbsent(alias, material);
/* 48 */       if (old != null) {
/* 49 */         material.release();
/* 50 */         material = old;
/*    */       }
/*    */     }
/*    */     
/* 54 */     return material.retain();
/*    */   }
/*    */   
/*    */   void destroy()
/*    */   {
/*    */     do
/*    */     {
/* 61 */       Iterator<OpenSslKeyMaterial> iterator = this.cache.values().iterator();
/* 62 */       while (iterator.hasNext()) {
/* 63 */         ((OpenSslKeyMaterial)iterator.next()).release();
/* 64 */         iterator.remove();
/*    */       }
/* 66 */     } while (!this.cache.isEmpty());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\OpenSslCachingKeyMaterialProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */