/*     */ package org.eclipse.jetty.websocket.common.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectUtils
/*     */ {
/*     */   private static class GenericRef
/*     */   {
/*     */     private final Class<?> baseClass;
/*     */     private final Class<?> ifaceClass;
/*     */     Class<?> genericClass;
/*     */     public Type genericType;
/*     */     private int genericIndex;
/*     */     
/*     */     public GenericRef(Class<?> baseClass, Class<?> ifaceClass)
/*     */     {
/*  46 */       this.baseClass = baseClass;
/*  47 */       this.ifaceClass = ifaceClass;
/*     */     }
/*     */     
/*     */     public boolean needsUnwrap()
/*     */     {
/*  52 */       return (this.genericClass == null) && (this.genericType != null) && ((this.genericType instanceof TypeVariable));
/*     */     }
/*     */     
/*     */ 
/*     */     public void setGenericFromType(Type type, int index)
/*     */     {
/*  58 */       this.genericType = type;
/*  59 */       this.genericIndex = index;
/*  60 */       if ((type instanceof Class))
/*     */       {
/*  62 */         this.genericClass = ((Class)type);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/*  69 */       StringBuilder builder = new StringBuilder();
/*  70 */       builder.append("GenericRef [baseClass=");
/*  71 */       builder.append(this.baseClass);
/*  72 */       builder.append(", ifaceClass=");
/*  73 */       builder.append(this.ifaceClass);
/*  74 */       builder.append(", genericType=");
/*  75 */       builder.append(this.genericType);
/*  76 */       builder.append(", genericClass=");
/*  77 */       builder.append(this.genericClass);
/*  78 */       builder.append("]");
/*  79 */       return builder.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static StringBuilder appendTypeName(StringBuilder sb, Type type, boolean ellipses)
/*     */   {
/*  85 */     if ((type instanceof Class))
/*     */     {
/*  87 */       Class<?> ctype = (Class)type;
/*  88 */       if (ctype.isArray())
/*     */       {
/*     */         try
/*     */         {
/*  92 */           int dimensions = 0;
/*  93 */           while (ctype.isArray())
/*     */           {
/*  95 */             dimensions++;
/*  96 */             ctype = ctype.getComponentType();
/*     */           }
/*  98 */           sb.append(ctype.getName());
/*  99 */           for (int i = 0; i < dimensions; i++)
/*     */           {
/* 101 */             if (ellipses)
/*     */             {
/* 103 */               sb.append("...");
/*     */             }
/*     */             else
/*     */             {
/* 107 */               sb.append("[]");
/*     */             }
/*     */           }
/* 110 */           return sb;
/*     */         }
/*     */         catch (Throwable localThrowable) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 118 */       sb.append(ctype.getName());
/*     */     }
/*     */     else
/*     */     {
/* 122 */       sb.append(type.toString());
/*     */     }
/*     */     
/* 125 */     return sb;
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
/*     */   public static Class<?> findGenericClassFor(Class<?> baseClass, Class<?> ifaceClass)
/*     */   {
/* 139 */     GenericRef ref = new GenericRef(baseClass, ifaceClass);
/* 140 */     if (resolveGenericRef(ref, baseClass))
/*     */     {
/*     */ 
/* 143 */       return ref.genericClass;
/*     */     }
/*     */     
/*     */ 
/* 147 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private static int findTypeParameterIndex(Class<?> clazz, TypeVariable<?> needVar)
/*     */   {
/* 153 */     TypeVariable<?>[] params = clazz.getTypeParameters();
/* 154 */     for (int i = 0; i < params.length; i++)
/*     */     {
/* 156 */       if (params[i].getName().equals(needVar.getName()))
/*     */       {
/*     */ 
/* 159 */         return i;
/*     */       }
/*     */     }
/*     */     
/* 163 */     return -1;
/*     */   }
/*     */   
/*     */   public static boolean isDefaultConstructable(Class<?> clazz)
/*     */   {
/* 168 */     int mods = clazz.getModifiers();
/* 169 */     if ((Modifier.isAbstract(mods)) || (!Modifier.isPublic(mods)))
/*     */     {
/*     */ 
/* 172 */       return false;
/*     */     }
/*     */     
/* 175 */     Class<?>[] noargs = new Class[0];
/*     */     
/*     */     try
/*     */     {
/* 179 */       Constructor<?> constructor = clazz.getConstructor(noargs);
/*     */       
/* 181 */       return Modifier.isPublic(constructor.getModifiers());
/*     */     }
/*     */     catch (NoSuchMethodException|SecurityException e) {}
/*     */     
/* 185 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private static boolean resolveGenericRef(GenericRef ref, Class<?> clazz, Type type)
/*     */   {
/* 191 */     if ((type instanceof Class))
/*     */     {
/* 193 */       if (type == ref.ifaceClass)
/*     */       {
/*     */ 
/*     */ 
/* 197 */         ref.setGenericFromType(type, 0);
/* 198 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 203 */       return resolveGenericRef(ref, type);
/*     */     }
/*     */     
/*     */ 
/* 207 */     if ((type instanceof ParameterizedType))
/*     */     {
/* 209 */       ParameterizedType ptype = (ParameterizedType)type;
/* 210 */       Type rawType = ptype.getRawType();
/* 211 */       if (rawType == ref.ifaceClass)
/*     */       {
/*     */ 
/*     */ 
/* 215 */         ref.setGenericFromType(ptype.getActualTypeArguments()[0], 0);
/* 216 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 221 */       return resolveGenericRef(ref, rawType);
/*     */     }
/*     */     
/* 224 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean resolveGenericRef(GenericRef ref, Type type)
/*     */   {
/* 229 */     if ((type == null) || (type == Object.class))
/*     */     {
/* 231 */       return false;
/*     */     }
/*     */     
/* 234 */     if ((type instanceof Class))
/*     */     {
/* 236 */       Class<?> clazz = (Class)type;
/*     */       
/*     */ 
/* 239 */       if (clazz.getName().matches("^javax*\\..*"))
/*     */       {
/* 241 */         return false;
/*     */       }
/*     */       
/* 244 */       Type[] ifaces = clazz.getGenericInterfaces();
/* 245 */       for (Type iface : ifaces)
/*     */       {
/*     */ 
/* 248 */         if (resolveGenericRef(ref, clazz, iface))
/*     */         {
/* 250 */           if (ref.needsUnwrap())
/*     */           {
/*     */ 
/* 253 */             TypeVariable<?> needVar = (TypeVariable)ref.genericType;
/*     */             
/*     */ 
/*     */ 
/* 257 */             int typeParamIdx = findTypeParameterIndex(clazz, needVar);
/*     */             
/*     */ 
/* 260 */             if (typeParamIdx >= 0)
/*     */             {
/*     */ 
/*     */ 
/* 264 */               TypeVariable<?>[] params = clazz.getTypeParameters();
/* 265 */               if (params.length >= typeParamIdx)
/*     */               {
/* 267 */                 ref.setGenericFromType(params[typeParamIdx], typeParamIdx);
/*     */               }
/*     */             }
/* 270 */             else if ((iface instanceof ParameterizedType))
/*     */             {
/*     */ 
/* 273 */               Type arg = ((ParameterizedType)iface).getActualTypeArguments()[ref.genericIndex];
/* 274 */               ref.setGenericFromType(arg, ref.genericIndex);
/*     */             }
/*     */           }
/* 277 */           return true;
/*     */         }
/*     */       }
/*     */       
/* 281 */       type = clazz.getGenericSuperclass();
/* 282 */       return resolveGenericRef(ref, type);
/*     */     }
/*     */     
/* 285 */     if ((type instanceof ParameterizedType))
/*     */     {
/* 287 */       ParameterizedType ptype = (ParameterizedType)type;
/* 288 */       Class<?> rawClass = (Class)ptype.getRawType();
/* 289 */       if (resolveGenericRef(ref, rawClass))
/*     */       {
/* 291 */         if (ref.needsUnwrap())
/*     */         {
/*     */ 
/* 294 */           Object needVar = (TypeVariable)ref.genericType;
/*     */           
/* 296 */           int typeParamIdx = findTypeParameterIndex(rawClass, (TypeVariable)needVar);
/*     */           
/*     */ 
/* 299 */           Type arg = ptype.getActualTypeArguments()[typeParamIdx];
/* 300 */           ref.setGenericFromType(arg, typeParamIdx);
/* 301 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 306 */     return false;
/*     */   }
/*     */   
/*     */   public static String toShortName(Type type)
/*     */   {
/* 311 */     if (type == null)
/*     */     {
/* 313 */       return "<null>";
/*     */     }
/*     */     
/* 316 */     if ((type instanceof Class))
/*     */     {
/* 318 */       String name = ((Class)type).getName();
/* 319 */       return trimClassName(name);
/*     */     }
/*     */     
/* 322 */     if ((type instanceof ParameterizedType))
/*     */     {
/* 324 */       ParameterizedType ptype = (ParameterizedType)type;
/* 325 */       StringBuilder str = new StringBuilder();
/* 326 */       str.append(trimClassName(((Class)ptype.getRawType()).getName()));
/* 327 */       str.append("<");
/* 328 */       Type[] args = ptype.getActualTypeArguments();
/* 329 */       for (int i = 0; i < args.length; i++)
/*     */       {
/* 331 */         if (i > 0)
/*     */         {
/* 333 */           str.append(",");
/*     */         }
/* 335 */         str.append(args[i]);
/*     */       }
/* 337 */       str.append(">");
/* 338 */       return str.toString();
/*     */     }
/*     */     
/* 341 */     return type.toString();
/*     */   }
/*     */   
/*     */   public static String toString(Class<?> pojo, Method method)
/*     */   {
/* 346 */     StringBuilder str = new StringBuilder();
/*     */     
/*     */ 
/* 349 */     int mod = method.getModifiers() & Modifier.methodModifiers();
/* 350 */     if (mod != 0)
/*     */     {
/* 352 */       str.append(Modifier.toString(mod)).append(' ');
/*     */     }
/*     */     
/*     */ 
/* 356 */     Type retType = method.getGenericReturnType();
/* 357 */     appendTypeName(str, retType, false).append(' ');
/*     */     
/*     */ 
/* 360 */     str.append(pojo.getName());
/* 361 */     str.append("#");
/*     */     
/*     */ 
/* 364 */     str.append(method.getName());
/*     */     
/*     */ 
/* 367 */     str.append('(');
/* 368 */     Type[] params = method.getGenericParameterTypes();
/* 369 */     for (int j = 0; j < params.length; j++)
/*     */     {
/* 371 */       boolean ellipses = (method.isVarArgs()) && (j == params.length - 1);
/* 372 */       appendTypeName(str, params[j], ellipses);
/* 373 */       if (j < params.length - 1)
/*     */       {
/* 375 */         str.append(", ");
/*     */       }
/*     */     }
/* 378 */     str.append(')');
/*     */     
/*     */ 
/* 381 */     return str.toString();
/*     */   }
/*     */   
/*     */   public static String trimClassName(String name)
/*     */   {
/* 386 */     int idx = name.lastIndexOf('.');
/* 387 */     name = name.substring(idx + 1);
/* 388 */     idx = name.lastIndexOf('$');
/* 389 */     if (idx >= 0)
/*     */     {
/* 391 */       name = name.substring(idx + 1);
/*     */     }
/* 393 */     return name;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\org\eclipse\jetty\websocket\common\util\ReflectUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */