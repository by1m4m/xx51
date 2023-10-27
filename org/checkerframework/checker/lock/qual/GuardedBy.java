package org.checkerframework.checker.lock.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.DefaultInUncheckedCodeFor;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.JavaExpression;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({GuardedByUnknown.class})
@Documented
@DefaultQualifierInHierarchy
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.EXCEPTION_PARAMETER, org.checkerframework.framework.qual.TypeUseLocation.UPPER_BOUND})
@DefaultInUncheckedCodeFor({org.checkerframework.framework.qual.TypeUseLocation.PARAMETER})
@ImplicitFor(types={org.checkerframework.framework.qual.TypeKind.BOOLEAN, org.checkerframework.framework.qual.TypeKind.BYTE, org.checkerframework.framework.qual.TypeKind.CHAR, org.checkerframework.framework.qual.TypeKind.DOUBLE, org.checkerframework.framework.qual.TypeKind.FLOAT, org.checkerframework.framework.qual.TypeKind.INT, org.checkerframework.framework.qual.TypeKind.LONG, org.checkerframework.framework.qual.TypeKind.SHORT}, typeNames={String.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface GuardedBy
{
  @JavaExpression
  String[] value() default {};
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\lock\qual\GuardedBy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */