package com.fasterxml.jackson.databind.ser;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
import com.fasterxml.jackson.databind.node.ObjectNode;

public abstract interface PropertyFilter
{
  public abstract void serializeAsField(Object paramObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, PropertyWriter paramPropertyWriter)
    throws Exception;
  
  public abstract void serializeAsElement(Object paramObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, PropertyWriter paramPropertyWriter)
    throws Exception;
  
  @Deprecated
  public abstract void depositSchemaProperty(PropertyWriter paramPropertyWriter, ObjectNode paramObjectNode, SerializerProvider paramSerializerProvider)
    throws JsonMappingException;
  
  public abstract void depositSchemaProperty(PropertyWriter paramPropertyWriter, JsonObjectFormatVisitor paramJsonObjectFormatVisitor, SerializerProvider paramSerializerProvider)
    throws JsonMappingException;
}


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\PropertyFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */