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
/*    */ public class CallbackParameterContext
/*    */   extends FromNativeContext
/*    */ {
/*    */   private Method method;
/*    */   private Object[] args;
/*    */   private int index;
/*    */   
/*    */   CallbackParameterContext(Class javaType, Method m, Object[] args, int index)
/*    */   {
/* 23 */     super(javaType);
/* 24 */     this.method = m;
/* 25 */     this.args = args;
/* 26 */     this.index = index; }
/*    */   
/* 28 */   public Method getMethod() { return this.method; }
/* 29 */   public Object[] getArguments() { return this.args; }
/* 30 */   public int getIndex() { return this.index; }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\CallbackParameterContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */