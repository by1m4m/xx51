/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.deser.CreatorProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.std.StdValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*     */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class CreatorCollector
/*     */ {
/*     */   protected static final int C_DEFAULT = 0;
/*     */   protected static final int C_STRING = 1;
/*     */   protected static final int C_INT = 2;
/*     */   protected static final int C_LONG = 3;
/*     */   protected static final int C_DOUBLE = 4;
/*     */   protected static final int C_BOOLEAN = 5;
/*     */   protected static final int C_DELEGATE = 6;
/*     */   protected static final int C_PROPS = 7;
/*  34 */   protected static final String[] TYPE_DESCS = { "default", "String", "int", "long", "double", "boolean", "delegate", "property-based" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final BeanDescription _beanDesc;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _canFixAccess;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   protected final AnnotatedWithParams[] _creators = new AnnotatedWithParams[8];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */   protected int _explicitCreators = 0;
/*     */   
/*  60 */   protected boolean _hasNonDefaultCreator = false;
/*     */   
/*     */ 
/*     */ 
/*     */   protected CreatorProperty[] _delegateArgs;
/*     */   
/*     */ 
/*     */ 
/*     */   protected CreatorProperty[] _propertyBasedArgs;
/*     */   
/*     */ 
/*     */   protected AnnotatedParameter _incompleteParameter;
/*     */   
/*     */ 
/*     */ 
/*     */   public CreatorCollector(BeanDescription beanDesc, boolean canFixAccess)
/*     */   {
/*  77 */     this._beanDesc = beanDesc;
/*  78 */     this._canFixAccess = canFixAccess;
/*     */   }
/*     */   
/*     */ 
/*     */   public ValueInstantiator constructValueInstantiator(DeserializationConfig config)
/*     */   {
/*  84 */     boolean maybeVanilla = !this._hasNonDefaultCreator;
/*     */     JavaType delegateType;
/*  86 */     JavaType delegateType; if ((maybeVanilla) || (this._creators[6] == null)) {
/*  87 */       delegateType = null;
/*     */     }
/*     */     else {
/*  90 */       int ix = 0;
/*  91 */       if (this._delegateArgs != null) {
/*  92 */         int i = 0; for (int len = this._delegateArgs.length; i < len; i++) {
/*  93 */           if (this._delegateArgs[i] == null) {
/*  94 */             ix = i;
/*  95 */             break;
/*     */           }
/*     */         }
/*     */       }
/*  99 */       TypeBindings bindings = this._beanDesc.bindingsForBeanType();
/* 100 */       delegateType = bindings.resolveType(this._creators[6].getGenericParameterType(ix));
/*     */     }
/*     */     
/* 103 */     JavaType type = this._beanDesc.getType();
/*     */     
/*     */ 
/*     */ 
/* 107 */     maybeVanilla &= !this._hasNonDefaultCreator;
/*     */     
/* 109 */     if (maybeVanilla)
/*     */     {
/*     */ 
/*     */ 
/* 113 */       Class<?> rawType = type.getRawClass();
/* 114 */       if ((rawType == Collection.class) || (rawType == java.util.List.class) || (rawType == ArrayList.class)) {
/* 115 */         return new Vanilla(1);
/*     */       }
/* 117 */       if ((rawType == Map.class) || (rawType == LinkedHashMap.class)) {
/* 118 */         return new Vanilla(2);
/*     */       }
/* 120 */       if (rawType == HashMap.class) {
/* 121 */         return new Vanilla(3);
/*     */       }
/*     */     }
/*     */     
/* 125 */     StdValueInstantiator inst = new StdValueInstantiator(config, type);
/* 126 */     inst.configureFromObjectSettings(this._creators[0], this._creators[6], delegateType, this._delegateArgs, this._creators[7], this._propertyBasedArgs);
/*     */     
/*     */ 
/* 129 */     inst.configureFromStringCreator(this._creators[1]);
/* 130 */     inst.configureFromIntCreator(this._creators[2]);
/* 131 */     inst.configureFromLongCreator(this._creators[3]);
/* 132 */     inst.configureFromDoubleCreator(this._creators[4]);
/* 133 */     inst.configureFromBooleanCreator(this._creators[5]);
/* 134 */     inst.configureIncompleteParameter(this._incompleteParameter);
/* 135 */     return inst;
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
/*     */   public void setDefaultCreator(AnnotatedWithParams creator)
/*     */   {
/* 154 */     this._creators[0] = ((AnnotatedWithParams)_fixAccess(creator));
/*     */   }
/*     */   
/*     */   public void addStringCreator(AnnotatedWithParams creator, boolean explicit) {
/* 158 */     verifyNonDup(creator, 1, explicit);
/*     */   }
/*     */   
/* 161 */   public void addIntCreator(AnnotatedWithParams creator, boolean explicit) { verifyNonDup(creator, 2, explicit); }
/*     */   
/*     */   public void addLongCreator(AnnotatedWithParams creator, boolean explicit) {
/* 164 */     verifyNonDup(creator, 3, explicit);
/*     */   }
/*     */   
/* 167 */   public void addDoubleCreator(AnnotatedWithParams creator, boolean explicit) { verifyNonDup(creator, 4, explicit); }
/*     */   
/*     */   public void addBooleanCreator(AnnotatedWithParams creator, boolean explicit) {
/* 170 */     verifyNonDup(creator, 5, explicit);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addDelegatingCreator(AnnotatedWithParams creator, boolean explicit, CreatorProperty[] injectables)
/*     */   {
/* 176 */     verifyNonDup(creator, 6, explicit);
/* 177 */     this._delegateArgs = injectables;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addPropertyCreator(AnnotatedWithParams creator, boolean explicit, CreatorProperty[] properties)
/*     */   {
/* 183 */     verifyNonDup(creator, 7, explicit);
/*     */     
/* 185 */     if (properties.length > 1) {
/* 186 */       HashMap<String, Integer> names = new HashMap();
/* 187 */       int i = 0; for (int len = properties.length; i < len; i++) {
/* 188 */         String name = properties[i].getName();
/*     */         
/*     */ 
/*     */ 
/* 192 */         if ((name.length() != 0) || (properties[i].getInjectableValueId() == null))
/*     */         {
/*     */ 
/* 195 */           Integer old = (Integer)names.put(name, Integer.valueOf(i));
/* 196 */           if (old != null)
/* 197 */             throw new IllegalArgumentException("Duplicate creator property \"" + name + "\" (index " + old + " vs " + i + ")");
/*     */         }
/*     */       }
/*     */     }
/* 201 */     this._propertyBasedArgs = properties;
/*     */   }
/*     */   
/*     */   public void addIncompeteParameter(AnnotatedParameter parameter) {
/* 205 */     if (this._incompleteParameter == null) {
/* 206 */       this._incompleteParameter = parameter;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public void addStringCreator(AnnotatedWithParams creator)
/*     */   {
/* 214 */     addStringCreator(creator, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/* 218 */   public void addIntCreator(AnnotatedWithParams creator) { addBooleanCreator(creator, false); }
/*     */   
/*     */   @Deprecated
/*     */   public void addLongCreator(AnnotatedWithParams creator) {
/* 222 */     addBooleanCreator(creator, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/* 226 */   public void addDoubleCreator(AnnotatedWithParams creator) { addBooleanCreator(creator, false); }
/*     */   
/*     */   @Deprecated
/*     */   public void addBooleanCreator(AnnotatedWithParams creator) {
/* 230 */     addBooleanCreator(creator, false);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void addDelegatingCreator(AnnotatedWithParams creator, CreatorProperty[] injectables) {
/* 235 */     addDelegatingCreator(creator, false, injectables);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void addPropertyCreator(AnnotatedWithParams creator, CreatorProperty[] properties) {
/* 240 */     addPropertyCreator(creator, false, properties);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected AnnotatedWithParams verifyNonDup(AnnotatedWithParams newOne, int typeIndex) {
/* 245 */     verifyNonDup(newOne, typeIndex, false);
/* 246 */     return this._creators[typeIndex];
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
/*     */   public boolean hasDefaultCreator()
/*     */   {
/* 259 */     return this._creators[0] != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T extends AnnotatedMember> T _fixAccess(T member)
/*     */   {
/* 270 */     if ((member != null) && (this._canFixAccess)) {
/* 271 */       ClassUtil.checkAndFixAccess((Member)member.getAnnotated());
/*     */     }
/* 273 */     return member;
/*     */   }
/*     */   
/*     */   protected void verifyNonDup(AnnotatedWithParams newOne, int typeIndex, boolean explicit)
/*     */   {
/* 278 */     int mask = 1 << typeIndex;
/* 279 */     this._hasNonDefaultCreator = true;
/* 280 */     AnnotatedWithParams oldOne = this._creators[typeIndex];
/*     */     
/* 282 */     if (oldOne != null) {
/*     */       boolean verify;
/*     */       boolean verify;
/* 285 */       if ((this._explicitCreators & mask) != 0)
/*     */       {
/* 287 */         if (!explicit) {
/* 288 */           return;
/*     */         }
/*     */         
/* 291 */         verify = true;
/*     */       }
/*     */       else {
/* 294 */         verify = !explicit;
/*     */       }
/*     */       
/*     */ 
/* 298 */       if ((verify) && (oldOne.getClass() == newOne.getClass()))
/*     */       {
/* 300 */         Class<?> oldType = oldOne.getRawParameterType(0);
/* 301 */         Class<?> newType = newOne.getRawParameterType(0);
/*     */         
/* 303 */         if (oldType == newType) {
/* 304 */           throw new IllegalArgumentException("Conflicting " + TYPE_DESCS[typeIndex] + " creators: already had explicitly marked " + oldOne + ", encountered " + newOne);
/*     */         }
/*     */         
/*     */ 
/* 308 */         if (newType.isAssignableFrom(oldType))
/*     */         {
/* 310 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 315 */     if (explicit) {
/* 316 */       this._explicitCreators |= mask;
/*     */     }
/* 318 */     this._creators[typeIndex] = ((AnnotatedWithParams)_fixAccess(newOne));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected static final class Vanilla
/*     */     extends ValueInstantiator
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public static final int TYPE_COLLECTION = 1;
/*     */     
/*     */     public static final int TYPE_MAP = 2;
/*     */     
/*     */     public static final int TYPE_HASH_MAP = 3;
/*     */     
/*     */     private final int _type;
/*     */     
/*     */ 
/*     */     public Vanilla(int t)
/*     */     {
/* 340 */       this._type = t;
/*     */     }
/*     */     
/*     */ 
/*     */     public String getValueTypeDesc()
/*     */     {
/* 346 */       switch (this._type) {
/* 347 */       case 1:  return ArrayList.class.getName();
/* 348 */       case 2:  return LinkedHashMap.class.getName();
/* 349 */       case 3:  return HashMap.class.getName();
/*     */       }
/* 351 */       return Object.class.getName();
/*     */     }
/*     */     
/*     */     public boolean canInstantiate() {
/* 355 */       return true;
/*     */     }
/*     */     
/* 358 */     public boolean canCreateUsingDefault() { return true; }
/*     */     
/*     */     public Object createUsingDefault(DeserializationContext ctxt) throws IOException
/*     */     {
/* 362 */       switch (this._type) {
/* 363 */       case 1:  return new ArrayList();
/* 364 */       case 2:  return new LinkedHashMap();
/* 365 */       case 3:  return new HashMap();
/*     */       }
/* 367 */       throw new IllegalStateException("Unknown type " + this._type);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\deser\impl\CreatorCollector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */