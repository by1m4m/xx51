/*    */ package org.eclipse.jetty.util;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.nio.charset.Charset;
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
/*    */ public class ByteArrayOutputStream2
/*    */   extends ByteArrayOutputStream
/*    */ {
/*    */   public ByteArrayOutputStream2() {}
/*    */   
/* 31 */   public ByteArrayOutputStream2(int size) { super(size); }
/* 32 */   public byte[] getBuf() { return this.buf; }
/* 33 */   public int getCount() { return this.count; }
/* 34 */   public void setCount(int count) { this.count = count; }
/*    */   
/*    */   public void reset(int minSize)
/*    */   {
/* 38 */     reset();
/* 39 */     if (this.buf.length < minSize)
/*    */     {
/* 41 */       this.buf = new byte[minSize];
/*    */     }
/*    */   }
/*    */   
/*    */   public void writeUnchecked(int b)
/*    */   {
/* 47 */     this.buf[(this.count++)] = ((byte)b);
/*    */   }
/*    */   
/*    */   public String toString(Charset charset)
/*    */   {
/* 52 */     return new String(this.buf, 0, this.count, charset);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\ByteArrayOutputStream2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */