/*    */ package org.eclipse.jetty.io;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.Socket;
/*    */ import java.nio.channels.SelectableChannel;
/*    */ import java.nio.channels.SelectionKey;
/*    */ import java.nio.channels.SocketChannel;
/*    */ import org.eclipse.jetty.util.log.Log;
/*    */ import org.eclipse.jetty.util.log.Logger;
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
/*    */ public class SocketChannelEndPoint
/*    */   extends ChannelEndPoint
/*    */ {
/* 34 */   private static final Logger LOG = Log.getLogger(SocketChannelEndPoint.class);
/*    */   private final Socket _socket;
/*    */   private final InetSocketAddress _local;
/*    */   private final InetSocketAddress _remote;
/*    */   
/*    */   public SocketChannelEndPoint(SelectableChannel channel, ManagedSelector selector, SelectionKey key, Scheduler scheduler)
/*    */   {
/* 41 */     this((SocketChannel)channel, selector, key, scheduler);
/*    */   }
/*    */   
/*    */   public SocketChannelEndPoint(SocketChannel channel, ManagedSelector selector, SelectionKey key, Scheduler scheduler)
/*    */   {
/* 46 */     super(channel, selector, key, scheduler);
/*    */     
/* 48 */     this._socket = channel.socket();
/* 49 */     this._local = ((InetSocketAddress)this._socket.getLocalSocketAddress());
/* 50 */     this._remote = ((InetSocketAddress)this._socket.getRemoteSocketAddress());
/*    */   }
/*    */   
/*    */   public Socket getSocket()
/*    */   {
/* 55 */     return this._socket;
/*    */   }
/*    */   
/*    */ 
/*    */   public InetSocketAddress getLocalAddress()
/*    */   {
/* 61 */     return this._local;
/*    */   }
/*    */   
/*    */ 
/*    */   public InetSocketAddress getRemoteAddress()
/*    */   {
/* 67 */     return this._remote;
/*    */   }
/*    */   
/*    */ 
/*    */   protected void doShutdownOutput()
/*    */   {
/*    */     try
/*    */     {
/* 75 */       if (!this._socket.isOutputShutdown()) {
/* 76 */         this._socket.shutdownOutput();
/*    */       }
/*    */     }
/*    */     catch (IOException e) {
/* 80 */       LOG.debug(e);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\io\SocketChannelEndPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */