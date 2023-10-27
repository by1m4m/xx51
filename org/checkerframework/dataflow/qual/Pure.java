/*    */ package org.checkerframework.dataflow.qual;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Documented
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR})
/*    */ public @interface Pure
/*    */ {
/*    */   public static enum Kind
/*    */   {
/* 24 */     SIDE_EFFECT_FREE, 
/*    */     
/*    */ 
/* 27 */     DETERMINISTIC;
/*    */     
/*    */     private Kind() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\dataflow\qual\Pure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */