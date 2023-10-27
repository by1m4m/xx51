/*     */ package org.eclipse.jetty.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.eclipse.jetty.client.api.Connection;
/*     */ import org.eclipse.jetty.client.api.Destination;
/*     */ import org.eclipse.jetty.util.Callback;
/*     */ import org.eclipse.jetty.util.annotation.ManagedAttribute;
/*     */ import org.eclipse.jetty.util.component.ContainerLifeCycle;
/*     */ import org.eclipse.jetty.util.log.Log;
/*     */ import org.eclipse.jetty.util.log.Logger;
/*     */ import org.eclipse.jetty.util.thread.Scheduler;
/*     */ import org.eclipse.jetty.util.thread.Scheduler.Task;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ValidatingConnectionPool
/*     */   extends DuplexConnectionPool
/*     */ {
/*  61 */   private static final Logger LOG = Log.getLogger(ValidatingConnectionPool.class);
/*     */   
/*     */   private final Scheduler scheduler;
/*     */   private final long timeout;
/*     */   private final Map<Connection, Holder> quarantine;
/*     */   
/*     */   public ValidatingConnectionPool(Destination destination, int maxConnections, Callback requester, Scheduler scheduler, long timeout)
/*     */   {
/*  69 */     super(destination, maxConnections, requester);
/*  70 */     this.scheduler = scheduler;
/*  71 */     this.timeout = timeout;
/*  72 */     this.quarantine = new HashMap(maxConnections);
/*     */   }
/*     */   
/*     */   @ManagedAttribute(value="The number of validating connections", readonly=true)
/*     */   public int getValidatingConnectionCount()
/*     */   {
/*  78 */     return this.quarantine.size();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean release(Connection connection)
/*     */   {
/*  84 */     lock();
/*     */     try
/*     */     {
/*  87 */       if (!getActiveConnections().remove(connection))
/*  88 */         return false;
/*  89 */       Holder holder = new Holder(connection);
/*  90 */       holder.task = this.scheduler.schedule(holder, this.timeout, TimeUnit.MILLISECONDS);
/*  91 */       this.quarantine.put(connection, holder);
/*  92 */       if (LOG.isDebugEnabled()) {
/*  93 */         LOG.debug("Validating for {}ms {}", new Object[] { Long.valueOf(this.timeout), connection });
/*     */       }
/*     */     }
/*     */     finally {
/*  97 */       unlock();
/*     */     }
/*     */     
/* 100 */     released(connection);
/* 101 */     return true;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean remove(Connection connection)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 9	org/eclipse/jetty/client/ValidatingConnectionPool:lock	()V
/*     */     //   4: aload_0
/*     */     //   5: getfield 2	org/eclipse/jetty/client/ValidatingConnectionPool:quarantine	Ljava/util/Map;
/*     */     //   8: aload_1
/*     */     //   9: invokeinterface 25 2 0
/*     */     //   14: checkcast 13	org/eclipse/jetty/client/ValidatingConnectionPool$Holder
/*     */     //   17: astore_2
/*     */     //   18: aload_0
/*     */     //   19: invokevirtual 12	org/eclipse/jetty/client/ValidatingConnectionPool:unlock	()V
/*     */     //   22: goto +10 -> 32
/*     */     //   25: astore_3
/*     */     //   26: aload_0
/*     */     //   27: invokevirtual 12	org/eclipse/jetty/client/ValidatingConnectionPool:unlock	()V
/*     */     //   30: aload_3
/*     */     //   31: athrow
/*     */     //   32: aload_2
/*     */     //   33: ifnonnull +9 -> 42
/*     */     //   36: aload_0
/*     */     //   37: aload_1
/*     */     //   38: invokespecial 26	org/eclipse/jetty/client/DuplexConnectionPool:remove	(Lorg/eclipse/jetty/client/api/Connection;)Z
/*     */     //   41: ireturn
/*     */     //   42: getstatic 3	org/eclipse/jetty/client/ValidatingConnectionPool:LOG	Lorg/eclipse/jetty/util/log/Logger;
/*     */     //   45: invokeinterface 19 1 0
/*     */     //   50: ifeq +21 -> 71
/*     */     //   53: getstatic 3	org/eclipse/jetty/client/ValidatingConnectionPool:LOG	Lorg/eclipse/jetty/util/log/Logger;
/*     */     //   56: ldc 27
/*     */     //   58: iconst_1
/*     */     //   59: anewarray 21	java/lang/Object
/*     */     //   62: dup
/*     */     //   63: iconst_0
/*     */     //   64: aload_1
/*     */     //   65: aastore
/*     */     //   66: invokeinterface 23 3 0
/*     */     //   71: aload_2
/*     */     //   72: invokevirtual 28	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:cancel	()Z
/*     */     //   75: istore_3
/*     */     //   76: iload_3
/*     */     //   77: ifeq +10 -> 87
/*     */     //   80: aload_0
/*     */     //   81: aload_1
/*     */     //   82: iconst_1
/*     */     //   83: invokevirtual 29	org/eclipse/jetty/client/ValidatingConnectionPool:remove	(Lorg/eclipse/jetty/client/api/Connection;Z)Z
/*     */     //   86: ireturn
/*     */     //   87: aload_0
/*     */     //   88: aload_1
/*     */     //   89: invokespecial 26	org/eclipse/jetty/client/DuplexConnectionPool:remove	(Lorg/eclipse/jetty/client/api/Connection;)Z
/*     */     //   92: ireturn
/*     */     // Line number table:
/*     */     //   Java source line #108	-> byte code offset #0
/*     */     //   Java source line #111	-> byte code offset #4
/*     */     //   Java source line #115	-> byte code offset #18
/*     */     //   Java source line #116	-> byte code offset #22
/*     */     //   Java source line #115	-> byte code offset #25
/*     */     //   Java source line #118	-> byte code offset #32
/*     */     //   Java source line #119	-> byte code offset #36
/*     */     //   Java source line #121	-> byte code offset #42
/*     */     //   Java source line #122	-> byte code offset #53
/*     */     //   Java source line #124	-> byte code offset #71
/*     */     //   Java source line #125	-> byte code offset #76
/*     */     //   Java source line #126	-> byte code offset #80
/*     */     //   Java source line #128	-> byte code offset #87
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	93	0	this	ValidatingConnectionPool
/*     */     //   0	93	1	connection	Connection
/*     */     //   17	2	2	holder	Holder
/*     */     //   32	40	2	holder	Holder
/*     */     //   25	6	3	localObject	Object
/*     */     //   75	2	3	cancelled	boolean
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	18	25	finally
/*     */   }
/*     */   
/*     */   public void dump(Appendable out, String indent)
/*     */     throws IOException
/*     */   {
/* 134 */     super.dump(out, indent);
/* 135 */     ContainerLifeCycle.dump(out, indent, new Collection[] { this.quarantine.values() });
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public String toString()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 9	org/eclipse/jetty/client/ValidatingConnectionPool:lock	()V
/*     */     //   4: aload_0
/*     */     //   5: getfield 2	org/eclipse/jetty/client/ValidatingConnectionPool:quarantine	Ljava/util/Map;
/*     */     //   8: invokeinterface 8 1 0
/*     */     //   13: istore_1
/*     */     //   14: aload_0
/*     */     //   15: invokevirtual 12	org/eclipse/jetty/client/ValidatingConnectionPool:unlock	()V
/*     */     //   18: goto +10 -> 28
/*     */     //   21: astore_2
/*     */     //   22: aload_0
/*     */     //   23: invokevirtual 12	org/eclipse/jetty/client/ValidatingConnectionPool:unlock	()V
/*     */     //   26: aload_2
/*     */     //   27: athrow
/*     */     //   28: ldc 34
/*     */     //   30: iconst_2
/*     */     //   31: anewarray 21	java/lang/Object
/*     */     //   34: dup
/*     */     //   35: iconst_0
/*     */     //   36: aload_0
/*     */     //   37: invokespecial 35	org/eclipse/jetty/client/DuplexConnectionPool:toString	()Ljava/lang/String;
/*     */     //   40: aastore
/*     */     //   41: dup
/*     */     //   42: iconst_1
/*     */     //   43: iload_1
/*     */     //   44: invokestatic 36	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   47: aastore
/*     */     //   48: invokestatic 37	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   51: areturn
/*     */     // Line number table:
/*     */     //   Java source line #142	-> byte code offset #0
/*     */     //   Java source line #145	-> byte code offset #4
/*     */     //   Java source line #149	-> byte code offset #14
/*     */     //   Java source line #150	-> byte code offset #18
/*     */     //   Java source line #149	-> byte code offset #21
/*     */     //   Java source line #151	-> byte code offset #28
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	52	0	this	ValidatingConnectionPool
/*     */     //   13	2	1	size	int
/*     */     //   28	16	1	size	int
/*     */     //   21	6	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   4	14	21	finally
/*     */   }
/*     */   
/*     */   private class Holder
/*     */     implements Runnable
/*     */   {
/* 156 */     private final long timestamp = System.nanoTime();
/* 157 */     private final AtomicBoolean done = new AtomicBoolean();
/*     */     private final Connection connection;
/*     */     public Scheduler.Task task;
/*     */     
/*     */     public Holder(Connection connection)
/*     */     {
/* 163 */       this.connection = connection;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void run()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 7	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:done	Ljava/util/concurrent/atomic/AtomicBoolean;
/*     */       //   4: iconst_0
/*     */       //   5: iconst_1
/*     */       //   6: invokevirtual 9	java/util/concurrent/atomic/AtomicBoolean:compareAndSet	(ZZ)Z
/*     */       //   9: ifeq +123 -> 132
/*     */       //   12: aload_0
/*     */       //   13: getfield 1	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:this$0	Lorg/eclipse/jetty/client/ValidatingConnectionPool;
/*     */       //   16: invokevirtual 10	org/eclipse/jetty/client/ValidatingConnectionPool:isClosed	()Z
/*     */       //   19: istore_1
/*     */       //   20: aload_0
/*     */       //   21: getfield 1	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:this$0	Lorg/eclipse/jetty/client/ValidatingConnectionPool;
/*     */       //   24: invokevirtual 11	org/eclipse/jetty/client/ValidatingConnectionPool:lock	()V
/*     */       //   27: invokestatic 12	org/eclipse/jetty/client/ValidatingConnectionPool:access$000	()Lorg/eclipse/jetty/util/log/Logger;
/*     */       //   30: invokeinterface 13 1 0
/*     */       //   35: ifeq +24 -> 59
/*     */       //   38: invokestatic 12	org/eclipse/jetty/client/ValidatingConnectionPool:access$000	()Lorg/eclipse/jetty/util/log/Logger;
/*     */       //   41: ldc 14
/*     */       //   43: iconst_1
/*     */       //   44: anewarray 15	java/lang/Object
/*     */       //   47: dup
/*     */       //   48: iconst_0
/*     */       //   49: aload_0
/*     */       //   50: getfield 8	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:connection	Lorg/eclipse/jetty/client/api/Connection;
/*     */       //   53: aastore
/*     */       //   54: invokeinterface 16 3 0
/*     */       //   59: aload_0
/*     */       //   60: getfield 1	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:this$0	Lorg/eclipse/jetty/client/ValidatingConnectionPool;
/*     */       //   63: invokestatic 17	org/eclipse/jetty/client/ValidatingConnectionPool:access$100	(Lorg/eclipse/jetty/client/ValidatingConnectionPool;)Ljava/util/Map;
/*     */       //   66: aload_0
/*     */       //   67: getfield 8	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:connection	Lorg/eclipse/jetty/client/api/Connection;
/*     */       //   70: invokeinterface 18 2 0
/*     */       //   75: pop
/*     */       //   76: iload_1
/*     */       //   77: ifne +15 -> 92
/*     */       //   80: aload_0
/*     */       //   81: getfield 1	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:this$0	Lorg/eclipse/jetty/client/ValidatingConnectionPool;
/*     */       //   84: aload_0
/*     */       //   85: getfield 8	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:connection	Lorg/eclipse/jetty/client/api/Connection;
/*     */       //   88: invokevirtual 19	org/eclipse/jetty/client/ValidatingConnectionPool:deactivate	(Lorg/eclipse/jetty/client/api/Connection;)Z
/*     */       //   91: pop
/*     */       //   92: aload_0
/*     */       //   93: getfield 1	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:this$0	Lorg/eclipse/jetty/client/ValidatingConnectionPool;
/*     */       //   96: invokevirtual 20	org/eclipse/jetty/client/ValidatingConnectionPool:unlock	()V
/*     */       //   99: goto +13 -> 112
/*     */       //   102: astore_2
/*     */       //   103: aload_0
/*     */       //   104: getfield 1	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:this$0	Lorg/eclipse/jetty/client/ValidatingConnectionPool;
/*     */       //   107: invokevirtual 20	org/eclipse/jetty/client/ValidatingConnectionPool:unlock	()V
/*     */       //   110: aload_2
/*     */       //   111: athrow
/*     */       //   112: aload_0
/*     */       //   113: getfield 1	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:this$0	Lorg/eclipse/jetty/client/ValidatingConnectionPool;
/*     */       //   116: aload_0
/*     */       //   117: getfield 8	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:connection	Lorg/eclipse/jetty/client/api/Connection;
/*     */       //   120: iload_1
/*     */       //   121: invokevirtual 21	org/eclipse/jetty/client/ValidatingConnectionPool:idle	(Lorg/eclipse/jetty/client/api/Connection;Z)Z
/*     */       //   124: pop
/*     */       //   125: aload_0
/*     */       //   126: getfield 1	org/eclipse/jetty/client/ValidatingConnectionPool$Holder:this$0	Lorg/eclipse/jetty/client/ValidatingConnectionPool;
/*     */       //   129: invokevirtual 22	org/eclipse/jetty/client/ValidatingConnectionPool:proceed	()V
/*     */       //   132: return
/*     */       // Line number table:
/*     */       //   Java source line #169	-> byte code offset #0
/*     */       //   Java source line #171	-> byte code offset #12
/*     */       //   Java source line #172	-> byte code offset #20
/*     */       //   Java source line #175	-> byte code offset #27
/*     */       //   Java source line #176	-> byte code offset #38
/*     */       //   Java source line #177	-> byte code offset #59
/*     */       //   Java source line #178	-> byte code offset #76
/*     */       //   Java source line #179	-> byte code offset #80
/*     */       //   Java source line #183	-> byte code offset #92
/*     */       //   Java source line #184	-> byte code offset #99
/*     */       //   Java source line #183	-> byte code offset #102
/*     */       //   Java source line #186	-> byte code offset #112
/*     */       //   Java source line #187	-> byte code offset #125
/*     */       //   Java source line #189	-> byte code offset #132
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	133	0	this	Holder
/*     */       //   19	102	1	closed	boolean
/*     */       //   102	9	2	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   27	92	102	finally
/*     */     }
/*     */     
/*     */     public boolean cancel()
/*     */     {
/* 193 */       if (this.done.compareAndSet(false, true))
/*     */       {
/* 195 */         this.task.cancel();
/* 196 */         return true;
/*     */       }
/* 198 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 204 */       return String.format("%s[validationLeft=%dms]", new Object[] { this.connection, 
/*     */       
/* 206 */         Long.valueOf(ValidatingConnectionPool.this.timeout - TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - this.timestamp)) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\client\ValidatingConnectionPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */