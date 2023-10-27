package org.checkerframework.framework.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE})
@Inherited
public @interface RelevantJavaTypes
{
  Class<?>[] value();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\framework\qual\RelevantJavaTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */