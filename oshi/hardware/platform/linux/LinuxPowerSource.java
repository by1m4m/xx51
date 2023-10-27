/*     */ package oshi.hardware.platform.linux;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.PowerSource;
/*     */ import oshi.hardware.common.AbstractPowerSource;
/*     */ import oshi.util.FileUtil;
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
/*     */ public class LinuxPowerSource
/*     */   extends AbstractPowerSource
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  41 */   private static final Logger LOG = LoggerFactory.getLogger(LinuxPowerSource.class);
/*     */   private static final String PS_PATH = "/sys/class/power_supply/";
/*     */   
/*     */   public LinuxPowerSource(String newName, double newRemainingCapacity, double newTimeRemaining)
/*     */   {
/*  46 */     super(newName, newRemainingCapacity, newTimeRemaining);
/*  47 */     LOG.debug("Initialized LinuxPowerSource");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PowerSource[] getPowerSources()
/*     */   {
/*  57 */     File f = new File("/sys/class/power_supply/");
/*  58 */     String[] psNames = f.list();
/*     */     
/*  60 */     if (psNames == null) {
/*  61 */       psNames = new String[0];
/*     */     }
/*  63 */     List<LinuxPowerSource> psList = new ArrayList(psNames.length);
/*     */     label455:
/*  65 */     for (String psName : psNames)
/*     */     {
/*  67 */       if ((!psName.startsWith("ADP")) && (!psName.startsWith("AC")))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*  72 */         List<String> psInfo = FileUtil.readFile("/sys/class/power_supply/" + psName + "/uevent", false);
/*  73 */         if (!psInfo.isEmpty())
/*     */         {
/*     */ 
/*     */ 
/*  77 */           boolean isPresent = false;
/*  78 */           boolean isCharging = false;
/*  79 */           String name = "Unknown";
/*  80 */           int energyNow = 0;
/*  81 */           int energyFull = 1;
/*  82 */           int powerNow = 1;
/*  83 */           for (String checkLine : psInfo) {
/*  84 */             if (checkLine.startsWith("POWER_SUPPLY_PRESENT"))
/*     */             {
/*  86 */               String[] psSplit = checkLine.split("=");
/*  87 */               if (psSplit.length > 1) {
/*  88 */                 isPresent = Integer.parseInt(psSplit[1]) > 0;
/*     */               }
/*  90 */               if (isPresent) {
/*     */                 break label455;
/*     */               }
/*  93 */             } else if (checkLine.startsWith("POWER_SUPPLY_NAME"))
/*     */             {
/*  95 */               String[] psSplit = checkLine.split("=");
/*  96 */               if (psSplit.length > 1) {
/*  97 */                 name = psSplit[1];
/*     */               }
/*  99 */             } else if ((checkLine.startsWith("POWER_SUPPLY_ENERGY_NOW")) || 
/* 100 */               (checkLine.startsWith("POWER_SUPPLY_CHARGE_NOW")))
/*     */             {
/* 102 */               String[] psSplit = checkLine.split("=");
/* 103 */               if (psSplit.length > 1) {
/* 104 */                 energyNow = Integer.parseInt(psSplit[1]);
/*     */               }
/* 106 */             } else if ((checkLine.startsWith("POWER_SUPPLY_ENERGY_FULL")) || 
/* 107 */               (checkLine.startsWith("POWER_SUPPLY_CHARGE_FULL"))) {
/* 108 */               String[] psSplit = checkLine.split("=");
/* 109 */               if (psSplit.length > 1) {
/* 110 */                 energyFull = Integer.parseInt(psSplit[1]);
/*     */               }
/* 112 */             } else if (checkLine.startsWith("POWER_SUPPLY_STATUS"))
/*     */             {
/* 114 */               String[] psSplit = checkLine.split("=");
/* 115 */               if ((psSplit.length > 1) && ("Charging".equals(psSplit[1]))) {
/* 116 */                 isCharging = true;
/*     */               }
/* 118 */             } else if ((checkLine.startsWith("POWER_SUPPLY_POWER_NOW")) || 
/* 119 */               (checkLine.startsWith("POWER_SUPPLY_CURRENT_NOW")))
/*     */             {
/* 121 */               String[] psSplit = checkLine.split("=");
/* 122 */               if (psSplit.length > 1) {
/* 123 */                 powerNow = Integer.parseInt(psSplit[1]);
/*     */               }
/* 125 */               if (powerNow <= 0) {
/* 126 */                 isCharging = true;
/*     */               }
/*     */             }
/*     */           }
/* 130 */           psList.add(new LinuxPowerSource(name, energyNow / energyFull, isCharging ? -2.0D : 3600.0D * energyNow / powerNow));
/*     */         }
/*     */       }
/*     */     }
/* 134 */     return (PowerSource[])psList.toArray(new LinuxPowerSource[psList.size()]);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\linux\LinuxPowerSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */