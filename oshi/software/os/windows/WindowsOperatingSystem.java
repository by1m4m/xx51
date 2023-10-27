/*     */ package oshi.software.os.windows;
/*     */ 
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.platform.win32.Advapi32;
/*     */ import com.sun.jna.platform.win32.Advapi32Util;
/*     */ import com.sun.jna.platform.win32.Advapi32Util.Account;
/*     */ import com.sun.jna.platform.win32.WinDef.DWORD;
/*     */ import com.sun.jna.platform.win32.WinNT.HANDLE;
/*     */ import com.sun.jna.platform.win32.WinNT.HANDLEByReference;
/*     */ import com.sun.jna.platform.win32.WinNT.LUID;
/*     */ import com.sun.jna.platform.win32.WinNT.LUID_AND_ATTRIBUTES;
/*     */ import com.sun.jna.platform.win32.WinNT.TOKEN_PRIVILEGES;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.jna.platform.windows.Kernel32;
/*     */ import oshi.jna.platform.windows.Psapi;
/*     */ import oshi.jna.platform.windows.Psapi.PERFORMANCE_INFORMATION;
/*     */ import oshi.software.common.AbstractOperatingSystem;
/*     */ import oshi.software.os.FileSystem;
/*     */ import oshi.software.os.NetworkParams;
/*     */ import oshi.software.os.OSProcess;
/*     */ import oshi.software.os.OSProcess.State;
/*     */ import oshi.software.os.OperatingSystem.ProcessSort;
/*     */ import oshi.util.ParseUtil;
/*     */ import oshi.util.platform.windows.WmiUtil;
/*     */ import oshi.util.platform.windows.WmiUtil.ValueType;
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
/*     */ public class WindowsOperatingSystem
/*     */   extends AbstractOperatingSystem
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  52 */   private static final Logger LOG = LoggerFactory.getLogger(WindowsOperatingSystem.class);
/*     */   
/*     */ 
/*  55 */   private static String processProperties = "Name,ExecutablePath,CommandLine,ExecutionState,ProcessID,ParentProcessId,ThreadCount,Priority,VirtualSize,WorkingSetSize,KernelModeTime,UserModeTime,CreationDate,ReadTransferCount,WriteTransferCount,__PATH,__PATH";
/*     */   
/*     */ 
/*  58 */   private static WmiUtil.ValueType[] processPropertyTypes = { WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.UINT32, WmiUtil.ValueType.UINT32, WmiUtil.ValueType.UINT32, WmiUtil.ValueType.UINT32, WmiUtil.ValueType.UINT32, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.STRING, WmiUtil.ValueType.DATETIME, WmiUtil.ValueType.UINT64, WmiUtil.ValueType.UINT64, WmiUtil.ValueType.PROCESS_GETOWNER, WmiUtil.ValueType.PROCESS_GETOWNERSID };
/*     */   
/*     */   private static final int UNKNOWN = 0;
/*     */   
/*     */   private static final int OTHER = 1;
/*     */   
/*     */   private static final int READY = 2;
/*     */   
/*     */   private static final int RUNNING = 3;
/*     */   
/*     */   private static final int BLOCKED = 4;
/*     */   
/*     */   private static final int SUSPENDED_BLOCKED = 5;
/*     */   
/*     */   private static final int SUSPENDED_READY = 6;
/*     */   
/*     */   private static final int TERMINATED = 7;
/*     */   
/*     */   private static final int STOPPED = 8;
/*     */   
/*     */   private static final int GROWING = 9;
/*     */   private static final int ERROR_ACCESS_DENIED = 5;
/*     */   
/*     */   static
/*     */   {
/*  83 */     enableDebugPrivilege();
/*     */   }
/*     */   
/*     */   public WindowsOperatingSystem() {
/*  87 */     this.manufacturer = "Microsoft";
/*  88 */     this.family = "Windows";
/*  89 */     this.version = new WindowsOSVersionInfoEx();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileSystem getFileSystem()
/*     */   {
/*  97 */     return new WindowsFileSystem();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public OSProcess[] getProcesses(int limit, OperatingSystem.ProcessSort sort)
/*     */   {
/* 105 */     Map<String, List<Object>> procs = WmiUtil.selectObjectsFrom(null, "Win32_Process", processProperties, null, processPropertyTypes);
/*     */     
/* 107 */     List<OSProcess> procList = processMapToList(procs);
/* 108 */     List<OSProcess> sorted = processSort(procList, limit, sort);
/* 109 */     return (OSProcess[])sorted.toArray(new OSProcess[sorted.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public OSProcess getProcess(int pid)
/*     */   {
/* 117 */     Map<String, List<Object>> procs = WmiUtil.selectObjectsFrom(null, "Win32_Process", processProperties, 
/* 118 */       String.format("WHERE ProcessId=%d", new Object[] {Integer.valueOf(pid) }), processPropertyTypes);
/* 119 */     List<OSProcess> procList = processMapToList(procs);
/* 120 */     return procList.isEmpty() ? null : (OSProcess)procList.get(0);
/*     */   }
/*     */   
/*     */   private List<OSProcess> processMapToList(Map<String, List<Object>> procs) {
/* 124 */     long now = System.currentTimeMillis();
/* 125 */     List<OSProcess> procList = new ArrayList();
/* 126 */     List<String> groupList = new ArrayList();
/* 127 */     List<String> groupIDList = new ArrayList();
/*     */     
/* 129 */     int procCount = ((List)procs.get("Name")).size();
/* 130 */     for (int p = 0; p < procCount; p++) {
/* 131 */       OSProcess proc = new OSProcess();
/* 132 */       proc.setName((String)((List)procs.get("Name")).get(p));
/* 133 */       proc.setPath((String)((List)procs.get("ExecutablePath")).get(p));
/* 134 */       proc.setCommandLine((String)((List)procs.get("CommandLine")).get(p));
/* 135 */       proc.setProcessID(((Long)((List)procs.get("ProcessID")).get(p)).intValue());
/* 136 */       proc.setParentProcessID(((Long)((List)procs.get("ParentProcessId")).get(p)).intValue());
/* 137 */       proc.setUser((String)((List)procs.get("PROCESS_GETOWNER")).get(p));
/* 138 */       proc.setUserID((String)((List)procs.get("PROCESS_GETOWNERSID")).get(p));
/*     */       
/*     */ 
/* 141 */       if (procCount == 1) {
/* 142 */         WinNT.HANDLE pHandle = Kernel32.INSTANCE.OpenProcess(1040, false, proc
/* 143 */           .getProcessID());
/* 144 */         if (pHandle != null) {
/* 145 */           WinNT.HANDLEByReference phToken = new WinNT.HANDLEByReference();
/* 146 */           if (Advapi32.INSTANCE.OpenProcessToken(pHandle, 10, phToken))
/*     */           {
/* 148 */             Advapi32Util.Account[] accounts = Advapi32Util.getTokenGroups(phToken.getValue());
/*     */             
/* 150 */             groupList.clear();
/* 151 */             groupIDList.clear();
/* 152 */             for (Advapi32Util.Account account : accounts) {
/* 153 */               groupList.add(account.name);
/* 154 */               groupIDList.add(account.sidString);
/*     */             }
/* 156 */             proc.setGroup(String.join(",", groupList));
/* 157 */             proc.setGroupID(String.join(",", groupIDList));
/*     */           } else {
/* 159 */             int error = Kernel32.INSTANCE.GetLastError();
/*     */             
/*     */ 
/* 162 */             if (error != 5) {
/* 163 */               LOG.error("Failed to get process token for process {}: {}", Integer.valueOf(proc.getProcessID()), 
/* 164 */                 Integer.valueOf(Kernel32.INSTANCE.GetLastError()));
/*     */             }
/*     */           }
/*     */         }
/* 168 */         Kernel32.INSTANCE.CloseHandle(pHandle);
/*     */       }
/* 170 */       switch (((Long)((List)procs.get("ExecutionState")).get(p)).intValue()) {
/*     */       case 2: 
/*     */       case 6: 
/* 173 */         proc.setState(OSProcess.State.SLEEPING);
/* 174 */         break;
/*     */       case 4: 
/*     */       case 5: 
/* 177 */         proc.setState(OSProcess.State.WAITING);
/* 178 */         break;
/*     */       case 3: 
/* 180 */         proc.setState(OSProcess.State.RUNNING);
/* 181 */         break;
/*     */       case 9: 
/* 183 */         proc.setState(OSProcess.State.NEW);
/* 184 */         break;
/*     */       case 7: 
/* 186 */         proc.setState(OSProcess.State.ZOMBIE);
/* 187 */         break;
/*     */       case 8: 
/* 189 */         proc.setState(OSProcess.State.STOPPED);
/* 190 */         break;
/*     */       case 0: 
/*     */       case 1: 
/*     */       default: 
/* 194 */         proc.setState(OSProcess.State.OTHER);
/*     */       }
/*     */       
/* 197 */       proc.setThreadCount(((Long)((List)procs.get("ThreadCount")).get(p)).intValue());
/* 198 */       proc.setPriority(((Long)((List)procs.get("Priority")).get(p)).intValue());
/* 199 */       proc.setVirtualSize(ParseUtil.parseLongOrDefault((String)((List)procs.get("VirtualSize")).get(p), 0L));
/* 200 */       proc.setResidentSetSize(ParseUtil.parseLongOrDefault((String)((List)procs.get("WorkingSetSize")).get(p), 0L));
/*     */       
/* 202 */       proc.setKernelTime(ParseUtil.parseLongOrDefault((String)((List)procs.get("KernelModeTime")).get(p), 0L) / 10000L);
/* 203 */       proc.setUserTime(ParseUtil.parseLongOrDefault((String)((List)procs.get("UserModeTime")).get(p), 0L) / 10000L);
/* 204 */       proc.setStartTime(((Long)((List)procs.get("CreationDate")).get(p)).longValue());
/* 205 */       proc.setUpTime(now - proc.getStartTime());
/* 206 */       proc.setBytesRead(((Long)((List)procs.get("ReadTransferCount")).get(p)).longValue());
/* 207 */       proc.setBytesWritten(((Long)((List)procs.get("WriteTransferCount")).get(p)).longValue());
/* 208 */       procList.add(proc);
/*     */     }
/* 210 */     return procList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getProcessId()
/*     */   {
/* 218 */     return Kernel32.INSTANCE.GetCurrentProcessId();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getProcessCount()
/*     */   {
/* 226 */     Psapi.PERFORMANCE_INFORMATION perfInfo = new Psapi.PERFORMANCE_INFORMATION();
/* 227 */     if (!Psapi.INSTANCE.GetPerformanceInfo(perfInfo, perfInfo.size())) {
/* 228 */       LOG.error("Failed to get Performance Info. Error code: {}", Integer.valueOf(Kernel32.INSTANCE.GetLastError()));
/* 229 */       return 0;
/*     */     }
/* 231 */     return perfInfo.ProcessCount.intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getThreadCount()
/*     */   {
/* 239 */     Psapi.PERFORMANCE_INFORMATION perfInfo = new Psapi.PERFORMANCE_INFORMATION();
/* 240 */     if (!Psapi.INSTANCE.GetPerformanceInfo(perfInfo, perfInfo.size())) {
/* 241 */       LOG.error("Failed to get Performance Info. Error code: {}", Integer.valueOf(Kernel32.INSTANCE.GetLastError()));
/* 242 */       return 0;
/*     */     }
/* 244 */     return perfInfo.ThreadCount.intValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NetworkParams getNetworkParams()
/*     */   {
/* 252 */     return new WindowsNetworkParams();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void enableDebugPrivilege()
/*     */   {
/* 260 */     WinNT.HANDLEByReference hToken = new WinNT.HANDLEByReference();
/* 261 */     boolean success = Advapi32.INSTANCE.OpenProcessToken(Kernel32.INSTANCE.GetCurrentProcess(), 40, hToken);
/*     */     
/* 263 */     if (!success) {
/* 264 */       LOG.error("OpenProcessToken failed. Error: {}" + Native.getLastError());
/* 265 */       return;
/*     */     }
/* 267 */     WinNT.LUID luid = new WinNT.LUID();
/* 268 */     success = Advapi32.INSTANCE.LookupPrivilegeValue(null, "SeDebugPrivilege", luid);
/* 269 */     if (!success) {
/* 270 */       LOG.error("LookupprivilegeValue failed. Error: {}" + Native.getLastError());
/* 271 */       return;
/*     */     }
/* 273 */     WinNT.TOKEN_PRIVILEGES tkp = new WinNT.TOKEN_PRIVILEGES(1);
/* 274 */     tkp.Privileges[0] = new WinNT.LUID_AND_ATTRIBUTES(luid, new WinDef.DWORD(2L));
/* 275 */     success = Advapi32.INSTANCE.AdjustTokenPrivileges(hToken.getValue(), false, tkp, 0, null, null);
/* 276 */     if (!success) {
/* 277 */       LOG.error("AdjustTokenPrivileges failed. Error: {}" + Native.getLastError());
/*     */     }
/* 279 */     Kernel32.INSTANCE.CloseHandle(hToken.getValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\windows\WindowsOperatingSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */