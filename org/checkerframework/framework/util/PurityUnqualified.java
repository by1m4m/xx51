package org.checkerframework.framework.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;

@InvisibleQualifier
@SubtypeOf({})
@DefaultQualifierInHierarchy
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface PurityUnqualified {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\framework\util\PurityUnqualified.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */