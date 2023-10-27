/*     */ package oshi.hardware.platform.linux;
/*     */ 
/*     */ import java.util.List;
/*     */ import oshi.hardware.common.AbstractGlobalMemory;
/*     */ import oshi.util.FileUtil;
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
/*     */ public class LinuxGlobalMemory
/*     */   extends AbstractGlobalMemory
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  38 */   private long memFree = 0L;
/*  39 */   private long activeFile = 0L;
/*  40 */   private long inactiveFile = 0L;
/*  41 */   private long sReclaimable = 0L;
/*  42 */   private long swapFree = 0L;
/*     */   
/*  44 */   private long lastUpdate = 0L;
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
/*     */   protected void updateMeminfo()
/*     */   {
/*  61 */     long now = System.currentTimeMillis();
/*  62 */     if (now - this.lastUpdate > 100L) {
/*  63 */       List<String> memInfo = FileUtil.readFile("/proc/meminfo");
/*  64 */       if (memInfo.isEmpty()) {
/*  65 */         return;
/*     */       }
/*  67 */       boolean found = false;
/*  68 */       for (String checkLine : memInfo) {
/*  69 */         String[] memorySplit = checkLine.split("\\s+");
/*  70 */         if (memorySplit.length > 1) {
/*  71 */           switch (memorySplit[0]) {
/*     */           case "MemTotal:": 
/*  73 */             this.memTotal = parseMeminfo(memorySplit);
/*  74 */             break;
/*     */           case "MemFree:": 
/*  76 */             this.memFree = parseMeminfo(memorySplit);
/*  77 */             break;
/*     */           case "MemAvailable:": 
/*  79 */             this.memAvailable = parseMeminfo(memorySplit);
/*  80 */             found = true;
/*  81 */             break;
/*     */           case "Active(file):": 
/*  83 */             this.activeFile = parseMeminfo(memorySplit);
/*  84 */             break;
/*     */           case "Inactive(file):": 
/*  86 */             this.inactiveFile = parseMeminfo(memorySplit);
/*  87 */             break;
/*     */           case "SReclaimable:": 
/*  89 */             this.sReclaimable = parseMeminfo(memorySplit);
/*  90 */             break;
/*     */           case "SwapTotal:": 
/*  92 */             this.swapTotal = parseMeminfo(memorySplit);
/*  93 */             break;
/*     */           case "SwapFree:": 
/*  95 */             this.swapFree = parseMeminfo(memorySplit);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 103 */       this.swapUsed = (this.swapTotal - this.swapFree);
/*     */       
/* 105 */       if (!found) {
/* 106 */         this.memAvailable = (this.memFree + this.activeFile + this.inactiveFile + this.sReclaimable);
/*     */       }
/*     */       
/* 109 */       this.lastUpdate = now;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateSwap()
/*     */   {
/* 118 */     updateMeminfo();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long parseMeminfo(String[] memorySplit)
/*     */   {
/* 129 */     if (memorySplit.length < 2) {
/* 130 */       return 0L;
/*     */     }
/* 132 */     long memory = ParseUtil.parseLongOrDefault(memorySplit[1], 0L);
/* 133 */     if ((memorySplit.length > 2) && ("kB".equals(memorySplit[2]))) {
/* 134 */       memory *= 1024L;
/*     */     }
/* 136 */     return memory;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\linux\LinuxGlobalMemory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */