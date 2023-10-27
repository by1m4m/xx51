package com.google.errorprone.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Immutable
{
  String[] containerOf() default {};
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\errorprone\annotations\Immutable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */