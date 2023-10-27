/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.core.Versioned;
/*     */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;
/*     */ import com.fasterxml.jackson.databind.ser.impl.TypeWrappedSerializer;
/*     */ import java.io.Closeable;
/*     */ import java.io.Flushable;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
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
/*     */ public class SequenceWriter
/*     */   implements Versioned, Closeable, Flushable
/*     */ {
/*     */   protected final DefaultSerializerProvider _provider;
/*     */   protected final SerializationConfig _config;
/*     */   protected final JsonGenerator _generator;
/*     */   protected final JsonSerializer<Object> _rootSerializer;
/*     */   protected final TypeSerializer _typeSerializer;
/*     */   protected final boolean _closeGenerator;
/*     */   protected final boolean _cfgFlush;
/*     */   protected final boolean _cfgCloseCloseable;
/*     */   protected PropertySerializerMap _dynamicSerializers;
/*     */   protected boolean _openArray;
/*     */   protected boolean _closed;
/*     */   
/*     */   public SequenceWriter(DefaultSerializerProvider prov, JsonGenerator gen, boolean closeGenerator, ObjectWriter.Prefetch prefetch)
/*     */     throws IOException
/*     */   {
/*  82 */     this._provider = prov;
/*  83 */     this._generator = gen;
/*  84 */     this._closeGenerator = closeGenerator;
/*  85 */     this._rootSerializer = prefetch.valueSerializer;
/*  86 */     this._typeSerializer = prefetch.typeSerializer;
/*     */     
/*  88 */     this._config = prov.getConfig();
/*  89 */     this._cfgFlush = this._config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);
/*  90 */     this._cfgCloseCloseable = this._config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE);
/*     */     
/*     */ 
/*  93 */     this._dynamicSerializers = PropertySerializerMap.emptyForRootValues();
/*     */   }
/*     */   
/*     */   public SequenceWriter init(boolean wrapInArray) throws IOException
/*     */   {
/*  98 */     if (wrapInArray) {
/*  99 */       this._generator.writeStartArray();
/* 100 */       this._openArray = true;
/*     */     }
/* 102 */     return this;
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
/*     */   public Version version()
/*     */   {
/* 117 */     return PackageVersion.VERSION;
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
/*     */   public SequenceWriter write(Object value)
/*     */     throws IOException
/*     */   {
/* 133 */     if (value == null) {
/* 134 */       this._provider.serializeValue(this._generator, null);
/* 135 */       return this;
/*     */     }
/*     */     
/* 138 */     if ((this._cfgCloseCloseable) && ((value instanceof Closeable))) {
/* 139 */       return _writeCloseableValue(value);
/*     */     }
/* 141 */     JsonSerializer<Object> ser = this._rootSerializer;
/* 142 */     if (ser == null) {
/* 143 */       Class<?> type = value.getClass();
/* 144 */       ser = this._dynamicSerializers.serializerFor(type);
/* 145 */       if (ser == null) {
/* 146 */         ser = _findAndAddDynamic(type);
/*     */       }
/*     */     }
/* 149 */     this._provider.serializeValue(this._generator, value, null, ser);
/* 150 */     if (this._cfgFlush) {
/* 151 */       this._generator.flush();
/*     */     }
/* 153 */     return this;
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
/*     */   public SequenceWriter write(Object value, JavaType type)
/*     */     throws IOException
/*     */   {
/* 167 */     if (value == null) {
/* 168 */       this._provider.serializeValue(this._generator, null);
/* 169 */       return this;
/*     */     }
/*     */     
/* 172 */     if ((this._cfgCloseCloseable) && ((value instanceof Closeable))) {
/* 173 */       return _writeCloseableValue(value, type);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */     JsonSerializer<Object> ser = this._dynamicSerializers.serializerFor(type.getRawClass());
/* 181 */     if (ser == null) {
/* 182 */       ser = _findAndAddDynamic(type);
/*     */     }
/* 184 */     this._provider.serializeValue(this._generator, value, type, ser);
/* 185 */     if (this._cfgFlush) {
/* 186 */       this._generator.flush();
/*     */     }
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public SequenceWriter writeAll(Object[] value) throws IOException
/*     */   {
/* 193 */     int i = 0; for (int len = value.length; i < len; i++) {
/* 194 */       write(value[i]);
/*     */     }
/* 196 */     return this;
/*     */   }
/*     */   
/*     */   public <C extends Collection<?>> SequenceWriter writeAll(C container) throws IOException
/*     */   {
/* 201 */     for (Object value : container) {
/* 202 */       write(value);
/*     */     }
/* 204 */     return this;
/*     */   }
/*     */   
/*     */   public void flush() throws IOException
/*     */   {
/* 209 */     if (!this._closed) {
/* 210 */       this._generator.flush();
/*     */     }
/*     */   }
/*     */   
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 217 */     if (!this._closed) {
/* 218 */       this._closed = true;
/* 219 */       if (this._openArray) {
/* 220 */         this._openArray = false;
/* 221 */         this._generator.writeEndArray();
/*     */       }
/* 223 */       if (this._closeGenerator) {
/* 224 */         this._generator.close();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SequenceWriter _writeCloseableValue(Object value)
/*     */     throws IOException
/*     */   {
/* 237 */     Closeable toClose = (Closeable)value;
/*     */     try {
/* 239 */       JsonSerializer<Object> ser = this._rootSerializer;
/* 240 */       if (ser == null) {
/* 241 */         Class<?> type = value.getClass();
/* 242 */         ser = this._dynamicSerializers.serializerFor(type);
/* 243 */         if (ser == null) {
/* 244 */           ser = _findAndAddDynamic(type);
/*     */         }
/*     */       }
/* 247 */       this._provider.serializeValue(this._generator, value, null, ser);
/* 248 */       if (this._cfgFlush) {
/* 249 */         this._generator.flush();
/*     */       }
/* 251 */       Closeable tmpToClose = toClose;
/* 252 */       toClose = null;
/* 253 */       tmpToClose.close();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 261 */       return this;
/*     */     }
/*     */     finally
/*     */     {
/* 255 */       if (toClose != null) {
/*     */         try {
/* 257 */           toClose.close();
/*     */         }
/*     */         catch (IOException ioe) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected SequenceWriter _writeCloseableValue(Object value, JavaType type) throws IOException
/*     */   {
/* 266 */     Closeable toClose = (Closeable)value;
/*     */     try
/*     */     {
/* 269 */       JsonSerializer<Object> ser = this._dynamicSerializers.serializerFor(type.getRawClass());
/* 270 */       if (ser == null) {
/* 271 */         ser = _findAndAddDynamic(type);
/*     */       }
/* 273 */       this._provider.serializeValue(this._generator, value, type, ser);
/* 274 */       if (this._cfgFlush) {
/* 275 */         this._generator.flush();
/*     */       }
/* 277 */       Closeable tmpToClose = toClose;
/* 278 */       toClose = null;
/* 279 */       tmpToClose.close();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 287 */       return this;
/*     */     }
/*     */     finally
/*     */     {
/* 281 */       if (toClose != null) {
/*     */         try {
/* 283 */           toClose.close();
/*     */         }
/*     */         catch (IOException ioe) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private final JsonSerializer<Object> _findAndAddDynamic(Class<?> type) throws JsonMappingException {
/*     */     PropertySerializerMap.SerializerAndMapResult result;
/*     */     PropertySerializerMap.SerializerAndMapResult result;
/* 293 */     if (this._typeSerializer == null) {
/* 294 */       result = this._dynamicSerializers.findAndAddRootValueSerializer(type, this._provider);
/*     */     } else {
/* 296 */       result = this._dynamicSerializers.addSerializer(type, new TypeWrappedSerializer(this._typeSerializer, this._provider.findValueSerializer(type, null)));
/*     */     }
/*     */     
/* 299 */     this._dynamicSerializers = result.map;
/* 300 */     return result.serializer;
/*     */   }
/*     */   
/*     */   private final JsonSerializer<Object> _findAndAddDynamic(JavaType type) throws JsonMappingException {
/*     */     PropertySerializerMap.SerializerAndMapResult result;
/*     */     PropertySerializerMap.SerializerAndMapResult result;
/* 306 */     if (this._typeSerializer == null) {
/* 307 */       result = this._dynamicSerializers.findAndAddRootValueSerializer(type, this._provider);
/*     */     } else {
/* 309 */       result = this._dynamicSerializers.addSerializer(type, new TypeWrappedSerializer(this._typeSerializer, this._provider.findValueSerializer(type, null)));
/*     */     }
/*     */     
/* 312 */     this._dynamicSerializers = result.map;
/* 313 */     return result.serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\SequenceWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */