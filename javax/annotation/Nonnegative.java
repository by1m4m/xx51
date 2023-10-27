/*    */ package javax.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import javax.annotation.meta.TypeQualifier;
/*    */ import javax.annotation.meta.TypeQualifierValidator;
/*    */ import javax.annotation.meta.When;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Documented
/*    */ @TypeQualifier(applicableTo=Number.class)
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ public @interface Nonnegative
/*    */ {
/*    */   When when() default When.ALWAYS;
/*    */   
/*    */   public static class Checker
/*    */     implements TypeQualifierValidator<Nonnegative>
/*    */   {
/*    */     public When forConstantValue(Nonnegative annotation, Object v)
/*    */     {
/* 25 */       if (!(v instanceof Number)) {
/* 26 */         return When.NEVER;
/*    */       }
/* 28 */       Number value = (Number)v;
/* 29 */       boolean isNegative; boolean isNegative; if ((value instanceof Long)) {
/* 30 */         isNegative = value.longValue() < 0L; } else { boolean isNegative;
/* 31 */         if ((value instanceof Double)) {
/* 32 */           isNegative = value.doubleValue() < 0.0D; } else { boolean isNegative;
/* 33 */           if ((value instanceof Float)) {
/* 34 */             isNegative = value.floatValue() < 0.0F;
/*    */           } else
/* 36 */             isNegative = value.intValue() < 0;
/*    */         } }
/* 38 */       if (isNegative) {
/* 39 */         return When.NEVER;
/*    */       }
/* 41 */       return When.ALWAYS;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\javax\annotation\Nonnegative.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */