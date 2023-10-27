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
/*    */ public abstract class StreamingXXHash32
/*    */ {
/*    */   final int seed;
/*    */   
/*    */   StreamingXXHash32(int seed)
/*    */   {
/* 52 */     this.seed = seed;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract int getValue();
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
/* 93 */         return StreamingXXHash32.this.getValue() & 0xFFFFFFF;
/*    */       }
/*    */       
/*    */       public void reset()
/*    */       {
/* 98 */         StreamingXXHash32.this.reset();
/*    */       }
/*    */       
/*    */       public void update(int b)
/*    */       {
/* :3 */         StreamingXXHash32.this.update(new byte[] { (byte)b }, 0, 1);
/*    */       }
/*    */       
/*    */       public void update(byte[] b, int off, int len)
/*    */       {
/* :8 */         StreamingXXHash32.this.update(b, off, len);
/*    */       }
/*    */       
/*    */       public String toString()
/*    */       {
/* ;3 */         return StreamingXXHash32.this.toString();
/*    */       }
/*    */     };
/*    */   }
/*    */   
/*    */   static abstract interface Factory
/*    */   {
/*    */     public abstract StreamingXXHash32 newStreamingHash(int paramInt);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\StreamingXXHash32.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */