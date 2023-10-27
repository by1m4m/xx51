/*     */ package oshi.software.os.unix.freebsd;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import oshi.jna.platform.linux.Libc;
/*     */ import oshi.software.common.AbstractOperatingSystem;
/*     */ import oshi.software.os.FileSystem;
/*     */ import oshi.software.os.NetworkParams;
/*     */ import oshi.software.os.OSProcess;
/*     */ import oshi.software.os.OSProcess.State;
/*     */ import oshi.software.os.OperatingSystem.ProcessSort;
/*     */ import oshi.util.ExecutingCommand;
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
/*     */ public class FreeBsdOperatingSystem
/*     */   extends AbstractOperatingSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public FreeBsdOperatingSystem()
/*     */   {
/*  44 */     this.manufacturer = "Unix/BSD";
/*  45 */     this.family = BsdSysctlUtil.sysctl("kern.ostype", "FreeBSD");
/*  46 */     this.version = new FreeBsdOSVersionInfoEx();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileSystem getFileSystem()
/*     */   {
/*  54 */     return new FreeBsdFileSystem();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public OSProcess[] getProcesses(int limit, OperatingSystem.ProcessSort sort)
/*     */   {
/*  62 */     List<OSProcess> procs = getProcessListFromPS("ps -awwxo state,pid,ppid,user,uid,group,gid,nlwp,pri,vsz,rss,etimes,systime,time,comm,args");
/*     */     
/*  64 */     List<OSProcess> sorted = processSort(procs, limit, sort);
/*  65 */     return (OSProcess[])sorted.toArray(new OSProcess[sorted.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public OSProcess getProcess(int pid)
/*     */   {
/*  73 */     List<OSProcess> procs = getProcessListFromPS("ps -awwxo state,pid,ppid,user,uid,group,gid,nlwp,pri,vsz,rss,etimes,systime,time,comm,args -p " + pid);
/*     */     
/*  75 */     if (procs.isEmpty()) {
/*  76 */       return null;
/*     */     }
/*  78 */     return (OSProcess)procs.get(0);
/*     */   }
/*     */   
/*     */   private List<OSProcess> getProcessListFromPS(String psCommand) {
/*  82 */     List<OSProcess> procs = new ArrayList();
/*  83 */     List<String> procList = ExecutingCommand.runNative(psCommand);
/*  84 */     if ((procList.isEmpty()) || (procList.size() < 2)) {
/*  85 */       return procs;
/*     */     }
/*     */     
/*  88 */     procList.remove(0);
/*     */     
/*  90 */     for (String proc : procList) {
/*  91 */       String[] split = proc.trim().split("\\s+", 16);
/*     */       
/*  93 */       if (split.length >= 16)
/*     */       {
/*     */ 
/*  96 */         long now = System.currentTimeMillis();
/*  97 */         OSProcess fproc = new OSProcess();
/*  98 */         switch (split[0].charAt(0)) {
/*     */         case 'R': 
/* 100 */           fproc.setState(OSProcess.State.RUNNING);
/* 101 */           break;
/*     */         case 'I': 
/*     */         case 'S': 
/* 104 */           fproc.setState(OSProcess.State.SLEEPING);
/* 105 */           break;
/*     */         case 'D': 
/*     */         case 'L': 
/*     */         case 'U': 
/* 109 */           fproc.setState(OSProcess.State.WAITING);
/* 110 */           break;
/*     */         case 'Z': 
/* 112 */           fproc.setState(OSProcess.State.ZOMBIE);
/* 113 */           break;
/*     */         case 'T': 
/* 115 */           fproc.setState(OSProcess.State.STOPPED);
/* 116 */           break;
/*     */         case 'E': case 'F': case 'G': case 'H': case 'J': case 'K': case 'M': case 'N': case 'O': case 'P': case 'Q': case 'V': case 'W': case 'X': case 'Y': default: 
/* 118 */           fproc.setState(OSProcess.State.OTHER);
/*     */         }
/*     */         
/* 121 */         fproc.setProcessID(ParseUtil.parseIntOrDefault(split[1], 0));
/* 122 */         fproc.setParentProcessID(ParseUtil.parseIntOrDefault(split[2], 0));
/* 123 */         fproc.setUser(split[3]);
/* 124 */         fproc.setUserID(split[4]);
/* 125 */         fproc.setGroup(split[5]);
/* 126 */         fproc.setGroupID(split[6]);
/* 127 */         fproc.setThreadCount(ParseUtil.parseIntOrDefault(split[7], 0));
/* 128 */         fproc.setPriority(ParseUtil.parseIntOrDefault(split[8], 0));
/*     */         
/* 130 */         fproc.setVirtualSize(ParseUtil.parseLongOrDefault(split[9], 0L) * 1024L);
/* 131 */         fproc.setResidentSetSize(ParseUtil.parseLongOrDefault(split[10], 0L) * 1024L);
/*     */         
/* 133 */         long elapsedTime = ParseUtil.parseDHMSOrDefault(split[11], 0L);
/* 134 */         fproc.setUpTime(elapsedTime < 1L ? 1L : elapsedTime);
/* 135 */         fproc.setStartTime(now - fproc.getUpTime());
/* 136 */         fproc.setKernelTime(ParseUtil.parseDHMSOrDefault(split[12], 0L));
/* 137 */         fproc.setUserTime(ParseUtil.parseDHMSOrDefault(split[13], 0L) - fproc.getKernelTime());
/* 138 */         fproc.setPath(split[14]);
/* 139 */         fproc.setName(fproc.getPath().substring(fproc.getPath().lastIndexOf('/') + 1));
/* 140 */         fproc.setCommandLine(split[15]);
/*     */         
/* 142 */         procs.add(fproc);
/*     */       } }
/* 144 */     return procs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getProcessId()
/*     */   {
/* 152 */     return Libc.INSTANCE.getpid();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getProcessCount()
/*     */   {
/* 160 */     List<String> procList = ExecutingCommand.runNative("ps -axo pid");
/* 161 */     if (!procList.isEmpty())
/*     */     {
/* 163 */       return procList.size() - 1;
/*     */     }
/* 165 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getThreadCount()
/*     */   {
/* 173 */     int threads = 0;
/* 174 */     for (String proc : ExecutingCommand.runNative("ps -axo nlwp")) {
/* 175 */       threads += ParseUtil.parseIntOrDefault(proc.trim(), 0);
/*     */     }
/* 177 */     return threads;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NetworkParams getNetworkParams()
/*     */   {
/* 185 */     return new FreeBsdNetworkParams();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\unix\freebsd\FreeBsdOperatingSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */