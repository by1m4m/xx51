package com.fasterxml.jackson.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonPropertyOrder
{
  String[] value() default {};
  
  boolean alphabetic() default false;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\annotation\JsonPropertyOrder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */