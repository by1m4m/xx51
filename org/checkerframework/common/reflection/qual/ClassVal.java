package org.checkerframework.common.reflection.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({UnknownClass.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface ClassVal
{
  String[] value();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\reflection\qual\ClassVal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */