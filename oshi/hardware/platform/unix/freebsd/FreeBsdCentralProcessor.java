/*     */ package oshi.hardware.platform.unix.freebsd;
/*     */ 
/*     */ import com.sun.jna.Memory;
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.CentralProcessor.TickType;
/*     */ import oshi.hardware.common.AbstractCentralProcessor;
/*     */ import oshi.jna.platform.unix.CLibrary.Timeval;
/*     */ import oshi.jna.platform.unix.freebsd.Libc;
/*     */ import oshi.jna.platform.unix.freebsd.Libc.CpTime;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.FileUtil;
/*     */ import oshi.util.ParseUtil;
/*     */ import oshi.util.platform.unix.freebsd.BsdSysctlUtil;
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
/*     */ public class FreeBsdCentralProcessor
/*     */   extends AbstractCentralProcessor
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  51 */   private static final Logger LOG = LoggerFactory.getLogger(FreeBsdCentralProcessor.class);
/*     */   
/*  53 */   private static final Pattern CPUMASK = Pattern.compile(".*<cpu\\s.*mask=\"(?:0x)?(\\p{XDigit}+)\".*>.*</cpu>.*");
/*     */   
/*     */ 
/*  56 */   private static final Pattern CPUINFO = Pattern.compile("Origin=\"([^\"]*)\".*Id=(\\S+).*Family=(\\S+).*Model=(\\S+).*Stepping=(\\S+).*");
/*  57 */   private static final Pattern CPUINFO2 = Pattern.compile("Features=(\\S+)<.*");
/*     */   private static final long BOOTTIME;
/*     */   
/*     */   static {
/*  61 */     CLibrary.Timeval tv = new CLibrary.Timeval();
/*  62 */     if ((!BsdSysctlUtil.sysctl("kern.boottime", tv)) || (tv.tv_sec == 0L))
/*     */     {
/*     */ 
/*  65 */       BOOTTIME = ParseUtil.parseLongOrDefault(
/*  66 */         ExecutingCommand.getFirstAnswer("sysctl -n kern.boottime").split(",")[0].replaceAll("\\D", ""), 
/*  67 */         System.currentTimeMillis() / 1000L);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*  72 */       BOOTTIME = tv.tv_sec;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FreeBsdCentralProcessor()
/*     */   {
/*  82 */     initVars();
/*     */     
/*  84 */     initTicks();
/*     */     
/*  86 */     LOG.debug("Initialized Processor");
/*     */   }
/*     */   
/*     */   private void initVars() {
/*  90 */     setName(BsdSysctlUtil.sysctl("hw.model", ""));
/*     */     
/*     */ 
/*  93 */     long processorID = 0L;
/*  94 */     List<String> cpuInfo = FileUtil.readFile("/var/run/dmesg.boot");
/*  95 */     for (String line : cpuInfo) {
/*  96 */       line = line.trim();
/*     */       
/*  98 */       if ((line.startsWith("CPU:")) && (getName().isEmpty())) {
/*  99 */         setName(line.replace("CPU:", "").trim());
/* 100 */       } else if (line.startsWith("Origin=")) {
/* 101 */         Matcher m = CPUINFO.matcher(line);
/* 102 */         if (m.matches()) {
/* 103 */           setVendor(m.group(1));
/* 104 */           processorID |= Long.decode(m.group(2)).longValue();
/* 105 */           setFamily(Integer.decode(m.group(3)).toString());
/* 106 */           setModel(Integer.decode(m.group(4)).toString());
/* 107 */           setStepping(Integer.decode(m.group(5)).toString());
/*     */         }
/* 109 */       } else if (line.startsWith("Features=")) {
/* 110 */         Matcher m = CPUINFO2.matcher(line);
/* 111 */         if (!m.matches()) break;
/* 112 */         processorID |= Long.decode(m.group(1)).longValue() << 32; break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 118 */     setCpu64(ExecutingCommand.getFirstAnswer("uname -m").trim().contains("64"));
/* 119 */     setProcessorID(getProcessorID(processorID));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void calculateProcessorCounts()
/*     */   {
/* 127 */     String[] topology = BsdSysctlUtil.sysctl("kern.sched.topology_spec", "").split("\\n|\\r");
/* 128 */     long physMask = 0L;
/* 129 */     long virtMask = 0L;
/* 130 */     long lastMask = 0L;
/* 131 */     for (String topo : topology) {
/* 132 */       if (topo.contains("<cpu"))
/*     */       {
/* 134 */         Matcher m = CPUMASK.matcher(topo);
/* 135 */         if (m.matches())
/*     */         {
/* 137 */           lastMask = Long.parseLong(m.group(1), 16);
/* 138 */           physMask |= lastMask;
/* 139 */           virtMask |= lastMask;
/*     */         }
/* 141 */       } else if ((topo.contains("<flags>")) && (
/* 142 */         (topo.contains("HTT")) || (topo.contains("SMT")) || (topo.contains("THREAD"))))
/*     */       {
/* 144 */         physMask &= (lastMask ^ 0xFFFFFFFFFFFFFFFF);
/*     */       }
/*     */     }
/*     */     
/* 148 */     this.logicalProcessorCount = Long.bitCount(virtMask);
/* 149 */     this.physicalProcessorCount = Long.bitCount(physMask);
/*     */     
/* 151 */     if (this.logicalProcessorCount < 1) {
/* 152 */       LOG.error("Couldn't find logical processor count. Assuming 1.");
/* 153 */       this.logicalProcessorCount = 1;
/*     */     }
/* 155 */     if (this.physicalProcessorCount < 1) {
/* 156 */       LOG.error("Couldn't find physical processor count. Assuming 1.");
/* 157 */       this.physicalProcessorCount = 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized long[] getSystemCpuLoadTicks()
/*     */   {
/* 166 */     long[] ticks = new long[CentralProcessor.TickType.values().length];
/* 167 */     Libc.CpTime cpTime = new Libc.CpTime();
/* 168 */     BsdSysctlUtil.sysctl("kern.cp_time", cpTime);
/* 169 */     ticks[CentralProcessor.TickType.USER.getIndex()] = cpTime.cpu_ticks[0];
/* 170 */     ticks[CentralProcessor.TickType.NICE.getIndex()] = cpTime.cpu_ticks[1];
/* 171 */     ticks[CentralProcessor.TickType.SYSTEM.getIndex()] = cpTime.cpu_ticks[2];
/* 172 */     ticks[CentralProcessor.TickType.IRQ.getIndex()] = cpTime.cpu_ticks[3];
/* 173 */     ticks[CentralProcessor.TickType.IDLE.getIndex()] = cpTime.cpu_ticks[4];
/* 174 */     return ticks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[] getSystemLoadAverage(int nelem)
/*     */   {
/* 182 */     if ((nelem < 1) || (nelem > 3)) {
/* 183 */       throw new IllegalArgumentException("Must include from one to three elements.");
/*     */     }
/* 185 */     double[] average = new double[nelem];
/* 186 */     int retval = Libc.INSTANCE.getloadavg(average, nelem);
/* 187 */     if (retval < nelem) {
/* 188 */       for (int i = Math.max(retval, 0); i < average.length; i++) {
/* 189 */         average[i] = -1.0D;
/*     */       }
/*     */     }
/* 192 */     return average;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long[][] getProcessorCpuLoadTicks()
/*     */   {
/* 200 */     long[][] ticks = new long[this.logicalProcessorCount][CentralProcessor.TickType.values().length];
/*     */     
/*     */ 
/* 203 */     int offset = new Libc.CpTime().size();
/* 204 */     int size = offset * this.logicalProcessorCount;
/* 205 */     Pointer p = new Memory(size);
/* 206 */     String name = "kern.cp_times";
/*     */     
/* 208 */     if (0 != Libc.INSTANCE.sysctlbyname(name, p, new IntByReference(size), null, 0)) {
/* 209 */       LOG.error("Failed syctl call: {}, Error code: {}", name, Integer.valueOf(Native.getLastError()));
/* 210 */       return ticks;
/*     */     }
/*     */     
/* 213 */     for (int cpu = 0; cpu < this.logicalProcessorCount; cpu++) {
/* 214 */       ticks[cpu][CentralProcessor.TickType.USER.getIndex()] = p.getLong(offset * cpu + 0 * Libc.UINT64_SIZE);
/* 215 */       ticks[cpu][CentralProcessor.TickType.NICE.getIndex()] = p.getLong(offset * cpu + 1 * Libc.UINT64_SIZE);
/* 216 */       ticks[cpu][CentralProcessor.TickType.SYSTEM.getIndex()] = p.getLong(offset * cpu + 2 * Libc.UINT64_SIZE);
/* 217 */       ticks[cpu][CentralProcessor.TickType.IRQ.getIndex()] = p.getLong(offset * cpu + 3 * Libc.UINT64_SIZE);
/* 218 */       ticks[cpu][CentralProcessor.TickType.IDLE.getIndex()] = p.getLong(offset * cpu + 4 * Libc.UINT64_SIZE);
/*     */     }
/* 220 */     return ticks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getSystemUptime()
/*     */   {
/* 228 */     return System.currentTimeMillis() / 1000L - BOOTTIME;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String getSystemSerialNumber()
/*     */   {
/* 237 */     return new FreeBsdComputerSystem().getSerialNumber();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getProcessorID(long processorID)
/*     */   {
/* 248 */     boolean procInfo = false;
/* 249 */     String marker = "Processor Information";
/* 250 */     for (String checkLine : ExecutingCommand.runNative("dmidecode -t system")) {
/* 251 */       if ((!procInfo) && (checkLine.contains(marker))) {
/* 252 */         marker = "ID:";
/* 253 */         procInfo = true;
/* 254 */       } else if ((procInfo) && (checkLine.contains(marker))) {
/* 255 */         return checkLine.split(marker)[1].trim();
/*     */       }
/*     */     }
/*     */     
/* 259 */     return String.format("%016X", new Object[] { Long.valueOf(processorID) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\unix\freebsd\FreeBsdCentralProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */