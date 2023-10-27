/*    */ package org.eclipse.jetty.util;
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
/*    */ public class Utf8StringBuilder
/*    */   extends Utf8Appendable
/*    */ {
/*    */   final StringBuilder _buffer;
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
/*    */   public Utf8StringBuilder()
/*    */   {
/* 41 */     super(new StringBuilder());
/* 42 */     this._buffer = ((StringBuilder)this._appendable);
/*    */   }
/*    */   
/*    */   public Utf8StringBuilder(int capacity)
/*    */   {
/* 47 */     super(new StringBuilder(capacity));
/* 48 */     this._buffer = ((StringBuilder)this._appendable);
/*    */   }
/*    */   
/*    */ 
/*    */   public int length()
/*    */   {
/* 54 */     return this._buffer.length();
/*    */   }
/*    */   
/*    */ 
/*    */   public void reset()
/*    */   {
/* 60 */     super.reset();
/* 61 */     this._buffer.setLength(0);
/*    */   }
/*    */   
/*    */ 
/*    */   public String getPartialString()
/*    */   {
/* 67 */     return this._buffer.toString();
/*    */   }
/*    */   
/*    */   public StringBuilder getStringBuilder()
/*    */   {
/* 72 */     checkState();
/* 73 */     return this._buffer;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 79 */     checkState();
/* 80 */     return this._buffer.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\Utf8StringBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */