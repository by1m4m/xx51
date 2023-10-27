/*    */ package oshi.jna.platform.linux;
/*    */ 
/*    */ import com.sun.jna.Library;
/*    */ import com.sun.jna.Native;
/*    */ import com.sun.jna.Pointer;
/*    */ import com.sun.jna.PointerType;
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
/*    */ public abstract interface Udev
/*    */   extends Library
/*    */ {
/* 33 */   public static final Udev INSTANCE = (Udev)Native.loadLibrary("udev", Udev.class);
/*    */   
/*    */   public abstract UdevHandle udev_new();
/*    */   
/*    */   public static class UdevHandle extends PointerType {
/* 38 */     public UdevHandle(Pointer address) { super(); }
/*    */     
/*    */     public UdevHandle() {}
/*    */   }
/*    */   
/*    */   public abstract void udev_unref(UdevHandle paramUdevHandle);
/*    */   
/*    */   public abstract void udev_device_unref(UdevDevice paramUdevDevice);
/*    */   
/*    */   public abstract void udev_enumerate_unref(UdevEnumerate paramUdevEnumerate);
/*    */   
/* 49 */   public static final class UdevDevice extends PointerType { public UdevDevice(Pointer address) { super(); }
/*    */     
/*    */     public UdevDevice() {}
/*    */   }
/*    */   
/*    */   public abstract UdevEnumerate udev_enumerate_new(UdevHandle paramUdevHandle);
/*    */   
/*    */   public abstract UdevDevice udev_device_get_parent_with_subsystem_devtype(UdevDevice paramUdevDevice, String paramString1, String paramString2);
/*    */   
/*    */   public abstract UdevDevice udev_device_new_from_syspath(UdevHandle paramUdevHandle, String paramString);
/*    */   
/* 60 */   public static class UdevEnumerate extends PointerType { public UdevEnumerate(Pointer address) { super(); }
/*    */     
/*    */     public UdevEnumerate() {}
/*    */   }
/*    */   
/*    */   public abstract UdevListEntry udev_list_entry_get_next(UdevListEntry paramUdevListEntry);
/*    */   
/*    */   public abstract String udev_device_get_sysattr_value(UdevDevice paramUdevDevice, String paramString);
/*    */   
/*    */   public abstract int udev_enumerate_add_match_subsystem(UdevEnumerate paramUdevEnumerate, String paramString);
/*    */   
/* 71 */   public static class UdevListEntry extends PointerType { public UdevListEntry(Pointer address) { super(); }
/*    */     
/*    */     public UdevListEntry() {}
/*    */   }
/*    */   
/*    */   public abstract int udev_enumerate_scan_devices(UdevEnumerate paramUdevEnumerate);
/*    */   
/*    */   public abstract UdevListEntry udev_enumerate_get_list_entry(UdevEnumerate paramUdevEnumerate);
/*    */   
/*    */   public abstract String udev_list_entry_get_name(UdevListEntry paramUdevListEntry);
/*    */   
/*    */   public abstract String udev_device_get_devtype(UdevDevice paramUdevDevice);
/*    */   
/*    */   public abstract String udev_device_get_devnode(UdevDevice paramUdevDevice);
/*    */   
/*    */   public abstract String udev_device_get_syspath(UdevDevice paramUdevDevice);
/*    */   
/*    */   public abstract String udev_device_get_property_value(UdevDevice paramUdevDevice, String paramString);
/*    */   
/*    */   public abstract String udev_device_get_sysname(UdevDevice paramUdevDevice);
/*    */ }


/* Location:              C:\Users\Lab\Desktop\a.jar!\oshi\jna\platform\linux\Udev.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */