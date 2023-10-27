/*     */ package org.eclipse.jetty.client.api;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.EventListener;
/*     */ import java.util.List;
/*     */ import org.eclipse.jetty.http.HttpField;
/*     */ import org.eclipse.jetty.http.HttpFields;
/*     */ import org.eclipse.jetty.http.HttpVersion;
/*     */ import org.eclipse.jetty.util.Callback;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface Response
/*     */ {
/*     */   public abstract Request getRequest();
/*     */   
/*     */   public abstract <T extends ResponseListener> List<T> getListeners(Class<T> paramClass);
/*     */   
/*     */   public abstract HttpVersion getVersion();
/*     */   
/*     */   public abstract int getStatus();
/*     */   
/*     */   public abstract String getReason();
/*     */   
/*     */   public abstract HttpFields getHeaders();
/*     */   
/*     */   public abstract boolean abort(Throwable paramThrowable);
/*     */   
/*     */   public static abstract interface Listener
/*     */     extends Response.BeginListener, Response.HeaderListener, Response.HeadersListener, Response.ContentListener, Response.AsyncContentListener, Response.SuccessListener, Response.FailureListener, Response.CompleteListener
/*     */   {
/*     */     public static class Adapter
/*     */       implements Response.Listener
/*     */     {
/*     */       public void onBegin(Response response) {}
/*     */       
/*     */       public boolean onHeader(Response response, HttpField field)
/*     */       {
/* 230 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       public void onHeaders(Response response) {}
/*     */       
/*     */ 
/*     */ 
/*     */       public void onContent(Response response, ByteBuffer content) {}
/*     */       
/*     */ 
/*     */ 
/*     */       public void onContent(Response response, ByteBuffer content, Callback callback)
/*     */       {
/*     */         try
/*     */         {
/* 248 */           onContent(response, content);
/* 249 */           callback.succeeded();
/*     */         }
/*     */         catch (Throwable x)
/*     */         {
/* 253 */           callback.failed(x);
/*     */         }
/*     */       }
/*     */       
/*     */       public void onSuccess(Response response) {}
/*     */       
/*     */       public void onFailure(Response response, Throwable failure) {}
/*     */       
/*     */       public void onComplete(Result result) {}
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface CompleteListener
/*     */     extends Response.ResponseListener
/*     */   {
/*     */     public abstract void onComplete(Result paramResult);
/*     */   }
/*     */   
/*     */   public static abstract interface FailureListener
/*     */     extends Response.ResponseListener
/*     */   {
/*     */     public abstract void onFailure(Response paramResponse, Throwable paramThrowable);
/*     */   }
/*     */   
/*     */   public static abstract interface SuccessListener
/*     */     extends Response.ResponseListener
/*     */   {
/*     */     public abstract void onSuccess(Response paramResponse);
/*     */   }
/*     */   
/*     */   public static abstract interface AsyncContentListener
/*     */     extends Response.ResponseListener
/*     */   {
/*     */     public abstract void onContent(Response paramResponse, ByteBuffer paramByteBuffer, Callback paramCallback);
/*     */   }
/*     */   
/*     */   public static abstract interface ContentListener
/*     */     extends Response.ResponseListener
/*     */   {
/*     */     public abstract void onContent(Response paramResponse, ByteBuffer paramByteBuffer);
/*     */   }
/*     */   
/*     */   public static abstract interface HeadersListener
/*     */     extends Response.ResponseListener
/*     */   {
/*     */     public abstract void onHeaders(Response paramResponse);
/*     */   }
/*     */   
/*     */   public static abstract interface HeaderListener
/*     */     extends Response.ResponseListener
/*     */   {
/*     */     public abstract boolean onHeader(Response paramResponse, HttpField paramHttpField);
/*     */   }
/*     */   
/*     */   public static abstract interface BeginListener
/*     */     extends Response.ResponseListener
/*     */   {
/*     */     public abstract void onBegin(Response paramResponse);
/*     */   }
/*     */   
/*     */   public static abstract interface ResponseListener
/*     */     extends EventListener
/*     */   {}
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\api\Response.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */