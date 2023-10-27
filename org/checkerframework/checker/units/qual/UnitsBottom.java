package org.checkerframework.checker.units.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;

@SubtypeOf({})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.LOWER_BOUND})
@ImplicitFor(typeNames={Void.class}, literals={org.checkerframework.framework.qual.LiteralKind.NULL})
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@TargetLocations({org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_LOWER_BOUND, org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_UPPER_BOUND})
public @interface UnitsBottom {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\units\qual\UnitsBottom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */