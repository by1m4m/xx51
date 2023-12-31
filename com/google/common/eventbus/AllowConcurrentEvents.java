package com.google.common.eventbus;

import com.google.common.annotations.Beta;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
@Beta
public @interface AllowConcurrentEvents {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\eventbus\AllowConcurrentEvents.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */