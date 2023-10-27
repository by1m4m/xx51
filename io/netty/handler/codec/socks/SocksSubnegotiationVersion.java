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
/*    */ public enum SocksSubnegotiationVersion
/*    */ {
/* 20 */   AUTH_PASSWORD((byte)1), 
/* 21 */   UNKNOWN((byte)-1);
/*    */   
/*    */   private final byte b;
/*    */   
/*    */   private SocksSubnegotiationVersion(byte b) {
/* 26 */     this.b = b;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static SocksSubnegotiationVersion fromByte(byte b)
/*    */   {
/* 34 */     return valueOf(b);
/*    */   }
/*    */   
/*    */   public static SocksSubnegotiationVersion valueOf(byte b) {
/* 38 */     for (SocksSubnegotiationVersion code : ) {
/* 39 */       if (code.b == b) {
/* 40 */         return code;
/*    */       }
/*    */     }
/* 43 */     return UNKNOWN;
/*    */   }
/*    */   
/*    */   public byte byteValue() {
/* 47 */     return this.b;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\socks\SocksSubnegotiationVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */