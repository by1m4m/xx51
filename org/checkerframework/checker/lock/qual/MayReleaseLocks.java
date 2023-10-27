package org.checkerframework.checker.lock.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.InheritedAnnotation;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@InheritedAnnotation
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR})
public @interface MayReleaseLocks {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\lock\qual\MayReleaseLocks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */