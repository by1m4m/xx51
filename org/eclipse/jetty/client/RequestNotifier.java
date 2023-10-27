/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.client.api.Request.BeginListener;
/*     */ import org.eclipse.jetty.client.api.Request.CommitListener;
/*     */ import org.eclipse.jetty.client.api.Request.ContentListener;
/*     */ import org.eclipse.jetty.client.api.Request.FailureListener;
/*     */ import org.eclipse.jetty.client.api.Request.HeadersListener;
/*     */ import org.eclipse.jetty.client.api.Request.Listener;
/*     */ import org.eclipse.jetty.client.api.Request.QueuedListener;
/*     */ import org.eclipse.jetty.client.api.Request.RequestListener;
/*     */ import org.eclipse.jetty.client.api.Request.SuccessListener;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
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
/*     */ public class RequestNotifier
/*     */ {
/*  30 */   private static final Logger LOG = Log.getLogger(ResponseNotifier.class);
/*     */   
/*     */   private final HttpClient client;
/*     */   
/*     */   public RequestNotifier(HttpClient client)
/*     */   {
/*  36 */     this.client = client;
/*     */   }
/*     */   
/*     */ 
/*     */   public void notifyQueued(Request request)
/*     */   {
/*  42 */     List<Request.RequestListener> requestListeners = request.getRequestListeners(null);
/*  43 */     for (int i = 0; i < requestListeners.size(); i++)
/*     */     {
/*  45 */       Request.RequestListener listener = (Request.RequestListener)requestListeners.get(i);
/*  46 */       if ((listener instanceof Request.QueuedListener))
/*  47 */         notifyQueued((Request.QueuedListener)listener, request);
/*     */     }
/*  49 */     List<Request.Listener> listeners = this.client.getRequestListeners();
/*  50 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/*  52 */       Request.Listener listener = (Request.Listener)listeners.get(i);
/*  53 */       notifyQueued(listener, request);
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyQueued(Request.QueuedListener listener, Request request)
/*     */   {
/*     */     try
/*     */     {
/*  61 */       listener.onQueued(request);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/*  65 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void notifyBegin(Request request)
/*     */   {
/*  72 */     List<Request.RequestListener> requestListeners = request.getRequestListeners(null);
/*  73 */     for (int i = 0; i < requestListeners.size(); i++)
/*     */     {
/*  75 */       Request.RequestListener listener = (Request.RequestListener)requestListeners.get(i);
/*  76 */       if ((listener instanceof Request.BeginListener))
/*  77 */         notifyBegin((Request.BeginListener)listener, request);
/*     */     }
/*  79 */     List<Request.Listener> listeners = this.client.getRequestListeners();
/*  80 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/*  82 */       Request.Listener listener = (Request.Listener)listeners.get(i);
/*  83 */       notifyBegin(listener, request);
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyBegin(Request.BeginListener listener, Request request)
/*     */   {
/*     */     try
/*     */     {
/*  91 */       listener.onBegin(request);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/*  95 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void notifyHeaders(Request request)
/*     */   {
/* 102 */     List<Request.RequestListener> requestListeners = request.getRequestListeners(null);
/* 103 */     for (int i = 0; i < requestListeners.size(); i++)
/*     */     {
/* 105 */       Request.RequestListener listener = (Request.RequestListener)requestListeners.get(i);
/* 106 */       if ((listener instanceof Request.HeadersListener))
/* 107 */         notifyHeaders((Request.HeadersListener)listener, request);
/*     */     }
/* 109 */     List<Request.Listener> listeners = this.client.getRequestListeners();
/* 110 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/* 112 */       Request.Listener listener = (Request.Listener)listeners.get(i);
/* 113 */       notifyHeaders(listener, request);
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyHeaders(Request.HeadersListener listener, Request request)
/*     */   {
/*     */     try
/*     */     {
/* 121 */       listener.onHeaders(request);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 125 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void notifyCommit(Request request)
/*     */   {
/* 132 */     List<Request.RequestListener> requestListeners = request.getRequestListeners(null);
/* 133 */     for (int i = 0; i < requestListeners.size(); i++)
/*     */     {
/* 135 */       Request.RequestListener listener = (Request.RequestListener)requestListeners.get(i);
/* 136 */       if ((listener instanceof Request.CommitListener))
/* 137 */         notifyCommit((Request.CommitListener)listener, request);
/*     */     }
/* 139 */     List<Request.Listener> listeners = this.client.getRequestListeners();
/* 140 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/* 142 */       Request.Listener listener = (Request.Listener)listeners.get(i);
/* 143 */       notifyCommit(listener, request);
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyCommit(Request.CommitListener listener, Request request)
/*     */   {
/*     */     try
/*     */     {
/* 151 */       listener.onCommit(request);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 155 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void notifyContent(Request request, ByteBuffer content)
/*     */   {
/* 162 */     content = content.slice();
/* 163 */     if (!content.hasRemaining()) {
/* 164 */       return;
/*     */     }
/* 166 */     List<Request.RequestListener> requestListeners = request.getRequestListeners(null);
/* 167 */     for (int i = 0; i < requestListeners.size(); i++)
/*     */     {
/* 169 */       Request.RequestListener listener = (Request.RequestListener)requestListeners.get(i);
/* 170 */       if ((listener instanceof Request.ContentListener))
/*     */       {
/*     */ 
/*     */ 
/* 174 */         content.clear();
/* 175 */         notifyContent((Request.ContentListener)listener, request, content);
/*     */       }
/*     */     }
/* 178 */     List<Request.Listener> listeners = this.client.getRequestListeners();
/* 179 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/* 181 */       Request.Listener listener = (Request.Listener)listeners.get(i);
/*     */       
/*     */ 
/* 184 */       content.clear();
/* 185 */       notifyContent(listener, request, content);
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyContent(Request.ContentListener listener, Request request, ByteBuffer content)
/*     */   {
/*     */     try
/*     */     {
/* 193 */       listener.onContent(request, content);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 197 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void notifySuccess(Request request)
/*     */   {
/* 204 */     List<Request.RequestListener> requestListeners = request.getRequestListeners(null);
/* 205 */     for (int i = 0; i < requestListeners.size(); i++)
/*     */     {
/* 207 */       Request.RequestListener listener = (Request.RequestListener)requestListeners.get(i);
/* 208 */       if ((listener instanceof Request.SuccessListener))
/* 209 */         notifySuccess((Request.SuccessListener)listener, request);
/*     */     }
/* 211 */     List<Request.Listener> listeners = this.client.getRequestListeners();
/* 212 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/* 214 */       Request.Listener listener = (Request.Listener)listeners.get(i);
/* 215 */       notifySuccess(listener, request);
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifySuccess(Request.SuccessListener listener, Request request)
/*     */   {
/*     */     try
/*     */     {
/* 223 */       listener.onSuccess(request);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 227 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void notifyFailure(Request request, Throwable failure)
/*     */   {
/* 234 */     List<Request.RequestListener> requestListeners = request.getRequestListeners(null);
/* 235 */     for (int i = 0; i < requestListeners.size(); i++)
/*     */     {
/* 237 */       Request.RequestListener listener = (Request.RequestListener)requestListeners.get(i);
/* 238 */       if ((listener instanceof Request.FailureListener))
/* 239 */         notifyFailure((Request.FailureListener)listener, request, failure);
/*     */     }
/* 241 */     List<Request.Listener> listeners = this.client.getRequestListeners();
/* 242 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/* 244 */       Request.Listener listener = (Request.Listener)listeners.get(i);
/* 245 */       notifyFailure(listener, request, failure);
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyFailure(Request.FailureListener listener, Request request, Throwable failure)
/*     */   {
/*     */     try
/*     */     {
/* 253 */       listener.onFailure(request, failure);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 257 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\RequestNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */