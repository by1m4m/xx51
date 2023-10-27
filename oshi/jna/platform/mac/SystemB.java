/*     */ package oshi.jna.platform.mac;
/*     */ 
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.NativeLong;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.Structure;
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import oshi.jna.platform.unix.CLibrary;
/*     */ import oshi.jna.platform.unix.CLibrary.Timeval;
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
/*     */ public abstract interface SystemB
/*     */   extends CLibrary, com.sun.jna.platform.mac.SystemB
/*     */ {
/*  39 */   public static final SystemB INSTANCE = (SystemB)Native.loadLibrary("System", SystemB.class);
/*     */   public static final int MAXCOMLEN = 16;
/*     */   public static final int MAXPATHLEN = 1024;
/*     */   public static final int PROC_PIDPATHINFO_MAXSIZE = 4096;
/*     */   public static final int PROC_ALL_PIDS = 1;
/*     */   public static final int PROC_PIDTASKALLINFO = 2;
/*     */   public static final int PROC_PIDTBSDINFO = 3;
/*     */   public static final int PROC_PIDTASKINFO = 4;
/*     */   public static final int MFSTYPENAMELEN = 16;
/*     */   public static final int MNAMELEN = 1024;
/*     */   public static final int MNT_WAIT = 1;
/*     */   public static final int MNT_NOWAIT = 16;
/*     */   public static final int MNT_DWAIT = 256;
/*     */   public static final int RUSAGE_INFO_V2 = 2;
/*     */   
/*     */   public abstract Passwd getpwuid(int paramInt);
/*     */   
/*     */   public abstract Group getgrgid(int paramInt);
/*     */   
/*     */   public abstract int mach_task_self();
/*     */   
/*     */   public abstract int proc_listpids(int paramInt1, int paramInt2, int[] paramArrayOfInt, int paramInt3);
/*     */   
/*     */   public abstract int proc_pidinfo(int paramInt1, int paramInt2, long paramLong, Structure paramStructure, int paramInt3);
/*     */   
/*     */   public abstract int proc_pidpath(int paramInt1, Pointer paramPointer, int paramInt2);
/*     */   
/*     */   public abstract int proc_pid_rusage(int paramInt1, int paramInt2, RUsageInfoV2 paramRUsageInfoV2);
/*     */   
/*     */   public abstract int getfsstat64(Statfs[] paramArrayOfStatfs, int paramInt1, int paramInt2);
/*     */   
/*     */   public static class ProcTaskAllInfo extends Structure {
/*  71 */     protected List<String> getFieldOrder() { return Arrays.asList(new String[] { "pbsd", "ptinfo" }); }
/*     */     
/*     */     public SystemB.ProcBsdInfo pbsd;
/*     */     public SystemB.ProcTaskInfo ptinfo; }
/*     */   
/*     */   public static class ProcBsdInfo extends Structure { public int pbi_flags;
/*     */     public int pbi_status;
/*     */     public int pbi_xstatus;
/*     */     public int pbi_pid;
/*     */     public int pbi_ppid;
/*     */     public int pbi_uid;
/*     */     public int pbi_gid;
/*     */     public int pbi_ruid;
/*     */     public int pbi_rgid;
/*     */     public int pbi_svuid;
/*     */     public int pbi_svgid;
/*     */     public int rfu_1;
/*  88 */     public byte[] pbi_comm = new byte[16];
/*  89 */     public byte[] pbi_name = new byte[32];
/*     */     public int pbi_nfiles;
/*     */     public int pbi_pgid;
/*     */     public int pbi_pjobc;
/*     */     public int e_tdev;
/*     */     public int e_tpgid;
/*     */     public int pbi_nice;
/*     */     public long pbi_start_tvsec;
/*     */     public long pbi_start_tvusec;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 101 */       return Arrays.asList(new String[] { "pbi_flags", "pbi_status", "pbi_xstatus", "pbi_pid", "pbi_ppid", "pbi_uid", "pbi_gid", "pbi_ruid", "pbi_rgid", "pbi_svuid", "pbi_svgid", "rfu_1", "pbi_comm", "pbi_name", "pbi_nfiles", "pbi_pgid", "pbi_pjobc", "e_tdev", "e_tpgid", "pbi_nice", "pbi_start_tvsec", "pbi_start_tvusec" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class ProcTaskInfo
/*     */     extends Structure
/*     */   {
/*     */     public long pti_virtual_size;
/*     */     public long pti_resident_size;
/*     */     public long pti_total_user;
/*     */     public long pti_total_system;
/*     */     public long pti_threads_user;
/*     */     public long pti_threads_system;
/*     */     public int pti_policy;
/*     */     public int pti_faults;
/*     */     public int pti_pageins;
/*     */     public int pti_cow_faults;
/*     */     public int pti_messages_sent;
/*     */     public int pti_messages_received;
/*     */     public int pti_syscalls_mach;
/*     */     public int pti_syscalls_unix;
/*     */     public int pti_csw;
/*     */     public int pti_threadnum;
/*     */     public int pti_numrunning;
/*     */     public int pti_priority;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 130 */       return Arrays.asList(new String[] { "pti_virtual_size", "pti_resident_size", "pti_total_user", "pti_total_system", "pti_threads_user", "pti_threads_system", "pti_policy", "pti_faults", "pti_pageins", "pti_cow_faults", "pti_messages_sent", "pti_messages_received", "pti_syscalls_mach", "pti_syscalls_unix", "pti_csw", "pti_threadnum", "pti_numrunning", "pti_priority" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class RUsageInfoV2
/*     */     extends Structure
/*     */   {
/* 138 */     public byte[] ri_uuid = new byte[16];
/*     */     public long ri_user_time;
/*     */     public long ri_system_time;
/*     */     public long ri_pkg_idle_wkups;
/*     */     public long ri_interrupt_wkups;
/*     */     public long ri_pageins;
/*     */     public long ri_wired_size;
/*     */     public long ri_resident_size;
/*     */     public long ri_phys_footprint;
/*     */     public long ri_proc_start_abstime;
/*     */     public long ri_proc_exit_abstime;
/*     */     public long ri_child_user_time;
/*     */     public long ri_child_system_time;
/*     */     public long ri_child_pkg_idle_wkups;
/*     */     public long ri_child_interrupt_wkups;
/*     */     public long ri_child_pageins;
/*     */     public long ri_child_elapsed_abstime;
/*     */     public long ri_diskio_bytesread;
/*     */     public long ri_diskio_byteswritten;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 160 */       return Arrays.asList(new String[] { "ri_uuid", "ri_user_time", "ri_system_time", "ri_pkg_idle_wkups", "ri_interrupt_wkups", "ri_pageins", "ri_wired_size", "ri_resident_size", "ri_phys_footprint", "ri_proc_start_abstime", "ri_proc_exit_abstime", "ri_child_user_time", "ri_child_system_time", "ri_child_pkg_idle_wkups", "ri_child_interrupt_wkups", "ri_child_pageins", "ri_child_elapsed_abstime", "ri_diskio_bytesread", "ri_diskio_byteswritten" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Statfs
/*     */     extends Structure
/*     */   {
/*     */     public int f_bsize;
/*     */     
/*     */     public int f_iosize;
/*     */     
/*     */     public long f_blocks;
/*     */     
/*     */     public long f_bfree;
/*     */     
/*     */     public long f_bavail;
/*     */     
/*     */     public long f_files;
/*     */     
/*     */     public long f_ffree;
/* 181 */     public int[] f_fsid = new int[2];
/*     */     
/*     */     public int f_owner;
/*     */     public int f_type;
/*     */     public int f_flags;
/*     */     public int f_fssubtype;
/* 187 */     public byte[] f_fstypename = new byte[16];
/*     */     
/* 189 */     public byte[] f_mntonname = new byte['Ѐ'];
/*     */     
/* 191 */     public byte[] f_mntfromname = new byte['Ѐ'];
/* 192 */     public int[] f_reserved = new int[8];
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 196 */       return Arrays.asList(new String[] { "f_bsize", "f_iosize", "f_blocks", "f_bfree", "f_bavail", "f_files", "f_ffree", "f_fsid", "f_owner", "f_type", "f_flags", "f_fssubtype", "f_fstypename", "f_mntonname", "f_mntfromname", "f_reserved" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class XswUsage
/*     */     extends Structure
/*     */   {
/*     */     public long xsu_total;
/*     */     
/*     */     public long xsu_avail;
/*     */     
/*     */     public long xsu_used;
/*     */     
/*     */     public int xsu_pagesize;
/*     */     public boolean xsu_encrypted;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 215 */       return Arrays.asList(new String[] { "xsu_total", "xsu_avail", "xsu_used", "xsu_pagesize", "xsu_encrypted" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class IFdata
/*     */     extends Structure
/*     */   {
/*     */     public byte ifi_type;
/*     */     public byte ifi_typelen;
/*     */     public byte ifi_physical;
/*     */     public byte ifi_addrlen;
/*     */     public byte ifi_hdrlen;
/*     */     public byte ifi_recvquota;
/*     */     public byte ifi_xmitquota;
/*     */     public byte ifi_unused1;
/*     */     public int ifi_mtu;
/*     */     public int ifi_metric;
/*     */     public int ifi_baudrate;
/*     */     public int ifi_ipackets;
/*     */     public int ifi_ierrors;
/*     */     public int ifi_opackets;
/*     */     public int ifi_oerrors;
/*     */     public int ifi_collisions;
/*     */     public int ifi_ibytes;
/*     */     public int ifi_obytes;
/*     */     public int ifi_imcasts;
/*     */     public int ifi_omcasts;
/*     */     public int ifi_iqdrops;
/*     */     public int ifi_noproto;
/*     */     public int ifi_recvtiming;
/*     */     public int ifi_xmittiming;
/*     */     public CLibrary.Timeval ifi_lastchange;
/*     */     public int ifi_unused2;
/*     */     public int ifi_hwassist;
/*     */     public int ifi_reserved1;
/*     */     public int ifi_reserved2;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 255 */       return Arrays.asList(new String[] { "ifi_type", "ifi_typelen", "ifi_physical", "ifi_addrlen", "ifi_hdrlen", "ifi_recvquota", "ifi_xmitquota", "ifi_unused1", "ifi_mtu", "ifi_metric", "ifi_baudrate", "ifi_ipackets", "ifi_ierrors", "ifi_opackets", "ifi_oerrors", "ifi_collisions", "ifi_ibytes", "ifi_obytes", "ifi_imcasts", "ifi_omcasts", "ifi_iqdrops", "ifi_noproto", "ifi_recvtiming", "ifi_xmittiming", "ifi_lastchange", "ifi_unused2", "ifi_hwassist", "ifi_reserved1", "ifi_reserved2" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class IFmsgHdr
/*     */     extends Structure
/*     */   {
/*     */     public short ifm_msglen;
/*     */     
/*     */     public byte ifm_version;
/*     */     
/*     */     public byte ifm_type;
/*     */     
/*     */     public int ifm_addrs;
/*     */     
/*     */     public int ifm_flags;
/*     */     
/*     */     public short ifm_index;
/*     */     
/*     */     public SystemB.IFdata ifm_data;
/*     */     
/*     */     public IFmsgHdr() {}
/*     */     
/*     */     public IFmsgHdr(Pointer p)
/*     */     {
/* 281 */       super();
/*     */     }
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 286 */       return Arrays.asList(new String[] { "ifm_msglen", "ifm_version", "ifm_type", "ifm_addrs", "ifm_flags", "ifm_index", "ifm_data" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class IFdata64
/*     */     extends Structure
/*     */   {
/*     */     public byte ifi_type;
/*     */     
/*     */     public byte ifi_typelen;
/*     */     public byte ifi_physical;
/*     */     public byte ifi_addrlen;
/*     */     public byte ifi_hdrlen;
/*     */     public byte ifi_recvquota;
/*     */     public byte ifi_xmitquota;
/*     */     public byte ifi_unused1;
/*     */     public int ifi_mtu;
/*     */     public int ifi_metric;
/*     */     public long ifi_baudrate;
/*     */     public long ifi_ipackets;
/*     */     public long ifi_ierrors;
/*     */     public long ifi_opackets;
/*     */     public long ifi_oerrors;
/*     */     public long ifi_collisions;
/*     */     public long ifi_ibytes;
/*     */     public long ifi_obytes;
/*     */     public long ifi_imcasts;
/*     */     public long ifi_omcasts;
/*     */     public long ifi_iqdrops;
/*     */     public long ifi_noproto;
/*     */     public int ifi_recvtiming;
/*     */     public int ifi_xmittiming;
/*     */     public CLibrary.Timeval ifi_lastchange;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 323 */       return Arrays.asList(new String[] { "ifi_type", "ifi_typelen", "ifi_physical", "ifi_addrlen", "ifi_hdrlen", "ifi_recvquota", "ifi_xmitquota", "ifi_unused1", "ifi_mtu", "ifi_metric", "ifi_baudrate", "ifi_ipackets", "ifi_ierrors", "ifi_opackets", "ifi_oerrors", "ifi_collisions", "ifi_ibytes", "ifi_obytes", "ifi_imcasts", "ifi_omcasts", "ifi_iqdrops", "ifi_noproto", "ifi_recvtiming", "ifi_xmittiming", "ifi_lastchange" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class IFmsgHdr2
/*     */     extends Structure
/*     */   {
/*     */     public short ifm_msglen;
/*     */     
/*     */     public byte ifm_version;
/*     */     
/*     */     public byte ifm_type;
/*     */     
/*     */     public int ifm_addrs;
/*     */     public int ifm_flags;
/*     */     public short ifm_index;
/*     */     public int ifm_snd_len;
/*     */     public int ifm_snd_maxlen;
/*     */     public int ifm_snd_drops;
/*     */     public int ifm_timer;
/*     */     public SystemB.IFdata64 ifm_data;
/*     */     
/*     */     public IFmsgHdr2(Pointer p)
/*     */     {
/* 348 */       super();
/*     */     }
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 353 */       return Arrays.asList(new String[] { "ifm_msglen", "ifm_version", "ifm_type", "ifm_addrs", "ifm_flags", "ifm_index", "ifm_snd_len", "ifm_snd_maxlen", "ifm_snd_drops", "ifm_timer", "ifm_data" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Passwd
/*     */     extends Structure
/*     */   {
/*     */     public String pw_name;
/*     */     
/*     */     public String pw_passwd;
/*     */     public int pw_uid;
/*     */     public int pw_gid;
/*     */     public NativeLong pw_change;
/*     */     public String pw_class;
/*     */     public String pw_gecos;
/*     */     public String pw_dir;
/*     */     public String pw_shell;
/*     */     public NativeLong pw_expire;
/*     */     public int pw_fields;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 376 */       return Arrays.asList(new String[] { "pw_name", "pw_passwd", "pw_uid", "pw_gid", "pw_change", "pw_class", "pw_gecos", "pw_dir", "pw_shell", "pw_expire", "pw_fields" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Group
/*     */     extends Structure
/*     */   {
/*     */     public String gr_name;
/*     */     
/*     */     public String gr_passwd;
/*     */     public int gr_gid;
/*     */     public PointerByReference gr_mem;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 392 */       return Arrays.asList(new String[] { "gr_name", "gr_passwd", "gr_gid", "gr_mem" });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\mac\SystemB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */