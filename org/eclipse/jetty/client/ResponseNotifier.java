/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.eclipse.jetty.client.api.ContentResponse;
/*     */ import org.eclipse.jetty.client.api.Request;
/*     */ import org.eclipse.jetty.client.api.Response;
/*     */ import org.eclipse.jetty.client.api.Response.AsyncContentListener;
/*     */ import org.eclipse.jetty.client.api.Response.BeginListener;
/*     */ import org.eclipse.jetty.client.api.Response.CompleteListener;
/*     */ import org.eclipse.jetty.client.api.Response.FailureListener;
/*     */ import org.eclipse.jetty.client.api.Response.HeaderListener;
/*     */ import org.eclipse.jetty.client.api.Response.HeadersListener;
/*     */ import org.eclipse.jetty.client.api.Response.ResponseListener;
/*     */ import org.eclipse.jetty.client.api.Response.SuccessListener;
/*     */ import org.eclipse.jetty.client.api.Result;
/*     */ import org.eclipse.jetty.http.HttpField;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.IteratingCallback.Action;
/*     */ import org.eclipse.jetty.util.IteratingNestedCallback;
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
/*     */ public class ResponseNotifier
/*     */ {
/*  37 */   private static final Logger LOG = Log.getLogger(ResponseNotifier.class);
/*     */   
/*     */ 
/*     */   public void notifyBegin(List<Response.ResponseListener> listeners, Response response)
/*     */   {
/*  42 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/*  44 */       Response.ResponseListener listener = (Response.ResponseListener)listeners.get(i);
/*  45 */       if ((listener instanceof Response.BeginListener)) {
/*  46 */         notifyBegin((Response.BeginListener)listener, response);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyBegin(Response.BeginListener listener, Response response)
/*     */   {
/*     */     try {
/*  54 */       listener.onBegin(response);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/*  58 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean notifyHeader(List<Response.ResponseListener> listeners, Response response, HttpField field)
/*     */   {
/*  64 */     boolean result = true;
/*     */     
/*  66 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/*  68 */       Response.ResponseListener listener = (Response.ResponseListener)listeners.get(i);
/*  69 */       if ((listener instanceof Response.HeaderListener))
/*  70 */         result &= notifyHeader((Response.HeaderListener)listener, response, field);
/*     */     }
/*  72 */     return result;
/*     */   }
/*     */   
/*     */   private boolean notifyHeader(Response.HeaderListener listener, Response response, HttpField field)
/*     */   {
/*     */     try
/*     */     {
/*  79 */       return listener.onHeader(response, field);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/*  83 */       LOG.info("Exception while notifying listener " + listener, x); }
/*  84 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void notifyHeaders(List<Response.ResponseListener> listeners, Response response)
/*     */   {
/*  91 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/*  93 */       Response.ResponseListener listener = (Response.ResponseListener)listeners.get(i);
/*  94 */       if ((listener instanceof Response.HeadersListener)) {
/*  95 */         notifyHeaders((Response.HeadersListener)listener, response);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyHeaders(Response.HeadersListener listener, Response response)
/*     */   {
/*     */     try {
/* 103 */       listener.onHeaders(response);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 107 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void notifyContent(List<Response.ResponseListener> listeners, Response response, ByteBuffer buffer, Callback callback)
/*     */   {
/* 116 */     ContentCallback contentCallback = new ContentCallback(listeners, response, buffer, callback, null);
/* 117 */     contentCallback.iterate();
/*     */   }
/*     */   
/*     */   private void notifyContent(Response.AsyncContentListener listener, Response response, ByteBuffer buffer, Callback callback)
/*     */   {
/*     */     try
/*     */     {
/* 124 */       listener.onContent(response, buffer, callback);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 128 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void notifySuccess(List<Response.ResponseListener> listeners, Response response)
/*     */   {
/* 135 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/* 137 */       Response.ResponseListener listener = (Response.ResponseListener)listeners.get(i);
/* 138 */       if ((listener instanceof Response.SuccessListener)) {
/* 139 */         notifySuccess((Response.SuccessListener)listener, response);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifySuccess(Response.SuccessListener listener, Response response)
/*     */   {
/*     */     try {
/* 147 */       listener.onSuccess(response);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 151 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void notifyFailure(List<Response.ResponseListener> listeners, Response response, Throwable failure)
/*     */   {
/* 158 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/* 160 */       Response.ResponseListener listener = (Response.ResponseListener)listeners.get(i);
/* 161 */       if ((listener instanceof Response.FailureListener)) {
/* 162 */         notifyFailure((Response.FailureListener)listener, response, failure);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyFailure(Response.FailureListener listener, Response response, Throwable failure)
/*     */   {
/*     */     try {
/* 170 */       listener.onFailure(response, failure);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 174 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void notifyComplete(List<Response.ResponseListener> listeners, Result result)
/*     */   {
/* 181 */     for (int i = 0; i < listeners.size(); i++)
/*     */     {
/* 183 */       Response.ResponseListener listener = (Response.ResponseListener)listeners.get(i);
/* 184 */       if ((listener instanceof Response.CompleteListener)) {
/* 185 */         notifyComplete((Response.CompleteListener)listener, result);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyComplete(Response.CompleteListener listener, Result result)
/*     */   {
/*     */     try {
/* 193 */       listener.onComplete(result);
/*     */     }
/*     */     catch (Throwable x)
/*     */     {
/* 197 */       LOG.info("Exception while notifying listener " + listener, x);
/*     */     }
/*     */   }
/*     */   
/*     */   public void forwardSuccess(List<Response.ResponseListener> listeners, Response response)
/*     */   {
/* 203 */     notifyBegin(listeners, response);
/* 204 */     for (Iterator<HttpField> iterator = response.getHeaders().iterator(); iterator.hasNext();)
/*     */     {
/* 206 */       HttpField field = (HttpField)iterator.next();
/* 207 */       if (!notifyHeader(listeners, response, field))
/* 208 */         iterator.remove();
/*     */     }
/* 210 */     notifyHeaders(listeners, response);
/* 211 */     if ((response instanceof ContentResponse))
/* 212 */       notifyContent(listeners, response, ByteBuffer.wrap(((ContentResponse)response).getContent()), Callback.NOOP);
/* 213 */     notifySuccess(listeners, response);
/*     */   }
/*     */   
/*     */   public void forwardSuccessComplete(List<Response.ResponseListener> listeners, Request request, Response response)
/*     */   {
/* 218 */     forwardSuccess(listeners, response);
/* 219 */     notifyComplete(listeners, new Result(request, response));
/*     */   }
/*     */   
/*     */   public void forwardFailure(List<Response.ResponseListener> listeners, Response response, Throwable failure)
/*     */   {
/* 224 */     notifyBegin(listeners, response);
/* 225 */     for (Iterator<HttpField> iterator = response.getHeaders().iterator(); iterator.hasNext();)
/*     */     {
/* 227 */       HttpField field = (HttpField)iterator.next();
/* 228 */       if (!notifyHeader(listeners, response, field))
/* 229 */         iterator.remove();
/*     */     }
/* 231 */     notifyHeaders(listeners, response);
/* 232 */     if ((response instanceof ContentResponse))
/* 233 */       notifyContent(listeners, response, ByteBuffer.wrap(((ContentResponse)response).getContent()), Callback.NOOP);
/* 234 */     notifyFailure(listeners, response, failure);
/*     */   }
/*     */   
/*     */   public void forwardFailureComplete(List<Response.ResponseListener> listeners, Request request, Throwable requestFailure, Response response, Throwable responseFailure)
/*     */   {
/* 239 */     forwardFailure(listeners, response, responseFailure);
/* 240 */     notifyComplete(listeners, new Result(request, requestFailure, response, responseFailure));
/*     */   }
/*     */   
/*     */   private class ContentCallback extends IteratingNestedCallback
/*     */   {
/*     */     private final List<Response.ResponseListener> listeners;
/*     */     private final Response response;
/*     */     private final ByteBuffer buffer;
/*     */     private int index;
/*     */     
/*     */     private ContentCallback(Response listeners, ByteBuffer response, Callback buffer)
/*     */     {
/* 252 */       super();
/* 253 */       this.listeners = listeners;
/* 254 */       this.response = response;
/*     */       
/* 256 */       this.buffer = buffer.slice();
/*     */     }
/*     */     
/*     */     protected IteratingCallback.Action process()
/*     */       throws Exception
/*     */     {
/* 262 */       if (this.index == this.listeners.size()) {
/* 263 */         return IteratingCallback.Action.SUCCEEDED;
/*     */       }
/* 265 */       Response.ResponseListener listener = (Response.ResponseListener)this.listeners.get(this.index);
/* 266 */       if ((listener instanceof Response.AsyncContentListener))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 271 */         this.buffer.clear();
/* 272 */         ResponseNotifier.this.notifyContent((Response.AsyncContentListener)listener, this.response, this.buffer, this);
/* 273 */         return IteratingCallback.Action.SCHEDULED;
/*     */       }
/*     */       
/*     */ 
/* 277 */       succeeded();
/* 278 */       return IteratingCallback.Action.SCHEDULED;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 285 */       this.index += 1;
/* 286 */       super.succeeded();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\ResponseNotifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */