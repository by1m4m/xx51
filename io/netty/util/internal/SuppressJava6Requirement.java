package io.netty.util.internal;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface SuppressJava6Requirement
{
  String reason();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\SuppressJava6Requirement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */