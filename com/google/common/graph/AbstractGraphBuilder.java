/*    */ package com.google.common.graph;
/*    */ 
/*    */ import com.google.common.base.Optional;
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
/*    */ abstract class AbstractGraphBuilder<N>
/*    */ {
/*    */   final boolean directed;
/* 28 */   boolean allowsSelfLoops = false;
/* 29 */   ElementOrder<N> nodeOrder = ElementOrder.insertion();
/* 30 */   Optional<Integer> expectedNodeCount = Optional.absent();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   AbstractGraphBuilder(boolean directed)
/*    */   {
/* 39 */     this.directed = directed;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\graph\AbstractGraphBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */