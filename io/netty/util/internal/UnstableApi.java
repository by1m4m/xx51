package io.netty.util.internal;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PACKAGE, java.lang.annotation.ElementType.TYPE})
@Documented
public @interface UnstableApi {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\internal\UnstableApi.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */