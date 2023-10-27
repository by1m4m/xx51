package org.checkerframework.checker.nullness.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.MonotonicQualifier;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@SubtypeOf({Nullable.class})
@Target({java.lang.annotation.ElementType.TYPE_USE})
@MonotonicQualifier(NonNull.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface MonotonicNonNull {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\nullness\qual\MonotonicNonNull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */