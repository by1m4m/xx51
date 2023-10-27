/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import org.eclipse.jetty.client.api.ContentProvider;
/*     */ import org.eclipse.jetty.util.BufferUtil;
/*     */ import org.eclipse.jetty.util.Callback;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpContent
/*     */   implements Callback, Closeable
/*     */ {
/*  68 */   private static final Logger LOG = Log.getLogger(HttpContent.class);
/*  69 */   private static final ByteBuffer AFTER = ByteBuffer.allocate(0);
/*  70 */   private static final ByteBuffer CLOSE = ByteBuffer.allocate(0);
/*     */   
/*     */   private final ContentProvider provider;
/*     */   private final Iterator<ByteBuffer> iterator;
/*     */   private ByteBuffer buffer;
/*     */   private ByteBuffer content;
/*     */   private boolean last;
/*     */   
/*     */   public HttpContent(ContentProvider provider)
/*     */   {
/*  80 */     this.provider = provider;
/*  81 */     this.iterator = (provider == null ? Collections.emptyIterator() : provider.iterator());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isTheCloseBuffer(ByteBuffer buffer)
/*     */   {
/*  91 */     boolean isTheCloseBuffer = buffer == CLOSE;
/*  92 */     return isTheCloseBuffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasContent()
/*     */   {
/* 100 */     return this.provider != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isLast()
/*     */   {
/* 108 */     return this.last;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuffer getByteBuffer()
/*     */   {
/* 116 */     return this.buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuffer getContent()
/*     */   {
/* 124 */     return this.content;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean advance()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 5	org/eclipse/jetty/client/HttpContent:iterator	Ljava/util/Iterator;
/*     */     //   4: instanceof 10
/*     */     //   7: ifeq +34 -> 41
/*     */     //   10: aload_0
/*     */     //   11: getfield 5	org/eclipse/jetty/client/HttpContent:iterator	Ljava/util/Iterator;
/*     */     //   14: checkcast 10	org/eclipse/jetty/client/Synchronizable
/*     */     //   17: invokeinterface 11 1 0
/*     */     //   22: dup
/*     */     //   23: astore_1
/*     */     //   24: monitorenter
/*     */     //   25: aload_0
/*     */     //   26: aload_0
/*     */     //   27: getfield 5	org/eclipse/jetty/client/HttpContent:iterator	Ljava/util/Iterator;
/*     */     //   30: invokespecial 12	org/eclipse/jetty/client/HttpContent:advance	(Ljava/util/Iterator;)Z
/*     */     //   33: aload_1
/*     */     //   34: monitorexit
/*     */     //   35: ireturn
/*     */     //   36: astore_2
/*     */     //   37: aload_1
/*     */     //   38: monitorexit
/*     */     //   39: aload_2
/*     */     //   40: athrow
/*     */     //   41: aload_0
/*     */     //   42: aload_0
/*     */     //   43: getfield 5	org/eclipse/jetty/client/HttpContent:iterator	Ljava/util/Iterator;
/*     */     //   46: invokespecial 12	org/eclipse/jetty/client/HttpContent:advance	(Ljava/util/Iterator;)Z
/*     */     //   49: ireturn
/*     */     // Line number table:
/*     */     //   Java source line #140	-> byte code offset #0
/*     */     //   Java source line #142	-> byte code offset #10
/*     */     //   Java source line #144	-> byte code offset #25
/*     */     //   Java source line #145	-> byte code offset #36
/*     */     //   Java source line #149	-> byte code offset #41
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	50	0	this	HttpContent
/*     */     //   23	15	1	Ljava/lang/Object;	Object
/*     */     //   36	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   25	35	36	finally
/*     */     //   36	39	36	finally
/*     */   }
/*     */   
/*     */   private boolean advance(Iterator<ByteBuffer> iterator)
/*     */   {
/* 155 */     boolean hasNext = iterator.hasNext();
/* 156 */     ByteBuffer bytes = hasNext ? (ByteBuffer)iterator.next() : null;
/* 157 */     boolean hasMore = (hasNext) && (iterator.hasNext());
/* 158 */     boolean wasLast = this.last;
/* 159 */     this.last = (!hasMore);
/*     */     
/* 161 */     if (hasNext)
/*     */     {
/* 163 */       this.buffer = bytes;
/* 164 */       this.content = (bytes == null ? null : bytes.slice());
/* 165 */       if (LOG.isDebugEnabled())
/* 166 */         LOG.debug("Advanced content to {} chunk {}", new Object[] { hasMore ? "next" : "last", String.valueOf(bytes) });
/* 167 */       return bytes != null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 172 */     if (wasLast)
/*     */     {
/* 174 */       this.buffer = (this.content = AFTER);
/* 175 */       if (LOG.isDebugEnabled()) {
/* 176 */         LOG.debug("Advanced content past last chunk", new Object[0]);
/*     */       }
/*     */     }
/*     */     else {
/* 180 */       this.buffer = (this.content = CLOSE);
/* 181 */       if (LOG.isDebugEnabled())
/* 182 */         LOG.debug("Advanced content to last chunk", new Object[0]);
/*     */     }
/* 184 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isConsumed()
/*     */   {
/* 194 */     return this.buffer == AFTER;
/*     */   }
/*     */   
/*     */ 
/*     */   public void succeeded()
/*     */   {
/* 200 */     if (isConsumed())
/* 201 */       return;
/* 202 */     if (isTheCloseBuffer(this.buffer))
/* 203 */       return;
/* 204 */     if ((this.iterator instanceof Callback)) {
/* 205 */       ((Callback)this.iterator).succeeded();
/*     */     }
/*     */   }
/*     */   
/*     */   public void failed(Throwable x)
/*     */   {
/* 211 */     if (isConsumed())
/* 212 */       return;
/* 213 */     if (isTheCloseBuffer(this.buffer))
/* 214 */       return;
/* 215 */     if ((this.iterator instanceof Callback)) {
/* 216 */       ((Callback)this.iterator).failed(x);
/*     */     }
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/*     */     try
/*     */     {
/* 224 */       if ((this.iterator instanceof Closeable)) {
/* 225 */         ((Closeable)this.iterator).close();
/*     */       }
/*     */     }
/*     */     catch (Throwable x) {
/* 229 */       LOG.ignore(x);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 236 */     return String.format("%s@%x - has=%b,last=%b,consumed=%b,buffer=%s", new Object[] {
/* 237 */       getClass().getSimpleName(), 
/* 238 */       Integer.valueOf(hashCode()), 
/* 239 */       Boolean.valueOf(hasContent()), 
/* 240 */       Boolean.valueOf(isLast()), 
/* 241 */       Boolean.valueOf(isConsumed()), 
/* 242 */       BufferUtil.toDetailString(getContent()) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\HttpContent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */