/*      */ package com.fasterxml.jackson.databind.type;
/*      */ 
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*      */ import com.fasterxml.jackson.databind.util.LRUMap;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class TypeFactory
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   37 */   private static final JavaType[] NO_TYPES = new JavaType[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   44 */   protected static final TypeFactory instance = new TypeFactory();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   56 */   protected static final SimpleType CORE_TYPE_STRING = new SimpleType(String.class);
/*   57 */   protected static final SimpleType CORE_TYPE_BOOL = new SimpleType(Boolean.TYPE);
/*   58 */   protected static final SimpleType CORE_TYPE_INT = new SimpleType(Integer.TYPE);
/*   59 */   protected static final SimpleType CORE_TYPE_LONG = new SimpleType(Long.TYPE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   66 */   protected final LRUMap<ClassKey, JavaType> _typeCache = new LRUMap(16, 100);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected transient HierarchicType _cachedHashMapType;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected transient HierarchicType _cachedArrayListType;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final TypeModifier[] _modifiers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final TypeParser _parser;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private TypeFactory()
/*      */   {
/*  107 */     this._parser = new TypeParser(this);
/*  108 */     this._modifiers = null;
/*      */   }
/*      */   
/*      */   protected TypeFactory(TypeParser p, TypeModifier[] mods) {
/*  112 */     this._parser = p;
/*  113 */     this._modifiers = mods;
/*      */   }
/*      */   
/*      */   public TypeFactory withModifier(TypeModifier mod)
/*      */   {
/*  118 */     if (mod == null) {
/*  119 */       return new TypeFactory(this._parser, this._modifiers);
/*      */     }
/*  121 */     if (this._modifiers == null) {
/*  122 */       return new TypeFactory(this._parser, new TypeModifier[] { mod });
/*      */     }
/*  124 */     return new TypeFactory(this._parser, (TypeModifier[])ArrayBuilders.insertInListNoDup(this._modifiers, mod));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static TypeFactory defaultInstance()
/*      */   {
/*  132 */     return instance;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearCache()
/*      */   {
/*  145 */     this._typeCache.clear();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static JavaType unknownType()
/*      */   {
/*  160 */     return defaultInstance()._unknownType();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> rawClass(Type t)
/*      */   {
/*  170 */     if ((t instanceof Class)) {
/*  171 */       return (Class)t;
/*      */     }
/*      */     
/*  174 */     return defaultInstance().constructType(t).getRawClass();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass)
/*      */   {
/*  193 */     if (baseType.getRawClass() == subclass) {
/*  194 */       return baseType;
/*      */     }
/*      */     
/*  197 */     if ((baseType instanceof SimpleType))
/*      */     {
/*  199 */       if ((subclass.isArray()) || (Map.class.isAssignableFrom(subclass)) || (Collection.class.isAssignableFrom(subclass)))
/*      */       {
/*      */ 
/*      */ 
/*  203 */         if (!baseType.getRawClass().isAssignableFrom(subclass)) {
/*  204 */           throw new IllegalArgumentException("Class " + subclass.getClass().getName() + " not subtype of " + baseType);
/*      */         }
/*      */         
/*  207 */         JavaType subtype = _fromClass(subclass, new TypeBindings(this, baseType.getRawClass()));
/*      */         
/*  209 */         Object h = baseType.getValueHandler();
/*  210 */         if (h != null) {
/*  211 */           subtype = subtype.withValueHandler(h);
/*      */         }
/*  213 */         h = baseType.getTypeHandler();
/*  214 */         if (h != null) {
/*  215 */           subtype = subtype.withTypeHandler(h);
/*      */         }
/*  217 */         return subtype;
/*      */       }
/*      */     }
/*      */     
/*  221 */     return baseType.narrowBy(subclass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructFromCanonical(String canonical)
/*      */     throws IllegalArgumentException
/*      */   {
/*  236 */     return this._parser.parse(canonical);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType[] findTypeParameters(JavaType type, Class<?> expType)
/*      */   {
/*  258 */     if (expType == type.getParameterSource())
/*      */     {
/*  260 */       int count = type.containedTypeCount();
/*  261 */       if (count == 0) return null;
/*  262 */       JavaType[] result = new JavaType[count];
/*  263 */       for (int i = 0; i < count; i++) {
/*  264 */         result[i] = type.containedType(i);
/*      */       }
/*  266 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  274 */     Class<?> raw = type.getRawClass();
/*  275 */     return findTypeParameters(raw, expType, new TypeBindings(this, type));
/*      */   }
/*      */   
/*      */   public JavaType[] findTypeParameters(Class<?> clz, Class<?> expType) {
/*  279 */     return findTypeParameters(clz, expType, new TypeBindings(this, clz));
/*      */   }
/*      */   
/*      */ 
/*      */   public JavaType[] findTypeParameters(Class<?> clz, Class<?> expType, TypeBindings bindings)
/*      */   {
/*  285 */     HierarchicType subType = _findSuperTypeChain(clz, expType);
/*      */     
/*  287 */     if (subType == null) {
/*  288 */       throw new IllegalArgumentException("Class " + clz.getName() + " is not a subtype of " + expType.getName());
/*      */     }
/*      */     
/*  291 */     HierarchicType superType = subType;
/*  292 */     while (superType.getSuperType() != null) {
/*  293 */       superType = superType.getSuperType();
/*  294 */       Class<?> raw = superType.getRawClass();
/*  295 */       TypeBindings newBindings = new TypeBindings(this, raw);
/*  296 */       if (superType.isGeneric()) {
/*  297 */         ParameterizedType pt = superType.asGeneric();
/*  298 */         Type[] actualTypes = pt.getActualTypeArguments();
/*  299 */         TypeVariable<?>[] vars = raw.getTypeParameters();
/*  300 */         int len = actualTypes.length;
/*  301 */         for (int i = 0; i < len; i++) {
/*  302 */           String name = vars[i].getName();
/*  303 */           JavaType type = _constructType(actualTypes[i], bindings);
/*  304 */           newBindings.addBinding(name, type);
/*      */         }
/*      */       }
/*  307 */       bindings = newBindings;
/*      */     }
/*      */     
/*      */ 
/*  311 */     if (!superType.isGeneric()) {
/*  312 */       return null;
/*      */     }
/*  314 */     return bindings.typesAsArray();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType moreSpecificType(JavaType type1, JavaType type2)
/*      */   {
/*  329 */     if (type1 == null) {
/*  330 */       return type2;
/*      */     }
/*  332 */     if (type2 == null) {
/*  333 */       return type1;
/*      */     }
/*  335 */     Class<?> raw1 = type1.getRawClass();
/*  336 */     Class<?> raw2 = type2.getRawClass();
/*  337 */     if (raw1 == raw2) {
/*  338 */       return type1;
/*      */     }
/*      */     
/*  341 */     if (raw1.isAssignableFrom(raw2)) {
/*  342 */       return type2;
/*      */     }
/*  344 */     return type1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructType(Type type)
/*      */   {
/*  354 */     return _constructType(type, null);
/*      */   }
/*      */   
/*      */   public JavaType constructType(Type type, TypeBindings bindings) {
/*  358 */     return _constructType(type, bindings);
/*      */   }
/*      */   
/*      */   public JavaType constructType(TypeReference<?> typeRef) {
/*  362 */     return _constructType(typeRef.getType(), null);
/*      */   }
/*      */   
/*      */   public JavaType constructType(Type type, Class<?> context) {
/*  366 */     TypeBindings b = context == null ? null : new TypeBindings(this, context);
/*  367 */     return _constructType(type, b);
/*      */   }
/*      */   
/*      */   public JavaType constructType(Type type, JavaType context) {
/*  371 */     TypeBindings b = context == null ? null : new TypeBindings(this, context);
/*  372 */     return _constructType(type, b);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _constructType(Type type, TypeBindings context)
/*      */   {
/*      */     JavaType resultType;
/*      */     
/*      */ 
/*      */ 
/*  385 */     if ((type instanceof Class)) {
/*  386 */       resultType = _fromClass((Class)type, context);
/*      */     } else {
/*      */       JavaType resultType;
/*  389 */       if ((type instanceof ParameterizedType)) {
/*  390 */         resultType = _fromParamType((ParameterizedType)type, context);
/*      */       } else {
/*  392 */         if ((type instanceof JavaType))
/*  393 */           return (JavaType)type;
/*      */         JavaType resultType;
/*  395 */         if ((type instanceof GenericArrayType)) {
/*  396 */           resultType = _fromArrayType((GenericArrayType)type, context);
/*      */         } else { JavaType resultType;
/*  398 */           if ((type instanceof TypeVariable)) {
/*  399 */             resultType = _fromVariable((TypeVariable)type, context);
/*      */           } else { JavaType resultType;
/*  401 */             if ((type instanceof WildcardType)) {
/*  402 */               resultType = _fromWildcard((WildcardType)type, context);
/*      */             }
/*      */             else
/*  405 */               throw new IllegalArgumentException("Unrecognized Type: " + (type == null ? "[null]" : type.toString()));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     JavaType resultType;
/*  411 */     if ((this._modifiers != null) && (!resultType.isContainerType())) {
/*  412 */       for (TypeModifier mod : this._modifiers) {
/*  413 */         resultType = mod.modifyType(resultType, type, context, this);
/*      */       }
/*      */     }
/*  416 */     return resultType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ArrayType constructArrayType(Class<?> elementType)
/*      */   {
/*  432 */     return ArrayType.construct(_constructType(elementType, null), null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ArrayType constructArrayType(JavaType elementType)
/*      */   {
/*  442 */     return ArrayType.construct(elementType, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionType constructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass)
/*      */   {
/*  452 */     return CollectionType.construct(collectionClass, constructType(elementClass));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionType constructCollectionType(Class<? extends Collection> collectionClass, JavaType elementType)
/*      */   {
/*  462 */     return CollectionType.construct(collectionClass, elementType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionLikeType constructCollectionLikeType(Class<?> collectionClass, Class<?> elementClass)
/*      */   {
/*  472 */     return CollectionLikeType.construct(collectionClass, constructType(elementClass));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionLikeType constructCollectionLikeType(Class<?> collectionClass, JavaType elementType)
/*      */   {
/*  482 */     return CollectionLikeType.construct(collectionClass, elementType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapType constructMapType(Class<? extends Map> mapClass, JavaType keyType, JavaType valueType)
/*      */   {
/*  492 */     return MapType.construct(mapClass, keyType, valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapType constructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass)
/*      */   {
/*  502 */     return MapType.construct(mapClass, constructType(keyClass), constructType(valueClass));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapLikeType constructMapLikeType(Class<?> mapClass, JavaType keyType, JavaType valueType)
/*      */   {
/*  512 */     return MapLikeType.construct(mapClass, keyType, valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapLikeType constructMapLikeType(Class<?> mapClass, Class<?> keyClass, Class<?> valueClass)
/*      */   {
/*  522 */     return MapType.construct(mapClass, constructType(keyClass), constructType(valueClass));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JavaType constructSimpleType(Class<?> rawType, JavaType[] parameterTypes)
/*      */   {
/*  532 */     return constructSimpleType(rawType, rawType, parameterTypes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JavaType constructSimpleType(Class<?> rawType, Class<?> parameterTarget, JavaType[] parameterTypes)
/*      */   {
/*  539 */     TypeVariable<?>[] typeVars = parameterTarget.getTypeParameters();
/*  540 */     if (typeVars.length != parameterTypes.length) {
/*  541 */       throw new IllegalArgumentException("Parameter type mismatch for " + rawType.getName() + " (and target " + parameterTarget.getName() + "): expected " + typeVars.length + " parameters, was given " + parameterTypes.length);
/*      */     }
/*      */     
/*      */ 
/*  545 */     String[] names = new String[typeVars.length];
/*  546 */     int i = 0; for (int len = typeVars.length; i < len; i++) {
/*  547 */       names[i] = typeVars[i].getName();
/*      */     }
/*  549 */     return new SimpleType(rawType, names, parameterTypes, null, null, false, parameterTarget);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType uncheckedSimpleType(Class<?> cls)
/*      */   {
/*  560 */     return new SimpleType(cls);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructParametrizedType(Class<?> parametrized, Class<?> parametersFor, Class<?>... parameterClasses)
/*      */   {
/*  585 */     int len = parameterClasses.length;
/*  586 */     JavaType[] pt = new JavaType[len];
/*  587 */     for (int i = 0; i < len; i++) {
/*  588 */       pt[i] = _fromClass(parameterClasses[i], null);
/*      */     }
/*  590 */     return constructParametrizedType(parametrized, parametersFor, pt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses)
/*      */   {
/*  598 */     return constructParametrizedType(parametrized, parametrized, parameterClasses);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructParametrizedType(Class<?> parametrized, Class<?> parametersFor, JavaType... parameterTypes)
/*      */   {
/*      */     JavaType resultType;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     JavaType resultType;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  628 */     if (parametrized.isArray())
/*      */     {
/*  630 */       if (parameterTypes.length != 1) {
/*  631 */         throw new IllegalArgumentException("Need exactly 1 parameter type for arrays (" + parametrized.getName() + ")");
/*      */       }
/*  633 */       resultType = constructArrayType(parameterTypes[0]);
/*      */     } else { JavaType resultType;
/*  635 */       if (Map.class.isAssignableFrom(parametrized)) {
/*  636 */         if (parameterTypes.length != 2) {
/*  637 */           throw new IllegalArgumentException("Need exactly 2 parameter types for Map types (" + parametrized.getName() + ")");
/*      */         }
/*  639 */         resultType = constructMapType(parametrized, parameterTypes[0], parameterTypes[1]);
/*      */       } else { JavaType resultType;
/*  641 */         if (Collection.class.isAssignableFrom(parametrized)) {
/*  642 */           if (parameterTypes.length != 1) {
/*  643 */             throw new IllegalArgumentException("Need exactly 1 parameter type for Collection types (" + parametrized.getName() + ")");
/*      */           }
/*  645 */           resultType = constructCollectionType(parametrized, parameterTypes[0]);
/*      */         } else {
/*  647 */           resultType = constructSimpleType(parametrized, parametersFor, parameterTypes);
/*      */         } } }
/*  649 */     return resultType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JavaType constructParametricType(Class<?> parametrized, JavaType... parameterTypes)
/*      */   {
/*  657 */     return constructParametrizedType(parametrized, parametrized, parameterTypes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionType constructRawCollectionType(Class<? extends Collection> collectionClass)
/*      */   {
/*  679 */     return CollectionType.construct(collectionClass, unknownType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionLikeType constructRawCollectionLikeType(Class<?> collectionClass)
/*      */   {
/*  694 */     return CollectionLikeType.construct(collectionClass, unknownType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapType constructRawMapType(Class<? extends Map> mapClass)
/*      */   {
/*  709 */     return MapType.construct(mapClass, unknownType(), unknownType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapLikeType constructRawMapLikeType(Class<?> mapClass)
/*      */   {
/*  724 */     return MapLikeType.construct(mapClass, unknownType(), unknownType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _fromClass(Class<?> clz, TypeBindings context)
/*      */   {
/*  740 */     if (clz == String.class) return CORE_TYPE_STRING;
/*  741 */     if (clz == Boolean.TYPE) return CORE_TYPE_BOOL;
/*  742 */     if (clz == Integer.TYPE) return CORE_TYPE_INT;
/*  743 */     if (clz == Long.TYPE) { return CORE_TYPE_LONG;
/*      */     }
/*      */     
/*  746 */     ClassKey key = new ClassKey(clz);
/*  747 */     JavaType result = (JavaType)this._typeCache.get(key);
/*  748 */     if (result != null) {
/*  749 */       return result;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  760 */     if (clz.isArray()) {
/*  761 */       result = ArrayType.construct(_constructType(clz.getComponentType(), null), null, null);
/*      */ 
/*      */ 
/*      */     }
/*  765 */     else if (clz.isEnum()) {
/*  766 */       result = new SimpleType(clz);
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*  771 */     else if (Map.class.isAssignableFrom(clz)) {
/*  772 */       result = _mapType(clz);
/*  773 */     } else if (Collection.class.isAssignableFrom(clz)) {
/*  774 */       result = _collectionType(clz);
/*      */ 
/*      */     }
/*  777 */     else if (Map.Entry.class.isAssignableFrom(clz)) {
/*  778 */       JavaType[] pts = findTypeParameters(clz, Map.Entry.class);
/*      */       JavaType kt;
/*  780 */       JavaType kt; JavaType vt; if ((pts == null) || (pts.length != 2)) { JavaType vt;
/*  781 */         kt = vt = unknownType();
/*      */       } else {
/*  783 */         kt = pts[0];
/*  784 */         vt = pts[1];
/*      */       }
/*  786 */       result = constructSimpleType(clz, Map.Entry.class, new JavaType[] { kt, vt });
/*      */     } else {
/*  788 */       result = new SimpleType(clz);
/*      */     }
/*      */     
/*  791 */     this._typeCache.put(key, result);
/*  792 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _fromParameterizedClass(Class<?> clz, List<JavaType> paramTypes)
/*      */   {
/*  801 */     if (clz.isArray()) {
/*  802 */       return ArrayType.construct(_constructType(clz.getComponentType(), null), null, null);
/*      */     }
/*  804 */     if (clz.isEnum()) {
/*  805 */       return new SimpleType(clz);
/*      */     }
/*  807 */     if (Map.class.isAssignableFrom(clz))
/*      */     {
/*      */ 
/*  810 */       if (paramTypes.size() > 0) {
/*  811 */         JavaType keyType = (JavaType)paramTypes.get(0);
/*  812 */         JavaType contentType = paramTypes.size() >= 2 ? (JavaType)paramTypes.get(1) : _unknownType();
/*      */         
/*  814 */         return MapType.construct(clz, keyType, contentType);
/*      */       }
/*  816 */       return _mapType(clz);
/*      */     }
/*  818 */     if (Collection.class.isAssignableFrom(clz)) {
/*  819 */       if (paramTypes.size() >= 1) {
/*  820 */         return CollectionType.construct(clz, (JavaType)paramTypes.get(0));
/*      */       }
/*  822 */       return _collectionType(clz);
/*      */     }
/*  824 */     if (paramTypes.size() == 0) {
/*  825 */       return new SimpleType(clz);
/*      */     }
/*      */     
/*  828 */     JavaType[] pt = (JavaType[])paramTypes.toArray(new JavaType[paramTypes.size()]);
/*  829 */     return constructSimpleType(clz, clz, pt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _fromParamType(ParameterizedType type, TypeBindings context)
/*      */   {
/*  844 */     Class<?> rawType = (Class)type.getRawType();
/*  845 */     Type[] args = type.getActualTypeArguments();
/*  846 */     int paramCount = args == null ? 0 : args.length;
/*      */     
/*      */     JavaType[] pt;
/*      */     JavaType[] pt;
/*  850 */     if (paramCount == 0) {
/*  851 */       pt = NO_TYPES;
/*      */     } else {
/*  853 */       pt = new JavaType[paramCount];
/*  854 */       for (int i = 0; i < paramCount; i++) {
/*  855 */         pt[i] = _constructType(args[i], context);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  860 */     if (Map.class.isAssignableFrom(rawType)) {
/*  861 */       JavaType subtype = constructSimpleType(rawType, rawType, pt);
/*  862 */       JavaType[] mapParams = findTypeParameters(subtype, Map.class);
/*  863 */       if (mapParams.length != 2) {
/*  864 */         throw new IllegalArgumentException("Could not find 2 type parameters for Map class " + rawType.getName() + " (found " + mapParams.length + ")");
/*      */       }
/*  866 */       return MapType.construct(rawType, mapParams[0], mapParams[1]);
/*      */     }
/*  868 */     if (Collection.class.isAssignableFrom(rawType)) {
/*  869 */       JavaType subtype = constructSimpleType(rawType, rawType, pt);
/*  870 */       JavaType[] collectionParams = findTypeParameters(subtype, Collection.class);
/*  871 */       if (collectionParams.length != 1) {
/*  872 */         throw new IllegalArgumentException("Could not find 1 type parameter for Collection class " + rawType.getName() + " (found " + collectionParams.length + ")");
/*      */       }
/*  874 */       return CollectionType.construct(rawType, collectionParams[0]);
/*      */     }
/*  876 */     if (paramCount == 0) {
/*  877 */       return new SimpleType(rawType);
/*      */     }
/*  879 */     return constructSimpleType(rawType, pt);
/*      */   }
/*      */   
/*      */ 
/*      */   protected JavaType _fromArrayType(GenericArrayType type, TypeBindings context)
/*      */   {
/*  885 */     JavaType compType = _constructType(type.getGenericComponentType(), context);
/*  886 */     return ArrayType.construct(compType, null, null);
/*      */   }
/*      */   
/*      */   protected JavaType _fromVariable(TypeVariable<?> type, TypeBindings context)
/*      */   {
/*  891 */     String name = type.getName();
/*      */     
/*  893 */     if (context == null)
/*      */     {
/*  895 */       context = new TypeBindings(this, (Class)null);
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  902 */       JavaType actualType = context.findType(name, false);
/*  903 */       if (actualType != null) {
/*  904 */         return actualType;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  913 */     Type[] bounds = type.getBounds();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  928 */     context._addPlaceholder(name);
/*  929 */     return _constructType(bounds[0], context);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _fromWildcard(WildcardType type, TypeBindings context)
/*      */   {
/*  942 */     return _constructType(type.getUpperBounds()[0], context);
/*      */   }
/*      */   
/*      */   private JavaType _mapType(Class<?> rawClass)
/*      */   {
/*  947 */     JavaType[] typeParams = findTypeParameters(rawClass, Map.class);
/*      */     
/*  949 */     if (typeParams == null) {
/*  950 */       return MapType.construct(rawClass, _unknownType(), _unknownType());
/*      */     }
/*      */     
/*  953 */     if (typeParams.length != 2) {
/*  954 */       throw new IllegalArgumentException("Strange Map type " + rawClass.getName() + ": can not determine type parameters");
/*      */     }
/*  956 */     return MapType.construct(rawClass, typeParams[0], typeParams[1]);
/*      */   }
/*      */   
/*      */   private JavaType _collectionType(Class<?> rawClass)
/*      */   {
/*  961 */     JavaType[] typeParams = findTypeParameters(rawClass, Collection.class);
/*      */     
/*  963 */     if (typeParams == null) {
/*  964 */       return CollectionType.construct(rawClass, _unknownType());
/*      */     }
/*      */     
/*  967 */     if (typeParams.length != 1) {
/*  968 */       throw new IllegalArgumentException("Strange Collection type " + rawClass.getName() + ": can not determine type parameters");
/*      */     }
/*  970 */     return CollectionType.construct(rawClass, typeParams[0]);
/*      */   }
/*      */   
/*      */ 
/*      */   protected JavaType _resolveVariableViaSubTypes(HierarchicType leafType, String variableName, TypeBindings bindings)
/*      */   {
/*  976 */     if ((leafType != null) && (leafType.isGeneric())) {
/*  977 */       TypeVariable<?>[] typeVariables = leafType.getRawClass().getTypeParameters();
/*  978 */       int i = 0; for (int len = typeVariables.length; i < len; i++) {
/*  979 */         TypeVariable<?> tv = typeVariables[i];
/*  980 */         if (variableName.equals(tv.getName()))
/*      */         {
/*  982 */           Type type = leafType.asGeneric().getActualTypeArguments()[i];
/*  983 */           if ((type instanceof TypeVariable)) {
/*  984 */             return _resolveVariableViaSubTypes(leafType.getSubType(), ((TypeVariable)type).getName(), bindings);
/*      */           }
/*      */           
/*  987 */           return _constructType(type, bindings);
/*      */         }
/*      */       }
/*      */     }
/*  991 */     return _unknownType();
/*      */   }
/*      */   
/*      */   protected JavaType _unknownType() {
/*  995 */     return new SimpleType(Object.class);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected HierarchicType _findSuperTypeChain(Class<?> subtype, Class<?> supertype)
/*      */   {
/* 1013 */     if (supertype.isInterface()) {
/* 1014 */       return _findSuperInterfaceChain(subtype, supertype);
/*      */     }
/* 1016 */     return _findSuperClassChain(subtype, supertype);
/*      */   }
/*      */   
/*      */   protected HierarchicType _findSuperClassChain(Type currentType, Class<?> target)
/*      */   {
/* 1021 */     HierarchicType current = new HierarchicType(currentType);
/* 1022 */     Class<?> raw = current.getRawClass();
/* 1023 */     if (raw == target) {
/* 1024 */       return current;
/*      */     }
/*      */     
/* 1027 */     Type parent = raw.getGenericSuperclass();
/* 1028 */     if (parent != null) {
/* 1029 */       HierarchicType sup = _findSuperClassChain(parent, target);
/* 1030 */       if (sup != null) {
/* 1031 */         sup.setSubType(current);
/* 1032 */         current.setSuperType(sup);
/* 1033 */         return current;
/*      */       }
/*      */     }
/* 1036 */     return null;
/*      */   }
/*      */   
/*      */   protected HierarchicType _findSuperInterfaceChain(Type currentType, Class<?> target)
/*      */   {
/* 1041 */     HierarchicType current = new HierarchicType(currentType);
/* 1042 */     Class<?> raw = current.getRawClass();
/* 1043 */     if (raw == target) {
/* 1044 */       return new HierarchicType(currentType);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1050 */     if ((raw == HashMap.class) && 
/* 1051 */       (target == Map.class)) {
/* 1052 */       return _hashMapSuperInterfaceChain(current);
/*      */     }
/*      */     
/* 1055 */     if ((raw == ArrayList.class) && 
/* 1056 */       (target == List.class)) {
/* 1057 */       return _arrayListSuperInterfaceChain(current);
/*      */     }
/*      */     
/* 1060 */     return _doFindSuperInterfaceChain(current, target);
/*      */   }
/*      */   
/*      */   protected HierarchicType _doFindSuperInterfaceChain(HierarchicType current, Class<?> target)
/*      */   {
/* 1065 */     Class<?> raw = current.getRawClass();
/* 1066 */     Type[] parents = raw.getGenericInterfaces();
/*      */     
/*      */ 
/* 1069 */     if (parents != null) {
/* 1070 */       for (Type parent : parents) {
/* 1071 */         HierarchicType sup = _findSuperInterfaceChain(parent, target);
/* 1072 */         if (sup != null) {
/* 1073 */           sup.setSubType(current);
/* 1074 */           current.setSuperType(sup);
/* 1075 */           return current;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1080 */     Type parent = raw.getGenericSuperclass();
/* 1081 */     if (parent != null) {
/* 1082 */       HierarchicType sup = _findSuperInterfaceChain(parent, target);
/* 1083 */       if (sup != null) {
/* 1084 */         sup.setSubType(current);
/* 1085 */         current.setSuperType(sup);
/* 1086 */         return current;
/*      */       }
/*      */     }
/* 1089 */     return null;
/*      */   }
/*      */   
/*      */   protected synchronized HierarchicType _hashMapSuperInterfaceChain(HierarchicType current)
/*      */   {
/* 1094 */     if (this._cachedHashMapType == null) {
/* 1095 */       HierarchicType base = current.deepCloneWithoutSubtype();
/* 1096 */       _doFindSuperInterfaceChain(base, Map.class);
/* 1097 */       this._cachedHashMapType = base.getSuperType();
/*      */     }
/* 1099 */     HierarchicType t = this._cachedHashMapType.deepCloneWithoutSubtype();
/* 1100 */     current.setSuperType(t);
/* 1101 */     t.setSubType(current);
/* 1102 */     return current;
/*      */   }
/*      */   
/*      */   protected synchronized HierarchicType _arrayListSuperInterfaceChain(HierarchicType current)
/*      */   {
/* 1107 */     if (this._cachedArrayListType == null) {
/* 1108 */       HierarchicType base = current.deepCloneWithoutSubtype();
/* 1109 */       _doFindSuperInterfaceChain(base, List.class);
/* 1110 */       this._cachedArrayListType = base.getSuperType();
/*      */     }
/* 1112 */     HierarchicType t = this._cachedArrayListType.deepCloneWithoutSubtype();
/* 1113 */     current.setSuperType(t);
/* 1114 */     t.setSubType(current);
/* 1115 */     return current;
/*      */   }
/*      */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\TypeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */