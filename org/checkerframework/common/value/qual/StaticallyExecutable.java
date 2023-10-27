package org.checkerframework.common.value.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR})
public @interface StaticallyExecutable {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\value\qual\StaticallyExecutable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */