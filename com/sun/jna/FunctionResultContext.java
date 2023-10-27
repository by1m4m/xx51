/*    */ package com.sun.jna;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FunctionResultContext
/*    */   extends FromNativeContext
/*    */ {
/*    */   private Function function;
/*    */   
/*    */ 
/*    */ 
/*    */   private Object[] args;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   FunctionResultContext(Class resultClass, Function function, Object[] args)
/*    */   {
/* 21 */     super(resultClass);
/* 22 */     this.function = function;
/* 23 */     this.args = args;
/*    */   }
/*    */   
/* 26 */   public Function getFunction() { return this.function; }
/*    */   
/* 28 */   public Object[] getArguments() { return this.args; }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\FunctionResultContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */