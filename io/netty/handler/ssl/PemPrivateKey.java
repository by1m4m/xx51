/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.IllegalReferenceCountException;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.security.PrivateKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PemPrivateKey
/*     */   extends AbstractReferenceCounted
/*     */   implements PrivateKey, PemEncoded
/*     */ {
/*     */   private static final long serialVersionUID = 7978017465645018936L;
/*  46 */   private static final byte[] BEGIN_PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n".getBytes(CharsetUtil.US_ASCII);
/*  47 */   private static final byte[] END_PRIVATE_KEY = "\n-----END PRIVATE KEY-----\n".getBytes(CharsetUtil.US_ASCII);
/*     */   private static final String PKCS8_FORMAT = "PKCS#8";
/*     */   private final ByteBuf content;
/*     */   
/*     */   /* Error */
/*     */   static PemEncoded toPEM(io.netty.buffer.ByteBufAllocator allocator, boolean useDirect, PrivateKey key)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: instanceof 1
/*     */     //   4: ifeq +13 -> 17
/*     */     //   7: aload_2
/*     */     //   8: checkcast 1	io/netty/handler/ssl/PemEncoded
/*     */     //   11: invokeinterface 2 1 0
/*     */     //   16: areturn
/*     */     //   17: aload_2
/*     */     //   18: invokeinterface 3 1 0
/*     */     //   23: astore_3
/*     */     //   24: aload_3
/*     */     //   25: ifnonnull +36 -> 61
/*     */     //   28: new 4	java/lang/IllegalArgumentException
/*     */     //   31: dup
/*     */     //   32: new 5	java/lang/StringBuilder
/*     */     //   35: dup
/*     */     //   36: invokespecial 6	java/lang/StringBuilder:<init>	()V
/*     */     //   39: aload_2
/*     */     //   40: invokevirtual 7	java/lang/Object:getClass	()Ljava/lang/Class;
/*     */     //   43: invokevirtual 8	java/lang/Class:getName	()Ljava/lang/String;
/*     */     //   46: invokevirtual 9	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   49: ldc 10
/*     */     //   51: invokevirtual 9	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   54: invokevirtual 11	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   57: invokespecial 12	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
/*     */     //   60: athrow
/*     */     //   61: aload_3
/*     */     //   62: invokestatic 13	io/netty/buffer/Unpooled:wrappedBuffer	([B)Lio/netty/buffer/ByteBuf;
/*     */     //   65: astore 4
/*     */     //   67: aload_0
/*     */     //   68: aload 4
/*     */     //   70: invokestatic 14	io/netty/handler/ssl/SslUtils:toBase64	(Lio/netty/buffer/ByteBufAllocator;Lio/netty/buffer/ByteBuf;)Lio/netty/buffer/ByteBuf;
/*     */     //   73: astore 5
/*     */     //   75: getstatic 15	io/netty/handler/ssl/PemPrivateKey:BEGIN_PRIVATE_KEY	[B
/*     */     //   78: arraylength
/*     */     //   79: aload 5
/*     */     //   81: invokevirtual 16	io/netty/buffer/ByteBuf:readableBytes	()I
/*     */     //   84: iadd
/*     */     //   85: getstatic 17	io/netty/handler/ssl/PemPrivateKey:END_PRIVATE_KEY	[B
/*     */     //   88: arraylength
/*     */     //   89: iadd
/*     */     //   90: istore 6
/*     */     //   92: iconst_0
/*     */     //   93: istore 7
/*     */     //   95: iload_1
/*     */     //   96: ifeq +14 -> 110
/*     */     //   99: aload_0
/*     */     //   100: iload 6
/*     */     //   102: invokeinterface 18 2 0
/*     */     //   107: goto +11 -> 118
/*     */     //   110: aload_0
/*     */     //   111: iload 6
/*     */     //   113: invokeinterface 19 2 0
/*     */     //   118: astore 8
/*     */     //   120: aload 8
/*     */     //   122: getstatic 15	io/netty/handler/ssl/PemPrivateKey:BEGIN_PRIVATE_KEY	[B
/*     */     //   125: invokevirtual 20	io/netty/buffer/ByteBuf:writeBytes	([B)Lio/netty/buffer/ByteBuf;
/*     */     //   128: pop
/*     */     //   129: aload 8
/*     */     //   131: aload 5
/*     */     //   133: invokevirtual 21	io/netty/buffer/ByteBuf:writeBytes	(Lio/netty/buffer/ByteBuf;)Lio/netty/buffer/ByteBuf;
/*     */     //   136: pop
/*     */     //   137: aload 8
/*     */     //   139: getstatic 17	io/netty/handler/ssl/PemPrivateKey:END_PRIVATE_KEY	[B
/*     */     //   142: invokevirtual 20	io/netty/buffer/ByteBuf:writeBytes	([B)Lio/netty/buffer/ByteBuf;
/*     */     //   145: pop
/*     */     //   146: new 22	io/netty/handler/ssl/PemValue
/*     */     //   149: dup
/*     */     //   150: aload 8
/*     */     //   152: iconst_1
/*     */     //   153: invokespecial 23	io/netty/handler/ssl/PemValue:<init>	(Lio/netty/buffer/ByteBuf;Z)V
/*     */     //   156: astore 9
/*     */     //   158: iconst_1
/*     */     //   159: istore 7
/*     */     //   161: aload 9
/*     */     //   163: astore 10
/*     */     //   165: iload 7
/*     */     //   167: ifne +8 -> 175
/*     */     //   170: aload 8
/*     */     //   172: invokestatic 24	io/netty/handler/ssl/SslUtils:zerooutAndRelease	(Lio/netty/buffer/ByteBuf;)V
/*     */     //   175: aload 5
/*     */     //   177: invokestatic 24	io/netty/handler/ssl/SslUtils:zerooutAndRelease	(Lio/netty/buffer/ByteBuf;)V
/*     */     //   180: aload 4
/*     */     //   182: invokestatic 24	io/netty/handler/ssl/SslUtils:zerooutAndRelease	(Lio/netty/buffer/ByteBuf;)V
/*     */     //   185: aload 10
/*     */     //   187: areturn
/*     */     //   188: astore 11
/*     */     //   190: iload 7
/*     */     //   192: ifne +8 -> 200
/*     */     //   195: aload 8
/*     */     //   197: invokestatic 24	io/netty/handler/ssl/SslUtils:zerooutAndRelease	(Lio/netty/buffer/ByteBuf;)V
/*     */     //   200: aload 11
/*     */     //   202: athrow
/*     */     //   203: astore 12
/*     */     //   205: aload 5
/*     */     //   207: invokestatic 24	io/netty/handler/ssl/SslUtils:zerooutAndRelease	(Lio/netty/buffer/ByteBuf;)V
/*     */     //   210: aload 12
/*     */     //   212: athrow
/*     */     //   213: astore 13
/*     */     //   215: aload 4
/*     */     //   217: invokestatic 24	io/netty/handler/ssl/SslUtils:zerooutAndRelease	(Lio/netty/buffer/ByteBuf;)V
/*     */     //   220: aload 13
/*     */     //   222: athrow
/*     */     // Line number table:
/*     */     //   Java source line #59	-> byte code offset #0
/*     */     //   Java source line #60	-> byte code offset #7
/*     */     //   Java source line #63	-> byte code offset #17
/*     */     //   Java source line #64	-> byte code offset #24
/*     */     //   Java source line #65	-> byte code offset #28
/*     */     //   Java source line #68	-> byte code offset #61
/*     */     //   Java source line #70	-> byte code offset #67
/*     */     //   Java source line #72	-> byte code offset #75
/*     */     //   Java source line #74	-> byte code offset #92
/*     */     //   Java source line #75	-> byte code offset #95
/*     */     //   Java source line #77	-> byte code offset #120
/*     */     //   Java source line #78	-> byte code offset #129
/*     */     //   Java source line #79	-> byte code offset #137
/*     */     //   Java source line #81	-> byte code offset #146
/*     */     //   Java source line #82	-> byte code offset #158
/*     */     //   Java source line #83	-> byte code offset #161
/*     */     //   Java source line #86	-> byte code offset #165
/*     */     //   Java source line #87	-> byte code offset #170
/*     */     //   Java source line #91	-> byte code offset #175
/*     */     //   Java source line #94	-> byte code offset #180
/*     */     //   Java source line #83	-> byte code offset #185
/*     */     //   Java source line #86	-> byte code offset #188
/*     */     //   Java source line #87	-> byte code offset #195
/*     */     //   Java source line #89	-> byte code offset #200
/*     */     //   Java source line #91	-> byte code offset #203
/*     */     //   Java source line #92	-> byte code offset #210
/*     */     //   Java source line #94	-> byte code offset #213
/*     */     //   Java source line #95	-> byte code offset #220
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	223	0	allocator	io.netty.buffer.ByteBufAllocator
/*     */     //   0	223	1	useDirect	boolean
/*     */     //   0	223	2	key	PrivateKey
/*     */     //   23	39	3	bytes	byte[]
/*     */     //   65	151	4	encoded	ByteBuf
/*     */     //   73	133	5	base64	ByteBuf
/*     */     //   90	22	6	size	int
/*     */     //   93	98	7	success	boolean
/*     */     //   118	78	8	pem	ByteBuf
/*     */     //   156	6	9	value	PemValue
/*     */     //   163	23	10	localPemValue1	PemValue
/*     */     //   188	13	11	localObject1	Object
/*     */     //   203	8	12	localObject2	Object
/*     */     //   213	8	13	localObject3	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   120	165	188	finally
/*     */     //   188	190	188	finally
/*     */     //   75	175	203	finally
/*     */     //   188	205	203	finally
/*     */     //   67	180	213	finally
/*     */     //   188	215	213	finally
/*     */   }
/*     */   
/*     */   public static PemPrivateKey valueOf(byte[] key)
/*     */   {
/* 105 */     return valueOf(Unpooled.wrappedBuffer(key));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PemPrivateKey valueOf(ByteBuf key)
/*     */   {
/* 115 */     return new PemPrivateKey(key);
/*     */   }
/*     */   
/*     */ 
/*     */   private PemPrivateKey(ByteBuf content)
/*     */   {
/* 121 */     this.content = ((ByteBuf)ObjectUtil.checkNotNull(content, "content"));
/*     */   }
/*     */   
/*     */   public boolean isSensitive()
/*     */   {
/* 126 */     return true;
/*     */   }
/*     */   
/*     */   public ByteBuf content()
/*     */   {
/* 131 */     int count = refCnt();
/* 132 */     if (count <= 0) {
/* 133 */       throw new IllegalReferenceCountException(count);
/*     */     }
/*     */     
/* 136 */     return this.content;
/*     */   }
/*     */   
/*     */   public PemPrivateKey copy()
/*     */   {
/* 141 */     return replace(this.content.copy());
/*     */   }
/*     */   
/*     */   public PemPrivateKey duplicate()
/*     */   {
/* 146 */     return replace(this.content.duplicate());
/*     */   }
/*     */   
/*     */   public PemPrivateKey retainedDuplicate()
/*     */   {
/* 151 */     return replace(this.content.retainedDuplicate());
/*     */   }
/*     */   
/*     */   public PemPrivateKey replace(ByteBuf content)
/*     */   {
/* 156 */     return new PemPrivateKey(content);
/*     */   }
/*     */   
/*     */   public PemPrivateKey touch()
/*     */   {
/* 161 */     this.content.touch();
/* 162 */     return this;
/*     */   }
/*     */   
/*     */   public PemPrivateKey touch(Object hint)
/*     */   {
/* 167 */     this.content.touch(hint);
/* 168 */     return this;
/*     */   }
/*     */   
/*     */   public PemPrivateKey retain()
/*     */   {
/* 173 */     return (PemPrivateKey)super.retain();
/*     */   }
/*     */   
/*     */   public PemPrivateKey retain(int increment)
/*     */   {
/* 178 */     return (PemPrivateKey)super.retain(increment);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void deallocate()
/*     */   {
/* 185 */     SslUtils.zerooutAndRelease(this.content);
/*     */   }
/*     */   
/*     */   public byte[] getEncoded()
/*     */   {
/* 190 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public String getAlgorithm()
/*     */   {
/* 195 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public String getFormat()
/*     */   {
/* 200 */     return "PKCS#8";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 211 */     release(refCnt());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isDestroyed()
/*     */   {
/* 222 */     return refCnt() == 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\ssl\PemPrivateKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */