package org.checkerframework.checker.nullness.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({Raw.class})
@DefaultQualifierInHierarchy
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.IMPLICIT_UPPER_BOUND, org.checkerframework.framework.qual.TypeUseLocation.IMPLICIT_LOWER_BOUND, org.checkerframework.framework.qual.TypeUseLocation.EXCEPTION_PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface NonRaw {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\nullness\qual\NonRaw.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */