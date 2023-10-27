package com.google.errorprone.annotations.concurrent;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.FIELD})
public @interface LazyInit {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\errorprone\annotations\concurrent\LazyInit.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */