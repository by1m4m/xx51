package com.google.errorprone.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.PACKAGE})
@Retention(RetentionPolicy.CLASS)
public @interface SuppressPackageLocation {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\errorprone\annotations\SuppressPackageLocation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */