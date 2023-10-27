package org.checkerframework.common.reflection.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;

@InvisibleQualifier
@SubtypeOf({})
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@TargetLocations({org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_LOWER_BOUND, org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_UPPER_BOUND})
@DefaultQualifierInHierarchy
public @interface UnknownClass {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\reflection\qual\UnknownClass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */