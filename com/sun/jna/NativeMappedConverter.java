/*    */ package com.sun.jna;
/*    */ 
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.util.Map;
/*    */ import java.util.WeakHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NativeMappedConverter
/*    */   implements TypeConverter
/*    */ {
/* 22 */   private static final Map converters = new WeakHashMap();
/*    */   private final Class type;
/*    */   private final Class nativeType;
/*    */   private final NativeMapped instance;
/*    */   
/*    */   public static NativeMappedConverter getInstance(Class cls) {
/* 28 */     synchronized (converters) {
/* 29 */       Reference r = (Reference)converters.get(cls);
/* 30 */       NativeMappedConverter nmc = r != null ? (NativeMappedConverter)r.get() : null;
/* 31 */       if (nmc == null) {
/* 32 */         nmc = new NativeMappedConverter(cls);
/* 33 */         converters.put(cls, new SoftReference(nmc));
/*    */       }
/* 35 */       return nmc;
/*    */     }
/*    */   }
/*    */   
/*    */   public NativeMappedConverter(Class type) {
/* 40 */     if (!NativeMapped.class.isAssignableFrom(type)) {
/* 41 */       throw new IllegalArgumentException("Type must derive from " + NativeMapped.class);
/*    */     }
/* 43 */     this.type = type;
/* 44 */     this.instance = defaultValue();
/* 45 */     this.nativeType = this.instance.nativeType();
/*    */   }
/*    */   
/*    */   public NativeMapped defaultValue() {
/*    */     try {
/* 50 */       return (NativeMapped)this.type.newInstance();
/*    */     }
/*    */     catch (InstantiationException e) {
/* 53 */       String msg = "Can't create an instance of " + this.type + ", requires a no-arg constructor: " + e;
/*    */       
/* 55 */       throw new IllegalArgumentException(msg);
/*    */     }
/*    */     catch (IllegalAccessException e) {
/* 58 */       String msg = "Not allowed to create an instance of " + this.type + ", requires a public, no-arg constructor: " + e;
/*    */       
/* 60 */       throw new IllegalArgumentException(msg);
/*    */     }
/*    */   }
/*    */   
/* 64 */   public Object fromNative(Object nativeValue, FromNativeContext context) { return this.instance.fromNative(nativeValue, context); }
/*    */   
/*    */   public Class nativeType()
/*    */   {
/* 68 */     return this.nativeType;
/*    */   }
/*    */   
/*    */   public Object toNative(Object value, ToNativeContext context) {
/* 72 */     if (value == null) {
/* 73 */       if (Pointer.class.isAssignableFrom(this.nativeType)) {
/* 74 */         return null;
/*    */       }
/* 76 */       value = defaultValue();
/*    */     }
/* 78 */     return ((NativeMapped)value).toNative();
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\NativeMappedConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */