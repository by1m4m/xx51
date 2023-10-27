package org.checkerframework.checker.nullness.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;

@InvisibleQualifier
@SubtypeOf({})
@DefaultQualifierInHierarchy
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.LOWER_BOUND})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface UnknownKeyFor {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\nullness\qual\UnknownKeyFor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */