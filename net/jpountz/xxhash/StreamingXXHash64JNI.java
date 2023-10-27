/*    */ package net.jpountz.xxhash;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class StreamingXXHash64JNI
/*    */   extends StreamingXXHash64
/*    */ {
/*    */   private long state;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static class Factory
/*    */     implements StreamingXXHash64.Factory
/*    */   {
/* 22 */     public static final StreamingXXHash64.Factory INSTANCE = new Factory();
/*    */     
/*    */     public StreamingXXHash64 newStreamingHash(long seed)
/*    */     {
/* 26 */       return new StreamingXXHash64JNI(seed);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   StreamingXXHash64JNI(long seed)
/*    */   {
/* 34 */     super(seed);
/* 35 */     this.state = XXHashJNI.XXH64_init(seed);
/*    */   }
/*    */   
/*    */   private void checkState() {
/* 39 */     if (this.state == 0L) {
/* 40 */       throw new AssertionError("Already finalized");
/*    */     }
/*    */   }
/*    */   
/*    */   public void reset()
/*    */   {
/* 46 */     checkState();
/* 47 */     XXHashJNI.XXH64_free(this.state);
/* 48 */     this.state = XXHashJNI.XXH64_init(this.seed);
/*    */   }
/*    */   
/*    */   public long getValue()
/*    */   {
/* 53 */     checkState();
/* 54 */     return XXHashJNI.XXH64_digest(this.state);
/*    */   }
/*    */   
/*    */   public void update(byte[] bytes, int off, int len)
/*    */   {
/* 59 */     checkState();
/* 60 */     XXHashJNI.XXH64_update(this.state, bytes, off, len);
/*    */   }
/*    */   
/*    */   protected void finalize() throws Throwable
/*    */   {
/* 65 */     super.finalize();
/*    */     
/* 67 */     XXHashJNI.XXH64_free(this.state);
/* 68 */     this.state = 0L;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\StreamingXXHash64JNI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */