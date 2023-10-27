/*    */ package io.netty.handler.codec.dns;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ public abstract interface DnsRecordDecoder
/*    */ {
/* 29 */   public static final DnsRecordDecoder DEFAULT = new DefaultDnsRecordDecoder();
/*    */   
/*    */   public abstract DnsQuestion decodeQuestion(ByteBuf paramByteBuf)
/*    */     throws Exception;
/*    */   
/*    */   public abstract <T extends DnsRecord> T decodeRecord(ByteBuf paramByteBuf)
/*    */     throws Exception;
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\dns\DnsRecordDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */