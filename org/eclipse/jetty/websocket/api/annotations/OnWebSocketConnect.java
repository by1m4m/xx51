package org.eclipse.jetty.websocket.api.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface OnWebSocketConnect {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\api\annotations\OnWebSocketConnect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */