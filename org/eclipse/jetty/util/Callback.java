/*     */ package org.eclipse.jetty.util;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import org.eclipse.jetty.util.thread.Invocable;
/*     */ import org.eclipse.jetty.util.thread.Invocable.InvocationType;
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
/*     */ public abstract interface Callback
/*     */   extends Invocable
/*     */ {
/*  37 */   public static final Callback NOOP = new Callback()
/*     */   {
/*     */ 
/*     */     public Invocable.InvocationType getInvocationType()
/*     */     {
/*  42 */       return Invocable.InvocationType.NON_BLOCKING;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void succeeded() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void failed(Throwable x) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Callback from(CompletableFuture<?> completable)
/*     */   {
/*  75 */     return from(completable, Invocable.InvocationType.NON_BLOCKING);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Callback from(CompletableFuture<?> completable, final Invocable.InvocationType invocation)
/*     */   {
/*  88 */     if ((completable instanceof Callback)) {
/*  89 */       return (Callback)completable;
/*     */     }
/*  91 */     new Callback()
/*     */     {
/*     */ 
/*     */       public void succeeded()
/*     */       {
/*  96 */         Callback.this.complete(null);
/*     */       }
/*     */       
/*     */ 
/*     */       public void failed(Throwable x)
/*     */       {
/* 102 */         Callback.this.completeExceptionally(x);
/*     */       }
/*     */       
/*     */ 
/*     */       public Invocable.InvocationType getInvocationType()
/*     */       {
/* 108 */         return invocation;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public static class Nested implements Callback
/*     */   {
/*     */     private final Callback callback;
/*     */     
/*     */     public Nested(Callback callback)
/*     */     {
/* 119 */       this.callback = callback;
/*     */     }
/*     */     
/*     */     public Nested(Nested nested)
/*     */     {
/* 124 */       this.callback = nested.callback;
/*     */     }
/*     */     
/*     */     public Callback getCallback()
/*     */     {
/* 129 */       return this.callback;
/*     */     }
/*     */     
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 135 */       this.callback.succeeded();
/*     */     }
/*     */     
/*     */ 
/*     */     public void failed(Throwable x)
/*     */     {
/* 141 */       this.callback.failed(x);
/*     */     }
/*     */     
/*     */ 
/*     */     public Invocable.InvocationType getInvocationType()
/*     */     {
/* 147 */       return this.callback.getInvocationType();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Completable
/*     */     extends CompletableFuture<Void>
/*     */     implements Callback
/*     */   {
/*     */     private final Invocable.InvocationType invocation;
/*     */     
/*     */     public Completable()
/*     */     {
/* 159 */       this(Invocable.InvocationType.NON_BLOCKING);
/*     */     }
/*     */     
/*     */     public Completable(Invocable.InvocationType invocation)
/*     */     {
/* 164 */       this.invocation = invocation;
/*     */     }
/*     */     
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 170 */       complete(null);
/*     */     }
/*     */     
/*     */ 
/*     */     public void failed(Throwable x)
/*     */     {
/* 176 */       completeExceptionally(x);
/*     */     }
/*     */     
/*     */ 
/*     */     public Invocable.InvocationType getInvocationType()
/*     */     {
/* 182 */       return this.invocation;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\Callback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */