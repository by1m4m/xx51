/*     */ package net.jpountz.lz4;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Arrays;
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
/*     */ 
/*     */ 
/*     */ public final class LZ4Factory
/*     */ {
/*     */   private static LZ4Factory NATIVE_INSTANCE;
/*     */   private static LZ4Factory JAVA_UNSAFE_INSTANCE;
/*     */   private static LZ4Factory JAVA_SAFE_INSTANCE;
/*     */   private final String impl;
/*     */   private final LZ4Compressor fastCompressor;
/*     */   private final LZ4Compressor highCompressor;
/*     */   private final LZ4FastDecompressor fastDecompressor;
/*     */   private final LZ4SafeDecompressor safeDecompressor;
/*     */   
/*     */   private static LZ4Factory instance(String impl)
/*     */   {
/*     */     try
/*     */     {
/*  51 */       return new LZ4Factory(impl);
/*     */     } catch (Exception e) {
/*  53 */       throw new AssertionError(e);
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
/*     */   public static synchronized LZ4Factory nativeInstance()
/*     */   {
/*  83 */     if (NATIVE_INSTANCE == null) {
/*  84 */       NATIVE_INSTANCE = instance("JNI");
/*     */     }
/*  86 */     return NATIVE_INSTANCE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static synchronized LZ4Factory safeInstance()
/*     */   {
/*  97 */     if (JAVA_SAFE_INSTANCE == null) {
/*  98 */       JAVA_SAFE_INSTANCE = instance("JavaSafe");
/*     */     }
/* 100 */     return JAVA_SAFE_INSTANCE;
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
/*     */   public static synchronized LZ4Factory unsafeInstance()
/*     */   {
/* 113 */     if (JAVA_UNSAFE_INSTANCE == null) {
/* 114 */       JAVA_UNSAFE_INSTANCE = instance("JavaUnsafe");
/*     */     }
/* 116 */     return JAVA_UNSAFE_INSTANCE;
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
/*     */   public static LZ4Factory fastestJavaInstance()
/*     */   {
/* 130 */     if (Utils.isUnalignedAccessAllowed()) {
/*     */       try {
/* 132 */         return unsafeInstance();
/*     */       } catch (Throwable t) {
/* 134 */         return safeInstance();
/*     */       }
/*     */     }
/* 137 */     return safeInstance();
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
/*     */   public static LZ4Factory fastestInstance()
/*     */   {
/* 154 */     if ((Native.isLoaded()) || (Native.class.getClassLoader() == ClassLoader.getSystemClassLoader())) {
/*     */       try
/*     */       {
/* 157 */         return nativeInstance();
/*     */       } catch (Throwable t) {
/* 159 */         return fastestJavaInstance();
/*     */       }
/*     */     }
/* 162 */     return fastestJavaInstance();
/*     */   }
/*     */   
/*     */   private static <T> T classInstance(String cls)
/*     */     throws NoSuchFieldException, SecurityException, ClassNotFoundException, IllegalArgumentException, IllegalAccessException
/*     */   {
/* 168 */     ClassLoader loader = LZ4Factory.class.getClassLoader();
/* 169 */     loader = loader == null ? ClassLoader.getSystemClassLoader() : loader;
/* 170 */     Class<?> c = loader.loadClass(cls);
/* 171 */     Field f = c.getField("INSTANCE");
/* 172 */     return (T)f.get(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */   private final LZ4Compressor[] highCompressors = new LZ4Compressor[18];
/*     */   
/*     */   private LZ4Factory(String impl) throws ClassNotFoundException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, NoSuchMethodException, InstantiationException, InvocationTargetException {
/* 183 */     this.impl = impl;
/* 184 */     this.fastCompressor = ((LZ4Compressor)classInstance("net.jpountz.lz4.LZ4" + impl + "Compressor"));
/* 185 */     this.highCompressor = ((LZ4Compressor)classInstance("net.jpountz.lz4.LZ4HC" + impl + "Compressor"));
/* 186 */     this.fastDecompressor = ((LZ4FastDecompressor)classInstance("net.jpountz.lz4.LZ4" + impl + "FastDecompressor"));
/* 187 */     this.safeDecompressor = ((LZ4SafeDecompressor)classInstance("net.jpountz.lz4.LZ4" + impl + "SafeDecompressor"));
/* 188 */     Constructor<? extends LZ4Compressor> highConstructor = this.highCompressor.getClass().getDeclaredConstructor(new Class[] { Integer.TYPE });
/* 189 */     this.highCompressors[9] = this.highCompressor;
/* 190 */     for (int level = 1; level <= 17; level++) {
/* 191 */       if (level != 9) {
/* 192 */         this.highCompressors[level] = ((LZ4Compressor)highConstructor.newInstance(new Object[] { Integer.valueOf(level) }));
/*     */       }
/*     */     }
/*     */     
/* 196 */     byte[] original = { 97, 98, 99, 100, 32, 32, 32, 32, 32, 32, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106 };
/* 197 */     for (LZ4Compressor compressor : Arrays.asList(new LZ4Compressor[] { this.fastCompressor, this.highCompressor })) {
/* 198 */       int maxCompressedLength = compressor.maxCompressedLength(original.length);
/* 199 */       byte[] compressed = new byte[maxCompressedLength];
/* 200 */       int compressedLength = compressor.compress(original, 0, original.length, compressed, 0, maxCompressedLength);
/* 201 */       byte[] restored = new byte[original.length];
/* 202 */       this.fastDecompressor.decompress(compressed, 0, restored, 0, original.length);
/* 203 */       if (!Arrays.equals(original, restored)) {
/* 204 */         throw new AssertionError();
/*     */       }
/* 206 */       Arrays.fill(restored, (byte)0);
/* 207 */       int decompressedLength = this.safeDecompressor.decompress(compressed, 0, compressedLength, restored, 0);
/* 208 */       if ((decompressedLength != original.length) || (!Arrays.equals(original, restored))) {
/* 209 */         throw new AssertionError();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4Compressor fastCompressor()
/*     */   {
/* 221 */     return this.fastCompressor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4Compressor highCompressor()
/*     */   {
/* 232 */     return this.highCompressor;
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
/*     */   public LZ4Compressor highCompressor(int compressionLevel)
/*     */   {
/* 250 */     if (compressionLevel > 17) {
/* 251 */       compressionLevel = 17;
/* 252 */     } else if (compressionLevel < 1) {
/* 253 */       compressionLevel = 9;
/*     */     }
/* 255 */     return this.highCompressors[compressionLevel];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4FastDecompressor fastDecompressor()
/*     */   {
/* 264 */     return this.fastDecompressor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LZ4SafeDecompressor safeDecompressor()
/*     */   {
/* 273 */     return this.safeDecompressor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public LZ4UnknownSizeDecompressor unknownSizeDecompressor()
/*     */   {
/* 283 */     return safeDecompressor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public LZ4Decompressor decompressor()
/*     */   {
/* 293 */     return fastDecompressor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/* 302 */     System.out.println("Fastest instance is " + fastestInstance());
/* 303 */     System.out.println("Fastest Java instance is " + fastestJavaInstance());
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 308 */     return getClass().getSimpleName() + ":" + this.impl;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\net\jpountz\lz4\LZ4Factory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */