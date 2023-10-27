/*     */ package com.fasterxml.jackson.databind.exc;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
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
/*     */ public abstract class PropertyBindingException
/*     */   extends JsonMappingException
/*     */ {
/*     */   protected final Class<?> _referringClass;
/*     */   protected final String _propertyName;
/*     */   protected final Collection<Object> _propertyIds;
/*     */   protected transient String _propertiesAsString;
/*     */   private static final int MAX_DESC_LENGTH = 1000;
/*     */   
/*     */   protected PropertyBindingException(String msg, JsonLocation loc, Class<?> referringClass, String propName, Collection<Object> propertyIds)
/*     */   {
/*  49 */     super(msg, loc);
/*  50 */     this._referringClass = referringClass;
/*  51 */     this._propertyName = propName;
/*  52 */     this._propertyIds = propertyIds;
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
/*     */   public String getMessageSuffix()
/*     */   {
/*  70 */     String suffix = this._propertiesAsString;
/*  71 */     if ((suffix == null) && (this._propertyIds != null)) {
/*  72 */       StringBuilder sb = new StringBuilder(100);
/*  73 */       int len = this._propertyIds.size();
/*  74 */       if (len == 1) {
/*  75 */         sb.append(" (one known property: \"");
/*  76 */         sb.append(String.valueOf(this._propertyIds.iterator().next()));
/*  77 */         sb.append('"');
/*     */       } else {
/*  79 */         sb.append(" (").append(len).append(" known properties: ");
/*  80 */         Iterator<Object> it = this._propertyIds.iterator();
/*  81 */         while (it.hasNext()) {
/*  82 */           sb.append('"');
/*  83 */           sb.append(String.valueOf(it.next()));
/*  84 */           sb.append('"');
/*     */           
/*  86 */           if (sb.length() > 1000) {
/*  87 */             sb.append(" [truncated]");
/*  88 */             break;
/*     */           }
/*  90 */           if (it.hasNext()) {
/*  91 */             sb.append(", ");
/*     */           }
/*     */         }
/*     */       }
/*  95 */       sb.append("])");
/*  96 */       this._propertiesAsString = (suffix = sb.toString());
/*     */     }
/*  98 */     return suffix;
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
/*     */   public Class<?> getReferringClass()
/*     */   {
/* 112 */     return this._referringClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPropertyName()
/*     */   {
/* 121 */     return this._propertyName;
/*     */   }
/*     */   
/*     */   public Collection<Object> getKnownPropertyIds()
/*     */   {
/* 126 */     if (this._propertyIds == null) {
/* 127 */       return null;
/*     */     }
/* 129 */     return Collections.unmodifiableCollection(this._propertyIds);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\exc\PropertyBindingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */