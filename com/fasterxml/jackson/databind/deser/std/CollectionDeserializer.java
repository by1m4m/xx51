/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class CollectionDeserializer
/*     */   extends ContainerDeserializerBase<Collection<Object>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = -1L;
/*     */   protected final JavaType _collectionType;
/*     */   protected final JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final JsonDeserializer<Object> _delegateDeserializer;
/*     */   
/*     */   public CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator)
/*     */   {
/*  73 */     this(collectionType, valueDeser, valueTypeDeser, valueInstantiator, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator, JsonDeserializer<Object> delegateDeser)
/*     */   {
/*  84 */     super(collectionType);
/*  85 */     this._collectionType = collectionType;
/*  86 */     this._valueDeserializer = valueDeser;
/*  87 */     this._valueTypeDeserializer = valueTypeDeser;
/*  88 */     this._valueInstantiator = valueInstantiator;
/*  89 */     this._delegateDeserializer = delegateDeser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CollectionDeserializer(CollectionDeserializer src)
/*     */   {
/*  98 */     super(src._collectionType);
/*  99 */     this._collectionType = src._collectionType;
/* 100 */     this._valueDeserializer = src._valueDeserializer;
/* 101 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 102 */     this._valueInstantiator = src._valueInstantiator;
/* 103 */     this._delegateDeserializer = src._delegateDeserializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CollectionDeserializer withResolved(JsonDeserializer<?> dd, JsonDeserializer<?> vd, TypeDeserializer vtd)
/*     */   {
/* 113 */     if ((dd == this._delegateDeserializer) && (vd == this._valueDeserializer) && (vtd == this._valueTypeDeserializer)) {
/* 114 */       return this;
/*     */     }
/* 116 */     return new CollectionDeserializer(this._collectionType, vd, vtd, this._valueInstantiator, dd);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/* 125 */     return (this._valueDeserializer == null) && (this._valueTypeDeserializer == null) && (this._delegateDeserializer == null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CollectionDeserializer createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 147 */     JsonDeserializer<Object> delegateDeser = null;
/* 148 */     if ((this._valueInstantiator != null) && (this._valueInstantiator.canCreateUsingDelegate())) {
/* 149 */       JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/* 150 */       if (delegateType == null) {
/* 151 */         throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._collectionType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
/*     */       }
/*     */       
/*     */ 
/* 155 */       delegateDeser = findDeserializer(ctxt, delegateType, property);
/*     */     }
/*     */     
/* 158 */     JsonDeserializer<?> valueDeser = this._valueDeserializer;
/*     */     
/*     */ 
/* 161 */     valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
/* 162 */     JavaType vt = this._collectionType.getContentType();
/* 163 */     if (valueDeser == null) {
/* 164 */       valueDeser = ctxt.findContextualValueDeserializer(vt, property);
/*     */     } else {
/* 166 */       valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, vt);
/*     */     }
/*     */     
/* 169 */     TypeDeserializer valueTypeDeser = this._valueTypeDeserializer;
/* 170 */     if (valueTypeDeser != null) {
/* 171 */       valueTypeDeser = valueTypeDeser.forProperty(property);
/*     */     }
/* 173 */     return withResolved(delegateDeser, valueDeser, valueTypeDeser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/* 184 */     return this._collectionType.getContentType();
/*     */   }
/*     */   
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 189 */     return this._valueDeserializer;
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
/*     */   public Collection<Object> deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 203 */     if (this._delegateDeserializer != null) {
/* 204 */       return (Collection)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 211 */     if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
/* 212 */       String str = p.getText();
/* 213 */       if (str.length() == 0) {
/* 214 */         return (Collection)this._valueInstantiator.createFromString(ctxt, str);
/*     */       }
/*     */     }
/* 217 */     return deserialize(p, ctxt, (Collection)this._valueInstantiator.createUsingDefault(ctxt));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<Object> deserialize(JsonParser p, DeserializationContext ctxt, Collection<Object> result)
/*     */     throws IOException
/*     */   {
/* 226 */     if (!p.isExpectedStartArrayToken()) {
/* 227 */       return handleNonArray(p, ctxt, result);
/*     */     }
/*     */     
/* 230 */     p.setCurrentValue(result);
/*     */     
/* 232 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 233 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 234 */     CollectionReferringAccumulator referringAccumulator = valueDes.getObjectIdReader() == null ? null : new CollectionReferringAccumulator(this._collectionType.getContentType().getRawClass(), result);
/*     */     
/*     */ 
/*     */     JsonToken t;
/*     */     
/* 239 */     while ((t = p.nextToken()) != JsonToken.END_ARRAY) {
/*     */       try { Object value;
/*     */         Object value;
/* 242 */         if (t == JsonToken.VALUE_NULL) {
/* 243 */           value = valueDes.getNullValue(); } else { Object value;
/* 244 */           if (typeDeser == null) {
/* 245 */             value = valueDes.deserialize(p, ctxt);
/*     */           } else
/* 247 */             value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */         }
/* 249 */         if (referringAccumulator != null) {
/* 250 */           referringAccumulator.add(value);
/*     */         } else {
/* 252 */           result.add(value);
/*     */         }
/*     */       } catch (UnresolvedForwardReference reference) {
/* 255 */         if (referringAccumulator == null) {
/* 256 */           throw JsonMappingException.from(p, "Unresolved forward reference but no identity info", reference);
/*     */         }
/*     */         
/* 259 */         ReadableObjectId.Referring ref = referringAccumulator.handleUnresolvedReference(reference);
/* 260 */         reference.getRoid().appendReferring(ref);
/*     */       } catch (Exception e) {
/* 262 */         throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */       }
/*     */     }
/* 265 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 274 */     return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Collection<Object> handleNonArray(JsonParser p, DeserializationContext ctxt, Collection<Object> result)
/*     */     throws IOException
/*     */   {
/* 287 */     if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)) {
/* 288 */       throw ctxt.mappingException(this._collectionType.getRawClass());
/*     */     }
/* 290 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 291 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 292 */     JsonToken t = p.getCurrentToken();
/*     */     Object value;
/*     */     try
/*     */     {
/*     */       Object value;
/* 297 */       if (t == JsonToken.VALUE_NULL) {
/* 298 */         value = valueDes.getNullValue(); } else { Object value;
/* 299 */         if (typeDeser == null) {
/* 300 */           value = valueDes.deserialize(p, ctxt);
/*     */         } else {
/* 302 */           value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 306 */       throw JsonMappingException.wrapWithPath(e, Object.class, result.size());
/*     */     }
/* 308 */     result.add(value);
/* 309 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public static final class CollectionReferringAccumulator
/*     */   {
/*     */     private final Class<?> _elementType;
/*     */     
/*     */     private final Collection<Object> _result;
/*     */     
/* 319 */     private List<CollectionDeserializer.CollectionReferring> _accumulator = new ArrayList();
/*     */     
/*     */     public CollectionReferringAccumulator(Class<?> elementType, Collection<Object> result) {
/* 322 */       this._elementType = elementType;
/* 323 */       this._result = result;
/*     */     }
/*     */     
/*     */     public void add(Object value)
/*     */     {
/* 328 */       if (this._accumulator.isEmpty()) {
/* 329 */         this._result.add(value);
/*     */       } else {
/* 331 */         CollectionDeserializer.CollectionReferring ref = (CollectionDeserializer.CollectionReferring)this._accumulator.get(this._accumulator.size() - 1);
/* 332 */         ref.next.add(value);
/*     */       }
/*     */     }
/*     */     
/*     */     public ReadableObjectId.Referring handleUnresolvedReference(UnresolvedForwardReference reference)
/*     */     {
/* 338 */       CollectionDeserializer.CollectionReferring id = new CollectionDeserializer.CollectionReferring(this, reference, this._elementType);
/* 339 */       this._accumulator.add(id);
/* 340 */       return id;
/*     */     }
/*     */     
/*     */     public void resolveForwardReference(Object id, Object value) throws IOException
/*     */     {
/* 345 */       Iterator<CollectionDeserializer.CollectionReferring> iterator = this._accumulator.iterator();
/*     */       
/*     */ 
/*     */ 
/* 349 */       Collection<Object> previous = this._result;
/* 350 */       while (iterator.hasNext()) {
/* 351 */         CollectionDeserializer.CollectionReferring ref = (CollectionDeserializer.CollectionReferring)iterator.next();
/* 352 */         if (ref.hasId(id)) {
/* 353 */           iterator.remove();
/* 354 */           previous.add(value);
/* 355 */           previous.addAll(ref.next);
/* 356 */           return;
/*     */         }
/* 358 */         previous = ref.next;
/*     */       }
/*     */       
/* 361 */       throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id + "] that wasn't previously seen as unresolved.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class CollectionReferring
/*     */     extends ReadableObjectId.Referring
/*     */   {
/*     */     private final CollectionDeserializer.CollectionReferringAccumulator _parent;
/*     */     
/*     */ 
/* 373 */     public final List<Object> next = new ArrayList();
/*     */     
/*     */ 
/*     */     CollectionReferring(CollectionDeserializer.CollectionReferringAccumulator parent, UnresolvedForwardReference reference, Class<?> contentType)
/*     */     {
/* 378 */       super(contentType);
/* 379 */       this._parent = parent;
/*     */     }
/*     */     
/*     */     public void handleResolvedForwardReference(Object id, Object value) throws IOException
/*     */     {
/* 384 */       this._parent.resolveForwardReference(id, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\std\CollectionDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */