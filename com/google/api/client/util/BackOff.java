/*    */ package com.google.api.client.util;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public abstract interface BackOff
/*    */ {
/*    */   public static final long STOP = -1L;
/* 56 */   public static final BackOff ZERO_BACKOFF = new BackOff()
/*    */   {
/*    */     public void reset() throws IOException
/*    */     {}
/*    */     
/*    */     public long nextBackOffMillis() throws IOException {
/* 62 */       return 0L;
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 70 */   public static final BackOff STOP_BACKOFF = new BackOff()
/*    */   {
/*    */     public void reset() throws IOException
/*    */     {}
/*    */     
/*    */     public long nextBackOffMillis() throws IOException {
/* 76 */       return -1L;
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract void reset()
/*    */     throws IOException;
/*    */   
/*    */   public abstract long nextBackOffMillis()
/*    */     throws IOException;
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\BackOff.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */