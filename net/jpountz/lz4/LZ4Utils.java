/*    */ package net.jpountz.lz4;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */  enum LZ4Utils
/*    */ {
/*    */   private static final int MAX_INPUT_SIZE = 2113929216;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private LZ4Utils() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static int maxCompressedLength(int length)
/*    */   {
/* 33 */     if (length < 0)
/* 34 */       throw new IllegalArgumentException("length must be >= 0, got " + length);
/* 35 */     if (length >= 2113929216) {
/* 36 */       throw new IllegalArgumentException("length must be < 2113929216");
/*    */     }
/* 38 */     return length + length / 255 + 16;
/*    */   }
/*    */   
/*    */   static int hash(int i) {
/* 42 */     return i * -1640531535 >>> 20;
/*    */   }
/*    */   
/*    */   static int hash64k(int i) {
/* 46 */     return i * -1640531535 >>> 19;
/*    */   }
/*    */   
/*    */ 
/* 50 */   static int hashHC(int i) { return i * -1640531535 >>> 17; }
/*    */   
/*    */   static class Match {
/*    */     int start;
/*    */     int ref;
/*    */     int len;
/*    */     
/* 57 */     void fix(int correction) { this.start += correction;
/* 58 */       this.ref += correction;
/* 59 */       this.len -= correction;
/*    */     }
/*    */     
/*    */     int end() {
/* 63 */       return this.start + this.len;
/*    */     }
/*    */   }
/*    */   
/*    */   static void copyTo(Match m1, Match m2) {
/* 68 */     m2.len = m1.len;
/* 69 */     m2.start = m1.start;
/* 70 */     m2.ref = m1.ref;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4Utils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */