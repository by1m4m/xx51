/*    */ package io.netty.buffer;
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
/*    */ @Deprecated
/*    */ public class SlicedByteBuf
/*    */   extends AbstractUnpooledSlicedByteBuf
/*    */ {
/*    */   private int length;
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
/*    */   public SlicedByteBuf(ByteBuf buffer, int index, int length)
/*    */   {
/* 32 */     super(buffer, index, length);
/*    */   }
/*    */   
/*    */   final void initLength(int length)
/*    */   {
/* 37 */     this.length = length;
/*    */   }
/*    */   
/*    */   final int length()
/*    */   {
/* 42 */     return this.length;
/*    */   }
/*    */   
/*    */   public int capacity()
/*    */   {
/* 47 */     return this.length;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\buffer\SlicedByteBuf.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */