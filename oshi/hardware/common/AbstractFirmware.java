/*     */ package oshi.hardware.common;
/*     */ 
/*     */ import java.time.LocalDate;
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
/*     */ 
/*     */ public abstract class AbstractFirmware
/*     */   implements Firmware
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private String manufacturer;
/*     */   private String name;
/*     */   private String description;
/*     */   private String version;
/*     */   private LocalDate releaseDate;
/*     */   
/*     */   public AbstractFirmware()
/*     */   {
/*  42 */     this.manufacturer = "unknown";
/*  43 */     this.name = "unknown";
/*  44 */     this.description = "unknown";
/*  45 */     this.version = "unknown";
/*  46 */     this.releaseDate = null;
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
/*     */   public String getName()
/*     */   {
/*  62 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDescription()
/*     */   {
/*  70 */     return this.description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getVersion()
/*     */   {
/*  78 */     return this.version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public LocalDate getReleaseDate()
/*     */   {
/*  86 */     return this.releaseDate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setManufacturer(String manufacturer)
/*     */   {
/*  94 */     this.manufacturer = manufacturer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/* 102 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDescription(String description)
/*     */   {
/* 110 */     this.description = description;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVersion(String version)
/*     */   {
/* 118 */     this.version = version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReleaseDate(LocalDate releaseDate)
/*     */   {
/* 126 */     this.releaseDate = releaseDate;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\common\AbstractFirmware.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */