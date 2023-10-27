package org.checkerframework.checker.regex.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@SubtypeOf({UnknownRegex.class})
public @interface Regex
{
  int value() default 0;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\regex\qual\Regex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */