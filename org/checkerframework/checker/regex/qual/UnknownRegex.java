package org.checkerframework.checker.regex.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;

@InvisibleQualifier
@DefaultQualifierInHierarchy
@SubtypeOf({})
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@TargetLocations({org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_LOWER_BOUND, org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_UPPER_BOUND})
public @interface UnknownRegex {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\regex\qual\UnknownRegex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */