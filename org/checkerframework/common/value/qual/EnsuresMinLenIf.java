package org.checkerframework.common.value.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ConditionalPostconditionAnnotation;
import org.checkerframework.framework.qual.InheritedAnnotation;
import org.checkerframework.framework.qual.QualifierArgument;

@ConditionalPostconditionAnnotation(qualifier=MinLen.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR})
@InheritedAnnotation
public @interface EnsuresMinLenIf
{
  String[] expression();
  
  boolean result();
  
  @QualifierArgument("value")
  int targetValue() default 0;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\value\qual\EnsuresMinLenIf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */