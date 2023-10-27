/*     */ package com.fasterxml.jackson.databind.type;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class TypeBindings
/*     */ {
/*  13 */   private static final JavaType[] NO_TYPES = new JavaType[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  18 */   public static final JavaType UNBOUND = new SimpleType(Object.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TypeFactory _typeFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JavaType _contextType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Class<?> _contextClass;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Map<String, JavaType> _bindings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HashSet<String> _placeholders;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final TypeBindings _parentBindings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeBindings(TypeFactory typeFactory, Class<?> cc)
/*     */   {
/*  64 */     this(typeFactory, null, cc, null);
/*     */   }
/*     */   
/*     */   public TypeBindings(TypeFactory typeFactory, JavaType type)
/*     */   {
/*  69 */     this(typeFactory, null, type.getRawClass(), type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeBindings childInstance()
/*     */   {
/*  79 */     return new TypeBindings(this._typeFactory, this, this._contextClass, this._contextType);
/*     */   }
/*     */   
/*     */ 
/*     */   private TypeBindings(TypeFactory tf, TypeBindings parent, Class<?> cc, JavaType type)
/*     */   {
/*  85 */     this._typeFactory = tf;
/*  86 */     this._parentBindings = parent;
/*  87 */     this._contextClass = cc;
/*  88 */     this._contextType = type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType resolveType(Class<?> cls)
/*     */   {
/*  98 */     return this._typeFactory._constructType(cls, this);
/*     */   }
/*     */   
/*     */   public JavaType resolveType(Type type) {
/* 102 */     return this._typeFactory._constructType(type, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getBindingCount()
/*     */   {
/* 112 */     if (this._bindings == null) {
/* 113 */       _resolve();
/*     */     }
/* 115 */     return this._bindings.size();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public JavaType findType(String name) {
/* 120 */     return findType(name, true);
/*     */   }
/*     */   
/*     */   public JavaType findType(String name, boolean mustFind)
/*     */   {
/* 125 */     if (this._bindings == null) {
/* 126 */       _resolve();
/*     */     }
/* 128 */     JavaType t = (JavaType)this._bindings.get(name);
/* 129 */     if (t != null) {
/* 130 */       return t;
/*     */     }
/* 132 */     if ((this._placeholders != null) && (this._placeholders.contains(name))) {
/* 133 */       return UNBOUND;
/*     */     }
/* 135 */     if (this._parentBindings != null) {
/* 136 */       return this._parentBindings.findType(name);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 145 */     if (this._contextClass != null) {
/* 146 */       Class<?> enclosing = this._contextClass.getEnclosingClass();
/* 147 */       if (enclosing != null)
/*     */       {
/*     */ 
/* 150 */         if (!java.lang.reflect.Modifier.isStatic(this._contextClass.getModifiers())) {
/* 151 */           return UNBOUND;
/*     */         }
/*     */       }
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
/* 167 */     if (!mustFind) {
/* 168 */       return null;
/*     */     }
/*     */     String className;
/*     */     String className;
/* 172 */     if (this._contextClass != null) {
/* 173 */       className = this._contextClass.getName(); } else { String className;
/* 174 */       if (this._contextType != null) {
/* 175 */         className = this._contextType.toString();
/*     */       } else
/* 177 */         className = "UNKNOWN";
/*     */     }
/* 179 */     throw new IllegalArgumentException("Type variable '" + name + "' can not be resolved (with context of class " + className + ")");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addBinding(String name, JavaType type)
/*     */   {
/* 187 */     if ((this._bindings == null) || (this._bindings.size() == 0)) {
/* 188 */       this._bindings = new LinkedHashMap();
/*     */     }
/* 190 */     this._bindings.put(name, type);
/*     */   }
/*     */   
/*     */   public JavaType[] typesAsArray()
/*     */   {
/* 195 */     if (this._bindings == null) {
/* 196 */       _resolve();
/*     */     }
/* 198 */     if (this._bindings.size() == 0) {
/* 199 */       return NO_TYPES;
/*     */     }
/* 201 */     return (JavaType[])this._bindings.values().toArray(new JavaType[this._bindings.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _resolve()
/*     */   {
/* 212 */     _resolveBindings(this._contextClass);
/*     */     
/*     */ 
/* 215 */     if (this._contextType != null) {
/* 216 */       int count = this._contextType.containedTypeCount();
/* 217 */       if (count > 0) {
/* 218 */         for (int i = 0; i < count; i++) {
/* 219 */           String name = this._contextType.containedTypeName(i);
/* 220 */           JavaType type = this._contextType.containedType(i);
/* 221 */           addBinding(name, type);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 227 */     if (this._bindings == null) {
/* 228 */       this._bindings = java.util.Collections.emptyMap();
/*     */     }
/*     */   }
/*     */   
/*     */   public void _addPlaceholder(String name) {
/* 233 */     if (this._placeholders == null) {
/* 234 */       this._placeholders = new HashSet();
/*     */     }
/* 236 */     this._placeholders.add(name);
/*     */   }
/*     */   
/*     */   protected void _resolveBindings(Type t)
/*     */   {
/* 241 */     if (t == null)
/*     */       return;
/*     */     Class<?> raw;
/* 244 */     if ((t instanceof ParameterizedType)) {
/* 245 */       ParameterizedType pt = (ParameterizedType)t;
/* 246 */       Type[] args = pt.getActualTypeArguments();
/* 247 */       if ((args != null) && (args.length > 0)) {
/* 248 */         Class<?> rawType = (Class)pt.getRawType();
/* 249 */         TypeVariable<?>[] vars = rawType.getTypeParameters();
/* 250 */         if (vars.length != args.length) {
/* 251 */           throw new IllegalArgumentException("Strange parametrized type (in class " + rawType.getName() + "): number of type arguments != number of type parameters (" + args.length + " vs " + vars.length + ")");
/*     */         }
/* 253 */         int i = 0; for (int len = args.length; i < len; i++) {
/* 254 */           TypeVariable<?> var = vars[i];
/* 255 */           String name = var.getName();
/* 256 */           if (this._bindings == null) {
/* 257 */             this._bindings = new LinkedHashMap();
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 262 */             if (this._bindings.containsKey(name))
/*     */               continue;
/*     */           }
/* 265 */           _addPlaceholder(name);
/*     */           
/* 267 */           this._bindings.put(name, this._typeFactory._constructType(args[i], this));
/*     */         }
/*     */       }
/* 270 */       raw = (Class)pt.getRawType();
/* 271 */     } else if ((t instanceof Class)) {
/* 272 */       Class<?> raw = (Class)t;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 278 */       Class<?> decl = raw.getDeclaringClass();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 283 */       if ((decl != null) && (!decl.isAssignableFrom(raw))) {
/* 284 */         _resolveBindings(raw.getDeclaringClass());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 290 */       TypeVariable<?>[] vars = raw.getTypeParameters();
/* 291 */       if ((vars != null) && (vars.length > 0)) {
/* 292 */         JavaType[] typeParams = null;
/*     */         
/* 294 */         if ((this._contextType != null) && (raw.isAssignableFrom(this._contextType.getRawClass()))) {
/* 295 */           typeParams = this._typeFactory.findTypeParameters(this._contextType, raw);
/*     */         }
/*     */         
/* 298 */         for (int i = 0; i < vars.length; i++) {
/* 299 */           TypeVariable<?> var = vars[i];
/*     */           
/* 301 */           String name = var.getName();
/* 302 */           Type varType = var.getBounds()[0];
/* 303 */           if (varType != null) {
/* 304 */             if (this._bindings == null) {
/* 305 */               this._bindings = new LinkedHashMap();
/*     */             } else
/* 307 */               if (this._bindings.containsKey(name))
/*     */                 continue;
/* 309 */             _addPlaceholder(name);
/*     */             
/* 311 */             if ((typeParams != null) && (typeParams.length > i)) {
/* 312 */               this._bindings.put(name, typeParams[i]);
/*     */             } else {
/* 314 */               this._bindings.put(name, this._typeFactory._constructType(varType, this));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       return;
/*     */     }
/*     */     
/*     */     Class<?> raw;
/* 326 */     _resolveBindings(raw.getGenericSuperclass());
/* 327 */     for (Type intType : raw.getGenericInterfaces()) {
/* 328 */       _resolveBindings(intType);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 335 */     if (this._bindings == null) {
/* 336 */       _resolve();
/*     */     }
/* 338 */     StringBuilder sb = new StringBuilder("[TypeBindings for ");
/* 339 */     if (this._contextType != null) {
/* 340 */       sb.append(this._contextType.toString());
/*     */     } else {
/* 342 */       sb.append(this._contextClass.getName());
/*     */     }
/* 344 */     sb.append(": ").append(this._bindings).append("]");
/* 345 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\fasterxml\jackson\databind\type\TypeBindings.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */