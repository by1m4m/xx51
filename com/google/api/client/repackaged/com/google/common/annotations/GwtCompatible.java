package com.google.api.client.repackaged.com.google.common.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
@Documented
@GwtCompatible
public @interface GwtCompatible
{
  boolean serializable() default false;
  
  boolean emulated() default false;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\repackaged\com\google\common\annotations\GwtCompatible.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */