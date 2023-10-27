package org.checkerframework.checker.lock.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;
import org.checkerframework.framework.qual.TargetLocations;

@SubtypeOf({GuardedByUnknown.class})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@TargetLocations({org.checkerframework.framework.qual.TypeUseLocation.RECEIVER, org.checkerframework.framework.qual.TypeUseLocation.PARAMETER, org.checkerframework.framework.qual.TypeUseLocation.RETURN})
@Target({java.lang.annotation.ElementType.TYPE_USE})
public @interface GuardSatisfied
{
  int value() default -1;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\lock\qual\GuardSatisfied.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */