package org.checkerframework.common.util.report.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface ReportOverride {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\util\report\qual\ReportOverride.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */