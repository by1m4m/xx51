package org.checkerframework.common.value.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface MinLen
{
  int value() default 0;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\value\qual\MinLen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */