/*    */ package io.netty.util;
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
/*    */ public abstract interface HashingStrategy<T>
/*    */ {
/* 62 */   public static final HashingStrategy JAVA_HASHER = new HashingStrategy()
/*    */   {
/*    */     public int hashCode(Object obj) {
/* 65 */       return obj != null ? obj.hashCode() : 0;
/*    */     }
/*    */     
/*    */     public boolean equals(Object a, Object b)
/*    */     {
/* 70 */       return (a == b) || ((a != null) && (a.equals(b)));
/*    */     }
/*    */   };
/*    */   
/*    */   public abstract int hashCode(T paramT);
/*    */   
/*    */   public abstract boolean equals(T paramT1, T paramT2);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\HashingStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */