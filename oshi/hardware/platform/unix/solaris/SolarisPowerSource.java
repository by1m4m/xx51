/*     */ package oshi.hardware.platform.unix.solaris;
/*     */ 
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.PowerSource;
/*     */ import oshi.hardware.common.AbstractPowerSource;
/*     */ import oshi.jna.platform.unix.solaris.LibKstat.Kstat;
/*     */ import oshi.util.platform.unix.solaris.KstatUtil;
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
/*     */ public class SolarisPowerSource
/*     */   extends AbstractPowerSource
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  38 */   private static final Logger LOG = LoggerFactory.getLogger(SolarisPowerSource.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  43 */   private static final String[] KSTAT_BATT_MOD = { null, "battery", "acpi_drv" };
/*     */   private static final int KSTAT_BATT_IDX;
/*     */   
/*     */   static
/*     */   {
/*  48 */     if (KstatUtil.kstatLookup(KSTAT_BATT_MOD[1], 0, null) != null) {
/*  49 */       KSTAT_BATT_IDX = 1;
/*  50 */     } else if (KstatUtil.kstatLookup(KSTAT_BATT_MOD[2], 0, null) != null) {
/*  51 */       KSTAT_BATT_IDX = 2;
/*     */     } else {
/*  53 */       KSTAT_BATT_IDX = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   public SolarisPowerSource(String newName, double newRemainingCapacity, double newTimeRemaining) {
/*  58 */     super(newName, newRemainingCapacity, newTimeRemaining);
/*  59 */     LOG.debug("Initialized SolarisPowerSource");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static PowerSource[] getPowerSources()
/*     */   {
/*  69 */     if (KSTAT_BATT_IDX == 0) {
/*  70 */       return new SolarisPowerSource[0];
/*     */     }
/*     */     
/*  73 */     LibKstat.Kstat ksp = KstatUtil.kstatLookup(KSTAT_BATT_MOD[KSTAT_BATT_IDX], 0, "battery BIF0");
/*  74 */     if (ksp == null) {
/*  75 */       return new SolarisPowerSource[0];
/*     */     }
/*     */     
/*     */ 
/*  79 */     long energyFull = KstatUtil.kstatDataLookupLong(ksp, "bif_last_cap");
/*  80 */     if ((energyFull == -1L) || (energyFull <= 0L)) {
/*  81 */       energyFull = KstatUtil.kstatDataLookupLong(ksp, "bif_design_cap");
/*     */     }
/*  83 */     if ((energyFull == -1L) || (energyFull <= 0L)) {
/*  84 */       return new SolarisPowerSource[0];
/*     */     }
/*     */     
/*     */ 
/*  88 */     ksp = KstatUtil.kstatLookup(KSTAT_BATT_MOD[KSTAT_BATT_IDX], 0, "battery BST0");
/*  89 */     if (ksp == null) {
/*  90 */       return new SolarisPowerSource[0];
/*     */     }
/*     */     
/*     */ 
/*  94 */     long energyNow = KstatUtil.kstatDataLookupLong(ksp, "bst_rem_cap");
/*  95 */     if (energyNow < 0L) {
/*  96 */       return new SolarisPowerSource[0];
/*     */     }
/*     */     
/*     */ 
/* 100 */     long powerNow = KstatUtil.kstatDataLookupLong(ksp, "bst_rate");
/* 101 */     if (powerNow == -1L) {
/* 102 */       powerNow = 0L;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */     boolean isCharging = (KstatUtil.kstatDataLookupLong(ksp, "bst_state") & 0x10) > 0L;
/*     */     
/*     */ 
/* 112 */     SolarisPowerSource[] ps = new SolarisPowerSource[1];
/* 113 */     ps[0] = new SolarisPowerSource("BAT0", energyNow / energyFull, powerNow > 0L ? 3600.0D * energyNow / powerNow : isCharging ? -2.0D : -1.0D);
/*     */     
/* 115 */     return ps;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\solaris\SolarisPowerSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */