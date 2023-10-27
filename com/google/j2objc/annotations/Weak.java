package com.google.j2objc.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.LOCAL_VARIABLE, java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.CLASS)
public @interface Weak {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\j2objc\annotations\Weak.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */