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
/*    */ final class XXHash64JNI
/*    */   extends XXHash64
/*    */ {
/* 24 */   public static final XXHash64 INSTANCE = new XXHash64JNI();
/*    */   private static XXHash64 SAFE_INSTANCE;
/*    */   
/*    */   public long hash(byte[] buf, int off, int len, long seed)
/*    */   {
/* 29 */     SafeUtils.checkRange(buf, off, len);
/* 30 */     return XXHashJNI.XXH64(buf, off, len, seed);
/*    */   }
/*    */   
/*    */   public long hash(ByteBuffer buf, int off, int len, long seed)
/*    */   {
/* 35 */     if (buf.isDirect()) {
/* 36 */       ByteBufferUtils.checkRange(buf, off, len);
/* 37 */       return XXHashJNI.XXH64BB(buf, off, len, seed); }
/* 38 */     if (buf.hasArray()) {
/* 39 */       return hash(buf.array(), off + buf.arrayOffset(), len, seed);
/*    */     }
/* 41 */     XXHash64 safeInstance = SAFE_INSTANCE;
/* 42 */     if (safeInstance == null) {
/* 43 */       safeInstance = SAFE_INSTANCE = XXHashFactory.safeInstance().hash64();
/*    */     }
/* 45 */     return safeInstance.hash(buf, off, len, seed);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\XXHash64JNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */