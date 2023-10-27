/*     */ package org.eclipse.jetty.http;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.eclipse.jetty.util.ArrayTernaryTrie;
/*     */ import org.eclipse.jetty.util.ArrayTrie;
/*     */ import org.eclipse.jetty.util.StringUtil;
/*     */ import org.eclipse.jetty.util.Trie;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum HttpMethod
/*     */ {
/*  34 */   GET, 
/*  35 */   POST, 
/*  36 */   HEAD, 
/*  37 */   PUT, 
/*  38 */   OPTIONS, 
/*  39 */   DELETE, 
/*  40 */   TRACE, 
/*  41 */   CONNECT, 
/*  42 */   MOVE, 
/*  43 */   PROXY, 
/*  44 */   PRI;
/*     */   
/*     */ 
/*     */   public static final Trie<HttpMethod> INSENSITIVE_CACHE;
/*     */   
/*     */   public static final Trie<HttpMethod> CACHE;
/*     */   
/*     */   private final ByteBuffer _buffer;
/*     */   private final byte[] _bytes;
/*     */   
/*     */   public static HttpMethod lookAheadGet(byte[] bytes, int position, int limit)
/*     */   {
/*  56 */     int length = limit - position;
/*  57 */     if (length < 4)
/*  58 */       return null;
/*  59 */     switch (bytes[position])
/*     */     {
/*     */     case 71: 
/*  62 */       if ((bytes[(position + 1)] == 69) && (bytes[(position + 2)] == 84) && (bytes[(position + 3)] == 32))
/*  63 */         return GET;
/*     */       break;
/*     */     case 80: 
/*  66 */       if ((bytes[(position + 1)] == 79) && (bytes[(position + 2)] == 83) && (bytes[(position + 3)] == 84) && (length >= 5) && (bytes[(position + 4)] == 32))
/*  67 */         return POST;
/*  68 */       if ((bytes[(position + 1)] == 82) && (bytes[(position + 2)] == 79) && (bytes[(position + 3)] == 88) && (length >= 6) && (bytes[(position + 4)] == 89) && (bytes[(position + 5)] == 32))
/*  69 */         return PROXY;
/*  70 */       if ((bytes[(position + 1)] == 85) && (bytes[(position + 2)] == 84) && (bytes[(position + 3)] == 32))
/*  71 */         return PUT;
/*  72 */       if ((bytes[(position + 1)] == 82) && (bytes[(position + 2)] == 73) && (bytes[(position + 3)] == 32))
/*  73 */         return PRI;
/*     */       break;
/*     */     case 72: 
/*  76 */       if ((bytes[(position + 1)] == 69) && (bytes[(position + 2)] == 65) && (bytes[(position + 3)] == 68) && (length >= 5) && (bytes[(position + 4)] == 32))
/*  77 */         return HEAD;
/*     */       break;
/*     */     case 79: 
/*  80 */       if ((bytes[(position + 1)] == 80) && (bytes[(position + 2)] == 84) && (bytes[(position + 3)] == 73) && (length >= 8) && (bytes[(position + 4)] == 79) && (bytes[(position + 5)] == 78) && (bytes[(position + 6)] == 83) && (bytes[(position + 7)] == 32))
/*     */       {
/*  82 */         return OPTIONS; }
/*     */       break;
/*     */     case 68: 
/*  85 */       if ((bytes[(position + 1)] == 69) && (bytes[(position + 2)] == 76) && (bytes[(position + 3)] == 69) && (length >= 7) && (bytes[(position + 4)] == 84) && (bytes[(position + 5)] == 69) && (bytes[(position + 6)] == 32))
/*     */       {
/*  87 */         return DELETE; }
/*     */       break;
/*     */     case 84: 
/*  90 */       if ((bytes[(position + 1)] == 82) && (bytes[(position + 2)] == 65) && (bytes[(position + 3)] == 67) && (length >= 6) && (bytes[(position + 4)] == 69) && (bytes[(position + 5)] == 32))
/*     */       {
/*  92 */         return TRACE; }
/*     */       break;
/*     */     case 67: 
/*  95 */       if ((bytes[(position + 1)] == 79) && (bytes[(position + 2)] == 78) && (bytes[(position + 3)] == 78) && (length >= 8) && (bytes[(position + 4)] == 69) && (bytes[(position + 5)] == 67) && (bytes[(position + 6)] == 84) && (bytes[(position + 7)] == 32))
/*     */       {
/*  97 */         return CONNECT; }
/*     */       break;
/*     */     case 77: 
/* 100 */       if ((bytes[(position + 1)] == 79) && (bytes[(position + 2)] == 86) && (bytes[(position + 3)] == 69) && (length >= 5) && (bytes[(position + 4)] == 32)) {
/* 101 */         return MOVE;
/*     */       }
/*     */       
/*     */       break;
/*     */     }
/*     */     
/* 107 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HttpMethod lookAheadGet(ByteBuffer buffer)
/*     */   {
/* 118 */     if (buffer.hasArray()) {
/* 119 */       return lookAheadGet(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.arrayOffset() + buffer.limit());
/*     */     }
/* 121 */     int l = buffer.remaining();
/* 122 */     if (l >= 4)
/*     */     {
/* 124 */       HttpMethod m = (HttpMethod)CACHE.getBest(buffer, 0, l);
/* 125 */       if (m != null)
/*     */       {
/* 127 */         int ml = m.asString().length();
/* 128 */         if ((l > ml) && (buffer.get(buffer.position() + ml) == 32))
/* 129 */           return m;
/*     */       }
/*     */     }
/* 132 */     return null;
/*     */   }
/*     */   
/*     */   static {
/* 136 */     INSENSITIVE_CACHE = new ArrayTrie();
/*     */     
/*     */ 
/* 139 */     for (HttpMethod method : values()) {
/* 140 */       INSENSITIVE_CACHE.put(method.toString(), method);
/*     */     }
/*     */     
/*     */ 
/* 144 */     CACHE = new ArrayTernaryTrie(false);
/*     */     
/*     */ 
/* 147 */     for (HttpMethod method : values()) {
/* 148 */       CACHE.put(method.toString(), method);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private HttpMethod()
/*     */   {
/* 158 */     this._bytes = StringUtil.getBytes(toString());
/* 159 */     this._buffer = ByteBuffer.wrap(this._bytes);
/*     */   }
/*     */   
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/* 165 */     return this._bytes;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean is(String s)
/*     */   {
/* 171 */     return toString().equalsIgnoreCase(s);
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer asBuffer()
/*     */   {
/* 177 */     return this._buffer.asReadOnlyBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */   public String asString()
/*     */   {
/* 183 */     return toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HttpMethod fromString(String method)
/*     */   {
/* 194 */     return (HttpMethod)CACHE.get(method);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */