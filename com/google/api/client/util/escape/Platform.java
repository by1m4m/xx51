/*    */ package com.google.api.client.util.escape;
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
/*    */ final class Platform
/*    */ {
/*    */   static char[] charBufferFromThreadLocal()
/*    */   {
/* 28 */     return (char[])DEST_TL.get();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 36 */   private static final ThreadLocal<char[]> DEST_TL = new ThreadLocal()
/*    */   {
/*    */     protected char[] initialValue() {
/* 39 */       return new char['Ѐ'];
/*    */     }
/*    */   };
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\escape\Platform.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */