package com.fasterxml.jackson.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonProperty
{
  public static final String USE_DEFAULT_NAME = "";
  public static final int INDEX_UNKNOWN = -1;
  
  String value() default "";
  
  boolean required() default false;
  
  int index() default -1;
  
  String defaultValue() default "";
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\annotation\JsonProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */