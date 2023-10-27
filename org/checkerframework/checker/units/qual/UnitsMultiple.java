package org.checkerframework.checker.units.qual;

import java.lang.annotation.Annotation;

public @interface UnitsMultiple
{
  Class<? extends Annotation> quantity();
  
  Prefix prefix() default Prefix.one;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\checkerframework\checker\units\qual\UnitsMultiple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */