/*    */ package io.netty.internal.tcnative;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SessionTicketKey
/*    */ {
/*    */   public static final int NAME_SIZE = 16;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int HMAC_KEY_SIZE = 16;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int AES_KEY_SIZE = 16;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static final int TICKET_KEY_SIZE = 48;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   final byte[] name;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   final byte[] hmacKey;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   final byte[] aesKey;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SessionTicketKey(byte[] name, byte[] hmacKey, byte[] aesKey)
/*    */   {
/* 52 */     if ((name == null) || (name.length != 16)) {
/* 53 */       throw new IllegalArgumentException("Length of name should be 16");
/*    */     }
/* 55 */     if ((hmacKey == null) || (hmacKey.length != 16)) {
/* 56 */       throw new IllegalArgumentException("Length of hmacKey should be 16");
/*    */     }
/* 58 */     if ((aesKey == null) || (aesKey.length != 16)) {
/* 59 */       throw new IllegalArgumentException("Length of aesKey should be 16");
/*    */     }
/* 61 */     this.name = name;
/* 62 */     this.hmacKey = hmacKey;
/* 63 */     this.aesKey = aesKey;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte[] getName()
/*    */   {
/* 72 */     return (byte[])this.name.clone();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte[] getHmacKey()
/*    */   {
/* 80 */     return (byte[])this.hmacKey.clone();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte[] getAesKey()
/*    */   {
/* 88 */     return (byte[])this.aesKey.clone();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\internal\tcnative\SessionTicketKey.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */