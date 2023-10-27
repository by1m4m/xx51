/*     */ package oshi.software.os.unix.solaris;
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
/*     */ public class SolarisOperatingSystem
/*     */   extends AbstractOperatingSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public SolarisOperatingSystem()
/*     */   {
/*  44 */     this.manufacturer = "Oracle";
/*  45 */     this.family = "SunOS";
/*  46 */     this.version = new SolarisOSVersionInfoEx();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileSystem getFileSystem()
/*     */   {
/*  54 */     return new SolarisFileSystem();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public OSProcess[] getProcesses(int limit, OperatingSystem.ProcessSort sort)
/*     */   {
/*  62 */     List<OSProcess> procs = getProcessListFromPS("ps -eo s,pid,ppid,user,uid,group,gid,nlwp,pri,vsz,rss,etime,time,comm,args");
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
/*  73 */     List<OSProcess> procs = getProcessListFromPS("ps -o s,pid,ppid,user,uid,group,gid,nlwp,pri,vsz,rss,etime,time,comm,args -p " + pid);
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
/*  91 */       String[] split = proc.trim().split("\\s+", 15);
/*     */       
/*  93 */       if (split.length >= 15)
/*     */       {
/*     */ 
/*  96 */         long now = System.currentTimeMillis();
/*  97 */         OSProcess sproc = new OSProcess();
/*  98 */         switch (split[0].charAt(0)) {
/*     */         case 'O': 
/* 100 */           sproc.setState(OSProcess.State.RUNNING);
/* 101 */           break;
/*     */         case 'S': 
/* 103 */           sproc.setState(OSProcess.State.SLEEPING);
/* 104 */           break;
/*     */         case 'R': 
/*     */         case 'W': 
/* 107 */           sproc.setState(OSProcess.State.WAITING);
/* 108 */           break;
/*     */         case 'Z': 
/* 110 */           sproc.setState(OSProcess.State.ZOMBIE);
/* 111 */           break;
/*     */         case 'T': 
/* 113 */           sproc.setState(OSProcess.State.STOPPED);
/* 114 */           break;
/*     */         case 'P': case 'Q': case 'U': case 'V': case 'X': case 'Y': default: 
/* 116 */           sproc.setState(OSProcess.State.OTHER);
/*     */         }
/*     */         
/* 119 */         sproc.setProcessID(ParseUtil.parseIntOrDefault(split[1], 0));
/* 120 */         sproc.setParentProcessID(ParseUtil.parseIntOrDefault(split[2], 0));
/* 121 */         sproc.setUser(split[3]);
/* 122 */         sproc.setUserID(split[4]);
/* 123 */         sproc.setGroup(split[5]);
/* 124 */         sproc.setGroupID(split[6]);
/* 125 */         sproc.setThreadCount(ParseUtil.parseIntOrDefault(split[7], 0));
/* 126 */         sproc.setPriority(ParseUtil.parseIntOrDefault(split[8], 0));
/*     */         
/* 128 */         sproc.setVirtualSize(ParseUtil.parseLongOrDefault(split[9], 0L) * 1024L);
/* 129 */         sproc.setResidentSetSize(ParseUtil.parseLongOrDefault(split[10], 0L) * 1024L);
/*     */         
/* 131 */         long elapsedTime = ParseUtil.parseDHMSOrDefault(split[11], 0L);
/* 132 */         sproc.setUpTime(elapsedTime < 1L ? 1L : elapsedTime);
/* 133 */         sproc.setStartTime(now - sproc.getUpTime());
/* 134 */         sproc.setUserTime(ParseUtil.parseDHMSOrDefault(split[12], 0L));
/* 135 */         sproc.setPath(split[13]);
/* 136 */         sproc.setName(sproc.getPath().substring(sproc.getPath().lastIndexOf('/') + 1));
/* 137 */         sproc.setCommandLine(split[14]);
/*     */         
/* 139 */         procs.add(sproc);
/*     */       } }
/* 141 */     return procs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getProcessId()
/*     */   {
/* 149 */     return Libc.INSTANCE.getpid();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getProcessCount()
/*     */   {
/* 157 */     return ProcUtil.getPidFiles().length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getThreadCount()
/*     */   {
/* 165 */     List<String> threadList = ExecutingCommand.runNative("ps -eLo pid");
/* 166 */     if (!threadList.isEmpty())
/*     */     {
/* 168 */       return threadList.size() - 1;
/*     */     }
/* 170 */     return getProcessCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NetworkParams getNetworkParams()
/*     */   {
/* 178 */     return new SolarisNetworkParams();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\unix\solaris\SolarisOperatingSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */