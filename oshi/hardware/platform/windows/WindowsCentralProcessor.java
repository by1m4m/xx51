/*     */ package oshi.hardware.platform.windows;
/*     */ 
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.platform.win32.Advapi32Util;
/*     */ import com.sun.jna.platform.win32.Kernel32Util;
/*     */ import com.sun.jna.platform.win32.WinBase.FILETIME;
/*     */ import com.sun.jna.platform.win32.WinBase.SYSTEM_INFO;
/*     */ import com.sun.jna.platform.win32.WinBase.SYSTEM_INFO.PI;
/*     */ import com.sun.jna.platform.win32.WinBase.SYSTEM_INFO.UNION;
/*     */ import com.sun.jna.platform.win32.WinDef.DWORD;
/*     */ import com.sun.jna.platform.win32.WinDef.WORD;
/*     */ import com.sun.jna.platform.win32.WinNT.OSVERSIONINFO;
/*     */ import com.sun.jna.platform.win32.WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION;
/*     */ import com.sun.jna.platform.win32.WinReg;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.CentralProcessor.TickType;
/*     */ import oshi.hardware.common.AbstractCentralProcessor;
/*     */ import oshi.jna.platform.windows.Kernel32;
/*     */ import oshi.util.ParseUtil;
/*     */ import oshi.util.platform.windows.WmiUtil;
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
/*     */ public class WindowsCentralProcessor
/*     */   extends AbstractCentralProcessor
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  53 */   private static final Logger LOG = LoggerFactory.getLogger(WindowsCentralProcessor.class);
/*     */   private static final byte majorVersion;
/*     */   
/*     */   static
/*     */   {
/*  58 */     WinNT.OSVERSIONINFO lpVersionInfo = new WinNT.OSVERSIONINFO();
/*     */     
/*     */ 
/*  61 */     if (!Kernel32.INSTANCE.GetVersionEx(lpVersionInfo)) {
/*  62 */       majorVersion = lpVersionInfo.dwMajorVersion.byteValue();
/*     */     } else {
/*  64 */       majorVersion = 0;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WindowsCentralProcessor()
/*     */   {
/*  74 */     initVars();
/*     */     
/*  76 */     initTicks();
/*     */     
/*  78 */     LOG.debug("Initialized Processor");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void initVars()
/*     */   {
/*  85 */     String cpuRegistryRoot = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor";
/*  86 */     String[] processorIds = Advapi32Util.registryGetKeys(WinReg.HKEY_LOCAL_MACHINE, "HARDWARE\\DESCRIPTION\\System\\CentralProcessor");
/*  87 */     if (processorIds.length > 0) {
/*  88 */       String cpuRegistryPath = "HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\" + processorIds[0];
/*  89 */       setVendor(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, cpuRegistryPath, "VendorIdentifier"));
/*     */       
/*  91 */       setName(Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, cpuRegistryPath, "ProcessorNameString"));
/*     */       
/*  93 */       setIdentifier(
/*  94 */         Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, cpuRegistryPath, "Identifier"));
/*     */     }
/*  96 */     WinBase.SYSTEM_INFO sysinfo = new WinBase.SYSTEM_INFO();
/*  97 */     Kernel32.INSTANCE.GetNativeSystemInfo(sysinfo);
/*  98 */     if ((sysinfo.processorArchitecture.pi.wProcessorArchitecture.intValue() == 9) || 
/*  99 */       (sysinfo.processorArchitecture.pi.wProcessorArchitecture.intValue() == 6)) {
/* 100 */       setCpu64(true);
/* 101 */     } else if (sysinfo.processorArchitecture.pi.wProcessorArchitecture.intValue() == 0) {
/* 102 */       setCpu64(false);
/*     */     }
/* 104 */     setProcessorID(WmiUtil.selectStringFrom(null, "Win32_Processor", "ProcessorID", null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void calculateProcessorCounts()
/*     */   {
/* 113 */     WinBase.SYSTEM_INFO sysinfo = new WinBase.SYSTEM_INFO();
/* 114 */     Kernel32.INSTANCE.GetSystemInfo(sysinfo);
/* 115 */     this.logicalProcessorCount = sysinfo.dwNumberOfProcessors.intValue();
/*     */     
/*     */ 
/* 118 */     WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION[] processors = Kernel32Util.getLogicalProcessorInformation();
/* 119 */     for (WinNT.SYSTEM_LOGICAL_PROCESSOR_INFORMATION proc : processors) {
/* 120 */       if (proc.relationship == 0) {
/* 121 */         this.physicalProcessorCount += 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long[] getSystemCpuLoadTicks()
/*     */   {
/* 131 */     long[] ticks = new long[CentralProcessor.TickType.values().length];
/* 132 */     WinBase.FILETIME lpIdleTime = new WinBase.FILETIME();
/* 133 */     WinBase.FILETIME lpKernelTime = new WinBase.FILETIME();
/* 134 */     WinBase.FILETIME lpUserTime = new WinBase.FILETIME();
/* 135 */     if (!Kernel32.INSTANCE.GetSystemTimes(lpIdleTime, lpKernelTime, lpUserTime)) {
/* 136 */       LOG.error("Failed to update system idle/kernel/user times. Error code: " + Native.getLastError());
/* 137 */       return ticks;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 145 */     Map<String, List<String>> irq = WmiUtil.selectStringsFrom(null, "Win32_PerfRawData_Counters_ProcessorInformation", "PercentInterruptTime,PercentDPCTime", "WHERE Name=\"_Total\"");
/*     */     
/*     */ 
/* 148 */     if (!((List)irq.get("PercentInterruptTime")).isEmpty()) {
/* 149 */       ticks[CentralProcessor.TickType.IRQ.getIndex()] = (ParseUtil.parseLongOrDefault((String)((List)irq.get("PercentInterruptTime")).get(0), 0L) / 10000L);
/*     */       
/* 151 */       ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] = (ParseUtil.parseLongOrDefault((String)((List)irq.get("PercentDPCTime")).get(0), 0L) / 10000L);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 157 */     ticks[CentralProcessor.TickType.IDLE.getIndex()] = (WinBase.FILETIME.dateToFileTime(lpIdleTime.toDate()) / 10000L);
/* 158 */     ticks[CentralProcessor.TickType.SYSTEM.getIndex()] = 
/* 159 */       (WinBase.FILETIME.dateToFileTime(lpKernelTime.toDate()) / 10000L - ticks[CentralProcessor.TickType.IDLE.getIndex()]);
/* 160 */     ticks[CentralProcessor.TickType.USER.getIndex()] = (WinBase.FILETIME.dateToFileTime(lpUserTime.toDate()) / 10000L);
/*     */     
/* 162 */     ticks[CentralProcessor.TickType.SYSTEM.getIndex()] -= ticks[CentralProcessor.TickType.IRQ.getIndex()] + ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
/* 163 */     return ticks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[] getSystemLoadAverage(int nelem)
/*     */   {
/* 171 */     if ((nelem < 1) || (nelem > 3)) {
/* 172 */       throw new IllegalArgumentException("Must include from one to three elements.");
/*     */     }
/* 174 */     double[] average = new double[nelem];
/*     */     
/*     */ 
/* 177 */     for (int i = 0; i < average.length; i++) {
/* 178 */       average[i] = -1.0D;
/*     */     }
/* 180 */     return average;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long[][] getProcessorCpuLoadTicks()
/*     */   {
/* 188 */     long[][] ticks = new long[this.logicalProcessorCount][CentralProcessor.TickType.values().length];
/*     */     
/*     */ 
/* 191 */     Map<String, List<String>> wmiTicks = WmiUtil.selectStringsFrom(null, "Win32_PerfRawData_Counters_ProcessorInformation", "Name,PercentIdleTime,PercentPrivilegedTime,PercentUserTime,PercentInterruptTime,PercentDPCTime", "WHERE NOT Name LIKE \"%_Total\"");
/*     */     
/*     */ 
/*     */ 
/* 195 */     for (int index = 0; index < ((List)wmiTicks.get("Name")).size(); index++)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 200 */       for (int cpu = 0; cpu < this.logicalProcessorCount; cpu++) {
/* 201 */         String name = "0," + cpu;
/* 202 */         if (((String)((List)wmiTicks.get("Name")).get(index)).equals(name))
/*     */         {
/*     */ 
/* 205 */           ticks[cpu][CentralProcessor.TickType.USER.getIndex()] = (ParseUtil.parseLongOrDefault((String)((List)wmiTicks.get("PercentUserTime")).get(index), 0L) / 10000L);
/* 206 */           ticks[cpu][CentralProcessor.TickType.SYSTEM.getIndex()] = 
/* 207 */             (ParseUtil.parseLongOrDefault((String)((List)wmiTicks.get("PercentPrivilegedTime")).get(index), 0L) / 10000L);
/* 208 */           ticks[cpu][CentralProcessor.TickType.IDLE.getIndex()] = 
/* 209 */             (ParseUtil.parseLongOrDefault((String)((List)wmiTicks.get("PercentIdleTime")).get(index), 0L) / 10000L);
/* 210 */           ticks[cpu][CentralProcessor.TickType.IRQ.getIndex()] = 
/* 211 */             (ParseUtil.parseLongOrDefault((String)((List)wmiTicks.get("PercentInterruptTime")).get(index), 0L) / 10000L);
/* 212 */           ticks[cpu][CentralProcessor.TickType.SOFTIRQ.getIndex()] = 
/* 213 */             (ParseUtil.parseLongOrDefault((String)((List)wmiTicks.get("PercentDPCTime")).get(index), 0L) / 10000L); int 
/*     */           
/*     */ 
/* 216 */             tmp313_310 = CentralProcessor.TickType.SYSTEM.getIndex(); long[] tmp313_306 = ticks[cpu];tmp313_306[tmp313_310] = 
/* 217 */             (tmp313_306[tmp313_310] - (ticks[cpu][CentralProcessor.TickType.IRQ.getIndex()] + ticks[cpu][CentralProcessor.TickType.SOFTIRQ.getIndex()]));
/* 218 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 222 */     return ticks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getSystemUptime()
/*     */   {
/* 231 */     if (majorVersion >= 6) {
/* 232 */       return Kernel32.INSTANCE.GetTickCount64() / 1000L;
/*     */     }
/*     */     
/* 235 */     return Kernel32.INSTANCE.GetTickCount() / 1000L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String getSystemSerialNumber()
/*     */   {
/* 245 */     return new WindowsComputerSystem().getSerialNumber();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\windows\WindowsCentralProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */