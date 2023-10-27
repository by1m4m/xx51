/*    */ package net.jpountz.util;
/*    */ 
/*    */ import java.nio.ByteOrder;
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
/*    */ public enum Utils
/*    */ {
/*    */   public static final ByteOrder NATIVE_BYTE_ORDER;
/*    */   private static final boolean unalignedAccessAllowed;
/*    */   
/*    */   static
/*    */   {
/* 22 */     NATIVE_BYTE_ORDER = ByteOrder.nativeOrder();
/*    */     
/*    */ 
/*    */ 
/* 26 */     String arch = System.getProperty("os.arch");
/* 27 */     unalignedAccessAllowed = (arch.equals("i386")) || (arch.equals("x86")) || (arch.equals("amd64")) || (arch.equals("x86_64")) || (arch.equals("aarch64")) || (arch.equals("ppc64le"));
/*    */   }
/*    */   
/*    */ 
/*    */   public static boolean isUnalignedAccessAllowed()
/*    */   {
/* 33 */     return unalignedAccessAllowed;
/*    */   }
/*    */   
/*    */   private Utils() {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\util\Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */