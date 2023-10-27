/*    */ package io.netty.handler.codec.socks;
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
/*    */ public enum SocksAuthStatus
/*    */ {
/* 20 */   SUCCESS((byte)0), 
/* 21 */   FAILURE((byte)-1);
/*    */   
/*    */   private final byte b;
/*    */   
/*    */   private SocksAuthStatus(byte b) {
/* 26 */     this.b = b;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static SocksAuthStatus fromByte(byte b)
/*    */   {
/* 34 */     return valueOf(b);
/*    */   }
/*    */   
/*    */   public static SocksAuthStatus valueOf(byte b) {
/* 38 */     for (SocksAuthStatus code : ) {
/* 39 */       if (code.b == b) {
/* 40 */         return code;
/*    */       }
/*    */     }
/* 43 */     return FAILURE;
/*    */   }
/*    */   
/*    */   public byte byteValue() {
/* 47 */     return this.b;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\socks\SocksAuthStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */