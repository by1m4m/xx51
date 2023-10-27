package org.checkerframework.framework.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.SOURCE)
@Target({java.lang.annotation.ElementType.PACKAGE, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.LOCAL_VARIABLE, java.lang.annotation.ElementType.PARAMETER})
public @interface DefaultQualifier
{
  Class<? extends Annotation> value();
  
  TypeUseLocation[] locations() default {TypeUseLocation.ALL};
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\framework\qual\DefaultQualifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */