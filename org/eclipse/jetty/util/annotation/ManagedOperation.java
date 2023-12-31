package org.eclipse.jetty.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({java.lang.annotation.ElementType.METHOD})
public @interface ManagedOperation
{
  String value() default "Not Specified";
  
  String impact() default "UNKNOWN";
  
  boolean proxied() default false;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\util\annotation\ManagedOperation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */