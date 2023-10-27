package org.checkerframework.checker.guieffect.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD})
public @interface PolyUIEffect {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\guieffect\qual\PolyUIEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */