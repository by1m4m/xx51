package org.checkerframework.checker.nullness.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.DefaultInUncheckedCodeFor;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({MonotonicNonNull.class})
@ImplicitFor(literals={org.checkerframework.framework.qual.LiteralKind.STRING}, types={org.checkerframework.framework.qual.TypeKind.PACKAGE, org.checkerframework.framework.qual.TypeKind.INT, org.checkerframework.framework.qual.TypeKind.BOOLEAN, org.checkerframework.framework.qual.TypeKind.CHAR, org.checkerframework.framework.qual.TypeKind.DOUBLE, org.checkerframework.framework.qual.TypeKind.FLOAT, org.checkerframework.framework.qual.TypeKind.LONG, org.checkerframework.framework.qual.TypeKind.SHORT, org.checkerframework.framework.qual.TypeKind.BYTE})
@DefaultQualifierInHierarchy
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.EXCEPTION_PARAMETER})
@DefaultInUncheckedCodeFor({org.checkerframework.framework.qual.TypeUseLocation.PARAMETER, org.checkerframework.framework.qual.TypeUseLocation.LOWER_BOUND})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface NonNull {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\nullness\qual\NonNull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */