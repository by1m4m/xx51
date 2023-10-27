package org.checkerframework.framework.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Target;

@SubtypeOf({})
@Target({java.lang.annotation.ElementType.TYPE_USE, java.lang.annotation.ElementType.TYPE_PARAMETER})
@TargetLocations({TypeUseLocation.EXPLICIT_LOWER_BOUND, TypeUseLocation.EXPLICIT_UPPER_BOUND})
public @interface Bottom {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\framework\qual\Bottom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */