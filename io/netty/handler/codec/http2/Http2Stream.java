/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ 
/*    */ public abstract interface Http2Stream
/*    */ {
/*    */   public abstract int id();
/*    */   
/*    */ 
/*    */   public abstract State state();
/*    */   
/*    */ 
/*    */   public abstract Http2Stream open(boolean paramBoolean)
/*    */     throws Http2Exception;
/*    */   
/*    */ 
/*    */   public abstract Http2Stream close();
/*    */   
/*    */   public abstract Http2Stream closeLocalSide();
/*    */   
/*    */   public abstract Http2Stream closeRemoteSide();
/*    */   
/*    */   public abstract boolean isResetSent();
/*    */   
/*    */   public abstract Http2Stream resetSent();
/*    */   
/*    */   public abstract <V> V setProperty(Http2Connection.PropertyKey paramPropertyKey, V paramV);
/*    */   
/*    */   public static enum State
/*    */   {
/* 30 */     IDLE(false, false), 
/* 31 */     RESERVED_LOCAL(false, false), 
/* 32 */     RESERVED_REMOTE(false, false), 
/* 33 */     OPEN(true, true), 
/* 34 */     HALF_CLOSED_LOCAL(false, true), 
/* 35 */     HALF_CLOSED_REMOTE(true, false), 
/* 36 */     CLOSED(false, false);
/*    */     
/*    */     private final boolean localSideOpen;
/*    */     private final boolean remoteSideOpen;
/*    */     
/*    */     private State(boolean localSideOpen, boolean remoteSideOpen) {
/* 42 */       this.localSideOpen = localSideOpen;
/* 43 */       this.remoteSideOpen = remoteSideOpen;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public boolean localSideOpen()
/*    */     {
/* 51 */       return this.localSideOpen;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     public boolean remoteSideOpen()
/*    */     {
/* 59 */       return this.remoteSideOpen;
/*    */     }
/*    */   }
/*    */   
/*    */   public abstract <V> V getProperty(Http2Connection.PropertyKey paramPropertyKey);
/*    */   
/*    */   public abstract <V> V removeProperty(Http2Connection.PropertyKey paramPropertyKey);
/*    */   
/*    */   public abstract Http2Stream headersSent(boolean paramBoolean);
/*    */   
/*    */   public abstract boolean isHeadersSent();
/*    */   
/*    */   public abstract boolean isTrailersSent();
/*    */   
/*    */   public abstract Http2Stream headersReceived(boolean paramBoolean);
/*    */   
/*    */   public abstract boolean isHeadersReceived();
/*    */   
/*    */   public abstract boolean isTrailersReceived();
/*    */   
/*    */   public abstract Http2Stream pushPromiseSent();
/*    */   
/*    */   public abstract boolean isPushPromiseSent();
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2Stream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */