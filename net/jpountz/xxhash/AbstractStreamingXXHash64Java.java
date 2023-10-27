/*    */ package net.jpountz.xxhash;
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractStreamingXXHash64Java
/*    */   extends StreamingXXHash64
/*    */ {
/*    */   int memSize;
/*    */   
/*    */ 
/*    */   long v1;
/*    */   
/*    */ 
/*    */   long v2;
/*    */   
/*    */ 
/*    */   long v3;
/*    */   
/*    */   long v4;
/*    */   
/*    */   long totalLen;
/*    */   
/*    */   final byte[] memory;
/*    */   
/*    */ 
/*    */   AbstractStreamingXXHash64Java(long seed)
/*    */   {
/* 28 */     super(seed);
/* 29 */     this.memory = new byte[32];
/* 30 */     reset();
/*    */   }
/*    */   
/*    */   public void reset()
/*    */   {
/* 35 */     this.v1 = (this.seed + -7046029288634856825L + -4417276706812531889L);
/* 36 */     this.v2 = (this.seed + -4417276706812531889L);
/* 37 */     this.v3 = (this.seed + 0L);
/* 38 */     this.v4 = (this.seed - -7046029288634856825L);
/* 39 */     this.totalLen = 0L;
/* 40 */     this.memSize = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\AbstractStreamingXXHash64Java.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */