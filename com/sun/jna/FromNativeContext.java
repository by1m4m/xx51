/*    */ package com.sun.jna;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FromNativeContext
/*    */ {
/*    */   private Class type;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   FromNativeContext(Class javaType)
/*    */   {
/* 19 */     this.type = javaType;
/*    */   }
/*    */   
/*    */   public Class getTargetType() {
/* 23 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\FromNativeContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */