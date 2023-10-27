/*    */ package com.sun.jna;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FunctionParameterContext
/*    */   extends ToNativeContext
/*    */ {
/*    */   private Function function;
/*    */   
/*    */ 
/*    */ 
/*    */   private Object[] args;
/*    */   
/*    */ 
/*    */ 
/*    */   private int index;
/*    */   
/*    */ 
/*    */ 
/*    */   FunctionParameterContext(Function f, Object[] args, int index)
/*    */   {
/* 23 */     this.function = f;
/* 24 */     this.args = args;
/* 25 */     this.index = index;
/*    */   }
/*    */   
/* 28 */   public Function getFunction() { return this.function; }
/*    */   
/* 30 */   public Object[] getParameters() { return this.args; }
/* 31 */   public int getParameterIndex() { return this.index; }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\FunctionParameterContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */