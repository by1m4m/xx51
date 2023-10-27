package org.checkerframework.common.value.qual;

import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

@Inherited
@Target({java.lang.annotation.ElementType.TYPE})
public @interface MinLenFieldInvariant
{
  int[] minLen();
  
  String[] field();
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\common\value\qual\MinLenFieldInvariant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */