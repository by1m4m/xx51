package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifierDefault;

@Documented
@Nonnull
@TypeQualifierDefault({java.lang.annotation.ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ParametersAreNonnullByDefault {}


/* Location:              C:\Users\Lab\Desktop\a.jar!\javax\annotation\ParametersAreNonnullByDefault.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */