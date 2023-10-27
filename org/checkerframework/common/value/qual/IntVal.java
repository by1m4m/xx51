package org.checkerframework.common.value.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({UnknownVal.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_PARAMETER, java.lang.annotation.ElementType.TYPE_USE})
public @interface IntVal
{
  long[] value();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\value\qual\IntVal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */