package com.fasterxml.jackson.databind.type;

import com.fasterxml.jackson.databind.JavaType;
import java.lang.reflect.Type;

public abstract class TypeModifier
{
  public abstract JavaType modifyType(JavaType paramJavaType, Type paramType, TypeBindings paramTypeBindings, TypeFactory paramTypeFactory);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\TypeModifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */