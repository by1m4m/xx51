/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.List;
/*     */ import java.util.function.BiFunction;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLParameters;
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
/*     */ final class Java9SslUtils
/*     */ {
/*  34 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(Java9SslUtils.class);
/*     */   private static final Method SET_APPLICATION_PROTOCOLS;
/*     */   private static final Method GET_APPLICATION_PROTOCOL;
/*     */   private static final Method GET_HANDSHAKE_APPLICATION_PROTOCOL;
/*     */   private static final Method SET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR;
/*     */   private static final Method GET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR;
/*     */   
/*     */   static {
/*  42 */     Method getHandshakeApplicationProtocol = null;
/*  43 */     Method getApplicationProtocol = null;
/*  44 */     Method setApplicationProtocols = null;
/*  45 */     Method setHandshakeApplicationProtocolSelector = null;
/*  46 */     Method getHandshakeApplicationProtocolSelector = null;
/*     */     try
/*     */     {
/*  49 */       SSLContext context = SSLContext.getInstance("TLS");
/*  50 */       context.init(null, null, null);
/*  51 */       SSLEngine engine = context.createSSLEngine();
/*  52 */       getHandshakeApplicationProtocol = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Method run() throws Exception {
/*  55 */           return SSLEngine.class.getMethod("getHandshakeApplicationProtocol", new Class[0]);
/*     */         }
/*  57 */       });
/*  58 */       getHandshakeApplicationProtocol.invoke(engine, new Object[0]);
/*  59 */       getApplicationProtocol = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Method run() throws Exception {
/*  62 */           return SSLEngine.class.getMethod("getApplicationProtocol", new Class[0]);
/*     */         }
/*  64 */       });
/*  65 */       getApplicationProtocol.invoke(engine, new Object[0]);
/*     */       
/*  67 */       setApplicationProtocols = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Method run() throws Exception {
/*  70 */           return SSLParameters.class.getMethod("setApplicationProtocols", new Class[] { String[].class });
/*     */         }
/*  72 */       });
/*  73 */       setApplicationProtocols.invoke(engine.getSSLParameters(), new Object[] { EmptyArrays.EMPTY_STRINGS });
/*     */       
/*     */ 
/*  76 */       setHandshakeApplicationProtocolSelector = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Method run() throws Exception
/*     */         {
/*  79 */           return SSLEngine.class.getMethod("setHandshakeApplicationProtocolSelector", new Class[] { BiFunction.class });
/*     */         }
/*  81 */       });
/*  82 */       setHandshakeApplicationProtocolSelector.invoke(engine, new Object[] { new BiFunction()
/*     */       {
/*     */         public String apply(SSLEngine sslEngine, List<String> strings) {
/*  85 */           return null;
/*     */         }
/*     */         
/*     */ 
/*  89 */       } });
/*  90 */       getHandshakeApplicationProtocolSelector = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public Method run() throws Exception
/*     */         {
/*  93 */           return SSLEngine.class.getMethod("getHandshakeApplicationProtocolSelector", new Class[0]);
/*     */         }
/*  95 */       });
/*  96 */       getHandshakeApplicationProtocolSelector.invoke(engine, new Object[0]);
/*     */     } catch (Throwable t) {
/*  98 */       logger.error("Unable to initialize Java9SslUtils, but the detected javaVersion was: {}", 
/*  99 */         Integer.valueOf(PlatformDependent.javaVersion()), t);
/* 100 */       getHandshakeApplicationProtocol = null;
/* 101 */       getApplicationProtocol = null;
/* 102 */       setApplicationProtocols = null;
/* 103 */       setHandshakeApplicationProtocolSelector = null;
/* 104 */       getHandshakeApplicationProtocolSelector = null;
/*     */     }
/* 106 */     GET_HANDSHAKE_APPLICATION_PROTOCOL = getHandshakeApplicationProtocol;
/* 107 */     GET_APPLICATION_PROTOCOL = getApplicationProtocol;
/* 108 */     SET_APPLICATION_PROTOCOLS = setApplicationProtocols;
/* 109 */     SET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR = setHandshakeApplicationProtocolSelector;
/* 110 */     GET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR = getHandshakeApplicationProtocolSelector;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static boolean supportsAlpn()
/*     */   {
/* 117 */     return GET_APPLICATION_PROTOCOL != null;
/*     */   }
/*     */   
/*     */   static String getApplicationProtocol(SSLEngine sslEngine) {
/*     */     try {
/* 122 */       return (String)GET_APPLICATION_PROTOCOL.invoke(sslEngine, new Object[0]);
/*     */     } catch (UnsupportedOperationException ex) {
/* 124 */       throw ex;
/*     */     } catch (Exception ex) {
/* 126 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   static String getHandshakeApplicationProtocol(SSLEngine sslEngine) {
/*     */     try {
/* 132 */       return (String)GET_HANDSHAKE_APPLICATION_PROTOCOL.invoke(sslEngine, new Object[0]);
/*     */     } catch (UnsupportedOperationException ex) {
/* 134 */       throw ex;
/*     */     } catch (Exception ex) {
/* 136 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   static void setApplicationProtocols(SSLEngine engine, List<String> supportedProtocols) {
/* 141 */     SSLParameters parameters = engine.getSSLParameters();
/*     */     
/* 143 */     String[] protocolArray = (String[])supportedProtocols.toArray(EmptyArrays.EMPTY_STRINGS);
/*     */     try {
/* 145 */       SET_APPLICATION_PROTOCOLS.invoke(parameters, new Object[] { protocolArray });
/*     */     } catch (UnsupportedOperationException ex) {
/* 147 */       throw ex;
/*     */     } catch (Exception ex) {
/* 149 */       throw new IllegalStateException(ex);
/*     */     }
/* 151 */     engine.setSSLParameters(parameters);
/*     */   }
/*     */   
/*     */   static void setHandshakeApplicationProtocolSelector(SSLEngine engine, BiFunction<SSLEngine, List<String>, String> selector)
/*     */   {
/*     */     try {
/* 157 */       SET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR.invoke(engine, new Object[] { selector });
/*     */     } catch (UnsupportedOperationException ex) {
/* 159 */       throw ex;
/*     */     } catch (Exception ex) {
/* 161 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   static BiFunction<SSLEngine, List<String>, String> getHandshakeApplicationProtocolSelector(SSLEngine engine) {
/*     */     try {
/* 167 */       return 
/* 168 */         (BiFunction)GET_HANDSHAKE_APPLICATION_PROTOCOL_SELECTOR.invoke(engine, new Object[0]);
/*     */     } catch (UnsupportedOperationException ex) {
/* 170 */       throw ex;
/*     */     } catch (Exception ex) {
/* 172 */       throw new IllegalStateException(ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\Java9SslUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */