/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*     */ import java.io.IOException;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
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
/*     */ public class ReadableObjectId
/*     */ {
/*     */   @Deprecated
/*     */   public Object item;
/*     */   @Deprecated
/*     */   public final Object id;
/*     */   private final ObjectIdGenerator.IdKey _key;
/*     */   private LinkedList<Referring> _referringProperties;
/*     */   private ObjectIdResolver _resolver;
/*     */   
/*     */   @Deprecated
/*     */   public ReadableObjectId(Object id)
/*     */   {
/*  36 */     this.id = id;
/*  37 */     this._key = null;
/*     */   }
/*     */   
/*     */   public ReadableObjectId(ObjectIdGenerator.IdKey key) {
/*  41 */     this._key = key;
/*  42 */     this.id = key.key;
/*     */   }
/*     */   
/*     */   public void setResolver(ObjectIdResolver resolver) {
/*  46 */     this._resolver = resolver;
/*     */   }
/*     */   
/*     */   public ObjectIdGenerator.IdKey getKey() {
/*  50 */     return this._key;
/*     */   }
/*     */   
/*     */   public void appendReferring(Referring currentReferring) {
/*  54 */     if (this._referringProperties == null) {
/*  55 */       this._referringProperties = new LinkedList();
/*     */     }
/*  57 */     this._referringProperties.add(currentReferring);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void bindItem(Object ob)
/*     */     throws IOException
/*     */   {
/*  66 */     this._resolver.bindItem(this._key, ob);
/*  67 */     this.item = ob;
/*  68 */     if (this._referringProperties != null) {
/*  69 */       Iterator<Referring> it = this._referringProperties.iterator();
/*  70 */       this._referringProperties = null;
/*  71 */       while (it.hasNext()) {
/*  72 */         ((Referring)it.next()).handleResolvedForwardReference(this.id, ob);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object resolve() {
/*  78 */     return this.item = this._resolver.resolveId(this._key);
/*     */   }
/*     */   
/*     */   public boolean hasReferringProperties() {
/*  82 */     return (this._referringProperties != null) && (!this._referringProperties.isEmpty());
/*     */   }
/*     */   
/*     */   public Iterator<Referring> referringProperties() {
/*  86 */     if (this._referringProperties == null) {
/*  87 */       return Collections.emptyList().iterator();
/*     */     }
/*  89 */     return this._referringProperties.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static abstract class Referring
/*     */   {
/*     */     private final UnresolvedForwardReference _reference;
/*     */     
/*     */     private final Class<?> _beanType;
/*     */     
/*     */ 
/*     */     public Referring(UnresolvedForwardReference ref, Class<?> beanType)
/*     */     {
/* 103 */       this._reference = ref;
/* 104 */       this._beanType = beanType;
/*     */     }
/*     */     
/* 107 */     public JsonLocation getLocation() { return this._reference.getLocation(); }
/* 108 */     public Class<?> getBeanType() { return this._beanType; }
/*     */     
/*     */     public abstract void handleResolvedForwardReference(Object paramObject1, Object paramObject2) throws IOException;
/*     */     
/* 112 */     public boolean hasId(Object id) { return id.equals(this._reference.getUnresolvedId()); }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\ReadableObjectId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */