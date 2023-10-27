package org.checkerframework.checker.nullness.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.InheritedAnnotation;
import org.checkerframework.framework.qual.PostconditionAnnotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR})
@PostconditionAnnotation(qualifier=NonNull.class)
@InheritedAnnotation
public @interface EnsuresNonNull
{
  String[] value();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\nullness\qual\EnsuresNonNull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */