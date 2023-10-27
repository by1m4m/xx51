package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.annotation.meta.When;

@Documented
@Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckReturnValue
{
  When when() default When.ALWAYS;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\javax\annotation\CheckReturnValue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */