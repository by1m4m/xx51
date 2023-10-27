package org.checkerframework.framework.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.TYPE})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldInvariant
{
  Class<? extends Annotation>[] qualifier();
  
  String[] field();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\framework\qual\FieldInvariant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */