package org.checkerframework.common.aliasing.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@DefaultQualifierInHierarchy
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.UPPER_BOUND, org.checkerframework.framework.qual.TypeUseLocation.LOWER_BOUND})
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_PARAMETER, java.lang.annotation.ElementType.TYPE_USE})
@ImplicitFor(literals={org.checkerframework.framework.qual.LiteralKind.NULL}, typeNames={Void.class})
@SubtypeOf({})
public @interface MaybeAliased {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\aliasing\qual\MaybeAliased.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */