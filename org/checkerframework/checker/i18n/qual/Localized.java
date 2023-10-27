package org.checkerframework.checker.i18n.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({UnknownLocalized.class})
@ImplicitFor(literals={org.checkerframework.framework.qual.LiteralKind.INT, org.checkerframework.framework.qual.LiteralKind.LONG, org.checkerframework.framework.qual.LiteralKind.FLOAT, org.checkerframework.framework.qual.LiteralKind.DOUBLE, org.checkerframework.framework.qual.LiteralKind.BOOLEAN, org.checkerframework.framework.qual.LiteralKind.NULL})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface Localized {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\i18n\qual\Localized.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */