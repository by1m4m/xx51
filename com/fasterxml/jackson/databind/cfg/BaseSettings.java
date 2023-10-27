/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.StdDateFormat;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BaseSettings
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4939673998947122190L;
/*     */   protected final ClassIntrospector _classIntrospector;
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */   protected final VisibilityChecker<?> _visibilityChecker;
/*     */   protected final PropertyNamingStrategy _propertyNamingStrategy;
/*     */   protected final TypeFactory _typeFactory;
/*     */   protected final TypeResolverBuilder<?> _typeResolverBuilder;
/*     */   protected final DateFormat _dateFormat;
/*     */   protected final HandlerInstantiator _handlerInstantiator;
/*     */   protected final Locale _locale;
/*     */   protected final TimeZone _timeZone;
/*     */   protected final Base64Variant _defaultBase64;
/*     */   
/*     */   public BaseSettings(ClassIntrospector ci, AnnotationIntrospector ai, VisibilityChecker<?> vc, PropertyNamingStrategy pns, TypeFactory tf, TypeResolverBuilder<?> typer, DateFormat dateFormat, HandlerInstantiator hi, Locale locale, TimeZone tz, Base64Variant defaultBase64)
/*     */   {
/* 141 */     this._classIntrospector = ci;
/* 142 */     this._annotationIntrospector = ai;
/* 143 */     this._visibilityChecker = vc;
/* 144 */     this._propertyNamingStrategy = pns;
/* 145 */     this._typeFactory = tf;
/* 146 */     this._typeResolverBuilder = typer;
/* 147 */     this._dateFormat = dateFormat;
/* 148 */     this._handlerInstantiator = hi;
/* 149 */     this._locale = locale;
/* 150 */     this._timeZone = tz;
/* 151 */     this._defaultBase64 = defaultBase64;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseSettings withClassIntrospector(ClassIntrospector ci)
/*     */   {
/* 161 */     if (this._classIntrospector == ci) {
/* 162 */       return this;
/*     */     }
/* 164 */     return new BaseSettings(ci, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withAnnotationIntrospector(AnnotationIntrospector ai)
/*     */   {
/* 170 */     if (this._annotationIntrospector == ai) {
/* 171 */       return this;
/*     */     }
/* 173 */     return new BaseSettings(this._classIntrospector, ai, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withInsertedAnnotationIntrospector(AnnotationIntrospector ai)
/*     */   {
/* 179 */     return withAnnotationIntrospector(AnnotationIntrospectorPair.create(ai, this._annotationIntrospector));
/*     */   }
/*     */   
/*     */   public BaseSettings withAppendedAnnotationIntrospector(AnnotationIntrospector ai) {
/* 183 */     return withAnnotationIntrospector(AnnotationIntrospectorPair.create(this._annotationIntrospector, ai));
/*     */   }
/*     */   
/*     */   public BaseSettings withVisibilityChecker(VisibilityChecker<?> vc) {
/* 187 */     if (this._visibilityChecker == vc) {
/* 188 */       return this;
/*     */     }
/* 190 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, vc, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility)
/*     */   {
/* 196 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker.withVisibility(forMethod, visibility), this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseSettings withPropertyNamingStrategy(PropertyNamingStrategy pns)
/*     */   {
/* 204 */     if (this._propertyNamingStrategy == pns) {
/* 205 */       return this;
/*     */     }
/* 207 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, pns, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withTypeFactory(TypeFactory tf)
/*     */   {
/* 213 */     if (this._typeFactory == tf) {
/* 214 */       return this;
/*     */     }
/* 216 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, tf, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withTypeResolverBuilder(TypeResolverBuilder<?> typer)
/*     */   {
/* 222 */     if (this._typeResolverBuilder == typer) {
/* 223 */       return this;
/*     */     }
/* 225 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, typer, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withDateFormat(DateFormat df)
/*     */   {
/* 231 */     if (this._dateFormat == df) {
/* 232 */       return this;
/*     */     }
/* 234 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, df, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withHandlerInstantiator(HandlerInstantiator hi)
/*     */   {
/* 240 */     if (this._handlerInstantiator == hi) {
/* 241 */       return this;
/*     */     }
/* 243 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, hi, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings with(Locale l)
/*     */   {
/* 249 */     if (this._locale == l) {
/* 250 */       return this;
/*     */     }
/* 252 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, l, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseSettings with(TimeZone tz)
/*     */   {
/* 264 */     if (tz == null) {
/* 265 */       throw new IllegalArgumentException();
/*     */     }
/* 267 */     DateFormat df = this._dateFormat;
/* 268 */     if ((df instanceof StdDateFormat)) {
/* 269 */       df = ((StdDateFormat)df).withTimeZone(tz);
/*     */     }
/*     */     else {
/* 272 */       df = (DateFormat)df.clone();
/* 273 */       df.setTimeZone(tz);
/*     */     }
/* 275 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, df, this._handlerInstantiator, this._locale, tz, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseSettings with(Base64Variant base64)
/*     */   {
/* 285 */     if (base64 == this._defaultBase64) {
/* 286 */       return this;
/*     */     }
/* 288 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, base64);
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
/*     */   public ClassIntrospector getClassIntrospector()
/*     */   {
/* 301 */     return this._classIntrospector;
/*     */   }
/*     */   
/*     */   public AnnotationIntrospector getAnnotationIntrospector() {
/* 305 */     return this._annotationIntrospector;
/*     */   }
/*     */   
/*     */   public VisibilityChecker<?> getVisibilityChecker() {
/* 309 */     return this._visibilityChecker;
/*     */   }
/*     */   
/*     */   public PropertyNamingStrategy getPropertyNamingStrategy() {
/* 313 */     return this._propertyNamingStrategy;
/*     */   }
/*     */   
/*     */   public TypeFactory getTypeFactory() {
/* 317 */     return this._typeFactory;
/*     */   }
/*     */   
/*     */   public TypeResolverBuilder<?> getTypeResolverBuilder() {
/* 321 */     return this._typeResolverBuilder;
/*     */   }
/*     */   
/*     */   public DateFormat getDateFormat() {
/* 325 */     return this._dateFormat;
/*     */   }
/*     */   
/*     */   public HandlerInstantiator getHandlerInstantiator() {
/* 329 */     return this._handlerInstantiator;
/*     */   }
/*     */   
/*     */   public Locale getLocale() {
/* 333 */     return this._locale;
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 337 */     return this._timeZone;
/*     */   }
/*     */   
/*     */   public Base64Variant getBase64Variant() {
/* 341 */     return this._defaultBase64;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\cfg\BaseSettings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */