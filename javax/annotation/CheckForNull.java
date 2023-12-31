package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifierNickname;
import javax.annotation.meta.When;

@Documented
@Nonnull(when=When.MAYBE)
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifierNickname
public @interface CheckForNull {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\javax\annotation\CheckForNull.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */