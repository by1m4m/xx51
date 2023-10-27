/*    */ package io.netty.util.internal;
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
/*    */ final class NativeLibraryUtil
/*    */ {
/*    */   public static void loadLibrary(String libName, boolean absolute)
/*    */   {
/* 35 */     if (absolute) {
/* 36 */       System.load(libName);
/*    */     } else {
/* 38 */       System.loadLibrary(libName);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\NativeLibraryUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */