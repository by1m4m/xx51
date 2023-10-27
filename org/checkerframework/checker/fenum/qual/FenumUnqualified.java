package org.checkerframework.checker.fenum.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({FenumTop.class})
@DefaultQualifierInHierarchy
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.EXCEPTION_PARAMETER})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface FenumUnqualified {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\fenum\qual\FenumUnqualified.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */