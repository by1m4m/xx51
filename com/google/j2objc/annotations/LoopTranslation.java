/*    */ package com.google.j2objc.annotations;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
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
/*    */ @Target({java.lang.annotation.ElementType.LOCAL_VARIABLE})
/*    */ @Retention(RetentionPolicy.SOURCE)
/*    */ public @interface LoopTranslation
/*    */ {
/*    */   LoopStyle value();
/*    */   
/*    */   public static enum LoopStyle
/*    */   {
/* 44 */     JAVA_ITERATOR, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 50 */     FAST_ENUMERATION;
/*    */     
/*    */     private LoopStyle() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\j2objc\annotations\LoopTranslation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */