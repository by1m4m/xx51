/*    */ package com.fasterxml.jackson.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ import java.lang.reflect.Member;
/*    */ import java.lang.reflect.Modifier;
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
/*    */ @Target({java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.TYPE})
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @JacksonAnnotation
/*    */ public @interface JsonAutoDetect
/*    */ {
/*    */   Visibility getterVisibility() default Visibility.DEFAULT;
/*    */   
/*    */   Visibility isGetterVisibility() default Visibility.DEFAULT;
/*    */   
/*    */   Visibility setterVisibility() default Visibility.DEFAULT;
/*    */   
/*    */   Visibility creatorVisibility() default Visibility.DEFAULT;
/*    */   
/*    */   Visibility fieldVisibility() default Visibility.DEFAULT;
/*    */   
/*    */   public static enum Visibility
/*    */   {
/* 48 */     ANY, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 53 */     NON_PRIVATE, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 59 */     PROTECTED_AND_PUBLIC, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 64 */     PUBLIC_ONLY, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 70 */     NONE, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 77 */     DEFAULT;
/*    */     
/*    */     private Visibility() {}
/* 80 */     public boolean isVisible(Member m) { switch (JsonAutoDetect.1.$SwitchMap$com$fasterxml$jackson$annotation$JsonAutoDetect$Visibility[ordinal()]) {
/*    */       case 1: 
/* 82 */         return true;
/*    */       case 2: 
/* 84 */         return false;
/*    */       case 3: 
/* 86 */         return !Modifier.isPrivate(m.getModifiers());
/*    */       case 4: 
/* 88 */         if (Modifier.isProtected(m.getModifiers())) {
/* 89 */           return true;
/*    */         }
/*    */       
/*    */       case 5: 
/* 93 */         return Modifier.isPublic(m.getModifiers());
/*    */       }
/* 95 */       return false;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\annotation\JsonAutoDetect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */