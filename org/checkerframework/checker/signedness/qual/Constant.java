package org.checkerframework.checker.signedness.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@SubtypeOf({Unsigned.class, Signed.class})
@ImplicitFor(literals={org.checkerframework.framework.qual.LiteralKind.INT, org.checkerframework.framework.qual.LiteralKind.LONG, org.checkerframework.framework.qual.LiteralKind.CHAR})
public @interface Constant {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\signedness\qual\Constant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */