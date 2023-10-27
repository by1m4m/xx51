/*    */ package net.jpountz.xxhash;
/*    */ 
/*    */ import java.nio.ByteBuffer;
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
/*    */ 
/*    */ public abstract class XXHash64
/*    */ {
/*    */   public abstract long hash(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong);
/*    */   
/*    */   public abstract long hash(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, long paramLong);
/*    */   
/*    */   public final long hash(ByteBuffer buf, long seed)
/*    */   {
/* 61 */     long hash = hash(buf, buf.position(), buf.remaining(), seed);
/* 62 */     buf.position(buf.limit());
/* 63 */     return hash;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 68 */     return getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\XXHash64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */