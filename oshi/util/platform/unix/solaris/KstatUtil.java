/*     */ package oshi.util.platform.unix.solaris;
/*     */ 
/*     */ import com.sun.jna.Native;
/*     */ import com.sun.jna.Pointer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import oshi.jna.platform.unix.solaris.LibKstat;
/*     */ import oshi.jna.platform.unix.solaris.LibKstat.Kstat;
/*     */ import oshi.jna.platform.unix.solaris.LibKstat.KstatCtl;
/*     */ import oshi.jna.platform.unix.solaris.LibKstat.KstatNamed;
/*     */ import oshi.jna.platform.unix.solaris.LibKstat.KstatNamed.UNION;
/*     */ import oshi.jna.platform.unix.solaris.LibKstat.KstatNamed.UNION.STR;
/*     */ import oshi.util.FormatUtil;
/*     */ import oshi.util.Util;
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
/*     */ public class KstatUtil
/*     */ {
/*  43 */   private static final Logger LOG = LoggerFactory.getLogger(KstatUtil.class);
/*     */   
/*  45 */   private static LibKstat.KstatCtl kc = LibKstat.INSTANCE.kstat_open();
/*     */   
/*     */   static {
/*  48 */     Runtime.getRuntime().addShutdownHook(new Thread()
/*     */     {
/*     */       public void run() {
/*  51 */         LibKstat.INSTANCE.kstat_close(KstatUtil.kc);
/*     */       }
/*     */     });
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
/*     */   public static String kstatDataLookupString(LibKstat.Kstat ksp, String name)
/*     */   {
/*  74 */     if ((ksp.ks_type != 1) && (ksp.ks_type != 4)) {
/*  75 */       throw new IllegalArgumentException("Not a kstat_named or kstat_timer kstat.");
/*     */     }
/*  77 */     Pointer p = LibKstat.INSTANCE.kstat_data_lookup(ksp, name);
/*  78 */     if (p == null) {
/*  79 */       LOG.error("Failed lo lookup kstat value for key {}", name);
/*  80 */       return "";
/*     */     }
/*  82 */     LibKstat.KstatNamed data = new LibKstat.KstatNamed(p);
/*  83 */     switch (data.data_type) {
/*     */     case 0: 
/*  85 */       return new String(data.value.charc).trim();
/*     */     case 1: 
/*  87 */       return Integer.toString(data.value.i32);
/*     */     case 2: 
/*  89 */       return Integer.toUnsignedString(data.value.ui32);
/*     */     case 3: 
/*  91 */       return Long.toString(data.value.i64);
/*     */     case 4: 
/*  93 */       return Long.toUnsignedString(data.value.ui64);
/*     */     case 9: 
/*  95 */       return data.value.str.addr.getString(0L);
/*     */     }
/*  97 */     LOG.error("Unimplemented kstat data type {}", Byte.valueOf(data.data_type));
/*  98 */     return "";
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
/*     */   public static long kstatDataLookupLong(LibKstat.Kstat ksp, String name)
/*     */   {
/* 118 */     if ((ksp.ks_type != 1) && (ksp.ks_type != 4)) {
/* 119 */       throw new IllegalArgumentException("Not a kstat_named or kstat_timer kstat.");
/*     */     }
/* 121 */     Pointer p = LibKstat.INSTANCE.kstat_data_lookup(ksp, name);
/* 122 */     if (p == null) {
/* 123 */       LOG.error("Failed lo lookup kstat value on {}:{}:{} for key {}", new Object[] { new String(ksp.ks_module).trim(), 
/* 124 */         Integer.valueOf(ksp.ks_instance), new String(ksp.ks_name).trim(), name });
/* 125 */       return 0L;
/*     */     }
/* 127 */     LibKstat.KstatNamed data = new LibKstat.KstatNamed(p);
/* 128 */     switch (data.data_type) {
/*     */     case 1: 
/* 130 */       return data.value.i32;
/*     */     case 2: 
/* 132 */       return FormatUtil.getUnsignedInt(data.value.ui32);
/*     */     case 3: 
/* 134 */       return data.value.i64;
/*     */     case 4: 
/* 136 */       return data.value.ui64;
/*     */     }
/* 138 */     LOG.error("Unimplemented or non-numeric kstat data type {}", Byte.valueOf(data.data_type));
/* 139 */     return 0L;
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
/*     */   public static boolean kstatRead(LibKstat.Kstat ksp)
/*     */   {
/* 156 */     int retry = 0;
/* 157 */     while (0 > LibKstat.INSTANCE.kstat_read(kc, ksp, null)) {
/* 158 */       if ((11 != Native.getLastError()) || (5 <= ++retry)) {
/* 159 */         LOG.error("Failed to read kstat {}:{}:{}", new Object[] { new String(ksp.ks_module).trim(), Integer.valueOf(ksp.ks_instance), new String(ksp.ks_name)
/* 160 */           .trim() });
/* 161 */         return false;
/*     */       }
/* 163 */       Util.sleep(8 << retry);
/*     */     }
/* 165 */     return true;
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
/*     */   public static LibKstat.Kstat kstatLookup(String module, int instance, String name)
/*     */   {
/* 185 */     int ret = LibKstat.INSTANCE.kstat_chain_update(kc);
/* 186 */     if (ret < 0) {
/* 187 */       LOG.error("Failed to update kstat chain");
/* 188 */       return null;
/*     */     }
/* 190 */     return LibKstat.INSTANCE.kstat_lookup(kc, module, instance, name);
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
/*     */   public static List<LibKstat.Kstat> kstatLookupAll(String module, int instance, String name)
/*     */   {
/* 210 */     List<LibKstat.Kstat> kstats = new ArrayList();
/* 211 */     int ret = LibKstat.INSTANCE.kstat_chain_update(kc);
/* 212 */     if (ret < 0) {
/* 213 */       LOG.error("Failed to update kstat chain");
/* 214 */       return kstats;
/*     */     }
/* 216 */     for (LibKstat.Kstat ksp = LibKstat.INSTANCE.kstat_lookup(kc, module, instance, name); ksp != null; ksp = ksp.next()) {
/* 217 */       if (((module == null) || (module.equals(new String(ksp.ks_module).trim()))) && ((instance < 0) || (instance == ksp.ks_instance)) && ((name == null) || 
/*     */       
/* 219 */         (name.equals(new String(ksp.ks_name).trim())))) {
/* 220 */         kstats.add(ksp);
/*     */       }
/*     */     }
/* 223 */     return kstats;
/*     */   }
/*     */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\util\platform\unix\solaris\KstatUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */