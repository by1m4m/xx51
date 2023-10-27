package org.checkerframework.checker.interning.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.ImplicitFor;
import org.checkerframework.framework.qual.SubtypeOf;

@SubtypeOf({UnknownInterned.class})
@ImplicitFor(literals={org.checkerframework.framework.qual.LiteralKind.PRIMITIVE, org.checkerframework.framework.qual.LiteralKind.STRING}, types={org.checkerframework.framework.qual.TypeKind.BOOLEAN, org.checkerframework.framework.qual.TypeKind.BYTE, org.checkerframework.framework.qual.TypeKind.CHAR, org.checkerframework.framework.qual.TypeKind.DOUBLE, org.checkerframework.framework.qual.TypeKind.FLOAT, org.checkerframework.framework.qual.TypeKind.INT, org.checkerframework.framework.qual.TypeKind.LONG, org.checkerframework.framework.qual.TypeKind.SHORT})
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface Interned {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\interning\qual\Interned.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */