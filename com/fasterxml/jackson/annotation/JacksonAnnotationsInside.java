package com.fasterxml.jackson.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JacksonAnnotationsInside {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\annotation\JacksonAnnotationsInside.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */