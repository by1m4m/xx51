package org.checkerframework.checker.fenum.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;

@SubtypeOf({})
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.LOCAL_VARIABLE, org.checkerframework.framework.qual.TypeUseLocation.RESOURCE_VARIABLE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@TargetLocations({org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_LOWER_BOUND, org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_UPPER_BOUND})
public @interface FenumTop {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\fenum\qual\FenumTop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */