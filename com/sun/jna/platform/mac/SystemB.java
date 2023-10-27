/*     */ package com.sun.jna.platform.mac;
/*     */ 
/*     */ import com.sun.jna.Library;
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.Structure;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import com.sun.jna.ptr.LongByReference;
/*     */ import com.sun.jna.ptr.PointerByReference;
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
/*     */ public abstract interface SystemB
/*     */   extends Library
/*     */ {
/*  36 */   public static final SystemB INSTANCE = (SystemB)Native.loadLibrary("System", SystemB.class);
/*     */   
/*     */   public static final int HOST_LOAD_INFO = 1;
/*     */   
/*     */   public static final int HOST_VM_INFO = 2;
/*     */   
/*     */   public static final int HOST_CPU_LOAD_INFO = 3;
/*     */   
/*     */   public static final int HOST_VM_INFO64 = 4;
/*     */   
/*     */   public static final int CPU_STATE_MAX = 4;
/*     */   
/*     */   public static final int CPU_STATE_USER = 0;
/*     */   
/*     */   public static final int CPU_STATE_SYSTEM = 1;
/*     */   
/*     */   public static final int CPU_STATE_IDLE = 2;
/*     */   
/*     */   public static final int CPU_STATE_NICE = 3;
/*     */   
/*     */   public static final int PROCESSOR_BASIC_INFO = 1;
/*     */   
/*     */   public static final int PROCESSOR_CPU_LOAD_INFO = 2;
/*  59 */   public static final int UINT64_SIZE = Native.getNativeSize(Long.TYPE);
/*  60 */   public static final int INT_SIZE = Native.getNativeSize(Integer.TYPE);
/*     */   
/*     */   public static class HostCpuLoadInfo extends Structure {
/*  63 */     public int[] cpu_ticks = new int[4];
/*     */     
/*     */ 
/*  66 */     protected List<String> getFieldOrder() { return Arrays.asList(new String[] { "cpu_ticks" }); }
/*     */   }
/*     */   
/*     */   public abstract int mach_host_self();
/*     */   
/*  71 */   public static class HostLoadInfo extends Structure { public int[] avenrun = new int[3];
/*  72 */     public int[] mach_factor = new int[3];
/*     */     
/*     */     protected List<String> getFieldOrder() {
/*  75 */       return Arrays.asList(new String[] { "avenrun", "mach_factor" });
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract int host_page_size(int paramInt, LongByReference paramLongByReference);
/*     */   
/*     */   public abstract int host_statistics(int paramInt1, int paramInt2, Structure paramStructure, IntByReference paramIntByReference);
/*     */   
/*     */   public abstract int host_statistics64(int paramInt1, int paramInt2, Structure paramStructure, IntByReference paramIntByReference);
/*     */   
/*     */   public static class VMStatistics
/*     */     extends Structure
/*     */   {
/*     */     public int free_count;
/*     */     public int active_count;
/*     */     public int inactive_count;
/*     */     public int wire_count;
/*     */     public int zero_fill_count;
/*     */     public int reactivations;
/*     */     public int pageins;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/*  98 */       return Arrays.asList(new String[] { "free_count", "active_count", "inactive_count", "wire_count", "zero_fill_count", "reactivations", "pageins", "pageouts", "faults", "cow_faults", "lookups", "hits", "purgeable_count", "purges", "speculative_count" });
/*     */     }
/*     */     
/*     */ 
/*     */     public int pageouts;
/*     */     
/*     */     public int faults;
/*     */     
/*     */     public int cow_faults;
/*     */     public int lookups;
/*     */     public int hits;
/*     */     public int purgeable_count;
/*     */     public int purges;
/*     */     public int speculative_count;
/*     */   }
/*     */   
/*     */   public abstract int sysctl(int[] paramArrayOfInt, int paramInt1, Pointer paramPointer1, IntByReference paramIntByReference, Pointer paramPointer2, int paramInt2);
/*     */   
/*     */   public abstract int sysctlbyname(String paramString, Pointer paramPointer1, IntByReference paramIntByReference, Pointer paramPointer2, int paramInt);
/*     */   
/*     */   public abstract int sysctlnametomib(String paramString, Pointer paramPointer, IntByReference paramIntByReference);
/*     */   
/*     */   public abstract int host_processor_info(int paramInt1, int paramInt2, IntByReference paramIntByReference1, PointerByReference paramPointerByReference, IntByReference paramIntByReference2);
/*     */   
/*     */   public static class VMStatistics64
/*     */     extends Structure
/*     */   {
/*     */     public int free_count;
/*     */     public int active_count;
/*     */     public int inactive_count;
/*     */     public int wire_count;
/*     */     public long zero_fill_count;
/*     */     public long reactivations;
/*     */     public long pageins;
/*     */     public long pageouts;
/*     */     public long faults;
/*     */     public long cow_faults;
/*     */     public long lookups;
/*     */     public long hits;
/*     */     
/*     */     protected List<String> getFieldOrder()
/*     */     {
/* 140 */       return Arrays.asList(new String[] { "free_count", "active_count", "inactive_count", "wire_count", "zero_fill_count", "reactivations", "pageins", "pageouts", "faults", "cow_faults", "lookups", "hits", "purges", "purgeable_count", "speculative_count", "decompressions", "compressions", "swapins", "swapouts", "compressor_page_count", "throttled_count", "external_page_count", "internal_page_count", "total_uncompressed_pages_in_compressor" });
/*     */     }
/*     */     
/*     */     public long purges;
/*     */     public int purgeable_count;
/*     */     public int speculative_count;
/*     */     public long decompressions;
/*     */     public long compressions;
/*     */     public long swapins;
/*     */     public long swapouts;
/*     */     public int compressor_page_count;
/*     */     public int throttled_count;
/*     */     public int external_page_count;
/*     */     public int internal_page_count;
/*     */     public long total_uncompressed_pages_in_compressor;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\com\sun\jna\platform\mac\SystemB.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */