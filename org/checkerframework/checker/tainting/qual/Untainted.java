package org.checkerframework.checker.tainting.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({Tainted.class})
@ImplicitFor(literals={org.checkerframework.framework.qual.LiteralKind.STRING, org.checkerframework.framework.qual.LiteralKind.NULL})
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.LOWER_BOUND})
public @interface Untainted {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\tainting\qual\Untainted.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */