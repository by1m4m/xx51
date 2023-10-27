/*    */ package org.eclipse.jetty.io;
/*    */ 
/*    */ import java.nio.channels.SelectableChannel;
/*    */ import java.nio.channels.SelectionKey;
/*    */ import org.eclipse.jetty.util.thread.Scheduler;
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
/*    */ public class SelectChannelEndPoint
/*    */   extends SocketChannelEndPoint
/*    */ {
/*    */   public SelectChannelEndPoint(SelectableChannel channel, ManagedSelector selector, SelectionKey key, Scheduler scheduler, long idleTimeout)
/*    */   {
/* 36 */     super(channel, selector, key, scheduler);
/* 37 */     setIdleTimeout(idleTimeout);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\SelectChannelEndPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */