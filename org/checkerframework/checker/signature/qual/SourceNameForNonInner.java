package org.checkerframework.checker.signature.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({FullyQualifiedName.class, BinaryName.class})
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface SourceNameForNonInner {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\signature\qual\SourceNameForNonInner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */