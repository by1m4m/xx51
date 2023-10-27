/*    */ package com.mysql.jdbc;
/*    */ 
/*    */ import java.io.CharArrayWriter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class WatchableWriter
/*    */   extends CharArrayWriter
/*    */ {
/*    */   private WriterWatcher watcher;
/*    */   
/*    */   public void close()
/*    */   {
/* 48 */     super.close();
/*    */     
/*    */ 
/* 51 */     if (this.watcher != null) {
/* 52 */       this.watcher.writerClosed(this);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setWatcher(WriterWatcher watcher)
/*    */   {
/* 63 */     this.watcher = watcher;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\mysql\jdbc\WatchableWriter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */