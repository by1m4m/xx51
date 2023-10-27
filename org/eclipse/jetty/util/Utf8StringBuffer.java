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
/*    */ public class Utf8StringBuffer
/*    */   extends Utf8Appendable
/*    */ {
/*    */   final StringBuffer _buffer;
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
/*    */   public Utf8StringBuffer()
/*    */   {
/* 40 */     super(new StringBuffer());
/* 41 */     this._buffer = ((StringBuffer)this._appendable);
/*    */   }
/*    */   
/*    */   public Utf8StringBuffer(int capacity)
/*    */   {
/* 46 */     super(new StringBuffer(capacity));
/* 47 */     this._buffer = ((StringBuffer)this._appendable);
/*    */   }
/*    */   
/*    */ 
/*    */   public int length()
/*    */   {
/* 53 */     return this._buffer.length();
/*    */   }
/*    */   
/*    */ 
/*    */   public void reset()
/*    */   {
/* 59 */     super.reset();
/* 60 */     this._buffer.setLength(0);
/*    */   }
/*    */   
/*    */ 
/*    */   public String getPartialString()
/*    */   {
/* 66 */     return this._buffer.toString();
/*    */   }
/*    */   
/*    */   public StringBuffer getStringBuffer()
/*    */   {
/* 71 */     checkState();
/* 72 */     return this._buffer;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 78 */     checkState();
/* 79 */     return this._buffer.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\Utf8StringBuffer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */