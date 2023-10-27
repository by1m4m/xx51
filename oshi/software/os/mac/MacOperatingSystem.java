/*     */ package oshi.software.os.mac;
/*     */ 
/*     */ import com.sun.jna.Memory;
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.jna.platform.mac.SystemB;
/*     */ import oshi.jna.platform.mac.SystemB.Group;
/*     */ import oshi.jna.platform.mac.SystemB.Passwd;
/*     */ import oshi.jna.platform.mac.SystemB.ProcBsdInfo;
/*     */ import oshi.jna.platform.mac.SystemB.ProcTaskAllInfo;
/*     */ import oshi.jna.platform.mac.SystemB.ProcTaskInfo;
/*     */ import oshi.jna.platform.mac.SystemB.RUsageInfoV2;
/*     */ import oshi.software.common.AbstractOperatingSystem;
/*     */ import oshi.software.os.FileSystem;
/*     */ import oshi.software.os.NetworkParams;
/*     */ import oshi.software.os.OSProcess;
/*     */ import oshi.software.os.OSProcess.State;
/*     */ import oshi.software.os.OperatingSystem.ProcessSort;
/*     */ import oshi.software.os.OperatingSystemVersion;
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
/*     */ public class MacOperatingSystem
/*     */   extends AbstractOperatingSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  49 */   private static final Logger LOG = LoggerFactory.getLogger(MacOperatingSystem.class);
/*     */   
/*  51 */   private int maxProc = 1024;
/*     */   
/*     */   private static final int SSLEEP = 1;
/*     */   
/*     */   private static final int SWAIT = 2;
/*     */   
/*     */   private static final int SRUN = 3;
/*     */   private static final int SIDL = 4;
/*     */   private static final int SZOMB = 5;
/*     */   private static final int SSTOP = 6;
/*     */   
/*     */   public MacOperatingSystem()
/*     */   {
/*  64 */     this.manufacturer = "Apple";
/*  65 */     this.version = new MacOSVersionInfoEx();
/*     */     
/*  67 */     this.family = ((ParseUtil.getFirstIntValue(this.version.getVersion()) == 10) && (ParseUtil.getNthIntValue(this.version.getVersion(), 2) >= 12) ? "macOS" : System.getProperty("os.name"));
/*     */     
/*  69 */     this.maxProc = SysctlUtil.sysctl("kern.maxproc", 4096);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileSystem getFileSystem()
/*     */   {
/*  77 */     return new MacFileSystem();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public OSProcess[] getProcesses(int limit, OperatingSystem.ProcessSort sort)
/*     */   {
/*  85 */     List<OSProcess> procs = new ArrayList();
/*  86 */     int[] pids = new int[this.maxProc];
/*  87 */     int numberOfProcesses = SystemB.INSTANCE.proc_listpids(1, 0, pids, pids.length) / SystemB.INT_SIZE;
/*     */     
/*  89 */     for (int i = 0; i < numberOfProcesses; i++) {
/*  90 */       OSProcess proc = getProcess(pids[i]);
/*  91 */       if (proc != null) {
/*  92 */         procs.add(proc);
/*     */       }
/*     */     }
/*  95 */     List<OSProcess> sorted = processSort(procs, limit, sort);
/*  96 */     return (OSProcess[])sorted.toArray(new OSProcess[sorted.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public OSProcess getProcess(int pid)
/*     */   {
/* 104 */     SystemB.ProcTaskAllInfo taskAllInfo = new SystemB.ProcTaskAllInfo();
/* 105 */     if (0 > SystemB.INSTANCE.proc_pidinfo(pid, 2, 0L, taskAllInfo, taskAllInfo.size())) {
/* 106 */       return null;
/*     */     }
/* 108 */     String name = null;
/* 109 */     String path = "";
/* 110 */     Pointer buf = new Memory(4096L);
/* 111 */     if (0 < SystemB.INSTANCE.proc_pidpath(pid, buf, 4096)) {
/* 112 */       path = buf.getString(0L).trim();
/*     */       
/* 114 */       String[] pathSplit = path.split("/");
/* 115 */       if (pathSplit.length > 0) {
/* 116 */         name = pathSplit[(pathSplit.length - 1)];
/*     */       }
/*     */     }
/*     */     
/* 120 */     if (taskAllInfo.ptinfo.pti_threadnum < 1) {
/* 121 */       return null;
/*     */     }
/* 123 */     if (name == null)
/*     */     {
/*     */ 
/* 126 */       for (int t = 0; t < taskAllInfo.pbsd.pbi_comm.length; t++) {
/* 127 */         if (taskAllInfo.pbsd.pbi_comm[t] == 0) {
/* 128 */           name = new String(taskAllInfo.pbsd.pbi_comm, 0, t);
/* 129 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 133 */     long bytesRead = 0L;
/* 134 */     long bytesWritten = 0L;
/* 135 */     if (getVersion().getOsxVersionNumber() >= 9) {
/* 136 */       SystemB.RUsageInfoV2 rUsageInfoV2 = new SystemB.RUsageInfoV2();
/* 137 */       if (0 == SystemB.INSTANCE.proc_pid_rusage(pid, 2, rUsageInfoV2)) {
/* 138 */         bytesRead = rUsageInfoV2.ri_diskio_bytesread;
/* 139 */         bytesWritten = rUsageInfoV2.ri_diskio_byteswritten;
/*     */       }
/*     */     }
/* 142 */     long now = System.currentTimeMillis();
/* 143 */     OSProcess proc = new OSProcess();
/* 144 */     proc.setName(name);
/* 145 */     proc.setPath(path);
/* 146 */     switch (taskAllInfo.pbsd.pbi_status) {
/*     */     case 1: 
/* 148 */       proc.setState(OSProcess.State.SLEEPING);
/* 149 */       break;
/*     */     case 2: 
/* 151 */       proc.setState(OSProcess.State.WAITING);
/* 152 */       break;
/*     */     case 3: 
/* 154 */       proc.setState(OSProcess.State.RUNNING);
/* 155 */       break;
/*     */     case 4: 
/* 157 */       proc.setState(OSProcess.State.NEW);
/* 158 */       break;
/*     */     case 5: 
/* 160 */       proc.setState(OSProcess.State.ZOMBIE);
/* 161 */       break;
/*     */     case 6: 
/* 163 */       proc.setState(OSProcess.State.STOPPED);
/* 164 */       break;
/*     */     default: 
/* 166 */       proc.setState(OSProcess.State.OTHER);
/*     */     }
/*     */     
/* 169 */     proc.setProcessID(pid);
/* 170 */     proc.setParentProcessID(taskAllInfo.pbsd.pbi_ppid);
/* 171 */     proc.setUserID(Integer.toString(taskAllInfo.pbsd.pbi_uid));
/* 172 */     SystemB.Passwd user = SystemB.INSTANCE.getpwuid(taskAllInfo.pbsd.pbi_uid);
/* 173 */     proc.setUser(user == null ? proc.getUserID() : user.pw_name);
/* 174 */     proc.setGroupID(Integer.toString(taskAllInfo.pbsd.pbi_gid));
/* 175 */     SystemB.Group group = SystemB.INSTANCE.getgrgid(taskAllInfo.pbsd.pbi_gid);
/* 176 */     proc.setGroup(group == null ? proc.getGroupID() : group.gr_name);
/* 177 */     proc.setThreadCount(taskAllInfo.ptinfo.pti_threadnum);
/* 178 */     proc.setPriority(taskAllInfo.ptinfo.pti_priority);
/* 179 */     proc.setVirtualSize(taskAllInfo.ptinfo.pti_virtual_size);
/* 180 */     proc.setResidentSetSize(taskAllInfo.ptinfo.pti_resident_size);
/* 181 */     proc.setKernelTime(taskAllInfo.ptinfo.pti_total_system / 1000000L);
/* 182 */     proc.setUserTime(taskAllInfo.ptinfo.pti_total_user / 1000000L);
/* 183 */     proc.setStartTime(taskAllInfo.pbsd.pbi_start_tvsec * 1000L + taskAllInfo.pbsd.pbi_start_tvusec / 1000L);
/* 184 */     proc.setUpTime(now - proc.getStartTime());
/* 185 */     proc.setBytesRead(bytesRead);
/* 186 */     proc.setBytesWritten(bytesWritten);
/* 187 */     proc.setCommandLine(getCommandLine(pid));
/* 188 */     return proc;
/*     */   }
/*     */   
/*     */   private String getCommandLine(int pid)
/*     */   {
/* 193 */     int[] mib = new int[3];
/* 194 */     mib[0] = 1;
/* 195 */     mib[1] = 49;
/* 196 */     mib[2] = pid;
/*     */     
/* 198 */     int argmax = SysctlUtil.sysctl("kern.argmax", 0);
/* 199 */     Pointer procargs = new Memory(argmax);
/* 200 */     IntByReference size = new IntByReference(argmax);
/*     */     
/* 202 */     if (0 != SystemB.INSTANCE.sysctl(mib, mib.length, procargs, size, null, 0)) {
/* 203 */       LOG.error("Failed syctl call: kern.procargs2, Error code: {}", Integer.valueOf(Native.getLastError()));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 209 */     int nargs = procargs.getInt(0L);
/* 210 */     List<String> args = new ArrayList(nargs);
/*     */     
/* 212 */     long offset = SystemB.INT_SIZE;
/*     */     
/* 214 */     offset += procargs.getString(offset).length();
/*     */     
/*     */ 
/* 217 */     while ((nargs-- > 0) && (offset < size.getValue()))
/*     */     {
/* 219 */       while (procargs.getByte(offset) == 0) {
/* 220 */         if (++offset >= size.getValue()) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       
/* 225 */       String arg = procargs.getString(offset);
/* 226 */       args.add(arg);
/*     */       
/* 228 */       offset += arg.length();
/*     */     }
/*     */     
/* 231 */     return String.join("\000", args);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MacOSVersionInfoEx getVersion()
/*     */   {
/* 239 */     return (MacOSVersionInfoEx)this.version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getProcessId()
/*     */   {
/* 247 */     return SystemB.INSTANCE.getpid();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getProcessCount()
/*     */   {
/* 255 */     return SystemB.INSTANCE.proc_listpids(1, 0, null, 0) / SystemB.INT_SIZE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getThreadCount()
/*     */   {
/* 265 */     int[] pids = new int[getProcessCount() + 10];
/* 266 */     int numberOfProcesses = SystemB.INSTANCE.proc_listpids(1, 0, pids, pids.length) / SystemB.INT_SIZE;
/*     */     
/* 268 */     int numberOfThreads = 0;
/* 269 */     SystemB.ProcTaskInfo taskInfo = new SystemB.ProcTaskInfo();
/* 270 */     for (int i = 0; i < numberOfProcesses; i++) {
/* 271 */       SystemB.INSTANCE.proc_pidinfo(pids[i], 4, 0L, taskInfo, taskInfo.size());
/* 272 */       numberOfThreads += taskInfo.pti_threadnum;
/*     */     }
/* 274 */     return numberOfThreads;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NetworkParams getNetworkParams()
/*     */   {
/* 282 */     return new MacNetworkParams();
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\mac\MacOperatingSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */