/*    */ package com.sun.jna;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class MethodParameterContext
/*    */   extends FunctionParameterContext
/*    */ {
/*    */   private Method method;
/*    */   
/*    */   MethodParameterContext(Function f, Object[] args, int index, Method m)
/*    */   {
/* 22 */     super(f, args, index);
/* 23 */     this.method = m;
/*    */   }
/*    */   
/* 26 */   public Method getMethod() { return this.method; }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\MethodParameterContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */