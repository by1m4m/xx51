package org.checkerframework.checker.units.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@SubtypeOf({Area.class})
public @interface m2
{
  Prefix value() default Prefix.one;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\units\qual\m2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */