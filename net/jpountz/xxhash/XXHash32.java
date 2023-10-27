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
/*    */ public abstract class XXHash32
/*    */ {
/*    */   public abstract int hash(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   public abstract int hash(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   public final int hash(ByteBuffer buf, int seed)
/*    */   {
/* 61 */     int hash = hash(buf, buf.position(), buf.remaining(), seed);
/* 62 */     buf.position(buf.limit());
/* 63 */     return hash;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 68 */     return getClass().getSimpleName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\XXHash32.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */