/*     */ package oshi.hardware.platform.unix.solaris;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.CentralProcessor.TickType;
/*     */ import oshi.hardware.common.AbstractCentralProcessor;
/*     */ import oshi.jna.platform.linux.Libc;
/*     */ import oshi.jna.platform.unix.solaris.LibKstat.Kstat;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.ParseUtil;
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
/*     */ public class SolarisCentralProcessor
/*     */   extends AbstractCentralProcessor
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  44 */   private static final Logger LOG = LoggerFactory.getLogger(SolarisCentralProcessor.class);
/*     */   
/*  46 */   private static final Pattern PSRINFO = Pattern.compile(".*physical processor has (\\d+) virtual processors.*");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SolarisCentralProcessor()
/*     */   {
/*  54 */     initVars();
/*     */     
/*  56 */     initTicks();
/*     */     
/*  58 */     LOG.debug("Initialized Processor");
/*     */   }
/*     */   
/*     */   private void initVars()
/*     */   {
/*  63 */     LibKstat.Kstat ksp = KstatUtil.kstatLookup("cpu_info", -1, null);
/*     */     
/*  65 */     if ((ksp != null) && (KstatUtil.kstatRead(ksp))) {
/*  66 */       setVendor(KstatUtil.kstatDataLookupString(ksp, "vendor_id"));
/*  67 */       setName(KstatUtil.kstatDataLookupString(ksp, "brand"));
/*  68 */       setStepping(KstatUtil.kstatDataLookupString(ksp, "stepping"));
/*  69 */       setModel(KstatUtil.kstatDataLookupString(ksp, "model"));
/*  70 */       setFamily(KstatUtil.kstatDataLookupString(ksp, "family"));
/*     */     }
/*  72 */     setCpu64("64".equals(ExecutingCommand.getFirstAnswer("isainfo -b").trim()));
/*  73 */     setProcessorID(getProcessorID(getStepping(), getModel(), getFamily()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void calculateProcessorCounts()
/*     */   {
/*  82 */     this.logicalProcessorCount = 0;
/*  83 */     this.physicalProcessorCount = 0;
/*     */     
/*  85 */     for (String cpu : ExecutingCommand.runNative("psrinfo -pv")) {
/*  86 */       Matcher m = PSRINFO.matcher(cpu.trim());
/*  87 */       if (m.matches()) {
/*  88 */         this.physicalProcessorCount += 1;
/*  89 */         this.logicalProcessorCount += ParseUtil.parseIntOrDefault(m.group(1), 0);
/*     */       }
/*     */     }
/*  92 */     if (this.logicalProcessorCount < 1) {
/*  93 */       LOG.error("Couldn't find logical processor count. Assuming 1.");
/*  94 */       this.logicalProcessorCount = 1;
/*     */     }
/*  96 */     if (this.physicalProcessorCount < 1) {
/*  97 */       LOG.error("Couldn't find physical processor count. Assuming 1.");
/*  98 */       this.physicalProcessorCount = 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized long[] getSystemCpuLoadTicks()
/*     */   {
/* 107 */     long[] ticks = new long[CentralProcessor.TickType.values().length];
/*     */     
/* 109 */     long[][] procTicks = getProcessorCpuLoadTicks();
/* 110 */     for (int i = 0; i < ticks.length; i++) {
/* 111 */       for (long[] procTick : procTicks) {
/* 112 */         ticks[i] += procTick[i];
/*     */       }
/* 114 */       ticks[i] /= procTicks.length;
/*     */     }
/* 116 */     return ticks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[] getSystemLoadAverage(int nelem)
/*     */   {
/* 124 */     if ((nelem < 1) || (nelem > 3)) {
/* 125 */       throw new IllegalArgumentException("Must include from one to three elements.");
/*     */     }
/* 127 */     double[] average = new double[nelem];
/* 128 */     int retval = Libc.INSTANCE.getloadavg(average, nelem);
/* 129 */     if (retval < nelem) {
/* 130 */       for (int i = Math.max(retval, 0); i < average.length; i++) {
/* 131 */         average[i] = -1.0D;
/*     */       }
/*     */     }
/* 134 */     return average;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long[][] getProcessorCpuLoadTicks()
/*     */   {
/* 142 */     long[][] ticks = new long[this.logicalProcessorCount][CentralProcessor.TickType.values().length];
/* 143 */     int cpu = -1;
/* 144 */     for (LibKstat.Kstat ksp : KstatUtil.kstatLookupAll("cpu", -1, "sys"))
/*     */     {
/* 146 */       cpu++; if (cpu >= ticks.length) {
/*     */         break;
/*     */       }
/*     */       
/* 150 */       if (KstatUtil.kstatRead(ksp)) {
/* 151 */         ticks[cpu][CentralProcessor.TickType.IDLE.getIndex()] = KstatUtil.kstatDataLookupLong(ksp, "cpu_ticks_idle");
/* 152 */         ticks[cpu][CentralProcessor.TickType.SYSTEM.getIndex()] = KstatUtil.kstatDataLookupLong(ksp, "cpu_ticks_kernel");
/* 153 */         ticks[cpu][CentralProcessor.TickType.USER.getIndex()] = KstatUtil.kstatDataLookupLong(ksp, "cpu_ticks_user");
/*     */       }
/*     */     }
/* 156 */     return ticks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getSystemUptime()
/*     */   {
/* 164 */     LibKstat.Kstat ksp = KstatUtil.kstatLookup("unix", 0, "system_misc");
/* 165 */     if (ksp == null) {
/* 166 */       return 0L;
/*     */     }
/*     */     
/* 169 */     return ksp.ks_snaptime / 1000000000L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String getSystemSerialNumber()
/*     */   {
/* 178 */     return new SolarisComputerSystem().getSerialNumber();
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
/*     */   private String getProcessorID(String stepping, String model, String family)
/*     */   {
/* 191 */     List<String> isainfo = ExecutingCommand.runNative("isainfo -v");
/* 192 */     StringBuilder flags = new StringBuilder();
/* 193 */     for (String line : isainfo)
/* 194 */       if (!line.startsWith("64-bit"))
/*     */       {
/* 196 */         if (line.startsWith("32-bit")) {
/*     */           break;
/*     */         }
/* 199 */         flags.append(' ').append(line.trim());
/*     */       }
/* 201 */     return createProcessorID(stepping, model, family, flags.toString().toLowerCase().split("\\s+"));
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\solaris\SolarisCentralProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */