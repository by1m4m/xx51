package org.checkerframework.checker.optional.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.PolymorphicQualifier;

@Documented
@PolymorphicQualifier(MaybePresent.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface PolyPresent {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\optional\qual\PolyPresent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */