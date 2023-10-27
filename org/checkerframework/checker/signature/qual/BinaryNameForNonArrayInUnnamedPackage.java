package org.checkerframework.checker.signature.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({BinaryNameForNonArray.class, BinaryNameInUnnamedPackage.class, InternalFormForNonArray.class})
@ImplicitFor(stringPatterns={"^[A-Za-z_][A-Za-z_0-9]*(\\$[A-Za-z_0-9]+)*$"})
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface BinaryNameForNonArrayInUnnamedPackage {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\signature\qual\BinaryNameForNonArrayInUnnamedPackage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */