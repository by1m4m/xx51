/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMap.Builder;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ @Beta
/*     */ public final class TypeResolver
/*     */ {
/*     */   private final TypeTable typeTable;
/*     */   
/*     */   public TypeResolver()
/*     */   {
/*  60 */     this.typeTable = new TypeTable();
/*     */   }
/*     */   
/*     */   private TypeResolver(TypeTable typeTable) {
/*  64 */     this.typeTable = typeTable;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static TypeResolver covariantly(Type contextType)
/*     */   {
/*  75 */     return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(contextType));
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
/*     */   static TypeResolver invariantly(Type contextType)
/*     */   {
/*  91 */     Type invariantContext = WildcardCapturer.INSTANCE.capture(contextType);
/*  92 */     return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(invariantContext));
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
/*     */ 
/*     */ 
/*     */   public TypeResolver where(Type formal, Type actual)
/*     */   {
/* 115 */     Map<TypeVariableKey, Type> mappings = Maps.newHashMap();
/* 116 */     populateTypeMappings(mappings, (Type)Preconditions.checkNotNull(formal), (Type)Preconditions.checkNotNull(actual));
/* 117 */     return where(mappings);
/*     */   }
/*     */   
/*     */   TypeResolver where(Map<TypeVariableKey, ? extends Type> mappings)
/*     */   {
/* 122 */     return new TypeResolver(this.typeTable.where(mappings));
/*     */   }
/*     */   
/*     */   private static void populateTypeMappings(Map<TypeVariableKey, Type> mappings, Type from, final Type to)
/*     */   {
/* 127 */     if (from.equals(to)) {
/* 128 */       return;
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
/* 208 */     new TypeVisitor()
/*     */     {
/*     */       void visitTypeVariable(TypeVariable<?> typeVariable)
/*     */       {
/* 133 */         this.val$mappings.put(new TypeResolver.TypeVariableKey(typeVariable), to);
/*     */       }
/*     */       
/*     */       void visitWildcardType(WildcardType fromWildcardType)
/*     */       {
/* 138 */         if (!(to instanceof WildcardType)) {
/* 139 */           return;
/*     */         }
/* 141 */         WildcardType toWildcardType = (WildcardType)to;
/* 142 */         Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
/* 143 */         Type[] toUpperBounds = toWildcardType.getUpperBounds();
/* 144 */         Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
/* 145 */         Type[] toLowerBounds = toWildcardType.getLowerBounds();
/* 146 */         Preconditions.checkArgument((fromUpperBounds.length == toUpperBounds.length) && (fromLowerBounds.length == toLowerBounds.length), "Incompatible type: %s vs. %s", fromWildcardType, to);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 152 */         for (int i = 0; i < fromUpperBounds.length; i++) {
/* 153 */           TypeResolver.populateTypeMappings(this.val$mappings, fromUpperBounds[i], toUpperBounds[i]);
/*     */         }
/* 155 */         for (int i = 0; i < fromLowerBounds.length; i++) {
/* 156 */           TypeResolver.populateTypeMappings(this.val$mappings, fromLowerBounds[i], toLowerBounds[i]);
/*     */         }
/*     */       }
/*     */       
/*     */       void visitParameterizedType(ParameterizedType fromParameterizedType)
/*     */       {
/* 162 */         if ((to instanceof WildcardType)) {
/* 163 */           return;
/*     */         }
/* 165 */         ParameterizedType toParameterizedType = (ParameterizedType)TypeResolver.expectArgument(ParameterizedType.class, to);
/* 166 */         if ((fromParameterizedType.getOwnerType() != null) && 
/* 167 */           (toParameterizedType.getOwnerType() != null)) {
/* 168 */           TypeResolver.populateTypeMappings(this.val$mappings, fromParameterizedType
/* 169 */             .getOwnerType(), toParameterizedType.getOwnerType());
/*     */         }
/* 171 */         Preconditions.checkArgument(
/* 172 */           fromParameterizedType.getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", fromParameterizedType, to);
/*     */         
/*     */ 
/*     */ 
/* 176 */         Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
/* 177 */         Type[] toArgs = toParameterizedType.getActualTypeArguments();
/* 178 */         Preconditions.checkArgument(fromArgs.length == toArgs.length, "%s not compatible with %s", fromParameterizedType, toParameterizedType);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 183 */         for (int i = 0; i < fromArgs.length; i++) {
/* 184 */           TypeResolver.populateTypeMappings(this.val$mappings, fromArgs[i], toArgs[i]);
/*     */         }
/*     */       }
/*     */       
/*     */       void visitGenericArrayType(GenericArrayType fromArrayType)
/*     */       {
/* 190 */         if ((to instanceof WildcardType)) {
/* 191 */           return;
/*     */         }
/* 193 */         Type componentType = Types.getComponentType(to);
/* 194 */         Preconditions.checkArgument(componentType != null, "%s is not an array type.", to);
/* 195 */         TypeResolver.populateTypeMappings(this.val$mappings, fromArrayType.getGenericComponentType(), componentType);
/*     */       }
/*     */       
/*     */       void visitClass(Class<?> fromClass)
/*     */       {
/* 200 */         if ((to instanceof WildcardType)) {
/* 201 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 206 */         throw new IllegalArgumentException("No type mapping from " + fromClass + " to " + to); } }
/*     */     
/* 208 */       .visit(new Type[] { from });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type resolveType(Type type)
/*     */   {
/* 216 */     Preconditions.checkNotNull(type);
/* 217 */     if ((type instanceof TypeVariable))
/* 218 */       return this.typeTable.resolve((TypeVariable)type);
/* 219 */     if ((type instanceof ParameterizedType))
/* 220 */       return resolveParameterizedType((ParameterizedType)type);
/* 221 */     if ((type instanceof GenericArrayType))
/* 222 */       return resolveGenericArrayType((GenericArrayType)type);
/* 223 */     if ((type instanceof WildcardType)) {
/* 224 */       return resolveWildcardType((WildcardType)type);
/*     */     }
/*     */     
/* 227 */     return type;
/*     */   }
/*     */   
/*     */   Type[] resolveTypesInPlace(Type[] types)
/*     */   {
/* 232 */     for (int i = 0; i < types.length; i++) {
/* 233 */       types[i] = resolveType(types[i]);
/*     */     }
/* 235 */     return types;
/*     */   }
/*     */   
/*     */   private Type[] resolveTypes(Type[] types) {
/* 239 */     Type[] result = new Type[types.length];
/* 240 */     for (int i = 0; i < types.length; i++) {
/* 241 */       result[i] = resolveType(types[i]);
/*     */     }
/* 243 */     return result;
/*     */   }
/*     */   
/*     */   private WildcardType resolveWildcardType(WildcardType type) {
/* 247 */     Type[] lowerBounds = type.getLowerBounds();
/* 248 */     Type[] upperBounds = type.getUpperBounds();
/* 249 */     return new Types.WildcardTypeImpl(resolveTypes(lowerBounds), resolveTypes(upperBounds));
/*     */   }
/*     */   
/*     */   private Type resolveGenericArrayType(GenericArrayType type) {
/* 253 */     Type componentType = type.getGenericComponentType();
/* 254 */     Type resolvedComponentType = resolveType(componentType);
/* 255 */     return Types.newArrayType(resolvedComponentType);
/*     */   }
/*     */   
/*     */   private ParameterizedType resolveParameterizedType(ParameterizedType type) {
/* 259 */     Type owner = type.getOwnerType();
/* 260 */     Type resolvedOwner = owner == null ? null : resolveType(owner);
/* 261 */     Type resolvedRawType = resolveType(type.getRawType());
/*     */     
/* 263 */     Type[] args = type.getActualTypeArguments();
/* 264 */     Type[] resolvedArgs = resolveTypes(args);
/* 265 */     return Types.newParameterizedTypeWithOwner(resolvedOwner, (Class)resolvedRawType, resolvedArgs);
/*     */   }
/*     */   
/*     */   private static <T> T expectArgument(Class<T> type, Object arg)
/*     */   {
/*     */     try {
/* 271 */       return (T)type.cast(arg);
/*     */     } catch (ClassCastException e) {
/* 273 */       throw new IllegalArgumentException(arg + " is not a " + type.getSimpleName());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TypeTable
/*     */   {
/*     */     private final ImmutableMap<TypeResolver.TypeVariableKey, Type> map;
/*     */     
/*     */     TypeTable() {
/* 282 */       this.map = ImmutableMap.of();
/*     */     }
/*     */     
/*     */     private TypeTable(ImmutableMap<TypeResolver.TypeVariableKey, Type> map) {
/* 286 */       this.map = map;
/*     */     }
/*     */     
/*     */     final TypeTable where(Map<TypeResolver.TypeVariableKey, ? extends Type> mappings)
/*     */     {
/* 291 */       ImmutableMap.Builder<TypeResolver.TypeVariableKey, Type> builder = ImmutableMap.builder();
/* 292 */       builder.putAll(this.map);
/* 293 */       for (Map.Entry<TypeResolver.TypeVariableKey, ? extends Type> mapping : mappings.entrySet()) {
/* 294 */         TypeResolver.TypeVariableKey variable = (TypeResolver.TypeVariableKey)mapping.getKey();
/* 295 */         Type type = (Type)mapping.getValue();
/* 296 */         Preconditions.checkArgument(!variable.equalsType(type), "Type variable %s bound to itself", variable);
/* 297 */         builder.put(variable, type);
/*     */       }
/* 299 */       return new TypeTable(builder.build());
/*     */     }
/*     */     
/*     */     final Type resolve(final TypeVariable<?> var) {
/* 303 */       final TypeTable unguarded = this;
/* 304 */       TypeTable guarded = new TypeTable()
/*     */       {
/*     */         public Type resolveInternal(TypeVariable<?> intermediateVar, TypeResolver.TypeTable forDependent)
/*     */         {
/* 308 */           if (intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration())) {
/* 309 */             return intermediateVar;
/*     */           }
/* 311 */           return unguarded.resolveInternal(intermediateVar, forDependent);
/*     */         }
/* 313 */       };
/* 314 */       return resolveInternal(var, guarded);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     Type resolveInternal(TypeVariable<?> var, TypeTable forDependants)
/*     */     {
/* 326 */       Type type = (Type)this.map.get(new TypeResolver.TypeVariableKey(var));
/* 327 */       if (type == null) {
/* 328 */         Type[] bounds = var.getBounds();
/* 329 */         if (bounds.length == 0) {
/* 330 */           return var;
/*     */         }
/* 332 */         Type[] resolvedBounds = new TypeResolver(forDependants, null).resolveTypes(bounds);
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
/* 361 */         if ((Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) && 
/* 362 */           (Arrays.equals(bounds, resolvedBounds))) {
/* 363 */           return var;
/*     */         }
/* 365 */         return Types.newArtificialTypeVariable(var
/* 366 */           .getGenericDeclaration(), var.getName(), resolvedBounds);
/*     */       }
/*     */       
/* 369 */       return new TypeResolver(forDependants, null).resolveType(type);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TypeMappingIntrospector extends TypeVisitor
/*     */   {
/* 375 */     private final Map<TypeResolver.TypeVariableKey, Type> mappings = Maps.newHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     static ImmutableMap<TypeResolver.TypeVariableKey, Type> getTypeMappings(Type contextType)
/*     */     {
/* 382 */       Preconditions.checkNotNull(contextType);
/* 383 */       TypeMappingIntrospector introspector = new TypeMappingIntrospector();
/* 384 */       introspector.visit(new Type[] { contextType });
/* 385 */       return ImmutableMap.copyOf(introspector.mappings);
/*     */     }
/*     */     
/*     */     void visitClass(Class<?> clazz)
/*     */     {
/* 390 */       visit(new Type[] { clazz.getGenericSuperclass() });
/* 391 */       visit(clazz.getGenericInterfaces());
/*     */     }
/*     */     
/*     */     void visitParameterizedType(ParameterizedType parameterizedType)
/*     */     {
/* 396 */       Class<?> rawClass = (Class)parameterizedType.getRawType();
/* 397 */       TypeVariable<?>[] vars = rawClass.getTypeParameters();
/* 398 */       Type[] typeArgs = parameterizedType.getActualTypeArguments();
/* 399 */       Preconditions.checkState(vars.length == typeArgs.length);
/* 400 */       for (int i = 0; i < vars.length; i++) {
/* 401 */         map(new TypeResolver.TypeVariableKey(vars[i]), typeArgs[i]);
/*     */       }
/* 403 */       visit(new Type[] { rawClass });
/* 404 */       visit(new Type[] { parameterizedType.getOwnerType() });
/*     */     }
/*     */     
/*     */     void visitTypeVariable(TypeVariable<?> t)
/*     */     {
/* 409 */       visit(t.getBounds());
/*     */     }
/*     */     
/*     */     void visitWildcardType(WildcardType t)
/*     */     {
/* 414 */       visit(t.getUpperBounds());
/*     */     }
/*     */     
/*     */     private void map(TypeResolver.TypeVariableKey var, Type arg) {
/* 418 */       if (this.mappings.containsKey(var))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 424 */         return;
/*     */       }
/*     */       
/* 427 */       for (Type t = arg; t != null; t = (Type)this.mappings.get(TypeResolver.TypeVariableKey.forLookup(t))) {
/* 428 */         if (var.equalsType(t))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 433 */           for (Type x = arg; x != null; x = (Type)this.mappings.remove(TypeResolver.TypeVariableKey.forLookup(x))) {}
/* 434 */           return;
/*     */         }
/*     */       }
/* 437 */       this.mappings.put(var, arg);
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
/*     */   private static class WildcardCapturer
/*     */   {
/* 450 */     static final WildcardCapturer INSTANCE = new WildcardCapturer();
/*     */     private final AtomicInteger id;
/*     */     
/*     */     private WildcardCapturer()
/*     */     {
/* 455 */       this(new AtomicInteger());
/*     */     }
/*     */     
/*     */     private WildcardCapturer(AtomicInteger id) {
/* 459 */       this.id = id;
/*     */     }
/*     */     
/*     */     final Type capture(Type type) {
/* 463 */       Preconditions.checkNotNull(type);
/* 464 */       if ((type instanceof Class)) {
/* 465 */         return type;
/*     */       }
/* 467 */       if ((type instanceof TypeVariable)) {
/* 468 */         return type;
/*     */       }
/* 470 */       if ((type instanceof GenericArrayType)) {
/* 471 */         GenericArrayType arrayType = (GenericArrayType)type;
/* 472 */         return Types.newArrayType(
/* 473 */           notForTypeVariable().capture(arrayType.getGenericComponentType()));
/*     */       }
/* 475 */       if ((type instanceof ParameterizedType)) {
/* 476 */         ParameterizedType parameterizedType = (ParameterizedType)type;
/* 477 */         Class<?> rawType = (Class)parameterizedType.getRawType();
/* 478 */         TypeVariable<?>[] typeVars = rawType.getTypeParameters();
/* 479 */         Type[] typeArgs = parameterizedType.getActualTypeArguments();
/* 480 */         for (int i = 0; i < typeArgs.length; i++) {
/* 481 */           typeArgs[i] = forTypeVariable(typeVars[i]).capture(typeArgs[i]);
/*     */         }
/* 483 */         return Types.newParameterizedTypeWithOwner(
/* 484 */           notForTypeVariable().captureNullable(parameterizedType.getOwnerType()), rawType, typeArgs);
/*     */       }
/*     */       
/*     */ 
/* 488 */       if ((type instanceof WildcardType)) {
/* 489 */         WildcardType wildcardType = (WildcardType)type;
/* 490 */         Type[] lowerBounds = wildcardType.getLowerBounds();
/* 491 */         if (lowerBounds.length == 0) {
/* 492 */           return captureAsTypeVariable(wildcardType.getUpperBounds());
/*     */         }
/*     */         
/* 495 */         return type;
/*     */       }
/*     */       
/* 498 */       throw new AssertionError("must have been one of the known types");
/*     */     }
/*     */     
/*     */     TypeVariable<?> captureAsTypeVariable(Type[] upperBounds)
/*     */     {
/* 503 */       String name = "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join(upperBounds);
/* 504 */       return Types.newArtificialTypeVariable(WildcardCapturer.class, name, upperBounds);
/*     */     }
/*     */     
/*     */     private WildcardCapturer forTypeVariable(final TypeVariable<?> typeParam) {
/* 508 */       new WildcardCapturer(this.id, typeParam)
/*     */       {
/*     */         TypeVariable<?> captureAsTypeVariable(Type[] upperBounds) {
/* 511 */           Set<Type> combined = new LinkedHashSet(Arrays.asList(upperBounds));
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 518 */           combined.addAll(Arrays.asList(typeParam.getBounds()));
/* 519 */           if (combined.size() > 1) {
/* 520 */             combined.remove(Object.class);
/*     */           }
/* 522 */           return super.captureAsTypeVariable((Type[])combined.toArray(new Type[0]));
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     private WildcardCapturer notForTypeVariable() {
/* 528 */       return new WildcardCapturer(this.id);
/*     */     }
/*     */     
/*     */     private Type captureNullable(Type type) {
/* 532 */       if (type == null) {
/* 533 */         return null;
/*     */       }
/* 535 */       return capture(type);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class TypeVariableKey
/*     */   {
/*     */     private final TypeVariable<?> var;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     TypeVariableKey(TypeVariable<?> var)
/*     */     {
/* 556 */       this.var = ((TypeVariable)Preconditions.checkNotNull(var));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 561 */       return Objects.hashCode(new Object[] { this.var.getGenericDeclaration(), this.var.getName() });
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 566 */       if ((obj instanceof TypeVariableKey)) {
/* 567 */         TypeVariableKey that = (TypeVariableKey)obj;
/* 568 */         return equalsTypeVariable(that.var);
/*     */       }
/* 570 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 576 */       return this.var.toString();
/*     */     }
/*     */     
/*     */     static TypeVariableKey forLookup(Type t)
/*     */     {
/* 581 */       if ((t instanceof TypeVariable)) {
/* 582 */         return new TypeVariableKey((TypeVariable)t);
/*     */       }
/* 584 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     boolean equalsType(Type type)
/*     */     {
/* 593 */       if ((type instanceof TypeVariable)) {
/* 594 */         return equalsTypeVariable((TypeVariable)type);
/*     */       }
/* 596 */       return false;
/*     */     }
/*     */     
/*     */     private boolean equalsTypeVariable(TypeVariable<?> that)
/*     */     {
/* 601 */       return (this.var.getGenericDeclaration().equals(that.getGenericDeclaration())) && 
/* 602 */         (this.var.getName().equals(that.getName()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\google\common\reflect\TypeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */