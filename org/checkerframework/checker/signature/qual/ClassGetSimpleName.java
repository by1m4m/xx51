package org.checkerframework.checker.signature.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({SignatureUnknown.class})
@ImplicitFor(stringPatterns={"^(|[A-Za-z_][A-Za-z_0-9]*)(\\[\\])*$"})
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface ClassGetSimpleName {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\signature\qual\ClassGetSimpleName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */