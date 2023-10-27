package org.checkerframework.common.aliasing.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@SubtypeOf({MaybeAliased.class})
public @interface Unique {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\aliasing\qual\Unique.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */