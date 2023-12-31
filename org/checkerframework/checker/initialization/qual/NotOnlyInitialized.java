package org.checkerframework.checker.initialization.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotOnlyInitialized {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\initialization\qual\NotOnlyInitialized.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */