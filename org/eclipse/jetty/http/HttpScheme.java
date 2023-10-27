/*    */ package org.eclipse.jetty.http;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.eclipse.jetty.util.ArrayTrie;
/*    */ import org.eclipse.jetty.util.BufferUtil;
/*    */ import org.eclipse.jetty.util.Trie;
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
/*    */ public enum HttpScheme
/*    */ {
/* 32 */   HTTP("http"), 
/* 33 */   HTTPS("https"), 
/* 34 */   WS("ws"), 
/* 35 */   WSS("wss");
/*    */   
/*    */   static {
/* 38 */     CACHE = new ArrayTrie();
/*    */     
/*    */ 
/* 41 */     for (HttpScheme version : values()) {
/* 42 */       CACHE.put(version.asString(), version);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final Trie<HttpScheme> CACHE;
/*    */   private final String _string;
/*    */   private final ByteBuffer _buffer;
/*    */   private HttpScheme(String s)
/*    */   {
/* 51 */     this._string = s;
/* 52 */     this._buffer = BufferUtil.toBuffer(s);
/*    */   }
/*    */   
/*    */ 
/*    */   public ByteBuffer asByteBuffer()
/*    */   {
/* 58 */     return this._buffer.asReadOnlyBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean is(String s)
/*    */   {
/* 64 */     return (s != null) && (this._string.equalsIgnoreCase(s));
/*    */   }
/*    */   
/*    */   public String asString()
/*    */   {
/* 69 */     return this._string;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 76 */     return this._string;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\http\HttpScheme.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */