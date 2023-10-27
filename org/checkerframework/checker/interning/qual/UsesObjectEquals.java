package org.checkerframework.checker.interning.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Inherited
@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface UsesObjectEquals {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\interning\qual\UsesObjectEquals.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */