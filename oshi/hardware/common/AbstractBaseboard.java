/*     */ package oshi.hardware.common;
/*     */ 
/*     */ import oshi.hardware.Baseboard;
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
/*     */ public abstract class AbstractBaseboard
/*     */   implements Baseboard
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String manufacturer;
/*     */   private String model;
/*     */   private String version;
/*     */   private String serialNumber;
/*     */   
/*     */   public AbstractBaseboard()
/*     */   {
/*  38 */     this.manufacturer = "unknown";
/*  39 */     this.model = "unknown";
/*  40 */     this.version = "unknown";
/*  41 */     this.serialNumber = "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getManufacturer()
/*     */   {
/*  49 */     return this.manufacturer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getModel()
/*     */   {
/*  57 */     return this.model;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getVersion()
/*     */   {
/*  65 */     return this.version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSerialNumber()
/*     */   {
/*  73 */     return this.serialNumber;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setManufacturer(String manufacturer)
/*     */   {
/*  81 */     this.manufacturer = manufacturer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setModel(String model)
/*     */   {
/*  89 */     this.model = model;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVersion(String version)
/*     */   {
/*  97 */     this.version = version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSerialNumber(String serialNumber)
/*     */   {
/* 105 */     this.serialNumber = serialNumber;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\common\AbstractBaseboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */