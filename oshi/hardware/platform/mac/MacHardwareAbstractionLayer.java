/*     */ package oshi.hardware.platform.mac;
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
/*     */ public class MacHardwareAbstractionLayer
/*     */   extends AbstractHardwareAbstractionLayer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public ComputerSystem getComputerSystem()
/*     */   {
/*  41 */     if (this.computerSystem == null) {
/*  42 */       this.computerSystem = new MacComputerSystem();
/*     */     }
/*  44 */     return this.computerSystem;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CentralProcessor getProcessor()
/*     */   {
/*  52 */     if (this.processor == null) {
/*  53 */       this.processor = new MacCentralProcessor();
/*     */     }
/*  55 */     return this.processor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlobalMemory getMemory()
/*     */   {
/*  63 */     if (this.memory == null) {
/*  64 */       this.memory = new MacGlobalMemory();
/*     */     }
/*  66 */     return this.memory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PowerSource[] getPowerSources()
/*     */   {
/*  74 */     return MacPowerSource.getPowerSources();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HWDiskStore[] getDiskStores()
/*     */   {
/*  82 */     return new MacDisks().getDisks();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Display[] getDisplays()
/*     */   {
/*  90 */     return MacDisplay.getDisplays();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Sensors getSensors()
/*     */   {
/*  98 */     if (this.sensors == null) {
/*  99 */       this.sensors = new MacSensors();
/*     */     }
/* 101 */     return this.sensors;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NetworkIF[] getNetworkIFs()
/*     */   {
/* 109 */     return new MacNetworks().getNetworks();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UsbDevice[] getUsbDevices(boolean tree)
/*     */   {
/* 117 */     return MacUsbDevice.getUsbDevices(tree);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\mac\MacHardwareAbstractionLayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */