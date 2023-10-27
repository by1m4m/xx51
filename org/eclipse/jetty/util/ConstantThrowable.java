/*    */ package org.eclipse.jetty.util;
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
/*    */ public class ConstantThrowable
/*    */   extends Throwable
/*    */ {
/*    */   public ConstantThrowable()
/*    */   {
/* 29 */     this(null);
/*    */   }
/*    */   
/*    */   public ConstantThrowable(String name)
/*    */   {
/* 34 */     super(name, null, false, false);
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 40 */     return String.valueOf(getMessage());
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\ConstantThrowable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */