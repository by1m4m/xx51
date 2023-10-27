/*     */ package oshi.util.platform.unix.freebsd;
/*     */ 
/*     */ import com.sun.jna.Memory;
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Pointer;
/*     */ import com.sun.jna.Structure;
/*     */ import com.sun.jna.ptr.IntByReference;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.jna.platform.unix.freebsd.Libc;
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
/*     */ public class BsdSysctlUtil
/*     */ {
/*  38 */   private static final Logger LOG = LoggerFactory.getLogger(BsdSysctlUtil.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String SYSCTL_FAIL = "Failed syctl call: {}, Error code: {}";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static int sysctl(String name, int def)
/*     */   {
/*  55 */     IntByReference size = new IntByReference(Libc.INT_SIZE);
/*  56 */     Pointer p = new Memory(size.getValue());
/*  57 */     if (0 != Libc.INSTANCE.sysctlbyname(name, p, size, null, 0)) {
/*  58 */       LOG.error("Failed sysctl call: {}, Error code: {}", name, Integer.valueOf(Native.getLastError()));
/*  59 */       return def;
/*     */     }
/*  61 */     return p.getInt(0L);
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
/*     */   public static long sysctl(String name, long def)
/*     */   {
/*  74 */     IntByReference size = new IntByReference(Libc.UINT64_SIZE);
/*  75 */     Pointer p = new Memory(size.getValue());
/*  76 */     if (0 != Libc.INSTANCE.sysctlbyname(name, p, size, null, 0)) {
/*  77 */       LOG.error("Failed syctl call: {}, Error code: {}", name, Integer.valueOf(Native.getLastError()));
/*  78 */       return def;
/*     */     }
/*  80 */     return p.getLong(0L);
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
/*     */   public static String sysctl(String name, String def)
/*     */   {
/*  95 */     IntByReference size = new IntByReference();
/*  96 */     if (0 != Libc.INSTANCE.sysctlbyname(name, null, size, null, 0)) {
/*  97 */       LOG.error("Failed syctl call: {}, Error code: {}", name, Integer.valueOf(Native.getLastError()));
/*  98 */       return def;
/*     */     }
/*     */     
/* 101 */     Pointer p = new Memory(size.getValue() + 1);
/* 102 */     if (0 != Libc.INSTANCE.sysctlbyname(name, p, size, null, 0)) {
/* 103 */       LOG.error("Failed syctl call: {}, Error code: {}", name, Integer.valueOf(Native.getLastError()));
/* 104 */       return def;
/*     */     }
/* 106 */     return p.getString(0L);
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
/*     */   public static boolean sysctl(String name, Structure struct)
/*     */   {
/* 119 */     if (0 != Libc.INSTANCE.sysctlbyname(name, struct.getPointer(), new IntByReference(struct.size()), null, 0)) {
/* 120 */       LOG.error("Failed syctl call: {}, Error code: {}", name, Integer.valueOf(Native.getLastError()));
/* 121 */       return false;
/*     */     }
/* 123 */     struct.read();
/* 124 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\util\platform\unix\freebsd\BsdSysctlUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */