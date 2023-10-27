/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract interface Library
/*     */ {
/*     */   public static final String OPTION_TYPE_MAPPER = "type-mapper";
/*     */   public static final String OPTION_FUNCTION_MAPPER = "function-mapper";
/*     */   public static final String OPTION_INVOCATION_MAPPER = "invocation-mapper";
/*     */   public static final String OPTION_STRUCTURE_ALIGNMENT = "structure-alignment";
/*     */   public static final String OPTION_STRING_ENCODING = "string-encoding";
/*     */   public static final String OPTION_ALLOW_OBJECTS = "allow-objects";
/*     */   public static final String OPTION_CALLING_CONVENTION = "calling-convention";
/*     */   public static final String OPTION_OPEN_FLAGS = "open-flags";
/*     */   public static final String OPTION_CLASSLOADER = "classloader";
/*     */   
/*     */   public static class Handler
/*     */     implements InvocationHandler
/*     */   {
/*     */     static final Method OBJECT_TOSTRING;
/*     */     static final Method OBJECT_HASHCODE;
/*     */     static final Method OBJECT_EQUALS;
/*     */     private final NativeLibrary nativeLibrary;
/*     */     private final Class interfaceClass;
/*     */     private final Map options;
/*     */     private final InvocationMapper invocationMapper;
/*     */     
/*     */     static
/*     */     {
/*     */       try
/*     */       {
/* 112 */         OBJECT_TOSTRING = Object.class.getMethod("toString", new Class[0]);
/* 113 */         OBJECT_HASHCODE = Object.class.getMethod("hashCode", new Class[0]);
/* 114 */         OBJECT_EQUALS = Object.class.getMethod("equals", new Class[] { Object.class });
/*     */       }
/*     */       catch (Exception e) {
/* 117 */         throw new Error("Error retrieving Object.toString() method");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */     private final Map functions = new WeakHashMap();
/*     */     
/*     */     public Handler(String libname, Class interfaceClass, Map options) {
/* 129 */       if ((libname != null) && ("".equals(libname.trim()))) {
/* 130 */         throw new IllegalArgumentException("Invalid library name \"" + libname + "\"");
/*     */       }
/*     */       
/*     */ 
/* 134 */       this.interfaceClass = interfaceClass;
/* 135 */       options = new HashMap(options);
/*     */       
/* 137 */       int callingConvention = AltCallingConvention.class.isAssignableFrom(interfaceClass) ? 63 : 0;
/*     */       
/* 139 */       if (options.get("calling-convention") == null) {
/* 140 */         options.put("calling-convention", new Integer(callingConvention));
/*     */       }
/*     */       
/* 143 */       if (options.get("classloader") == null) {
/* 144 */         options.put("classloader", interfaceClass.getClassLoader());
/*     */       }
/* 146 */       this.options = options;
/* 147 */       this.nativeLibrary = NativeLibrary.getInstance(libname, options);
/* 148 */       this.invocationMapper = ((InvocationMapper)options.get("invocation-mapper"));
/*     */     }
/*     */     
/*     */     public NativeLibrary getNativeLibrary() {
/* 152 */       return this.nativeLibrary;
/*     */     }
/*     */     
/*     */     public String getLibraryName() {
/* 156 */       return this.nativeLibrary.getName();
/*     */     }
/*     */     
/*     */     public Class getInterfaceClass() {
/* 160 */       return this.interfaceClass;
/*     */     }
/*     */     
/*     */     private static final class FunctionInfo
/*     */     {
/*     */       final InvocationHandler handler;
/*     */       final Function function;
/*     */       final boolean isVarArgs;
/*     */       final Map options;
/*     */       final Class[] parameterTypes;
/*     */       
/*     */       FunctionInfo(InvocationHandler handler, Function function, Class[] parameterTypes, boolean isVarArgs, Map options) {
/* 172 */         this.handler = handler;
/* 173 */         this.function = function;
/* 174 */         this.isVarArgs = isVarArgs;
/* 175 */         this.options = options;
/* 176 */         this.parameterTypes = parameterTypes;
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
/*     */     public Object invoke(Object proxy, Method method, Object[] inArgs)
/*     */       throws Throwable
/*     */     {
/* 190 */       if (OBJECT_TOSTRING.equals(method)) {
/* 191 */         return "Proxy interface to " + this.nativeLibrary;
/*     */       }
/* 193 */       if (OBJECT_HASHCODE.equals(method)) {
/* 194 */         return new Integer(hashCode());
/*     */       }
/* 196 */       if (OBJECT_EQUALS.equals(method)) {
/* 197 */         Object o = inArgs[0];
/* 198 */         if ((o != null) && (Proxy.isProxyClass(o.getClass()))) {
/* 199 */           return Function.valueOf(Proxy.getInvocationHandler(o) == this);
/*     */         }
/* 201 */         return Boolean.FALSE;
/*     */       }
/*     */       
/*     */ 
/* 205 */       FunctionInfo f = (FunctionInfo)this.functions.get(method);
/* 206 */       if (f == null) {
/* 207 */         synchronized (this.functions) {
/* 208 */           f = (FunctionInfo)this.functions.get(method);
/* 209 */           if (f == null) {
/* 210 */             boolean isVarArgs = Function.isVarArgs(method);
/* 211 */             InvocationHandler handler = null;
/* 212 */             if (this.invocationMapper != null) {
/* 213 */               handler = this.invocationMapper.getInvocationHandler(this.nativeLibrary, method);
/*     */             }
/* 215 */             Function function = null;
/* 216 */             Class[] parameterTypes = null;
/* 217 */             Map options = null;
/* 218 */             if (handler == null)
/*     */             {
/* 220 */               function = this.nativeLibrary.getFunction(method.getName(), method);
/* 221 */               parameterTypes = method.getParameterTypes();
/* 222 */               options = new HashMap(this.options);
/* 223 */               options.put("invoking-method", method);
/*     */             }
/* 225 */             f = new FunctionInfo(handler, function, parameterTypes, isVarArgs, options);
/* 226 */             this.functions.put(method, f);
/*     */           }
/*     */         }
/*     */       }
/* 230 */       if (f.isVarArgs) {
/* 231 */         inArgs = Function.concatenateVarArgs(inArgs);
/*     */       }
/* 233 */       if (f.handler != null) {
/* 234 */         return f.handler.invoke(proxy, method, inArgs);
/*     */       }
/* 236 */       return f.function.invoke(method, f.parameterTypes, method.getReturnType(), inArgs, f.options);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\Library.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */