/*     */ package com.sun.jna;
/*     */ 
/*     */ import com.sun.jna.win32.DLLCallback;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ class CallbackReference
/*     */   extends WeakReference
/*     */ {
/*  39 */   static final Map callbackMap = new WeakHashMap();
/*  40 */   static final Map directCallbackMap = new WeakHashMap();
/*  41 */   static final Map pointerCallbackMap = new WeakHashMap();
/*  42 */   static final Map allocations = new WeakHashMap();
/*  43 */   private static final Map allocatedMemory = Collections.synchronizedMap(new WeakHashMap());
/*     */   private static final Method PROXY_CALLBACK_METHOD;
/*     */   
/*     */   static
/*     */   {
/*     */     try {
/*  49 */       PROXY_CALLBACK_METHOD = CallbackProxy.class.getMethod("callback", new Class[] { Object[].class });
/*     */     }
/*     */     catch (Exception e) {
/*  52 */       throw new Error("Error looking up CallbackProxy.callback() method");
/*     */     }
/*     */   }
/*     */   
/*  56 */   private static final Map initializers = new WeakHashMap();
/*     */   
/*  58 */   static void setCallbackThreadInitializer(Callback cb, CallbackThreadInitializer initializer) { synchronized (callbackMap) {
/*  59 */       if (initializer != null) {
/*  60 */         initializers.put(cb, initializer);
/*     */       }
/*     */       else
/*  63 */         initializers.remove(cb);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AttachOptions
/*     */     extends Structure {
/*     */     public boolean daemon;
/*     */     public boolean detach;
/*     */     public String name;
/*     */     
/*  73 */     AttachOptions() { setStringEncoding("utf8"); }
/*     */     
/*  75 */     protected List getFieldOrder() { return Arrays.asList(new String[] { "daemon", "detach", "name" }); }
/*     */   }
/*     */   
/*     */ 
/*     */   private static ThreadGroup initializeThread(Callback cb, AttachOptions args)
/*     */   {
/*  81 */     CallbackThreadInitializer init = null;
/*  82 */     if ((cb instanceof DefaultCallbackProxy)) {
/*  83 */       cb = ((DefaultCallbackProxy)cb).getCallback();
/*     */     }
/*  85 */     synchronized (callbackMap) {
/*  86 */       init = (CallbackThreadInitializer)initializers.get(cb);
/*     */     }
/*  88 */     ThreadGroup group = null;
/*  89 */     if (init != null) {
/*  90 */       group = init.getThreadGroup(cb);
/*  91 */       args.name = init.getName(cb);
/*  92 */       args.daemon = init.isDaemon(cb);
/*  93 */       args.detach = init.detach(cb);
/*  94 */       args.write();
/*     */     }
/*  96 */     return group;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   Pointer cbstruct;
/*     */   
/*     */ 
/*     */   public static Callback getCallback(Class type, Pointer p)
/*     */   {
/* 107 */     return getCallback(type, p, false);
/*     */   }
/*     */   
/*     */   private static Callback getCallback(Class type, Pointer p, boolean direct) {
/* 111 */     if (p == null) {
/* 112 */       return null;
/*     */     }
/*     */     
/* 115 */     if (!type.isInterface())
/* 116 */       throw new IllegalArgumentException("Callback type must be an interface");
/* 117 */     Map map = direct ? directCallbackMap : callbackMap;
/* 118 */     synchronized (callbackMap) {
/* 119 */       Callback cb = null;
/* 120 */       Reference ref = (Reference)pointerCallbackMap.get(p);
/* 121 */       if (ref != null) {
/* 122 */         cb = (Callback)ref.get();
/* 123 */         if ((cb != null) && (!type.isAssignableFrom(cb.getClass()))) {
/* 124 */           throw new IllegalStateException("Pointer " + p + " already mapped to " + cb + ".\nNative code may be re-using a default function pointer" + ", in which case you may need to use a common Callback class" + " wherever the function pointer is reused.");
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 129 */         return cb;
/*     */       }
/* 131 */       int ctype = AltCallingConvention.class.isAssignableFrom(type) ? 63 : 0;
/*     */       
/* 133 */       Map foptions = new HashMap(Native.getLibraryOptions(type));
/* 134 */       foptions.put("invoking-method", getCallbackMethod(type));
/* 135 */       NativeFunctionHandler h = new NativeFunctionHandler(p, ctype, foptions);
/* 136 */       cb = (Callback)Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, h);
/*     */       
/* 138 */       map.put(cb, null);
/* 139 */       pointerCallbackMap.put(p, new WeakReference(cb));
/* 140 */       return cb;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private CallbackReference(Callback callback, int callingConvention, boolean direct)
/*     */   {
/* 150 */     super(callback);
/* 151 */     TypeMapper mapper = Native.getTypeMapper(callback.getClass());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 157 */     boolean ppc = Platform.isPPC();
/* 158 */     if (direct) {
/* 159 */       Method m = getCallbackMethod(callback);
/* 160 */       Class[] ptypes = m.getParameterTypes();
/* 161 */       for (int i = 0; i < ptypes.length; i++)
/*     */       {
/* 163 */         if ((ppc) && ((ptypes[i] == Float.TYPE) || (ptypes[i] == Double.TYPE)))
/*     */         {
/* 165 */           direct = false;
/* 166 */           break;
/*     */         }
/*     */         
/* 169 */         if ((mapper != null) && 
/* 170 */           (mapper.getFromNativeConverter(ptypes[i]) != null)) {
/* 171 */           direct = false;
/* 172 */           break;
/*     */         }
/*     */       }
/*     */       
/* 176 */       if ((mapper != null) && 
/* 177 */         (mapper.getToNativeConverter(m.getReturnType()) != null)) {
/* 178 */         direct = false;
/*     */       }
/*     */     }
/*     */     
/* 182 */     String encoding = Native.getStringEncoding(callback.getClass());
/* 183 */     if (direct) {
/* 184 */       this.method = getCallbackMethod(callback);
/* 185 */       Class[] nativeParamTypes = this.method.getParameterTypes();
/* 186 */       Class returnType = this.method.getReturnType();
/* 187 */       int flags = 1;
/* 188 */       if ((callback instanceof DLLCallback)) {
/* 189 */         flags |= 0x2;
/*     */       }
/* 191 */       long peer = Native.createNativeCallback(callback, this.method, nativeParamTypes, returnType, callingConvention, flags, encoding);
/*     */       
/*     */ 
/*     */ 
/* 195 */       this.cbstruct = (peer != 0L ? new Pointer(peer) : null);
/* 196 */       allocatedMemory.put(this, new WeakReference(this));
/*     */     }
/*     */     else {
/* 199 */       if ((callback instanceof CallbackProxy)) {
/* 200 */         this.proxy = ((CallbackProxy)callback);
/*     */       }
/*     */       else {
/* 203 */         this.proxy = new DefaultCallbackProxy(getCallbackMethod(callback), mapper, encoding);
/*     */       }
/* 205 */       Class[] nativeParamTypes = this.proxy.getParameterTypes();
/* 206 */       Class returnType = this.proxy.getReturnType();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 211 */       if (mapper != null) {
/* 212 */         for (int i = 0; i < nativeParamTypes.length; i++) {
/* 213 */           FromNativeConverter rc = mapper.getFromNativeConverter(nativeParamTypes[i]);
/* 214 */           if (rc != null) {
/* 215 */             nativeParamTypes[i] = rc.nativeType();
/*     */           }
/*     */         }
/* 218 */         ToNativeConverter tn = mapper.getToNativeConverter(returnType);
/* 219 */         if (tn != null) {
/* 220 */           returnType = tn.nativeType();
/*     */         }
/*     */       }
/* 223 */       for (int i = 0; i < nativeParamTypes.length; i++) {
/* 224 */         nativeParamTypes[i] = getNativeType(nativeParamTypes[i]);
/* 225 */         if (!isAllowableNativeType(nativeParamTypes[i])) {
/* 226 */           String msg = "Callback argument " + nativeParamTypes[i] + " requires custom type conversion";
/*     */           
/* 228 */           throw new IllegalArgumentException(msg);
/*     */         }
/*     */       }
/* 231 */       returnType = getNativeType(returnType);
/* 232 */       if (!isAllowableNativeType(returnType)) {
/* 233 */         String msg = "Callback return type " + returnType + " requires custom type conversion";
/*     */         
/* 235 */         throw new IllegalArgumentException(msg);
/*     */       }
/* 237 */       int flags = (callback instanceof DLLCallback) ? 2 : 0;
/*     */       
/* 239 */       long peer = Native.createNativeCallback(this.proxy, PROXY_CALLBACK_METHOD, nativeParamTypes, returnType, callingConvention, flags, encoding);
/*     */       
/*     */ 
/*     */ 
/* 243 */       this.cbstruct = (peer != 0L ? new Pointer(peer) : null);
/*     */     }
/*     */   }
/*     */   
/*     */   private Class getNativeType(Class cls) {
/* 248 */     if (Structure.class.isAssignableFrom(cls))
/*     */     {
/* 250 */       Structure.validate(cls);
/* 251 */       if (!Structure.ByValue.class.isAssignableFrom(cls))
/* 252 */         return Pointer.class;
/*     */     } else {
/* 254 */       if (NativeMapped.class.isAssignableFrom(cls)) {
/* 255 */         return NativeMappedConverter.getInstance(cls).nativeType();
/*     */       }
/* 257 */       if ((cls == String.class) || (cls == WString.class) || (cls == String[].class) || (cls == WString[].class) || 
/*     */       
/*     */ 
/*     */ 
/* 261 */         (Callback.class.isAssignableFrom(cls)))
/* 262 */         return Pointer.class;
/*     */     }
/* 264 */     return cls;
/*     */   }
/*     */   
/*     */   private static Method checkMethod(Method m) {
/* 268 */     if (m.getParameterTypes().length > 256) {
/* 269 */       String msg = "Method signature exceeds the maximum parameter count: " + m;
/*     */       
/* 271 */       throw new UnsupportedOperationException(msg);
/*     */     }
/* 273 */     return m;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static Class findCallbackClass(Class type)
/*     */   {
/* 281 */     if (!Callback.class.isAssignableFrom(type)) {
/* 282 */       throw new IllegalArgumentException(type.getName() + " is not derived from com.sun.jna.Callback");
/*     */     }
/* 284 */     if (type.isInterface()) {
/* 285 */       return type;
/*     */     }
/* 287 */     Class[] ifaces = type.getInterfaces();
/* 288 */     for (int i = 0; i < ifaces.length; i++) {
/* 289 */       if (Callback.class.isAssignableFrom(ifaces[i])) {
/*     */         try
/*     */         {
/* 292 */           getCallbackMethod(ifaces[i]);
/* 293 */           return ifaces[i];
/*     */         }
/*     */         catch (IllegalArgumentException e) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 300 */     if (Callback.class.isAssignableFrom(type.getSuperclass())) {
/* 301 */       return findCallbackClass(type.getSuperclass());
/*     */     }
/* 303 */     return type;
/*     */   }
/*     */   
/*     */   private static Method getCallbackMethod(Callback callback) {
/* 307 */     return getCallbackMethod(findCallbackClass(callback.getClass()));
/*     */   }
/*     */   
/*     */   private static Method getCallbackMethod(Class cls)
/*     */   {
/* 312 */     Method[] pubMethods = cls.getDeclaredMethods();
/* 313 */     Method[] classMethods = cls.getMethods();
/* 314 */     Set pmethods = new HashSet(Arrays.asList(pubMethods));
/* 315 */     pmethods.retainAll(Arrays.asList(classMethods));
/*     */     
/*     */ 
/* 318 */     for (Iterator i = pmethods.iterator(); i.hasNext();) {
/* 319 */       Method m = (Method)i.next();
/* 320 */       if (Callback.FORBIDDEN_NAMES.contains(m.getName())) {
/* 321 */         i.remove();
/*     */       }
/*     */     }
/* 324 */     Method[] methods = (Method[])pmethods.toArray(new Method[pmethods.size()]);
/* 325 */     if (methods.length == 1) {
/* 326 */       return checkMethod(methods[0]);
/*     */     }
/* 328 */     for (int i = 0; i < methods.length; i++) {
/* 329 */       Method m = methods[i];
/* 330 */       if ("callback".equals(m.getName())) {
/* 331 */         return checkMethod(m);
/*     */       }
/*     */     }
/* 334 */     String msg = "Callback must implement a single public method, or one public method named 'callback'";
/*     */     
/* 336 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */   
/*     */   private void setCallbackOptions(int options)
/*     */   {
/* 341 */     this.cbstruct.setInt(Pointer.SIZE, options);
/*     */   }
/*     */   
/*     */   public Pointer getTrampoline()
/*     */   {
/* 346 */     if (this.trampoline == null) {
/* 347 */       this.trampoline = this.cbstruct.getPointer(0L);
/*     */     }
/* 349 */     return this.trampoline;
/*     */   }
/*     */   
/*     */   protected void finalize()
/*     */   {
/* 354 */     dispose();
/*     */   }
/*     */   
/*     */   protected synchronized void dispose()
/*     */   {
/* 359 */     if (this.cbstruct != null) {
/* 360 */       Native.freeNativeCallback(this.cbstruct.peer);
/* 361 */       this.cbstruct.peer = 0L;
/* 362 */       this.cbstruct = null;
/* 363 */       allocatedMemory.remove(this);
/*     */     }
/*     */   }
/*     */   
/*     */   static void disposeAll()
/*     */   {
/* 369 */     for (Iterator i = allocatedMemory.keySet().iterator(); i.hasNext();) {
/* 370 */       ((Memory)i.next()).dispose();
/*     */     }
/*     */   }
/*     */   
/*     */   private Callback getCallback() {
/* 375 */     return (Callback)get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Pointer getNativeFunctionPointer(Callback cb)
/*     */   {
/* 382 */     if (Proxy.isProxyClass(cb.getClass())) {
/* 383 */       Object handler = Proxy.getInvocationHandler(cb);
/* 384 */       if ((handler instanceof NativeFunctionHandler)) {
/* 385 */         return ((NativeFunctionHandler)handler).getPointer();
/*     */       }
/*     */     }
/* 388 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Pointer getFunctionPointer(Callback cb)
/*     */   {
/* 395 */     return getFunctionPointer(cb, false);
/*     */   }
/*     */   
/*     */   private static Pointer getFunctionPointer(Callback cb, boolean direct)
/*     */   {
/* 400 */     Pointer fp = null;
/* 401 */     if (cb == null) {
/* 402 */       return null;
/*     */     }
/* 404 */     if ((fp = getNativeFunctionPointer(cb)) != null) {
/* 405 */       return fp;
/*     */     }
/* 407 */     int callingConvention = (cb instanceof AltCallingConvention) ? 63 : 0;
/*     */     
/* 409 */     Map map = direct ? directCallbackMap : callbackMap;
/* 410 */     synchronized (callbackMap) {
/* 411 */       CallbackReference cbref = (CallbackReference)map.get(cb);
/* 412 */       if (cbref == null) {
/* 413 */         cbref = new CallbackReference(cb, callingConvention, direct);
/* 414 */         map.put(cb, cbref);
/* 415 */         pointerCallbackMap.put(cbref.getTrampoline(), new WeakReference(cb));
/* 416 */         if (initializers.containsKey(cb)) {
/* 417 */           cbref.setCallbackOptions(1);
/*     */         }
/*     */       }
/* 420 */       return cbref.getTrampoline();
/*     */     }
/*     */   }
/*     */   
/*     */   private class DefaultCallbackProxy implements CallbackProxy {
/*     */     private final Method callbackMethod;
/*     */     private ToNativeConverter toNative;
/*     */     private final FromNativeConverter[] fromNative;
/*     */     private final String encoding;
/*     */     
/* 430 */     public DefaultCallbackProxy(Method callbackMethod, TypeMapper mapper, String encoding) { this.callbackMethod = callbackMethod;
/* 431 */       this.encoding = encoding;
/* 432 */       Class[] argTypes = callbackMethod.getParameterTypes();
/* 433 */       Class returnType = callbackMethod.getReturnType();
/* 434 */       this.fromNative = new FromNativeConverter[argTypes.length];
/* 435 */       if (NativeMapped.class.isAssignableFrom(returnType)) {
/* 436 */         this.toNative = NativeMappedConverter.getInstance(returnType);
/*     */       }
/* 438 */       else if (mapper != null) {
/* 439 */         this.toNative = mapper.getToNativeConverter(returnType);
/*     */       }
/* 441 */       for (int i = 0; i < this.fromNative.length; i++) {
/* 442 */         if (NativeMapped.class.isAssignableFrom(argTypes[i])) {
/* 443 */           this.fromNative[i] = new NativeMappedConverter(argTypes[i]);
/*     */         }
/* 445 */         else if (mapper != null) {
/* 446 */           this.fromNative[i] = mapper.getFromNativeConverter(argTypes[i]);
/*     */         }
/*     */       }
/* 449 */       if (!callbackMethod.isAccessible()) {
/*     */         try {
/* 451 */           callbackMethod.setAccessible(true);
/*     */         }
/*     */         catch (SecurityException e) {
/* 454 */           throw new IllegalArgumentException("Callback method is inaccessible, make sure the interface is public: " + callbackMethod);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public Callback getCallback() {
/* 460 */       return CallbackReference.this.getCallback();
/*     */     }
/*     */     
/*     */     private Object invokeCallback(Object[] args) {
/* 464 */       Class[] paramTypes = this.callbackMethod.getParameterTypes();
/* 465 */       Object[] callbackArgs = new Object[args.length];
/*     */       
/*     */ 
/* 468 */       for (int i = 0; i < args.length; i++) {
/* 469 */         Class type = paramTypes[i];
/* 470 */         Object arg = args[i];
/* 471 */         if (this.fromNative[i] != null) {
/* 472 */           FromNativeContext context = new CallbackParameterContext(type, this.callbackMethod, args, i);
/*     */           
/* 474 */           callbackArgs[i] = this.fromNative[i].fromNative(arg, context);
/*     */         }
/*     */         else {
/* 477 */           callbackArgs[i] = convertArgument(arg, type);
/*     */         }
/*     */       }
/*     */       
/* 481 */       Object result = null;
/* 482 */       Callback cb = getCallback();
/* 483 */       if (cb != null) {
/*     */         try {
/* 485 */           result = convertResult(this.callbackMethod.invoke(cb, callbackArgs));
/*     */         }
/*     */         catch (IllegalArgumentException e) {
/* 488 */           Native.getCallbackExceptionHandler().uncaughtException(cb, e);
/*     */         }
/*     */         catch (IllegalAccessException e) {
/* 491 */           Native.getCallbackExceptionHandler().uncaughtException(cb, e);
/*     */         }
/*     */         catch (InvocationTargetException e) {
/* 494 */           Native.getCallbackExceptionHandler().uncaughtException(cb, e.getTargetException());
/*     */         }
/*     */       }
/*     */       
/* 498 */       for (int i = 0; i < callbackArgs.length; i++) {
/* 499 */         if (((callbackArgs[i] instanceof Structure)) && (!(callbackArgs[i] instanceof Structure.ByValue)))
/*     */         {
/* 501 */           ((Structure)callbackArgs[i]).autoWrite();
/*     */         }
/*     */       }
/*     */       
/* 505 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public Object callback(Object[] args)
/*     */     {
/*     */       try
/*     */       {
/* 515 */         return invokeCallback(args);
/*     */       }
/*     */       catch (Throwable t) {
/* 518 */         Native.getCallbackExceptionHandler().uncaughtException(getCallback(), t); }
/* 519 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private Object convertArgument(Object value, Class dstType)
/*     */     {
/* 527 */       if ((value instanceof Pointer)) {
/* 528 */         if (dstType == String.class) {
/* 529 */           value = ((Pointer)value).getString(0L, this.encoding);
/*     */         }
/* 531 */         else if (dstType == WString.class) {
/* 532 */           value = new WString(((Pointer)value).getWideString(0L));
/*     */         }
/* 534 */         else if (dstType == String[].class) {
/* 535 */           value = ((Pointer)value).getStringArray(0L, this.encoding);
/*     */         }
/* 537 */         else if (dstType == WString[].class) {
/* 538 */           value = ((Pointer)value).getWideStringArray(0L);
/*     */         }
/* 540 */         else if (Callback.class.isAssignableFrom(dstType)) {
/* 541 */           value = CallbackReference.getCallback(dstType, (Pointer)value);
/*     */         }
/* 543 */         else if (Structure.class.isAssignableFrom(dstType))
/*     */         {
/*     */ 
/* 546 */           if (Structure.ByValue.class.isAssignableFrom(dstType)) {
/* 547 */             Structure s = Structure.newInstance(dstType);
/* 548 */             byte[] buf = new byte[s.size()];
/* 549 */             ((Pointer)value).read(0L, buf, 0, buf.length);
/* 550 */             s.getPointer().write(0L, buf, 0, buf.length);
/* 551 */             s.read();
/* 552 */             value = s;
/*     */           }
/*     */           else {
/* 555 */             Structure s = Structure.newInstance(dstType, (Pointer)value);
/* 556 */             s.conditionalAutoRead();
/* 557 */             value = s;
/*     */           }
/*     */         }
/*     */       }
/* 561 */       else if (((Boolean.TYPE == dstType) || (Boolean.class == dstType)) && ((value instanceof Number)))
/*     */       {
/* 563 */         value = Function.valueOf(((Number)value).intValue() != 0);
/*     */       }
/* 565 */       return value;
/*     */     }
/*     */     
/*     */     private Object convertResult(Object value) {
/* 569 */       if (this.toNative != null) {
/* 570 */         value = this.toNative.toNative(value, new CallbackResultContext(this.callbackMethod));
/*     */       }
/* 572 */       if (value == null)
/* 573 */         return null;
/* 574 */       Class cls = value.getClass();
/* 575 */       if (Structure.class.isAssignableFrom(cls)) {
/* 576 */         if (Structure.ByValue.class.isAssignableFrom(cls)) {
/* 577 */           return value;
/*     */         }
/* 579 */         return ((Structure)value).getPointer();
/*     */       }
/* 581 */       if ((cls == Boolean.TYPE) || (cls == Boolean.class)) {
/* 582 */         return Boolean.TRUE.equals(value) ? Function.INTEGER_TRUE : Function.INTEGER_FALSE;
/*     */       }
/*     */       
/* 585 */       if ((cls == String.class) || (cls == WString.class)) {
/* 586 */         return CallbackReference.getNativeString(value, cls == WString.class);
/*     */       }
/* 588 */       if ((cls == String[].class) || (cls == WString.class)) {
/* 589 */         StringArray sa = cls == String[].class ? new StringArray((String[])value, this.encoding) : new StringArray((WString[])value);
/*     */         
/*     */ 
/*     */ 
/* 593 */         CallbackReference.allocations.put(value, sa);
/* 594 */         return sa;
/*     */       }
/* 596 */       if (Callback.class.isAssignableFrom(cls)) {
/* 597 */         return CallbackReference.getFunctionPointer((Callback)value);
/*     */       }
/* 599 */       return value;
/*     */     }
/*     */     
/* 602 */     public Class[] getParameterTypes() { return this.callbackMethod.getParameterTypes(); }
/*     */     
/*     */     public Class getReturnType() {
/* 605 */       return this.callbackMethod.getReturnType();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class NativeFunctionHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private final Function function;
/*     */     private final Map options;
/*     */     
/*     */     public NativeFunctionHandler(Pointer address, int callingConvention, Map options)
/*     */     {
/* 618 */       String encoding = (String)options.get("string-encoding");
/* 619 */       this.function = new Function(address, callingConvention, encoding);
/* 620 */       this.options = options;
/*     */     }
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*     */     {
/* 625 */       if (Library.Handler.OBJECT_TOSTRING.equals(method)) {
/* 626 */         String str = "Proxy interface to " + this.function;
/* 627 */         Method m = (Method)this.options.get("invoking-method");
/* 628 */         Class cls = CallbackReference.findCallbackClass(m.getDeclaringClass());
/* 629 */         str = str + " (" + cls.getName() + ")";
/*     */         
/* 631 */         return str;
/*     */       }
/* 633 */       if (Library.Handler.OBJECT_HASHCODE.equals(method)) {
/* 634 */         return new Integer(hashCode());
/*     */       }
/* 636 */       if (Library.Handler.OBJECT_EQUALS.equals(method)) {
/* 637 */         Object o = args[0];
/* 638 */         if ((o != null) && (Proxy.isProxyClass(o.getClass()))) {
/* 639 */           return Function.valueOf(Proxy.getInvocationHandler(o) == this);
/*     */         }
/* 641 */         return Boolean.FALSE;
/*     */       }
/* 643 */       if (Function.isVarArgs(method)) {
/* 644 */         args = Function.concatenateVarArgs(args);
/*     */       }
/* 646 */       return this.function.invoke(method.getReturnType(), args, this.options);
/*     */     }
/*     */     
/*     */     public Pointer getPointer() {
/* 650 */       return this.function;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   Pointer trampoline;
/*     */   
/*     */ 
/*     */ 
/*     */   CallbackProxy proxy;
/*     */   
/*     */ 
/*     */   Method method;
/*     */   
/*     */ 
/*     */   private static boolean isAllowableNativeType(Class cls)
/*     */   {
/* 669 */     return (cls == Void.TYPE) || (cls == Void.class) || (cls == Boolean.TYPE) || (cls == Boolean.class) || (cls == Byte.TYPE) || (cls == Byte.class) || (cls == Short.TYPE) || (cls == Short.class) || (cls == Character.TYPE) || (cls == Character.class) || (cls == Integer.TYPE) || (cls == Integer.class) || (cls == Long.TYPE) || (cls == Long.class) || (cls == Float.TYPE) || (cls == Float.class) || (cls == Double.TYPE) || (cls == Double.class) || ((Structure.ByValue.class.isAssignableFrom(cls)) && (Structure.class.isAssignableFrom(cls))) || (Pointer.class.isAssignableFrom(cls));
/*     */   }
/*     */   
/*     */   private static Pointer getNativeString(Object value, boolean wide) {
/* 673 */     if (value != null) {
/* 674 */       NativeString ns = new NativeString(value.toString(), wide);
/*     */       
/* 676 */       allocations.put(value, ns);
/* 677 */       return ns.getPointer();
/*     */     }
/* 679 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\CallbackReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */