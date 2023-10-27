/*     */ package oshi.hardware.common;
/*     */ 
/*     */ import oshi.hardware.Baseboard;
/*     */ import oshi.hardware.ComputerSystem;
/*     */ import oshi.hardware.Firmware;
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
/*     */ public abstract class AbstractComputerSystem
/*     */   implements ComputerSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String manufacturer;
/*     */   private String model;
/*     */   private String serialNumber;
/*     */   private Firmware firmware;
/*     */   private Baseboard baseboard;
/*     */   
/*     */   protected AbstractComputerSystem()
/*     */   {
/*  42 */     this.manufacturer = "unknown";
/*  43 */     this.model = "unknown";
/*  44 */     this.serialNumber = "unknown";
/*  45 */     this.firmware = null;
/*  46 */     this.baseboard = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getManufacturer()
/*     */   {
/*  54 */     return this.manufacturer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getModel()
/*     */   {
/*  62 */     return this.model;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSerialNumber()
/*     */   {
/*  70 */     return this.serialNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Firmware getFirmware()
/*     */   {
/*  78 */     return this.firmware;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Baseboard getBaseboard()
/*     */   {
/*  86 */     return this.baseboard;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setManufacturer(String manufacturer)
/*     */   {
/*  94 */     this.manufacturer = manufacturer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setModel(String model)
/*     */   {
/* 102 */     this.model = model;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setSerialNumber(String serialNumber)
/*     */   {
/* 110 */     this.serialNumber = serialNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setFirmware(Firmware firmware)
/*     */   {
/* 118 */     this.firmware = firmware;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setBaseboard(Baseboard baseboard)
/*     */   {
/* 126 */     this.baseboard = baseboard;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\common\AbstractComputerSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */