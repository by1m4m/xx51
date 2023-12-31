/*     */ package io.netty.handler.codec.protobuf;
/*     */ 
/*     */ import com.google.protobuf.ExtensionRegistry;
/*     */ import com.google.protobuf.ExtensionRegistryLite;
/*     */ import com.google.protobuf.MessageLite;
/*     */ import com.google.protobuf.MessageLite.Builder;
/*     */ import com.google.protobuf.Parser;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToMessageDecoder;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @ChannelHandler.Sharable
/*     */ public class ProtobufDecoder
/*     */   extends MessageToMessageDecoder<ByteBuf>
/*     */ {
/*     */   private static final boolean HAS_PARSER;
/*     */   private final MessageLite prototype;
/*     */   private final ExtensionRegistryLite extensionRegistry;
/*     */   
/*     */   static
/*     */   {
/*  70 */     boolean hasParser = false;
/*     */     try
/*     */     {
/*  73 */       MessageLite.class.getDeclaredMethod("getParserForType", new Class[0]);
/*  74 */       hasParser = true;
/*     */     }
/*     */     catch (Throwable localThrowable) {}
/*     */     
/*     */ 
/*  79 */     HAS_PARSER = hasParser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ProtobufDecoder(MessageLite prototype)
/*     */   {
/*  89 */     this(prototype, null);
/*     */   }
/*     */   
/*     */   public ProtobufDecoder(MessageLite prototype, ExtensionRegistry extensionRegistry) {
/*  93 */     this(prototype, extensionRegistry);
/*     */   }
/*     */   
/*     */   public ProtobufDecoder(MessageLite prototype, ExtensionRegistryLite extensionRegistry) {
/*  97 */     if (prototype == null) {
/*  98 */       throw new NullPointerException("prototype");
/*     */     }
/* 100 */     this.prototype = prototype.getDefaultInstanceForType();
/* 101 */     this.extensionRegistry = extensionRegistry;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 109 */     int length = msg.readableBytes();
/* 110 */     int offset; byte[] array; int offset; if (msg.hasArray()) {
/* 111 */       byte[] array = msg.array();
/* 112 */       offset = msg.arrayOffset() + msg.readerIndex();
/*     */     } else {
/* 114 */       array = new byte[length];
/* 115 */       msg.getBytes(msg.readerIndex(), array, 0, length);
/* 116 */       offset = 0;
/*     */     }
/*     */     
/* 119 */     if (this.extensionRegistry == null) {
/* 120 */       if (HAS_PARSER) {
/* 121 */         out.add(this.prototype.getParserForType().parseFrom(array, offset, length));
/*     */       } else {
/* 123 */         out.add(this.prototype.newBuilderForType().mergeFrom(array, offset, length).build());
/*     */       }
/*     */     }
/* 126 */     else if (HAS_PARSER) {
/* 127 */       out.add(this.prototype.getParserForType().parseFrom(array, offset, length, this.extensionRegistry));
/*     */     }
/*     */     else {
/* 130 */       out.add(this.prototype.newBuilderForType().mergeFrom(array, offset, length, this.extensionRegistry)
/* 131 */         .build());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\handler\codec\protobuf\ProtobufDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */