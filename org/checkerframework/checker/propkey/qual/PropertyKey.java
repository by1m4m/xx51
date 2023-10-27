package org.checkerframework.checker.propkey.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({UnknownPropertyKey.class})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface PropertyKey {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\propkey\qual\PropertyKey.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */