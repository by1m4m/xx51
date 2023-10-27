package org.checkerframework.common.value.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;

@InvisibleQualifier
@ImplicitFor(literals={org.checkerframework.framework.qual.LiteralKind.NULL}, typeNames={Void.class})
@SubtypeOf({ArrayLen.class, BoolVal.class, DoubleVal.class, IntVal.class, StringVal.class, ArrayLenRange.class, IntRange.class, IntRangeFromPositive.class, IntRangeFromGTENegativeOne.class, IntRangeFromNonNegative.class})
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@TargetLocations({org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_LOWER_BOUND, org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_UPPER_BOUND})
public @interface BottomVal {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\value\qual\BottomVal.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */