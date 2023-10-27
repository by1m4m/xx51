package org.checkerframework.common.value.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.PolymorphicQualifier;

@PolymorphicQualifier(UnknownVal.class)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface PolyValue {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\value\qual\PolyValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */