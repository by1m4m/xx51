/*     */ package oshi.hardware.platform.mac;
/*     */ 
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.PowerSource;
/*     */ import oshi.hardware.common.AbstractPowerSource;
/*     */ import oshi.jna.platform.mac.CoreFoundation;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFArrayRef;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFDictionaryRef;
/*     */ import oshi.jna.platform.mac.CoreFoundation.CFTypeRef;
/*     */ import oshi.jna.platform.mac.IOKit;
/*     */ import oshi.util.platform.mac.CfUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MacPowerSource
/*     */   extends AbstractPowerSource
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  48 */   private static final Logger LOG = LoggerFactory.getLogger(MacPowerSource.class);
/*     */   
/*     */   public MacPowerSource(String newName, double newRemainingCapacity, double newTimeRemaining) {
/*  51 */     super(newName, newRemainingCapacity, newTimeRemaining);
/*  52 */     LOG.debug("Initialized MacPowerSource");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PowerSource[] getPowerSources()
/*     */   {
/*  62 */     CoreFoundation.CFTypeRef powerSourcesInfo = IOKit.INSTANCE.IOPSCopyPowerSourcesInfo();
/*  63 */     CoreFoundation.CFArrayRef powerSourcesList = IOKit.INSTANCE.IOPSCopyPowerSourcesList(powerSourcesInfo);
/*  64 */     int powerSourcesCount = CoreFoundation.INSTANCE.CFArrayGetCount(powerSourcesList);
/*     */     
/*     */ 
/*     */ 
/*  68 */     double timeRemaining = IOKit.INSTANCE.IOPSGetTimeRemainingEstimate();
/*     */     
/*     */ 
/*  71 */     List<MacPowerSource> psList = new ArrayList(powerSourcesCount);
/*  72 */     for (int ps = 0; ps < powerSourcesCount; ps++)
/*     */     {
/*  74 */       CoreFoundation.CFTypeRef powerSource = CoreFoundation.INSTANCE.CFArrayGetValueAtIndex(powerSourcesList, ps);
/*  75 */       CoreFoundation.CFDictionaryRef dictionary = IOKit.INSTANCE.IOPSGetPowerSourceDescription(powerSourcesInfo, powerSource);
/*     */       
/*     */ 
/*     */ 
/*  79 */       boolean isPresent = false;
/*  80 */       Pointer isPresentRef = CoreFoundation.INSTANCE.CFDictionaryGetValue(dictionary, IOKit.IOPS_IS_PRESENT_KEY);
/*  81 */       if (isPresentRef != null) {
/*  82 */         isPresent = CoreFoundation.INSTANCE.CFBooleanGetValue(isPresentRef);
/*     */       }
/*  84 */       if (isPresent)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */         String name = CfUtil.cfPointerToString(CoreFoundation.INSTANCE.CFDictionaryGetValue(dictionary, IOKit.IOPS_NAME_KEY));
/*     */         
/*     */ 
/*  93 */         IntByReference currentCapacity = new IntByReference();
/*  94 */         if (!CoreFoundation.INSTANCE.CFDictionaryGetValueIfPresent(dictionary, IOKit.IOPS_CURRENT_CAPACITY_KEY, currentCapacity))
/*     */         {
/*  96 */           currentCapacity = new IntByReference(0);
/*     */         }
/*  98 */         IntByReference maxCapacity = new IntByReference();
/*  99 */         if (!CoreFoundation.INSTANCE.CFDictionaryGetValueIfPresent(dictionary, IOKit.IOPS_MAX_CAPACITY_KEY, maxCapacity))
/*     */         {
/* 101 */           maxCapacity = new IntByReference(1);
/*     */         }
/*     */         
/*     */ 
/* 105 */         psList.add(new MacPowerSource(name, currentCapacity.getValue() / maxCapacity.getValue(), timeRemaining));
/*     */       }
/*     */     }
/*     */     
/* 109 */     CoreFoundation.INSTANCE.CFRelease(powerSourcesInfo);
/*     */     
/* 111 */     return (PowerSource[])psList.toArray(new MacPowerSource[psList.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\mac\MacPowerSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */