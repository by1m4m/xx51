/*    */ package oshi.hardware.platform.linux;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import oshi.jna.platform.linux.Udev;
/*    */ import oshi.jna.platform.linux.Udev.UdevDevice;
/*    */ import oshi.util.ParseUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LinuxBlockDevStats
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   public final String device;
/*    */   public final long read_ops;
/*    */   public final long read_merged;
/*    */   public final long read_512bytes;
/*    */   public final long read_waits_ms;
/*    */   public final long write_ops;
/*    */   public final long write_merged;
/*    */   public final long write_512bytes;
/*    */   public final long write_waits_ms;
/*    */   public final long in_flight;
/*    */   public final long active_ms;
/*    */   public final long waits_ms;
/*    */   
/*    */   public LinuxBlockDevStats(String device, Udev.UdevDevice disk)
/*    */   {
/* 64 */     String devstat = Udev.INSTANCE.udev_device_get_sysattr_value(disk, "stat");
/* 65 */     String[] splitstats = devstat.split("\\s+");
/*    */     
/* 67 */     if (splitstats.length < 11) {
/* 68 */       throw new IllegalStateException("Unexpected length of array: " + splitstats.length);
/*    */     }
/*    */     
/*    */ 
/* 72 */     int startIndex = splitstats.length - 11;
/*    */     
/* 74 */     this.device = device;
/* 75 */     this.read_ops = ParseUtil.parseLongOrDefault(splitstats[(startIndex + 0)], 0L);
/* 76 */     this.read_merged = ParseUtil.parseLongOrDefault(splitstats[(startIndex + 1)], 0L);
/* 77 */     this.read_512bytes = ParseUtil.parseLongOrDefault(splitstats[(startIndex + 2)], 0L);
/* 78 */     this.read_waits_ms = ParseUtil.parseLongOrDefault(splitstats[(startIndex + 3)], 0L);
/* 79 */     this.write_ops = ParseUtil.parseLongOrDefault(splitstats[(startIndex + 4)], 0L);
/* 80 */     this.write_merged = ParseUtil.parseLongOrDefault(splitstats[(startIndex + 5)], 0L);
/* 81 */     this.write_512bytes = ParseUtil.parseLongOrDefault(splitstats[(startIndex + 6)], 0L);
/* 82 */     this.write_waits_ms = ParseUtil.parseLongOrDefault(splitstats[(startIndex + 7)], 0L);
/* 83 */     this.in_flight = ParseUtil.parseLongOrDefault(splitstats[(startIndex + 8)], 0L);
/* 84 */     this.active_ms = ParseUtil.parseLongOrDefault(splitstats[(startIndex + 9)], 0L);
/* 85 */     this.waits_ms = ParseUtil.parseLongOrDefault(splitstats[(startIndex + 10)], 0L);
/*    */   }
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\hardware\platform\linux\LinuxBlockDevStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */