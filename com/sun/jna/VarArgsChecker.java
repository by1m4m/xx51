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
/*    */ abstract class VarArgsChecker
/*    */ {
/*    */   private static final class RealVarArgsChecker
/*    */     extends VarArgsChecker
/*    */   {
/*    */     private RealVarArgsChecker()
/*    */     {
/* 22 */       super();
/*    */     }
/*    */     
/* 25 */     boolean isVarArgs(Method m) { return m.isVarArgs(); }
/*    */   }
/*    */   
/*    */ 
/*    */   private static final class NoVarArgsChecker
/*    */     extends VarArgsChecker
/*    */   {
/*    */     private NoVarArgsChecker()
/*    */     {
/* 34 */       super();
/*    */     }
/*    */     
/* 37 */     boolean isVarArgs(Method m) { return false; }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static VarArgsChecker create()
/*    */   {
/*    */     try
/*    */     {
/* 50 */       Method isVarArgsMethod = Method.class.getMethod("isVarArgs", new Class[0]);
/* 51 */       if (isVarArgsMethod != null)
/*    */       {
/* 53 */         return new RealVarArgsChecker(null);
/*    */       }
/* 55 */       return new NoVarArgsChecker(null);
/*    */     }
/*    */     catch (NoSuchMethodException e) {
/* 58 */       return new NoVarArgsChecker(null);
/*    */     } catch (SecurityException e) {}
/* 60 */     return new NoVarArgsChecker(null);
/*    */   }
/*    */   
/*    */   abstract boolean isVarArgs(Method paramMethod);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\VarArgsChecker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */