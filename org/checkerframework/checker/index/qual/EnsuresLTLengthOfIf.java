package org.checkerframework.checker.index.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ConditionalPostconditionAnnotation;
import org.checkerframework.framework.qual.InheritedAnnotation;
import org.checkerframework.framework.qual.JavaExpression;
import org.checkerframework.framework.qual.QualifierArgument;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR})
@ConditionalPostconditionAnnotation(qualifier=LTLengthOf.class)
@InheritedAnnotation
public @interface EnsuresLTLengthOfIf
{
  String[] expression();
  
  boolean result();
  
  @JavaExpression
  @QualifierArgument("value")
  String[] targetValue();
  
  @JavaExpression
  @QualifierArgument("offset")
  String[] offset() default {};
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\index\qual\EnsuresLTLengthOfIf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */