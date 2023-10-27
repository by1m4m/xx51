package com.google.common.util.concurrent;

import com.google.common.annotations.GwtCompatible;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@GwtCompatible
final class Partially
{
  @Retention(RetentionPolicy.CLASS)
  @Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD})
  @Documented
  static @interface GwtIncompatible
  {
    String value();
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\util\concurrent\Partially.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */