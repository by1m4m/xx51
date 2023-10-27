/*    */ package net.jpountz.lz4;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LZ4Exception
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LZ4Exception(String msg, Throwable t)
/*    */   {
/* 25 */     super(msg, t);
/*    */   }
/*    */   
/*    */   public LZ4Exception(String msg) {
/* 29 */     super(msg);
/*    */   }
/*    */   
/*    */   public LZ4Exception() {}
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4Exception.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */