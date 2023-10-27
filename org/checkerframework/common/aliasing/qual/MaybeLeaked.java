package org.checkerframework.common.aliasing.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@DefaultQualifierInHierarchy
@Retention(RetentionPolicy.RUNTIME)
@Target({})
@SubtypeOf({LeakedToResult.class})
@InvisibleQualifier
public @interface MaybeLeaked {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\aliasing\qual\MaybeLeaked.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */