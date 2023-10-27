/*    */ package org.apache.log4j.helpers;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.Writer;
/*    */ import org.apache.log4j.spi.ErrorHandler;
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
/*    */ public class CountingQuietWriter
/*    */   extends QuietWriter
/*    */ {
/*    */   protected long count;
/*    */   
/*    */   public CountingQuietWriter(Writer writer, ErrorHandler eh)
/*    */   {
/* 39 */     super(writer, eh);
/*    */   }
/*    */   
/*    */   public void write(String string)
/*    */   {
/*    */     try {
/* 45 */       this.out.write(string);
/* 46 */       this.count += string.length();
/*    */     }
/*    */     catch (IOException e) {
/* 49 */       this.errorHandler.error("Write failure.", e, 1);
/*    */     }
/*    */   }
/*    */   
/*    */   public long getCount()
/*    */   {
/* 55 */     return this.count;
/*    */   }
/*    */   
/*    */   public void setCount(long count)
/*    */   {
/* 60 */     this.count = count;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\apache\log4j\helpers\CountingQuietWriter.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       0.7.1
 */