/*    */ package javax.annotation;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.lang.annotation.Documented;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.util.regex.Pattern;
/*    */ import java.util.regex.PatternSyntaxException;
/*    */ import javax.annotation.meta.TypeQualifierNickname;
/*    */ import javax.annotation.meta.TypeQualifierValidator;
/*    */ import javax.annotation.meta.When;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Documented
/*    */ @Syntax("RegEx")
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @TypeQualifierNickname
/*    */ public @interface RegEx
/*    */ {
/*    */   When when() default When.ALWAYS;
/*    */   
/*    */   public static class Checker
/*    */     implements TypeQualifierValidator<RegEx>
/*    */   {
/*    */     public When forConstantValue(RegEx annotation, Object value)
/*    */     {
/* 29 */       if (!(value instanceof String)) {
/* 30 */         return When.NEVER;
/*    */       }
/*    */       try {
/* 33 */         Pattern.compile((String)value);
/*    */       } catch (PatternSyntaxException e) {
/* 35 */         return When.NEVER;
/*    */       }
/* 37 */       return When.ALWAYS;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\javax\annotation\RegEx.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */