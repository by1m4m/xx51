/*     */ package oshi.hardware.platform.unix.solaris;
/*     */ 
/*     */ import oshi.hardware.CentralProcessor;
/*     */ import oshi.hardware.ComputerSystem;
/*     */ import oshi.hardware.Display;
/*     */ import oshi.hardware.GlobalMemory;
/*     */ import oshi.hardware.HWDiskStore;
/*     */ import oshi.hardware.NetworkIF;
/*     */ import oshi.hardware.PowerSource;
/*     */ import oshi.hardware.Sensors;
/*     */ import oshi.hardware.UsbDevice;
/*     */ import oshi.hardware.common.AbstractHardwareAbstractionLayer;
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
/*     */ public class SolarisHardwareAbstractionLayer
/*     */   extends AbstractHardwareAbstractionLayer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public ComputerSystem getComputerSystem()
/*     */   {
/*  41 */     if (this.computerSystem == null) {
/*  42 */       this.computerSystem = new SolarisComputerSystem();
/*     */     }
/*  44 */     return this.computerSystem;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlobalMemory getMemory()
/*     */   {
/*  52 */     if (this.memory == null) {
/*  53 */       this.memory = new SolarisGlobalMemory();
/*     */     }
/*  55 */     return this.memory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CentralProcessor getProcessor()
/*     */   {
/*  63 */     if (this.processor == null) {
/*  64 */       this.processor = new SolarisCentralProcessor();
/*     */     }
/*  66 */     return this.processor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PowerSource[] getPowerSources()
/*     */   {
/*  74 */     return SolarisPowerSource.getPowerSources();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HWDiskStore[] getDiskStores()
/*     */   {
/*  82 */     return new SolarisDisks().getDisks();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Display[] getDisplays()
/*     */   {
/*  90 */     return SolarisDisplay.getDisplays();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Sensors getSensors()
/*     */   {
/*  98 */     if (this.sensors == null) {
/*  99 */       this.sensors = new SolarisSensors();
/*     */     }
/* 101 */     return this.sensors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NetworkIF[] getNetworkIFs()
/*     */   {
/* 109 */     return new SolarisNetworks().getNetworks();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UsbDevice[] getUsbDevices(boolean tree)
/*     */   {
/* 117 */     return SolarisUsbDevice.getUsbDevices(tree);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\solaris\SolarisHardwareAbstractionLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */