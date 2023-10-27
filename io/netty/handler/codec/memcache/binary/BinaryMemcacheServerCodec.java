/*    */ package io.netty.handler.codec.memcache.binary;
/*    */ 
/*    */ import io.netty.channel.CombinedChannelDuplexHandler;
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
/*    */ public class BinaryMemcacheServerCodec
/*    */   extends CombinedChannelDuplexHandler<BinaryMemcacheRequestDecoder, BinaryMemcacheResponseEncoder>
/*    */ {
/*    */   public BinaryMemcacheServerCodec()
/*    */   {
/* 33 */     this(8192);
/*    */   }
/*    */   
/*    */   public BinaryMemcacheServerCodec(int decodeChunkSize) {
/* 37 */     super(new BinaryMemcacheRequestDecoder(decodeChunkSize), new BinaryMemcacheResponseEncoder());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheServerCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */