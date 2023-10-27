package org.checkerframework.checker.regex.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;

@InvisibleQualifier
@SubtypeOf({UnknownRegex.class})
@Target({})
public @interface PartialRegex
{
  String value() default "";
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\regex\qual\PartialRegex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */