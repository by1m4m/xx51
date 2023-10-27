package com.google.errorprone.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.lang.model.element.Modifier;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({java.lang.annotation.ElementType.ANNOTATION_TYPE})
public @interface IncompatibleModifiers
{
  Modifier[] value();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\errorprone\annotations\IncompatibleModifiers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */