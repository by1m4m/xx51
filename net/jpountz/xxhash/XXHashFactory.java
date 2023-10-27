/*     */ package net.jpountz.xxhash;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Random;
/*     */ import net.jpountz.util.Native;
/*     */ import net.jpountz.util.Utils;
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
/*     */ public final class XXHashFactory
/*     */ {
/*     */   private static XXHashFactory NATIVE_INSTANCE;
/*     */   private static XXHashFactory JAVA_UNSAFE_INSTANCE;
/*     */   private static XXHashFactory JAVA_SAFE_INSTANCE;
/*     */   private final String impl;
/*     */   private final XXHash32 hash32;
/*     */   private final XXHash64 hash64;
/*     */   private final StreamingXXHash32.Factory streamingHash32Factory;
/*     */   private final StreamingXXHash64.Factory streamingHash64Factory;
/*     */   
/*     */   private static XXHashFactory instance(String impl)
/*     */   {
/*     */     try
/*     */     {
/*  47 */       return new XXHashFactory(impl);
/*     */     } catch (Exception e) {
/*  49 */       throw new AssertionError(e);
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
/*     */   public static synchronized XXHashFactory nativeInstance()
/*     */   {
/*  79 */     if (NATIVE_INSTANCE == null) {
/*  80 */       NATIVE_INSTANCE = instance("JNI");
/*     */     }
/*  82 */     return NATIVE_INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized XXHashFactory safeInstance()
/*     */   {
/*  93 */     if (JAVA_SAFE_INSTANCE == null) {
/*  94 */       JAVA_SAFE_INSTANCE = instance("JavaSafe");
/*     */     }
/*  96 */     return JAVA_SAFE_INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized XXHashFactory unsafeInstance()
/*     */   {
/* 107 */     if (JAVA_UNSAFE_INSTANCE == null) {
/* 108 */       JAVA_UNSAFE_INSTANCE = instance("JavaUnsafe");
/*     */     }
/* 110 */     return JAVA_UNSAFE_INSTANCE;
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
/*     */   public static XXHashFactory fastestJavaInstance()
/*     */   {
/* 124 */     if (Utils.isUnalignedAccessAllowed()) {
/*     */       try {
/* 126 */         return unsafeInstance();
/*     */       } catch (Throwable t) {
/* 128 */         return safeInstance();
/*     */       }
/*     */     }
/* 131 */     return safeInstance();
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
/*     */   public static XXHashFactory fastestInstance()
/*     */   {
/* 148 */     if ((Native.isLoaded()) || (Native.class.getClassLoader() == ClassLoader.getSystemClassLoader())) {
/*     */       try
/*     */       {
/* 151 */         return nativeInstance();
/*     */       } catch (Throwable t) {
/* 153 */         return fastestJavaInstance();
/*     */       }
/*     */     }
/* 156 */     return fastestJavaInstance();
/*     */   }
/*     */   
/*     */   private static <T> T classInstance(String cls)
/*     */     throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException
/*     */   {
/* 162 */     ClassLoader loader = XXHashFactory.class.getClassLoader();
/* 163 */     loader = loader == null ? ClassLoader.getSystemClassLoader() : loader;
/* 164 */     Class<?> c = loader.loadClass(cls);
/* 165 */     Field f = c.getField("INSTANCE");
/* 166 */     return (T)f.get(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private XXHashFactory(String impl)
/*     */     throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException
/*     */   {
/* 176 */     this.impl = impl;
/* 177 */     this.hash32 = ((XXHash32)classInstance("net.jpountz.xxhash.XXHash32" + impl));
/* 178 */     this.streamingHash32Factory = ((StreamingXXHash32.Factory)classInstance("net.jpountz.xxhash.StreamingXXHash32" + impl + "$Factory"));
/* 179 */     this.hash64 = ((XXHash64)classInstance("net.jpountz.xxhash.XXHash64" + impl));
/* 180 */     this.streamingHash64Factory = ((StreamingXXHash64.Factory)classInstance("net.jpountz.xxhash.StreamingXXHash64" + impl + "$Factory"));
/*     */     
/*     */ 
/* 183 */     byte[] bytes = new byte[100];
/* 184 */     Random random = new Random();
/* 185 */     random.nextBytes(bytes);
/* 186 */     int seed = random.nextInt();
/*     */     
/* 188 */     int h1 = this.hash32.hash(bytes, 0, bytes.length, seed);
/* 189 */     StreamingXXHash32 streamingHash32 = newStreamingHash32(seed);
/* 190 */     streamingHash32.update(bytes, 0, bytes.length);
/* 191 */     int h2 = streamingHash32.getValue();
/* 192 */     long h3 = this.hash64.hash(bytes, 0, bytes.length, seed);
/* 193 */     StreamingXXHash64 streamingHash64 = newStreamingHash64(seed);
/* 194 */     streamingHash64.update(bytes, 0, bytes.length);
/* 195 */     long h4 = streamingHash64.getValue();
/* 196 */     if (h1 != h2) {
/* 197 */       throw new AssertionError();
/*     */     }
/* 199 */     if (h3 != h4) {
/* 200 */       throw new AssertionError();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public XXHash32 hash32()
/*     */   {
/* 210 */     return this.hash32;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public XXHash64 hash64()
/*     */   {
/* 219 */     return this.hash64;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StreamingXXHash32 newStreamingHash32(int seed)
/*     */   {
/* 229 */     return this.streamingHash32Factory.newStreamingHash(seed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StreamingXXHash64 newStreamingHash64(long seed)
/*     */   {
/* 239 */     return this.streamingHash64Factory.newStreamingHash(seed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 248 */     System.out.println("Fastest instance is " + fastestInstance());
/* 249 */     System.out.println("Fastest Java instance is " + fastestJavaInstance());
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 254 */     return getClass().getSimpleName() + ":" + this.impl;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\xxhash\XXHashFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */