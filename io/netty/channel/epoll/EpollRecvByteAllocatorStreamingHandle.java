/*    */ package io.netty.channel.epoll;
/*    */ 
/*    */ import io.netty.channel.RecvByteBufAllocator.ExtendedHandle;
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
/*    */ final class EpollRecvByteAllocatorStreamingHandle
/*    */   extends EpollRecvByteAllocatorHandle
/*    */ {
/*    */   public EpollRecvByteAllocatorStreamingHandle(RecvByteBufAllocator.ExtendedHandle handle)
/*    */   {
/* 22 */     super(handle);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   boolean maybeMoreDataToRead()
/*    */   {
/* 33 */     return (lastBytesRead() == attemptedBytesRead()) || (isReceivedRdHup());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\epoll\EpollRecvByteAllocatorStreamingHandle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */