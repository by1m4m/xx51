package org.checkerframework.common.aliasing.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE})
@SubtypeOf({})
public @interface NonLeaked {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\aliasing\qual\NonLeaked.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */