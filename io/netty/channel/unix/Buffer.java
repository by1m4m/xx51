/*    */ package io.netty.channel.unix;
/*    */ 
/*    */ import io.netty.util.internal.PlatformDependent;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.ByteOrder;
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
/*    */ public final class Buffer
/*    */ {
/*    */   public static void free(ByteBuffer buffer)
/*    */   {
/* 33 */     PlatformDependent.freeDirectBuffer(buffer);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static ByteBuffer allocateDirectWithNativeOrder(int capacity)
/*    */   {
/* 40 */     return ByteBuffer.allocateDirect(capacity).order(PlatformDependent.BIG_ENDIAN_NATIVE_ORDER ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static long memoryAddress(ByteBuffer buffer)
/*    */   {
/* 48 */     assert (buffer.isDirect());
/* 49 */     if (PlatformDependent.hasUnsafe()) {
/* 50 */       return PlatformDependent.directBufferAddress(buffer);
/*    */     }
/* 52 */     return memoryAddress0(buffer);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static int addressSize()
/*    */   {
/* 59 */     if (PlatformDependent.hasUnsafe()) {
/* 60 */       return PlatformDependent.addressSize();
/*    */     }
/* 62 */     return addressSize0();
/*    */   }
/*    */   
/*    */   private static native int addressSize0();
/*    */   
/*    */   private static native long memoryAddress0(ByteBuffer paramByteBuffer);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\channel\unix\Buffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */