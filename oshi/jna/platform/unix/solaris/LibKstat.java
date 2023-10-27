/*     */ package oshi.jna.platform.unix.solaris;
/*     */ 
/*     */ import com.sun.jna.Library;
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.Structure;
/*     */ import com.sun.jna.Union;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
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
/*     */ public abstract interface LibKstat
/*     */   extends Library
/*     */ {
/*  40 */   public static final LibKstat INSTANCE = (LibKstat)Native.loadLibrary("kstat", LibKstat.class);
/*     */   
/*     */   public static final byte KSTAT_TYPE_RAW = 0;
/*     */   
/*     */   public static final byte KSTAT_TYPE_NAMED = 1;
/*     */   
/*     */   public static final byte KSTAT_TYPE_INTR = 2;
/*     */   
/*     */   public static final byte KSTAT_TYPE_IO = 3;
/*     */   
/*     */   public static final byte KSTAT_TYPE_TIMER = 4;
/*     */   
/*     */   public static final byte KSTAT_DATA_CHAR = 0;
/*     */   public static final byte KSTAT_DATA_INT32 = 1;
/*     */   public static final byte KSTAT_DATA_UINT32 = 2;
/*     */   public static final byte KSTAT_DATA_INT64 = 3;
/*     */   public static final byte KSTAT_DATA_UINT64 = 4;
/*     */   public static final byte KSTAT_DATA_STRING = 9;
/*     */   public static final int KSTAT_INTR_HARD = 0;
/*     */   public static final int KSTAT_INTR_SOFT = 1;
/*     */   public static final int KSTAT_INTR_WATCHDOG = 2;
/*     */   public static final int KSTAT_INTR_SPURIOUS = 3;
/*     */   public static final int KSTAT_INTR_MULTSVC = 4;
/*     */   public static final int KSTAT_NUM_INTRS = 5;
/*     */   public static final int KSTAT_STRLEN = 31;
/*     */   public static final int EAGAIN = 11;
/*     */   
/*     */   public abstract KstatCtl kstat_open();
/*     */   
/*     */   public abstract int kstat_close(KstatCtl paramKstatCtl);
/*     */   
/*     */   public abstract int kstat_chain_update(KstatCtl paramKstatCtl);
/*     */   
/*     */   public abstract int kstat_read(KstatCtl paramKstatCtl, Kstat paramKstat, Pointer paramPointer);
/*     */   
/*     */   public abstract int kstat_write(KstatCtl paramKstatCtl, Kstat paramKstat, Pointer paramPointer);
/*     */   
/*     */   public abstract Kstat kstat_lookup(KstatCtl paramKstatCtl, String paramString1, int paramInt, String paramString2);
/*     */   
/*     */   public abstract Pointer kstat_data_lookup(Kstat paramKstat, String paramString);
/*     */   
/*     */   public static class Kstat
/*     */     extends Structure
/*     */   {
/*     */     public long ks_crtime;
/*     */     public Pointer ks_next;
/*     */     public int ks_kid;
/*  87 */     public byte[] ks_module = new byte[31];
/*     */     public byte ks_resv;
/*     */     public int ks_instance;
/*  90 */     public byte[] ks_name = new byte[31];
/*     */     public byte ks_type;
/*  92 */     public byte[] ks_class = new byte[31];
/*     */     
/*     */     public byte ks_flags;
/*     */     public Pointer ks_data;
/*     */     public int ks_ndata;
/*     */     public long ks_data_size;
/*     */     public long ks_snaptime;
/*     */     public int ks_update;
/*     */     public Pointer ks_private;
/*     */     public int ks_snapshot;
/*     */     public Pointer ks_lock;
/*     */     
/*     */     public Kstat next()
/*     */     {
/* 106 */       if (this.ks_next == null) {
/* 107 */         return null;
/*     */       }
/* 109 */       Kstat n = new Kstat();
/* 110 */       n.useMemory(this.ks_next);
/* 111 */       n.read();
/* 112 */       return n;
/*     */     }
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 117 */       return Arrays.asList(new String[] { "ks_crtime", "ks_next", "ks_kid", "ks_module", "ks_resv", "ks_instance", "ks_name", "ks_type", "ks_class", "ks_flags", "ks_data", "ks_ndata", "ks_data_size", "ks_snaptime", "ks_update", "ks_private", "ks_snapshot", "ks_lock" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class KstatNamed
/*     */     extends Structure
/*     */   {
/* 127 */     public byte[] name = new byte[31];
/*     */     public byte data_type;
/*     */     public UNION value;
/*     */     public KstatNamed() {}
/*     */     
/* 132 */     public static class UNION extends Union { public byte[] charc = new byte[16];
/*     */       public int i32;
/*     */       public int ui32;
/*     */       public long i64;
/*     */       public long ui64;
/*     */       public STR str;
/*     */       
/*     */       public static class STR extends Structure
/*     */       {
/*     */         public Pointer addr;
/*     */         public int len;
/*     */         
/*     */         protected List<String> getFieldOrder() {
/* 145 */           return Arrays.asList(new String[] { "addr", "len" });
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public KstatNamed(Pointer p)
/*     */     {
/* 155 */       super();
/* 156 */       read();
/*     */     }
/*     */     
/*     */     public void read()
/*     */     {
/* 161 */       super.read();
/* 162 */       switch (this.data_type) {
/*     */       case 0: 
/* 164 */         this.value.setType(byte[].class);
/* 165 */         break;
/*     */       case 9: 
/* 167 */         this.value.setType(LibKstat.KstatNamed.UNION.STR.class);
/* 168 */         break;
/*     */       case 1: 
/*     */       case 2: 
/* 171 */         this.value.setType(Integer.TYPE);
/* 172 */         break;
/*     */       case 3: 
/*     */       case 4: 
/* 175 */         this.value.setType(Long.TYPE);
/* 176 */         break;
/*     */       }
/*     */       
/*     */       
/* 180 */       this.value.read();
/*     */     }
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 185 */       return Arrays.asList(new String[] { "name", "data_type", "value" });
/*     */     }
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
/*     */   public static class KstatIntr
/*     */     extends Structure
/*     */   {
/* 200 */     public int[] intrs = new int[5];
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 204 */       return Arrays.asList(new String[] { "intrs" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class KstatTimer
/*     */     extends Structure
/*     */   {
/* 215 */     public byte[] name = new byte[31];
/*     */     public byte resv;
/*     */     public long num_events;
/*     */     public long elapsed_time;
/*     */     public long min_time;
/*     */     public long max_time;
/*     */     public long start_time;
/*     */     public long stop_time;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 226 */       return Arrays.asList(new String[] { "name", "resv", "num_events", "elapsed_time", "min_time", "max_time", "start_time", "stop_time" });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class KstatIO
/*     */     extends Structure
/*     */   {
/*     */     public long nread;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public long nwritten;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int reads;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public int writes;
/*     */     
/*     */ 
/*     */ 
/*     */     public long wtime;
/*     */     
/*     */ 
/*     */ 
/*     */     public long wlentime;
/*     */     
/*     */ 
/*     */ 
/*     */     public long wlastupdate;
/*     */     
/*     */ 
/*     */ 
/*     */     public long rtime;
/*     */     
/*     */ 
/*     */ 
/*     */     public long rlentime;
/*     */     
/*     */ 
/*     */ 
/*     */     public long rlastupdate;
/*     */     
/*     */ 
/*     */ 
/*     */     public int wcnt;
/*     */     
/*     */ 
/*     */ 
/*     */     public int rcnt;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public KstatIO() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public KstatIO(Pointer p)
/*     */     {
/* 296 */       super();
/* 297 */       read();
/*     */     }
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 302 */       return Arrays.asList(new String[] { "nread", "nwritten", "reads", "writes", "wtime", "wlentime", "wlastupdate", "rtime", "rlentime", "rlastupdate", "wcnt", "rcnt" });
/*     */     }
/*     */   }
/*     */   
/*     */   public static class KstatCtl extends Structure
/*     */   {
/*     */     public int kc_chain_id;
/*     */     public LibKstat.Kstat kc_chain;
/*     */     public int kc_kd;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 314 */       return Arrays.asList(new String[] { "kc_chain_id", "kc_chain", "kc_kd" });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\unix\solaris\LibKstat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */