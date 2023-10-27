package org.checkerframework.checker.index.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.JavaExpression;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({LTLengthOf.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface LTOMLengthOf
{
  @JavaExpression
  String[] value();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\index\qual\LTOMLengthOf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */