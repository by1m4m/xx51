/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.core.FormatSchema;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ public class MappingIterator<T> implements Iterator<T>, java.io.Closeable
/*     */ {
/*  16 */   protected static final MappingIterator<?> EMPTY_ITERATOR = new MappingIterator(null, null, null, null, false, null);
/*     */   
/*     */ 
/*     */ 
/*     */   protected final JavaType _type;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final DeserializationContext _context;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final JsonDeserializer<T> _deserializer;
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonParser _parser;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final boolean _closeParser;
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean _hasNextChecked;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final T _updatedValue;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected MappingIterator(JavaType type, JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser)
/*     */   {
/*  52 */     this(type, jp, ctxt, deser, true, null);
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
/*     */   protected MappingIterator(JavaType type, JsonParser jp, DeserializationContext ctxt, JsonDeserializer<?> deser, boolean managedParser, Object valueToUpdate)
/*     */   {
/*  66 */     this._type = type;
/*  67 */     this._parser = jp;
/*  68 */     this._context = ctxt;
/*  69 */     this._deserializer = deser;
/*  70 */     this._closeParser = managedParser;
/*  71 */     if (valueToUpdate == null) {
/*  72 */       this._updatedValue = null;
/*     */     } else {
/*  74 */       this._updatedValue = valueToUpdate;
/*     */     }
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
/*  87 */     if ((managedParser) && (jp != null) && (jp.getCurrentToken() == JsonToken.START_ARRAY)) {
/*  88 */       jp.clearCurrentToken();
/*     */     }
/*     */   }
/*     */   
/*     */   protected static <T> MappingIterator<T> emptyIterator()
/*     */   {
/*  94 */     return EMPTY_ITERATOR;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasNext()
/*     */   {
/*     */     try
/*     */     {
/* 107 */       return hasNextValue();
/*     */     } catch (JsonMappingException e) {
/* 109 */       return ((Boolean)_handleMappingException(e)).booleanValue();
/*     */     } catch (IOException e) {
/* 111 */       return ((Boolean)_handleIOException(e)).booleanValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public T next()
/*     */   {
/*     */     try
/*     */     {
/* 119 */       return (T)nextValue();
/*     */     } catch (JsonMappingException e) {
/* 121 */       throw new RuntimeJsonMappingException(e.getMessage(), e);
/*     */     } catch (IOException e) {
/* 123 */       throw new RuntimeException(e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void remove()
/*     */   {
/* 129 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 134 */     if (this._parser != null) {
/* 135 */       this._parser.close();
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
/*     */   public boolean hasNextValue()
/*     */     throws IOException
/*     */   {
/* 151 */     if (this._parser == null) {
/* 152 */       return false;
/*     */     }
/* 154 */     if (!this._hasNextChecked) {
/* 155 */       JsonToken t = this._parser.getCurrentToken();
/* 156 */       this._hasNextChecked = true;
/* 157 */       if (t == null) {
/* 158 */         t = this._parser.nextToken();
/*     */         
/* 160 */         if ((t == null) || (t == JsonToken.END_ARRAY)) {
/* 161 */           JsonParser jp = this._parser;
/* 162 */           this._parser = null;
/* 163 */           if (this._closeParser) {
/* 164 */             jp.close();
/*     */           }
/* 166 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 170 */     return true;
/*     */   }
/*     */   
/*     */   public T nextValue()
/*     */     throws IOException
/*     */   {
/* 176 */     if ((!this._hasNextChecked) && 
/* 177 */       (!hasNextValue())) {
/* 178 */       return (T)_throwNoSuchElement();
/*     */     }
/*     */     
/* 181 */     if (this._parser == null) {
/* 182 */       return (T)_throwNoSuchElement();
/*     */     }
/* 184 */     this._hasNextChecked = false;
/*     */     try {
/*     */       Object localObject1;
/* 187 */       if (this._updatedValue == null) {
/* 188 */         return (T)this._deserializer.deserialize(this._parser, this._context);
/*     */       }
/* 190 */       this._deserializer.deserialize(this._parser, this._context, this._updatedValue);
/* 191 */       return (T)this._updatedValue;
/*     */ 
/*     */ 
/*     */     }
/*     */     finally
/*     */     {
/*     */ 
/*     */ 
/* 199 */       this._parser.clearCurrentToken();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<T> readAll()
/*     */     throws IOException
/*     */   {
/* 212 */     return readAll(new ArrayList());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <L extends List<? super T>> L readAll(L resultList)
/*     */     throws IOException
/*     */   {
/* 225 */     while (hasNextValue()) {
/* 226 */       resultList.add(nextValue());
/*     */     }
/* 228 */     return resultList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <C extends Collection<? super T>> C readAll(C results)
/*     */     throws IOException
/*     */   {
/* 239 */     while (hasNextValue()) {
/* 240 */       results.add(nextValue());
/*     */     }
/* 242 */     return results;
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
/*     */   public JsonParser getParser()
/*     */   {
/* 257 */     return this._parser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FormatSchema getParserSchema()
/*     */   {
/* 268 */     return this._parser.getSchema();
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
/*     */   public JsonLocation getCurrentLocation()
/*     */   {
/* 282 */     return this._parser.getCurrentLocation();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected <R> R _throwNoSuchElement()
/*     */   {
/* 292 */     throw new NoSuchElementException();
/*     */   }
/*     */   
/*     */   protected <R> R _handleMappingException(JsonMappingException e) {
/* 296 */     throw new RuntimeJsonMappingException(e.getMessage(), e);
/*     */   }
/*     */   
/*     */   protected <R> R _handleIOException(IOException e) {
/* 300 */     throw new RuntimeException(e.getMessage(), e);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\MappingIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */