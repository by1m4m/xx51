package com.fasterxml.jackson.databind.jsontype;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializationConfig;
import java.util.Collection;

public abstract interface TypeResolverBuilder<T extends TypeResolverBuilder<T>>
{
  public abstract Class<?> getDefaultImpl();
  
  public abstract TypeSerializer buildTypeSerializer(SerializationConfig paramSerializationConfig, JavaType paramJavaType, Collection<NamedType> paramCollection);
  
  public abstract TypeDeserializer buildTypeDeserializer(DeserializationConfig paramDeserializationConfig, JavaType paramJavaType, Collection<NamedType> paramCollection);
  
  public abstract T init(JsonTypeInfo.Id paramId, TypeIdResolver paramTypeIdResolver);
  
  public abstract T inclusion(JsonTypeInfo.As paramAs);
  
  public abstract T typeProperty(String paramString);
  
  public abstract T defaultImpl(Class<?> paramClass);
  
  public abstract T typeIdVisibility(boolean paramBoolean);
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\jsontype\TypeResolverBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */