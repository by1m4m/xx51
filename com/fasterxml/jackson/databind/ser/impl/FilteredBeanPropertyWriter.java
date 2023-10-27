/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class FilteredBeanPropertyWriter
/*     */ {
/*     */   public static BeanPropertyWriter constructViewBased(BeanPropertyWriter base, Class<?>[] viewsToIncludeIn)
/*     */   {
/*  17 */     if (viewsToIncludeIn.length == 1) {
/*  18 */       return new SingleView(base, viewsToIncludeIn[0]);
/*     */     }
/*  20 */     return new MultiView(base, viewsToIncludeIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class SingleView
/*     */     extends BeanPropertyWriter
/*     */   {
/*     */     protected final BeanPropertyWriter _delegate;
/*     */     
/*     */ 
/*     */     protected final Class<?> _view;
/*     */     
/*     */ 
/*     */ 
/*     */     protected SingleView(BeanPropertyWriter delegate, Class<?> view)
/*     */     {
/*  38 */       super();
/*  39 */       this._delegate = delegate;
/*  40 */       this._view = view;
/*     */     }
/*     */     
/*     */     public SingleView rename(NameTransformer transformer)
/*     */     {
/*  45 */       return new SingleView(this._delegate.rename(transformer), this._view);
/*     */     }
/*     */     
/*     */     public void assignSerializer(JsonSerializer<Object> ser)
/*     */     {
/*  50 */       this._delegate.assignSerializer(ser);
/*     */     }
/*     */     
/*     */     public void assignNullSerializer(JsonSerializer<Object> nullSer)
/*     */     {
/*  55 */       this._delegate.assignNullSerializer(nullSer);
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */       throws Exception
/*     */     {
/*  62 */       Class<?> activeView = prov.getActiveView();
/*  63 */       if ((activeView == null) || (this._view.isAssignableFrom(activeView))) {
/*  64 */         this._delegate.serializeAsField(bean, jgen, prov);
/*     */       } else {
/*  66 */         this._delegate.serializeAsOmittedField(bean, jgen, prov);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeAsElement(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */       throws Exception
/*     */     {
/*  74 */       Class<?> activeView = prov.getActiveView();
/*  75 */       if ((activeView == null) || (this._view.isAssignableFrom(activeView))) {
/*  76 */         this._delegate.serializeAsElement(bean, jgen, prov);
/*     */       } else {
/*  78 */         this._delegate.serializeAsPlaceholder(bean, jgen, prov);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class MultiView
/*     */     extends BeanPropertyWriter
/*     */   {
/*     */     protected final BeanPropertyWriter _delegate;
/*     */     protected final Class<?>[] _views;
/*     */     
/*     */     protected MultiView(BeanPropertyWriter delegate, Class<?>[] views)
/*     */     {
/*  91 */       super();
/*  92 */       this._delegate = delegate;
/*  93 */       this._views = views;
/*     */     }
/*     */     
/*     */     public MultiView rename(NameTransformer transformer)
/*     */     {
/*  98 */       return new MultiView(this._delegate.rename(transformer), this._views);
/*     */     }
/*     */     
/*     */     public void assignSerializer(JsonSerializer<Object> ser)
/*     */     {
/* 103 */       this._delegate.assignSerializer(ser);
/*     */     }
/*     */     
/*     */     public void assignNullSerializer(JsonSerializer<Object> nullSer)
/*     */     {
/* 108 */       this._delegate.assignNullSerializer(nullSer);
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */       throws Exception
/*     */     {
/* 115 */       Class<?> activeView = prov.getActiveView();
/* 116 */       if (activeView != null) {
/* 117 */         int i = 0;int len = this._views.length;
/* 118 */         for (; i < len; i++) {
/* 119 */           if (this._views[i].isAssignableFrom(activeView))
/*     */             break;
/*     */         }
/* 122 */         if (i == len) {
/* 123 */           this._delegate.serializeAsOmittedField(bean, jgen, prov);
/* 124 */           return;
/*     */         }
/*     */       }
/* 127 */       this._delegate.serializeAsField(bean, jgen, prov);
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeAsElement(Object bean, JsonGenerator jgen, SerializerProvider prov)
/*     */       throws Exception
/*     */     {
/* 134 */       Class<?> activeView = prov.getActiveView();
/* 135 */       if (activeView != null) {
/* 136 */         int i = 0;int len = this._views.length;
/* 137 */         for (; i < len; i++) {
/* 138 */           if (this._views[i].isAssignableFrom(activeView))
/*     */             break;
/*     */         }
/* 141 */         if (i == len) {
/* 142 */           this._delegate.serializeAsPlaceholder(bean, jgen, prov);
/* 143 */           return;
/*     */         }
/*     */       }
/* 146 */       this._delegate.serializeAsElement(bean, jgen, prov);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\ser\impl\FilteredBeanPropertyWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */