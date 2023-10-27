/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class SettableAnyProperty
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final BeanProperty _property;
/*     */   protected final AnnotatedMethod _setter;
/*     */   protected final JavaType _type;
/*     */   protected JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   
/*     */   public SettableAnyProperty(BeanProperty property, AnnotatedMethod setter, JavaType type, JsonDeserializer<Object> valueDeser, TypeDeserializer typeDeser)
/*     */   {
/*  50 */     this._property = property;
/*  51 */     this._setter = setter;
/*  52 */     this._type = type;
/*  53 */     this._valueDeserializer = valueDeser;
/*  54 */     this._valueTypeDeserializer = typeDeser;
/*     */   }
/*     */   
/*     */   public SettableAnyProperty withValueDeserializer(JsonDeserializer<Object> deser) {
/*  58 */     return new SettableAnyProperty(this._property, this._setter, this._type, deser, this._valueTypeDeserializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableAnyProperty(SettableAnyProperty src)
/*     */   {
/*  67 */     this._property = src._property;
/*  68 */     this._setter = src._setter;
/*  69 */     this._type = src._type;
/*  70 */     this._valueDeserializer = src._valueDeserializer;
/*  71 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
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
/*     */   Object readResolve()
/*     */   {
/*  85 */     if ((this._setter == null) || (this._setter.getAnnotated() == null)) {
/*  86 */       throw new IllegalArgumentException("Missing method (broken JDK (de)serialization?)");
/*     */     }
/*  88 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */   public BeanProperty getProperty() { return this._property; }
/*     */   
/*  99 */   public boolean hasValueDeserializer() { return this._valueDeserializer != null; }
/*     */   
/* 101 */   public JavaType getType() { return this._type; }
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
/*     */   public final void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object instance, String propName)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 118 */       set(instance, propName, deserialize(jp, ctxt));
/*     */     } catch (UnresolvedForwardReference reference) {
/* 120 */       if (this._valueDeserializer.getObjectIdReader() == null) {
/* 121 */         throw JsonMappingException.from(jp, "Unresolved forward reference but no identity info.", reference);
/*     */       }
/* 123 */       AnySetterReferring referring = new AnySetterReferring(this, reference, this._type.getRawClass(), instance, propName);
/*     */       
/* 125 */       reference.getRoid().appendReferring(referring);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */   {
/* 131 */     JsonToken t = jp.getCurrentToken();
/* 132 */     if (t == JsonToken.VALUE_NULL) {
/* 133 */       return null;
/*     */     }
/* 135 */     if (this._valueTypeDeserializer != null) {
/* 136 */       return this._valueDeserializer.deserializeWithType(jp, ctxt, this._valueTypeDeserializer);
/*     */     }
/* 138 */     return this._valueDeserializer.deserialize(jp, ctxt);
/*     */   }
/*     */   
/*     */   public void set(Object instance, String propName, Object value) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 145 */       this._setter.getAnnotated().invoke(instance, new Object[] { propName, value });
/*     */     } catch (Exception e) {
/* 147 */       _throwAsIOE(e, propName, value);
/*     */     }
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
/*     */   protected void _throwAsIOE(Exception e, String propName, Object value)
/*     */     throws IOException
/*     */   {
/* 165 */     if ((e instanceof IllegalArgumentException)) {
/* 166 */       String actType = value == null ? "[NULL]" : value.getClass().getName();
/* 167 */       StringBuilder msg = new StringBuilder("Problem deserializing \"any\" property '").append(propName);
/* 168 */       msg.append("' of class " + getClassName() + " (expected type: ").append(this._type);
/* 169 */       msg.append("; actual type: ").append(actType).append(")");
/* 170 */       String origMsg = e.getMessage();
/* 171 */       if (origMsg != null) {
/* 172 */         msg.append(", problem: ").append(origMsg);
/*     */       } else {
/* 174 */         msg.append(" (no error message provided)");
/*     */       }
/* 176 */       throw new JsonMappingException(msg.toString(), null, e);
/*     */     }
/* 178 */     if ((e instanceof IOException)) {
/* 179 */       throw ((IOException)e);
/*     */     }
/* 181 */     if ((e instanceof RuntimeException)) {
/* 182 */       throw ((RuntimeException)e);
/*     */     }
/*     */     
/* 185 */     Throwable t = e;
/* 186 */     while (t.getCause() != null) {
/* 187 */       t = t.getCause();
/*     */     }
/* 189 */     throw new JsonMappingException(t.getMessage(), null, t);
/*     */   }
/*     */   
/* 192 */   private String getClassName() { return this._setter.getDeclaringClass().getName(); }
/*     */   
/* 194 */   public String toString() { return "[any property on class " + getClassName() + "]"; }
/*     */   
/*     */   private static class AnySetterReferring extends ReadableObjectId.Referring
/*     */   {
/*     */     private final SettableAnyProperty _parent;
/*     */     private final Object _pojo;
/*     */     private final String _propName;
/*     */     
/*     */     public AnySetterReferring(SettableAnyProperty parent, UnresolvedForwardReference reference, Class<?> type, Object instance, String propName)
/*     */     {
/* 204 */       super(type);
/* 205 */       this._parent = parent;
/* 206 */       this._pojo = instance;
/* 207 */       this._propName = propName;
/*     */     }
/*     */     
/*     */ 
/*     */     public void handleResolvedForwardReference(Object id, Object value)
/*     */       throws IOException
/*     */     {
/* 214 */       if (!hasId(id)) {
/* 215 */         throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id.toString() + "] that wasn't previously registered.");
/*     */       }
/*     */       
/* 218 */       this._parent.set(this._pojo, this._propName, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\SettableAnyProperty.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */