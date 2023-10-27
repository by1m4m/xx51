package javax.annotation.meta;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Target({java.lang.annotation.ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeQualifier
{
  Class<?> applicableTo() default Object.class;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\javax\annotation\meta\TypeQualifier.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */