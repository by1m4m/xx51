/*     */ package oshi.software.os;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class OSProcess
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3L;
/*  35 */   private String name = "";
/*  36 */   private String path = "";
/*  37 */   private String commandLine = "";
/*  38 */   private String user = "";
/*  39 */   private String userID = "";
/*  40 */   private String group = "";
/*  41 */   private String groupID = "";
/*  42 */   private State state = State.OTHER;
/*     */   
/*     */   private int processID;
/*     */   
/*     */   private int parentProcessID;
/*     */   
/*     */   private int threadCount;
/*     */   
/*     */   private int priority;
/*     */   
/*     */   private long virtualSize;
/*     */   private long residentSetSize;
/*     */   private long kernelTime;
/*     */   private long userTime;
/*     */   private long startTime;
/*     */   private long upTime;
/*     */   private long bytesRead;
/*     */   private long bytesWritten;
/*     */   
/*     */   public static enum State
/*     */   {
/*  63 */     NEW, 
/*     */     
/*     */ 
/*     */ 
/*  67 */     RUNNING, 
/*     */     
/*     */ 
/*     */ 
/*  71 */     SLEEPING, 
/*     */     
/*     */ 
/*     */ 
/*  75 */     WAITING, 
/*     */     
/*     */ 
/*     */ 
/*  79 */     ZOMBIE, 
/*     */     
/*     */ 
/*     */ 
/*  83 */     STOPPED, 
/*     */     
/*     */ 
/*     */ 
/*  87 */     OTHER;
/*     */     
/*     */     private State() {}
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  94 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPath()
/*     */   {
/* 101 */     return this.path;
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
/*     */ 
/*     */   public String getCommandLine()
/*     */   {
/* 117 */     return this.commandLine;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUser()
/*     */   {
/* 125 */     return this.user;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUserID()
/*     */   {
/* 133 */     return this.userID;
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
/*     */ 
/*     */   public String getGroup()
/*     */   {
/* 149 */     return this.group;
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
/*     */ 
/*     */   public String getGroupID()
/*     */   {
/* 165 */     return this.groupID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public State getState()
/*     */   {
/* 172 */     return this.state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getProcessID()
/*     */   {
/* 179 */     return this.processID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getParentProcessID()
/*     */   {
/* 186 */     return this.parentProcessID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getThreadCount()
/*     */   {
/* 193 */     return this.threadCount;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPriority()
/*     */   {
/* 215 */     return this.priority;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getVirtualSize()
/*     */   {
/* 224 */     return this.virtualSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getResidentSetSize()
/*     */   {
/* 235 */     return this.residentSetSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getKernelTime()
/*     */   {
/* 243 */     return this.kernelTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getUserTime()
/*     */   {
/* 251 */     return this.userTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getUpTime()
/*     */   {
/* 258 */     if (this.upTime < this.kernelTime + this.userTime) {
/* 259 */       return this.kernelTime + this.userTime;
/*     */     }
/* 261 */     return this.upTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getStartTime()
/*     */   {
/* 269 */     return this.startTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getBytesRead()
/*     */   {
/* 276 */     return this.bytesRead;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getBytesWritten()
/*     */   {
/* 283 */     return this.bytesWritten;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/* 293 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPath(String path)
/*     */   {
/* 303 */     this.path = path;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCommandLine(String commandLine)
/*     */   {
/* 313 */     this.commandLine = commandLine;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUser(String user)
/*     */   {
/* 323 */     this.user = user;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserID(String userID)
/*     */   {
/* 333 */     this.userID = userID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setGroup(String group)
/*     */   {
/* 343 */     this.group = group;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setGroupID(String groupID)
/*     */   {
/* 353 */     this.groupID = groupID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setState(State state)
/*     */   {
/* 363 */     this.state = state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProcessID(int processID)
/*     */   {
/* 373 */     this.processID = processID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParentProcessID(int parentProcessID)
/*     */   {
/* 383 */     this.parentProcessID = parentProcessID;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThreadCount(int threadCount)
/*     */   {
/* 393 */     this.threadCount = threadCount;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPriority(int priority)
/*     */   {
/* 417 */     this.priority = priority;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVirtualSize(long virtualSize)
/*     */   {
/* 429 */     this.virtualSize = virtualSize;
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
/*     */   public void setResidentSetSize(long residentSetSize)
/*     */   {
/* 443 */     this.residentSetSize = residentSetSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setKernelTime(long kernelTime)
/*     */   {
/* 453 */     this.kernelTime = kernelTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUserTime(long userTime)
/*     */   {
/* 463 */     this.userTime = userTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStartTime(long startTime)
/*     */   {
/* 474 */     this.startTime = startTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUpTime(long upTime)
/*     */   {
/* 484 */     this.upTime = upTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBytesRead(long bytesRead)
/*     */   {
/* 494 */     this.bytesRead = bytesRead;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBytesWritten(long bytesWritten)
/*     */   {
/* 504 */     this.bytesWritten = bytesWritten;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\software\os\OSProcess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */