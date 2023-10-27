/*    */ package io.netty.util;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicLong;
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
/*    */ public abstract class AbstractConstant<T extends AbstractConstant<T>>
/*    */   implements Constant<T>
/*    */ {
/* 25 */   private static final AtomicLong uniqueIdGenerator = new AtomicLong();
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private final String name;
/*    */   private final long uniquifier;
/*    */   
/*    */   protected AbstractConstant(int id, String name)
/*    */   {
/* 34 */     this.id = id;
/* 35 */     this.name = name;
/* 36 */     this.uniquifier = uniqueIdGenerator.getAndIncrement();
/*    */   }
/*    */   
/*    */   public final String name()
/*    */   {
/* 41 */     return this.name;
/*    */   }
/*    */   
/*    */   public final int id()
/*    */   {
/* 46 */     return this.id;
/*    */   }
/*    */   
/*    */   public final String toString()
/*    */   {
/* 51 */     return name();
/*    */   }
/*    */   
/*    */   public final int hashCode()
/*    */   {
/* 56 */     return super.hashCode();
/*    */   }
/*    */   
/*    */   public final boolean equals(Object obj)
/*    */   {
/* 61 */     return super.equals(obj);
/*    */   }
/*    */   
/*    */   public final int compareTo(T o)
/*    */   {
/* 66 */     if (this == o) {
/* 67 */       return 0;
/*    */     }
/*    */     
/*    */ 
/* 71 */     AbstractConstant<T> other = o;
/*    */     
/*    */ 
/* 74 */     int returnCode = hashCode() - other.hashCode();
/* 75 */     if (returnCode != 0) {
/* 76 */       return returnCode;
/*    */     }
/*    */     
/* 79 */     if (this.uniquifier < other.uniquifier) {
/* 80 */       return -1;
/*    */     }
/* 82 */     if (this.uniquifier > other.uniquifier) {
/* 83 */       return 1;
/*    */     }
/*    */     
/* 86 */     throw new Error("failed to compare two different constants");
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\AbstractConstant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */