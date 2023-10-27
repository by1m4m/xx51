package com.google.api.client.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Value
{
  String value() default "##default";
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\util\Value.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */