package org.checkerframework.framework.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.ANNOTATION_TYPE})
public @interface ImplicitFor
{
  LiteralKind[] literals() default {};
  
  TypeKind[] types() default {};
  
  Class<?>[] typeNames() default {};
  
  String[] stringPatterns() default {};
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\framework\qual\ImplicitFor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */