package org.checkerframework.checker.index.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.InheritedAnnotation;
import org.checkerframework.framework.qual.JavaExpression;
import org.checkerframework.framework.qual.PostconditionAnnotation;
import org.checkerframework.framework.qual.QualifierArgument;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR})
@PostconditionAnnotation(qualifier=LTLengthOf.class)
@InheritedAnnotation
public @interface EnsuresLTLengthOf
{
  String[] value();
  
  @JavaExpression
  @QualifierArgument("value")
  String[] targetValue();
  
  @JavaExpression
  @QualifierArgument("offset")
  String[] offset() default {};
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\index\qual\EnsuresLTLengthOf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */