/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Http2FrameStreamEvent
/*    */ {
/*    */   private final Http2FrameStream stream;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final Type type;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static enum Type
/*    */   {
/* 27 */     State, 
/* 28 */     Writability;
/*    */     
/*    */     private Type() {} }
/*    */   
/* 32 */   private Http2FrameStreamEvent(Http2FrameStream stream, Type type) { this.stream = stream;
/* 33 */     this.type = type;
/*    */   }
/*    */   
/*    */   public Http2FrameStream stream() {
/* 37 */     return this.stream;
/*    */   }
/*    */   
/*    */   public Type type() {
/* 41 */     return this.type;
/*    */   }
/*    */   
/*    */   static Http2FrameStreamEvent stateChanged(Http2FrameStream stream) {
/* 45 */     return new Http2FrameStreamEvent(stream, Type.State);
/*    */   }
/*    */   
/*    */   static Http2FrameStreamEvent writabilityChanged(Http2FrameStream stream) {
/* 49 */     return new Http2FrameStreamEvent(stream, Type.Writability);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http2\Http2FrameStreamEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */