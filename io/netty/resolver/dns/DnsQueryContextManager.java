/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.util.NetUtil;
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import io.netty.util.collection.IntObjectMap;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DnsQueryContextManager
/*     */ {
/*  38 */   final Map<InetSocketAddress, IntObjectMap<DnsQueryContext>> map = new HashMap();
/*     */   
/*     */   int add(DnsQueryContext qCtx)
/*     */   {
/*  42 */     IntObjectMap<DnsQueryContext> contexts = getOrCreateContextMap(qCtx.nameServerAddr());
/*     */     
/*  44 */     int id = PlatformDependent.threadLocalRandom().nextInt(65535) + 1;
/*  45 */     int maxTries = 131070;
/*  46 */     int tries = 0;
/*     */     
/*  48 */     synchronized (contexts) {
/*     */       do {
/*  50 */         if (!contexts.containsKey(id)) {
/*  51 */           contexts.put(id, qCtx);
/*  52 */           return id;
/*     */         }
/*     */         
/*  55 */         id = id + 1 & 0xFFFF;
/*     */         
/*  57 */         tries++; } while (tries < 131070);
/*  58 */       throw new IllegalStateException("query ID space exhausted: " + qCtx.question());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   DnsQueryContext get(InetSocketAddress nameServerAddr, int id)
/*     */   {
/*  65 */     IntObjectMap<DnsQueryContext> contexts = getContextMap(nameServerAddr);
/*     */     DnsQueryContext qCtx;
/*  67 */     DnsQueryContext qCtx; if (contexts != null) { DnsQueryContext qCtx;
/*  68 */       synchronized (contexts) {
/*  69 */         qCtx = (DnsQueryContext)contexts.get(id);
/*     */       }
/*     */     } else {
/*  72 */       qCtx = null;
/*     */     }
/*     */     
/*  75 */     return qCtx;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   DnsQueryContext remove(InetSocketAddress nameServerAddr, int id)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: invokespecial 22	io/netty/resolver/dns/DnsQueryContextManager:getContextMap	(Ljava/net/InetSocketAddress;)Lio/netty/util/collection/IntObjectMap;
/*     */     //   5: astore_3
/*     */     //   6: aload_3
/*     */     //   7: ifnonnull +5 -> 12
/*     */     //   10: aconst_null
/*     */     //   11: areturn
/*     */     //   12: aload_3
/*     */     //   13: dup
/*     */     //   14: astore 4
/*     */     //   16: monitorenter
/*     */     //   17: aload_3
/*     */     //   18: iload_2
/*     */     //   19: invokeinterface 25 2 0
/*     */     //   24: checkcast 24	io/netty/resolver/dns/DnsQueryContext
/*     */     //   27: aload 4
/*     */     //   29: monitorexit
/*     */     //   30: areturn
/*     */     //   31: astore 5
/*     */     //   33: aload 4
/*     */     //   35: monitorexit
/*     */     //   36: aload 5
/*     */     //   38: athrow
/*     */     // Line number table:
/*     */     //   Java source line #79	-> byte code offset #0
/*     */     //   Java source line #80	-> byte code offset #6
/*     */     //   Java source line #81	-> byte code offset #10
/*     */     //   Java source line #84	-> byte code offset #12
/*     */     //   Java source line #85	-> byte code offset #17
/*     */     //   Java source line #86	-> byte code offset #31
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	39	0	this	DnsQueryContextManager
/*     */     //   0	39	1	nameServerAddr	InetSocketAddress
/*     */     //   0	39	2	id	int
/*     */     //   5	13	3	contexts	IntObjectMap<DnsQueryContext>
/*     */     //   14	20	4	Ljava/lang/Object;	Object
/*     */     //   31	6	5	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   17	30	31	finally
/*     */     //   31	36	31	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private IntObjectMap<DnsQueryContext> getContextMap(InetSocketAddress nameServerAddr)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	io/netty/resolver/dns/DnsQueryContextManager:map	Ljava/util/Map;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 4	io/netty/resolver/dns/DnsQueryContextManager:map	Ljava/util/Map;
/*     */     //   11: aload_1
/*     */     //   12: invokeinterface 26 2 0
/*     */     //   17: checkcast 27	io/netty/util/collection/IntObjectMap
/*     */     //   20: aload_2
/*     */     //   21: monitorexit
/*     */     //   22: areturn
/*     */     //   23: astore_3
/*     */     //   24: aload_2
/*     */     //   25: monitorexit
/*     */     //   26: aload_3
/*     */     //   27: athrow
/*     */     // Line number table:
/*     */     //   Java source line #90	-> byte code offset #0
/*     */     //   Java source line #91	-> byte code offset #7
/*     */     //   Java source line #92	-> byte code offset #23
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	28	0	this	DnsQueryContextManager
/*     */     //   0	28	1	nameServerAddr	InetSocketAddress
/*     */     //   5	20	2	Ljava/lang/Object;	Object
/*     */     //   23	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	22	23	finally
/*     */     //   23	26	23	finally
/*     */   }
/*     */   
/*     */   private IntObjectMap<DnsQueryContext> getOrCreateContextMap(InetSocketAddress nameServerAddr)
/*     */   {
/*  96 */     synchronized (this.map) {
/*  97 */       IntObjectMap<DnsQueryContext> contexts = (IntObjectMap)this.map.get(nameServerAddr);
/*  98 */       if (contexts != null) {
/*  99 */         return contexts;
/*     */       }
/*     */       
/* 102 */       IntObjectMap<DnsQueryContext> newContexts = new IntObjectHashMap();
/* 103 */       InetAddress a = nameServerAddr.getAddress();
/* 104 */       int port = nameServerAddr.getPort();
/* 105 */       this.map.put(nameServerAddr, newContexts);
/*     */       
/* 107 */       if ((a instanceof Inet4Address))
/*     */       {
/* 109 */         Inet4Address a4 = (Inet4Address)a;
/* 110 */         if (a4.isLoopbackAddress()) {
/* 111 */           this.map.put(new InetSocketAddress(NetUtil.LOCALHOST6, port), newContexts);
/*     */         } else {
/* 113 */           this.map.put(new InetSocketAddress(toCompactAddress(a4), port), newContexts);
/*     */         }
/* 115 */       } else if ((a instanceof Inet6Address))
/*     */       {
/* 117 */         Inet6Address a6 = (Inet6Address)a;
/* 118 */         if (a6.isLoopbackAddress()) {
/* 119 */           this.map.put(new InetSocketAddress(NetUtil.LOCALHOST4, port), newContexts);
/* 120 */         } else if (a6.isIPv4CompatibleAddress()) {
/* 121 */           this.map.put(new InetSocketAddress(toIPv4Address(a6), port), newContexts);
/*     */         }
/*     */       }
/*     */       
/* 125 */       return newContexts;
/*     */     }
/*     */   }
/*     */   
/*     */   private static Inet6Address toCompactAddress(Inet4Address a4) {
/* 130 */     byte[] b4 = a4.getAddress();
/* 131 */     byte[] b6 = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, b4[0], b4[1], b4[2], b4[3] };
/*     */     try {
/* 133 */       return (Inet6Address)InetAddress.getByAddress(b6);
/*     */     } catch (UnknownHostException e) {
/* 135 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static Inet4Address toIPv4Address(Inet6Address a6) {
/* 140 */     byte[] b6 = a6.getAddress();
/* 141 */     byte[] b4 = { b6[12], b6[13], b6[14], b6[15] };
/*     */     try {
/* 143 */       return (Inet4Address)InetAddress.getByAddress(b4);
/*     */     } catch (UnknownHostException e) {
/* 145 */       throw new Error(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\DnsQueryContextManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */