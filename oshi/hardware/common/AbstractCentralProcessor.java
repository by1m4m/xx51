/*     */ package oshi.hardware.common;
/*     */ 
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.hardware.CentralProcessor;
/*     */ import oshi.hardware.CentralProcessor.TickType;
/*     */ import oshi.util.ParseUtil;
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
/*     */ 
/*     */ public abstract class AbstractCentralProcessor
/*     */   implements CentralProcessor
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  43 */   private static final Logger LOG = LoggerFactory.getLogger(AbstractCentralProcessor.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   private static final java.lang.management.OperatingSystemMXBean OS_MXBEAN = ManagementFactory.getOperatingSystemMXBean();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   private double lastCpuLoad = 0.0D;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */   private long lastCpuLoadTime = 0L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  66 */   private boolean sunMXBean = false;
/*     */   
/*     */ 
/*  69 */   protected int logicalProcessorCount = 0;
/*     */   
/*  71 */   protected int physicalProcessorCount = 0;
/*     */   
/*     */ 
/*     */   private long tickTime;
/*     */   
/*     */ 
/*     */   private long[] prevTicks;
/*     */   
/*     */ 
/*     */   private long[] curTicks;
/*     */   
/*     */ 
/*     */   private long procTickTime;
/*     */   
/*     */ 
/*     */   private long[][] prevProcTicks;
/*     */   
/*     */   private long[][] curProcTicks;
/*     */   
/*     */   private String cpuVendor;
/*     */   
/*     */   private String cpuName;
/*     */   
/*     */   private String processorID;
/*     */   
/*     */   private String cpuIdentifier;
/*     */   
/*     */   private String cpuStepping;
/*     */   
/*     */   private String cpuModel;
/*     */   
/*     */   private String cpuFamily;
/*     */   
/*     */   private Long cpuVendorFreq;
/*     */   
/*     */   private Boolean cpu64;
/*     */   
/*     */ 
/*     */   public AbstractCentralProcessor()
/*     */   {
/* 111 */     initMXBean();
/*     */     
/* 113 */     calculateProcessorCounts();
/*     */   }
/*     */   
/*     */ 
/*     */   private void initMXBean()
/*     */   {
/*     */     try
/*     */     {
/* 121 */       Class.forName("com.sun.management.OperatingSystemMXBean");
/*     */       
/* 123 */       this.lastCpuLoad = ((com.sun.management.OperatingSystemMXBean)OS_MXBEAN).getSystemCpuLoad();
/* 124 */       this.lastCpuLoadTime = System.currentTimeMillis();
/* 125 */       this.sunMXBean = true;
/* 126 */       LOG.debug("Oracle MXBean detected.");
/*     */     } catch (ClassNotFoundException e) {
/* 128 */       LOG.debug("Oracle MXBean not detected.");
/* 129 */       LOG.trace("", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void initTicks()
/*     */   {
/* 138 */     this.prevProcTicks = new long[this.logicalProcessorCount][CentralProcessor.TickType.values().length];
/* 139 */     this.curProcTicks = new long[this.logicalProcessorCount][CentralProcessor.TickType.values().length];
/* 140 */     updateProcessorTicks();
/*     */     
/*     */ 
/*     */ 
/* 144 */     this.prevTicks = new long[CentralProcessor.TickType.values().length];
/* 145 */     this.curTicks = new long[CentralProcessor.TickType.values().length];
/* 146 */     updateSystemTicks();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void calculateProcessorCounts();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getVendor()
/*     */   {
/* 160 */     if (this.cpuVendor == null) {
/* 161 */       setVendor("");
/*     */     }
/* 163 */     return this.cpuVendor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVendor(String vendor)
/*     */   {
/* 171 */     this.cpuVendor = vendor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 179 */     if (this.cpuName == null) {
/* 180 */       setName("");
/*     */     }
/* 182 */     return this.cpuName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/* 190 */     this.cpuName = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProcessorID()
/*     */   {
/* 198 */     if (this.processorID == null) {
/* 199 */       setProcessorID("");
/*     */     }
/* 201 */     return this.processorID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProcessorID(String processorID)
/*     */   {
/* 209 */     this.processorID = processorID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getVendorFreq()
/*     */   {
/* 217 */     if (this.cpuVendorFreq == null) {
/* 218 */       Pattern pattern = Pattern.compile("@ (.*)$");
/* 219 */       Matcher matcher = pattern.matcher(getName());
/*     */       
/* 221 */       if (matcher.find()) {
/* 222 */         String unit = matcher.group(1);
/* 223 */         this.cpuVendorFreq = Long.valueOf(ParseUtil.parseHertz(unit));
/*     */       } else {
/* 225 */         this.cpuVendorFreq = Long.valueOf(-1L);
/*     */       }
/*     */     }
/* 228 */     return this.cpuVendorFreq.longValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVendorFreq(long freq)
/*     */   {
/* 236 */     this.cpuVendorFreq = Long.valueOf(freq);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getIdentifier()
/*     */   {
/* 244 */     if (this.cpuIdentifier == null) {
/* 245 */       StringBuilder sb = new StringBuilder();
/* 246 */       if (getVendor().contentEquals("GenuineIntel")) {
/* 247 */         sb.append(isCpu64bit() ? "Intel64" : "x86");
/*     */       } else {
/* 249 */         sb.append(getVendor());
/*     */       }
/* 251 */       sb.append(" Family ").append(getFamily());
/* 252 */       sb.append(" Model ").append(getModel());
/* 253 */       sb.append(" Stepping ").append(getStepping());
/* 254 */       setIdentifier(sb.toString());
/*     */     }
/* 256 */     return this.cpuIdentifier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIdentifier(String identifier)
/*     */   {
/* 264 */     this.cpuIdentifier = identifier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCpu64bit()
/*     */   {
/* 272 */     if (this.cpu64 == null) {
/* 273 */       setCpu64(false);
/*     */     }
/* 275 */     return this.cpu64.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCpu64(boolean value)
/*     */   {
/* 283 */     this.cpu64 = Boolean.valueOf(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getStepping()
/*     */   {
/* 291 */     if (this.cpuStepping == null) {
/* 292 */       if (this.cpuIdentifier == null) {
/* 293 */         return "?";
/*     */       }
/* 295 */       setStepping(parseIdentifier("Stepping"));
/*     */     }
/* 297 */     return this.cpuStepping;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStepping(String stepping)
/*     */   {
/* 305 */     this.cpuStepping = stepping;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getModel()
/*     */   {
/* 313 */     if (this.cpuModel == null) {
/* 314 */       if (this.cpuIdentifier == null) {
/* 315 */         return "?";
/*     */       }
/* 317 */       setModel(parseIdentifier("Model"));
/*     */     }
/* 319 */     return this.cpuModel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setModel(String model)
/*     */   {
/* 327 */     this.cpuModel = model;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFamily()
/*     */   {
/* 335 */     if (this.cpuFamily == null) {
/* 336 */       if (this.cpuIdentifier == null) {
/* 337 */         return "?";
/*     */       }
/* 339 */       setFamily(parseIdentifier("Family"));
/*     */     }
/* 341 */     return this.cpuFamily;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFamily(String family)
/*     */   {
/* 349 */     this.cpuFamily = family;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String parseIdentifier(String id)
/*     */   {
/* 360 */     String[] idSplit = getIdentifier().split("\\s+");
/* 361 */     boolean found = false;
/* 362 */     for (String s : idSplit)
/*     */     {
/* 364 */       if (found) {
/* 365 */         return s;
/*     */       }
/* 367 */       found = s.equals(id);
/*     */     }
/*     */     
/* 370 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized double getSystemCpuLoadBetweenTicks()
/*     */   {
/* 379 */     long now = System.currentTimeMillis();
/* 380 */     LOG.trace("Current time: {}  Last tick time: {}", Long.valueOf(now), Long.valueOf(this.tickTime));
/* 381 */     if (now - this.tickTime > 950L)
/*     */     {
/* 383 */       updateSystemTicks();
/*     */     }
/*     */     
/* 386 */     long total = 0L;
/* 387 */     for (int i = 0; i < this.curTicks.length; i++) {
/* 388 */       total += this.curTicks[i] - this.prevTicks[i];
/*     */     }
/*     */     
/*     */ 
/* 392 */     long idle = this.curTicks[CentralProcessor.TickType.IDLE.getIndex()] + this.curTicks[CentralProcessor.TickType.IOWAIT.getIndex()] - this.prevTicks[CentralProcessor.TickType.IDLE.getIndex()] - this.prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
/* 393 */     LOG.trace("Total ticks: {}  Idle ticks: {}", Long.valueOf(total), Long.valueOf(idle));
/*     */     
/* 395 */     return (total > 0L) && (idle >= 0L) ? (total - idle) / total : 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateSystemTicks()
/*     */   {
/* 406 */     LOG.trace("Updating System Ticks");
/* 407 */     long[] ticks = getSystemCpuLoadTicks();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 412 */     for (long tick : ticks) {
/* 413 */       if (tick != 0L)
/*     */       {
/* 415 */         this.tickTime = System.currentTimeMillis();
/*     */         
/* 417 */         System.arraycopy(this.curTicks, 0, this.prevTicks, 0, this.curTicks.length);
/* 418 */         System.arraycopy(ticks, 0, this.curTicks, 0, ticks.length);
/* 419 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getSystemCpuLoad()
/*     */   {
/* 429 */     if (this.sunMXBean) {
/* 430 */       long now = System.currentTimeMillis();
/*     */       
/* 432 */       if (now - this.lastCpuLoadTime < 200L) {
/* 433 */         return this.lastCpuLoad;
/*     */       }
/* 435 */       this.lastCpuLoad = ((com.sun.management.OperatingSystemMXBean)OS_MXBEAN).getSystemCpuLoad();
/* 436 */       this.lastCpuLoadTime = now;
/* 437 */       return this.lastCpuLoad;
/*     */     }
/* 439 */     return getSystemCpuLoadBetweenTicks();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getSystemLoadAverage()
/*     */   {
/* 447 */     return getSystemLoadAverage(1)[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[] getProcessorCpuLoadBetweenTicks()
/*     */   {
/* 456 */     long now = System.currentTimeMillis();
/* 457 */     LOG.trace("Current time: {}  Last tick time: {}", Long.valueOf(now), Long.valueOf(this.procTickTime));
/* 458 */     if (now - this.procTickTime > 950L)
/*     */     {
/*     */ 
/* 461 */       updateProcessorTicks();
/*     */     }
/* 463 */     double[] load = new double[this.logicalProcessorCount];
/* 464 */     for (int cpu = 0; cpu < this.logicalProcessorCount; cpu++) {
/* 465 */       long total = 0L;
/* 466 */       for (int i = 0; i < this.curProcTicks[cpu].length; i++) {
/* 467 */         total += this.curProcTicks[cpu][i] - this.prevProcTicks[cpu][i];
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 473 */       long idle = this.curProcTicks[cpu][CentralProcessor.TickType.IDLE.getIndex()] + this.curProcTicks[cpu][CentralProcessor.TickType.IOWAIT.getIndex()] - this.prevProcTicks[cpu][CentralProcessor.TickType.IDLE.getIndex()] - this.prevProcTicks[cpu][CentralProcessor.TickType.IOWAIT.getIndex()];
/* 474 */       LOG.trace("CPU: {}  Total ticks: {}  Idle ticks: {}", new Object[] { Integer.valueOf(cpu), Long.valueOf(total), Long.valueOf(idle) });
/*     */       
/* 476 */       load[cpu] = ((total > 0L) && (idle >= 0L) ? (total - idle) / total : 0.0D);
/*     */     }
/* 478 */     return load;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateProcessorTicks()
/*     */   {
/* 490 */     LOG.trace("Updating Processor Ticks");
/* 491 */     long[][] ticks = getProcessorCpuLoadTicks();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 496 */     for (long[] tick : ticks) {
/* 497 */       for (long element : tick) {
/* 498 */         if (element != 0L)
/*     */         {
/*     */ 
/*     */ 
/* 502 */           this.procTickTime = System.currentTimeMillis();
/*     */           
/* 504 */           for (int cpu = 0; cpu < this.logicalProcessorCount; cpu++) {
/* 505 */             System.arraycopy(this.curProcTicks[cpu], 0, this.prevProcTicks[cpu], 0, this.curProcTicks[cpu].length);
/*     */           }
/*     */           
/* 508 */           for (int cpu = 0; cpu < this.logicalProcessorCount; cpu++) {
/* 509 */             System.arraycopy(ticks[cpu], 0, this.curProcTicks[cpu], 0, ticks[cpu].length);
/*     */           }
/* 511 */           return;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getLogicalProcessorCount()
/*     */   {
/* 521 */     return this.logicalProcessorCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPhysicalProcessorCount()
/*     */   {
/* 529 */     return this.physicalProcessorCount;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 534 */     return getName();
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
/*     */   protected String createProcessorID(String stepping, String model, String family, String[] flags)
/*     */   {
/* 548 */     long processorID = 0L;
/* 549 */     long steppingL = ParseUtil.parseLongOrDefault(stepping, 0L);
/* 550 */     long modelL = ParseUtil.parseLongOrDefault(model, 0L);
/* 551 */     long familyL = ParseUtil.parseLongOrDefault(family, 0L);
/*     */     
/* 553 */     processorID |= steppingL & 0xF;
/*     */     
/* 555 */     processorID |= (modelL & 0xF) << 4;
/* 556 */     processorID |= (modelL & 0xF0) << 16;
/*     */     
/* 558 */     processorID |= (familyL & 0xF) << 8;
/* 559 */     processorID |= (familyL & 0xF0) << 20;
/*     */     
/* 561 */     for (String flag : flags) {
/* 562 */       switch (flag) {
/*     */       case "fpu": 
/* 564 */         processorID |= 0x100000000;
/* 565 */         break;
/*     */       case "vme": 
/* 567 */         processorID |= 0x200000000;
/* 568 */         break;
/*     */       case "de": 
/* 570 */         processorID |= 0x400000000;
/* 571 */         break;
/*     */       case "pse": 
/* 573 */         processorID |= 0x800000000;
/* 574 */         break;
/*     */       case "tsc": 
/* 576 */         processorID |= 0x1000000000;
/* 577 */         break;
/*     */       case "msr": 
/* 579 */         processorID |= 0x2000000000;
/* 580 */         break;
/*     */       case "pae": 
/* 582 */         processorID |= 0x4000000000;
/* 583 */         break;
/*     */       case "mce": 
/* 585 */         processorID |= 0x8000000000;
/* 586 */         break;
/*     */       case "cx8": 
/* 588 */         processorID |= 0x10000000000;
/* 589 */         break;
/*     */       case "apic": 
/* 591 */         processorID |= 0x20000000000;
/* 592 */         break;
/*     */       case "sep": 
/* 594 */         processorID |= 0x80000000000;
/* 595 */         break;
/*     */       case "mtrr": 
/* 597 */         processorID |= 0x100000000000;
/* 598 */         break;
/*     */       case "pge": 
/* 600 */         processorID |= 0x200000000000;
/* 601 */         break;
/*     */       case "mca": 
/* 603 */         processorID |= 0x400000000000;
/* 604 */         break;
/*     */       case "cmov": 
/* 606 */         processorID |= 0x800000000000;
/* 607 */         break;
/*     */       case "pat": 
/* 609 */         processorID |= 0x1000000000000;
/* 610 */         break;
/*     */       case "pse-36": 
/* 612 */         processorID |= 0x2000000000000;
/* 613 */         break;
/*     */       case "psn": 
/* 615 */         processorID |= 0x4000000000000;
/* 616 */         break;
/*     */       case "clfsh": 
/* 618 */         processorID |= 0x8000000000000;
/* 619 */         break;
/*     */       case "ds": 
/* 621 */         processorID |= 0x20000000000000;
/* 622 */         break;
/*     */       case "acpi": 
/* 624 */         processorID |= 0x40000000000000;
/* 625 */         break;
/*     */       case "mmx": 
/* 627 */         processorID |= 0x80000000000000;
/* 628 */         break;
/*     */       case "fxsr": 
/* 630 */         processorID |= 0x100000000000000;
/* 631 */         break;
/*     */       case "sse": 
/* 633 */         processorID |= 0x200000000000000;
/* 634 */         break;
/*     */       case "sse2": 
/* 636 */         processorID |= 0x400000000000000;
/* 637 */         break;
/*     */       case "ss": 
/* 639 */         processorID |= 0x800000000000000;
/* 640 */         break;
/*     */       case "htt": 
/* 642 */         processorID |= 0x1000000000000000;
/* 643 */         break;
/*     */       case "tm": 
/* 645 */         processorID |= 0x2000000000000000;
/* 646 */         break;
/*     */       case "ia64": 
/* 648 */         processorID |= 0x4000000000000000;
/* 649 */         break;
/*     */       case "pbe": 
/* 651 */         processorID |= 0x8000000000000000;
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/* 657 */     return String.format("%016X", new Object[] { Long.valueOf(processorID) });
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\common\AbstractCentralProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */