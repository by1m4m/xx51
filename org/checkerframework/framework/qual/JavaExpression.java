package org.checkerframework.framework.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JavaExpression {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\framework\qual\JavaExpression.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */