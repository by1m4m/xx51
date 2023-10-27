package org.checkerframework.framework.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.PACKAGE})
@SubtypeOf({})
public @interface FromByteCode {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\framework\qual\FromByteCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */