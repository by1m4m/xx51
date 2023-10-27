/*     */ package org.eclipse.jetty.websocket.api;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WebSocketPolicy
/*     */ {
/*     */   private static final int KB = 1024;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static WebSocketPolicy newClientPolicy()
/*     */   {
/*  30 */     return new WebSocketPolicy(WebSocketBehavior.CLIENT);
/*     */   }
/*     */   
/*     */   public static WebSocketPolicy newServerPolicy()
/*     */   {
/*  35 */     return new WebSocketPolicy(WebSocketBehavior.SERVER);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  45 */   private int maxTextMessageSize = 65536;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  54 */   private int maxTextMessageBufferSize = 32768;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private int maxBinaryMessageSize = 65536;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   private int maxBinaryMessageBufferSize = 32768;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */   private long asyncWriteTimeout = 60000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   private long idleTimeout = 300000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   private int inputBufferSize = 4096;
/*     */   
/*     */ 
/*     */   private final WebSocketBehavior behavior;
/*     */   
/*     */ 
/*     */ 
/*     */   public WebSocketPolicy(WebSocketBehavior behavior)
/*     */   {
/* 102 */     this.behavior = behavior;
/*     */   }
/*     */   
/*     */   private void assertLessThan(String name, long size, String otherName, long otherSize)
/*     */   {
/* 107 */     if (size > otherSize)
/*     */     {
/* 109 */       throw new IllegalArgumentException(String.format("%s [%d] must be less than %s [%d]", new Object[] { name, Long.valueOf(size), otherName, Long.valueOf(otherSize) }));
/*     */     }
/*     */   }
/*     */   
/*     */   private void assertGreaterThan(String name, long size, long minSize)
/*     */   {
/* 115 */     if (size < minSize)
/*     */     {
/* 117 */       throw new IllegalArgumentException(String.format("%s [%d] must be a greater than or equal to " + minSize, new Object[] { name, Long.valueOf(size) }));
/*     */     }
/*     */   }
/*     */   
/*     */   public void assertValidBinaryMessageSize(int requestedSize)
/*     */   {
/* 123 */     if (this.maxBinaryMessageSize > 0)
/*     */     {
/*     */ 
/* 126 */       if (requestedSize > this.maxBinaryMessageSize)
/*     */       {
/* 128 */         throw new MessageTooLargeException("Binary message size [" + requestedSize + "] exceeds maximum size [" + this.maxBinaryMessageSize + "]");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void assertValidTextMessageSize(int requestedSize)
/*     */   {
/* 135 */     if (this.maxTextMessageSize > 0)
/*     */     {
/*     */ 
/* 138 */       if (requestedSize > this.maxTextMessageSize)
/*     */       {
/* 140 */         throw new MessageTooLargeException("Text message size [" + requestedSize + "] exceeds maximum size [" + this.maxTextMessageSize + "]");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public WebSocketPolicy clonePolicy()
/*     */   {
/* 147 */     WebSocketPolicy clone = new WebSocketPolicy(this.behavior);
/* 148 */     clone.idleTimeout = this.idleTimeout;
/* 149 */     clone.maxTextMessageSize = this.maxTextMessageSize;
/* 150 */     clone.maxTextMessageBufferSize = this.maxTextMessageBufferSize;
/* 151 */     clone.maxBinaryMessageSize = this.maxBinaryMessageSize;
/* 152 */     clone.maxBinaryMessageBufferSize = this.maxBinaryMessageBufferSize;
/* 153 */     clone.inputBufferSize = this.inputBufferSize;
/* 154 */     clone.asyncWriteTimeout = this.asyncWriteTimeout;
/* 155 */     return clone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getAsyncWriteTimeout()
/*     */   {
/* 167 */     return this.asyncWriteTimeout;
/*     */   }
/*     */   
/*     */   public WebSocketBehavior getBehavior()
/*     */   {
/* 172 */     return this.behavior;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getIdleTimeout()
/*     */   {
/* 182 */     return this.idleTimeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getInputBufferSize()
/*     */   {
/* 194 */     return this.inputBufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxBinaryMessageBufferSize()
/*     */   {
/* 204 */     return this.maxBinaryMessageBufferSize;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxBinaryMessageSize()
/*     */   {
/* 222 */     return this.maxBinaryMessageSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxTextMessageBufferSize()
/*     */   {
/* 232 */     return this.maxTextMessageBufferSize;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxTextMessageSize()
/*     */   {
/* 250 */     return this.maxTextMessageSize;
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
/*     */   public void setAsyncWriteTimeout(long ms)
/*     */   {
/* 263 */     assertLessThan("AsyncWriteTimeout", ms, "IdleTimeout", this.idleTimeout);
/* 264 */     this.asyncWriteTimeout = ms;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIdleTimeout(long ms)
/*     */   {
/* 275 */     assertGreaterThan("IdleTimeout", ms, 0L);
/* 276 */     this.idleTimeout = ms;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInputBufferSize(int size)
/*     */   {
/* 287 */     assertGreaterThan("InputBufferSize", size, 1L);
/* 288 */     this.inputBufferSize = size;
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
/*     */   public void setMaxBinaryMessageBufferSize(int size)
/*     */   {
/* 301 */     assertGreaterThan("MaxBinaryMessageBufferSize", size, 1L);
/*     */     
/* 303 */     this.maxBinaryMessageBufferSize = size;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxBinaryMessageSize(int size)
/*     */   {
/* 322 */     assertGreaterThan("MaxBinaryMessageSize", size, -1L);
/*     */     
/* 324 */     this.maxBinaryMessageSize = size;
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
/*     */   public void setMaxTextMessageBufferSize(int size)
/*     */   {
/* 337 */     assertGreaterThan("MaxTextMessageBufferSize", size, 1L);
/*     */     
/* 339 */     this.maxTextMessageBufferSize = size;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxTextMessageSize(int size)
/*     */   {
/* 358 */     assertGreaterThan("MaxTextMessageSize", size, -1L);
/*     */     
/* 360 */     this.maxTextMessageSize = size;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 366 */     StringBuilder builder = new StringBuilder();
/* 367 */     builder.append("WebSocketPolicy@").append(Integer.toHexString(hashCode()));
/* 368 */     builder.append("[behavior=").append(this.behavior);
/* 369 */     builder.append(",maxTextMessageSize=").append(this.maxTextMessageSize);
/* 370 */     builder.append(",maxTextMessageBufferSize=").append(this.maxTextMessageBufferSize);
/* 371 */     builder.append(",maxBinaryMessageSize=").append(this.maxBinaryMessageSize);
/* 372 */     builder.append(",maxBinaryMessageBufferSize=").append(this.maxBinaryMessageBufferSize);
/* 373 */     builder.append(",asyncWriteTimeout=").append(this.asyncWriteTimeout);
/* 374 */     builder.append(",idleTimeout=").append(this.idleTimeout);
/* 375 */     builder.append(",inputBufferSize=").append(this.inputBufferSize);
/* 376 */     builder.append("]");
/* 377 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\WebSocketPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */