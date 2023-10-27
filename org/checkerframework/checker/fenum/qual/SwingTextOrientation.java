package org.checkerframework.checker.fenum.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@SubtypeOf({FenumTop.class})
public @interface SwingTextOrientation {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\fenum\qual\SwingTextOrientation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */