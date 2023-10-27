package org.checkerframework.checker.signature.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;

@SubtypeOf({Identifier.class, FieldDescriptorForArray.class, MethodDescriptor.class})
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@TargetLocations({org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_LOWER_BOUND, org.checkerframework.framework.qual.TypeUseLocation.EXPLICIT_UPPER_BOUND})
@ImplicitFor(literals={org.checkerframework.framework.qual.LiteralKind.NULL}, typeNames={Void.class})
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.LOWER_BOUND})
public @interface SignatureBottom {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\signature\qual\SignatureBottom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */