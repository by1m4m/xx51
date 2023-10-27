package javax.annotation.concurrent;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface GuardedBy
{
  String value();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\javax\annotation\concurrent\GuardedBy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */