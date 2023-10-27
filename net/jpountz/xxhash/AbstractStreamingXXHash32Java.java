/*    */ package net.jpountz.xxhash;
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class AbstractStreamingXXHash32Java
/*    */   extends StreamingXXHash32
/*    */ {
/*    */   int v1;
/*    */   
/*    */ 
/*    */   int v2;
/*    */   
/*    */ 
/*    */   int v3;
/*    */   
/*    */   int v4;
/*    */   
/*    */   int memSize;
/*    */   
/*    */   long totalLen;
/*    */   
/*    */   final byte[] memory;
/*    */   
/*    */ 
/*    */   AbstractStreamingXXHash32Java(int seed)
/*    */   {
/* 27 */     super(seed);
/* 28 */     this.memory = new byte[16];
/* 29 */     reset();
/*    */   }
/*    */   
/*    */   public void reset()
/*    */   {
/* 34 */     this.v1 = (this.seed + -1640531535 + -2048144777);
/* 35 */     this.v2 = (this.seed + -2048144777);
/* 36 */     this.v3 = (this.seed + 0);
/* 37 */     this.v4 = (this.seed - -1640531535);
/* 38 */     this.totalLen = 0L;
/* 39 */     this.memSize = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\AbstractStreamingXXHash32Java.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */