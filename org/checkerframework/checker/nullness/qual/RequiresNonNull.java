package org.checkerframework.checker.nullness.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.PreconditionAnnotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR})
@PreconditionAnnotation(qualifier=NonNull.class)
public @interface RequiresNonNull
{
  String[] value();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\nullness\qual\RequiresNonNull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */