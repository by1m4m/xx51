package org.checkerframework.framework.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@PolymorphicQualifier
public @interface PolyAll {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\framework\qual\PolyAll.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */