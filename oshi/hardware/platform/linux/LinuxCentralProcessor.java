/*     */ package oshi.hardware.platform.linux;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.CentralProcessor.TickType;
/*     */ import oshi.hardware.common.AbstractCentralProcessor;
/*     */ import oshi.jna.platform.linux.Libc;
/*     */ import oshi.util.ExecutingCommand;
/*     */ import oshi.util.FileUtil;
/*     */ import oshi.util.ParseUtil;
/*     */ import oshi.util.platform.linux.ProcUtil;
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
/*     */ public class LinuxCentralProcessor
/*     */   extends AbstractCentralProcessor
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  46 */   private static final Logger LOG = LoggerFactory.getLogger(LinuxCentralProcessor.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public LinuxCentralProcessor()
/*     */   {
/*  54 */     initVars();
/*     */     
/*  56 */     initTicks();
/*     */     
/*  58 */     LOG.debug("Initialized Processor");
/*     */   }
/*     */   
/*     */   private void initVars() {
/*  62 */     String[] flags = new String[0];
/*  63 */     List<String> cpuInfo = FileUtil.readFile("/proc/cpuinfo");
/*  64 */     for (String line : cpuInfo) {
/*  65 */       String[] splitLine = line.split("\\s+:\\s");
/*  66 */       if (splitLine.length < 2) {
/*     */         break;
/*     */       }
/*  69 */       switch (splitLine[0]) {
/*     */       case "vendor_id": 
/*  71 */         setVendor(splitLine[1]);
/*  72 */         break;
/*     */       case "model name": 
/*  74 */         setName(splitLine[1]);
/*  75 */         break;
/*     */       case "flags": 
/*  77 */         flags = splitLine[1].toLowerCase().split(" ");
/*  78 */         boolean found = false;
/*  79 */         for (String flag : flags) {
/*  80 */           if ("lm".equals(flag)) {
/*  81 */             found = true;
/*  82 */             break;
/*     */           }
/*     */         }
/*  85 */         setCpu64(found);
/*  86 */         break;
/*     */       case "stepping": 
/*  88 */         setStepping(splitLine[1]);
/*  89 */         break;
/*     */       case "model": 
/*  91 */         setModel(splitLine[1]);
/*  92 */         break;
/*     */       case "cpu family": 
/*  94 */         setFamily(splitLine[1]);
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/* 100 */     setProcessorID(getProcessorID(getStepping(), getModel(), getFamily(), flags));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void calculateProcessorCounts()
/*     */   {
/* 108 */     List<String> procCpu = FileUtil.readFile("/proc/cpuinfo");
/*     */     
/* 110 */     for (String cpu : procCpu) {
/* 111 */       if (cpu.startsWith("processor")) {
/* 112 */         this.logicalProcessorCount += 1;
/*     */       }
/*     */     }
/*     */     
/* 116 */     int siblings = 0;
/*     */     
/* 118 */     int[] uniqueID = new int[2];
/* 119 */     uniqueID[0] = -1;
/* 120 */     uniqueID[1] = -1;
/*     */     
/* 122 */     Set<String> ids = new HashSet();
/*     */     
/* 124 */     for (String cpu : procCpu) {
/* 125 */       if (cpu.startsWith("siblings"))
/*     */       {
/* 127 */         siblings = ParseUtil.parseLastInt(cpu, 1);
/* 128 */         if (siblings == 1) {
/* 129 */           this.physicalProcessorCount = this.logicalProcessorCount;
/* 130 */           break;
/*     */         }
/*     */       }
/* 133 */       if (cpu.startsWith("cpu cores"))
/*     */       {
/* 135 */         int cpucores = ParseUtil.parseLastInt(cpu, 1);
/* 136 */         if (siblings > 1) {
/* 137 */           this.physicalProcessorCount = (this.logicalProcessorCount * cpucores / siblings);
/* 138 */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 143 */       if ((cpu.startsWith("core id")) || (cpu.startsWith("cpu number"))) {
/* 144 */         uniqueID[0] = ParseUtil.parseLastInt(cpu, 0);
/* 145 */       } else if (cpu.startsWith("physical id")) {
/* 146 */         uniqueID[1] = ParseUtil.parseLastInt(cpu, 0);
/*     */       }
/* 148 */       if ((uniqueID[0] >= 0) && (uniqueID[1] >= 0)) {
/* 149 */         ids.add(uniqueID[0] + " " + uniqueID[1]);
/* 150 */         uniqueID[0] = -1;
/* 151 */         uniqueID[1] = -1;
/*     */       }
/*     */     }
/* 154 */     if (this.physicalProcessorCount == 0) {
/* 155 */       this.physicalProcessorCount = ids.size();
/*     */     }
/*     */     
/* 158 */     if ((this.physicalProcessorCount == 0) && (this.logicalProcessorCount > 0)) {
/* 159 */       this.physicalProcessorCount = 1;
/*     */     }
/*     */     
/* 162 */     if (this.logicalProcessorCount < 1) {
/* 163 */       LOG.error("Couldn't find logical processor count. Assuming 1.");
/* 164 */       this.logicalProcessorCount = 1;
/*     */     }
/* 166 */     if (this.physicalProcessorCount < 1) {
/* 167 */       LOG.error("Couldn't find physical processor count. Assuming 1.");
/* 168 */       this.physicalProcessorCount = 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized long[] getSystemCpuLoadTicks()
/*     */   {
/* 177 */     long[] ticks = new long[CentralProcessor.TickType.values().length];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 182 */     List<String> procStat = FileUtil.readFile("/proc/stat");
/* 183 */     String tickStr; if (!procStat.isEmpty()) {
/* 184 */       tickStr = (String)procStat.get(0);
/*     */     } else {
/* 186 */       return ticks;
/*     */     }
/*     */     
/*     */     String tickStr;
/* 190 */     String[] tickArr = tickStr.split("\\s+");
/* 191 */     if (tickArr.length <= CentralProcessor.TickType.IDLE.getIndex())
/*     */     {
/* 193 */       return ticks;
/*     */     }
/*     */     
/* 196 */     for (int i = 0; i < CentralProcessor.TickType.values().length; i++) {
/* 197 */       ticks[i] = ParseUtil.parseLongOrDefault(tickArr[(i + 1)], 0L);
/*     */     }
/*     */     
/* 200 */     if (tickArr.length > CentralProcessor.TickType.values().length + 1)
/*     */     {
/* 202 */       ticks[CentralProcessor.TickType.SYSTEM.getIndex()] += ParseUtil.parseLongOrDefault(tickArr[(CentralProcessor.TickType.values().length + 1)], 0L);
/*     */     }
/*     */     
/*     */ 
/* 206 */     return ticks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[] getSystemLoadAverage(int nelem)
/*     */   {
/* 214 */     if ((nelem < 1) || (nelem > 3)) {
/* 215 */       throw new IllegalArgumentException("Must include from one to three elements.");
/*     */     }
/* 217 */     double[] average = new double[nelem];
/* 218 */     int retval = Libc.INSTANCE.getloadavg(average, nelem);
/* 219 */     if (retval < nelem) {
/* 220 */       for (int i = Math.max(retval, 0); i < average.length; i++) {
/* 221 */         average[i] = -1.0D;
/*     */       }
/*     */     }
/* 224 */     return average;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long[][] getProcessorCpuLoadTicks()
/*     */   {
/* 232 */     long[][] ticks = new long[this.logicalProcessorCount][CentralProcessor.TickType.values().length];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 237 */     int cpu = 0;
/* 238 */     List<String> procStat = FileUtil.readFile("/proc/stat");
/* 239 */     for (Iterator localIterator = procStat.iterator(); localIterator.hasNext(); 
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
/* 261 */         cpu >= this.logicalProcessorCount)
/*     */     {
/* 239 */       String stat = (String)localIterator.next();
/* 240 */       if ((stat.startsWith("cpu")) && (!stat.startsWith("cpu ")))
/*     */       {
/*     */ 
/*     */ 
/* 244 */         String[] tickArr = stat.split("\\s+");
/* 245 */         if (tickArr.length <= CentralProcessor.TickType.IDLE.getIndex())
/*     */         {
/* 247 */           return ticks;
/*     */         }
/*     */         
/* 250 */         for (int i = 0; i < CentralProcessor.TickType.values().length; i++) {
/* 251 */           ticks[cpu][i] = ParseUtil.parseLongOrDefault(tickArr[(i + 1)], 0L);
/*     */         }
/*     */         
/* 254 */         if (tickArr.length > CentralProcessor.TickType.values().length + 1)
/*     */         {
/* 256 */           int tmp150_147 = CentralProcessor.TickType.SYSTEM.getIndex(); long[] tmp150_143 = ticks[cpu];tmp150_143[tmp150_147] = 
/* 257 */             (tmp150_143[tmp150_147] + ParseUtil.parseLongOrDefault(tickArr[(CentralProcessor.TickType.values().length + 1)], 0L));
/*     */         }
/*     */         
/*     */ 
/* 261 */         cpu++;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 266 */     return ticks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getSystemUptime()
/*     */   {
/* 274 */     return ProcUtil.getSystemUptimeFromProc();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public String getSystemSerialNumber()
/*     */   {
/* 283 */     return new LinuxComputerSystem().getSerialNumber();
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
/*     */ 
/*     */ 
/*     */   private String getProcessorID(String stepping, String model, String family, String[] flags)
/*     */   {
/* 298 */     boolean procInfo = false;
/* 299 */     String marker = "Processor Information";
/* 300 */     for (String checkLine : ExecutingCommand.runNative("dmidecode -t system")) {
/* 301 */       if ((!procInfo) && (checkLine.contains(marker))) {
/* 302 */         marker = "ID:";
/* 303 */         procInfo = true;
/* 304 */       } else if ((procInfo) && (checkLine.contains(marker))) {
/* 305 */         return checkLine.split(marker)[1].trim();
/*     */       }
/*     */     }
/*     */     
/* 309 */     marker = "eax=";
/* 310 */     for (String checkLine : ExecutingCommand.runNative("cpuid -1r")) {
/* 311 */       if ((checkLine.contains(marker)) && (checkLine.trim().startsWith("0x00000001"))) {
/* 312 */         String eax = "";
/* 313 */         String edx = "";
/* 314 */         for (String register : checkLine.split("\\s+")) {
/* 315 */           if (register.startsWith("eax=")) {
/* 316 */             eax = register.replace("eax=0x", "");
/* 317 */           } else if (register.startsWith("edx=")) {
/* 318 */             edx = register.replace("edx=0x", "");
/*     */           }
/*     */         }
/* 321 */         return edx + eax;
/*     */       }
/*     */     }
/*     */     
/* 325 */     return createProcessorID(stepping, model, family, flags);
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\linux\LinuxCentralProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */