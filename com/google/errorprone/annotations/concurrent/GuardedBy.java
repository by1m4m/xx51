package com.google.errorprone.annotations.concurrent;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface GuardedBy
{
  String value();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\errorprone\annotations\concurrent\GuardedBy.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */