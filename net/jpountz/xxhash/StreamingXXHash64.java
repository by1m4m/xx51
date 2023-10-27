/*    */ package net.jpountz.xxhash;
/*    */ 
/*    */ import java.util.zip.Checksum;
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
/*    */ public abstract class StreamingXXHash64
/*    */ {
/*    */   final long seed;
/*    */   
/*    */   StreamingXXHash64(long seed)
/*    */   {
/* 52 */     this.seed = seed;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract long getValue();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void reset();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 79 */     return getClass().getSimpleName() + "(seed=" + this.seed + ")";
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final Checksum asChecksum()
/*    */   {
/* 89 */     new Checksum()
/*    */     {
/*    */       public long getValue()
/*    */       {
/* 93 */         return StreamingXXHash64.this.getValue();
/*    */       }
/*    */       
/*    */       public void reset()
/*    */       {
/* 98 */         StreamingXXHash64.this.reset();
/*    */       }
/*    */       
/*    */       public void update(int b)
/*    */       {
/* :3 */         StreamingXXHash64.this.update(new byte[] { (byte)b }, 0, 1);
/*    */       }
/*    */       
/*    */       public void update(byte[] b, int off, int len)
/*    */       {
/* :8 */         StreamingXXHash64.this.update(b, off, len);
/*    */       }
/*    */       
/*    */       public String toString()
/*    */       {
/* ;3 */         return StreamingXXHash64.this.toString();
/*    */       }
/*    */     };
/*    */   }
/*    */   
/*    */   static abstract interface Factory
/*    */   {
/*    */     public abstract StreamingXXHash64 newStreamingHash(long paramLong);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\StreamingXXHash64.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */