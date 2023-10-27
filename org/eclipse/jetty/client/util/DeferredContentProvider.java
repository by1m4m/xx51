/*     */ package org.eclipse.jetty.client.util;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.eclipse.jetty.client.AsyncContentProvider;
/*     */ import org.eclipse.jetty.client.AsyncContentProvider.Listener;
/*     */ import org.eclipse.jetty.client.Synchronizable;
/*     */ import org.eclipse.jetty.util.BufferUtil;
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
/*     */ public class DeferredContentProvider
/*     */   implements AsyncContentProvider, Callback, Closeable
/*     */ {
/*  91 */   private static final Chunk CLOSE = new Chunk(BufferUtil.EMPTY_BUFFER, Callback.NOOP);
/*     */   
/*  93 */   private final Object lock = this;
/*  94 */   private final Deque<Chunk> chunks = new ArrayDeque();
/*  95 */   private final AtomicReference<AsyncContentProvider.Listener> listener = new AtomicReference();
/*  96 */   private final DeferredContentProviderIterator iterator = new DeferredContentProviderIterator(null);
/*  97 */   private final AtomicBoolean closed = new AtomicBoolean();
/*  98 */   private long length = -1L;
/*     */   
/*     */ 
/*     */   private int size;
/*     */   
/*     */ 
/*     */   private Throwable failure;
/*     */   
/*     */ 
/*     */   public DeferredContentProvider(ByteBuffer... buffers)
/*     */   {
/* 109 */     for (ByteBuffer buffer : buffers) {
/* 110 */       offer(buffer);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setListener(AsyncContentProvider.Listener listener)
/*     */   {
/* 116 */     if (!this.listener.compareAndSet(null, listener)) {
/* 117 */       throw new IllegalStateException(String.format("The same %s instance cannot be used in multiple requests", new Object[] {AsyncContentProvider.class
/* 118 */         .getName() }));
/*     */     }
/* 120 */     if (isClosed())
/*     */     {
/* 122 */       synchronized (this.lock)
/*     */       {
/* 124 */         long total = 0L;
/* 125 */         for (Chunk chunk : this.chunks)
/* 126 */           total += chunk.buffer.remaining();
/* 127 */         this.length = total;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public long getLength()
/*     */   {
/* 135 */     return this.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean offer(ByteBuffer buffer)
/*     */   {
/* 147 */     return offer(buffer, Callback.NOOP);
/*     */   }
/*     */   
/*     */   public boolean offer(ByteBuffer buffer, Callback callback)
/*     */   {
/* 152 */     return offer(new Chunk(buffer, callback));
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean offer(Chunk chunk)
/*     */   {
/* 158 */     boolean result = false;
/* 159 */     synchronized (this.lock)
/*     */     {
/* 161 */       Throwable failure = this.failure;
/* 162 */       if (failure == null)
/*     */       {
/* 164 */         result = this.chunks.offer(chunk);
/* 165 */         if ((result) && (chunk != CLOSE))
/* 166 */           this.size += 1;
/*     */       } }
/*     */     Throwable failure;
/* 169 */     if (failure != null) {
/* 170 */       chunk.callback.failed(failure);
/* 171 */     } else if (result)
/* 172 */       notifyListener();
/* 173 */     return result;
/*     */   }
/*     */   
/*     */   private void clear()
/*     */   {
/* 178 */     synchronized (this.lock)
/*     */     {
/* 180 */       this.chunks.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public void flush() throws IOException
/*     */   {
/* 186 */     synchronized (this.lock)
/*     */     {
/*     */       try
/*     */       {
/*     */         for (;;)
/*     */         {
/* 192 */           if (this.failure != null)
/* 193 */             throw new IOException(this.failure);
/* 194 */           if (this.size == 0)
/*     */             break;
/* 196 */           this.lock.wait();
/*     */         }
/*     */       }
/*     */       catch (InterruptedException x)
/*     */       {
/* 201 */         throw new InterruptedIOException();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/* 213 */     if (this.closed.compareAndSet(false, true)) {
/* 214 */       offer(CLOSE);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/* 219 */     return this.closed.get();
/*     */   }
/*     */   
/*     */ 
/*     */   public void failed(Throwable failure)
/*     */   {
/* 225 */     this.iterator.failed(failure);
/*     */   }
/*     */   
/*     */   private void notifyListener()
/*     */   {
/* 230 */     AsyncContentProvider.Listener listener = (AsyncContentProvider.Listener)this.listener.get();
/* 231 */     if (listener != null) {
/* 232 */       listener.onContent();
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterator<ByteBuffer> iterator()
/*     */   {
/* 238 */     return this.iterator;
/*     */   }
/*     */   
/*     */   private class DeferredContentProviderIterator implements Iterator<ByteBuffer>, Callback, Synchronizable
/*     */   {
/*     */     private DeferredContentProvider.Chunk current;
/*     */     
/*     */     private DeferredContentProviderIterator() {}
/*     */     
/*     */     public boolean hasNext() {
/* 248 */       synchronized (DeferredContentProvider.this.lock)
/*     */       {
/* 250 */         return DeferredContentProvider.this.chunks.peek() != DeferredContentProvider.CLOSE;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public ByteBuffer next()
/*     */     {
/* 257 */       synchronized (DeferredContentProvider.this.lock)
/*     */       {
/* 259 */         DeferredContentProvider.Chunk chunk = this.current = (DeferredContentProvider.Chunk)DeferredContentProvider.this.chunks.poll();
/* 260 */         if (chunk == DeferredContentProvider.CLOSE)
/*     */         {
/*     */ 
/*     */ 
/* 264 */           DeferredContentProvider.this.chunks.offerFirst(DeferredContentProvider.CLOSE);
/* 265 */           throw new NoSuchElementException();
/*     */         }
/* 267 */         return chunk == null ? null : chunk.buffer;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void remove()
/*     */     {
/* 274 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void succeeded()
/*     */     {
/* 281 */       synchronized (DeferredContentProvider.this.lock)
/*     */       {
/* 283 */         DeferredContentProvider.Chunk chunk = this.current;
/* 284 */         if (chunk != null)
/*     */         {
/* 286 */           DeferredContentProvider.access$406(DeferredContentProvider.this);
/* 287 */           DeferredContentProvider.this.lock.notify();
/*     */         } }
/*     */       DeferredContentProvider.Chunk chunk;
/* 290 */       if (chunk != null) {
/* 291 */         chunk.callback.succeeded();
/*     */       }
/*     */     }
/*     */     
/*     */     public void failed(Throwable x)
/*     */     {
/* 297 */       List<DeferredContentProvider.Chunk> chunks = new ArrayList();
/* 298 */       synchronized (DeferredContentProvider.this.lock)
/*     */       {
/* 300 */         DeferredContentProvider.this.failure = x;
/*     */         
/* 302 */         DeferredContentProvider.Chunk chunk = this.current;
/* 303 */         this.current = null;
/* 304 */         if (chunk != null)
/* 305 */           chunks.add(chunk);
/* 306 */         chunks.addAll(DeferredContentProvider.this.chunks);
/* 307 */         DeferredContentProvider.this.clear();
/* 308 */         DeferredContentProvider.this.lock.notify();
/*     */       }
/* 310 */       for (??? = chunks.iterator(); ((Iterator)???).hasNext();) { DeferredContentProvider.Chunk chunk = (DeferredContentProvider.Chunk)((Iterator)???).next();
/* 311 */         chunk.callback.failed(x);
/*     */       }
/*     */     }
/*     */     
/*     */     public Object getLock()
/*     */     {
/* 317 */       return DeferredContentProvider.this.lock;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Chunk
/*     */   {
/*     */     public final ByteBuffer buffer;
/*     */     public final Callback callback;
/*     */     
/*     */     public Chunk(ByteBuffer buffer, Callback callback)
/*     */     {
/* 328 */       this.buffer = ((ByteBuffer)Objects.requireNonNull(buffer));
/* 329 */       this.callback = ((Callback)Objects.requireNonNull(callback));
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 335 */       return String.format("%s@%x", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\util\DeferredContentProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */