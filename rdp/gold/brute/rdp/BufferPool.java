/*    */ package rdp.gold.brute.rdp;
/*    */ 
/*    */ public class BufferPool
/*    */ {
/*    */   public static byte[] allocateNewBuffer(int minSize) {
/*  6 */     if (minSize >= 0) {
/*  7 */       return new byte[minSize];
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 14 */     return new byte[131072];
/*    */   }
/*    */   
/*    */   public static void recycleBuffer(byte[] buf) {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\rdp\gold\brute\rdp\BufferPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */