/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.util.internal.InternalThreadLocalMap;
/*    */ import java.util.Map;
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
/*    */ public abstract class ChannelHandlerAdapter
/*    */   implements ChannelHandler
/*    */ {
/*    */   boolean added;
/*    */   
/*    */   protected void ensureNotSharable()
/*    */   {
/* 35 */     if (isSharable()) {
/* 36 */       throw new IllegalStateException("ChannelHandler " + getClass().getName() + " is not allowed to be shared");
/*    */     }
/*    */   }
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
/*    */   public boolean isSharable()
/*    */   {
/* 53 */     Class<?> clazz = getClass();
/* 54 */     Map<Class<?>, Boolean> cache = InternalThreadLocalMap.get().handlerSharableCache();
/* 55 */     Boolean sharable = (Boolean)cache.get(clazz);
/* 56 */     if (sharable == null) {
/* 57 */       sharable = Boolean.valueOf(clazz.isAnnotationPresent(ChannelHandler.Sharable.class));
/* 58 */       cache.put(clazz, sharable);
/*    */     }
/* 60 */     return sharable.booleanValue();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void handlerAdded(ChannelHandlerContext ctx)
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void handlerRemoved(ChannelHandlerContext ctx)
/*    */     throws Exception
/*    */   {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*    */     throws Exception
/*    */   {
/* 87 */     ctx.fireExceptionCaught(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\ChannelHandlerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */