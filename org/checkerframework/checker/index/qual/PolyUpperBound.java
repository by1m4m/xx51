package org.checkerframework.checker.index.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.PolymorphicQualifier;

@PolymorphicQualifier(UpperBoundUnknown.class)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface PolyUpperBound {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\index\qual\PolyUpperBound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */