/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExternalTypeHandler
/*     */ {
/*     */   private final ExtTypedProperty[] _properties;
/*     */   private final HashMap<String, Integer> _nameToPropertyIndex;
/*     */   private final String[] _typeIds;
/*     */   private final TokenBuffer[] _tokens;
/*     */   
/*     */   protected ExternalTypeHandler(ExtTypedProperty[] properties, HashMap<String, Integer> nameToPropertyIndex, String[] typeIds, TokenBuffer[] tokens)
/*     */   {
/*  31 */     this._properties = properties;
/*  32 */     this._nameToPropertyIndex = nameToPropertyIndex;
/*  33 */     this._typeIds = typeIds;
/*  34 */     this._tokens = tokens;
/*     */   }
/*     */   
/*     */   protected ExternalTypeHandler(ExternalTypeHandler h)
/*     */   {
/*  39 */     this._properties = h._properties;
/*  40 */     this._nameToPropertyIndex = h._nameToPropertyIndex;
/*  41 */     int len = this._properties.length;
/*  42 */     this._typeIds = new String[len];
/*  43 */     this._tokens = new TokenBuffer[len];
/*     */   }
/*     */   
/*     */   public ExternalTypeHandler start() {
/*  47 */     return new ExternalTypeHandler(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean handleTypePropertyValue(JsonParser jp, DeserializationContext ctxt, String propName, Object bean)
/*     */     throws IOException
/*     */   {
/*  60 */     Integer I = (Integer)this._nameToPropertyIndex.get(propName);
/*  61 */     if (I == null) {
/*  62 */       return false;
/*     */     }
/*  64 */     int index = I.intValue();
/*  65 */     ExtTypedProperty prop = this._properties[index];
/*  66 */     if (!prop.hasTypePropertyName(propName)) {
/*  67 */       return false;
/*     */     }
/*  69 */     String typeId = jp.getText();
/*     */     
/*  71 */     boolean canDeserialize = (bean != null) && (this._tokens[index] != null);
/*     */     
/*  73 */     if (canDeserialize) {
/*  74 */       _deserializeAndSet(jp, ctxt, bean, index, typeId);
/*     */       
/*  76 */       this._tokens[index] = null;
/*     */     } else {
/*  78 */       this._typeIds[index] = typeId;
/*     */     }
/*  80 */     return true;
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
/*     */   public boolean handlePropertyValue(JsonParser jp, DeserializationContext ctxt, String propName, Object bean)
/*     */     throws IOException
/*     */   {
/*  94 */     Integer I = (Integer)this._nameToPropertyIndex.get(propName);
/*  95 */     if (I == null) {
/*  96 */       return false;
/*     */     }
/*  98 */     int index = I.intValue();
/*  99 */     ExtTypedProperty prop = this._properties[index];
/*     */     boolean canDeserialize;
/* 101 */     boolean canDeserialize; if (prop.hasTypePropertyName(propName)) {
/* 102 */       this._typeIds[index] = jp.getText();
/* 103 */       jp.skipChildren();
/* 104 */       canDeserialize = (bean != null) && (this._tokens[index] != null);
/*     */     }
/*     */     else {
/* 107 */       TokenBuffer tokens = new TokenBuffer(jp);
/* 108 */       tokens.copyCurrentStructure(jp);
/* 109 */       this._tokens[index] = tokens;
/* 110 */       canDeserialize = (bean != null) && (this._typeIds[index] != null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 115 */     if (canDeserialize) {
/* 116 */       String typeId = this._typeIds[index];
/*     */       
/* 118 */       this._typeIds[index] = null;
/* 119 */       _deserializeAndSet(jp, ctxt, bean, index, typeId);
/* 120 */       this._tokens[index] = null;
/*     */     }
/* 122 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object complete(JsonParser jp, DeserializationContext ctxt, Object bean)
/*     */     throws IOException
/*     */   {
/* 129 */     int i = 0; for (int len = this._properties.length; i < len; i++) {
/* 130 */       String typeId = this._typeIds[i];
/* 131 */       if (typeId == null) {
/* 132 */         TokenBuffer tokens = this._tokens[i];
/*     */         
/*     */ 
/* 135 */         if (tokens == null) {
/*     */           continue;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 141 */         JsonToken t = tokens.firstToken();
/* 142 */         if ((t != null) && (t.isScalarValue())) {
/* 143 */           JsonParser buffered = tokens.asParser(jp);
/* 144 */           buffered.nextToken();
/* 145 */           SettableBeanProperty extProp = this._properties[i].getProperty();
/* 146 */           Object result = TypeDeserializer.deserializeIfNatural(buffered, ctxt, extProp.getType());
/* 147 */           if (result != null) {
/* 148 */             extProp.set(bean, result);
/* 149 */             continue;
/*     */           }
/*     */           
/* 152 */           if (!this._properties[i].hasDefaultType()) {
/* 153 */             throw ctxt.mappingException("Missing external type id property '" + this._properties[i].getTypePropertyName() + "'");
/*     */           }
/* 155 */           typeId = this._properties[i].getDefaultTypeId();
/*     */         }
/* 157 */       } else if (this._tokens[i] == null) {
/* 158 */         SettableBeanProperty prop = this._properties[i].getProperty();
/* 159 */         throw ctxt.mappingException("Missing property '" + prop.getName() + "' for external type id '" + this._properties[i].getTypePropertyName());
/*     */       }
/* 161 */       _deserializeAndSet(jp, ctxt, bean, i, typeId);
/*     */     }
/* 163 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object complete(JsonParser jp, DeserializationContext ctxt, PropertyValueBuffer buffer, PropertyBasedCreator creator)
/*     */     throws IOException
/*     */   {
/* 175 */     int len = this._properties.length;
/* 176 */     Object[] values = new Object[len];
/* 177 */     for (int i = 0; i < len; i++) {
/* 178 */       String typeId = this._typeIds[i];
/* 179 */       if (typeId == null)
/*     */       {
/* 181 */         if (this._tokens[i] == null) {
/*     */           continue;
/*     */         }
/*     */         
/*     */ 
/* 186 */         if (!this._properties[i].hasDefaultType()) {
/* 187 */           throw ctxt.mappingException("Missing external type id property '" + this._properties[i].getTypePropertyName() + "'");
/*     */         }
/* 189 */         typeId = this._properties[i].getDefaultTypeId();
/* 190 */       } else if (this._tokens[i] == null) {
/* 191 */         SettableBeanProperty prop = this._properties[i].getProperty();
/* 192 */         throw ctxt.mappingException("Missing property '" + prop.getName() + "' for external type id '" + this._properties[i].getTypePropertyName());
/*     */       }
/* 194 */       values[i] = _deserialize(jp, ctxt, i, typeId);
/*     */     }
/*     */     
/* 197 */     for (int i = 0; i < len; i++) {
/* 198 */       SettableBeanProperty prop = this._properties[i].getProperty();
/* 199 */       if (creator.findCreatorProperty(prop.getName()) != null) {
/* 200 */         buffer.assignParameter(prop.getCreatorIndex(), values[i]);
/*     */       }
/*     */     }
/* 203 */     Object bean = creator.build(ctxt, buffer);
/*     */     
/* 205 */     for (int i = 0; i < len; i++) {
/* 206 */       SettableBeanProperty prop = this._properties[i].getProperty();
/* 207 */       if (creator.findCreatorProperty(prop.getName()) == null) {
/* 208 */         prop.set(bean, values[i]);
/*     */       }
/*     */     }
/* 211 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final Object _deserialize(JsonParser jp, DeserializationContext ctxt, int index, String typeId)
/*     */     throws IOException
/*     */   {
/* 218 */     TokenBuffer merged = new TokenBuffer(jp);
/* 219 */     merged.writeStartArray();
/* 220 */     merged.writeString(typeId);
/* 221 */     JsonParser p2 = this._tokens[index].asParser(jp);
/* 222 */     p2.nextToken();
/* 223 */     merged.copyCurrentStructure(p2);
/* 224 */     merged.writeEndArray();
/*     */     
/*     */ 
/* 227 */     p2 = merged.asParser(jp);
/* 228 */     p2.nextToken();
/* 229 */     return this._properties[index].getProperty().deserialize(p2, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void _deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object bean, int index, String typeId)
/*     */     throws IOException
/*     */   {
/* 239 */     TokenBuffer merged = new TokenBuffer(jp);
/* 240 */     merged.writeStartArray();
/* 241 */     merged.writeString(typeId);
/* 242 */     JsonParser p2 = this._tokens[index].asParser(jp);
/* 243 */     p2.nextToken();
/* 244 */     merged.copyCurrentStructure(p2);
/* 245 */     merged.writeEndArray();
/*     */     
/* 247 */     p2 = merged.asParser(jp);
/* 248 */     p2.nextToken();
/* 249 */     this._properties[index].getProperty().deserializeAndSet(p2, ctxt, bean);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Builder
/*     */   {
/* 260 */     private final ArrayList<ExternalTypeHandler.ExtTypedProperty> _properties = new ArrayList();
/* 261 */     private final HashMap<String, Integer> _nameToPropertyIndex = new HashMap();
/*     */     
/*     */     public void addExternal(SettableBeanProperty property, TypeDeserializer typeDeser)
/*     */     {
/* 265 */       Integer index = Integer.valueOf(this._properties.size());
/* 266 */       this._properties.add(new ExternalTypeHandler.ExtTypedProperty(property, typeDeser));
/* 267 */       this._nameToPropertyIndex.put(property.getName(), index);
/* 268 */       this._nameToPropertyIndex.put(typeDeser.getPropertyName(), index);
/*     */     }
/*     */     
/*     */     public ExternalTypeHandler build() {
/* 272 */       return new ExternalTypeHandler((ExternalTypeHandler.ExtTypedProperty[])this._properties.toArray(new ExternalTypeHandler.ExtTypedProperty[this._properties.size()]), this._nameToPropertyIndex, null, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class ExtTypedProperty
/*     */   {
/*     */     private final SettableBeanProperty _property;
/*     */     private final TypeDeserializer _typeDeserializer;
/*     */     private final String _typePropertyName;
/*     */     
/*     */     public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeser)
/*     */     {
/* 285 */       this._property = property;
/* 286 */       this._typeDeserializer = typeDeser;
/* 287 */       this._typePropertyName = typeDeser.getPropertyName();
/*     */     }
/*     */     
/*     */     public boolean hasTypePropertyName(String n) {
/* 291 */       return n.equals(this._typePropertyName);
/*     */     }
/*     */     
/*     */     public boolean hasDefaultType() {
/* 295 */       return this._typeDeserializer.getDefaultImpl() != null;
/*     */     }
/*     */     
/*     */     public String getDefaultTypeId() {
/* 299 */       Class<?> defaultType = this._typeDeserializer.getDefaultImpl();
/* 300 */       if (defaultType == null) {
/* 301 */         return null;
/*     */       }
/* 303 */       return this._typeDeserializer.getTypeIdResolver().idFromValueAndType(null, defaultType);
/*     */     }
/*     */     
/* 306 */     public String getTypePropertyName() { return this._typePropertyName; }
/*     */     
/*     */     public SettableBeanProperty getProperty() {
/* 309 */       return this._property;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\ExternalTypeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */