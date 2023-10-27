package org.checkerframework.checker.signedness.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;

@SubtypeOf({Constant.class})
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@TargetLocations({org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_LOWER_BOUND, org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_UPPER_BOUND})
@ImplicitFor(literals={org.checkerframework.framework.qual.LiteralKind.NULL}, types={org.checkerframework.framework.qual.TypeKind.NULL})
public @interface SignednessBottom {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\signedness\qual\SignednessBottom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */