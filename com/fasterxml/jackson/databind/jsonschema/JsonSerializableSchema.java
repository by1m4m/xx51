package com.fasterxml.jackson.databind.jsonschema;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotation
public @interface JsonSerializableSchema
{
  public static final String NO_VALUE = "##irrelevant";
  
  String id() default "";
  
  String schemaType() default "any";
  
  @Deprecated
  String schemaObjectPropertiesDefinition() default "##irrelevant";
  
  @Deprecated
  String schemaItemDefinition() default "##irrelevant";
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsonschema\JsonSerializableSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */