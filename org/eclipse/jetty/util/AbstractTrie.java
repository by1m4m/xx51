/*    */ package org.eclipse.jetty.util;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.charset.StandardCharsets;
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
/*    */ public abstract class AbstractTrie<V>
/*    */   implements Trie<V>
/*    */ {
/*    */   final boolean _caseInsensitive;
/*    */   
/*    */   protected AbstractTrie(boolean insensitive)
/*    */   {
/* 39 */     this._caseInsensitive = insensitive;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean put(V v)
/*    */   {
/* 45 */     return put(v.toString(), v);
/*    */   }
/*    */   
/*    */ 
/*    */   public V remove(String s)
/*    */   {
/* 51 */     V o = get(s);
/* 52 */     put(s, null);
/* 53 */     return o;
/*    */   }
/*    */   
/*    */ 
/*    */   public V get(String s)
/*    */   {
/* 59 */     return (V)get(s, 0, s.length());
/*    */   }
/*    */   
/*    */ 
/*    */   public V get(ByteBuffer b)
/*    */   {
/* 65 */     return (V)get(b, 0, b.remaining());
/*    */   }
/*    */   
/*    */ 
/*    */   public V getBest(String s)
/*    */   {
/* 71 */     return (V)getBest(s, 0, s.length());
/*    */   }
/*    */   
/*    */ 
/*    */   public V getBest(byte[] b, int offset, int len)
/*    */   {
/* 77 */     return (V)getBest(new String(b, offset, len, StandardCharsets.ISO_8859_1));
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isCaseInsensitive()
/*    */   {
/* 83 */     return this._caseInsensitive;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\AbstractTrie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */