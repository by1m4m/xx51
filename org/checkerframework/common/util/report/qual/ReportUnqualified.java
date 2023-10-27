package org.checkerframework.common.util.report.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import org.checkerframework.framework.qual.DefaultQualifierInHierarchy;
import org.checkerframework.framework.qual.InvisibleQualifier;
import org.checkerframework.framework.qual.SubtypeOf;

@InvisibleQualifier
@SubtypeOf({})
@DefaultQualifierInHierarchy
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
public @interface ReportUnqualified {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\util\report\qual\ReportUnqualified.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */