package javax.annotation.meta;

import java.lang.annotation.Annotation;
import javax.annotation.Nonnull;

public abstract interface TypeQualifierValidator<A extends Annotation>
{
  @Nonnull
  public abstract When forConstantValue(@Nonnull A paramA, Object paramObject);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\javax\annotation\meta\TypeQualifierValidator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */