/*     */ package org.eclipse.jetty.websocket.common.events;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.websocket.api.Session;
/*     */ import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
/*     */ import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
/*     */ import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
/*     */ import org.eclipse.jetty.websocket.api.annotations.OnWebSocketFrame;
/*     */ import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
/*     */ import org.eclipse.jetty.websocket.api.extensions.Frame;
/*     */ import org.eclipse.jetty.websocket.common.events.annotated.AbstractMethodAnnotationScanner;
/*     */ import org.eclipse.jetty.websocket.common.events.annotated.CallableMethod;
/*     */ import org.eclipse.jetty.websocket.common.events.annotated.InvalidSignatureException;
/*     */ import org.eclipse.jetty.websocket.common.events.annotated.OptionalSessionCallableMethod;
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
/*     */ public class JettyAnnotatedScanner
/*     */   extends AbstractMethodAnnotationScanner<JettyAnnotatedMetadata>
/*     */ {
/*  42 */   private static final Logger LOG = Log.getLogger(JettyAnnotatedScanner.class);
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
/*     */   private static final ParamList validBinaryParams;
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
/*  76 */   private static final ParamList validConnectParams = new ParamList();
/*  77 */   static { validConnectParams.addParams(new Class[] { Session.class });
/*     */     
/*  79 */     validCloseParams = new ParamList();
/*  80 */     validCloseParams.addParams(new Class[] { Integer.TYPE, String.class });
/*  81 */     validCloseParams.addParams(new Class[] { Session.class, Integer.TYPE, String.class });
/*     */     
/*  83 */     validErrorParams = new ParamList();
/*  84 */     validErrorParams.addParams(new Class[] { Throwable.class });
/*  85 */     validErrorParams.addParams(new Class[] { Session.class, Throwable.class });
/*     */     
/*  87 */     validTextParams = new ParamList();
/*  88 */     validTextParams.addParams(new Class[] { String.class });
/*  89 */     validTextParams.addParams(new Class[] { Session.class, String.class });
/*  90 */     validTextParams.addParams(new Class[] { Reader.class });
/*  91 */     validTextParams.addParams(new Class[] { Session.class, Reader.class });
/*     */     
/*  93 */     validBinaryParams = new ParamList();
/*  94 */     validBinaryParams.addParams(new Class[] { byte[].class, Integer.TYPE, Integer.TYPE });
/*  95 */     validBinaryParams.addParams(new Class[] { Session.class, byte[].class, Integer.TYPE, Integer.TYPE });
/*  96 */     validBinaryParams.addParams(new Class[] { InputStream.class });
/*  97 */     validBinaryParams.addParams(new Class[] { Session.class, InputStream.class });
/*     */     
/*  99 */     validFrameParams = new ParamList();
/* 100 */     validFrameParams.addParams(new Class[] { Frame.class });
/* 101 */     validFrameParams.addParams(new Class[] { Session.class, Frame.class }); }
/*     */   
/*     */   private static final ParamList validCloseParams;
/*     */   private static final ParamList validErrorParams;
/*     */   private static final ParamList validFrameParams;
/*     */   private static final ParamList validTextParams;
/* 107 */   public void onMethodAnnotation(JettyAnnotatedMetadata metadata, Class<?> pojo, Method method, Annotation annotation) { if (LOG.isDebugEnabled()) {
/* 108 */       LOG.debug("onMethodAnnotation({}, {}, {}, {})", new Object[] { metadata, pojo, method, annotation });
/*     */     }
/* 110 */     if (isAnnotation(annotation, OnWebSocketConnect.class))
/*     */     {
/* 112 */       assertValidSignature(method, OnWebSocketConnect.class, validConnectParams);
/* 113 */       assertUnset(metadata.onConnect, OnWebSocketConnect.class, method);
/* 114 */       metadata.onConnect = new CallableMethod(pojo, method);
/* 115 */       return;
/*     */     }
/*     */     
/* 118 */     if (isAnnotation(annotation, OnWebSocketMessage.class))
/*     */     {
/* 120 */       if (isSignatureMatch(method, validTextParams))
/*     */       {
/*     */ 
/* 123 */         assertUnset(metadata.onText, OnWebSocketMessage.class, method);
/* 124 */         metadata.onText = new OptionalSessionCallableMethod(pojo, method);
/* 125 */         return;
/*     */       }
/*     */       
/* 128 */       if (isSignatureMatch(method, validBinaryParams))
/*     */       {
/*     */ 
/*     */ 
/* 132 */         assertUnset(metadata.onBinary, OnWebSocketMessage.class, method);
/* 133 */         metadata.onBinary = new OptionalSessionCallableMethod(pojo, method);
/* 134 */         return;
/*     */       }
/*     */       
/* 137 */       throw InvalidSignatureException.build(method, OnWebSocketMessage.class, new ParamList[] { validTextParams, validBinaryParams });
/*     */     }
/*     */     
/* 140 */     if (isAnnotation(annotation, OnWebSocketClose.class))
/*     */     {
/* 142 */       assertValidSignature(method, OnWebSocketClose.class, validCloseParams);
/* 143 */       assertUnset(metadata.onClose, OnWebSocketClose.class, method);
/* 144 */       metadata.onClose = new OptionalSessionCallableMethod(pojo, method);
/* 145 */       return;
/*     */     }
/*     */     
/* 148 */     if (isAnnotation(annotation, OnWebSocketError.class))
/*     */     {
/* 150 */       assertValidSignature(method, OnWebSocketError.class, validErrorParams);
/* 151 */       assertUnset(metadata.onError, OnWebSocketError.class, method);
/* 152 */       metadata.onError = new OptionalSessionCallableMethod(pojo, method);
/* 153 */       return;
/*     */     }
/*     */     
/* 156 */     if (isAnnotation(annotation, OnWebSocketFrame.class))
/*     */     {
/* 158 */       assertValidSignature(method, OnWebSocketFrame.class, validFrameParams);
/* 159 */       assertUnset(metadata.onFrame, OnWebSocketFrame.class, method);
/* 160 */       metadata.onFrame = new OptionalSessionCallableMethod(pojo, method);
/* 161 */       return;
/*     */     }
/*     */   }
/*     */   
/*     */   public JettyAnnotatedMetadata scan(Class<?> pojo)
/*     */   {
/* 167 */     JettyAnnotatedMetadata metadata = new JettyAnnotatedMetadata();
/* 168 */     scanMethodAnnotations(metadata, pojo);
/* 169 */     return metadata;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\events\JettyAnnotatedScanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */