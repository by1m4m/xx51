/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class JsonValueSerializer
/*     */   extends StdSerializer<Object>
/*     */   implements ContextualSerializer, JsonFormatVisitable, SchemaAware
/*     */ {
/*     */   protected final Method _accessorMethod;
/*     */   protected final JsonSerializer<Object> _valueSerializer;
/*     */   protected final BeanProperty _property;
/*     */   protected final boolean _forceTypeInformation;
/*     */   
/*     */   public JsonValueSerializer(Method valueMethod, JsonSerializer<?> ser)
/*     */   {
/*  66 */     super(valueMethod.getReturnType(), false);
/*  67 */     this._accessorMethod = valueMethod;
/*  68 */     this._valueSerializer = ser;
/*  69 */     this._property = null;
/*  70 */     this._forceTypeInformation = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonValueSerializer(JsonValueSerializer src, BeanProperty property, JsonSerializer<?> ser, boolean forceTypeInfo)
/*     */   {
/*  77 */     super(_notNullClass(src.handledType()));
/*  78 */     this._accessorMethod = src._accessorMethod;
/*  79 */     this._valueSerializer = ser;
/*  80 */     this._property = property;
/*  81 */     this._forceTypeInformation = forceTypeInfo;
/*     */   }
/*     */   
/*     */   private static final Class<Object> _notNullClass(Class<?> cls)
/*     */   {
/*  86 */     return cls == null ? Object.class : cls;
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonValueSerializer withResolved(BeanProperty property, JsonSerializer<?> ser, boolean forceTypeInfo)
/*     */   {
/*  92 */     if ((this._property == property) && (this._valueSerializer == ser) && (forceTypeInfo == this._forceTypeInformation))
/*     */     {
/*  94 */       return this;
/*     */     }
/*  96 */     return new JsonValueSerializer(this, property, ser, forceTypeInfo);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 114 */     JsonSerializer<?> ser = this._valueSerializer;
/* 115 */     if (ser == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 120 */       if ((provider.isEnabled(MapperFeature.USE_STATIC_TYPING)) || (Modifier.isFinal(this._accessorMethod.getReturnType().getModifiers())))
/*     */       {
/* 122 */         JavaType t = provider.constructType(this._accessorMethod.getGenericReturnType());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 129 */         ser = provider.findPrimaryPropertySerializer(t, property);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 134 */         boolean forceTypeInformation = isNaturalTypeWithStdHandling(t.getRawClass(), ser);
/* 135 */         return withResolved(property, ser, forceTypeInformation);
/*     */       }
/*     */     }
/*     */     else {
/* 139 */       ser = provider.handlePrimaryContextualization(ser, property);
/* 140 */       return withResolved(property, ser, this._forceTypeInformation);
/*     */     }
/* 142 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 155 */       Object value = this._accessorMethod.invoke(bean, new Object[0]);
/* 156 */       if (value == null) {
/* 157 */         prov.defaultSerializeNull(jgen);
/* 158 */         return;
/*     */       }
/* 160 */       JsonSerializer<Object> ser = this._valueSerializer;
/* 161 */       if (ser == null) {
/* 162 */         Class<?> c = value.getClass();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 168 */         ser = prov.findTypedValueSerializer(c, true, this._property);
/*     */       }
/* 170 */       ser.serialize(value, jgen, prov);
/*     */     } catch (IOException ioe) {
/* 172 */       throw ioe;
/*     */     } catch (Exception e) {
/* 174 */       Throwable t = e;
/*     */       
/* 176 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 177 */         t = t.getCause();
/*     */       }
/*     */       
/* 180 */       if ((t instanceof Error)) {
/* 181 */         throw ((Error)t);
/*     */       }
/*     */       
/* 184 */       throw JsonMappingException.wrapWithPath(t, bean, this._accessorMethod.getName() + "()");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(Object bean, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer0)
/*     */     throws IOException
/*     */   {
/* 193 */     Object value = null;
/*     */     try {
/* 195 */       value = this._accessorMethod.invoke(bean, new Object[0]);
/*     */       
/* 197 */       if (value == null) {
/* 198 */         provider.defaultSerializeNull(jgen);
/* 199 */         return;
/*     */       }
/* 201 */       JsonSerializer<Object> ser = this._valueSerializer;
/* 202 */       if (ser == null)
/*     */       {
/* 204 */         ser = provider.findValueSerializer(value.getClass(), this._property);
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/* 209 */       else if (this._forceTypeInformation) {
/* 210 */         typeSer0.writeTypePrefixForScalar(bean, jgen);
/* 211 */         ser.serialize(value, jgen, provider);
/* 212 */         typeSer0.writeTypeSuffixForScalar(bean, jgen);
/* 213 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 221 */       ser.serializeWithType(value, jgen, provider, typeSer0);
/*     */     } catch (IOException ioe) {
/* 223 */       throw ioe;
/*     */     } catch (Exception e) {
/* 225 */       Throwable t = e;
/*     */       
/* 227 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 228 */         t = t.getCause();
/*     */       }
/*     */       
/* 231 */       if ((t instanceof Error)) {
/* 232 */         throw ((Error)t);
/*     */       }
/*     */       
/* 235 */       throw JsonMappingException.wrapWithPath(t, bean, this._accessorMethod.getName() + "()");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 244 */     if ((this._valueSerializer instanceof SchemaAware)) {
/* 245 */       return ((SchemaAware)this._valueSerializer).getSchema(provider, null);
/*     */     }
/* 247 */     return JsonSchema.getDefaultSchemaNode();
/*     */   }
/*     */   
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 254 */     JsonSerializer<Object> ser = this._valueSerializer;
/* 255 */     if (ser == null) {
/* 256 */       if (typeHint == null) {
/* 257 */         if (this._property != null) {
/* 258 */           typeHint = this._property.getType();
/*     */         }
/* 260 */         if (typeHint == null) {
/* 261 */           typeHint = visitor.getProvider().constructType(this._accessorMethod.getReturnType());
/*     */         }
/*     */       }
/* 264 */       ser = visitor.getProvider().findTypedValueSerializer(typeHint, false, this._property);
/* 265 */       if (ser == null) {
/* 266 */         visitor.expectAnyFormat(typeHint);
/* 267 */         return;
/*     */       }
/*     */     }
/* 270 */     ser.acceptJsonFormatVisitor(visitor, null);
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean isNaturalTypeWithStdHandling(Class<?> rawType, JsonSerializer<?> ser)
/*     */   {
/* 276 */     if (rawType.isPrimitive()) {
/* 277 */       if ((rawType != Integer.TYPE) && (rawType != Boolean.TYPE) && (rawType != Double.TYPE)) {
/* 278 */         return false;
/*     */       }
/*     */     }
/* 281 */     else if ((rawType != String.class) && (rawType != Integer.class) && (rawType != Boolean.class) && (rawType != Double.class))
/*     */     {
/* 283 */       return false;
/*     */     }
/*     */     
/* 286 */     return isDefaultSerializer(ser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 297 */     return "(@JsonValue serializer for method " + this._accessorMethod.getDeclaringClass() + "#" + this._accessorMethod.getName() + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\std\JsonValueSerializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */