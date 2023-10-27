/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLException;
/*     */ import org.eclipse.jetty.alpn.ALPN;
/*     */ import org.eclipse.jetty.alpn.ALPN.ClientProvider;
/*     */ import org.eclipse.jetty.alpn.ALPN.ServerProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class JettyAlpnSslEngine
/*     */   extends JdkSslEngine
/*     */ {
/*  34 */   private static final boolean available = ;
/*     */   
/*     */   static boolean isAvailable() {
/*  37 */     return available;
/*     */   }
/*     */   
/*     */   private static boolean initAvailable() {
/*  41 */     if (PlatformDependent.javaVersion() <= 8) {
/*     */       try
/*     */       {
/*  44 */         Class.forName("sun.security.ssl.ALPNExtension", true, null);
/*  45 */         return true;
/*     */       }
/*     */       catch (Throwable localThrowable) {}
/*     */     }
/*     */     
/*  50 */     return false;
/*     */   }
/*     */   
/*     */   static JettyAlpnSslEngine newClientEngine(SSLEngine engine, JdkApplicationProtocolNegotiator applicationNegotiator)
/*     */   {
/*  55 */     return new ClientEngine(engine, applicationNegotiator);
/*     */   }
/*     */   
/*     */   static JettyAlpnSslEngine newServerEngine(SSLEngine engine, JdkApplicationProtocolNegotiator applicationNegotiator)
/*     */   {
/*  60 */     return new ServerEngine(engine, applicationNegotiator);
/*     */   }
/*     */   
/*     */   private JettyAlpnSslEngine(SSLEngine engine) {
/*  64 */     super(engine);
/*     */   }
/*     */   
/*     */   private static final class ClientEngine extends JettyAlpnSslEngine {
/*     */     ClientEngine(SSLEngine engine, final JdkApplicationProtocolNegotiator applicationNegotiator) {
/*  69 */       super(null);
/*  70 */       ObjectUtil.checkNotNull(applicationNegotiator, "applicationNegotiator");
/*  71 */       final JdkApplicationProtocolNegotiator.ProtocolSelectionListener protocolListener = (JdkApplicationProtocolNegotiator.ProtocolSelectionListener)ObjectUtil.checkNotNull(applicationNegotiator
/*  72 */         .protocolListenerFactory().newListener(this, applicationNegotiator.protocols()), "protocolListener");
/*     */       
/*  74 */       ALPN.put(engine, new ALPN.ClientProvider()
/*     */       {
/*     */         public List<String> protocols() {
/*  77 */           return applicationNegotiator.protocols();
/*     */         }
/*     */         
/*     */         public void selected(String protocol) throws SSLException
/*     */         {
/*     */           try {
/*  83 */             protocolListener.selected(protocol);
/*     */           } catch (Throwable t) {
/*  85 */             throw SslUtils.toSSLHandshakeException(t);
/*     */           }
/*     */         }
/*     */         
/*     */         public void unsupported()
/*     */         {
/*  91 */           protocolListener.unsupported();
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void closeInbound()
/*     */       throws SSLException
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual 12	io/netty/handler/ssl/JettyAlpnSslEngine$ClientEngine:getWrappedEngine	()Ljavax/net/ssl/SSLEngine;
/*     */       //   4: invokestatic 13	org/eclipse/jetty/alpn/ALPN:remove	(Ljavax/net/ssl/SSLEngine;)Lorg/eclipse/jetty/alpn/ALPN$Provider;
/*     */       //   7: pop
/*     */       //   8: aload_0
/*     */       //   9: invokespecial 14	io/netty/handler/ssl/JettyAlpnSslEngine:closeInbound	()V
/*     */       //   12: goto +10 -> 22
/*     */       //   15: astore_1
/*     */       //   16: aload_0
/*     */       //   17: invokespecial 14	io/netty/handler/ssl/JettyAlpnSslEngine:closeInbound	()V
/*     */       //   20: aload_1
/*     */       //   21: athrow
/*     */       //   22: return
/*     */       // Line number table:
/*     */       //   Java source line #99	-> byte code offset #0
/*     */       //   Java source line #101	-> byte code offset #8
/*     */       //   Java source line #102	-> byte code offset #12
/*     */       //   Java source line #101	-> byte code offset #15
/*     */       //   Java source line #102	-> byte code offset #20
/*     */       //   Java source line #103	-> byte code offset #22
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	23	0	this	ClientEngine
/*     */       //   15	6	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	8	15	finally
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void closeOutbound()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual 12	io/netty/handler/ssl/JettyAlpnSslEngine$ClientEngine:getWrappedEngine	()Ljavax/net/ssl/SSLEngine;
/*     */       //   4: invokestatic 13	org/eclipse/jetty/alpn/ALPN:remove	(Ljavax/net/ssl/SSLEngine;)Lorg/eclipse/jetty/alpn/ALPN$Provider;
/*     */       //   7: pop
/*     */       //   8: aload_0
/*     */       //   9: invokespecial 15	io/netty/handler/ssl/JettyAlpnSslEngine:closeOutbound	()V
/*     */       //   12: goto +10 -> 22
/*     */       //   15: astore_1
/*     */       //   16: aload_0
/*     */       //   17: invokespecial 15	io/netty/handler/ssl/JettyAlpnSslEngine:closeOutbound	()V
/*     */       //   20: aload_1
/*     */       //   21: athrow
/*     */       //   22: return
/*     */       // Line number table:
/*     */       //   Java source line #108	-> byte code offset #0
/*     */       //   Java source line #110	-> byte code offset #8
/*     */       //   Java source line #111	-> byte code offset #12
/*     */       //   Java source line #110	-> byte code offset #15
/*     */       //   Java source line #111	-> byte code offset #20
/*     */       //   Java source line #112	-> byte code offset #22
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	23	0	this	ClientEngine
/*     */       //   15	6	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	8	15	finally
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class ServerEngine
/*     */     extends JettyAlpnSslEngine
/*     */   {
/*     */     ServerEngine(SSLEngine engine, JdkApplicationProtocolNegotiator applicationNegotiator)
/*     */     {
/* 117 */       super(null);
/* 118 */       ObjectUtil.checkNotNull(applicationNegotiator, "applicationNegotiator");
/* 119 */       final JdkApplicationProtocolNegotiator.ProtocolSelector protocolSelector = (JdkApplicationProtocolNegotiator.ProtocolSelector)ObjectUtil.checkNotNull(applicationNegotiator.protocolSelectorFactory()
/* 120 */         .newSelector(this, new LinkedHashSet(applicationNegotiator.protocols())), "protocolSelector");
/*     */       
/* 122 */       ALPN.put(engine, new ALPN.ServerProvider()
/*     */       {
/*     */         public String select(List<String> protocols) throws SSLException {
/*     */           try {
/* 126 */             return protocolSelector.select(protocols);
/*     */           } catch (Throwable t) {
/* 128 */             throw SslUtils.toSSLHandshakeException(t);
/*     */           }
/*     */         }
/*     */         
/*     */         public void unsupported()
/*     */         {
/* 134 */           protocolSelector.unsupported();
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void closeInbound()
/*     */       throws SSLException
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual 14	io/netty/handler/ssl/JettyAlpnSslEngine$ServerEngine:getWrappedEngine	()Ljavax/net/ssl/SSLEngine;
/*     */       //   4: invokestatic 15	org/eclipse/jetty/alpn/ALPN:remove	(Ljavax/net/ssl/SSLEngine;)Lorg/eclipse/jetty/alpn/ALPN$Provider;
/*     */       //   7: pop
/*     */       //   8: aload_0
/*     */       //   9: invokespecial 16	io/netty/handler/ssl/JettyAlpnSslEngine:closeInbound	()V
/*     */       //   12: goto +10 -> 22
/*     */       //   15: astore_1
/*     */       //   16: aload_0
/*     */       //   17: invokespecial 16	io/netty/handler/ssl/JettyAlpnSslEngine:closeInbound	()V
/*     */       //   20: aload_1
/*     */       //   21: athrow
/*     */       //   22: return
/*     */       // Line number table:
/*     */       //   Java source line #142	-> byte code offset #0
/*     */       //   Java source line #144	-> byte code offset #8
/*     */       //   Java source line #145	-> byte code offset #12
/*     */       //   Java source line #144	-> byte code offset #15
/*     */       //   Java source line #145	-> byte code offset #20
/*     */       //   Java source line #146	-> byte code offset #22
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	23	0	this	ServerEngine
/*     */       //   15	6	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	8	15	finally
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void closeOutbound()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual 14	io/netty/handler/ssl/JettyAlpnSslEngine$ServerEngine:getWrappedEngine	()Ljavax/net/ssl/SSLEngine;
/*     */       //   4: invokestatic 15	org/eclipse/jetty/alpn/ALPN:remove	(Ljavax/net/ssl/SSLEngine;)Lorg/eclipse/jetty/alpn/ALPN$Provider;
/*     */       //   7: pop
/*     */       //   8: aload_0
/*     */       //   9: invokespecial 17	io/netty/handler/ssl/JettyAlpnSslEngine:closeOutbound	()V
/*     */       //   12: goto +10 -> 22
/*     */       //   15: astore_1
/*     */       //   16: aload_0
/*     */       //   17: invokespecial 17	io/netty/handler/ssl/JettyAlpnSslEngine:closeOutbound	()V
/*     */       //   20: aload_1
/*     */       //   21: athrow
/*     */       //   22: return
/*     */       // Line number table:
/*     */       //   Java source line #151	-> byte code offset #0
/*     */       //   Java source line #153	-> byte code offset #8
/*     */       //   Java source line #154	-> byte code offset #12
/*     */       //   Java source line #153	-> byte code offset #15
/*     */       //   Java source line #154	-> byte code offset #20
/*     */       //   Java source line #155	-> byte code offset #22
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	23	0	this	ServerEngine
/*     */       //   15	6	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	8	15	finally
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\JettyAlpnSslEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */