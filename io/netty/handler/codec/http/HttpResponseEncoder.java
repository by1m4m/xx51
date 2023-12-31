/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufUtil;
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
/*    */ public class HttpResponseEncoder
/*    */   extends HttpObjectEncoder<HttpResponse>
/*    */ {
/*    */   public boolean acceptOutboundMessage(Object msg)
/*    */     throws Exception
/*    */   {
/* 31 */     return (super.acceptOutboundMessage(msg)) && (!(msg instanceof HttpRequest));
/*    */   }
/*    */   
/*    */   protected void encodeInitialLine(ByteBuf buf, HttpResponse response) throws Exception
/*    */   {
/* 36 */     response.protocolVersion().encode(buf);
/* 37 */     buf.writeByte(32);
/* 38 */     response.status().encode(buf);
/* 39 */     ByteBufUtil.writeShortBE(buf, 3338);
/*    */   }
/*    */   
/*    */   protected void sanitizeHeadersBeforeEncode(HttpResponse msg, boolean isAlwaysEmpty)
/*    */   {
/* 44 */     if (isAlwaysEmpty) {
/* 45 */       HttpResponseStatus status = msg.status();
/* 46 */       if ((status.codeClass() == HttpStatusClass.INFORMATIONAL) || 
/* 47 */         (status.code() == HttpResponseStatus.NO_CONTENT.code()))
/*    */       {
/*    */ 
/*    */ 
/* 51 */         msg.headers().remove(HttpHeaderNames.CONTENT_LENGTH);
/*    */         
/*    */ 
/*    */ 
/* 55 */         msg.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
/* 56 */       } else if (status.code() == HttpResponseStatus.RESET_CONTENT.code())
/*    */       {
/*    */ 
/* 59 */         msg.headers().remove(HttpHeaderNames.TRANSFER_ENCODING);
/*    */         
/*    */ 
/*    */ 
/* 63 */         msg.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, 0);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected boolean isContentAlwaysEmpty(HttpResponse msg)
/*    */   {
/* 72 */     HttpResponseStatus status = msg.status();
/*    */     
/* 74 */     if (status.codeClass() == HttpStatusClass.INFORMATIONAL)
/*    */     {
/* 76 */       if (status.code() == HttpResponseStatus.SWITCHING_PROTOCOLS.code())
/*    */       {
/*    */ 
/*    */ 
/* 80 */         return msg.headers().contains(HttpHeaderNames.SEC_WEBSOCKET_VERSION);
/*    */       }
/* 82 */       return true;
/*    */     }
/* 84 */     return (status.code() == HttpResponseStatus.NO_CONTENT.code()) || 
/* 85 */       (status.code() == HttpResponseStatus.NOT_MODIFIED.code()) || 
/* 86 */       (status.code() == HttpResponseStatus.RESET_CONTENT.code());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\http\HttpResponseEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */