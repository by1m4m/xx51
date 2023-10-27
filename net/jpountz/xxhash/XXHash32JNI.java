/*    */ package net.jpountz.xxhash;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import net.jpountz.util.ByteBufferUtils;
/*    */ import net.jpountz.util.SafeUtils;
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
/*    */ final class XXHash32JNI
/*    */   extends XXHash32
/*    */ {
/* 24 */   public static final XXHash32 INSTANCE = new XXHash32JNI();
/*    */   private static XXHash32 SAFE_INSTANCE;
/*    */   
/*    */   public int hash(byte[] buf, int off, int len, int seed)
/*    */   {
/* 29 */     SafeUtils.checkRange(buf, off, len);
/* 30 */     return XXHashJNI.XXH32(buf, off, len, seed);
/*    */   }
/*    */   
/*    */   public int hash(ByteBuffer buf, int off, int len, int seed)
/*    */   {
/* 35 */     if (buf.isDirect()) {
/* 36 */       ByteBufferUtils.checkRange(buf, off, len);
/* 37 */       return XXHashJNI.XXH32BB(buf, off, len, seed); }
/* 38 */     if (buf.hasArray()) {
/* 39 */       return hash(buf.array(), off + buf.arrayOffset(), len, seed);
/*    */     }
/* 41 */     XXHash32 safeInstance = SAFE_INSTANCE;
/* 42 */     if (safeInstance == null) {
/* 43 */       safeInstance = SAFE_INSTANCE = XXHashFactory.safeInstance().hash32();
/*    */     }
/* 45 */     return safeInstance.hash(buf, off, len, seed);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\XXHash32JNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */