package com.google.api.client.json;

import com.google.api.client.util.Beta;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Beta
public @interface JsonPolymorphicTypeMap
{
  TypeDef[] typeDefinitions();
  
  @Target({java.lang.annotation.ElementType.FIELD})
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface TypeDef
  {
    String key();
    
    Class<?> ref();
  }
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\api\client\json\JsonPolymorphicTypeMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */