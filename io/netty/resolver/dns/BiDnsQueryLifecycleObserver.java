/*    */ package io.netty.resolver.dns;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class BiDnsQueryLifecycleObserver
/*    */   implements DnsQueryLifecycleObserver
/*    */ {
/*    */   private final DnsQueryLifecycleObserver a;
/*    */   private final DnsQueryLifecycleObserver b;
/*    */   
/*    */   public BiDnsQueryLifecycleObserver(DnsQueryLifecycleObserver a, DnsQueryLifecycleObserver b)
/*    */   {
/* 42 */     this.a = ((DnsQueryLifecycleObserver)ObjectUtil.checkNotNull(a, "a"));
/* 43 */     this.b = ((DnsQueryLifecycleObserver)ObjectUtil.checkNotNull(b, "b"));
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void queryWritten(java.net.InetSocketAddress dnsServerAddress, io.netty.channel.ChannelFuture future)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 5	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:a	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   4: aload_1
/*    */     //   5: aload_2
/*    */     //   6: invokeinterface 8 3 0
/*    */     //   11: aload_0
/*    */     //   12: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   15: aload_1
/*    */     //   16: aload_2
/*    */     //   17: invokeinterface 8 3 0
/*    */     //   22: goto +17 -> 39
/*    */     //   25: astore_3
/*    */     //   26: aload_0
/*    */     //   27: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   30: aload_1
/*    */     //   31: aload_2
/*    */     //   32: invokeinterface 8 3 0
/*    */     //   37: aload_3
/*    */     //   38: athrow
/*    */     //   39: return
/*    */     // Line number table:
/*    */     //   Java source line #49	-> byte code offset #0
/*    */     //   Java source line #51	-> byte code offset #11
/*    */     //   Java source line #52	-> byte code offset #22
/*    */     //   Java source line #51	-> byte code offset #25
/*    */     //   Java source line #52	-> byte code offset #37
/*    */     //   Java source line #53	-> byte code offset #39
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	40	0	this	BiDnsQueryLifecycleObserver
/*    */     //   0	40	1	dnsServerAddress	java.net.InetSocketAddress
/*    */     //   0	40	2	future	io.netty.channel.ChannelFuture
/*    */     //   25	13	3	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	11	25	finally
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void queryCancelled(int queriesRemaining)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 5	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:a	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   4: iload_1
/*    */     //   5: invokeinterface 9 2 0
/*    */     //   10: aload_0
/*    */     //   11: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   14: iload_1
/*    */     //   15: invokeinterface 9 2 0
/*    */     //   20: goto +16 -> 36
/*    */     //   23: astore_2
/*    */     //   24: aload_0
/*    */     //   25: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   28: iload_1
/*    */     //   29: invokeinterface 9 2 0
/*    */     //   34: aload_2
/*    */     //   35: athrow
/*    */     //   36: return
/*    */     // Line number table:
/*    */     //   Java source line #58	-> byte code offset #0
/*    */     //   Java source line #60	-> byte code offset #10
/*    */     //   Java source line #61	-> byte code offset #20
/*    */     //   Java source line #60	-> byte code offset #23
/*    */     //   Java source line #61	-> byte code offset #34
/*    */     //   Java source line #62	-> byte code offset #36
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	37	0	this	BiDnsQueryLifecycleObserver
/*    */     //   0	37	1	queriesRemaining	int
/*    */     //   23	12	2	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	10	23	finally
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public DnsQueryLifecycleObserver queryRedirected(java.util.List<java.net.InetSocketAddress> nameServers)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 5	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:a	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   4: aload_1
/*    */     //   5: invokeinterface 10 2 0
/*    */     //   10: pop
/*    */     //   11: aload_0
/*    */     //   12: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   15: aload_1
/*    */     //   16: invokeinterface 10 2 0
/*    */     //   21: pop
/*    */     //   22: goto +17 -> 39
/*    */     //   25: astore_2
/*    */     //   26: aload_0
/*    */     //   27: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   30: aload_1
/*    */     //   31: invokeinterface 10 2 0
/*    */     //   36: pop
/*    */     //   37: aload_2
/*    */     //   38: athrow
/*    */     //   39: aload_0
/*    */     //   40: areturn
/*    */     // Line number table:
/*    */     //   Java source line #67	-> byte code offset #0
/*    */     //   Java source line #69	-> byte code offset #11
/*    */     //   Java source line #70	-> byte code offset #22
/*    */     //   Java source line #69	-> byte code offset #25
/*    */     //   Java source line #70	-> byte code offset #37
/*    */     //   Java source line #71	-> byte code offset #39
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	41	0	this	BiDnsQueryLifecycleObserver
/*    */     //   0	41	1	nameServers	java.util.List<java.net.InetSocketAddress>
/*    */     //   25	13	2	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	11	25	finally
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public DnsQueryLifecycleObserver queryCNAMEd(io.netty.handler.codec.dns.DnsQuestion cnameQuestion)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 5	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:a	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   4: aload_1
/*    */     //   5: invokeinterface 11 2 0
/*    */     //   10: pop
/*    */     //   11: aload_0
/*    */     //   12: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   15: aload_1
/*    */     //   16: invokeinterface 11 2 0
/*    */     //   21: pop
/*    */     //   22: goto +17 -> 39
/*    */     //   25: astore_2
/*    */     //   26: aload_0
/*    */     //   27: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   30: aload_1
/*    */     //   31: invokeinterface 11 2 0
/*    */     //   36: pop
/*    */     //   37: aload_2
/*    */     //   38: athrow
/*    */     //   39: aload_0
/*    */     //   40: areturn
/*    */     // Line number table:
/*    */     //   Java source line #77	-> byte code offset #0
/*    */     //   Java source line #79	-> byte code offset #11
/*    */     //   Java source line #80	-> byte code offset #22
/*    */     //   Java source line #79	-> byte code offset #25
/*    */     //   Java source line #80	-> byte code offset #37
/*    */     //   Java source line #81	-> byte code offset #39
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	41	0	this	BiDnsQueryLifecycleObserver
/*    */     //   0	41	1	cnameQuestion	io.netty.handler.codec.dns.DnsQuestion
/*    */     //   25	13	2	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	11	25	finally
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public DnsQueryLifecycleObserver queryNoAnswer(io.netty.handler.codec.dns.DnsResponseCode code)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 5	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:a	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   4: aload_1
/*    */     //   5: invokeinterface 12 2 0
/*    */     //   10: pop
/*    */     //   11: aload_0
/*    */     //   12: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   15: aload_1
/*    */     //   16: invokeinterface 12 2 0
/*    */     //   21: pop
/*    */     //   22: goto +17 -> 39
/*    */     //   25: astore_2
/*    */     //   26: aload_0
/*    */     //   27: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   30: aload_1
/*    */     //   31: invokeinterface 12 2 0
/*    */     //   36: pop
/*    */     //   37: aload_2
/*    */     //   38: athrow
/*    */     //   39: aload_0
/*    */     //   40: areturn
/*    */     // Line number table:
/*    */     //   Java source line #87	-> byte code offset #0
/*    */     //   Java source line #89	-> byte code offset #11
/*    */     //   Java source line #90	-> byte code offset #22
/*    */     //   Java source line #89	-> byte code offset #25
/*    */     //   Java source line #90	-> byte code offset #37
/*    */     //   Java source line #91	-> byte code offset #39
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	41	0	this	BiDnsQueryLifecycleObserver
/*    */     //   0	41	1	code	io.netty.handler.codec.dns.DnsResponseCode
/*    */     //   25	13	2	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	11	25	finally
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void queryFailed(Throwable cause)
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 5	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:a	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   4: aload_1
/*    */     //   5: invokeinterface 13 2 0
/*    */     //   10: aload_0
/*    */     //   11: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   14: aload_1
/*    */     //   15: invokeinterface 13 2 0
/*    */     //   20: goto +16 -> 36
/*    */     //   23: astore_2
/*    */     //   24: aload_0
/*    */     //   25: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   28: aload_1
/*    */     //   29: invokeinterface 13 2 0
/*    */     //   34: aload_2
/*    */     //   35: athrow
/*    */     //   36: return
/*    */     // Line number table:
/*    */     //   Java source line #97	-> byte code offset #0
/*    */     //   Java source line #99	-> byte code offset #10
/*    */     //   Java source line #100	-> byte code offset #20
/*    */     //   Java source line #99	-> byte code offset #23
/*    */     //   Java source line #100	-> byte code offset #34
/*    */     //   Java source line #101	-> byte code offset #36
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	37	0	this	BiDnsQueryLifecycleObserver
/*    */     //   0	37	1	cause	Throwable
/*    */     //   23	12	2	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	10	23	finally
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public void querySucceed()
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: getfield 5	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:a	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   4: invokeinterface 14 1 0
/*    */     //   9: aload_0
/*    */     //   10: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   13: invokeinterface 14 1 0
/*    */     //   18: goto +15 -> 33
/*    */     //   21: astore_1
/*    */     //   22: aload_0
/*    */     //   23: getfield 7	io/netty/resolver/dns/BiDnsQueryLifecycleObserver:b	Lio/netty/resolver/dns/DnsQueryLifecycleObserver;
/*    */     //   26: invokeinterface 14 1 0
/*    */     //   31: aload_1
/*    */     //   32: athrow
/*    */     //   33: return
/*    */     // Line number table:
/*    */     //   Java source line #106	-> byte code offset #0
/*    */     //   Java source line #108	-> byte code offset #9
/*    */     //   Java source line #109	-> byte code offset #18
/*    */     //   Java source line #108	-> byte code offset #21
/*    */     //   Java source line #109	-> byte code offset #31
/*    */     //   Java source line #110	-> byte code offset #33
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	34	0	this	BiDnsQueryLifecycleObserver
/*    */     //   21	11	1	localObject	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   0	9	21	finally
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\resolver\dns\BiDnsQueryLifecycleObserver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */