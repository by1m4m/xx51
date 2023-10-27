package org.checkerframework.checker.index.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.PolymorphicQualifier;

@PolymorphicQualifier
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface PolyLength {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\index\qual\PolyLength.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */