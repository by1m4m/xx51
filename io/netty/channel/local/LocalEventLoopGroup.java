/*    */ package io.netty.channel.local;
/*    */ 
/*    */ import io.netty.channel.DefaultEventLoopGroup;
/*    */ import java.util.concurrent.ThreadFactory;
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
/*    */ @Deprecated
/*    */ public class LocalEventLoopGroup
/*    */   extends DefaultEventLoopGroup
/*    */ {
/*    */   public LocalEventLoopGroup() {}
/*    */   
/*    */   public LocalEventLoopGroup(int nThreads)
/*    */   {
/* 39 */     super(nThreads);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LocalEventLoopGroup(int nThreads, ThreadFactory threadFactory)
/*    */   {
/* 49 */     super(nThreads, threadFactory);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\local\LocalEventLoopGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */