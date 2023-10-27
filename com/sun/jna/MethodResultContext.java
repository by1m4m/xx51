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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MethodResultContext
/*    */   extends FunctionResultContext
/*    */ {
/*    */   private final Method method;
/*    */   
/*    */   MethodResultContext(Class resultClass, Function function, Object[] args, Method method)
/*    */   {
/* 25 */     super(resultClass, function, args);
/* 26 */     this.method = method;
/*    */   }
/*    */   
/* 29 */   public Method getMethod() { return this.method; }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\MethodResultContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */