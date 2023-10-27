package com.google.errorprone.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({java.lang.annotation.ElementType.METHOD})
@IncompatibleModifiers({javax.lang.model.element.Modifier.PUBLIC, javax.lang.model.element.Modifier.PRIVATE, javax.lang.model.element.Modifier.STATIC, javax.lang.model.element.Modifier.FINAL})
public @interface ForOverride {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\errorprone\annotations\ForOverride.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */