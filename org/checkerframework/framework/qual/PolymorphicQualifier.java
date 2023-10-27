package org.checkerframework.framework.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PolymorphicQualifier
{
  Class<? extends Annotation> value() default PolymorphicQualifier.class;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\framework\qual\PolymorphicQualifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */