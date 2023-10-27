/*    */ package io.netty.channel.socket;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public final class ChannelOutputShutdownException
/*    */   extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 6712549938359321378L;
/*    */   
/*    */   public ChannelOutputShutdownException(String msg)
/*    */   {
/* 32 */     super(msg);
/*    */   }
/*    */   
/*    */   public ChannelOutputShutdownException(String msg, Throwable cause) {
/* 36 */     super(msg, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\socket\ChannelOutputShutdownException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */