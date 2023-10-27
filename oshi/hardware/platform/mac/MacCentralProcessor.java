/*     */ package oshi.hardware.platform.mac;
/*     */ 
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.platform.mac.SystemB.HostCpuLoadInfo;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.CentralProcessor.TickType;
/*     */ import oshi.hardware.common.AbstractCentralProcessor;
/*     */ import oshi.jna.platform.mac.SystemB;
/*     */ import oshi.jna.platform.unix.CLibrary.Timeval;
/*     */ import oshi.util.FormatUtil;
/*     */ import oshi.util.ParseUtil;
/*     */ import oshi.util.platform.mac.SysctlUtil;
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
/*     */ public class MacCentralProcessor
/*     */   extends AbstractCentralProcessor
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  48 */   private static final Logger LOG = LoggerFactory.getLogger(MacCentralProcessor.class);
/*     */   private static final long BOOTTIME;
/*     */   
/*     */   static {
/*  52 */     CLibrary.Timeval tv = new CLibrary.Timeval();
/*  53 */     if ((!SysctlUtil.sysctl("kern.boottime", tv)) || (tv.tv_sec == 0L))
/*     */     {
/*     */ 
/*  56 */       BOOTTIME = ParseUtil.parseLongOrDefault(
/*  57 */         oshi.util.ExecutingCommand.getFirstAnswer("sysctl -n kern.boottime").split(",")[0].replaceAll("\\D", ""), 
/*  58 */         System.currentTimeMillis() / 1000L);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*  63 */       BOOTTIME = tv.tv_sec;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MacCentralProcessor()
/*     */   {
/*  73 */     initVars();
/*     */     
/*  75 */     initTicks();
/*     */     
/*  77 */     LOG.debug("Initialized Processor");
/*     */   }
/*     */   
/*     */   private void initVars() {
/*  81 */     setVendor(SysctlUtil.sysctl("machdep.cpu.vendor", ""));
/*  82 */     setName(SysctlUtil.sysctl("machdep.cpu.brand_string", ""));
/*  83 */     setCpu64(SysctlUtil.sysctl("hw.cpu64bit_capable", 0) != 0);
/*  84 */     int i = SysctlUtil.sysctl("machdep.cpu.stepping", -1);
/*  85 */     setStepping(i < 0 ? "" : Integer.toString(i));
/*  86 */     i = SysctlUtil.sysctl("machdep.cpu.model", -1);
/*  87 */     setModel(i < 0 ? "" : Integer.toString(i));
/*  88 */     i = SysctlUtil.sysctl("machdep.cpu.family", -1);
/*  89 */     setFamily(i < 0 ? "" : Integer.toString(i));
/*  90 */     long processorID = 0L;
/*  91 */     processorID |= SysctlUtil.sysctl("machdep.cpu.signature", 0);
/*  92 */     processorID |= (SysctlUtil.sysctl("machdep.cpu.feature_bits", 0L) & 0xFFFFFFFFFFFFFFFF) << 32;
/*  93 */     setProcessorID(String.format("%016X", new Object[] { Long.valueOf(processorID) }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void calculateProcessorCounts()
/*     */   {
/* 101 */     this.logicalProcessorCount = SysctlUtil.sysctl("hw.logicalcpu", 1);
/* 102 */     this.physicalProcessorCount = SysctlUtil.sysctl("hw.physicalcpu", 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long[] getSystemCpuLoadTicks()
/*     */   {
/* 110 */     long[] ticks = new long[CentralProcessor.TickType.values().length];
/* 111 */     int machPort = SystemB.INSTANCE.mach_host_self();
/* 112 */     SystemB.HostCpuLoadInfo cpuLoadInfo = new SystemB.HostCpuLoadInfo();
/* 113 */     if (0 != SystemB.INSTANCE.host_statistics(machPort, 3, cpuLoadInfo, new IntByReference(cpuLoadInfo
/* 114 */       .size()))) {
/* 115 */       LOG.error("Failed to get System CPU ticks. Error code: " + Native.getLastError());
/* 116 */       return ticks;
/*     */     }
/*     */     
/* 119 */     ticks[CentralProcessor.TickType.USER.getIndex()] = cpuLoadInfo.cpu_ticks[0];
/* 120 */     ticks[CentralProcessor.TickType.NICE.getIndex()] = cpuLoadInfo.cpu_ticks[3];
/* 121 */     ticks[CentralProcessor.TickType.SYSTEM.getIndex()] = cpuLoadInfo.cpu_ticks[1];
/* 122 */     ticks[CentralProcessor.TickType.IDLE.getIndex()] = cpuLoadInfo.cpu_ticks[2];
/*     */     
/* 124 */     return ticks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[] getSystemLoadAverage(int nelem)
/*     */   {
/* 132 */     if ((nelem < 1) || (nelem > 3)) {
/* 133 */       throw new IllegalArgumentException("Must include from one to three elements.");
/*     */     }
/* 135 */     double[] average = new double[nelem];
/* 136 */     int retval = SystemB.INSTANCE.getloadavg(average, nelem);
/* 137 */     if (retval < nelem) {
/* 138 */       for (int i = Math.max(retval, 0); i < average.length; i++) {
/* 139 */         average[i] = -1.0D;
/*     */       }
/*     */     }
/* 142 */     return average;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long[][] getProcessorCpuLoadTicks()
/*     */   {
/* 150 */     long[][] ticks = new long[this.logicalProcessorCount][CentralProcessor.TickType.values().length];
/*     */     
/* 152 */     int machPort = SystemB.INSTANCE.mach_host_self();
/*     */     
/* 154 */     IntByReference procCount = new IntByReference();
/* 155 */     PointerByReference procCpuLoadInfo = new PointerByReference();
/* 156 */     IntByReference procInfoCount = new IntByReference();
/* 157 */     if (0 != SystemB.INSTANCE.host_processor_info(machPort, 2, procCount, procCpuLoadInfo, procInfoCount))
/*     */     {
/* 159 */       LOG.error("Failed to update CPU Load. Error code: " + Native.getLastError());
/* 160 */       return ticks;
/*     */     }
/*     */     
/* 163 */     int[] cpuTicks = procCpuLoadInfo.getValue().getIntArray(0L, procInfoCount.getValue());
/* 164 */     for (int cpu = 0; cpu < procCount.getValue(); cpu++) {
/* 165 */       int offset = cpu * 4;
/* 166 */       ticks[cpu][CentralProcessor.TickType.USER.getIndex()] = FormatUtil.getUnsignedInt(cpuTicks[(offset + 0)]);
/* 167 */       ticks[cpu][CentralProcessor.TickType.NICE.getIndex()] = FormatUtil.getUnsignedInt(cpuTicks[(offset + 3)]);
/* 168 */       ticks[cpu][CentralProcessor.TickType.SYSTEM.getIndex()] = 
/* 169 */         FormatUtil.getUnsignedInt(cpuTicks[(offset + 1)]);
/* 170 */       ticks[cpu][CentralProcessor.TickType.IDLE.getIndex()] = FormatUtil.getUnsignedInt(cpuTicks[(offset + 2)]);
/*     */     }
/* 172 */     return ticks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getSystemUptime()
/*     */   {
/* 180 */     return System.currentTimeMillis() / 1000L - BOOTTIME;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String getSystemSerialNumber()
/*     */   {
/* 189 */     return new MacComputerSystem().getSerialNumber();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\mac\MacCentralProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */