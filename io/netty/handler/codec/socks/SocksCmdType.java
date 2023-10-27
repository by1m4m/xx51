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
/*    */ public enum SocksCmdType
/*    */ {
/* 20 */   CONNECT((byte)1), 
/* 21 */   BIND((byte)2), 
/* 22 */   UDP((byte)3), 
/* 23 */   UNKNOWN((byte)-1);
/*    */   
/*    */   private final byte b;
/*    */   
/*    */   private SocksCmdType(byte b) {
/* 28 */     this.b = b;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public static SocksCmdType fromByte(byte b)
/*    */   {
/* 36 */     return valueOf(b);
/*    */   }
/*    */   
/*    */   public static SocksCmdType valueOf(byte b) {
/* 40 */     for (SocksCmdType code : ) {
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


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\socks\SocksCmdType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */