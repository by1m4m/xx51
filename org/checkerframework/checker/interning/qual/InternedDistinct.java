package org.checkerframework.checker.interning.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultFor;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({Interned.class})
@DefaultFor({org.checkerframework.framework.qual.TypeUseLocation.LOWER_BOUND})
@ImplicitFor(literals={org.checkerframework.framework.qual.LiteralKind.NULL}, typeNames={Void.class})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface InternedDistinct {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\interning\qual\InternedDistinct.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */