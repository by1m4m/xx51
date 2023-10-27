/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.io.IOException;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AtomicReferenceDeserializer
/*    */   extends StdDeserializer<AtomicReference<?>>
/*    */   implements ContextualDeserializer
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final JavaType _referencedType;
/*    */   protected final TypeDeserializer _valueTypeDeserializer;
/*    */   protected final JsonDeserializer<?> _valueDeserializer;
/*    */   
/*    */   public AtomicReferenceDeserializer(JavaType referencedType)
/*    */   {
/* 31 */     this(referencedType, null, null);
/*    */   }
/*    */   
/*    */   public AtomicReferenceDeserializer(JavaType referencedType, TypeDeserializer typeDeser, JsonDeserializer<?> deser)
/*    */   {
/* 36 */     super(AtomicReference.class);
/* 37 */     this._referencedType = referencedType;
/* 38 */     this._valueDeserializer = deser;
/* 39 */     this._valueTypeDeserializer = typeDeser;
/*    */   }
/*    */   
/*    */   public AtomicReferenceDeserializer withResolved(TypeDeserializer typeDeser, JsonDeserializer<?> valueDeser) {
/* 43 */     return new AtomicReferenceDeserializer(this._referencedType, typeDeser, valueDeser);
/*    */   }
/*    */   
/*    */ 
/*    */   public AtomicReference<?> getNullValue()
/*    */   {
/* 49 */     return new AtomicReference();
/*    */   }
/*    */   
/*    */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*    */     throws JsonMappingException
/*    */   {
/* 55 */     JsonDeserializer<?> deser = this._valueDeserializer;
/* 56 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 57 */     if (deser == null) {
/* 58 */       deser = ctxt.findContextualValueDeserializer(this._referencedType, property);
/*    */     }
/* 60 */     if (typeDeser != null) {
/* 61 */       typeDeser = typeDeser.forProperty(property);
/*    */     }
/* 63 */     if ((deser == this._valueDeserializer) && (typeDeser == this._valueTypeDeserializer)) {
/* 64 */       return this;
/*    */     }
/* 66 */     return withResolved(typeDeser, deser);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public AtomicReference<?> deserialize(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException
/*    */   {
/* 75 */     if (this._valueTypeDeserializer != null) {
/* 76 */       return new AtomicReference(this._valueDeserializer.deserializeWithType(jp, ctxt, this._valueTypeDeserializer));
/*    */     }
/* 78 */     return new AtomicReference(this._valueDeserializer.deserialize(jp, ctxt));
/*    */   }
/*    */   
/*    */   public Object[] deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*    */     throws IOException
/*    */   {
/* 84 */     return (Object[])typeDeserializer.deserializeTypedFromAny(jp, ctxt);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\AtomicReferenceDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */