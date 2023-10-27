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
/*    */ public enum SocksAuthScheme
/*    */ {
/* 20 */   NO_AUTH((byte)0), 
/* 21 */   AUTH_GSSAPI((byte)1), 
/* 22 */   AUTH_PASSWORD((byte)2), 
/* 23 */   UNKNOWN((byte)-1);
/*    */   
/*    */   private final byte b;
/*    */   
/*    */   private SocksAuthScheme(byte b) {
/* 28 */     this.b = b;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static SocksAuthScheme fromByte(byte b)
/*    */   {
/* 36 */     return valueOf(b);
/*    */   }
/*    */   
/*    */   public static SocksAuthScheme valueOf(byte b) {
/* 40 */     for (SocksAuthScheme code : ) {
/* 41 */       if (code.b == b) {
/* 42 */         return code;
/*    */       }
/*    */     }
/* 45 */     return UNKNOWN;
/*    */   }
/*    */   
/*    */   public byte byteValue() {
/* 49 */     return this.b;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\socks\SocksAuthScheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */