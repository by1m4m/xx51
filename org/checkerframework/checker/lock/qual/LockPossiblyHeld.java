package org.checkerframework.checker.lock.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.DefaultInUncheckedCodeFor;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchyInUncheckedCode;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;

@InvisibleQualifier
@SubtypeOf({})
@Documented
@DefaultQualifierInHierarchy
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.LOWER_BOUND})
@DefaultQualifierInHierarchyInUncheckedCode
@DefaultInUncheckedCodeFor({org.checkerframework.framework.qual.TypeUseLocation.PARAMETER, org.checkerframework.framework.qual.TypeUseLocation.LOWER_BOUND})
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface LockPossiblyHeld {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\lock\qual\LockPossiblyHeld.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */