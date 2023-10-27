package org.eclipse.jetty.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({java.lang.annotation.ElementType.METHOD})
public @interface ManagedAttribute
{
  String value() default "Not Specified";
  
  String name() default "";
  
  boolean readonly() default false;
  
  boolean proxied() default false;
  
  String setter() default "";
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\annotation\ManagedAttribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */