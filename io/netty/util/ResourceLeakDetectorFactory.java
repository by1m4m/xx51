/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.SystemPropertyUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ResourceLeakDetectorFactory
/*     */ {
/*  33 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(ResourceLeakDetectorFactory.class);
/*     */   
/*  35 */   private static volatile ResourceLeakDetectorFactory factoryInstance = new DefaultResourceLeakDetectorFactory();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ResourceLeakDetectorFactory instance()
/*     */   {
/*  43 */     return factoryInstance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void setResourceLeakDetectorFactory(ResourceLeakDetectorFactory factory)
/*     */   {
/*  54 */     factoryInstance = (ResourceLeakDetectorFactory)ObjectUtil.checkNotNull(factory, "factory");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource)
/*     */   {
/*  65 */     return newResourceLeakDetector(resource, 128);
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
/*     */   @Deprecated
/*     */   public abstract <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> paramClass, int paramInt, long paramLong);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval)
/*     */   {
/*  93 */     return newResourceLeakDetector(resource, 128, Long.MAX_VALUE);
/*     */   }
/*     */   
/*     */   private static final class DefaultResourceLeakDetectorFactory extends ResourceLeakDetectorFactory
/*     */   {
/*     */     private final Constructor<?> obsoleteCustomClassConstructor;
/*     */     private final Constructor<?> customClassConstructor;
/*     */     
/*     */     DefaultResourceLeakDetectorFactory()
/*     */     {
/*     */       String customLeakDetector;
/*     */       try
/*     */       {
/* 106 */         customLeakDetector = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */ 
/* 109 */           public String run() { return SystemPropertyUtil.get("io.netty.customResourceLeakDetector"); }
/*     */         });
/*     */       } catch (Throwable cause) {
/*     */         String customLeakDetector;
/* 113 */         ResourceLeakDetectorFactory.logger.error("Could not access System property: io.netty.customResourceLeakDetector", cause);
/* 114 */         customLeakDetector = null;
/*     */       }
/* 116 */       if (customLeakDetector == null) {
/* 117 */         this.obsoleteCustomClassConstructor = (this.customClassConstructor = null);
/*     */       } else {
/* 119 */         this.obsoleteCustomClassConstructor = obsoleteCustomClassConstructor(customLeakDetector);
/* 120 */         this.customClassConstructor = customClassConstructor(customLeakDetector);
/*     */       }
/*     */     }
/*     */     
/*     */     private static Constructor<?> obsoleteCustomClassConstructor(String customLeakDetector) {
/*     */       try {
/* 126 */         Class<?> detectorClass = Class.forName(customLeakDetector, true, 
/* 127 */           PlatformDependent.getSystemClassLoader());
/*     */         
/* 129 */         if (ResourceLeakDetector.class.isAssignableFrom(detectorClass)) {
/* 130 */           return detectorClass.getConstructor(new Class[] { Class.class, Integer.TYPE, Long.TYPE });
/*     */         }
/* 132 */         ResourceLeakDetectorFactory.logger.error("Class {} does not inherit from ResourceLeakDetector.", customLeakDetector);
/*     */       }
/*     */       catch (Throwable t) {
/* 135 */         ResourceLeakDetectorFactory.logger.error("Could not load custom resource leak detector class provided: {}", customLeakDetector, t);
/*     */       }
/*     */       
/* 138 */       return null;
/*     */     }
/*     */     
/*     */     private static Constructor<?> customClassConstructor(String customLeakDetector) {
/*     */       try {
/* 143 */         Class<?> detectorClass = Class.forName(customLeakDetector, true, 
/* 144 */           PlatformDependent.getSystemClassLoader());
/*     */         
/* 146 */         if (ResourceLeakDetector.class.isAssignableFrom(detectorClass)) {
/* 147 */           return detectorClass.getConstructor(new Class[] { Class.class, Integer.TYPE });
/*     */         }
/* 149 */         ResourceLeakDetectorFactory.logger.error("Class {} does not inherit from ResourceLeakDetector.", customLeakDetector);
/*     */       }
/*     */       catch (Throwable t) {
/* 152 */         ResourceLeakDetectorFactory.logger.error("Could not load custom resource leak detector class provided: {}", customLeakDetector, t);
/*     */       }
/*     */       
/* 155 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval, long maxActive)
/*     */     {
/* 162 */       if (this.obsoleteCustomClassConstructor != null)
/*     */       {
/*     */         try
/*     */         {
/* 166 */           ResourceLeakDetector<T> leakDetector = (ResourceLeakDetector)this.obsoleteCustomClassConstructor.newInstance(new Object[] { resource, 
/* 167 */             Integer.valueOf(samplingInterval), Long.valueOf(maxActive) });
/* 168 */           ResourceLeakDetectorFactory.logger.debug("Loaded custom ResourceLeakDetector: {}", this.obsoleteCustomClassConstructor
/* 169 */             .getDeclaringClass().getName());
/* 170 */           return leakDetector;
/*     */         } catch (Throwable t) {
/* 172 */           ResourceLeakDetectorFactory.logger.error("Could not load custom resource leak detector provided: {} with the given resource: {}", new Object[] {this.obsoleteCustomClassConstructor
/*     */           
/* 174 */             .getDeclaringClass().getName(), resource, t });
/*     */         }
/*     */       }
/*     */       
/* 178 */       ResourceLeakDetector<T> resourceLeakDetector = new ResourceLeakDetector(resource, samplingInterval, maxActive);
/*     */       
/* 180 */       ResourceLeakDetectorFactory.logger.debug("Loaded default ResourceLeakDetector: {}", resourceLeakDetector);
/* 181 */       return resourceLeakDetector;
/*     */     }
/*     */     
/*     */     public <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval)
/*     */     {
/* 186 */       if (this.customClassConstructor != null)
/*     */       {
/*     */         try
/*     */         {
/* 190 */           ResourceLeakDetector<T> leakDetector = (ResourceLeakDetector)this.customClassConstructor.newInstance(new Object[] { resource, Integer.valueOf(samplingInterval) });
/* 191 */           ResourceLeakDetectorFactory.logger.debug("Loaded custom ResourceLeakDetector: {}", this.customClassConstructor
/* 192 */             .getDeclaringClass().getName());
/* 193 */           return leakDetector;
/*     */         } catch (Throwable t) {
/* 195 */           ResourceLeakDetectorFactory.logger.error("Could not load custom resource leak detector provided: {} with the given resource: {}", new Object[] {this.customClassConstructor
/*     */           
/* 197 */             .getDeclaringClass().getName(), resource, t });
/*     */         }
/*     */       }
/*     */       
/* 201 */       ResourceLeakDetector<T> resourceLeakDetector = new ResourceLeakDetector(resource, samplingInterval);
/* 202 */       ResourceLeakDetectorFactory.logger.debug("Loaded default ResourceLeakDetector: {}", resourceLeakDetector);
/* 203 */       return resourceLeakDetector;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\io\netty\util\ResourceLeakDetectorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */